package com.cau.vostom.comment.service;

import com.cau.vostom.comment.domain.Comment;
import com.cau.vostom.comment.domain.CommentLikes;
import com.cau.vostom.comment.dto.request.CreateCommentDto;
import com.cau.vostom.comment.dto.request.RequestCommentLikeDto;
import com.cau.vostom.comment.dto.response.ResponseMusicCommentDto;
import com.cau.vostom.comment.dto.response.ResponseMyCommentDto;
import com.cau.vostom.comment.repository.CommentLikesRepository;
import com.cau.vostom.comment.repository.CommentRepository;
import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.repository.UserRepository;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final CommentLikesRepository commentLikesRepository;

    //댓글 쓰기
    @Transactional
    public Long writeComment(CreateCommentDto createCommentDto) {
        User user = getUserById(createCommentDto.getUserId());
        Music music = getMusicById(createCommentDto.getCoverSongId());
        Comment comment = Comment.createComment(user, music, createCommentDto.getContent());
        return commentRepository.save(comment).getId();
    }

    //내 댓글 조회
    @Transactional(readOnly = true)
    public List<ResponseMyCommentDto> getUserComment(Long userId) {
        User user = getUserById(userId);
        List<Comment> comments = commentRepository.findAllByUserId(user.getId());
        if(comments.isEmpty()) { //댓글이 없는 경우
            return List.of();
        }
        /*List<ResponseCommentDto> commentDtos = new ArrayList<>();

        for(Comment comment : comments) {
            commentDtos.add(ResponseCommentDto.from(comment));
        }
        return commentDtos;*/
        return comments.stream().map(ResponseMyCommentDto::from).collect(Collectors.toList());

    }

    //노래의 댓글 조회
    @Transactional(readOnly = true)
    public List<ResponseMusicCommentDto> getMusicComment(Long musicId, Long userId) {
        Music music = getMusicById(musicId);
        List<Comment> comments = commentRepository.findAllByMusicId(music.getId());
        List<ResponseMusicCommentDto> musicComments = new ArrayList<>();
        for(Comment comment : comments){
            boolean isLiked = commentLikesRepository.existsByUserIdAndCommentId(userId, comment.getId());
            int likeCount = comment.getCommentLikes().size();
            musicComments.add(ResponseMusicCommentDto.of(comment.getId(), comment.getUser().getId(), comment.getContent(), comment.getUser().getNickname(), comment.getUser().getProfileImage(), comment.getCommentDate(), likeCount, isLiked));
        }
        if(comments.isEmpty()) { //댓글이 없는 경우
            return List.of();
        }
        return musicComments;
    }

    //댓글 좋아요 누르기
    @Transactional
    public void likeComment(RequestCommentLikeDto requestCommentLikeDto) {
        User user = getUserById(requestCommentLikeDto.getUserId());
        Comment comment = getCommentById(requestCommentLikeDto.getCommentId());
        CommentLikes commentlikes = CommentLikes.createCommentLikes(user, comment);
        if(commentLikesRepository.existsByUserIdAndCommentId(user.getId(), comment.getId()))
            throw new UserException(ResponseCode.LIKE_ALREADY_EXISTS);
        commentLikesRepository.save(commentlikes);
    }

    //댓글 좋아요 취소
    @Transactional
    public void unlikeComment(RequestCommentLikeDto requestCommentLikeDto) {
        User user = getUserById(requestCommentLikeDto.getUserId());
        Comment comment = getCommentById(requestCommentLikeDto.getCommentId());
        if(!(commentLikesRepository.existsByUserIdAndCommentId(user.getId(), comment.getId())))
            throw new UserException(ResponseCode.LIKE_ALREADY_DELETED);
        commentLikesRepository.deleteByUserIdAndCommentId(user.getId(), comment.getId());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    private Music getMusicById(Long musicId) {
        return musicRepository.findById(musicId).orElseThrow(() -> new UserException(ResponseCode.MUSIC_NOT_FOUND));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new UserException(ResponseCode.COMMENT_NOT_FOUND));
    }
}

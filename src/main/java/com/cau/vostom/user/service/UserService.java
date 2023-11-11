package com.cau.vostom.user.service;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.team.repository.TeamMusicRepository;
import com.cau.vostom.team.repository.TeamUserRepository;
import com.cau.vostom.user.domain.Comment;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.dto.request.*;
import com.cau.vostom.user.dto.response.ResponseCommentDto;
import com.cau.vostom.user.dto.response.ResponseMusicDto;
import com.cau.vostom.user.dto.response.ResponseUserDto;
import com.cau.vostom.user.repository.CommentRepository;
import com.cau.vostom.user.repository.LikesRepository;
import com.cau.vostom.user.repository.UserRepository;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final TeamUserRepository teamUserRepository;
    private final TeamMusicRepository teamMusicRepository;

    //회원 정보 수정
    @Transactional
    public void updateUser(UpdateUserDto updateUserDto) {
        User user = getUserById(updateUserDto.getUserId());
        user.updateUser(updateUserDto.getNickname());
        userRepository.save(user);
    }

    //회원 탈퇴
    @Transactional
    public void deleteUser(DeleteUserDto deleteUserDto) {
        User user = getUserById(deleteUserDto.getUserId());
        userRepository.delete(user);
    }

    // 회원 중복 체크
    private void validateUser(Long kakaoId) {
        if(userRepository.existsById((kakaoId))) {
            throw new UserException(ResponseCode.USER_ALREADY_EXISTS);
        }
    }

    //회원 조회
    @Transactional(readOnly = true)
    public ResponseUserDto getUser(Long userId) {
        User user = getUserById(userId);
        return ResponseUserDto.from(user);
    }

    //내 댓글 조회
    @Transactional(readOnly = true)
    public List<ResponseCommentDto> getUserComment(Long userId) {
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
        return comments.stream().map(ResponseCommentDto::from).collect(Collectors.toList());

    }

    //내가 좋아요 한 노래 조회
    @Transactional(readOnly = true)
    public List<ResponseMusicDto> getUserLikedMusic(Long userId) {
        User user = getUserById(userId);
        List<Music> likedMusics = musicRepository.findLikedMusicsByUserId(user.getId());
        if (likedMusics.isEmpty()) {// 좋아요한 노래가 없는 경우
            return List.of();
        }

        // 좋아요한 노래를 ResponseMusicDto로 변환하여 반환
        return likedMusics.stream().map(ResponseMusicDto::from).collect(Collectors.toList());
    }


    //내 노래 조회
    @Transactional(readOnly = true)
    public List<ResponseMusicDto> getUserMusic(Long userId) {
        User user = getUserById(userId);
        List<Music> musics = musicRepository.findAllByUserId(user.getId());
        if(musics.isEmpty()) { //노래가 없는 경우
            return List.of();
        }
        return musics.stream().map(ResponseMusicDto::from).collect(Collectors.toList());
    }

    //댓글 쓰기
    @Transactional
    public Long writeComment(CreateCommentDto createCommentDto) {
        User user = getUserById(createCommentDto.getUserId());
        Music music = musicRepository.findById(createCommentDto.getMusicId()).orElseThrow(() -> new UserException(ResponseCode.MUSIC_NOT_FOUND));
        Comment comment = Comment.createComment(user, music, createCommentDto.getContent());
        return commentRepository.save(comment).getId();
    }



    /*
    //학습 데이터 수정
    @Transactional
    public void retryVoiceData(RetryVoiceDataDto retryVoiceDataDto) {
        User user = getUserById(retryVoiceDataDto.getUserId());
        user.retryVoiceData(retryVoiceDataDto.getModelPath(), 2);
        userRepository.save(user);
    }
   */

    /*목소리 학습 요청
    @Transactional
    public void requestVoiceTrain(RequestVoiceTrainDto requestVoiceTrainDto){
        User user = getUserById(requestVoiceTrainDto.getUserId());
        //서버에 파일 업로드 <- 서버의 어디에 저장할 지 경로 지정,
        //modelCompleted = 1
        // runtime.exec('python3 train.py --voice_path={경로} --model_name='{kakao_id}')
        //return 2
        user.requestVoiceTrain(requestVoiceTrainDto.getModelCompleted(), requestVoiceTrainDto.getUserVoice());
    }
    */

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }
}

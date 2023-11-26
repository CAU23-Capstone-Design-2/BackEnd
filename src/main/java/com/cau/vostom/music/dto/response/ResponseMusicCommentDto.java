package com.cau.vostom.music.dto.response;

import com.cau.vostom.user.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseMusicCommentDto {
    private Long commentId;
    private Long userId;
    private String content;
    private String nickname;
    private String profileImage;
    private LocalDateTime commentDate;

    public static ResponseMusicCommentDto of(Long commentId, Long userId, String content, String nickname, String profileImage, LocalDateTime commentDate) {
        return new ResponseMusicCommentDto(commentId, userId, content, nickname, profileImage, commentDate);
    }

    public static ResponseMusicCommentDto from(Comment comment){
        return ResponseMusicCommentDto.of(comment.getId(), comment.getUser().getId(), comment.getContent(), comment.getUser().getNickname(), comment.getUser().getProfileImage(), comment.getCommentDate());
    }
}

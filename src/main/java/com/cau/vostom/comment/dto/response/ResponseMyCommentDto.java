package com.cau.vostom.comment.dto.response;

import com.cau.vostom.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseMyCommentDto {
    private Long commentId;
    private String content;
    private String nickname;
    private String profileImage;
    private LocalDateTime commentDate;

    public static ResponseMyCommentDto of(Long commentId, String content, String nickname, String profileImage, LocalDateTime commentDate) {
        return new ResponseMyCommentDto(commentId, content, nickname, profileImage, commentDate);
    }

    public static ResponseMyCommentDto from(Comment comment){
        return ResponseMyCommentDto.of(comment.getId(), comment.getContent(), comment.getUser().getNickname(), comment.getUser().getProfileImage(), comment.getCommentDate());
    }
}

package com.cau.vostom.user.dto.response;

import com.cau.vostom.user.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseCommentDto {
    private Long commentId;
    private String content;
    private String nickname;
    private String profileImage;
    private LocalDateTime commentDate;

    public static ResponseCommentDto of(Long commentId, String content, String nickname, String profileImage, LocalDateTime commentDate) {
        return new ResponseCommentDto(commentId, content, nickname, profileImage, commentDate);
    }

    public static ResponseCommentDto from(Comment comment){
        return ResponseCommentDto.of(comment.getId(), comment.getContent(), comment.getUser().getNickname(), comment.getUser().getProfileImage(), comment.getCommentDate());
    }
}

package com.cau.vostom.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCommentLikeDto {
    private Long userId;
    private Long commentId;

    public static RequestCommentLikeDto of(Long userId, Long commentId) {
        return new RequestCommentLikeDto(userId, commentId);
    }
}

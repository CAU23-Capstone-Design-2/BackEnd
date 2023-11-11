package com.cau.vostom.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {
    private Long userId;
    private Long musicId;
    private String content;

    public static CreateCommentDto of(Long userId, Long musicId, String content) {
        return new CreateCommentDto(userId, musicId, content);
    }
}

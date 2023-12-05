package com.cau.vostom.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {
    private String content;

    public static CreateCommentDto of(String content) {
        return new CreateCommentDto(content);
    }
}

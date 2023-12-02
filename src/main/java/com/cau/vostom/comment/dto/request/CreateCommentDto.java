package com.cau.vostom.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {
    private Long coverSongId;
    private String content;

    public static CreateCommentDto of(Long coverSongId, String content) {
        return new CreateCommentDto(coverSongId, content);
    }
}

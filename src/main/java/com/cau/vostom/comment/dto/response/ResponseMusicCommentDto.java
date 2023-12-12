package com.cau.vostom.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseMusicCommentDto {
    private Long id;
    private Long userId;
    private String content;
    private String nickname;
    private String userImgUri;
    private LocalDateTime date;
    private int likeCount;
    private Boolean likedByUser;

    public static ResponseMusicCommentDto of(Long id, Long userId, String content, String nickname, String userImgUri, LocalDateTime date, int likeCount, Boolean likedByUser) {
        return new ResponseMusicCommentDto(id, userId, content, nickname, userImgUri, date, likeCount, likedByUser);
    }
}

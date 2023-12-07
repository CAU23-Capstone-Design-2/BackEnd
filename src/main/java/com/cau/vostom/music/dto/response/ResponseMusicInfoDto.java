package com.cau.vostom.music.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseMusicInfoDto {
    private Long id; //musicId
    private String title;
    private String albumArtUri;
    private String contentUri;
    private int likeCount;
    private Boolean likedByUser;

    public static ResponseMusicInfoDto of(Long id, String title, String albumArtUri, String contentUri, int likeCount, Boolean likedByUser) {
        return new ResponseMusicInfoDto(id, title, albumArtUri, contentUri, likeCount, likedByUser);
    }
}

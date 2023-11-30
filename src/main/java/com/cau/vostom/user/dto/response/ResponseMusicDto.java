package com.cau.vostom.user.dto.response;

import com.cau.vostom.music.domain.Music;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//내 노래 조회
public class ResponseMusicDto {
    private Long userId;
    private String userName;
    private String userImgUri;
    private Long id; //musicId
    private String title;
    private String albumArtUri;
    private String contentUri;
    private int likeCount;
    private Boolean likedByUser;

    public static ResponseMusicDto of(Long userId, String userName, String userImgUri, Long id, String title, String albumArtUri, String contentUri, int likeCount, Boolean likedByUser) {
        return new ResponseMusicDto(userId, userName, userImgUri, id, title, albumArtUri, contentUri, likeCount, likedByUser);
    }
}

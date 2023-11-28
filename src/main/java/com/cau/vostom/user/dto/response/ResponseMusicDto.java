package com.cau.vostom.user.dto.response;

import com.cau.vostom.music.domain.Music;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//내 노래 조회
public class ResponseMusicDto {
    private Long userId;
    private Long musicId;
    private String title;
    private String musicImage;
    private String fileUrl;

    public static ResponseMusicDto of(Long userId, Long musicId, String title, String musicImage, String fileUrl) {
        return new ResponseMusicDto(userId, musicId, title, musicImage, fileUrl);
    }

    public static ResponseMusicDto from(Music music) {
        return ResponseMusicDto.of(music.getUser().getId(), music.getId(), music.getTitle(), music.getMusicImage(), music.getFileUrl());
    }
}

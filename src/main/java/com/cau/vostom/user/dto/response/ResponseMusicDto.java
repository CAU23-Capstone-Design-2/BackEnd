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
    private String musicName;
    private String singerName;
    private String musicImage;
    private String fileUrl;

    public static ResponseMusicDto of(Long userId, Long musicId, String musicName, String singerName, String musicImage, String fileUrl) {
        return new ResponseMusicDto(userId, musicId, musicName, singerName, musicImage, fileUrl);
    }

    public static ResponseMusicDto from(Music music) {
        return ResponseMusicDto.of(music.getUser().getId(), music.getId(), music.getMusicName(), music.getSingerName(), music.getMusicImage(), music.getFileUrl());
    }
}

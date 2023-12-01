package com.cau.vostom.music.dto.request;

import com.cau.vostom.music.domain.Music;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestMusicTrainDto {
    private String imgUrl;
    private String title;
    private String url;

    public static RequestMusicTrainDto of(String imgUrl, String title, String url) {
        return new RequestMusicTrainDto(imgUrl, title, url);
    }

    public static RequestMusicTrainDto from(Music music){
        return new RequestMusicTrainDto(music.getMusicImage(), music.getTitle(), music.getFileUrl());
    }
}

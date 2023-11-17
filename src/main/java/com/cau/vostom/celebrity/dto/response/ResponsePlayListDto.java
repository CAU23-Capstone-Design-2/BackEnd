package com.cau.vostom.celebrity.dto.response;

import com.cau.vostom.celebrity.domain.Celebrity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponsePlayListDto {
    private String musicName;
    private String musicImg;
    private String singerName;

    public static ResponsePlayListDto of(String musicName, String musicImg, String singerName) {
        return new ResponsePlayListDto(musicName, musicImg, singerName);
    }

    public static ResponsePlayListDto from(Celebrity celebrity) {
        return new ResponsePlayListDto(celebrity.getMusicName(), celebrity.getMusicImg(), celebrity.getSingerName());
    }
}

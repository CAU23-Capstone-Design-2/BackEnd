package com.cau.vostom.celebrity.dto.response;

import com.cau.vostom.celebrity.domain.Celebrity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseCelebrityDto {
    private String celebrityName;

    private String celebrityImg;

    public static ResponseCelebrityDto of(String celebrityName, String celebrityImg) {
        return new ResponseCelebrityDto(celebrityName, celebrityImg);
    }

    public static ResponseCelebrityDto from(Celebrity celebrity) {
        return new ResponseCelebrityDto(celebrity.getCelebrityName(), celebrity.getCelebrityImg());
    }
}

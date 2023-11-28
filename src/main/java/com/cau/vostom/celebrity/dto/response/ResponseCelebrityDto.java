package com.cau.vostom.celebrity.dto.response;

import com.cau.vostom.celebrity.domain.Celebrity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseCelebrityDto {
    private String celebrityName;

    private String imgUri;

    private Long id;

    public static ResponseCelebrityDto of(String celebrityName, String imgUri, Long id) {
        return new ResponseCelebrityDto(celebrityName, imgUri, id);
    }

    public static ResponseCelebrityDto from(Celebrity celebrity) {
        return new ResponseCelebrityDto(celebrity.getCelebrityName(), celebrity.getCelebrityImg(), celebrity.getId());
    }
}

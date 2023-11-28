package com.cau.vostom.user.dto.response;

import com.cau.vostom.user.domain.User;
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

    public static ResponseCelebrityDto from(User user) {
        return new ResponseCelebrityDto(user.getNickname(), user.getProfileImage(), user.getId());
    }
}

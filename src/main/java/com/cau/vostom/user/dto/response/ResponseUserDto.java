package com.cau.vostom.user.dto.response;

import com.cau.vostom.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

//회원 정보 조회
@Getter @AllArgsConstructor
public class ResponseUserDto {
    private String nickname;
    private String profileImage;

    public static ResponseUserDto of (String nickname, String profileImage) {
        return new ResponseUserDto(nickname, profileImage);
    }

    public static ResponseUserDto from(User user) {
        return new ResponseUserDto(user.getNickname(), user.getProfileImage());
    }
}

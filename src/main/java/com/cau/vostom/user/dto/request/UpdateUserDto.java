package com.cau.vostom.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
//회원 정보 수정
public class UpdateUserDto {
    private Long userId;
    private String nickname;

    public static UpdateUserDto of (Long userId, String nickname) {
        return new UpdateUserDto(userId, nickname);
    }
}

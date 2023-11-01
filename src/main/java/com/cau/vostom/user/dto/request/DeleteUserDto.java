package com.cau.vostom.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
//회원 탈퇴
public class DeleteUserDto {
    private Long userId;
    private Long kakaoId;

    public static DeleteUserDto of(Long userId, Long kakaoId) {
        return new DeleteUserDto(userId, kakaoId);
    }
}

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

    public static DeleteUserDto of(Long userId) {
        return new DeleteUserDto(userId);
    }
}

package com.cau.vostom.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLikeDto {
    private Long userId;
    private Long musicId;

    public static RequestLikeDto of(Long userId, Long musicId) {
        return new RequestLikeDto(userId, musicId);
    }
}

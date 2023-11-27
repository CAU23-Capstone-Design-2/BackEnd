package com.cau.vostom.music.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MusicLikeDto {
    private Long userId;
    private Long musicId;

    public static MusicLikeDto of(Long userId, Long musicId) {
        return new MusicLikeDto(userId, musicId);
    }
}

package com.cau.vostom.music.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMusicDto {
    private Long musicId;
    private Long userId;

    public static DeleteMusicDto of(Long musicId, Long userId) {
        return new DeleteMusicDto(musicId, userId);
    }
}

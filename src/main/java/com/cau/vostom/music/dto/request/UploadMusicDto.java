package com.cau.vostom.music.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
//그룹에 음악 업로드
public class UploadMusicDto {
    private Long teamId;
    private Long musicId;

    public static UploadMusicDto of(Long teamId, Long musicId) {
        return new UploadMusicDto(teamId, musicId);
    }
}

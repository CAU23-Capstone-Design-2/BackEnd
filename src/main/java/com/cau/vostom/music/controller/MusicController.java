package com.cau.vostom.music.controller;

import com.cau.vostom.music.dto.request.DeleteMusicDto;
import com.cau.vostom.music.dto.request.UploadMusicDto;
import com.cau.vostom.music.service.MusicService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Api(tags = {"Music"})
@RequestMapping("/api/music")
public class MusicController {

    private final MusicService musicService;

    //음악 삭제
    @Operation(summary = "음악 삭제")
    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteMusic(DeleteMusicDto deleteMusicDto) {
        musicService.deleteMusic(deleteMusicDto);
        return ApiResponse.success(null, ResponseCode.MUSIC_DELETED.getMessage());
    }

    //그룹에 음악 추가
    @Operation(summary = "그룹에 음악 추가")
    @PutMapping("/upload")
    public ApiResponse<Long> uploadMusicToTeam(UploadMusicDto uploadMusicDto) {
        return ApiResponse.success(musicService.uploadMusicToTeam(uploadMusicDto), ResponseCode.MUSIC_DELETED.getMessage());
    }

}

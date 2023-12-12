package com.cau.vostom.music.controller;

import com.cau.vostom.auth.component.JwtTokenProvider;
import com.cau.vostom.music.dto.request.MusicLikeDto;
import com.cau.vostom.music.dto.request.RequestMusicTrainDto;
import com.cau.vostom.music.dto.response.ResponseMusicInfoDto;
import com.cau.vostom.music.service.MusicService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"Music"})
@RequestMapping("/api/music")
public class MusicController {

    private final MusicService musicService;
    private final JwtTokenProvider jwtTokenProvider;

    //내 음악 삭제
    @Operation(summary = "내 음악 삭제")
    @DeleteMapping("/{musicId}")
    public ApiResponse<Void> deleteMusic(@RequestHeader String accessToken, @PathVariable Long musicId) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        musicService.deleteMusic(userId, musicId);
        return ApiResponse.success(null, ResponseCode.MUSIC_DELETED.getMessage());
    }

    //음악 정보 조회
    @Operation(summary = "음악 정보 조회")
    @GetMapping("/{musicId}")
    public ApiResponse<ResponseMusicInfoDto> getMusic(@RequestHeader String accessToken, @PathVariable Long musicId) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(musicService.getMusicInfo(userId, musicId), ResponseCode.MUSIC_FOUND.getMessage());
    }

    /* 
    //그룹에 음악 추가
    @Operation(summary = "그룹에 음악 추가")
    @PutMapping("/group")
    public ApiResponse<Long> uploadMusicToTeam(@RequestHeader String accessToken, @RequestBody UploadMusicDto uploadMusicDto) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(musicService.uploadMusicToGroup(userId, uploadMusicDto), ResponseCode.MUSIC_UPLOADED.getMessage());
    }
    */

    /* 
    //그룹에 업로드한 음악 삭제
    @Operation(summary = "그룹에 업로드한 음악 삭제")
    @DeleteMapping("/group")
    public ApiResponse<Void> deleteMusicToTeam(@RequestHeader String accessToken, @RequestBody UploadMusicDto uploadMusicDto) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        musicService.deleteMusicToGroup(userId, uploadMusicDto);
        return ApiResponse.success(null, ResponseCode.GROUP_MUSIC_DELETED.getMessage());
    }
    */

    //좋아요 누르기
    @Operation(summary = "노래에 좋아요 누르기")
    @PostMapping("/like")
    public ApiResponse<Void> like(@RequestHeader String accessToken, @RequestParam Long id) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        log.info("\nmusicId:" + id);
        musicService.likeMusic(userId, id);
        return ApiResponse.success(null, ResponseCode.MUSIC_LIKE_CREATED.getMessage());
    }

    //좋아요 취소
    @Operation(summary = "노래 좋아요 취소")
    @DeleteMapping("/like/undo")
    public ApiResponse<Void> deleteLike(@RequestHeader String accessToken, @RequestParam Long id) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        log.info("\nmusicId:" + id);
        musicService.unlikeMusic(userId, id);
        return ApiResponse.success(null, ResponseCode.MUSIC_LIKE_UNDO.getMessage());
    }

    //음악 파일 합성 요청
    @Operation(summary = "음악 파일 합성 요청")
    @PostMapping("/train")
    public ApiResponse<Void> trainMusic(@RequestHeader String accessToken, @RequestBody RequestMusicTrainDto requestMusicTrainDto, @RequestParam String url) throws IOException, InterruptedException {
        Long userId = Long.parseLong((jwtTokenProvider.getUserPk(accessToken)));
        musicService.trainMusic(userId, requestMusicTrainDto, url);
        return ApiResponse.success(null, ResponseCode.MUSIC_TRAIN_REQUESTED.getMessage());
    }

    // 음악 파일 스트리밍
    @Operation(summary = "음악 파일 스트리밍")
    @GetMapping(value = "/stream/{musicId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> streamMusic(@RequestHeader String accessToken, @PathVariable Long musicId) throws IOException {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return musicService.streamMusic(userId, musicId);
    }

}

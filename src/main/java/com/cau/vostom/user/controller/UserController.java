package com.cau.vostom.user.controller;

import com.cau.vostom.auth.component.JwtTokenProvider;
import com.cau.vostom.group.dto.response.ResponseGroupMusicDto;
import com.cau.vostom.user.dto.request.*;
import com.cau.vostom.user.dto.response.ResponseCelebrityDto;
import com.cau.vostom.user.dto.response.ResponseMusicDto;
import com.cau.vostom.user.dto.response.ResponseUserDto;
import com.cau.vostom.user.service.UserService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController @RequiredArgsConstructor
@Api(tags = {"User"})
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원정보 수정
    @Operation(summary = "회원정보 수정", description = "updateUSerDto를 받아서 처리하고 반환데이터는 null")
    @PutMapping
    public ApiResponse<Void> updateUser(@RequestHeader String accessToken,
                                         @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        log.info("userController.updateUser()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        userService.updateUser(userId, imageFile);
        return ApiResponse.success(null, ResponseCode.USER_UPDATED.getMessage());
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴", description = "deleteUserDto를 받아서 처리하고 반환데이터는 null")
    @DeleteMapping
    public ApiResponse<Void> deleteUser(@RequestBody DeleteUserDto deleteUserDto) {
        log.info("userController.deleteUser()");
        userService.deleteUser(deleteUserDto);
        return ApiResponse.success(null, ResponseCode.USER_DELETED.getMessage());
    }

    //사용자 목소리 재학습
    @Operation(summary = "사용자 목소리 재학습")
    @PutMapping("/retrain")
    public ApiResponse<Void> retrainUser(@RequestHeader String accessToken) {
        log.info("userController.retrainUser()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        userService.retryTraining(userId);
        return ApiResponse.success(null, ResponseCode.USER_RETRAINED.getMessage());
    }

    @Operation(summary = "유저 프로필 정보 조회")
    @GetMapping("/profile")
    public ApiResponse<ResponseUserDto> getUser(@RequestHeader String accessToken) {
        log.info("userController.getUser()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.getUserProfile(userId), ResponseCode.USER_READ.getMessage());
    }

    //내가 좋아요 한 노래 조회
    @Operation(summary = "내가 좋아요 한 노래 조회")
    @GetMapping("/likedMusic")
    public ApiResponse<List<ResponseMusicDto>> getUserLikedMusic(@RequestHeader String accessToken) {
        log.info("userController.getUserLikedMusic()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.getUserLikedMusic(userId), ResponseCode.MUSIC_LIKED_READ.getMessage());
    }

    //내 노래 조회
    @Operation(summary = "내 노래 조회")
    @GetMapping("/music")
    public ApiResponse<List<ResponseMusicDto>> getUserMusic(@RequestHeader String accessToken) {
        log.info("userController.getUserMusic()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.getUserMusic(userId, true), ResponseCode.MY_MUSIC_READ.getMessage());
    }

    //요청중인 내 노래 조회
    @Operation(summary = "요청 중인 내 노래 조회")
    @GetMapping("/music/request")
    public ApiResponse<List<ResponseMusicDto>> getUserMusicRequest(@RequestHeader String accessToken) {
        log.info("userController.getUserMusicRequest()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.getUserMusic(userId, false), ResponseCode.REQUESTED_MUSIC_READ.getMessage());
    }

    //내 그룹 노래 조회
    @Operation(summary = "내 그룹 노래 조회")
    @GetMapping("/groupMusic")
    public ApiResponse<List<ResponseGroupMusicDto>> getUserGroupMusic(@RequestHeader String accessToken) {
        log.info("userController.getUserGroupMusic()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.getUserGroupMusic(userId), ResponseCode.GROUP_MUSIC_READ.getMessage());
    }

    //연예인 리스트 조회
    @Operation(summary = "연예인 리스트 조회")
    @GetMapping("/celebrityList")
    public ApiResponse<List<ResponseCelebrityDto>> getCelebrityList(@RequestHeader String accessToken){
        log.info("userController.getCelebrityList()");
        return ApiResponse.success(userService.getCelebrityList(), ResponseCode.CELEBRITY_LISTED.getMessage());
    }

    //특정 연예인의 노래 리스트 조회
    @Operation(summary = "특정 연예인의 노래 리스트 조회")
    @GetMapping("/celebrity/musicList")
    public ApiResponse<List<ResponseMusicDto>> getCelebrityMusic(@RequestHeader String accessToken, @RequestParam Long id) {
        log.info("userController.getCelebrityMusic()");
        return ApiResponse.success(userService.getUserMusic(id, true), ResponseCode.CELEBRITY_MUSIC_LISTED.getMessage());
    }

    //사용자 목소리 데이터 업로드
    @Operation(summary = "사용자 목소리 데이터 업로드")
    @PostMapping("/voiceData")
    public ApiResponse<Void> uploadVoice(@RequestHeader String accessToken,
                                              @RequestPart("voiceFiles") MultipartFile[] voiceFiles) throws IOException {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        userService.uploadVoice(userId, Arrays.asList(voiceFiles));
        return ApiResponse.success(null, ResponseCode.USER_VOICE_DATA_UPLOADED.getMessage());
    }

    //사용자 목소리 학습 상태 확인
    @Operation(summary = "사용자 목소리 학습 상태 확인")
    @GetMapping("/checkTrained")
    public ApiResponse<Integer> checkTrained(@RequestHeader String accessToken) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.checkTrained(userId), ResponseCode.CHECK_TRAINED.getMessage());
    }

    @Operation(summary = "프로필 이미지 다운로드")
    @GetMapping(value = "/profileImage/{fileName}", produces = { "image/jpeg", "image/png" })
    public ResponseEntity<ByteArrayResource> downloadProfileImage(@PathVariable String fileName) throws IOException {
        ByteArrayResource resource = userService.downloadProfileImage(fileName);

        HttpHeaders headers = new HttpHeaders();
        if (fileName.contains(".jpg")) {
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (fileName.contains(".png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        }

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }
}

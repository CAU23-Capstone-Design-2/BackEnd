package com.cau.vostom.user.controller;

import com.cau.vostom.auth.component.JwtTokenProvider;
import com.cau.vostom.team.dto.response.ResponseTeamMusicDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor
@Api(tags = {"User"})
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원정보 수정
    @Operation(summary = "회원정보 수정", description = "updateUSerDto를 받아서 처리하고 반환데이터는 null")
    @PutMapping("/update")
    public ApiResponse<Void> updateUser(@RequestHeader String accessToken, @RequestBody UpdateUserDto updateUserDto) {
        userService.updateUser(updateUserDto);
        return ApiResponse.success(null, ResponseCode.USER_UPDATED.getMessage());
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴", description = "deleteUserDto를 받아서 처리하고 반환데이터는 null")
    @DeleteMapping
    public ApiResponse<Void> deleteUser(@RequestBody DeleteUserDto deleteUserDto) {
        userService.deleteUser(deleteUserDto);
        return ApiResponse.success(null, ResponseCode.USER_DELETED.getMessage());
    }

    /*
    @Operation(summary = "학습 데이터 수정")
    @PutMapping("/retry")
    public ApiResponse<Void> retryVoiceData(RetryVoiceDataDto retryVoiceDataDto) {
        userService.retryVoiceData(retryVoiceDataDto);
        return ApiResponse.success(null, ResponseCode.USER_VOICE_DATA_UPDATED.getMessage());
    }

     */

    @Operation(summary = "유저 프로필 정보 조회")
    @GetMapping("/profile")
    public ApiResponse<ResponseUserDto> getUser(@RequestHeader String accessToken) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.getUserProfile(userId), ResponseCode.USER_READ.getMessage());
    }

    //내가 좋아요 한 노래 조회
    @Operation(summary = "내가 좋아요 한 노래 조회")
    @GetMapping("/likedMusic")
    public ApiResponse<List<ResponseMusicDto>> getUserLikedMusic(@RequestHeader String accessToken) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.getUserLikedMusic(userId), ResponseCode.MUSIC_LIKED_READ.getMessage());
    }

    //내 노래 조회
    @Operation(summary = "내 노래 조회")
    @GetMapping("/music")
    public ApiResponse<List<ResponseMusicDto>> getUserMusic(@RequestHeader String accessToken) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.getUserMusic(userId), ResponseCode.MY_MUSIC_READ.getMessage());
    }

    //내 그룹 노래 조회
    @Operation(summary = "내 그룹 노래 조회")
    @GetMapping("/teamMusic")
    public ApiResponse<List<ResponseTeamMusicDto>> getUserTeamMusic(@RequestHeader String accessToken) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(userService.getUserTeamMusic(userId), ResponseCode.TEAM_MUSIC_READ.getMessage());
    }

    //연예인 리스트 조회
    @Operation(summary = "연예인 리스트 조회")
    @GetMapping("/celebrityList")
    public ApiResponse<List<ResponseCelebrityDto>> getCelebrityList(@RequestHeader String accessToken){
        return ApiResponse.success(userService.getCelebrityList(), ResponseCode.CELEBRITY_LISTED.getMessage());
    }

    //특정 연예인의 노래 리스트 조회
    @Operation(summary = "특정 연예인의 노래 리스트 조회")
    @GetMapping("/celebrity/musicList")
    public ApiResponse<List<ResponseMusicDto>> getCelebrityMusic(@RequestHeader String accessToken, @RequestParam Long id) {
        return ApiResponse.success(userService.getUserMusic(id), ResponseCode.MY_MUSIC_READ.getMessage());
    }

}

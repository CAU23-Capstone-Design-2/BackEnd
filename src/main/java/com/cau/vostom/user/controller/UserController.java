package com.cau.vostom.user.controller;

import com.cau.vostom.team.dto.response.ResponseTeamMusicDto;
import com.cau.vostom.user.dto.request.*;
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

    // 회원정보 수정
    @Operation(summary = "회원정보 수정", description = "updateUSerDto를 받아서 처리하고 반환데이터는 null")
    @PutMapping("/update")
    public ApiResponse<Void> updateUser(@RequestBody UpdateUserDto updateUserDto) {
        userService.updateUser(updateUserDto);
        return ApiResponse.success(null, ResponseCode.USER_UPDATED.getMessage());
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴", description = "deleteUserDto를 받아서 처리하고 반환데이터는 null")
    @DeleteMapping("/delete")
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

    @Operation(summary = "회원 정보 조회")
    @GetMapping("/info/user/{userId}")
    public ApiResponse<ResponseUserDto> getUser(@PathVariable Long userId) {
        return ApiResponse.success(userService.getUser(userId), ResponseCode.USER_READ.getMessage());
    }


    //내가 좋아요 한 노래 조회
    @Operation(summary = "내가 좋아요 한 노래 조회")
    @GetMapping("/info/likedMusic/{userId}")
    public ApiResponse<List<ResponseMusicDto>> getUserLikedMusic(@PathVariable Long userId) {
        return ApiResponse.success(userService.getUserLikedMusic(userId), ResponseCode.MUSIC_LIKED_READ.getMessage());
    }

    //내 노래 조회
    @Operation(summary = "내 노래 조회")
    @GetMapping("/info/music/{userId}")
    public ApiResponse<List<ResponseMusicDto>> getUserMusic(@PathVariable Long userId) {
        return ApiResponse.success(userService.getUserMusic(userId), ResponseCode.MUSIC_READ.getMessage());
    }

    //내 그룹 노래 조회
    @Operation(summary = "내 그룹 노래 조회")
    @GetMapping("/info/teamMusic/{userId}")
    public ApiResponse<List<ResponseTeamMusicDto>> getUserTeamMusic(@PathVariable Long userId) {
        return ApiResponse.success(userService.getUserTeamMusic(userId), ResponseCode.MUSIC_READ.getMessage());
    }

    //좋아요 누르기
    @Operation(summary = "좋아요 누르기")
    @PostMapping("/like")
    public ApiResponse<Void> like(@RequestBody RequestLikeDto requestLikeDto) {
        userService.likeMusic(requestLikeDto);
        return ApiResponse.success(null, ResponseCode.LIKE_CREATED.getMessage());
    }

    //좋아요 취소
    @Operation(summary = "좋아요 취소")
    @DeleteMapping("/like/delete")
    public ApiResponse<Void> deleteLike(@RequestBody RequestLikeDto requestLikeDto) {
        userService.unlikeMusic(requestLikeDto);
        return ApiResponse.success(null, ResponseCode.LIKE_DELETED.getMessage());
    }
}

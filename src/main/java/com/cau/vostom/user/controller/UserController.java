package com.cau.vostom.user.controller;

import com.cau.vostom.user.dto.request.DeleteUserDto;
import com.cau.vostom.user.dto.request.RetryVoiceDataDto;
import com.cau.vostom.user.dto.request.UpdateUserDto;
import com.cau.vostom.user.service.UserService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequiredArgsConstructor
@Api(tags = {"User"})
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 회원정보 수정
    @Operation(summary = "회원정보 수정")
    @PutMapping("/update")
    public ApiResponse<Void> updateUser(UpdateUserDto updateUserDto) {
        userService.updateUser(updateUserDto);
        return ApiResponse.success(null, ResponseCode.USER_UPDATED.getMessage());
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteUser(DeleteUserDto deleteUserDto) {
        userService.deleteUser(deleteUserDto);
        return ApiResponse.success(null, ResponseCode.USER_DELETED.getMessage());
    }

    @Operation(summary = "학습 데이터 수정")
    @PutMapping("/retry")
    public ApiResponse<Void> retryVoiceData(RetryVoiceDataDto retryVoiceDataDto) {
        userService.retryVoiceData(retryVoiceDataDto);
        return ApiResponse.success(null, ResponseCode.USER_VOICE_DATA_UPDATED.getMessage());
    }
}

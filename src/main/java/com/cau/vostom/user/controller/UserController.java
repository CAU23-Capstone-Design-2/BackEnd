package com.cau.vostom.user.controller;

import com.cau.vostom.user.dto.request.UpdateUserDto;
import com.cau.vostom.user.service.UserService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
}

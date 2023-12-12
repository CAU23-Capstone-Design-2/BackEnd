package com.cau.vostom.auth.controller;

import com.cau.vostom.auth.component.JwtTokenProvider;
import com.cau.vostom.auth.dto.ResponseJwtDto;
import com.cau.vostom.auth.service.KakaoAuthService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    // 카카오 로그인을 위해 회원가입 여부 확인, 이미 회원이면 Jwt 토큰 발급
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 위해 회원가입 여부 확인, 이미 회원이면 Jwt 토큰 발급")
    @PostMapping("/login")
    public ApiResponse<ResponseJwtDto> authCheck(@RequestHeader String accessToken) {
        Long userId = kakaoAuthService.userLogin(accessToken); // 유저 고유번호 추출
        String userJwt = jwtTokenProvider.createToken(userId.toString());
        return ApiResponse.success(ResponseJwtDto.of(userId, userJwt), ResponseCode.USER_LOGINED.getMessage());
    }
}
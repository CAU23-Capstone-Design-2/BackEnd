package com.cau.vostom.dev.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cau.vostom.auth.component.JwtTokenProvider;
import com.cau.vostom.auth.dto.ResponseJwtDto;
import com.cau.vostom.dev.service.CrawllerService;
import com.cau.vostom.dev.service.DevKakaoAuthService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Api(tags = {"Dev"})
@RequestMapping("/api/dev")
public class DevController {

    private final DevKakaoAuthService devKakaoAuthService;
    private final CrawllerService crawllerService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "개발용 로그인", description = "개발용 사용자 회원가입, 토큰 발급")
    @PostMapping("/login")
    public ApiResponse<ResponseJwtDto> authCheck(@RequestParam long kakaoId ) {
        Long userId = devKakaoAuthService.devUserLogin(kakaoId); // 유저 고유번호 추출
        String userJwt = jwtTokenProvider.createToken(Long.toString(userId));
        return ApiResponse.success(ResponseJwtDto.of(userId, userJwt), ResponseCode.USER_LOGINED.getMessage());
    }

    @Operation(summary = "유튜브 크롤링", description = "유튜브 크롤링 테스트")
    @PostMapping("/crawl")
    public ApiResponse<String> crawl() {
        return ApiResponse.success("크롤링 완료", ResponseCode.CRAWL_SUCCESS.getMessage());
    }
    
}

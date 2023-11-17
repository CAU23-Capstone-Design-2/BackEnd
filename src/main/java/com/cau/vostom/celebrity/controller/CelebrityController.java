package com.cau.vostom.celebrity.controller;

import com.cau.vostom.celebrity.dto.response.ResponseCelebrityDto;
import com.cau.vostom.celebrity.dto.response.ResponsePlayListDto;
import com.cau.vostom.celebrity.service.CelebrityService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Celebrity"})
@RequestMapping("/api/celebrity")
public class CelebrityController {
    private final CelebrityService celebrityService;

    //연예인 리스트
    @Operation(summary = "연예인 리스트")
    @GetMapping("/playlist")
    public ApiResponse<List<ResponseCelebrityDto>> getAllCelebrity() {
        return ApiResponse.success(celebrityService.getCelebrityList(), ResponseCode.CELEBRITY_LISTED.getMessage());
    }

    //연예인 노래 리스트
    @Operation(summary = "연예인 노래 리스트")
    @GetMapping("/playlist/{celebrityName}")
    public ApiResponse<List<ResponsePlayListDto>> getPlayList(@PathVariable String celebrityName) {
        return ApiResponse.success(celebrityService.getPlayList(celebrityName), ResponseCode.CELEBRITY_LISTED.getMessage());
    }

}

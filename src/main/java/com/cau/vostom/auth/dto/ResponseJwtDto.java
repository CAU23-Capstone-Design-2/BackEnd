package com.cau.vostom.auth.dto;

import lombok.Getter;

@Getter
public class ResponseJwtDto {
    private Long id;
    private String accessToken;

    public static ResponseJwtDto of(Long id, String jwt) {
        ResponseJwtDto responseJwtDto = new ResponseJwtDto();
        responseJwtDto.id = id;
        responseJwtDto.accessToken = jwt;
        return responseJwtDto;
    }
}

package com.cau.vostom.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class RetryVoiceDataDto {
    private Long userId;
    private String modelPath;

    public static RetryVoiceDataDto of (Long userId, String modelPath) {
        return new RetryVoiceDataDto(userId, modelPath);
    }

}

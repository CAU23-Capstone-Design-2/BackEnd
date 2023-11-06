package com.cau.vostom.auth.dto;

import lombok.Getter;

@Getter
public class KakaoProfile {

    private String nickname;
    private String profileImageUrl;
    private String thumbnailImageUrl;
    private boolean isDefaultImage;
}

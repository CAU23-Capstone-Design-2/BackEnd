package com.cau.vostom.group.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseGroupMusicDto {
    private String title; //
    private String albumArtUri; //커버곡 사진
    private Long userId; //커버곡 유저 아이디
    private String userName; //커버곡 유저 닉네임
    private String userImgUri; //커버곡 이미지
    private String contentUri; //커버곡 파일 경로
    private int likeCount;
    private boolean likedByUser;

    public static ResponseGroupMusicDto of(String title, String albumArtUri, Long userId, String userName, String userImage, String contentUri, int likeCount, boolean isLike) {
        return new ResponseGroupMusicDto(title, albumArtUri, userId, userName, userImage, contentUri, likeCount, isLike);
    }
}

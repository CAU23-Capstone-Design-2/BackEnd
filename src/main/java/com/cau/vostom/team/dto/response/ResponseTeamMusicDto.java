package com.cau.vostom.team.dto.response;


import com.cau.vostom.team.domain.TeamMusic;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseTeamMusicDto {
    private Long teamMusicId; //커버곡 아이디
    private String title; //커버곡 이름
    private String albumArtUri; //커버곡 사진
    private Long userId; //커버곡 유저 아이디
    private String userName; //커버곡 유저 닉네임
    private String userImage; //커버곡 이미지
    private String contentUri; //커버곡 파일 경로
    private int likeCount;
    private boolean isLike;

    public static ResponseTeamMusicDto of(Long teamMusicId, String title, String albumArtUri, Long userId, String userName, String userImage, String contentUri, int likeCount, boolean isLike) {
        return new ResponseTeamMusicDto(teamMusicId, title, albumArtUri, userId, userName, userImage, contentUri, likeCount, isLike);
    }
}

package com.cau.vostom.team.dto.response;


import com.cau.vostom.team.domain.TeamMusic;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseTeamMusicDto {
    private Long teamMusicId;
    private String title;
    private String musicImage;
    private String userName;
    private String userImage;

    public static ResponseTeamMusicDto of(Long teamMusicId, String title, String musicImage, String userName, String userImage) {
        return new ResponseTeamMusicDto(teamMusicId, title, musicImage, userName, userImage);
    }

    public static ResponseTeamMusicDto from(TeamMusic teamMusic){
        return new ResponseTeamMusicDto(teamMusic.getId(), teamMusic.getMusic().getTitle(), teamMusic.getMusic().getMusicImage(), teamMusic.getMusic().getUser().getNickname(), teamMusic.getMusic().getUser().getProfileImage());
    }
}

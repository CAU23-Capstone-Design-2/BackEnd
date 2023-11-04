package com.cau.vostom.team.dto.response;


import com.cau.vostom.team.domain.TeamMusic;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseTeamMusicDto {
    private Long teamMusicId;
    private String musicTitle;
    private String musicArtist;
    private String musicImage;
    private String userName;
    private String userImage;

    public static ResponseTeamMusicDto of(Long teamMusicId, String musicTitle, String musicArtist, String musicImage, String userName, String userImage) {
        return new ResponseTeamMusicDto(teamMusicId, musicTitle, musicArtist, musicImage, userName, userImage);
    }

    public static ResponseTeamMusicDto from(TeamMusic teamMusic){
        return new ResponseTeamMusicDto(teamMusic.getId(), teamMusic.getMusic().getMusicName(), teamMusic.getMusic().getSingerName(), teamMusic.getMusic().getMusicImage(), teamMusic.getMusic().getUser().getNickname(), teamMusic.getMusic().getUser().getProfileImage());
    }
}

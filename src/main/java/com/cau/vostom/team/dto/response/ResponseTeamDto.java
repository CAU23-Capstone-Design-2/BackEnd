package com.cau.vostom.team.dto.response;

import com.cau.vostom.team.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseTeamDto {
    private Long teamId;
    private String teamName;
    private String teamImage;
    private int teamUserCount;

    public static ResponseTeamDto of(Long teamId, String teamName, String teamImage, int teamUserCount) {
        return new ResponseTeamDto(teamId, teamName, teamImage, teamUserCount);
    }

    public static ResponseTeamDto from(Team team) {
        return new ResponseTeamDto(team.getId(), team.getGroupName(), team.getGroupImage(), team.getTeamUsers().size());
    }

}

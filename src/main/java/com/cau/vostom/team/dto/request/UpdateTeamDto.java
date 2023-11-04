package com.cau.vostom.team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamDto {
    private Long teamId;
    private String teamName;
    private String teamImage;
    private String teamDescription;

    public static UpdateTeamDto of(Long teamId, String teamName, String teamImage, String teamDescription) {
        return new UpdateTeamDto(teamId, teamName, teamImage, teamDescription);
    }
}

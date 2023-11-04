package com.cau.vostom.team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamDto {
    private String teamName;
    private String teamImage;
    private String teamDescription;

    public static CreateTeamDto of(String teamName, String teamImage, String teamDescription) {
        return new CreateTeamDto(teamName, teamImage, teamDescription);
    }
}

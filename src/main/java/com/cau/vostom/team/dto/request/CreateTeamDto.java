package com.cau.vostom.team.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamDto {
    private String teamName;
    private String teamDescription;
    private MultipartFile teamImage;

    public static CreateTeamDto of(String teamName, String teamDescription, MultipartFile teamImage) {
        return new CreateTeamDto(teamName, teamDescription, teamImage);
    }
}

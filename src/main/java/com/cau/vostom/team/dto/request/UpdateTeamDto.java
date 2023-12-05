package com.cau.vostom.team.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamDto {
    private Long teamId;
    private String teamName;
    private String teamDescription;
    private MultipartFile teamImage;

    public static UpdateTeamDto of(Long teamId, String teamName, String teamDescription, MultipartFile teamImage) {
        return new UpdateTeamDto(teamId, teamName, teamDescription, teamImage);
    }
}

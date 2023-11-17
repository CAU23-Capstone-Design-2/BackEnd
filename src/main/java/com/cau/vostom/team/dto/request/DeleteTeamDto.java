package com.cau.vostom.team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTeamDto {
    private Long teamId;
    private Long userId;

    public static DeleteTeamDto of(Long teamId, Long userId) {
        return new DeleteTeamDto(teamId, userId);
    }
}

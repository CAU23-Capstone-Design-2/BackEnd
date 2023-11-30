package com.cau.vostom.team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveTeamDto {
    private Long teamId;

    public static LeaveTeamDto of(Long teamId) {
        return new LeaveTeamDto(teamId);
    }
}

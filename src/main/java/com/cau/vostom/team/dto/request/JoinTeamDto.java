package com.cau.vostom.team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinTeamDto {
    private Long teamId;
    private Long userId;

    public static JoinTeamDto of(Long teamId, Long userId) {
        return new JoinTeamDto(teamId, userId);
    }
}

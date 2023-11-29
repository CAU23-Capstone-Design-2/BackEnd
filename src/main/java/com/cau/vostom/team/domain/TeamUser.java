package com.cau.vostom.team.domain;

import com.cau.vostom.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isLeader = false; //그룹장 여부

    @CreatedDate
    private LocalDateTime joinDate;

    public static TeamUser createGroupUser(Team team, User user, boolean isLeader) {
        TeamUser teamUser = new TeamUser();
        teamUser.team = team;
        teamUser.user = user;
        teamUser.isLeader = isLeader;
        return teamUser;
    }
}

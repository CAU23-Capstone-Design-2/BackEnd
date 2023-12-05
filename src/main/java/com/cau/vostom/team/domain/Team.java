package com.cau.vostom.team.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Entity 
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "group_tb")
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String groupName;

    private String groupImagePath;

    private String groupInfo;

    @CreatedDate
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.REMOVE})
    private List<TeamMusic> teamMusics = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = {CascadeType.REMOVE})
    private List<TeamUser> teamUsers = new ArrayList<>();

    public static Team createGroup(String groupName, String groupInfo) {
        Team team = new Team();
        team.groupName = groupName;
        team.groupInfo = groupInfo;
        return team;
    }

    public void updateGroup(String groupName, String groupImagePath, String groupInfo) {
        this.groupName = groupName;
        this.groupImagePath = groupImagePath;
        this.groupInfo = groupInfo;
    }
}

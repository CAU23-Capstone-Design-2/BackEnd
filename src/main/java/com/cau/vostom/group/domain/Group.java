package com.cau.vostom.group.domain;

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
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String groupName;

    private String groupImagePath;

    private String groupInfo;

    @CreatedDate
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.REMOVE})
    private List<GroupMusic> groupMusics = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = {CascadeType.REMOVE})
    private List<GroupUser> groupUsers = new ArrayList<>();

    public static Group createGroup(String groupName, String groupInfo) {
        Group group = new Group();
        group.groupName = groupName;
        group.groupInfo = groupInfo;
        return group;
    }

    public void updateGroup(String groupName, String groupImagePath, String groupInfo) {
        this.groupName = groupName;
        this.groupImagePath = groupImagePath;
        this.groupInfo = groupInfo;
    }
}

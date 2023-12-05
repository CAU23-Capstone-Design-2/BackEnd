package com.cau.vostom.group.domain;

import com.cau.vostom.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isLeader = false; //그룹장 여부

    @CreatedDate
    private LocalDateTime joinDate;

    public static GroupUser createGroupUser(Group group, User user, boolean isLeader) {
        GroupUser groupUser = new GroupUser();
        groupUser.group = group;
        groupUser.user = user;
        groupUser.isLeader = isLeader;
        return groupUser;
    }
}

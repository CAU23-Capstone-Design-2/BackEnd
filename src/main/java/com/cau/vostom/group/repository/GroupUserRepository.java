package com.cau.vostom.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cau.vostom.group.domain.GroupUser;

import java.util.List;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);
    boolean existsByUserId(Long userId);
    void deleteByUserIdAndGroupId(Long userId, Long groupId);
    List<GroupUser> findAllByUserId(Long userId);
    List<GroupUser> findByUserId(Long userId);
    GroupUser findByUserIdAndGroupId(Long userId, Long groupId);
    GroupUser findByGroupIdAndIsLeader(Long groupId, boolean isLeader);
    int countGroupUserByGroupId(Long groupId);
    Boolean existsByUserIdAndGroupIdAndIsLeader(Long userId, Long groupId, boolean isLeader);
}

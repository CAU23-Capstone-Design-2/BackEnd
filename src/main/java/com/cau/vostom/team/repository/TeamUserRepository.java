package com.cau.vostom.team.repository;

import com.cau.vostom.team.domain.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
    boolean existsByUserIdAndTeamId(Long userId, Long teamId);
    void deleteByUserIdAndTeamId(Long userId, Long teamId);

    List<TeamUser> findByTeamId(Long teamId);
    List<TeamUser> findByUserId(Long userId);
}

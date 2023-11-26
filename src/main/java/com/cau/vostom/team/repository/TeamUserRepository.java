package com.cau.vostom.team.repository;

import com.cau.vostom.team.domain.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
    boolean existsByUserIdAndTeamId(Long userId, Long teamId);

    boolean existsByUserId(Long userId);
    void deleteByUserIdAndTeamId(Long userId, Long teamId);

    List<TeamUser> findAllByUserId(Long userId);
    List<TeamUser> findByUserId(Long userId);


    int countTeamUserByTeamId(Long teamId);
}

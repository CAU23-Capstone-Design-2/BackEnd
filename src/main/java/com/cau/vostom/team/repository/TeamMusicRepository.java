package com.cau.vostom.team.repository;

import com.cau.vostom.team.domain.TeamMusic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMusicRepository extends JpaRepository<TeamMusic, Long>{

    boolean existsByMusicIdAndTeamId(Long musicId, Long teamId);
    List<TeamMusic> findByTeamId(Long teamId);

    void deleteByMusicIdAndTeamId(Long musicId, Long teamId);
}

package com.cau.vostom.user.repository;

import com.cau.vostom.user.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByUserIdAndTeamMusicId(Long userId, Long teamMusicId);
    void deleteByUserIdAndTeamMusicId(Long userId, Long teamMusicId);
}

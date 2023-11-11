package com.cau.vostom.user.repository;

import com.cau.vostom.user.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByUserIdAndMusicId(Long userId, Long musicId);
    void deleteByUserIdAndMusicId(Long userId, Long musicId);
}

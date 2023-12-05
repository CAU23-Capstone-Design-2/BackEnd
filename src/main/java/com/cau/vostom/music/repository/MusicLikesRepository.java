package com.cau.vostom.music.repository;

import com.cau.vostom.music.domain.MusicLikes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicLikesRepository extends JpaRepository<MusicLikes, Long> {
    boolean existsByUserIdAndMusicId(Long userId, Long musicId);
    void deleteByUserIdAndMusicId(Long userId, Long musicId);
    List<MusicLikes> findAllByUserId(Long userId);
}

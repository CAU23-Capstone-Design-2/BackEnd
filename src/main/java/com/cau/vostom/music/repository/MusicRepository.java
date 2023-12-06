package com.cau.vostom.music.repository;

import com.cau.vostom.music.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Long>{
    List<Music> findAllByUserId(Long userId);

    List<Music> findAllByUserIdAndIsTrained(Long userId, boolean isTrained);
    List<Music> findLikedMusicsByUserId(Long userId);
    boolean existsByFileUrl(String fileUrl);
    boolean existsByUserIdAndId(Long userId, Long musicId);


    Optional<Music> findById(Long musicId);


}

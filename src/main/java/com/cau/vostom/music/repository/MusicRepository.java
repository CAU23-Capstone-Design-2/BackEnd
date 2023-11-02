package com.cau.vostom.music.repository;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.user.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long>{
    List<Music> findAllByUserId(Long userId);
}

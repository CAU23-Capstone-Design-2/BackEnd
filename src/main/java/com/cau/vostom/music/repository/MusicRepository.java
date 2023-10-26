package com.cau.vostom.music.repository;

import com.cau.vostom.music.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long>{
}

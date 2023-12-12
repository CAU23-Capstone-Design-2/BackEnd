package com.cau.vostom.music.repository;

import com.cau.vostom.music.domain.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CacheRepository extends JpaRepository<Cache, Long> {

    boolean existsByYoutubeCode(String youtubeCode);
}

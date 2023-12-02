package com.cau.vostom.music.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cache_id")
    private Long id;

    private String youtubeCode;

    public static Cache createCache(String youtubeCode) {
        Cache cache = new Cache();
        cache.youtubeCode = youtubeCode;
        return cache;
    }
}

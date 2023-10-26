package com.cau.vostom.music.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Music {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_id")
    private Long id;

    private String musicName;

    private String singerName;

}

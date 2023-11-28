package com.cau.vostom.celebrity.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Celebrity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "celebrity_music_id")
    private Long id;

    private String celebrityName;

    private Long celebrityId;

    private String celebrityImg;

    private String title;

    private String musicImg;

    private String musicPath;
}

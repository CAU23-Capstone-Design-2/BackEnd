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
    @Column(name = "celebrity_id")
    private Long id;

    private String celebrityName;

    private String singerName;

    private String celebrityImg;

    private String musicName;

    private String musicImg;

    private String musicPath;
}

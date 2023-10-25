package com.cau.vostom.user.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    private String profileImage;
}

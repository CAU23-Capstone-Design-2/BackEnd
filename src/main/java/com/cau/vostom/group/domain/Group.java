package com.cau.vostom.group.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter @Entity
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String groupName;
}

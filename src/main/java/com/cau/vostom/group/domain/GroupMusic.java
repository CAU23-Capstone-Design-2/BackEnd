package com.cau.vostom.group.domain;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.user.domain.Likes;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Entity
public class GroupMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_music_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id")
    private Music music;

    @OneToMany(mappedBy = "group_music_id", cascade = {CascadeType.REMOVE})
    private List<Likes> likes = new ArrayList<>();
}

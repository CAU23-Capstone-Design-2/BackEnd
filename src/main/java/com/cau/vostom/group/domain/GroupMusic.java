package com.cau.vostom.group.domain;

import com.cau.vostom.music.domain.Music;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static GroupMusic createGroupMusic(Group group, Music music) {
        GroupMusic groupMusic = new GroupMusic();
        groupMusic.group = group;
        groupMusic.music = music;
        return groupMusic;
    }
}

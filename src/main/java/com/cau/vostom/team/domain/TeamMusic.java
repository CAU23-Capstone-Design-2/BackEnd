package com.cau.vostom.team.domain;

import com.cau.vostom.music.domain.Music;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_music_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id")
    private Music music;

    public static TeamMusic createGroupMusic(Team team, Music music) {
        TeamMusic teamMusic = new TeamMusic();
        teamMusic.team = team;
        teamMusic.music = music;
        return teamMusic;
    }
}

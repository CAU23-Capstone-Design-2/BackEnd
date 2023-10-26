package com.cau.vostom.team.domain;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.user.domain.Comment;
import com.cau.vostom.user.domain.Likes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "teamMusic", cascade = {CascadeType.REMOVE})
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "teamMusic", cascade = {CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();

    public static TeamMusic createGroupMusic(Team team, Music music) {
        TeamMusic teamMusic = new TeamMusic();
        teamMusic.team = team;
        teamMusic.music = music;
        return teamMusic;
    }
}

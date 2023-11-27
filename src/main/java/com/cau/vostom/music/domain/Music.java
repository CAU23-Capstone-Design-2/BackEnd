package com.cau.vostom.music.domain;

import com.cau.vostom.comment.domain.Comment;
import com.cau.vostom.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Music {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_id")
    private Long id;

    private String musicName;

    private String singerName;

    private String musicImage;

    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "music", cascade = {CascadeType.REMOVE})
    private List<MusicLikes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "music", cascade = {CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();

    public static Music createMusic(String musicName, String singerName, String musicImage, String fileUrl) {
        Music music = new Music();
        music.musicName = musicName;
        music.singerName = singerName;
        music.musicImage = musicImage;
        music.fileUrl = fileUrl;
        return music;
    }

}

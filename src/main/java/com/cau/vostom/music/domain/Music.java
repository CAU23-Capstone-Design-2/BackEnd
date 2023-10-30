package com.cau.vostom.music.domain;

import com.cau.vostom.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public static Music createMusic(String musicName, String singerName, String musicImage, String fileUrl) {
        Music music = new Music();
        music.musicName = musicName;
        music.singerName = singerName;
        music.musicImage = musicImage;
        music.fileUrl = fileUrl;
        return music;
    }

}

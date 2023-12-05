package com.cau.vostom.music.domain;

import com.cau.vostom.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MusicLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id")
    private Music music;

    private LocalDateTime likesDate;

    public static MusicLikes createMusicLikes(User user, Music music) {
        MusicLikes musicLikes = new MusicLikes();
        musicLikes.user = user;
        musicLikes.music = music;
        musicLikes.likesDate = LocalDateTime.now();
        return musicLikes;
    }
}

package com.cau.vostom.user.domain;

import com.cau.vostom.team.domain.TeamMusic;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_music_id")
    private TeamMusic teamMusic;

    @CreatedDate
    private LocalDateTime likesDate;

    public static Likes createLikes(User user, TeamMusic groupmusic) {
        Likes likes = new Likes();
        likes.user = user;
        likes.teamMusic = groupmusic;
        return likes;
    }
}

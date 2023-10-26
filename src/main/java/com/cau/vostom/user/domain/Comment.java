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
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_music_id")
    private TeamMusic teamMusic;

    private String content;

    @CreatedDate
    private LocalDateTime commentDate;

    public static Comment createComment(User user, TeamMusic teammusic, String content) {
        Comment comment = new Comment();
        comment.user = user;
        comment.teamMusic = teammusic;
        comment.content = content;
        return comment;
    }

}

package com.cau.vostom.comment.domain;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id")
    private Music music;

    private String content;

    @CreatedDate
    private LocalDateTime commentDate;

    public static Comment createComment(User user, Music music, String content) {
        Comment comment = new Comment();
        comment.user = user;
        comment.music = music;
        comment.content = content;
        return comment;
    }
}

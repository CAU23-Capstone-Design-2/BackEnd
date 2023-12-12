package com.cau.vostom.comment.domain;

import com.cau.vostom.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

public static CommentLikes createCommentLikes(User user, Comment comment) {
        CommentLikes commentLikes = new CommentLikes();
        commentLikes.user = user;
        commentLikes.comment = comment;
        return commentLikes;
    }
}

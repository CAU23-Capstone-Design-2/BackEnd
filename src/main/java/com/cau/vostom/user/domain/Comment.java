package com.cau.vostom.user.domain;

import com.cau.vostom.group.domain.GroupMusic;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Entity
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_music_id")
    private GroupMusic music;

    private String content;

    @CreatedDate
    private LocalDateTime commentDate;

}

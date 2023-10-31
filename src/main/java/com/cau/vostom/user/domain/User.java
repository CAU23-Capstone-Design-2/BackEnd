package com.cau.vostom.user.domain;

import com.cau.vostom.team.domain.TeamUser;
import com.cau.vostom.music.domain.Music;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    private String profileImage;

    private String modelPath;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user" , cascade = {CascadeType.REMOVE})
    private List<Music> musics = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<TeamUser> teamUsers = new ArrayList<>();

    public static User createUser(String nickname , String profileImage, String modelPath) {
        User user = new User();
        user.nickname = nickname;
        user.profileImage = profileImage;
        user.modelPath = modelPath;
        return user;
    }

    public void updateUser(String nickname) {
        this.nickname = nickname;
    }

    public void deleteUser() {
        this.nickname = null;
        this.profileImage = null;
        this.modelPath = null;
    }
}

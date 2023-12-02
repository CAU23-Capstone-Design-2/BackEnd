package com.cau.vostom.user.domain;

import com.cau.vostom.comment.domain.Comment;
import com.cau.vostom.comment.domain.CommentLikes;
import com.cau.vostom.music.domain.MusicLikes;
import com.cau.vostom.team.domain.TeamUser;
import com.cau.vostom.music.domain.Music;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private Long kakaoId; //로그인 식별키

    private String nickname;

    private String profileImage;

    private String modelPath;

    private boolean isCelebrity = false;

    private int modelCompleted;
    // 0 : default, 1 : ing, 2 : finish

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<MusicLikes> musiclikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<CommentLikes> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user" , cascade = {CascadeType.REMOVE})
    private List<Music> musics = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<TeamUser> teamUsers = new ArrayList<>();


    public static User createUser(String nickname, String profileImage, Long kakaoId, int modelCompleted) {
        User user = new User();
        user.nickname = nickname;
        user.profileImage = profileImage;
        user.kakaoId = kakaoId;
        user.modelCompleted = 0;
        return user;
    }

    public void setmodelCompleted(){
        this.modelCompleted = 1;
    }
    public void updateUser(String nickname) {
        this.nickname = nickname;
    }


    //Jwt 설정을 위한 UserDetails 메소드
    @ElementCollection(fetch = FetchType.EAGER) //roles 컬렉션
    private List<String> roles = new ArrayList<>();
    @Override   //사용자의 권한 목록 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return kakaoId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    //Jwt 설정 종료
}

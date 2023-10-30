package com.cau.vostom.user.service;

import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.team.repository.TeamMusicRepository;
import com.cau.vostom.team.repository.TeamUserRepository;
import com.cau.vostom.user.repository.CommentRepository;
import com.cau.vostom.user.repository.LikesRepository;
import com.cau.vostom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service @RequiredArgsConstructor
public class UserService {

    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final TeamUserRepository teamUserRepository;
    private final TeamMusicRepository teamMusicRepository;

    //회원 가입
    @Transactional
    public void createUser() {
    }

    //회원 정보 수정
    @Transactional
    public void updateUser() {
    }

    //회원 탈퇴
    @Transactional
    public void deleteUser() {
    }

    // 회원 중복 체크
    private boolean checkUser() {
        return true;
    }

    //회원 조회
    @Transactional(readOnly = true)
    public void getUser() {
    }

    //내 댓글 조회
    @Transactional(readOnly = true)
    public void getMyComment() {
    }

    //내가 좋아요 한 곡 조회
    @Transactional(readOnly = true)
    public void getMyLike() {
    }

    //내 노래 조회
    @Transactional(readOnly = true)
    public void getMyMusic() {
    }

    //학습 데이터 삭제
    @Transactional
    public void deleteData() {
    }
}

package com.cau.vostom.team.service;

import com.cau.vostom.team.repository.TeamMusicRepository;
import com.cau.vostom.team.repository.TeamRepository;
import com.cau.vostom.team.repository.TeamUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class TeamMusicService {

    private final TeamMusicRepository teamMusicRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;

    //그룹에 노래 추가
    @Transactional
    public void addMusic() {
    }

    //그룹에 노래 삭제
    @Transactional
    public void deleteMusic() {
    }

    //그룹에 노래 조회
    @Transactional(readOnly = true)
    public void getMusic() {
    }

    //그룹에 해당 노래 중복 체크
    private boolean checkMusic() {
        return true;
    }

}
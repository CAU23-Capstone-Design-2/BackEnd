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

//    //그룹에 노래 삭제
//    @Transactional
//    public void deleteMusic() {
//    }


}

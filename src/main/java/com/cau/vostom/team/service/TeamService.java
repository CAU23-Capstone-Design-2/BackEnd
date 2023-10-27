package com.cau.vostom.team.service;

import com.cau.vostom.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    //그룹 생성 날짜 조회
    @Transactional(readOnly = true)
    public void getTeamCreateDate() {
    }

    //그룹 생성
    @Transactional
    public void createTeam() {
    }

}

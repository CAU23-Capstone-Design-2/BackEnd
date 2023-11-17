package com.cau.vostom.team.service;

import com.cau.vostom.team.domain.Team;
import com.cau.vostom.team.domain.TeamUser;
import com.cau.vostom.team.dto.request.CreateTeamDto;
import com.cau.vostom.team.dto.request.UpdateTeamDto;
import com.cau.vostom.team.dto.response.ResponseTeamDetailDto;
import com.cau.vostom.team.dto.response.ResponseTeamDto;
import com.cau.vostom.team.dto.response.ResponseTeamMusicDto;
import com.cau.vostom.team.repository.TeamRepository;
import com.cau.vostom.team.repository.TeamUserRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.repository.UserRepository;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.TeamException;
import com.cau.vostom.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;
    private final UserRepository userRepository;

//    //그룹 인원 수 조회
//    @Transactional(readOnly = true)
//    public int getTeamUserCount(Long teamId) {
//        if(!teamRepository.existsById(teamId)) throw new UserException(ResponseCode.TEAM_NOT_FOUND);
//        return teamUserRepository.countTeamUserByTeamId(teamId);
//    }

    //그룹 생성
    @Transactional
    public Long createTeam(CreateTeamDto createTeamDto) {
        Team team = Team.createGroup(createTeamDto.getTeamName(), createTeamDto.getTeamImage(), createTeamDto.getTeamDescription());
        return teamRepository.save(team).getId();
    }

    //그룹 가입
    @Transactional
    public void joinTeam(Long userId, Long teamId) {
        if(!teamRepository.existsById(teamId)) throw new TeamException(ResponseCode.TEAM_NOT_FOUND);
        if(teamUserRepository.existsByUserIdAndTeamId(userId, teamId)) throw new TeamException(ResponseCode.TEAM_ALREADY_JOINED);
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamException(ResponseCode.TEAM_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        TeamUser teamUser = TeamUser.createGroupUser(team, user);
        teamUserRepository.save(teamUser);
    }

    //그룹 정보 수정
    @Transactional
    public void updateTeam(UpdateTeamDto updateTeamDto) {
        Team team = getTeamById(updateTeamDto.getTeamId());
        team.updateGroup(updateTeamDto.getTeamName(), updateTeamDto.getTeamImage(), updateTeamDto.getTeamDescription());
        teamRepository.save(team);
    }

    //모든 그룹 정보 조회
    @Transactional(readOnly = true)
    public List<ResponseTeamDto> getAllTeam() {
        return teamRepository.findAll().stream().map(ResponseTeamDto::from).collect(Collectors.toList());
    }

    //내 그룹 정보 조회
    @Transactional(readOnly = true)
    public List<ResponseTeamDto> getMyTeam(Long userId) {
        if(!teamUserRepository.existsByUserId(userId)) throw new UserException(ResponseCode.USER_NOT_FOUND);
        List<TeamUser> teamUsers = teamUserRepository.findByUserId(userId);
        return teamUsers.stream().map(teamUser -> ResponseTeamDto.from(teamUser.getTeam())).collect(Collectors.toList());
    }

    //특정 그룹 상세 정보 조회
    @Transactional(readOnly = true)
    public ResponseTeamDetailDto getTeamDetail(Long teamId) {
        Team team = getTeamById(teamId);
        List<ResponseTeamMusicDto> teamMusicList = team.getTeamMusics().stream().map(ResponseTeamMusicDto::from).collect(Collectors.toList());
        return ResponseTeamDetailDto.of(team.getGroupName(), team.getGroupImage(), team.getGroupInfo(), team.getTeamUsers().size(), teamMusicList);
    }

    //그룹 탈퇴
    @Transactional
    public void deleteTeam(Long userId, Long teamId) {
        if(!teamUserRepository.existsByUserIdAndTeamId(userId, teamId)) throw new TeamException(ResponseCode.TEAM_NOT_FOUND);
        teamUserRepository.deleteByUserIdAndTeamId(userId, teamId);
    }

    private Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(() -> new TeamException(ResponseCode.TEAM_NOT_FOUND));
    }

}

package com.cau.vostom.team.service;

import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.team.domain.Team;
import com.cau.vostom.team.domain.TeamMusic;
import com.cau.vostom.team.domain.TeamUser;
import com.cau.vostom.team.dto.request.CreateTeamDto;
import com.cau.vostom.team.dto.request.LeaveTeamDto;
import com.cau.vostom.team.dto.request.JoinTeamDto;
import com.cau.vostom.team.dto.request.UpdateTeamDto;
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

import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;

//    //그룹 인원 수 조회
//    @Transactional(readOnly = true)
//    public int getTeamUserCount(Long teamId) {
//        if(!teamRepository.existsById(teamId)) throw new UserException(ResponseCode.TEAM_NOT_FOUND);
//        return teamUserRepository.countTeamUserByTeamId(teamId);
//    }

    //그룹 생성
    @Transactional
    public Long createTeam(CreateTeamDto createTeamDto, Long userId) {
        Team team = Team.createGroup(createTeamDto.getTeamName(), createTeamDto.getTeamImage(), createTeamDto.getTeamDescription());
        User user = getUserById(userId);
        Long teamId = teamRepository.save(team).getId();
        TeamUser teamUser = TeamUser.createGroupUser(team, user, true);
        teamUserRepository.save(teamUser);
        return teamId;
    }

    //그룹 가입
    @Transactional
    public void joinTeam(JoinTeamDto joinTeamDto) {
        if(!teamRepository.existsById(joinTeamDto.getTeamId())) throw new TeamException(ResponseCode.TEAM_NOT_FOUND);
        if(teamUserRepository.existsByUserIdAndTeamId(joinTeamDto.getUserId(), joinTeamDto.getTeamId())) throw new TeamException(ResponseCode.TEAM_ALREADY_JOINED);
        Team team = getTeamById(joinTeamDto.getTeamId());
        User user = getUserById(joinTeamDto.getUserId());
        TeamUser teamUser = TeamUser.createGroupUser(team, user, false);
        teamUserRepository.save(teamUser);
    }

    //그룹 정보 수정
    @Transactional
    public void updateTeam(UpdateTeamDto updateTeamDto, Long userId) {
        Team team = getTeamById(updateTeamDto.getTeamId());
        if(!teamUserRepository.findByUserIdAndTeamId(userId, updateTeamDto.getTeamId()).isLeader()) throw new TeamException(ResponseCode.NOT_LEADER);
        team.updateGroup(updateTeamDto.getTeamName(), updateTeamDto.getTeamImage(), updateTeamDto.getTeamDescription());
        teamRepository.save(team);
    }

    //모든 그룹 정보 조회
    @Transactional(readOnly = true)
    public List<ResponseTeamDto> getAllTeam() {
        List<Team> teams = teamRepository.findAll();
        List<ResponseTeamDto> allTeamInfo = new ArrayList<>();
        for(Team team : teams) {
            TeamUser teamLeader = teamUserRepository.findByTeamIdAndIsLeader(team.getId(), true);
            allTeamInfo.add(ResponseTeamDto.of(team.getId(), team.getGroupName(), team.getGroupImage(), team.getGroupInfo(), team.getTeamUsers().size(), teamLeader.getUser().getId(), teamLeader.getUser().getNickname(), teamLeader.getUser().getProfileImage()));
        }
        return allTeamInfo;
    }

    //내 그룹 정보 조회
    @Transactional(readOnly = true)
    public List<ResponseTeamDto> getMyTeam(Long userId) {
        if(!teamUserRepository.existsByUserId(userId)) throw new UserException(ResponseCode.USER_NOT_FOUND);
        List<TeamUser> teamUsers = teamUserRepository.findByUserId(userId);
        List<ResponseTeamDto> myTeamInfo = new ArrayList<>();
        for(TeamUser teamUser : teamUsers) {
            Team team = teamUser.getTeam();
            TeamUser teamLeader = teamUserRepository.findByTeamIdAndIsLeader(team.getId(), true);
            myTeamInfo.add(ResponseTeamDto.of(team.getId(), team.getGroupName(), team.getGroupImage(), team.getGroupInfo(), team.getTeamUsers().size(), teamLeader.getUser().getId(), teamLeader.getUser().getNickname(), teamLeader.getUser().getProfileImage()));
        }
        return myTeamInfo;
    }

    //특정 그룹 플레이 리스트 조회
    @Transactional(readOnly = true)
    public List<ResponseTeamMusicDto> getTeamPlaylist(Long teamId, Long userId) {
        Team team = getTeamById(teamId);
        List<ResponseTeamMusicDto> myTeamMusic = new ArrayList<>();
        List<TeamMusic> teamMusics = team.getTeamMusics();
        for(TeamMusic teamMusic : teamMusics) {
            boolean isLiked = musicRepository.existsByUserIdAndId(userId,teamMusic.getMusic().getId());
            int likeCount = teamMusic.getMusic().getLikes().size();
            myTeamMusic.add(ResponseTeamMusicDto.of(teamMusic.getId(), teamMusic.getMusic().getTitle(), teamMusic.getMusic().getMusicImage(), teamMusic.getMusic().getUser().getId(), teamMusic.getMusic().getUser().getNickname(), teamMusic.getMusic().getUser().getProfileImage(), teamMusic.getMusic().getFileUrl(), likeCount, isLiked));
        }
        return myTeamMusic;
    }

    //그룹 탈퇴
    @Transactional
    public void leaveTeam(LeaveTeamDto leaveTeamDto, Long userId) {
        if(!teamUserRepository.existsByUserIdAndTeamId(userId, leaveTeamDto.getTeamId())) throw new TeamException(ResponseCode.TEAM_NOT_FOUND);
        if(teamUserRepository.findByUserIdAndTeamId(userId, leaveTeamDto.getTeamId()).isLeader()) throw new TeamException(ResponseCode.LEADER_CANNOT_LEAVE);
        teamUserRepository.deleteByUserIdAndTeamId(userId, leaveTeamDto.getTeamId());
    }

    private Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(() -> new TeamException(ResponseCode.TEAM_NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

}

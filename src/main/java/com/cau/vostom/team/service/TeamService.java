package com.cau.vostom.team.service;

import com.cau.vostom.music.repository.MusicLikesRepository;
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
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service @RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final MusicLikesRepository musicLikesRepository;

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    //그룹 생성
    @Transactional
    public Long createTeam(CreateTeamDto createTeamDto, Long userId) {
        log.info("createTeam");
        log.info("TeamName : " + createTeamDto.getTeamName() + "\n\n");
        log.info("TeamDescription : " + createTeamDto.getTeamDescription() + "\n\n");
        log.info("TeamImage : " + createTeamDto.getTeamImage().getOriginalFilename() + "\n\n");

        // 그룹 명, 그룹 설명 저장
        Team team = Team.createGroup(createTeamDto.getTeamName(),createTeamDto.getTeamDescription());

        // 그룹 이미지는 업로드 후 경로 저장
        MultipartFile imageFile = createTeamDto.getTeamImage();
        String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
        String fileName = saveImage(imageFile, extension);
        String filePath = "http://165.194.104.167:1100/api/group/profileImage/" + fileName;
        team.setGroupImagePath(filePath);
        Long teamId = teamRepository.save(team).getId();

        User user = getUserById(userId);
        TeamUser teamUser = TeamUser.createGroupUser(team, user, true);
        teamUserRepository.save(teamUser);
        return teamId;
    }

       //사용자 프로필 사진 다운로드
    @Transactional(readOnly = true)
    public ByteArrayResource downloadProfileImage(String fileName) throws IOException {
        log.info("downloadProfileImage");
        log.info("fileName : " + fileName + "\n\n");
        // 프로필 이미지 파일 경로
        String imagePath = uploadDirectory + fileName;
        log.info("imagePath : " + imagePath + "\n\n");

        // 프로필 이미지 파일을 읽어와서 Resource로 반환
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            return new ByteArrayResource(imageBytes);
        }


    //그룹 가입
    @Transactional
    public void joinTeam(Long userId, JoinTeamDto joinTeamDto) {
        if(!teamRepository.existsById(joinTeamDto.getTeamId())) throw new TeamException(ResponseCode.TEAM_NOT_FOUND);
        if(teamUserRepository.existsByUserIdAndTeamId(userId, joinTeamDto.getTeamId())) throw new TeamException(ResponseCode.TEAM_ALREADY_JOINED);
        Team team = getTeamById(joinTeamDto.getTeamId());
        User user = getUserById(userId);
        TeamUser teamUser = TeamUser.createGroupUser(team, user, false);
        teamUserRepository.save(teamUser);
    }

    //그룹 정보 수정
    @Transactional
    public void updateTeam(UpdateTeamDto updateTeamDto, Long userId) {
        log.info("updateTeam");
        log.info("TeamId: " + updateTeamDto.getTeamId() + "\n\n");
        log.info("TeamName : " + updateTeamDto.getTeamName() + "\n\n");
        log.info("TeamDescription : " + updateTeamDto.getTeamDescription() + "\n\n");
        log.info("TeamImage : " + updateTeamDto.getTeamImage().getOriginalFilename() + "\n\n");
        if(!teamRepository.existsById(updateTeamDto.getTeamId())) throw new TeamException(ResponseCode.TEAM_NOT_FOUND);
        Team team = getTeamById(updateTeamDto.getTeamId());
        if(!teamUserRepository.existsByUserIdAndTeamId(userId, updateTeamDto.getTeamId())){
            throw new UserException(ResponseCode.NOT_TEAM_MEMBER);
        }
        if(!teamUserRepository.findByUserIdAndTeamId(userId, updateTeamDto.getTeamId()).isLeader()) throw new TeamException(ResponseCode.NOT_LEADER);
        if(updateTeamDto.getTeamImage() != null) {
            MultipartFile imageFile = updateTeamDto.getTeamImage();
            String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
            String fileName = saveImage(imageFile, extension);
            String filePath = "http://165.194.104.167:1100/api/group/profileImage/" + fileName;
            team.updateGroup(updateTeamDto.getTeamName(), filePath, updateTeamDto.getTeamDescription());
            teamRepository.save(team);
            return;
        }else{
            team.updateGroup(updateTeamDto.getTeamName(), team.getGroupImagePath(), updateTeamDto.getTeamDescription());
            teamRepository.save(team);
            return;
        }
    }

    //모든 그룹 정보 조회
    @Transactional(readOnly = true)
    public List<ResponseTeamDto> getAllTeam() {
        List<Team> teams = teamRepository.findAll();
        List<ResponseTeamDto> allTeamInfo = new ArrayList<>();
        for(Team team : teams) {
            TeamUser teamLeader = teamUserRepository.findByTeamIdAndIsLeader(team.getId(), true);
            allTeamInfo.add(ResponseTeamDto.of(team.getId(), team.getGroupName(), team.getGroupImagePath(), team.getGroupInfo(), team.getTeamUsers().size(), teamLeader.getUser().getId(), teamLeader.getUser().getNickname(), teamLeader.getUser().getProfileImage()));
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
            myTeamInfo.add(ResponseTeamDto.of(team.getId(), team.getGroupName(), team.getGroupImagePath(), team.getGroupInfo(), team.getTeamUsers().size(), teamLeader.getUser().getId(), teamLeader.getUser().getNickname(), teamLeader.getUser().getProfileImage()));
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
            boolean isLiked = musicLikesRepository.existsByUserIdAndMusicId(userId,teamMusic.getMusic().getId());
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

    //그룹 삭제
    @Transactional
    public void deleteTeam(Long teamId, Long userId) {
        Team team = getTeamById(teamId);
        if(!teamUserRepository.findByUserIdAndTeamId(userId, teamId).isLeader()) throw new TeamException(ResponseCode.NOT_LEADER);
        teamRepository.delete(team);
    }

    private Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(() -> new TeamException(ResponseCode.TEAM_NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    private String saveImage(MultipartFile imageFile, String extension) {
        String fileName = "group_profile_" + imageFile.getOriginalFilename() + extension;
        try {
            log.info("\n\n파일 저장\n\n");
            log.info("fileName : " + fileName + "\n\n");
            imageFile.transferTo(new java.io.File(uploadDirectory + fileName));
        } catch (Exception e) {
            throw new TeamException(ResponseCode.FILE_UPLOAD_FAILED);
        }
        return fileName;
    }

}

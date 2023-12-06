package com.cau.vostom.group.service;

import com.cau.vostom.group.domain.Group;
import com.cau.vostom.group.domain.GroupMusic;
import com.cau.vostom.group.domain.GroupUser;
import com.cau.vostom.group.dto.request.RequestGroupDto;
import com.cau.vostom.group.dto.response.ResponseGroupDto;
import com.cau.vostom.group.dto.response.ResponseGroupMusicDto;
import com.cau.vostom.group.repository.GroupMusicRepository;
import com.cau.vostom.group.repository.GroupRepository;
import com.cau.vostom.group.repository.GroupUserRepository;
import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.repository.MusicLikesRepository;
import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.repository.UserRepository;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.GroupException;
import com.cau.vostom.util.exception.MusicException;
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
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final UserRepository userRepository;
    private final MusicLikesRepository musicLikesRepository;
    private final MusicRepository musicRepository;
    private final GroupMusicRepository groupMusicRepository;

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    // 그룹 생성
    @Transactional
    public Long createGroup(RequestGroupDto createGroupDto, Long userId, MultipartFile imageFile) {
        log.info("createGroup");
        log.info("GroupName : " + createGroupDto.getGroupName() + "\n\n");
        log.info("GroupDescription : " + createGroupDto.getGroupDescription() + "\n\n");
        log.info("GroupImage : " + imageFile.getOriginalFilename() + "\n\n");

        // 그룹 명, 그룹 설명 저장
        Group group = Group.createGroup(createGroupDto.getGroupName(), createGroupDto.getGroupDescription());

        // 그룹 이미지는 업로드 후 경로 저장
        String fileName = saveImage(imageFile);
        String filePath = "http://165.194.104.167:1100/api/groups/profileImage/" + fileName;
        group.setGroupImagePath(filePath);
        Long groupId = groupRepository.save(group).getId();

        User user = getUserById(userId);
        GroupUser groupUser = GroupUser.createGroupUser(group, user, true);
        groupUserRepository.save(groupUser);
        return groupId;
    }

    // 그룹 정보 수정
    @Transactional
    public Long updateGroup(RequestGroupDto requestGroupDto, Long userId, MultipartFile imageFile, Long groupId) {
        log.info("updateGroup");
        log.info("GroupName : " + requestGroupDto.getGroupName() + "\n\n");
        log.info("GroupDescription : " + requestGroupDto.getGroupDescription() + "\n\n");
        log.info("GroupImage : " + imageFile.getOriginalFilename() + "\n\n");

        if (!groupRepository.existsById(groupId))
            throw new GroupException(ResponseCode.GROUP_NOT_FOUND);
        Group group = getGroupById(groupId);
        if (!groupUserRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new UserException(ResponseCode.NOT_GROUP_MEMBER);
        }
        if (!groupUserRepository.findByUserIdAndGroupId(userId, groupId).isLeader())
            throw new GroupException(ResponseCode.NOT_LEADER);
        String fileName = saveImage(imageFile);
        String filePath = "http://165.194.104.167:1100/api/groups/profileImage/" + fileName;
        group.updateGroup(requestGroupDto.getGroupName(), filePath, requestGroupDto.getGroupDescription());
        groupRepository.save(group);
        return groupId;
    }

    // 사용자 프로필 사진 다운로드
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

    // 그룹 가입
    @Transactional
    public void joinGroup(Long userId, Long groupId) {
        if (!groupRepository.existsById(groupId))
            throw new GroupException(ResponseCode.GROUP_NOT_FOUND);
        if (groupUserRepository.existsByUserIdAndGroupId(userId, groupId))
            throw new GroupException(ResponseCode.GROUP_ALREADY_JOINED);
        Group group = getGroupById(groupId);
        User user = getUserById(userId);
        GroupUser groupUser = GroupUser.createGroupUser(group, user, false);
        groupUserRepository.save(groupUser);
    }


    // 모든 그룹 정보 조회
    @Transactional(readOnly = true)
    public List<ResponseGroupDto> getAllGroups(Long userId) {
        List<Group> groups = groupRepository.findAll();
        List<ResponseGroupDto> allGroupInfo = new ArrayList<>();
        for (Group group : groups) {
            GroupUser groupLeader = groupUserRepository.findByGroupIdAndIsLeader(group.getId(), true);
            Boolean isLeader = groupUserRepository.existsByUserIdAndGroupIdAndIsLeader(userId, group.getId(), true);
            Boolean isMember = groupUserRepository.existsByUserIdAndGroupId(userId, group.getId());
            allGroupInfo.add(ResponseGroupDto.of(
                    group.getId(),
                    group.getGroupName(),
                    group.getGroupImagePath(),
                    group.getGroupInfo(),
                    group.getGroupUsers().size(),
                    groupLeader.getUser().getId(),
                    groupLeader.getUser().getNickname(),
                    groupLeader.getUser().getProfileImage(),
                    isLeader,
                    isMember));
        }
        return allGroupInfo;
    }

    // 내 그룹 정보 조회
    @Transactional(readOnly = true)
    public List<ResponseGroupDto> getMyGroup(Long userId) {
        if (!groupUserRepository.existsByUserId(userId))
            throw new UserException(ResponseCode.USER_NOT_FOUND);
        List<GroupUser> groupUsers = groupUserRepository.findByUserId(userId);
        List<ResponseGroupDto> myGroupInfo = new ArrayList<>();
        for (GroupUser groupUser : groupUsers) {
            Group group = groupUser.getGroup();
            boolean isLeader = groupUser.isLeader();
            boolean isMember = true;
            GroupUser groupLeader = groupUserRepository.findByGroupIdAndIsLeader(group.getId(), true);
            myGroupInfo.add(ResponseGroupDto.of(
                group.getId(), 
                group.getGroupName(), 
                group.getGroupImagePath(),
                group.getGroupInfo(), 
                group.getGroupUsers().size(),
                groupLeader.getUser().getId(), 
                groupLeader.getUser().getNickname(),
                groupLeader.getUser().getProfileImage(), 
                isLeader, isMember));
        }
        return myGroupInfo;
    }

    // 특정 그룹 플레이 리스트 조회
    @Transactional(readOnly = true)
    public List<ResponseGroupMusicDto> getGroupPlaylist(Long groupId, Long userId) {
        Group group = getGroupById(groupId);
        List<ResponseGroupMusicDto> myGroupMusic = new ArrayList<>();
        List<GroupMusic> groupMusics = group.getGroupMusics();
        for (GroupMusic groupMusic : groupMusics) {
            boolean isLiked = musicLikesRepository.existsByUserIdAndMusicId(userId, groupMusic.getMusic().getId());
            int likeCount = groupMusic.getMusic().getLikes().size();
            myGroupMusic.add(ResponseGroupMusicDto.of(
                groupMusic.getMusic().getId(),
                groupMusic.getMusic().getTitle(),
                groupMusic.getMusic().getMusicImage(), groupMusic.getMusic().getUser().getId(),
                groupMusic.getMusic().getUser().getNickname(), groupMusic.getMusic().getUser().getProfileImage(),
                groupMusic.getMusic().getFileUrl(), likeCount, isLiked));
        }
        return myGroupMusic;
    }


    // 그룹 탈퇴
    @Transactional
    public void leaveGroup(Long groupId, Long userId) {
        if (!groupRepository.existsById((groupId)))
            throw new GroupException(ResponseCode.GROUP_NOT_FOUND);
        if (!groupUserRepository.existsByUserIdAndGroupId(userId, groupId))
            throw new GroupException(ResponseCode.NOT_GROUP_MEMBER);
        if (groupUserRepository.findByUserIdAndGroupId(userId, groupId).isLeader())
            throw new GroupException(ResponseCode.LEADER_CANNOT_LEAVE);
        groupUserRepository.deleteByUserIdAndGroupId(userId, groupId);
    }

    // 그룹 삭제
    @Transactional
    public void deleteGroup(Long groupId, Long userId) {
        Group group = getGroupById(groupId);
        if (!groupUserRepository.findByUserIdAndGroupId(userId, groupId).isLeader())
            throw new GroupException(ResponseCode.NOT_LEADER);
        groupRepository.delete(group);
    }

    private Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupException(ResponseCode.GROUP_NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    private Music getMusicById(Long musicId) {
        return musicRepository.findById(musicId).orElseThrow(() -> new MusicException(ResponseCode.MUSIC_NOT_FOUND));
    }

    private String saveImage(MultipartFile imageFile) {
        String fileName = "group_profile_" + imageFile.getOriginalFilename();
        try {
            log.info("\n\n파일 저장\n\n");
            log.info("fileName : " + fileName + "\n\n");
            imageFile.transferTo(new java.io.File(uploadDirectory + fileName));
        } catch (Exception e) {
            throw new GroupException(ResponseCode.FILE_UPLOAD_FAILED);
        }
        return fileName;
    }

    // 그룹에 노래 추가
    @Transactional
    public Long addSongToGroup(Long userId, Long groupId, Long musicId) {
        if(!groupUserRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new UserException(ResponseCode.NOT_GROUP_MEMBER);
        }
        Music music = getMusicById(musicId);
        if(!Objects.equals(music.getUser().getId(), userId)) {
            throw new UserException(ResponseCode.NOT_MUSIC_OWNER);
        }
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new MusicException(ResponseCode.GROUP_NOT_FOUND));
        GroupMusic groupMusic = GroupMusic.createGroupMusic(group, music);

        return groupMusicRepository.save(groupMusic).getId();
    }

    // 그룹에 노래 삭제
    @Transactional
    public void removeSongFromGroup(Long userId, Long groupId, Long musicId) {
        Music music = getMusicById(musicId);
        if(!Objects.equals(userId, music.getUser().getId()))
            throw new UserException(ResponseCode.NOT_MUSIC_OWNER);
        groupMusicRepository.deleteByMusicIdAndGroupId(musicId, groupId);
    }

}

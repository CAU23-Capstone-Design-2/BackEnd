package com.cau.vostom.user.service;

import com.cau.vostom.group.domain.GroupMusic;
import com.cau.vostom.group.domain.GroupUser;
import com.cau.vostom.group.dto.response.ResponseGroupMusicDto;
import com.cau.vostom.group.repository.GroupUserRepository;
import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.domain.MusicLikes;
import com.cau.vostom.music.repository.MusicLikesRepository;
import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.dto.request.*;
import com.cau.vostom.user.dto.response.ResponseCelebrityDto;
import com.cau.vostom.user.dto.response.ResponseMusicDto;
import com.cau.vostom.user.dto.response.ResponseUserDto;
import com.cau.vostom.user.repository.UserRepository;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final GroupUserRepository groupUserRepository;
    private final MusicLikesRepository musicLikesRepository;

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    //사용자 프로필 사진 업데이트
    @Transactional
    public void updateUser(Long userId, MultipartFile imageFile) throws IOException {
        User user = getUserById(userId);


        log.info("\n\n파일 저장 경로: " + uploadDirectory + "\n\n");
        String extension = StringUtils.getFilenameExtension(imageFile.getOriginalFilename());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String fileName ="profile_" + userId + "_" +  uuid + "." + extension;
        File file = new File(uploadDirectory + fileName);
        imageFile.transferTo(file);

        String filePath = "http://165.194.104.167:1100/api/user/profileImage/" + fileName;
        user.updateUser(filePath);
        userRepository.save(user);
    }

   //사용자 프로필 사진 다운로드
   @Transactional(readOnly = true)
   public ByteArrayResource downloadProfileImage(String fileName) throws IOException {

       // 프로필 이미지 파일 경로
       String imagePath = uploadDirectory + fileName;

       // 프로필 이미지 파일을 읽어와서 Resource로 반환
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        return new ByteArrayResource(imageBytes);
    }

    //회원 탈퇴
    @Transactional
    public void deleteUser(DeleteUserDto deleteUserDto) {
        User user = getUserById(deleteUserDto.getUserId());
        userRepository.delete(user);
    }


    //회원 조회
    @Transactional(readOnly = true)
    public ResponseUserDto getUserProfile(Long userId) {
        User user = getUserById(userId);
        return ResponseUserDto.from(user);
    }


    //내가 좋아요 한 노래 조회
    @Transactional(readOnly = true)
    public List<ResponseMusicDto> getUserLikedMusic(Long userId) {
        User user = getUserById(userId);
        List<MusicLikes> likedMusics = musicLikesRepository.findAllByUserId(user.getId());
        log.info("\n\n좋아요한 노래 개수 : " + likedMusics.size() + "\n\n");
        log.info("\n\n좋아요한 노래 likedMusics : " + likedMusics + "\n\n");
        List<ResponseMusicDto> userLikedMusics = new ArrayList<>();
        if (likedMusics.isEmpty()) {// 좋아요한 노래가 없는 경우
            return List.of();
        }
        for(MusicLikes musicLike : likedMusics) {
            log.info("\n\n좋아요한 노래 musicID : " + musicLike.getId() + "\n\n");
            Music music = musicLike.getMusic();
            log.info("\n\n좋아요한 노래 music : " + music.getTitle() + "\n\n");
            boolean isLiked = musicLikesRepository.existsByUserIdAndMusicId(userId, music.getId());
            int likeCount = music.getLikes().size();
            userLikedMusics.add(ResponseMusicDto.of(music.getUser().getId(), music.getUser().getNickname(), music.getUser().getProfileImage(), music.getId(), music.getTitle(), music.getMusicImage(), music.getFileUrl(), likeCount, isLiked));
        }
        // 좋아요한 노래를 ResponseMusicDto로 변환하여 반환
        return userLikedMusics;
    }


    //내 노래 조회
    @Transactional(readOnly = true)
    public List<ResponseMusicDto> getUserMusic(Long userId, boolean isTrained) {
        User user = getUserById(userId);
        List<Music> musics = musicRepository.findAllByUserIdAndIsTrained(user.getId(), isTrained);
        List<ResponseMusicDto> userMusics = new ArrayList<>();
        for(Music music : musics) {
            boolean isLiked = musicLikesRepository.existsByUserIdAndMusicId(userId, music.getId());
            int likeCount = music.getLikes().size();
            userMusics.add(ResponseMusicDto.of(music.getUser().getId(), music.getUser().getNickname(), music.getUser().getProfileImage(), music.getId(), music.getTitle(), music.getMusicImage(), music.getFileUrl(), likeCount, isLiked));
        }
        if(musics.isEmpty()) { //노래가 없는 경우
            return List.of();
        }
        return userMusics;
    }

    //연예인 리스트 조회
    @Transactional(readOnly = true)
    public List<ResponseCelebrityDto> getCelebrityList(){
        List<User> celebrity = userRepository.findAllByIsCelebrity(true);

        return celebrity.stream()
                .map(ResponseCelebrityDto::from)
                .distinct()
                .collect(Collectors.toList());
    }

    //내 그룹 노래 조회
    @Transactional(readOnly = true)
    public List<ResponseGroupMusicDto> getUserGroupMusic(Long userId) {
        User user = getUserById(userId);
        List<GroupUser> groupUsers = groupUserRepository.findAllByUserId(user.getId());
        if(groupUsers.isEmpty()) { //그룹에 속해 있지 않은 경우
            log.info("\n\n그룹에 속해 있지 않은 경우\n\n");
            return List.of();
        }
        List<ResponseGroupMusicDto> myGroupMusics = new ArrayList<>();
        for(GroupUser groupUser : groupUsers) {
            log.info("groupUser : " + groupUser.getGroup().getId());
            List<GroupMusic> groupMusics = groupUser.getGroup().getGroupMusics();
            for(GroupMusic groupMusic : groupMusics) {
                // music를 빼고
                long musicId = groupMusic.getMusic().getId();
                log.info("groupMusic : " + groupMusic.getMusic().getTitle());
                boolean isLiked = musicLikesRepository.existsByUserIdAndMusicId(userId,groupMusic.getMusic().getId());
                int likeCount = groupMusic.getMusic().getLikes().size();
                // 넣기 전에 myGroupMusic에 musicid가 있으면 넣지 않는다.
                if(myGroupMusics.stream().anyMatch(myGroupMusic -> myGroupMusic.getId() == musicId)) {
                    continue;
                }
                myGroupMusics.add(ResponseGroupMusicDto.of( groupMusic.getMusic().getId(),groupMusic.getMusic().getTitle(), groupMusic.getMusic().getMusicImage(), groupMusic.getMusic().getUser().getId(), groupMusic.getMusic().getUser().getNickname(), groupMusic.getMusic().getUser().getProfileImage(), groupMusic.getMusic().getFileUrl(), likeCount, isLiked));
            }
        }
        return myGroupMusics;
    }

    //사용자 음성 데이터 업로드
    @Transactional
    public void uploadVoice(Long userId, List<MultipartFile> voiceFiles) throws IOException {
        // 파일 저장 경로 생성
        String uploadDirectory = "/home/snark/dev/jiwoo/RVC/RVC-Model/upload_voice/" + userId + "/";
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (int i = 0; i < voiceFiles.size(); i++) {
            MultipartFile voiceFile = voiceFiles.get(i);

            // 파일 저장
            String fileName = "voice" + (i + 1) + ".m4a";
            String filePath = uploadDirectory + fileName;
            log.info("\n\n파일 저장 경로: " + filePath + "\n\n");
            File file = new File(filePath);
            voiceFile.transferTo(file);
        }

        User user = getUserById(userId);
        user.setmodelCompleted();
        log.info("\n\nUser 정보 : " + user.getId() + " " + user.getNickname() + " " + user.getModelCompleted() + "\n\n");
        userRepository.save(user);
    }

    //사용자 학습 완료 여부
    @Transactional(readOnly = true)
    public Integer checkTrained(Long userId) {
        User user = getUserById(userId);
        return user.getModelCompleted();
    }

    //사용자 목소리 재학습
    @Transactional
    public void retryTraining(Long userId){
        User user = getUserById(userId);
        user.retryVoiceData();
        userRepository.save(user);
    }


    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }


}

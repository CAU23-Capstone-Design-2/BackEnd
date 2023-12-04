package com.cau.vostom.user.service;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.domain.MusicLikes;
import com.cau.vostom.music.repository.MusicLikesRepository;
import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.team.domain.TeamMusic;
import com.cau.vostom.team.domain.TeamUser;
import com.cau.vostom.team.dto.response.ResponseTeamMusicDto;
import com.cau.vostom.team.repository.TeamMusicRepository;
import com.cau.vostom.team.repository.TeamUserRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.dto.request.*;
import com.cau.vostom.user.dto.response.ResponseCelebrityDto;
import com.cau.vostom.user.dto.response.ResponseMusicDto;
import com.cau.vostom.user.dto.response.ResponseUserDto;
import com.cau.vostom.user.repository.UserRepository;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.UserException;
import com.nimbusds.jose.util.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final TeamUserRepository teamUserRepository;
    private final TeamMusicRepository teamMusicRepository;
    private final MusicLikesRepository musicLikesRepository;

    //사용자 프로필 사진 업데이트
    @Transactional
    public void updateUser(Long userId, MultipartFile imageFile) throws IOException {
        User user = getUserById(userId);

        String uploadDirectory = "/home/snark/dev/jiwoo/RVC/RVC-Model/upload_profileImage/";

        log.info("\n\n파일 저장 경로: " + uploadDirectory + "\n\n");
        String extension = StringUtils.getFilenameExtension(imageFile.getOriginalFilename());
        String fileName = uploadDirectory + userId + "." + extension;
        File file = new File(fileName);
        imageFile.transferTo(file);

        String filePath = "http://165.194.104.167:1100/api/user/profileImage/" + userId + "." + extension;
        user.updateUser(filePath);
        userRepository.save(user);
    }

   //사용자 프로필 사진 다운로드
   @Transactional(readOnly = true)
   public ByteArrayResource downloadProfileImage(String fileName) throws IOException {

       // 프로필 이미지 파일 경로
       String imagePath = "/home/snark/dev/jiwoo/RVC/RVC-Model/upload_profileImage/" + fileName;

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

    // 회원 중복 체크
    private void validateUser(Long kakaoId) {
        if(userRepository.existsById((kakaoId))) {
            throw new UserException(ResponseCode.USER_ALREADY_EXISTS);
        }
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
    public List<ResponseTeamMusicDto> getUserTeamMusic(Long userId) {
        User user = getUserById(userId);
        List<TeamUser> teamUsers = teamUserRepository.findAllByUserId(user.getId());
        if(teamUsers.isEmpty()) { //그룹에 속해 있지 않은 경우
            return List.of();
        }
        List<ResponseTeamMusicDto> myTeamMusic = new ArrayList<>();
        for(TeamUser teamUser : teamUsers) {
            List<TeamMusic> teamMusics = teamUser.getTeam().getTeamMusics();
            for(TeamMusic teamMusic : teamMusics) {
                boolean isLiked = musicLikesRepository.existsByUserIdAndMusicId(userId,teamMusic.getMusic().getId());
                int likeCount = teamMusic.getMusic().getLikes().size();
                myTeamMusic.add(ResponseTeamMusicDto.of(teamMusic.getId(), teamMusic.getMusic().getTitle(), teamMusic.getMusic().getMusicImage(), teamMusic.getMusic().getUser().getId(), teamMusic.getMusic().getUser().getNickname(), teamMusic.getMusic().getUser().getProfileImage(), teamMusic.getMusic().getFileUrl(), likeCount, isLiked));
            }
        }
        return myTeamMusic;
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


    /*
    //학습 데이터 수정
    @Transactional
    public void retryVoiceData(RetryVoiceDataDto retryVoiceDataDto) {
        User user = getUserById(retryVoiceDataDto.getUserId());
        user.retryVoiceData(retryVoiceDataDto.getModelPath(), 2);
        userRepository.save(user);
    }
   */


    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }


}

package com.cau.vostom.user.service;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.team.domain.TeamMusic;
import com.cau.vostom.team.domain.TeamUser;
import com.cau.vostom.team.dto.response.ResponseTeamMusicDto;
import com.cau.vostom.team.repository.TeamMusicRepository;
import com.cau.vostom.team.repository.TeamUserRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.dto.request.*;
import com.cau.vostom.user.dto.response.ResponseMusicDto;
import com.cau.vostom.user.dto.response.ResponseUserDto;
import com.cau.vostom.user.repository.UserRepository;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final TeamUserRepository teamUserRepository;
    private final TeamMusicRepository teamMusicRepository;

    //회원 정보 수정
    @Transactional
    public void updateUser(UpdateUserDto updateUserDto) {
        User user = getUserById(updateUserDto.getUserId());
        user.updateUser(updateUserDto.getNickname());
        userRepository.save(user);
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
    public ResponseUserDto getUser(Long userId) {
        User user = getUserById(userId);
        return ResponseUserDto.from(user);
    }


    //내가 좋아요 한 노래 조회
    @Transactional(readOnly = true)
    public List<ResponseMusicDto> getUserLikedMusic(Long userId) {
        User user = getUserById(userId);
        List<Music> likedMusics = musicRepository.findLikedMusicsByUserId(user.getId());
        if (likedMusics.isEmpty()) {// 좋아요한 노래가 없는 경우
            return List.of();
        }

        // 좋아요한 노래를 ResponseMusicDto로 변환하여 반환
        return likedMusics.stream().map(ResponseMusicDto::from).collect(Collectors.toList());
    }


    //내 노래 조회
    @Transactional(readOnly = true)
    public List<ResponseMusicDto> getUserMusic(Long userId) {
        User user = getUserById(userId);
        List<Music> musics = musicRepository.findAllByUserId(user.getId());
        if(musics.isEmpty()) { //노래가 없는 경우
            return List.of();
        }
        return musics.stream().map(ResponseMusicDto::from).collect(Collectors.toList());
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
            if(teamMusics.isEmpty()) { //그룹에 노래가 없는 경우
                continue;
            }
            for(TeamMusic teamMusic : teamMusics) {
                myTeamMusic.add(ResponseTeamMusicDto.from(teamMusic));
            }
        }
        return myTeamMusic.stream().distinct().collect(Collectors.toList());
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

    /*목소리 학습 요청
    @Transactional
    public void requestVoiceTrain(RequestVoiceTrainDto requestVoiceTrainDto){
        User user = getUserById(requestVoiceTrainDto.getUserId());
        //서버에 파일 업로드 <- 서버의 어디에 저장할 지 경로 지정,
        //modelCompleted = 1
        // runtime.exec('python3 train.py --voice_path={경로} --model_name='{kakao_id}')
        //return 2
        user.requestVoiceTrain(requestVoiceTrainDto.getModelCompleted(), requestVoiceTrainDto.getUserVoice());
    }
    */

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }


}

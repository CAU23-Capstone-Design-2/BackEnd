package com.cau.vostom.user.service;

import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.team.repository.TeamMusicRepository;
import com.cau.vostom.team.repository.TeamUserRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.dto.request.DeleteUserDto;
import com.cau.vostom.user.dto.request.RetryVoiceDataDto;
import com.cau.vostom.user.dto.request.UpdateUserDto;
import com.cau.vostom.user.dto.response.ResponseUserDto;
import com.cau.vostom.user.repository.CommentRepository;
import com.cau.vostom.user.repository.LikesRepository;
import com.cau.vostom.user.repository.UserRepository;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final TeamUserRepository teamUserRepository;
    private final TeamMusicRepository teamMusicRepository;

    //회원 가입
    @Transactional
    public void createUser() {
    }

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

    //내 댓글 조회
    @Transactional(readOnly = true)
    public void getMyComment() {
    }

    //내가 좋아요 한 곡 조회
    @Transactional(readOnly = true)
    public void getMyLike() {
    }

    //내 노래 조회
    @Transactional(readOnly = true)
    public void getMyMusic() {
    }

    //학습 데이터 수정
    @Transactional
    public void retryVoiceData(RetryVoiceDataDto retryVoiceDataDto) {
        User user = getUserById(retryVoiceDataDto.getUserId());
        user.retryVoiceData(retryVoiceDataDto.getModelPath());
        userRepository.save(user);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }
}

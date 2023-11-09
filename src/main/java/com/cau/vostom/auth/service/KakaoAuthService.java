package com.cau.vostom.auth.service;

import com.cau.vostom.auth.component.KakaoUserInfo;
import com.cau.vostom.auth.dto.KakaoUserInfoResponse;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class KakaoAuthService {

    private final KakaoUserInfo kakaoUserInfo;
    private final UserRepository userRepository;

    @Transactional
    public Long userLogin(String token) {
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
        Optional<User> user = userRepository.findByKakaoId(userInfo.getId());
        if(user.isPresent()) {
            return user.get().getId();
        }
        else {
            User newUser = User.createUser(userInfo.getKakao_account().getProfile().getNickname(), userInfo.getKakao_account().getProfile().getProfile_image_url(), userInfo.getId());
            return userRepository.save(newUser).getId();
        }
    }
}

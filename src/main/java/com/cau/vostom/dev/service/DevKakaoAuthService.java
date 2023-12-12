package com.cau.vostom.dev.service;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DevKakaoAuthService {

    private final UserRepository userRepository;
    
    @Transactional
    public long devUserLogin(long kakaoId){
        // 만약 user db에 userId가 존재한다면
        // return userId;
        if(userRepository.findByKakaoId(kakaoId).isPresent()){
            System.out.println("이미 존재하는 유저입니다.");
            return userRepository.findByKakaoId(kakaoId).get().getId();
        }
        else{// 존재하지 않는다면
            // createUser(String nickname, String profileImage, Long kakaoId, int modelCompleted)
            String devProfileImage = "https://mblogthumb-phinf.pstatic.net/MjAyMjAxMjVfMjg0/MDAxNjQzMTAyOTg0Nzgz.sLrqOJ2S3r6pLboUm5yJTjB_JECC0zO9Tt3y_h86aJcg.w5VER_KDRAW3yRq8-nypsm2aGmKurM5YieSFcr1Vg0Qg.JPEG.minziminzi128/IMG_7374.JPG?type=w800";
            User newUser = User.createUser("개발용_"+Long.toString(kakaoId), devProfileImage, kakaoId, 0);
            return userRepository.save(newUser).getId();

        }


    }    
}

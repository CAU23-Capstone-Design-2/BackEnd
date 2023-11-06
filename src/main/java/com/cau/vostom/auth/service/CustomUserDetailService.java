package com.cau.vostom.auth.service;

import com.cau.vostom.user.repository.UserRepository;
import com.cau.vostom.util.api.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String kakaoId) throws UsernameNotFoundException {
        return userRepository.findByKakaoId(Long.parseLong(kakaoId))
                .orElseThrow(() -> new UsernameNotFoundException(ResponseCode.USER_NOT_FOUND.getMessage()));
    }
}


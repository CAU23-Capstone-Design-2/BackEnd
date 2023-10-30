package com.cau.vostom.music.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class MusicService {

    //그룹에 음악 업로드
    @Transactional
    public void uploadMusicToTeam() {
    }

    //음악 삭제
    @Transactional
    public void deleteMusic() {
    }

    //음악 학습 요청
    @Transactional
    public void requestMusic() {
    }
}

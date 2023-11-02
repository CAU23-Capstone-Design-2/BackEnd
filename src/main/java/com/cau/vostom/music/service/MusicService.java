package com.cau.vostom.music.service;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.dto.request.DeleteMusicDto;
import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.team.repository.TeamMusicRepository;
import com.cau.vostom.team.repository.TeamUserRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.MusicException;
import com.cau.vostom.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final TeamMusicRepository teamMusicRepository;
    private final TeamUserRepository teamUserRepository;

    //그룹에 음악 업로드
    @Transactional
    public void uploadMusicToTeam() {
    }

    //음악 삭제
    @Transactional
    public void deleteMusic(DeleteMusicDto deleteMusicDto) {
        Music music = getMusicById(deleteMusicDto.getMusicId());
        musicRepository.delete(music);
    }

    //음악 학습 요청
    @Transactional
    public void requestMusic() {
    }

    private Music getMusicById(Long musicId) {
        return musicRepository.findById(musicId).orElseThrow(() -> new UserException(ResponseCode.MUSIC_NOT_FOUND));
    }
}

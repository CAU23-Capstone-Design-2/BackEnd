package com.cau.vostom.music.service;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.domain.MusicLikes;
import com.cau.vostom.music.dto.request.DeleteMusicDto;
import com.cau.vostom.music.dto.request.MusicLikeDto;
import com.cau.vostom.music.dto.request.UploadMusicDto;
import com.cau.vostom.music.repository.MusicLikesRepository;
import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.team.domain.Team;
import com.cau.vostom.team.domain.TeamMusic;
import com.cau.vostom.team.repository.TeamMusicRepository;
import com.cau.vostom.team.repository.TeamRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.repository.UserRepository;
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
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final MusicLikesRepository musicLikesRepository;

    //그룹에 음악 업로드
    @Transactional
    public Long uploadMusicToTeam(UploadMusicDto uploadMusicDto) {
    Music music = getMusicById(uploadMusicDto.getMusicId());
    Team team = teamRepository.findById(uploadMusicDto.getTeamId()).orElseThrow(() -> new MusicException(ResponseCode.TEAM_NOT_FOUND));
    TeamMusic teamMusic = TeamMusic.createGroupMusic(team, music);

    return teamMusicRepository.save(teamMusic).getId();
    }

    //좋아요 누르기
    @Transactional
    public void likeMusic(MusicLikeDto musicLikeDto) {
        User user = getUserById(musicLikeDto.getUserId());
        Music music = getMusicById(musicLikeDto.getMusicId());
        MusicLikes musicLikes = MusicLikes.createMusicLikes(user, music);
        if(musicLikesRepository.existsByUserIdAndMusicId(user.getId(), music.getId()))
            throw new UserException(ResponseCode.LIKE_ALREADY_EXISTS);
        musicLikesRepository.save(musicLikes);
    }

    //좋아요 취소
    @Transactional
    public void unlikeMusic(MusicLikeDto musicLikeDto) {
        User user = getUserById(musicLikeDto.getUserId());
        Music music = getMusicById(musicLikeDto.getMusicId());
        if(!(musicLikesRepository.existsByUserIdAndMusicId(user.getId(), music.getId())))
            throw new UserException(ResponseCode.LIKE_ALREADY_DELETED);
        musicLikesRepository.deleteByUserIdAndMusicId(user.getId(), music.getId());
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

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }
}

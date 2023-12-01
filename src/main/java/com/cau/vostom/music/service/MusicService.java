package com.cau.vostom.music.service;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.domain.MusicLikes;
import com.cau.vostom.music.dto.request.DeleteMusicDto;
import com.cau.vostom.music.dto.request.MusicLikeDto;
import com.cau.vostom.music.dto.request.RequestMusicTrainDto;
import com.cau.vostom.music.dto.request.UploadMusicDto;
import com.cau.vostom.music.repository.CacheRepository;
import com.cau.vostom.music.repository.MusicLikesRepository;
import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.team.domain.Team;
import com.cau.vostom.team.domain.TeamMusic;
import com.cau.vostom.team.repository.TeamMusicRepository;
import com.cau.vostom.team.repository.TeamRepository;
import com.cau.vostom.team.repository.TeamUserRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.repository.UserRepository;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.MusicException;
import com.cau.vostom.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Objects;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service @RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final TeamMusicRepository teamMusicRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;
    private final UserRepository userRepository;
    private final MusicLikesRepository musicLikesRepository;
    private final CacheRepository cacheRepository;

    //그룹에 음악 업로드
    @Transactional
    public Long uploadMusicToTeam(Long userId, UploadMusicDto uploadMusicDto) {
        if(!teamUserRepository.existsByUserIdAndTeamId(userId, uploadMusicDto.getTeamId())){
            throw new UserException(ResponseCode.NOT_TEAM_MEMBER);
        }
        Music music = getMusicById(uploadMusicDto.getMusicId());
        Team team = teamRepository.findById(uploadMusicDto.getTeamId()).orElseThrow(() -> new MusicException(ResponseCode.TEAM_NOT_FOUND));
        TeamMusic teamMusic = TeamMusic.createGroupMusic(team, music);

        return teamMusicRepository.save(teamMusic).getId();
    }

    //그룹에 업로드한 음악 삭제
    @Transactional
    public void deleteMusicToTeam(Long userId, UploadMusicDto uploadMusicDto){
        Music music = getMusicById(uploadMusicDto.getMusicId());
        if(!Objects.equals(userId, music.getUser().getId()))
            throw new UserException(ResponseCode.NOT_MUSIC_OWNER);
        teamMusicRepository.deleteByMusicIdAndTeamId(uploadMusicDto.getMusicId(), uploadMusicDto.getTeamId());
    }

    //좋아요 누르기
    @Transactional
    public void likeMusic(MusicLikeDto musicLikeDto) {
        User user = getUserById(musicLikeDto.getUserId());
        Music music = getMusicById(musicLikeDto.getId());
        MusicLikes musicLikes = MusicLikes.createMusicLikes(user, music);
        if(musicLikesRepository.existsByUserIdAndMusicId(user.getId(), music.getId()))
            throw new UserException(ResponseCode.LIKE_ALREADY_EXISTS);
        musicLikesRepository.save(musicLikes);
    }

    //좋아요 취소
    @Transactional
    public void unlikeMusic(MusicLikeDto musicLikeDto) {
        User user = getUserById(musicLikeDto.getUserId());
        Music music = getMusicById(musicLikeDto.getId());
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
    @Async
    public void trainMusic(Long userId, RequestMusicTrainDto requestMusicTrainDto, String url) throws IOException {
        String d_url = URLDecoder.decode(url, "UTF-8");

        String title = requestMusicTrainDto.getTitle();
        String imgUrl = requestMusicTrainDto.getImgUrl();
        String youtubeCode = d_url.split("v=")[1].split("&")[0];

        Music music = musicRepository.save(Music.createMusic(title, imgUrl, userId.toString()+'_'+youtubeCode+".mp3"));


        String newDirectoryPath = "/home/snark/dev/jiwoo/RVC/RVC-model/";
        // Java(Spring)에서 현재 작업 디렉토리 변경
        System.setProperty(userId+".dir", newDirectoryPath);

        if(cacheRepository.existsByYoutubeCode(youtubeCode)) {
            String command = "python myinfer.py " + youtubeCode + ".wav " + userId + ".pth";

            // command 실행
            Process process = Runtime.getRuntime().exec(command);
        }
        else {
            String command = "python pre.py --url " + d_url;
            Process process = Runtime.getRuntime().exec(command); // 2m

            // 2. 학습 (infer.py)
            String command2 = "python myinfer.py " + youtubeCode + ".wav " + userId + ".pth";

            // command 실행
            Process process2 = Runtime.getRuntime().exec(command2); // 20s
        }

        music.setTrained();
        musicRepository.save(music);
    }

    private Music getMusicById(Long musicId) {
        return musicRepository.findById(musicId).orElseThrow(() -> new UserException(ResponseCode.MUSIC_NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }
}

package com.cau.vostom.music.service;

import com.cau.vostom.music.domain.Cache;
import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.domain.MusicLikes;
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
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Objects;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final TeamMusicRepository teamMusicRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;
    private final UserRepository userRepository;
    private final MusicLikesRepository musicLikesRepository;
    private final CacheRepository cacheRepository;

    // 그룹에 음악 업로드
    @Transactional
    public Long uploadMusicToTeam(Long userId, UploadMusicDto uploadMusicDto) {
        if (!teamUserRepository.existsByUserIdAndTeamId(userId, uploadMusicDto.getTeamId())) {
            throw new UserException(ResponseCode.NOT_TEAM_MEMBER);
        }
        Music music = getMusicById(uploadMusicDto.getMusicId());
        if (!Objects.equals(music.getUser().getId(), userId)) {
            throw new UserException(ResponseCode.NOT_MUSIC_OWNER);
        }
        Team team = teamRepository.findById(uploadMusicDto.getTeamId())
                .orElseThrow(() -> new MusicException(ResponseCode.TEAM_NOT_FOUND));
        TeamMusic teamMusic = TeamMusic.createGroupMusic(team, music);

        return teamMusicRepository.save(teamMusic).getId();
    }

    // 그룹에 업로드한 음악 삭제
    @Transactional
    public void deleteMusicToTeam(Long userId, UploadMusicDto uploadMusicDto) {
        Music music = getMusicById(uploadMusicDto.getMusicId());
        if (!Objects.equals(userId, music.getUser().getId()))
            throw new UserException(ResponseCode.NOT_MUSIC_OWNER);
        teamMusicRepository.deleteByMusicIdAndTeamId(uploadMusicDto.getMusicId(), uploadMusicDto.getTeamId());
    }

    // 좋아요 누르기
    @Transactional
    public void likeMusic(MusicLikeDto musicLikeDto) {
        User user = getUserById(musicLikeDto.getUserId());
        Music music = getMusicById(musicLikeDto.getId());
        MusicLikes musicLikes = MusicLikes.createMusicLikes(user, music);
        if (musicLikesRepository.existsByUserIdAndMusicId(user.getId(), music.getId()))
            throw new UserException(ResponseCode.LIKE_ALREADY_EXISTS);
        musicLikesRepository.save(musicLikes);
    }

    // 좋아요 취소
    @Transactional
    public void unlikeMusic(MusicLikeDto musicLikeDto) {
        User user = getUserById(musicLikeDto.getUserId());
        Music music = getMusicById(musicLikeDto.getId());
        if (!(musicLikesRepository.existsByUserIdAndMusicId(user.getId(), music.getId())))
            throw new UserException(ResponseCode.LIKE_ALREADY_DELETED);
        musicLikesRepository.deleteByUserIdAndMusicId(user.getId(), music.getId());
    }

    // 음악 삭제
    @Transactional
    public void deleteMusic(Long userId, Long musicId) {
        Music music = getMusicById(musicId);
        if (!Objects.equals(music.getUser().getId(), userId))
            throw new UserException(ResponseCode.NOT_MUSIC_OWNER);
        musicRepository.delete(music);
    }

    // 음악 학습 요청
    public void trainMusic(Long userId, RequestMusicTrainDto requestMusicTrainDto, String url)
            throws IOException, InterruptedException {
        String d_url = URLDecoder.decode(url, "UTF-8");

        log.info("\n\n인코딩된 url을 디코딩 하는 과정: " + d_url);

        String title = requestMusicTrainDto.getTitle();
        String imgUrl = requestMusicTrainDto.getImgUrl();
        String youtubeCode = d_url.split("v=")[1].split("&")[0];
        log.info("\n\n유튜브 코드 파싱하기: " + youtubeCode);


        
        Music music = musicRepository.save(
                Music.createMusic(getUserById(userId), title, imgUrl, userId.toString() + '_' + youtubeCode + ".mp3"));
        log.info("\n\nMusic table에 음악 저장하기: " + title + ' ' + imgUrl + ' ' + music.getFileUrl() + ' '
                + music.isTrained());

        String newDirectoryPath = "/home/snark/dev/jiwoo/RVC/RVC-Model/";
        // Java(Spring)에서 현재 작업 디렉토리 변경
        System.setProperty("user.dir", newDirectoryPath);

        // 현재 작업 디렉토리 확인
        String currentDirectory = System.getProperty("user.dir");
        log.info("\n\n현재 작업 디렉토리 변경 완료. 현재 작업 디렉토리: " + currentDirectory);

        String activateCommand = "/home/snark/anaconda3/bin/conda run -n jiwoo-diffsvc";
        String pythonCommand = "python myinfer.py " + userId + ".pth " + youtubeCode + ".wav";
        String fullCommand = activateCommand + " " + pythonCommand;

        if (cacheRepository.existsByYoutubeCode(youtubeCode)) {
            log.info("\n\n캐시테이블에 존재하는 음악인 경우");
        } else {
            log.info("\n\n캐시테이블에 존재하지 않는 음악인 경우");
            String preProcessCommand = "/home/snark/anaconda3/bin/conda run -n jiwoo-diffsvc python pre.py --url \""
                    + d_url + "\"";
            executeCommand(preProcessCommand);
            cacheRepository.save(Cache.createCache((youtubeCode)));
            log.info("\n\npreProcessCommand 실행: " + preProcessCommand);
        }

        // command 실행
        executeCommand(fullCommand);
        log.info("\n\ncommand 실행: " + fullCommand);

        music.setTrained();
        log.info("\n\nisTrained를 true로 변경: " + title + imgUrl + music.getFileUrl() + music.isTrained());
        musicRepository.save(music);
        log.info("\n\nMusic_DB에 저장 성공!");
    }

    // 스트리밍 요청
    /*
     * 스트리밍을 요청 할 수 있는 경우
     *   1. celebrity 음악
     *   2. 음악의 소유자
     *   3. 그룹에 소속된 사용자
     * 
     */
    public ResponseEntity<StreamingResponseBody> streamMusic(Long userId, Long musicId) throws IOException{
        User user = getUserById(userId);
        Music music = getMusicById(musicId);
        checkValidation(user,music);
        String musicDirectoryPath = "/home/snark/dev/jiwoo/RVC/RVC-Model/inference_result/";
        String filePath = musicDirectoryPath + music.getFileUrl();

        StreamingResponseBody responseBody = outputStream -> {
            try (InputStream inputStream = new FileInputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };


        
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + music.getFileUrl() + "\"")
                .body(responseBody);


    }


    private void executeCommand(String command) throws IOException, InterruptedException {
        String newDirectoryPath = "/home/snark/dev/jiwoo/RVC/RVC-Model/";
        ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
        processBuilder.directory(new File(newDirectoryPath));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // 프로세스의 출력 및 에러 스트림을 읽어오기
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("\n\nProcess Output: " + line);
            }
        }

        // 프로세스의 종료를 기다리고 결과 코드 확인
        int exitCode = process.waitFor();
        log.info("\n\nProcess exit code: " + exitCode);
    }

    private Music getMusicById(Long musicId) {
        return musicRepository.findById(musicId).orElseThrow(() -> new UserException(ResponseCode.MUSIC_NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    private void checkValidation(User user, Music music) {
        // 다음 3개의 조건 or 조건을 만족하지 않으면 예외 발생
        // 1. celebrity 음악
        // 2. 음악의 소유자
        // 3. 그룹에 소속된 사용자

    }
    
}

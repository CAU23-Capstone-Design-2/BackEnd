package com.cau.vostom.music.service;

import com.cau.vostom.music.domain.Music;
import com.cau.vostom.music.dto.request.DeleteMusicDto;
import com.cau.vostom.music.dto.request.UploadMusicDto;
import com.cau.vostom.music.dto.response.ResponseMusicCommentDto;
import com.cau.vostom.music.repository.MusicRepository;
import com.cau.vostom.team.domain.Team;
import com.cau.vostom.team.domain.TeamMusic;
import com.cau.vostom.team.repository.TeamMusicRepository;
import com.cau.vostom.team.repository.TeamRepository;
import com.cau.vostom.team.repository.TeamUserRepository;
import com.cau.vostom.user.domain.Comment;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.user.dto.response.ResponseMyCommentDto;
import com.cau.vostom.user.repository.CommentRepository;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.MusicException;
import com.cau.vostom.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final TeamMusicRepository teamMusicRepository;
    private final TeamRepository teamRepository;
    private final CommentRepository commentRepository;

    //그룹에 음악 업로드
    @Transactional
    public Long uploadMusicToTeam(UploadMusicDto uploadMusicDto) {
    Music music = getMusicById(uploadMusicDto.getMusicId());
    Team team = teamRepository.findById(uploadMusicDto.getTeamId()).orElseThrow(() -> new MusicException(ResponseCode.TEAM_NOT_FOUND));
    TeamMusic teamMusic = TeamMusic.createGroupMusic(team, music);

    return teamMusicRepository.save(teamMusic).getId();
    }

    //음악 삭제
    @Transactional
    public void deleteMusic(DeleteMusicDto deleteMusicDto) {
        Music music = getMusicById(deleteMusicDto.getMusicId());
        musicRepository.delete(music);
    }

    //노래의 댓글 조회
    @Transactional(readOnly = true)
    public List<ResponseMusicCommentDto> getMusicComment(Long musicId) {
        Music music = getMusicById(musicId);
        List<Comment> comments = commentRepository.findAllByMusicId(music.getId());
        if(comments.isEmpty()) { //댓글이 없는 경우
            return List.of();
        }
        return comments.stream().map(ResponseMusicCommentDto::from).collect(Collectors.toList());

    }

    //음악 학습 요청
    @Transactional
    public void requestMusic() {

    }

    private Music getMusicById(Long musicId) {
        return musicRepository.findById(musicId).orElseThrow(() -> new UserException(ResponseCode.MUSIC_NOT_FOUND));
    }
}

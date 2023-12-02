package com.cau.vostom.team.controller;

import com.cau.vostom.auth.component.JwtTokenProvider;
import com.cau.vostom.team.dto.request.CreateTeamDto;
import com.cau.vostom.team.dto.request.LeaveTeamDto;
import com.cau.vostom.team.dto.request.JoinTeamDto;
import com.cau.vostom.team.dto.request.UpdateTeamDto;
import com.cau.vostom.team.dto.response.ResponseTeamDto;
import com.cau.vostom.team.dto.response.ResponseTeamMusicDto;
import com.cau.vostom.team.service.TeamService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Groups"})
@RequestMapping("/api/group")
public class TeamController {
    private final TeamService teamService;
    private final JwtTokenProvider jwtTokenProvider;

    //모든 그룹 정보 조회
    @Operation(summary = "모든 그룹 정보 조회")
    @GetMapping("/list")
    public ApiResponse<List<ResponseTeamDto>> getAllTeam(@RequestHeader String accessToken) {
        return ApiResponse.success(teamService.getAllTeam(), ResponseCode.TEAM_LISTED.getMessage());
    }

    //내 그룹 정보 조회
    @Operation(summary = "내 그룹 정보 조회")
    @GetMapping("/mylist")
    public ApiResponse<List<ResponseTeamDto>> getMyTeam(@RequestHeader String accessToken) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(teamService.getMyTeam(userId), ResponseCode.MY_TEAM_LISTED.getMessage());
    }

    //그룹 상세 정보 조회
    @Operation(summary = "특정 그룹의 플레이 리스트 조회")
    @GetMapping("/playlist")
    public ApiResponse<List<ResponseTeamMusicDto>> getTeamDetail(@RequestHeader String accessToken, @RequestParam Long id) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(teamService.getTeamPlaylist(id, userId), ResponseCode.TEAM_DETAIL_READ.getMessage());
    }

    //그룹 생성
    @Operation(summary = "그룹 생성")
    @PostMapping
    public ApiResponse<Long> createTeam(@RequestHeader String accessToken, @RequestBody CreateTeamDto createTeamDto) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(teamService.createTeam(createTeamDto, userId), ResponseCode.TEAM_CREATED.getMessage());
    }

    //그룹 가입
    @Operation(summary = "그룹 가입")
    @PostMapping("/join")
    public ApiResponse<Void> joinTeam(@RequestHeader String accessToken, @RequestBody JoinTeamDto joinTeamDto) {
        teamService.joinTeam(joinTeamDto);
        return ApiResponse.success(null, ResponseCode.TEAM_JOINED.getMessage());
    }

    //그룹 탈퇴
    @Operation(summary = "그룹 탈퇴")
    @PostMapping("/leave")
    public ApiResponse<Void> leaveTeam(@RequestHeader String accessToken, @RequestBody LeaveTeamDto leaveTeamDto) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        teamService.leaveTeam(leaveTeamDto, userId);
        return ApiResponse.success(null, ResponseCode.TEAM_LEAVED.getMessage());
    }

    //그룹 정보 수정
    @Operation(summary = "그룹 정보 수정")
    @PutMapping
    public ApiResponse<Void> updateTeam(@RequestHeader String accessToken, @RequestBody UpdateTeamDto updateTeamDto) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        teamService.updateTeam(updateTeamDto, userId);
        return ApiResponse.success(null, ResponseCode.TEAM_UPDATED.getMessage());
    }

    //그룹 삭제
    @Operation(summary = "그룹 삭제")
    @DeleteMapping
    public ApiResponse<Void> deleteTeam(@RequestHeader String accessToken, @RequestBody Long id) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        teamService.deleteTeam(id, userId);
        return ApiResponse.success(null, ResponseCode.TEAM_DELETED.getMessage());
    }
}

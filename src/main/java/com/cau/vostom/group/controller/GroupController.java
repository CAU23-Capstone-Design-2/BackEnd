package com.cau.vostom.group.controller;

import com.cau.vostom.auth.component.JwtTokenProvider;
import com.cau.vostom.group.dto.request.CreateGroupDto;
import com.cau.vostom.group.dto.request.UpdateGroupDto;
import com.cau.vostom.group.dto.response.ResponseGroupDto;
import com.cau.vostom.group.dto.response.ResponseGroupMusicDto;
import com.cau.vostom.group.service.GroupService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"Groups"})
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;
    private final JwtTokenProvider jwtTokenProvider;

    //모든 그룹 정보 조회
    // isLeader, isMember 추가
    @Operation(summary = "모든 그룹 정보 조회")
    @GetMapping
    public ApiResponse<List<ResponseGroupDto>> getAllGroups(@RequestHeader String accessToken) {
        log.info("groupController.getAllGroups()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(groupService.getAllGroups(userId), ResponseCode.GROUP_LISTED.getMessage());
    }

    //내 그룹 정보 조회
    // isLeader, isMember 추가 
    @Operation(summary = "내 그룹 정보 조회")
    @GetMapping("/my-groups")
    public ApiResponse<List<ResponseGroupDto>> getMyGroup(@RequestHeader String accessToken) {
        log.info("groupController.getMyGroup()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(groupService.getMyGroup(userId), ResponseCode.MY_GROUP_LISTED.getMessage());
    }

    //그룹 상세 정보 조회
    @Operation(summary = "특정 그룹의 플레이 리스트 조회")
    @GetMapping("{groupId}/playlist")
    public ApiResponse<List<ResponseGroupMusicDto>> getGroupDetail(@RequestHeader String accessToken, @PathVariable Long groupId) {
        log.info("groupController.getGroupDetail()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(groupService.getGroupPlaylist(groupId, userId), ResponseCode.GROUP_DETAIL_READ.getMessage());
    }


    //그룹 생성
    @Operation(summary = "그룹 생성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse<Long> createGroup(@RequestHeader String accessToken, 
                                        @RequestPart MultipartFile groupImage,
                                        @RequestPart CreateGroupDto createGroupDto ) throws IOException{
        log.info("groupConteroller.createGroup()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(groupService.createGroup(createGroupDto, userId, groupImage ), ResponseCode.GROUP_CREATED.getMessage());
    }

    //그룹 정보 수정
    @Operation(summary = "그룹 정보 수정")
    @PutMapping("/{groupId}")
    public ApiResponse<Void> updateGroup(@RequestHeader String accessToken, @PathVariable Long groupId,  
                                                    @RequestBody UpdateGroupDto updateGroupDto) {
        log.info("groupController.updateGroup()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        groupService.updateGroup(updateGroupDto, groupId, userId);
        return ApiResponse.success(null, ResponseCode.GROUP_UPDATED.getMessage());
    }


    //그룹 가입
    @Operation(summary = "그룹 가입")
    @PostMapping("{groupId}/join")
    public ApiResponse<Void> joinGroup(@RequestHeader String accessToken, @PathVariable Long groupId) {
        log.info("groupController.joinGroup()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        groupService.joinGroup(userId, groupId);
        return ApiResponse.success(null, ResponseCode.GROUP_JOINED.getMessage());
    }

    //그룹 탈퇴
    @Operation(summary = "그룹 탈퇴")
    @DeleteMapping("{groupId}/leave")
    public ApiResponse<Void> leaveGroup(@RequestHeader String accessToken, @PathVariable Long groupId) {
        log.info("groupController.leaveGroup()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        groupService.leaveGroup(groupId, userId);
        return ApiResponse.success(null, ResponseCode.GROUP_LEAVED.getMessage());
    }


    //그룹 삭제
    @Operation(summary = "그룹 삭제")
    @DeleteMapping("/{groupId}")
    public ApiResponse<Void> deleteGroup(@RequestHeader String accessToken, @PathVariable Long groupId) {
        log.info("groupController.deleteGroup()");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        groupService.deleteGroup(groupId, userId);
        return ApiResponse.success(null, ResponseCode.GROUP_DELETED.getMessage());
    }


    @Operation(summary = "그룹에 노래 추가")
    @PostMapping("{groupId}/add-song/{musicId}")
    public ApiResponse<Long> addSongToGroup(
        @RequestHeader String accessToken,
        @PathVariable Long groupId,
        @PathVariable Long musicId
    ) {
        log.info("GroupController.addSongToGroup");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken)); 
        return ApiResponse.success(groupService.addSongToGroup(userId,groupId,musicId) , ResponseCode.SONG_ADDED.getMessage());
    }

    @Operation(summary = "그룹에서 노래 삭제")
    @DeleteMapping("{groupId}/remove-song/{musicId}")
    public ApiResponse<Void> removeSongFromGroup(
        @RequestHeader String accessToken,
        @PathVariable Long groupId,
        @PathVariable Long musicId
    ) {
        log.info("GroupController.removeSongFromGroup");
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken)); 
        groupService.removeSongFromGroup(userId, groupId, musicId);
        return ApiResponse.success(null, ResponseCode.SONG_REMOVED.getMessage());
    }


    @Operation(summary = "프로필 이미지 다운로드")
    @GetMapping(value = "/profileImage/{fileName}", produces = { "image/jpeg", "image/png" })
    public ResponseEntity<ByteArrayResource> downloadProfileImage(@PathVariable String fileName) throws IOException {
        ByteArrayResource resource = groupService.downloadProfileImage(fileName);

        HttpHeaders headers = new HttpHeaders();
        if (fileName.contains(".jpg")) {
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (fileName.contains(".png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        }

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }

}

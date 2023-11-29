package com.cau.vostom.comment.controller;

import com.cau.vostom.auth.component.JwtTokenProvider;
import com.cau.vostom.comment.dto.request.CreateCommentDto;
import com.cau.vostom.comment.dto.request.RequestCommentLikeDto;
import com.cau.vostom.comment.dto.response.ResponseMusicCommentDto;
import com.cau.vostom.comment.dto.response.ResponseMyCommentDto;
import com.cau.vostom.comment.service.CommentService;
import com.cau.vostom.util.api.ApiResponse;
import com.cau.vostom.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Comment"})
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "내 댓글 조회")
    @GetMapping("/mylist")
    public ApiResponse<List<ResponseMyCommentDto>> getUserComment(@RequestHeader String accessToken) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(commentService.getUserComment(userId), ResponseCode.COMMENT_READ.getMessage());
    }

    //댓글 작성
    @Operation(summary = "댓글 작성")
    @PostMapping("/create")
    public ApiResponse<Long> createComment(@RequestHeader String accessToken, @RequestBody CreateCommentDto createCommentDto) {
        return ApiResponse.success(commentService.writeComment(createCommentDto), ResponseCode.COMMENT_CREATED.getMessage());
    }

    @Operation(summary = "노래의 댓글 조회")
    @GetMapping("/music")
    public ApiResponse<List<ResponseMusicCommentDto>> getMusicComment(@RequestHeader String accessToken, @RequestParam String id) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        return ApiResponse.success(commentService.getMusicComment(Long.parseLong(id), userId), ResponseCode.MUSIC_COMMENT_READ.getMessage());
    }

    //댓글 좋아요
    @Operation(summary = "댓글 좋아요")
    @PostMapping("/like")
    public ApiResponse<Void> likeComment(@RequestHeader String accessToken, @RequestParam String id) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        commentService.likeComment(RequestCommentLikeDto.of(userId, Long.parseLong(id)));
        return ApiResponse.success(null, ResponseCode.COMMENT_LIKE_CREATED.getMessage());
    }

    //댓글 좋아요 취소
    @Operation(summary = "댓글 좋아요 취소")
    @DeleteMapping("/like/undo")
    public ApiResponse<Void> deleteLike(@RequestHeader String accessToken, @RequestParam String id) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        commentService.unlikeComment(RequestCommentLikeDto.of(userId, Long.parseLong(id)));
        return ApiResponse.success(null, ResponseCode.COMMENT_LIKE_UNDO.getMessage());
    }

    //댓글 삭제
    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteComment(@RequestHeader String accessToken, @RequestParam String id) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        commentService.deleteComment(userId, Long.parseLong(id));
        return ApiResponse.success(null, ResponseCode.COMMENT_DELETED.getMessage());
    }

    //댓글 수정
    @Operation(summary = "댓글 수정")
    @PutMapping("/update")
    public ApiResponse<Void> updateComment(@RequestHeader String accessToken, @RequestParam String id, @RequestBody String content) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(accessToken));
        commentService.updateComment(userId, Long.parseLong(id), content);
        return ApiResponse.success(null, ResponseCode.COMMENT_UPDATED.getMessage());
    }
}

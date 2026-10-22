package com.scheduleappdevelop.comment.controller;

import com.scheduleappdevelop.comment.dto.*;
import com.scheduleappdevelop.comment.service.CommentService;
import com.scheduleappdevelop.config.SessionUser;
import com.scheduleappdevelop.exception.UnauthorizedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CreateCommentResponse> createComment(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, @PathVariable Long scheduleId, @Valid @RequestBody CreateCommentRequest request){
        if(sessionUser == null){
            throw new UnauthorizedException("로그인이 필요합니다");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(sessionUser.getId(), scheduleId, request));
    }

    //댓글 조회 -> 로그인 필요 없음
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<GetCommentResponse> getComment(@PathVariable Long commentId){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getOne(commentId));
    }

    //전체 댓글 조회 -> 로그인 필요 없음
    @GetMapping("/comments")
    public ResponseEntity<List<GetCommentResponse>> getAllComments(){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAll(null));
    }

    //유저 댓글 전체 조회 (login 상태)
    @GetMapping("/comments/currentUser")
    public ResponseEntity<List<GetCommentResponse>> getAllComments(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser){
        if(sessionUser == null){
            throw new UnauthorizedException("로그인이 필요합니다");
        }
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAll(sessionUser.getId()));
    }

    //댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<UpdateCommentResponse> updateComment(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, @PathVariable Long commentId, @RequestBody UpdateCommentRequest request){
        if(sessionUser == null){
            throw new UnauthorizedException("로그인이 필요합니다");
        }
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(sessionUser.getId(), commentId, request));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, @PathVariable Long commentId){
        if(sessionUser == null){
            throw new UnauthorizedException("로그인이 필요합니다");
        }
        commentService.delete(sessionUser.getId(), commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

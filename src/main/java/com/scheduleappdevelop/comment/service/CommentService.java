package com.scheduleappdevelop.comment.service;

import com.scheduleappdevelop.comment.dto.*;
import com.scheduleappdevelop.comment.entity.Comment;
import com.scheduleappdevelop.comment.repository.CommentRepository;
import com.scheduleappdevelop.exception.EntityNotFoundException;
import com.scheduleappdevelop.exception.UnauthorizedException;
import com.scheduleappdevelop.schedule.entity.Schedule;
import com.scheduleappdevelop.schedule.repository.ScheduleRepository;
import com.scheduleappdevelop.user.entity.User;
import com.scheduleappdevelop.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateCommentResponse createComment(Long userId, Long scheduleId, @Valid CreateCommentRequest request) {
        //일정 탐색
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("일정이 없습니다"));

        //현재 유저 가져오기
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new EntityNotFoundException("유저가 없습니다"));

        //댓글 생성
        Comment comment = new Comment(request.getContent(), schedule, user);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponse(savedComment.getId(), savedComment.getContent(), user.getName(), savedComment.getCreatedAt(), savedComment.getModifiedAt());
    }

    @Transactional(readOnly = true)
    public GetCommentResponse getOne(Long commentId){
        //댓글 탐색
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId).orElseThrow(
                () -> new EntityNotFoundException("댓글이 없습니다"));

        return new GetCommentResponse(comment.getId(),comment.getContent(),comment.getUser().getName(), comment.getCreatedAt(), comment.getModifiedAt());
    }

    @Transactional(readOnly = true)
    public List<GetCommentResponse> getAll(Long userId) {
        //userId가 null이면 전체 조회,  null이 아니면 userId에 해당하는 댓글만 조회
        List<Comment> comments = commentRepository.findAllByUserId(userId);

        return comments.stream().map( comment -> new GetCommentResponse(comment.getId(),comment.getContent(),comment.getUser().getName(), comment.getCreatedAt(), comment.getModifiedAt())).toList();
    }

    @Transactional
    public UpdateCommentResponse updateComment(Long userId, Long commentId, UpdateCommentRequest request) {
        //댓글 탐색
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId).orElseThrow(
                () -> new EntityNotFoundException("댓글이 없습니다"));

        //본인이 작성한 댓글만 수정할 수 있음
        if(comment.getUser().getId() != userId){
            throw new UnauthorizedException("이 댓글을 수정할 권한이 없습니다");
        }

        comment.update(request.getContent());

        return new UpdateCommentResponse(comment.getId(), comment.getContent(), comment.getUser().getName(), comment.getCreatedAt(), comment.getModifiedAt());
    }


    @Transactional
    public void delete(Long userId, Long commentId) {
        //댓글 탐색
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId).orElseThrow(
                () -> new EntityNotFoundException("댓글이 없습니다"));

        //본인이 작성한 댓글만 삭제할 수 있음
        if(comment.getUser().getId() != userId){
            throw new UnauthorizedException("이 댓글을 삭제할 권한이 없습니다");
        }

        commentRepository.deleteById(commentId);
    }
}

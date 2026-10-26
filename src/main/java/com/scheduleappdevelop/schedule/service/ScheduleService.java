package com.scheduleappdevelop.schedule.service;

import com.scheduleappdevelop.comment.dto.GetCommentResponse;
import com.scheduleappdevelop.comment.entity.Comment;
import com.scheduleappdevelop.comment.repository.CommentRepository;
import com.scheduleappdevelop.exception.EntityNotFoundException;
import com.scheduleappdevelop.exception.UnauthorizedException;
import com.scheduleappdevelop.schedule.dto.*;
import com.scheduleappdevelop.schedule.entity.Schedule;
import com.scheduleappdevelop.schedule.repository.ScheduleRepository;
import com.scheduleappdevelop.user.entity.User;
import com.scheduleappdevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CreateScheduleResponse createSchedule(Long userId, CreateScheduleRequest request) {
        //User 탐색 + User가 soft delete 되어있는지 확인
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new EntityNotFoundException("없는 유저입니다"));

        Schedule schedule = new Schedule(request.getTitle(), request.getContent(), user);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new CreateScheduleResponse(savedSchedule.getId(), savedSchedule.getTitle(), savedSchedule.getContent(), savedSchedule.getUser().getName(), savedSchedule.getCreatedAt(),savedSchedule.getModifiedAt());
    }

    @Transactional(readOnly = true)
    public GetScheduleResponse getOne(Long scheduleId) {
        //일정 탐색
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("일정이 없습니다"));

        //댓글도 탐색
        List<Comment> comments = commentRepository.findAllByScheduleId(scheduleId);
        List<GetCommentResponse> dtos = comments.stream().map(
                comment -> new GetCommentResponse(comment.getId(),comment.getSchedule().getId(), comment.getContent(), comment.getUser().getName(), comment.getCreatedAt(), comment.getModifiedAt())).toList();

        return new GetScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().getName(), schedule.getCreatedAt(), schedule.getModifiedAt(), dtos);
    }

    @Transactional(readOnly = true)
    public List<GetSchedulesResponse> getCurrentAll(Long userId) {
        //userId에 해당하는 일정만 조회
        List<Schedule> schedules = scheduleRepository.findAllByActiveSchedule(userId);

        return schedules.stream().map( schedule -> new GetSchedulesResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().getName(), schedule.getCreatedAt(), schedule.getModifiedAt())).toList();
    }


    @Transactional(readOnly = true)
    public List<GetSchedulesResponse> getAll(String author) {
        //author이 포함되어 있으면 author 기준으로 전체 조회 (수정일 기준 정렬)
        List<Schedule> schedules = scheduleRepository.findAllByAuthor(author);

        return schedules.stream().map( schedule -> new GetSchedulesResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().getName(), schedule.getCreatedAt(), schedule.getModifiedAt())).toList();
    }

    @Transactional
    public UpdateScheduleResponse updateSchedule(Long scheduleId, Long userId, UpdateScheduleRequest request) {
        //일정 탐색
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("일정이 없습니다"));

        //로기인된 유저 -> 본인 일정만 수정 가능
        if(!Objects.equals(schedule.getUser().getId(), userId)){
            throw new UnauthorizedException("이 일정을 수정할 권한이 없습니다");
        }

        //유저 가져오기
        User user = schedule.getUser();

        schedule.update(request.getTitle(),request.getContent());
        return new UpdateScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), user.getName(), schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    @Transactional
    public void delete(Long scheduleId, Long userId) {
        //일정 탐색
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("일정이 없습니다"));

        //로기인된 유저 -> 본인 일정만 삭제 가능
        if(!Objects.equals(schedule.getUser().getId(), userId)){
            throw new UnauthorizedException("이 일정을 삭제할 권한이 없습니다");
        }

        //관련된 모든 댓글도 soft delete
        List<Comment> comments = commentRepository.findAllByScheduleId(scheduleId);
        for (Comment comment : comments) {
            comment.softDelete();
        }

        //soft delete
        schedule.softDelete();
    }

    @Transactional(readOnly = true)
    public Page<SchedulePageResponse> getSchedulesByPage(int page, int size) {

        //수정일 기준 내림차순으로 정렬 + soft Delete은 뒤로
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("modifiedAt")));

        return scheduleRepository.getSchedulesByPage(pageable);
    }
}

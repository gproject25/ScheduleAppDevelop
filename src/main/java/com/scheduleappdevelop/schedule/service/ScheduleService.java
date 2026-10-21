package com.scheduleappdevelop.schedule.service;

import com.scheduleappdevelop.exception.EntityNotFoundException;
import com.scheduleappdevelop.schedule.dto.*;
import com.scheduleappdevelop.schedule.entity.Schedule;
import com.scheduleappdevelop.schedule.repository.ScheduleRepository;
import com.scheduleappdevelop.user.entity.User;
import com.scheduleappdevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

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
        //일정 탐색 + soft delete 여부 확인
        Schedule schedule = scheduleRepository.findByIdAndActiveUser(scheduleId).orElseThrow(
                ()-> new EntityNotFoundException("없는 일정입니다"));

        return new GetScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().getName(), schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    @Transactional(readOnly = true)
    public List<GetScheduleResponse> getAll(Long userId) {
        //userId가 null이면 전체 조회,  null이 아니면 userId에 해당하는 일정만 조회
        List<Schedule> schedules = scheduleRepository.findAllByActiveUser(userId);

        return schedules.stream().map( schedule -> new GetScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().getName(), schedule.getCreatedAt(), schedule.getModifiedAt())).toList();
    }

    @Transactional
    public UpdateScheduleResponse updateSchedule(Long scheduleId, Long userId, UpdateScheduleRequest request) {
        //일정 존재 확인
        boolean existence = scheduleRepository.existsById(scheduleId);
        if(!existence){
            throw new EntityNotFoundException("일정이 없습니다");
        }

        //로기인된 유저 -> 본인 일정 탐색
        Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId,userId).orElseThrow(
                ()-> new IllegalStateException("이 일정을 수정할 권한이 없습니다"));

        //유저 가져오기
        User user = schedule.getUser();

        schedule.update(request.getTitle(),request.getContent());
        return new UpdateScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), user.getName(), schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    @Transactional
    public void delete(Long scheduleId, Long userId) {
        //일정 존재 확인
        boolean existence = scheduleRepository.existsById(scheduleId);
        if(!existence){
            throw new EntityNotFoundException("일정이 없습니다");
        }

        //로기인된 유저 -> 본인 일정 탐색
        Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId,userId).orElseThrow(
                ()-> new IllegalStateException("이 일정을 삭제할 권한이 없습니다"));

        //삭제
        scheduleRepository.delete(schedule);
    }
}

package com.scheduleappdevelop.schedule.service;

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
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new IllegalStateException("없는 유저입니다"));

        Schedule schedule = new Schedule(request.getTitle(), request.getContent(), user);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new CreateScheduleResponse(savedSchedule.getId(), savedSchedule.getTitle(), savedSchedule.getContent(), savedSchedule.getUser().getName(), savedSchedule.getCreatedAt(),savedSchedule.getModifiedAt());
    }

    @Transactional(readOnly = true)
    public GetScheduleResponse getOne(Long scheduleId) {
        //일정 탐색
        Schedule schedule = scheduleRepository.findByIdAndActiveUser(scheduleId).orElseThrow(
                ()-> new IllegalStateException("없는 일정입니다"));

        return new GetScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().getName(), schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    @Transactional(readOnly = true)
    public List<GetScheduleResponse> getAll(Long userId) {
        List<Schedule> schedules = scheduleRepository.findAllByActiveUser(userId);

        return schedules.stream().map( schedule -> new GetScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().getName(), schedule.getCreatedAt(), schedule.getModifiedAt())).toList();
    }

    @Transactional
    public UpdateScheduleResponse updateSchedule(Long scheduleId, Long userId, UpdateScheduleRequest request) {
        //로기인된 유저 -> 본인 일정 탐색
        Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId,userId).orElseThrow(
                ()-> new IllegalStateException("이 일정은 수정할 수 없습니다"));

        //유저 가져오기
        User user = schedule.getUser();

        schedule.update(request.getTitle(),request.getContent());
        return new UpdateScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), user.getName(), schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    @Transactional
    public void delete(Long scheduleId, Long userId) {
        //로기인된 유저 -> 본인 일정 탐색
        Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId,userId).orElseThrow(
                ()-> new IllegalStateException("이 일정은 삭제할 수 없습니다"));

        //삭제
        scheduleRepository.delete(schedule);
    }
}

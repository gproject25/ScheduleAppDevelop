package com.scheduleappdevelop.schedule.controller;

import com.scheduleappdevelop.schedule.dto.*;
import com.scheduleappdevelop.schedule.service.ScheduleService;
import com.scheduleappdevelop.config.SessionUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    //일정 생성
    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponse> createSchedule(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, @Valid @RequestBody CreateScheduleRequest request){
        if(sessionUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(sessionUser.getId(), request));
    }

    //단 건 조회 -> 로그인 필요 없음
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> getSchedule(@PathVariable Long scheduleId){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getOne(scheduleId));
    }

    //전체 일정 조회 -> 로그인 필요 없음
    @GetMapping("/schedules")
    public ResponseEntity<List<GetScheduleResponse>> getSchedules(){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getAll(null));
    }

    //유저 일정 전체 조회 (login 상태)
    @GetMapping("users/currentUser/schedules")
    public ResponseEntity<List<GetScheduleResponse>> getCurrentSchedules(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser){
        if(sessionUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getAll(sessionUser.getId()));
    }

    //일정 수정
    @PutMapping("/schedules")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, @Valid @RequestBody UpdateScheduleRequest request){
        if(sessionUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.updateSchedule(sessionUser.getId(), request));
    }

    //특정 일정 삭제
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, @PathVariable Long scheduleId){
        if(sessionUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        scheduleService.delete(scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

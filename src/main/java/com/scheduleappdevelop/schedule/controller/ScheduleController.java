package com.scheduleappdevelop.schedule.controller;

import com.scheduleappdevelop.schedule.dto.*;
import com.scheduleappdevelop.schedule.service.ScheduleService;
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

    @PostMapping("/users/{userId}/schedules")
    public ResponseEntity<CreateScheduleResponse> createSchedule(@PathVariable Long userId, @Valid @RequestBody CreateScheduleRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(userId, request));
    }

    @GetMapping("/users/{userId}/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> getSchedule(@PathVariable Long scheduleId){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getOne(scheduleId));
    }

    @GetMapping("/users/{userId}/schedules")
    public ResponseEntity<List<GetScheduleResponse>> getSchedules(){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getAll());
    }

    @PutMapping("/users/{userId}/schedules/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(@PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.updateSchedule(scheduleId, request));
    }

    @DeleteMapping("/users/{userId}/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId){
        scheduleService.delete(scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

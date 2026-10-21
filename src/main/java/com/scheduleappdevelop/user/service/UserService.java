package com.scheduleappdevelop.user.service;

import com.scheduleappdevelop.schedule.dto.GetScheduleResponse;
import com.scheduleappdevelop.schedule.entity.Schedule;
import com.scheduleappdevelop.schedule.repository.ScheduleRepository;
import com.scheduleappdevelop.user.dto.*;
import com.scheduleappdevelop.user.entity.User;
import com.scheduleappdevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        User user = new User(request.getName(),request.getEmail());
        User savedUser = userRepository.save(user);
        return new CreateUserResponse(savedUser.getId(),savedUser.getName(), savedUser.getEmail(), savedUser.getCreatedAt(),savedUser.getModifiedAt());
    }

    @Transactional(readOnly = true)
    public List<GetUsersResponse> getAll() {
        List<User> users = userRepository.findAllByDeletedFalse();

        return users.stream().map(user -> new GetUsersResponse(user.getId(),user.getName(), user.getEmail(),user.getCreatedAt(),user.getModifiedAt())).toList();
    }

    @Transactional(readOnly = true)
    public GetUserResponse getOne(Long userId) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new IllegalStateException("유저가 없습니다"));
        List<Schedule> schedules = scheduleRepository.findSchedulesByUserId(userId);
        List<GetScheduleResponse> dtos= schedules.stream().map(schedule -> new GetScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(),user.getName(), schedule.getCreatedAt(), schedule.getModifiedAt())).toList();

        return new GetUserResponse(user.getId(),user.getName(), user.getEmail(),user.getCreatedAt(),user.getModifiedAt(), dtos);
    }

    @Transactional
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new IllegalStateException("유저가 없습니다"));

        user.update(request.getName(), request.getEmail());
        return new UpdateUserResponse(user.getId(),user.getName(), user.getEmail(),user.getCreatedAt(),user.getModifiedAt());
    }

    @Transactional
    public void delete(Long userId) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new IllegalStateException("유저가 없습니다"));

        //dirt checking soft delete
        user.softDelete();

        //userRepository.deleteById(userId);
    }
}

package com.scheduleappdevelop.user.service;

import com.scheduleappdevelop.comment.entity.Comment;
import com.scheduleappdevelop.comment.repository.CommentRepository;
import com.scheduleappdevelop.config.PasswordEncoder;
import com.scheduleappdevelop.exception.EntityNotFoundException;
import com.scheduleappdevelop.schedule.dto.GetScheduleResponse;
import com.scheduleappdevelop.schedule.dto.GetSchedulesResponse;
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
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        //이메일 중복 검증
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        //비밀번호 암호화
        String encodePassword = passwordEncoder.encode(request.getPassword());

        //유저 저장
        User user = new User(request.getName(),request.getEmail(), encodePassword);
        User savedUser = userRepository.save(user);
        return new CreateUserResponse(savedUser.getId(),savedUser.getName(), savedUser.getEmail(), savedUser.getCreatedAt(),savedUser.getModifiedAt());
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request){
        //이메일 존재 확인
        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail()).orElseThrow(
                () -> new IllegalStateException("이메일이 올바르지 않습니다"));

        //비밀번호 검증
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        //로그인 성공
        return new LoginResponse(user.getId(),user.getName(), user.getEmail());
    }

    @Transactional(readOnly = true)
    public List<GetUsersResponse> getAll() {
        //soft Delete 안되어 있는 유저들 전체 조회
        List<User> users = userRepository.findAllByDeletedFalse();

        return users.stream().map(user -> new GetUsersResponse(user.getId(),user.getName(), user.getEmail(),user.getCreatedAt(),user.getModifiedAt())).toList();
    }

    @Transactional(readOnly = true)
    public GetUserResponse getOne(Long userId) {
        //soft Delete 안되어있는 유저 조회
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new EntityNotFoundException("유저가 없습니다"));

        //일정도 탐색
        List<Schedule> schedules = scheduleRepository.findAllByActiveSchedule(userId);
        List<GetSchedulesResponse> dtos= schedules.stream().map(schedule -> new GetSchedulesResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(),user.getName(), schedule.getCreatedAt(), schedule.getModifiedAt())).toList();

        return new GetUserResponse(user.getId(),user.getName(), user.getEmail(),user.getCreatedAt(),user.getModifiedAt(), dtos);
    }

    @Transactional
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest request) {
        //중복 이메일 검증
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        //soft Delete 안되어있는 유저 조회
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new EntityNotFoundException("유저가 없습니다"));

        //dirty check update
        user.update(request.getName(), request.getEmail());
        return new UpdateUserResponse(user.getId(),user.getName(), user.getEmail(),user.getCreatedAt(),user.getModifiedAt());
    }

    @Transactional
    public void delete(Long userId) {
        //Id 탐색 + soft delete이 안된 경우를 검증
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new EntityNotFoundException("유저가 없습니다"));

        //dirty check방식으로 soft delete

        //관련된 일정 + 댓글 soft delete
        List<Schedule> schedules = scheduleRepository.findAllByActiveSchedule(userId);
        for (Schedule schedule : schedules) {

            List<Comment> comments = commentRepository.findAllByScheduleId(schedule.getId());
            for (Comment comment : comments) {
                comment.softDelete();
            }
            schedule.softDelete();
        }

        user.softDelete();
    }
}

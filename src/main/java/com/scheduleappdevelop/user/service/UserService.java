package com.scheduleappdevelop.user.service;

import com.scheduleappdevelop.user.dto.*;
import com.scheduleappdevelop.user.entity.User;
import com.scheduleappdevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        User user = new User(request.getEmail());
        User savedUser = userRepository.save(user);
        return new CreateUserResponse(savedUser.getId(),savedUser.getEmail(), savedUser.getCreatedAt(),savedUser.getModifiedAt());
    }

    @Transactional(readOnly = true)
    public List<GetUsersResponse> getAll() {
        List<User> users = userRepository.findAll();
        List<GetUsersResponse> dtos = new ArrayList<>();
        return users.stream().map(user -> new GetUsersResponse(user.getId(),user.getEmail(),user.getCreatedAt(),user.getModifiedAt())).toList();
    }

    @Transactional(readOnly = true)
    public GetUserResponse getOne(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("유저가 없습니다"));

        return new GetUserResponse(user.getId(),user.getEmail(),user.getCreatedAt(),user.getModifiedAt());
    }

    @Transactional
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("유저가 없습니다"));

        user.update(request.getEmail());
        return new UpdateUserResponse(user.getId(),user.getEmail(),user.getCreatedAt(),user.getModifiedAt());
    }

    @Transactional
    public void delete(Long userId) {
        boolean existence = userRepository.existsById(userId);
        if(!existence){
            throw new IllegalStateException("유저가 없습니다");
        }
        userRepository.deleteById(userId);
    }
}

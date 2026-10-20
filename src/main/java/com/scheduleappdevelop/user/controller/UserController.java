package com.scheduleappdevelop.user.controller;

import com.scheduleappdevelop.user.dto.*;
import com.scheduleappdevelop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/users")
    public ResponseEntity<List<GetUsersResponse>> getUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOne(userId));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userId, request));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

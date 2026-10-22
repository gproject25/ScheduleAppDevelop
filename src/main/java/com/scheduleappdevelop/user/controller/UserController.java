package com.scheduleappdevelop.user.controller;

import com.scheduleappdevelop.config.SessionUser;
import com.scheduleappdevelop.exception.UnauthorizedException;
import com.scheduleappdevelop.user.dto.*;
import com.scheduleappdevelop.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<CreateUserResponse> signup(@Valid @RequestBody CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = userService.login(request);

        SessionUser sessionUser = new SessionUser(response.getId(), response.getName(), response.getEmail());
        session.setAttribute("loginUser", sessionUser);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, HttpSession session) {
        if(sessionUser == null){
            return ResponseEntity.badRequest().build();
        }
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //전체 조회 -> 로그인 필요 없음
    @GetMapping("/users")
    public ResponseEntity<List<GetUsersResponse>> getUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    //단 건 조회  -> 로그인 필요 없음
    @GetMapping("/users/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOne(userId));
    }
    //단 건 조회 (현재 login 된 유저)
    @GetMapping("/users/currentUser")
    public ResponseEntity<GetUserResponse> getCurrentUser(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser){
        if(sessionUser == null){
            throw new UnauthorizedException("로그인이 필요합니다");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOne(sessionUser.getId()));
    }

    //수정
    @PutMapping("/users")
    public ResponseEntity<UpdateUserResponse> updateUser(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, @Valid @RequestBody UpdateUserRequest request){
        if(sessionUser == null){
            throw new UnauthorizedException("로그인이 필요합니다");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(sessionUser.getId(), request));
    }

    //삭제 (삭제시 자동 로그아웃)
    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, HttpSession session){
        if(sessionUser == null){
            throw new UnauthorizedException("로그인이 필요합니다");
        }
        userService.delete(sessionUser.getId());
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

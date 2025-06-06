package com.campus.boardserver.boardserver.controller;

import com.campus.boardserver.boardserver.aop.LoginCheck;
import com.campus.boardserver.boardserver.dto.UserDTO;
import com.campus.boardserver.boardserver.dto.request.UserDeleteId;
import com.campus.boardserver.boardserver.dto.request.UserLoginRequest;
import com.campus.boardserver.boardserver.dto.request.UserUpdatePasswordRequest;
import com.campus.boardserver.boardserver.dto.response.LoginResponse;
import com.campus.boardserver.boardserver.dto.response.UserInfoResponse;
import com.campus.boardserver.boardserver.service.UserService;
import com.campus.boardserver.boardserver.service.UserServiceImpl;
import com.campus.boardserver.boardserver.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

    private final UserServiceImpl userService;
    private static LoginResponse loginResponse;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDTO userDTO) {
        if(UserDTO.hasNullDateBeforeRegister(userDTO)) {
            throw new RuntimeException("회원가입 정보를 확인해주세요");
        }
        userService.register(userDTO);
    }

    @PostMapping("sign-in")
    public HttpStatus login(@RequestBody UserLoginRequest userLoginRequest,
                            HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = userLoginRequest.getUserId();
        String password = userLoginRequest.getPassword();
        LoginResponse loginResponse = null;
        UserDTO userInfo = userService.login(id, password);

        if(userInfo == null) {
            return HttpStatus.NOT_FOUND;
        } else if(userInfo != null) {
            loginResponse = LoginResponse.success(userInfo);
            if(userInfo.getStatus() == (UserDTO.Status.ADMIN))
                SessionUtil.setLoginAdminId(session, id);
            else {
                SessionUtil.setLoginMemberId(session, id);
            }

            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            throw new RuntimeException("Login Error! 유저 정보가 없거나 지원되지 않는 유저입니다.");
        }

        return HttpStatus.OK;
    }

    @GetMapping("my-info")
    public UserInfoResponse memberInfo(HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);
        if(id==null) id = SessionUtil.getLoginAdminId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        return new UserInfoResponse(memberInfo);
    }

    @PostMapping("logout")
    public void logout(HttpSession session) {
        SessionUtil.clear(session);
    }

    @PatchMapping("password")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<LoginResponse> updateUserPassword(String accountId, @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
                                                            HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = accountId;
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            userService.updatePassowrd(id, beforePassword, afterPassword);

            ResponseEntity.ok(new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK));
        } catch(IllegalArgumentException e) {
            log.error("updatePassword 실패", e);
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteId userDeleteId,
                                                  HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = SessionUtil.getLoginMemberId(session);

        try {
            userService.deleteId(id, userDeleteId.getPassword());
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch(RuntimeException e) {
            log.error("deleteId 실패");
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}

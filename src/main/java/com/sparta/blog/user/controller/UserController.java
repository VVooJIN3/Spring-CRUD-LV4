package com.sparta.blog.user.controller;

import com.sparta.blog.common.dto.ApiResponseDto;
import com.sparta.blog.user.dto.SignupRequestDto;
import com.sparta.blog.user.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //예외처리 메서드
    //컨트롤러 내 API가 호출되다가 Exception 발생 시, 코드 실행
    @ExceptionHandler
    public ResponseEntity<ApiResponseDto> handleException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(ex.getMessage()));
    }

    @GetMapping("/user/login-page")
    public String loginPage() {
        return "로그인 페이지";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "회원가입 페이지";
    }

    @PostMapping("/user/signup")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
//        //validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();

        //validation 예외가 1건 이상인 경우
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                errorMessage.append(fieldError.getDefaultMessage()).append(" ");
            }
            throw new IllegalArgumentException(errorMessage.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.signup(requestDto));
    }

}

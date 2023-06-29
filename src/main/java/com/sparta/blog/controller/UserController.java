package com.sparta.blog.controller;

import com.sparta.blog.dto.SignupRequestDto;
import com.sparta.blog.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
//        //validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                errorMessage.append(fieldError.getDefaultMessage()).append("\n");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
        }
        String result = userService.signup(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

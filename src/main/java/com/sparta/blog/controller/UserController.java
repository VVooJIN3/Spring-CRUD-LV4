package com.sparta.blog.controller;

import com.sparta.blog.dto.SignupRequestDto;
import com.sparta.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user/signup")
    @ResponseBody
    public String signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {

//        //validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/user/signup";
        }
        userService.signup(requestDto);

//        return "redirect:/api/user/login-page";
        return "회원가입 완료";
    }
}

package com.sparta.blog.user.service;

import com.sparta.blog.common.dto.ApiResponseDto;
import com.sparta.blog.common.jwt.JwtUtil;
import com.sparta.blog.user.dto.SignupRequestDto;
import com.sparta.blog.user.entity.User;
import com.sparta.blog.user.entity.UserRoleEnum;
import com.sparta.blog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService {
    //(07.25)Service 클래스를 인터페이스와 구현체로 분리하고, 인터페이스 메서드에 주석달기

    /* 회원가입
     * @param requestDto 회원가입 요청 정보
     */
    ApiResponseDto signup(SignupRequestDto requestDto);

}

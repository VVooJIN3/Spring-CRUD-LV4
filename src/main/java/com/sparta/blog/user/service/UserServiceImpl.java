package com.sparta.blog.user.service;

import com.sparta.blog.common.dto.ApiResponseDto;
import com.sparta.blog.common.jwt.JwtUtil;
import com.sparta.blog.user.dto.SignupRequestDto;
import com.sparta.blog.user.entity.User;
import com.sparta.blog.user.entity.UserRoleEnum;
import com.sparta.blog.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ApiResponseDto signup(SignupRequestDto requestDto) {

        //회원 중복 확인
        //DB에 이미 존재하는 username으로 회원가입을 요청한 경우
        // "중복된 username 입니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
        Optional<User> checkUsername = userRepository.findByUsername(requestDto.getUsername());
        if (checkUsername.isPresent())
            throw new IllegalArgumentException("중복된 username입니다.");

        Optional<User> checkEmail = userRepository.findByEmail(requestDto.getEmail());
        if (checkEmail.isPresent())
            throw new IllegalArgumentException("중복된 Email입니다.");

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        String password = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto.getUsername(), password, requestDto.getEmail(), role);
        userRepository.save(user);
        return new ApiResponseDto("회원가입 완료");
    }
}

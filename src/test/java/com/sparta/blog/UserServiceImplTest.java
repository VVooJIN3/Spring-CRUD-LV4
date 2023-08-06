package com.sparta.blog;

import com.sparta.blog.common.dto.ApiResponseDto;
import com.sparta.blog.user.dto.SignupRequestDto;
import com.sparta.blog.user.entity.User;
import com.sparta.blog.user.repository.UserRepository;
import com.sparta.blog.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Signup Test")
    @Test
    void signupTest() {
        // given
//        var newUser = User.builder().username("user1").password("password1").email("test1@gmail.com").role(UserRoleEnum.USER).build();
        String username = "user1";
        String password = passwordEncoder.encode("password1");
        String email = "test1@gmail.com";
        SignupRequestDto signupRequestDto = new SignupRequestDto(username, password, email);
        Optional<User> existingUser = userRepository.findByUserId(Long.valueOf(1));

        given(userRepository.findByUsername(username)).willReturn(Optional.empty()); // findByUsername 메소드가 빈 Optional을 반환하도록 설정.
        given(userRepository.findByEmail(email)).willReturn(Optional.empty()); // findByEmail 메소드가 빈 Optional을 반환하도록 설정.

        // when
        ApiResponseDto response = userService.signup(signupRequestDto);

        // then
        assertEquals("회원가입 완료", response.getMessage());
    }
}
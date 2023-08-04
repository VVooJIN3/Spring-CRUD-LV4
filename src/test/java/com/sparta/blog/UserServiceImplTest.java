package com.sparta.blog;

import com.sparta.blog.user.dto.SignupRequestDto;
import com.sparta.blog.user.entity.User;
import com.sparta.blog.user.entity.UserRoleEnum;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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

    @DisplayName("insertUser Test")
    @Test
    void insertUser_ValidUser_Success() {
        // given
        var newUser = User.builder().username("user1").password("password1").email("test1@gmail.com").role(UserRoleEnum.USER).build();
        given(userRepository.findByUsername(eq(newUser.getUsername()))).willReturn(Optional.empty()); // findById 메소드가 newUser.getId()로 호출될 때, 빈 Optional을 반환하도록 설정.
        given(userRepository.save(any())).willReturn(newUser); //save메소드가 어떤 User객체로 호출되든, 주어진 newUser객체를 반환하도록 설정

        SignupRequestDto signupRequestDto = new SignupRequestDto(newUser);
        // when
        userService.signup(signupRequestDto);

        // then
        then(userRepository).should(times(1)).save(newUser);
    }
}

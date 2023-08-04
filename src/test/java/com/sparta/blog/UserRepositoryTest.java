package com.sparta.blog;

import com.sparta.blog.common.config.JPAConfiguration;
import com.sparta.blog.user.entity.User;
import com.sparta.blog.user.entity.UserRoleEnum;
import com.sparta.blog.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JPAConfiguration.class)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void dynamicInsertTest() {
        // given
        var newUser = User.builder().username("user").password("password").email("test@gmail.com").role(UserRoleEnum.USER).build();

        // when
        var savedUser = userRepository.save(newUser);

        // then
        assertThat(savedUser).isNotNull();
    }

    @Test
    void dynamicUpdateTest() {
        // given
        var newUser = User.builder().username("user").password("password").email("test@gmail.com").role(UserRoleEnum.USER).build();
        userRepository.save(newUser);
        var newPassword = "new password";

        // when
        newUser.setPassword(newPassword);
        var savedUser = userRepository.save(newUser);

        // then
        assertThat(savedUser.getPassword()).isEqualTo(newPassword);
    }
}
package com.sparta.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.blog.blog.dto.BlogRequestDto;
import com.sparta.blog.blog.repository.BlogRepository;
import com.sparta.blog.blog.service.BlogService;
import com.sparta.blog.common.security.UserDetailsImpl;
import com.sparta.blog.user.entity.User;
import com.sparta.blog.user.entity.UserRoleEnum;
import com.sparta.blog.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BlogControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BlogRepository blogRepository;

    @MockBean
    private BlogService blogService;

    private static final String BASE_URL = "/api";

    @Test
    @DisplayName("게시글 추가 테스트")
    public void testAddPost() throws Exception {
        //given
        String title = "test title";
        String contents = "Test contents";
        var newUser = User.builder().username("user").password("password").email("test@gmail.com").role(UserRoleEnum.USER).build();
        userRepository.save(newUser);
        UserDetailsImpl userDetails = new UserDetailsImpl(newUser);//UserDetailsImpl 객체 생성
        Principal mockPrincipal = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        // when

        String body = mapper.writeValueAsString(
                BlogRequestDto.builder().title(title).contents(contents).build()
        );
        // then
        mvc.perform(post(BASE_URL + "/blogs")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));


    }
}

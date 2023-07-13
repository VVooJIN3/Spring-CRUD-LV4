package com.sparta.blog.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.blog.dto.LoginRequestDto;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    //POST "api/user/login"을 로그인 인증필터의 url으로 설정
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }


    //로그인 시도 시 동작하는 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //requestBody 요청 값을 LoginRequestDto 객체타입으로 변환
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            //아이디, 비밀번호 입력내용을 체크.
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    // 인증 성공(로그인 성공) 시 동작하는 메서드
    // JWT 생성 및 client에 응답
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String userId = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        //사용자 ID/권한으로 JWT 생성
        String token = jwtUtil.createToken(userId, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); //헤더에 Authorztion = Bearer {JWT} 추가

        // 응답 데이터 생성
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true).writeValueAsString("로그인 성공");
        // 응답 전송
        response.getWriter().write(responseBody);
        response.getWriter().flush();
        response.getWriter().close();
    }

    //인증 실패(로그인 실패)시 동작하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException{
        response.setStatus(400);

        // 응답 데이터 생성
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true).writeValueAsString("로그인 실패");
        // 응답 전송
        response.getWriter().write(responseBody);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
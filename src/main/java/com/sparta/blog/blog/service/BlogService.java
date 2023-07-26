package com.sparta.blog.blog.service;

import com.sparta.blog.blog.dto.BlogRequestDto;
import com.sparta.blog.blog.dto.BlogResponseDto;
import com.sparta.blog.blog.entity.Blog;
import com.sparta.blog.blog.entity.BlogLike;
import com.sparta.blog.common.dto.ApiResponseDto;
import com.sparta.blog.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogService {


    //(07.25)Service 클래스를 인터페이스와 구현체로 분리하고, 인터페이스 메서드에 주석달기

    /*게시글 조회
     * @return 전체 게시글 List
     */
    public List<BlogResponseDto> getBlogs();


    /*게시글 생성
    * @param requestDto 게시글 생성 요청정보
    * @param user 게시글 생성 요청자
    * @return 게시글 생성 결과
     */
    @Transactional
    BlogResponseDto createBlog(BlogRequestDto requestDto, User user) ;
//    {
//        // DB에 존재하는 유저인지 검증
//        User loginedUser = findUser(user.getUsername());
//        Blog blog = new Blog(requestDto, loginedUser);
//        blogRepository.save(blog);
//        return new BlogResponseDto(blog);
//    }

    /*게시글 수정
     * @param id 수정할 게시글 번호
     * @param requestDto 게시글 수정 요청정보
     * @param user 게시글 수정 요청자
     * @return 수정된 게시글
     */
    @Transactional
    BlogResponseDto updateBlog(Long id, BlogRequestDto requestDto, User user);

    /*게시글 삭제
     * @param id 삭제할 게시글 번호
     * @param user 게시글 삭제 요청자
     * @return 삭제 여부 답변 문자열
     */
    @Transactional
    ApiResponseDto deleteBlog(Long id, User user);


    /* 좋아요 이벤트
     * @param id 게시글 번호
     * @param user 좋아요/취소 요청자
     * @param requestMethod 메소드 정보 "POST", "DELETE" 여부 확인
     * @return 삭제 여부 답변 문자열
     */
    @Transactional
    ApiResponseDto likeEvent(Long id, User user, String requestMethod) ;


    /* 좋아요 증가
     * @param blog 게시글
     * @param user 요청자
     */
    @Transactional
    void increaseLike(Blog blog, User user);

    /* 좋아요 취소
     * @param blog 게시글
     * @param user 요청자
     */
    @Transactional
     void decreaseLike(Blog blog, BlogLike blogLike);

    BlogResponseDto getBlog(Long id);

    /* 게시글 찾기
     * @param id 게시글번호
     */
    Blog findBlog(Long id);

    /* 게시글 찾기
     * @param username 아이디
     */
    User findUser(String username);

    /* 게시글 좋아요 찾기
     * @param blog 대상 게시글
     * @param user 아이디
     */
    BlogLike findBlogLike(Blog blog, User user);
}
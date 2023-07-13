package com.sparta.blog.service;

import com.sparta.blog.dto.*;
import com.sparta.blog.entity.Reply;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.repository.ReplyRepository;
import com.sparta.blog.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public BlogService(BlogRepository blogRepository, ReplyRepository replyRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    public List<BlogResponseDto> getBlogs() {
        return blogRepository.findAllByOrderByModifiedAtDesc().stream().map(BlogResponseDto::new).toList();
    }

    @Transactional
    public BlogResponseDto createBlog(BlogRequestDto requestDto, User user) {
        // DB에 존재하는 유저인지 검증
        User loginedUser = findUser(user.getUsername());

        Blog blog = new Blog(requestDto, loginedUser);
        blogRepository.save(blog);
        return new BlogResponseDto(blog);

    }

    @Transactional
    public BlogResponseDto updateBlog(Long id, BlogRequestDto requestDto, User user) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);

        //DB에 존재하는 유저인지 + 게시글 작성자와 일치여부 확인
        User loginedUser = findUser(user.getUsername());

        // 사용자가 작성한 게시글이 아닌 경우에는
        // “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 Client에 반환하기
        if (!loginedUser.getUsername().equals(blog.getUsername()))
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");

        blog.update(requestDto);
        return new BlogResponseDto(blog);

    }

    @Transactional
    public ApiResponseDto deleteBlog(Long id, User user) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);
        // DB에 존재하는 유저인지 확인
        User loginedUser = findUser(user.getUsername());
        //게시글 작성자와 일치여부 확인
        // 사용자가 작성한 게시글이 아닌 경우에는
        // “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 Client에 반환하기
        if (!loginedUser.getUsername().equals(blog.getUsername()))
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");

        blogRepository.delete(blog);
        return new ApiResponseDto("선택한 게시글의 삭제를 완료했습니다.");
    }

    public BlogResponseDto getBlog(Long id) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);
        return new BlogResponseDto(blog);
    }

    private Blog findBlog(Long id) {
        return blogRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );

    }

}
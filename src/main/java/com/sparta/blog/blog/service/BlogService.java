package com.sparta.blog.blog.service;

import com.sparta.blog.blog.dto.BlogRequestDto;
import com.sparta.blog.blog.dto.BlogResponseDto;
import com.sparta.blog.blog.entity.Blog;
import com.sparta.blog.blog.entity.BlogLike;
import com.sparta.blog.blog.repository.BlogLikeRepository;
import com.sparta.blog.blog.repository.BlogRepository;
import com.sparta.blog.common.dto.ApiResponseDto;
import com.sparta.blog.reply.repository.ReplyRepository;
import com.sparta.blog.user.entity.User;
import com.sparta.blog.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final BlogLikeRepository blogLikeRepository;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public BlogService(BlogRepository blogRepository, ReplyRepository replyRepository, UserRepository userRepository, BlogLikeRepository blogLikeRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.blogLikeRepository =blogLikeRepository;
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


    @Transactional
    public ApiResponseDto likeEvent(Long id, User user, String requestMethod) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);
        // DB에 존재하는 유저인지 확인
        User loginedUser = findUser(user.getUsername());

//        if(blog.getUsername().equals(loginedUser.getUsername()))
//            throw new IllegalArgumentException("자신이 작성한 게시글에는 좋아요를 할 수 없습니다.");

        // 사용자가 좋아요를 눌렀는지 확인
        BlogLike blogLike = findBlogLike(blog, loginedUser);

        //요청메서드가 POST일 때
        if (requestMethod.equals("POST")) {
            //유저가 게시글에 좋아요를 누르지 않은 상태의 경우
            if (blogLike == null) {
                increaseLike(blog, loginedUser);
                return new ApiResponseDto("좋아요를 눌렀습니다. 좋아요 수 : " + blog.getLikeCnt());
            }
            //이미 유저가 해당 게시글에 좋아요를 누른 상태인 경우
            else {
                throw new IllegalArgumentException("이미 좋아요를 누르셨습니다. 좋아요 수 : " + blog.getLikeCnt());
            }
        }
        //요청 메서드가 DELETE일 때
        else {//else if (requestMethod.equals("DELETE")) {
            //유저가 게시글에 좋아요를 누르지 않은 상태의 경우
            if (blogLike == null) {
                throw new IllegalArgumentException("좋아요를 누르지 않은 상태입니다. 좋아요 수 : " + blog.getLikeCnt());
            }
            //이미 유저가 해당 게시글에 좋아요를 누른 상태인 경우
            else {
                decreaseLike(blog, blogLike);
                return new ApiResponseDto("좋아요룰 취소했습니다. 좋아요 수 : " + blog.getLikeCnt());
            }
        }
    }

    @Transactional
    public void increaseLike(Blog blog, User user){
        blog.increaseLike();
        BlogLike blogLike = new BlogLike(blog, user);
        blogLikeRepository.save(blogLike);
    }

    @Transactional
    public void decreaseLike(Blog blog, BlogLike blogLike){
        blog.decreaseLike();
        blogLikeRepository.delete(blogLike);
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
    private BlogLike findBlogLike(Blog blog, User user){
        return blogLikeRepository.findByBlogAndUser(blog,user).orElse(null);
    }

}
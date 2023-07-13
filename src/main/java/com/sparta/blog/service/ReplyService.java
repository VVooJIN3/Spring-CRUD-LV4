package com.sparta.blog.service;

import com.sparta.blog.dto.ApiResponseDto;
import com.sparta.blog.dto.ReplyRequestDto;
import com.sparta.blog.dto.ReplyResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Reply;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.repository.ReplyRepository;
import com.sparta.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final BlogRepository blogRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;


    @Transactional
    public ReplyResponseDto addReply(ReplyRequestDto requestDto, User user, Long blogId) {
        Blog blog = findBlog(blogId);
        User loginedUser = findUser(user.getUsername());

        Reply reply = new Reply(requestDto, loginedUser, blog);
        Reply saveReply = replyRepository.save(reply);
        return new ReplyResponseDto(reply);

    }

    public List<ReplyResponseDto> getReplies(Long blogId) {
        Blog blog = findBlog(blogId);
        return replyRepository.findAllByOrderByModifiedAtDesc().stream().map(ReplyResponseDto::new).toList();
    }

    @Transactional
    public ReplyResponseDto updateReply(Long blogId, ReplyRequestDto requestDto, User user, Long replyId) {
        //존재하는 게시글/댓글/유저인지 확인 후 결과값 찾아냄
        Blog blog = findBlog(blogId);
        Reply reply = findReply(replyId);
        User loginedUser = findUser(user.getUsername());

        //사용자가 작성한 댓글이 아닌 경우
        if (!loginedUser.getUsername().equals(reply.getUsername()))
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");

        reply.update(requestDto);
        return new ReplyResponseDto(reply);

    }

    @Transactional
    public ApiResponseDto deleteReply(Long blogId, User user, Long replyId) {
        //존재하는 게시글/댓글/유저인지 확인 후 결과값 찾아냄
        Blog blog = findBlog(blogId);
        Reply reply = findReply(replyId);
        User loginedUser = findUser(user.getUsername());

        if (!user.getUsername().equals(reply.getUsername()))
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");

        replyRepository.delete(reply);
        return new ApiResponseDto(blogId + "번 게시물의" + replyId + "번째 댓글 삭제 완료");

    }

    private Reply findReply(Long id) {
        return replyRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
        );
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

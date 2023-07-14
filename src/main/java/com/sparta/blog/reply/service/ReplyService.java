package com.sparta.blog.reply.service;

import com.sparta.blog.blog.entity.Blog;
import com.sparta.blog.blog.repository.BlogRepository;
import com.sparta.blog.common.dto.ApiResponseDto;
import com.sparta.blog.reply.dto.ReplyRequestDto;
import com.sparta.blog.reply.dto.ReplyResponseDto;
import com.sparta.blog.reply.entity.Reply;
import com.sparta.blog.reply.entity.ReplyLike;
import com.sparta.blog.reply.repository.ReplyLikeRepository;
import com.sparta.blog.reply.repository.ReplyRepository;
import com.sparta.blog.user.entity.User;
import com.sparta.blog.user.repository.UserRepository;
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
    private final ReplyLikeRepository replyLikeRepository;

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

    @Transactional
    public ApiResponseDto likeEvent(Long id, User user, String requestMethod) {
        // 해당 댓글이 DB에 존재하는지 확인
        Reply reply = findReply(id);
        // DB에 존재하는 유저인지 확인
        User loginedUser = findUser(user.getUsername());
        // 사용자가 좋아요를 눌렀는지 확인
        ReplyLike replyLike = findReplyLike(reply, loginedUser);

        //요청메서드가 POST일 때
        if (requestMethod.equals("POST")) {
            //유저가 댓글에 좋아요를 누르지 않은 상태의 경우
            if (replyLike == null) {
                increaseLike(reply, loginedUser);
                return new ApiResponseDto("댓글에 좋아요를 눌렀습니다. 좋아요 수 : " + reply.getLikeCnt());
            }
            //이미 유저가 해당 게시글에 좋아요를 누른 상태인 경우
            else {
                throw new IllegalArgumentException("이미 좋아요를 누르셨습니다. 좋아요 수 : " + reply.getLikeCnt());
            }
        } else {//else if (requestMethod.equals("DELETE")) {//유저가 댓글에 좋아요를 누르지 않은 상태의 경우
            if (replyLike == null) {
                throw new IllegalArgumentException("좋아요를 누르지 않은 상태입니다. 좋아요 수 : " + reply.getLikeCnt());
            }
            //이미 유저가 해당 게시글에 좋아요를 누른 상태인 경우
            else {
                decreaseLike(reply, replyLike);
                return new ApiResponseDto("이미 누른 좋아요룰 취소했습니다. 좋아요 수 : " + reply.getLikeCnt());
            }
        }
    }

    @Transactional
    public void increaseLike(Reply reply, User user) {
        reply.increaseLike();
        replyLikeRepository.save(new ReplyLike(reply, user));
    }

    @Transactional
    public void decreaseLike(Reply reply, ReplyLike replyLike) {
        reply.decreaseLike();
        replyLikeRepository.delete(replyLike);
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

    private ReplyLike findReplyLike(Reply reply, User user) {
        return replyLikeRepository.findByReplyAndUser(reply, user).orElse(null);
    }


}

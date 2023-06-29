package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.dto.ReplyRequestDto;
import com.sparta.blog.dto.ReplyResponseDto;
import com.sparta.blog.entity.Reply;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.repository.ReplyRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BlogService {
    private final BlogRepository blogRepository;
    private final ReplyRepository replyRepository;
    public BlogService(BlogRepository blogRepository,ReplyRepository replyRepository ) {
        this.blogRepository = blogRepository;
        this.replyRepository = replyRepository;
    }

    public List<BlogResponseDto> getBlogs() {
        return blogRepository.findAllByOrderByModifiedAtDesc().stream().map(BlogResponseDto::new).toList();
    }

    public Blog createBlog(BlogRequestDto requestDto, User user) {
        Blog blog = new Blog(requestDto, user);
        blogRepository.save(blog);
        return blog;
    }

    @Transactional
    public Blog updateBlog(Long id, BlogRequestDto requestDto, User user) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);
        if (blog != null) {
            if(user.getUsername().equals(blog.getUsername())) {
                blog.update(requestDto);
                return  blog;
            }
            else{
                throw new IllegalArgumentException("로그인한 작성자의 게시물이 아닙니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다. 수정할 수 없습니다.");
        }
    }

    public Long deleteBlog(Long id, User user) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog blog =findBlog(id);
        if (blog != null) {
            if(user.getUsername().equals(blog.getUsername())) {
                blogRepository.delete(blog);
                return id;
            }else{
                throw new IllegalArgumentException("로그인한 작성자의 게시물이 아닙니다. 삭제할 수 없습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }
    }

    public Blog getBlog(Long id) {
        Blog blog = findBlog(id);
        if (blog != null) {
            return blog;
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }

    }

    private Blog findBlog(Long id){
        return blogRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

    //reply----------------------------------

    public Reply addReply(ReplyRequestDto requestDto, User user, Long blogId) {
        Blog blog = findBlog(blogId);
        if (blog != null) {
            Reply reply = new Reply(requestDto, user, blog);
            Reply saveReply = replyRepository.save(reply);
            return reply;
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }
    }

    public List<ReplyResponseDto> getReplies(Long blogId) {
        Blog blog = findBlog(blogId);
        if (blog != null) {
            return replyRepository.findAllByOrderByModifiedAtDesc().stream().map(ReplyResponseDto::new).toList();
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }
    }

    public Reply updateReply(Long blogId, ReplyRequestDto requestDto, User user, Long replyId) {
        Blog blog = findBlog(blogId);
        if (blog != null) {
            Reply reply = findReply(replyId);
            if (reply != null && reply.getBlog().getId() == blog.getId()) { // null이 아니고 게시글에 속한 댓글인 경우
                if (user.getUsername().equals(reply.getUsername())) {
                    reply.update(requestDto);
                    return reply;
                } else {
                    throw new IllegalArgumentException("로그인한 작성자의 댓글이 아닙니다.");
                }
            } else {
                throw new IllegalArgumentException("해당 게시물에서 선택한 댓글은 존재하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }
    }


    public String deleteReply(Long blogId, User user, Long replyId) {
        Blog blog = findBlog(blogId);
        if (blog != null) {
            Reply reply = findReply(replyId);
            if (reply != null) {
                if (user.getUsername().equals(reply.getUsername())) {
                    replyRepository.delete(reply);
                    return blogId+"번 게시물의"+replyId+"번째 댓글 삭제 완료";
                } else {
                    throw new IllegalArgumentException("로그인한 작성자의 댓글이 아닙니다.");
                }
            } else {
                throw new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }
    }

    private Reply findReply(Long id) {
        return replyRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
        );
    }
}

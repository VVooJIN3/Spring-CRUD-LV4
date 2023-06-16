package com.sparta.blog.repository;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class BlogRepository {

    private final JdbcTemplate jdbcTemplate;
    public BlogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<BlogResponseDto> findAll() {
        String sql = "SELECT * FROM blog";
        return jdbcTemplate.query(sql, new RowMapper<BlogResponseDto>() {
            @Override
            public BlogResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Blog 데이터들을 blogResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                LocalDateTime dateTime  = rs.getTimestamp("datetime").toLocalDateTime();
//                Blog blog = new Blog(id, title, username, contents,dateTime);
                return new BlogResponseDto(id,title, username, contents,dateTime);

            }
        });
        
    }

    public Blog save(Blog blog) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO blog (title, username, contents,dateTime) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,blog.getTitle());
            preparedStatement.setString(2,blog.getUsername());
            preparedStatement.setString(3,blog.getContents());
            preparedStatement.setTimestamp(4,Timestamp.valueOf(blog.getDateTime()));
            return preparedStatement;
        },
                keyHolder);
        Long id = keyHolder.getKey().longValue();
        blog.setId(id);
        return blog;
    }

    public Blog findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM blog WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Blog blog = new Blog();
                blog.setTitle(resultSet.getString("title"));
                blog.setUsername(resultSet.getString("username"));
                blog.setContents(resultSet.getString("contents"));
                blog.setDateTime(resultSet.getTimestamp("datetime").toLocalDateTime());
                return blog;
            } else {
                return null;
            }
        }, id);
    }

    public void update(Long id, BlogRequestDto requestDto) {
        String sql = "UPDATE blog SET title = ?, username = ?, contents = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getUsername(), requestDto.getContents(), id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM blog WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public BlogResponseDto findOne(Long id) {
        String sql = "SELECT * FROM blog WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<BlogResponseDto>() {
            @Override
            public BlogResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long blogId = rs.getLong("id");
                String title = rs.getString("title");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                LocalDateTime dateTime = rs.getTimestamp("datetime").toLocalDateTime();
                return new BlogResponseDto(blogId, title, username, contents, dateTime);
            }
        });
    }
}

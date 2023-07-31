package com.sparta.blog.useTime.repository;

import com.sparta.blog.useTime.entity.ApiUseTime;
import com.sparta.blog.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {

  Optional<ApiUseTime> findByUser(User user);
}

package com.lizxing.redis.demo.dao;

import com.lizxing.redis.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("user")
public interface UserDao extends JpaRepository<User,Integer> {
}

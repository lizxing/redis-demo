package com.lizxing.redis.demo.service.impl;

import com.lizxing.redis.demo.dao.UserDao;
import com.lizxing.redis.demo.entity.User;
import com.lizxing.redis.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public List<User> list() {
        List<User> all = userDao.findAll();
        String a = new String();
        return all;
    }
}

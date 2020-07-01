package com.lizxing.redis.demo;

import com.lizxing.redis.demo.entity.User;
import com.lizxing.redis.demo.service.UserService;
import com.lizxing.redis.demo.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @Test
    void testRedis(){
        List<User> userList = userService.list();
        System.out.println(userList);

        if (redisUtil.hasKey(userList.get(0).getName())){
            System.out.println("已存在");
        }else {
            System.out.println("不存在");
            System.out.println(redisUtil.set(userList.get(0).getName(), userList.get(0).getAge().toString()) ? "成功" : "失败");
        }
    }
}

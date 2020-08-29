package com.lizxing.redis.demo;

import com.lizxing.redis.demo.entity.User;
import com.lizxing.redis.demo.service.UserService;
import com.lizxing.redis.demo.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @Resource
    Redisson redisson;

//    @Autowired
//    RedisTemplate redisTemplate;

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

    @Test
    void threadTest(){
        int threadCount = 100;
        final CountDownLatch latch = new CountDownLatch(threadCount);
        for(int i=0; i< threadCount; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testRedis3();
                    latch.countDown();
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(threadCount+"个线程已经执行完毕");
    }

    @Test
    void testRedis2(){
        String lockKey = "lockKey";
        boolean flag = redisUtil.setnx(lockKey, 1 + "", 10);
        System.out.println("线程id=" + Thread.currentThread().getId() + "是否获取到锁" + flag);
        if (flag) {
            try {
                int stock = Integer.parseInt((String) redisUtil.get("stock"));
                if (stock > 0) {
                    // 库存-1
                    --stock;
                    redisUtil.set("stock", stock + "");
                    System.out.println("线程id=" + Thread.currentThread().getId() + "交易成功，库存为：" + stock);
                } else {
                    System.out.println("线程id=" + Thread.currentThread().getId() + "交易失败，库存为：" + stock);
                }
            } finally {
                redisUtil.del(lockKey);
            }
        }
    }

    @Test
    void testRedis3(){
        String lockKey = "lockKey";
        RLock lock = redisson.getLock(lockKey);
        try {
            lock.lock();
            int stock = Integer.parseInt((String) redisUtil.get("stock"));
            if (stock > 0) {
                // 库存-1
                --stock;
                redisUtil.set("stock", stock + "");
                System.out.println("线程id=" + Thread.currentThread().getId() + "交易成功，库存为：" + stock);
            } else {
                System.out.println("线程id=" + Thread.currentThread().getId() + "交易失败，库存为：" + stock);
            }
        }finally {
            lock.unlock();
        }
    }
}

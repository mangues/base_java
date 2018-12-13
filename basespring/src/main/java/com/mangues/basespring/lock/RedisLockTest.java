package com.mangues.basespring.lock;

import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

public class RedisLockTest {
    public static void start(ApplicationContext applicationContext){
        StringRedisTemplate redisTemplate = applicationContext.getBean(StringRedisTemplate.class);
        CountDownLatch countDownLatch = new CountDownLatch(100);
        Lock lock = new RedisLock(redisTemplate,"BasespringApplication");

        Runnable re = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                //倒计时
                try {
                    lock.lock();
                    countDownLatch.await();
                    count++;
                    System.out.println(Thread.currentThread() + "=" + count);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        };

        for (int i=0;i<100;i++) {
            new Thread(re).start();
            countDownLatch.countDown();
        }
    }
}

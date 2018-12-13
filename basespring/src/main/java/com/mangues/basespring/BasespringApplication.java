package com.mangues.basespring;

import com.mangues.basespring.lock.RedisLockTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class BasespringApplication {




    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext run = SpringApplication.run(BasespringApplication.class, args);
        //测试redis 高并发 redis 锁
        RedisLockTest.start(run);
    }

}


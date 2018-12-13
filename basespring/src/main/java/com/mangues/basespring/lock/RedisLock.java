package com.mangues.basespring.lock;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class RedisLock implements Lock {
    //超时时间
    private static final long LOCK_TIMEOUT = 5*1000;
    private StringRedisTemplate redisTemplate;
    private String lockKey;
    //延迟时间
    private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;

    public RedisLock(StringRedisTemplate redisTemplate,String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
    }


    /**
     * 调用后一直阻塞到获得锁
     */
    @Override
    public void lock() {
        while (true) {
            // 获取到期时候的时间，解锁死锁用
            if (tryLock()){
                return;
            }else {
                String result = redisTemplate.opsForValue().get(lockKey);
                Long current_lock_timeout_str = result == null ? null : Long.parseLong(result);
                //锁已经失效,防止redis设置完崩溃导致缓存失效没有设置
                if (current_lock_timeout_str != null && current_lock_timeout_str < currentTimeForRedis()) {
                    //判断是否为空，不为空时，说明已经失效，如果被其他线程设置了值，则第二个判断条件无法执行
                    //获取上一个锁到期时间，并设置现在的锁到期时间
                    //不用del key 是防止 几个线程一起进来发现没有del掉，都获得锁

                    //获取上一个锁到期时间，并设置现在的锁到期时间，
                    //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                    Long lock_timeout = currentTimeForRedis() + LOCK_TIMEOUT + 1;
                    Long old_lock_timeout_str = Long.valueOf(redisTemplate.opsForValue().getAndSet(lockKey,lock_timeout.toString() ));
                    //时间和自己一样，成功设置了新的时间
                    if (old_lock_timeout_str != null && old_lock_timeout_str.equals(current_lock_timeout_str)) {
                        System.out.println(Thread.currentThread()+"=加锁成功========222");
                        redisTemplate.expire(lockKey,LOCK_TIMEOUT,TimeUnit.MILLISECONDS);
                        return;
                    }
                }
            }
             /*
                延迟100 毫秒,  这里使用随机时间可能会好一点,可以防止饥饿进程的出现,即,当同时到达多个进程,
                只会有一个进程获得锁,其他的都用同样的频率进行尝试,后面有来了一些进行,也以同样的频率申请锁,这将可能导致前面来的锁得不到满足.
                使用随机的等待时间可以一定程度上保证公平性
             */
            try {
                Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 调用后一直阻塞到获得锁 但是接受中断信号(sleep,interrupt)
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    /**
     * 尝试加锁,可以加就加个锁，不会阻塞等待
     * 尝试是否能获得锁 如果不能获得立即返回。如果锁未被另一个线程保持，则获取锁。
     * @return
     */
    @Override
    public boolean tryLock() {
        Long lock_timeout = currentTimeForRedis() + LOCK_TIMEOUT + 1;
        Boolean execute = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                Boolean aBoolean = connection.setNX(lockKey.getBytes(), lock_timeout.toString().getBytes());
                return aBoolean;
            }
        });

        if (execute){
            System.out.println(Thread.currentThread()+"加锁成功========111");
            //设置超时时间
            redisTemplate.expire(lockKey,LOCK_TIMEOUT,TimeUnit.MILLISECONDS);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        System.out.println(Thread.currentThread()+"==解锁成功=======");
        redisTemplate.delete(lockKey);
    }

    @Override
    public Condition newCondition() {
        return null;
    }


    //集群下获取统一的时间
    public long currentTimeForRedis(){
       return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Long time = connection.time();
                return time;
            }
        });
    }
}

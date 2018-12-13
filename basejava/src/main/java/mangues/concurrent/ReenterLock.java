package mangues.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReenterLock {
    public static void main(String[] args) {
        final int current = 100;

        Runnable target = new Runnable() {
            int count = 0;
            //倒计时器,保证并发在减到0时同事执行
            CountDownLatch countDownLatch = new CountDownLatch(current);
            /*
              公平锁的原则就是两个线程谁先来，谁先使用资源。不会产生饥饿的现象，只要排队，都可以被执行。
             */
            Lock lock = new ReentrantLock();

            public void run() {
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.lock();
                count++;
                System.out.println(Thread.currentThread() + "=" + count);
                lock.unlock();
            }
        };

        for (int i = 0; i < current; i++) {
            new Thread(target).start();
        }
    }
}

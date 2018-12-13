package mangues.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteLockTest {
    private String cache = null;

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String getCache() {
        return this.cache;
    }

    static final int current = 100;
    private static CountDownLatch countDownLatch = new CountDownLatch(current);

    private Lock lock = new ReentrantLock();

    public static void main(String[] args) throws Exception {
        List<Thread> list = new ArrayList<Thread>();
        final ReadWriteLockTest tr = new ReadWriteLockTest();
        for (int i = 0; i < current; i++) {
            Thread thread = new Thread(new Runnable() {
                //倒计时器,保证并发在减到0时同事执行
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    tr.data();
                }
            });
            thread.start();
            countDownLatch.countDown();
            list.add(thread);
        }

        while (1 == 1) {

        }

    }

    public void data() {
        String cache = getCache();
        if (cache != null) {
            return;
        }


        try {


            System.out.println("读数据库！");
            setCache("===100");
        } finally {

        }

    }

}


//package com.mangues.concurrent;
//
//        import java.util.ArrayList;
//        import java.util.List;
//        import java.util.concurrent.CountDownLatch;
//        import java.util.concurrent.locks.Lock;
//        import java.util.concurrent.locks.ReentrantLock;
//
//public class ReadWriteLockTest {
//    private  String cache = null;
//
//    public  void setCache(String cache) {
//        this.cache = cache;
//    }
//
//    public  String getCache() {
//        return this.cache;
//    }
//    static final int current = 100;
//    private static CountDownLatch countDownLatch = new CountDownLatch(current);
//
//    private  Lock lock= new ReentrantLock();
//    public static void main(String[] args) throws Exception{
//        List<Thread> list = new ArrayList<Thread>();
//        final ReadWriteLockTest tr = new ReadWriteLockTest();
//        for (int i = 0; i < current; i++) {
//            Thread thread = new Thread(new Runnable() {
//                //倒计时器,保证并发在减到0时同事执行
//                public void run() {
//                    try {
//                        countDownLatch.await();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    tr.data();
//                }
//            });
//            thread.start();
//            countDownLatch.countDown();
//            list.add(thread);
//        }
//
//        while (1==1){
//
//        }
//
//    }
//
//    public   void data() {
//        String cache = getCache();
//        if (cache != null){
//            return;
//        }
//        try {
//            lock.lock();
//            cache = getCache();
//            if (cache != null){
//                return;
//            }
//
//
//            System.out.println("读数据库！");
//            setCache("===100");
//        }finally {
//            lock.unlock();
//        }
//
//    }
//
//}

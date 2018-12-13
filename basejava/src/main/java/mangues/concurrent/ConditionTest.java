package mangues.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest implements Runnable {
    public static ReentrantLock relock = new ReentrantLock();
    public static Condition condition = relock.newCondition();

    public void run() {
        try {
            relock.lock();
            condition.await();
            System.out.println("Thread is bbq");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            relock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Thread is start");
        ConditionTest con = new ConditionTest();
        Thread thread = new Thread(con);
        thread.start();
        Thread.sleep(2000);
        relock.lock();
        condition.signal();
        relock.unlock();
    }
}
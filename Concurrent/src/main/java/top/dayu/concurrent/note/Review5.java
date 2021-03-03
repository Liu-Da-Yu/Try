package top.dayu.concurrent.note;

import top.dayu.concurrent.temp.TestPool;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Classname Review5
 * @Description TODO
 * @Date 2021/2/20 17:32
 * @Created by ldy
 */
public class Review5 {

    /**
     第八章：线程池  juc 其他并发工具包

     线程池/池/的好处：避免创建超过内存的线程数/充分利用已有的线程

     自定义线程池的代码{@link TestPool } // todo 重点看看

     //TODO java线程池的使用 Executors  ScheduledExecutorService
     todo https://blog.csdn.net/jspamd/article/details/107927638


     */

    /**

     重点是 ReentrantLock 及其原理

     默认是非公平锁： 默认的构造方法
     public ReentrantLock() {
     sync = new NonfairSync();
     }

     lock方法的实现，aqs需要维护一个状态
     final void lock() {
        if (compareAndSetState(0, 1))
            setExclusiveOwnerThread(Thread.currentThread());
        else
            acquire(1);
     }

     */

    public void test1() throws Exception {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();

    }

    /**
     ReentrantReadWriteLock 读写锁：当读的次数远大于写的次数的时候，使用读写锁，提高并发
     因为读-读操作不是互斥的，多个线程可以同时执行，只有读写和写写是互斥的。
     关于读写锁的实现方式
        todo 重点看看 {@link top.dayu.concurrent.temp.TestReadWriteLock }
    */
    public static void main(String[] args) {



    }


}

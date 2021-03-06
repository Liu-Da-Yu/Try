package top.dayu.concurrent.note;

import top.dayu.concurrent.temp.TestPool;
import top.dayu.concurrent.temp.TestSemaphore;
import top.dayu.concurrent.temp.TestStampedLock;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

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

    /**
     StampedLock 是jdk8加入的 可以校验数据有没有被更改过， 读读性能会比ReentrantReadWriteLock更高
     因为后者需要cas修改状态。

     特点就是需要配合一个戳来使用,加锁时会返回，解锁时就需要这个戳。
     性能高是因为采取乐观读的方式，读取之前先去验证这个戳有没有问题，如果不是旧的，就能读到数据
     如果是旧的就会进行锁升级，升为读锁。

     不支持条件变量 不支持重入

     todo 重点看看 {@link TestStampedLock }


    */

    public void test33() {

        StampedLock stampedLock = new StampedLock();
        final long l = stampedLock.readLock();
        final long l1 = stampedLock.writeLock();
        stampedLock.unlock(l);

    }


    /**

     Semaphore:(sei 么 for) 信号量：用来限  同时访问资源的线程的上限
     //todo 重点看看  {@link TestSemaphore }

    */
    private void test123(){

        //上限为3个线程（相当于有 3个许可证）
        Semaphore semaphore = new Semaphore(3);









    }








}

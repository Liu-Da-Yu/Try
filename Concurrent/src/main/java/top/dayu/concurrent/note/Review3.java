package top.dayu.concurrent.note;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Classname Review3
 * @Description TODO
 * @Date 2021/2/16 21:08
 * @Created by ldy
 */
public class Review3 {

    public  void test () {
        LockSupport.park();
    }

    /**
     之前的消息中间件是进程间通信的

     //暂停 park unpark可以比较精确的控制某一个线程
     LockSupport.park();
     //恢复  unpark在park以后调用也可以；并且不需要配合monitor；
     LockSupport.unpark(t1);

     park翻译过来即是停车的意思，我们可以这样理解，每个被应用程序启动的线程就是一辆在计算机总线赛道上奔驰着的跑车，
     当你想让某台车停下来休息会时那么就给它一个park信号，它就会立即停到赛道旁边的停车位中，
     当你想让它从停车位中驶出并继续在赛道上奔跑时再给它一个unpark信号即可


     park的底层原理是每个线程都有一个Parker对象，是用c语言写的。


     线程活跃性：死锁 活锁 饥饿者 都可以使用ReentrantLock来解决(entrant:进入 re：重新，再次)

     为了增强并发度，锁最好设计成细粒度的 不要锁一个大房间，要分别对大房间中的卧室和书房上锁。
     但是容易出现死锁(deadlock)问题：
     t1线程获得了a对象的锁，接下来想要获取b对象的锁；
     t2线程获得了b对象的锁，接下来想要获取a对象的锁；

     定位死锁：
     1: 可以在Terminal 命令 jps 然后找到进程 然后jstack 进程id
     然后找到found one java-lever deadlock

     2:可以使用j console 链接进去点击线程-死锁

     3：解决方案：
        顺序加锁：多个加锁对象都使用一样的顺序加锁；线程1先给a加锁，再给b加锁；
                线程2先给a加锁再给线程b加锁


     Found one Java-level deadlock:
     =============================
     "t2":
     waiting to lock monitor 0x00007ff6c9837208 (object 0x00000007aaded908, a java.lang.Object),
     which is held by "t1"
     "t1":
     waiting to lock monitor 0x00007ff6c983b2a8 (object 0x00000007aaded918, a java.lang.Object),
     which is held by "t2"

     Java stack information for the threads listed above:
     ===================================================
     "t2":
     at top.dayu.concurrent.note.Review3.lambda$testDeadLock$1(Review3.java:64)
     - waiting to lock <0x00000007aaded908> (a java.lang.Object)
     - locked <0x00000007aaded918> (a java.lang.Object)
     at top.dayu.concurrent.note.Review3$$Lambda$2/1627960023.run(Unknown Source)
     at java.lang.Thread.run(Thread.java:745)
     "t1":
     at top.dayu.concurrent.note.Review3.lambda$testDeadLock$0(Review3.java:49)
     - waiting to lock <0x00000007aaded918> (a java.lang.Object)
     - locked <0x00000007aaded908> (a java.lang.Object)
     at top.dayu.concurrent.note.Review3$$Lambda$1/249515771.run(Unknown Source)
     at java.lang.Thread.run(Thread.java:745)

     Found 1 deadlock.

     */

    //演示死锁的代码
    private static void testDeadLock(){
        final Object obj1 = new Object();
        final Object obj2 = new Object();

        new Thread(()->{
            synchronized (obj1){
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t1获取到了obj1");
                synchronized (obj2){
                    System.out.println("t1获取到了obj2");
                }
            }

        },"t1").start();

        new Thread(()->{
            synchronized (obj2){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t2获取到了obj2");
                synchronized (obj1){
                    System.out.println("t2获取到了obj1");
                }
            }
        },"t2").start();

    }

    /**
        活锁：两个线程互相改变对方的结束条件，最后谁也无法结束。
            比如以一个线程操作一个变量不断+1，当>10的时候就执行完毕；
            另外一个线程不断也操作这个变量-1，当<0的时候就执行结束；
            如果两个线程一块执行，则谁也执行不完

        饥饿：以恶搞线程由于优先级太低，一直无法使用cpu，也就结束不了。
    */

    /**

     ReentrantLock为juc工具包中的类
     相比于synchronized的优点有：可中断 可设置超时时间 可设置为公平锁 支持多个条件变量


     synchronized 和 ReentrantLock 两者的共同点：
     1. 都是用来协调多线程对共享对象、变量的访问
     2. 都是可重入锁，同一线程可以多次获得同一个锁
     3. 都保证了可见性和互斥性
     两者的不同点：
     1. ReentrantLock 显示的获得、释放锁，synchronized 隐式获得释放锁
     2. ReentrantLock 可响应中断、可轮回，synchronized 是不可以响应中断的，为处理锁的
     不可用性提供了更高的灵活性
     3. ReentrantLock 是 API 级别的，synchronized 是 JVM 级别的
     4. ReentrantLock 可以实现公平锁
     5. ReentrantLock 通过 Condition 可以绑定多个条件
     6. 底层实现不一样， synchronized 是同步阻塞，使用的是悲观并发策略，lock 是同步非阻
     塞，采用的是乐观并发策略
     7. Lock 是一个接口，而 synchronized 是 Java 中的关键字，synchronized 是内置的语言
     实现。
     8. synchronized 在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；
     而 Lock 在发生异常时，如果没有主动通过 unLock()去释放锁，则很可能造成死锁现象，
     因此使用 Lock 时需要在 finally 块中释放锁。
     9. Lock 可以让等待锁的线程响应中断，而 synchronized 却不行，使用 synchronized 时，
     等待的线程会一直等待下去，不能够响应中断。
     10. 通过 Lock 可以知道有没有成功获取锁，而 synchronized 却无法办到。
     11. Lock 可以提高多个线程进行读操作的效率，既就是实现读写锁等




     //语法
     ReentrantLock reentrantLock = new ReentrantLock();
     reentrantLock.lock();
     try {
     //保护的临界区代码
     }finally {
     //释放锁
     reentrantLock.unlock();
     }

     ReentrantLock的特性： 可重入 可打断 锁超时 公平锁 条件变量

     不可重入锁：第二次获得锁时，自己也会被锁住
     可重入锁：一个线程获得了锁，就有权再次进入（ReentrantLock/synchronized都是哦可重入的）
        在以恶搞对象的锁中调用了还是这个对象的锁，是可以进去执行，不会阻塞住

     可打断的：
     使用reentrantLock.lockInterruptibly 加锁
     在加锁的时候如果没有竞争就会获得当前锁，如果有竞争就会进入到阻塞队列等待，
     此时可以被别的线程使用Interrupt方法打断

     锁超时：
     //尝试获得锁
     使用 boolean reentrantLock.tryLock();
     使用reentrantLock本身也可以被继承，以获得一些锁的特性

     公平锁：
     ReentrantLock默认是不公平的锁，因为释放锁了以后，
     其他等待的线程会竞争获得锁而不是按照先阻塞顺序先到先得。
     开启公平锁：ReentrantLock reentrantLock = new ReentrantLock(false);
    一般没必要使用公平锁，使用会降低并发度。

     条件变量：(相当于设置多个休息室，专门等烟的，专门等外卖的)
     ReentrantLock lock = new ReentrantLock();
     Condition condition = lock.newCondition();
     condition.await();//等待的休息室
     condition.signal();//唤醒


     第四章总结： monitor就是管程
     应用：
     互斥： 使用synchronized或ReentrantLock来达到共享资源的互斥效果
     同步： 使用wait/notify或者Lock的条件变量来达到线程间的通信效果
     其他的关键字底层用的都是monitor都是用的c++写的
     只有ReentrantLock相当于是在java层面实现来monitor


    */


    private void test1(){


    }


    public static void main(String[] args) {
        //testDeadLock();
        ReentrantLock reentrantLock = new ReentrantLock(false);

    }

}

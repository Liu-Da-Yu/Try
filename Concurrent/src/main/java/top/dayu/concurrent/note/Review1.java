package top.dayu.concurrent.note;

import java.util.concurrent.TimeUnit;

/**
 * @Classname Review
 * @Description TODO
 * @Date 2021/2/10 14:28
 * @Created by ldy
 */
@SuppressWarnings("all")
public class Review1 {

    /**
     查看进程线程
     windows： tasklist taskkill
     linux：kill杀死进程
     java： jps查看所有java进程
     win + r： jcconsole查看java线程运行情况，是一个程序，直接打开支持查看远程

     线程运行原理：
     jvm是由 堆 栈 方法区组成的  其中栈是分配给线程使用的，栈由栈帧组成，每个方法调用都会产生一个栈帧内存
     同一个线程只能有一个活动着的栈帧（frame）
     方法中使用的变量都存在堆里面，方法执行完内存就被释放了，堆内存需要等待垃圾回收

     每个线程的内存都是独立的互不干扰

     线程的上下文切换
     原因：因为以下一些原因导致cpu不再执行当前的线程，转为执行别的线程
     1：当前线程的cpu时间片用完了
     2：垃圾回收，垃圾回收时会暂停所有的工作着的线程，转为执行垃圾回收线程
     3：有更高优先级的线程需要执行
     4：线程自己调用了sleep/yield/wait/join/park/synchronized/lock
     切换的时候操作系统需要保存当前线程的状态，并恢复另外线程的状态(栈帧的信息，局部变量，返回地址等)
     java中对应的就是程序计数器 作用就是记住下一条指令的执行地址，为线程私有的。

     run和start方法的区别，前者启动后会执行里面的方法但是是由主线程执行的，后者是另起一个线程来执行

     睡眠时使用后者，可读性更好
     Thread.sleep(1000);
     TimeUnit.SECONDS.sleep(1);

     sleep ：睡眠 会让线程从Running运行状态进入到Timed Waiting状态（阻塞）
     yield [jiːld]【一哦的】【屈服;让步】：让出 让线程从Running运行状态进入到Runnable就绪状态
     底层还是依赖操作系统的任务调度器，yield后进入到就绪状态，
     下一次任务调度器还是有可能会分配时间片给当前线程，而不会分配给处于阻塞状态的线程
     yield是一次性的，只是让出本次线程时间片，下次就会再执行，没有任何参数，而睡眠是有时间的。

     线程优先级： 只是一个提示，还是取决于任务调度器，只有cpu忙的时候才会有点作用，
     默认是5，最高是10  MIN_PRIORITY = 1 priority【praɪˈɒrəti】【谱乳癌奥瑞忒】【首要事情;优先;优先权】

     这是一个死循环的写法： for(;;){}


     sleep：避免空转，写一个死循环里面要是不睡眠，cpu会爆，加一个sleep50 可以有效地降低cpu使用率

     join：等待线程结束（那个线程调用，等待哪个线程）
     还可以设置等待时间可以限制最长等待时间 join的底层原理就是wait方法

     打断线程 .interrupt 打断阻塞中(wait join sleep)的线程会报异常
     只会会产生一个布尔值的打断标记，非阻塞状态被打断后标记才会为true
     */

    public static void test1() throws Exception {

        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    //说明被打断了，就退出执行
                    break;
                }
            }
        });

        //1秒钟后打断t1线程， t1线程的标记就会为true就会退出执行
        TimeUnit.SECONDS.sleep(1);
        t1.interrupt();

    }


    //代码演示 两阶段终止模式：
    public static void test2() {

        Thread t1 = new Thread(() -> {
            while (true) {

                if (Thread.currentThread().isInterrupted()) {
                    //说明被打断了，做一下善后工作，比如关闭资源等 然后退出执行
                    break;
                } else {

                    //让线程睡眠两秒，避免过高的占用cpu
                    try {
                        Thread.sleep(2000);
                        //此处写该死循环的主要目的，比如监控cpu使用率的代码逻辑
                        //todo
                    } catch (InterruptedException e) {
                        //如果在睡眠中被打断，说明想要结束当前线程，此处可以设置打断标记为true
                        //技巧 重新执行打断，使其值设置为true
                        Thread.currentThread().interrupt();
                        //interrupt()  不会清除打断标记
                        //interrupted() 会清除打断标志，标记就会置为false，设置属性值可以用这个方法
                    }
                }
            }
        });
        t1.start();
    }

    /**

     不推荐使用的方法(都已过时)：容易破坏同步代码块 造成线程死锁
     stop 停止线程 用两阶段终止模式来取代
     suppend 挂起暂停线程 用wait替代
     resume 恢复线程运行 用notifide来代替


     只要还有一个线程在运行，java进程就不会结束
     守护线程：只要其他非守护线程运行结束了，即使守护线程还没运行完，也会强制结束。
     主线程和创建的线程默认都是非守护线程
     设置守护线程  t1.setDaemon(true);
     垃圾回收器就是使用的守护线程  垃圾回收: 堆内存中的对象当没有被对象引用时，就会被垃圾回收，尤其是在内存紧张时。
     Tomcat中的Acceptor和poller线程都是守护线程，所以当接收到shutdown命令后，不会等待它们处理完请求。

     从操作系统的角度线程分为5种状态：
     初始状态：仅语言层面创建了线程
     可运行状态/就绪：已与操作系统线程关联，等待cpu调用
     运行状态：正在运行，获得了时间片，时间片用完会切换到就绪状态，发生上下文切换
     阻塞状态：如果调用了阻塞的api，比如读写文件，线程会进入阻塞状态，不会使用到cpu，会发生上下文切换
     直到被唤醒，然后进入到就绪状态，等待获得时间片

     终止状态：线程执行完毕，生命周期结束，不会再转为别的状态了。


     java线程的6种状态
     ①初始(NEW)：新创建了一个线程对象，但还没有调用start()方法。
     ②运行(RUNNABLE)：Java线程中将就绪（ready）和运行中（running）两种状态笼统的成为“运行”。
     线程对象创建后，其他线程(比如main线程）调用了该对象的start()方法。
     该状态的线程位于可运行线程池中，等待被线程调度选中，获取cpu 的使用权，此时处于就绪状态（ready）。
     就绪状态的线程在获得cpu 时间片后变为运行中状态（running）。
     ③阻塞(BLOCKED)：表线程阻塞于锁。
     ④等待(WAITING)：进入该状态的线程需要等待其他线程做出一些特定动作（通知或中断）。
     ⑤超时等待(TIME_WAITING)：该状态不同于WAITING，它可以在指定的时间内自行返回;有时间的等待
     ⑥终止(TERMINATED)：表示该线程已经执行完毕


     Thread. public enum State{
     NEW: 线程刚被创建，但是还没有调用start方法
     RUNNABLE: 相当于系统层面的 可运行/运行/阻塞
     BLOCKED(): WAITING(join): TIMED_WAITING(sleep):
     这三种都是javaAPI层面对阻塞状态的细分，后面会在状态转换中讲到
     TERMINATED: 终止状态
     }
     */

    public static void main(String[] args) {
        test3();
    }

    //代码演示线程的六种状态
    public static void test3() {

        //NEW
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                System.out.println("running....");
            }
        };


        //RUNNABLE
        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while (true) {

                }
            }
        };
        t2.start();


        //TERMINATED
        Thread t3 = new Thread("t3") {
            @Override
            public void run() {
                System.out.println("running....");
            }
        };
        t3.start();


        //TIMED_WAITING
        Thread t4 = new Thread("t4") {
            @Override
            public void run() {
                synchronized (Test1.class) {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t4.start();


        //WAITING
        Thread t5 = new Thread("t5") {
            @Override
            public void run() {
                try {
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t5.start();


        //BLOCKED
        Thread t6 = new Thread("t6") {
            @Override
            public void run() {
                //因为t4上了锁，并且在睡眠，所以当前线程永远拿不到锁
                synchronized (Test1.class) {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        t6.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(t1.getState());
        System.out.println(t2.getState());
        System.out.println(t3.getState());
        System.out.println(t4.getState());
        System.out.println(t5.getState());
        System.out.println(t6.getState());

    }


}

package top.dayu.concurrent.note;

import sun.misc.Unsafe;
import top.dayu.concurrent.temp.ConnectionPool;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @Classname Review4
 * @Description TODO
 * @Date 2021/2/17 16:57
 * @Created by ldy
 */
public class Review4 {

    /**
     java内存模型(JMM java memory model):
     定义了 主存和工作内存的抽象概念
     主存：
     所有线程共享的数据：成员变量，静态的成员变量
     工作内存：
     每个线程私有的数据： 局部变量等




     JMM体现在以下几个方面：
     原子性：保证指令不会受到线程上下文切换带来的影响
     可见性：保证指令不会受到cpu缓存的影响
     有序性：保证指令不会受到CPU指令并行优化的影响

     有些变量声明好了以后，如果被某一线程大量的引用，jit就会做一个优化，把这个变量放到线程的高速缓存中；
     以后即使原变量改变了值，该线程也还是会使用原来的旧值。
     解决： 在原变量声明的时候多添加一个关键字： static volatile boolean run = false;
     加了这个关键字以后就不会加入缓存了，每次都是到主存中获取数值；
     synchronized内的变量也不会使用缓存

     volatile只能解决可见性，有序性，并不能保证原子性；synchronized既能保证原子性有序性，也能保证可见性。
     但是synchronized会对性能有影响

     指令重排序：为了提升cpu并发执行度，会把指令做重排序操作。多线程下可能会有问题。
     单线程下不会有问题，因为单线程排序的都是不影响结果的排序。
     使用关键字volatile可以避免(volatile中的读屏障 写屏障)

     DCL：  Double-Checked Locking
     */




    /**
     第六章： 无锁并发(cas+volatile)（cas是无锁并发，非阻塞并发）

     可以不上锁来保证共享资源的安全性 重点了解这个类：  @link{TestAccount} -> AtomicInteger
     boolean compareAndSet(prev, next)  重点是compareAndSet方法，简称就是cas，意思就是比较并设置
     compareAndSet在cpu指令级别是原子的，但不是用锁实现的
     compareAndSet想要更新成功必须满足一个条件就是要修改的值没有被别的线程改变过。
     cas操作执行时需要获取到最新的值 所以需要volatile的支持。

     使用了cas即使执行失败，就立即重试，线程始终在高速执行，没有等待所以是非阻塞的。

     为什么无锁效率高：首先没有阻塞，线程一直在运行，其次没有发生上下文切换。
     线程数少于cpu核心数的时候(不需要上下文切换)使用cas还是比较合适。
     如果线程竞争激烈想必重试会非常频繁，反而会影响效率。

     乐观锁与悲观锁：
     cas是一种乐观的思想：乐观估计不会有别的线程来修改共享变量，就算改来也没关系我再重试一下
     synchronized是一种悲观的思想，估计别人会来修改共享变量，所以得防着先上锁。

     运算的接口用来配置lambda表达式使用的一元二元：IntUnaryOperator

     JUC中提供了一些支持包括：
     原子整数：
     AtomicBoolean/AtomicInteger/AtomicLong
     -> updateAndGet

     原子引用：
     AtomicReference/AtomicMarkableReference/AtomicStampedReference
     AtomicStampedReference相比较于AtomicReference多了一个版本号，用来感知是否修改过变量，可以追踪过程
     如果用AtomicReference，感知不到a改为b，再改为a的过程
     如果只关心是否修改过可以使用AtomicMarkableReference

     原子数组：
     /AtomicIntegerArray/AtomicLongArray/AtomicReferenceArray

     字段更新器：
     可以针对某个对象的某个字段进行原子操作  只能配合volatile一块使用 否则会报错
     /AtomicIntegerFieldUpdater/AtomicReferenceFieldUpdater/AtomicLongFieldUpdater

     原子累加器：
     // todo 源码 LongAdder.java java8提供的专门用来累加的，性能更高；
     原理就是会设置多个累加变量，然后再汇总，这就导致cas设置值失败的概率降低，速度更快


     cas也可以加锁 就是定义一个状态 0没加锁 1加锁 写两个方法  用来加锁和解锁修改这个状态即可
     很多底层实现代码都是这么用的，但是自己别这么写，因为有风险。


     //此注解用来防止缓存行的伪共享
     @sun.misc.Contended public class Review4 {}

     缓存行：
     伪共享：

     cpu到不同地方读取数据的时钟周期
     寄存器(就在cpu内部)  1个cycle(1个时钟周期)(4GHZ 的cpu约为0.25ns)
     一级缓存 3-4 个cycle
     二级缓存 10-20 个cycle
     三级缓存 40-45 个cycle
     内存   120-240 个cycle
     由于cpu执行和内存速度的差异巨大，所以先预读数据到缓存中。时间空间的局部性原理
     而缓存以缓存行为单位，大约64字节；但是缓存存在着一个问题就是内存中的数据被两个cpu使用，
     并且其中一个cpu修改了这个属性值，那么必须要要让另一个cpu中缓存的数据失效。

     练拳不练功 到老一场空

     Unsafe对象提供了底层的操作内存和线程的方法，对象不能直接调用，只能通过反射获得


     */


    public static void test2() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);//允许访问私有的
            Unsafe unsafe = (Unsafe) theUnsafe.get(null);
            System.out.println(unsafe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     第七章： 不可变(如果一个变量是不可变的，即使被共享，多线程下也不会有问题)
     如果多个线程访问一个可变的对象，就有可能会出问题，参考以下实例：
     */

    public static void test() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    System.out.println(sdf.parse("1951-04-21"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     Exception in thread "Thread-1" Exception in thread "Thread-0" java.lang.NumberFormatException: multiple points
     at sun.misc.FloatingDecimal.readJavaFormatString(FloatingDecimal.java:1890)
     at sun.misc.FloatingDecimal.parseDouble(FloatingDecimal.java:110)
     at java.lang.Double.parseDouble(Double.java:538)
     at java.text.DigitList.getDouble(DigitList.java:169)
     at java.text.DecimalFormat.parse(DecimalFormat.java:2056)
     at java.text.SimpleDateFormat.subParse(SimpleDateFormat.java:1869)
     at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1514)
     at java.text.DateFormat.parse(DateFormat.java:364)
     at top.dayu.concurrent.note.Review4.lambda$test$0(Review4.java:123)
     at java.lang.Thread.run(Thread.java:745)
     java.lang.NumberFormatException: multiple points
     at sun.misc.FloatingDecimal.readJavaFormatString(FloatingDecimal.java:1890)
     at sun.misc.FloatingDecimal.parseDouble(FloatingDecimal.java:110)
     at java.lang.Double.parseDouble(Double.java:538)
     at java.text.DigitList.getDouble(DigitList.java:169)
     at java.text.DecimalFormat.parse(DecimalFormat.java:2056)
     at java.text.SimpleDateFormat.subParse(SimpleDateFormat.java:1869)
     at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1514)
     at java.text.DateFormat.parse(DateFormat.java:364)
     at top.dayu.concurrent.note.Review4.lambda$test$0(Review4.java:123)
     at java.lang.Thread.run(Thread.java:745)

     可变类如果不加保护就会出现问题

     可以使用DateTimeFormatter对象 该对象是线程安全的

     不可变对象的设计：
     String类用final修饰，防止子类修改，属性要么使用final要么不提供set方法。
     保护性拷贝：
     String的char数组构造方法，为了防止传递的引用的对象发生变化，在char构造时会拷贝一份，使其以后不能改变



    设计模式之享元模式： 当需要重用数量有限的同一类对象时
     jdk中最典型的使用享元模式就是 包装类Long.valueOf(); Integer.valueOf();
     Byte/Short/Long 如果数据在-128-127之间会从缓存中取，否则才会创建。
     Char缓存0-127 ； Boolean缓存了true和false ；Integer可以通过虚拟机参数修改缓存的最大值
     还有BigDecimal BigInteger String都使用了享元模式
     以上所有的类都是线程安全的，原子的，但是注意多个方法的组合并不是原子的不是线程安全的。
     // todo String享元模式的串池机制

     重点看 连接池的代码类 好好体会 收获颇多 {@link ConnectionPool }


     final关键字的原理： 就是在final修饰的变量赋值后，底层指令中会加入一个写屏障；
     写屏障能保证写屏障之前的指令不会被重排序到写屏障指令后面去；并且能同步变量值到内存，保证可见性。
     一个变量假如是 int i = 0 ； 那么底层会分为两步先开辟空间 int i 然后第二步设置为20
     然后多个线程访问时，有可能会出现某个线程读取到了之前的值。加上final 就不会出现这个情况了；

     */


    public static void main(String[] args) {
        test2();
    }
}

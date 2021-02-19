package top.dayu.concurrent.note;

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
        LongAdder.java java8提供的专门用来累加的，性能更高；
        原理就是会设置多个累加变量，然后再汇总，这就导致cas设置值失败的概率降低，速度更快


     cas也可以加锁 就是定义一个状态 0没加锁 1加锁 写两个方法  用来加锁和解锁修改这个状态即可
     很多底层实现代码都是这么用的，但是自己别这么写，因为有风险。









     */

    public static void main(String[] args) {

    }
}

package top.dayu.concurrent.note;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Classname Review
 * @Description 线程安全集合类
 * @Date 2021/3/6 14:50
 * @Created by ldy
 */
public class Review6 {

    /**
        //todo 装饰器模式： Collections.synchronizedMap()

     ConcurrentHashMap最全的构造方法：
     public ConcurrentHashMap(int initialCapacity,float loadFactor, int concurrencyLevel)
     initialCapacity 最后的大小，不一定是你自己指定的大小，内部会计算为2的n次方为指定的大小

     get方法，全程无锁
     put：使用的cas  如果put到链表中，算出的下标冲突后 sync会锁住整个链表

     CopyOnWriteArrayList
     CopyOnWriteArraySet 底层大部分都是使用CopyOnWriteArrayList提供的方法
     CopyOnWrite： 写入时拷贝： 增 删改 会拷贝一份，不会影响读，就能做到读写是并发的
     读读也是并发的，只有写写才是互斥的。 jdk11方法都是用sync保护的 jdk8用的reentraLock

     CopyOnWrite和Concurrent开头的类中get方法和遍历方法都会有弱一致性的问题

     高并发和一致性是相对的，需要权衡。

    */
    public void test1(){

        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.get("");
        //注意这个方法： 如果缺少则设置  相当于 get set 在一个原子的方法中完成
        //内部也是使用的sync锁，只是锁住了链表头，不是锁住整个方法 所以效率高

        //先检查有没有这个key1这个key 如果没有就连同1 一块put进去
        concurrentHashMap.computeIfAbsent("key1",(x)->1);




    }

}

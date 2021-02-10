package top.dayu.notes.java8study;

import java.util.stream.LongStream;

public class D_Parallel {

    /**

     传统的Fork／Join 框架，可以把一个大任务分成多个小任务执行完再合并。

     传统的线程池有个问题，就是用两个线程各自执行若干个任务，但是其中一个阻塞了，另一个线程没阻塞就执行完了；
     然后第一个线程恢复后开始执行（还有很多排队的任务），但是另一个线程在空闲状态，会造成资源浪费。

     Fork／Join有一个窃取模式，当某个线程空闲时会随机去另一个线程中偷一个任务来执行
     示例类 TestForkJoinPool.java

     注意：使用的时候注意临界值的设定，设的少会格外耗费拆分的时间；注意要在数据量大的时候使用，
     本机有几个cpu就会使用几个，效率提升更明显。


     * */

    public static void main(String[] args) {

        //这是传统的顺序流执行
        Long s=  LongStream.rangeClosed(0,1000000000L)
                .reduce(0,Long::sum);

        //会使用并行流执行  parallel 并行的意思
        Long l =  LongStream.rangeClosed(0,1000000000L)
                .parallel() //底层还是fork-join
                .reduce(0,Long::sum);


    }


}

package top.dayu.concurrent.note;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.dayu.concurrent.ConcurrentApplication;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @Classname Test1
 * @Description TODO
 * @Date 2021/2/10 12:13
 * @Created by ldy
 */
@SpringBootTest(classes = ConcurrentApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class Test1 {

    @Test
    public void test() throws Exception{

        //第一种方法创建线程
        log.info("12312321321"); // main线程
        Thread t = new Thread("t1") {
            @Override
            public void run() {
                log.info("qwerwqeqwe"); //  t1线程
            }
        };
        t.start();



        //第二种方法创建一个任务交给线程，这样更灵活
        Runnable run = new Runnable() { //任务对象
            @Override
            public void run() {
                log.info("runnable");
            }
        };
        //线程对象
        Thread t2 = new Thread(run);
        t2.setName("t2");
        t2.start();



        //3: lambda 简化代码
        Runnable run1 = () -> log.info("run2");
        Thread t3 = new Thread(run1,"t3");
        t3.start();

        //4: lambda 简化代码
        new Thread(() -> log.info("run3"),"t4").start();


        //5: 返回值和异常的 线程
        FutureTask<Integer>  task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {

                log.info("123123213");
                Thread.sleep(1000);
                return 100;
            }
        });
        Thread t4 = new Thread(task,"t4");
        t4.start();
        //这个get用来获取任务的返回值，运行到这当前线程会等待(阻塞)当前任务执行完
        task.get();


    }


}

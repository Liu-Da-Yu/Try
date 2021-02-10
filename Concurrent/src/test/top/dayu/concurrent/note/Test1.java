package top.dayu.concurrent.note;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.dayu.concurrent.ConcurrentApplication;

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
    public void test() {


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
        Runnable run = new Runnable() {
            @Override
            public void run() {
                log.info("runnable");
            }
        };
        Thread t2 = new Thread(run);
        t2.setName("t2");
        t2.start();






    }



}

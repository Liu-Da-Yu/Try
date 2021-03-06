package top.dayu.concurrent.temp;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Semaphore;

@Slf4j(topic = "c.TestSemaphore")
public class TestSemaphore {
    public static void main(String[] args) throws Exception{
        // 1. 创建 semaphore 对象
        Semaphore semaphore = new Semaphore(3);

        // 2. 10个线程同时运行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();//获得许可，但是只有三个
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    log.debug("running...");
                    Thread.sleep(10000);
                    log.debug("end...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //释放许可
                    semaphore.release();
                }
            }).start();
        }
    }
}

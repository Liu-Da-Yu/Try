package top.dayu.concurrent.note;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * @Classname Review2
 * @Description 视频第四章的内容
 * @Date 2021/2/11 13:17
 * @Created by ldy
 */
public class Review2 {


    /**
     临界区(多线程访问会导致问题的代码部分)竞态条件：
     阻塞式的解决方案  synchronized lock
     非阻塞式的解决方案：原子变量

     synchronize： 俗称对象锁
     synchronize(对象){//线程1进去的时候获得了一把锁，没执行完的时候线程2进不去会阻塞，状态为blocked
     //临届区    外面有锁，只有一个线程执行完毕了，才能执行下一个线程
     //参数可以随意指定一个空对象比如： static final Object lock = new Object();
     //要设置称为final的，防止在别的地方更改
     }

     注意： 1：如果某个线程在上锁的代码中运行着即使时间片用完了，他也是有钥匙的，别的线程也进不去。
     2：锁中的线程执行完代码后会交出钥匙，并且唤醒正在阻塞的想要进入的线程，
     如果有多个正在等待的线程就会同时唤醒，让cpu决定分配给哪个线程用
     3：实际上是用对象锁保证了临界区内的代码的原子性

     简化的等价写法：
     public synchronize void test(){}
     等价于
     public void test(){
     synchronize(this){
     }
     }

     public synchronize static void test(){}
     等价于
     public static void test(){
     synchronize(CurrentClass.class){
     }
     }


     成员变量/静态变量只要是共享的并且有写入的操作就需要考虑线程安全问题；
     局部变量是线程安全的，因为每个线程执行的时候都会把这个变量存到自己的线程内存中，没有发生共享；
     但局部变量引用的对象逃离了方法的作用范围，也需要考虑线程安全问题；

     除此以外都是线程安全的。

     设计多线程安全的方法的时候  尽量把方法设为final private的因为公开的或者可继承的方法会带来安全隐患。

     常见的线程安全(中的每个方法都是原子的，但注意多方法的组合不是原子的)类：
     String Integer StringBuffer Random Vector(list) hashtable(hashmap) java.util.concurrent包下的类(JUC)
     为什么String Integer线程安全？ 因为该类只能读不能改，如substring类的修改方法内部也是new的对象，所以安全
     为什么StringBuffer Random hashtable线程安全？ 因为里面的方法都是加了synchronize

     上锁的时候如果需要锁住一个类的成员变量，就锁this 如果被该类的多个实例对象引用就得对 类.class 上锁

     java对象(以32位虚拟机为例)
     普通对象：object header 64bit    MarkWord 32bit       KlassWord 32bit
     数组对象多了一个长度: array length 32bit


     //todo  synchronize底层原理 Monitor 和 偏向锁 轻量级锁 都和synchronize关键字有关
     //synchronize的底层原理就是使用操作系统提供的Monitor来上锁，Monitor是重量级的锁；
     但是使用synchronize关键字来上锁时，默认会先使用轻量级的锁，即没有线程竞争的时候，就会默认使用轻量级的锁 来优化。
     在加轻量级锁之前会优先加偏向锁，默认启动项目延迟2-3s后生成的对象才会默认开启偏向锁。如果想要不延迟，需加启动参数。

     自旋优化：jdk7以后自旋是不受控制的，就是线程在等待获取锁的时候一直自旋重试，单核时是无效的，因为自旋需要占用cpu
     偏向锁优化：只有第一次使用cas将线程id设置到对象的markWord头上，之后发现这个线程id是自己就表示没有竞争，不用重新cas
     以后只要不发生竞争，这个对象就归该线程所有。

     调用hashcode会导致撤销偏向锁；如果有新的线程进来，也会导致撤销偏向锁，转为轻量级锁。
     调用wait或者notify也会导致撤销偏向锁。因为只有重量级锁才能使用这两个方法。

     jvm有一个即时编译器叫jit 可以优化代码，其中的一个优化就是，如果发现加锁的对象是一个局部方法，
     就说明该对象不会被共享，干脆去掉synchronize。这叫 锁消除 默认是开启的。

     */

    //注意: wait方法： 只有获得锁的线程才能调用wait方法
    static final Object Lock = new Object();

    public void test3() {
        new Thread(() -> {
            try {
                //必须获得锁才能调用等待方法
                synchronized (Lock) {
                    Lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1").start();
    }

    /**
     wait:让线程进入到等待状态
     notify:随机唤醒一个正在等待的线程
     notifyAll:唤醒所有正在等待的线程
     都得获得锁了以后(synchronize内部)，才能调用

     sleep 和  wait
     sleep： 是线程的方法；不强制和sync一块使用；睡眠时不会释放对象锁
     wait： 是锁对象的方法；强制和sync一块使用；等待时会释放对象锁
     共同点：wait和sleep的状态都是timed_waiting

     wait/notifyAll 组合使用的正确姿势：
     while(条件不成立){lock.wait();}  //干活的代码
     //另外一个叫醒的线程使用notifyAll叫醒


     同步模式之保护性暂停：
     一个线程使用一个线程的结果使用Guarded Suspension（join的底层用的就是这个）
     如果结果不断的从一个线程到一个线程就使用消息队列（生产者/消费者）

     实现的桥梁就是GuardedObject(保护对象) 见以下代码示例：
     */

    private void testGuardedObject() {

        //模拟使用GuardedObject
        GuardedObject guardedObject = new GuardedObject(1);
        new Thread(() -> {
            Object o = guardedObject.get(1);
            System.out.println("另一个线程执行完获得的结果是：" + o);
        }, "t1").start();

        new Thread(() -> {
            //比如执行一段读取文件的代码，当前线程就会进入阻塞状态，
            //执行完以后，会把结果放到guardedObject中，里面的方法会唤醒使用结果的线程
            guardedObject.complete("读取完文件得到的结果");
        }, "t2").start();

    }


    public static void main(String[] args) {
        //测试GuardedObject  这种设计模式就是一一对应的
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Integer id : MailBoxes.getIds()) {
            new Postman(id, "内容为：" + id).start();
        }
    }
}

//正确的使用wait/notify来构造一个桥梁用来传输线程间的结果
//还可以增加一个超时等待的配置。( 详见json的底层代码，几乎一摸一样)
class GuardedObject {
    private Object response;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GuardedObject(int id) {
        this.id = id;
    }

    public Object get(long timeout) {
        synchronized (this) {
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}

//用来解耦
class MailBoxes {

    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();

    private static int id = 1;

    private static synchronized int generateId() {
        //对MailBoxes类的所有实例都加锁，产生唯一的id
        return id++;
    }

    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        // boxes = new Hashtable 所以put方法是线程安全的
        boxes.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }

    public static GuardedObject getGuardedObject(int id) {
        //注意这个地方使用remove而不是get，因为留着也占内存，不如删掉
        return boxes.remove(id);
    }


}


class People extends Thread {
    @Override
    public void run() {
        //收信
        GuardedObject go = MailBoxes.createGuardedObject();
        Object o = go.get(5000);

    }
}

class Postman extends Thread {
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        //送信
        GuardedObject guardedObject = MailBoxes.getGuardedObject(id);
        guardedObject.complete(mail);

    }
}

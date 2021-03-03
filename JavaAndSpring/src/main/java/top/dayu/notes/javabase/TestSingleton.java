package top.dayu.notes.javabase;

import java.io.Serializable;

/**
 * @Classname TestSingleton
 * @Description 用来实现一个单例模式
 * @Date 2021/2/27 13:32
 * @Created by ldy
 */
public final class TestSingleton implements Serializable {

    private TestSingleton(){
    }

    private static final TestSingleton INSTANCE = new TestSingleton();

    public static TestSingleton getInstance(){
        return INSTANCE;
    }

    public Object readResolve(){
        return INSTANCE;
    }


    /**
     类加final 防止继承重写方法
     构造器私有，防止new对象
     防止反序列化
     static修饰可以防止创建时的线程安全
    */

}

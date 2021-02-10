package top.dayu.notes.java8study;

import top.dayu.temp.*;

import java.io.PrintStream;
import java.util.*;
import java.util.function.*;

/**
 * @Classname TestLambda2
 * @Description lambda 表达式的使用2 （ 核心内置接口，用来解决，需要自己创建函数式接口的问题 ）
 * @Date 2020/6/15 14:50
 * @Author by ldy
 */
@SuppressWarnings("all")
public class B_Lambda2 {

    static  List<Employee> employees= Arrays.asList(
            new Employee("张三",23,10000),
            new Employee("李四",33,20000),
            new Employee("王五",43,30000)
    );

    public static void main(String[] args) {
        testLambda1();
    }

    public static void testLambda1() {

        /**
            四大内置核心函数式接口
            1. Consumer<T>:   消费型接口        void accept(T t)
                 [kənˈsjuːmə(r)] 消费者;顾客;用户
            2. Supplier<T> :  供给型接口        T get()
                 [səˈplaɪə(r)]  供应者;供货商;供货方
            3. Function<T,R>: 函数型接口        R apply(T t)
                 [ˈfʌŋkʃn]  作用;功能;职能;机能;
            4. Predicate<T>:  断言(判断)型接口   boolean test(T t)
                 [ˈpredɪkət] 谓语  使基于;使以…为依据;表明;阐明;断言
            5. 其他的查找文档使用
         */

        //消费型->花钱
        happy(10000D, (m) -> System.out.println(m) );

        //供给型->产生指定个数的整数，并放入集合中 (例如产生10个随机数)
        List<Integer> numList = getNumList(10, () -> (int) (Math.random() * 100));
        for (Integer integer : numList) {
            System.out.println(integer);
        }

        //函数型->(用于处理字符串,去除空格)
        String s = strHandle(" das  ", (str) -> str.trim());
        System.out.println(s);

        //断言型->(满足条件的字符串添加到集合中)
        List<String> strings = Arrays.asList("qwe", "rtyu");
        List<String> strings1 = filterStr(strings, (ss) -> ss.length() > 3);
        System.out.println(strings1);
    }

    public static void testLambda2() {

        PrintStream printStream = System.out;

        //TODO: 如果lambda体中已经有别的方法实现了，可以使用 传递方法

        //对象::方法名
        Consumer<String> consumer1 = (x) -> printStream.println(x);

        //使用printStream实例的println方法()
        Consumer<String> consumer2 = printStream::println;

        //静态类名::方法名
        Comparator<Integer> consumer3 = (x,y) -> Integer.compare(x,y);
        Comparator<Integer> consumer4 = Integer::compare;

        //比较两个字符串是不是相等 Predicate 是传入一个参数，返回boolean，他的子类可以传入两个参数
        // BiPredicate<T,U>
        BiPredicate<String,String> biPredicate1 = (x,y) -> x.equals(y);
        BiPredicate<String,String> biPredicate2 = String::equals;

        //ClassName:new

    }

    public static  List<String> filterStr ( List<String> str , Predicate<String> predicate){
        List<String> list = new ArrayList<>();
        for (String s : str) {
            //重点部分： 只有用来这个test，才能自定义灵活的在调用的时候 来动态的判断它，这个部分好好的理解一下。
            if( predicate.test(s) ) {
                list.add(s);
            }
        }
        return list;
    }

    public static String strHandle (String str , Function<String,String> function){
        return function.apply(str);
    }

    public static List<Integer> getNumList (int num , Supplier<Integer> sup ){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(sup.get());
        }
        return list;
    }

    public static void happy (double money , Consumer<Double> con ){
        con.accept(money);
    }


}

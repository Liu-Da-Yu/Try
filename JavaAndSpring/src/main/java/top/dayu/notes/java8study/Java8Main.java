package top.dayu.notes.java8study;

import top.dayu.temp.Employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @Classname Java8Main
 * @Description 来记录java8的学习笔记
 * @Date 2020/6/15 14:49
 * @Author by ldy
 */
@SuppressWarnings("all")
public class Java8Main {


    /**
     “流（ stream）是什么？”这是Java 8的一个新功能。它们的特点和集合（ collection）差不多，但有几个明显的优点，让我们可以使用新的编程风格。
     首先，如果你使用过SQL等数据库查询语言，就会发现用几行代码写出的查询语句要是换成Java要写好长。
     Java 8的流支持这种简明的数据库查询式编程——但用的是Java语法，而无需了解数据库！
     其次，流被设计成无需同时将所有的数据调入内存（甚至根本无需计算），这样就可以处理无法装入计算机内存的流数据了。但Java 8可以对流做一些集合所不能的优化操作，
     例如，它可以将对同一个流的若干操作组合起来，从而只遍历一次数据，而不是花很大代价去多次遍历它。更妙的是， Java可以自动将流操作并行化（集合可不行）。

     “还有函数式编程，这又是什么？”就像面向对象编程一样，它是另一种编程风格，其核心是把函数作为值，前面在讨论Lambda的时候提到过。

     C和C++仍然是构建操作系统和各种嵌入式系统
     的流行工具，因为它们编出的程序尽管安全性不佳，但运行时占用资源少。缺乏安全性可能导致
     程序意外崩溃，并把安全漏洞暴露给病毒和其他东西；确实， Java和C#等安全型语言在诸多运行
     资源不太紧张的应用中已经取代了C和C++。

     1: 速度更快

     hashmap ：
     1.8以前每添加一个元素都要比较集合中所有的判断是不是一样，如果集合中有10000个数据，新添加一个以后，要equels10000次。
     效率低，后来把对象做哈希运算，然后计算成数组的索引，然后如果该位置是空的，直接放进去，如果不是空的，就比较是否一样，如果不一样，会形成链表，存储多个。
     但是索引位置比较少，总是会重复称为碰撞，所以提供了一个加载因子，默认0.75，到了75%的时候就扩容，减少发生碰撞的次数。
     扩容以后，会把里面所有的数据都重新运算，重新放到别的位置。默认数组容量为16.

     1.8以后做了一个改变 以前是数组+链表，现在是数组+链表+红黑树。
     当碰撞的个数大于8的时候，即数组中的某个位置里面放了8个链表的时候，并且总元素大于64个长度时，会把原来的链表转为红黑树。
     红黑树除了添加以外，其余都比链表快。添加时，大的放右边，需要比较，比链表慢，因为链表直接放。
     并且如果遇到扩容，就不需要把树中的元素重新都执行哈希算法算位置了。

     */


    /**
     * 四大内置核心函数式接口
     * 1. Consumer<T>:   消费型接口        void accept(T t)
     * 2. Supplier<T> :  供给型接口        T get()
     * 3. Function<T,R>: 函数型接口        R apply(T t)
     * 4. Predicate<T>:  断言(判断)型接口   boolean test(T t)
     * 5. 其他的查找文档使用
     */

    static List<Employee> employees = Arrays.asList(
            new Employee("张三", 23, 10000),
            new Employee("李四", 33, 20000),
            new Employee("王五", 43, 30000)
    );

    public static void main(String[] args) {

        //TODO 获取集合中年龄大于23岁的员工信息
        List<Employee> employeeByAge = getEmployeeByAge(employees, (x) -> x.getAge() > 30);

        //TODO 获取集合中工资大于10000的员工信息
        List<Employee> employeeBySalary = getEmployeeByAge(employees, (x) -> x.getSalary() > 10000);

    }

    // 主要是为了过滤通用的条件，想要过滤数据传个条件给我就行
    static List<Employee> getEmployeeByAge(List<Employee> employees, Predicate<Employee> predicate) {
        List<Employee> data = new ArrayList<>();
        for (Employee employee : employees) {
            if (predicate.test(employee)) {
                data.add(employee);
            }
        }
        return data;
    }


}

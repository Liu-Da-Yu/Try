package top.dayu.notes.java8study;

import top.dayu.temp.Employee;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Classname TestStream1
 * @Description Stream Api 的学习 （数据可以像sql一样操作）
 * @Date 2020/6/19 16:36
 * @Author by ldy
 */
@SuppressWarnings("all")
public class C_StreamAPI1 {

    static List<Employee> employees = Arrays.asList(
            new Employee("张三", 23, 10000),
            new Employee("李四", 33, 20000),
            new Employee("王五", 43, 30000)
    );

    static List<String> strs = Arrays.asList(
            "aaa", "bbb", "ccc"
    );

    public static void main(String[] args) {
        //new TestStreamAPI1().testStream();
        //new TestStreamAPI1().testStream1();
        //new TestStreamAPI1().testMap();
        new C_StreamAPI1().test2();
    }

    /**
     * “流（ stream）是什么？”这是Java 8的一个新功能。
     * 它们的特点和集合（ collection）差不多，但有几个明显的优点，让我们可以使用新的编程风格。
     * 首先，如果你使用过SQL等数据库查询语言，就会发现用几行代码写出的查询语句要是换成Java要写好长。
     * Java 8的流支持这种简明的数据库查询式编程——但用的是Java语法，而无需了解数据库！
     * 其次，流被设计成无需同时将所有的数据调入内存（甚至根本无需计算），这样就可以处理无法装入计算机内存的流数据了。
     * 但Java 8可以对流做一些集合所不能的优化操作，
     * 例如，它可以将对同一个流的若干操作组合起来，从而只遍历一次数据，而不是花很大代价去多次遍历它。
     * 更妙的是， Java可以自动将流操作并行化（集合可不行）。
     * <p>
     * “还有函数式编程，这又是什么？”就像面向对象编程一样，它是另一种编程风格，
     * 其核心是把函数作为值，前面在讨论Lambda的时候提到过。
     * <p>
     * C和C++仍然是构建操作系统和各种嵌入式系统
     * 的流行工具，因为它们编出的程序尽管安全性不佳，但运行时占用资源少。缺乏安全性可能导致
     * 程序意外崩溃，并把安全漏洞暴露给病毒和其他东西；确实， Java和C#等安全型语言在诸多运行
     * 资源不太紧张的应用中已经取代了C和C++。
     * <p>
     * 指定数据源： 集合或数组
     * StreamAPi 在流的传输过程中做一系列的操作（过滤,映射，分组，切片），不会改变数据源的数据，操作完会产生一个新的流
     * 1:创建流（或者说转换为流）     2：操作    3：关闭，生成新的流
     *
     */


    private void test() {

        //创建的四种方式：

        //1: 通过Collection系列集合提供的stream()方法 或者 parallelStream()(并行流)
        strs.stream();

        //2: Arrays.stream(Obj);
        Arrays.stream(new String[]{"", ""});

        //3: Stream.of(); Stream.iterate(); Stream.generate();

        //forEach 需要一个消费型的 Consumer<? super T> action
        strs.stream().forEach(System.out::println);
    }


    private void testStream() {
        Stream<Employee> stream = employees.stream();

        //创建无限流  种子(起始值) , 一元运算
        //迭代
        Stream<Integer> stream4 = Stream.iterate(0, (x) -> x + 2);
        stream4.limit(10).forEach(System.out::println);

        //生成
        Stream.generate(() -> Math.random()).limit(10).forEach(System.out::println);

        stream.filter((x) -> x.getAge() > 10);
    }

    //2:操作
    private void testStream1() {

        /**
         筛选与切片
         * filter : 排除某些元素
         * limit： 截断，使返回的元素不超过给定数量
         * skip： 跳过
         * dictint： 通过流的生成元素的hashCode和equals 去重 （Employee 需要重写这两个方法，才能去重）
        */

        Stream<Employee> stream = employees.stream();

        // 过滤，比如年龄大于35 （必须要有终止操作,否则只有中间操作不会有任何结果(也不执行)
        // 在终止的时候才会一次性处理所有结果，这个叫做惰性求值，延迟加载）
        stream.filter((e) -> {
            System.out.println(e);
            return e.getAge() > 35;
        }).forEach(System.out::println);

        // filter需要传输一个判断/断言式的lamnda表达式(判断符合条件的就会排除掉)
        // Stream<T> filter(Predicate<? super T> predicate)

    }

    //3. 映射 (map flatMAp )
    private void testMap() {
        // map: 接受lambda，将元素转成其他形式或提取信息，接受一个函数作为参数，
        // 该函数会被映射到每个元素上，并将其映射成一个新的元素。

        // 把集合中的所有元素转为大写
        // 会把strs中的每一项都来执行一遍这个 (x) -> x.toUpperCase()
        strs.stream().map((x) -> x.toUpperCase()).forEach(System.out::println);

        //获取所有员工的名字
        employees.stream().map((x) -> x.getName()).forEach(System.out::println);

        // flatMAp flat：扁平 平铺 用法和map一样，可以操作流中的流  把里面的流中的东西拿到外层的流中
        // map flatMap 的区别就类似于 list.add 和 list.addAll
    }


    //4.排序(sorted)
    private void testSort() {

        // sorted() --> 自然排序 参考String 实现的Comparable 方法
        // sorted( Comparator com ) --> 定制排序
        employees.stream().sorted((e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return Integer.compare(e1.getAge(), e2.getAge());
            }
        }).forEach(System.out::println);

    }

    /**
     * 终止操作： 查找与匹配
     * allMatch - 检查是否匹配所有元素
     * anyMatch -  检查是否至少匹配一个元素
     * noneMatch -  检查是否没有匹配所有元素
     * findFirst -  返回第一个元素
     * findAny -  返回当前流中的任意元素
     * count - 返回流中元素的总个数
     * max -  返回流中的最大值
     * min -  返回流中的最小值
     */
    private void test12 () {
        Optional<Employee> first = employees.stream()
                .sorted((x, y) -> Double.compare(x.getSalary(), y.getSalary()))
                .findFirst();
        // first.orElse() 如果为空，则找个替代的 类似于sql中的ifnull
        // first.get() 获取出来
    }


    private void test2() {
        /**
         规约： reduce  将流中的元素反复结合起来，得到一个值
         * */

        //求数组元素的累加和
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        // 起始值 和 二元运算
        Integer reduce = list.stream().reduce(0, (x, y) -> x + y);
        System.out.println(reduce); // 55

        // reduce的计算过程： 先把起始值0作为第一个x，然后从数组中取出一个y 相加，
        // 然后把得到的值当作了x，再取出取出一个值作为y相加，得到的结果作为x再取....

        //计算所有员工工资总和(先得到所有员工的工资，然后做累加)(二者经常一块使用，称为map-ruduce)
        //employees.stream().map(e -> e.getSalary()).reduce( (x,y) -> Double.sum(x,y) );
        Optional<Double> op = employees.stream().map(e -> e.getSalary()).reduce(Double::sum);
        //只要有可能为空的值都会返回Optional
        System.out.println(op.get());


        //收集 collect ： 接受一个Collector接口的实现，用于给Stream中的元素做汇总方法
        //案例：收集所有员工的姓名到list集合中
        List<String> collect = employees.stream().
                map(Employee::getName).collect(Collectors.toList());

        collect.forEach(System.out::println);

        //收集到其他的集合
        HashSet<String> collect1 = employees.stream().
                map(Employee::getName).collect(Collectors.toCollection(HashSet::new));


        //取所有工资的平均值(最大，最小，总数，总和)
        employees.stream().collect(Collectors.averagingDouble(Employee::getSalary));

        //获取员工中最高的工资
        employees.stream().
                collect(Collectors.maxBy((x, y) -> Double.compare(x.getSalary(), y.getSalary())));

        //根据年龄分组（可以是先年龄再按工资分，就把工资放在age参数后，可以是无限多个）
        employees.stream().collect(Collectors.groupingBy(Employee::getAge));

        //多级分组
        employees.stream().
                collect(
                        Collectors.groupingBy(
                                Employee::getAge,
                                Collectors.groupingBy(
                                        (x) -> {
                                            if (x.getSalary() < 5000) {
                                                return "低收入人群";
                                            } else {
                                                return "高收入人群";
                                            }
                                        }
                                )));


        //分区(满足条件的一个区，不满足条件的一个区 Collectors.partitioningBy(条件)

        //获取集合中的聚合操作
        DoubleSummaryStatistics statistics = employees.stream().collect(Collectors.summarizingDouble(Employee::getSalary));
        statistics.getAverage();
        statistics.getCount();
        statistics.getMin();

        //连接集合中指定的所有字符串 Collectors.joining
        String str = employees.stream()
                .map(Employee::getName).collect(Collectors.joining(",", "头部", "尾部"));


    }


}


















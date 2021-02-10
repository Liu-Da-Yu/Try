package top.dayu.notes.java8study;

import top.dayu.temp.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @Classname TestLambda1
 * @Description lambda 表达式的使用 （基础部分）
 * @Date 2020/6/15 14:50
 * @Author by ldy
 */
@SuppressWarnings("all")
public class B_Lambda1 {

    /**

     箭头操作符 -> ( 表达式的参数列表 , 表达式需要执行的功能 )
     语法格式
     1: 无参数 无返回值 () -> { }  test1
     2: 一个参数 无返回值          test2
     3: 多个参数 又返回值 lambda体中有多条语句 test3

     所以 Lambda 表达式需要函数式接口的支持（接口中只有一个抽象方法的接口）
     使用@FunctionalInterface 来修饰类，可以检查是否满足函数式接口

     */

    public void test1() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("");
            }
        };

        Runnable runnable1 = () -> System.out.println(1);

        //runnable.run();

    }


    public void test2() {

        Consumer<String> consumer = (x) -> System.out.println(x);
        consumer.accept("直接就打印出来了!");

    }

    public void test3() {

        // int compare(T o1, T o2)
        Comparator<Integer> comparator = (x, y) -> {
            System.out.println(" 有多条语句，要加个{} ");
            return Integer.compare(x, y);
        };

        //如果只有1条语句  return 和 {}  都可以省略不写
        Comparator<Integer> comparator1 = (x, y) -> Integer.compare(x, y);

    }


    /**
     * 四大内置核心函数式接口
     * 1. Consumer<T>:   消费型接口        void accept(T t)
     * 2. Supplier<T> :  供给型接口        T get()
     * 3. Function<T,R>: 函数型接口        R apply(T t)
     * 4. Predicate<T>:  断言(判断)型接口   boolean test(T t)
     * 5. 其他的查找文档使用
     */


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


    //准备一个有数据的集合
    static List<Employee> employees = Arrays.asList(
            new Employee("张三", 23, 10000),
            new Employee("李四", 33, 20000),
            new Employee("王五", 43, 30000)
    );


    public static void main(String[] args) {
        testLambda1();
        testLambda2();

        //TODO 获取集合中年龄大于23岁的员工信息
        List<Employee> employeeByAge = getEmployeeByAge(employees, (x) -> x.getAge() > 30);

        //TODO 获取集合中工资大于10000的员工信息
        List<Employee> employeeBySalary = getEmployeeByAge(employees, (x) -> x.getSalary() > 10000);


    }

    public static void testLambda1() {

        /**
         1: Integer.compare(x,y) === x.compareTo(y)
         并且： 对象大于目标参数,返回1   对象小于目标参数,返回-1   对象等于目标参数,返回0
         */

        //TODO 1. 传统的比较器，匿名函数
        Comparator<Integer> com1 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };

        //创建一个TreeSet，指定比较排序方法。
        TreeSet<Integer> set1 = new TreeSet<>(com1);

        //使用Lambda表达式
        Comparator<Integer> com2 = (x, y) -> Integer.compare(x, y);
        TreeSet<Integer> set2 = new TreeSet<>(com2);

        //TODO 2. 获取当前公司中员工年龄>35的员工信息  （ Arrays.asList(): 数组转集合 ）

        //传统方法: 写一个方法1，方法中遍历集合，age属性大于35的放进新集合，遍历结束后返回
        //如果过滤 工资大于10000的还得再写个 方法2 方法中遍历集合，salary属性大于10000的放进新集合，遍历结束后返回

        /**
         优化；最好的优化代码的方法 就是设计模式 （策略模式）
         新建一个接口MyPredicate,里面定义一个boolean的方法，然后写几个过滤类 实现这个接口，在每个过滤类中实现方法，然后编写自己的过滤规则
         再使用的时候,只需要写一个过滤的方法，传入要过滤的数组和按照什么规则过滤（写了过滤规则的子类）

         给什么策略按照什么策略过滤（age>35  or salary>10000）
         策略模式把对象本身和运算规则区分开来，因此我们整个模式也分为三个部分。
         环境类(Context): 用来操作策略的上下文环境，也就是我们游客。
         抽象策略类(Strategy): 策略的抽象，出行方式的抽象
         具体策略类(ConcreteStrategy): 具体的策略实现，每一种出行方式的具体实现。

         */

        //按照年龄过滤 >35   FilterEmployeeByAge类中定义了过滤规则
        List<Employee> filterEmp1 = filterEmployee(employees, new FilterEmployeeByAge());

        //按照工资过滤 >10000   FilterEmployeeBySalary类中定义了过滤规则
        List<Employee> filterEmp2 = filterEmployee(employees, new FilterEmployeeBySalary());

        //继续优化 ---> 上文中麻烦的地方在于 每实现一种过滤需要创建一个MyPredicate的子类 优化可以使用匿名内部类
        //按照工资过滤 > 20000 (不用创建过滤规则类了)
        List<Employee> filterEmp3 = filterEmployee(employees, new MyPredicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getSalary() > 20000;
            }
        });

        //继续使用lambda 优化
        List<Employee> filterEmp4 = filterEmployee(employees, (e) -> e.getSalary() > 20000);
        filterEmp4.forEach(System.out::println);

        //*** 继续优化(工资大于20000，取前两条) （ stream API ）
        employees.stream()
                .filter((e) -> e.getSalary() > 20000)
                .limit(2)
                .forEach(System.out::println);

        //*** 获取集合中的所有人的名字并打印
        employees.stream()
                .map(Employee::getName)
                .forEach(System.out::println);

    }

    public static void testLambda2() {

        /**


         需求，要对一个数值，进行运算 （可能是平方，可能是扩大2倍 .... )
         首先创建一个函数式接口CalcValueInterface.java,用@FunctionalInterface标注，
         然后创建一个计算的接口(对所有计算过程的抽象),然后创建一个方法用来定义计算过程
         System.out.println(calcValue( 1000 , (x) -> (x * x ) ));
         System.out.println(calcValue( 1000 , (x) -> (x * 2 ) ));
         */

        // eg. 把员工信息表集合排序，先按年龄排序，再按照名称排序
        Collections.sort(employees, (e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            }
            return Integer.compare(e1.getAge(), e2.getAge());
        });

        //两个lang类型的参数
        op(12L, 13L, (x, y) -> x * y);

    }

    public static void op(Long l1, Long l2, MyFunction2<Long, Long> mf) {
        System.out.println(mf.getValue(l1, l2));
    }


    public static Integer calcValue(Integer value, CalcValueInterface i) {
        return i.cala(value);
    }

    public static List<Employee> filterEmployee(List<Employee> employees, MyPredicate<Employee> myPredicate) {
        List<Employee> emps = new ArrayList<>();
        for (Employee employee : employees) {
            if (myPredicate.test(employee)) {
                emps.add(employee);
            }
        }
        return emps;
    }

}

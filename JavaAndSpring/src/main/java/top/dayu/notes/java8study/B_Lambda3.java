package top.dayu.notes.java8study;

import top.dayu.temp.Employee;
import top.dayu.temp.MyFunction;
import top.dayu.temp.MyFunction2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Classname TestLambda2
 * @Description lambda 表达式的练习
 * @Date 2020/6/15 14:50
 * @Author by ldy
 */
@SuppressWarnings("all")
public class B_Lambda3 {

    public static void main(String[] args) {
        //new B_Lambda3().test1();
        new B_Lambda3().test3();
    }

    static  List<Employee> employees= Arrays.asList(
            new Employee("张三",23,10000),
            new Employee("李四",44,20000),
            new Employee("王五",79,30000),
            new Employee("李六",43,30000)
    );

    //先按照年龄排 年龄相同按照姓名排序
    public void test1(){
        Collections.sort(employees,(x,y) -> {
            if(x.getAge() == y.getAge()){
                return x.getName().compareTo(y.getName());
            }else{
                return Integer.compare(x.getAge(),y.getAge());
            }
        });

        for (Employee employees : employees) {
            System.out.println(employees);
        }

    }

    //用于处理字符串的方法
    public String strHandle(String str , MyFunction myFunction){
        return myFunction.getValue(str);
    }

    public void test2(){
        String str = strHandle("\t\t\t 天青色等烟雨，而我在等你 ", (s) -> s.trim());
        System.out.println(str);
    }


    public Long longHandle(long l1,long l2 , MyFunction2<Long,Long> myFunction){
        return myFunction.getValue(l1,l2);
    }

    public void test3(){
        System.out.println(longHandle(100,200,(x,y) -> x*y));
    }


}

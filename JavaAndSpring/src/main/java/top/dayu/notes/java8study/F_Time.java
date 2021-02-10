package top.dayu.notes.java8study;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * @Classname F_Time
 * @Description TODO
 * @Date 2021/2/6 20:34
 * @Created by ldy
 */
public class F_Time {

    /**
        之前的Date SimpleDateFormat 等很多方法都过期了，并且写的不太规范，
        并且都是线程不安全的，所以java8对此做了改进， 新加的方法和类都在time 包下。
    */


    public static void main(String args[]){
       new F_Time().testLocalDateTime();

    }

    public void testLocalDateTime(){

        // 获取当前的日期时间
        LocalDateTime currentTime = LocalDateTime.now();

        System.out.println("当前时间: " + currentTime); //当前时间: 2021-02-07T11:03:47.582

        LocalDate date1 = currentTime.toLocalDate();
        System.out.println("date1: " + date1); //date1: 2021-02-07

        Month month = currentTime.getMonth();
        int day = currentTime.getDayOfMonth();
        int seconds = currentTime.getSecond();

        System.out.println("月: " + month +", 日: " + day +", 秒: " + seconds);//月: FEBRUARY, 日: 7, 秒: 47

        //修改当前时间的一部分属性 比如年份和月份 其他的不变
        LocalDateTime date2 = currentTime.withDayOfMonth(10).withYear(2012);
        System.out.println("date2: " + date2);//date2: 2012-02-10T11:03:47.582

        // 构造一个时间 年月日
        LocalDate date3 = LocalDate.of(2014, Month.DECEMBER, 12);
        System.out.println("date3: " + date3);//date3: 2014-12-12

        // 构造一个时间 时分
        LocalTime date4 = LocalTime.of(22, 15);
        System.out.println("date4: " + date4);//date4: 22:15

        // 解析字符串
        LocalTime date5 = LocalTime.parse("20:15:30");
        System.out.println("date5: " + date5);//date5: 20:15:30

    }



}

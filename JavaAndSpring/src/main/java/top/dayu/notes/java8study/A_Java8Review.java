package top.dayu.notes.java8study;

import java.util.Comparator;

/**
 * @Classname A_Java8Review
 * @Description 来记录java8的学习笔记 --> 哔哩哔哩尚硅谷的JAVA8新特性课程
 * @Date 2020/6/15 14:49
 * @Author by ldy
 */
@SuppressWarnings("all")
public class A_Java8Review {

    /**

    java8的新特性:


    1: 速度更快

        hashmap:
        1.8以前每添加一个元素都要比较集合中所有的判断是不是一样，如果集合中有10000个数据，
        新添加一个以后，要equels10000次。效率低，后来把对象做哈希运算，然后计算成数组的索引，
        然后如果该位置是空的，直接放进去，如果不是空的，就比较是否一样，如果不一样，会形成链表，存储多个。
        但是索引位置比较少，总是会重复称为碰撞，所以提供了一个加载因子，默认0.75，
        到了75%的时候就扩容，减少发生碰撞的次数。扩容以后，会把里面所有的数据都重新运算，重新放到别的位置。
        默认数组容量为16.

        1.8以后做了一个改变 以前是数组+链表，现在是数组+链表+红黑树。
        当碰撞的个数大于8的时候，即数组中的某个位置里面放了8个链表的时候，
        并且总元素大于64个长度时，会把原来的链表转为红黑树。
        红黑树除了添加以外，其余都比链表快。添加时，大的放右边，需要比较，比链表慢，因为链表直接放。
        并且如果遇到扩容，就不需要把树中的元素重新都执行哈希算法算位置了。

    2: 代码更少 增加了新的语法lambda表达式
    3: 强大的streamAPI
    4: 便于并行
    5: 最大化减少空指针异常 Optional

    */

    //代码简化（比如排序 原来是匿名内部类，现在一行代码搞定）
    public void testSort1(){
        Comparator<Integer> comparator1 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                //其实有用的代码就这一句
                return Integer.compare(o1,o2);
            }
        };

        Comparator<Integer> comparator2 = (x,y) -> Integer.compare(x,y);
    }

}

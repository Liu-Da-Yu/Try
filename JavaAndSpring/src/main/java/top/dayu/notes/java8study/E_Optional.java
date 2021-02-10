package top.dayu.notes.java8study;

//java8尽量避免空指针的类
@SuppressWarnings("all")
public class E_Optional {

    public static void main(String[] args) {

    }

    /**


     Optional.of  如果传入的值存在，就返回包含该值的Optional对象，否则就抛出NullPointerException异常
     empty	  放回一个值为空的Optional实例
     filter	  如果值存在并且满足提供的谓词，就返回包含该Optional对象；否则返回一个空的Optional对象
     flatMap  如果值存在，就对该值执行提供的mapping函数，将mapping函数返回值用Optional封装并返回，否则就返回一个空的Optional对象
     get	  如果值存在就返回该Optional对象，否则就抛出一个 NoSuchElementException异常
     boolean ifPresent	如果值存在就对该值执行传入的方法，否则就什么也不做
     isPresent	如果值存在就返回true，否则就返回false
     map   如果值存在，就对该值执行提供的mapping函数调用，将mapping函数返回值用Optional封装并返回
     ofNullable	 如果传入的值存在，就返回包含该值的Optional对象，否则返回一个空的Optional对象
     orElse	     如果值存在就将其值返回，否则返回传入的默认值
     orElseGet	 如果值存在就将其值返回，否则返回一个由指定的Supplier接口生成的值
            *** 可以自定义条件 比较灵活 重点的试一试

     orElseThrow 如果值存在就将其值返回，否则返回一个由指定的Supplier接口生成的异常


     * */

}

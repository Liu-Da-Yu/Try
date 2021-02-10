package top.dayu.notes.javabase;

/**
 * @Description 用来记录java基础知识的学习笔记
 * @Date 2020/6/10 11:14
 * @Author by ldy
 */

/**
 接口的幂等性：幂等性指多次操作对系统产生的影响与一次操作相同。
 多次查询（get），多次更新（put），多次删除（delete）对系统的影响都是一样的，所以都是幂等的；而新增post是不幂等的，因为重复添加系统会出现重复的数据。
 无论是微服务中各个子系统相互之间的调用，还是客户端对服务端的调用，都存在网络延迟等问题，会导致重复请求接口，这时候接口就需要支持幂等性，来防止出现问题。
 最经典的一个例子就是订单支付操作，假如因为网络问题等因素导致用户重复提交，这时候不可能对用户重复扣款。
 那么服务端接口对于幂等性应该如何支持呢？有如下两个思路：
 1. 逻辑判断处理
 支付时对订单状态进行判断，如果该订单已支付，则不应该再次进行扣款操作。
 2. 请求带ticket
 异步请求获取ticket，此ticket是唯一并且一次性的，保存在页面中，每次发起支付请求都带上ticket，后端检查ticket，若支付成功则删除ticket，这样就算重复提交也不会导致重复扣款。

 */


public class JavaBaseMain {

}

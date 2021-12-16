package top.dayu.concurrent.temp;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * @Classname Test
 * @Description TODO
 * @Date 2021/2/27 10:41
 * @Created by ldy
 */
public class Test {

    static volatile int number = 0 ;
    static Object lock = new Object();

    public static void main(String[] args) throws Exception{

        String result = "{\"param\":\"{\"code\":\"0\",\"message\":\"成功\",\"result\":[{\"claimStatus\":\"审核中\"}]}\",\"success\":\"Y\"}";
        String ss = StringEscapeUtils.unescapeJava(result);
        System.out.println("result:" + ss );

    //todo happens-before
        //todo volatile关键字
        //https://baijiahao.baidu.com/s?id=1663045221235771554&wfr=spider&for=pc


    }

}

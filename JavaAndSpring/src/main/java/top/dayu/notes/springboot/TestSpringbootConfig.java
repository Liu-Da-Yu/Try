package top.dayu.notes.springboot;

import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("all")
public class TestSpringbootConfig {

    /**
         <!-- 之前xml配置连接池 -->
         <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
            init-method="init" destroy-method="close">
                <property name="url" value="${jdbc.url}" />
                <property name="username" value="${jdbc.username}" />
                <property name="password" value="${jdbc.password}" />
         </bean>
        现在通过java配置

     @Configuration ： 声明该类是配置类
     @Bean ：配置的bean，并且加入spring容器

     */

    /*@Bean
    public DataSource dataSource (){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("");
        druidDataSource.setPassword("");
        druidDataSource.setName("");
        druidDataSource.setDriverClassName("");
        return druidDataSource;
    }*/

    //最简单的方法，会自己根据该类的set方法映射对应的属性
    /*@Bean
    @ConfigurationProperties( prefix = "jdbc" )
    public DataSource dataSource () {
        return new DruidDataSource();
    }*/






}

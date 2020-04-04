package com.lyj.demo.product;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/4 21:53
 * @Author: 李玉杰
 * @Description: 生产者
 */
public class SenderMessageProduct {
    public static void main(String[] args) {
        new SenderMessageProduct().senderMessage();
    }
    //生产消息->点对点方式
    public void senderMessage(){
        //1.创建连接MQ的连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //2.连接MQ的虚拟主机地址
        connectionFactory.setHost("192.168.37.128");
        //3.设置端口号(通讯端口号)
        connectionFactory.setPort(5672);
        //4.设置连接哪个虚拟主机,这里的虚拟机名称要看web界面配置的虚拟机名称前面是否有"/"
        //有就是"/",反之就是单纯的名字
        connectionFactory.setVirtualHost("ems");
        //5.设置访问虚拟主机的用户名和密码
        connectionFactory.setUsername("ems");
        connectionFactory.setPassword("ems");
        //6.获取连接对象
        try {
            Connection connection = connectionFactory.newConnection();
            //7.获取连接通道
            Channel channel = connection.createChannel();
            //8.绑定消息队列
            msgQueue(channel);
            //9.发布消息
            /**
             * 参数1：交换机名称
             * 参数2：队列名称
             * 参数3：传递消息时额外的设置
             * 参数4：发布的消息具体内容，要求是byte类型
             */
            channel.basicPublish("","hello",null,"'hello rabbitmq".getBytes());
            //10.关闭通道
            channel.close();
            //11.关闭连接
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
    public void msgQueue(Channel channel) throws IOException {
        //通道绑定消息队列
        /**
         * 参数1：绑定的消息队列的名称，如果rabbitmq的web界面没有创建该消息队列，可以通过代码直接创建
         * 参数2：用来定义队列特性，队列是否要持久化，true：持久化，false：不持久化，
         *        持久化队列会保存在磁盘里下次启动rabbitmq时还会存在，非持久化的不会存在磁盘，下次启动就会消失
         * 参数3：是否独占队列，true：独占，false：非独占，独占队列只允许该连接的生产者使用，其他的不能使用
         * 参数4：autoDelete 是否在消息消费完后自动删除队列，true：自动删除，false：不自动删除
         * 参数5：额外附加参数
         */
        channel.queueDeclare("hello",false,false,false,null);
    }
}

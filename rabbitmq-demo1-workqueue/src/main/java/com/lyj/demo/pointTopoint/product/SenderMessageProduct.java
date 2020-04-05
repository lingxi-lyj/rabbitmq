package com.lyj.demo.pointTopoint.product;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/4 21:53
 * @Author: 李玉杰
 * @Description: 生产者
 */
public class SenderMessageProduct {
    public static void main(String[] args) throws IOException, TimeoutException {
//        new SenderMessageProduct().senderMessage();
        new SenderMessageProduct().senderMessage1();
    }
    //点对点方式-优化
    public void senderMessage1() throws IOException, TimeoutException {
        Connection connection = RabbitMqUtil.getConnection("192.168.37.128", 5672, "ems", "ems", "ems");
        Channel channel = RabbitMqUtil.getChannel(connection);
        channel.basicPublish("","hello",null,"'hello rabbitmq".getBytes());
        RabbitMqUtil.closeChannelAndConnection(connection,channel);

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
             * 参数3：传递消息时额外的设置，这里的设置可以对消息进行持久化设置
             *        MessageProperties.PERSISTENT_TEXT_PLAIN :消息持久化，重新启动rabbitmq消息不会丢失
             * 参数4：发布的消息具体内容，要求是byte类型
             */
            channel.basicPublish("","hello",MessageProperties.PERSISTENT_TEXT_PLAIN,"'hello rabbitmq".getBytes());
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
         * 参数2：用来定义队列特性，队列是否要持久化，true：持久化，false：不持久化，注意这里并不能对消息进行持久化，
         *        重新启动rabbitmq消息还是会丢失
         *        持久化队列会保存在磁盘里下次启动rabbitmq时还会存在，非持久化的不会存在磁盘，下次启动就会消失
         * 参数3：是否独占队列，true：独占，false：非独占，独占队列：队列只能由当前通道所绑定，其他不能绑定，
         *        如果有其他链接或者通道使用该队列则会直接报错。
         * 参数4：autoDelete 是否在消息消费完后队列为空是否自动删除队列，true：自动删除，false：不自动删除
         *        自动删除功能只有在消费者程序停止才能删除掉，否则不会自动删除，因为消费者一直在监听（前提
         *        是消费者不不关闭链接和通道）
         * 参数5：额外附加参数
         * 注意：生产者和消费者的绑定队列的参数类型要一致，生产者绑定为持久化，消费者同样也要绑定为持久化
         *       否则报错，因为生产者和消费者连的是同一个队列，所以队列的特性要一致
         */
        channel.queueDeclare("hello",true,false,false,null);
    }
}

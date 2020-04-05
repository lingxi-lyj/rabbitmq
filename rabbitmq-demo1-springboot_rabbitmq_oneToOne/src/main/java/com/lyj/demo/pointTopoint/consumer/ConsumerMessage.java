package com.lyj.demo.pointTopoint.consumer;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/4 22:56
 * @Author: 李玉杰
 * @Description: 消费者消费消息
 */
public class ConsumerMessage {
    public static void main(String[] args) throws IOException {
//       new ConsumerMessage().consumerMessage();
        new ConsumerMessage().consumerMessage1();
    }

    //点对点方式-优化
    public void consumerMessage1() throws IOException {
        Connection connection = RabbitMqUtil.getConnection("192.168.37.128", 5672, "ems", "ems", "ems");
        Channel channel = RabbitMqUtil.getChannel(connection);
        channel.queueDeclare("hello",false,false,false,null);
        channel.basicConsume("hello",true,new DefaultConsumer(channel){
            /**
             *
             * @param consumerTag 类似于标签
             * @param envelope  类似于消息传递过程中的信封
             * @param properties 类似于消息传递时的属性
             * @param body 消息队列中取出的消息
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费的消息是："+new String(body));
                super.handleDelivery(consumerTag, envelope, properties, body);
            }
        });
    }
    //消费者消费消息
    public void consumerMessage(){
        //1.创建连接工厂和生产者保持一致
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.37.128");
        connectionFactory.setVirtualHost("ems");
        connectionFactory.setUsername("ems");
        connectionFactory.setPassword("ems");
        connectionFactory.setPort(5672);
        try {
            Connection connection = connectionFactory.newConnection();
            //2.创建通道
            Channel channel = connection.createChannel();
             //3.通道绑定队列,这里的参数和生产者的参数含义是一样的
            channel.queueDeclare("hello",true,false,false,null);
            //4.消费消息
            /**
             * 参数1：消费消息的队列名称
             * 参数2：开启消息的自动确认机制，消息被消费者拿走了，会告诉队列，队列就会把拿走的消息
             *        从队列移除掉，但确认机制并不关心消息在消费者的业务代码(handleDelivery)中是否正常消费完
             *        如果这时候消费者这边宕机，还有些消息没消费，但事实消息已经从队列拿走，只是还没消费完，这时候
             *        消费者就会丢失消息
             * 参数3: 消费时的回调接口
             */
            channel.basicConsume("hello",true,new DefaultConsumer(channel){
                /**
                 *
                 * @param consumerTag 类似于标签
                 * @param envelope  类似于消息传递过程中的信封
                 * @param properties 类似于消息传递时的属性
                 * @param body 消息队列中取出的消息
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("消费的消息是："+new String(body));
                    super.handleDelivery(consumerTag, envelope, properties, body);
                }
            });
            //关闭通道和连接 如果不关闭通道和连接，消费者会一直监听队列中的消息,
            //如果关闭了则回调不一定不成功，当回调时，通道已关闭，所以消费者通道和连接不建议关闭
            //channel.close();
            //connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}

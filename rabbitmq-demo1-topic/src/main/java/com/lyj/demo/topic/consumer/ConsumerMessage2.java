package com.lyj.demo.topic.consumer;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 13:36
 * @Author: 李玉杰
 * @Description: 消费者基于user.#路由键
 */
public class ConsumerMessage2 {

    public static final String EXCHANGE_NAME="topics";
    public static final String EXCHANGE_TYPE="topic";
    /**
     * *：匹配一个单词
     * #：匹配多个单词
     */
    public static final String ROUTING_KEY="user.#";
    public static void main(String[] args) throws IOException, TimeoutException {
        new ConsumerMessage2().consumerMsg();
    }
    public void consumerMsg() throws IOException, TimeoutException {
        Connection connection = RabbitMqUtil.getConnection();
        Channel channel = RabbitMqUtil.getChannel(connection);
        //通道绑定交换机
        channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE);
        //创建临时队列
        String queueName = channel.queueDeclare().getQueue();
        //通道绑定队列和交换机 这里采用是动态通配符去绑定路由key
        channel.queueBind(queueName,EXCHANGE_NAME,ROUTING_KEY);
        //消费消息
        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("基于user.*路由键的消费者2："+new String(body));
                super.handleDelivery(consumerTag, envelope, properties, body);
            }
        });



    }
}

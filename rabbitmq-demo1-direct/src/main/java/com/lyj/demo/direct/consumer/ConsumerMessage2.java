package com.lyj.demo.direct.consumer;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 11:36
 * @Author: 李玉杰
 * @Description: 订阅模式（直连）（路由模式）
 * 消费者1
 */
public class ConsumerMessage2 {

    public static final String EXCHANGE_NAME="logs-direct";
    public static final String EXCHANGE_TYPE="direct";
    public static void main(String[] args) throws IOException, TimeoutException {
        new ConsumerMessage2().consumerMsg();
    }

    public void consumerMsg() throws IOException, TimeoutException {
        Connection connection = RabbitMqUtil.getConnection();
        Channel channel = RabbitMqUtil.getChannel(connection);
        //绑定交换机
        channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE);
        //创建临时队列
        String queueName = channel.queueDeclare().getQueue();
        //基于路由key绑定交换机和队列
        /**
         * 参数1：绑定的队列名称
         * 参数2：绑定的交换机名称
         * 参数3：绑定的路由key,以后只会从交换机中取error、warning、info类型的路由key消息
         */
        channel.queueBind(queueName,EXCHANGE_NAME,"error");
        channel.queueBind(queueName,EXCHANGE_NAME,"info");
        channel.queueBind(queueName,EXCHANGE_NAME,"warning");
        //消费消息
        /**
         * 参数1：队列名称
         * 参数2：自动确认机制
         */
        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("error、info、warning的消费者2："+new String(body));
                super.handleDelivery(consumerTag, envelope, properties, body);

            }
        });

    }

}

package com.lyj.demo.workqueue.consumer;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 8:44
 * @Author: 李玉杰
 * @Description: Task queue 消费者
 */
public class ConsumerMessage2 {

    public static void main(String[] args) throws IOException, TimeoutException {
        new ConsumerMessage2().consumerMsg();
    }

    public void consumerMsg() throws IOException, TimeoutException {
        Connection connection = RabbitMqUtil.getConnection();
        Channel channel = RabbitMqUtil.getChannel(connection);
        channel.queueDeclare("work",true,false,false,null);
        channel.basicConsume("work",true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2："+new String(body));
                super.handleDelivery(consumerTag, envelope, properties, body);
            }
        });

    }
}

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
 * 执行比较快的消费者
 */
public class ConsumerMessage2 {

    public static void main(String[] args) throws IOException, TimeoutException {
        new ConsumerMessage2().consumerMsg();
    }

    public void consumerMsg() throws IOException, TimeoutException {
        Connection connection = RabbitMqUtil.getConnection();
        Channel channel = RabbitMqUtil.getChannel(connection);
        channel.queueDeclare("work",true,false,false,null);
        channel.basicQos(1);//限制通道每次只能消费一条消息
        channel.basicConsume("work",false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2："+new String(body));
                /**
                 * 手动确认  参数1：确认队列中哪个消息具体消息，这里可以用envelope.getDeliveryTag()获取消息的标志
                 *                  手动确认消息标识，消息被业务代码处理完后通知队列
                 *                  消息已经被消费者成功消费了，队列就可以移除消息了，否则队列那边unack的值不等于0
                 *                  代表消息被消费者拿走，但不确认消息被消费者成功消费
                 *           参数2：是否开启多条消息同时确认 true：开启多条消息确认false 每次确认一个，
                 *                  这样能保证消息不丢失
                 */
                channel.basicAck(envelope.getDeliveryTag(),false);
                super.handleDelivery(consumerTag, envelope, properties, body);
            }
        });

    }
}

package com.lyj.demo.workqueue.consumer;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 8:44
 * @Author: 李玉杰
 * @Description: Task queue 消费者-》消费者1,2采用了能者多劳的设置
 * 执行比较慢的消费者
 */
public class ConsumerMessage1 {

    public static void main(String[] args) throws IOException, TimeoutException {
        new ConsumerMessage1().consumerMsg();
    }

    public void consumerMsg() throws IOException, TimeoutException {
        Connection connection = RabbitMqUtil.getConnection();
        Channel channel = RabbitMqUtil.getChannel(connection);
        channel.queueDeclare("work",true,false,false,null);
//        限制通道每一次只能消费一条消息，如果不加限制且队列有消息，则会一直消费到消费者这边，但消费者不一定能真正消费
        channel.basicQos(1);
        /**
         * 参数1：消费消息的队列名称
         * 参数2：开启消息的自动确认机制，消息被消费者拿走了，会告诉队列，队列就会把拿走的消息
         *        从队列移除掉，但确认机制并不关心消息在消费者的业务代码(handleDelivery)中是否正常消费完
         *        如果这时候消费者这边宕机，还有些消息没消费，但事实消息已经从队列拿走，只是还没消费完，这时候
         *        消费者就会丢失消息,所以设置为不自动确认
         * 参数3: 消费时的回调接口
         */
        channel.basicConsume("work",false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者1："+new String(body));
                try {
                    //这里加了额外限制，可以让消费者1消费慢点，但还是会和消费者2平均消费
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(),false);
                super.handleDelivery(consumerTag, envelope, properties, body);
            }
        });

    }
}

package com.lyj.demo.fanout.consumer;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 10:33
 * @Author: 李玉杰
 * @Description: 广播模式-》消费者
 * 广播模式下采用的是临时队列
 */
public class ConsumerMessage3 {

    public static void main(String[] args) throws IOException, TimeoutException {
        new ConsumerMessage3().consumerMsg();
    }
    public void consumerMsg() throws IOException, TimeoutException {
        Connection connection = RabbitMqUtil.getConnection();
        Channel channel = RabbitMqUtil.getChannel(connection);
        //通道绑定交换机
        // 也可以使用这个去绑定channel.exchangeBind()
        channel.exchangeDeclare("logs","fanout");
        //通过通道创建临时队列，这里是队列名称
        String queueName = channel.queueDeclare().getQueue();
        //绑定队列和交换机
        /**
         * 参数1：队列名称
         * 参数2：交换机名称
         * 参数3：路由key，在广播模式下无意义
         */
        channel.queueBind(queueName,"logs","");
        //消费消息
//        参数2：是否开启自动确认
        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者3："+new String(body));
                super.handleDelivery(consumerTag, envelope, properties, body);
            }
        });

    }

}

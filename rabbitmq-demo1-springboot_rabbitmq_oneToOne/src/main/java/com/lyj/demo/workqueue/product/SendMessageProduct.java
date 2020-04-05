package com.lyj.demo.workqueue.product;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 8:43
 * @Author: 李玉杰
 * @Description: 工作模式（Task queue）
 * 对消费者不加任何限制的话，消费者是平分消费消息，消费者1:1 3 5 7 9
 * 消费者2:0 2 4 6 8
 */
public class SendMessageProduct {

    public static void main(String[] args) throws IOException, TimeoutException {
        new SendMessageProduct().productMessage();
    }

    public void productMessage() throws IOException, TimeoutException {
//        1.获取连接对象
        Connection connection = RabbitMqUtil.getConnection();
//        2.获取通道
        Channel channel = RabbitMqUtil.getChannel(connection);
//        3.通过通道绑定队列
        channel.queueDeclare("work",true,false,false,null);
//        4.发布消息（持久化的消息）
        for (int i=0;i<10;i++){
            channel.basicPublish("","work",MessageProperties.PERSISTENT_TEXT_PLAIN,(i+"hello work queue").getBytes());
        }
//        5.关闭通道和连接
        RabbitMqUtil.closeChannelAndConnection(connection,channel);
    }
}

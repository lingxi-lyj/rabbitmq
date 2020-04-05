package com.lyj.demo.topic.product;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 13:35
 * @Author: 李玉杰
 * @Description: 订阅模式（topic）
 * 生产者
 */
public class SendMessageProduct {

    public static void main(String[] args) throws IOException, TimeoutException {
        new SendMessageProduct().productMsg();
    }
    public void productMsg() throws IOException, TimeoutException {
        Connection connection = RabbitMqUtil.getConnection();
        Channel channel = RabbitMqUtil.getChannel(connection);
        //声明交换机绑定
        channel.exchangeDeclare("topics","topic");
        //定义routing key
        String routingKey = "user.save.item";
        //发布消息
        channel.basicPublish("topics",routingKey,null,("This is topic model base on routingKey ["+routingKey+"]").getBytes());
        //关闭通道和连接
        RabbitMqUtil.closeChannelAndConnection(connection,channel);
    }
}

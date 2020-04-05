package com.lyj.demo.direct.product;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 11:35
 * @Author: 李玉杰
 * @Description: 订阅模式（直连）（路由模式）
 * 生产者
 */
public class SendMessageProduct {

    public static final String EXCHANGE_NAME="logs-direct";
    public static final String EXCHANGE_TYPE="direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        new SendMessageProduct().productMsg();
    }
    public void productMsg() throws IOException, TimeoutException {
        Connection connection = RabbitMqUtil.getConnection();
        Channel channel = RabbitMqUtil.getChannel(connection);
        //通过通道绑定交换机
        /**
         * 参数1：绑定的交换机名称
         * 参数2：交换机的类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE);
//        定义路由key
        String routingKey="error";
        //发布消息
        /**
         * 参数1：交换机名称
         * 参数2：路由key名称
         * 参数3：发布消息时额外配置信息
         * 参数4：发送的消息
         */
        channel.basicPublish(EXCHANGE_NAME,routingKey,null,("This is Direct model message base on routingKey : ["+routingKey+"]").getBytes());
        //关闭连接和通道
        RabbitMqUtil.closeChannelAndConnection(connection,channel);
    }

}

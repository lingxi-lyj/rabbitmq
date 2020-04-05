package com.lyj.demo.fanout.product;

import com.lyj.demo.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 10:33
 * @Author: 李玉杰
 * @Description: 广播模式-》生产者
 */
public class SendMessageProduct {

    public static void main(String[] args) throws IOException, TimeoutException {
        new SendMessageProduct().productMessage();
    }

    public void productMessage() throws IOException, TimeoutException {
        Connection connection = RabbitMqUtil.getConnection();
        Channel channel = RabbitMqUtil.getChannel(connection);
        /**
         *  通道声明交换机
         *  参数1：交换机的名称
         *  参数2：交换机类型，有多种类型。rabbitmq的web界面如果没有交换机，
         *  可以通过代码去创建交换机。fanout：广播类型
         */
        channel.exchangeDeclare("logs","fanout");
        //发布消息
        /**
         * 参数1：交换机名称
         * 参数2：路由键,在广播模式下，路由key没有任何效果意义
         * 参数3：额外配置信息
         * 参数4：发送的消息
         */
        channel.basicPublish("logs","",null,"fanout type message".getBytes());
        //关闭通道和连接
        RabbitMqUtil.closeChannelAndConnection(connection,channel);

    }
}

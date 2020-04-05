package com.lyj.demo.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Builder;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/4 23:53
 * @Author: 李玉杰
 * @Description:
 */
public class RabbitMqUtil {

    //重量级的资源，让对象只创建一次采用类加载静态机制只执行一次
    private static ConnectionFactory connectionFactory;
    static {
        connectionFactory = new ConnectionFactory();
//        如果把connection属性写死的话可以直接在这里全部初始化完
        connectionFactory.setHost("192.168.37.128");
        connectionFactory.setVirtualHost("ems");
        connectionFactory.setUsername("ems");
        connectionFactory.setPassword("ems");
        connectionFactory.setPort(5672);
    }

    public static Connection getConnection() throws IOException, TimeoutException {
        return connectionFactory.newConnection();
    }
    //MQ创建连接
    public static Connection getConnection(String host,int port,String virtualHost,String username,String password){
        try {
            connectionFactory.setHost(host);
            connectionFactory.setPort(port);
            connectionFactory.setUsername(username);
            connectionFactory.setPassword(password);
            connectionFactory.setVirtualHost(virtualHost);
            return connectionFactory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //MQ创建通道
    public static Channel getChannel(Connection connection) throws IOException {
        return connection.createChannel();
    }
    //MQ关闭通道和连接 -记住这里一定是先关闭通道再关闭连接
    public static void closeChannelAndConnection(Connection connection,Channel channel) throws IOException, TimeoutException {
        try {
            if (channel!=null)
                channel.close();
            if (connection!=null)
            connection.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

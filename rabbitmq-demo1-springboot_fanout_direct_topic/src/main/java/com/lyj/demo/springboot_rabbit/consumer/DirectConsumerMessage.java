package com.lyj.demo.springboot_rabbit.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 15:45
 * @Author: 李玉杰
 * @Description: 路由模式（direct）直连
 * 消费者
 */
@Component
public class DirectConsumerMessage {

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue,//创建临时队列
                    exchange = @Exchange(value = "directs",type = "direct"),//绑定交换机
                    key = {"info","error","warning"})//定义接收消息的路由key
    })
    public void receive1(String message){
        System.out.println("消费者1："+message);
    }
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue,//创建临时队列
                    exchange = @Exchange(value = "directs",type = "direct"),//绑定交换机
                    key = {"error"})//定义接收消息的路由key
    })
    public void receive2(String message){
        System.out.println("消费者2："+message);
    }
}

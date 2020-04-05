package com.lyj.demo.springboot_rabbit.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 15:24
 * @Author: 李玉杰
 * @Description: 广播模式-fanout
 * 消费者
 */
@Component
public class FanoutConsumerMessage {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue, //创建临时队列，如果指定名字则显式的创建
                    exchange = @Exchange(value = "logs",type = "fanout")//绑定交换机并指定类型
            )
    })
    public void receive1(String message){
        System.out.println("消费者1："+message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue,exchange = @Exchange(value = "logs",type = "fanout"))
    })
    public void rective2(String message){
        System.out.println("消费者2："+message);
    }

}

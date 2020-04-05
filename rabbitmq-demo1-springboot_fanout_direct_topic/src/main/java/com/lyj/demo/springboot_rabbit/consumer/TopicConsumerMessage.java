package com.lyj.demo.springboot_rabbit.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 15:59
 * @Author: 李玉杰
 * @Description: 订阅模式（topic）
 * 消费者
 */
@Component
public class TopicConsumerMessage {

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue,//创建临时队列
                    exchange = @Exchange(value = "topics",type = "topic"),//绑定交换机
            key = {"user.*","user.save"})//动态路由键匹配
    })
    public void receive1(String message){
        System.out.println("消费者1： "+message);
    }
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue,//创建临时队列
                    exchange = @Exchange(value = "topics",type = "topic"),//绑定交换机
                    key = {"user.#","order.#"})//动态路由键匹配
    })
    public void receive2(String message){
        System.out.println("消费者2： "+message);
    }
}

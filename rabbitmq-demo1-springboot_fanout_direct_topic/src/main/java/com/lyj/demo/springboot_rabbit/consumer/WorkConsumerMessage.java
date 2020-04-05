package com.lyj.demo.springboot_rabbit.consumer;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 15:11
 * @Author: 李玉杰
 * @Description: 工作模式（task model）消费者
 */
@Component
public class WorkConsumerMessage {

    /**
     * 消费者1
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void consumerMsg1(String message){
        System.out.println("work model消费者1： 消费的消息"+message);
    }
    /**
     * 消费者2
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void consumerMsg2(String message){
        System.out.println("work model消费者2： 消费的消息"+message);
    }
}

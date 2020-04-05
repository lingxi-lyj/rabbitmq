package com.lyj.demo.tms.consumer;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 14:37
 * @Author: 李玉杰
 * @Description:  这里是测试springboot集成rabbitmq的使用，生产者在测试包里，这里定义消费者
 * 消费者模型
 * 添加到组件里
 */
@Component
/**
 * 对队列进行额外属性定义只需要在@Queue中定义即可,
 * 具体可以看@Queue注解
 * @Queue 默认情况下是持久化，非独占，非自动删除队列
 */
@RabbitListener(queuesToDeclare = @Queue(value = "tms",durable = "false",autoDelete = "false"))//监听的队列名称
public class ConsumerMessage {

    @RabbitHandler //从队列中获取到消息后指定哪个方法进行处理
    public void receive(String message){
        System.out.println("队列中的消息：  "+message);
    }

}

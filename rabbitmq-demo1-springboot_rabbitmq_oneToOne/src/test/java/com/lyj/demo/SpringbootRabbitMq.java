package com.lyj.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: rabbitmq-demo1
 * @Date: 2020/4/5 14:23
 * @Author: 李玉杰
 * @Description: rabbitmq测试->sprinboot集成Rabbitmq测试
 */
@SpringBootTest(classes = RabbitmqDemoApplication.class)
@RunWith(SpringRunner.class)
public class SpringbootRabbitMq {

    /**
     * springboot集成的rabbitmq使用
     * 如果只有生产者，没有消费者，则消息队列是无法创建的在web界面是看不到的，即消息是发送不出去的
     * 所以使用使用springboot的集成rabbitmq使用生产者和消费者时，必须要创建消费者模型
     */
    //注入RabbitTemplate，这个是Rabbitmq创建好的，只需要注入就可以使用
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 点对点模式
     */
    @Test
    public void  oneToOne(){

        rabbitTemplate.convertAndSend("tms","hello oneToOne model");
    }
}

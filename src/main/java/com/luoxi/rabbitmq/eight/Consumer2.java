package com.luoxi.rabbitmq.eight;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

//死信队列消费者2
public class Consumer2 {

    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";
    //死信队列
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        System.out.println("Consumer2等待接收消息 ..........");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println("Consumer2控制台打印接收到的消息"+ routingKey + "-" + message);
        };
        CancelCallback cancelCallback = (consumerTag)->{

        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,cancelCallback);
    }
}

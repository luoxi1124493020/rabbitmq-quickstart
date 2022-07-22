package com.luoxi.rabbitmq.seven;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

//topic模式交换机消费者1
public class ReceiveLogsTopic1 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = "Q1";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");
        System.out.println("ReceiveLogsTopic1等待接受消息........");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println("ReceiveLogsTopic1控制台打印接收到的消息"+ routingKey + "-" + message);
        };
        CancelCallback cancelCallback = (consumerTag)->{

        };
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);
    }
}

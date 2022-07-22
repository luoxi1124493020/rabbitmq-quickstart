package com.luoxi.rabbitmq.seven;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

//topic模式交换机消费者1
public class ReceiveLogsTopic2 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = "Q2";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");
        System.out.println("ReceiveLogsTopic2等待接受消息........");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println("ReceiveLogsTopic2控制台打印接收到的消息"+ routingKey + "-" + message);
        };
        CancelCallback cancelCallback = (consumerTag)->{

        };
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);
    }
}

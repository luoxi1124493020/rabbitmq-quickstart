package com.luoxi.rabbitmq.six;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class ReceiveLogsDirect2 {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException {
        //获取交换机
        Channel channel = RabbitmqUtil.getChannel();
        //创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //创建队列
        channel.queueDeclare("disk",false,false,false,null);
        //绑定交换机
        channel.queueBind("disk",EXCHANGE_NAME,"error");
        System.out.println("消费者2等待接受消息........");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("ReceiveLogsDirect2控制台打印接收到的消息"+message);
        };
        CancelCallback cancelCallback = (consumerTag)->{

        };
        channel.basicConsume("disk",true,deliverCallback,cancelCallback);
    }
}

package com.luoxi.rabbitmq.six;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

//direct模式交换机消费者1
public class ReceiveLogsDirect1 {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException {
        //获取交换机
        Channel channel = RabbitmqUtil.getChannel();
        //创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //创建队列
        channel.queueDeclare("console",false,false,false,null);
        //绑定交换机
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");
        System.out.println("消费者1等待接受消息........");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("ReceiveLogsDirect1控制台打印接收到的消息:"+message);
        };
        CancelCallback cancelCallback = (consumerTag)->{

        };
        channel.basicConsume("console",true,deliverCallback,cancelCallback);
    }
}

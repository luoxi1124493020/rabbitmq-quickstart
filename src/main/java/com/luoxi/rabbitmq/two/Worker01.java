package com.luoxi.rabbitmq.two;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class Worker01 {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        //消息传递的回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            String msg = new String(message.getBody());
            System.out.println("worker01接受到的消息：" + msg);
        };
        //消息取消的回调
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag + "消费者消费被取消");
        };

        System.out.println("worker01等待接受消息");
        //消费者消费消息
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}

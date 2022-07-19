package com.luoxi.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        //连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //配置信息
        factory.setHost("192.168.200.130");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            //消息传递的回调
            DeliverCallback deliverCallback = (consumerTag,message)->{
                String msg = new String(message.getBody());
                System.out.println(msg);
            };
            //消息取消的回调
            CancelCallback cancelCallback = (consumerTag)->{
                System.out.println("消费者消费被取消");
            };

            //消费者消费消息
            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}

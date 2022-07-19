package com.luoxi.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        //连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //配置信息
        factory.setHost("192.168.200.130");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try {
            //创建队列
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            //队列中发布消息
            String msg = "我是傻逼";
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            System.out.println("消息已发布。。。");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}

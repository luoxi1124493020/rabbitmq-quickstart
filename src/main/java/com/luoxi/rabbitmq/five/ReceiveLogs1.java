package com.luoxi.rabbitmq.five;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

//fanout交换机消费者1
public class ReceiveLogs1 {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException {
        //获取交换机
        Channel channel = RabbitmqUtil.getChannel();
        //创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //生成临时队列，消费者断开连接时自动删除队列
        String queueName = channel.queueDeclare().getQueue();
        //将队列与交换机进行绑定
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("消费者1等待接受消息........");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("控制台打印接收到的消息"+message);
        };
        CancelCallback cancelCallback = (consumerTag)->{

        };
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);

    }

}

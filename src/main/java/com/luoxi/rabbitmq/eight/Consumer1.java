package com.luoxi.rabbitmq.eight;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//死信队列消费者1
public class Consumer1 {
    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列
    private static final String NORMAL_QUEUE = "normal_queue";
    //死信队列
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        //声明普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);

        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        //给普通队列设置死信交换机联系
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置队列的最大长度
        arguments.put("x-max-length",6);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

        //绑定交换机与队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        System.out.println("Consumer1等待接收消息 ..........");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println("Consumer1控制台打印接收到的消息"+ routingKey + "-" + message);
        };
        CancelCallback cancelCallback = (consumerTag)->{

        };
        channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,cancelCallback);
    }
}

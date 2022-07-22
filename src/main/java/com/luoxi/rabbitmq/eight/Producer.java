package com.luoxi.rabbitmq.eight;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;

//死信队列生成者
public class Producer {
    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 0; i < 10; i++) {
            String msg = "消息-" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",/*properties*/ null,msg.getBytes("UTF-8"));
            System.out.println("生产者发送消息：" + msg);
        }
    }
}

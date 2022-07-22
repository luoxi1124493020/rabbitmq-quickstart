package com.luoxi.rabbitmq.five;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

//fanout交换机生产者
public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //控制台输入
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext())
        {
            String msg = scanner.nextLine();
            //发布消息指定交换机与routingKey
            channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息" + msg);
        }
    }
}

package com.luoxi.rabbitmq.three;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;

public class Task02 {

    private static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        //设置队列持久化：第二个参数设置为true
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        /*Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext())
        {
            String message = scanner.next();
            //设置消息持久化：props参数进行设置
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("发送消息完成:"+message);
        }*/
        for (int i = 0; i < 10; i++) {
            String msg = "msg-";
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,(msg + i).getBytes());
        }
    }
}

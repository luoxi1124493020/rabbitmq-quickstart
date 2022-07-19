package com.luoxi.rabbitmq.four;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.UUID;

/**
 * 发布确认机制
 */
public class ConfirmMessage {


    //批量确认发布
    //异步确认发布

    //消息数目
    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) {
        //单个确认发布
        try {
            ConfirmMessage.publishMessageIndividually();        //发布1000个单独确认消息,耗时583ms
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //单个确认发布
    public static void publishMessageIndividually() throws IOException, InterruptedException {
        Channel channel = RabbitmqUtil.getChannel();
        //uuid当做队列名
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //信道开启发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            if(channel.waitForConfirms())
            {
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时" + (end - begin) + "ms");
    }
}

package com.luoxi.rabbitmq.three;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Worker04 {

    private static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        System.out.println("C2 等待接收消息处理时间较长");
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            String msg = new String(message.getBody());
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接收到消息:" + msg);
            //手动应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        //不公平分发（设置每个信道的消息数量，满了则询问下一个信道）
        channel.basicQos(5);
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,(consumerTag) -> {
            System.out.println(consumerTag+"消费者取消消费接口回调逻辑");
        });
    }
}

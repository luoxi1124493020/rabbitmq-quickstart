package com.luoxi.rabbitmq.seven;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EmitLogsTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        Map<String, String> messageMap  = new HashMap<>();
        messageMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        messageMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        messageMap.put("quick.orange.fox","被队列 Q1 接收到");
        messageMap.put("lazy.brown.fox","被队列 Q2 接收到");
        messageMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        messageMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        messageMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        messageMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");

        for (Map.Entry<String, String> messageEntry : messageMap.entrySet()) {
            channel.basicPublish(EXCHANGE_NAME, messageEntry.getKey(), null,messageEntry.getValue().getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("生产者已发出消息");
    }
}

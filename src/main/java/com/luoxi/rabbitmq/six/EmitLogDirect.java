package com.luoxi.rabbitmq.six;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        //创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //使用map存放消息
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("info","普通 info 信息");
        messageMap.put("warning","警告 warning 信息");
        messageMap.put("error","错误 error 信息");
        messageMap.put("debug","调试 debug 信息");
        for (Map.Entry<String, String> messageEntry: messageMap.entrySet()) {
            channel.basicPublish(EXCHANGE_NAME,messageEntry.getKey(),null,messageEntry.getValue().getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者已发送消息");
        }
    }
}

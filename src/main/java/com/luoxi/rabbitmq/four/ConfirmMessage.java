package com.luoxi.rabbitmq.four;

import com.luoxi.rabbitmq.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认机制
 */
public class ConfirmMessage {


    //消息数目
    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) {
        //单个确认发布
        /*try {
            ConfirmMessage.publishMessageIndividually();        //发布1000个单独确认消息,耗时583ms
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //批量确认发布
        /*try {
            ConfirmMessage.publishMessageBatch();        //发布1000个批量确认消息,耗时76ms
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //异步确认发布
        try {
            ConfirmMessage.publishMessageAsync();       //发布1000个异步确认消息,耗时40ms
        } catch (IOException e) {
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

    //批量发布确认
    //批量确认发布
    public static void publishMessageBatch() throws IOException, InterruptedException {
        Channel channel = RabbitmqUtil.getChannel();
        //UUID队列名
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        //批量数
        int batchCount = 100;
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = i +"";
            channel.basicPublish("",queueName,null,msg.getBytes());
            if((i+1) % batchCount == 0)
            {
                channel.waitForConfirms();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) + "ms");

    }

    //异步确认发布
    public static void publishMessageAsync() throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        channel.confirmSelect();
        ConcurrentSkipListMap<Long,String> messageQueue = new ConcurrentSkipListMap<>();
        //确认接收回调
        ConfirmCallback ackCallback = (deliveryTag, multiple)->{
            if(multiple)
            {
                //head获取key值小于tag的map的视图，对其操作也会影响源map（messageQueue），true为等于tag值的也获取
                ConcurrentNavigableMap<Long, String> confirmedMessage = messageQueue.headMap(deliveryTag, true);
                System.out.println("确认消息tag:" + deliveryTag);
                confirmedMessage.clear();
            }
            else
            {
                System.out.println("确认消息tag:" + deliveryTag + "消息内容:" + messageQueue.get(deliveryTag));
                messageQueue.remove(deliveryTag);
            }
        };
        //未确认接收回调
        ConfirmCallback nackCallback = (deliveryTag, multiple)->{
            System.out.println("未确认消息-" + deliveryTag);
        };
        channel.addConfirmListener(ackCallback,nackCallback);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = "消息" + i;
            messageQueue.put(channel.getNextPublishSeqNo(),msg);
            channel.basicPublish("",queueName,null,msg.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息,耗时" + (end - begin) + "ms");
    }
}

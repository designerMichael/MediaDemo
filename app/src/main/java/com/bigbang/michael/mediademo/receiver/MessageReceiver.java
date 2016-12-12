package com.bigbang.michael.mediademo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.bigbang.michael.mediademo.event.SmsEvent;

import org.greenrobot.eventbus.EventBus;


 /*
 * Created by Michael on 2016/11/26.
 * 描    述：短信接收器
 * 修订历史：
 */

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus"); // 提取短信消息
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }
        String address = messages[0].getOriginatingAddress(); // 获取发送方号码

        String fullMessage = "";
        for (SmsMessage message : messages) {
            fullMessage += message.getMessageBody(); // 获取短信内容
        }
        EventBus.getDefault().post(new SmsEvent(SmsEvent.RECEIVE_SMS, address, fullMessage));
//        sender.setText(address);
//        content.setText(fullMessage);
        abortBroadcast(); //因为短信广播是有序的；
    }
}

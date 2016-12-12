package com.bigbang.michael.mediademo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigbang.michael.mediademo.event.SmsEvent;
import com.bigbang.michael.mediademo.receiver.MessageReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SMSActivity extends AppCompatActivity {
    @BindView(R.id.tv_sender)
    TextView tvSender;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.activity_sms)
    LinearLayout activitySms;
    @BindView(R.id.et_receiver)
    EditText etReceiver;
    @BindView(R.id.et_receive_content)
    EditText etReceiveContent;
    @BindView(R.id.btn_send)
    Button btnSend;
    private IntentFilter intentFilter;
    private MessageReceiver receiver;
    private SendStatusReceiver sendStatusReceiver;
    private IntentFilter sendFilter;
    private static final String TAG = "SMSActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initReceiver();
    }

    private void initReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(100); //设置最高权限，优先于系统自带的短信接收
        receiver = new MessageReceiver();

        registerReceiver(receiver, intentFilter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SmsEvent event) {
        if (event.getCode() == SmsEvent.RECEIVE_SMS) {
            tvSender.setText(event.getSender() == null ? "" : event.getSender());
            content.setText(event.getContent() == null ? "" : event.getContent());
        }
    }

    @OnClick(R.id.btn_send)
    public void sendMsg() {
        sendFilter = new IntentFilter();
        sendFilter.addAction("SENT_SMS_ACTION");
        sendStatusReceiver = new SendStatusReceiver();
        registerReceiver(sendStatusReceiver, sendFilter);
        Intent sentIntent = new Intent("SENT_SMS_ACTION");
        PendingIntent pi = PendingIntent.getBroadcast
                (SMSActivity.this, 0, sentIntent, 0);
        SmsManager manager = SmsManager.getDefault();
        Log.e(TAG, "sendMsg: SDK_INT = " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e(TAG, "sendMsg: SDK>=23");
            checkPermission();
        } else {
            Log.e(TAG, "sendMsg: SDK<23");
        }
        manager.sendTextMessage(etReceiver.getText().toString(), null, etReceiveContent.getText().toString(), pi, null);
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    private void checkPermission() {
        Log.e(TAG, "checkPermission: ");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this, "要发短信需要短信权限啦=====", Toast.LENGTH_SHORT).show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 200);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限允许成功", Toast.LENGTH_SHORT).show();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {
                Toast.makeText(this, "权限允许失败", Toast.LENGTH_SHORT).show();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(sendStatusReceiver);
        EventBus.getDefault().unregister(this);
    }

    class SendStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == RESULT_OK) {
                // 短信发送成功
                Toast.makeText(context, "Send succeeded",
                        Toast.LENGTH_LONG).show();
            } else {
                // 短信发送失败
                Toast.makeText(context, "Send failed",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}

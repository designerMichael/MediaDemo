package com.bigbang.michael.mediademo;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //取消导航栏上的提示；
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
       // manager.cancel(0x01);
        getMIEI();
    }

    private void getMIEI() {
        Context context = getWindow().getContext();

        TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            StringBuilder sb = new StringBuilder();
            Toast.makeText(this, "MIEI码为：" + telephonemanage.getDeviceId(), Toast.LENGTH_SHORT).show();
            sb.append("telephonemanage :\r\n MIEI :" + telephonemanage.getDeviceId()
                    + "\r\n Line1Num:(本机号码)" + telephonemanage.getLine1Number()
                    + "\r\n getPhoneType :" + telephonemanage.getPhoneType()
            +"\r\n getSubscriberId（用户id）"+telephonemanage.getSubscriberId()
            +"\r\n getSimSerialNum:"+telephonemanage.getSimSerialNumber());
            Log.e(TAG, "getMIEI: " + sb);
        } catch (Exception e) {

            Log.i("error", e.getMessage());

        }
    }
}

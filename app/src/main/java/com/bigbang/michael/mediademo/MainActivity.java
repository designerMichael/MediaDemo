package com.bigbang.michael.mediademo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.btn_notify)
    Button btnNotify;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.btn_listen_sms)
    Button btnListenSms;

    private NotificationCompat.Builder builder;
    PendingIntent pendingIntent;
    PendingIntent remotePending;
    RemoteViews contentView;
    RemoteViews bigContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getMIEI();


        Intent resultIntent = new Intent(this, ResultActivity.class);
        pendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        Intent remoteIntent = new Intent(this, ShareActivity.class);
        remotePending = PendingIntent.getActivity(
                this,
                0,
                remoteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        contentView = new RemoteViews(getPackageName(), R.layout.remoteview);
        contentView.setTextViewText(R.id.share_content, "这是自定义的view");
        contentView.setOnClickPendingIntent(R.id.share_facebook, remotePending);
        contentView.setOnClickPendingIntent(R.id.share_twitter, remotePending);
        contentView.setInt(R.id.share_content, "setTextColor", isDarkNotificationTheme(MainActivity.this) == true ? Color.WHITE : Color.BLACK);

        bigContentView = new RemoteViews(getPackageName(), R.layout.bigcontentview);
        bigContentView.setTextViewText(R.id.share_content, "这是自定义的BigContentview");
        bigContentView.setOnClickPendingIntent(R.id.share_facebook, remotePending);
        bigContentView.setOnClickPendingIntent(R.id.share_twitter, remotePending);
        bigContentView.setInt(R.id.share_content, "setTextColor", isDarkNotificationTheme(MainActivity.this) == true ? Color.WHITE : Color.BLACK);

    }


    @OnClick(R.id.btn_notify)
    public void onClicked() {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
        ArrayList<String> contents = new ArrayList<>();
        contents.add("hello");
        contents.add("你好");
        contents.add("去吃饭吗");
        contents.add("木头，回答我的问题");
//        sendActionNotification("您有一条新消息", "关于宇宙500光年处诞生的生命", "天文学家发现“地球表兄妹”：距离500光年或有生命这颗名叫开普勒-186f的行星围绕一个恒星运行，距地球500光年，跟地球差不多大。就像天文学家解释的，它围绕它的恒星运行，距离正好可以使行星表面有液态水。我们知道，这是生命存在的基本条件。"
//                , R.mipmap.ic_launcher , pendingIntent, android.R.drawable.ic_menu_call, "打电话", remotePending, android.R.drawable.ic_media_play, "听音乐", remotePending, true, true, true);
//        

//        sendProgressNotification("您有一条新消息", "关于宇宙500光年处诞生的生命", "天文学家发现“地球表兄妹”：距离500光年或有生命这颗名叫开普勒-186f的行星围绕一个恒星运行，距地球500光年，跟地球差不多大。就像天文学家解释的，它围绕它的恒星运行，距离正好可以使行星表面有液态水。我们知道，这是生命存在的基本条件。"
//                , R.mipmap.ic_launcher , pendingIntent, false, false, true);
        sendCustomerNotification("您有一条新消息", "关于宇宙500光年处诞生的生命", "天文学家发现“地球表兄妹”：距离500光年或有生命这颗名叫开普勒-186f的行星围绕一个恒星运行，距地球500光年，跟地球差不多大。就像天文学家解释的，它围绕它的恒星运行，距离正好可以使行星表面有液态水。我们知道，这是生命存在的基本条件。"
                , R.mipmap.ic_launcher, pendingIntent, contentView, bigContentView, false, true, true);

    }

    @OnClick(R.id.btn_listen_sms)
    public void onClickSms() {
        startActivity(new Intent(this, SMSActivity.class));
    }
    
    @OnClick(R.id.btn_ui)
    public void onClickUI(){
        startActivity(new Intent(this, UIDemoActivity.class));
    }


    /**
     * 发送单行文本
     *
     * @param ticker
     * @param title
     * @param content
     * @param smallIcon
     * @param intent
     * @param sound
     * @param vibrate
     * @param lights
     */
    public void sendSingleLineNotification(String ticker, String title, String content, int smallIcon, PendingIntent intent, boolean sound, boolean vibrate, boolean lights) {
        buildNotification(ticker, title, content, smallIcon, intent, sound, vibrate, lights);
        //自定义铃声；
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.guitar);
        builder.setSound(soundUri);
        Notification notification = builder.build();
        sendNotification(notification);
    }

    /**
     * 多行文本 》=api16 android 4.1.2
     *
     * @param ticker
     * @param title
     * @param content
     * @param smallIcon
     * @param intent
     * @param sound
     * @param vibrate
     * @param lights
     */
    public void sendMoreLineNotification(String ticker, String title, String content, int smallIcon, PendingIntent intent, boolean sound, boolean vibrate, boolean lights) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(this, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        buildNotification(ticker, title, content, smallIcon, intent, sound, vibrate, lights);
        Notification notification = new NotificationCompat.BigTextStyle(builder).bigText(content).build();
        sendNotification(notification);
    }

    public void sendBigPicNotification(String ticker, String title, String content, int smallIcon, PendingIntent intent, boolean sound, boolean vibrate, boolean lights) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(this, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        buildNotification(ticker, title, content, smallIcon, intent, sound, vibrate, lights);
        //大图
        Bitmap bigPicture = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
        //图标
        Bitmap bigLargeIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.image2);
        Notification notification = new NotificationCompat.BigPictureStyle(builder).bigLargeIcon(bigLargeIcon).bigPicture(bigPicture).build();
        sendNotification(notification);
    }

    /**
     * \
     * 多条内容
     *
     * @param ticker
     * @param title
     * @param content
     * @param smallIcon
     * @param intent
     * @param conntents
     * @param sound
     * @param vibrate
     * @param lights
     */
    public void sendListNotification(String ticker, String title, String content, int smallIcon, PendingIntent intent, ArrayList<String> conntents, boolean sound, boolean vibrate, boolean lights) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(this, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        buildNotification(ticker, title, content, smallIcon, intent, sound, vibrate, lights);
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle(builder);
        for (String conntent : conntents) {
            style.addLine(conntent);
        }
        style.setSummaryText(conntents.size() + "条消息");
        style.setBigContentTitle(title);
        Notification notification = style.build();
        sendNotification(notification);
    }

    /**
     * 添加按钮操作
     *
     * @param ticker
     * @param title
     * @param content
     * @param smallIcon
     * @param intent
     * @param leftIcon
     * @param leftText
     * @param leftPI
     * @param rightIcon
     * @param rightText
     * @param rightPI
     * @param sound
     * @param vibrate
     * @param lights
     */
    public void sendActionNotification(String ticker, String title, String content, int smallIcon, PendingIntent intent, int leftIcon, String leftText, PendingIntent leftPI, int rightIcon, String rightText, PendingIntent rightPI, boolean sound, boolean vibrate, boolean lights) {
        buildNotification(ticker, title, content, smallIcon, intent, sound, vibrate, lights);
        builder.addAction(leftIcon, leftText, leftPI);
        builder.addAction(rightIcon, rightText, rightPI);
        Notification notification = builder.build();
        sendNotification(notification);
    }

    /**
     * 有进度条的notification
     *
     * @param ticker
     * @param title
     * @param content
     * @param smallIcon
     * @param intent
     * @param sound
     * @param vibrate
     * @param lights
     */
    public void sendProgressNotification(String ticker, String title, String content, int smallIcon, PendingIntent intent, boolean sound, boolean vibrate, boolean lights) {
        buildNotification(ticker, title, content, smallIcon, intent, sound, vibrate, lights);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i += 10) {
                    builder.setProgress(100, i, false);
                    sendNotification(builder.build());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //下载完成
                builder.setContentText("下载完成").setProgress(0, 0, false);
                sendNotification(builder.build());
            }
        }).start();
    }

    public void sendCustomerNotification(String ticker, String title, String content, int smallIcon, PendingIntent intent, RemoteViews contentView, RemoteViews bigContentView, boolean sound, boolean vibrate, boolean lights) {
        buildNotification(ticker, title, content, smallIcon, intent, sound, vibrate, lights);
        builder.setCustomBigContentView(bigContentView);
        builder.setCustomContentView(contentView);
        Notification notification = builder.build();
//        notification.bigContentView = bigContentView;
//        notification.contentView=contentView;
        sendNotification(notification);
    }

    /**
     * 发送notification
     *
     * @param notification
     */
    public void sendNotification(Notification notification) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0x01, notification);
    }


    public void buildNotification(String ticker, String title, String content, int smallIcon, PendingIntent intent, boolean sound, boolean vibrate, boolean lights) {
        builder = new NotificationCompat.Builder(this);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setContentIntent(intent);
        builder.setColor(Color.RED); //icon背景设
        builder.setSmallIcon(smallIcon);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        int defaults = 0;
        if (sound) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (lights) {
            defaults |= Notification.DEFAULT_LIGHTS;
        }
        builder.setDefaults(defaults);
        builder.setOngoing(true);
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
                    + "\r\n getSubscriberId（用户id）" + telephonemanage.getSubscriberId()
                    + "\r\n getSimSerialNum:" + telephonemanage.getSimSerialNumber());
            Log.e(TAG, "getMIEI: " + sb);
            tvInfo.setText(sb.toString());

        } catch (Exception e) {

            Log.i("error", e.getMessage());

        }
    }


    ////////////获取通知栏背景颜色////////////////
    public static boolean isDarkNotificationTheme(Context context) {
        return !isSimilarColor(Color.BLACK, getNotificationColor(context));
    }

    /**
     * 获取通知栏颜色
     *
     * @param context
     * @return
     */
    public static int getNotificationColor(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.build();
        int layoutId = notification.contentView.getLayoutId();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null, false);
        if (viewGroup.findViewById(android.R.id.title) != null) {
            return ((TextView) viewGroup.findViewById(android.R.id.title)).getCurrentTextColor();
        }
        return findColor(viewGroup);
    }

    private static boolean isSimilarColor(int baseColor, int color) {
        int simpleBaseColor = baseColor | 0xff000000;
        int simpleColor = color | 0xff000000;
        int baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor);
        int baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor);
        int baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor);
        double value = Math.sqrt(baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue);
        if (value < 180.0) {
            return true;
        }
        return false;
    }

    private static int findColor(ViewGroup viewGroupSource) {
        int color = Color.TRANSPARENT;
        LinkedList<ViewGroup> viewGroups = new LinkedList<>();
        viewGroups.add(viewGroupSource);
        while (viewGroups.size() > 0) {
            ViewGroup viewGroup1 = viewGroups.getFirst();
            for (int i = 0; i < viewGroup1.getChildCount(); i++) {
                if (viewGroup1.getChildAt(i) instanceof ViewGroup) {
                    viewGroups.add((ViewGroup) viewGroup1.getChildAt(i));
                } else if (viewGroup1.getChildAt(i) instanceof TextView) {
                    if (((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor() != -1) {
                        color = ((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor();
                    }
                }
            }
            viewGroups.remove(viewGroup1);
        }
        return color;
    }
}

package com.bigbang.michael.mediademo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


 /*
 * Created by Michael on 2016/12/10.
 * 描    述：
 * 修订历史：
 */

public class UIDemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uidemo);
        handler.sendEmptyMessage(1);
        
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        
            switch (msg.what) {
                case 1:
                    // 在这里可以进行UI操作
                    break;
                default:
                    break;
            }
        }
    };
    private void newThread(){
        //第一种：匿名内部类
        new Thread(new Runnable() {
            @Override
            public void run() {
                
            }
        }).start();
        //第二种继承Thread;
        new MyThread().start();
        
        //第三种：实现Runable接口类；
        new Thread(new ThreadImple()).start();
        
    }
    
    class MyThread extends Thread{
        @Override
        public void run() {
        }
    }
    
    class ThreadImple implements Runnable{
        @Override
        public void run() {
            
        }
    }
    
    
 
}

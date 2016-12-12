package com.bigbang.michael.mediademo.event;

 /*
 * Created by Michael on 2016/11/26.
 * 描    述：
 * 修订历史：
 */

public class SmsEvent {
    private int code;
    public static final int RECEIVE_SMS = 1;
    private String sender;
    private String content;

    public SmsEvent(int code,String sender,String content){
        this.code = code;
        this.sender = sender;
        this.content = content;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

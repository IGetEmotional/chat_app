package com.example.chat_app;

import java.util.Date;

public class Message {
    public String userName;
    public String textMessage;
    private long messageTime;

    public Message(){};
    public Message(String userName, String textMessage){
        this.userName = userName;
        this.textMessage = textMessage;

        this.messageTime = new Date().getTime();
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setTextMessage(String textMessage){
        this.textMessage = textMessage;
    }

    public void setMessageTime(long messageTime){
        this.messageTime = messageTime;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getTextMessage(){
        return this.textMessage;
    }

    public long getMessageTime(){
        return this.messageTime;
    }



}

package com.pfa.chat_bot;

import android.os.Parcel;
import android.os.Parcelable;

public class Message {
    private String Message;
    private String sender;
    private boolean isUser;
    public Message(String sender,String Message,boolean isUser){
        this.isUser = isUser;
        this.sender = sender;
        this.Message = Message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return Message;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setMessage(String message) {
        Message = message;
    }
}

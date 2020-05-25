package com.pfa.chat_bot;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class AnswerHandler extends Handler{
    private ListView view ;
    private ConversationAdapter Adapter;
    ArrayList<com.pfa.chat_bot.Message> messages;
    public AnswerHandler(ListView view, ConversationAdapter Adapter , ArrayList<com.pfa.chat_bot.Message> messages){
        this.view = view;
        this.Adapter = Adapter;
        this.messages = messages;

    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        String Answer = (String) msg.obj;
        messages.add(new com.pfa.chat_bot.Message("YourChatbot",Answer,false));
        Adapter.notifyDataSetChanged();
        view.smoothScrollToPosition(messages.size()-1);
    }
}

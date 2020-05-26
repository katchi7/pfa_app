package com.pfa.chat_bot;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class AnswerHandler extends Handler{
    private ListView view ;
    private ConversationAdapter Adapter;
    ArrayList<com.pfa.chat_bot.Message> messages;
    private Context context;
    int Posision;
    public AnswerHandler(ListView view, ConversationAdapter Adapter , ArrayList<com.pfa.chat_bot.Message> messages){
        this.view = view;
        this.Adapter = Adapter;
        this.messages = messages;
    }
    public AnswerHandler(Context context){
        this.context = context;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        String Answer;
        switch (msg.arg1){
            case-2:
                Toast.makeText(context,"Failed to connect",Toast.LENGTH_LONG);
                ((loading)context).finish();
                break;
            case -1:
                Intent i = new Intent(context,MainActivity.class);
                context.startActivity(i);
                ((loading)context).finish();
                break;
            case 0:
                Answer = "";
                Posision = messages.size();
                UpdateItem(Posision, Answer);
                break;
            case 1:

                Answer = (String) msg.obj;
                Posision = msg.arg2;
                UpdateItem(Posision, Answer);
                break;

        }
    }
    private void UpdateItem(int position,String Answer){
        if (position == messages.size())messages.add(new com.pfa.chat_bot.Message("YourChatbot",Answer,false));
        else messages.get(position).setMessage(Answer);

        Adapter.notifyDataSetChanged();
        view.smoothScrollToPosition(messages.size()-1);
    }
}

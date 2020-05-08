package com.pfa.chat_bot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ConversationAdapter extends BaseAdapter {
    private List<Message> Messages;
    Context context;
    public ConversationAdapter(Context context,List<Message> Messages){
        this.Messages = Messages;
        this.context = context;
    }
    @Override
    public int getCount() {
        return Messages.size();
    }

    @Override
    public Object getItem(int position) {
        return Messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message CurentMessage = (Message) getItem(position);
        if(!CurentMessage.isUser()) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.chatbot_message, parent, false);
        }
        else{
            convertView = LayoutInflater.from(context).inflate(R.layout.user_message, parent, false);

        }
        TextView Message_textView = convertView.findViewById(R.id.message_body);
        Message_textView.setText(CurentMessage.getMessage());
        return convertView;
    }
}

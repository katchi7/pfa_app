package com.pfa.chat_bot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.pfa.ChatBot.ChatBot;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText Message_Et;
    private ImageButton Send_btn;
    public static ListView Messages_lv;
    private ConversationAdapter Adapter;
    private ArrayList<Message> message;
    private boolean sender = true;
    private ChatBot chatBot ;
    private ProgressDialog mProgressBar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);Message_Et = findViewById(R.id.message_et);
        Send_btn = findViewById(R.id.send);
        Messages_lv = findViewById(R.id.Conversation_lv);
        message = new ArrayList<>();
        Adapter = new ConversationAdapter(MainActivity.this,message);
        Messages_lv.setAdapter(Adapter);
        Send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(chatBot!=null) {
                if (!isEmpty(Message_Et) && chatBot.isTrained()) {
                    String Msg = Message_Et.getText().toString();
                    String ChatBotAnswer = null;
                    message.add(new Message("You", Msg, sender));
                    Message_Et.getText().clear();
                        try {
                            ChatBotAnswer = chatBot.ChatbotAnswer(Msg);
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    Log.d("testing","---------------checking ChatbotAnswer : "+ChatBotAnswer);
                        if (ChatBotAnswer != null)
                            message.add(new Message("Your Chatbot", ChatBotAnswer, !sender));
                    Adapter.notifyDataSetChanged();
                    Messages_lv.smoothScrollToPosition(Adapter.getCount()-1);
                }else{
                    if (!chatBot.isTrained()) Toast.makeText(MainActivity.this,"Wait a minute, I'm not loaded yet",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(MainActivity.this,"I'm waiting for your questions",Toast.LENGTH_SHORT).show();
                }
                }else{

                     Toast.makeText(MainActivity.this,"Wait a minute, I'm not loaded yet",Toast.LENGTH_LONG).show();
                 }
            }
        });

    }
    public boolean isEmpty(EditText v){
        if(v.getText().toString().trim().length()>0)return false;
        return true;
    }
    @Override
    public Dialog onCreateDialog(int identifiant) {

        if(mProgressBar == null) {
            mProgressBar = new ProgressDialog(this);
            mProgressBar.setTitle("Loading your chatbot");
            mProgressBar.setCancelable(false);
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.setIndeterminate(true);
        }
        return mProgressBar;
    }
    public void loadChatbot(){
        showDialog(0);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                chatBot = new ChatBot(MainActivity.this);
                mProgressBar.dismiss();
                ConversationAdapter.setChatBot(chatBot);
            }
        });
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadChatbot();

    }
}

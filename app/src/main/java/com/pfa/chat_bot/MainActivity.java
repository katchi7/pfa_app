package com.pfa.chat_bot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText Message_Et;
    private ImageButton Send_btn;
    public static ListView Messages_lv;
    private ConversationAdapter Adapter;
    private ArrayList<Message> message;
    private CategoryAswer categoryAswer;
    private AnswerHandler handler;
    private boolean sender = true;
    private static final String MESSAGE_ID = "com.pfa.chat_bot.message";
    private static final String SIZE_ID = "com.pfa.chat_bot.size";
    public static final String API = "https://bbb7b760.eu-gb.apigw.appdomain.cloud/chatbotapi/chatbotapi/";
    private int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);Message_Et = findViewById(R.id.message_et);
        loadAnswers();
        Send_btn = findViewById(R.id.send);
        Messages_lv = findViewById(R.id.Conversation_lv);
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        message = new ArrayList<>();
        Adapter = new ConversationAdapter(MainActivity.this,message);
        Messages_lv.setAdapter(Adapter);
        handler = new AnswerHandler(Messages_lv,Adapter,message);
        Send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(Message_Et)){
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    String Msg = Message_Et.getText().toString();
                    message.add(new Message("You", Msg, sender));
                    GetCategory();
                    Message_Et.getText().clear();
                    Adapter.notifyDataSetChanged();
                    Messages_lv.smoothScrollToPosition(Adapter.getCount()-1);
                }
            }
        });

    }
    public void loadAnswers(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                categoryAswer=new CategoryAswer(MainActivity.this);
            }
        });
    }
    public boolean isEmpty(EditText v){
        if(v.getText().toString().trim().length()>0)return false;
        return true;
    }
    public void GetCategory(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                android.os.Message tosend = handler.obtainMessage();
                tosend.arg1=0;
                handler.sendMessage(tosend);
                String question = message.get(message.size()-1).getMessage();
                int position = message.size();

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(API + question.replace(" ","%20"))
                        .get()
                        .addHeader("accept", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    Log.d("testing",data);
                    if(data.contains("answer")) {
                        JsonParser parser = new JsonParser();
                        JsonObject obj = parser.parse(data).getAsJsonObject();
                        String Categorie = obj.getAsJsonPrimitive("answer").getAsString();
                        String Answer = categoryAswer.get(Categorie.trim());
                        tosend = handler.obtainMessage();
                        tosend.obj = Answer;
                        tosend.arg1=1;
                        tosend.arg2 = position;
                        handler.sendMessage(tosend);

                    }
                    else{
                        Log.d("testing","go check ibm");}
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("testing",e.toString());
                }

            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
    }


}

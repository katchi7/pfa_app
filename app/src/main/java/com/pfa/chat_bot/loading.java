package com.pfa.chat_bot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loading extends AppCompatActivity {

    private AnswerHandler handler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        handler = new AnswerHandler(loading.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Load();

    }
    public void Load(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                CategoryAswer.loadAnswers(loading.this);
                String data = "";
                android.os.Message tosend;
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(MainActivity.API + "hey".replace(" ","%20"))
                        .get()
                        .addHeader("accept", "application/json")
                        .build();

                try {

                    int trynb =0;
                    while (!data.contains("answer") && trynb < 5) {
                        Response response = client.newCall(request).execute();
                        data = response.body().string();
                        Log.d("testing", data);
                        if (data.contains("answer")) {
                            JsonParser parser = new JsonParser();
                            JsonObject obj = parser.parse(data).getAsJsonObject();

                            String Categorie = obj.getAsJsonPrimitive("answer").getAsString();
                        } else {
                            Log.d("testing", "go check ibm");
                        }
                        trynb++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("testing",e.toString());
                    data = "";
                }
                if(!data.contains("answer")){
                    tosend = handler.obtainMessage();
                    tosend.arg1 = -2;
                    handler.sendMessage(tosend);
                }
                String Answer = "";
                client = new OkHttpClient();
                String json = "{\"question\":\"Bonjour\"}";

                RequestBody body = RequestBody.create(
                        MediaType.parse("application/json"), json);

                request = new Request.Builder()
                        .url(MainActivity.Frensh_URL)
                        .post(body)
                        .build();

                Call call = client.newCall(request);
                Response response = null;
                try {
                    data="";
                    int trynb =0;
                    while (!data.contains("answer") && trynb < 5) {
                    response = call.execute();
                    Log.d("testing","I'm here");
                    data = response.body().string();
                    if(data.contains("answer")) {

                        Answer = CategoryAswer.getFR(data.split(":")[1].trim());
                        Log.d("testing", "Entered");
                        Log.d("testing", data.split(":")[1].trim());
                        Log.d("testing", Answer);
                        tosend = handler.obtainMessage();
                        tosend.arg1 = -1;
                        handler.sendMessage(tosend);
                        }
                        trynb++;
                    }

                } catch (IOException e) {
                    Log.d("testing",e.toString());
                }
                if(!data.contains("answer")){
                    tosend = handler.obtainMessage();
                    tosend.arg1 = -2;
                    handler.sendMessage(tosend);
                }
            }
        });
    }
}

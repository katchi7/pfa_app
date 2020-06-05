package com.pfa.chat_bot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        Load.StartLoading(loading.this,handler);
        Intent i =new Intent(loading.this,Load.class);
        startService(i);

    }

}

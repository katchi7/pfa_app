package com.pfa.chat_bot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

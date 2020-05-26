package com.pfa.chat_bot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
    private AnswerHandler handler;
    private LinearLayout parent_ly;
    private boolean DarkMode = false;
    private boolean sender = true;
    private static final String DARK_MODE = "com.pfa.chat_bot.DarkMode";
    public static final String API = "https://bbb7b760.eu-gb.apigw.appdomain.cloud/chatbotapi/chatbotapi/";
    private SharedPreferences User_Preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Message_Et = findViewById(R.id.message_et);
        parent_ly = findViewById(R.id.parently);
        Send_btn = findViewById(R.id.send);
        User_Preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Messages_lv = findViewById(R.id.Conversation_lv);
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        message = new ArrayList<>();
        Adapter = new ConversationAdapter(MainActivity.this, message);
        Messages_lv.setAdapter(Adapter);
        handler = new AnswerHandler(Messages_lv, Adapter, message);
        registerForContextMenu(Messages_lv);
        Send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(Message_Et)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    String Msg = Message_Et.getText().toString();
                    message.add(new Message("You", Msg, sender));
                    GetCategory();
                    Message_Et.getText().clear();
                    Adapter.notifyDataSetChanged();
                    Messages_lv.smoothScrollToPosition(Adapter.getCount() - 1);
                }
            }
        });

    }


    public boolean isEmpty(EditText v) {
        if (v.getText().toString().trim().length() > 0) return false;
        return true;
    }

    public void GetCategory() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                android.os.Message tosend = handler.obtainMessage();
                tosend.arg1 = 0;
                handler.sendMessage(tosend);
                String question = message.get(message.size() - 1).getMessage();
                int position = message.size();

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(API + question.replace(" ", "%20"))
                        .get()
                        .addHeader("accept", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    Log.d("testing", data);
                    if (data.contains("answer")) {
                        JsonParser parser = new JsonParser();
                        JsonObject obj = parser.parse(data).getAsJsonObject();
                        String Categorie = obj.getAsJsonPrimitive("answer").getAsString();
                        String Answer = CategoryAswer.get(Categorie.trim());
                        tosend = handler.obtainMessage();
                        tosend.obj = Answer;
                        tosend.arg1 = 1;
                        tosend.arg2 = position;
                        handler.sendMessage(tosend);

                    } else {
                        Log.d("testing", "go check ibm");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("testing", e.toString());
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        User_Preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        DarkMode = User_Preferences.getBoolean(DARK_MODE,false);
        if (DarkMode)
            parent_ly.setBackgroundColor(getResources().getColor(R.color.DarkMode));
        else parent_ly.setBackgroundColor(getResources().getColor(R.color.DefaultMode));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.DarkMode:
                DarkMode = !DarkMode;
                item.setChecked(DarkMode);
                if (DarkMode)
                    parent_ly.setBackgroundColor(getResources().getColor(R.color.DarkMode));
                else parent_ly.setBackgroundColor(getResources().getColor(R.color.DefaultMode));
                return true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = User_Preferences.edit();
            editor.putBoolean(DARK_MODE,DarkMode);
            editor.commit();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE,Menu.FIRST,Menu.NONE,"Coupier");


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == Menu.FIRST){
             AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String toCopy = ((TextView)info.targetView.findViewById(R.id.message_body)).getText().toString();
            ClipboardManager clip_board_manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("toCopy",toCopy);
            clip_board_manager.setPrimaryClip(clipData);
            return true;
        }
        return super.onContextItemSelected(item);
    }
}

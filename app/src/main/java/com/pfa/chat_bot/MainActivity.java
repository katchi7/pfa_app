package com.pfa.chat_bot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    private static final String LANG_PREF = "com.pfa.chat_bot.LANGUAGE";
    public static final String API = "https://bbb7b760.eu-gb.apigw.appdomain.cloud/chatbotapi/chatbotapi/";
    public static final String Frensh_URL = "https://us-central1-trusty-magnet-279117.cloudfunctions.net/Chatbot";
    public static final String Frensh_URL2 = "https://us-central1-trusty-magnet-279117.cloudfunctions.net/Chatbot-1";
    private MessageDao Message_database;
    private static String LANGUAGE ="";
    private SharedPreferences User_Preferences;
    private Menu m;
    private int size;

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
                if(LANGUAGE.contains("ENG")) {
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
                            String Answer = "";
                            if (Categorie.contains("Sorry i got confused ")) Answer = Categorie;
                            else Answer = CategoryAswer.get(Categorie.trim());

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
                else {
                    String Answer = "";
                    String data = "";
                    String url = Frensh_URL;
                    while (!data.contains("answer")) {

                        OkHttpClient client = new OkHttpClient();
                        String json = "{\"question\":\"" + question + "\"}";

                        RequestBody body = RequestBody.create(
                                MediaType.parse("application/json"), json);

                        Request request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .build();

                        Call call = client.newCall(request);
                        Response response = null;
                        try {
                            response = call.execute();


                            data = response.body().string();
                            if (data.contains("answer")) {

                                Answer = CategoryAswer.getFR(data.split(":")[1].trim());
                                Log.d("testing", "Entered");
                                Log.d("testing", data.split(":")[1].trim());
                                Log.d("testing", Answer);
                                tosend = handler.obtainMessage();
                                tosend.obj = Answer;
                                tosend.arg1 = 1;
                                tosend.arg2 = position;
                                handler.sendMessage(tosend);


                            }
                            else{
                                if(url.contains(Frensh_URL2)){
                                    url=Frensh_URL;
                                    Log.d("testing","Changed");
                                }
                                else url=Frensh_URL2;
                                Log.d("testing","URL Changed -> "+url);

                            }
                        } catch (IOException e) {
                            if(url.contains(Frensh_URL2)) url=Frensh_URL;
                            else url=Frensh_URL2;
                            Log.d("testing",e.toString());
                            Log.d("testing","URL Changed -> "+url);
                        }
                    }
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        User_Preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        DarkMode = User_Preferences.getBoolean(DARK_MODE,false);
        LANGUAGE = User_Preferences.getString(LANG_PREF,"ENG");
        Log.d("testing",LANGUAGE);
        if (DarkMode) {
            parent_ly.setBackgroundColor(getResources().getColor(R.color.DarkMode));

        }
        else parent_ly.setBackgroundColor(getResources().getColor(R.color.DefaultMode));
        Message_database = new MessageDao(MainActivity.this);
        Message_database.open();
        Cursor c = Message_database.SelectMessages();
        message.clear();
        while (c.moveToNext()){
           message.add(new Message(c.getString(3),c.getString(1),c.getInt(2)!=0));
           size++;
        }
        Adapter.notifyDataSetChanged();
        Log.d("testing",Integer.toString(Adapter.getCount()-1));
        Messages_lv.setSelection(Adapter.getCount()-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        m=menu;
        Log.d("testing",LANGUAGE);
        if(LANGUAGE.contains("ENG")){
            m.findItem(R.id.English).setChecked(true);
            Log.d("testing","Entered");
        }
        else m.findItem(R.id.french).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        MenuItem ang;
        MenuItem fr;
        SharedPreferences.Editor editor;
        switch (item.getItemId()) {
            case R.id.DarkMode:
                DarkMode = !DarkMode;
                item.setChecked(DarkMode);
                if (DarkMode)
                    parent_ly.setBackgroundColor(getResources().getColor(R.color.DarkMode));
                else parent_ly.setBackgroundColor(getResources().getColor(R.color.DefaultMode));
                return true;
            case R.id.English:
                item.setChecked(!item.isChecked());
                if(item.isChecked()) LANGUAGE = "ENG";

                fr = m.findItem(R.id.french);
                fr.setChecked(!item.isChecked());
                return true;
            case R.id.french:
                item.setChecked(!item.isChecked());
                if(item.isChecked()){
                    LANGUAGE = "FR";
                }
                ang = m.findItem(R.id.English);
                ang.setChecked(!item.isChecked());
                return true;
            case R.id.About:
                Intent i = new Intent(MainActivity.this,AboutActivity.class);
                MainActivity.this.startActivityForResult(i,0);

        }
        return false;
    }


    @Override
    protected void onStop() {
        super.onStop();
        Message_database.DeleteAll();
        for(int i = 0;i<message.size();i++ ) if(message.get(i).getMessage()!="") Message_database.Insert(message.get(i));
        message.clear();
        Message_database.close();
        SharedPreferences.Editor editor = User_Preferences.edit();
        editor.putBoolean(DARK_MODE,DarkMode);
        Log.d("testing",LANGUAGE);
        editor.putString(LANG_PREF,LANGUAGE);
        editor.commit();
        Log.d("testing","done");

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE,Menu.FIRST,Menu.NONE,"Copy");
        menu.add(Menu.NONE,Menu.FIRST+1,Menu.NONE,"Delete");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case Menu.FIRST:
                AdapterView.AdapterContextMenuInfo info;
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                String toCopy = ((TextView) info.targetView.findViewById(R.id.message_body)).getText().toString();
                ClipboardManager clip_board_manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("toCopy", toCopy);
                clip_board_manager.setPrimaryClip(clipData);
                return true;
            case Menu.FIRST+1:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int Position = info.position;
                message.remove(Position);
                if (Position < size) size--;
                Adapter.notifyDataSetChanged();
                return true;
            }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

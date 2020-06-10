package com.pfa.chat_bot;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class Save extends IntentService {
    private static Context context;
    private static ArrayList<Message> messages;
    private static MessageDao Messeges_DB;
    private static boolean DarkMode;
    private static SharedPreferences User_Preferences;
    private static String LANG_PREF;
    private static String LANGUAGE;
    private static String DARK_MODE;

    public Save() {
        super("Save");
    }
    public static void StartActionSave(Context context,MessageDao messageDB,boolean DarkMode,SharedPreferences User_Preferences,ArrayList<Message> messages,String LANG_PREF,String LANGUAGE,String DARK_MOD){
        Save.context = context;
        Save.messages = messages;
        Save.Messeges_DB = messageDB;
        Save.DarkMode = DarkMode;
        Save.DARK_MODE = DARK_MOD;
        Save.User_Preferences = User_Preferences;
        Save.LANG_PREF = LANG_PREF;
        Save.LANGUAGE = LANGUAGE;
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Messeges_DB.DeleteAll();
            for(int i = 0;i<messages.size();i++ ) if(messages.get(i).getMessage()!="") Messeges_DB.Insert(messages.get(i));
            messages.clear();
            Messeges_DB.close();
            SharedPreferences.Editor editor = User_Preferences.edit();
            editor.putBoolean(DARK_MODE,DarkMode);
            editor.putString(LANG_PREF,LANGUAGE);
            editor.commit();

        }

    }
}

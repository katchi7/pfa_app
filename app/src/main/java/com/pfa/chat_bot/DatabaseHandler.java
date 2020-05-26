package com.pfa.chat_bot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    private final static String Table_Name = "Messages";
    private final static String Table_Creation = "CREATE TABLE "+Table_Name+"(\n" +
            "    Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "    message TEXT NOT NULL,\n" +
            "    isUser BOOLEAN NOT NULL,\n" +
            "    Sender VARCHAR(20) NOT NULL" +
            ");";
    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Table_Creation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name + ";");
        this.onCreate(db);
    }
}

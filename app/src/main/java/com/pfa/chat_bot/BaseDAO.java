package com.pfa.chat_bot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDAO {

    protected final static int VERSION = 2;
    protected final static String NOM = "chatbot.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler Handler = null;

    public BaseDAO(Context pContext) {
        this.Handler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        mDb = Handler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }
}


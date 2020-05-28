package com.pfa.chat_bot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class MessageDao extends BaseDAO{
    private static final String ID = "Id";
    private static final String MESSAGE = "message";
    private static final String ISUSER = "isUser";
    private static final String SENDER = "Sender";
    private static final String Table_Name = "Messages";

    public MessageDao(Context pContext) {
        super(pContext);
    }
    public void DeleteAll(){
        mDb.delete(Table_Name,ID + ">=0",null);
    }
    public void Insert(Message message){
        ContentValues value = new ContentValues();
        value.put(MessageDao.MESSAGE, message.getMessage());
        value.put(MessageDao.ISUSER, message.isUser());
        value.put(MessageDao.SENDER, message.getSender());
        mDb.insert(MessageDao.Table_Name, null, value);
    }
    public Cursor SelectMessages(){
        Cursor c = mDb.rawQuery("select * from " + Table_Name+" ORDER BY "+ID+" ASC"  ,null);
        return c;
    }
}

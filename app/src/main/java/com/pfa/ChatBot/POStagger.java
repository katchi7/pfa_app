package com.pfa.ChatBot;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class POStagger  {
    private InputStream modelIn;
    private POSTaggerME myCategorizer;
    private String[] posTokens;
    private Context context;
    public POStagger(Context context)  {
        this.context = context;
        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
        try {
            AssetFileDescriptor fileDescriptor =
                    context.getAssets().openFd("en_pos_maxent.bin");
            FileInputStream inputStream = fileDescriptor.createInputStream();
            POSModel posModel = new POSModel(inputStream);
            POSTaggerME posTaggerME = new POSTaggerME(posModel);
            myCategorizer = posTaggerME;
        } catch (Exception e) {
            Log.d("testing","error :"+e.toString());
        }
        Log.d("testing","---------------checking : "+myCategorizer);




    }
    public String[] detectPOSTags(String[] tokens) {

        // Tag sentence.
        String[] posTokens = myCategorizer.tag(tokens);

        return posTokens;
    }
}
package com.pfa.ChatBot;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;

public class Lemmatizer {
    private InputStream modelIn;
    private LemmatizerME myCategorizer;
    private String[] lemmaTokens;
    private Context context;
    public Lemmatizer(Context context) throws IOException {
            modelIn = context.getAssets().open("en-lemmatizer.bin");
            myCategorizer = new LemmatizerME(new LemmatizerModel( modelIn));
        Log.d("testing","Modelin : "+modelIn +" lemmatizerMe : "+myCategorizer);

    }
    public String[] lemmatizeTokens(String[] tokens, String[] posTags)
    {
        // Better to read file once at start of program & store model in instance
        // variable. but keeping here for simplicity in understanding.
            String[] lemmaTokens = myCategorizer.lemmatize(tokens, posTags);
        return lemmaTokens;
    }
}

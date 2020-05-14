package com.pfa.ChatBot;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
public class SentenceBreaker  {
    private InputStream modelIn;
    private SentenceDetectorME myCategorizer;
    private String[] sentences;
    private Context context;
    public SentenceBreaker(Context context)  throws FileNotFoundException, IOException {
        this.context = context;
        modelIn = context.getAssets().open("en-sent.bin");
        myCategorizer = new SentenceDetectorME(new SentenceModel(modelIn));

    }
    public String[] breakSentence(String data) {
        // Better to read file once at start of program & store model in instance
        // variable. but keeping here for simplicity in understanding.
            sentences = myCategorizer.sentDetect(data);
            return sentences;
        }
    }
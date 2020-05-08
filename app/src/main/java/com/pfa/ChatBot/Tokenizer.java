package com.pfa.ChatBot;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Tokenizer {
    private InputStream modelIn;
    private TokenizerME myCategorizer;
    private String[] tokens;
    private  Context context;
    public Tokenizer(Context context) throws FileNotFoundException, IOException {
        //Getting the model
        this.context = context;
        modelIn = context.getAssets().open("en-token.bin");
        // Initialize tokenizer tool
        myCategorizer = new TokenizerME(new TokenizerModel(modelIn));
    }
    public String[] tokenizeSentence(String sentence) {
             // Tokenize sentence.
            tokens = myCategorizer.tokenize(sentence);
        return tokens;
        }
    }

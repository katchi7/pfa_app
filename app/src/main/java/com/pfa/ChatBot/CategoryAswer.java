package com.pfa.ChatBot;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CategoryAswer  {
    private Context context;
    private static Map<String, String> questionAnswer = new HashMap<>();
    /*
     * Define answers for each given category.
     */
    public CategoryAswer(Context context) {
        String temp = "";
        String[] Answers;
        byte[] buffer;
        try {
            InputStream file = context.getAssets().open("ChatbotAnswers.txt");
            buffer = new byte[file.available()];
            while(file.read(buffer)!=-1){
                temp += new String(buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();


        }

        if (temp != null) {
            Answers = temp.split("/");
            for (String S : Answers) {
                String[] contets = S.split("#");
                questionAnswer.put(contets[0].trim(), contets[1]);
            }
        }
    }
    public String get(String category){
        return questionAnswer.get(category);
    }
}
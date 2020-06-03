package com.pfa.chat_bot;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CategoryAswer  {
    private Context context;
    private static Map<String, String> questionAnswer = new HashMap<>();
    private static Map<String, String> questionAnswerFR = new HashMap<>();
    private static CategoryAswer categoryAswer;
    /*
     * Define answers for each given category.
     * a singleton class
     */
    private CategoryAswer(Context context) {
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
            String collect = "";
            for (String S : Answers) {

                String[] contets = S.split("#");
                collect += contets[0].trim()+"\n";
                questionAnswer.put(contets[0].trim(), contets[1]);
            }
            Log.d("collect",collect);
        }
        temp = "";
        try {
            InputStream file = context.getAssets().open("ChatbotAnswersFR.txt");
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
                questionAnswerFR.put(contets[0].trim(), contets[1]);
            }
        }
    }
    public static void loadAnswers(Context context){

        categoryAswer = new CategoryAswer(context);
    }
    public static String get(String category){
        return questionAnswer.get(category);
    }
    public static String getFR(String category){
        return questionAnswerFR.get(category);
    }
}
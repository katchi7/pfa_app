package com.pfa.ChatBot;



import android.content.Context;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

public class Categorizer {
    private DoccatModel model;
    private DocumentCategorizerME myCategorizer;
    private Context context;
    public  Categorizer(Context context) throws FileNotFoundException, IOException {

        /*
        * Getting the model
        */
        this.context = context;
        InputStream ModelIn = context.getAssets().open("ChaBotModel.bin");
        model = new DoccatModel(ModelIn);
        myCategorizer = new DocumentCategorizerME(model);
    }

    public ArrayList<Integer> detectCategoryIndex(String[] finalTokens) throws IOException {

        // Get best possible category.
        double[] probabilitiesOfOutcomes = myCategorizer.categorize(finalTokens);
        ArrayList<Integer> categories = new ArrayList<Integer>();
        double confusion_coef = 1 ;
        double temp = 1.0/probabilitiesOfOutcomes.length;
        while(temp <= 1){
            temp *= 10;
            confusion_coef /= 10;
        }
        int max = 0;
        for(int i =0 ;i< probabilitiesOfOutcomes.length;i++) {
            System.out.println(probabilitiesOfOutcomes[i] + " : " + myCategorizer.getCategory(i));
            if(probabilitiesOfOutcomes[i]>probabilitiesOfOutcomes[max]) max = i;
        }
        if(probabilitiesOfOutcomes[max] <= 1.0/probabilitiesOfOutcomes.length) {
            categories.add(0);
            return categories ;
        }
        for (int i =0 ;i< probabilitiesOfOutcomes.length;i++){
            if((probabilitiesOfOutcomes[max] - probabilitiesOfOutcomes[i])<confusion_coef) categories.add(i);
        }

        return categories;
    }
    public String getCategory(int index){return myCategorizer.getCategory(index);}
}

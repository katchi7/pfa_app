package com.pfa.ChatBot;


import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;

public class ChatBot{
    private SentenceBreaker breaker;
    private Tokenizer tokenizer;
    private POStagger POStags;
    private Lemmatizer lemma;
    private Categorizer categorizer;
    private CategoryAswer Answers;
    private Boolean conversationComplete = false;
    private Boolean Trained;
    private Context context;
    public ChatBot(Context context)  {
        this.context = context;
        try {
            breaker = new SentenceBreaker(context);
            tokenizer = new Tokenizer(context);
            lemma = new Lemmatizer(context);
            POStags = new POStagger(context);
            Answers = new CategoryAswer(context);
            categorizer = new Categorizer(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("testing","---------------checking : "+POStags);
        Trained = true;
    }
    public String ChatbotAnswer(String userInput) throws IOException {
        // Break users chat input into sentences using sentence detection.
        String[] sentences = breaker.breakSentence(userInput);
        String answer = "";
        // Loop through sentences.
        for (String sentence : sentences) {
            int epoch=0;
            // Separate words from each sentence using tokenizer.
            String[] tokens = new String[0];
                tokens = tokenizer.tokenizeSentence(sentence);

            // Tag separated words with POS tags to understand their gramatical structure.
                String[] posTags = POStags.detectPOSTags(tokens);

                // Lemmatize each word so that its easy to categorize.
                String[] lemmas = lemma.lemmatizeTokens(tokens, posTags);

                // Determine BEST category using lemmatized tokens used a mode that we trained
                // at start.
                ArrayList<Integer> category = categorizer.detectCategoryIndex(lemmas);
            if(category.size()<2){
                answer = answer + " " + Answers.get(categorizer.getCategory(category.get(0)));
                System.out.println("Category : " + categorizer.getCategory(category.get(0)));
            }
            else{
                answer = answer + "Sorry i got confused ,what Do you mean : " ;
                for (int index : category){
                    answer += categorizer.getCategory(index) + " \n ";
                }
            }
            // Get predefined answer from given category & add to answer
            if ("conversation-complete".equals(category)) {
                conversationComplete = true;
            }
        }
        return answer;

    }

    public Boolean getConversationComplete() {
        return conversationComplete;
    }
    public Boolean isTrained(){return Trained; }
}

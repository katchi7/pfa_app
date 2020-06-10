package com.pfa.chat_bot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class ConversationAdapter extends BaseAdapter implements View.OnClickListener{
    private List<Message> Messages;
    private Context context;
    private TextView Message_textView;
    private TextView Message3_textView;
    private TextView Message2_textView;
    private LinearLayout Message_ly;
    private Animation animation1;
    private Animation animation2;
    private static HashMap<String,String> Question_token = new HashMap<>();
    private static HashMap<String,String> token_Question = new HashMap<>();
    static {
        Question_token.put("cuts".trim(),"treatment for cuts.".trim());Question_token.put("animal-bites".trim(),"treatment for animal bites.".trim());Question_token.put("insect-sting".trim(),"treatment for insect stings.".trim());Question_token.put("abdominal-pain".trim(),"treatment for abdominal pain.".trim());Question_token.put("bruises".trim(),"treatment for bruises.".trim());Question_token.put("choking".trim(),"treatment for chokes.".trim());
        Question_token.put("diarrhea".trim(),"treatment for diarrhea.".trim());Question_token.put("dizziness".trim(),"treatment for dizziness.".trim());Question_token.put("thermal-burns".trim(),"treatment for burns".trim());Question_token.put("food-poisoning".trim(),"treatment for food poisoning.".trim());Question_token.put("heart-palpitations".trim(),"treatment for heart palpitations.".trim());Question_token.put("heat-exhaustion".trim(),"treatment for heat exhaustion.".trim());
        Question_token.put("heat-stroke".trim(),"treatment for heat stroke.".trim());Question_token.put("hypothermia".trim(),"treatment for hypothermia.".trim());Question_token.put("nosebleeds".trim(),"treatment for nosebleeds.".trim());Question_token.put("muscle-strain".trim(),"treatment for muscle strain.".trim());Question_token.put("broken-toe".trim(),"treatment for broken toe.".trim());Question_token.put("urine-blood".trim(),"treatment for urine blood.".trim());Question_token.put("sprains-strains".trim(),"sprain treatment.".trim());Question_token.put("sunburn".trim(),"treatment for sunburns.".trim());
    }
    public ConversationAdapter(Context context, List<Message> Messages){
        this.Messages = Messages;
        this.context = context;
        for (String s : Question_token.keySet()){
            token_Question.put(Question_token.get(s).trim(),s);
        }
        animation1 = AnimationUtils.loadAnimation(context,R.anim.loading);
         animation1.setAnimationListener(new Animation.AnimationListener() {
             @Override
             public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {Message_textView.startAnimation(animation2);}
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
         animation2 = AnimationUtils.loadAnimation(context,R.anim.loading2);
         animation2.setAnimationListener(new Animation.AnimationListener() {
             @Override
             public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {Message_textView.startAnimation(animation1);}
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
    @Override
    public int getCount() {
        return Messages.size();
    }

    @Override
    public Object getItem(int position) {
        return Messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message CurentMessage = (Message) getItem(position);
        boolean first = false;
        if(convertView==null){
            first = true;
        }
            if (!CurentMessage.isUser()) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chatbot_message, parent, false);

                Message_textView = convertView.findViewById(R.id.message_body);
                Message2_textView = convertView.findViewById(R.id.message_body2);
                Message3_textView = convertView.findViewById(R.id.message_body3);
                Message_ly = convertView.findViewById(R.id.Message_ly);
                String msg = CurentMessage.getMessage();

                if (msg == "") {
                    Message2_textView.setVisibility(View.GONE);
                    Message3_textView.setVisibility(View.GONE);
                    Message_textView.setText(context.getResources().getString(R.string.typing));
                    Message_textView.setTextSize(15);
                    Message_textView.setVisibility(View.VISIBLE);
                    Message_textView.startAnimation(animation1);

                } else {

                    Message_textView.setTextSize(18);
                    msg = CurentMessage.getMessage();
                    if (msg.contains("Sorry i got confused ")) {
                        String[] message = msg.split(":");
                        Message2_textView.setVisibility(View.VISIBLE);
                        Message3_textView.setVisibility(View.VISIBLE);
                        Message_textView.setText(message[0]);

                        message = message[1].replace("miss-understanding\n", "").replace("greeting\n", "").replace("conversation-complete\n", "").replace("conversation-continue\n", "").split("\\n");
                        Message2_textView.setText(Question_token.get(message[0].trim()));
                        Message3_textView.setText(Question_token.get(message[1].trim()));
                        Message2_textView.setOnClickListener(this::onClick);
                        Message3_textView.setOnClickListener(this::onClick);

                    } else {
                        Message_textView.setText(msg);
                        Message2_textView.setVisibility(View.GONE);
                        Message3_textView.setVisibility(View.GONE);
                    }
                }


            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.user_message, parent, false);
                TextView Message_textView = convertView.findViewById(R.id.message_body);
                Message_textView.setText(CurentMessage.getMessage());

            }

        return convertView;
    }
    public void onClick(View v) {
        TextView textView = (TextView) v;
        String Answer = CategoryAswer.get(token_Question.get(textView.getText().toString().trim()));
        Messages.add(new Message("YourChatbot",Answer,false));
        this.notifyDataSetChanged();
        MainActivity.Messages_lv.smoothScrollToPosition(this.getCount()-1);

    }
}

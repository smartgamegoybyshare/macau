package com.threesing.macau.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.threesing.macau.R;
import com.threesing.macau.Support.Value;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class UserDataList extends BaseAdapter {

    private List<String> user_record;
    private List<View> saveView;

    @SuppressLint("InflateParams")
    public UserDataList(Context context, List<String> user_record) {
        this.user_record = user_record;
        LayoutInflater inflater = LayoutInflater.from(context);
        saveView = new ArrayList<>();
        saveView.clear();
        for(int i = 0; i < user_record.size(); i++){
            View view = inflater.inflate(R.layout.userdata, null);
            saveView.add(view);
        }
    }

    @Override
    public int getCount() {
        return user_record.size();
    }

    @Override
    public Object getItem(int position) {
        return user_record.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view;
        view = saveView.get(position);

        TextView date = view.findViewById(R.id.textView1);
        TextView chart_code = view.findViewById(R.id.textView2);
        TextView remarks = view.findViewById(R.id.textView3);
        TextView gain = view.findViewById(R.id.textView4);
        TextView lose = view.findViewById(R.id.textView5);
        TextView balance = view.findViewById(R.id.textView6);
        CheckBox checkBox = view.findViewById(R.id.checkBox1);
        LinearLayout linearLayout1 = view.findViewById(R.id.linearLayout1);
        LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout2);
        LinearLayout linearLayout3 = view.findViewById(R.id.linearLayout3);
        LinearLayout linearLayout4 = view.findViewById(R.id.linearLayout4);
        LinearLayout linearLayout5 = view.findViewById(R.id.linearLayout5);
        LinearLayout linearLayout6 = view.findViewById(R.id.linearLayout6);
        LinearLayout linearLayout7 = view.findViewById(R.id.linearLayout7);

        if (position != user_record.size() - 1) {
            JSONObject jsonObject;
            String check = "", last_check = "";
            try {
                jsonObject = new JSONObject(user_record.get(position));
                check = jsonObject.get("record_check").toString();
                last_check = jsonObject.get("record_last_check").toString();
                if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    date.setText(jsonObject.get("record_datetime_en").toString());
                    chart_code.setText(jsonObject.get("record_chartcode_en").toString());
                    remarks.setText(jsonObject.get("record_remark").toString());
                }else if(Value.language_flag == 1){
                    date.setText(jsonObject.get("record_datetime_en").toString());
                    chart_code.setText(jsonObject.get("record_chartcode_tw").toString());
                    remarks.setText(jsonObject.get("record_remark").toString());
                }else if(Value.language_flag == 2){
                    date.setText(jsonObject.get("record_datetime_en").toString());
                    chart_code.setText(jsonObject.get("record_chartcode_cn").toString());
                    remarks.setText(jsonObject.get("record_remark").toString());
                }
                String gain_value = jsonObject.get("record_amount_forex").toString();
                String balance_value = jsonObject.get("record_forex_total").toString();
                if (gain_value.contains("-")) {
                    gain.setText("");
                    lose.setText(gain_value);
                    lose.setTextColor(Color.RED);
                } else {
                    gain.setText(gain_value);
                    lose.setText("");
                    gain.setTextColor(Color.BLUE);
                }
                balance.setText(balance_value);
                if (balance_value.contains("-")) {
                    balance.setTextColor(Color.RED);
                } else {
                    balance.setTextColor(Color.BLUE);
                }
                if(check.matches("1")){
                    checkBox.setChecked(true);
                }else {
                    checkBox.setChecked(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(check.matches("1")){
                if(last_check.matches("0")) {
                    linearLayout1.setBackgroundResource(R.drawable.datalist_start_frame_gray);
                    linearLayout2.setBackgroundResource(R.drawable.datalist_frame_gray);
                    linearLayout3.setBackgroundResource(R.drawable.datalist_frame_gray);
                    linearLayout4.setBackgroundResource(R.drawable.datalist_frame_gray);
                    linearLayout5.setBackgroundResource(R.drawable.datalist_frame_gray);
                    linearLayout6.setBackgroundResource(R.drawable.datalist_frame_gray);
                    linearLayout7.setBackgroundResource(R.drawable.datalist_frame_gray);
                }else if(last_check.matches("1")){
                    linearLayout1.setBackgroundResource(R.drawable.datalist_start_frame_yellow);
                    linearLayout2.setBackgroundResource(R.drawable.datalist_frame_yellow);
                    linearLayout3.setBackgroundResource(R.drawable.datalist_frame_yellow);
                    linearLayout4.setBackgroundResource(R.drawable.datalist_frame_yellow);
                    linearLayout5.setBackgroundResource(R.drawable.datalist_frame_yellow);
                    linearLayout6.setBackgroundResource(R.drawable.datalist_frame_yellow);
                    linearLayout7.setBackgroundResource(R.drawable.datalist_frame_yellow);
                }
            }else if(check.matches("2")){
                if(last_check.matches("0")) {
                    linearLayout1.setBackgroundResource(R.drawable.datalist_start_frame_red);
                    linearLayout2.setBackgroundResource(R.drawable.datalist_frame_red);
                    linearLayout3.setBackgroundResource(R.drawable.datalist_frame_red);
                    linearLayout4.setBackgroundResource(R.drawable.datalist_frame_red);
                    linearLayout5.setBackgroundResource(R.drawable.datalist_frame_red);
                    linearLayout6.setBackgroundResource(R.drawable.datalist_frame_red);
                    linearLayout7.setBackgroundResource(R.drawable.datalist_frame_red);
                }else if(last_check.matches("1")){
                    linearLayout1.setBackgroundResource(R.drawable.datalist_start_frame_darkblue);
                    linearLayout2.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                    linearLayout3.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                    linearLayout4.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                    linearLayout5.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                    linearLayout6.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                    linearLayout7.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                }
            }
            else {
                if (position % 2 == 0) {
                    linearLayout1.setBackgroundResource(R.drawable.datalist_start_left_frame);
                    linearLayout2.setBackgroundResource(R.drawable.datalist_frame_white);
                    linearLayout3.setBackgroundResource(R.drawable.datalist_frame_white);
                    linearLayout4.setBackgroundResource(R.drawable.datalist_frame_white);
                    linearLayout5.setBackgroundResource(R.drawable.datalist_frame_white);
                    linearLayout6.setBackgroundResource(R.drawable.datalist_frame_white);
                    linearLayout7.setBackgroundResource(R.drawable.datalist_frame_white);
                } else {
                    linearLayout1.setBackgroundResource(R.drawable.datalist_start_frame_blue);
                    linearLayout2.setBackgroundResource(R.drawable.datalist_frame_blue);
                    linearLayout3.setBackgroundResource(R.drawable.datalist_frame_blue);
                    linearLayout4.setBackgroundResource(R.drawable.datalist_frame_blue);
                    linearLayout5.setBackgroundResource(R.drawable.datalist_frame_blue);
                    linearLayout6.setBackgroundResource(R.drawable.datalist_frame_blue);
                    linearLayout7.setBackgroundResource(R.drawable.datalist_frame_blue);
                }
            }
        } else {
            JSONObject jsonObject;
            try {
                Log.e("123", "進來");
                jsonObject = new JSONObject(user_record.get(user_record.size() - 1));
                remarks.setGravity(Gravity.END);
                remarks.setPadding(0,0,10, 0);
                remarks.setTextSize(9);
                if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    remarks.setText("Total");
                }else if(Value.language_flag == 1){
                    remarks.setText("總數");
                }else if(Value.language_flag == 2){
                    remarks.setText("总数");
                }
                String gain_value = jsonObject.get("surplus").toString();
                String lose_value = jsonObject.get("loss").toString();
                String balance_value = jsonObject.get("surplus_loss").toString();
                checkBox.setButtonDrawable(null);
                if (gain_value.contains("-")) {
                    gain.setText(gain_value);
                    gain.setTextColor(Color.RED);
                } else {
                    gain.setText(gain_value);
                    gain.setTextColor(Color.BLUE);
                }
                lose.setText(lose_value);
                lose.setTextColor(Color.RED);
                /*if (lose_value.contains("-")) {
                    lose.setText(lose_value);
                    lose.setTextColor(Color.RED);
                } else {
                    lose.setText(lose_value);
                    lose.setTextColor(Color.BLUE);
                }*/
                if (balance_value.contains("-")) {
                    balance.setText(balance_value);
                    balance.setTextColor(Color.RED);
                } else {
                    balance.setText(balance_value);
                    balance.setTextColor(Color.BLUE);
                }

                date.setText("");
                chart_code.setText("");

                //if(position % 2 == 0) {
                    linearLayout1.setBackgroundResource(R.drawable.liststyle_left);
                    linearLayout2.setBackgroundResource(R.drawable.datalist_total_buttom);
                    linearLayout3.setBackgroundResource(R.drawable.datalist_total_frame);
                    linearLayout4.setBackgroundResource(R.drawable.datalist_total_frame);
                    linearLayout5.setBackgroundResource(R.drawable.datalist_total_frame);
                    linearLayout6.setBackgroundResource(R.drawable.datalist_total_frame);
                    linearLayout7.setBackgroundResource(R.drawable.liststyle_right);
                /*}else {
                    linearLayout1.setBackgroundResource(R.drawable.liststyle_left_blue);
                    linearLayout2.setBackgroundResource(R.drawable.datalist_total_buttom_blue);
                    linearLayout3.setBackgroundResource(R.drawable.datalist_frame_blue);
                    linearLayout4.setBackgroundResource(R.drawable.datalist_frame_blue);
                    linearLayout5.setBackgroundResource(R.drawable.datalist_frame_blue);
                    linearLayout6.setBackgroundResource(R.drawable.liststyle_right_blue);
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
/*
        else {
            TextView total = view.findViewById(R.id.textView1);
            TextView gain = view.findViewById(R.id.textView2);
            TextView lose = view.findViewById(R.id.textView3);
            TextView balance = view.findViewById(R.id.textView4);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(user_record.get(user_record.size() - 1));
                String gain_value = jsonObject.get("surplus").toString();
                String lose_value = jsonObject.get("loss").toString();
                String balance_value = jsonObject.get("surplus_loss").toString();
                total.setText("Total");
                if (gain_value.contains("-")) {
                    gain.setText(gain_value);
                    gain.setTextColor(Color.RED);
                } else {
                    gain.setText(gain_value);
                    gain.setTextColor(Color.BLUE);
                }
                if (lose_value.contains("-")) {
                    lose.setText(lose_value);
                    lose.setTextColor(Color.RED);
                } else {
                    lose.setText(lose_value);
                    lose.setTextColor(Color.BLUE);
                }
                if (balance_value.contains("-")) {
                    balance.setText(balance_value);
                    balance.setTextColor(Color.RED);
                } else {
                    balance.setText(balance_value);
                    balance.setTextColor(Color.BLUE);
                }

                total.setBackgroundResource(R.drawable.datalist_start_frame);
                gain.setBackgroundResource(R.drawable.datalist_frame);
                lose.setBackgroundResource(R.drawable.datalist_frame);
                balance.setBackgroundResource(R.drawable.datalist_frame);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
        return view;
    }
}

package com.threesing.macau.ListView.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.threesing.macau.R;
import com.threesing.macau.Support.Value;
import java.util.ArrayList;
import java.util.List;

public class NewsList extends BaseAdapter {

    private List<String> new_api;
    private List<View> viewlist;
    private TextView newsText, textView1;
    private int nowPosition;

    public NewsList(Context context, List<String> new_api){
        this.new_api = new_api;
        viewlist = new ArrayList<>();
        viewlist.clear();
        for (int i = 0; i < new_api.size(); i++) {
            if (i == 0) {
                View view;
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.news_title, null);
                viewlist.add(view);
            } else {
                View view;
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.news_api, null);
                viewlist.add(view);
            }
        }
    }

    public void setLangueage(){

    }

    @Override
    public int getCount() {
        return new_api.size();
    }

    @Override
    public Object getItem(int position) {
        return new_api.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {   //禁用item被點擊
        return false;
    }
    @Override
    public boolean isEnabled(int position) {    //禁用item被點擊
        return false;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view;

        this.nowPosition = position;
        view = viewlist.get(position);

        if(position == 0){
            newsText = view.findViewById(R.id.textView1);
            if(Value.language_flag == 0){
                newsText.setText("News");
            }else if(Value.language_flag == 1){
                newsText.setText("最新訊息");
            }else if(Value.language_flag == 2){
                newsText.setText("最新信息");
            }
        }
        else {
            textView1 = view.findViewById(R.id.textView1);
            if(Value.language_flag == 0){
                textView1.setText(new_api.get(nowPosition));
            }else if(Value.language_flag == 1){
                textView1.setText(new_api.get(nowPosition));
            }else if(Value.language_flag == 2){
                textView1.setText(new_api.get(nowPosition));
            }
        }

        return view;
    }
}

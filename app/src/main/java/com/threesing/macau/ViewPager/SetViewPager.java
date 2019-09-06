package com.threesing.macau.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.threesing.macau.R;
import com.threesing.macau.Support.Value;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SetViewPager {

    private String TAG = "SetViewPager";

    public SetViewPager() {
        super();
    }

    public View setView(Context context, List<JSONObject> getList) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        @SuppressLint("InflateParams")
        View view = layoutInflater.inflate(R.layout.tablayoutview, null);

        ListView listView = view.findViewById(R.id.listview);

        try {
            List<String> news_api = new ArrayList<>();
            news_api.clear();
            for (int i = 0; i < getList.size(); i++) {
                JSONObject jsonObject = getList.get(i);
                if (jsonObject.get("post_category").toString().matches("marquee")) {
                    Log.e(TAG, "公告資訊");
                }else {
                    String news = "";
                    if(Value.language_flag == 0){
                        news = jsonObject.get("post_name_en").toString();
                    }else if(Value.language_flag == 1){
                        news = jsonObject.get("post_name_tw").toString();
                    }else if(Value.language_flag == 2){
                        news = jsonObject.get("post_name_cn").toString();
                    }
                    news_api.add(news);
                    Log.e(TAG, "news_api = " + news_api);
                }
                NewsList newsList = new NewsList(context, news_api);
                listView.setAdapter(newsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}

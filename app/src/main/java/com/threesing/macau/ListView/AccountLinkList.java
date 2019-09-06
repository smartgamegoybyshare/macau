package com.threesing.macau.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.threesing.macau.ListView.buttonItem.ButtonHolder;
import com.threesing.macau.ListView.buttonItem.GetButtonItem;
import com.threesing.macau.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AccountLinkList extends BaseAdapter{

    private List<String> user_Link;
    private List<View> saveView;
    private List<ButtonHolder> saveHolder;
    private GetButtonItem getButtonItem;
    private String nowcompany, nowaccount;

    public AccountLinkList(Context context, List<String> user_Link, GetButtonItem getButtonItem,
                           String company, String account) {
        saveView = new ArrayList<>();
        saveHolder = new ArrayList<>();
        saveView.clear();
        saveHolder.clear();
        this.user_Link = user_Link;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.getButtonItem = getButtonItem;
        this.nowcompany = company;
        this.nowaccount= account;
        for(int i = 0; i < user_Link.size(); i++){
            @SuppressLint("InflateParams")
            View view = inflater.inflate(R.layout.linkchose, null);
            ButtonHolder buttonHolder = new ButtonHolder();
            saveView.add(view);
            saveHolder.add(buttonHolder);
        }
    }

    @Override
    public int getCount() {
        return user_Link.size();
    }

    @Override
    public Object getItem(int position) {
        return user_Link.get(position);
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

        try {
            JSONObject jsonObject = new JSONObject(user_Link.get(position));
            LinearLayout linearLayout1 = view.findViewById(R.id.linearLayout1);
            LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout2);
            TextView textView1 = view.findViewById(R.id.textView1);
            Button button = view.findViewById(R.id.button1);

            String company = jsonObject.get("link_from_code").toString();
            String account = jsonObject.get("link_from_user").toString();

            textView1.setText(company);
            button.setText(account);

            if(nowcompany.matches(company) && nowaccount.matches(account)){
                button.setBackgroundResource(R.drawable.accountlinkbutton_mine);
            }else {
                button.setBackgroundResource(R.drawable.accountlinkbutton_other);
            }

            if(position != user_Link.size() - 1){
                linearLayout1.setBackgroundResource(R.drawable.datalist_start_frame);
                linearLayout2.setBackgroundResource(R.drawable.datalist_frame);
            }else {
                linearLayout1.setBackgroundResource(R.drawable.linksettingstyle_left);
                linearLayout2.setBackgroundResource(R.drawable.liststyle_right);
            }

            saveHolder.get(position).button = button;
            saveHolder.get(position).button.setOnClickListener(view1 -> getButtonItem.clickItem(view1,
                    company, account));
            saveHolder.get(position).button.setTag(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}

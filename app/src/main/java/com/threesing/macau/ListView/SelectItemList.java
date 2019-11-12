package com.threesing.macau.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.threesing.macau.R;
import com.threesing.macau.Support.Value;

import java.util.List;

public class SelectItemList extends BaseAdapter {

    private List<String> select_context;
    private List<String> select_title;
    private Context context;

    public SelectItemList(Context context, List<String> select_title, List<String> select_context) {
        this.select_context = select_context;
        this.select_title = select_title;
        this.context = context;
    }

    @Override
    public int getCount() {
        return select_title.size();
    }

    @Override
    public Object getItem(int position) {
        return select_title.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewGroup view;

        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView != null) {
            view = (ViewGroup) convertView;
        } else {
            view = (ViewGroup) inflater.inflate(R.layout.selectdata, null);
        }

        TextView textView1 = view.findViewById(R.id.textView1);
        TextView textView2 = view.findViewById(R.id.textView2);

        if(position != select_title.size() - 1) {
            textView1.setText(select_title.get(position));
            textView2.setText(select_context.get(position));
        }else {
            textView1.setText(select_title.get(position));
            if(select_context.get(position).matches("0")){
                if (Value.language_flag == 0) {
                    textView2.setText("Unchecked");
                }else if (Value.language_flag == 1) {
                    textView2.setText("未對帳");
                }else if (Value.language_flag == 2) {
                    textView2.setText("未对帐");
                }
            }else {
                if (Value.language_flag == 0) {
                    textView2.setText("Checked");
                }else if (Value.language_flag == 1) {
                    textView2.setText("已對帳");
                }else if (Value.language_flag == 2) {
                    textView2.setText("已对帐");
                }
            }
        }

        return view;
    }
}

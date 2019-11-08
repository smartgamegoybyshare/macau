package com.threesing.macau.Support;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.threesing.macau.ListView.SelectItemList;
import com.threesing.macau.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectDialog {

    private String TAG = "SelectDialog";
    private Context context;
    private Screen screen;
    private Dialog newDialog = null;
    private int Width, Height;

    public SelectDialog(Context context) {
        this.context = context;
        screen = new Screen(context);
    }

    public void show(String select) {

        DisplayMetrics dm = screen.size();
        Width = dm.widthPixels;
        Height = dm.heightPixels;

        newDialog = showDialog(context, select);
        newDialog.show();
        //newDialog.setCanceledOnTouchOutside(false);
    }

    @SuppressLint("SetTextI18n")
    private Dialog showDialog(Context context, String select) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable());

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.selectdialog, null);

        ConstraintLayout constraintLayout = v.findViewById(R.id.constraintlayout);
        ListView listView = v.findViewById(R.id.listview1); //詳細清單
        //Button button = v.findViewById(R.id.button1);   //關閉

        List<String> select_title = new ArrayList<>();
        List<String> select_context = new ArrayList<>();
        select_title.clear();
        select_context.clear();
        JSONObject jsonObject;
        String date, chartcode = "", remark, forex, balance, checked, last_check;
        try {
            jsonObject = new JSONObject(select);
            date = jsonObject.get("record_datetime").toString();  //時間
            remark = jsonObject.get("record_remark").toString();   //備註
            forex = jsonObject.get("record_amount_forex").toString(); //盈虧金額
            balance = jsonObject.get("record_forex_total").toString();  //餘額
            checked = jsonObject.get("record_check").toString();  //是否已對帳
            last_check = jsonObject.get("record_last_check").toString();  //是否重新對帳

            Log.e(TAG, "jsonObject = " + jsonObject);
            if (Value.language_flag == 0) {
                //button.setText("close");
                chartcode = jsonObject.get("record_chartcode_en").toString(); //交易
                select_title.add("Date：");
                select_title.add("Chart Code：");
                select_title.add("Remarks：");
                if(forex.contains("-")){
                    select_title.add("Loss：");
                }else {
                    select_title.add("Gain：");
                }
                select_title.add("Balance：");
                select_title.add("Checked：");
            } else if (Value.language_flag == 1) {
                //button.setText("關閉");
                chartcode = jsonObject.get("record_chartcode_tw").toString(); //交易
                select_title.add("日期：");
                select_title.add("交易：");
                select_title.add("備註：");
                if(forex.contains("-")){
                    select_title.add("虧：");
                }else {
                    select_title.add("盈：");
                }
                select_title.add("餘額：");
                select_title.add("對帳：");
            } else if (Value.language_flag == 2) {
                //button.setText("关闭");
                chartcode = jsonObject.get("record_chartcode_cn").toString(); //交易
                select_title.add("日期：");
                select_title.add("交易：");
                select_title.add("备注：");
                if(forex.contains("-")){
                    select_title.add("亏：");
                }else {
                    select_title.add("盈：");
                }
                select_title.add("馀额：");
                select_title.add("对帐：");
            }

            select_context.add(date);
            select_context.add(chartcode);
            select_context.add(remark);
            select_context.add(forex);
            select_context.add(balance);
            select_context.add(checked);

            SelectItemList selectItemList = new SelectItemList(context, select_title, select_context);
            listView.setAdapter(selectItemList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        constraintLayout.setOnClickListener(v1 -> progressDialog.dismiss());
        //button.setOnClickListener(v1 -> progressDialog.dismiss());

        if (Height > Width) {
            progressDialog.setContentView(v, new LinearLayout.LayoutParams(Width, Height));
        } else {
            progressDialog.setContentView(v, new LinearLayout.LayoutParams(Width, Height));
        }

        return progressDialog;
    }
}

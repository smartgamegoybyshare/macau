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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.threesing.macau.ListView.SelectTotalList;
import com.threesing.macau.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectTotalDialog {

    private String TAG = "SelectDialog";
    private Context context;
    private Screen screen;
    private Dialog newDialog = null;
    private int Width, Height;

    public SelectTotalDialog(Context context) {
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
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.selecttotaldialog, null);

        ConstraintLayout constraintLayout = v.findViewById(R.id.constraintlayout);
        TextView textView = v.findViewById(R.id.textView1);
        ListView listView = v.findViewById(R.id.listview1); //詳細清單
        //Button button = v.findViewById(R.id.button1);   //關閉

        List<String> select_title = new ArrayList<>();
        List<String> select_context = new ArrayList<>();
        select_title.clear();
        select_context.clear();
        JSONObject jsonObject;
        String gain, loss, balance;
        try {
            jsonObject = new JSONObject(select);
            gain = jsonObject.get("surplus").toString();   //盈
            loss = jsonObject.get("loss").toString();   //虧
            balance = jsonObject.get("surplus_loss").toString();  //餘額

            Log.e(TAG, "jsonObject = " + jsonObject);
            if (Value.language_flag == 0) {
                textView.setText("Total");
                //button.setText("close");
                select_title.add("Gain：");
                select_title.add("Loss：");
                select_title.add("Balance：");
            } else if (Value.language_flag == 1) {
                textView.setText("總數");
                //button.setText("關閉");
                select_title.add("盈：");
                select_title.add("虧：");
                select_title.add("餘額：");
            } else if (Value.language_flag == 2) {
                textView.setText("总数");
                //button.setText("关闭");
                select_title.add("盈：");
                select_title.add("亏：");
                select_title.add("馀额：");
            }

            select_context.add(gain);
            select_context.add(loss);
            select_context.add(balance);

            SelectTotalList selectTotalList = new SelectTotalList(context, select_title, select_context);
            listView.setAdapter(selectTotalList);
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

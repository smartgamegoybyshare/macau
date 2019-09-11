package com.threesing.macau.Support;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.threesing.macau.R;
import java.util.Objects;

public class Loading {

    private Context context;
    private Screen screen;
    private Dialog newDialog = null;
    private int Width, Height;

    public Loading(Context context){
        this.context = context;
        screen = new Screen(context);
    }

    public void show(String str){

        DisplayMetrics dm = screen.size();
        Width = dm.widthPixels;
        Height = dm.heightPixels;

        newDialog = showDialog(context, str);
        newDialog.show();
        newDialog.setCanceledOnTouchOutside(false);
    }

    private Dialog showDialog(Context context, String str){
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable());

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.waiting, null);
        TextView message = v.findViewById(R.id.message);

        if (str == null || str.equals("")) {
            message.setVisibility(View.GONE);
        } else {
            message.setText(str);
            message.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }

        if(Height > Width) {
            progressDialog.setContentView(v, new LinearLayout.LayoutParams(Width,Height));
        }
        else {
            progressDialog.setContentView(v, new LinearLayout.LayoutParams(Width,Height));
        }

        return progressDialog;
    }

    public void dismiss(){
        if(newDialog != null && newDialog.isShowing())
            newDialog.dismiss();
    }

    public boolean isshow(){
        return newDialog.isShowing();
    }
}

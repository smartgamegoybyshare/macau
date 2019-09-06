package com.threesing.macau.Language;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.threesing.macau.R;
import com.threesing.macau.SQL.LanguageSQL;
import com.threesing.macau.Support.Screen;
import com.threesing.macau.Support.Value;
import java.util.Objects;

public class LanguageChose {

    private Context context;
    private Screen screen;
    private Dialog newDialog = null;
    private int Width, Height;

    public LanguageChose(Context context) {
        this.context = context;
        screen = new Screen(context);
    }

    public void show(SetLanguage setLanguage, LanguageSQL languageSQL) {

        DisplayMetrics dm = screen.size();
        Width = dm.widthPixels;
        Height = dm.heightPixels;

        newDialog = showDialog(context, setLanguage, languageSQL);
        newDialog.show();
        newDialog.setCanceledOnTouchOutside(false);
    }

    private Dialog showDialog(Context context, SetLanguage setLanguage, LanguageSQL languageSQL) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable());

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.language, null);

        TextView Cht = v.findViewById(R.id.textView1);
        TextView Chs = v.findViewById(R.id.textView2);
        TextView Eng = v.findViewById(R.id.textView3);

        Cht.setOnClickListener(view -> {
            Value.language_flag = 1;
            languageSQL.insert(Value.language_flag);
            setLanguage.isSet();
            dismiss();
        });

        Chs.setOnClickListener(view -> {
            Value.language_flag = 2;
            languageSQL.insert(Value.language_flag);
            setLanguage.isSet();
            dismiss();
        });

        Eng.setOnClickListener(view -> {
            Value.language_flag = 0;
            languageSQL.insert(Value.language_flag);
            setLanguage.isSet();
            dismiss();
        });

        if (Height > Width) {
            progressDialog.setContentView(v, new LinearLayout.LayoutParams(Width, Height));
        } else {
            progressDialog.setContentView(v, new LinearLayout.LayoutParams(Width, Height));
        }

        progressDialog.setOnKeyListener((dialog, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0);

        return progressDialog;
    }

    public void dismiss() {
        if (newDialog != null && newDialog.isShowing())
            newDialog.dismiss();
    }
}

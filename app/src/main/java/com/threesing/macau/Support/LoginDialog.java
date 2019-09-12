package com.threesing.macau.Support;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.threesing.macau.Post_Get.Login.Connected;
import com.threesing.macau.Post_Get.Login.GetConnect;
import com.threesing.macau.R;
import com.threesing.macau.SQL.LoginSQL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginDialog {

    private Context context;
    private Screen screen;
    private Dialog newDialog = null;
    private int Width, Height;
    private String company, account, password;

    public LoginDialog(Context context) {
        this.context = context;
        screen = new Screen(context);
    }

    public void show(Loading loading, LoginSQL loginSQL, Connected connected, GetConnect getConnect) {

        DisplayMetrics dm = screen.size();
        Width = dm.widthPixels;
        Height = dm.heightPixels;

        newDialog = showDialog(context, loading, loginSQL, connected, getConnect);
        newDialog.show();
        newDialog.setCanceledOnTouchOutside(false);
    }

    @SuppressLint("SetTextI18n")
    private Dialog showDialog(Context context, Loading loading, LoginSQL loginSQL,
                              Connected connected, GetConnect getConnect) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable());

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.logindialog, null);

        EditText editText1 = v.findViewById(R.id.editText1);   //公司
        EditText editText2 = v.findViewById(R.id.editText2);  //戶口
        EditText editText3 = v.findViewById(R.id.editText3);  //密碼
        CheckBox checkBox = v.findViewById(R.id.checkBox);    //記住我的登入資訊
        Button login = v.findViewById(R.id.button1);   //登入按鈕
        Button tryin = v.findViewById(R.id.button2);   //試用按鈕
        ImageView imageView = v.findViewById(R.id.imageView);

        if (Value.language_flag == 0) {
            editText1.setHint("Sub Account");
            editText2.setHint("User");
            editText3.setHint("Password");
            checkBox.setText("  Remember Me");
            login.setText("Login");
            tryin.setText("Try");
        } else if (Value.language_flag == 1) {
            editText1.setHint("分公司/子帳號");
            editText2.setHint("戶口");
            editText3.setHint("密碼");
            checkBox.setText("  記住我的登入資訊");
            login.setText("登入");
            tryin.setText("試用");
        } else if (Value.language_flag == 2) {
            editText1.setHint("分公司/子帐号");
            editText2.setHint("户口");
            editText3.setHint("密码");
            checkBox.setText("  记住我的登陆资讯");
            login.setText("登陆");
            tryin.setText("试用");
        }

        List<String> dataList = new ArrayList<>();
        dataList.clear();
        dataList = loginSQL.getlist();
        if (dataList.size() != 0) {
            editText1.setText(dataList.get(0));
            editText2.setText(dataList.get(1));
            editText3.setText(dataList.get(2));
            checkBox.setChecked(true);
        }

        imageView.setOnClickListener(view -> progressDialog.dismiss());

        login.setOnClickListener(view -> {
            company = editText1.getText().toString().trim();
            account = editText2.getText().toString().trim();
            password = editText3.getText().toString().trim();

            if (company.matches("")) {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(context, "Sub Account is empty", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(context, "子帳號不可為空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(context, "子帐号不可为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (account.matches("")) {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(context, "User is empty", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(context, "戶口不可為空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(context, "户口不可为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (password.matches("")) {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(context, "Password is empty", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(context, "密碼不可為空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(context, "密码不可为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    loading.show("Logining...");
                } else if (Value.language_flag == 1) {
                    loading.show("登入中...");
                } else if (Value.language_flag == 2) {
                    loading.show("登陆中...");
                }
                progressDialog.dismiss();
                connected.setConnect(company, account, password, getConnect, checkBox);
            }
        });

        tryin.setOnClickListener(view -> {
            company = "api01";
            account = "demo";
            password = "demo";
            if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                loading.show("Logining...");
            } else if (Value.language_flag == 1) {
                loading.show("登入中...");
            } else if (Value.language_flag == 2) {
                loading.show("登陆中...");
            }
            progressDialog.dismiss();
            connected.setConnect(company, account, password, getConnect, checkBox);
        });

        if (Height > Width) {
            progressDialog.setContentView(v, new LinearLayout.LayoutParams(Width, Height));
        } else {
            progressDialog.setContentView(v, new LinearLayout.LayoutParams(Width, Height));
        }

        return progressDialog;
    }
}

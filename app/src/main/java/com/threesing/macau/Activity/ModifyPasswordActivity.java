package com.threesing.macau.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.threesing.macau.Post_Get.ChangePassword.ChangePassword;
import com.threesing.macau.Post_Get.ChangePassword.ChangePasswordListener;
import com.threesing.macau.Post_Get.ChangePassword.PostChangePassword;
import com.threesing.macau.R;
import com.threesing.macau.Support.InternetImage;
import com.threesing.macau.Support.Loading;
import com.threesing.macau.Support.Value;
import org.json.JSONException;
import org.json.JSONObject;
import pl.droidsonroids.gif.GifImageView;

public class ModifyPasswordActivity extends AppCompatActivity implements ChangePasswordListener {

    private String TAG = "ModifyPasswordActivity";
    private String company, account, act;
    private Bitmap preview_bitmap;
    private GifImageView gifImageView1;
    private Handler handler = new Handler();
    private Loading loading = new Loading(this);
    private ChangePassword changePassword = new ChangePassword(this);
    private PostChangePassword postChangePassword = new PostChangePassword();
    private InternetImage internetImage = new InternetImage();
    private EditText editText1, editText2, editText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //隱藏標題欄
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modifypassword);
        get_Intent();
    }

    private void get_Intent() {
        Intent intent = getIntent();
        company = intent.getStringExtra("company");
        account = intent.getStringExtra("account");
        act  = intent.getStringExtra("act");
        Log.e(TAG, "company = " + company);
        Log.e(TAG, "account = " + account);

        postChangePassword.setListener(this);
        showview();
    }

    @SuppressLint("SetTextI18n")
    private void showview() {
        TextView textView = findViewById(R.id.textView);    //title:修改密碼
        TextView textView1 = findViewById(R.id.textView1);  //title:返回
        TextView textView2 = findViewById(R.id.textView2);  //舊密碼：
        editText1 = findViewById(R.id.editText1);  //舊密碼輸入框
        TextView textView3 = findViewById(R.id.textView3);  //新密碼(至少含6位元長度)：
        editText2 = findViewById(R.id.editText2);  //新密碼(至少含6位元長度)輸入框
        TextView textView4 = findViewById(R.id.textView4);  //新密碼確認：
        editText3 = findViewById(R.id.editText3);  //新密碼確認輸入框
        Button button = findViewById(R.id.button);  //修改按鈕
        TextView copyright = findViewById(R.id.copyright);  //下方版權
        TextView nowTime = findViewById(R.id.nowTime);  //資料庫數據更新時間
        gifImageView1 = findViewById(R.id.imageView1); //廣告欄
        Runnable getimage = () -> {
            String imageUri = "https://dl.kz168168.com/img/omen-ad05.png";
            preview_bitmap = internetImage.fetchImage(imageUri);
            handler.post(() -> {
                gifImageView1.setImageBitmap(preview_bitmap);
                gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        };
        new Thread(getimage).start();
            /*GifDrawable gifFromPath = new GifDrawable(this.getResources(), R.drawable.adphoto);
            gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            gifImageView1.setImageDrawable(gifFromPath);*/
        gifImageView1.setOnClickListener(view -> {
            //vibrator.vibrate(100);
            Uri uri = Uri.parse("http://181282.com/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            textView.setText("Change Password");
            textView1.setText("back");
            textView2.setText("Old Password");
            textView3.setText("New Password");
            textView4.setText("Confirm New Password");
            editText1.setHint("Old Password");
            editText2.setHint("At least 6 characters long");
            editText3.setHint("Confirm new Password");
            button.setText("Comfirm");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }else if(Value.language_flag == 1){
            textView.setText("修改密碼");
            textView1.setText("返回");
            textView2.setText("舊密碼");
            textView3.setText("新密碼");
            textView4.setText("確認新密碼");
            editText1.setHint("舊密碼");
            editText2.setHint("至少含6字元長度");
            editText3.setHint("確認新密碼");
            button.setText("確認");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }else if(Value.language_flag == 2){
            textView.setText("修改密码");
            textView1.setText("返回");
            textView2.setText("旧密码");
            textView3.setText("新密码");
            textView4.setText("确认新密码");
            editText1.setHint("旧密码");
            editText2.setHint("最少6位");
            editText3.setHint("确认新密码");
            button.setText("确认");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }

        textView1.setOnClickListener(view -> backform());

        button.setOnClickListener(view -> {
            String old_password = editText1.getText().toString().trim();    //獲取舊密碼輸入框之字串
            String new_password = editText2.getText().toString().trim();    //獲取新密碼(至少含6位元長度)輸入框之字串
            String new_check = editText3.getText().toString().trim();   //獲取新密碼確認輸入框之字串
            if (old_password.matches("")) {   //若舊密碼輸入框之字串為空
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Please input the old password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "請輸入舊密碼", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "请输入旧密码", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (new_password.matches("")) { //若獲取新密碼(至少含6位元長度)輸入框之字串為空
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Please input the new password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "請輸入新密碼", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (new_check.matches("")) {    //若獲取新密碼確認輸入框之字串為空
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Please input the new password check", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "請輸入新密碼確認", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "请输入新密码确认", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (!new_password.matches(new_check)) { //若獲取新密碼(至少含6位元長度)輸入框之字串與獲取新密碼確認輸入框之字串不相符
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "New password and new password check is incorrect", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "新密碼與新密碼確認不符", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "新密码与新密码确认不符", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (new_password.length() < 6) { //新密碼長度小於6
                Toast.makeText(ModifyPasswordActivity.this, "至少含6位元長度", Toast.LENGTH_SHORT).show();
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "At least six bytes", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "至少含6位元長度", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "至少含6位元长度", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    loading.show("Getting data");
                } else if (Value.language_flag == 1) {
                    loading.show("取得資料中");
                } else if (Value.language_flag == 2) {
                    loading.show("获取资料中");
                }
                changePassword.setConnect(company, account, old_password, new_password, postChangePassword);
            }
        });
    }

    private void backform() {
        Intent intent;
        if(act.matches("login")){
            intent = new Intent(this, LoginMainActivity.class);
        }else {
            intent = new Intent(this, FormActivity.class);
        }
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                backform();
            }
            break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void getChange(JSONObject responseJson) {
        try {
            Log.e(TAG, "getChange");
            String result = responseJson.get("result").toString();
            Log.e(TAG, "success = " + responseJson);
            if (result.matches("ok")) {
                loading.dismiss();
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Changing Password Successfully", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(Value.language_flag == 1){
                    Toast toast = Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(Value.language_flag == 2){
                    Toast toast = Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            } else if (result.matches("error1")) {
                loading.dismiss();
                if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Changing Password Failed", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(Value.language_flag == 1){
                    Toast toast = Toast.makeText(this, "密碼修改失敗", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(Value.language_flag == 2){
                    Toast toast = Toast.makeText(this, "密码修改失败", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            } else if (result.matches("error3")) {
                loading.dismiss();
                if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Sub Account Does Not Exist", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(Value.language_flag == 1){
                    Toast toast = Toast.makeText(this, "子帳號不存在", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(Value.language_flag == 2){
                    Toast toast = Toast.makeText(this, "子帐号不存在", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

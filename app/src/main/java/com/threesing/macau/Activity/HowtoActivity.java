package com.threesing.macau.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.threesing.macau.Language.LanguageListener;
import com.threesing.macau.Language.SetLanguage;
import com.threesing.macau.R;
import com.threesing.macau.Support.InternetImage;
import com.threesing.macau.Support.Value;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class HowtoActivity extends AppCompatActivity implements LanguageListener {

    private String TAG = "HowtoActivity";
    private SetLanguage setLanguage = new SetLanguage();
    private TextView title, back, copyright, nowTime;
    private Bitmap preview_bitmap;
    private GifImageView gifImageView1;
    private Handler handler = new Handler();
    private InternetImage internetImage = new InternetImage();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //隱藏標題欄
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.howtopage);

        title = findViewById(R.id.textView);   //對帳通知
        back = findViewById(R.id.textView1);   //返回
        TextView textView3 = findViewById(R.id.textView3);
        TextView textView4 = findViewById(R.id.textView4);
        copyright = findViewById(R.id.copyright);
        nowTime = findViewById(R.id.nowTime);
        gifImageView1 = findViewById(R.id.imageView1); //廣告欄
        GifImageView gifImageView2 = findViewById(R.id.imageView2); //說明圖
        try {
            Runnable getimage = () -> {
                String imageUri = "https://dl.kz168168.com/img/android-ad02.png";
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
                Uri uri = Uri.parse("http://181282.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });

            GifDrawable gifFromPath2 = new GifDrawable(this.getResources(), R.drawable.howto);
            gifImageView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            gifImageView2.setImageDrawable(gifFromPath2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Value.language_flag == 0){
            textView3.setText(" Please press \"Check All\"");
            textView4.setText("after confirm the details.");
        }else if(Value.language_flag == 1){
            textView3.setText("敬愛的客戶您好，提醒您於帳務核對無誤後，");
            textView4.setText("按下「確認對帳」按鈕完成對帳確認。");
        }else if(Value.language_flag == 2){
            textView3.setText("敬爱的客户您好，提醒您于帐务核对无误后，");
            textView4.setText("按下「确认对帐」按钮完成对帐确认。");
        }

        back.setOnClickListener(view -> homePage());
        Value.updateTime = getDateTime();
        setLanguage.setListener(this);
        setLanguage.isSet();
    }

    private String getDateTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTime.setTimeZone(TimeZone.getTimeZone("America/New_York")); //美東時區
        return dateTime.format(date);
    }

    private void homePage(){
        Intent intent = new Intent(this, MainActivity.class);   //MainActivity
        startActivity(intent);
        finish();
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                homePage();
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

    @SuppressLint("SetTextI18n")
    @Override
    public void setLanguage() {    //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
        if(Value.language_flag == 0){
            title.setText("Check Notice");
            back.setText("back");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }else if(Value.language_flag == 1){
            title.setText("對帳通知");
            back.setText("返回");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }else if(Value.language_flag == 2){
            title.setText("对帐通知");
            back.setText("返回");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }
    }
}

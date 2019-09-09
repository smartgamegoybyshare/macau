package com.threesing.macau.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.threesing.macau.Language.LanguageListener;
import com.threesing.macau.Language.SetLanguage;
import com.threesing.macau.R;
import com.threesing.macau.Support.InternetImage;
import com.threesing.macau.Support.Value;
import com.threesing.macau.ViewPager.SetPagerAdapter;
import com.threesing.macau.ViewPager.SetViewPager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import pl.droidsonroids.gif.GifImageView;

public class AnnounceActivity extends AppCompatActivity implements LanguageListener {

    private String TAG = "AnnounceActivity";
    private SetLanguage setLanguage = new SetLanguage();
    private String company, account, message;
    private TextView title, back, copyright, nowTime;
    private TabLayout tabLayout;
    private ViewPager viewPager;
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
        get_Intent();
    }

    private void get_Intent() {
        Intent intent = getIntent();
        company = intent.getStringExtra("company");
        account = intent.getStringExtra("account");
        message = intent.getStringExtra("message");
        Log.e(TAG, "company = " + company);
        Log.e(TAG, "account = " + account);

        showView();
    }

    private void showView() {
        setContentView(R.layout.announcepage);

        title = findViewById(R.id.textView);   //標題bar
        back = findViewById(R.id.textView1);   //返回
        tabLayout = findViewById(R.id.tablayout); //橫向標題欄
        viewPager = findViewById(R.id.viewpager); //每頁的page
        copyright = findViewById(R.id.copyright);
        nowTime = findViewById(R.id.nowTime);
        gifImageView1 = findViewById(R.id.imageView1); //廣告欄

        setLanguage.setListener(this);
        setLanguage.isSet();

        Runnable getimage = () -> {
            String imageUri = "https://dl.kz168168.com/img/android-ad02.png";
            preview_bitmap = internetImage.fetchImage(imageUri);
            handler.post(() -> {
                gifImageView1.setImageBitmap(preview_bitmap);
                gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        };
        new Thread(getimage).start();
        gifImageView1.setOnClickListener(view -> {
            Uri uri = Uri.parse("http://181282.com/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        back.setOnClickListener(view -> loginPage());
    }

    private void analyMessage() {
        try {
            List<String> tabList = new ArrayList<>();
            List<JSONObject> serviceList = new ArrayList<>();
            List<JSONObject> statusList = new ArrayList<>();
            tabList.clear();
            serviceList.clear();
            statusList.clear();
            if (Value.language_flag == 0) {
                tabList.add("Service notification");
                tabList.add("System notification");
            } else if (Value.language_flag == 1) {
                tabList.add("服務通知");
                tabList.add("系統通知");
            } else if (Value.language_flag == 2) {
                tabList.add("服务通知");
                tabList.add("系统通知");
            }
            Log.e(TAG, "message = " + message);
            JSONArray jsonArray = new JSONArray(message);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                Log.e(TAG, "jsonObject = " + jsonObject);
                if (jsonObject.get("post_name_en").toString().contains("[Service Suspension Notification]")) {
                    serviceList.add(jsonObject);
                } else if (jsonObject.get("post_name_en").toString().contains("[System Status Notification]")) {
                    statusList.add(jsonObject);
                }
            }
            setTabLayout(tabList, serviceList, statusList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTabLayout(List<String> tabList, List<JSONObject> serviceList, List<JSONObject> statusList) {
        SetViewPager setViewPager = new SetViewPager();
        SetPagerAdapter setPagerAdapter = new SetPagerAdapter();
        List<View> viewList = new ArrayList<>();
        List<List<JSONObject>> jsonList = new ArrayList<>();
        viewList.clear();
        jsonList.clear();
        jsonList.add(serviceList);
        jsonList.add(statusList);
        for (int i = 0; i < tabList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
            Objects.requireNonNull(tabLayout.getTabAt(i)).setText(tabList.get(i));
            viewList.add(setViewPager.setView(this, jsonList.get(i)));
        }

        setPagerAdapter.setView(viewList);
        viewPager.setAdapter(setPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    private void loginPage() {
        Intent intent = new Intent(this, LoginMainActivity.class);
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
                loginPage();
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
    public void setLanguage() {
        if (Value.language_flag == 0) {
            title.setText("Newest messages");
            back.setText("back");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 1) {
            title.setText("最新訊息");
            back.setText("返回");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 2) {
            title.setText("最新信息");
            back.setText("返回");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }
        analyMessage();
    }
}

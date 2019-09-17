package com.threesing.macau.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.threesing.macau.Language.LanguageListener;
import com.threesing.macau.Language.SetLanguage;
import com.threesing.macau.PageView.PageAdapter;
import com.threesing.macau.PageView.PageFourView;
import com.threesing.macau.PageView.PageOneView;
import com.threesing.macau.PageView.PageThreeView;
import com.threesing.macau.PageView.PageTwoView;
import com.threesing.macau.PageView.PageView;
import com.threesing.macau.PageView.ViewPagerIndicator;
import com.threesing.macau.Post_Get.Message.GetPost;
import com.threesing.macau.Post_Get.Message.Post;
import com.threesing.macau.Post_Get.Message.PostListener;
import com.threesing.macau.Post_Get.UserData.ConnectUserDataBase;
import com.threesing.macau.Post_Get.UserData.GetUserData;
import com.threesing.macau.Post_Get.UserData.UserdataListener;
import com.threesing.macau.R;
import com.threesing.macau.SQL.LastLoginSQL;
import com.threesing.macau.Support.InternetImage;
import com.threesing.macau.Support.MarqueeTextView;
import com.threesing.macau.Support.Value;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class LoginMainActivity extends AppCompatActivity implements LanguageListener,
        PostListener, UserdataListener {

    private String TAG = "LoginMainActivity";
    private ViewPager viewPager;
    private List<PageView> pageList;
    private ViewPagerIndicator viewPagerIndicator;
    private MarqueeTextView announcement;
    private String company, account, message;
    private TextView accounttxt, lasttime, copyright, nowTime, logout, textView1, textView2,
            textView3, textView4, textView5, textView6, textView7, textView8;
    private ImageView imageViewtitle;
    private Bitmap bitmap_title;
    private Handler viewpageHandler = new Handler();
    private SetLanguage setLanguage = new SetLanguage();
    private Post post = new Post(this);
    private GetPost getPost = new GetPost();
    private InternetImage internetImage = new InternetImage();
    private Handler titleHandler = new Handler();
    private ConnectUserDataBase connectUserDataBase = new ConnectUserDataBase(this);
    private GetUserData getUserData = new GetUserData();
    private LastLoginSQL lastLoginSQL = new LastLoginSQL(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //隱藏標題欄
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        /**
         //隱藏狀態欄
         getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
         WindowManager.LayoutParams.FLAG_FULLSCREEN);
         //隱藏底部HOME工具列
         getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
         **/
        get_Intent();
    }

    private void get_Intent() {
        Intent intent = getIntent();
        company = intent.getStringExtra("company");
        account = intent.getStringExtra("account");
        Log.e(TAG, "company = " + company);
        Log.e(TAG, "account = " + account);
        showview();
    }

    private void showview() {
        setContentView(R.layout.homepage2);

        LinearLayout announcelinear = findViewById(R.id.announcelinear);    //最新訊息按鈕
        LinearLayout travellinear = findViewById(R.id.travellinear);    //旅遊資訊按鈕
        LinearLayout watchlinear = findViewById(R.id.watchlinear);  //線上影音按鈕
        LinearLayout newslinear = findViewById(R.id.newslinear);    //即時新聞按鈕
        imageViewtitle = findViewById(R.id.imageView1);
        announcement = findViewById(R.id.marqueeTextView);   //公告字串
        accounttxt = findViewById(R.id.accounttxt); //登入帳號
        lasttime = findViewById(R.id.lasttime); //上次登入
        viewPager = findViewById(R.id.pager);   //slider廣告介面
        viewPagerIndicator = findViewById(R.id.indicator);  //slider下的點點
        copyright = findViewById(R.id.copyright);
        nowTime = findViewById(R.id.nowTime);
        logout = findViewById(R.id.logout);
        textView1 = findViewById(R.id.textView1);   //戶口資料
        textView2 = findViewById(R.id.textView2);   //綁定戶口
        textView3 = findViewById(R.id.textView3);   //修改密碼
        textView4 = findViewById(R.id.textView4);   //客戶看帳
        textView5 = findViewById(R.id.textView5);   //最新訊息
        textView6 = findViewById(R.id.textView6);   //旅遊資訊
        textView7 = findViewById(R.id.textView7);   //線上影音
        textView8 = findViewById(R.id.textView8);   //即時新聞
        ImageView logoutimg = findViewById(R.id.logouticon);
        ImageView accounticon = findViewById(R.id.imageButton1);
        ImageView linkicon = findViewById(R.id.imageButton2);
        ImageView modifyicon = findViewById(R.id.imageButton3);
        ImageView formicon = findViewById(R.id.imageButton4);
        Value.updateTime = getDateTime();
        getUserData.setListener(this);
        getPost.setListener(this);
        setLanguage.setListener(this);
        setLanguage.isSet();
        connectUserDataBase.setConnect(company, account, getUserData);

        logoutimg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        accounticon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        linkicon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        modifyicon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        formicon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        Runnable gettitle = () -> {
            String imageUri = "https://dl.kz168168.com/img/omen-logo01.png";
            bitmap_title = internetImage.fetchImage(imageUri);
            titleHandler.post(() -> {
                imageViewtitle.setImageBitmap(bitmap_title);
                //imageViewtitle.setImageDrawable(this.getResources().getDrawable((R.drawable.logo01)));
                imageViewtitle.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        };
        new Thread(gettitle).start();

        listView();

        announcement.setOnClickListener(view -> showhowto());
        logout.setOnClickListener(view -> homePage());
        logoutimg.setOnClickListener(view -> homePage());
        accounticon.setOnClickListener(view -> memberData());
        linkicon.setOnClickListener(view -> linkset());
        modifyicon.setOnClickListener(view -> modifypassword());
        formicon.setOnClickListener(view -> nextPage());
        announcelinear.setOnClickListener(view -> { //最新訊息
            if(!account.matches("demo")) {
                shownews();
            }
        });
        travellinear.setOnClickListener(view -> {   //旅遊資訊
            String url = "https://washpower.ga/";
            goWebview(textView6, url);
        });
        watchlinear.setOnClickListener(view -> {    //線上影音
            String url = "https://freetoshare.ga";
            goWebview(textView7, url);
        });
        newslinear.setOnClickListener(view -> { //即時新聞
            String url = "https://ineedwater.ga/";
            goWebview(textView8, url);
        });

        Log.e(TAG, "getDateTime() = " + getDateTime());
        Log.e(TAG, "recordTime() = " + recordTime());
    }

    private void listView() {
        pageList = new ArrayList<>();
        pageList.clear();
        pageList.add(new PageOneView(this));
        pageList.add(new PageTwoView(this));
        pageList.add(new PageThreeView(this));
        pageList.add(new PageFourView(this));
        PageAdapter pageAdapter = new PageAdapter(initItemList());
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(1);
        autoViewpager();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        viewpageHandler.removeCallbacksAndMessages(null);
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (viewPager.getCurrentItem() == 0) {
                            viewPager.removeAllViews();
                            viewPager.addView(pageList.get(3));
                            viewPager.addView(pageList.get(4));
                            viewPager.addView(pageList.get(5));
                            viewPager.setCurrentItem(4, false);
                        } else if (viewPager.getCurrentItem() == 5) {
                            viewPager.removeAllViews();
                            viewPager.addView(pageList.get(0));
                            viewPager.addView(pageList.get(1));
                            viewPager.addView(pageList.get(2));
                            viewPager.setCurrentItem(1, false);
                        }
                        autoViewpager();
                        viewPagerIndicator.setSelected(viewPager.getCurrentItem() - 1);
                        break;
                }
            }
        });
    }

    private List<PageView> initItemList() {
        List<PageView> newPageView = new ArrayList<>();
        newPageView.clear();
        newPageView.addAll(pageList);
        viewPagerIndicator.setLength(pageList.size());
        if (newPageView.size() > 1) {
            //第0个位最后一个，向左拉动时，可以实现直接滑动到最后一个，最后一个是第0个，可以实现向右滑动的时直接跳到第0个
            newPageView.add(0, pageList.get(pageList.size() - 1));
            newPageView.add(pageList.get(0));
        }

        pageList = newPageView;

        return newPageView;
    }

    public void autoViewpager() {
        viewpageHandler.postDelayed(() -> {
            int i = viewPager.getCurrentItem();
            i++;
            viewPager.setCurrentItem(i);
            viewpageHandler.removeCallbacksAndMessages(null);
        }, 4000);
    }

    private String getDateTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTime.setTimeZone(TimeZone.getTimeZone("America/New_York")); //美東時區
        return dateTime.format(date);
    }

    private String recordTime(){
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy.MM.dd");
        //dateTime.setTimeZone(TimeZone.getTimeZone("America/New_York")); //美東時區
        return dateTime.format(date);
    }

    private String getrecordTime(){
        String time = "";
        if(lastLoginSQL.getCount() == 0){
            Log.e(TAG, "time first");
            time = recordTime();
            lastLoginSQL.insert(time);
        }else {
            Log.e(TAG, "time had");
            List<String> dataList = new ArrayList<>();
            dataList.clear();
            dataList = lastLoginSQL.getlist();
            if (dataList.size() != 0) {
                time = dataList.get(0);
                lastLoginSQL.deleteAll();
                lastLoginSQL.insert(time);
            }
        }
        return time;
    }

    private SpannableStringBuilder setBuilder(String str){

        SpannableStringBuilder builder = new SpannableStringBuilder(str + account);
        ForegroundColorSpan buleSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_newsannounce));
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.RED);

        builder.setSpan(buleSpan, 0, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(blackSpan, str.length(), str.length() + account.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }

    private void homePage() {
        Intent intent = new Intent(this, MainActivity.class);   //MainActivity
        startActivity(intent);
        finish();
    }

    private void showhowto() {
        Intent intent = new Intent(this, Howto2Activity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }

    private void nextPage() {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }

    private void memberData() {
        Intent intent = new Intent(this, MemberDataActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        intent.putExtra("act", "login");
        startActivity(intent);
        finish();
    }

    private void modifypassword() {
        Intent intent = new Intent(this, ModifyPasswordActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        intent.putExtra("act", "login");
        startActivity(intent);
        finish();
    }

    private void shownews() {
        Intent intent = new Intent(this, AnnounceActivity.class);  //LoginMainActivity
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        intent.putExtra("message", message);
        startActivity(intent);
        finish();
    }

    private void linkset() {
        Intent intent = new Intent(this, LinksettingActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        intent.putExtra("act", "login");
        startActivity(intent);
        finish();
    }

    private void goWebview(TextView textView, String url){
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra("title", textView.getText());
        intent.putExtra("url", url);
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
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    new AlertDialog.Builder(LoginMainActivity.this)
                            .setTitle("三昇澳門")
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("Do you want to Logout?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                Intent intent = new Intent(this, MainActivity.class);   //MainActivity
                                startActivity(intent);
                                finish();
                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (Value.language_flag == 1) {
                    new AlertDialog.Builder(LoginMainActivity.this)
                            .setTitle("三昇澳門")
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("確定要登出?")
                            .setPositiveButton("確定", (dialog, which) -> {
                                Intent intent = new Intent(this, MainActivity.class);   //MainActivity
                                startActivity(intent);
                                finish();
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (Value.language_flag == 2) {
                    new AlertDialog.Builder(LoginMainActivity.this)
                            .setTitle("三昇澳门")
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("确定要登出?")
                            .setPositiveButton("确定", (dialog, which) -> {
                                Intent intent = new Intent(this, MainActivity.class);   //MainActivity
                                startActivity(intent);
                                finish();
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                }
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
        viewpageHandler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setLanguage() {
        Log.e(TAG, "setLanguage()");
        if (Value.language_flag == 0) {
            accounttxt.setText(setBuilder("Account："));
            lasttime.setText("Last login：" + getrecordTime());
            logout.setText("logout");
            textView1.setText("Account");
            textView2.setText("Add Account");
            textView3.setText("Change Password");
            textView4.setText("Account Checking");
            textView5.setText("Newest messages");
            textView6.setText("Travel information");
            textView7.setText("Online Videos");
            textView8.setText("Instant news");
            textView1.setTextSize(8);
            textView2.setTextSize(8);
            textView3.setTextSize(8);
            textView4.setTextSize(8);
            textView5.setTextSize(12);
            textView6.setTextSize(12);
            textView7.setTextSize(12);
            textView8.setTextSize(12);
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 1) {
            accounttxt.setText(setBuilder("登入帳號："));
            lasttime.setText("上次登入：" + getrecordTime());
            logout.setText("登出");
            textView1.setText("戶口帳號");
            textView2.setText("綁定戶口");
            textView3.setText("修改密碼");
            textView4.setText("客戶看帳");
            textView5.setText("最新訊息");
            textView6.setText("旅遊資訊");
            textView7.setText("線上影音");
            textView8.setText("即時新聞");
            textView1.setTextSize(14);
            textView2.setTextSize(14);
            textView3.setTextSize(14);
            textView4.setTextSize(14);
            textView5.setTextSize(16);
            textView6.setTextSize(16);
            textView7.setTextSize(16);
            textView8.setTextSize(16);
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 2) {
            accounttxt.setText(setBuilder("登入帐号："));
            lasttime.setText("上次登入：" + getrecordTime());
            logout.setText("登出");
            textView1.setText("户口帐号");
            textView2.setText("绑定户口");
            textView3.setText("修改密码");
            textView4.setText("客户看帐");
            textView5.setText("最新信息");
            textView6.setText("旅游资讯");
            textView7.setText("线上影音");
            textView8.setText("即时新闻");
            textView1.setTextSize(14);
            textView2.setTextSize(14);
            textView3.setTextSize(14);
            textView4.setTextSize(14);
            textView5.setTextSize(16);
            textView6.setTextSize(16);
            textView7.setTextSize(16);
            textView8.setTextSize(16);
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }

        post.setConnect(company, account, getPost);
    }

    @Override
    public void getmessage(JSONObject responseJson) {
        try {
            Log.e(TAG, "getmessage = " + responseJson);
            JSONArray jsonArray = new JSONArray(responseJson.get("records").toString());
            message = jsonArray.toString();
            Log.e(TAG, "jsonArray = " + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                Log.e(TAG, "jsonObject = " + jsonObject);
                if (jsonObject.get("post_category").toString().matches("marquee")) {
                    String announce_text = "";
                    if(Value.language_flag == 0){
                        announce_text = jsonObject.get("post_name_en").toString();
                    }else if(Value.language_flag == 1){
                        announce_text = jsonObject.get("post_name_tw").toString();
                    }else if(Value.language_flag == 2){
                        announce_text = jsonObject.get("post_name_cn").toString();
                    }
                    Log.e(TAG, "announce_text.lenth = " + announce_text.length());
                    if (announce_text.length() < 80) {
                        StringBuilder announce_textBuilder = new StringBuilder(announce_text);
                        for (int j = announce_textBuilder.length(); j < 80; j++) {
                            announce_textBuilder.append("  ");
                        }
                        announce_text = announce_textBuilder.toString();
                    }
                    announcement.setText(announce_text);
                } else {
                    Log.e(TAG, "not marquee");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUserData(JSONObject responseJson) {
        try {
            Log.e(TAG, "getUserData");
            String result = responseJson.get("result").toString();
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                Value.get_user_data = responseJson;
            } else if (result.matches("error2")) {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Sub Account Does Not Exist", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "子帳號戶口不存在", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "子帐号户口不存在", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (result.matches("error3")) {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Sub Account Does Not Exist", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "子帳號不存在", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "子帐号不存在", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

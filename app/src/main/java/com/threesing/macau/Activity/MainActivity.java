package com.threesing.macau.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.threesing.macau.Language.LanguageChose;
import com.threesing.macau.Language.LanguageListener;
import com.threesing.macau.Language.SetLanguage;
import com.threesing.macau.PageView.PageAdapter;
import com.threesing.macau.PageView.PageFourView;
import com.threesing.macau.PageView.PageOneView;
import com.threesing.macau.PageView.PageThreeView;
import com.threesing.macau.PageView.PageTwoView;
import com.threesing.macau.PageView.PageView;
import com.threesing.macau.PageView.ViewPagerIndicator;
import com.threesing.macau.Post_Get.Login.ConnectListener;
import com.threesing.macau.Post_Get.Login.Connected;
import com.threesing.macau.Post_Get.Login.GetConnect;
import com.threesing.macau.R;
import com.threesing.macau.SQL.LanguageSQL;
import com.threesing.macau.SQL.LoginSQL;
import com.threesing.macau.Support.InternetImage;
import com.threesing.macau.Support.Loading;
import com.threesing.macau.Support.LoginDialog;
import com.threesing.macau.Support.MarqueeTextView;
import com.threesing.macau.Support.Value;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements ConnectListener, LanguageListener {

    private String TAG = "MainActivity";
    private ViewPager viewPager;
    private List<PageView> pageList;
    private ViewPagerIndicator viewPagerIndicator;
    private MarqueeTextView announcement;
    private String company, account, imformation;
    private TextView copyright, nowTime, login, textView1, textView2, textView3, textView4,
            textView5, textView6, textView7, textView8;
    private ImageView imageViewtitle;
    private Bitmap bitmap_title;
    private Handler viewpageHandler = new Handler();
    private SetLanguage setLanguage = new SetLanguage();
    private Connected connected = new Connected(this);
    private GetConnect getConnect = new GetConnect();
    private LoginSQL loginSQL = new LoginSQL(this);
    private LanguageSQL languageSQL = new LanguageSQL(this);
    private LanguageChose languageChose = new LanguageChose(this);
    private InternetImage internetImage = new InternetImage();
    private Handler titleHandler = new Handler(), announceHandler = new Handler();
    private LoginDialog loginDialog = new LoginDialog(this);
    private Loading loading = new Loading(this);

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
        getConnect.setListener(this);
        setLanguage.setListener(this);
        if (languageSQL.getCount() == 0) {
            languageChose.show(setLanguage, languageSQL);
        } else {
            Value.language_flag = languageSQL.getflag();
        }
        try {
            Value.updateTime = getDateTime();
            String thisversion = getVersionName(this);
            Log.e(TAG, "thisversion = " + thisversion);
            Value.ver = thisversion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        showview();
    }

    private void showview() {
        setContentView(R.layout.homepage);

        LinearLayout loginlinear = findViewById(R.id.loginlinear);  //登入鈕背景
        LinearLayout loginimage = findViewById(R.id.loginimage);    //登入鈕
        LinearLayout announcelinear = findViewById(R.id.announcelinear);    //最新訊息按鈕
        LinearLayout travellinear = findViewById(R.id.travellinear);    //旅遊資訊按鈕
        LinearLayout watchlinear = findViewById(R.id.watchlinear);  //線上影音按鈕
        LinearLayout newslinear = findViewById(R.id.newslinear);    //即時新聞按鈕
        imageViewtitle = findViewById(R.id.imageView1); //LOGO
        announcement = findViewById(R.id.marqueeTextView);   //公告字串
        login = findViewById(R.id.login);
        textView1 = findViewById(R.id.textView1);   //戶口資料
        textView2 = findViewById(R.id.textView2);   //綁定戶口
        textView3 = findViewById(R.id.textView3);   //修改密碼
        textView4 = findViewById(R.id.textView4);   //客戶看帳
        textView5 = findViewById(R.id.textView5);   //最新訊息
        textView6 = findViewById(R.id.textView6);   //旅遊資訊
        textView7 = findViewById(R.id.textView7);   //線上影音
        textView8 = findViewById(R.id.textView8);   //即時新聞
        viewPager = findViewById(R.id.pager);   //slider廣告介面
        viewPagerIndicator = findViewById(R.id.indicator);  //slider下的點點
        copyright = findViewById(R.id.copyright);
        nowTime = findViewById(R.id.nowTime);

        setLanguage.setListener(this);
        setLanguage.isSet();

        Runnable gettitle = () -> {
            String imageUri = "https://dl.kz168168.com/img/omen-logo01.png";
            bitmap_title = internetImage.fetchImage(imageUri);
            titleHandler.post(() -> {
                imageViewtitle.setImageBitmap(bitmap_title);
                imageViewtitle.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        };
        new Thread(gettitle).start();

        announcement.setOnClickListener(view -> showhowto());
        loginlinear.setOnClickListener(view -> loginDialog.show(loading, loginSQL, connected, getConnect));
        loginimage.setOnClickListener(view -> loginDialog.show(loading, loginSQL, connected, getConnect));
        announcelinear.setOnClickListener(view -> { //最新訊息

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

    public String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionName;
    }

    private String getDateTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTime.setTimeZone(TimeZone.getTimeZone("America/New_York")); //美東時區
        return dateTime.format(date);
    }

    private Runnable announce = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("https://dl.kz168168.com/apk/nuba_default_marquee.json");
                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                urlCon.setConnectTimeout(2000);
                InputStream uin = urlCon.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(uin));
                boolean more = true;
                StringBuilder line = new StringBuilder();
                for (; more; ) {
                    String getline = in.readLine();
                    Log.e(TAG, "getline = " + getline);
                    if (getline != null) {
                        line.append(getline);
                    } else {
                        more = false;
                    }
                }
                Log.e(TAG, "line = " + line);
                JSONObject jsonObject = new JSONObject(line.toString());
                Log.e(TAG, "jsonObject = " + jsonObject);
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    imformation = jsonObject.getString("text_en");
                } else if (Value.language_flag == 1) {
                    imformation = jsonObject.getString("text_tw");
                } else if (Value.language_flag == 2) {
                    imformation = jsonObject.getString("text_cn");
                }
                if (imformation.length() < 80) {
                    StringBuilder imformationBuilder = new StringBuilder(imformation);
                    for (int j = imformationBuilder.length(); j < 80; j++) {
                        imformationBuilder.append("  ");
                    }
                    imformation = imformationBuilder.toString();
                }
                announceHandler.post(() -> announcement.setText(imformation));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void showhowto() {
        Intent intent = new Intent(this, HowtoActivity.class);
        startActivity(intent);
        finish();
    }

    private void nextPage() {
        Intent intent = new Intent(this, LoginMainActivity.class);  //LoginMainActivity
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }

    private void goWebview(TextView textView, String url){
        company = "";
        account = "";
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
                    new AlertDialog.Builder(this)
                            .setTitle("三昇澳門")
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("Do you want to exit?")
                            .setPositiveButton("Yes", (dialog, which) -> finish())
                            .setNegativeButton("No", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (Value.language_flag == 1) {
                    new AlertDialog.Builder(this)
                            .setTitle("三昇澳門")
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("確定要離開?")
                            .setPositiveButton("離開", (dialog, which) -> finish())
                            .setNegativeButton("取消", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (Value.language_flag == 2) {
                    new AlertDialog.Builder(this)
                            .setTitle("三昇澳门")
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("确定要离开?")
                            .setPositiveButton("离开", (dialog, which) -> finish())
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
        loginSQL.close();
        viewpageHandler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setLanguage() {
        Log.e(TAG, "setLanguage()");
        if (Value.language_flag == 0) {
            login.setText("Login");
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
            login.setText("登入帳號");
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
            login.setText("登入帐号");
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
        listView();
        new Thread(announce).start();
    }

    @Override
    public void isConnected(JSONObject responseJson, String company, String account, String password,
                            CheckBox checkBox) {
        try {
            String result = responseJson.get("result").toString();
            this.company = company;
            this.account = account;
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                loading.dismiss();
                Value.check_user = responseJson;
                if (checkBox.isChecked()) {
                    if(!account.matches("demo")) {
                        if (loginSQL.getCount() != 0) {
                            loginSQL.deleteAll();
                            loginSQL.insert(company, account, password);
                        } else {
                            loginSQL.insert(company, account, password);
                        }
                    }
                } else {
                    loginSQL.deleteAll();
                }
                Value.login_in = true;
                nextPage();
            } else if (result.matches("error1")) {
                loading.dismiss();
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Password Incorrect", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "密碼不正確", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "密码不正确", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (result.matches("error3")) {
                loading.dismiss();
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
            } else if (result.matches("error4")) {
                loading.dismiss();
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Reconciliation Invalid", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "尚未開放對帳", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "尚未开放对帐", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

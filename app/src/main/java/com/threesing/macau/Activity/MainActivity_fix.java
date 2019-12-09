package com.threesing.macau.Activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.threesing.macau.Language.LanguageChose;
import com.threesing.macau.Language.LanguageListener;
import com.threesing.macau.Language.SetLanguage;
import com.threesing.macau.Post_Get.Login.ConnectListener;
import com.threesing.macau.Post_Get.Login.Connected;
import com.threesing.macau.Post_Get.Login.GetConnect;
import com.threesing.macau.R;
import com.threesing.macau.SQL.LanguageSQL;
import com.threesing.macau.SQL.LoginSQL;
import com.threesing.macau.Support.InternetImage;
import com.threesing.macau.Support.Loading;
import com.threesing.macau.Support.ParaseUrl;
import com.threesing.macau.Support.Value;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity_fix extends AppCompatActivity implements ConnectListener, LanguageListener {

    private String TAG = "MainActivity_fix";
    private String company, account, password;
    private EditText editText1, editText2, editText3;
    private CheckBox checkBox;
    private Button login;
    private TextView copyright, nowTime;
    private ImageView imageViewtitle;
    private Bitmap bitmap_title;
    private Handler checkHandler = new Handler(), titleHandler = new Handler();
    private SetLanguage setLanguage = new SetLanguage();
    private Connected connected = new Connected(this);
    private GetConnect getConnect = new GetConnect();
    private LoginSQL loginSQL = new LoginSQL(this);
    private LanguageSQL languageSQL = new LanguageSQL(this);
    private LanguageChose languageChose = new LanguageChose(this);
    private InternetImage internetImage = new InternetImage();
    private Loading loading = new Loading(this);
    private boolean error = false;

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
        setContentView(R.layout.homepage_fix);

        imageViewtitle = findViewById(R.id.imageView1); //LOGO
        editText1 = findViewById(R.id.editText1);   //公司
        editText2 = findViewById(R.id.editText2);  //戶口
        editText3 = findViewById(R.id.editText3);  //密碼
        checkBox = findViewById(R.id.checkBox);    //記住我的登入資訊
        login = findViewById(R.id.button1);   //登入按鈕
        copyright = findViewById(R.id.copyright);
        nowTime = findViewById(R.id.nowTime);

        getConnect.setListener(this);
        //new Thread(versionCheck).start();
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

        List<String> dataList = new ArrayList<>();
        dataList.clear();
        dataList = loginSQL.getlist();
        if (dataList.size() != 0) {
            editText1.setText(dataList.get(0));
            editText2.setText(dataList.get(1));
            //editText3.setText(dataList.get(2));
            checkBox.setChecked(true);
        }

        login.setOnClickListener(view -> {
            company = editText1.getText().toString().trim();
            account = editText2.getText().toString().trim();
            password = editText3.getText().toString().trim();

            if (company.matches("")) {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Sub Account is empty", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "子帳號不可為空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "子帐号不可为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (account.matches("")) {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "User is empty", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "戶口不可為空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "户口不可为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (password.matches("")) {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "密碼不可為空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "密码不可为空", Toast.LENGTH_SHORT);
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
                connected.setConnect(company, account, password, getConnect, checkBox);
            }
        });
    }

    private void nextPage() {
        Intent intent = new Intent(MainActivity_fix.this, FormActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }

    private Runnable versionCheck = () -> {
        ParaseUrl paraseUrl = new ParaseUrl();
        String version = paraseUrl.getDoc();
        String thisversion = Value.ver;
        Log.e(TAG, "version = " + version);
        Log.e(TAG, "thisversion = " + thisversion);
        if (!version.matches(thisversion)) {
            checkHandler.post(() -> {
                if (Value.language_flag == 0) {
                    new AlertDialog.Builder(MainActivity_fix.this)
                            .setTitle("三昇澳門" + thisversion)
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("Check out the new version" + version + "\nUpdate now?")
                            .setPositiveButton("Yes", (dialog, which) -> gotoMarket())
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (Value.language_flag == 1) {
                    new AlertDialog.Builder(MainActivity_fix.this)
                            .setTitle("三昇澳門" + thisversion)
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("偵測到有新版本" + version + "\n現在要更新嗎?")
                            .setPositiveButton("確定", (dialog, which) -> gotoMarket())
                            .setNegativeButton("取消", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (Value.language_flag == 2) {
                    new AlertDialog.Builder(MainActivity_fix.this)
                            .setTitle("三昇澳门" + thisversion)
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("侦测到有新版本" + version + "\n现在要更新吗?")
                            .setPositiveButton("确定", (dialog, which) -> gotoMarket())
                            .setNegativeButton("取消", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                }
            });
        }
    };

    private void gotoMarket() {
        Uri marketUri = Uri.parse("market://details?id=com.threesing.macau");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
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
        new Thread(versionCheck).start();
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
        languageSQL.close();
        titleHandler.removeCallbacksAndMessages(true);
        checkHandler.removeCallbacksAndMessages(true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setLanguage() {
        Log.e(TAG, "setLanguage()");
        if (Value.language_flag == 0) {
            editText1.setHint("Sub Account");
            editText2.setHint("User");
            editText3.setHint("Password");
            checkBox.setText("  Remember Me");
            login.setText("Login");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 1) {
            editText1.setHint("分公司/子帳號");
            editText2.setHint("戶口");
            editText3.setHint("密碼");
            checkBox.setText("  記住我的登入資訊");
            login.setText("登入");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 2) {
            editText1.setHint("分公司/子帐号");
            editText2.setHint("户口");
            editText3.setHint("密码");
            checkBox.setText("  记住我的登陆资讯");
            login.setText("登陆");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }
    }

    @Override
    public void isConnected(JSONObject responseJson, String company, String account, String password, CheckBox checkBox) {
        try {
            String result = responseJson.get("result").toString();
            this.company = company;
            this.account = account;
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                loading.dismiss();
                Value.check_user = responseJson;
                if (checkBox.isChecked()) {
                    if (!account.matches("demo")) {
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
                if(!error) {
                    error = true;
                    int flag = Value.api_flag;
                    Value.err_password = flag;
                    Value.api_flag = flag + 1;
                    Log.e(TAG, "Value.err_password = " + Value.err_password);
                    Log.e(TAG, "Value.api_flag = " + Value.api_flag);
                    connected.setConnect(company, account, password, getConnect, checkBox);
                }else if(Value.api_flag == Value.err_password){
                    loading.dismiss();
                    error = false;
                    Value.api_flag = 0;
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
                }else {
                    Value.api_flag = Value.api_flag + 1;
                    connected.setConnect(company, account, password, getConnect, checkBox);
                }
            } else if (result.matches("error3")) {
                if(Value.api_flag == Value.api_count - 1) {
                    loading.dismiss();
                    Value.api_flag = 0;
                    if(!error) {
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
                    }else {
                        connected.setConnect(company, account, password, getConnect, checkBox);
                    }
                }
                else {
                    Value.api_flag = Value.api_flag + 1;
                    connected.setConnect(company, account, password, getConnect, checkBox);
                }
            } else if (result.matches("error4")) {
                loading.dismiss();
                Value.api_flag = 0;
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

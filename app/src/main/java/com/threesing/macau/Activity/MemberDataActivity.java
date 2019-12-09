package com.threesing.macau.Activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.threesing.macau.R;
import com.threesing.macau.Support.Constant;
import com.threesing.macau.Support.InternetImage;
import com.threesing.macau.Support.ParaseUrl;
import com.threesing.macau.Support.Value;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.droidsonroids.gif.GifImageView;

public class MemberDataActivity extends AppCompatActivity {

    private String TAG = "MemberDataActivity";
    private String company, account, act;
    private Bitmap preview_bitmap;
    //private GifImageView gifImageView1;
    private Handler handler = new Handler(), checkHandler = new Handler();
    private InternetImage internetImage = new InternetImage();
    public final static String WEIXIN_CHATTING_MIMETYPE = "vnd.android.cursor.item/vnd.com.tencent.mm.chatting.profile";    //微信聊天

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //隱藏標題欄
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.memberdata);
        get_Intent();
    }

    private void get_Intent() {
        Intent intent = getIntent();
        company = intent.getStringExtra("company");
        account = intent.getStringExtra("account");
        act = intent.getStringExtra("act");
        Log.e(TAG, "company = " + company);
        Log.e(TAG, "account = " + account);
        showview();
    }

    @SuppressLint("SetTextI18n")
    private void showview() {
        try {
            LinearLayout row1 = findViewById(R.id.row1);
            LinearLayout row2 = findViewById(R.id.row2);
            LinearLayout row3 = findViewById(R.id.row3);
            LinearLayout row4 = findViewById(R.id.row4);
            LinearLayout row5 = findViewById(R.id.row5);
            LinearLayout row6 = findViewById(R.id.row6);
            LinearLayout row7 = findViewById(R.id.row7);
            LinearLayout row8 = findViewById(R.id.row8);
            TextView title = findViewById(R.id.textView);   //會員資料
            TextView back = findViewById(R.id.textView1);   //返回
            TextView textView1 = findViewById(R.id.textView3);  //客戶帳號內容
            TextView membername = findViewById(R.id.textView4); //姓名欄
            TextView textView2 = findViewById(R.id.textView5);  //姓名
            TextView guarantor = findViewById(R.id.textView6);  //擔保人
            TextView textView3 = findViewById(R.id.textView7);  //擔保人姓名
            TextView currency = findViewById(R.id.textView8);  //貨幣
            TextView textView4 = findViewById(R.id.textView9);  //貨幣代碼
            TextView method = findViewById(R.id.textView10); //交收方式
            TextView textView5 = findViewById(R.id.textView11); //string
            TextView share = findViewById(R.id.textView12); //占成數(%)
            TextView textView6 = findViewById(R.id.textView13); //value
            TextView skypeAccount = findViewById(R.id.textView14); //skypeAccount
            TextView textView7 = findViewById(R.id.textView15); //account
            TextView weChatAccount = findViewById(R.id.textView16); //weChatAccount
            TextView textView8 = findViewById(R.id.textView17); //account
            TextView remark = findViewById(R.id.textView18); //備註
            TextView textView9 = findViewById(R.id.textView19); //remark
            TextView copyright = findViewById(R.id.copyright);
            TextView nowTime = findViewById(R.id.nowTime);
            //gifImageView1 = findViewById(R.id.imageView1);

            row2.setVisibility(View.GONE);
            row4.setVisibility(View.GONE);
            row5.setVisibility(View.GONE);
            row6.setVisibility(View.GONE);
            row7.setVisibility(View.GONE);
            row8.setVisibility(View.GONE);

            //row1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, R.attr.actionBarSize));
            //row3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, R.attr.actionBarSize));

            /*Runnable getimage = () -> {
                String imageUri = "https://dl.kz168168.com/img/omen-ad06.png";
                preview_bitmap = internetImage.fetchImage(imageUri);
                handler.post(() -> {
                    gifImageView1.setImageBitmap(preview_bitmap);
                    gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                });
            };
            new Thread(getimage).start();*/
            /*GifDrawable gifFromPath = new GifDrawable(this.getResources(), R.drawable.adphoto);
            gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            gifImageView1.setImageDrawable(gifFromPath);*/
            /*gifImageView1.setOnClickListener(view -> {
                //vibrator.vibrate(100);
                Uri uri = Uri.parse("http://3singsport.win/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });*/
            if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                title.setText("Account");
                back.setText("Back");
                membername.setText("Name");
                guarantor.setText("Guarantor");
                currency.setText("Currency");
                method.setText("Settlement");
                share.setText("Percentage(%)");
                skypeAccount.setText("Skype");
                weChatAccount.setText("Wechat");
                remark.setText("Remarks");
                copyright.setText(Value.copyright_text + Value.ver);
                nowTime.setText(Value.updatestring + Value.updateTime);
            }else if(Value.language_flag == 1){
                title.setText("戶口帳號");
                back.setText("返回");
                membername.setText("姓名");
                guarantor.setText("擔保人");
                currency.setText("貨幣");
                method.setText("交收方式");
                share.setText("占成數(%)");
                skypeAccount.setText("Skype");
                weChatAccount.setText("Wechat");
                remark.setText("備註");
                copyright.setText(Value.copyright_text + Value.ver);
                nowTime.setText(Value.updatestring + Value.updateTime);
            }else if(Value.language_flag == 2){
                title.setText("户口帐号");
                back.setText("返回");
                membername.setText("姓名");
                guarantor.setText("担保人");
                currency.setText("货币");
                method.setText("交收方式");
                share.setText("占成数(%)");
                skypeAccount.setText("Skype");
                weChatAccount.setText("Wechat");
                remark.setText("备注");
                copyright.setText(Value.copyright_text + Value.ver);
                nowTime.setText(Value.updatestring + Value.updateTime);
            }

            JSONArray jsonArray = new JSONArray(Value.get_user_data.get("records").toString());
            JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
            textView1.setText(jsonObject.get("username").toString());    //客戶帳號
            textView2.setText(jsonObject.get("full_name").toString());    //姓名
            textView3.setText(jsonObject.get("guarantor").toString()); //擔保人
            textView4.setText(jsonObject.get("currency").toString()); //貨幣
            if(Value.language_flag == 0){
                textView5.setText(jsonObject.get("settlement_method_en").toString()); //交收方式
            }else if(Value.language_flag == 1){
                textView5.setText(jsonObject.get("settlement_method_tw").toString()); //交收方式
            }else if(Value.language_flag == 2){
                textView5.setText(jsonObject.get("settlement_method_cn").toString()); //交收方式
            }
            textView6.setText(jsonObject.get("share").toString()); //占成數(%)
            textView7.setText(jsonObject.get("skype").toString()); //Skype帳號
            textView8.setText(jsonObject.get("wechat").toString()); //Wechat帳號
            textView9.setText(jsonObject.get("remark").toString()); //備註
            textView7.setTextColor(Color.BLUE);
            textView8.setTextColor(Color.BLUE);
            textView7.setOnClickListener(view -> {
                try {
                    toSkype(jsonObject.get("skype").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            textView8.setOnClickListener(view -> {
                try {
                    toWechat(jsonObject.get("wechat").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            back.setOnClickListener(view -> backform());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void toSkype(String skypename){
        String appName = "Skype";
        String packageName = "com.skype.raider";
        openApp(this, appName, packageName, skypename);
    }

    private void openApp(Context context, String appName, String packageName, String skypename) {
        if (isAppInstalled(context, packageName))   //檢查是否已安裝app
            if (isAppEnabled(context, packageName)) { //是否啟用
                Intent skype_intent = new Intent(Intent.ACTION_VIEW);
                skype_intent.setClassName(packageName, "com.skype.raider.Main");
                skype_intent.setData(Uri.parse("skype:" + skypename + "?chat"));
                startActivity(skype_intent);
                //context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
            }
            else Toast.makeText(context, appName + " app is not enabled.", Toast.LENGTH_SHORT).show();
        else goToMarket(context);
    }

    private void openChat(Context context, String appName, String packageName, String chatName) {
        if (isAppInstalled(context, packageName))   //檢查是否已安裝app
            if (isAppEnabled(context, packageName)) { //是否啟用
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                startActivity(intent);

                Constant.flag = 1;
                Constant.wechatId = chatName;
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
//                intent.setDataAndType(Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, chatName),
//                        WEIXIN_CHATTING_MIMETYPE);
//                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setComponent(cmp);
//                startActivity(intent);
            }
            else Toast.makeText(context, appName + " app is not enabled.", Toast.LENGTH_SHORT).show();
        else goToChatMarket(context);
    }

    public void goToChatMarket(Context myContext) { //若沒有安裝wechat則導入play商店
        Uri marketUri = Uri.parse("market://details?id=com.tencent.mm&hl=en-US");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);
    }

    public void goToMarket(Context myContext) { //若沒有安裝skype則導入play商店
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider&hl=en_TW");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);
    }

    private boolean isAppEnabled(Context context, String packageName) {  //是否啟用
        boolean appStatus = false;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 0);
            appStatus = ai.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appStatus;
    }

    private boolean isAppInstalled(Context context, String packageName) {    //已安裝
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }

    private void toWechat(String chatName){
        String appName = "WeChat";
        String packageName = "com.tencent.mm";
        openChat(this, appName, packageName, chatName);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        SimplePhone sp = (SimplePhone) mAdpater.getItem(position);
//        intent.setDataAndType(Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, String.valueOf(sp.dataid)), WX_MIMETYPE);
//        startActivity(intent);
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

    private void gotoMarket() {
        Uri marketUri = Uri.parse("market://details?id=com.threesing.macau");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
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
                    new AlertDialog.Builder(MemberDataActivity.this)
                            .setTitle("三昇澳門" + thisversion)
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("Check out the new version" + version + "\nUpdate now?")
                            .setPositiveButton("Yes", (dialog, which) -> gotoMarket())
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (Value.language_flag == 1) {
                    new AlertDialog.Builder(MemberDataActivity.this)
                            .setTitle("三昇澳門" + thisversion)
                            .setIcon(R.drawable.app_icon_mini)
                            .setMessage("偵測到有新版本" + version + "\n現在要更新嗎?")
                            .setPositiveButton("確定", (dialog, which) -> gotoMarket())
                            .setNegativeButton("取消", (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (Value.language_flag == 2) {
                    new AlertDialog.Builder(MemberDataActivity.this)
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
    }
}

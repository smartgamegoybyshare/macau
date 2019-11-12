package com.threesing.macau.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.threesing.macau.ListView.AccountLinkList;
import com.threesing.macau.ListView.buttonItem.ButtonItemOnclickListener;
import com.threesing.macau.ListView.buttonItem.GetButtonItem;
import com.threesing.macau.Post_Get.GetCheckLink.CheckLink;
import com.threesing.macau.Post_Get.GetCheckLink.CheckLinkListener;
import com.threesing.macau.Post_Get.GetCheckLink.GetCheckLink;
import com.threesing.macau.R;
import com.threesing.macau.Support.InternetImage;
import com.threesing.macau.Support.Loading;
import com.threesing.macau.Support.Value;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;

public class AccountLinkActivity extends AppCompatActivity implements CheckLinkListener,
        ButtonItemOnclickListener {

    private String TAG = "AccountLinkActivity";
    private String company, account;
    private ListView listView;
    private Bitmap preview_bitmap;
    //private GifImageView gifImageView1;
    private Handler handler = new Handler();
    private Loading loading = new Loading(this);
    private CheckLink checkLink = new CheckLink(this);
    private GetCheckLink getCheckLink = new GetCheckLink();
    private GetButtonItem getButtonItem = new GetButtonItem();
    private InternetImage internetImage = new InternetImage();
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //隱藏標題欄
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.accountlink);
        get_Intent();
    }

    private void get_Intent() {
        if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            loading.show("Getting data");
        }else if(Value.language_flag == 1){
            loading.show("取得資料中");
        }else if(Value.language_flag == 2){
            loading.show("获取资料中");
        }

        Intent intent = getIntent();
        company = intent.getStringExtra("company");
        account = intent.getStringExtra("account");
        Log.e(TAG, "company = " + company);
        Log.e(TAG, "account = " + account);
        showview();
    }

    @SuppressLint("SetTextI18n")
    private void showview() {
        TextView title = findViewById(R.id.textView);
        TextView back = findViewById(R.id.textView1);
        TextView text_company = findViewById(R.id.textView2);
        TextView text_account = findViewById(R.id.textView3);
        TextView copyright = findViewById(R.id.copyright);
        TextView nowTime = findViewById(R.id.nowTime);
        listView = findViewById(R.id.listView1);

        /*gifImageView1 = findViewById(R.id.imageView1);
        Runnable getimage = () -> {
            String imageUri = "https://dl.kz168168.com/img/omen-ad04.png";
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
        /*gifImageView1.setOnClickListener(view -> {
            //vibrator.vibrate(100);
            Uri uri = Uri.parse("http://3singsport.win/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });*/

        copyright.setText(Value.copyright_text + Value.ver);
        if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            title.setText("Account");
            back.setText("Back");
            text_company.setText("Sub Account");
            text_account.setText("User");
            nowTime.setText(Value.updatestring + Value.updateTime);
        }else if(Value.language_flag == 1){
            title.setText("轉換戶口");
            back.setText("返回");
            text_company.setText("分公司/子帳號");
            text_account.setText("戶口");
            nowTime.setText(Value.updatestring + Value.updateTime);
        }else if(Value.language_flag == 2){
            title.setText("转换户口");
            back.setText("返回");
            text_company.setText("分公司/子帐号");
            text_account.setText("户口");
            nowTime.setText(Value.updatestring + Value.updateTime);
        }

        back.setOnClickListener(view ->  backform());

        getButtonItem.setButtonItemOnclickListener(this);
        getCheckLink.setListener(this);
        checkLink.setConnect(company, account, getCheckLink);
    }

    private void backform() {
        Intent intent = new Intent(this, FormActivity.class);
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
    public void getlink(JSONObject responseJson) {  // API = http://api-kz.zyue88.com/api/get_check_link
        try {
            Log.e(TAG, "getlink");
            String result = responseJson.get("result").toString();
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                jsonArray = new JSONArray(responseJson.get("records").toString());
                List<String> user_link = new ArrayList<>();
                user_link.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    user_link.add(jsonArray.get(i).toString());
                }
                AccountLinkList accountLinkList = new AccountLinkList(this, user_link,
                        getButtonItem, company, account);
                listView.setAdapter(accountLinkList);
                loading.dismiss();
            } else if (result.matches("error1")) {
                loading.dismiss();
                if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "No Combined Sub Account", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(Value.language_flag == 1){
                    Toast toast = Toast.makeText(this, "無連結的子帳號戶口", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(Value.language_flag == 2){
                    Toast toast = Toast.makeText(this, "无连结的子帐号户口", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void itemOnClick(View v, String nowcompany, String nowaccount) {
        company = nowcompany;
        account = nowaccount;
        backform();
    }
}

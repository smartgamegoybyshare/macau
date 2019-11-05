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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.threesing.macau.ListView.InnerItem.GetInnerItem;
import com.threesing.macau.ListView.InnerItem.InnerItemOnclickListener;
import com.threesing.macau.ListView.LinksettingList;
import com.threesing.macau.Post_Get.CancelLink.CancelLink;
import com.threesing.macau.Post_Get.CancelLink.CancelLinkListener;
import com.threesing.macau.Post_Get.CancelLink.GetCancelLink;
import com.threesing.macau.Post_Get.GetLink.GetLink;
import com.threesing.macau.Post_Get.GetLink.Link;
import com.threesing.macau.Post_Get.GetLink.LinkListener;
import com.threesing.macau.Post_Get.LinkForm.GetLinkForm;
import com.threesing.macau.Post_Get.LinkForm.LinkForm;
import com.threesing.macau.Post_Get.LinkForm.LinkFormListener;
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

public class LinksettingActivity extends AppCompatActivity implements LinkListener, InnerItemOnclickListener,
        CancelLinkListener, LinkFormListener {

    private String TAG = "LinksettingActivity";
    private String company, account, act;
    private Bitmap preview_bitmap;
    //private GifImageView gifImageView1;
    private Handler handler = new Handler();
    private Link link = new Link(this);
    private Loading loading = new Loading(this);
    private GetLink getLink = new GetLink();
    private GetInnerItem getInnerItem = new GetInnerItem();
    private CancelLink cancelLink = new CancelLink(this);
    private GetCancelLink getCancelLink = new GetCancelLink();
    private LinkForm linkForm = new LinkForm(this);
    private GetLinkForm getLinkForm = new GetLinkForm();
    private InternetImage internetImage = new InternetImage();
    private LinksettingList linksettingList;
    private JSONArray jsonArray;
    private EditText editText1, editText2, editText3;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //隱藏標題欄
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.accountlinksetting);

        setView();
    }

    @SuppressLint("SetTextI18n")
    private void setView() {
        Intent intent = getIntent();
        company = intent.getStringExtra("company");
        account = intent.getStringExtra("account");
        act = intent.getStringExtra("act");
        Log.e(TAG, "company = " + company);
        Log.e(TAG, "account = " + account);

        getLink.setListener(this);
        getInnerItem.setInnerItemOnclickListener(this);
        getCancelLink.setListener(this);
        getLinkForm.setListener(this);

        TextView title = findViewById(R.id.textView);   //會員資料
        TextView back = findViewById(R.id.textView1);   //返回
        TextView textView1 = findViewById(R.id.textView2);  //公司
        TextView textView2 = findViewById(R.id.textView3);  //帳號
        TextView textView3 = findViewById(R.id.textView4);  //密碼
        TextView textView4 = findViewById(R.id.textView5);  //公司
        TextView textView5 = findViewById(R.id.textView6);  //帳號
        TextView textView6 = findViewById(R.id.textView7);  //操作
        TextView copyright = findViewById(R.id.copyright);
        TextView nowTime = findViewById(R.id.nowTime);
        editText1 = findViewById(R.id.editText1);  //公司
        editText2 = findViewById(R.id.editText2);  //帳號
        editText3 = findViewById(R.id.editText3);  //密碼
        Button button = findViewById(R.id.button1); //確認鈕
        listView = findViewById(R.id.listView1);
        //ifImageView1 = findViewById(R.id.imageView1);

        /*Runnable getimage = () -> {
            String imageUri = "https://dl.kz168168.com/img/omen-ad07.png";
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
            Uri uri = Uri.parse("http://181282.com/");
            Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent2);
        });*/

        if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            title.setText("Add Account");
            back.setText("back");
            textView1.setText("Sub Account");
            textView2.setText("User");
            textView3.setText("Password");
            textView4.setText("Sub Account");
            textView5.setText("User");
            textView6.setText("Action");
            button.setText("Comfirm");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 1) {
            title.setText("綁定戶口");
            back.setText("返回");
            textView1.setText("分公司/子帳號");
            textView2.setText("戶口");
            textView3.setText("密碼");
            textView4.setText("子帳號");
            textView5.setText("戶口");
            textView6.setText("操作");
            button.setText("確認");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 2) {
            title.setText("绑定户口");
            back.setText("返回");
            textView1.setText("分公司/子帐号");
            textView2.setText("户口");
            textView3.setText("密码");
            textView4.setText("子帐号");
            textView5.setText("户口");
            textView6.setText("操作");
            button.setText("确认");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }

        back.setOnClickListener(view -> backform());

        button.setOnClickListener(view -> {
            String new_company = editText1.getText().toString().trim();
            String new_account = editText2.getText().toString().trim();
            String password = editText3.getText().toString().trim();
            Log.e(TAG, "new_company = " + new_company);
            Log.e(TAG, "new_account = " + new_account);
            Log.e(TAG, "password = " + password);
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
                    loading.show("Getting data");
                } else if (Value.language_flag == 1) {
                    loading.show("取得資料中");
                } else if (Value.language_flag == 2) {
                    loading.show("获取资料中");
                }
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                linkForm.setConnect(company, account, new_company, new_account, password, getLinkForm);
            }
        });

        getLink();
    }

    private void getLink() {
        if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            loading.show("Getting data");
        } else if (Value.language_flag == 1) {
            loading.show("取得資料中");
        } else if (Value.language_flag == 2) {
            loading.show("获取资料中");
        }

        link.setConnect(company, account, getLink);
    }

    private void showview() {
        listView.setAdapter(linksettingList);
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

    private void cancelLink(View v) {
        try {
            int position;
            position = (Integer) v.getTag();
            if (v.getId() == R.id.textView3) {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    loading.show("Getting data");
                } else if (Value.language_flag == 1) {
                    loading.show("取得資料中");
                } else if (Value.language_flag == 2) {
                    loading.show("获取资料中");
                }
                String getItem = jsonArray.get(position).toString();
                JSONObject jsonObject = new JSONObject(getItem);
                Log.e("TAG", "getItem = " + getItem);
                String company_chose = jsonObject.get("link_from_code").toString();
                String account_chose = jsonObject.get("link_from_user").toString();
                String linkid = jsonObject.get("link_id").toString();
                cancelLink.setConnect(company_chose, account_chose, linkid, getCancelLink);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void getlink(JSONObject responseJson) {  //API = http://api-kz.zyue88.com/api/get_check_link
        try {
            Log.e(TAG, "getlink");
            String result = responseJson.get("result").toString();
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                jsonArray = new JSONArray(responseJson.get("records").toString());
                List<String> userlink = new ArrayList<>();
                userlink.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    userlink.add(jsonArray.get(i).toString());
                }
                Log.e(TAG, "userlink = " + userlink);
                linksettingList = new LinksettingList(this, userlink, getInnerItem, company, account);
                loading.dismiss();
            } else if (result.matches("error1")) {
                loading.dismiss();
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "No Combined Sub Account", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "無連結的子帳號戶口", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "无连结的子帐号户口", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                linksettingList = null;
                listView.setAdapter(linksettingList);
            }
            showview();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void itemOnClick(View v, String nowcompany, String nowaccount) {
        if(v.getId() == R.id.textView3) {
            if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                new AlertDialog.Builder(this)
                        .setTitle("努霸財富管家")
                        .setIcon(R.drawable.app_icon_mini)
                        .setMessage("Are you sure to cancel?")
                        .setPositiveButton("Yes", (dialog, which) -> cancelLink(v))
                        .setNegativeButton("No", (dialog, which) -> {
                            // TODO Auto-generated method stub
                        }).show();
            } else if (Value.language_flag == 1) {
                new AlertDialog.Builder(this)
                        .setTitle("努霸財富管家")
                        .setIcon(R.drawable.app_icon_mini)
                        .setMessage("確定要取消嗎?")
                        .setPositiveButton("確定", (dialog, which) -> cancelLink(v))
                        .setNegativeButton("取消", (dialog, which) -> {
                            // TODO Auto-generated method stub
                        }).show();
            } else if (Value.language_flag == 2) {
                new AlertDialog.Builder(this)
                        .setTitle("努霸财富管家")
                        .setIcon(R.drawable.app_icon_mini)
                        .setMessage("确定要取消吗?")
                        .setPositiveButton("确定", (dialog, which) -> cancelLink(v))
                        .setNegativeButton("取消", (dialog, which) -> {
                            // TODO Auto-generated method stub
                        }).show();
            }
        }else if(v.getId() == R.id.button1){
            company = nowcompany;
            account = nowaccount;
            backform();
        }
    }

    @Override
    public void canceled(JSONObject responseJson) { // API = http://api-kz.zyue88.com/api/cancel_link
        try {
            Log.e(TAG, "canceled");
            String result = responseJson.get("result").toString();
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                link.setConnect(company, account, getLink);
            } else if (result.matches("error1")) {
                loading.dismiss();
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Cancel Combine Failed", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "取消連結失敗", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "取消连结失败", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tolink(JSONObject responseJson) {   // API = http://api-kz.zyue88.com/api/link_form
        try {
            Log.e(TAG, "tolink");
            String result = responseJson.get("result").toString();
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                link.setConnect(company, account, getLink);
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
                    Toast toast = Toast.makeText(this, "Object Must Not Be Yourself", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "對象不可為自己", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "对象不可为自己", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (result.matches("error5")) {
                loading.dismiss();
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Object Sub Account Or Password Incorrect", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "對象子帳號戶口或密碼錯誤", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "对象子帐号户口或密码错误", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

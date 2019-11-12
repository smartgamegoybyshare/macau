package com.threesing.macau.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.threesing.macau.Language.LanguageListener;
import com.threesing.macau.Language.SetLanguage;
import com.threesing.macau.ListView.UserDataList;
import com.threesing.macau.Post_Get.CheckAll.CheckAll;
import com.threesing.macau.Post_Get.CheckAll.CheckAllListener;
import com.threesing.macau.Post_Get.CheckAll.GetCheck;
import com.threesing.macau.Post_Get.UserData.ConnectUserDataBase;
import com.threesing.macau.Post_Get.UserData.GetUserData;
import com.threesing.macau.Post_Get.UserData.UserdataListener;
import com.threesing.macau.Post_Get.UserRecord.GetUserRecord;
import com.threesing.macau.Post_Get.UserRecord.UserRecord;
import com.threesing.macau.Post_Get.UserRecord.UserRecordListener;
import com.threesing.macau.R;
import com.threesing.macau.SQL.LanguageSQL;
import com.threesing.macau.Support.InternetImage;
import com.threesing.macau.Support.Loading;
import com.threesing.macau.Support.SelectDialog;
import com.threesing.macau.Support.SelectTotalDialog;
import com.threesing.macau.Support.Value;
import com.threesing.macau.TextType.CustomTypeFaceSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import pl.droidsonroids.gif.GifImageView;

public class FormActivity extends AppCompatActivity implements UserdataListener, UserRecordListener,
        CheckAllListener, LanguageListener {

    private String TAG = "FormActivity";
    private String company, account;
    private Loading loading = new Loading(this);
    private LanguageSQL languageSQL = new LanguageSQL(this);
    private UserRecord userRecord = new UserRecord(this);
    private ConnectUserDataBase connectUserDataBase = new ConnectUserDataBase(this);
    private SetLanguage setLanguage = new SetLanguage();
    private GetUserData getUserData = new GetUserData();
    private GetUserRecord getUserRecord = new GetUserRecord();
    private CheckAll checkAll = new CheckAll(this);
    private GetCheck getCheck = new GetCheck();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private LinearLayout lineaBackground1, lineaBackground2, lineaBackground3, lineaBackground4,
            lineaBackground5, lineaBackground6, lineaBackground7;
    private int select_item = -1;
    private Bitmap preview_bitmap;
    //private GifImageView gifImageView1;
    private TextView toolbartitle, nowtime, date, chartcode, remark, gain, loss, balance, checked, back;
    private Button accountLink, checkform, refresh;
    private InternetImage internetImage = new InternetImage();
    private Handler handler = new Handler(), buttonHandler = new Handler(), swipeHandler = new Handler();
    private PopupWindow popWindow;
    private boolean popWindowView = false, regetalldata = false, language_bool = false, swipe = false;
    private int click = 0;
    private Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "FormActivity");
        setContentView(R.layout.formpage);

        toolbartitle = findViewById(R.id.toolbar_title);
        back = findViewById(R.id.backView1);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        listView = findViewById(R.id.listView1);
        nowtime = findViewById(R.id.nowTime);   //更新數據時間
        accountLink = findViewById(R.id.button1);   //客戶看帳按鈕
        checkform = findViewById(R.id.button2); //對帳按鈕
        date = findViewById(R.id.textView3);    //title日期
        chartcode = findViewById(R.id.textView4);
        remark = findViewById(R.id.textView5);
        gain = findViewById(R.id.textView6);
        loss = findViewById(R.id.textView7);
        balance = findViewById(R.id.textView8);
        checked = findViewById(R.id.textView9);

        face = Typeface.createFromAsset(getAssets(), "fonts/GenJyuuGothic-Normal.ttf");

        back.setVisibility(View.GONE);
        Log.e(TAG, "開始加粗");
        date.getPaint().setFakeBoldText(true);
        chartcode.getPaint().setFakeBoldText(true);
        remark.getPaint().setFakeBoldText(true);
        gain.getPaint().setFakeBoldText(true);
        loss.getPaint().setFakeBoldText(true);
        balance.getPaint().setFakeBoldText(true);
        checked.getPaint().setFakeBoldText(true);
        Log.e(TAG, "字體加粗");

        refresh = findViewById(R.id.button5);
        if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            loading.show("Getting data");
        } else if (Value.language_flag == 1) {
            loading.show("取得資料中");
        } else if (Value.language_flag == 2) {
            loading.show("获取资料中");
        }
        get_Intent();
    }

    private void get_Intent() {
        Intent intent = getIntent();
        company = intent.getStringExtra("company");
        account = intent.getStringExtra("account");
        Log.e(TAG, "company = " + company);
        Log.e(TAG, "account = " + account);
        getUserData.setListener(this);
        getUserRecord.setListener(this);
        getCheck.setListener(this);
        setLanguage.setListener(this);
        connectUserDataBase.setConnect(company, account, getUserData);
    }

    @SuppressLint("SetTextI18n")
    private void showpage() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        try {
            TextView copyright = findViewById(R.id.textView);
            Button listButtondown = findViewById(R.id.button3);
            Button listButtonup = findViewById(R.id.button4);
            TextView username = findViewById(R.id.textView1);

            copyright.setTypeface(face);
            username.setTypeface(face);
            /*gifImageView1 = findViewById(R.id.imageView1);
            Runnable getimage = () -> {
                String imageUri = "https://dl.kz168168.com/img/omen-ad03.png";
                preview_bitmap = internetImage.fetchImage(imageUri);
                handler.post(() -> {
                    gifImageView1.setImageBitmap(preview_bitmap);
                    gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                });
            };
            new Thread(getimage).start();
                /*GifDrawable gifFromPath = new GifDrawable(this.getResources(), R.drawable.adphoto2);
                gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                gifImageView1.setImageDrawable(gifFromPath);*/
            /*gifImageView1.setOnClickListener(view -> {
                Uri uri = Uri.parse("http://181282.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });*/
            String userdata = Value.get_user_data.get("records").toString();
            JSONArray userdatas = new JSONArray(userdata);
            String getdata = userdatas.get(0).toString();
            JSONObject namedata = new JSONObject(getdata);
            String getname = namedata.get("username").toString();
            username.setText(getname);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                swipe = true;
                regetalldata = true;
                connectUserDataBase.setConnect(company, account, getUserData);
            });
            swipeRefreshLayout.setColorSchemeResources(R.color.progressColor);
            refresh.setTextColor(Color.WHITE);
            refresh.setOnClickListener(view -> {
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    loading.show("Getting data");
                } else if (Value.language_flag == 1) {
                    loading.show("取得資料中");
                } else if (Value.language_flag == 2) {
                    loading.show("获取资料中");
                }
                regetalldata = true;
                connectUserDataBase.setConnect(company, account, getUserData);
            });
            accountLink.setTextColor(Color.WHITE);
            accountLink.setOnClickListener(View -> accountLink());
            if (Value.get_record.get("all_checked").toString().matches("n")) {
                checkform.setTextColor(Color.WHITE);
                checkform.setBackgroundResource(R.drawable.checkall_style);
                checkform.setOnClickListener(view -> {
                    if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                        loading.show("Checking data");
                    } else if (Value.language_flag == 1) {
                        loading.show("確認資料中");
                    } else if (Value.language_flag == 2) {
                        loading.show("确认资料中");
                    }
                    regetalldata = true;
                    checkAll.setConnect(company, account, getCheck);
                });
            } else {
                checkform.setTextColor(Color.WHITE);
                checkform.setBackgroundResource(R.drawable.checkall_style2);
                checkform.setOnClickListener(view -> {
                    if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                        loading.show("Checking data");
                    } else if (Value.language_flag == 1) {
                        loading.show("確認資料中");
                    } else if (Value.language_flag == 2) {
                        loading.show("确认资料中");
                    }
                    checkAll.setConnect(company, account, getCheck);
                });
            }
            copyright.setText(Value.copyright_text + Value.ver);
            listButtondown.setOnClickListener(view -> {
                listButtondown.setVisibility(View.GONE);
                listButtonup.setVisibility(View.GONE);
                int count = Value.user_record.size();
                if (count > 50) {
                    listView.setSelection(count - 50);
                }
                listView.smoothScrollToPosition(count - 1);
                listView.setFriction(ViewConfiguration.getScrollFriction() / 10);
                buttonHandler.postDelayed(() -> {
                    listButtondown.setVisibility(View.VISIBLE);
                    listButtonup.setVisibility(View.VISIBLE);
                }, 1000);
            });
            listButtonup.setOnClickListener(view -> {
                listButtondown.setVisibility(View.GONE);
                listButtonup.setVisibility(View.GONE);
                int count = Value.user_record.size();
                if (count > 50) {
                    listView.setSelection(50);
                }
                listView.smoothScrollToPosition(0);
                listView.setFriction(ViewConfiguration.getScrollFriction() / 10);
                buttonHandler.postDelayed(() -> {
                    listButtondown.setVisibility(View.VISIBLE);
                    listButtonup.setVisibility(View.VISIBLE);
                }, 1000);
            });
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // TODO Auto-generated method stub
                    switch (scrollState) {
                        // 当不滚动时
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                            listButtonup.setVisibility(View.VISIBLE);
                            listButtondown.setVisibility(View.VISIBLE);
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                            listButtondown.setVisibility(View.GONE);
                            listButtonup.setVisibility(View.GONE);
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                            listButtondown.setVisibility(View.GONE);
                            listButtonup.setVisibility(View.GONE);
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                }
            });
            back.setOnClickListener(view -> back());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getDateTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTime.setTimeZone(TimeZone.getTimeZone("America/New_York")); //美東時區
        return dateTime.format(date);
    }

    private void homePage() {
        if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            new AlertDialog.Builder(FormActivity.this)
                    .setTitle("三昇澳門")
                    .setIcon(R.drawable.app_icon_mini)
                    .setMessage("Do you want to Logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        //Intent intent = new Intent(this, MainActivity.class);   //MainActivity
                        Intent intent = new Intent(this, MainActivity_fix.class);
                        Value.get_record = null;
                        Value.record = null;
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // TODO Auto-generated method stub
                    }).show();
        } else if (Value.language_flag == 1) {
            new AlertDialog.Builder(FormActivity.this)
                    .setTitle("三昇澳門")
                    .setIcon(R.drawable.app_icon_mini)
                    .setMessage("確定要登出?")
                    .setPositiveButton("確定", (dialog, which) -> {
                        //Intent intent = new Intent(this, MainActivity.class);   //MainActivity
                        Intent intent = new Intent(this, MainActivity_fix.class);
                        Value.get_record = null;
                        Value.record = null;
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        // TODO Auto-generated method stub
                    }).show();
        } else if (Value.language_flag == 2) {
            new AlertDialog.Builder(FormActivity.this)
                    .setTitle("三昇澳门")
                    .setIcon(R.drawable.app_icon_mini)
                    .setMessage("确定要登出?")
                    .setPositiveButton("确定", (dialog, which) -> {
                        //Intent intent = new Intent(this, MainActivity.class);   //MainActivity
                        Intent intent = new Intent(this, MainActivity_fix.class);
                        Value.get_record = null;
                        Value.record = null;
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        // TODO Auto-generated method stub
                    }).show();
        }
    }

    private void back(){
        Intent intent = new Intent(this, MainActivity_fix.class);
        //Intent intent = new Intent(this, LoginMainActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }

    private void modifyPassword() {
        Intent intent = new Intent(this, ModifyPasswordActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        intent.putExtra("act", "form");
        startActivity(intent);
        finish();
    }

    private void accountLink() {
        Intent intent = new Intent(this, AccountLinkActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }

    private void memberData() {
        Intent intent = new Intent(this, MemberDataActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        intent.putExtra("act", "form");
        startActivity(intent);
        finish();
    }

    private void linkSetting() {
        Intent intent = new Intent(this, LinksettingActivity.class);
        intent.putExtra("company", company);
        intent.putExtra("account", account);
        intent.putExtra("act", "form");
        startActivity(intent);
        finish();
    }

    private Runnable setListView = new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            try {
                JSONArray jsonArray = new JSONArray(Value.get_record.get("records").toString());
                Value.record = jsonArray;
                List<String> user_record = new ArrayList<>();
                user_record.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    user_record.add(jsonArray.get(i).toString());
                }
                user_record.add(Value.get_record.toString());
                Log.e(TAG, "jsonArray = " + jsonArray);
                Log.e(TAG, "user_record = " + user_record);
                Value.user_record = user_record;
                Value.mUserDataList = new UserDataList(FormActivity.this, user_record);
                Log.e(TAG, "執行緒");
                Value.updateTime = getDateTime();
                loading.dismiss();
                handler.post(() -> {
                    listView.setAdapter(Value.mUserDataList);
                    listView.setOnItemClickListener(mListClickListener);
                    listView.setOnItemLongClickListener(mLongListClickListener);
                    nowtime.setText(Value.updatestring + Value.updateTime);
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private AdapterView.OnItemLongClickListener mLongListClickListener = (parent, view, position, id) -> {
        Log.e(TAG, "select = " + Value.user_record.get(position));
        String select = Value.user_record.get(position);
        if(position != Value.user_record.size() - 1) {
            SelectDialog selectDialog = new SelectDialog(this);
            selectDialog.show(select);
        }else {
            SelectTotalDialog selectTotalDialog = new SelectTotalDialog(this);
            selectTotalDialog.show(select);
        }
        return true;
    };

    private AdapterView.OnItemClickListener mListClickListener = new AdapterView.OnItemClickListener() {
        //設置點選view的背景顏色(Yellow)
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            if(click != position){
                click = position;
            }else {
                String select = Value.user_record.get(position);
                if(position != Value.user_record.size() - 1) {
                    SelectDialog selectDialog = new SelectDialog(FormActivity.this);
                    selectDialog.show(select);
                }else {
                    SelectTotalDialog selectTotalDialog = new SelectTotalDialog(FormActivity.this);
                    selectTotalDialog.show(select);
                }
            }

            try {
                LinearLayout linearLayout1 = view.findViewById(R.id.linearLayout1);
                LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout2);
                LinearLayout linearLayout3 = view.findViewById(R.id.linearLayout3);
                LinearLayout linearLayout4 = view.findViewById(R.id.linearLayout4);
                LinearLayout linearLayout5 = view.findViewById(R.id.linearLayout5);
                LinearLayout linearLayout6 = view.findViewById(R.id.linearLayout6);
                LinearLayout linearLayout7 = view.findViewById(R.id.linearLayout7);

                if ((select_item == -1) || (select_item == position)) {
                    if (position != Value.user_record.size() - 1) {
                        linearLayout1.setBackgroundResource(R.drawable.datalist_start_frame_yellow);
                        linearLayout2.setBackgroundResource(R.drawable.datalist_frame_yellow);
                        linearLayout3.setBackgroundResource(R.drawable.datalist_frame_yellow);
                        linearLayout4.setBackgroundResource(R.drawable.datalist_frame_yellow);
                        linearLayout5.setBackgroundResource(R.drawable.datalist_frame_yellow);
                        linearLayout6.setBackgroundResource(R.drawable.datalist_frame_yellow);
                        linearLayout7.setBackgroundResource(R.drawable.datalist_frame_yellow);
                    } else {
                        linearLayout1.setBackgroundResource(R.drawable.liststyle_left_yellow);
                        linearLayout2.setBackgroundResource(R.drawable.datalist_total_buttom_yellow);
                        linearLayout3.setBackgroundResource(R.drawable.datalist_frame_yellow_bottom);
                        linearLayout4.setBackgroundResource(R.drawable.datalist_frame_yellow_bottom);
                        linearLayout5.setBackgroundResource(R.drawable.datalist_frame_yellow_bottom);
                        linearLayout6.setBackgroundResource(R.drawable.datalist_frame_yellow_bottom);
                        linearLayout7.setBackgroundResource(R.drawable.liststyle_right_yellow);
                    }
                } else {
                    String selectrecord = Value.user_record.get(select_item);
                    Log.e(TAG, "selectrecord = " + Value.user_record.get(position));
                    JSONObject jsonObject = new JSONObject(selectrecord);
                    if (position != Value.user_record.size() - 1) {
                        if (select_item != Value.user_record.size() - 1) {
                            if(jsonObject.get("record_check").toString().matches("1")){
                                if(jsonObject.get("record_last_check").toString().matches("0")) {
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_frame_gray);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_gray);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_gray);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_gray);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_gray);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_gray);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_gray);
                                }else if(jsonObject.get("record_last_check").toString().matches("1")){
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_frame_yellow);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                }
                            }else if(jsonObject.get("record_check").toString().matches("2")){
                                if(jsonObject.get("record_last_check").toString().matches("0")) {
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_frame_red);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_red);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_red);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_red);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_red);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_red);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_red);
                                }else if(jsonObject.get("record_last_check").toString().matches("1")){
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_frame_darkblue);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                }
                            } else {
                                if (select_item % 2 == 0) {
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_form_frame);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_form_frame);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_form_frame);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_form_frame);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_form_frame);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_form_frame);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_form_frame);
                                } else {
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_frame_blue);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_blue);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_blue);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_blue);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_blue);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_blue);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_blue);
                                }
                            }
                        } else {
                            //if (select_item % 2 == 0) {
                            lineaBackground1.setBackgroundResource(R.drawable.liststyle_left);
                            lineaBackground2.setBackgroundResource(R.drawable.datalist_total_buttom);
                            lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_bottom);
                            lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_bottom);
                            lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_bottom);
                            lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_bottom);
                            lineaBackground7.setBackgroundResource(R.drawable.liststyle_right);
                        /*} else {
                            lineaBackground1.setBackgroundResource(R.drawable.liststyle_left_blue);
                            lineaBackground2.setBackgroundResource(R.drawable.datalist_total_buttom_blue);
                            lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_blue);
                            lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_blue);
                            lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_blue);
                            lineaBackground6.setBackgroundResource(R.drawable.liststyle_right_blue);
                        }*/
                        }
                        linearLayout1.setBackgroundResource(R.drawable.datalist_start_frame_yellow);
                        linearLayout2.setBackgroundResource(R.drawable.datalist_frame_yellow);
                        linearLayout3.setBackgroundResource(R.drawable.datalist_frame_yellow);
                        linearLayout4.setBackgroundResource(R.drawable.datalist_frame_yellow);
                        linearLayout5.setBackgroundResource(R.drawable.datalist_frame_yellow);
                        linearLayout6.setBackgroundResource(R.drawable.datalist_frame_yellow);
                        linearLayout7.setBackgroundResource(R.drawable.datalist_frame_yellow);
                    } else {
                        if (select_item != Value.user_record.size() - 1) {
                            if(jsonObject.get("record_check").toString().matches("1")){
                                if(jsonObject.get("record_last_check").toString().matches("0")) {
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_frame_gray);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_gray);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_gray);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_gray);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_gray);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_gray);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_gray);
                                }else if(jsonObject.get("record_last_check").toString().matches("1")){
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_frame_yellow);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_yellow);
                                }
                            }else if(jsonObject.get("record_check").toString().matches("2")){
                                if(jsonObject.get("record_last_check").toString().matches("0")) {
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_frame_red);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_red);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_red);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_red);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_red);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_red);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_red);
                                }else if(jsonObject.get("record_last_check").toString().matches("1")){
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_frame_darkblue);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_darkblue);
                                }
                            }else {
                                if (select_item % 2 == 0) {
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_left_frame);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_white);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_white);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_white);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_white);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_white);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_white);
                                } else {
                                    lineaBackground1.setBackgroundResource(R.drawable.datalist_start_frame_blue);
                                    lineaBackground2.setBackgroundResource(R.drawable.datalist_frame_blue);
                                    lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_blue);
                                    lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_blue);
                                    lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_blue);
                                    lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_blue);
                                    lineaBackground7.setBackgroundResource(R.drawable.datalist_frame_blue);
                                }
                            }
                        } else {
                            //if(select_item % 2 == 0) {
                            lineaBackground1.setBackgroundResource(R.drawable.liststyle_left);
                            lineaBackground2.setBackgroundResource(R.drawable.datalist_total_buttom);
                            lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_white);
                            lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_white);
                            lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_white);
                            lineaBackground6.setBackgroundResource(R.drawable.datalist_frame_white);
                            lineaBackground7.setBackgroundResource(R.drawable.liststyle_right);
                        /*}else {
                            lineaBackground1.setBackgroundResource(R.drawable.liststyle_left_blue);
                            lineaBackground2.setBackgroundResource(R.drawable.datalist_total_buttom_blue);
                            lineaBackground3.setBackgroundResource(R.drawable.datalist_frame_blue);
                            lineaBackground4.setBackgroundResource(R.drawable.datalist_frame_blue);
                            lineaBackground5.setBackgroundResource(R.drawable.datalist_frame_blue);
                            lineaBackground6.setBackgroundResource(R.drawable.liststyle_right_blue);
                        }*/
                        }
                        linearLayout1.setBackgroundResource(R.drawable.liststyle_left_yellow);
                        linearLayout2.setBackgroundResource(R.drawable.datalist_total_buttom_yellow);
                        linearLayout3.setBackgroundResource(R.drawable.datalist_frame_yellow_bottom);
                        linearLayout4.setBackgroundResource(R.drawable.datalist_frame_yellow_bottom);
                        linearLayout5.setBackgroundResource(R.drawable.datalist_frame_yellow_bottom);
                        linearLayout6.setBackgroundResource(R.drawable.datalist_frame_yellow_bottom);
                        linearLayout7.setBackgroundResource(R.drawable.liststyle_right_yellow);
                    }
                }
                lineaBackground1 = linearLayout1;
                lineaBackground2 = linearLayout2;
                lineaBackground3 = linearLayout3;
                lineaBackground4 = linearLayout4;
                lineaBackground5 = linearLayout5;
                lineaBackground6 = linearLayout6;
                lineaBackground7 = linearLayout7;
                select_item = position;//保存目前的View位置
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View view) {
        // 这里的view代表popupMenu需要依附的view
        Context wrapper = new ContextThemeWrapper(this, R.style.YOURSTYLE);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
            assert mHelper != null;
            mHelper.setForceShowIcon(true);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            SpannableString password = new SpannableString("Change Password");
            password.setSpan(new CustomTypeFaceSpan("", face, Color.BLACK), 0, password.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            popupMenu.getMenu().findItem(R.id.modify).setTitle(password);
            SpannableString logout = new SpannableString("Logout");
            logout.setSpan(new CustomTypeFaceSpan("", face, Color.BLACK), 0, logout.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            popupMenu.getMenu().findItem(R.id.logout).setTitle(logout);
        } else if (Value.language_flag == 1) {
            SpannableString password = new SpannableString("修改密碼");
            password.setSpan(new CustomTypeFaceSpan("", face, Color.BLACK), 0, password.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            popupMenu.getMenu().findItem(R.id.modify).setTitle(password);
            SpannableString logout = new SpannableString("登出");
            logout.setSpan(new CustomTypeFaceSpan("", face, Color.BLACK), 0, logout.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            popupMenu.getMenu().findItem(R.id.logout).setTitle(logout);
        } else if (Value.language_flag == 2) {
            SpannableString password = new SpannableString("修改密码");
            password.setSpan(new CustomTypeFaceSpan("", face, Color.BLACK), 0, password.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            popupMenu.getMenu().findItem(R.id.modify).setTitle(password);
            SpannableString logout = new SpannableString("登出");
            logout.setSpan(new CustomTypeFaceSpan("", face, Color.BLACK), 0, logout.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            popupMenu.getMenu().findItem(R.id.logout).setTitle(logout);
        }
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.modify) {
                modifyPassword();
                return true;
            } else if (id == R.id.logout) {
                homePage();
                return true;
            }
            // 控件每一个item的点击事件
            return true;
        });
        popupMenu.setOnDismissListener(menu -> {
            // 控件消失时的事件
        });

    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void initPopWindow(View v) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(this).inflate(R.layout.membersetting, null, false);
        LinearLayout linearLayout1 = view.findViewById(R.id.linearLayout1);
        LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout2);
        LinearLayout linearLayout3 = view.findViewById(R.id.linearLayout3);
        LinearLayout linearLayout4 = view.findViewById(R.id.linearLayout4);
        LinearLayout linearLayout5 = view.findViewById(R.id.linearLayout5);
        LinearLayout linearLayout6 = view.findViewById(R.id.linearLayout6);
        TextView textView1 = view.findViewById(R.id.textView1);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);
        TextView textView4 = view.findViewById(R.id.textView4);
        TextView textView5 = view.findViewById(R.id.textView5);
        TextView textView6 = view.findViewById(R.id.textView6);

        linearLayout4.setVisibility(View.GONE);
        linearLayout5.setVisibility(View.GONE);
        linearLayout6.setVisibility(View.GONE);
        textView4.setVisibility(View.GONE);
        textView5.setVisibility(View.GONE);
        textView6.setVisibility(View.GONE);

        textView1.setTypeface(face);
        textView2.setTypeface(face);
        textView3.setTypeface(face);
        textView4.setTypeface(face);
        textView5.setTypeface(face);
        textView6.setTypeface(face);

        if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            textView1.setText("Account");
            textView2.setText("Add Account");
            textView3.setText("Language");
        } else if (Value.language_flag == 1) {
            textView1.setText("戶口帳號");
            textView2.setText("綁定戶口");
            textView3.setText("語言");
        } else if (Value.language_flag == 2) {
            textView1.setText("户口帐号");
            textView2.setText("绑定户口");
            textView3.setText("语言");
        }
        textView4.setText("繁體中文");
        textView5.setText("简体中文");
        textView6.setText("English");
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        popWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor((v1, event) -> {
            popWindowView = false;
            return false;
            // 这里如果返回true的话，touch事件将被拦截
            // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效

        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 0, 0);

        //设置popupWindow里的按钮的事件
        //會員資料
        linearLayout1.setOnClickListener(v12 -> {
            popWindow.dismiss();
            memberData();
        });
        //帳號連結設定
        linearLayout2.setOnClickListener(v13 -> {
            popWindow.dismiss();
            linkSetting();
        });

        linearLayout3.setOnClickListener(view1 -> {
            if (language_bool) {
                language_bool = false;
                linearLayout4.setVisibility(View.GONE);
                linearLayout5.setVisibility(View.GONE);
                linearLayout6.setVisibility(View.GONE);
                textView4.setVisibility(View.GONE);
                textView5.setVisibility(View.GONE);
                textView6.setVisibility(View.GONE);
            } else {
                language_bool = true;
                linearLayout4.setVisibility(View.VISIBLE);
                linearLayout5.setVisibility(View.VISIBLE);
                linearLayout6.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.VISIBLE);
                textView5.setVisibility(View.VISIBLE);
                textView6.setVisibility(View.VISIBLE);
            }
        });

        linearLayout4.setOnClickListener(view14 -> {
            popWindow.dismiss();
            language_bool = false;
            Value.language_flag = 1;
            int id = languageSQL.getID();
            languageSQL.update(id, Value.language_flag);
            new Thread(setListView).start();
            setLanguage.isSet();
        });

        linearLayout5.setOnClickListener(view13 -> {
            popWindow.dismiss();
            language_bool = false;
            Value.language_flag = 2;
            int id = languageSQL.getID();
            languageSQL.update(id, Value.language_flag);
            new Thread(setListView).start();
            setLanguage.isSet();
        });

        linearLayout6.setOnClickListener(view12 -> {
            popWindow.dismiss();
            language_bool = false;
            Value.language_flag = 0;
            int id = languageSQL.getID();
            languageSQL.update(id, Value.language_flag);
            new Thread(setListView).start();
            setLanguage.isSet();
        });
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                //vibrator.vibrate(100);
                back();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.account_chose) {
            showPopupMenu(this.findViewById(R.id.account_chose));
            return true;
        } else if (id == R.id.menu_icon) {
            if (!popWindowView) {
                popWindowView = true;
                initPopWindow(this.findViewById(R.id.menu_icon));
            } else {
                popWindowView = false;
                popWindow.dismiss();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getUserData(JSONObject responseJson) {  //API = http://api-kz.zyue88.com/api/get_user_data
        try {
            Log.e(TAG, "getUserData");
            String result = responseJson.get("result").toString();
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                Value.get_user_data = responseJson;
                userRecord.setConnect(company, account, getUserRecord);
            } else if (result.matches("error2")) {
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
            } else if (result.matches("error3")) {
                loading.dismiss();
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

    @SuppressLint("SetTextI18n")
    @Override
    public void getRecord(JSONObject responseJson) {    // API = http://api-kz.zyue88.com/api/get_record
        try {
            Log.e(TAG, "getRecord");
            String result = responseJson.get("result").toString();
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                if (swipe) {
                    swipe = false;
                    swipeHandler.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 2000);
                }
                Value.get_record = responseJson;
                JSONArray jsonArray = new JSONArray(responseJson.get("records").toString());
                if (Value.record == null) {
                    new Thread(setListView).start();
                } else if (Value.record.length() != jsonArray.length()) {
                    new Thread(setListView).start();
                } else {
                    Log.e(TAG, "same");
                    if (Value.get_record.get("all_checked").toString().matches("y")) {
                        if (!regetalldata) {
                            loading.dismiss();
                            Log.e(TAG, "Value.updateTime = " + Value.updateTime);
                            nowtime.setText(Value.updatestring + Value.updateTime);
                            listView.setAdapter(Value.mUserDataList);
                            listView.setOnItemClickListener(mListClickListener);
                        } else {
                            regetalldata = false;
                            new Thread(setListView).start();
                        }
                    } else {
                        new Thread(setListView).start();
                    }
                }
                setLanguage.isSet();
                showpage();
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkresult(JSONObject responseJson) { // API = http://api-kz.zyue88.com/api/check_all
        try {
            Log.e(TAG, "checkresult");
            String result = responseJson.get("result").toString();
            if (result.matches("ok")) {
                Log.e(TAG, "success = " + responseJson);
                connectUserDataBase.setConnect(company, account, getUserData);
            } else if (result.matches("error1")) {
                loading.dismiss();
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    Toast toast = Toast.makeText(this, "Account login failed", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 1) {
                    Toast toast = Toast.makeText(this, "帳號登入失敗", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Value.language_flag == 2) {
                    Toast toast = Toast.makeText(this, "帐号登入失败", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else if (result.matches("error2")) {
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
            } else if (result.matches("error3")) {
                loading.dismiss();
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

    @SuppressLint("SetTextI18n")
    @Override
    public void setLanguage() {
        try {
            if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                toolbartitle.setText("Account Checking");
                back.setText("back");
                date.setText("Date");
                chartcode.setText("Chart Code");
                remark.setText("Remarks");
                gain.setText("Gain");
                loss.setText("Loss");
                balance.setText("Balance");
                checked.setText("Checked");
                accountLink.setText("Account");
                refresh.setText("Refresh");
                if (Value.get_record.get("all_checked").toString().matches("n")) {
                    checkform.setText("Check All");
                } else {
                    checkform.setText("All Checked");
                }
            } else if (Value.language_flag == 1) {
                toolbartitle.setText("客戶看帳");
                back.setText("返回");
                date.setText("日期");
                chartcode.setText("交易");
                remark.setText("備註");
                gain.setText("盈");
                loss.setText("虧");
                balance.setText("餘額");
                checked.setText("對帳");
                accountLink.setText("轉換戶口");
                refresh.setText("更新");
                if (Value.get_record.get("all_checked").toString().matches("n")) {
                    checkform.setText("對帳");
                } else {
                    checkform.setText("已對帳完畢");
                }
            } else if (Value.language_flag == 2) {
                toolbartitle.setText("客户看帐");
                back.setText("返回");
                date.setText("日期");
                chartcode.setText("交易");
                remark.setText("备注");
                gain.setText("盈");
                loss.setText("亏");
                balance.setText("馀额");
                checked.setText("对帐");
                accountLink.setText("转换户口");
                refresh.setText("更新");
                if (Value.get_record.get("all_checked").toString().matches("n")) {
                    checkform.setText("对帐");
                } else {
                    checkform.setText("已对帐完毕");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

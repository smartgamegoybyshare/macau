package com.threesing.macau.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.threesing.macau.Language.LanguageListener;
import com.threesing.macau.Language.SetLanguage;
import com.threesing.macau.R;
import com.threesing.macau.Support.InternetImage;
import com.threesing.macau.Support.Loading;
import com.threesing.macau.Support.Value;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import pl.droidsonroids.gif.GifImageView;

public class WebviewActivity extends AppCompatActivity implements LanguageListener {

    private String TAG = "WebviewActivity";
    private SetLanguage setLanguage = new SetLanguage();
    private TextView title, back, copyright, nowTime;
    private LinearLayout lineargif, backgroundlinear;
    private String title_text, geturl, company, account;
    private GifImageView gifImageView1;
    private Bitmap preview_bitmap;
    private WebView webview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean swipe = false;
    private InternetImage internetImage = new InternetImage();
    private Handler handler = new Handler(), refreshHandler = new Handler();
    private Loading loading = new Loading(this);
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //隱藏標題欄
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            loading.show("Loading...");
        } else if (Value.language_flag == 1) {
            loading.show("載入中...");
        } else if (Value.language_flag == 2) {
            loading.show("加載中...");
        }
        get_Intent();
    }

    private Runnable refreshUrl = new Runnable() {
        @Override
        public void run() {
            if (swipe) {
                swipe = false;
                refreshHandler.postDelayed(() -> {
                    webview.clearCache(true);
                    webview.reload();
                    swipeRefreshLayout.setRefreshing(false);
                }, 2000);
            }
        }
    };

    private void get_Intent() {
        Intent intent = getIntent();
        title_text = intent.getStringExtra("title");
        geturl = intent.getStringExtra("url");
        company = intent.getStringExtra("company");
        account = intent.getStringExtra("account");
        Log.e(TAG, "title = " + title_text);
        Log.e(TAG, "geturl = " + geturl);
        Log.e(TAG, "company = " + company);
        Log.e(TAG, "account = " + account);

        watchView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void watchView() {
        setContentView(R.layout.watchweb);

        backgroundlinear = findViewById(R.id.backgroundlinear);
        lineargif = findViewById(R.id.linear_gif);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        title = findViewById(R.id.textView);   //對帳通知
        back = findViewById(R.id.textView1);   //返回
        copyright = findViewById(R.id.copyright);
        nowTime = findViewById(R.id.nowTime);
        gifImageView1 = findViewById(R.id.imageView1); //廣告欄
        Runnable getimage = () -> {
            String imageUri = "https://dl.kz168168.com/img/omen-ad01.png";
            preview_bitmap = internetImage.fetchImage(imageUri);
            handler.post(() -> {
                gifImageView1.setImageBitmap(preview_bitmap);
                gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        };
        new Thread(getimage).start();
        gifImageView1.setOnClickListener(view -> {
            Uri uri = Uri.parse("http://3singsport.win/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        back.setOnClickListener(view -> homePage());
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipe = true;
            new Thread(refreshUrl).start();
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.progressColor);
        Value.updateTime = getDateTime();
        setLanguage.setListener(this);
        setLanguage.isSet();

        webview = findViewById(R.id.web_view);
        /*String userAgent = webview.getSettings().getUserAgentString();
        if (!TextUtils.isEmpty(userAgent)) {    //去除浮窗式廣告
            webview.getSettings().setUserAgentString(userAgent
                    .replace("Android", "")
                    .replace("android", "") + " cldc");
        }*/
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true); // 支持缩放
        // 设置出现缩放工具
        webview.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webview.getSettings().setUseWideViewPort(true);
        //不显示webview缩放按钮
        webview.getSettings().setDisplayZoomControls(false);
        //自适应屏幕
        //noinspection deprecation
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setAllowFileAccessFromFileURLs(true);
        webview.getSettings().setBlockNetworkImage(false);//不阻塞网络图片
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                requeststorage(url);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重寫此方法表明點選網頁裡面的連結還是在當前的webview裡跳轉,不跳到瀏覽器那邊
                Log.e(TAG, "點選新連結");
                if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                    loading.show("Loading...");
                } else if (Value.language_flag == 1) {
                    loading.show("載入中...");
                } else if (Value.language_flag == 2) {
                    loading.show("加載中...");
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e(TAG, "加載中");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setTitle(view.getTitle());
                super.onPageFinished(view, url);
                loading.dismiss();
                Log.e(TAG, "加載完畢");
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){

            private View mCustomView;
            private CustomViewCallback mCustomViewCallback;
            private int mOriginalOrientation;
            private int mOriginalSystemUiVisibility;

            public Bitmap getDefaultVideoPoster() {
                if (mCustomView == null) {
                    return null;
                }
                return BitmapFactory.decodeResource(getApplicationContext().getResources(), 0);
            }

            public void onHideCustomView() {
                ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
                this.mCustomView = null;
                getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
                setRequestedOrientation(this.mOriginalOrientation);
                this.mCustomViewCallback.onCustomViewHidden();
                this.mCustomViewCallback = null;
            }

            public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback) {
                if (this.mCustomView != null)
                {
                    onHideCustomView();
                    return;
                }
                this.mCustomView = paramView;
                this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                this.mOriginalOrientation = getRequestedOrientation();
                this.mCustomViewCallback = paramCustomViewCallback;
                ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
                getWindow().getDecorView().setSystemUiVisibility(3846);
            }
        });

        webview.loadUrl(geturl);
    }

    private void downloadManager(String url){
        Log.e(TAG, "url = " + url);
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        Log.e(TAG, "fileName = " + fileName);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        long downloadId = 0;
        if (dm != null) {
            downloadId = dm.enqueue(request);
        }
        Toast.makeText(getApplicationContext(), "即將開始下載",
                //To notify the Client that the file is being downloaded
                Toast.LENGTH_LONG).show();
        Log.e(TAG, "downloadId = " + downloadId);
        /*NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.app_icon_mini)
                .setPriority(NotificationCompat.PRIORITY_LOW);

// Issue the initial notification with zero progress
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = 0;
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManager.notify(notificationId, builder.build());

// Do the job here that tracks the progress.
// Usually, this should be in a
// worker thread
// To show progress, update PROGRESS_CURRENT and update the notification with:
// builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
// notificationManager.notify(notificationId, builder.build());

// When done, update the notification one more time to remove the progress bar
        builder.setContentText("Download complete")
                .setProgress(0,0,false);
        notificationManager.notify(notificationId, builder.build());*/
    }

    private void requeststorage(String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //未取得權限，向使用者要求允許權限
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        REQUEST_EXTERNAL_STORAGE);
            } else {
                downloadManager(url);
                //已有權限，可進行工作
            }
        } else {
            downloadManager(url);
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
        if (company.matches("") && account.matches("")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginMainActivity.class);
            intent.putExtra("company", company);
            intent.putExtra("account", account);
            startActivity(intent);
            finish();
        }
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    homePage();
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
        webview.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        webview.onPause();
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
        webview.destroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
            title.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            backgroundlinear.setVisibility(View.GONE);
            lineargif.setVisibility(View.GONE);
            gifImageView1.setVisibility(View.GONE);
            copyright.setVisibility(View.GONE);
            nowTime.setVisibility(View.GONE);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            title.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            backgroundlinear.setVisibility(View.VISIBLE);
            lineargif.setVisibility(View.VISIBLE);
            gifImageView1.setVisibility(View.VISIBLE);
            copyright.setVisibility(View.VISIBLE);
            nowTime.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setLanguage() {    //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
        if (Value.language_flag == 0) {
            title.setText(title_text);
            back.setText("back");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 1) {
            title.setText(title_text);
            back.setText("返回");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 2) {
            title.setText(title_text);
            back.setText("返回");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }
    }
}

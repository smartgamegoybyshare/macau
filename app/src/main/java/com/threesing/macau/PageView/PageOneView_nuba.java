package com.threesing.macau.PageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.threesing.macau.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PageOneView_nuba extends PageView {

    private Bitmap preview_bitmap;
    private ImageView imageView;
    private Handler handler = new Handler();

    public PageOneView_nuba(Context context) {
        super(context);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.pageone, null);
        imageView = view.findViewById(R.id.imageView);
        Runnable getimage = () -> {
            String imageUri = "https://dl.kz168168.com/img/android-slider01.png";
            preview_bitmap = fetchImage(imageUri);
            handler.post(() -> imageView.setImageBitmap(preview_bitmap));
        };
        new Thread(getimage).start();
        //MakeBitmap makeBitmap = new MakeBitmap();
        /*imageView.setImageBitmap(makeBitmap.getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.slider01), 45f));*/

        imageView.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://3singsport.win");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        });

        addView(view);
    }

    private Bitmap fetchImage(String urlstr ) {  //連接網頁獲取的圖片
        try {
            URL url;
            url = new URL(urlstr);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setDoInput( true );
            c.connect();
            InputStream is = c.getInputStream();
            Bitmap img;
            img = BitmapFactory.decodeStream(is);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

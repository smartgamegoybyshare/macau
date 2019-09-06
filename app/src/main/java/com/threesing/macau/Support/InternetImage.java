package com.threesing.macau.Support;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetImage {

    public InternetImage(){
        super();
    }

    public Bitmap fetchImage(String urlstr ) {  //連接網頁獲取的圖片
        try {
            URL url;
            url = new URL(urlstr);
            HttpURLConnection c = ( HttpURLConnection ) url.openConnection();
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

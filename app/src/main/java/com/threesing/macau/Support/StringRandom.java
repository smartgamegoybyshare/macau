package com.threesing.macau.Support;

import android.util.Log;
import java.util.Random;

public class StringRandom {

    private String TAG = "StringRandom";

    public StringRandom(){
        super();
    }

    //生成隨機數字和字母,
    public String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //參數length，表示生成幾位隨機數
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //輸出字母還是數字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //輸出是大寫字母還是小寫字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}

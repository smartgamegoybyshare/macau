package com.threesing.macau.Support;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParaseUrl {

    private String TAG = "ParaseUrl";

    public ParaseUrl(){
        super();
    }

    public String getDoc(){
        String version = "";
        try {
            //Result result = new Result();
            Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.threesing.macau").timeout(30000).get();
                if (doc.select("span[class=htlgb]") != null) {
                    Elements elScripts = doc.select("span[class=htlgb]").select("div[class=IQ1z0d]").select("span[class=htlgb]");
                    String elScriptList = elScripts.get(3).toString();
                    version = elScriptList.substring(elScriptList.indexOf(">") + 1, elScriptList.lastIndexOf("<"));
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return version;
    }
}

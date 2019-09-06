package com.threesing.macau.Post_Get.Login;

import android.widget.CheckBox;
import org.json.JSONObject;

public interface ConnectListener {
    void isConnected(JSONObject responseJson, String company, String account,
                     String password, CheckBox checkBox);
}


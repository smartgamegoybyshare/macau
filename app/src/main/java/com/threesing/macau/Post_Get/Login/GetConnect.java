package com.threesing.macau.Post_Get.Login;

import android.widget.CheckBox;
import org.json.JSONObject;

public class GetConnect {

    private ConnectListener connectListener;

    public void setListener(ConnectListener mConnectListener){
        connectListener = mConnectListener;
    }

    void connected(JSONObject responseJson, String company, String account, String password, CheckBox checkBox){
        if(connectListener != null && responseJson != null && company != null && account != null &&
                password != null && checkBox != null){
            connectListener.isConnected(responseJson, company, account, password, checkBox);
        }
    }
}

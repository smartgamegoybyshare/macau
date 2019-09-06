package com.threesing.macau.Post_Get.UserData;

import org.json.JSONObject;

public class GetUserData {

    private UserdataListener userdataListener;

    public void setListener(UserdataListener mUserdataListener){
        userdataListener = mUserdataListener;
    }

    void connected(JSONObject responseJson){
        if(userdataListener != null && responseJson != null){
            userdataListener.getUserData(responseJson);
        }
    }
}

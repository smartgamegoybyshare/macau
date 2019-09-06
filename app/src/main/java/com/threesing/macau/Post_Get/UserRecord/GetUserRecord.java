package com.threesing.macau.Post_Get.UserRecord;

import org.json.JSONObject;

public class GetUserRecord {

    private UserRecordListener userRecordListener;

    public void setListener(UserRecordListener mUserRecordListener){
        userRecordListener = mUserRecordListener;
    }

    void connected(JSONObject responseJson){
        if(userRecordListener != null && responseJson != null){
            userRecordListener.getRecord(responseJson);
        }
    }
}

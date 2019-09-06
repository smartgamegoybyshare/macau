package com.threesing.macau.Post_Get.Message;

import org.json.JSONObject;

public class GetPost {

    private PostListener postListener;

    public void setListener(PostListener mPostListener){
        postListener = mPostListener;
    }

    void getMessage(JSONObject responseJson) {
        if (postListener != null && responseJson != null) {
            postListener.getmessage(responseJson);
        }
    }
}

package com.threesing.macau.Post_Get.CheckAll;

import org.json.JSONException;
import org.json.JSONObject;

public class GetCheck {

    private CheckAllListener checkAllListener;

    public void setListener(CheckAllListener mCheckAllListener) {
        checkAllListener = mCheckAllListener;
    }

    void checkresult(JSONObject responseJson) {
        try {
            if (checkAllListener != null && responseJson != null) {
                if (responseJson.get("result").toString().matches("ok"))
                    checkAllListener.checkresult(responseJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

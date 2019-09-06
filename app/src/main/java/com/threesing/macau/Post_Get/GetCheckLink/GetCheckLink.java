package com.threesing.macau.Post_Get.GetCheckLink;

import org.json.JSONObject;

public class GetCheckLink {

    private CheckLinkListener checkLinkListener;

    public void setListener(CheckLinkListener mCheckLinkListener) {
        checkLinkListener = mCheckLinkListener;
    }

    void checklink(JSONObject responseJson) {
        if (checkLinkListener != null && responseJson != null) {
            checkLinkListener.getlink(responseJson);
        }
    }
}

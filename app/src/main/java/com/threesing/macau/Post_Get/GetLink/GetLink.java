package com.threesing.macau.Post_Get.GetLink;

import org.json.JSONObject;

public class GetLink {

    private LinkListener linkListener;

    public void setListener(LinkListener mLinkListener) {
        linkListener = mLinkListener;
    }

    void link(JSONObject responseJson) {
        if (linkListener != null && responseJson != null) {
            linkListener.getlink(responseJson);
        }
    }
}

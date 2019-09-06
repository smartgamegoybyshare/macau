package com.threesing.macau.Post_Get.CancelLink;

import org.json.JSONObject;

public class GetCancelLink {

    private CancelLinkListener cancelLinkListener;

    public void setListener(CancelLinkListener mCancelLinkListener) {
        cancelLinkListener = mCancelLinkListener;
    }

    void checkCancel(JSONObject responseJson) {
        if (cancelLinkListener != null && responseJson != null) {
            cancelLinkListener.canceled(responseJson);
        }
    }
}

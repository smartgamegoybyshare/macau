package com.threesing.macau.ListView.buttonItem;

import android.view.View;

public class GetButtonItem {

    private ButtonItemOnclickListener buttonItemOnclickListener;

    public void setButtonItemOnclickListener(ButtonItemOnclickListener mButtonItemOnclickListener){
        buttonItemOnclickListener = mButtonItemOnclickListener;
    }

    public void clickItem(View view, String company, String account){
        if(buttonItemOnclickListener != null && view != null){
            buttonItemOnclickListener.itemOnClick(view, company, account);
        }
    }
}

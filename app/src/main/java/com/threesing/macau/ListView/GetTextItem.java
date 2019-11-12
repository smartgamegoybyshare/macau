package com.threesing.macau.ListView;

import android.view.View;

public class GetTextItem {

    private TextItemOnclickListener textItemOnclickListener;

    public void setTextItemOnclickListener(TextItemOnclickListener mTextItemOnclickListener){
        textItemOnclickListener = mTextItemOnclickListener;
    }

    public void clickItem(View view, String title){
        if(textItemOnclickListener != null & title != null){
            textItemOnclickListener.itemOnClick(view, title);
        }
    }
}

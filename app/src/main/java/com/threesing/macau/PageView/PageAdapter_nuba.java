package com.threesing.macau.PageView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class PageAdapter_nuba extends PagerAdapter {

    private String TAG = "PageAdapter";
    private List<PageView> pageList;

    public PageAdapter_nuba(List<PageView> pageList) {
        //initItemList(pageList);
        this.pageList = pageList;
    }

    @Override
    public int getCount() {
        return pageList.size();
        //return Integer.MAX_VALUE;   //unlimited circle
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return o == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //unlimited circle
        /*try {
            container.addView(pageList.get(position % pageList.size()));
        }catch (Exception ignored){
        }
        return pageList.get(position % pageList.size());*/
        /*Log.e(TAG, "addView = " + position);
        container.addView(pageList.get(position), 0);*/
        //对ViewPager页号求模取出View列表中要显示的项  
        Log.e(TAG, "addView = " + position);
        position %= pageList.size();
        Log.e(TAG, "new position = " + position);
        if (position < 0) {
            position = pageList.size() + position;
        }
//如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。  
        ViewParent vp = pageList.get(position).getParent();
        if (vp != null) {
            container.removeView(pageList.get(position));
        }
        container.addView(pageList.get(position));
//add listeners here if necessary  
        return pageList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //Log.e(TAG, "position = " + position);
        //container.removeView(pageList.get(position));
    }
}

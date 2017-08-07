package com.example.administrator.ewmarket.app.View;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.administrator.ewmarket.R;


/**
 * Created by Administrator on 2016/12/22 0022.
 */

public class LoadingLayoutLoader
{
    private RelativeLayout LOADING_LAYOUT_root;
    public LoadingLayoutLoader(View layoutView)
    {
        if (layoutView != null)
        {
           LOADING_LAYOUT_root = (RelativeLayout) layoutView.findViewById(R.id.LOADING_LAYOUT);
        }
    }
    public LoadingLayoutLoader(Activity activity)
    {
        if (activity!=null)
        {
            LOADING_LAYOUT_root = (RelativeLayout)activity.findViewById(R.id.LOADING_LAYOUT);
        }
    }
    //把那个进程加载的那个View挂掉
    public void closed()
    {

        LOADING_LAYOUT_root.setVisibility(View.GONE);
    }
    //进程加载的那个View显示
    public void onLoading()
    {
        LOADING_LAYOUT_root.setVisibility(View.VISIBLE);
    }
}

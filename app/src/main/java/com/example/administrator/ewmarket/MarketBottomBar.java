package com.example.administrator.ewmarket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class MarketBottomBar extends LinearLayout
{
    private LinearLayout mHome;
    private LinearLayout mDestination;
    private LinearLayout mFinder;
    private LinearLayout mTrip;
    private LinearLayout mMy;
    private MainActivity mainActivity;
    private View.OnClickListener mClickListener= new OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            handleOnClick(view);
        }
    };
    public MarketBottomBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        //z这块遇到个坑
        mainActivity=(MainActivity) context;
        LayoutInflater.from(context).inflate(R.layout.market_bottombar,this);
        initView();
        initEvent();
        //初始化进入app的第一个fragment
        initPage(0);
    }
    private void initView()
    {
        mHome=(LinearLayout)this.findViewById(R.id.tab_bar1);
        mDestination=(LinearLayout)this.findViewById(R.id.tab_bar2);
        mFinder=(LinearLayout)this.findViewById(R.id.tab_bar3);
        mTrip=(LinearLayout)this.findViewById(R.id.tab_bar4);
        mMy=(LinearLayout)this.findViewById(R.id.tab_bar5);
    }
    private void initEvent()
    {
        mHome.setOnClickListener(mClickListener);
        mDestination.setOnClickListener(mClickListener);
        mFinder.setOnClickListener(mClickListener);
        mTrip.setOnClickListener(mClickListener);
        mMy.setOnClickListener(mClickListener);
    }

    //就是初始第一个被点击的按钮
    public void initPage(int page)
    {
        switch (page)
        {
            case 0:
                handleOnClick(mHome);
                break;
            case 1:
                handleOnClick(mDestination);
                break;
            case 2:
                handleOnClick(mFinder);
                break;
            case 3:
                handleOnClick(mTrip);
                break;
            case 4:
                handleOnClick(mMy);
                break;
            default:
                break;

        }
    }
    private void handleOnClick(View view)
    {
       if (view==mHome)
       {
           tabBarisSelected(mHome);
           mainActivity.initFragment(0);

       }else if (view==mDestination)
       {
           tabBarisSelected(mDestination);
           mainActivity.initFragment(1);
       }else if (view==mFinder)
       {
           tabBarisSelected(mFinder);
           mainActivity.initFragment(2);
       }else if (view==mTrip)
       {
           tabBarisSelected(mTrip);
           mainActivity.initFragment(3);
       }else if (view==mMy)
       {
           tabBarisSelected(mMy);
           mainActivity.initFragment(4);
       }

    }
    //先这么写以后再说
    private void tabBarisSelected(View view)
    {
        if (view==mHome)
        {
            mHome.setSelected(true);
            mDestination.setSelected(false);
            mFinder.setSelected(false);
            mTrip.setSelected(false);
            mMy.setSelected(false);
        }
        if (view==mDestination)
        {
            mHome.setSelected(false);
            mDestination.setSelected(true);
            mFinder.setSelected(false);
            mTrip.setSelected(false);
            mMy.setSelected(false);
        }
        if (view==mFinder)
        {
            mHome.setSelected(false);
            mDestination.setSelected(false);
            mFinder.setSelected(true);
            mTrip.setSelected(false);
            mMy.setSelected(false);
        }
        if (view==mTrip)
        {
            mHome.setSelected(false);
            mDestination.setSelected(false);
            mFinder.setSelected(false);
            mTrip.setSelected(true);
            mMy.setSelected(false);
        }
        if (view==mMy)
        {
            mHome.setSelected(false);
            mDestination.setSelected(false);
            mFinder.setSelected(false);
            mTrip.setSelected(false);
            mMy.setSelected(true);
        }

    }

}

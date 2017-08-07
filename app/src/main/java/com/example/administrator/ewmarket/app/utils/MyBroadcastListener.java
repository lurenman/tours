package com.example.administrator.ewmarket.app.utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/28 0028.
 */
//
public class MyBroadcastListener
{

    private  ArrayList<OnLoginSuccessListener> mLoginSuccessListeners;
    public MyBroadcastListener()
    {     //这块每次建立类都会创建一次数组，所以应该把类创建成单例模式
         mLoginSuccessListeners=new ArrayList<OnLoginSuccessListener>();
    }
    //添加到登录成功管理数组中
    public void addLoginSuccessListener(OnLoginSuccessListener listener)
    {
        if (listener!=null&&!mLoginSuccessListeners.contains(listener))
        {
            synchronized (mLoginSuccessListeners)
            {
                mLoginSuccessListeners.add(listener);
            }
        }
    }
    //在管理数组中删除listener
    public void removeLoginSuccessListener(OnLoginSuccessListener listener)
    {
        if (listener!=null&&mLoginSuccessListeners.contains(listener))
        {
            synchronized (mLoginSuccessListeners)
            {
                mLoginSuccessListeners.remove(listener);
            }
        }
    }

    public void LoginSuccessBroadcast()
    {
          //onLoginSuccessListener.onUpdateUi();
        int size=mLoginSuccessListeners.size();
        for (int i=0;i<size;i++)
        {
            mLoginSuccessListeners.get(i).onUpdateUi();
        }
    }
    public interface OnLoginSuccessListener
    {
        void onUpdateUi();
    }
}

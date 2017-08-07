package com.example.administrator.ewmarket;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.example.administrator.ewmarket.app.utils.MyBroadcastListener;

/**
 * Created by Administrator on 2016/12/28 0028.
 */

public class EWApp extends Application
{
    private MyBroadcastListener myBroadcastListener;
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    @Override
    public void onCreate()
    {
        super.onCreate();
        myBroadcastListener=new MyBroadcastListener();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
    }
    public MyBroadcastListener getMyBroadcastListener()
    {
        return this.myBroadcastListener;
    }
    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}

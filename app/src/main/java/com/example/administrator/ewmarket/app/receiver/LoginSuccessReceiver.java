package com.example.administrator.ewmarket.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.ewmarket.EWApp;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.MyBroadcastListener;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class LoginSuccessReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (ConstantValues.LoginSuccess.equals(intent.getAction()))
        {   //跟新UI的操作
//            MyBroadcastListener myBroadcastListener=new MyBroadcastListener();
//            myBroadcastListener.LoginSuccessBroadcast();
            EWApp ewApp= (EWApp) context.getApplicationContext();
            MyBroadcastListener myBroadcastListener=ewApp.getMyBroadcastListener();
            myBroadcastListener.LoginSuccessBroadcast();

        }
    }
}

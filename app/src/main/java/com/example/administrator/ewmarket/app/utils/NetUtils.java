package com.example.administrator.ewmarket.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author by
 * Created by Administrator on 2016/12/22 0022.
 */

public class NetUtils
{    //判断有没有网的
    public static boolean isNetWorkAvailable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network != null)
        {
            return network.isAvailable() && network.isConnected();
        }

        return false;
    }

}

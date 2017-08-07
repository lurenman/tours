package com.example.administrator.ewmarket.app.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/23 0023.
 */

public class Env
{
    //日期格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    //详细时间格式
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT1 = "MM-dd HH:mm:ss";
    /**
     * 跳转activity用的
     */
    public static void startActivity(Context context, Class<?> cls, Bundle bundle)
    {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, cls);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }

        context.startActivity(intent);
    }
    /**
     * 获取日期的方法
     */
    public static String getCurrentDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Env.DATE_FORMAT);
        return dateFormat.format(new Date());
    }

    public static String getCurrentTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Env.DATE_TIME_FORMAT1);
        return dateFormat.format(new Date());
    }
}

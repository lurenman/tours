package com.example.administrator.ewmarket.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author by
 * Created by Administrator on 2016/12/27 0027.
 */

public class SpUtils
{
    private static SharedPreferences sp;

    public static void putString(Context ctx, String key, String value){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context ctx,String key,String defValue){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }
    public static void putInt(Context ctx, String key, int value){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key,value).commit();
    }

    public static int getInt(Context ctx, String key, int defValue){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getInt(key,defValue);
    }

    public static void putBoolean(Context ctx,String key,boolean value){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }
    public static boolean getBoolean(Context ctx,String key,boolean defValue){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

}

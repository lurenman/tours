package com.example.administrator.ewmarket.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class UesrInformationOpenHelper extends SQLiteOpenHelper
{
    public UesrInformationOpenHelper(Context context)
    {
        super(context, "UesrInformation.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS userbaseinformation(userid integer primary key autoincrement, username varchar(20), " +
                "password varchar(20),usericon INTEGER,editname varchar(20),sex varchar(20),birthday varchar(20),englishName varchar(20),phoneNumber varchar(20))");
        //创建一个那个用户订购资讯的表
        db.execSQL("CREATE TABLE IF NOT EXISTS userOrderGoodsTourInfromation(userid integer primary key autoincrement, username varchar(20), ordergoods varchar(20))");
        //创建一个用户评论的旅游资讯的表
        db.execSQL("CREATE TABLE IF NOT EXISTS userDiscussInfo(userid integer primary key autoincrement, username varchar(20), usericon INTEGER,tourname varchar(20),discusscontent varchar(20),discussdate varchar(20))");
        //创建一个用户点赞或鄙视的资讯表
        db.execSQL("CREATE TABLE IF NOT EXISTS userRemarkInfo(userid integer primary key autoincrement, username varchar(20),tourname varchar(20),upremark varchar(20),downremark varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}

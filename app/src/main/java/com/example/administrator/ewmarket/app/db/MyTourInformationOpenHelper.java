package com.example.administrator.ewmarket.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/26 0026.
 */

public class MyTourInformationOpenHelper extends SQLiteOpenHelper
{
    public MyTourInformationOpenHelper(Context context)
    {
        super(context, "TourInformation.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS tourInformation(id integer primary key autoincrement,itemid varchar(20),tourname varchar(20),icon INTEGER,startplace varchar(20), destination varchar(20), price varchar(20),tour1content varchar(20),tour2content varchar(20),upremark INTEGER,downremark INTEGER,detailcontent varchar(20),costcontent varchar(20))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}

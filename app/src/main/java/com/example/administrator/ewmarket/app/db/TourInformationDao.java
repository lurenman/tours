package com.example.administrator.ewmarket.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.ewmarket.R;
import com.example.administrator.ewmarket.app.engine.InitTourInformation;
import com.example.administrator.ewmarket.app.engine.TourInformation;
import com.example.administrator.ewmarket.app.utils.ConstantValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/26 0026.
 */

public class TourInformationDao
{
    private Context context;
    private MyTourInformationOpenHelper tourInformationOpenHelper;
    private static TourInformationDao tourInformationDao=null;
    private TourInformationDao(Context context)
    {
        this.context = context;
        tourInformationOpenHelper = new MyTourInformationOpenHelper(context);
        //这个写到数据的方法调用一次就可以了，不要再调用了
        //initData();
        List mList=findAll();
        if (mList.size()==0)
        {
          InitTourInformation.initData(tourInformationOpenHelper);
        }
    }
    public static TourInformationDao getInstance(Context context)
    {
        if (tourInformationDao==null)
        {
            tourInformationDao=new TourInformationDao(context);
        }
        return tourInformationDao;
    }
    /**
     * 查询所有信息
     */
    public List<TourInformation> findAll() {
        List<TourInformation> TourInformations = null;
        SQLiteDatabase db = tourInformationOpenHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query("tourInformation", null, null, null, null, null,
                    "id desc");
            TourInformations = new ArrayList<TourInformation>();
            while (cursor.moveToNext()) {
                TourInformation tourInformation = new TourInformation();

                String itemid=cursor.getString(cursor.getColumnIndex("itemid"));
                String tourname=cursor.getString(cursor.getColumnIndex("tourname"));
                int icon=cursor.getInt(cursor.getColumnIndex("icon"));
                String startplace=cursor.getString(cursor.getColumnIndex("startplace"));
                String destination=cursor.getString(cursor.getColumnIndex("destination"));
                String price=cursor.getString(cursor.getColumnIndex("price"));
                String tour1content=cursor.getString(cursor.getColumnIndex("tour1content"));
                String tour2content=cursor.getString(cursor.getColumnIndex("tour2content"));
                int upremark=cursor.getInt(cursor.getColumnIndex("upremark"));
                int downremark=cursor.getInt(cursor.getColumnIndex("downremark"));
                String detailcontent=cursor.getString(cursor.getColumnIndex("detailcontent"));
                String costcontent=cursor.getString(cursor.getColumnIndex("costcontent"));

                tourInformation.setItemid(itemid);
                tourInformation.setTourname(tourname);
                tourInformation.setIcon(icon);
                tourInformation.setStartplace(startplace);
                tourInformation.setDestination(destination);
                tourInformation.setPrice(price);
                tourInformation.setTour1content(tour1content);
                tourInformation.setTour2content(tour2content);
                tourInformation.setUpremark(upremark);
                tourInformation.setDownremark(downremark);
                tourInformation.setDetailcontent(detailcontent);
                tourInformation.setCostcontent(costcontent);

                TourInformations.add(tourInformation);
            }
            cursor.close();
            db.close();
        }
        return TourInformations;
    }
    /**
     * 添加发布的资讯信息
     */
    public void addPublishInfo(String currentUser,String tourname, String startplace, String price, String tour1content,
                               String tour2content, String detailcontent, int icon)
    {
        SQLiteDatabase db = tourInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            ContentValues values = new ContentValues();
            values.put("itemid",currentUser);
            values.put("tourname",tourname);
            values.put("icon",icon);
            values.put("startplace",startplace);
            values.put("destination","");
            values.put("price", "￥"+price+"起");
            values.put("tour1content", tour1content);
            values.put("tour2content", tour2content);
            values.put("upremark", 0);
            values.put("downremark", 0);
            values.put("detailcontent",detailcontent);
            values.put("costcontent","费用包含\n" +
                    "交通\n" +
                    "往返团队/散客机票含税费（团队机票将统一出票，散客机票因实时计价预定后即刻出票）\n" +
                    "当地旅游巴士\n" +
                    "往返船票(鼓浪屿轮渡往返船票)\n" +
                    "住宿\n" +
                    "行程所列酒店。\n" +
                    "用餐\n" +
                    "行程中团队标准用餐，5早1正，正餐餐标25元/人。鼓浪屿上酒店无早餐提供，为了您的方便，我们为您安排的是简易早餐（中式餐或自助餐或特色餐，自由活动期间用餐请自理；如因自身原因放弃用餐，则餐费不退）。\n" +
                    "导服");
            db.insert("tourInformation", null, values);
            db.close();
        }
    }
    /**
     * 删除发布的资讯信息
     */
    public void deletePublishInfo(String publishUser, String tourname)
    {
        SQLiteDatabase db = tourInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            db.delete("tourInformation","itemid=? and tourname=?", new String[]{publishUser,tourname});
            db.close();
        }
    }
    /**
     * 查询发布资讯的所有信息
     */
    public List<TourInformation> findPublishInfoAll(String publishUser) {
        List<TourInformation> TourInformations = null;
        SQLiteDatabase db = tourInformationOpenHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query("tourInformation", null,"itemid=?",new String[]{publishUser}, null, null,
                    "id desc");
            TourInformations = new ArrayList<TourInformation>();
            while (cursor.moveToNext()) {
                TourInformation tourInformation = new TourInformation();

                String itemid=cursor.getString(cursor.getColumnIndex("itemid"));
                String tourname=cursor.getString(cursor.getColumnIndex("tourname"));
                int icon=cursor.getInt(cursor.getColumnIndex("icon"));
                String startplace=cursor.getString(cursor.getColumnIndex("startplace"));
                String destination=cursor.getString(cursor.getColumnIndex("destination"));
                String price=cursor.getString(cursor.getColumnIndex("price"));
                String tour1content=cursor.getString(cursor.getColumnIndex("tour1content"));
                String tour2content=cursor.getString(cursor.getColumnIndex("tour2content"));
                int upremark=cursor.getInt(cursor.getColumnIndex("upremark"));
                int downremark=cursor.getInt(cursor.getColumnIndex("downremark"));
                String detailcontent=cursor.getString(cursor.getColumnIndex("detailcontent"));
                String costcontent=cursor.getString(cursor.getColumnIndex("costcontent"));

                tourInformation.setItemid(itemid);
                tourInformation.setTourname(tourname);
                tourInformation.setIcon(icon);
                tourInformation.setStartplace(startplace);
                tourInformation.setDestination(destination);
                tourInformation.setPrice(price);
                tourInformation.setTour1content(tour1content);
                tourInformation.setTour2content(tour2content);
                tourInformation.setUpremark(upremark);
                tourInformation.setDownremark(downremark);
                tourInformation.setDetailcontent(detailcontent);
                tourInformation.setCostcontent(costcontent);

                TourInformations.add(tourInformation);
            }
            cursor.close();
            db.close();
        }
        return TourInformations;
    }

    /**
     * 查询某条资讯的所有信息
     */
    public TourInformation findOrderGoodAllInfo(String ordergood) {

        SQLiteDatabase db = tourInformationOpenHelper.getReadableDatabase();
        TourInformation tourInformation = new TourInformation();
        if (db.isOpen()) {
            Cursor cursor = db.query("tourInformation", null,"tourname=?", new String[] {ordergood}, null, null,
                    null);
            if(cursor.moveToFirst()) {
                String itemid=cursor.getString(cursor.getColumnIndex("itemid"));
                String tourname=cursor.getString(cursor.getColumnIndex("tourname"));
                int icon=cursor.getInt(cursor.getColumnIndex("icon"));
                String startplace=cursor.getString(cursor.getColumnIndex("startplace"));
                String destination=cursor.getString(cursor.getColumnIndex("destination"));
                String price=cursor.getString(cursor.getColumnIndex("price"));
                String tour1content=cursor.getString(cursor.getColumnIndex("tour1content"));
                String tour2content=cursor.getString(cursor.getColumnIndex("tour2content"));
                int upremark=cursor.getInt(cursor.getColumnIndex("upremark"));
                int downremark=cursor.getInt(cursor.getColumnIndex("downremark"));
                String detailcontent=cursor.getString(cursor.getColumnIndex("detailcontent"));
                String costcontent=cursor.getString(cursor.getColumnIndex("costcontent"));

                tourInformation.setItemid(itemid);
                tourInformation.setTourname(tourname);
                tourInformation.setIcon(icon);
                tourInformation.setStartplace(startplace);
                tourInformation.setDestination(destination);
                tourInformation.setPrice(price);
                tourInformation.setTour1content(tour1content);
                tourInformation.setTour2content(tour2content);
                tourInformation.setUpremark(upremark);
                tourInformation.setDownremark(downremark);
                tourInformation.setDetailcontent(detailcontent);
                tourInformation.setCostcontent(costcontent);


            }
            cursor.close();
            db.close();
        }
        return tourInformation;
    }
    /**
     * 查询资讯列tourname所有的旅游名字
     */
    public List<String> findAllTourName()
    {

        SQLiteDatabase db = tourInformationOpenHelper.getReadableDatabase();
        List<String> tourNamesList =  tourNamesList = new ArrayList<String>();
        if (db.isOpen())
        {
            Cursor cursor = db.query("tourInformation", new String[]{"tourname"}, null, null, null, null, null);
            while (cursor.moveToNext())
            {
                tourNamesList.add(cursor.getString(0));
            }
            cursor.close();
            db.close();
        }
        return tourNamesList;
    }
    /**
     * 更新点赞次数
     */
    public void updateUpremark(String tourname,int upremark)
    {
        SQLiteDatabase db = tourInformationOpenHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("upremark",upremark);
        db.update("tourInformation",contentValues, "tourname = ?", new String[]{tourname});
        db.close();
    }
    /**
     * 更新鄙视次数
     */
    public void updateDownremark(String tourname,int downremark)
    {
        SQLiteDatabase db = tourInformationOpenHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("downremark",downremark);
        db.update("tourInformation",contentValues, "tourname = ?", new String[]{tourname});
        db.close();
    }
    /**
     * 查询点赞次数
     */
    public int findUpremark(String tourname)
    {
        SQLiteDatabase db = tourInformationOpenHelper.getReadableDatabase();
        int upremark=0;
        if (db.isOpen())
        {
            Cursor cursor = db.query("tourInformation", null, "tourname=?",
                    new String[] { tourname }, null, null, null);

            if (cursor.moveToFirst()) {
                upremark=cursor.getInt(cursor.getColumnIndex("upremark"));
            }
            cursor.close();
            db.close();
        }
        return upremark;
    }
    /**
     * 查询鄙视次数
     */
    public int findDownremark(String tourname)
    {
        SQLiteDatabase db = tourInformationOpenHelper.getReadableDatabase();
        int downremark=0;
        if (db.isOpen())
        {
            Cursor cursor = db.query("tourInformation", null, "tourname=?",
                    new String[] { tourname }, null, null, null);

            if (cursor.moveToFirst()) {
                downremark=cursor.getInt(cursor.getColumnIndex("downremark"));
            }
            cursor.close();
            db.close();
        }
        return downremark;
    }
}
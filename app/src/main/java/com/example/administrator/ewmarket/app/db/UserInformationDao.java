package com.example.administrator.ewmarket.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.ewmarket.R;
import com.example.administrator.ewmarket.app.engine.CurrentUserOrderGood;
import com.example.administrator.ewmarket.app.engine.TourDiscuss;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class UserInformationDao
{
    private Context context;
    private UesrInformationOpenHelper uesrInformationOpenHelper;
    private static UserInformationDao userInformationDao=null;
    private UserInformationDao(Context context)
    {
        this.context = context;
        uesrInformationOpenHelper=new UesrInformationOpenHelper(context);
    }
    public static UserInformationDao getInstance(Context context)
    {
        if (userInformationDao==null)
        {
            userInformationDao=new UserInformationDao(context);
        }
        return userInformationDao;
    }
    /**
     * 添加一个用户记录
     */
    public void addUser(String username, String password,int usericon)
    {
        SQLiteDatabase db = uesrInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", password);
            values.put("usericon", usericon);
            values.put("editname","");
            values.put("sex","");
            values.put("birthday","");
            values.put("englishName","");
            values.put("phoneNumber","");
            db.insert("userbaseinformation", null, values);
            db.close();
        }
    }
    /**
     * 数据库的查询操作
     */
    public boolean find(String username) {
        boolean result = false;
        SQLiteDatabase db = uesrInformationOpenHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query("userbaseinformation", null, "username=?",
                    new String[] { username }, null, null, null);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    /**
     * 核实用户身份操作
     */
    public boolean checkUser(String username,String password) {
        boolean result = false;
        SQLiteDatabase db = uesrInformationOpenHelper.getReadableDatabase();
        if (db.isOpen()) {
            //妈蛋一点一点试的
            Cursor cursor = db.query("userbaseinformation", null, "username=? and password=?",
                    new String[] { username,password}, null, null, null);
            //cursor.getString(cursor.getColumnIndex(password));
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }
    /**
     * 查找icon
     */
    public int findIcon(String username)
    {

        SQLiteDatabase db = uesrInformationOpenHelper.getReadableDatabase();
        //防止查那头蹦所以设一个默认值吧
        int usericon= R.mipmap.ic_person_center;
        if (db.isOpen())
        {
            Cursor cursor = db.query("userbaseinformation", null, "username=?",
                    new String[] { username }, null, null, null);

            if (cursor.moveToFirst()) {
                usericon=cursor.getInt(cursor.getColumnIndex("usericon"));
            }
            cursor.close();
            db.close();
        }
        return usericon;
    }
    /**
     * 添加用户订购的资讯
     */
    public void addUserOrderGoods(String username, String tourname)
    {
        SQLiteDatabase db = uesrInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("ordergoods", tourname);
            db.insert("userOrderGoodsTourInfromation", null, values);
            db.close();
        }
    }
    /**
     * 删除用户订购的资讯
     */
    public void deleteUserOrderGoods(String username, String tourname)
    {
        SQLiteDatabase db = uesrInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            db.delete("userOrderGoodsTourInfromation","username=? and ordergoods=?", new String[]{username,tourname});
            db.close();
        }
    }
    /**
     * 查询是否已经订购过此资讯
     */
    public boolean findOrderGoods(String username,String tourname) {
        boolean result = false;
        SQLiteDatabase db = uesrInformationOpenHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query("userOrderGoodsTourInfromation", null, "username=? and ordergoods=?",
                    new String[] { username ,tourname}, null, null, null);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    /**
     * 查询当前用户订购的所有的资讯
     */
    public List findCurrentUserAllOrderGoods(String username) {
        SQLiteDatabase db = uesrInformationOpenHelper.getReadableDatabase();
        List<CurrentUserOrderGood> CurrentUserOrderGoodses=null;
        if (db.isOpen()) {
            Cursor cursor = db.query("userOrderGoodsTourInfromation", null, "username=?",
                    new String[] { username}, null, null, null);
            CurrentUserOrderGoodses=new ArrayList<CurrentUserOrderGood>();
            while (cursor.moveToNext()){
                CurrentUserOrderGood currentUserOrderGood=new CurrentUserOrderGood();
                String ordergoods=cursor.getString(cursor.getColumnIndex("ordergoods"));
                currentUserOrderGood.setOrdergoods(ordergoods);
                CurrentUserOrderGoodses.add(currentUserOrderGood);
            }
            cursor.close();
            db.close();
        }
       return CurrentUserOrderGoodses;
    }

    /**
     * 添加一个评论记录
     */
    public void addDiscuss(String username,int usericon,String tourname,String discusscontent,String discussdate)
    {
        SQLiteDatabase db = uesrInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("usericon", usericon);
            values.put("tourname", tourname);
            values.put("discusscontent",discusscontent);
            values.put("discussdate",discussdate);
            db.insert("userDiscussInfo", null, values);
            db.close();
        }
    }

    /**
     * 查询当前资讯的所有评论内容倒序查出
     */
    public List findCurrentTourDiscussInfo(String tourname) {
        SQLiteDatabase db = uesrInformationOpenHelper.getReadableDatabase();
        List<TourDiscuss> DisCussesContents=null;
        if (db.isOpen()) {
            Cursor cursor = db.query("userDiscussInfo", null, "tourname=?",
                    new String[] {tourname}, null, null,"userid desc");
            DisCussesContents=new ArrayList<TourDiscuss>();
            while (cursor.moveToNext()){
                TourDiscuss tourDiscuss=new TourDiscuss();

                String username=cursor.getString(cursor.getColumnIndex("username"));
                int usericon=cursor.getInt(cursor.getColumnIndex("usericon"));
                String discusscontent=cursor.getString(cursor.getColumnIndex("discusscontent"));
                String discussdate=cursor.getString(cursor.getColumnIndex("discussdate"));

                tourDiscuss.setUsername(username);
                tourDiscuss.setIcon(usericon);
                tourDiscuss.setDiscusscontent(discusscontent);
                tourDiscuss.setDiscussdate(discussdate);

                DisCussesContents.add(tourDiscuss);

            }
            cursor.close();
            db.close();
        }
        return DisCussesContents;
    }
    /**
     * 添加一个用户点赞资讯记录
     */
    public void addRemark(String username,String tourname,String upremark,String downremark)
    {
        SQLiteDatabase db = uesrInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("tourname", tourname);
            values.put("upremark", upremark);
            values.put("downremark", downremark);
            db.insert("userRemarkInfo", null, values);
            db.close();
        }
    }

    /**
     * 查询当前用户是否已经点赞过此资讯
     */
    public boolean findUpRemark(String username,String tourname,String upremark) {
        boolean result = false;
        SQLiteDatabase db = uesrInformationOpenHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query("userRemarkInfo", null, "username=? and tourname=? and upremark=?",
                    new String[] {username,tourname,upremark}, null, null, null);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }
    /**
     * 查询当前用户是否已经鄙视过此资讯
     */
    public boolean findDownRemark(String username,String tourname,String downremark) {
        boolean result = false;
        SQLiteDatabase db = uesrInformationOpenHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor1 = db.query("userRemarkInfo", null, "username=? and tourname=? and downremark=?",
                    new String[] {username, tourname ,downremark}, null, null, null);
            if (cursor1.moveToFirst()) {
                result = true;
            }
            cursor1.close();
            db.close();
        }
        return result;
    }
    /**
     * 下面方法都是发布者删除资讯后，所有用户信息中都要删除关于此资讯的信息。
     */
    public void deletePublishDiscussInfo(String tourname)
    {
        SQLiteDatabase db = uesrInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            db.delete("userDiscussInfo", "tourname=?", new String[]{tourname});
            db.close();
        }
    }
    public void deletePublishOrderGoodsInfo(String tourname)
    {
        SQLiteDatabase db = uesrInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            db.delete("userOrderGoodsTourInfromation", "ordergoods=?", new String[]{tourname});
            db.close();
        }
    }
    public void deletePublishUserRemarkInfo(String tourname)
    {
        SQLiteDatabase db = uesrInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            db.delete("userRemarkInfo", "tourname=?", new String[]{tourname});
            db.close();
        }
    }
    /**
     * 编辑个人资料，更新用户个人信息的
     */
    public void updateUserInfo(String username,String editname,String sex,String birthday,String englishName,String phoneNumber)
    {
        SQLiteDatabase db = uesrInformationOpenHelper.getWritableDatabase();
        if (db.isOpen())
        {
            ContentValues values = new ContentValues();
            values.put("editname",editname);
            values.put("sex",sex);
            values.put("birthday",birthday);
            values.put("englishName",englishName);
            values.put("phoneNumber",phoneNumber);
            db.update("userbaseinformation",values,"username=?",
                    new String[]{username});
            db.close();
        }
    }
    /**
     * 编辑个人资料，更新用户个人信息的
     */
    public List<String> queryUserInfo(String username)
    {
        SQLiteDatabase db = uesrInformationOpenHelper.getReadableDatabase();
        List<String> userBaseList=new ArrayList<String>();
        if (db.isOpen())
        {
            Cursor cursor = db.query("userbaseinformation", null, "username=?", new String[]{username}, null, null, null);
            if (cursor.moveToFirst())
            {
                String editname = cursor.getString(cursor.getColumnIndex("editname"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                String englishName = cursor.getString(cursor.getColumnIndex("englishName"));
                String phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));

                userBaseList.add(0,editname);
                userBaseList.add(1,sex);
                userBaseList.add(2,birthday);
                userBaseList.add(3,englishName);
                userBaseList.add(4,phoneNumber);
            }
            cursor.close();
            db.close();
        }

        return  userBaseList;
    }

}

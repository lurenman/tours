package com.example.administrator.ewmarket.app.engine;

import android.content.Context;

import com.example.administrator.ewmarket.app.db.TourInformationDao;
import com.example.administrator.ewmarket.app.db.UserInformationDao;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class PublishTourInformation
{   private static TourInformationDao tourInformationDao;
    private static UserInformationDao userInformationDao;
    public static void publish(Context context,String currentUser, String tourname, String startplace, String price, String tour1content,
                               String tour2content, String detailcontent, int icon)
    {
       //1根据得到的资讯信息添加到资讯信息数据库
        tourInformationDao =TourInformationDao.getInstance(context);
        tourInformationDao.addPublishInfo(currentUser,tourname,startplace,price,tour1content,tour2content,detailcontent,icon);

    }
    //删除所有表中关于此资讯的信息
    public static void deletePublishinfo(Context context,String currentUser,String tourname)
    {
        tourInformationDao =TourInformationDao.getInstance(context);
        tourInformationDao.deletePublishInfo(currentUser,tourname);

        userInformationDao=UserInformationDao.getInstance(context);
        userInformationDao.deletePublishDiscussInfo(tourname);
        userInformationDao.deletePublishOrderGoodsInfo(tourname);
        userInformationDao.deletePublishUserRemarkInfo(tourname);
    }
}

package com.example.administrator.ewmarket.app.baseFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.utils.NetUtils;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

public class BaseItemFragment extends Fragment
{
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //Toast.makeText(getContext(),"Start",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private void loadData()
    {

        if (!NetUtils.isNetWorkAvailable(getContext().getApplicationContext()))
     {

         onPostLoadData();
     }
     else
     {
        onPreLoadData();
        startLoadData();
     }


    }
    //准备加载网络数据
    protected void onPreLoadData()
   {

   }
    //开始加载数据
    protected void startLoadData()
    {

    }
    //没有网络时候调用的方法
    protected void onPostLoadData()
    {
        Toast.makeText(getContext(),"网络不给力，请检查网络设置",Toast.LENGTH_SHORT).show();
    }
}

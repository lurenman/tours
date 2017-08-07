package com.example.administrator.ewmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import net.youmi.android.AdManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

public class LoadingActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //这块设置图片全屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);
        AdManager.getInstance(this).init("378e025888d2e434","502c3b9406d4ad4e",true);
        final Intent it=new Intent(this,MainActivity.class);
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run()
            {
                startActivity(it);
                //跳到主界面就给它结束，省了按返回键还里回到这个界面
                LoadingActivity.this.finish();
            }
        };
        timer.schedule(task,1000*2);

    }
}

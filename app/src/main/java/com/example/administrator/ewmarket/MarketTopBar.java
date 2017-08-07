package com.example.administrator.ewmarket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.Env;
import com.example.administrator.ewmarket.app.utils.SpUtils;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class MarketTopBar extends LinearLayout implements View.OnClickListener
{  private LinearLayout title_barsearch;
   private EditText edit_search;
    private ImageView iv_publish;
    private String currentUser;
    public MarketTopBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
       LayoutInflater.from(context).inflate(R.layout.market_topbar,this);
        title_barsearch=(LinearLayout)findViewById(R.id.title_barsearch);
        edit_search=(EditText)findViewById(R.id.edit_search);
        iv_publish=(ImageView)findViewById(R.id.iv_publish);
        iv_publish.setOnClickListener(this);
        edit_search.setOnClickListener(this);
        title_barsearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view==iv_publish)
        {
           checkUser();
        }else {
            Env.startActivity(getContext(),SearchActivity.class,null);
        }
    }
    private void checkUser()
    {
        currentUser= SpUtils.getString(getContext(), ConstantValues.CURRENT_USER, "");
        if (currentUser.equals("娜美"))
        {
            Env.startActivity(getContext(),PublishTourInformationActivity.class,null);
        }else if (currentUser.equals(""))
        {
            Toast.makeText(getContext(),"对不起请先注册登录才可以编辑个人信息",Toast.LENGTH_SHORT).show();
        }
        else  {
            Env.startActivity(getContext(),EditInfoActivity.class,null);
        }
    }
}

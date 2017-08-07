package com.example.administrator.ewmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ewmarket.app.View.RoundImageView;
import com.example.administrator.ewmarket.app.db.UserInformationDao;
import com.example.administrator.ewmarket.app.engine.TourDiscuss;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.Env;
import com.example.administrator.ewmarket.app.utils.SpUtils;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/1/9 0009.
 */

public class DiscussActivity extends Activity implements View.OnClickListener
{
    private ImageView iv_btn_back;
    private ListView lv_discuss;
    private TextView tv_discuss;
    private EditText et_discuss;
    private TextView tv_send;
    private String tourname;
    private String currentUser;
    private MyBaseAdapater mBaseAdapater;
    private UserInformationDao userDao;
    private LinearLayout ll_discuss;
    //是否订购过此资讯
    private Boolean isOrderGoods=false;
    private ArrayList<TourDiscuss> mDiscussContents;
    private final static String tag="DiscussActivity";
    private Handler mFindDiscussContentHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            tv_discuss.setText(String.format("热门评论（%d）",mDiscussContents.size()));
            mBaseAdapater=new MyBaseAdapater();
            lv_discuss.setAdapter(mBaseAdapater);

        }
    };
    private Handler mUpdateFindDiscussContentHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            tv_discuss.setText(String.format("热门评论（%d）",mDiscussContents.size()));
            if (mBaseAdapater!=null)
            {
                mBaseAdapater.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        mDiscussContents=new ArrayList<TourDiscuss>();
        currentUser=SpUtils.getString(getApplicationContext(), ConstantValues.CURRENT_USER,"");
        userDao=UserInformationDao.getInstance(getApplicationContext());
        initView();
        initData();
        isOrderGoods=userDao.findOrderGoods(currentUser,tourname);
        if (isOrderGoods==false)
        {
            //如果该资讯没有订购就不让那个评论的View显示
            ll_discuss.setVisibility(View.GONE);
        }

    }

    private void initView()
    {
        iv_btn_back=(ImageView) findViewById(R.id.iv_btn_back);
        ll_discuss=(LinearLayout)findViewById(R.id.ll_discuss);
        lv_discuss=(ListView) findViewById(R.id.lv_discuss);
        tv_discuss=(TextView)findViewById(R.id.tv_discuss);
        et_discuss=(EditText) findViewById(R.id.et_discuss);
        tv_send=(TextView) findViewById(R.id.tv_send);

        tv_send.setOnClickListener(this);
        iv_btn_back.setOnClickListener(this);

    }
    private void initData()
    {
        Intent intent = this.getIntent();
        Bundle bundle = null;
        if (intent != null && (bundle = intent.getExtras()) != null)
        {
            tourname=bundle.getString("tourname");
            mDiscussContents=(ArrayList<TourDiscuss>)userDao.findCurrentTourDiscussInfo(tourname);
            tv_discuss.setText(String.format("热门评论（%d）",mDiscussContents.size()));
            mBaseAdapater=new MyBaseAdapater();
            lv_discuss.setAdapter(mBaseAdapater);
            //findDiscussContent();
            // Toast.makeText(getApplicationContext(),Integer.toString(mDiscussContents.size()),Toast.LENGTH_LONG).show();
        }
    }
    private void updateData()
    {
        mDiscussContents=(ArrayList<TourDiscuss>)userDao.findCurrentTourDiscussInfo(tourname);
        tv_discuss.setText(String.format("热门评论（%d）",mDiscussContents.size()));
        if (mBaseAdapater!=null)
        {
            mBaseAdapater.notifyDataSetChanged();
        }
        //updateFindDiscussContent();
    }
    private void findDiscussContent()
    {
        new  Thread(){
            @Override
            public void run()
            {
                super.run();
                mDiscussContents=(ArrayList<TourDiscuss>)userDao.findCurrentTourDiscussInfo(tourname);
                mFindDiscussContentHandler.sendEmptyMessage(0);
            }
        }.start();
    }
    private void updateFindDiscussContent()
    {
        new Thread(){
            @Override
            public void run()
            {
                super.run();
                mDiscussContents=(ArrayList<TourDiscuss>)userDao.findCurrentTourDiscussInfo(tourname);
                mUpdateFindDiscussContentHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    public void onClick(View view)
    {
        if (view==iv_btn_back)
        {
            Intent intent =new Intent();
            intent.putExtra("discusscount",mDiscussContents.size());
            setResult(0,intent);
            finish();
        }
        if (view==tv_send)
        {



            //一切都在用户不为空时判断,这块也没必要判断了，要是这个界面显示说明有用户了。
            if (!currentUser.equals("")&&!et_discuss.getText().toString().trim().equals(""))
            {    //1.拿到你评论的内容et_discus上的内容
                String discussContent=et_discuss.getText().toString();
                //2.在数据库中插入你评论的内容
                userDao.addDiscuss(currentUser,userDao.findIcon(currentUser),tourname,discussContent, Env.getCurrentTime());
                et_discuss.setText("");
                Toast.makeText(getApplicationContext(),"评论成功",Toast.LENGTH_SHORT).show();
                //通知跟新UI及数据更新
                  updateData();
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent =new Intent();
            intent.putExtra("discusscount",mDiscussContents.size());
            setResult(0,intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyBaseAdapater extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return mDiscussContents.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mDiscussContents.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup)
        {
            ViewHolder holder= null;
            if (convertView==null)
            {
                holder=new ViewHolder();
                convertView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_discusslistviewitem_content,null,false);
                holder.ll_root=(RelativeLayout)convertView.findViewById(R.id.ll_root);
                holder.ri_icon=(RoundImageView)convertView.findViewById(R.id.ri_icon);
                holder.tv_username=(TextView)convertView.findViewById(R.id.tv_username);
                holder.tv_date=(TextView)convertView.findViewById(R.id.tv_date);
                holder.tv_discusscontent=(TextView)convertView.findViewById(R.id.tv_discusscontent);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            holder.ri_icon.setImageResource(mDiscussContents.get(position).getIcon());
            holder.tv_username.setText(mDiscussContents.get(position).getUsername());
            holder.tv_date.setText(mDiscussContents.get(position).getDiscussdate());
            holder.tv_discusscontent.setText(mDiscussContents.get(position).getDiscusscontent());

            return convertView;
        }
    }
    static class ViewHolder
   {
        RoundImageView ri_icon;
        TextView tv_username;
        TextView tv_date;
        TextView tv_discusscontent;
        RelativeLayout ll_root;
   }
}

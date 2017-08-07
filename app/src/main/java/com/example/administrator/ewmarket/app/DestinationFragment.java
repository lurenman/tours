package com.example.administrator.ewmarket.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.example.administrator.ewmarket.DetailsActivity;
import com.example.administrator.ewmarket.R;
import com.example.administrator.ewmarket.app.engine.TourInformation;
import com.example.administrator.ewmarket.app.db.TourInformationDao;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.Env;
import com.example.administrator.ewmarket.app.utils.ImageUtils;
import com.example.administrator.ewmarket.app.utils.MemoryCacheUtils;
import com.example.administrator.ewmarket.app.utils.UIUtils;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class DestinationFragment extends Fragment
{
    private View mLayoutView;
    private ListView lv_mylistview;
    // private List<TourInformation> TourInformations;
    private ArrayList<TourInformation> TourInformations;
    private XRefreshView rv_refreshView;
    private PublishBroadcastReceiver publishBroadcastReceiver;
    private DeletePublishBroadcastReceiver deletePublishBroadcastReceiver;
    private final static int typeDestinationFragment = 1;
    private String mSdFileAbsolutePath = UIUtils.getContext().getExternalFilesDir(null).getAbsolutePath();
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            lv_mylistview.setAdapter(new MyBaseAdapater());
        }
    };
    private MemoryCacheUtils mMemoryCache;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //注册发布成功广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantValues.PublishSuccess);
        publishBroadcastReceiver = new PublishBroadcastReceiver();
        getContext().registerReceiver(publishBroadcastReceiver, intentFilter);
        //注册删除发布成功广播
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(ConstantValues.DeletePublishSuccess);
        deletePublishBroadcastReceiver = new DeletePublishBroadcastReceiver();
        getContext().registerReceiver(deletePublishBroadcastReceiver, intentFilter1);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (mLayoutView == null)
        {
            mLayoutView = inflater.inflate(R.layout.fragment_app_destination, null);
            mMemoryCache = new MemoryCacheUtils(typeDestinationFragment);
            initView();
            initData();
        }
        return mLayoutView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (publishBroadcastReceiver != null)
        {
            getContext().unregisterReceiver(publishBroadcastReceiver);
        }
        if (deletePublishBroadcastReceiver != null)
        {
            getContext().unregisterReceiver(deletePublishBroadcastReceiver);
        }
    }

    private void initView()
    {
        lv_mylistview = (ListView) mLayoutView.findViewById(R.id.lv_mylistview);
        // 实例化广告条
        AdView adView = new AdView(getContext(), AdSize.FIT_SCREEN);

        // 获取要嵌入广告条的布局
        LinearLayout adLayout = (LinearLayout) mLayoutView.findViewById(R.id.ad_Layout);

        // 将广告条加入到布局中
        adLayout.addView(adView);
        rv_refreshView = (XRefreshView) mLayoutView.findViewById(R.id.rv_refreshView);
        rv_refreshView.setPullRefreshEnable(true);
        rv_refreshView.setPullLoadEnable(true);
        rv_refreshView.setAutoLoadMore(false);
        rv_refreshView.setXRefreshViewListener(new XRefreshView.XRefreshViewListener()
        {
            @Override
            public void onRefresh()
            {   //这块什么也没干就是让那个刷新自动三秒后关闭，觉得没必要真正的通知ListView刷新，所以先这么做
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        rv_refreshView.stopRefresh();
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore(boolean isSilence)
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        rv_refreshView.stopLoadMore();
                    }
                }, 2000);
            }

            @Override
            public void onRelease(float direction)
            {

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY)
            {

            }
        });
    }

    private void initData()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                TourInformationDao tourInformationDao = TourInformationDao.getInstance(getContext());
                TourInformations = (ArrayList<TourInformation>) tourInformationDao.findAll();
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    class MyBaseAdapater extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return TourInformations.size();
        }

        @Override
        public TourInformation getItem(int i)
        {
            return TourInformations.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup)
        {
            ViewHolder holder = null;
            if (convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_destinationlistviewitem_content, null, false);
                holder.ll_tourinfo = (LinearLayout) convertView.findViewById(R.id.ll_tourinfo);
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_tourname = (TextView) convertView.findViewById(R.id.tv_tourname);
                holder.tv_startplace = (TextView) convertView.findViewById(R.id.tv_startplace);
                holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
                holder.tv_tourcontent1 = (TextView) convertView.findViewById(R.id.tv_tourcontent1);
                holder.tv_tourcontent2 = (TextView) convertView.findViewById(R.id.tv_tourcontent2);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ll_tourinfo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("icon", TourInformations.get(position).getIcon());
                    bundle.putString("tourname", TourInformations.get(position).getTourname());
                    bundle.putString("startplace", TourInformations.get(position).getStartplace());
                    bundle.putString("price", TourInformations.get(position).getPrice());
                    bundle.putString("tour1content", TourInformations.get(position).getTour1content());
                    bundle.putString("tour2content", TourInformations.get(position).getTour2content());
                    bundle.putInt("upremark", TourInformations.get(position).getUpremark());
                    bundle.putInt("downremark", TourInformations.get(position).getDownremark());
                    bundle.putString("detailcontent", TourInformations.get(position).getDetailcontent());
                    bundle.putString("costcontent", TourInformations.get(position).getCostcontent());
                    Env.startActivity(getContext(), DetailsActivity.class, bundle);
                }
            });
            Bitmap bitmap = ImageUtils.intToBitmap(UIUtils.getContext(), getItem(position).getIcon());
            if (bitmap == null)
            {
                //重sd卡中读取文件bitmap
                int icon = getItem(position).getIcon();
                bitmap = BitmapFactory.decodeFile(mSdFileAbsolutePath + "/Picture" + "/" + String.valueOf(icon) + ".png");
            }
            //LruCache内存优化
            if (mMemoryCache != null)
            {
                if (bitmap != null)
                {
                    mMemoryCache.setMemoryCache(getItem(position).getTourname(), bitmap);
                    holder.iv_icon.setImageBitmap(mMemoryCache.getMemoryCache(getItem(position).getTourname()));
                }
                else
                {
                    //如果在lru缓存和sd卡内存中都没有找到就给设置一个默认的背景
                    Bitmap bitmap1 = ImageUtils.intToBitmap(UIUtils.getContext(),R.mipmap.banner1);
                    mMemoryCache.setMemoryCache(getItem(position).getTourname(), bitmap1);
                    holder.iv_icon.setImageBitmap(mMemoryCache.getMemoryCache(getItem(position).getTourname()));
                }

            }
            // holder.iv_icon.setBackgroundResource(TourInformations.get(position).getIcon());
            holder.tv_tourname.setText(TourInformations.get(position).getTourname());
            holder.tv_startplace.setText("跟团游|" + TourInformations.get(position).getStartplace());
            holder.tv_price.setText(TourInformations.get(position).getPrice());
            holder.tv_tourcontent1.setText(TourInformations.get(position).getTour1content());
            holder.tv_tourcontent2.setText(TourInformations.get(position).getTour2content());


            return convertView;
        }
    }

    //    private View.OnClickListener mClickListener=new View.OnClickListener() {
    //        @Override
    //        public void onClick(View view)
    //        {
    //            Env.startActivity(getContext(), LoginActivity.class,null);
    //        }
    //    };
    static class ViewHolder
    {
        ImageView iv_icon;
        TextView tv_tourname;
        TextView tv_startplace;
        TextView tv_price;
        TextView tv_tourcontent1;
        TextView tv_tourcontent2;
        LinearLayout ll_tourinfo;
    }

    class PublishBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        { //跟新ui
            initData();
        }
    }

    class DeletePublishBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initData();
        }
    }
}

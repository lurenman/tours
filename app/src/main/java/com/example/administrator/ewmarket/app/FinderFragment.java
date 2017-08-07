package com.example.administrator.ewmarket.app;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ewmarket.R;
import com.example.administrator.ewmarket.app.View.BannerLayout;
import com.example.administrator.ewmarket.app.View.LoadingLayoutLoader;
import com.example.administrator.ewmarket.app.View.fly.ShakeListener;
import com.example.administrator.ewmarket.app.View.fly.StellarMap;
import com.example.administrator.ewmarket.app.utils.ConstantValues;
import com.example.administrator.ewmarket.app.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class FinderFragment extends Fragment
{
    private View mLayoutView;
    private BannerLayout bannerLayout;
    private List<Integer> mBanberIcons;
    private LoadingLayoutLoader mLoadingLayoutLoader;
    private boolean mDataArrived=false;
    private StellarMap stellar;
    private  String data;
    private  ArrayList<String> listData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (mLayoutView==null)
    {
        mLayoutView=inflater.inflate(R.layout.fragment_app_finder,null);
        initView();
        data= ConstantValues.finderJsonData;
        try
        {
            JSONArray ja=new JSONArray(data);
            listData = new ArrayList<String>();
            for (int i = 0; i < ja.length(); i++) {
                String keyword = ja.getString(i);
                listData.add(keyword);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        initData();


    }
        return mLayoutView;
       // return super.onCreateView(inflater, container, savedInstanceState);
    }
    private void initView()
    {
        mLoadingLayoutLoader=new LoadingLayoutLoader(mLayoutView);
        bannerLayout=(BannerLayout) mLayoutView.findViewById(R.id.banner_layout);
        stellar= (StellarMap) mLayoutView.findViewById(R.id.mStellarMap);
    }
    private void initData()
    {
        mBanberIcons=new ArrayList<Integer>();
        mBanberIcons.add(0,R.mipmap.banner1);
        mBanberIcons.add(1,R.mipmap.banner3);
        mBanberIcons.add(2,R.mipmap.banner4);
        if (mBanberIcons.size()==3)
       {
           new Handler().postDelayed(new Runnable()
       {
           @Override
           public void run()
           {
               mDataArrived=true;
               mLoadingLayoutLoader.closed();
           }
       }, 200);

       }
        bannerLayout.setViewRes(mBanberIcons);
        stellar.setAdapter(new RecommendAdapter());
        // 随机方式, 将控件划分为8行6列的的格子, 然后在格子中随机展示
        stellar.setRegularity(6, 8);

        // 设置内边距10dp
        int padding = UIUtils.dip2px(10);
        stellar.setInnerPadding(padding, padding, padding, padding);

        // 设置默认页面, 第一组数据
        stellar.setGroup(0, true);

        ShakeListener shake = new ShakeListener(UIUtils.getContext());
        final StellarMap finalStellar = stellar;
        shake.setOnShakeListener(new ShakeListener.OnShakeListener() {

            @Override
            public void onShake() {
                finalStellar.zoomIn();// 跳到下一页数据
            }
        });


    }

    class RecommendAdapter implements StellarMap.Adapter {

        // 返回组的个数
        @Override
        public int getGroupCount(){
            return 3;
        }

        // 返回某组的item个数
        @Override
        public int getCount(int group) {
            int count = listData.size() / getGroupCount();
            if (group == getGroupCount() - 1) {
                // 最后一页, 将除不尽,余下来的数量追加在最后一页, 保证数据完整不丢失
                count += listData.size() % getGroupCount();
            }

            return count;
        }

        // 初始化布局
        @Override
        public View getView(int group, int position, View convertView) {
            // 因为position每组都会从0开始计数, 所以需要将前面几组数据的个数加起来,才能确定当前组获取数据的角标位置
            position += (group) * getCount(group - 1);

            // System.out.println("pos:" + position);

          //  final String keyword = data.get(position);
            final String keyword = listData.get(position);

            TextView view = new TextView(UIUtils.getContext());
            view.setText(keyword);

            Random random = new Random();
            // 随机大小
            int size = 14 + random.nextInt(10);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

            // 随机颜色
            // r g b, 0-255 -> 30-230, 颜色值不能太小或太大, 从而避免整体颜色过亮或者过暗
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);

            view.setTextColor(Color.rgb(r, g, b));

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), keyword,
                            Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }

        // 返回下一组的id
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            System.out.println("isZoomIn:" + isZoomIn);
            if (isZoomIn) {
                // 往下滑加载上一页
                if (group > 0) {
                    group--;
                } else {
                    // 跳到最后一页
                    group = getGroupCount() - 1;
                }
            } else {
                // 往上滑加载下一页
                if (group < getGroupCount() - 1) {
                    group++;
                } else {
                    // 跳到第一页
                    group = 0;
                }
            }
            return group;
        }

    }
}

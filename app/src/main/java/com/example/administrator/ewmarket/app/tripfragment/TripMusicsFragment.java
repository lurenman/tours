package com.example.administrator.ewmarket.app.tripfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ewmarket.R;
import com.example.administrator.ewmarket.app.View.ParallaxListView;
import com.example.administrator.ewmarket.app.utils.UIUtils;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class TripMusicsFragment extends Fragment
{
    private View mLayoutView;
    private ParallaxListView mlistview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //return super.onCreateView(inflater, container, savedInstanceState);
        if (mLayoutView==null)
        {
            mLayoutView=inflater.inflate(R.layout.fragment_trip_musics,null);
            initView();
        }
        return mLayoutView;
    }

    private void initView()
    {
        mlistview = (ParallaxListView)mLayoutView.findViewById(R.id.mlistview);

        mlistview.setOverScrollMode(ListView.OVER_SCROLL_NEVER);//永远不显示蓝色阴影
        //添加header
        View headerView = View.inflate(UIUtils.getContext(),R.layout.layout_music_header, null);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.imageView);
        mlistview.setParallaxImageView(imageView);
        mlistview.addHeaderView(headerView);
        mlistview.setAdapter(new MyBaseAdapater() );



    }
    class MyBaseAdapater extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return 10;
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup)
        {
            ViewHolder holder=null;
            if (convertView==null)
            {
                holder=new ViewHolder();
                convertView=View.inflate(UIUtils.getContext(),R.layout.fragment_musiclistviewitem_content,null);
                holder.tv_music= (TextView) convertView.findViewById(R.id.tv_music);
                convertView.setTag(holder);
            }
            else {
                holder= (ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }
    static class ViewHolder
    {
        TextView tv_music;
    }
}

package com.example.administrator.ewmarket.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.administrator.ewmarket.R;
import com.example.administrator.ewmarket.app.tripfragment.TripBooksFragment;
import com.example.administrator.ewmarket.app.tripfragment.TripMoviesFragment;
import com.example.administrator.ewmarket.app.tripfragment.TripMusicsFragment;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class TripFragment extends Fragment
{
    private View mLayoutView;
    private RadioGroup rg_group;
    private RadioButton rb_movies;
    private RadioButton rb_musics;
    private RadioButton rb_books;
    private LinearLayout mTabLine;//那个下面的线
    private int screenWidth;// 屏幕的宽度
    private ViewPager myViewPager;
    private TripMoviesFragment mMoviesFragment=new TripMoviesFragment();
    private TripMusicsFragment mMusicsFragment=new TripMusicsFragment();
    private TripBooksFragment  mBooskFragment=new TripBooksFragment();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (mLayoutView==null)
        {
            mLayoutView=inflater.inflate(R.layout.fragment_app_trip,null);
            myViewPager= (ViewPager) mLayoutView.findViewById(R.id.myviewpager);
            myViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
            myViewPager.setOnPageChangeListener(new PageChangeListener());
            myViewPager.setOffscreenPageLimit(3);
            rg_group= (RadioGroup)mLayoutView.findViewById(R.id.rg_group);
            rb_movies=(RadioButton)mLayoutView.findViewById(R.id.rb_movies);
            rb_musics=(RadioButton)mLayoutView.findViewById(R.id.rb_musics);
            rb_books=(RadioButton)mLayoutView.findViewById(R.id.rb_books);
            rg_group.setOnCheckedChangeListener(new CheckedChangeListener());
            rg_group.check(R.id.rb_movies);
            mTabLine=(LinearLayout) mLayoutView.findViewById(R.id.tab_line);
            //获取屏幕的宽度
            DisplayMetrics outMetrics=new DisplayMetrics();
            getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            screenWidth=outMetrics.widthPixels;
            //设置mTabLine宽度//获取控件的(注意：一定要用父控件的LayoutParams写LinearLayout.LayoutParams)
            LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams) mTabLine.getLayoutParams();//获取控件的布局参数对象
            lp.width=screenWidth/3;
            mTabLine.setLayoutParams(lp); //设置该控件的layoutParams参数
        }
        return mLayoutView;
       // return super.onCreateView(inflater, container, savedInstanceState);
    }
    private class CheckedChangeListener implements RadioGroup.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            switch (checkedId)
            {
                case R.id.rb_movies:
                    myViewPager.setCurrentItem(0);
                    break;
                case R.id.rb_musics:
                    myViewPager.setCurrentItem(1);
                    break;
                case R.id.rb_books:
                    myViewPager.setCurrentItem(2);
                    break;
            }
        }
    }
    private class PageChangeListener implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams) mTabLine.getLayoutParams();
            //获取组件距离左侧组件的距离
            lp.leftMargin=(int) ((positionOffset+position)*screenWidth/3);
            mTabLine.setLayoutParams(lp);
        }
        @Override
        public void onPageSelected(int position)
        {
            switch (position) {
                case 0:
                    rg_group.check(R.id.rb_movies);
                    break;
                case 1:
                    rg_group.check(R.id.rb_musics);
                    break;
                case 2:
                    rg_group.check(R.id.rb_books);
                    break;

            }
        }
        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }
    private class MyPagerAdapter extends FragmentPagerAdapter
    {
        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position)
        {
            if (position==0)
            {
                return mMoviesFragment;
            }
            if (position==1)
            {
                return mMusicsFragment;
            }
            if (position==2)
            {
                return mBooskFragment;
            }
            else
                return null;
        }

        @Override
        public int getCount()
        {
            return 3;
        }
    }
}

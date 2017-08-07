package com.example.administrator.ewmarket.app.tripfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.ewmarket.R;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class TripMoviesFragment extends Fragment
{
    private View mLayoutView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //return super.onCreateView(inflater, container, savedInstanceState);
        if (mLayoutView==null)
        {
            mLayoutView=inflater.inflate(R.layout.fragment_trip_movies,null);

        }
        return mLayoutView;
    }
}

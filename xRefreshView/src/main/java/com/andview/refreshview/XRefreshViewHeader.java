package com.andview.refreshview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.callback.IHeaderCallBack;
import com.andview.refreshview.utils.Utils;

import java.util.Calendar;

public class XRefreshViewHeader extends LinearLayout implements IHeaderCallBack
{
    private ViewGroup mContent;
    private ImageView mArrowImageView;
    private ImageView mOkImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    private TextView mHeaderTimeTextView;

    public XRefreshViewHeader(Context context)
    {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XRefreshViewHeader(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context)
    {
        mContent = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.xrefreshview_header, this);
        mArrowImageView = (ImageView) findViewById(R.id.xrefreshview_header_arrow);
        mOkImageView = (ImageView) findViewById(R.id.xrefreshview_header_ok);
        mHintTextView = (TextView) findViewById(R.id.xrefreshview_header_hint_textview);
        mHeaderTimeTextView = (TextView) findViewById(R.id.xrefreshview_header_time);
        mProgressBar = (ProgressBar) findViewById(R.id.xrefreshview_header_progressbar);
    }

    public void setRefreshTime(long lastRefreshTime)
    {
        // 获取当前时间
        Calendar mCalendar = Calendar.getInstance();
        long refreshTime = mCalendar.getTimeInMillis();
        long howLong = refreshTime - lastRefreshTime;
        int minutes = (int) (howLong / 1000 / 60);
        String refreshTimeText = null;
        Resources resources = getContext().getResources();
        if (minutes < 1)
        {
            refreshTimeText = resources.getString(R.string.xrefreshview_refresh_justnow);
        }
        else if (minutes < 60)
        {
            refreshTimeText = resources.getString(R.string.xrefreshview_refresh_minutes_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes);
        }
        else if (minutes < 60 * 24)
        {
            refreshTimeText = resources.getString(R.string.xrefreshview_refresh_hours_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes / 60);
        }
        else
        {
            refreshTimeText = resources.getString(R.string.xrefreshview_refresh_days_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes / 60 / 24);
        }
        mHeaderTimeTextView.setText(refreshTimeText);
    }

    /**
     * hide footer when disable pull refresh
     */
    public void hide()
    {
        setVisibility(View.GONE);
    }

    public void show()
    {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateNormal()
    {
        mProgressBar.setVisibility(View.GONE);
        mArrowImageView.setVisibility(View.VISIBLE);
        mOkImageView.setVisibility(View.GONE);
        mArrowImageView.setAlpha(128);
        mHintTextView.setText(R.string.xrefreshview_header_hint_normal);
    }

    @Override
    public void onStateReady()
    {
        mProgressBar.setVisibility(View.GONE);
        mOkImageView.setVisibility(View.GONE);
        mArrowImageView.setVisibility(View.VISIBLE);
        mArrowImageView.clearAnimation();
        mArrowImageView.setAlpha(255);
        mHintTextView.setText(R.string.xrefreshview_header_hint_ready);
       // mHeaderTimeTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateRefreshing()
    {
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(View.GONE);
        mOkImageView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mHintTextView.setText(R.string.xrefreshview_header_hint_loading);
    }

    @Override
    public void onStateFinish(boolean success)
    {
        mArrowImageView.setVisibility(View.GONE);
        mOkImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mHintTextView.setText(success ? R.string.xrefreshview_header_hint_loaded : R.string.xrefreshview_header_hint_loaded_fail);
       // mHeaderTimeTextView.setVisibility(View.GONE);
    }

    @Override
    public void onHeaderMove(double headerMovePercent, int offsetY, int deltaY)
    {
        mArrowImageView.setPivotX(mArrowImageView.getWidth()/2);
        mArrowImageView.setPivotY(mArrowImageView.getHeight()/2);//支点在图片中心
        mArrowImageView.setRotation(offsetY);
    }

    @Override
    public int getHeaderHeight()
    {
        return getMeasuredHeight();
    }
}

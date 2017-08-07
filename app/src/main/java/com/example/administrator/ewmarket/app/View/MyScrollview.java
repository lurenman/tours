package com.example.administrator.ewmarket.app.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollview extends ScrollView
{
	private float FistXLocation;
	private float FistYlocation;
	private boolean Istrigger = false;
	private final int TRIGER_LENTH = 50;
	private final int HORIZOTAL_LENTH = 50;
    private ScrollViewListener scrollViewListener = null;
	public MyScrollview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}



    @Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		int deltaX = 0;
		int deltaY = 0;

		final float x = ev.getX();
		final float y = ev.getY();

		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			deltaX = (int) (FistXLocation - x);
			deltaY = (int) (FistYlocation - y);
			if (Math.abs(deltaY) > TRIGER_LENTH
					&& Math.abs(deltaX) < HORIZOTAL_LENTH) {

				Istrigger = true;
				return super.onInterceptTouchEvent(ev);

			}

			return false;

		case MotionEvent.ACTION_DOWN:
			FistXLocation = x;
			FistYlocation = y;

			requestDisallowInterceptTouchEvent(false);
			return super.onInterceptTouchEvent(ev);

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (Istrigger) {

				Istrigger = false;
				return super.onInterceptTouchEvent(ev);
			}

			break;
		}
		return super.onInterceptTouchEvent(ev);

	}
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            // 在这里将方法暴露出去
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
    // 是否要其弹性滑动
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        // 弹性滑动关键则是maxOverScrollX， 以及maxOverScrollY，
        // 一般默认值都是0，需要弹性时，更改其值即可
        // 即就是，为零则不会发生弹性，不为零（>0,负数未测试）则会滑动到其值的位置
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, 0, 0, isTouchEvent);
    }
    // 接口
    public interface ScrollViewListener {

        void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy);

    }

    public void setScrollViewListener(ScrollViewListener listener) {
        scrollViewListener = listener;
    }

}

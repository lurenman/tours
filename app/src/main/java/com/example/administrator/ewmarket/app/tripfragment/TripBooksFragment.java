package com.example.administrator.ewmarket.app.tripfragment;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.andview.refreshview.XRefreshView;
import com.example.administrator.ewmarket.R;
import com.example.administrator.ewmarket.WebViewActivity;
import com.example.administrator.ewmarket.app.HomeFragment;
import com.example.administrator.ewmarket.app.View.LoadingLayoutLoader;
import com.example.administrator.ewmarket.app.baseFragment.BaseItemFragment;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class TripBooksFragment extends BaseItemFragment
{   private View mLayoutView;
    private WebView mWebView;
    private LoadingLayoutLoader mLoadingLayoutLoader;
    final String URL="http://sj.imengxiang.cn/?cm=dmtg";
    public static final int WEBTYPE_BOOKS = 1;
    private XRefreshView rv_refreshView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {if (mLayoutView==null)
    {
        mLayoutView=inflater.inflate(R.layout.fragment_trip_books,null,false);
        mWebView = (WebView) mLayoutView.findViewById(R.id.wv_webview);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mLoadingLayoutLoader=new LoadingLayoutLoader(mLayoutView);
        rv_refreshView = (XRefreshView) mLayoutView.findViewById(R.id.rv_refreshView);
        rv_refreshView.setPullRefreshEnable(true);
        rv_refreshView.setPullLoadEnable(true);
        rv_refreshView.setAutoLoadMore(false);
        rv_refreshView.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh()
            {
                startLoadData();
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
        return mLayoutView;
    }

    @Override
    protected void startLoadData()
    {
        super.startLoadData();
        mWebView.loadUrl(URL);
        mWebView.setWebViewClient(new DetailWebViewClient());
    }
    class DetailWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            WebViewActivity.StartActivity(getContext(), url, WEBTYPE_BOOKS);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            mLoadingLayoutLoader.closed();
            rv_refreshView.stopRefresh();
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
        {   //这是请求网络数据失败时候就把刷新关掉
            rv_refreshView.stopRefresh();
            mLoadingLayoutLoader.closed();
            handler.proceed();
        }
    }
}

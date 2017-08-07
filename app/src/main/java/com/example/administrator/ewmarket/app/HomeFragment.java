package com.example.administrator.ewmarket.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.andview.refreshview.XRefreshView;
import com.example.administrator.ewmarket.R;
import com.example.administrator.ewmarket.app.View.LoadingLayoutLoader;
import com.example.administrator.ewmarket.WebViewActivity;
import com.example.administrator.ewmarket.app.baseFragment.BaseItemFragment;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class HomeFragment extends BaseItemFragment
{
    private View mLayoutView;
    private WebView mWebView;
    private LoadingLayoutLoader mLoadingLayoutLoader;
    final String URL="https://m.tuniu.com/#form_http";
    public static final int WEBTYPE_TUNIU = 1;
    private XRefreshView rv_refreshView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (mLayoutView==null)
        {
            mLayoutView=inflater.inflate(R.layout.fragment_app_home,null,false);
            mWebView = (WebView) mLayoutView.findViewById(R.id.wv_webview);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        /*     //启用数据库

             webSettings.setDatabaseEnabled(true);

             String dir = getContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
             //启用地理定位

             webSettings.setGeolocationEnabled(true);
            ///设置定位的数据库路径

             webSettings.setGeolocationDatabasePath(dir);*/

             webSettings.setDomStorageEnabled(true);

           mWebView.setWebChromeClient(new mDetailWebChromeClient());


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
        //return super.onCreateView(inflater, container, savedInstanceState);
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
            WebViewActivity.StartActivity(getContext(), url, WEBTYPE_TUNIU);
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
    class mDetailWebChromeClient extends WebChromeClient
    {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback)
        {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }
}

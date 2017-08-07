package com.example.administrator.ewmarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.ewmarket.app.View.LoadingLayoutLoader;
import com.example.administrator.ewmarket.app.utils.Env;

/**
 * Created by Administrator on 2016/12/23 0023.
 */

public class WebViewActivity extends Activity
{
    private String mUrl;
    private WebView mWebView;
    //改变webView字体的大小
    private WebSettings settings;
    //根据传进来不同的webView类型设置字体大小
    private int mWebType;
    private TextView mTitle;
    private ImageView mBack;
    private LoadingLayoutLoader mLoaddingLayout;
    public static void StartActivity(Context context, String url, int webType)
    {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putInt("webType", webType);
        Env.startActivity(context, WebViewActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initVariables();

        initViews();

        initLoadingLayout();

        loadData();
    }
    private void initVariables()
    {
        Intent i = getIntent();
        Bundle b = null;
        if (i != null && (b = i.getExtras()) != null)
        {
            mUrl = b.getString("url");
            mWebType = b.getInt("webType");
        }
    }
    private void  initViews()
    {
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = (ImageView) findViewById(R.id.btn_back);
        mWebView = (WebView) findViewById(R.id.wv_webview);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
       // mWebView.getSettings().setJavaScriptEnabled(true);
        settings = mWebView.getSettings();
        settings.setSupportZoom(true);
        if (mWebType == 0)
        {
            settings.setTextSize(WebSettings.TextSize.SMALLER);
        }
        else if (mWebType == 1)
        {
            settings.setTextSize(WebSettings.TextSize.NORMAL);
        }
        else
        {
            settings.setTextSize(WebSettings.TextSize.NORMAL);
        }
        settings.setJavaScriptEnabled(true);
        //启用数据库

       /* settings.setDatabaseEnabled(true);

        String dir =getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位

        settings.setGeolocationEnabled(true);
        ///设置定位的数据库路径

        settings.setGeolocationDatabasePath(dir);*/

        settings.setDomStorageEnabled(true);

        mWebView.setWebChromeClient(new DetailWebChromeClient());
        //防止出现anr错误的
        mBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mWebView.stopLoading();
                mWebView.pauseTimers();
                mWebView.destroyDrawingCache();
                mWebView = null;
                finish();

            }
        });
    }
    private void initLoadingLayout()
    {
        mLoaddingLayout = new LoadingLayoutLoader(this);
    }
    private void  loadData()
    {   mLoaddingLayout.onLoading();
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new DetailWebViewClient());
        mWebView.setWebChromeClient(new DetailWebChromeClient());
    }
    class DetailWebChromeClient extends WebChromeClient
    {
        @Override
        public void onReceivedTitle(WebView view, String title)
        {
            super.onReceivedTitle(view, title);
            mTitle.setText(title);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback)
        {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }

    class DetailWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            mWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            mLoaddingLayout.onLoading();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            mLoaddingLayout.closed();
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
        {
            mLoaddingLayout.closed();
            handler.proceed();
        }
    }
    @Override
    public void onBackPressed()
    {
        if (mWebView.canGoBack())
        {
            mWebView.goBack();
        }
        else
        {
            mWebView.stopLoading();
            mWebView.pauseTimers();
            mWebView.destroyDrawingCache();
            mWebView = null;
            finish();
        }
    }

}

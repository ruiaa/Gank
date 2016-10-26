package com.ruiaa.gank.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ruiaa.gank.R;
import com.ruiaa.gank.ui.base.ToolbarActivity;
import com.ruiaa.gank.util.ResUtil;
import com.ruiaa.gank.util.ToastUtil;
import com.ruiaa.gank.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebActivity extends ToolbarActivity {

    private static final String ARG_URL = "arg_url";

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.web_progress_bar)
    ProgressBar progressBar;

    private String url;

    public static Intent newIntent(Context context, @NonNull String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(ARG_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra(ARG_URL);

        initWebView();
    }


    private void initWebView() {

        //设置
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//等宽显示
        settings.setSupportZoom(true);//缩放

        //UI事件
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setProgress(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }
        });

        //内容呈现事件
        webView.setWebViewClient(getWebViewClient());

        webView.loadUrl(url);
    }

    public static WebViewClient getWebViewClient() {
        if (Build.VERSION.SDK_INT >= 21) {
            return new WebViewClient() {
                @TargetApi(21)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }
            };
        } else {
            return new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            };
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_web_refresh:
                refresh();
                return true;
            case R.id.menu_web_copy_url:
                Util.copyToClipBoard(this, url, ResUtil.getString(R.string.tip_copy_url_ok));
                return true;
            case R.id.menu_web_open_url:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(url);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    ToastUtil.showLong(R.string.tip_open_url_fail);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void refresh() {
        if (webView != null) {
            webView.reload();
        }
    }

    @Override
    protected void onPause() {
        if (webView != null) {
            webView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (webView != null) {
            webView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected boolean canTurnBack() {
        return true;
    }
}

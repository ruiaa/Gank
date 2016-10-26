package com.ruiaa.gank.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ruiaa.gank.BuildConfig;
import com.ruiaa.gank.R;
import com.ruiaa.gank.ui.base.BaseActivity;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ruiaa on 2016/10/23.
 */

public class VideoActivity extends BaseActivity {

    private static final String ARG_VIDEO_URL = "video_url";

    @BindView(R.id.activity_video_web)
    WebView webView;

    private String videoUrl;

    public static Intent newIntent(Context context, String videoUrl) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(ARG_VIDEO_URL, videoUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //旋转屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        videoUrl = getIntent().getStringExtra(ARG_VIDEO_URL);
        init();
    }

    void init() {
        webView.setWebViewClient(new LoveClient());
        webView.setWebChromeClient(new Chrome());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (BuildConfig.DEBUG) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        webView.loadUrl(videoUrl);
    }

    private class LoveClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 这些视频需要hack CSS才能达到全屏播放的效果
            if (url.contains("www.vmovier.com")) {
                injectCSS("vmovier.css");
            } else if (url.contains("video.weibo.com")) {
                injectCSS("weibo.css");
            } else if (url.contains("m.miaopai.com")) {
                injectCSS("miaopai.css");
            }
        }
    }

    // Inject CSS method: read style.css from assets folder
    // Append stylesheet to document head
    private void injectCSS(String filename) {
        try {
            InputStream inputStream = this.getAssets().open(filename);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            webView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Chrome extends WebChromeClient implements MediaPlayer.OnCompletionListener {

        @Override public void onCompletion(MediaPlayer player) {
            if (player != null) {
                if (player.isPlaying()) player.stop();
                player.reset();
                player.release();
            }
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
}

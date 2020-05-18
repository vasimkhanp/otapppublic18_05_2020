package com.otapp.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterTermsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tvBack)
    public TextView tvBack;
    @BindView(R.id.wvProcess)
    public WebView wvProcess;

    private String title = "", mUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_register_terms);
        ButterKnife.bind(this);

        InitializeControls();

    }

    private void InitializeControls() {

        wvProcess.setWebChromeClient(new MyWebChromeClient());
        wvProcess.setWebViewClient(new MyWebViewClient());
        wvProcess.getSettings().setDomStorageEnabled(true);
        wvProcess.getSettings().setJavaScriptEnabled(true);
        wvProcess.getSettings().setAppCacheEnabled(true);
        wvProcess.getSettings().setLoadsImagesAutomatically(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString(Constants.BNDL_TITLE);
            mUrl = bundle.getString(Constants.BNDL_URL);
            if (!TextUtils.isEmpty(mUrl)) {
                wvProcess.loadUrl(mUrl);
            }
        }

        tvBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == tvBack){
            finish();
        }
    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int progress) {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        }
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Utils.showProgressDialog(getActivity());
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Utils.closeProgressDialog();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.e("", "shouldOverrideUrlLoading url::" + url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
        }
    }

    private Context getActivity() {
        return RegisterTermsActivity.this;
    }
}

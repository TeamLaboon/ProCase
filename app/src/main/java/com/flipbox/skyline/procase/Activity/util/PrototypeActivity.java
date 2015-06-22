package com.flipbox.skyline.procase.Activity.util;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.flipbox.skyline.procase.Activity.ProjectListDev;
import com.flipbox.skyline.procase.R;

public class PrototypeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype);

        Bundle intent = getIntent().getExtras();

        String prototype = intent.getString(ProjectListDev.EXTRA_MESSAGE_PROTOTYPE);
        String title = intent.getString(ProjectListDev.EXTRA_MESSAGE_NAME);
        setTitle(title);
        WebView myWebView = (WebView)findViewById(R.id.webView);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.loadUrl(prototype);
        myWebView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            webView.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url)
        {
            // Obvious next step is: document.forms[0].submit()
            view.loadUrl("javascript:document.forms[0].q.value='[android]'");
        }
    }


}

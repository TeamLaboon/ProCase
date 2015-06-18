package com.flipbox.skyline.procase.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.flipbox.skyline.procase.R;


public class ProjectDescription extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_description);
        Bundle intent = getIntent().getExtras();
        //String name = intent.getString(ProjectList.EXTRA_MESSAGE_NAME);
        String prototype = intent.getString(ProjectListDev.EXTRA_MESSAGE_PROTOTYPE);
      //  TextView textName = (TextView)findViewById(R.id.textView6);
      //  TextView textDesc = (TextView)findViewById(R.id.textView7);
      //  textName.setText(name);
     //   textDesc.setText(desc);
        WebView myWebView = (WebView)findViewById(R.id.webView);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
      //  WebViewClientImpl webViewClient = new WebViewClientImpl(this);
       // webView.setWebViewClient(webViewClient);
       // MyWebViewClient webViewClient = new MyWebViewClient(this);

        myWebView.loadUrl(prototype);
        myWebView.setWebViewClient(new MyWebViewClient());
      //  myWebView.setWebViewClient(new WebViewClient());
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}

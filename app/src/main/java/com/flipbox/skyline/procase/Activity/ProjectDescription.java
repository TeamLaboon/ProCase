package com.flipbox.skyline.procase.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.flipbox.skyline.procase.R;


public class ProjectDescription extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

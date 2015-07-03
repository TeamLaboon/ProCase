package com.flipbox.skyline.procase.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.flipbox.skyline.procase.R;


public class ProjectDescription extends Activity {
    private ProgressDialog pDialog;             // Progress dialog
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog= new ProgressDialog(ProjectDescription.this);
        showpDialog("Please wait...");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_project_description);
        Bundle intent = getIntent().getExtras();
        String prototype = intent.getString(ProjectListDev.EXTRA_MESSAGE_PROTOTYPE);
      //  TextView textName = (TextView)findViewById(R.id.textView6);
      //  TextView textDesc = (TextView)findViewById(R.id.textView7);
      //  textName.setText(name);
     //   textDesc.setText(desc);
        myWebView = (WebView)findViewById(R.id.webView);

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
        public void onLoadResource(WebView view, String url) {
            if(myWebView.getProgress()>=55)
                hidepDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            // Obvious next step is: document.forms[0].submit()
            view.loadUrl("javascript:document.forms[0].q.value='[android]'");
        }
    }

    private void showpDialog(String message){
        if(!pDialog.isShowing()) {
            pDialog.setMessage(message);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }
    private void hidepDialog(){
        if(pDialog.isShowing())
            pDialog.dismiss();
    }
}

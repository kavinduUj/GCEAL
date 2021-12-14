package com.appflavorsz.gceal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.appflavorsz.gceal.R;

public class LoadUrl extends AppCompatActivity {

    public static TextView tt;
    private ImageView b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_url);

        Intent i = getIntent();
        String tit = i.getStringExtra("title");
        String getPri = i.getStringExtra("htmlUrl");

        tt = (TextView)findViewById(R.id.urlTtle);
        b = (ImageView)findViewById(R.id.urlBack);
        tt.setText(tit);
        b.setOnClickListener((View vv)->{
            finish();
        });

        WebView mWebView = null;
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(getPri);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
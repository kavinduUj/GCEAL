package com.appflavorsz.gceal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.appflavorsz.gceal.Config;
import com.appflavorsz.gceal.R;
import com.appflavorsz.gceal.service.other.Waiting;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Webview extends AppCompatActivity {

    PDFView pdfView;
    private  boolean loading = false;
    private Waiting waiting;
    InputStream inputStream = null;
    private AdView adView;
    ImageView more;

    private boolean isConnection;
    int cntt;
    String answerPdf;
    String getTT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_webview);

        waiting = new Waiting(Webview.this);
        ImageView secondback = (ImageView)findViewById(R.id.pdfBack);
        TextView getT = (TextView)findViewById(R.id.getTitlePdf);
        more = findViewById(R.id.more);


        pdfView = (PDFView) findViewById(R.id.loadPdf);
        Intent intent = getIntent();
        String imageUrl0 = intent.getStringExtra("pdfUrl");
        getTT = intent.getStringExtra("getTitle");
        answerPdf = intent.getStringExtra("getAnswerPDF");

        getT.setText(getTT);
        new retPdf().execute(imageUrl0);

        if (!isLoading()){
            showProcessDialog();
        }

        secondback.setOnClickListener((View vv)->{
            finish();
        });

        bannerAds();

        Tovuti.from(this).monitor(new Monitor.ConnectivityListener(){
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast){

                isConnection = isConnected;
                if (isConnected==false){
                    if (isLoading()){
                        goneProcessDialog();
                    }
                    Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Please check your internet connection..",
                            Snackbar.LENGTH_INDEFINITE)
                            .setTextColor(Color.WHITE)
                            .setBackgroundTint(Color.RED)
                            .setAction("Try Again!", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);
                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.white ))
                            .show();
                }else {

                }

            }
        });

        if (answerPdf.isEmpty()){
            more.setVisibility(View.GONE);
        }else {
            more.setVisibility(View.VISIBLE);
        }

        more.setOnClickListener(v->{

            cntt++;
            Prefs.putInt("clic_cntt",cntt);
            PopupMenu popup = new PopupMenu(Webview.this, more);
            popup.getMenu().add("Answer PDF");
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getTitle().toString().equals("Answer PDF")){

                        Intent intent1 = new Intent(Webview.this,AnswerPDF.class);
                        intent1.putExtra("putAnswer",answerPdf);
                        intent1.putExtra("getTitle",getTT);
                        startActivity(intent1);

                    }
                    return true;
                }
            });

            popup.show();
        });

    }

    class retPdf extends AsyncTask<String,Void, InputStream>{


        @Override
        protected InputStream doInBackground(String... strings) {

            try {
                URL url = new URL (strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                if (urlConnection.getResponseCode()==200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
               return null;
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);

            if (isLoading()){
                goneProcessDialog();
            }

            if (isConnection==false){

            }
            else if (inputStream == null){
                new AlertDialog.Builder(Webview.this)
                        .setTitle("Sorry! ")
                        .setMessage("PDF file is missing.try again!")
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_baseline_error_24)
                        .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).show();
            }

            else {

                pdfView.fromStream(inputStream).swipeVertical(true).load();
            }
        }
    }


    public void showProcessDialog(){
        loading = true;
        waiting.loadingStart();
    }
    public void goneProcessDialog(){
        loading = false;
        waiting.loadingGone();
    }
    public boolean isLoading(){
        return loading;
    }



    private void bannerAds() {

        if (Prefs.getBoolean("adEnable",true)){
            AudienceNetworkAds.initialize(Webview.this);
            adView = new AdView(Webview.this, Config.FB_Banner, AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = findViewById(R.id.rectangle_Ads);
            adContainer.addView(adView);
            adView.loadAd();
            Log.i("ActionFragment", "showBanner: " + "addLoaded!");
        }else {}


    }



    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
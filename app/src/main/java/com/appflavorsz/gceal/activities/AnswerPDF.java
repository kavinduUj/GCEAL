package com.appflavorsz.gceal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class AnswerPDF extends AppCompatActivity {

    private PDFView pdfView;
    private  boolean loading = false;
    private Waiting waiting;
    InputStream inputStream = null;
    private AdView adView;
    private boolean isConnection;
    private static final int PERMISSION_REQUEST_STORAGE = 101;
    String savePath = Environment.getExternalStorageDirectory().getPath() +"GCEAL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_p_d_f);

        waiting = new Waiting(this);
        pdfView = (PDFView) findViewById(R.id.loadAnswer);
        AudienceNetworkAds.initialize(AnswerPDF.this);
        Intent intent = getIntent();
        String imageUrl0 = intent.getStringExtra("putAnswer");
        String pdfAnswerTitle = intent.getStringExtra("getTitle");
        new retPdf().execute(imageUrl0);

        if (!isLoading()){
            showProcessDialog();
        }


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

        bannerAds();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("TAGTAG", "onClick: " + pdfAnswerTitle);
                downloadpdf(AnswerPDF.this,pdfAnswerTitle,DIRECTORY_DOWNLOADS + "/GCEAL/",imageUrl0);

            }
        });

        requestStorage();
    }

    private void downloadpdf(Context context, String fileName, String directory, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(directory,fileName + ".pdf");

        downloadManager.enqueue(request);

    }


    private void requestStorage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(AnswerPDF.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class retPdf extends AsyncTask<String,Void, InputStream> {


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
                new AlertDialog.Builder(AnswerPDF.this)
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

            adView = new AdView(AnswerPDF.this, Config.FB_Banner, AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = findViewById(R.id.rectangle_Ads);
            adContainer.addView(adView);
            adView.loadAd();
            Log.i("ActionFragment", "showBanner: " + "addLoaded!");
        }else {}


    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
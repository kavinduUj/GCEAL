package com.appflavorsz.gceal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.appflavorsz.gceal.Config;
import com.appflavorsz.gceal.R;
import com.appflavorsz.gceal.adapters.OnClick;
import com.appflavorsz.gceal.adapters.dataModelAdapter;
import com.appflavorsz.gceal.model.GetAllData;
import com.appflavorsz.gceal.service.other.Waiting;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

public class PaperActiviry extends AppCompatActivity implements OnClick {

    private View v;
    private RecyclerView myRecy;
    private DatabaseReference databaseReference0;
    private dataModelAdapter dataModelAdapter0;
    private ArrayList<Object> arrayList;
    private ImageView back, nodata;
    private Waiting waiting;
    private boolean loading = false;
    private TextView setName;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String getSub = Prefs.getString("setSubjectName", "");
    private InterstitialAd interstitialAd;
    int counter = 1;
    private Dialog dialog;
    private boolean isConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_activiry);

        AudienceNetworkAds.initialize(this);

        myRecy = (RecyclerView) findViewById(R.id.getAllData);
        myRecy.setHasFixedSize(true);
        myRecy.setLayoutManager(new LinearLayoutManager(PaperActiviry.this));
        arrayList = new ArrayList<>();
        setName = findViewById(R.id.setName);
        waiting = new Waiting(PaperActiviry.this);
        nodata = findViewById(R.id.noData);
        dialog = new Dialog(PaperActiviry.this,android.R.style.Theme_Translucent_NoTitleBar);
//        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipRefreshmain);
//        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.lightDark));
//        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.lightGreen));

        back = (ImageView) findViewById(R.id.goBack);
        back.setOnClickListener((View vv) -> {
            finish();
        });

        getSubPapers(getSub);
        interstitial();
        Tovuti.from(this).monitor(new Monitor.ConnectivityListener(){
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast){

                isConnection = isConnected;
                if (isConnected==false){
                    if (isLoading()){
                        goneProcessDialog();
                    }
                    Snackbar.make(findViewById(R.id.myCoordinatorLayout001), "Please check your internet connection..",
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


    }

    public void showDialog(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.no_connection);
        ImageView imageView = dialog.findViewById(R.id.refreshIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    private void interstitial(){
        interstitialAd = new InterstitialAd(PaperActiviry.this, Config.FB_Interstitial);
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Show the ad
                Log.i("adLoaded","true");



            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }


    private void showAd(){
        if (interstitialAd.isAdLoaded() && Prefs.getBoolean("adEnable",true)){
            Log.i("showAdShow","loading:" +"Loading...");
            if (counter == Prefs.getInt("interstitial_count",3)){
                Log.i("showAdShow","show::" + Prefs.getInt("interstitial_count",3) + "\n" + "counter:" + counter);
                interstitialAd.show();
                counter = 1;
            }else {
                counter++;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        interstitial();
    }

    private void getData(String subName) {
        if (!isLoading()) {
            showProcessDialog();
        }
        databaseReference0 = FirebaseDatabase.getInstance().getReference().child("subject").child(subName);
        databaseReference0.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (isLoading()) {
                    goneProcessDialog();
                }
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    GetAllData getAllData = dataSnapshot1.getValue(GetAllData.class);
                    arrayList.add(getAllData);
                }

                dataModelAdapter0 = new dataModelAdapter(PaperActiviry.this, arrayList, arrayList.size(),PaperActiviry.this::OnItemClick);
                myRecy.setAdapter(dataModelAdapter0);
                dataModelAdapter0.notifyDataSetChanged();
                dataModelAdapter0.initNativeAds();

                if (arrayList.isEmpty()) {
                    nodata.setVisibility(View.VISIBLE);
                } else {
                    nodata.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getSubPapers(String nameSub) {

        switch (nameSub) {
            case "bio":
                setName.setText("Biology");
                getData("bio");
                break;
            case "chemistry":
                setName.setText("Chemistry");
                getData("chemistry");
                break;
            case "physics":
                setName.setText("Physics");
                getData("physics");
                break;
            case "agri":
                setName.setText("Agriculture");
                getData("agri");
                break;
            case "combine":
                setName.setText("Combine Maths");
                getData("combine");
                break;
            case "business":
                setName.setText("Business Studies");
                getData("business");
                break;
            case "econ":
                setName.setText("Econ");
                getData("econ");
                break;
            case "accounting":
                setName.setText("Accounting");
                getData("accounting");
                break;
            case "st":
                setName.setText("Science for Technology");
                getData("st");
                break;
            case "bt":
                setName.setText("Bio System");
                getData("bt");
                break;
            case "et":
                setName.setText("Engineering Technology");
                getData("et");
                break;
            case "logic":
                setName.setText("Logic");
                getData("logic");
                break;
            case "sinhala":
                setName.setText("Sinhala");
                getData("sinhala");
                break;
            case "political":
                setName.setText("Political");
                getData("political");
                break;
            case "it":
                setName.setText("Information Technology");
                getData("it");
                break;
            case "media":
                setName.setText("Media");
                getData("media");
                break;
            case "geo":
                setName.setText("Geography");
                getData("geo");
                break;
        }
    }

    public void showProcessDialog() {
        loading = true;
        waiting.loadingStart();
    }

    public void goneProcessDialog() {
        loading = false;
        waiting.loadingGone();
    }

    public boolean isLoading() {
        return loading;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void OnItemClick(GetAllData Pos) {
        Intent i = new Intent(PaperActiviry.this, Webview.class);
        i.putExtra("pdfUrl", Pos.getPdfUrl());
        i.putExtra("getTitle", Pos.getTitle());
        i.putExtra("getAnswerPDF",Pos.getAnswerUrl());
        startActivity(i);
        showAd();
    }
}
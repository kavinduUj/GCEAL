package com.appflavorsz.gceal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.appflavorsz.gceal.activities.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixplicity.easyprefs.library.Prefs;

public class Splash extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference images,details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();


            }
        }, 5000);
        getImages();
        getAppDetails();
    }

    private void getImages(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        images = firebaseDatabase.getReference("images");

        images.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String im01 = ""+dataSnapshot.child("image01").getValue();
                String im02 = ""+dataSnapshot.child("image02").getValue();
                String im03 = ""+dataSnapshot.child("image03").getValue();
                String im04 = ""+dataSnapshot.child("image04").getValue();
                String im05 = ""+dataSnapshot.child("image05").getValue();
                String im06 = ""+dataSnapshot.child("image06").getValue();
                String im07 = ""+dataSnapshot.child("image07").getValue();

                Prefs.putString("image01",im01);
                Prefs.putString("image02",im02);
                Prefs.putString("image03",im03);
                Prefs.putString("image04",im04);
                Prefs.putString("image05",im05);
                Prefs.putString("image06",im06);
                Prefs.putString("image07",im07);

                Log.i("printDataimage",im01 + "\n" + im02 + "\n" + im03);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getAppDetails(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        details = firebaseDatabase.getReference("details");

        details.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String adEnable = ""+dataSnapshot.child("ad_enable").getValue();
                String appVersion = ""+dataSnapshot.child("app_version").getValue();
                String interstitial_count = ""+dataSnapshot.child("interstitial_count").getValue();
                String native_posi = ""+dataSnapshot.child("native_posi").getValue();
                String native_interval = ""+dataSnapshot.child("native_interval").getValue();

                 int posi = Integer.parseInt(native_posi);
                 int inteval = Integer.parseInt(native_interval);
                 int interstitial = Integer.parseInt(interstitial_count);
                 Boolean enable = Boolean.valueOf(adEnable);

                 Prefs.putBoolean("adEnable",enable);
                 Prefs.putInt("interstitial_count",interstitial);
                 Prefs.putInt("nativePosi",posi);
                 Prefs.putInt("native_interval",inteval);

                 Log.i("details","posi:" + posi + "\n" + "interval:" + inteval + "\n" + "interstitial:" + interstitial + "\n" + "enable:" + enable);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
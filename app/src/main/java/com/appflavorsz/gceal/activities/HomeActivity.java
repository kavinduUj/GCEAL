package com.appflavorsz.gceal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.appflavorsz.gceal.R;
import com.appflavorsz.gceal.fragment.MenuPage;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixplicity.easyprefs.library.Prefs;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

public class HomeActivity extends AppCompatActivity {

    private View v;
    private Button test;
    private SliderLayout sliderLayout;
    private ImageView menu;
    private ImageView bio,chemistry,physics,combine,agri,business,econ,accounting,st,et,bt,logic,sinhala,political,it,media,geo;
    private Dialog menuDialog;
    private Dialog dialog;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_home_activity);

        variableCasting();
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.COLOR);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(2);

        imageSlider();

        menu.setOnClickListener((View vv)->{

            BottomSheetDialogFragment bottomSheetFragment = new MenuPage();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

        });

        bio.setOnClickListener((View v)->{

            gotoPaperActivity("bio");
        });

        chemistry.setOnClickListener((View v)->{
            gotoPaperActivity("chemistry");
        });

        physics.setOnClickListener((View v)->{
            gotoPaperActivity("physics");
        });

        agri.setOnClickListener((View v)->{
            gotoPaperActivity("agri");
        });

        combine.setOnClickListener((View v)->{
            gotoPaperActivity("combine");
        });

        business.setOnClickListener((View v)->{
            gotoPaperActivity("business");
        });

        econ.setOnClickListener((View v)->{
            gotoPaperActivity("econ");
        });

        accounting.setOnClickListener((View v)->{
            gotoPaperActivity("accounting");
        });

        st.setOnClickListener((View v)->{
            gotoPaperActivity("st");
        });

        bt.setOnClickListener((View v)->{
            gotoPaperActivity("bt");
        });

        et.setOnClickListener((View v)->{
            gotoPaperActivity("et");
        });

        logic.setOnClickListener((View v)->{
            gotoPaperActivity("logic");
        });

        sinhala.setOnClickListener((View v)->{
            gotoPaperActivity("sinhala");
        });

        political.setOnClickListener((View v)->{
            gotoPaperActivity("political");
        });

        it.setOnClickListener((View v)->{
            gotoPaperActivity("it");
        });

        media.setOnClickListener((View v)->{
            gotoPaperActivity("media");
        });

        geo.setOnClickListener((View v)->{
            gotoPaperActivity("geo");
        });

        Tovuti.from(this).monitor(new Monitor.ConnectivityListener(){
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast){
                if (isConnected==false){

                    showDialog();
                }else {

                }

            }
        });


    }

    public void showDialog(){
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

    public void imageSlider(){

        String im1 =  Prefs.getString("image01","");
        String im2 =  Prefs.getString("image02","");
        String im3 =  Prefs.getString("image03","");
        String im4 =  Prefs.getString("image04","");
        String im5 =  Prefs.getString("image05","");
        String im6 =  Prefs.getString("image06","");
        String im7 =  Prefs.getString("image07","");



        for (int i = 0; i <= 6; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(HomeActivity.this);

            switch (i) {
                case 0:
                    sliderView.setImageUrl(im1);
                    break;
                case 1:
                    sliderView.setImageUrl(im2);
                    break;
                case 2:
                    sliderView.setImageUrl(im3);
                    break;
                case 3:
                    sliderView.setImageUrl(im4);
                    break;
                case 4:
                    sliderView.setImageUrl(im5);
                    break;
                case 5:
                    sliderView.setImageUrl(im6);
                    break;
                case 6:
                    sliderView.setImageUrl(im7);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderLayout.addSliderView(sliderView);
        }

    }

    public void variableCasting(){

        Animation aniFade = AnimationUtils.loadAnimation(HomeActivity.this,R.anim.fade_in);

        sliderLayout = (SliderLayout)findViewById(R.id.imageSlider00);
        menu = (ImageView)findViewById(R.id.hMenu);

        bio = (ImageView)findViewById(R.id.hBio);
        chemistry = (ImageView)findViewById(R.id.hChem);
        combine = (ImageView)findViewById(R.id.hComb);
        physics = (ImageView)findViewById(R.id.hPhy);
        agri = (ImageView)findViewById(R.id.hAgri);

        business = (ImageView)findViewById(R.id.hBs);
        econ = (ImageView)findViewById(R.id.hEcon);
        accounting = (ImageView)findViewById(R.id.hAcc);

        st = (ImageView)findViewById(R.id.hSTech);
        et = (ImageView)findViewById(R.id.hEng);
        bt = (ImageView)findViewById(R.id.hBioTech);

        logic = (ImageView)findViewById(R.id.hLogic);
        sinhala = (ImageView)findViewById(R.id.hSinhala);
        political = (ImageView)findViewById(R.id.hPoli);
        it = (ImageView)findViewById(R.id.hIt);
        geo = (ImageView)findViewById(R.id.hGeo);
        media = (ImageView)findViewById(R.id.hMedia);

        menuDialog = new Dialog(HomeActivity.this);
        dialog = new Dialog(HomeActivity.this,android.R.style.Theme_Translucent_NoTitleBar);

    }

    private void gotoPaperActivity(String name){
        Intent intent = new Intent(HomeActivity.this,PaperActiviry.class);
        Prefs.putString("setSubjectName",name);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
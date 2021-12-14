package com.appflavorsz.gceal.service.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.appflavorsz.gceal.R;

public class Waiting {

    private Activity activity;
    private AlertDialog alertDialog;

    public Waiting(Activity activity){
        this.activity = activity;
    }

    public void loadingStart(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.waiting, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void loadingGone(){

        alertDialog.dismiss();
    }
}

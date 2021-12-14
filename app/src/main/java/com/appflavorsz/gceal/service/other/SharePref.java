package com.appflavorsz.gceal.service.other;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {

    public SharedPreferences sharedPreferences;
    public Context context;
    String loginPref ="com.education.gceal_login_preference";
    String loginStatus ="com.education.gceal_login_status_preference";


    public SharePref(Context context){

        this.context = context;
        sharedPreferences = context.getSharedPreferences(loginPref,Context.MODE_PRIVATE);

    }


    public void wireLogStatus (boolean status){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(loginStatus,status);
        editor.apply();
    }

    public boolean readStatus (){

        boolean status=false;
        status=sharedPreferences.getBoolean(loginStatus,false);
        return status;
    }
}

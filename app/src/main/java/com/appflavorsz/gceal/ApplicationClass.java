package com.appflavorsz.gceal;

import android.app.Application;
import android.content.ContextWrapper;

import com.facebook.ads.AudienceNetworkAds;
import com.onesignal.OneSignal;
import com.pixplicity.easyprefs.library.Prefs;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkInitializeHelper.initialize(this);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


    }
}

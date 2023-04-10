package com.rixengine.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.rixengine.api.AlxAdSDK;
import com.rixengine.api.AlxSdkInitCallback;
import com.anythink.core.api.ATSDK;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainApp extends Application {
    private final String TAG = "MainApp";

    private static MainApp mApp;

    public static MainApp getInstance(){
        return mApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;
        initAlxAd();
        initAggregationPlatform();
    }

    /**
     * 广告配置
     */
    public void initAlxAd(){
        try {
            //Alx Ad Init
            AlxAdSDK.setAC(true, 3600 * 24);
            AlxAdSDK.init(this, AdConfig.ALX_HOST,AdConfig.ALX_TOKEN, AdConfig.ALX_SID, AdConfig.ALX_APP_ID, new AlxSdkInitCallback() {
                @Override
                public void onInit(boolean isOk, String msg) {
                    Log.i(TAG, Thread.currentThread().getName() + ":" + isOk + "-" + msg);
                }
            });
            AlxAdSDK.setDebug(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //聚合平台配置
    private void initAggregationPlatform() {
        try {
            //TopOn Ad Init
            ATSDK.init(this, AdConfig.TOPON_APP_ID, AdConfig.TOPON_KEY);
            ATSDK.setNetworkLogDebug(true);

            //Google Ad Init
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

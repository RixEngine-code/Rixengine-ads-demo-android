package com.rixengine.demo.tradplus;

import android.os.Bundle;
import android.util.Log;

import com.rixengine.demo.AdConfig;
import com.rixengine.demo.BaseListActivity;
import com.rixengine.demo.R;
import com.tradplus.ads.open.TradPlusSdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TradPlusDemoListActivity extends BaseListActivity {


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        sdkInit();
    }

    @Override
    public List<AdapterData> initAdapterData() {
        List<AdapterData> list = new ArrayList<>();
        AdapterData item = new AdapterData(getString(R.string.splash_ad), TradPlusSplashActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.banner_ad), TradPlusBannerActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.reward_ad), TradPlusRewardVideoActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.interstitial_ad), TradPlusInterstitialActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.native_ad), TradPlusNativeActivity.class);
        list.add(item);

        return list;
    }

    private static AtomicBoolean sdkInitBoolean = new AtomicBoolean(false);

    private void sdkInit() {
        if (sdkInitBoolean.compareAndSet(false, true)) {
            Log.i("sdkInit", "init=1");
            TradPlusSdk.initSdk(this.getApplicationContext(), AdConfig.TRAD_PLUS_APP_ID);
        } else {
            Log.i("sdkInit", "sdkInit() > 1");
        }
    }


}
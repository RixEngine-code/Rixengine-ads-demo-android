package com.rixengine.demo.topon;

import android.os.Bundle;

import com.rixengine.demo.BaseListActivity;
import com.rixengine.demo.R;

import java.util.ArrayList;
import java.util.List;


public class TopOnAdDemoListActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public List<AdapterData> initAdapterData() {
        List<AdapterData> list = new ArrayList<>();
        AdapterData item = new AdapterData(getString(R.string.banner_ad), TopOnBannerActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.reward_ad), TopOnRewardVideoActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.interstitial_ad), TopOnInterstitialActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.native_ad), TopOnNativeActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.splash_ad), TopOnSplashActivity.class);
        list.add(item);

        return list;
    }

}
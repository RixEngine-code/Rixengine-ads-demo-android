package com.rixengine.demo.ironsource;

import android.os.Bundle;

import com.rixengine.demo.AdConfig;
import com.rixengine.demo.BaseListActivity;
import com.rixengine.demo.R;
import com.ironsource.mediationsdk.IronSource;

import java.util.ArrayList;
import java.util.List;

public class IronSourceDemoListActivity extends BaseListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adInit();
    }


    @Override
    public List<AdapterData> initAdapterData() {
        List<AdapterData> list = new ArrayList<>();
        //IronSourceDemoListActivity.AdapterData bannerItem = new IronSourceDemoListActivity.AdapterData("banner 广告", MoPubBannerActivity.class);
        AdapterData rewardItem = new AdapterData(getString(R.string.reward_ad), IronSourceRewardedVideoActivity.class);
        AdapterData interstitialItem = new AdapterData(getString(R.string.interstitial_ad), IronSourceInterstitialActivity.class);
        AdapterData bannerItem = new AdapterData(getString(R.string.banner_ad), IronSourceBannerActivity.class);
        // IronSourceDemoListActivity.AdapterData nativeItem = new IronSourceDemoListActivity.AdapterData("native 广告", MoPubNativeActivity.class);
        //list.add(bannerItem);
        list.add(rewardItem);
        list.add(interstitialItem);
        list.add(bannerItem);
        //list.add(nativeItem);
        return list;
    }

    //广告配置
    private void adInit() {
        //IronSource初始化
        IronSource.init(this, AdConfig.IRON_SOURCE_APP_KEY,
                IronSource.AD_UNIT.OFFERWALL,
                IronSource.AD_UNIT.INTERSTITIAL,
                IronSource.AD_UNIT.REWARDED_VIDEO,
                IronSource.AD_UNIT.BANNER);
    }

}
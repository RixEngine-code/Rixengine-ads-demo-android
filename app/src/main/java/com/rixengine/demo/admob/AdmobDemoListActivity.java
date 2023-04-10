package com.rixengine.demo.admob;

import android.os.Bundle;

import com.rixengine.demo.BaseListActivity;
import com.rixengine.demo.R;

import java.util.ArrayList;
import java.util.List;

public class AdmobDemoListActivity extends BaseListActivity {

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public List<AdapterData> initAdapterData() {
        List<AdapterData> list = new ArrayList<>();

        AdapterData bannerItem = new AdapterData(getString(R.string.banner_ad), AdmobBannerActivity.class);
        AdapterData rewardItem = new AdapterData(getString(R.string.reward_ad), AdmobRewardVideoActivity.class);
        AdapterData interstitialItem = new AdapterData(getString(R.string.interstitial_ad), AdmobInterstitialActivity.class);
        AdapterData nativeItem = new AdapterData(getString(R.string.native_ad), AdmobNativeActivity.class);
        list.add(bannerItem);
        list.add(rewardItem);
        list.add(interstitialItem);
        list.add(nativeItem);
        return list;
    }

}
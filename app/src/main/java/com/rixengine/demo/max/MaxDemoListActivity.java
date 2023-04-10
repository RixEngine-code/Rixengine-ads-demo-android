package com.rixengine.demo.max;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.rixengine.demo.BaseListActivity;
import com.rixengine.demo.R;
import com.applovin.sdk.AppLovinPrivacySettings;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinUserService;

import java.util.ArrayList;
import java.util.List;

public class MaxDemoListActivity extends BaseListActivity {
    private static final String TAG = "MaxDemoListActivity";


    private Context mContext;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mContext = this;
        initSdk();
    }


    @Override
    public List<AdapterData> initAdapterData() {
        List<AdapterData> list = new ArrayList<>();

        AdapterData bannerItem = new AdapterData(getString(R.string.banner_ad), MaxBannerActivity.class);
        AdapterData rewardItem = new AdapterData(getString(R.string.reward_ad), MaxRewardVideoActivity.class);
        AdapterData interstitialItem = new AdapterData(getString(R.string.interstitial_ad), MaxInterstitialActivity.class);
        AdapterData nativeItem = new AdapterData(getString(R.string.native_ad), MaxNativeActivity.class);
        list.add(bannerItem);
        list.add(rewardItem);
        list.add(interstitialItem);
        list.add(nativeItem);
        return list;
    }


    //Applovin广告初始化
    private void initSdk() {
        AppLovinSdk.getInstance(mContext.getApplicationContext()).setMediationProvider("algorix");
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration config) {
                String name = config.getConsentDialogState().name();
                Log.i(TAG, "AppLovinSdk-init:" + name);
                // showDialog();

                if (config.getConsentDialogState() == AppLovinSdkConfiguration.ConsentDialogState.APPLIES) {
                    AppLovinUserService userService = AppLovinSdk.getInstance(mContext.getApplicationContext()).getUserService();
                    userService.showConsentDialog(MaxDemoListActivity.this, new AppLovinUserService.OnConsentDialogDismissListener() {
                        @Override
                        public void onDismiss() {

                        }
                    });
                }

            }
        });
    }

    private void showDialog() {
        Log.i(TAG, "showDialog:" + Thread.currentThread().getName());
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("update config info")
                .setMessage("update config info")
                .setPositiveButton("set to true", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppLovinPrivacySettings.setHasUserConsent(true, mContext);
                        AppLovinPrivacySettings.setIsAgeRestrictedUser(true, mContext);
                        AppLovinPrivacySettings.setDoNotSell(true, mContext);
                        Log.d(TAG, "true-onclick:" + AppLovinPrivacySettings.hasUserConsent(mContext));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("set to false", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppLovinPrivacySettings.setHasUserConsent(false, mContext);
                        AppLovinPrivacySettings.setIsAgeRestrictedUser(false, mContext);
                        AppLovinPrivacySettings.setDoNotSell(false, mContext);
                        Log.d(TAG, "false-onclick:" + AppLovinPrivacySettings.hasUserConsent(mContext));
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

}
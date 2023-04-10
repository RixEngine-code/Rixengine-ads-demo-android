package com.rixengine.demo.topon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.rixengine.demo.AdConfig;
import com.rixengine.demo.MainActivity;
import com.rixengine.demo.R;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.splashad.api.ATSplashAd;
import com.anythink.splashad.api.ATSplashAdExtraInfo;
import com.anythink.splashad.api.ATSplashAdListener;


public class TopOnSplashActivity extends AppCompatActivity {
    private static final String TAG = "TopOnSplashDemoActivity";

    private ATSplashAd mAD;
    private FrameLayout mAdContainer;

    //控制开屏广告点击跳转
    private boolean canJump = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topon_splash);
        initView();
        loadAd();
    }

    private void initView() {
        mAdContainer = (FrameLayout) findViewById(R.id.ad_container);
    }

    private void loadAd() {
        mAD = new ATSplashAd(this, AdConfig.TOPON_SPLASH_PID, new ATSplashAdListener() {
            @Override
            public void onAdLoaded(boolean b) {
                Log.d(TAG, "onAdLoaded:" + getThreadName());
                if (mAD.isAdReady()) {
                    mAD.show(TopOnSplashActivity.this, mAdContainer);
                }
            }

            @Override
            public void onAdLoadTimeout() {
                Log.d(TAG, "onAdLoadTimeout:" + getThreadName());
            }

            @Override
            public void onNoAdError(AdError adError) {
                Log.d(TAG, "onNoAdError:" + adError.getCode() + ";" + adError.getDesc() + "=" + getThreadName());
                goToMainActivity();
            }

            @Override
            public void onAdShow(ATAdInfo atAdInfo) {
                Log.d(TAG, "onAdShow:" + getThreadName());

            }

            @Override
            public void onAdClick(ATAdInfo atAdInfo) {
                Log.d(TAG, "onAdClick:" + getThreadName());
                canJump = true;
            }

            @Override
            public void onAdDismiss(ATAdInfo atAdInfo, ATSplashAdExtraInfo atSplashAdExtraInfo) {
                Log.d(TAG, "onAdDismiss:" + getThreadName());
                goToMainActivity();
            }

        });
        mAD.loadAd();
    }

    private void goToMainActivity() {
        this.startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            goToMainActivity();
        }
//        canJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
//        canJump = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAD != null) {
            mAD.onDestory();
        }
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }

}
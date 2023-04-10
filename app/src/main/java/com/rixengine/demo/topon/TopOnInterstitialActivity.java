package com.rixengine.demo.topon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rixengine.demo.AdConfig;
import com.rixengine.demo.R;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.interstitial.api.ATInterstitial;
import com.anythink.interstitial.api.ATInterstitialListener;


public class TopOnInterstitialActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TopOnInterstitialDemo";
    private TextView mTvTip;
    private TextView mTvShow;
    private ATInterstitial mAD;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_and_show);
        initView();
    }

    private void initView() {
        TextView tv_load = findViewById(R.id.tv_load);
        mTvShow = findViewById(R.id.tv_show);
        mTvTip = findViewById(R.id.tv_tip);
        mTvShow.setEnabled(false);
        tv_load.setOnClickListener(this);
        mTvShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_load) {
            loadAd();
        } else if (id == R.id.tv_show) {
            if (mAD == null) {
                Toast.makeText(this, getString(R.string.show_ad_no_load), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mAD.isAdReady()) {
                mAD.show(this);
            } else {
                Toast.makeText(this, "isAdReady()==false", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 加载广告
     */
    public void loadAd() {
        mTvTip.setText(R.string.loading);
        startTime = System.currentTimeMillis();

        mAD = new ATInterstitial(this, AdConfig.TOPON_INTERSTITIAL_PID);
        mAD.setAdListener(new ATInterstitialListener() {

            @Override
            public void onInterstitialAdLoaded() {
                Log.i(TAG, "onInterstitialAdLoaded:" + getThreadName());
                Toast.makeText(getBaseContext(), getString(R.string.load_success), Toast.LENGTH_SHORT).show();
                mTvTip.setText(getString(R.string.format_load_success, (System.currentTimeMillis() - startTime) / 1000));
                mTvShow.setEnabled(true);
            }

            @Override
            public void onInterstitialAdLoadFail(AdError adError) {
                Log.e(TAG, "onInterstitialAdLoadFail:" + adError.getCode() + ";" + adError.getDesc() + ";" + getThreadName());
                Toast.makeText(getBaseContext(), getString(R.string.load_failed), Toast.LENGTH_SHORT).show();
                mTvTip.setText(R.string.load_failed);
                mTvShow.setEnabled(false);
            }

            @Override
            public void onInterstitialAdClicked(ATAdInfo atAdInfo) {
                Log.i(TAG, "onInterstitialAdClicked:" + getThreadName());
            }

            @Override
            public void onInterstitialAdShow(ATAdInfo atAdInfo) {
                Log.i(TAG, "onInterstitialAdShow:" + getThreadName());
            }

            @Override
            public void onInterstitialAdClose(ATAdInfo atAdInfo) {
                Log.i(TAG, "onInterstitialAdClose:" + getThreadName());
                mTvShow.setEnabled(false);
                mTvTip.setText("");
            }

            @Override
            public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) {
                Log.i(TAG, "onInterstitialAdVideoStart:" + getThreadName());
            }

            @Override
            public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {
                Log.i(TAG, "onInterstitialAdVideoEnd:" + getThreadName());
            }

            @Override
            public void onInterstitialAdVideoError(AdError adError) {
                Log.i(TAG, "onInterstitialAdVideoError:" + adError.getCode() + ";" + adError.getDesc() + ";" + getThreadName());
            }

        });
        mAD.load();
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }

}

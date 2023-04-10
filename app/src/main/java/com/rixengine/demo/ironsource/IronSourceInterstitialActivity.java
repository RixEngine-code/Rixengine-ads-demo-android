package com.rixengine.demo.ironsource;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rixengine.demo.R;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener;

public class IronSourceInterstitialActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "IronSourceInterstitial";
    private TextView mTvTip;
    private TextView mTvShow;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_and_show);
        initView();
        String advertisingId = IronSource.getAdvertiserId(IronSourceInterstitialActivity.this);
        // we're using an advertisingId as the 'userId'
        //initIronSource(APP_KEY, advertisingId);
        Log.d(TAG, "advertisid : " + advertisingId);
        IntegrationHelper.validateIntegration(this);
        IronSource.setUserId(advertisingId);
        IronSource.getAdvertiserId(this);
        //Network Connectivity Status
        IronSource.shouldTrackNetworkState(this, true);
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
            bnLoad();
        } else if (id == R.id.tv_show) {
            bnShow();
        }
    }

    private void bnLoad() {
        mTvTip.setText(R.string.loading);
        startTime = System.currentTimeMillis();
        mTvShow.setEnabled(false);

        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
            @Override
            public void onAdReady(AdInfo adInfo) {
                Log.d(TAG, "onAdReady");
                Toast.makeText(getBaseContext(), getString(R.string.load_success), Toast.LENGTH_SHORT).show();
                mTvTip.setText(getString(R.string.format_load_success, (System.currentTimeMillis() - startTime) / 1000));
                mTvShow.setEnabled(true);
            }

            @Override
            public void onAdLoadFailed(IronSourceError ironSourceError) {
                Log.d(TAG, "onAdLoadFailed: " + ironSourceError.getErrorCode()+";"+ironSourceError.getErrorMessage());
                Toast.makeText(getBaseContext(), getString(R.string.load_failed), Toast.LENGTH_SHORT).show();
                mTvTip.setText(getString(R.string.format_load_failed, ironSourceError.getErrorMessage()));
                mTvShow.setEnabled(false);
            }

            @Override
            public void onAdOpened(AdInfo adInfo) {
                Log.d(TAG, "onAdOpened");
            }

            @Override
            public void onAdShowSucceeded(AdInfo adInfo) {
                Log.d(TAG, "onAdShowSucceeded");
            }

            @Override
            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                Log.d(TAG, "onAdShowFailed: " + ironSourceError.getErrorCode()+";"+ironSourceError.getErrorMessage());
            }

            @Override
            public void onAdClicked(AdInfo adInfo) {
                Log.d(TAG, "onAdClicked");
            }

            @Override
            public void onAdClosed(AdInfo adInfo) {
                Log.d(TAG, "onAdClosed");
            }
        });

        IronSource.loadInterstitial();
    }


    private void bnShow() {
        if (IronSource.isInterstitialReady()) {
            IronSource.showInterstitial();
        }
    }

    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }
}
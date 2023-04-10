package com.rixengine.demo.ironsource;

import android.annotation.SuppressLint;
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
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;

public class IronSourceRewardedVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "IronSourceRewardedVideo";
    private TextView mTvTip;
    private TextView mTvShow;
    private long startTime;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_and_show);
        initView();
        //IronSource.init(this, AdConfig.IRON_SOURCE_APP_KEY);
        String advertisingId = IronSource.getAdvertiserId(IronSourceRewardedVideoActivity.this);
        // we're using an advertisingId as the 'userId'
        //initIronSource(APP_KEY, advertisingId);
        Log.d(TAG, "advertisid : " + advertisingId);
        IronSource.setAdaptersDebug(true);
        IntegrationHelper.validateIntegration(this);
        IronSource.setUserId(advertisingId);
        IronSource.getAdvertiserId(this);
        //Network Connectivity Status
        IronSource.shouldTrackNetworkState(this, true);
    }

    public void initView() {
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

        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
            @Override
            public void onAdAvailable(AdInfo adInfo) {
                Log.d(TAG, "onAdAvailable");
                Toast.makeText(getBaseContext(), getString(R.string.load_success), Toast.LENGTH_SHORT).show();
                mTvTip.setText(getString(R.string.format_load_success, (System.currentTimeMillis() - startTime) / 1000));
                mTvShow.setEnabled(true);
            }

            @Override
            public void onAdUnavailable() {
                Log.d(TAG, "onAdUnavailable");
            }

            @Override
            public void onAdOpened(AdInfo adInfo) {
                Log.d(TAG, "onAdOpened");
            }

            @Override
            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                Log.d(TAG, "onAdShowFailed: " + ironSourceError.getErrorCode()+";"+ironSourceError.getErrorMessage());
                Toast.makeText(getBaseContext(), getString(R.string.load_failed), Toast.LENGTH_SHORT).show();
                mTvTip.setText(getString(R.string.format_load_failed, ironSourceError.getErrorMessage()));
                mTvShow.setEnabled(false);
            }

            @Override
            public void onAdClicked(Placement placement, AdInfo adInfo) {
                Log.d(TAG, "onAdClicked");
            }

            @Override
            public void onAdRewarded(Placement placement, AdInfo adInfo) {
                Log.d(TAG, "onAdRewarded");
            }

            @Override
            public void onAdClosed(AdInfo adInfo) {
                Log.d(TAG, "onAdClosed");
            }
        });

        IronSource.loadRewardedVideo();
    }


    private void bnShow() {
        if (IronSource.isRewardedVideoAvailable()) {
            IronSource.showRewardedVideo();
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
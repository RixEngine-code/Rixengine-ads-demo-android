package com.rixengine.demo.rixengine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rixengine.api.AlxRewardVideoAD;
import com.rixengine.api.AlxRewardVideoADListener;
import com.rixengine.demo.AdConfig;
import com.rixengine.demo.R;


public class RewardVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AlxRewardVideoDemo";

    private TextView mTvTip;
    private TextView mTvShow;
    private AlxRewardVideoAD mVideoAD;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_and_show);
        initView();
    }

    private void initView() {
        TextView tv_video_load = findViewById(R.id.tv_load);
        mTvShow = findViewById(R.id.tv_show);
        mTvTip = findViewById(R.id.tv_tip);
        mTvShow.setEnabled(false);
        tv_video_load.setOnClickListener(this);
        mTvShow.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_load:
                loadAd();
                break;
            case R.id.tv_show:
                if (mVideoAD == null) {
                    Toast.makeText(this, getString(R.string.show_ad_no_load), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mVideoAD.isReady()) {
                    mVideoAD.showVideo(this);
                } else {
                    loadAd();
                }
                break;
        }
    }

    /**
     * load Ad
     */
    public void loadAd() {
        mTvTip.setText(R.string.loading);
        startTime = System.currentTimeMillis();
        mVideoAD = new AlxRewardVideoAD();
        mVideoAD.load(this, AdConfig.ALX_REWARD_VIDEO_AD_PID, new AlxRewardVideoADListener() {

            @Override
            public void onRewardedVideoAdLoaded(AlxRewardVideoAD var1) {
                Log.i(TAG, "onRewardedVideoAdLoaded");
                mTvShow.setEnabled(true);
                Toast.makeText(getBaseContext(), getString(R.string.load_success), Toast.LENGTH_SHORT).show();
                mTvTip.setText(getString(R.string.format_load_success, (System.currentTimeMillis() - startTime) / 1000) + "｜ ecpm:" + mVideoAD.getPrice());

                mVideoAD.reportChargingUrl();
                mVideoAD.reportBiddingUrl();
            }

            @Override
            public void onRewardedVideoAdFailed(AlxRewardVideoAD var1, int errCode, String errMsg) {
                Log.i(TAG, "onRewardedVideoAdFailed：" + errCode + "; " + errMsg);
                mTvShow.setEnabled(false);
                Toast.makeText(getBaseContext(), getString(R.string.load_failed), Toast.LENGTH_SHORT).show();
                mTvTip.setText(R.string.load_failed);
            }

            @Override
            public void onRewardedVideoAdPlayStart(AlxRewardVideoAD var1) {
                Log.i(TAG, "onRewardedVideoAdPlayStart");
            }

            @Override
            public void onRewardedVideoAdPlayEnd(AlxRewardVideoAD var1) {
                Log.i(TAG, "onRewardedVideoAdPlayEnd");
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AlxRewardVideoAD var2, int errCode, String errMsg) {
                Log.i(TAG, "onRewardedVideoAdPlayFailed:" + errCode + ";" + errMsg);
            }

            @Override
            public void onRewardedVideoAdClosed(AlxRewardVideoAD var1) {
                Log.i(TAG, "onRewardedVideoAdClosed");
                mTvShow.setEnabled(false);
                mTvTip.setText(R.string.tip_message);
            }

            @Override
            public void onRewardedVideoAdPlayClicked(AlxRewardVideoAD var1) {
                Log.i(TAG, "onRewardedVideoAdPlayClicked");
            }

            @Override
            public void onReward(AlxRewardVideoAD var1) {
                Log.i(TAG, "onReward");
            }

            @Override
            public void onRewardVideoCache(boolean isSuccess) {
                Log.i(TAG, "onRewardVideoCache:" + isSuccess + ";" + Thread.currentThread().getName());
            }
        });

    }

}
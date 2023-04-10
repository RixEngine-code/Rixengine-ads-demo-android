package com.rixengine.demo.topon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rixengine.demo.AdConfig;
import com.rixengine.demo.R;
import com.anythink.banner.api.ATBannerListener;
import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;


public class TopOnBannerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TopOnBannerDemoActivity";

    private FrameLayout mAdContainerView;
    private View mBnLoad;
    private TextView mTvTip;
    ATBannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_with_viewgroup);
        initView();
    }

    private void initView() {
        mAdContainerView = (FrameLayout) findViewById(R.id.ad_container);
        mTvTip = findViewById(R.id.tv_tip);
        mBnLoad = findViewById(R.id.bn_load);
        mBnLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bn_load) {
            loadAd();
        }
    }

    private void loadAd() {
        mTvTip.setText(R.string.loading);
        mBnLoad.setEnabled(false);

        if (bannerView != null) {
            bannerView.destroy();
        }

        bannerView = new ATBannerView(this);
        bannerView.setPlacementId(AdConfig.TOPON_BANNER_AD_PID);
        bannerView.setBannerAdListener(new ATBannerListener() {
            @Override
            public void onBannerLoaded() {
                Log.d(TAG, "onBannerLoaded");
                mBnLoad.setEnabled(true);
                mTvTip.setText(R.string.load_success);
                showAd();
            }

            @Override
            public void onBannerFailed(AdError adError) {
                mBnLoad.setEnabled(true);
                String msg = adError.getCode() + ":" + adError.getDesc();
                Log.d(TAG, "onBannerFailed:" + msg);
                mTvTip.setText(getString(R.string.format_load_failed, msg));
                Toast.makeText(getBaseContext(), getString(R.string.load_failed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBannerClicked(ATAdInfo atAdInfo) {
                Log.d(TAG, "onBannerClicked");
            }

            @Override
            public void onBannerShow(ATAdInfo atAdInfo) {
                Log.d(TAG, "onBannerShow");
            }

            @Override
            public void onBannerClose(ATAdInfo atAdInfo) {
                Log.d(TAG, "onBannerClose");
            }

            @Override
            public void onBannerAutoRefreshed(ATAdInfo atAdInfo) {
                Log.d(TAG, "onBannerAutoRefreshed");
            }

            @Override
            public void onBannerAutoRefreshFail(AdError adError) {
                Log.d(TAG, "onBannerAutoRefreshFail:" + adError.getCode() + "," + adError.getDesc());
            }
        });
        bannerView.loadAd();
    }

    private void showAd() {
        mAdContainerView.removeAllViews();
        if (bannerView != null) {
            mAdContainerView.addView(bannerView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bannerView != null) {
            bannerView.destroy();
        }
    }
}

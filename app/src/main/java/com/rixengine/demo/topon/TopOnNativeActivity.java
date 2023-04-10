package com.rixengine.demo.topon;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rixengine.demo.AdConfig;
import com.rixengine.demo.R;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.nativead.api.ATNative;
import com.anythink.nativead.api.ATNativeAdView;
import com.anythink.nativead.api.ATNativeDislikeListener;
import com.anythink.nativead.api.ATNativeEventExListener;
import com.anythink.nativead.api.ATNativeImageView;
import com.anythink.nativead.api.ATNativeMaterial;
import com.anythink.nativead.api.ATNativeNetworkListener;
import com.anythink.nativead.api.ATNativePrepareExInfo;
import com.anythink.nativead.api.ATNativePrepareInfo;
import com.anythink.nativead.api.NativeAd;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopOnNativeActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = TopOnNativeActivity.class.getSimpleName();

    private TextView mTvLoad;
    private TextView mTvTip;

    private long mStartTime;

    private ATNative mATNative;
    private NativeAd mNativeAd;
    private ATNativeAdView mATNativeAdView; //渲染广告必须创建的容器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topon_native);
        initView();
    }

    private void initView() {
        mTvLoad = (TextView) findViewById(R.id.tv_load);
        mTvTip = (TextView) findViewById(R.id.tv_tip);
        mATNativeAdView = (ATNativeAdView) findViewById(R.id.ad_container);

        mTvLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_load) {
            loadNativeAd();
        }
    }

    private void loadNativeAd() {
        mTvTip.setText("Ad loading...");
        mTvLoad.setEnabled(false);
        mStartTime = System.currentTimeMillis();

        mATNative = new ATNative(this, AdConfig.TOPON_NATIVE_PID, new ATNativeNetworkListener() {
            @Override
            public void onNativeAdLoaded() {
                Log.i(TAG, "onNativeAdLoaded：" + getThreadName());
                mTvLoad.setEnabled(true);
                mTvTip.setText(getString(R.string.format_load_success,(System.currentTimeMillis() - mStartTime) / 1000));
                showNativeAd();
            }

            @Override
            public void onNativeAdLoadFail(AdError adError) {
                Log.e(TAG, "onNativeAdLoadFail:"+ adError.getFullErrorInfo());
                mTvLoad.setEnabled(true);
                mTvTip.setText(getString(R.string.format_load_failed,adError.getFullErrorInfo()));
            }
        });

        int mAdViewWidth = getResources().getDisplayMetrics().widthPixels;
        int mAdViewHeight = dip2px(340);
        Map<String, Object> localMap = new HashMap<>();
        localMap.put("imageWidth", mAdViewWidth);
        localMap.put("imageHeight", mAdViewHeight);
        localMap.put("nativeType", "0");
        mATNative.setLocalExtra(localMap);

        //load ad
        mATNative.makeAdRequest();
    }

    private void showNativeAd() {
        if (mATNative == null) {
            return;
        }
        if (!mATNative.checkAdStatus().isReady()) {
            return;
        }

        NativeAd nativeAd = mATNative.getNativeAd();
        if (nativeAd == null) {
            return;
        }
        if (mNativeAd != null) {
            mNativeAd.destory();
        }
        mNativeAd = nativeAd;
        mNativeAd.setNativeEventListener(new ATNativeEventExListener() {
            @Override
            public void onDeeplinkCallback(ATNativeAdView atNativeAdView, ATAdInfo atAdInfo, boolean b) {
                Log.i(TAG, "onDeeplinkCallback");
            }

            @Override
            public void onAdImpressed(ATNativeAdView atNativeAdView, ATAdInfo atAdInfo) {
                Log.i(TAG, "onAdImpressed");
            }

            @Override
            public void onAdClicked(ATNativeAdView atNativeAdView, ATAdInfo atAdInfo) {
                Log.i(TAG, "onAdClicked");
            }

            @Override
            public void onAdVideoStart(ATNativeAdView atNativeAdView) {
                Log.i(TAG, "onAdVideoStart");
            }

            @Override
            public void onAdVideoEnd(ATNativeAdView atNativeAdView) {
                Log.i(TAG, "onAdVideoEnd");
            }

            @Override
            public void onAdVideoProgress(ATNativeAdView atNativeAdView, int i) {
                Log.i(TAG, "onAdVideoProgress:" + i);
            }
        });

        mNativeAd.setDislikeCallbackListener(new ATNativeDislikeListener() {
            @Override
            public void onAdCloseButtonClick(ATNativeAdView view, ATAdInfo entity) {
                Log.i(TAG, "native ad onAdCloseButtonClick");
                //在这里开发者可实现广告View的移除操作
                mATNativeAdView.removeAllViews();
                if (mNativeAd != null) {
                    mNativeAd.destory();
                }
            }
        });

        mATNativeAdView.removeAllViews();
        ATNativePrepareInfo nativePrepareInfo = null;

        if (!mNativeAd.isNativeExpress()) {
            Log.d(TAG,"native self render");
            //自渲染 (如果也需要支持自渲染广告可参考自渲染广告集成方式)
            try {
                View view = getLayoutInflater().inflate(R.layout.topon_native_custom_ad_view, null);
                nativePrepareInfo = renderNativeAdView(mNativeAd, view);
                mNativeAd.renderAdContainer(mATNativeAdView, view);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.d(TAG,"native express");
            //模板渲染 (模版渲染只需要实现这步即可)
            mNativeAd.renderAdContainer(mATNativeAdView, null);
        }
        mNativeAd.prepare(mATNativeAdView, nativePrepareInfo);
    }


    /**
     * 自渲染广告
     *
     * @return
     */
    private ATNativePrepareInfo renderNativeAdView(NativeAd bean, View view) throws Exception {
        if (mATNativeAdView != null) {
            mATNativeAdView.removeAllViews();
            mATNativeAdView.addView(view);
        }

        TextView titleView = (TextView) view.findViewById(R.id.native_title);
        TextView descView = (TextView) view.findViewById(R.id.native_description);
        TextView adFromView = (TextView) view.findViewById(R.id.native_source);
        ImageView iconView = (ImageView) view.findViewById(R.id.native_icon);
//        ImageView imageView = (ImageView) view.findViewById(R.id.native_image);
        ImageView logoView = (ImageView) view.findViewById(R.id.native_logo);
        Button callToActionView = (Button) view.findViewById(R.id.ad_call_to_action);
        ImageView closeView = (ImageView) view.findViewById(R.id.native_close);
        FrameLayout contentArea = (FrameLayout) view.findViewById(R.id.native_media);

        ATNativePrepareInfo nativePrepareInfo = new ATNativePrepareInfo();
        ATNativeMaterial adMaterial = bean.getAdMaterial();

        List<View> clickViewList = new ArrayList<>();//click views

        // title
        String title = adMaterial.getTitle();
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
            nativePrepareInfo.setTitleView(titleView);//bind title
            clickViewList.add(titleView);
            titleView.setVisibility(View.VISIBLE);
        } else {
            titleView.setVisibility(View.GONE);
        }

        String descriptionText = adMaterial.getDescriptionText();
        if (!TextUtils.isEmpty(descriptionText)) {
            // desc
            descView.setText(descriptionText);
            nativePrepareInfo.setDescView(descView);//bind desc
            clickViewList.add(descView);
            descView.setVisibility(View.VISIBLE);
        } else {
            descView.setVisibility(View.GONE);
        }

        String iconUrl = adMaterial.getIconImageUrl();
        if (!TextUtils.isEmpty(descriptionText)) {
            Glide.with(this).load(iconUrl).into(iconView);
            nativePrepareInfo.setDescView(iconView);
            clickViewList.add(iconView);
            iconView.setVisibility(View.VISIBLE);
        } else {
            iconView.setVisibility(View.GONE);
        }

        String adFrom = adMaterial.getAdFrom();
        // ad from
        if (!TextUtils.isEmpty(adFrom)) {
            adFromView.setText(adFrom);
            adFromView.setVisibility(View.VISIBLE);
        } else {
            adFromView.setVisibility(View.GONE);
        }
        nativePrepareInfo.setAdFromView(adFromView);//bind ad from

        // cta button
        String callToActionText = adMaterial.getCallToActionText();
        if (!TextUtils.isEmpty(callToActionText)) {
            callToActionView.setText(callToActionText);
            nativePrepareInfo.setCtaView(callToActionView);//bind cta button
            clickViewList.add(callToActionView);
            callToActionView.setVisibility(View.VISIBLE);
        } else {
            callToActionView.setVisibility(View.GONE);
        }

        // media view
        View mediaView = adMaterial.getAdMediaView();

        RelativeLayout.LayoutParams mainImageParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        contentArea.removeAllViews();

        if (mediaView != null) {
            if (mediaView.getParent() != null) {
                ((ViewGroup) mediaView.getParent()).removeView(mediaView);
            }
//            mainImageParam.gravity = Gravity.CENTER;
            mediaView.setLayoutParams(mainImageParam);
            contentArea.addView(mediaView, mainImageParam);
            clickViewList.add(mediaView);
            contentArea.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(adMaterial.getMainImageUrl())) {
            ATNativeImageView imageView = new ATNativeImageView(this);
            imageView.setImage(adMaterial.getMainImageUrl());
            imageView.setLayoutParams(mainImageParam);
            contentArea.addView(imageView, mainImageParam);

            nativePrepareInfo.setMainImageView(imageView);//bind main image
            clickViewList.add(imageView);
            contentArea.setVisibility(View.VISIBLE);
        } else {
            contentArea.removeAllViews();
            contentArea.setVisibility(View.GONE);
        }

        //Ad Logo
        String adChoiceIconUrl = adMaterial.getAdChoiceIconUrl();
        Bitmap adLogoBitmap = adMaterial.getAdLogo();
        if (!TextUtils.isEmpty(adChoiceIconUrl)) {
            Glide.with(this).load(adChoiceIconUrl).into(logoView);
            nativePrepareInfo.setAdLogoView(logoView);//bind ad choice
            logoView.setVisibility(View.VISIBLE);
        } else if (adLogoBitmap != null) {
            logoView.setImageBitmap(adLogoBitmap);
            logoView.setVisibility(View.VISIBLE);
        } else {
            logoView.setImageBitmap(null);
            logoView.setVisibility(View.GONE);
        }

        nativePrepareInfo.setCloseView(closeView);

        nativePrepareInfo.setClickViewList(clickViewList);//bind click view list

        if (nativePrepareInfo instanceof ATNativePrepareExInfo) {
            List<View> creativeClickViewList = new ArrayList<>();//click views
            creativeClickViewList.add(callToActionView);
            ((ATNativePrepareExInfo) nativePrepareInfo).setCreativeClickViewList(creativeClickViewList);//bind custom view list
        }
        return nativePrepareInfo;
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }

    public int dip2px(float dipValue) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
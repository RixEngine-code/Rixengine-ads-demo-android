package com.admob.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.mediation.Adapter;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationBannerAd;
import com.google.android.gms.ads.mediation.MediationBannerAdCallback;
import com.google.android.gms.ads.mediation.MediationBannerAdConfiguration;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.MediationInterstitialAd;
import com.google.android.gms.ads.mediation.MediationInterstitialAdCallback;
import com.google.android.gms.ads.mediation.MediationInterstitialAdConfiguration;
import com.google.android.gms.ads.mediation.MediationNativeAdCallback;
import com.google.android.gms.ads.mediation.MediationNativeAdConfiguration;
import com.google.android.gms.ads.mediation.MediationRewardedAd;
import com.google.android.gms.ads.mediation.MediationRewardedAdCallback;
import com.google.android.gms.ads.mediation.MediationRewardedAdConfiguration;
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper;
import com.google.android.gms.ads.mediation.VersionInfo;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.rixengine.api.AlxAdParam;
import com.rixengine.api.AlxAdSDK;
import com.rixengine.api.AlxBannerView;
import com.rixengine.api.AlxBannerViewAdListener;
import com.rixengine.api.AlxImage;
import com.rixengine.api.AlxInterstitialAD;
import com.rixengine.api.AlxInterstitialADListener;
import com.rixengine.api.AlxRewardVideoAD;
import com.rixengine.api.AlxRewardVideoADListener;
import com.rixengine.api.AlxSdkInitCallback;
import com.rixengine.api.nativead.AlxMediaView;
import com.rixengine.api.nativead.AlxNativeAd;
import com.rixengine.api.nativead.AlxNativeAdLoadedListener;
import com.rixengine.api.nativead.AlxNativeAdLoader;
import com.rixengine.api.nativead.AlxNativeAdView;
import com.rixengine.api.nativead.AlxNativeEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Admob ads AlgoriX Adapter (四合一。include: banner、native、interstitial、reward)
 */
public class AlgorixMediationAdapter extends Adapter implements MediationBannerAd, MediationInterstitialAd, MediationRewardedAd {
    private static final String TAG = "AlgorixMediationAdapter";

    private static final String ADAPTER_VERSION = "3.8.0";

    private static final String ALX_AD_UNIT_KEY = "parameter";

    private String unitid = "";
    private String appid = "";
    private String sid = "";
    private String token = "";
    private String host = "";
    private Boolean isdebug = false;

    //banner
    private MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback> mBannerLoadCallback;
    private MediationBannerAdCallback mBannerEventCallback;
    AlxBannerView mBannerView;

    //native
    private MediationAdLoadCallback<UnifiedNativeAdMapper, MediationNativeAdCallback> mNativeLoadCallback;
    private MediationNativeAdCallback mNativeEventCallback;
    private AlxNativeAd nativeAd;
    private CustomNativeAdMapper nativeAdMapper;

    //interstitial
    private MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback> mInterstitialLoadCallback;
    private MediationInterstitialAdCallback mInterstitialEventCallback;
    private AlxInterstitialAD interstitialAd;

    //reward
    private MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback> mRewardLoadCallback;
    private MediationRewardedAdCallback mRewardEventCallback;
    private AlxRewardVideoAD rewardVideoAd;

    @Override
    public void initialize(Context context, InitializationCompleteCallback initializationCompleteCallback, List<MediationConfiguration> list) {
        Log.d(TAG, "alx-admob-adapter: initialize");
        if (context == null) {
            initializationCompleteCallback.onInitializationFailed(
                    "Initialization Failed: Context is null.");
            return;
        }
        initializationCompleteCallback.onInitializationSucceeded();
    }

    @Override
    public void loadBannerAd(@NonNull MediationBannerAdConfiguration configuration, @NonNull MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback> callback) {
        Log.d(TAG, "alx-admob-adapter-version:" + ADAPTER_VERSION);
        Log.d(TAG, "alx-admob-adapter: loadBannerAd");
        mBannerLoadCallback = callback;
        String parameter = configuration.getServerParameters().getString(ALX_AD_UNIT_KEY);
        if (!TextUtils.isEmpty(parameter)) {
            parseServer(parameter);
        }
        initSdk(configuration.getContext(), callback, 1);
    }

    //banner ad
    @NonNull
    @Override
    public View getView() {
        return mBannerView;
    }

    @Override
    public void loadNativeAd(@NonNull MediationNativeAdConfiguration configuration, @NonNull MediationAdLoadCallback<UnifiedNativeAdMapper, MediationNativeAdCallback> callback) {
        Log.d(TAG, "alx-admob-adapter-version:" + ADAPTER_VERSION);
        Log.d(TAG, "alx-admob-adapter: loadNativeAd");
        mNativeLoadCallback = callback;
        String parameter = configuration.getServerParameters().getString(ALX_AD_UNIT_KEY);
        if (!TextUtils.isEmpty(parameter)) {
            parseServer(parameter);
        }
        initSdk(configuration.getContext(), callback, 2);
    }

    @Override
    public void loadInterstitialAd(@NonNull MediationInterstitialAdConfiguration configuration, @NonNull MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback> callback) {
        Log.d(TAG, "alx-admob-adapter-version:" + ADAPTER_VERSION);
        Log.d(TAG, "alx-admob-adapter: loadInterstitialAd");
        mInterstitialLoadCallback = callback;
        String parameter = configuration.getServerParameters().getString(ALX_AD_UNIT_KEY);
        if (!TextUtils.isEmpty(parameter)) {
            parseServer(parameter);
        }
        initSdk(configuration.getContext(), callback, 3);
    }

    @Override
    public void loadRewardedAd(@NonNull MediationRewardedAdConfiguration configuration, @NonNull MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback> callback) {
        Log.d(TAG, "alx-admob-adapter-version:" + ADAPTER_VERSION);
        Log.d(TAG, "alx-admob-adapter: loadRewardedAd");
        mRewardLoadCallback = callback;
        String parameter = configuration.getServerParameters().getString(ALX_AD_UNIT_KEY);
        if (!TextUtils.isEmpty(parameter)) {
            parseServer(parameter);
        }
        initSdk(configuration.getContext(), callback, 4);
    }

    //interstitial、reward 共用
    @Override
    public void showAd(@NonNull Context context) {
        if (interstitialAd != null) {
            if (!interstitialAd.isReady()) {
                if (mInterstitialEventCallback != null) {
                    mInterstitialEventCallback.onAdFailedToShow(new AdError(1, "isReady: Ad not loaded or Ad load failed"
                            , AlxAdSDK.getNetWorkName()));
                }
                return;
            }
            if (context != null && context instanceof Activity) {
                interstitialAd.show((Activity) context);
            } else {
                Log.i(TAG, "context is not an Activity");
                interstitialAd.show(null);
            }
        } else if (rewardVideoAd != null) {
            if (!rewardVideoAd.isReady()) {
                if (mRewardEventCallback != null) {
                    mRewardEventCallback.onAdFailedToShow(new AdError(1, "isReady: Ad not loaded or Ad load failed"
                            , AlxAdSDK.getNetWorkName()));
                }
                return;
            }
            if (context != null && context instanceof Activity) {
                rewardVideoAd.showVideo((Activity) context);
            } else {
                Log.i(TAG, "context is not an Activity");
                rewardVideoAd.showVideo(null);
            }
        }
    }

    private void initSdk(final Context context, final MediationAdLoadCallback callback, final int type) {
        if (TextUtils.isEmpty(unitid)) {
            Log.d(TAG, "alx unitid is empty");
            loadError(callback, 1, "alx unitid is empty.");
            return;
        }
        if (TextUtils.isEmpty(sid)) {
            Log.d(TAG, "alx sid is empty");
            loadError(callback, 1, "alx sid is empty.");
            return;
        }
        if (TextUtils.isEmpty(appid)) {
            Log.d(TAG, "alx appid is empty");
            loadError(callback, 1, "alx appid is empty.");
            return;
        }
        if (TextUtils.isEmpty(token)) {
            Log.d(TAG, "alx token is empty");
            loadError(callback, 1, "alx token is empty");
            return;
        }
        if (TextUtils.isEmpty(host)) {
            Log.d(TAG, "alx host is empty");
            loadError(callback, 1, "alx host is empty");
            return;
        }

        try {
            Log.i(TAG, "alx host: " + host + " alx token: " + token + " alx appid: " + appid + "alx sid: " + sid);
            // init
            AlxAdSDK.init(context, host, token, sid, appid, new AlxSdkInitCallback() {
                @Override
                public void onInit(boolean isOk, String msg) {
                    switch (type) {
                        case 1:
                            requestBannerAd(context);
                            break;
                        case 2:
                            requestNativeAd(context);
                            break;
                        case 3:
                            requestInterstitialAd(context);
                            break;
                        case 4:
                            requestRewardAd(context);
                            break;
                    }
                }
            });
//            // set GDPR
//            AlxAdSDK.setSubjectToGDPR(true);
//            // set GDPR Consent
//            AlxAdSDK.setUserConsent("1");
//            // set COPPA
//            AlxAdSDK.setBelowConsentAge(true);
//            // set CCPA
//            AlxAdSDK.subjectToUSPrivacy("1YYY");
            AlxAdSDK.setDebug(isdebug);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            loadError(callback, 1, "alx sdk init error");
        }
    }

    private void requestBannerAd(final Context context) {
        mBannerView = new AlxBannerView(context);
        // auto refresh ad  default = open = 1, 0 = close
        mBannerView.setBannerRefresh(0);
        //mBannerView.setBannerRefresh(15);
        final AlxBannerViewAdListener alxBannerADListener = new AlxBannerViewAdListener() {
            @Override
            public void onAdLoaded() {
                if (mBannerLoadCallback != null) {
                    mBannerEventCallback = mBannerLoadCallback.onSuccess(AlgorixMediationAdapter.this);
                }
            }

            @Override
            public void onAdError(int errorCode, String errorMsg) {
                loadError(mBannerLoadCallback, errorCode, errorMsg);
            }

            @Override
            public void onAdClicked() {
                if (mBannerEventCallback != null) {
                    mBannerEventCallback.reportAdClicked();
                }
            }

            @Override
            public void onAdShow() {
                if (mBannerEventCallback != null) {
                    mBannerEventCallback.reportAdImpression();
                    mBannerEventCallback.onAdOpened();
                }
            }

            @Override
            public void onAdClose() {
                if (mBannerEventCallback != null) {
                    mBannerEventCallback.onAdClosed();
                }
            }
        };
        mBannerView.loadAd(unitid, alxBannerADListener);
    }

    private void requestNativeAd(final Context context) {
        AlxNativeAdLoader loader = new AlxNativeAdLoader.Builder(context, unitid).build();
        loader.loadAd(new AlxAdParam.Builder().build(), new AlxNativeAdLoadedListener() {
            @Override
            public void onAdFailed(int errorCode, String errorMsg) {
                Log.i(TAG, "onAdLoadedFail:" + errorCode + ";" + errorMsg);
                loadError(mNativeLoadCallback, errorCode, errorMsg);
            }

            @Override
            public void onAdLoaded(List<AlxNativeAd> list) {
                Log.i(TAG, "onAdLoaded:");

                if (list == null || list.isEmpty()) {
                    loadError(mNativeLoadCallback, 100, "no data ads");
                    return;
                }

                try {
                    nativeAd = list.get(0);
                    if (nativeAd == null) {
                        loadError(mNativeLoadCallback, 100, "no data ads");
                        return;
                    }


                    nativeAdMapper = new CustomNativeAdMapper(context, nativeAd);
                    if (mNativeLoadCallback != null) {
                        Log.i(TAG, "onAdLoaded:listener-ok");
                        mNativeEventCallback = mNativeLoadCallback.onSuccess(nativeAdMapper);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                    loadError(mNativeLoadCallback, 101, e.getMessage());
                }

            }
        });
    }

    private void requestInterstitialAd(final Context context) {
        interstitialAd = new AlxInterstitialAD();
        interstitialAd.load(context, unitid, new AlxInterstitialADListener() {

            @Override
            public void onInterstitialAdLoaded() {
                if (mInterstitialLoadCallback != null) {
                    mInterstitialEventCallback = (MediationInterstitialAdCallback) mInterstitialLoadCallback.onSuccess(AlgorixMediationAdapter.this);
                }
            }

            @Override
            public void onInterstitialAdLoadFail(int errorCode, String errorMsg) {
                loadError(mInterstitialLoadCallback, errorCode, errorMsg);
            }

            @Override
            public void onInterstitialAdClicked() {
                if (mInterstitialEventCallback != null) {
                    mInterstitialEventCallback.reportAdClicked();
                }
            }

            @Override
            public void onInterstitialAdShow() {
                if (mInterstitialEventCallback != null) {
                    mInterstitialEventCallback.reportAdImpression();
                    mInterstitialEventCallback.onAdOpened();
                }
            }

            @Override
            public void onInterstitialAdClose() {
                if (mInterstitialEventCallback != null) {
                    mInterstitialEventCallback.onAdClosed();
                }
            }

            @Override
            public void onInterstitialAdVideoStart() {

            }

            @Override
            public void onInterstitialAdVideoEnd() {

            }

            @Override
            public void onInterstitialAdVideoError(int errorCode, String errorMsg) {

            }
        });
    }

    private void requestRewardAd(final Context context) {
        rewardVideoAd = new AlxRewardVideoAD();
        rewardVideoAd.load(context, unitid, new AlxRewardVideoADListener() {
            @Override
            public void onRewardedVideoAdLoaded(AlxRewardVideoAD var1) {
                Log.d(TAG, "onRewardedVideoAdLoaded");
                if (mRewardLoadCallback != null)
                    mRewardEventCallback = (MediationRewardedAdCallback) mRewardLoadCallback
                            .onSuccess(AlgorixMediationAdapter.this);
            }


            @Override
            public void onRewardedVideoAdFailed(AlxRewardVideoAD var1, int errCode, String errMsg) {
                Log.d(TAG, "onRewardedVideoAdFailed: " + errMsg);
                loadError(mRewardLoadCallback, errCode, errMsg);
            }

            @Override
            public void onRewardedVideoAdPlayStart(AlxRewardVideoAD var1) {
                if (mRewardEventCallback != null) {
                    mRewardEventCallback.reportAdImpression();
                    mRewardEventCallback.onAdOpened();
                    mRewardEventCallback.onVideoStart();
                }
            }

            @Override
            public void onRewardedVideoAdPlayEnd(AlxRewardVideoAD var1) {
                Log.d(TAG, "onRewardedVideoAdPlayEnd: ");
                if (mRewardEventCallback != null)
                    mRewardEventCallback.onVideoComplete();
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AlxRewardVideoAD var2, int errCode, String errMsg) {
                Log.d(TAG, "onShowFail: " + errMsg);
                if (mRewardEventCallback != null)
                    mRewardEventCallback.onAdFailedToShow(new AdError(errCode, errMsg, AlxAdSDK.getNetWorkName()));
            }

            @Override
            public void onRewardedVideoAdClosed(AlxRewardVideoAD var1) {
                Log.d(TAG, "onRewardedVideoAdClosed: ");
                if (mRewardEventCallback != null) {
                    mRewardEventCallback.onAdClosed();
                }
            }

            @Override
            public void onRewardedVideoAdPlayClicked(AlxRewardVideoAD var1) {
                Log.d(TAG, "onRewardedVideoAdPlayClicked: ");
                if (mRewardEventCallback != null)
                    mRewardEventCallback.reportAdClicked();
            }

            @Override
            public void onReward(AlxRewardVideoAD var1) {
                Log.d(TAG, "onReward: ");
                if (mRewardEventCallback != null) {
                    mRewardEventCallback.onUserEarnedReward(new RewardItem() {
                        @Override
                        public String getType() {
                            return "";
                        }

                        @Override
                        public int getAmount() {
                            return 1;
                        }
                    });
                }
            }
        });
    }

    private void parseServer(String s) {
        if (TextUtils.isEmpty(s)) {
            Log.d(TAG, "serviceString  is empty ");
            return;
        }
        Log.d(TAG, "serviceString   " + s);
        try {
            JSONObject json = new JSONObject(s);
            host = json.getString("host");
            appid = json.getString("appid");
            sid = json.getString("sid");
            token = json.getString("token");
            unitid = json.getString("unitid");
            String debug = json.optString("isdebug", "false");
            if (TextUtils.equals(debug, "true")) {
                isdebug = true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void loadError(MediationAdLoadCallback callback, int code, String message) {
        if (callback != null) {
            callback.onFailure(new AdError(code, message, AlxAdSDK.getNetWorkName()));
        }
    }


    @Override
    public VersionInfo getVersionInfo() {
        String versionString = AlxAdSDK.getNetWorkVersion();
        VersionInfo result = getAdapterVersionInfo(versionString);
        if (result != null) {
            return result;
        }
        return new VersionInfo(0, 0, 0);
    }

    @Override
    public VersionInfo getSDKVersionInfo() {
        String versionString = AlxAdSDK.getNetWorkVersion();
        VersionInfo result = getAdapterVersionInfo(versionString);
        if (result != null) {
            return result;
        }
        return new VersionInfo(0, 0, 0);
    }

    private VersionInfo getAdapterVersionInfo(String version) {
        if (TextUtils.isEmpty(version)) {
            return null;
        }
        try {
            String[] arr = version.split("\\.");
            if (arr == null || arr.length < 3) {
                return null;
            }
            int major = Integer.parseInt(arr[0]);
            int minor = Integer.parseInt(arr[1]);
            int micro = Integer.parseInt(arr[2]);
            return new VersionInfo(major, minor, micro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class CustomNativeAdMapper extends UnifiedNativeAdMapper {

        private AlxNativeAd bean;
        private Context context;
        private AlxNativeAdView mRootView;

        public CustomNativeAdMapper(Context context, AlxNativeAd bean) {
            this.bean = bean;
            this.context = context;
            bindListener();
            init();
        }

        private void init() {
            if (bean == null) {
                return;
            }
            setHeadline(bean.getTitle());
            setBody(bean.getDescription());
            setPrice(bean.getPrice() + "");
            setAdvertiser(bean.getAdSource());
            setCallToAction(bean.getCallToAction());
            setIcon(new SimpleImage(bean.getIcon()));
            setImages(getImageList());
            setHasVideoContent(bean.getMediaContent().hasVideo());

            mRootView = new AlxNativeAdView(context);
            AlxMediaView mediaView = new AlxMediaView(context);
            mediaView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mediaView.setMediaContent(bean.getMediaContent());
            mRootView.setMediaView(mediaView);
            setMediaView(mediaView);
        }

        @Override
        public void trackViews(@NonNull View view, @NonNull Map<String, View> map, @NonNull Map<String, View> map1) {
            Log.i(TAG, "trackViews");
            if (view instanceof ViewGroup) {
                Log.i(TAG, "trackViews: rootView is ViewGroup");
                ViewGroup rootView = (ViewGroup) view;
                try {
                    if (mRootView != null) {
                        rootView.removeView(mRootView);
                    }
                    if (mRootView == null) {
                        mRootView = new AlxNativeAdView(context);
                    }
                    mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    if (map != null && !map.isEmpty()) {
                        for (Map.Entry<String, View> entry : map.entrySet()) {
                            Log.i(TAG, "register:key=" + entry.getKey());
                            mRootView.addView(entry.getKey(), entry.getValue());
                        }
                    }
                    if (map1 != null && !map1.isEmpty()) {
                        for (Map.Entry<String, View> entry : map1.entrySet()) {
                            Log.i(TAG, "register2:key=" + entry.getKey());
                        }
                    }
                    mRootView.setNativeAd(bean);
                    rootView.addView(mRootView, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            } else {
                Log.i(TAG, "trackViews: rootView is other");
            }
        }

        @Override
        public void untrackView(@NonNull View view) {
            Log.i(TAG, "untrackView");
        }

        private List<NativeAd.Image> getImageList() {
            List<NativeAd.Image> imageList = new ArrayList<>();
            if (bean.getImages() != null && bean.getImages().size() > 0) {
                for (AlxImage item : bean.getImages()) {
                    if (item != null) {
                        imageList.add(new SimpleImage(item));
                    }
                }
            }
            return imageList;
        }

        public AlxNativeAdView getAlgorixAdView() {
            return mRootView;
        }

        private void bindListener() {
            if (bean == null) {
                return;
            }
            bean.setNativeEventListener(new AlxNativeEventListener() {
                @Override
                public void onAdClicked() {
                    Log.d(TAG, "onAdClick");
                    if (mNativeEventCallback != null) {
                        mNativeEventCallback.reportAdClicked();
                    }
                }

                @Override
                public void onAdImpression() {
                    Log.d(TAG, "onAdShow");
                    if (mNativeEventCallback != null) {
                        mNativeEventCallback.reportAdImpression();
                        mNativeEventCallback.onAdOpened();
                    }
                }

                @Override
                public void onAdClosed() {
                    Log.d(TAG, "onAdClose");
                    if (mNativeEventCallback != null) {
                        mNativeEventCallback.onAdClosed();
                    }
                }
            });
        }


        private class SimpleImage extends NativeAd.Image {

            private AlxImage image;

            public SimpleImage(AlxImage image) {
                this.image = image;
            }

            @Override
            public double getScale() {
                return 0;
            }

            @Nullable
            @Override
            public Drawable getDrawable() {
                return null;
            }

            @Nullable
            @Override
            public Uri getUri() {
                if (image != null) {
                    return Uri.parse(image.getImageUrl());
                }
                return null;
            }
        }
    }

}

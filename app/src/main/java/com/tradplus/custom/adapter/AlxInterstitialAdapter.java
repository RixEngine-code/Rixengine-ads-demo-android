package com.tradplus.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.rixengine.api.AlxAdSDK;
import com.rixengine.api.AlxInterstitialAD;
import com.rixengine.api.AlxInterstitialADListener;
import com.rixengine.api.AlxSdkInitCallback;
import com.tradplus.ads.base.adapter.interstitial.TPInterstitialAdapter;
import com.tradplus.ads.base.common.TPError;

import java.util.Map;

/**
 * TradPlus平台 插屏广告适配器
 */
public class AlxInterstitialAdapter extends TPInterstitialAdapter {

    private static final String TAG = "AlxInterstitialAdapter";

    private AlxInterstitialAD alxInterstitialAD;
    private String unitid = "";
    private String appid = "";
    private String sid = "";
    private String token = "";
    private String host = "";
    private Boolean isdebug = false;
    private Context mContext;


    @Override
    public void loadCustomAd(Context context, Map<String, Object> userParams, Map<String, String> tpParams) {
        Log.d(TAG, "alx-tradplus-adapter-version:" + AlxMetaInf.ADAPTER_VERSION);
        Log.i(TAG, "loadCustomAd");
        mContext = context;
        if (tpParams != null && parseServer(tpParams)) {
            initSdk(context);
        } else {
            if (mLoadAdapterListener != null) {
                mLoadAdapterListener.loadAdapterLoadFailed(new TPError("alx host | unitid | token | sid | appid is empty."));
            }
        }
    }

    private boolean parseServer(Map<String, String> serverExtras) {
        try {
            if (serverExtras.containsKey("host")) {
                host = serverExtras.get("host");
            }
            if (serverExtras.containsKey("appid")) {
                appid = serverExtras.get("appid");
            }
            if (serverExtras.containsKey("sid")) {
                sid = serverExtras.get("sid");
            }
            if (serverExtras.containsKey("token")) {
                token = serverExtras.get("token");
            }
            if (serverExtras.containsKey("unitid")) {
                unitid = serverExtras.get("unitid");
            }

            if (serverExtras.containsKey("isdebug")) {
                String debug = serverExtras.get("isdebug");
                Log.e(TAG, "alx debug mode:" + debug);
                if (TextUtils.equals(debug, "true")) {
                    isdebug = true;
                } else {
                    isdebug = false;
                }
            } else {
                Log.e(TAG, "alx debug mode: false");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(host) || TextUtils.isEmpty(unitid) || TextUtils.isEmpty(token) || TextUtils.isEmpty(sid) || TextUtils.isEmpty(appid)) {
            Log.i(TAG, "alx host | unitid | token | sid | appid is empty");
            return false;
        }
        return true;
    }

    private void initSdk(final Context context) {
        try {
            Log.i(TAG, "alx ver:" + AlxAdSDK.getNetWorkVersion() + " alx host: " + host + " alx token: " + token + " alx appid: " + appid + " alx sid: " + sid);

            AlxAdSDK.setDebug(isdebug);
            AlxAdSDK.init(context, host, token, sid, appid, new AlxSdkInitCallback() {
                @Override
                public void onInit(boolean isOk, String msg) {
                    //if (isOk){
                    startAdLoad(context);
                    //}
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startAdLoad(Context context) {
        alxInterstitialAD = new AlxInterstitialAD();
        alxInterstitialAD.load(context, unitid, new AlxInterstitialADListener() {

            @Override
            public void onInterstitialAdLoaded() {
                if (mLoadAdapterListener != null) {
                    mLoadAdapterListener.loadAdapterLoaded(null);
                }
            }

            @Override
            public void onInterstitialAdLoadFail(int errorCode, String errorMsg) {
                if (mLoadAdapterListener != null) {
                    mLoadAdapterListener.loadAdapterLoadFailed(new TPError(errorCode + "", errorMsg));
                }
            }

            @Override
            public void onInterstitialAdClicked() {
                if (mShowListener != null) {
                    mShowListener.onAdClicked();
                }
            }

            @Override
            public void onInterstitialAdShow() {
                if (mShowListener != null) {
                    mShowListener.onAdShown();
                }
            }

            @Override
            public void onInterstitialAdClose() {
                if (mShowListener != null) {
                    mShowListener.onAdClosed();
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
                if (mShowListener != null) {
                    mShowListener.onAdVideoError(new TPError(String.valueOf(errorCode), errorMsg));
                }
            }
        });
    }


    @Override
    public boolean isReady() {
        if (alxInterstitialAD != null) {
            return alxInterstitialAD.isReady();
        }
        return false;
    }


    @Override
    public void showAd() {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (alxInterstitialAD != null && alxInterstitialAD.isReady()) {
                alxInterstitialAD.show(activity);
            } else {
                if (mShowListener != null) {
                    mShowListener.onAdVideoError(new TPError(TPError.SHOW_FAILED));
                }
            }
        } else {
            Log.e(TAG, "context is not an Activity");
        }
    }

    @Override
    public void clean() {
        if (alxInterstitialAD != null) {
            alxInterstitialAD.destroy();
            alxInterstitialAD = null;
        }
    }

    @Override
    public String getNetworkVersion() {
        return AlxAdSDK.getNetWorkVersion();
    }

    @Override
    public String getNetworkName() {
        return AlxAdSDK.getNetWorkName();
    }
}
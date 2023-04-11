package com.rixengine.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rixengine.demo.admob.AdmobDemoListActivity;
import com.rixengine.demo.rixengine.AdListActivity;
import com.rixengine.demo.ironsource.IronSourceDemoListActivity;
import com.rixengine.demo.max.MaxDemoListActivity;
import com.rixengine.demo.topon.TopOnAdDemoListActivity;
import com.rixengine.demo.tradplus.TradPlusDemoListActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseListActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    //Applying for relevant permissions can push AD resources more accurately
    String[] mPermissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initPermission();
        }
    }

    /**
     * Authority judgment and application
     */
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String strPermission : mPermissions) {
                if (ContextCompat.checkSelfPermission(this,
                        strPermission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, mPermissions, 6);
                }
            }
        }
    }

    @Override
    public List<AdapterData> initAdapterData() {
        List<AdapterData> list = new ArrayList<>();
        AdapterData item = new AdapterData("Rixengine AD Demo", AdListActivity.class);
        list.add(item);

        item = new AdapterData("Admob AD Demo", AdmobDemoListActivity.class);
        list.add(item);

        item = new AdapterData("TopOn AD Demo", TopOnAdDemoListActivity.class);
        list.add(item);

        item = new AdapterData("TradPlus AD Demo", TradPlusDemoListActivity.class);
        list.add(item);

        item = new AdapterData("IronSource AD Demo", IronSourceDemoListActivity.class);
        list.add(item);

        item = new AdapterData("Max AD Demo", MaxDemoListActivity.class);
        list.add(item);

        return list;
    }


}
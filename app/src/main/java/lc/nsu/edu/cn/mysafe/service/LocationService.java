package lc.nsu.edu.cn.mysafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;

import lc.nsu.edu.cn.mysafe.utils.ConstantValue;
import lc.nsu.edu.cn.mysafe.utils.SpUtil;

/**
 * Created by 刘畅 on 2017/7/12.
 */
public class LocationService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);
        MyLocationListener myLocationListener = new MyLocationListener();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        }
        lm.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);

    }

    class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(
                    SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, ""),
                    null,
                    "longitude="+longitude+"latitude="+latitude,
                    null,
                    null
            );
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

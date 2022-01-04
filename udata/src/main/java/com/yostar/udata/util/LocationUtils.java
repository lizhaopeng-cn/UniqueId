package com.yostar.udata.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;

public class LocationUtils {
    private static String TAG = LocationUtils.class.getSimpleName();
    private static LocationUtils mInstance;
    private Context mContext;
    private static LocationListener mLocationListener = new LocationListener() {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged");
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled");

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
        }
    };


    private LocationUtils(Context context) {
        this.mContext = context;
    }

    public static LocationUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocationUtils(context.getApplicationContext());
        }
        return mInstance;
    }

    private boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null
                && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 获取地理位置，先根据GPS获取，再根据网络获取
     *
     * @return
     */
    public Location getLocation() {
        Location location = null;
        try {
            if (mContext == null) {
                return null;
            }
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager == null) {
                return null;
            }
            if (isWifi(mContext)) {
                location = getLocationByNetwork();
            } else {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //从gps获取经纬度
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {//当GPS信号弱没获取到位置的时候再从网络获取
                        location = getLocationByNetwork();
                    }
                } else {    //从网络获取经纬度
                    location = getLocationByNetwork();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return location;
    }

    /**
     * 判断是否开启了GPS或网络定位开关
     *
     * @return
     */
    public boolean isLocationProviderEnabled() {
        boolean result = false;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return result;
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            result = true;
        }
        return result;
    }

    /**
     * 获取地理位置，先根据GPS获取，再根据网络获取
     *
     * @return
     */
    private Location getLocationByNetwork() {
        Location location = null;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mLocationListener);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return location;
    }

    //获取地址信息:城市、街道等信息
    private Address getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(mContext, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * 国家
     */
    public String getCountry() {
        Address address = getAddress(getLocation());
        if (address != null) {
            return address.getCountryName();
        } else {
            return "";
        }
    }

    /**
     * 省份
     */
    public String getProvince() {
        Address address = getAddress(getLocation());
        if (address != null) {
            Log.e(TAG, address.getAdminArea());
            return address.getAdminArea();
        } else {
            Log.e(TAG, "address.getAdminArea()");
            return "";
        }
    }

    /**
     * 城市
     */
    public String getCity() {
        Address address = getAddress(getLocation());
        if (address != null) {
            return address.getLocality();
        } else {
            return "";
        }
    }

    /**
     * 经度
     */
    public String getLongitude() {
        Location location = getLocation();
        if (location != null) {
            return String.valueOf(location.getLongitude());
        } else {
            return "";
        }
    }

    /**
     * 纬度
     */
    public String getLatitude() {
        Location location = getLocation();
        if (location != null) {
            return String.valueOf(location.getLatitude());
        } else {
            return "";
        }
    }

}

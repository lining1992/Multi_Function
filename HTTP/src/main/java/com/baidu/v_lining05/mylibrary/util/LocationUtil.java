//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.util;

import android.content.Context;
import android.text.TextUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import java.text.DecimalFormat;

public class LocationUtil implements BDLocationListener, INoProguard {
    private static final String TAG = "LocationUtil";
    private Context mContext;
    private static final int SPAN_MIL = 10000;
    private static final int SAFE_POSITION_DURATION_TIME = 30;
    private static final int SAFE_POSITION_DURATION_MS = 300000;
    public static final String COORDINATE_SYSTEM_GCJ02 = "gcj02ll";
    public static final String COORDINATE_SYSTEM_BD09 = "bd09ll";
    private String mCoordinateSystem = "gcj02ll";
    private static final String KEY_LAST_LATITUDE = "last_latitude";
    private static final String KEY_LAST_LONGITUDE = "last_longitude";
    private static final String KEY_LAST_LATITUDE_BD09LL = "last_latitude_bd09ll";
    private static final String KEY_LAST_LONGITUDE_BD09LL = "last_longitude_bd09ll";
    private static final double DEFAULT_LATITUDE = 39.912733D;
    private static final double DEFAULT_LONGITUDE = 116.403963D;
    private static final double DEFAULT_LATITUDE_BD09LL = 39.912733D;
    private static final double DEFAULT_LONGITUDE_BD09LL = 116.403963D;
    private static LocationUtil mInstance;
    private LocationClient mLocationClient;
    private BDLocation mLocationGCJ02;
    private BDLocation mLocationBD09LL;
    private double mLastLatitude;
    private double mLastLongitude;
    private double mLastLatitudeBd09ll;
    private double mLastLongitudeBd09ll;
    private int mRequestLocCnt = 0;
    private LocationUtil.LocationListener mLocationListener;

    private LocationUtil() {
    }

    public static LocationUtil getInstance() {
        if(mInstance == null) {
            Class var0 = LocationUtil.class;
            synchronized(LocationUtil.class) {
                if(mInstance == null) {
                    mInstance = new LocationUtil();
                    return mInstance;
                }
            }
        }

        return mInstance;
    }

    public String getCoordinateSysmem() {
        return this.mCoordinateSystem;
    }

    public void setCoordinateSystem(String coordinateSystem) {
        this.mCoordinateSystem = coordinateSystem;
    }

    private void init(Context context) {
        this.mLocationClient = new LocationClient(context.getApplicationContext());
        this.mLocationClient.setLocOption(this.initLocationOption());
        this.mLocationClient.registerLocationListener(this);
    }

    public void setLocationListener(LocationUtil.LocationListener locationListener) {
        this.mLocationListener = locationListener;
    }

    public void start(Context context) {
        this.mContext = context;
        if(this.mLocationClient == null) {
            this.init(context);
        }

        this.mLocationClient.start();
        this.getLastPosition();
    }

    public void stop() {
        if(this.mLocationClient != null) {
            this.mLocationClient.unRegisterLocationListener(this);
            this.mLocationClient.stop();
        }
    }

    public void safeLastPosition() {
        LogUtil.d("LocationUtil", "safe position to shared_prefs");
        SharePreferenceUtil.setLong(this.mContext, "last_latitude", (long)(this.mLastLatitude * 1000000.0D));
        SharePreferenceUtil.setLong(this.mContext, "last_longitude", (long)(this.mLastLongitude * 1000000.0D));
        SharePreferenceUtil.setLong(this.mContext, "last_latitude_bd09ll", (long)(this.mLastLatitudeBd09ll * 1000000.0D));
        SharePreferenceUtil.setLong(this.mContext, "last_longitude_bd09ll", (long)(this.mLastLongitudeBd09ll * 1000000.0D));
    }

    public void getLastPosition() {
        this.mLastLatitude = (double)SharePreferenceUtil.getLong(this.mContext, "last_latitude", 39912733L);
        this.mLastLatitude /= 1000000.0D;
        this.mLastLongitude = (double)SharePreferenceUtil.getLong(this.mContext, "last_longitude", 116403963L);
        this.mLastLongitude /= 1000000.0D;
        this.mLastLatitudeBd09ll = (double)SharePreferenceUtil.getLong(this.mContext, "last_latitude_bd09ll", 39912733L);
        this.mLastLatitudeBd09ll /= 1000000.0D;
        this.mLastLongitudeBd09ll = (double)SharePreferenceUtil.getLong(this.mContext, "last_longitude_bd09ll", 116403963L);
        this.mLastLongitudeBd09ll /= 1000000.0D;
    }

    public boolean isReady() {
        return this.mLocationGCJ02 != null;
    }

    public String getCity() {
        if(this.mLocationGCJ02 != null && this.mLocationGCJ02.getCity() != null) {
            return this.mLocationGCJ02.getCity();
        } else {
            LogUtil.e("LocationUtil", "mLocationGCJ02 null!!!");
            return "";
        }
    }

    public double getLatitude() {
        if(this.mLocationGCJ02 != null) {
            this.mLastLatitude = this.mLocationGCJ02.getLatitude();
        } else {
            LogUtil.e("LocationUtil", "mLocationGCJ02 null!!!");
        }

        LogUtil.d("LocationUtil", "mLastLatitude = " + this.mLastLatitude);
        return this.mLastLatitude;
    }

    public double getLongitude() {
        if(this.mLocationGCJ02 != null) {
            this.mLastLongitude = this.mLocationGCJ02.getLongitude();
        } else {
            LogUtil.e("LocationUtil", "mLocationGCJ02 null!!!");
        }

        LogUtil.d("LocationUtil", "mLastLongitude = " + this.mLastLongitude);
        return this.mLastLongitude;
    }

    public double calculateDistance(double lat, double lng) {
        LatLng from = new LatLng(this.getLatitude(), this.getLongitude());
        LatLng to = new LatLng(lat, lng);
        return DistanceUtil.getDistance(from, to);
    }

    public double getLatitudeBd09ll() {
        if(this.mLocationBD09LL != null) {
            this.mLastLatitudeBd09ll = this.mLocationBD09LL.getLatitude();
        } else {
            LogUtil.e("LocationUtil", "mLocationBD09LL null!!!");
        }

        LogUtil.d("LocationUtil", "mLastLatitudeBd09ll = " + this.mLastLatitudeBd09ll);
        return this.mLastLatitudeBd09ll;
    }

    public double getLongitudeBd09ll() {
        if(this.mLocationBD09LL != null) {
            this.mLastLongitudeBd09ll = this.mLocationBD09LL.getLongitude();
        } else {
            LogUtil.e("LocationUtil", "mLocationBD09LL null!!!");
        }

        LogUtil.d("LocationUtil", "mLastLongitudeBd09ll = " + this.mLastLongitudeBd09ll);
        return this.mLastLongitudeBd09ll;
    }

    public double getSpeed() {
        return this.mLocationGCJ02 != null && this.mLocationGCJ02.getSpeed() >= 0.0F?(double)this.mLocationGCJ02.getSpeed():-1.0D;
    }

    public double getHeight() {
        if(this.mLocationGCJ02 != null && this.mLocationGCJ02.getAltitude() >= 0.0D) {
            DecimalFormat df = new DecimalFormat("#.00");
            return Double.parseDouble(df.format(this.mLocationGCJ02.getAltitude()));
        } else {
            return -1.0D;
        }
    }

    public int getDirection() {
        return this.mLocationGCJ02 != null && this.mLocationGCJ02.getDirection() >= 0.0F?(int)this.mLocationGCJ02.getDirection():-1;
    }

    public double getRadius() {
        return this.mLocationGCJ02 != null && this.mLocationGCJ02.getRadius() >= 0.0F?(double)this.mLocationGCJ02.getRadius():-1.0D;
    }

    public String getLocationAddress() {
        LogUtil.d("LocationUtil", "getLocationAddress = " + this.mLocationGCJ02.getAddrStr());
        return this.mLocationGCJ02 != null && !TextUtils.isEmpty(this.mLocationGCJ02.getAddrStr())?this.mLocationGCJ02.getAddrStr():"";
    }

    private LocationClientOption initLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setScanSpan(10000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(true);
        option.setIsNeedAltitude(true);
        return option;
    }

    public void onReceiveLocation(BDLocation bdLocation) {
        if(bdLocation.getLocType() != 61 && bdLocation.getLocType() != 161 && bdLocation.getLocType() != 66) {
            LogUtil.e("LocationUtil", "getLocType: Type Error, type=" + bdLocation.getLocType());
        } else {
            this.mLocationGCJ02 = bdLocation;
            this.mLocationBD09LL = this.convertToBD09LL(bdLocation);
            this.mLastLongitude = bdLocation.getLongitude();
            this.mLastLatitude = bdLocation.getLatitude();
            this.mLastLongitudeBd09ll = this.mLocationBD09LL.getLongitude();
            this.mLastLatitudeBd09ll = this.mLocationBD09LL.getLatitude();
            if(this.mRequestLocCnt++ == 30) {
                this.safeLastPosition();
                this.mRequestLocCnt = 0;
            }

            if(this.mLocationListener != null) {
                this.mLocationListener.onReceiveLocation();
            }

        }
    }

    private BDLocation convertToBD09LL(BDLocation bdLocation) {
        return LocationClient.getBDLocationInCoorType(bdLocation, "bd09ll");
    }

    private BDLocation convertToBD09MC(BDLocation bdLocation) {
        return LocationClient.getBDLocationInCoorType(bdLocation, "bd09");
    }

    public interface LocationListener {
        void onReceiveLocation();
    }
}

package com.bokang.common_amap_location;

import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.*;

/**
 * @author JiaLei
 * Created on 2019/5/22 09:21
 * ━━━━━━神兽出没━━━━━━
 * 　　┏┓　　　┏┓+ +
 * 　┏┛┻━━━┛┻┓ + +
 * 　┃　　　　　　　┃
 * 　┃　　　━　　　┃ ++ + + +
 * ████━████ ┃+
 * 　┃　　　　　　　┃ +
 * 　┃　　　┻　　　┃
 * 　┃　　　　　　　┃ + +
 * 　┗━┓　　　┏━┛
 * 　　　┃　　　┃
 * 　　　┃　　　┃ + + + +
 * 　　　┃　　　┃
 * 　　　┃　　　┃ +  神兽保佑
 * 　　　┃　　　┃    代码无bug
 * 　　　┃　　　┃　　+
 * 　　　┃　 　　┗━━━┓ + +
 * 　　　┃ 　　　　　　　┣┓
 * 　　　┃ 　　　　　　　┏┛
 * 　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　┃┫┫　┃┫┫
 * 　　　　┗┻┛　┗┻┛+ + + +
 * ━━━━━━感觉萌萌哒━━━━━━
 */
public class AMapLoactionOptionBuilder {
    private GeoLanguage mGeoLanguage= GeoLanguage.DEFAULT;
    private AMapLocationMode mAMapLocationMode= AMapLocationMode.Battery_Saving;
    // 设置是否需要显示地址信息
    private boolean mNeedAddress=true;
    private boolean mGpsFirst=false;
    // 设置是否开启缓存
    private boolean mLocationCacheEnable=false;
    // 设置是否单次定位
    private boolean mOnceLocation=true;
    //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用(提高首次精度)
    private boolean mOnceLocationLatest=true;
    //设置是否使用传感器
    private boolean mSensorEnable=false;
    //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
    private boolean mWifiScan=true;
    //设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
    private long mInterval=2000;
    // 设置网络请求超时时间
    private long mHttpTimeOut=30000;


    public final AMapLoactionOptionBuilder geoLanguage(GeoLanguage geoLanguageType){
        mGeoLanguage=geoLanguageType;
        return this;
    }
    public final AMapLoactionOptionBuilder aMapLocationMode(AMapLocationMode mAMapLocationMode){
        this.mAMapLocationMode=mAMapLocationMode;
        return this;
    }
    public final AMapLoactionOptionBuilder needAddress(boolean mNeedAddress){
        this.mNeedAddress=mNeedAddress;
        return this;
    }

    /**
     * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
     * 注意：只有在高精度模式下的单次定位有效，其他方式无效
     */
    public final AMapLoactionOptionBuilder gpsFirst(boolean mGpsFirst){
        this.mGpsFirst=mGpsFirst;
        return this;
    }
    public final AMapLoactionOptionBuilder locationCacheEnable(boolean mboolean){
        this.mLocationCacheEnable=mboolean;
        return this;
    }
    public final AMapLoactionOptionBuilder onceLocation(boolean mboolean){
        this.mOnceLocation=mboolean;
        return this;
    }
    public final AMapLoactionOptionBuilder onceLocationLatest(boolean mboolean){
        this.mOnceLocationLatest=mboolean;
        return this;
    }
    public final AMapLoactionOptionBuilder sensorEnable(boolean mboolean){
        this.mSensorEnable=mboolean;
        return this;
    }
    public final AMapLoactionOptionBuilder wifiScan(boolean mboolean){
        this.mWifiScan=mboolean;
        return this;
    }
    public final AMapLoactionOptionBuilder interval(long time){
        this.mInterval=time;
        return this;
    }
    public final AMapLoactionOptionBuilder httpTimeOut(long time){
        this.mHttpTimeOut=time;
        return this;
    }
    public final AMapLocationClientOption build(){
        AMapLocationClientOption mAMapLocationClientOption=new AMapLocationClientOption();
        mAMapLocationClientOption.setLocationMode(mAMapLocationMode);
        mAMapLocationClientOption.setGeoLanguage(mGeoLanguage);
        mAMapLocationClientOption.setGpsFirst(mGpsFirst);
        mAMapLocationClientOption.setLocationCacheEnable(mLocationCacheEnable);
        mAMapLocationClientOption.setOnceLocation(mOnceLocation);
        mAMapLocationClientOption.setOnceLocationLatest(mOnceLocationLatest);
        mAMapLocationClientOption.setSensorEnable(mSensorEnable);
        mAMapLocationClientOption.setInterval(mInterval);
        mAMapLocationClientOption.setHttpTimeOut(mHttpTimeOut);
        return mAMapLocationClientOption;

    }


    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public static AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

}

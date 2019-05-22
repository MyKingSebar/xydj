package com.bokang.common_amap_location;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.Map;

/**
 * @author JiaLei
 * Created on 2019/5/21 20:38
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
public class AMapLocationClientBuilder {
    private Context mContext = null;
    private AMapLocationListener mAMapLocationListener = null;
    private AMapLocationClientOption mAMapLocationClientOption = null;

    public final AMapLocationClientBuilder context(Context context) {
        this.mContext = context;
        return this;
    }

    public final AMapLocationClientBuilder aMapLocationListener(AMapLocationListener mAMapLocationListener) {
        this.mAMapLocationListener = mAMapLocationListener;
        return this;
    }

    public final AMapLocationClientBuilder mAMapLocationClientOption(AMapLocationClientOption mAMapLocationClientOption) {
        this.mAMapLocationClientOption = mAMapLocationClientOption;
        return this;
    }

    public final AMapLocationClient build() {
        if (mContext == null) {
            throw new RuntimeException("AMapLocationClientBuilder.mContext must be initialize!!!");
        }
        if (mAMapLocationListener == null) {
            throw new RuntimeException("AMapLocationClientBuilder.mAMapLocationListener must be initialize!!!");
        }
        AMapLocationClient mAMapLocationClient = new AMapLocationClient(mContext);
        if (mAMapLocationClientOption != null) {
            mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
        } else {
            mAMapLocationClient.setLocationOption(AMapLoactionOptionBuilder.getDefaultOption());
        }
        mAMapLocationClient.setLocationListener(mAMapLocationListener);
        return mAMapLocationClient;
    }


}

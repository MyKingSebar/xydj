package com.example.common_tencent_tuikit;

import android.content.Context;

import com.tencent.qcloud.uikit.BaseUIKitConfigs;
import com.tencent.qcloud.uikit.TUIKit;

public class TuiKitConfig {
    public static final int SDKAPPID=1400181535;

    public static void initTencentTuiKit(Context context){
        TUIKit.init(context,SDKAPPID, BaseUIKitConfigs.getDefaultConfigs());
    }
}

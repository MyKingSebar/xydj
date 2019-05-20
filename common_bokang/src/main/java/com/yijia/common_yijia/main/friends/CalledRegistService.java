package com.yijia.common_yijia.main.friends;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CalledRegistService extends Service {


    public CalledRegistService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SHImpl.getInstance(this).init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

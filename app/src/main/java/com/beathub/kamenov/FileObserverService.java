package com.beathub.kamenov;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Borislav on 18.1.2015 г..
 */
public class FileObserverService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

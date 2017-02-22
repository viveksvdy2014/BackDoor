package com.speedyapps.mybackdoor;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class backGroundService extends Service{
    public backGroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Started!!", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);

    }
}

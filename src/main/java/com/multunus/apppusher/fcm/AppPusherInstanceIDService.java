package com.multunus.apppusher.fcm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AppPusherInstanceIDService extends Service {
    public AppPusherInstanceIDService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

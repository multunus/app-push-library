package com.multunus.apppusher.util;

import android.util.Log;

import com.multunus.apppusher.BuildConfig;
import com.multunus.apppusher.config.Config;

/**
 * Created by yedhukrishnan on 17/06/16.
 */

public final class Logger {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void debug(String message) {
        if(DEBUG) {
            Log.d(Config.LOG_TAG, message);
        }
    }

    public static void error(Exception exception) {
        if(DEBUG) {
            Log.e(Config.LOG_TAG, exception.getMessage(), exception);
        }
    }
}


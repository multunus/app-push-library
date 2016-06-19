package com.multunus.apppusher;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.multunus.apppusher.models.App;
import com.multunus.apppusher.services.AppInstallationService;
import com.multunus.apppusher.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yedhukrishnan on 09/06/16.
 */

public class AppPusher {
    public static void init() {
    }

    public static void parseMessage(Context context, String messageBody) {
        Logger.debug(messageBody);
        if (getMessageType(messageBody).equals("app_install")) {
            Gson gson = new Gson();
            App app = gson.fromJson(messageBody, App.class);
//            startAppInstallationService(context, app);
        }
    }

    private static void startAppInstallationService(Context context, App app) {
        Intent intent = new Intent(context, AppInstallationService.class);
        intent.putExtra("app", app);
        context.startService(intent);
    }


    private static String getMessageType(String messageBody) {
        String messageType = "";
        try {
            JSONObject messageJson = null;
            messageJson = new JSONObject(messageBody);
            messageType = messageJson.getString("type");
        } catch (JSONException e) {
            Logger.error(e);
            e.printStackTrace();
        }
        return messageType;
    }
}

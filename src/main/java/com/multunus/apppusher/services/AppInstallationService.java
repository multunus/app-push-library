package com.multunus.apppusher.services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.multunus.apppusher.models.App;
import com.multunus.apppusher.util.Logger;

import java.util.UUID;

public class AppInstallationService extends IntentService {
    private App app;
    private Context context;
    private DownloadManager downloadManager;

    public AppInstallationService() {
        super(null);
    }

    public AppInstallationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        app = intent.getParcelableExtra("app");
        context = getApplicationContext();
        downloadManager = (DownloadManager) this.getSystemService(this.DOWNLOAD_SERVICE);

        Uri fileUri = Uri.parse(app.getUrl());
        DownloadManager.Request request = new DownloadManager.Request(fileUri);
        request.setTitle("Downloading " + app.getName());
        request.setDescription("Downloading application " + app.getName()
                + " for installing in this device");
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,
                app.getFileName());
        long downloadId = downloadManager.enqueue(request);

        Logger.debug("Downloading -- " + downloadId);

        registerDownloadCompleteReceiver(downloadId);
    }

    private void registerDownloadCompleteReceiver(final long downloadId) {
        registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(downloadId);
                        Cursor cursor = downloadManager.query(query);
                        if (cursor.moveToFirst()) {
                            int index = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                            if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(index)) {
                                Logger.debug("Download successful: " + app.getName());
                                context.unregisterReceiver(this);
                                showAppInstallationNotification();
                            }
                        }
                        cursor.close();
                    }
                },
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        );
    }

    private void showAppInstallationNotification() {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setContentTitle(app.getName())
                .setContentText("Click to install " + app.getName())
                .setContentIntent(getPendingIntentForAppInstallation())
                .setAutoCancel(true)
                .setOngoing(true);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = notificationBuilder.build();
        } else {
            notification = notificationBuilder.getNotification();
        }

        notificationManager.notify(getUniqueId(), notification);
    }

    private PendingIntent getPendingIntentForAppInstallation() {
        Intent pendingIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        pendingIntent.setData(Uri.fromFile(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS + "/" + app.getFileName())));
        pendingIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);

        //pendingIntent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, app.getPackageName());

        return PendingIntent.getActivity(
                context,
                getUniqueId(),
                pendingIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
    }

    private int getUniqueId() {
        return UUID.randomUUID().hashCode();
    }
}
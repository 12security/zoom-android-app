package com.zipow.videobox;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.service.notification.StatusBarNotification;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat.Builder;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public abstract class ZMBaseService extends Service {
    protected boolean mIsNeedCheckStartService = true;

    public void onCreate() {
        super.onCreate();
        if (this.mIsNeedCheckStartService) {
            checkStartService(null);
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (this.mIsNeedCheckStartService) {
            checkStartService(intent);
        }
        return 2;
    }

    /* access modifiers changed from: protected */
    public void checkStartService(@Nullable Intent intent) {
        if (OsUtil.isAtLeastO()) {
            boolean z = false;
            if (intent != null && intent.getBooleanExtra(CompatUtils.ARG_ISSTARTFOREGROUNDSERVICE, false)) {
                z = true;
            }
            if (z) {
                if (hasNotification(4)) {
                    showConfNotification();
                } else {
                    showDefaultForgroundNotification();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void showConfNotification() {
        Builder builder;
        Intent intent = new Intent(this, IntegrationActivity.class);
        intent.setAction(IntegrationActivity.ACTION_RETURN_TO_CONF);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 268435456);
        String string = getString(C4558R.string.zm_app_name);
        String string2 = getString(C4558R.string.zm_msg_conf_in_progress);
        int i = C4558R.C4559drawable.zm_conf_notification;
        if (OsUtil.isAtLeastL()) {
            i = C4558R.C4559drawable.zm_conf_notification_5_0;
        }
        int color = getResources().getColor(C4558R.color.zm_notification_icon_bg);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), C4558R.C4559drawable.zm_launcher);
        if (VideoBoxApplication.getInstance().isSDKMode()) {
            builder = new Builder(getApplicationContext());
            if (OsUtil.isAtLeastO()) {
                String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.SDK_CONF_NOTIFICATION_CHANNEL_ID, "");
                Builder confNotificationCompatBuilder = getConfNotificationCompatBuilder(!StringUtil.isEmptyOrNull(readStringValue) ? readStringValue : NotificationMgr.ZOOM_SERVICE_NOTIFICATION_CHANNEL_ID);
                confNotificationCompatBuilder.setChannelId(readStringValue);
                builder = confNotificationCompatBuilder;
            }
        } else {
            builder = getConfNotificationCompatBuilder(NotificationMgr.ZOOM_SERVICE_NOTIFICATION_CHANNEL_ID);
        }
        builder.setWhen(0).setSmallIcon(i).setColor(color).setContentTitle(string).setContentText(string2).setOnlyAlertOnce(true).setContentIntent(activity);
        if (OsUtil.isAtLeastL() && getResources().getBoolean(C4558R.bool.zm_config_show_large_icon_in_notification_on_api21_above)) {
            builder.setLargeIcon(decodeResource);
        }
        startForeground(4, builder.build());
    }

    private Builder getConfNotificationCompatBuilder(@NonNull String str) {
        if (!OsUtil.isAtLeastO()) {
            return new Builder(this);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(str);
        if (notificationChannel == null) {
            notificationChannel = new NotificationChannel(str, getResources().getString(C4558R.string.zm_service_notification_channel_name_43235), 2);
            if (notificationChannel.canShowBadge()) {
                notificationChannel.setShowBadge(false);
            }
        }
        notificationManager.createNotificationChannel(notificationChannel);
        return new Builder(this, str);
    }

    @RequiresApi(api = 26)
    private void showDefaultForgroundNotification() {
        NotificationChannel notificationChannel = new NotificationChannel(NotificationMgr.ZOOM_SERVICE_NOTIFICATION_CHANNEL_ID, NotificationMgr.getServiceNotificationChannelName(getApplicationContext()), 2);
        if (notificationChannel.canShowBadge()) {
            notificationChannel.setShowBadge(false);
        }
        ((NotificationManager) getSystemService("notification")).createNotificationChannel(notificationChannel);
        startForeground(9, new Notification.Builder(getApplicationContext(), NotificationMgr.ZOOM_SERVICE_NOTIFICATION_CHANNEL_ID).build());
    }

    private boolean hasNotification(int i) {
        if (OsUtil.isAtLeastM()) {
            try {
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService("notification");
                if (notificationManager != null) {
                    for (StatusBarNotification id : notificationManager.getActiveNotifications()) {
                        if (id.getId() == i) {
                            return true;
                        }
                    }
                }
            } catch (Exception unused) {
                return false;
            }
        }
        return false;
    }
}

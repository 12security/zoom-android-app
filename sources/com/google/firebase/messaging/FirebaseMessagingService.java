package com.google.firebase.messaging;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.WorkerThread;
import com.google.firebase.iid.zzau;
import com.google.firebase.iid.zzc;
import java.util.ArrayDeque;
import java.util.Queue;

public class FirebaseMessagingService extends zzc {
    private static final Queue<String> zzeb = new ArrayDeque(10);

    @WorkerThread
    public void onDeletedMessages() {
    }

    @WorkerThread
    public void onMessageReceived(RemoteMessage remoteMessage) {
    }

    @WorkerThread
    public void onMessageSent(String str) {
    }

    @WorkerThread
    public void onNewToken(String str) {
    }

    @WorkerThread
    public void onSendError(String str, Exception exc) {
    }

    /* access modifiers changed from: protected */
    public final Intent zzb(Intent intent) {
        return zzau.zzai().zzaj();
    }

    public final boolean zzc(Intent intent) {
        if (!"com.google.firebase.messaging.NOTIFICATION_OPEN".equals(intent.getAction())) {
            return false;
        }
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("pending_intent");
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (CanceledException unused) {
                Log.e("FirebaseMessaging", "Notification pending intent canceled");
            }
        }
        if (MessagingAnalytics.shouldUploadMetrics(intent)) {
            MessagingAnalytics.logNotificationOpen(intent);
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00f7, code lost:
        if (r0.equals("send_event") == false) goto L_0x0118;
     */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x014d  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0157  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x015b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzd(android.content.Intent r10) {
        /*
            r9 = this;
            java.lang.String r0 = r10.getAction()
            java.lang.String r1 = "com.google.android.c2dm.intent.RECEIVE"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0059
            java.lang.String r1 = "com.google.firebase.messaging.RECEIVE_DIRECT_BOOT"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x0015
            goto L_0x0059
        L_0x0015:
            java.lang.String r1 = "com.google.firebase.messaging.NOTIFICATION_DISMISS"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x0027
            boolean r0 = com.google.firebase.messaging.MessagingAnalytics.shouldUploadMetrics(r10)
            if (r0 == 0) goto L_0x0058
            com.google.firebase.messaging.MessagingAnalytics.logNotificationDismiss(r10)
            return
        L_0x0027:
            java.lang.String r1 = "com.google.firebase.messaging.NEW_TOKEN"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0039
            java.lang.String r0 = "token"
            java.lang.String r10 = r10.getStringExtra(r0)
            r9.onNewToken(r10)
            return
        L_0x0039:
            java.lang.String r0 = "FirebaseMessaging"
            java.lang.String r1 = "Unknown intent action: "
            java.lang.String r10 = r10.getAction()
            java.lang.String r10 = java.lang.String.valueOf(r10)
            int r2 = r10.length()
            if (r2 == 0) goto L_0x0050
            java.lang.String r10 = r1.concat(r10)
            goto L_0x0055
        L_0x0050:
            java.lang.String r10 = new java.lang.String
            r10.<init>(r1)
        L_0x0055:
            android.util.Log.d(r0, r10)
        L_0x0058:
            return
        L_0x0059:
            java.lang.String r0 = "google.message_id"
            java.lang.String r0 = r10.getStringExtra(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            r2 = 2
            if (r1 == 0) goto L_0x006c
            r1 = 0
            com.google.android.gms.tasks.Task r1 = com.google.android.gms.tasks.Tasks.forResult(r1)
            goto L_0x007e
        L_0x006c:
            android.os.Bundle r1 = new android.os.Bundle
            r1.<init>()
            java.lang.String r3 = "google.message_id"
            r1.putString(r3, r0)
            com.google.firebase.iid.zzab r3 = com.google.firebase.iid.zzab.zzc(r9)
            com.google.android.gms.tasks.Task r1 = r3.zza(r2, r1)
        L_0x007e:
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            r4 = 1
            r5 = 3
            r6 = 0
            if (r3 == 0) goto L_0x0089
            r0 = 0
            goto L_0x00cb
        L_0x0089:
            java.util.Queue<java.lang.String> r3 = zzeb
            boolean r3 = r3.contains(r0)
            if (r3 == 0) goto L_0x00b6
            java.lang.String r3 = "FirebaseMessaging"
            boolean r3 = android.util.Log.isLoggable(r3, r5)
            if (r3 == 0) goto L_0x00b4
            java.lang.String r3 = "FirebaseMessaging"
            java.lang.String r7 = "Received duplicate message: "
            java.lang.String r0 = java.lang.String.valueOf(r0)
            int r8 = r0.length()
            if (r8 == 0) goto L_0x00ac
            java.lang.String r0 = r7.concat(r0)
            goto L_0x00b1
        L_0x00ac:
            java.lang.String r0 = new java.lang.String
            r0.<init>(r7)
        L_0x00b1:
            android.util.Log.d(r3, r0)
        L_0x00b4:
            r0 = 1
            goto L_0x00cb
        L_0x00b6:
            java.util.Queue<java.lang.String> r3 = zzeb
            int r3 = r3.size()
            r7 = 10
            if (r3 < r7) goto L_0x00c5
            java.util.Queue<java.lang.String> r3 = zzeb
            r3.remove()
        L_0x00c5:
            java.util.Queue<java.lang.String> r3 = zzeb
            r3.add(r0)
            r0 = 0
        L_0x00cb:
            if (r0 != 0) goto L_0x01b0
            java.lang.String r0 = "message_type"
            java.lang.String r0 = r10.getStringExtra(r0)
            if (r0 != 0) goto L_0x00d7
            java.lang.String r0 = "gcm"
        L_0x00d7:
            r3 = -1
            int r7 = r0.hashCode()
            r8 = -2062414158(0xffffffff85120eb2, float:-6.867586E-36)
            if (r7 == r8) goto L_0x010e
            r4 = 102161(0x18f11, float:1.43158E-40)
            if (r7 == r4) goto L_0x0104
            r4 = 814694033(0x308f3e91, float:1.0422402E-9)
            if (r7 == r4) goto L_0x00fa
            r4 = 814800675(0x3090df23, float:1.0540798E-9)
            if (r7 == r4) goto L_0x00f1
            goto L_0x0118
        L_0x00f1:
            java.lang.String r4 = "send_event"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0118
            goto L_0x0119
        L_0x00fa:
            java.lang.String r2 = "send_error"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L_0x0118
            r2 = 3
            goto L_0x0119
        L_0x0104:
            java.lang.String r2 = "gcm"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L_0x0118
            r2 = 0
            goto L_0x0119
        L_0x010e:
            java.lang.String r2 = "deleted_messages"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L_0x0118
            r2 = 1
            goto L_0x0119
        L_0x0118:
            r2 = -1
        L_0x0119:
            switch(r2) {
                case 0: goto L_0x015b;
                case 1: goto L_0x0157;
                case 2: goto L_0x014d;
                case 3: goto L_0x0130;
                default: goto L_0x011c;
            }
        L_0x011c:
            java.lang.String r10 = "FirebaseMessaging"
            java.lang.String r2 = "Received message with unknown type: "
            java.lang.String r0 = java.lang.String.valueOf(r0)
            int r3 = r0.length()
            if (r3 == 0) goto L_0x01a8
            java.lang.String r0 = r2.concat(r0)
            goto L_0x01ad
        L_0x0130:
            java.lang.String r0 = "google.message_id"
            java.lang.String r0 = r10.getStringExtra(r0)
            if (r0 != 0) goto L_0x013e
            java.lang.String r0 = "message_id"
            java.lang.String r0 = r10.getStringExtra(r0)
        L_0x013e:
            com.google.firebase.messaging.SendException r2 = new com.google.firebase.messaging.SendException
            java.lang.String r3 = "error"
            java.lang.String r10 = r10.getStringExtra(r3)
            r2.<init>(r10)
            r9.onSendError(r0, r2)
            goto L_0x01b0
        L_0x014d:
            java.lang.String r0 = "google.message_id"
            java.lang.String r10 = r10.getStringExtra(r0)
            r9.onMessageSent(r10)
            goto L_0x01b0
        L_0x0157:
            r9.onDeletedMessages()
            goto L_0x01b0
        L_0x015b:
            boolean r0 = com.google.firebase.messaging.MessagingAnalytics.shouldUploadMetrics(r10)
            if (r0 == 0) goto L_0x0164
            com.google.firebase.messaging.MessagingAnalytics.logNotificationReceived(r10)
        L_0x0164:
            android.os.Bundle r0 = r10.getExtras()
            if (r0 != 0) goto L_0x016f
            android.os.Bundle r0 = new android.os.Bundle
            r0.<init>()
        L_0x016f:
            java.lang.String r2 = "androidx.contentpager.content.wakelockid"
            r0.remove(r2)
            boolean r2 = com.google.firebase.messaging.zza.zzh(r0)
            if (r2 == 0) goto L_0x019f
            java.util.concurrent.ExecutorService r2 = java.util.concurrent.Executors.newSingleThreadExecutor()
            com.google.firebase.messaging.zzb r3 = new com.google.firebase.messaging.zzb
            r3.<init>(r9, r0, r2)
            boolean r3 = r3.zzas()     // Catch:{ all -> 0x019a }
            if (r3 == 0) goto L_0x018d
            r2.shutdown()
            goto L_0x01b0
        L_0x018d:
            r2.shutdown()
            boolean r2 = com.google.firebase.messaging.MessagingAnalytics.shouldUploadMetrics(r10)
            if (r2 == 0) goto L_0x019f
            com.google.firebase.messaging.MessagingAnalytics.logNotificationForeground(r10)
            goto L_0x019f
        L_0x019a:
            r10 = move-exception
            r2.shutdown()
            throw r10
        L_0x019f:
            com.google.firebase.messaging.RemoteMessage r10 = new com.google.firebase.messaging.RemoteMessage
            r10.<init>(r0)
            r9.onMessageReceived(r10)
            goto L_0x01b0
        L_0x01a8:
            java.lang.String r0 = new java.lang.String
            r0.<init>(r2)
        L_0x01ad:
            android.util.Log.w(r10, r0)
        L_0x01b0:
            r2 = 1
            java.util.concurrent.TimeUnit r10 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ InterruptedException | ExecutionException | TimeoutException -> 0x01b8 }
            com.google.android.gms.tasks.Tasks.await(r1, r2, r10)     // Catch:{ InterruptedException | ExecutionException | TimeoutException -> 0x01b8 }
            return
        L_0x01b8:
            r10 = move-exception
            java.lang.String r0 = "FirebaseMessaging"
            java.lang.String r10 = java.lang.String.valueOf(r10)
            java.lang.String r1 = java.lang.String.valueOf(r10)
            int r1 = r1.length()
            int r1 = r1 + 20
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>(r1)
            java.lang.String r1 = "Message ack failed: "
            r2.append(r1)
            r2.append(r10)
            java.lang.String r10 = r2.toString()
            android.util.Log.w(r0, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.messaging.FirebaseMessagingService.zzd(android.content.Intent):void");
    }
}

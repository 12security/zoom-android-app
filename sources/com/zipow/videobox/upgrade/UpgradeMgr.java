package com.zipow.videobox.upgrade;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import com.google.common.base.Ascii;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.ZMUtils;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class UpgradeMgr {
    private static final Uri CONTENT_URI = Uri.parse("content://downloads/");
    public static final int DOWNLOAD_FAILED = 3;
    public static final int DOWNLOAD_READY = 1;
    public static final int DOWNLOAD_RUNNING = 2;
    public static final int STATUS_FAILED = 1;
    public static final int STATUS_PAUSED = 2;
    public static final int STATUS_PENDING = 3;
    public static final int STATUS_RUNNING = 4;
    public static final int STATUS_SUCCESSFUL = 5;
    private static final String TAG = "UpgradeMgr";
    private static UpgradeMgr instance;
    /* access modifiers changed from: private */
    @NonNull
    public final DownloadManager downloadMgr;
    private DownloadsChangeObserver downloadObserver;
    /* access modifiers changed from: private */
    public int downloadSize;
    /* access modifiers changed from: private */
    public int fileSize;
    @NonNull
    private ListenerList listeners = new ListenerList();
    /* access modifiers changed from: private */
    public long mDownloadId = -1;
    @Nullable
    private BroadcastReceiver mDownloadReceiver;
    @Nullable
    private String packageCheckSum;
    /* access modifiers changed from: private */
    public int status;

    private class DownloadsChangeObserver extends ContentObserver {
        private int currentStatus;

        public DownloadsChangeObserver() {
            super(null);
        }

        public void onChange(boolean z) {
            Query query = new Query();
            query.setFilterById(new long[]{UpgradeMgr.this.mDownloadId});
            Cursor query2 = UpgradeMgr.this.downloadMgr.query(query);
            if (query2 != null && query2.moveToFirst()) {
                int i = query2.getInt(query2.getColumnIndex("status"));
                int columnIndex = query2.getColumnIndex("total_size");
                int columnIndex2 = query2.getColumnIndex("bytes_so_far");
                UpgradeMgr.this.fileSize = query2.getInt(columnIndex);
                UpgradeMgr.this.downloadSize = query2.getInt(columnIndex2);
                this.currentStatus = i;
                if (i == 4) {
                    UpgradeMgr.this.notifyEvent(2, 0, 0);
                } else if (i == 8) {
                    UpgradeMgr.this.removeContentObserver();
                    UpgradeMgr.this.install();
                } else if (i != 16) {
                    switch (i) {
                        case 1:
                            UpgradeMgr.this.notifyEvent(3, 0, 0);
                            break;
                        case 2:
                            UpgradeMgr.this.status = 2;
                            UpgradeMgr upgradeMgr = UpgradeMgr.this;
                            upgradeMgr.notifyEvent(4, upgradeMgr.downloadSize, UpgradeMgr.this.fileSize);
                            break;
                        default:
                            return;
                    }
                } else {
                    UpgradeMgr.this.status = 3;
                    UpgradeMgr.this.notifyEvent(1, 0, 0);
                    UpgradeMgr.this.removeContentObserver();
                }
            } else if (this.currentStatus == 2) {
                UpgradeMgr.this.status = 3;
                UpgradeMgr.this.removeContentObserver();
                UpgradeMgr.this.notifyEvent(1, 0, 0);
            }
            if (query2 != null) {
                query2.close();
            }
        }
    }

    public interface UpgradeMgrListener extends IListener {
        void onUpgradeEvent(int i, int i2, int i3);
    }

    public static synchronized UpgradeMgr getInstance(@NonNull Context context) {
        UpgradeMgr upgradeMgr;
        synchronized (UpgradeMgr.class) {
            if (instance == null) {
                instance = new UpgradeMgr(context);
            }
            upgradeMgr = instance;
        }
        return upgradeMgr;
    }

    private UpgradeMgr(Context context) {
        this.downloadMgr = (DownloadManager) context.getSystemService("download");
        this.status = 1;
    }

    public synchronized void addUpgradeMgrListener(UpgradeMgrListener upgradeMgrListener) {
        this.listeners.add(upgradeMgrListener);
    }

    public synchronized void removeUpgradeMgrListener(UpgradeMgrListener upgradeMgrListener) {
        this.listeners.remove(upgradeMgrListener);
    }

    /* access modifiers changed from: private */
    public synchronized void notifyEvent(int i, int i2, int i3) {
        if (i == 1 || i == 5) {
            unregisterDownloadReceiver(VideoBoxApplication.getInstance());
        }
        for (IListener iListener : this.listeners.getAll()) {
            ((UpgradeMgrListener) iListener).onUpgradeEvent(i, i2, i3);
        }
    }

    @SuppressLint({"NewApi"})
    public boolean installZoomByAPK(@Nullable Context context, String str) {
        if (context == null) {
            return false;
        }
        this.packageCheckSum = getZoomAPKCheckSum();
        if (StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(this.packageCheckSum)) {
            return false;
        }
        try {
            Uri parse = Uri.parse(str);
            if (this.status == 2) {
                Query query = new Query();
                query.setFilterById(new long[]{this.mDownloadId});
                Cursor query2 = this.downloadMgr.query(query);
                if (query2 != null) {
                    if (query2.getCount() == 1 && query2.moveToFirst()) {
                        int i = query2.getInt(query2.getColumnIndex("status"));
                        if (i != 4) {
                            if (i == 8) {
                                checkApkAndInstall(Uri.parse(query2.getString(query2.getColumnIndex("local_uri"))));
                                query2.close();
                                return true;
                            } else if (i != 16) {
                                switch (i) {
                                    case 1:
                                    case 2:
                                        break;
                                }
                            } else {
                                this.downloadSize = 0;
                                this.fileSize = 0;
                                notifyEvent(1, 0, 0);
                            }
                        }
                        query2.close();
                        return true;
                    }
                    query2.close();
                }
            }
            try {
                Request request = new Request(parse);
                request.setAllowedNetworkTypes(3);
                String downloadApkName = getDownloadApkName(Environment.DIRECTORY_DOWNLOADS);
                if (downloadApkName == null) {
                    return false;
                }
                request.setDestinationInExternalFilesDir(VideoBoxApplication.getGlobalContext(), Environment.DIRECTORY_DOWNLOADS, downloadApkName);
                request.setMimeType("application/vnd.android.package-archive");
                request.setVisibleInDownloadsUi(true);
                request.setTitle(context.getString(C4558R.string.zm_app_full_name));
                this.downloadObserver = new DownloadsChangeObserver();
                context.getContentResolver().registerContentObserver(CONTENT_URI, true, this.downloadObserver);
                this.status = 2;
                try {
                    this.mDownloadId = this.downloadMgr.enqueue(request);
                    registerDownloadReceiver(context.getApplicationContext());
                    return true;
                } catch (Throwable unused) {
                    this.status = 1;
                    return false;
                }
            } catch (Exception unused2) {
                this.status = 1;
                return false;
            }
        } catch (Exception unused3) {
            return false;
        }
    }

    private String getDownloadApkName(String str) {
        File file;
        String str2;
        StringBuilder sb;
        String latestVersionString = PTApp.getInstance().getLatestVersionString();
        String packageName = PTApp.getInstance().getPackageName();
        if (StringUtil.isEmptyOrNull(packageName) || StringUtil.isEmptyOrNull(latestVersionString)) {
            return null;
        }
        try {
            file = Environment.getExternalStoragePublicDirectory(str);
        } catch (Exception unused) {
            file = null;
        }
        if (file == null) {
            return null;
        }
        String str3 = ".apk";
        int i = 0;
        if (packageName.endsWith(str3)) {
            packageName = packageName.substring(0, packageName.length() - 4);
        }
        do {
            if (i == 0) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(packageName);
                sb2.append("-");
                sb2.append(latestVersionString);
                sb2.append(str3);
                str2 = sb2.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(packageName);
                sb3.append("-");
                sb3.append(latestVersionString);
                sb3.append("(");
                sb3.append(i);
                sb3.append(")");
                sb3.append(str3);
                str2 = sb3.toString();
            }
            i++;
            sb = new StringBuilder();
            sb.append(file.toString());
            sb.append(File.separator);
            sb.append(str2);
        } while (new File(sb.toString()).exists());
        return str2;
    }

    private void registerDownloadReceiver(@NonNull Context context) {
        if (this.mDownloadReceiver == null) {
            this.mDownloadReceiver = new BroadcastReceiver() {
                public void onReceive(@NonNull Context context, @NonNull Intent intent) {
                    if ("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED".equals(intent.getAction())) {
                        try {
                            Intent intent2 = new Intent("android.intent.action.VIEW_DOWNLOADS");
                            intent2.setFlags(268435456);
                            ActivityStartHelper.startActivityForeground(context, intent2);
                        } catch (Exception unused) {
                        }
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED");
            context.registerReceiver(this.mDownloadReceiver, intentFilter);
        }
    }

    private void unregisterDownloadReceiver(@NonNull Context context) {
        BroadcastReceiver broadcastReceiver = this.mDownloadReceiver;
        if (broadcastReceiver != null) {
            try {
                context.unregisterReceiver(broadcastReceiver);
            } catch (Exception unused) {
            }
            this.mDownloadReceiver = null;
        }
    }

    private void cleanDownload() {
        this.mDownloadId = -1;
        this.downloadSize = 0;
        this.fileSize = 0;
    }

    private boolean installApk(@Nullable Uri uri) {
        if (uri == null) {
            return false;
        }
        System.currentTimeMillis();
        String md5 = md5(uri);
        if (md5 == null || !StringUtil.isSameString(md5, this.packageCheckSum) || !checkPackageAndCertficate(uri)) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        if (OsUtil.isAtLeastN()) {
            intent.addFlags(1);
            uri = FileProvider.getUriForFile(VideoBoxApplication.getInstance(), VideoBoxApplication.getInstance().getResources().getString(C4558R.string.zm_app_provider), new File(FileUtils.getPathFromUri(VideoBoxApplication.getInstance(), uri)));
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        ActivityStartHelper.startActivityForeground(VideoBoxApplication.getInstance(), intent);
        return true;
    }

    private boolean checkPackageAndCertficate(@NonNull Uri uri) {
        PackageManager packageManager = VideoBoxApplication.getInstance().getPackageManager();
        int i = OsUtil.isAtLeastP() ? 134217792 : 64;
        if (uri.getPath() == null) {
            return false;
        }
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(uri.getPath(), i);
        if (packageArchiveInfo == null) {
            return false;
        }
        Signature[] signatureArr = null;
        if (packageArchiveInfo.applicationInfo == null || packageArchiveInfo.applicationInfo.packageName == null || !packageArchiveInfo.applicationInfo.packageName.equalsIgnoreCase(AppUtil.getAppPackageName())) {
            return false;
        }
        if (!OsUtil.isAtLeastP()) {
            signatureArr = packageArchiveInfo.signatures;
        } else if (packageArchiveInfo.signingInfo != null) {
            signatureArr = packageArchiveInfo.signingInfo.getApkContentsSigners();
        }
        if (signatureArr == null) {
            return false;
        }
        if (!ZMUtils.SIGNATURE_SHA256.equalsIgnoreCase(ZMUtils.hexDigest(signatureArr[0].toByteArray(), MessageDigestAlgorithms.SHA_256))) {
            return false;
        }
        return true;
    }

    @Nullable
    public static String getZoomAPKDownloadUrl() {
        return PTApp.getInstance().getPackageDownloadUrl();
    }

    @Nullable
    private static String getZoomAPKCheckSum() {
        return PTApp.getInstance().getPackageCheckSum();
    }

    public long getDownloadId() {
        return this.mDownloadId;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public int getDownloadSize() {
        return this.downloadSize;
    }

    /* access modifiers changed from: private */
    public void removeContentObserver() {
        ContentResolver contentResolver = VideoBoxApplication.getInstance().getContentResolver();
        if (contentResolver != null) {
            try {
                contentResolver.unregisterContentObserver(this.downloadObserver);
            } catch (Exception unused) {
            }
        }
    }

    public void cancel(@Nullable Context context) {
        if (context != null) {
            unregisterDownloadReceiver(context.getApplicationContext());
        }
        removeContentObserver();
        try {
            this.downloadMgr.remove(new long[]{this.mDownloadId});
        } catch (Exception unused) {
        }
        this.status = 1;
        cleanDownload();
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public void install() {
        Query query = new Query();
        query.setFilterById(new long[]{this.mDownloadId});
        Cursor query2 = this.downloadMgr.query(query);
        Uri uri = null;
        if (query2 != null) {
            if (query2.getCount() == 1 && query2.moveToFirst()) {
                uri = Uri.parse(query2.getString(query2.getColumnIndex("local_uri")));
            }
            query2.close();
        }
        checkApkAndInstall(uri);
    }

    private void checkApkAndInstall(@Nullable Uri uri) {
        cleanDownload();
        if (!(uri != null ? installApk(uri) : false)) {
            this.status = 3;
            notifyEvent(1, 0, 0);
            return;
        }
        this.status = 1;
        notifyEvent(5, 0, 0);
    }

    private String md5(Uri uri) {
        FileInputStream fileInputStream;
        Throwable th;
        Throwable th2;
        int read;
        int i;
        byte[] digest;
        File file = new File(uri.getPath());
        if (!file.exists()) {
            return null;
        }
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance2 = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    byte[] bArr = new byte[10240];
                    do {
                        read = fileInputStream.read(bArr);
                        if (read > 0) {
                            instance2.update(bArr, 0, read);
                            continue;
                        }
                    } while (read > 0);
                    char[] cArr2 = new char[(r2 * 2)];
                    int i2 = 0;
                    for (byte b : instance2.digest()) {
                        int i3 = i2 + 1;
                        cArr2[i2] = cArr[(b >>> 4) & 15];
                        i2 = i3 + 1;
                        cArr2[i3] = cArr[b & Ascii.f228SI];
                    }
                    String str = new String(cArr2);
                    fileInputStream.close();
                    return str;
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    th2 = r12;
                    th = th4;
                }
            } catch (Exception unused) {
                return null;
            }
        } catch (NoSuchAlgorithmException unused2) {
            return null;
        }
        throw th;
        if (th2 != null) {
            try {
                fileInputStream.close();
            } catch (Throwable th5) {
                th2.addSuppressed(th5);
            }
        } else {
            fileInputStream.close();
        }
        throw th;
    }
}

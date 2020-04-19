package com.zipow.cmmlib;

import android.annotation.SuppressLint;
import android.content.pm.Signature;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMUtils;
import java.io.File;
import java.util.UUID;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;

public class AppUtil {
    private static final long MIN_RESERVED_STORAGE_SPACE = 10485760;
    private static final int REQUEST_CONTACT_PERMISSION_PERIOD = 259200000;
    private static final String SHARE_CACHE_FILE_NAME_PREFIX = "share_cache_file";
    private static final String TAG = "AppUtil";

    public static String getAppPackageName() {
        return VideoBoxApplication.getInstance().getPackageName();
    }

    public static String getPublicFilesPath() {
        return FileUtils.getPublicDataPath(VideoBoxApplication.getInstance());
    }

    public static String getTempPath() {
        return FileUtils.getTempPath(VideoBoxApplication.getInstance());
    }

    public static String createTempFile(String str, String str2) {
        return createTempFile(str, str2, null);
    }

    public static String createTempFile(String str, String str2, String str3) {
        return FileUtils.createTempFile(VideoBoxApplication.getInstance(), str, str2, str3);
    }

    @Nullable
    public static String getDataPath() {
        return getDataPath(false, false);
    }

    @Nullable
    public static String getCachePath() {
        return getDataPath();
    }

    public static String getShareCachePathByExtension(@Nullable String str, String str2) {
        String str3;
        if (StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return null;
        }
        String fileExtendName = AndroidAppUtil.getFileExtendName(str2);
        if (fileExtendName == null) {
            fileExtendName = "";
        }
        if (str.endsWith(File.separator)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(SHARE_CACHE_FILE_NAME_PREFIX);
            sb.append(fileExtendName);
            str3 = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(File.separator);
            sb2.append(SHARE_CACHE_FILE_NAME_PREFIX);
            sb2.append(fileExtendName);
            str3 = sb2.toString();
        }
        return str3;
    }

    public static boolean isTabletOrTV() {
        return UIUtil.isTabletOrTV(VideoBoxApplication.getGlobalContext());
    }

    public static String getLogParentPath() {
        String externalStorageState = Environment.getExternalStorageState();
        if (externalStorageState == null || !externalStorageState.equals("mounted")) {
            StringBuilder sb = new StringBuilder();
            sb.append("/sdcard/Android/data/");
            sb.append(getAppPackageName());
            return sb.toString();
        }
        File externalFilesDir = VideoBoxApplication.getInstance().getExternalFilesDir(null);
        if (externalFilesDir != null) {
            return externalFilesDir.getParent();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("/sdcard/Android/data/");
        sb2.append(getAppPackageName());
        return sb2.toString();
    }

    @Nullable
    public static String getDataPath(boolean z, boolean z2) {
        return FileUtils.getDataPath(VideoBoxApplication.getInstance(), z2, z);
    }

    @SuppressLint({"NewApi"})
    public static boolean hasEnoughDiskSpace(String str, long j) {
        long j2;
        boolean z = true;
        try {
            StatFs statFs = new StatFs(str);
            if (VERSION.SDK_INT < 18) {
                j2 = ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
            } else {
                j2 = statFs.getAvailableBytes();
            }
            if (j2 < j + MIN_RESERVED_STORAGE_SPACE) {
                z = false;
            }
            return z;
        } catch (Exception unused) {
            return true;
        }
    }

    public static void saveRequestContactPermissionTime() {
        PreferenceUtil.saveLongValue(PreferenceUtil.LAST_REQUEST_CONTACT_PERMISSION_TIME, System.currentTimeMillis());
    }

    @NonNull
    public static String getCertificateFingerprintMD5() {
        Signature[] signatures = ZMUtils.getSignatures(VideoBoxApplication.getNonNullInstance());
        return (signatures == null || signatures.length == 0 || signatures[0] == null) ? "" : StringUtil.safeString(ZMUtils.hexDigest(signatures[0].toByteArray(), MessageDigestAlgorithms.MD5)).toLowerCase();
    }

    public static boolean canRequestContactPermission() {
        long readLongValue = PreferenceUtil.readLongValue(PreferenceUtil.LAST_REQUEST_CONTACT_PERMISSION_TIME, 0);
        if (readLongValue != 0 && System.currentTimeMillis() - readLongValue <= 259200000) {
            return false;
        }
        return true;
    }

    public static String getGUID() {
        return UUID.randomUUID().toString();
    }
}

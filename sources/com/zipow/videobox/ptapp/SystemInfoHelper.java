package com.zipow.videobox.ptapp;

import android.bluetooth.BluetoothAdapter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxUser;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.PreferenceUtil;
import java.util.UUID;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.HardwareUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class SystemInfoHelper {
    private static final String TAG = "SystemInfoHelper";

    public static String getClientInfo() {
        return AAD.ADAL_ID_PLATFORM_VALUE;
    }

    @NonNull
    public static String getMacAddress() {
        try {
            return getMacAddressImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return "";
        }
    }

    @NonNull
    private static String getMacAddressImpl() {
        String cachedWifiMacAddress = getCachedWifiMacAddress();
        if (StringUtil.isEmptyOrNull(cachedWifiMacAddress)) {
            cachedWifiMacAddress = makeDummyMacAddress();
            cacheWifiMacAddress(cachedWifiMacAddress);
        }
        String str = null;
        try {
            String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.SYSTEM_DEVICE_ID, null);
            if (StringUtil.isEmptyOrNull(readStringValue)) {
                str = Secure.getString(VideoBoxApplication.getInstance().getContentResolver(), "android_id");
                PreferenceUtil.saveStringValue(PreferenceUtil.SYSTEM_DEVICE_ID, str);
            } else {
                str = readStringValue;
            }
        } catch (Exception unused) {
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("-");
        sb.append(cachedWifiMacAddress);
        return sb.toString();
    }

    @NonNull
    public static String getDeviceId() {
        return getMacAddress();
    }

    @NonNull
    private static String makeDummyMacAddress() {
        String replaceAll = UUID.randomUUID().toString().replaceAll("\\-", "");
        if (replaceAll.length() < 12) {
            return replaceAll;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < 6) {
            int i2 = i + 1;
            sb.append(replaceAll.substring(i * 2, i2 * 2));
            if (i < 5) {
                sb.append(":");
            }
            i = i2;
        }
        return sb.toString();
    }

    private static void cacheWifiMacAddress(String str) {
        PreferenceUtil.saveStringValue(PreferenceUtil.WIFI_MAC_ADDRESS, str);
    }

    @Nullable
    private static String getCachedWifiMacAddress() {
        return PreferenceUtil.readStringValue(PreferenceUtil.WIFI_MAC_ADDRESS, null);
    }

    public static String getOSInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Android ");
        sb.append(VERSION.RELEASE);
        return sb.toString();
    }

    public static String getHardwareInfo() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(";Manufacturer:");
            sb.append(Build.MANUFACTURER);
            sb.append(";Model:");
            sb.append(Build.MODEL);
            sb.append(";OS:Android ");
            sb.append(VERSION.RELEASE);
            sb.append(";CPU_ABI:");
            sb.append(HardwareUtil.getPreferredCpuABI());
            sb.append(";CPU Kernels:");
            sb.append(HardwareUtil.getCPUKernalNumbers());
            sb.append(";CPU Frequency:");
            sb.append(HardwareUtil.getCPUKernelFrequency(0, 2));
            return sb.toString();
        } catch (Throwable th) {
            return th.getMessage();
        }
    }

    private static String getDeviceName() {
        try {
            return BluetoothAdapter.getDefaultAdapter().getName();
        } catch (Exception unused) {
            return Build.MODEL;
        }
    }

    @NonNull
    private static String[] getMccAndMnc() {
        String[] strArr = new String[2];
        ConnectivityManager connectivityManager = (ConnectivityManager) VideoBoxApplication.getInstance().getSystemService("connectivity");
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.getType() == 1) {
                strArr[0] = "wifi";
                strArr[1] = "NA";
                return strArr;
            }
        }
        TelephonyManager telephonyManager = (TelephonyManager) VideoBoxApplication.getInstance().getSystemService(BoxUser.FIELD_PHONE);
        if (telephonyManager != null) {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (networkOperator == null || networkOperator.length() != 5) {
                strArr[0] = "NA";
                strArr[1] = "NA";
            } else {
                strArr[0] = networkOperator.substring(0, 3);
                strArr[1] = networkOperator.substring(3);
            }
        }
        return strArr;
    }

    @Nullable
    private static String getCountryIso() {
        TelephonyManager telephonyManager = (TelephonyManager) VideoBoxApplication.getInstance().getSystemService(BoxUser.FIELD_PHONE);
        if (telephonyManager == null) {
            return null;
        }
        String simCountryIso = telephonyManager.getSimCountryIso();
        return (simCountryIso == null || simCountryIso.length() == 0) ? CompatUtils.getLocalDefault().getCountry() : simCountryIso;
    }

    private static String getLanguage() {
        return CompatUtils.getLocalDefault().getLanguage();
    }
}

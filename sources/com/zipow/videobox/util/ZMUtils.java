package com.zipow.videobox.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.hardware.Camera.CameraInfo;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.common.primitives.UnsignedBytes;
import com.zipow.nydus.NydusUtil;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.File;
import java.net.Proxy.Type;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import p021us.zoom.androidlib.proxy.ProxyConfig;
import p021us.zoom.androidlib.proxy.ProxySettings;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.videomeetings.BuildConfig;
import p021us.zoom.videomeetings.C4558R;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class ZMUtils {
    public static final String SIGNATURE_SHA256 = "60B75724B34686E52BDE944969DE120F16BD6D959788D54384494CAEDD4E445D";
    private static final String TAG = "com.zipow.videobox.util.ZMUtils";
    public static final Set<String> ZOOM_APP_IDS = new HashSet();
    private static final String ZOOM_APP_SIGNATURE = "308201e53082014ea00302010202044faa0a6b300d06092a864886f70d01010505003036310b300906035504061302555331273025060355040a131e5a6f6f6d20566964656f20436f6d6d756e69636174696f6e7320496e632e3020170d3132303530393036313035315a180f32303632303432373036313035315a3036310b300906035504061302555331273025060355040a131e5a6f6f6d20566964656f20436f6d6d756e69636174696f6e7320496e632e30819f300d06092a864886f70d010101050003818d00308189028181009b463f2d26827dcd115aecc70e5124b9d68cd78e401489c9eae4cd19bc4ca0576ad28168a81f71e8d8b5a7cdc956d937510df3cfa956c28d55668894c33ce08052946ae4af1455becfd2243897f1731fd17a547260c5a52daaebf8ab8a9aad1ad18f99ff696dcf7d713f6540f102c274fbfbc895045f25af67d0fe8dedc536510203010001300d06092a864886f70d0101050500038181000db7990467b840f362bad88c35874abe4d10d3a872356e57581f06fcbac79ebf6d82bb380d14461eded133d9630d77a6b7bcc9953f1ab02437c6317646218b6a37f3c75e833096fa24a473a9b53b1cca4269f0c753ec33239c9a293ea87c27121f424cb9ec1d7765c7fc0c51b7ee2ec4ab9d15a896eeb150ac06fe67086f1c70";

    public static void printFunctionCallStack(@NonNull String str) {
    }

    static {
        ZOOM_APP_IDS.add(BuildConfig.APPLICATION_ID);
        ZOOM_APP_IDS.add("us.zoom.videomeetings4intune");
        ZOOM_APP_IDS.add("us.zoom.videomeetings4mdm2");
    }

    public static boolean isValidSignature(@NonNull Context context) {
        if (ZMBuildConfig.BUILD_TARGET != 0) {
            return true;
        }
        Signature[] signatures = getSignatures(context);
        if (signatures == null) {
            return false;
        }
        for (Signature charsString : signatures) {
            if (charsString.toCharsString().equals(ZOOM_APP_SIGNATURE)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isZoomApp(@NonNull Context context) {
        return ZOOM_APP_IDS.contains(context.getPackageName());
    }

    public static boolean isItuneApp(@NonNull Context context) {
        return "us.zoom.videomeetings4intune".equals(context.getPackageName());
    }

    @Nullable
    public static List<String> getDefaultBrowserPkgName(@NonNull Context context) {
        if (isItuneApp(context)) {
            return Arrays.asList(new String[]{"com.microsoft.intune.mam.managedbrowser", "com.microsoft.emmx"});
        }
        return null;
    }

    @NonNull
    public static String getCertificateFingerprint(Context context) {
        Signature[] signatures = getSignatures(context);
        return (signatures == null || signatures.length == 0) ? "" : hexDigest(signatures[0].toByteArray(), MessageDigestAlgorithms.SHA_256);
    }

    @NonNull
    public static String hexDigest(byte[] bArr, String str) {
        try {
            byte[] digest = MessageDigest.getInstance(str).digest(bArr);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                byte b2 = b & UnsignedBytes.MAX_VALUE;
                if (b2 < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(b2));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Nullable
    public static Signature[] getSignatures(@NonNull Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), OsUtil.isAtLeastP() ? 134217792 : 64);
            if (!OsUtil.isAtLeastP()) {
                return packageInfo.signatures;
            }
            if (packageInfo.signingInfo != null) {
                return packageInfo.signingInfo.getApkContentsSigners();
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0037 */
    @androidx.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCmdResult(java.lang.String[] r4) {
        /*
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            r1 = 0
            java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0055, all -> 0x004a }
            java.lang.Process r1 = r2.exec(r4)     // Catch:{ Throwable -> 0x0055, all -> 0x004a }
            java.io.InputStream r4 = r1.getInputStream()     // Catch:{ Throwable -> 0x0055, all -> 0x004a }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x0055, all -> 0x004a }
            r2.<init>(r4)     // Catch:{ Throwable -> 0x0055, all -> 0x004a }
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x0055, all -> 0x004a }
            r4.<init>(r2)     // Catch:{ Throwable -> 0x0055, all -> 0x004a }
        L_0x001c:
            java.lang.String r2 = r4.readLine()     // Catch:{ Exception -> 0x0037, all -> 0x003b }
            if (r2 == 0) goto L_0x0037
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0037, all -> 0x003b }
            r3.<init>()     // Catch:{ Exception -> 0x0037, all -> 0x003b }
            r3.append(r2)     // Catch:{ Exception -> 0x0037, all -> 0x003b }
            java.lang.String r2 = "\n"
            r3.append(r2)     // Catch:{ Exception -> 0x0037, all -> 0x003b }
            java.lang.String r2 = r3.toString()     // Catch:{ Exception -> 0x0037, all -> 0x003b }
            r0.append(r2)     // Catch:{ Exception -> 0x0037, all -> 0x003b }
            goto L_0x001c
        L_0x0037:
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r4)     // Catch:{ Throwable -> 0x0055, all -> 0x004a }
            goto L_0x0040
        L_0x003b:
            r2 = move-exception
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r4)     // Catch:{ Throwable -> 0x0055, all -> 0x004a }
            throw r2     // Catch:{ Throwable -> 0x0055, all -> 0x004a }
        L_0x0040:
            if (r1 == 0) goto L_0x0045
            r1.destroy()
        L_0x0045:
            java.lang.String r4 = r0.toString()
            return r4
        L_0x004a:
            if (r1 == 0) goto L_0x0050
            r1.destroy()
        L_0x0050:
            java.lang.String r4 = r0.toString()
            return r4
        L_0x0055:
            if (r1 == 0) goto L_0x005b
            r1.destroy()
        L_0x005b:
            java.lang.String r4 = r0.toString()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ZMUtils.getCmdResult(java.lang.String[]):java.lang.String");
    }

    public static String getSubFileInfo(String str) {
        return getCmdResult(new String[]{"ls", "-l", str});
    }

    public static int getMaxDescriptors() {
        try {
            return Integer.parseInt(getCmdResult(new String[]{"sh", "-c", "ulimit -n"}).replace(FontStyleHelper.SPLITOR, ""));
        } catch (Exception unused) {
            return -1;
        }
    }

    public static String getDescriptors(int i) {
        StringBuffer stringBuffer = new StringBuffer();
        if (i < 0) {
            return stringBuffer.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("/proc/");
        sb.append(i);
        sb.append("/fd");
        File file = new File(sb.toString());
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("num: ");
            sb2.append(listFiles.length);
            sb2.append(FontStyleHelper.SPLITOR);
            stringBuffer.append(sb2.toString());
            stringBuffer.append(getSubFileInfo(file.getAbsolutePath()));
        }
        return stringBuffer.toString().replace(FontStyleHelper.SPLITOR, PreferencesConstants.COOKIE_DELIMITER);
    }

    @NonNull
    public static String formatMeetingNumberWithChar(String str, char c) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        if (length > 3) {
            sb.append(str.substring(0, 3));
            sb.append(c);
        }
        if (length >= 11) {
            sb.append(str.substring(3, 7));
            sb.append(c);
            sb.append(str.substring(7, length));
        } else if (length > 6) {
            sb.append(str.substring(3, 6));
            sb.append(c);
            sb.append(str.substring(6, length));
        } else if (length <= 3) {
            return str;
        } else {
            sb.append(str.substring(3, length));
        }
        return sb.toString();
    }

    public static int getRotation(@Nullable String str, int i) {
        int i2;
        if (str == null) {
            return 0;
        }
        int i3 = -1;
        try {
            i3 = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            ZMLog.m281e(TAG, e, "getRotation NumberFormatException", new Object[0]);
        }
        if (i3 < 0) {
            return 0;
        }
        CameraInfo cameraInfo = new CameraInfo();
        NydusUtil.getCameraInfo(i3, cameraInfo);
        int realCameraOrientation = NydusUtil.getRealCameraOrientation(i3);
        int i4 = ((i + 45) / 90) * 90;
        if (!NydusUtil.isCameraMirror(i3)) {
            if (cameraInfo.facing == 1) {
                i2 = ((realCameraOrientation - i4) + 360) % 360;
            } else {
                i2 = (realCameraOrientation + i4) % 360;
            }
        } else if (cameraInfo.facing == 0) {
            i2 = ((realCameraOrientation - i4) + 360) % 360;
        } else {
            i2 = (realCameraOrientation + i4) % 360;
        }
        return i2;
    }

    public static String getTaiwanDisplayName() {
        int i = C4558R.string.zm_lbl_taiwan_116444;
        if (PTAppDelegation.getInstance().isTaiWanZH()) {
            i = C4558R.string.zm_lbl_taiwan_china_116444;
        }
        return ResourcesUtil.getString((Context) VideoBoxApplication.getInstance(), i);
    }

    private static boolean isTaiwanIdOrCode(String str) {
        return "886".equalsIgnoreCase(str) || "TW".equalsIgnoreCase(str) || "+886".equalsIgnoreCase(str) || "TWN".equalsIgnoreCase(str);
    }

    private static boolean isHongkongIdOrCode(String str) {
        return "852".equalsIgnoreCase(str) || "HK".equalsIgnoreCase(str) || "+852".equalsIgnoreCase(str) || "HKG".equalsIgnoreCase(str);
    }

    private static boolean isMacaoIdOrCode(String str) {
        return "853".equalsIgnoreCase(str) || "MO".equalsIgnoreCase(str) || "+853".equalsIgnoreCase(str) || "MAC".equalsIgnoreCase(str);
    }

    public static boolean isSpecialCountryIdOrCode(String str) {
        return isTaiwanIdOrCode(str) || isMacaoIdOrCode(str) || isHongkongIdOrCode(str);
    }

    @Nullable
    public static String getCountryName(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        if (isTaiwanIdOrCode(str)) {
            return getTaiwanDisplayName();
        }
        if (isHongkongIdOrCode(str)) {
            return ResourcesUtil.getString((Context) VideoBoxApplication.getInstance(), C4558R.string.zm_lbl_hongkong_china_114874);
        }
        if (isMacaoIdOrCode(str)) {
            return ResourcesUtil.getString((Context) VideoBoxApplication.getInstance(), C4558R.string.zm_lbl_macao_china_114874);
        }
        return new Locale(CompatUtils.getLocalDefault().getLanguage(), str).getDisplayCountry();
    }

    @Nullable
    public static String[] getAuthenticator() {
        ProxyConfig proxyConfig;
        ProxyConfig[] httpProxyConfigs = ProxySettings.getHttpProxyConfigs(VideoBoxApplication.getInstance());
        int i = 0;
        while (true) {
            if (i >= httpProxyConfigs.length) {
                proxyConfig = null;
                break;
            } else if (httpProxyConfigs[i].getProxyType() != Type.DIRECT) {
                proxyConfig = httpProxyConfigs[i];
                break;
            } else {
                i++;
            }
        }
        if (proxyConfig == null) {
            return null;
        }
        String[] strArr = new String[1];
        String[] strArr2 = new String[1];
        Type proxyType = proxyConfig.getProxyType();
        Type type = Type.DIRECT;
        int i2 = proxyType == Type.HTTP ? 1 : 0;
        if (VideoBoxApplication.getNonNullInstance().isPTApp()) {
            if (PTApp.getInstance().getAuthInfo(i2, proxyConfig.getHost(), proxyConfig.getPort(), strArr, strArr2) != 1 || TextUtils.isEmpty(strArr[0]) || TextUtils.isEmpty(strArr2[0])) {
                return null;
            }
            return new String[]{strArr[0], strArr2[0]};
        } else if (ConfMgr.getInstance().getAuthInfo(i2, proxyConfig.getHost(), proxyConfig.getPort(), strArr, strArr2) != 1 || TextUtils.isEmpty(strArr[0]) || TextUtils.isEmpty(strArr2[0])) {
            return null;
        } else {
            return new String[]{strArr[0], strArr2[0]};
        }
    }
}

package com.zipow.videobox.mainboard;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.AppContext;
import com.zipow.cmmlib.Logger;
import com.zipow.cmmlib.ZoomAppPropData;
import com.zipow.nydus.UVCPermissionUtil;
import com.zipow.nydus.UVCUtil;
import com.zipow.nydus.UVCUtil.IUVCListener;
import com.zipow.nydus.UVCUtil.SimpleUVCListener;
import com.zipow.nydus.VideoCapturer;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.IPTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfIPCPort;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.poll.PollingMgr;
import com.zipow.videobox.confapp.poll.PollingUI;
import com.zipow.videobox.config.ConfigForVCode;
import com.zipow.videobox.config.ConfigReader;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.AutoLogoffChecker;
import com.zipow.videobox.ptapp.AutoStreamConflictChecker;
import com.zipow.videobox.ptapp.PT4SIPIPCPort;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTIPCPort;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.RevokeTokenAutoLogoffChecker;
import com.zipow.videobox.sip.client.SIPIPCPort;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.AppStateMonitor;
import com.zipow.videobox.util.AppStateMonitor.IAppStateListener;
import com.zipow.videobox.util.IZMResourcesLoader;
import com.zipow.videobox.util.LogUtil;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.RawFileUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMActivity.GlobalActivityListener;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.cptshare.AndroidContext;
import p021us.zoom.videomeetings.C4558R;

public class Mainboard implements IAppStateListener {
    public static final String CONF_MAINBOARD_NAME = "zVideoApp";
    public static final String PT_MAINBOARD_NAME = "zChatApp";
    public static final String SIP_MAINBOARD_NAME = "zSipApp";
    private static final String TAG = "Mainboard";
    @Nullable
    private static Mainboard instanceConfMainboard = null;
    @Nullable
    private static Mainboard instancePTMainboard = null;
    private static boolean sIsNativeCrashed = false;
    private Context mContext;
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mInitialized = false;
    private String mName;
    private int mNetType = 0;
    @Nullable
    private BroadcastReceiver mNetworkStateReceiver;
    @NonNull
    private Runnable mRunnableRequestUVCPermission = new Runnable() {
        public void run() {
            Set devicesWithPermissionNotRequested = Mainboard.this.mUVCPermissionUtil.getDevicesWithPermissionNotRequested();
            if (devicesWithPermissionNotRequested.size() > 0) {
                Mainboard.this.mUVCPermissionUtil.requestPermission((UsbDevice) devicesWithPermissionNotRequested.iterator().next());
            }
        }
    };
    @Nullable
    private IUVCListener mUVCListener;
    /* access modifiers changed from: private */
    @Nullable
    public UVCPermissionUtil mUVCPermissionUtil;

    private native int initMainboard(String str, String str2, byte[] bArr, String[] strArr, int i);

    private static native int installNativeCrashHandlerImpl(int i, String str, String str2);

    private native boolean isNeonSupportedImpl();

    private native void notifyAppActiveImpl();

    private native void notifyAppInactiveImpl();

    private native void notifyConfProcessExitCorrectlyImpl();

    private native void notifyNetworkStateImpl(int i, int i2);

    private native void notifyUrlActionImpl(String str);

    private native boolean queryBooleanPolicyValueFromMemoryImpl();

    private native void setPBXExtensionNumberImpl(String str);

    private Mainboard(String str, Context context) {
        this.mName = str;
        this.mContext = context;
    }

    public boolean isInitialized() {
        return this.mInitialized;
    }

    public static synchronized Mainboard getMainboard() {
        synchronized (Mainboard.class) {
            VideoBoxApplication instance = VideoBoxApplication.getInstance();
            if (instance == null) {
                return null;
            }
            if (instance.isPTApp()) {
                if (instancePTMainboard == null) {
                    instancePTMainboard = new Mainboard(PT_MAINBOARD_NAME, instance);
                }
                Mainboard mainboard = instancePTMainboard;
                return mainboard;
            } else if (!instance.isConfApp()) {
                return null;
            } else {
                if (instanceConfMainboard == null) {
                    instanceConfMainboard = new Mainboard(CONF_MAINBOARD_NAME, instance);
                }
                Mainboard mainboard2 = instanceConfMainboard;
                return mainboard2;
            }
        }
    }

    public void initialize(@Nullable String str) {
        int i;
        if (!this.mInitialized) {
            System.currentTimeMillis();
            this.mNetType = NetworkUtil.getDataNetworkType(VideoBoxApplication.getInstance());
            try {
                loadNativeModules();
                if (isPTMainboard()) {
                    PTIPCPort.getInstance().initialize();
                    if (!VideoBoxApplication.getNonNullInstance().isSDKMode()) {
                        SIPIPCPort.getInstance().initialize();
                        PT4SIPIPCPort.getInstance().initialize();
                    }
                    if (!OsUtil.isAtLeastN()) {
                        VideoCapturer.getInstance().initCameraCapabilities();
                    }
                    i = 1;
                } else if (isConfMainboard()) {
                    ConfUI.getInstance().initialize();
                    ConfIPCPort.getInstance().initialize();
                    VideoCapturer.getInstance().initCameraCapabilities();
                    i = 2;
                } else {
                    i = 0;
                }
                if (!VideoBoxApplication.getNonNullInstance().isSDKMode()) {
                    installNativeCrashHandler();
                }
                ConfigForVCode readCurrentConfig = ConfigForVCode.readCurrentConfig(VideoBoxApplication.getNonNullInstance());
                initCommonResources(readCurrentConfig);
                if (isConfMainboard()) {
                    initConfResources(readCurrentConfig);
                } else if (isPTMainboard()) {
                    initPtResource(readCurrentConfig);
                }
                byte[] loadConfigData = loadConfigData(this.mContext);
                if (str == null) {
                    str = "";
                }
                initMainboard(this.mContext.getPackageName(), this.mName, loadConfigData, commandLineToArgs(str), i);
                this.mInitialized = true;
                if (isPTMainboard()) {
                    checkUpgradeSettingsData();
                    PTUI.getInstance().initialize();
                    PTApp.getInstance().initialize();
                    PTApp.getInstance().setLanguageIdAsSystemConfiguration();
                    startListenNetworkState();
                    initUVCUtils();
                    AutoLogoffChecker.getInstance().startChecker();
                    AutoStreamConflictChecker.getInstance().startChecker();
                    RevokeTokenAutoLogoffChecker.getInstance().startChecker();
                } else if (isConfMainboard()) {
                    ConfMgr.getInstance().initialize();
                    ConfMgr.getInstance().setLanguageIdAsSystemConfiguration();
                    ConfUI.getInstance().notifyNetworkType();
                    PollingMgr pollObj = ConfMgr.getInstance().getPollObj();
                    if (pollObj != null) {
                        pollObj.setPollingUI(PollingUI.getInstance());
                    }
                }
                initLog();
                startToListenActiveState();
                ZMActivity.addGlobalActivityListener(new GlobalActivityListener() {
                    public void onUserActivityOnUI() {
                    }

                    public void onActivityMoveToFront(@NonNull ZMActivity zMActivity) {
                        Mainboard.this.onNetworkState(zMActivity);
                        if (Mainboard.this.isConfMainboard()) {
                            IPTService pTService = VideoBoxApplication.getNonNullInstance().getPTService();
                            if (pTService != null) {
                                try {
                                    pTService.onConfUIMoveToFront(zMActivity.getClass().getName());
                                } catch (RemoteException unused) {
                                }
                            }
                            AppStateMonitor.getInstance().onConfUIMoveToFront();
                        } else if (Mainboard.this.isPTMainboard()) {
                            IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
                            if (confService != null) {
                                try {
                                    confService.onPTUIMoveToFront(zMActivity.getClass().getName());
                                } catch (RemoteException unused2) {
                                }
                            }
                            AppStateMonitor.getInstance().onPTUIMoveToFront();
                        }
                    }

                    public void onUIMoveToBackground() {
                        if (Mainboard.this.isConfMainboard()) {
                            IPTService pTService = VideoBoxApplication.getNonNullInstance().getPTService();
                            if (pTService != null) {
                                try {
                                    pTService.onConfUIMoveToBackground();
                                } catch (RemoteException unused) {
                                }
                            }
                            AppStateMonitor.getInstance().onConfUIMoveToBackground();
                        } else if (Mainboard.this.isPTMainboard()) {
                            IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
                            if (confService != null) {
                                try {
                                    confService.onPTUIMoveToBackground();
                                } catch (RemoteException unused2) {
                                }
                            }
                            AppStateMonitor.getInstance().onPTUIMoveToBackground();
                        }
                    }
                });
            } catch (Error e) {
                throw e;
            } catch (Exception e2) {
                throw e2;
            }
        }
    }

    private void initCommonResources(@NonNull ConfigForVCode configForVCode) {
        SparseArray sparseArray = new SparseArray();
        sparseArray.put(C4558R.raw.zm_zcacert_pin, "zcacert.pem");
        sparseArray.put(C4558R.raw.zm_dingdong, "dingdong.pcm");
        sparseArray.put(C4558R.raw.zm_leave, "leave.pcm");
        installResource(configForVCode, sparseArray);
        String string = ResourcesUtil.getString((Context) VideoBoxApplication.getInstance(), C4558R.string.zm_config_ext_common_resources_loader);
        if (!StringUtil.isEmptyOrNull(string)) {
            try {
                ((IZMResourcesLoader) Class.forName(string).newInstance()).loadResources(VideoBoxApplication.getInstance());
            } catch (Exception unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void requestPermissionForAllUVCDevices() {
        if (!UIUtil.isTV(this.mContext) && !ResourcesUtil.getBoolean(this.mContext, C4558R.bool.zm_config_no_uvc_camera, false)) {
            this.mHandler.removeCallbacks(this.mRunnableRequestUVCPermission);
            this.mHandler.postDelayed(this.mRunnableRequestUVCPermission, 1000);
        }
    }

    @SuppressLint({"NewApi"})
    private void initUVCUtils() {
        if (VERSION.SDK_INT >= UVCUtil.getMinimumSupportedSdkInt()) {
            this.mUVCListener = new SimpleUVCListener() {
                public void onDeviceAttached(@NonNull UsbDevice usbDevice) {
                    if (VideoBoxApplication.getInstance().isAtFront()) {
                        Mainboard.this.requestPermissionForAllUVCDevices();
                    }
                }

                public void onPermissionGranted(@NonNull UsbDevice usbDevice, boolean z) {
                    if (Mainboard.this.mUVCPermissionUtil != null) {
                        Mainboard.this.requestPermissionForAllUVCDevices();
                    }
                }
            };
        }
    }

    private void initConfResources(@NonNull ConfigForVCode configForVCode) {
        SparseArray sparseArray = new SparseArray();
        sparseArray.put(C4558R.raw.zm_dingdong1, "dingdong1.pcm");
        sparseArray.put(C4558R.raw.zm_record_start, "record_start.pcm");
        sparseArray.put(C4558R.raw.zm_record_stop, "record_stop.pcm");
        sparseArray.put(C4558R.raw.zm_meeting_raisehand_chime, "meeting_raisehand_chime.pcm");
        sparseArray.put(C4558R.raw.zm_meeting_chat_chime, "meeting_chat_chime.pcm");
        installResource(configForVCode, sparseArray);
    }

    private void initPtResource(@NonNull ConfigForVCode configForVCode) {
        SparseArray sparseArray = new SparseArray();
        sparseArray.put(C4558R.raw.zm_sip_dtmf_a, "dtmf_a.wav");
        sparseArray.put(C4558R.raw.zm_ring, "ring.pcm");
        sparseArray.put(C4558R.raw.zm_zpbxcacert, "root_cert_zpbxcacert.pem");
        sparseArray.put(C4558R.raw.zm_sip_oos, "oos.wav");
        installResource(configForVCode, sparseArray);
    }

    private void installResource(@NonNull ConfigForVCode configForVCode, @NonNull SparseArray<String> sparseArray) {
        List list = configForVCode.getmInstallResFileNameList();
        if (list == null) {
            list = new ArrayList();
            configForVCode.setmInstallResFileNameList(list);
        }
        int size = sparseArray.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                int keyAt = sparseArray.keyAt(i);
                String str = (String) sparseArray.valueAt(i);
                boolean contains = list.contains(str);
                if (RawFileUtil.installRawFileToLocal(VideoBoxApplication.getInstance(), keyAt, str, contains) && !contains) {
                    list.add(str);
                }
            }
            configForVCode.setmInstallResFileNameList(list);
            configForVCode.save();
        }
    }

    private void checkUpgradeSettingsData() {
        ZoomAppPropData instance = ZoomAppPropData.getInstance();
        if (instance != null) {
            String queryWithKey = instance.queryWithKey(ZoomAppPropData.SETTINGS_VERSION, "2.0");
            if ("2.0".equals(queryWithKey)) {
                instance.setKeyValue(ZoomAppPropData.SETTINGS_VERSION, "2.5");
                String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.SCREEN_NAME, "");
                if (StringUtil.isEmptyOrNull(PTApp.getInstance().getDeviceUserName())) {
                    if (!StringUtil.isEmptyOrNull(readStringValue)) {
                        PTApp.getInstance().setDeviceUserName(readStringValue);
                    } else {
                        PTApp.getInstance().setDeviceUserName(getDeviceDefaultName());
                    }
                }
            } else if ("2.1".equals(queryWithKey)) {
                if (getDeviceDefaultNameV2_1().equals(PTApp.getInstance().getDeviceUserName())) {
                    PTApp.getInstance().setDeviceUserName(getDeviceDefaultName());
                } else {
                    PreferenceUtil.saveBooleanValue(PreferenceUtil.IS_DEVICE_NAME_CUSTOMIZED, true);
                }
                instance.setKeyValue(ZoomAppPropData.SETTINGS_VERSION, "2.5");
            } else if ("2.5".equals(queryWithKey)) {
                mapDeviceSettingsToZoom();
            }
            ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
            if (aBContactsHelper != null && aBContactsHelper.needValidatePhoneNumber()) {
                String verifiedPhoneNumber = aBContactsHelper.getVerifiedPhoneNumber();
                if (!StringUtil.isEmptyOrNull(verifiedPhoneNumber)) {
                    String countryCodeFromFormatedPhoneNumber = PhoneNumberUtil.getCountryCodeFromFormatedPhoneNumber(verifiedPhoneNumber);
                    if (!StringUtil.isEmptyOrNull(countryCodeFromFormatedPhoneNumber)) {
                        String substring = verifiedPhoneNumber.substring(countryCodeFromFormatedPhoneNumber.length() + 1);
                        if (substring.startsWith("0")) {
                            String substring2 = substring.substring(1);
                            StringBuilder sb = new StringBuilder();
                            sb.append("+");
                            sb.append(countryCodeFromFormatedPhoneNumber);
                            sb.append(substring2);
                            verifiedPhoneNumber = sb.toString();
                        }
                    }
                }
                if (verifiedPhoneNumber == null) {
                    verifiedPhoneNumber = "";
                }
                aBContactsHelper.updateValidatePhoneNumber(verifiedPhoneNumber);
            }
        }
    }

    private void mapDeviceSettingsToZoom() {
        if (!PreferenceUtil.readBooleanValue(PreferenceUtil.IS_DEVICE_NAME_CUSTOMIZED, false)) {
            PTApp.getInstance().setDeviceUserName(getDeviceDefaultName());
        }
    }

    private static String getDeviceDefaultNameV2_1() {
        StringBuilder sb = new StringBuilder();
        if (!"Unknown".equalsIgnoreCase(Build.MANUFACTURER)) {
            sb.append(Build.MANUFACTURER);
            sb.append(OAuth.SCOPE_DELIMITER);
        }
        sb.append(Build.MODEL);
        return sb.toString();
    }

    @NonNull
    public static String getDeviceDefaultName() {
        String localBluetoothName = getLocalBluetoothName();
        if (!StringUtil.isEmptyOrNull(localBluetoothName)) {
            return localBluetoothName;
        }
        StringBuilder sb = new StringBuilder();
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        if (StringUtil.isEmptyOrNull(str)) {
            str = "Unknown";
        }
        if (StringUtil.isEmptyOrNull(str2)) {
            str2 = "Unknown";
        }
        if (!"Unknown".equalsIgnoreCase(str)) {
            if ("samsung".equalsIgnoreCase(str)) {
                sb.append("Samsung ");
            } else if (!str2.toLowerCase().startsWith(str.toLowerCase())) {
                sb.append(str);
                sb.append(OAuth.SCOPE_DELIMITER);
            }
        }
        sb.append(str2);
        return sb.toString();
    }

    private static String getLocalBluetoothName() {
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter == null) {
                return null;
            }
            return defaultAdapter.getName();
        } catch (Exception e) {
            ZMLog.m289w(TAG, e, "getLocalBluetoothName exception", new Object[0]);
            return null;
        }
    }

    private void initLog() {
        Logger instance = Logger.getInstance();
        AppContext appContext = new AppContext(AppContext.PREFER_NAME_CHAT);
        String queryWithKey = appContext.queryWithKey(ConfigReader.KEY_ENABLELOG, AppContext.APP_NAME_CHAT);
        String queryWithKey2 = appContext.queryWithKey(ConfigReader.KEY_LOG_LEVEL, AppContext.APP_NAME_CHAT);
        if ("true".equals(queryWithKey)) {
            instance.setEnabled(true);
        } else {
            instance.setEnabled(false);
        }
        if (queryWithKey2 == null || queryWithKey2.length() == 0) {
            instance.setLevel(1);
        } else if ("info".equals(queryWithKey2)) {
            instance.setLevel(1);
        } else if ("warning".equals(queryWithKey2)) {
            instance.setLevel(2);
        } else {
            instance.setLevel(1);
        }
        instance.startNativeLog(true);
    }

    @Nullable
    private String[] commandLineToArgs(@Nullable String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("mainboard ");
        sb.append(str);
        return sb.toString().split("\\s+");
    }

    /* access modifiers changed from: private */
    public boolean isPTMainboard() {
        return PT_MAINBOARD_NAME.equals(this.mName);
    }

    /* access modifiers changed from: private */
    public boolean isConfMainboard() {
        return CONF_MAINBOARD_NAME.equals(this.mName);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:46:0x006c, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x006d, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0071, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0072, code lost:
        r5 = r2;
        r2 = r7;
        r7 = r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x006c A[ExcHandler: all (th java.lang.Throwable), Splitter:B:14:0x0029] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] loadConfigData(android.content.Context r7) {
        /*
            r6 = this;
            boolean r7 = r6.isPTMainboard()
            r0 = 0
            if (r7 == 0) goto L_0x0013
            boolean r7 = r6.isNeonSupported()
            if (r7 == 0) goto L_0x0010
            int r7 = p021us.zoom.videomeetings.C4558R.raw.zm_modules_chat_neon
            goto L_0x0024
        L_0x0010:
            int r7 = p021us.zoom.videomeetings.C4558R.raw.zm_modules_chat
            goto L_0x0024
        L_0x0013:
            boolean r7 = r6.isConfMainboard()
            if (r7 == 0) goto L_0x0085
            boolean r7 = r6.isNeonSupported()
            if (r7 == 0) goto L_0x0022
            int r7 = p021us.zoom.videomeetings.C4558R.raw.zm_modules_video_neon
            goto L_0x0024
        L_0x0022:
            int r7 = p021us.zoom.videomeetings.C4558R.raw.zm_modules_video
        L_0x0024:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0084 }
            r1.<init>()     // Catch:{ IOException -> 0x0084 }
            com.zipow.videobox.VideoBoxApplication r2 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
            android.content.res.Resources r2 = r2.getResources()     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
            java.io.InputStream r7 = r2.openRawResource(r7)     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r2]     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
        L_0x0039:
            int r3 = r7.read(r2)     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
            if (r3 <= 0) goto L_0x0043
            r4 = 0
            r1.write(r2, r4, r3)     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
        L_0x0043:
            if (r3 > 0) goto L_0x0039
            byte[] r2 = r1.toByteArray()     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
            if (r7 == 0) goto L_0x004e
            r7.close()     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
        L_0x004e:
            r1.close()     // Catch:{ IOException -> 0x0084 }
            return r2
        L_0x0052:
            r2 = move-exception
            r3 = r0
            goto L_0x005b
        L_0x0055:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0057 }
        L_0x0057:
            r3 = move-exception
            r5 = r3
            r3 = r2
            r2 = r5
        L_0x005b:
            if (r7 == 0) goto L_0x006b
            if (r3 == 0) goto L_0x0068
            r7.close()     // Catch:{ Throwable -> 0x0063, all -> 0x006c }
            goto L_0x006b
        L_0x0063:
            r7 = move-exception
            r3.addSuppressed(r7)     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
            goto L_0x006b
        L_0x0068:
            r7.close()     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
        L_0x006b:
            throw r2     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
        L_0x006c:
            r7 = move-exception
            r2 = r0
            goto L_0x0075
        L_0x006f:
            r7 = move-exception
            throw r7     // Catch:{ all -> 0x0071 }
        L_0x0071:
            r2 = move-exception
            r5 = r2
            r2 = r7
            r7 = r5
        L_0x0075:
            if (r2 == 0) goto L_0x0080
            r1.close()     // Catch:{ Throwable -> 0x007b }
            goto L_0x0083
        L_0x007b:
            r1 = move-exception
            r2.addSuppressed(r1)     // Catch:{ IOException -> 0x0084 }
            goto L_0x0083
        L_0x0080:
            r1.close()     // Catch:{ IOException -> 0x0084 }
        L_0x0083:
            throw r7     // Catch:{ IOException -> 0x0084 }
        L_0x0084:
            return r0
        L_0x0085:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.mainboard.Mainboard.loadConfigData(android.content.Context):byte[]");
    }

    private void loadNativeModules() {
        System.currentTimeMillis();
        if (isPTMainboard()) {
            System.loadLibrary("crypto_sb");
            System.loadLibrary("ssl_sb");
            System.loadLibrary("cmmlib");
            System.loadLibrary("zoom_tp");
            System.loadLibrary("zWebService");
            System.loadLibrary("nydus");
            System.loadLibrary("zoom");
            System.loadLibrary("zAutoUpdate");
            System.loadLibrary("srtp");
            if (!VideoBoxApplication.getInstance().isSDKMode()) {
                System.loadLibrary("sipsdk");
                System.loadLibrary("zSipCallApp");
                System.loadLibrary(SIP_MAINBOARD_NAME);
            }
            System.loadLibrary(PT_MAINBOARD_NAME);
            System.loadLibrary("zChatUI");
            System.loadLibrary("zLoader");
            System.loadLibrary("zData");
            if (VideoBoxApplication.getInstance().isSDKMode()) {
                return;
            }
            if (isNeonSupported()) {
                System.loadLibrary("viper_neon");
                System.loadLibrary("mcm_neon");
                return;
            }
            System.loadLibrary("viper");
            System.loadLibrary("mcm");
        } else if (isConfMainboard()) {
            System.loadLibrary("crypto_sb");
            System.loadLibrary("ssl_sb");
            System.loadLibrary("cmmlib");
            System.loadLibrary("zoom_tp");
            System.loadLibrary("zWebService");
            System.loadLibrary("zlt");
            System.loadLibrary("nydus");
            System.loadLibrary("zoom");
            System.loadLibrary(CONF_MAINBOARD_NAME);
            System.loadLibrary("zVideoUI");
            System.loadLibrary("zLoader");
            System.loadLibrary("zData");
            System.loadLibrary("ssb_sdk");
            System.loadLibrary("annotate");
            if (isNeonSupported()) {
                System.loadLibrary("viper_neon");
                System.loadLibrary("mcm_neon");
            } else {
                System.loadLibrary("viper");
                System.loadLibrary("mcm");
            }
            AndroidContext.initialize(VideoBoxApplication.getInstance());
        }
    }

    public void notifyUrlAction(String str) {
        notifyUrlActionImpl(str);
    }

    private void notifyNetworkState(int i) {
        notifyNetworkStateImpl(i, 0);
    }

    private void startListenNetworkState() {
        if (this.mNetworkStateReceiver == null) {
            this.mNetworkStateReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, @Nullable Intent intent) {
                    if (intent != null) {
                        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                            Mainboard.this.onNetworkState(context);
                        }
                    }
                }
            };
            VideoBoxApplication.getInstance().registerReceiver(this.mNetworkStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    /* access modifiers changed from: private */
    public void onNetworkState(@Nullable Context context) {
        if (context != null) {
            int dataNetworkType = NetworkUtil.getDataNetworkType(context);
            if (dataNetworkType != this.mNetType) {
                this.mNetType = dataNetworkType;
                notifyNetworkState(NetworkUtil.hasDataNetwork(context) ^ true ? 1 : 0);
            }
        }
    }

    private void startToListenActiveState() {
        AppStateMonitor.getInstance().addListener(this);
        AppStateMonitor.getInstance().start();
    }

    public void onAppActivated() {
        notifyAppActiveImpl();
        if (isPTMainboard()) {
            this.mUVCPermissionUtil = UVCPermissionUtil.getInstance(this.mContext);
            UVCPermissionUtil uVCPermissionUtil = this.mUVCPermissionUtil;
            if (uVCPermissionUtil != null) {
                uVCPermissionUtil.addUVCListener(this.mUVCListener);
                requestPermissionForAllUVCDevices();
            }
            CmmSIPCallManager.getInstance().onSipActivated();
        }
    }

    public void onAppInactivated() {
        notifyAppInactiveImpl();
        if (isPTMainboard()) {
            UVCPermissionUtil uVCPermissionUtil = this.mUVCPermissionUtil;
            if (uVCPermissionUtil != null) {
                uVCPermissionUtil.removeUVCListener(this.mUVCListener);
            }
            CmmSIPCallManager.getInstance().onSipInactivated();
        }
    }

    public void notifyConfProcessExitCorrectly() {
        notifyConfProcessExitCorrectlyImpl();
    }

    public boolean isNeonSupported() {
        return isNeonSupportedImpl();
    }

    public static void onNativeCrashed(int i, String str) {
        String str2;
        if (i == 4) {
            str2 = "SIGILL";
        } else if (i == 11) {
            str2 = "SIGSEGV";
        } else if (i == 13) {
            str2 = "SIGPIPE";
        } else if (i != 16) {
            switch (i) {
                case 6:
                    str2 = "SIGABRT";
                    break;
                case 7:
                    str2 = "SIGBUS";
                    break;
                case 8:
                    str2 = "SIGFPE";
                    break;
                default:
                    str2 = "???";
                    break;
            }
        } else {
            str2 = "SIGSTKFLT";
        }
        ZMLog.m280e(TAG, "onNativeCrashed, signum=%d, signame=%s, info=%s", Integer.valueOf(i), str2, str);
        sIsNativeCrashed = true;
        VideoBoxApplication.getInstance().notifyStabilityServiceCrashInfo();
    }

    public static boolean isNativeCrashed() {
        return sIsNativeCrashed;
    }

    private static void installNativeCrashHandler() {
        String logFolder = LogUtil.getLogFolder();
        if (logFolder == null) {
            Log.e(TAG, "can not get log folder , installNativeCrashHandler failed");
        } else {
            installNativeCrashHandlerImpl(VERSION.SDK_INT, logFolder, LogUtil.getDeviceInfo());
        }
    }

    public boolean queryBooleanPolicyValueFromMemory() {
        return queryBooleanPolicyValueFromMemoryImpl();
    }

    public void setPBXExtensionNumber(String str) {
        if (!TextUtils.isEmpty(str)) {
            setPBXExtensionNumberImpl(str);
        }
    }
}

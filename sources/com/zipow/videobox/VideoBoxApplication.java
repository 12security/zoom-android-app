package com.zipow.videobox;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.FirebaseApp;
import com.zipow.cmmlib.AppContext;
import com.zipow.cmmlib.AppUtil;
import com.zipow.cmmlib.CmmProxySettings;
import com.zipow.cmmlib.Logger;
import com.zipow.videobox.IConfService.Stub;
import com.zipow.videobox.common.LeaveConfAction;
import com.zipow.videobox.confapp.ConfIPCPort;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.config.ConfigReader;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.nos.NOSMgr;
import com.zipow.videobox.ptapp.IncomingCallManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTIPCPort;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.sip.client.AssistantAppClientMgr;
import com.zipow.videobox.stabilility.JavaCrashHandler;
import com.zipow.videobox.stabilility.StabilityService;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.IPCHelper;
import com.zipow.videobox.util.ImageLoader;
import com.zipow.videobox.util.LogUtil;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMDomainUtil;
import com.zipow.videobox.util.ZMPolicyDataHelper;
import com.zipow.videobox.util.ZMServiceHelper;
import com.zipow.videobox.util.ZMUtils;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zoom.androidlib.app.IZMAppUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.cache.IoUtils;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.HardwareUtil;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ImageCache;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.common.ZMThirdPartyUtils;
import p021us.zoom.util.AndroidContext;
import p021us.zoom.videomeetings.C4558R;

public class VideoBoxApplication extends BaseVideoBoxApplication {
    private static final String CONF_PROCESS_EXT_NAME = "conf";
    private static final int MAX_CONF_LOG_FILES_COUNT = 15;
    private static final int MAX_COUNT_CRASH_REPORT_IN_ONE_DAY = 3;
    private static final int MAX_PT_LOG_FILES_COUNT = 15;
    private static final int MAX_UTIL_LOG_FILES_COUNT = 15;
    private static final long ONE_DAY = 86400000;
    public static final int PROCESS_TYPE_CONF = 1;
    public static final int PROCESS_TYPE_PT = 0;
    public static final int PROCESS_TYPE_SIP = 3;
    public static final int PROCESS_TYPE_STB = 2;
    private static final String PT_PROCESS_EXT_NAME = "";
    private static final String SIP_PROCESS_EXT_NAME = "sip";
    public static final int START_CONF_SERVICE_ERROR_INTERRUPTED = 1;
    public static final int START_CONF_SERVICE_ERROR_SUCCESS = 0;
    public static final int START_CONF_SERVICE_ERROR_WAITING_INIT_TIMEOUT = 3;
    public static final int START_CONF_SERVICE_ERROR_WAITING_PID_TIMEOUT = 2;
    private static final String STD_PROCESS_EXT_NAME = "stb";
    @Nullable
    private static String TAG;
    @Nullable
    private static VideoBoxApplication gInstance;
    @Nullable
    private static Context gZoomSdkApplication;
    @NonNull
    private Runnable mConfMessageLoopRunnable = new Runnable() {
        long lastDispatchIdleMessageTime = 0;

        public void run() {
            if (!VideoBoxApplication.this.mbMessageLoopStopped) {
                long currentTimeMillis = System.currentTimeMillis();
                long j = this.lastDispatchIdleMessageTime;
                if (currentTimeMillis - j >= 300 || currentTimeMillis < j) {
                    this.lastDispatchIdleMessageTime = currentTimeMillis;
                    ConfMgr.getInstance().dispatchIdleMessage();
                    System.currentTimeMillis();
                }
                VideoBoxApplication.this.startConfMessageLoop(50);
            }
        }
    };
    @NonNull
    private ListenerList mConfProcessListenerList = new ListenerList();
    @Nullable
    private IConfService mConfService = null;
    @Nullable
    private ServiceConnection mConfServiceConnection = null;
    @NonNull
    private Handler mHandler = new Handler();
    private Timer mMemMonTimer;
    @NonNull
    private Runnable mPTMessageLoopRunnable = new Runnable() {
        long lastDispatchIdleMessageTime = 0;

        public void run() {
            if (!VideoBoxApplication.this.mbMessageLoopStopped) {
                long currentTimeMillis = System.currentTimeMillis();
                long j = this.lastDispatchIdleMessageTime;
                if (currentTimeMillis - j >= 300 || currentTimeMillis < j) {
                    this.lastDispatchIdleMessageTime = currentTimeMillis;
                    PTApp.getInstance().dispatchIdleMessage();
                    AssistantAppClientMgr.getInstance().dispatchIdleMessage();
                    System.currentTimeMillis();
                }
                VideoBoxApplication.this.startPTMessageLoop();
            }
        }
    };
    @Nullable
    private IPTService mPTService = null;
    @Nullable
    private ServiceConnection mPTServiceConnection = null;
    @Nullable
    private WakeLock mPartialWakeLock = null;
    private int mProcessType = -1;
    private volatile long mStartForegroundServiceTime = 0;
    private boolean mbAppInitialized = false;
    /* access modifiers changed from: private */
    public boolean mbMessageLoopStopped = false;

    public interface IConfProcessListener extends IListener {
        void onConfProcessStarted();

        void onConfProcessStopped();
    }

    private void checkFD() {
    }

    public void notifyStabilityServiceCrashInfo() {
    }

    private VideoBoxApplication(Context context, int i, @Nullable String str) {
        super(context);
        if (str != null) {
            this.mConfProcessExtName = str;
        }
        this.mProcessType = i;
    }

    @Deprecated
    @Nullable
    public static synchronized VideoBoxApplication getInstance() {
        VideoBoxApplication videoBoxApplication;
        synchronized (VideoBoxApplication.class) {
            videoBoxApplication = gInstance;
        }
        return videoBoxApplication;
    }

    @NonNull
    public static synchronized VideoBoxApplication getNonNullInstance() {
        VideoBoxApplication videoBoxApplication;
        synchronized (VideoBoxApplication.class) {
            videoBoxApplication = gInstance;
        }
        return videoBoxApplication;
    }

    public static synchronized void setZoomSDKApplicationContext(Context context) {
        synchronized (VideoBoxApplication.class) {
            gZoomSdkApplication = context;
        }
    }

    @Nullable
    public static synchronized Context getZoomSDKApplicatonContext() {
        Context context;
        synchronized (VideoBoxApplication.class) {
            context = gZoomSdkApplication;
        }
        return context;
    }

    public static synchronized Context getGlobalContext() {
        synchronized (VideoBoxApplication.class) {
            if (gInstance != null) {
                VideoBoxApplication videoBoxApplication = gInstance;
                return videoBoxApplication;
            } else if (gZoomSdkApplication == null) {
                return null;
            } else {
                Context context = gZoomSdkApplication;
                return context;
            }
        }
    }

    public static synchronized void initialize(@NonNull Context context, boolean z) {
        synchronized (VideoBoxApplication.class) {
            initialize(context, z, getProcessType(context), null);
        }
    }

    public static synchronized void initialize(Context context, boolean z, int i) {
        synchronized (VideoBoxApplication.class) {
            initialize(context, z, i, null);
        }
    }

    public static synchronized void initialize(Context context, boolean z, int i, String str) {
        synchronized (VideoBoxApplication.class) {
            if (gInstance == null) {
                gInstance = new VideoBoxApplication(context, i, str);
                gInstance.onCreated(z);
            }
        }
    }

    public void addConfProcessListener(IConfProcessListener iConfProcessListener) {
        this.mConfProcessListenerList.add(iConfProcessListener);
    }

    public void removeConfProcessListener(IConfProcessListener iConfProcessListener) {
        this.mConfProcessListenerList.remove(iConfProcessListener);
    }

    private void removeUnSecurityFile() {
        if (TextUtils.equals(Environment.getExternalStorageState(), "mounted")) {
            File externalFilesDir = getExternalFilesDir(null);
            if (externalFilesDir != null) {
                File parentFile = externalFilesDir.getParentFile();
                File file = new File(parentFile, "data");
                final File file2 = new File(parentFile, "data1");
                try {
                    String[] list = file.list();
                    if (file.exists() && list != null && list.length > 0) {
                        file.renameTo(file2);
                    }
                    if (file2.exists()) {
                        new Thread() {
                            public void run() {
                                FileUtils.deleteFile(file2.getAbsolutePath());
                            }
                        }.start();
                    }
                } catch (Exception unused) {
                }
                File file3 = new File(parentFile, "files");
                final File file4 = new File(parentFile, "files1");
                try {
                    String[] list2 = file3.list();
                    if (file3.exists() && list2 != null && list2.length > 0) {
                        file3.renameTo(file4);
                    }
                    if (file4.exists()) {
                        new Thread() {
                            public void run() {
                                FileUtils.deleteFile(file4.getAbsolutePath());
                            }
                        }.start();
                    }
                } catch (Exception unused2) {
                }
            }
        }
    }

    private void onCreated(boolean z) {
        checkFD();
        PreferenceUtil.initialize(this);
        if (!z) {
            prepareNativeCrash();
            removeUnSecurityFile();
        }
        if (OsUtil.isAtLeastP()) {
            WebView.setDataDirectorySuffix(Application.getProcessName());
        }
        ZMThirdPartyUtils.init(new IZMAppUtil() {
            @Nullable
            public String getCachePath() {
                return AppUtil.getCachePath();
            }

            public boolean hasEnoughDiskSpace(String str, long j) {
                return AppUtil.hasEnoughDiskSpace(str, j);
            }

            public boolean isZoomApp() {
                return ZMUtils.isZoomApp(getAppContext());
            }

            @Nullable
            public String getShareCachePathByExtension(@Nullable String str, String str2) {
                return AppUtil.getShareCachePathByExtension(str, str2);
            }

            @Nullable
            public Context getAppContext() {
                return VideoBoxApplication.getGlobalContext();
            }
        }, 1, 2, 4, 3);
        ZMThirdPartyUtils.checkShareCloudFileClientInfo(this, ZMUtils.isZoomApp(this));
        AndroidContext.initialize(this);
        AppContext.initialize(this);
        CmmProxySettings.initialize(this);
        initLog();
        if (!z) {
            installJavaCrashHandler();
            FirebaseApp.initializeApp(this);
        }
        HeadsetUtil.getInstance().initialize(this, VoiceEngineCompat.isBluetoothScoSupported());
        initApp(z);
        UIMgr.initialize(this);
        registerComponentCallbacks(ImageCache.getInstance());
        if (!z) {
            ImageLoader.getInstance().init();
        }
    }

    private void initApp(boolean z) {
        this.mbSDKMode = z;
        if (isPTApp()) {
            this.mbAppInitialized = true;
            NotificationMgr.removeConfNotification(this);
            if (!z) {
                IncomingCallManager.getInstance().initialize(this);
                NOSMgr.getInstance().initialize(this);
                startStabilityService();
            }
            removeTempFiles();
            connectConfService();
            try {
                CookieSyncManager.createInstance(this);
            } catch (Exception unused) {
            }
            if (!z) {
                checkAutoRecovery();
            }
            PreferenceUtil.removeValue(PreferenceUtil.CAMERA_IS_FREEZED);
            if (z) {
                initPTMainboard();
            }
        } else if (isConfApp()) {
            this.mbAppInitialized = true;
            NotificationMgr.removeConfNotification(this);
            if (!isConfProcessLegal()) {
                killConfProcessAfter(1000);
                return;
            }
            setConfProcessLegal(false);
            connectPTService();
            if (!z) {
                startStabilityService();
            }
        }
    }

    private void killConfProcessAfter(long j) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                VideoBoxApplication.this.stopConfService();
            }
        }, j);
    }

    public void initPTMainboard() {
        initPTMainboard(false);
    }

    public void initPTMainboard(boolean z) {
        if (isPTApp()) {
            if (isMainThread()) {
                setPTProcessId(Process.myPid());
                if (!this.mbAppInitialized) {
                    initApp(z);
                }
                Mainboard mainboard = Mainboard.getMainboard();
                if (!mainboard.isInitialized()) {
                    SystemClock.uptimeMillis();
                    mainboard.initialize(null);
                    setConfProcessId(-1);
                    setSipProcessId(-1);
                    startPTMessageLoop();
                    startDeadLockDetector();
                    initConfServiceUrl();
                    if (Logger.getInstance().isEnabled()) {
                        startMemMonitor();
                    }
                }
                ZMServiceHelper.doServiceActionInFront(PTService.ACTION_DEAMON, PTService.class);
                return;
            }
            throw new RuntimeException("called from wrong thread");
        }
    }

    public void initConfMainboard(String str) {
        if (isConfApp()) {
            if (isMainThread()) {
                setConfProcessId(Process.myPid());
                if (!this.mbAppInitialized) {
                    initApp(false);
                }
                Mainboard mainboard = Mainboard.getMainboard();
                if (!mainboard.isInitialized()) {
                    mainboard.initialize(str);
                    Logger.getInstance().startNativeLog(true);
                    startConfMessageLoop(50);
                    setConfProcessReadyFlag(true);
                    startDeadLockDetector();
                    if (Logger.getInstance().isEnabled()) {
                        startMemMonitor();
                    }
                    ZMPolicyDataHelper.getInstance().setBooleanValue(123, false);
                }
                return;
            }
            throw new RuntimeException("called from wrong thread");
        }
    }

    private void setConfProcessReadyFlag(boolean z) {
        File filesDir = getFilesDir();
        if (filesDir != null) {
            filesDir.mkdir();
            if (filesDir.exists() && filesDir.isDirectory()) {
                String absolutePath = filesDir.getAbsolutePath();
                if (!absolutePath.endsWith("/")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(absolutePath);
                    sb.append("/");
                    absolutePath = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(absolutePath);
                sb2.append("conf_process_ready");
                File file = new File(sb2.toString());
                if (z) {
                    try {
                        file.createNewFile();
                    } catch (IOException unused) {
                    }
                } else {
                    file.delete();
                }
            }
        }
    }

    public boolean isConfProcessReady() {
        File filesDir = getFilesDir();
        if (filesDir == null) {
            return true;
        }
        String absolutePath = filesDir.getAbsolutePath();
        if (!absolutePath.endsWith("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append(absolutePath);
            sb.append("/");
            absolutePath = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(absolutePath);
        sb2.append("conf_process_ready");
        return new File(sb2.toString()).exists();
    }

    public void setConfUIPreloaded(boolean z) {
        File filesDir = getFilesDir();
        if (filesDir != null) {
            filesDir.mkdir();
            if (filesDir.exists() && filesDir.isDirectory()) {
                String absolutePath = filesDir.getAbsolutePath();
                if (!absolutePath.endsWith("/")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(absolutePath);
                    sb.append("/");
                    absolutePath = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(absolutePath);
                sb2.append("conf_ui_preloaded");
                File file = new File(sb2.toString());
                if (z) {
                    try {
                        file.createNewFile();
                    } catch (IOException unused) {
                    }
                } else {
                    file.delete();
                }
            }
        }
    }

    public boolean isConfUIPreloaded() {
        File filesDir = getFilesDir();
        if (filesDir == null) {
            return true;
        }
        String absolutePath = filesDir.getAbsolutePath();
        if (!absolutePath.endsWith("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append(absolutePath);
            sb.append("/");
            absolutePath = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(absolutePath);
        sb2.append("conf_ui_preloaded");
        return new File(sb2.toString()).exists();
    }

    private void setConfProcessId(int i) {
        FileOutputStream fileOutputStream;
        File filesDir = getFilesDir();
        if (filesDir != null) {
            filesDir.mkdir();
            if (filesDir.exists() && filesDir.isDirectory()) {
                String absolutePath = filesDir.getAbsolutePath();
                if (!absolutePath.endsWith("/")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(absolutePath);
                    sb.append("/");
                    absolutePath = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(absolutePath);
                sb2.append("conf_process_id");
                File file = new File(sb2.toString());
                if (i > 0) {
                    try {
                        file.createNewFile();
                        fileOutputStream = new FileOutputStream(file);
                        try {
                            fileOutputStream.write(String.valueOf(i).getBytes());
                            fileOutputStream.flush();
                        } catch (Exception unused) {
                        } catch (Throwable th) {
                            th = th;
                            IoUtils.closeSilently(fileOutputStream);
                            throw th;
                        }
                    } catch (Exception unused2) {
                        fileOutputStream = null;
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = null;
                        IoUtils.closeSilently(fileOutputStream);
                        throw th;
                    }
                    IoUtils.closeSilently(fileOutputStream);
                } else {
                    file.delete();
                }
            }
        }
    }

    private void setPTProcessId(int i) {
        FileOutputStream fileOutputStream;
        File filesDir = getFilesDir();
        if (filesDir != null) {
            filesDir.mkdir();
            if (filesDir.exists() && filesDir.isDirectory()) {
                String absolutePath = filesDir.getAbsolutePath();
                if (!absolutePath.endsWith("/")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(absolutePath);
                    sb.append("/");
                    absolutePath = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(absolutePath);
                sb2.append("pt_process_id");
                File file = new File(sb2.toString());
                if (i > 0) {
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        try {
                            file.createNewFile();
                            fileOutputStream.write(String.valueOf(i).getBytes());
                            fileOutputStream.flush();
                        } catch (Exception unused) {
                        } catch (Throwable th) {
                            th = th;
                            IoUtils.closeSilently(fileOutputStream);
                            throw th;
                        }
                    } catch (Exception unused2) {
                        fileOutputStream = null;
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = null;
                        IoUtils.closeSilently(fileOutputStream);
                        throw th;
                    }
                    IoUtils.closeSilently(fileOutputStream);
                } else {
                    file.delete();
                }
            }
        }
    }

    public int getPTProcessId() {
        FileInputStream fileInputStream;
        Throwable th;
        File filesDir = getFilesDir();
        if (filesDir == null) {
            return -1;
        }
        String absolutePath = filesDir.getAbsolutePath();
        if (!absolutePath.endsWith("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append(absolutePath);
            sb.append("/");
            absolutePath = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(absolutePath);
        sb2.append("pt_process_id");
        File file = new File(sb2.toString());
        if (!file.exists()) {
            return -1;
        }
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[fileInputStream.available()];
                fileInputStream.read(bArr);
                int parseInt = Integer.parseInt(new String(bArr));
                IoUtils.closeSilently(fileInputStream);
                return parseInt;
            } catch (Exception unused) {
                IoUtils.closeSilently(fileInputStream);
                return -1;
            } catch (Throwable th2) {
                th = th2;
                IoUtils.closeSilently(fileInputStream);
                throw th;
            }
        } catch (Exception unused2) {
            fileInputStream = null;
            IoUtils.closeSilently(fileInputStream);
            return -1;
        } catch (Throwable th3) {
            fileInputStream = null;
            th = th3;
            IoUtils.closeSilently(fileInputStream);
            throw th;
        }
    }

    private void setSipProcessId(int i) {
        FileOutputStream fileOutputStream;
        File filesDir = getFilesDir();
        if (filesDir != null) {
            filesDir.mkdir();
            if (filesDir.exists() && filesDir.isDirectory()) {
                String absolutePath = filesDir.getAbsolutePath();
                if (!absolutePath.endsWith("/")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(absolutePath);
                    sb.append("/");
                    absolutePath = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(absolutePath);
                sb2.append("sip_process_id");
                File file = new File(sb2.toString());
                if (i > 0) {
                    try {
                        file.createNewFile();
                        fileOutputStream = new FileOutputStream(file);
                        try {
                            fileOutputStream.write(String.valueOf(i).getBytes());
                            fileOutputStream.flush();
                        } catch (Exception unused) {
                        } catch (Throwable th) {
                            th = th;
                            IoUtils.closeSilently(fileOutputStream);
                            throw th;
                        }
                    } catch (Exception unused2) {
                        fileOutputStream = null;
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = null;
                        IoUtils.closeSilently(fileOutputStream);
                        throw th;
                    }
                    IoUtils.closeSilently(fileOutputStream);
                } else {
                    file.delete();
                }
            }
        }
    }

    public int getSipProcessId() {
        FileInputStream fileInputStream;
        Throwable th;
        File filesDir = getFilesDir();
        if (filesDir == null) {
            return -1;
        }
        String absolutePath = filesDir.getAbsolutePath();
        if (!absolutePath.endsWith("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append(absolutePath);
            sb.append("/");
            absolutePath = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(absolutePath);
        sb2.append("sip_process_id");
        File file = new File(sb2.toString());
        if (!file.exists()) {
            return -1;
        }
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[fileInputStream.available()];
                fileInputStream.read(bArr);
                int parseInt = Integer.parseInt(new String(bArr));
                IoUtils.closeSilently(fileInputStream);
                return parseInt;
            } catch (Exception unused) {
                IoUtils.closeSilently(fileInputStream);
                return -1;
            } catch (Throwable th2) {
                th = th2;
                IoUtils.closeSilently(fileInputStream);
                throw th;
            }
        } catch (Exception unused2) {
            fileInputStream = null;
            IoUtils.closeSilently(fileInputStream);
            return -1;
        } catch (Throwable th3) {
            fileInputStream = null;
            th = th3;
            IoUtils.closeSilently(fileInputStream);
            throw th;
        }
    }

    private boolean isMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    /* access modifiers changed from: private */
    public boolean isC2DMUsed() {
        return !StringUtil.isEmptyOrNull(PreferenceUtil.readStringValue(PreferenceUtil.GCM_REGISTRATION_ID, null));
    }

    private void startStabilityService() {
        if (!isC2DMUsed() && !OsUtil.isAtLeastO()) {
            Intent intent = new Intent();
            intent.setClassName(getPackageName(), StabilityService.class.getName());
            intent.setAction(StabilityService.ACTION_LOG_CRASH);
            try {
                CompatUtils.startService(this, intent, !isAtFront(), isMultiProcess());
            } catch (Exception unused) {
            }
        }
    }

    public void notifyStabilityServiceToProtectPT() {
        if (!OsUtil.isAtLeastO()) {
            Intent intent = new Intent();
            intent.setClassName(getPackageName(), StabilityService.class.getName());
            intent.setAction(StabilityService.ACTION_PROTECT_PT);
            try {
                CompatUtils.startService(this, intent, !isAtFront(), isMultiProcess());
            } catch (Exception unused) {
            }
        }
    }

    private void initConfServiceUrl() {
        AppContext appContext = new AppContext(AppContext.PREFER_NAME_CHAT);
        String queryWithKey = appContext.queryWithKey(ConfigReader.KEY_WEBSERVER, AppContext.APP_NAME_CHAT);
        if (queryWithKey != null && queryWithKey.indexOf(ZMDomainUtil.ZM_URL_WEB_SERVER_DOMAIN) > 0) {
            appContext.setKeyValue(ConfigReader.KEY_WEBSERVER, null, AppContext.APP_NAME_CHAT);
        }
    }

    public void runOnMainThread(@Nullable Runnable runnable) {
        if (runnable != null) {
            this.mHandler.post(runnable);
        }
    }

    public void runOnMainThreadDelayed(@Nullable Runnable runnable, long j) {
        if (runnable != null) {
            this.mHandler.postDelayed(runnable, j);
        }
    }

    public void removeCallbacks(@Nullable Runnable runnable) {
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
    }

    public void exit() {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            ZMActivity frontActivity = ZMActivity.getFrontActivity();
            if (frontActivity != null) {
                frontActivity.finish();
            }
        }
        stopConfService();
        ZMServiceHelper.stopService(this, getPackageName(), PTService.class.getName());
        ZMServiceHelper.stopService(this, getPackageName(), StabilityService.class.getName());
        killProcess(this, 0);
    }

    public void restart() {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            ZMActivity frontActivity = ZMActivity.getFrontActivity();
            if (frontActivity != null) {
                frontActivity.finish();
            }
        }
        stopConfService();
        killProcess(this, 0);
    }

    /* access modifiers changed from: private */
    public void onPTServiceConnected(IPTService iPTService) {
        this.mPTService = iPTService;
        if (isConfApp()) {
            ConfIPCPort.getInstance().sendBufferedMessages();
        }
        PTAppDelegation.getInstance().initDelegations();
    }

    /* access modifiers changed from: private */
    public void onPTServiceDisconnected() {
        this.mPTService = null;
        this.mPTServiceConnection = null;
        setPTProcessId(-1);
        if (isConfApp()) {
            ConfMgr.getInstance().leaveConference();
        }
    }

    /* access modifiers changed from: private */
    public void onConfServiceConnected(IConfService iConfService) {
        this.mConfService = iConfService;
        if (isPTApp()) {
            PTIPCPort.getInstance().sendBufferedMessages();
        }
        notifyConfProcessStarted();
    }

    /* access modifiers changed from: private */
    public void onConfServiceDisconnected() {
        this.mConfService = null;
        setConfProcessId(-1);
        setConfProcessReadyFlag(false);
        NotificationMgr.removeConfNotification(this);
        keepPartialWake(false);
        notifyConfProcessStopped();
    }

    private void notifyConfProcessStarted() {
        if (isConfProcessReady()) {
            IListener[] all = this.mConfProcessListenerList.getAll();
            if (all != null && all.length > 0) {
                for (IListener iListener : all) {
                    ((IConfProcessListener) iListener).onConfProcessStarted();
                }
            }
        } else if (this.mConfService != null) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    VideoBoxApplication.this.notifyConfProcessStopped();
                }
            }, 10);
        }
    }

    public void notifyConfProcessStopped() {
        if (!isConfProcessRunning()) {
            IListener[] all = this.mConfProcessListenerList.getAll();
            if (all != null && all.length > 0) {
                for (IListener iListener : all) {
                    ((IConfProcessListener) iListener).onConfProcessStopped();
                }
            }
        } else if (this.mConfService == null) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    VideoBoxApplication.this.notifyConfProcessStopped();
                }
            }, 10);
        }
    }

    private boolean isConfProcessLegal() {
        File filesDir = getFilesDir();
        if (filesDir == null) {
            return true;
        }
        String absolutePath = filesDir.getAbsolutePath();
        if (!absolutePath.endsWith("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append(absolutePath);
            sb.append("/");
            absolutePath = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(absolutePath);
        sb2.append("conf_process_legal");
        return new File(sb2.toString()).exists();
    }

    private void setConfProcessLegal(boolean z) {
        File filesDir = getFilesDir();
        if (filesDir != null) {
            filesDir.mkdir();
            if (filesDir.exists() && filesDir.isDirectory()) {
                String absolutePath = filesDir.getAbsolutePath();
                if (!absolutePath.endsWith("/")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(absolutePath);
                    sb.append("/");
                    absolutePath = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(absolutePath);
                sb2.append("conf_process_legal");
                File file = new File(sb2.toString());
                if (z) {
                    try {
                        file.createNewFile();
                    } catch (IOException unused) {
                    }
                } else {
                    file.delete();
                }
            }
        }
    }

    private void initLogTag() {
        StringBuilder sb = new StringBuilder();
        sb.append(VideoBoxApplication.class.getSimpleName());
        sb.append("[");
        TAG = sb.toString();
        if (isPTApp()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(TAG);
            sb2.append("PT");
            TAG = sb2.toString();
        } else if (isConfApp()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(TAG);
            sb3.append("Conf");
            TAG = sb3.toString();
        } else if (isStbProcess()) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(TAG);
            sb4.append("STB");
            TAG = sb4.toString();
        } else {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(TAG);
            sb5.append("Unknown");
            TAG = sb5.toString();
        }
        StringBuilder sb6 = new StringBuilder();
        sb6.append(TAG);
        sb6.append("]");
        TAG = sb6.toString();
    }

    /* access modifiers changed from: private */
    public void startPTMessageLoop() {
        this.mHandler.postDelayed(this.mPTMessageLoopRunnable, 50);
    }

    /* access modifiers changed from: private */
    public void startConfMessageLoop(long j) {
        this.mHandler.postDelayed(this.mConfMessageLoopRunnable, j);
    }

    private void stopMessageLoop() {
        this.mbMessageLoopStopped = true;
    }

    private static int getProcessType(@NonNull Context context) {
        String packageName = context.getPackageName();
        String currentProcessName = getCurrentProcessName(context);
        if (!packageName.equals(currentProcessName)) {
            StringBuilder sb = new StringBuilder();
            sb.append(packageName);
            sb.append(":");
            sb.append("");
            if (!sb.toString().equals(currentProcessName)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(packageName);
                sb2.append(":");
                sb2.append(CONF_PROCESS_EXT_NAME);
                if (sb2.toString().equals(currentProcessName)) {
                    return 1;
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append(packageName);
                sb3.append(":");
                sb3.append(STD_PROCESS_EXT_NAME);
                if (sb3.toString().equals(currentProcessName)) {
                    return 2;
                }
                StringBuilder sb4 = new StringBuilder();
                sb4.append(packageName);
                sb4.append(":");
                sb4.append(SIP_PROCESS_EXT_NAME);
                if (sb4.toString().equals(currentProcessName)) {
                    return 3;
                }
                return 0;
            }
        }
        return 0;
    }

    @NonNull
    private static String getCurrentProcessName(@NonNull Context context) {
        String str = "";
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager != null) {
            try {
                List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                if (runningAppProcesses != null && runningAppProcesses.size() > 0) {
                    int myPid = Process.myPid();
                    for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                        if (runningAppProcessInfo.pid == myPid) {
                            return runningAppProcessInfo.processName;
                        }
                    }
                }
            } catch (Exception unused) {
            }
        }
        if (StringUtil.isEmptyOrNull(str)) {
            if (OsUtil.isAtLeastP()) {
                str = Application.getProcessName();
            } else {
                try {
                    str = (String) Class.forName("android.app.ActivityThread").getDeclaredMethod("currentProcessName", new Class[0]).invoke(null, new Object[0]);
                } catch (Exception unused2) {
                }
            }
        }
        return StringUtil.safeString(str);
    }

    public boolean isPTApp() {
        return this.mProcessType == 0;
    }

    public boolean isConfApp() {
        return this.mProcessType == 1;
    }

    public boolean isStbProcess() {
        return this.mProcessType == 2;
    }

    public void onApplicationTerminated() {
        stopMessageLoop();
        stopMemMonitor();
    }

    public int startConfService(@NonNull Bundle bundle) {
        if (isConfApp()) {
            ZMServiceHelper.doServiceAction(null, ConfService.ARGS_EXTRA, bundle, ConfService.class);
            return 0;
        }
        ServiceConnection serviceConnection = this.mConfServiceConnection;
        if (serviceConnection != null) {
            try {
                unbindService(serviceConnection);
            } catch (Exception unused) {
            }
            this.mConfServiceConnection = null;
            this.mConfService = null;
            setConfProcessId(-1);
            setConfProcessReadyFlag(false);
        }
        checkNeedWaitToStopConfService();
        ZMServiceHelper.stopService(this, getPackageName(), ConfService.class.getName());
        setConfProcessLegal(true);
        long currentTimeMillis = System.currentTimeMillis();
        if (OsUtil.isAtLeastO() && !isAtFront()) {
            this.mStartForegroundServiceTime = currentTimeMillis;
        }
        ZMServiceHelper.doServiceAction(null, ConfService.ARGS_EXTRA, bundle, ConfService.class);
        connectConfService();
        int i = 0;
        while (!isConfProcessRunning() && i < 200) {
            try {
                Thread.sleep(20);
                i++;
            } catch (InterruptedException unused2) {
                return 1;
            }
        }
        int currentTimeMillis2 = (int) ((4500 - (System.currentTimeMillis() - currentTimeMillis)) / 20);
        int i2 = 0;
        while (!isConfProcessReady() && i2 < currentTimeMillis2) {
            try {
                Thread.sleep(20);
                i2++;
            } catch (InterruptedException unused3) {
                return 1;
            }
        }
        if (!isConfProcessRunning()) {
            return 2;
        }
        if (!isConfProcessReady()) {
            return 3;
        }
        keepPartialWake(true);
        return 0;
    }

    public boolean hasPTProcess() {
        return getPidByProcessType(0) > 0;
    }

    public boolean isProcessRunning(int i) {
        if (i < 0) {
            return false;
        }
        ActivityManager activityManager = (ActivityManager) getSystemService("activity");
        if (activityManager != null) {
            for (RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
                if (runningAppProcessInfo.pid == i) {
                    return true;
                }
            }
        }
        return false;
    }

    public void stopConfService() {
        NotificationMgr.removeConfNotification(this);
        PTIPCPort.getInstance().setNativeHandle(0);
        clearConfAppContext();
        ServiceConnection serviceConnection = this.mConfServiceConnection;
        if (serviceConnection != null) {
            try {
                unbindService(serviceConnection);
            } catch (Exception unused) {
            }
            this.mConfServiceConnection = null;
            this.mConfService = null;
            setConfProcessReadyFlag(false);
        }
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null && mainboard.isInitialized()) {
            mainboard.notifyConfProcessExitCorrectly();
        }
        checkNeedWaitToStopConfService();
        ZMServiceHelper.stopService(this, getPackageName(), ConfService.class.getName());
        killConfProcess();
    }

    private void checkNeedWaitToStopConfService() {
        if (OsUtil.isAtLeastO() && !isConfApp() && System.currentTimeMillis() - this.mStartForegroundServiceTime < 5000) {
            try {
                Thread.sleep(60);
            } catch (InterruptedException unused) {
            }
        }
    }

    public void clearConfAppContext() {
        setConfProcessReadyFlag(false);
    }

    public void killConfProcess() {
        if (isConfApp()) {
            setConfProcessId(-1);
            IPCHelper.getInstance().notifyLeaveAndPerformAction(LeaveConfAction.BEFORE_CONF_KILL_HIMSELF_PROCESS.ordinal(), 0);
            Runtime.getRuntime().exit(0);
            return;
        }
        killProcess(this, 1);
        while (isConfProcessRunning()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException unused) {
            }
        }
    }

    public void killCurrentProcess() {
        if (isPTApp()) {
            if (!PTApp.getInstance().isPhoneNumberRegistered()) {
                NotificationMgr.removeMessageNotificationMM(this);
            }
            setPTProcessId(-1);
        } else if (isConfApp()) {
            NotificationMgr.removeConfNotification(this);
            setConfProcessId(-1);
        }
        Process.killProcess(Process.myPid());
    }

    private void connectConfService() {
        if (this.mConfService == null) {
            if (this.mConfServiceConnection == null) {
                this.mConfServiceConnection = new ServiceConnection() {
                    public void onServiceConnected(ComponentName componentName, @NonNull IBinder iBinder) {
                        VideoBoxApplication.this.onConfServiceConnected(Stub.asInterface(iBinder));
                        try {
                            iBinder.linkToDeath(new ConfProcessDeathHandler(iBinder), 0);
                            VideoBoxApplication.this.mIsConfProcessDeathLinked = true;
                        } catch (RemoteException unused) {
                        }
                    }

                    public void onServiceDisconnected(ComponentName componentName) {
                        VideoBoxApplication.this.onConfServiceDisconnected();
                    }
                };
            }
            Intent intent = new Intent();
            intent.setClassName(getPackageName(), ConfService.class.getName());
            bindService(intent, this.mConfServiceConnection, 64);
        }
    }

    @Nullable
    public IConfService getConfService() {
        return this.mConfService;
    }

    private void connectPTService() {
        if (this.mPTService == null) {
            if (this.mPTServiceConnection == null) {
                this.mPTServiceConnection = new ServiceConnection() {
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        VideoBoxApplication.this.onPTServiceConnected(IPTService.Stub.asInterface(iBinder));
                    }

                    public void onServiceDisconnected(ComponentName componentName) {
                        VideoBoxApplication.this.onPTServiceDisconnected();
                    }
                };
            }
            Intent intent = new Intent();
            intent.setClassName(getPackageName(), PTService.class.getName());
            bindService(intent, this.mPTServiceConnection, 64);
        }
    }

    @Nullable
    public IPTService getPTService() {
        return this.mPTService;
    }

    public boolean isAtFront() {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if (frontActivity != null && frontActivity.isActive()) {
            return true;
        }
        if (isConfApp()) {
            IPTService pTService = getPTService();
            if (pTService != null) {
                try {
                    if (pTService.isPTAppAtFront()) {
                        return true;
                    }
                } catch (RemoteException unused) {
                }
            }
        } else {
            IConfService confService = getConfService();
            if (confService != null) {
                if (confService.isConfAppAtFront()) {
                    return true;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void printMemoryCPU() {
        if (Logger.getInstance().isEnabled() && Logger.getInstance().getLevel() <= 1) {
            getMemoryCPUStatistics();
        }
    }

    @NonNull
    public static String getMemoryCPUStatistics() {
        VideoBoxApplication instance = getInstance();
        if (instance == null) {
            return "";
        }
        ActivityManager activityManager = (ActivityManager) instance.getSystemService("activity");
        if (activityManager == null) {
            return "";
        }
        MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{Process.myPid()});
        if (processMemoryInfo == null || processMemoryInfo.length != 1) {
            return "";
        }
        MemoryInfo memoryInfo = processMemoryInfo[0];
        if (memoryInfo == null) {
            return "";
        }
        ActivityManager.MemoryInfo memoryInfo2 = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo2);
        long freeMemory = Runtime.getRuntime().freeMemory();
        long j = Runtime.getRuntime().totalMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        long j2 = j - freeMemory;
        long nativeHeapFreeSize = Debug.getNativeHeapFreeSize();
        ActivityManager.MemoryInfo memoryInfo3 = memoryInfo2;
        long nativeHeapSize = Debug.getNativeHeapSize();
        MemoryInfo memoryInfo4 = memoryInfo;
        long nativeHeapAllocatedSize = Debug.getNativeHeapAllocatedSize();
        int cPUKernelFrequency = HardwareUtil.getCPUKernelFrequency(0, 0);
        ActivityManager.MemoryInfo memoryInfo5 = memoryInfo3;
        MemoryInfo memoryInfo6 = memoryInfo4;
        ActivityManager.MemoryInfo memoryInfo7 = memoryInfo5;
        return String.format(Locale.US, "Mem: PSS=%d, SharedDirty=%d, PrivateDirty=%d (Dalvik:[%d, %d, %d]; Native:[%d, %d, %d]; Other:[%d, %d, %d])\nHeap: dalvik[Max=%.2fM, Free=%.2fM, Heap=%.2fM, Allocated=%.2fM], native[Free=%.2fM, Heap=%.2fM, Allocated=%.2fM]\nActMem: availMem=%d, lowMemory=%b, threshold=%d\nCPU Freq: %d", new Object[]{Integer.valueOf(memoryInfo4.getTotalPss()), Integer.valueOf(memoryInfo4.getTotalSharedDirty()), Integer.valueOf(memoryInfo4.getTotalPrivateDirty()), Integer.valueOf(memoryInfo6.dalvikPss), Integer.valueOf(memoryInfo6.dalvikSharedDirty), Integer.valueOf(memoryInfo6.dalvikPrivateDirty), Integer.valueOf(memoryInfo6.nativePss), Integer.valueOf(memoryInfo6.nativeSharedDirty), Integer.valueOf(memoryInfo6.nativePrivateDirty), Integer.valueOf(memoryInfo6.otherPss), Integer.valueOf(memoryInfo6.otherSharedDirty), Integer.valueOf(memoryInfo6.otherPrivateDirty), Float.valueOf((((float) maxMemory) / 1024.0f) / 1024.0f), Float.valueOf((((float) freeMemory) / 1024.0f) / 1024.0f), Float.valueOf((((float) j) / 1024.0f) / 1024.0f), Float.valueOf((((float) j2) / 1024.0f) / 1024.0f), Float.valueOf((((float) nativeHeapFreeSize) / 1024.0f) / 1024.0f), Float.valueOf((((float) nativeHeapSize) / 1024.0f) / 1024.0f), Float.valueOf((((float) nativeHeapAllocatedSize) / 1024.0f) / 1024.0f), Long.valueOf(memoryInfo7.availMem), Boolean.valueOf(memoryInfo7.lowMemory), Long.valueOf(memoryInfo7.threshold), Integer.valueOf(cPUKernelFrequency)});
    }

    private void startMemMonitor() {
        try {
            this.mMemMonTimer = new Timer();
            this.mMemMonTimer.schedule(new TimerTask() {
                public void run() {
                    VideoBoxApplication.this.printMemoryCPU();
                }
            }, 0, 10000);
        } catch (Exception unused) {
        }
    }

    private void stopMemMonitor() {
        Timer timer = this.mMemMonTimer;
        if (timer != null) {
            timer.cancel();
        }
    }

    private int getPidByProcessType(int i) {
        if (i == 0) {
            return getPTProcessId();
        }
        if (i == 1) {
            return getConfProcessId();
        }
        if (i == 3) {
            return getSipProcessId();
        }
        return -1;
    }

    public void killProcess(Context context, int i) {
        int pidByProcessType = getPidByProcessType(i);
        if (pidByProcessType > 0) {
            Process.killProcess(pidByProcessType);
            if (i == 0) {
                setPTProcessId(-1);
            } else if (i == 1) {
                setConfProcessId(-1);
            } else if (i == 3) {
                setSipProcessId(-1);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x0118 A[SYNTHETIC, Splitter:B:45:0x0118] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0186 A[Catch:{ Exception -> 0x01be, all -> 0x01ba }] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01af A[SYNTHETIC, Splitter:B:65:0x01af] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01c6 A[SYNTHETIC, Splitter:B:81:0x01c6] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x01cd A[SYNTHETIC, Splitter:B:85:0x01cd] */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x01d4 A[SYNTHETIC, Splitter:B:92:0x01d4] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x01db A[SYNTHETIC, Splitter:B:96:0x01db] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void prepareNativeCrash() {
        /*
            r12 = this;
            java.lang.String r0 = com.zipow.videobox.util.LogUtil.getLogFolder()
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x000b
            return
        L_0x000b:
            java.io.File r1 = new java.io.File
            r1.<init>(r0)
            boolean r0 = r1.isDirectory()
            if (r0 != 0) goto L_0x0017
            return
        L_0x0017:
            com.zipow.videobox.VideoBoxApplication$13 r0 = new com.zipow.videobox.VideoBoxApplication$13
            r0.<init>()
            java.io.File[] r0 = r1.listFiles(r0)
            com.zipow.videobox.VideoBoxApplication$14 r2 = new com.zipow.videobox.VideoBoxApplication$14
            r2.<init>()
            java.io.File[] r2 = r1.listFiles(r2)
            r3 = 8192(0x2000, float:1.14794E-41)
            r4 = 0
            byte[] r3 = new byte[r3]     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            boolean r5 = com.zipow.videobox.util.ZMUtils.isItuneApp(r12)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            if (r5 == 0) goto L_0x0037
            java.lang.String r5 = "crash-intune-breakpad-"
            goto L_0x0039
        L_0x0037:
            java.lang.String r5 = "crash-native-breakpad-"
        L_0x0039:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r6.<init>()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r6.append(r5)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r5 = com.zipow.videobox.util.LogUtil.getDeviceInfo()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r6.append(r5)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r5 = r6.toString()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r6 = 0
            if (r0 == 0) goto L_0x00ce
            int r7 = r0.length     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            if (r7 != 0) goto L_0x0054
            goto L_0x00ce
        L_0x0054:
            r7 = r0[r6]     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r8 = r7.getName()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r9 = "-"
            java.lang.String[] r8 = r8.split(r9)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            int r9 = r8.length     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r10 = 2
            if (r9 == r10) goto L_0x0065
            return
        L_0x0065:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r9.<init>()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r9.append(r5)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r5 = r8[r6]     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r9.append(r5)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r5 = ".dump"
            r9.append(r5)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r5 = r9.toString()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.io.File r9 = new java.io.File     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r10.<init>()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r10.append(r5)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r11 = ".gz"
            r10.append(r11)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r9.<init>(r1, r10)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            boolean r1 = r9.exists()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            if (r1 != 0) goto L_0x009a
            r9.createNewFile()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
        L_0x009a:
            java.util.zip.ZipOutputStream r1 = new java.util.zip.ZipOutputStream     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.io.FileOutputStream r10 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r10.<init>(r9)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r1.<init>(r10)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.util.zip.ZipEntry r9 = new java.util.zip.ZipEntry     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r10 = 1
            r8 = r8[r10]     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r9.<init>(r8)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r1.putNextEntry(r9)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.io.FileInputStream r8 = new java.io.FileInputStream     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r8.<init>(r7)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
        L_0x00b4:
            int r7 = r8.read(r3)     // Catch:{ Exception -> 0x00ca, all -> 0x00c5 }
            if (r7 <= 0) goto L_0x00be
            r1.write(r3, r6, r7)     // Catch:{ Exception -> 0x00ca, all -> 0x00c5 }
            goto L_0x00b4
        L_0x00be:
            r1.closeEntry()     // Catch:{ Exception -> 0x00ca, all -> 0x00c5 }
            r8.close()     // Catch:{ Exception -> 0x00ca, all -> 0x00c5 }
            goto L_0x0116
        L_0x00c5:
            r0 = move-exception
            r4 = r1
            r5 = r8
            goto L_0x01c4
        L_0x00ca:
            r4 = r1
            r5 = r8
            goto L_0x01d2
        L_0x00ce:
            if (r2 == 0) goto L_0x01c1
            int r7 = r2.length     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            if (r7 != 0) goto L_0x00d5
            goto L_0x01c1
        L_0x00d5:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r7.<init>()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r7.append(r5)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r7.append(r8)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r5 = ".dump"
            r7.append(r5)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r5 = r7.toString()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r8.<init>()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r8.append(r5)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r9 = ".gz"
            r8.append(r9)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r7.<init>(r1, r8)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            boolean r1 = r7.exists()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            if (r1 != 0) goto L_0x010c
            r7.createNewFile()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
        L_0x010c:
            java.util.zip.ZipOutputStream r1 = new java.util.zip.ZipOutputStream     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            java.io.FileOutputStream r8 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r8.<init>(r7)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r1.<init>(r8)     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
        L_0x0116:
            if (r2 == 0) goto L_0x0148
            int r7 = r2.length     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            if (r7 <= 0) goto L_0x0148
            java.util.zip.ZipEntry r7 = new java.util.zip.ZipEntry     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r7.<init>(r5)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r1.putNextEntry(r7)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r7 = r2[r6]     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r5.<init>(r7)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
        L_0x012a:
            int r7 = r5.read(r3)     // Catch:{ Exception -> 0x01bf, all -> 0x0146 }
            if (r7 <= 0) goto L_0x0134
            r1.write(r3, r6, r7)     // Catch:{ Exception -> 0x01bf, all -> 0x0146 }
            goto L_0x012a
        L_0x0134:
            r1.closeEntry()     // Catch:{ Exception -> 0x01bf, all -> 0x0146 }
            r5.close()     // Catch:{ Exception -> 0x01bf, all -> 0x0146 }
            int r3 = r2.length     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r5 = 0
        L_0x013c:
            if (r5 >= r3) goto L_0x0148
            r7 = r2[r5]     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r7.delete()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            int r5 = r5 + 1
            goto L_0x013c
        L_0x0146:
            r0 = move-exception
            goto L_0x01bc
        L_0x0148:
            java.util.zip.ZipEntry r2 = new java.util.zip.ZipEntry     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.lang.String r3 = "deviceInfo.txt"
            r2.<init>(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r1.putNextEntry(r2)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.lang.String r3 = "version: "
            r2.<init>(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            com.zipow.videobox.VideoBoxApplication r3 = getInstance()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.lang.String r3 = r3.getVersionName()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r2.append(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.lang.String r3 = "\nversion: "
            r2.append(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            com.zipow.videobox.VideoBoxApplication r3 = getInstance()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.lang.String r3 = r3.getVersionName()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r2.append(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.lang.String r3 = "\nOS: "
            r2.append(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.lang.String r3 = com.zipow.videobox.ptapp.SystemInfoHelper.getOSInfo()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r2.append(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            boolean r3 = com.zipow.cmmlib.AppContext.BAASecurity_IsEnabled()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            if (r3 != 0) goto L_0x0192
            java.lang.String r3 = "\nHardware: "
            r2.append(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.lang.String r3 = com.zipow.videobox.ptapp.SystemInfoHelper.getHardwareInfo()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r2.append(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
        L_0x0192:
            java.lang.String r3 = "\nIsRooted: "
            r2.append(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            boolean r3 = p021us.zoom.androidlib.util.RootCheckUtils.isRooted()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r2.append(r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            byte[] r2 = r2.getBytes()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            int r3 = r2.length     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r1.write(r2, r6, r3)     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            r1.close()     // Catch:{ Exception -> 0x01be, all -> 0x01ba }
            if (r0 == 0) goto L_0x01de
            int r1 = r0.length     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
        L_0x01b0:
            if (r6 >= r1) goto L_0x01de
            r2 = r0[r6]     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            r2.delete()     // Catch:{ Exception -> 0x01d1, all -> 0x01c2 }
            int r6 = r6 + 1
            goto L_0x01b0
        L_0x01ba:
            r0 = move-exception
            r5 = r4
        L_0x01bc:
            r4 = r1
            goto L_0x01c4
        L_0x01be:
            r5 = r4
        L_0x01bf:
            r4 = r1
            goto L_0x01d2
        L_0x01c1:
            return
        L_0x01c2:
            r0 = move-exception
            r5 = r4
        L_0x01c4:
            if (r4 == 0) goto L_0x01cb
            r4.close()     // Catch:{ IOException -> 0x01ca }
            goto L_0x01cb
        L_0x01ca:
        L_0x01cb:
            if (r5 == 0) goto L_0x01d0
            r5.close()     // Catch:{ IOException -> 0x01d0 }
        L_0x01d0:
            throw r0
        L_0x01d1:
            r5 = r4
        L_0x01d2:
            if (r4 == 0) goto L_0x01d9
            r4.close()     // Catch:{ IOException -> 0x01d8 }
            goto L_0x01d9
        L_0x01d8:
        L_0x01d9:
            if (r5 == 0) goto L_0x01de
            r5.close()     // Catch:{ IOException -> 0x01de }
        L_0x01de:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.VideoBoxApplication.prepareNativeCrash():void");
    }

    private void removeTempFiles() {
        if (!isConfProcessRunning()) {
            removeTmpFiles(new File(AppUtil.getDataPath()));
        }
    }

    private void removeTmpFiles(File file) {
        if (file.exists()) {
            File[] listFiles = file.listFiles(new FileFilter() {
                public boolean accept(@NonNull File file) {
                    String name = file.getName();
                    return name.endsWith(".tmp") || name.startsWith("tmp-");
                }
            });
            if (listFiles != null) {
                for (File delete : listFiles) {
                    delete.delete();
                }
            }
        }
    }

    private void removeOldestPTLogs() {
        removeOldestLogs(Mainboard.PT_MAINBOARD_NAME, ".log", 15);
    }

    private void removeOldestConfLogs() {
        removeOldestLogs(Mainboard.CONF_MAINBOARD_NAME, ".log", 15);
    }

    private void removeOldestUtilLogs() {
        removeOldestLogs("util", ".log", 15);
    }

    private void removeOldestCrashLogs() {
        removeOldestLogs("crash-java-", ".log.sent", 0);
        removeOldestLogs("crash-native-", ".log.sent", 0);
        removeOldestLogs(LogUtil.CRASH_LOG_PREFIX, ".log", 4);
        removeOldestLogs(LogUtil.FREEZE_LOG_PREFIX, ".log", 4);
        removeOldestLogs(LogUtil.FREEZE_LOG_PREFIX, ".log.sent", 0);
        removeOldestLogs(LogUtil.CRASH_LOG_PREFIX, ".gz", 4);
        removeOldestLogs(LogUtil.CRASH_LOG_PREFIX, ".gz.sent", 0);
        removeOldestLogs("memlog_file_sent_", ".log.sent.zip", 4);
        removeOldestLogs("memlog_file_sent_", ".log.sent.zip.zenc", 4);
    }

    private void removeOldestASLogs() {
        removeOldestLogs("cptshare-", ".log", 15);
    }

    private void removeOldestLogs(@Nullable final String str, @Nullable final String str2, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(AppUtil.getLogParentPath());
        sb.append("/logs");
        File file = new File(sb.toString());
        if (file.exists()) {
            LogUtil.removeOldestLogFiles(i, file, new FileFilter() {
                public boolean accept(@NonNull File file) {
                    String name = file.getName();
                    String str = str;
                    if (str != null && name.startsWith(str)) {
                        String str2 = str2;
                        if (str2 != null && name.endsWith(str2)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void restrictCrashReportFrequency() {
        StringBuilder sb = new StringBuilder();
        sb.append(AppUtil.getLogParentPath());
        sb.append("/logs");
        File file = new File(sb.toString());
        if (file.exists()) {
            File[] listFiles = file.listFiles(new FileFilter() {
                public boolean accept(@Nullable File file) {
                    boolean z = false;
                    if (file == null || !file.isFile()) {
                        return false;
                    }
                    String name = file.getName();
                    long lastModified = file.lastModified();
                    if (name.startsWith(LogUtil.CRASH_LOG_PREFIX) && System.currentTimeMillis() - lastModified < 86400000) {
                        z = true;
                    }
                    return z;
                }
            });
            if (listFiles != null && listFiles.length > 3) {
                int i = 0;
                for (File file2 : listFiles) {
                    if (file2 != null && file2.getName().endsWith(".sent")) {
                        i++;
                    }
                }
                int length = listFiles.length - i;
                int i2 = 3 - i;
                if (i2 < 0) {
                    i2 = 0;
                }
                if (length > i2) {
                    for (File file3 : listFiles) {
                        if (file3 != null && !file3.getName().endsWith(".sent")) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(file3.getAbsolutePath());
                            sb2.append(".sent");
                            file3.renameTo(new File(sb2.toString()));
                            length--;
                            if (length <= i2) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void initLog() {
        initLogTag();
        Logger.getInstance();
        if (isPTApp()) {
            removeOldestCrashLogs();
            removeOldestPTLogs();
            removeOldestConfLogs();
            removeOldestUtilLogs();
            removeOldestASLogs();
            removeOldestSIPLogs();
            restrictCrashReportFrequency();
        }
    }

    private void installJavaCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new JavaCrashHandler(Thread.getDefaultUncaughtExceptionHandler()));
    }

    @NonNull
    public String getVersionName() {
        return getString(C4558R.string.zm_display_version);
    }

    @NonNull
    public String getKernelVersion() {
        return getString(C4558R.string.zm_version_name);
    }

    private void startDeadLockDetector() {
        if (!"yes".equals(new AppContext(AppContext.PREFER_NAME_CHAT).queryWithKey(ConfigReader.KEY_DISABLE_DEADLOCK_DETECT, AppContext.APP_NAME_CHAT)) && HardwareUtil.getCPUKernalNumbers() > 1) {
            HardwareUtil.getCPUKernelFrequency(0, 2);
        }
    }

    public void keepPartialWake(boolean z) {
        try {
            PowerManager powerManager = (PowerManager) getSystemService("power");
            if (powerManager != null) {
                if (this.mPartialWakeLock == null) {
                    if (z) {
                        this.mPartialWakeLock = powerManager.newWakeLock(1, getClass().getName());
                        if (this.mPartialWakeLock == null) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                if (!z) {
                    if (this.mPartialWakeLock.isHeld()) {
                        this.mPartialWakeLock.release();
                    }
                    this.mPartialWakeLock = null;
                } else if (!this.mPartialWakeLock.isHeld()) {
                    this.mPartialWakeLock.acquire();
                }
            }
        } catch (Exception unused) {
        }
    }

    private void checkAutoRecovery() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (ZMActivity.hasActivityCreated() || System.currentTimeMillis() - ZMFirebaseMessagingService.getLastNormalMsgTimeStamp() < 5000) {
                    return;
                }
                if (VideoBoxApplication.this.isC2DMUsed()) {
                    VideoBoxApplication.this.exit();
                } else {
                    AutoRecoveryUtil.getInstance().autoRecovery(VideoBoxApplication.this);
                }
            }
        }, 3000);
    }

    private void removeOldestSIPLogs() {
        removeOldestLogs(Mainboard.SIP_MAINBOARD_NAME, ".log", 15);
    }

    @SuppressLint({"InlinedApi"})
    @TargetApi(23)
    public void requestIgnoreBatteryOptimization() {
        if (OsUtil.isAtLeastM()) {
            PowerManager powerManager = (PowerManager) getSystemService("power");
            if (powerManager != null) {
                try {
                    Intent intent = new Intent();
                    if (powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
                        intent.setAction("android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS");
                    } else {
                        intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                        StringBuilder sb = new StringBuilder();
                        sb.append("package:");
                        sb.append(getPackageName());
                        intent.setData(Uri.parse(sb.toString()));
                    }
                    intent.addFlags(268435456);
                    ActivityStartHelper.startActivityForeground(this, intent);
                } catch (Exception unused) {
                }
            }
        }
    }
}

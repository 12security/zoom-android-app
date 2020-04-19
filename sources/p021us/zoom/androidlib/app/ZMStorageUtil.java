package p021us.zoom.androidlib.app;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import androidx.core.app.NotificationCompat;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.http.cookie.ClientCookie;
import p021us.zoom.androidlib.cache.IoUtils;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;

/* renamed from: us.zoom.androidlib.app.ZMStorageUtil */
public class ZMStorageUtil {
    private static final String TAG = "ZMStorageUtil";
    public static final int TYPE_INTERNAL_STORAGE = 1;
    public static final int TYPE_SDCARD = 2;
    public static final int TYPE_USB_STORAGE = 3;
    /* access modifiers changed from: private */
    public static ArrayList<ZMStorageItem> sCachedStorages = new ArrayList<>();
    /* access modifiers changed from: private */
    public static Handler sHandler = new Handler();
    private static ListenerList sListeners = new ListenerList();
    /* access modifiers changed from: private */
    public static AsyncLoadAllStorage sLoader = null;
    /* access modifiers changed from: private */
    public static Runnable sTimeOutRunnable = new Runnable() {
        public void run() {
            ZMStorageUtil.notifyListeners(ZMStorageUtil.sCachedStorages);
        }
    };

    /* renamed from: us.zoom.androidlib.app.ZMStorageUtil$AsyncLoadAllStorage */
    private static class AsyncLoadAllStorage extends ZMAsyncTask<Void, Void, List<ZMStorageItem>> {
        private AsyncLoadAllStorage() {
        }

        /* access modifiers changed from: protected */
        public List<ZMStorageItem> doInBackground(Void... voidArr) {
            return getAllStorageInfo();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<ZMStorageItem> list) {
            ZMStorageUtil.sCachedStorages.clear();
            ZMStorageUtil.sCachedStorages.addAll(list);
            ZMStorageUtil.notifyListeners(list);
            ZMStorageUtil.sHandler.removeCallbacks(ZMStorageUtil.sTimeOutRunnable);
            ZMStorageUtil.sLoader = null;
        }

        private List<ZMStorageItem> getAllStorageInfo() {
            ArrayList arrayList = new ArrayList();
            List<String> allExternalStoragePaths = getAllExternalStoragePaths();
            ZMStorageItem zMStorageItem = new ZMStorageItem();
            zMStorageItem.path = ZMStorageUtil.getInternalStoragePath();
            zMStorageItem.type = 1;
            zMStorageItem.mounted = ZMStorageUtil.isInternalStorageMounted();
            zMStorageItem.availableSize = ZMStorageUtil.getAvailableMemSize(zMStorageItem.path);
            arrayList.add(zMStorageItem);
            for (String str : allExternalStoragePaths) {
                boolean access$500 = ZMStorageUtil.checkFsWritable(str);
                ZMStorageItem zMStorageItem2 = new ZMStorageItem();
                if (isUsbStorage(str.toLowerCase(Locale.US))) {
                    zMStorageItem2.type = 3;
                } else {
                    zMStorageItem2.type = 2;
                }
                zMStorageItem2.path = str;
                zMStorageItem2.mounted = access$500;
                zMStorageItem2.availableSize = ZMStorageUtil.getAvailableMemSize(str);
                arrayList.add(zMStorageItem2);
            }
            return arrayList;
        }

        private boolean isUsbStorage(String str) {
            if (!StringUtil.isEmptyOrNull(str) && str.toLowerCase(Locale.US).contains("usb")) {
                return true;
            }
            return false;
        }

        private List<String> getAllExternalStoragePaths() {
            Process process;
            ArrayList arrayList = new ArrayList();
            try {
                process = Runtime.getRuntime().exec("mount");
            } catch (IOException unused) {
                process = null;
            }
            if (process == null) {
                return arrayList;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    } else if (!readLine.contains(ClientCookie.SECURE_ATTR) && !readLine.contains("asec") && !readLine.contains("media") && !readLine.contains("system") && !readLine.contains("cache") && !readLine.contains(NotificationCompat.CATEGORY_SYSTEM) && !readLine.contains("data") && !readLine.contains("tmpfs") && !readLine.contains("shell") && !readLine.contains("root") && !readLine.contains("acct") && !readLine.contains("proc") && !readLine.contains("misc")) {
                        if (!readLine.contains("obb")) {
                            if (readLine.contains("fat") || readLine.contains("fuse") || readLine.contains("ntfs")) {
                                String[] split = readLine.split(OAuth.SCOPE_DELIMITER);
                                if (split.length > 1) {
                                    if (!StringUtil.isEmptyOrNull(split[1])) {
                                        String replace = split[1].replace("..", "");
                                        if (!arrayList.contains(replace) && (replace.toLowerCase(Locale.US).contains("sd") || isUsbStorage(replace))) {
                                            arrayList.add(split[1]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception unused2) {
                } catch (Throwable th) {
                    IoUtils.closeSilently(bufferedReader);
                    throw th;
                }
            }
            IoUtils.closeSilently(bufferedReader);
            arrayList.remove(ZMStorageUtil.getInternalStoragePath());
            return arrayList;
        }
    }

    /* renamed from: us.zoom.androidlib.app.ZMStorageUtil$ZMStorageItem */
    public static class ZMStorageItem {
        long availableSize;
        public boolean mounted = false;
        public String path;
        public int type = 0;
    }

    /* renamed from: us.zoom.androidlib.app.ZMStorageUtil$ZMStorageListener */
    public interface ZMStorageListener extends IListener {
        void onRecieveStorageInfo(List<ZMStorageItem> list);
    }

    private static void addListener(ZMStorageListener zMStorageListener) {
        sListeners.add(zMStorageListener);
    }

    public static void removeListener(ZMStorageListener zMStorageListener) {
        sListeners.remove(zMStorageListener);
    }

    public static boolean isInternalStorageMounted() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getInternalStoragePath() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory == null) {
            return null;
        }
        return externalStorageDirectory.getPath();
    }

    /* access modifiers changed from: private */
    public static boolean checkFsWritable(String str) {
        if (str == null) {
            return false;
        }
        if (!str.endsWith(File.separator)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(File.separator);
            str = sb.toString();
        }
        File file = new File(str);
        if (!file.isDirectory() && !file.mkdirs()) {
            return false;
        }
        File file2 = new File(file, ".zoomflajfalsfka");
        try {
            if (file2.exists()) {
                file2.delete();
            }
            if (!file2.createNewFile()) {
                return false;
            }
            file2.delete();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static void asyncGetAllStorages(ZMStorageListener zMStorageListener, long j) {
        addListener(zMStorageListener);
        if (sLoader == null) {
            sLoader = new AsyncLoadAllStorage();
            sLoader.execute((Params[]) new Void[0]);
            sHandler.removeCallbacks(sTimeOutRunnable);
            sHandler.postDelayed(sTimeOutRunnable, j);
        }
    }

    /* access modifiers changed from: private */
    public static void notifyListeners(List<ZMStorageItem> list) {
        IListener[] all;
        for (IListener iListener : sListeners.getAll()) {
            if (iListener instanceof ZMStorageListener) {
                ((ZMStorageListener) iListener).onRecieveStorageInfo(list);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static long getAvailableMemSize(String str) {
        long j;
        if (StringUtil.isEmptyOrNull(str)) {
            return 0;
        }
        try {
            StatFs statFs = new StatFs(str);
            if (VERSION.SDK_INT < 18) {
                j = ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
            } else {
                j = statFs.getAvailableBytes();
            }
            return j;
        } catch (Exception unused) {
            return 0;
        }
    }
}

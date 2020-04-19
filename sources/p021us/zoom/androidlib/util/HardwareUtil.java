package p021us.zoom.androidlib.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/* renamed from: us.zoom.androidlib.util.HardwareUtil */
public class HardwareUtil {
    public static final int FLAG_CUR_FREQUENCY = 0;
    public static final int FLAG_MAX_FREQUENCY = 2;
    public static final int FLAG_MIN_FREQUENCY = 1;

    public static int getCPUKernalNumbers() {
        File file = new File("/sys/devices/system/cpu");
        try {
            if (!file.isDirectory()) {
                return 1;
            }
            String[] list = file.list(new FilenameFilter() {
                public boolean accept(File file, String str) {
                    return Pattern.compile("cpu(\\d+)").matcher(str).matches();
                }
            });
            if (list == null) {
                return 1;
            }
            return list.length;
        } catch (Exception unused) {
            return 1;
        }
    }

    public static int getCPUKernelFrequency(int i, int i2) {
        String str;
        FileReader fileReader;
        BufferedReader bufferedReader;
        Throwable th;
        Throwable th2;
        StringBuilder sb = new StringBuilder();
        sb.append("/sys/devices/system/cpu/cpu");
        sb.append(i);
        String sb2 = sb.toString();
        int i3 = 0;
        if (i2 == 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append("/cpufreq/scaling_cur_freq");
            str = sb3.toString();
        } else if (i2 == 1) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(sb2);
            sb4.append("/cpufreq/cpuinfo_min_freq");
            str = sb4.toString();
        } else if (i2 != 2) {
            return 0;
        } else {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(sb2);
            sb5.append("/cpufreq/cpuinfo_max_freq");
            str = sb5.toString();
        }
        try {
            fileReader = new FileReader(new File(str));
            bufferedReader = new BufferedReader(fileReader);
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    i3 = Integer.parseInt(readLine);
                }
                bufferedReader.close();
                fileReader.close();
            } catch (Throwable th3) {
                Throwable th4 = th3;
                th = r2;
                th2 = th4;
            }
        } catch (Exception unused) {
        } catch (Throwable th5) {
            r6.addSuppressed(th5);
        }
        return i3;
        throw th2;
        if (th != null) {
            try {
                bufferedReader.close();
            } catch (Throwable th6) {
                th.addSuppressed(th6);
            }
        } else {
            bufferedReader.close();
        }
        throw th2;
        throw th;
    }

    public static boolean isBluetoothLESupported(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return false;
        }
        if (VERSION.SDK_INT >= 18 && packageManager.hasSystemFeature("android.hardware.bluetooth_le")) {
            z = true;
        }
        return z;
    }

    public static String getPreferredCpuABI() {
        if (VERSION.SDK_INT < 21 || Build.SUPPORTED_ABIS == null || Build.SUPPORTED_ABIS.length <= 0) {
            return Build.CPU_ABI;
        }
        return Build.SUPPORTED_ABIS[0];
    }

    public static int getMaxCpuFreq() {
        int cPUKernalNumbers = getCPUKernalNumbers();
        if (cPUKernalNumbers <= 0) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < cPUKernalNumbers; i2++) {
            int cPUKernelFrequency = getCPUKernelFrequency(i2, 2);
            if (cPUKernelFrequency > i) {
                i = cPUKernelFrequency;
            }
        }
        return i;
    }

    public static DeviceModelRank getDeviceModelRank() {
        int maxCpuFreq = getMaxCpuFreq();
        if (maxCpuFreq == 0) {
            return DeviceModelRank.Medium;
        }
        if (maxCpuFreq >= 2600000) {
            return DeviceModelRank.High;
        }
        if (maxCpuFreq >= 2100000) {
            return DeviceModelRank.Medium;
        }
        return DeviceModelRank.Low;
    }
}

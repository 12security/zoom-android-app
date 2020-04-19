package com.zipow.nydus;

import androidx.annotation.NonNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import org.apache.http.cookie.ClientCookie;
import p021us.zoom.videomeetings.BuildConfig;

public class SysBusUsbManager {
    private static final String DEVICE_END = "__DEV_END__";
    private static final String DEVICE_START = "__DEV_START__";
    private static final String PATH_SYS_BUS_USB = "/sys/bus/usb/devices/";
    private HashMap<String, SysBusUsbDevice> myUsbDevices = new HashMap<>();

    public HashMap<String, SysBusUsbDevice> getUsbDevices() {
        populateList(PATH_SYS_BUS_USB);
        return this.myUsbDevices;
    }

    private void populateList(@NonNull String str) {
        this.myUsbDevices.clear();
        File file = new File(str);
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    if (!".".equals(file2.getName()) && !"..".equals(file2.getName())) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(file2.getAbsolutePath());
                        sb.append(File.separator);
                        String sb2 = sb.toString();
                        SysBusUsbDevice sysBusUsbDevice = new SysBusUsbDevice();
                        sysBusUsbDevice.setDevicePath(sb2);
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(sb2);
                        sb3.append("busnum");
                        sysBusUsbDevice.setBusNumber(readFileContents(sb3.toString()));
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(sb2);
                        sb4.append("bDeviceClass");
                        sysBusUsbDevice.setDeviceClass(readFileContents(sb4.toString()));
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(sb2);
                        sb5.append("devnum");
                        sysBusUsbDevice.setDeviceNumber(readFileContents(sb5.toString()));
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(sb2);
                        sb6.append("bDeviceProtocol");
                        sysBusUsbDevice.setDeviceProtocol(readFileContents(sb6.toString()));
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(sb2);
                        sb7.append("bDeviceSubClass");
                        sysBusUsbDevice.setDeviceSubClass(readFileContents(sb7.toString()));
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(sb2);
                        sb8.append("bMaxPower");
                        sysBusUsbDevice.setMaxPower(readFileContents(sb8.toString()));
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append(sb2);
                        sb9.append("idProduct");
                        sysBusUsbDevice.setPID(readFileContents(sb9.toString()));
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(sb2);
                        sb10.append(BuildConfig.FLAVOR);
                        sysBusUsbDevice.setReportedProductName(readFileContents(sb10.toString()));
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append(sb2);
                        sb11.append("manufacturer");
                        sysBusUsbDevice.setReportedVendorName(readFileContents(sb11.toString()));
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append(sb2);
                        sb12.append("serial");
                        sysBusUsbDevice.setSerialNumber(readFileContents(sb12.toString()));
                        StringBuilder sb13 = new StringBuilder();
                        sb13.append(sb2);
                        sb13.append("speed");
                        sysBusUsbDevice.setSpeed(readFileContents(sb13.toString()));
                        StringBuilder sb14 = new StringBuilder();
                        sb14.append(sb2);
                        sb14.append("idVendor");
                        sysBusUsbDevice.setVID(readFileContents(sb14.toString()));
                        StringBuilder sb15 = new StringBuilder();
                        sb15.append(sb2);
                        sb15.append(ClientCookie.VERSION_ATTR);
                        sysBusUsbDevice.setUsbVersion(readFileContents(sb15.toString()));
                        if (sysBusUsbDevice.getBusNumber().length() > 0 && sysBusUsbDevice.getDeviceNumber().length() > 0) {
                            this.myUsbDevices.put(file2.getName(), sysBusUsbDevice);
                        }
                    }
                }
            }
        }
    }

    private String readFileContents(@NonNull String str) {
        BufferedReader bufferedReader;
        File file = new File(str);
        if (!file.exists()) {
            return "";
        }
        if (file.isDirectory()) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(1000);
        try {
            bufferedReader = new BufferedReader(new FileReader(str));
            while (true) {
                char[] cArr = new char[1024];
                int read = bufferedReader.read(cArr);
                if (read != -1) {
                    stringBuffer.append(String.valueOf(cArr, 0, read));
                } else {
                    bufferedReader.close();
                    return stringBuffer.toString().trim();
                }
            }
        } catch (Exception unused) {
            return "";
        } catch (Throwable th) {
            r7.addSuppressed(th);
        }
        throw th;
    }
}

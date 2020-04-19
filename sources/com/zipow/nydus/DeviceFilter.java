package com.zipow.nydus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.hardware.usb.UsbDevice;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.BuildConfig;
import p021us.zoom.videomeetings.C4558R;

@SuppressLint({"NewApi"})
public final class DeviceFilter {
    private static final String DEFAULT_USBFS = "/dev/bus/usb";
    private static final String TAG = "DeviceFilter";
    private static final int USB_CLASS_VIDEO = 14;
    public final boolean mCheckVideoInterface;
    public final int mClass;
    @Nullable
    public final String mManufacturerName;
    public final int mProductId;
    @Nullable
    public final String mProductName;
    public final int mProtocol;
    @Nullable
    public final String mSerialNumber;
    public final int mSubclass;
    public final int mVendorId;

    private static final native int nativeCheckVideoInterface(int i, int i2, int i3, String str);

    static {
        System.loadLibrary("nydus");
    }

    public static DeviceFilter buildUVCDeviceFilter() {
        DeviceFilter deviceFilter = new DeviceFilter(-1, -1, DummyPolicyIDType.zPolicy_DisableSharingOverProxy, 2, -1, null, null, null, true);
        return deviceFilter;
    }

    public DeviceFilter(int i, int i2, int i3, int i4, int i5, @Nullable String str, @Nullable String str2, @Nullable String str3, boolean z) {
        this.mVendorId = i;
        this.mProductId = i2;
        this.mClass = i3;
        this.mSubclass = i4;
        this.mProtocol = i5;
        this.mManufacturerName = str;
        this.mProductName = str2;
        this.mSerialNumber = str3;
        this.mCheckVideoInterface = z;
    }

    public DeviceFilter(UsbDevice usbDevice) {
        this.mVendorId = usbDevice.getVendorId();
        this.mProductId = usbDevice.getProductId();
        this.mClass = usbDevice.getDeviceClass();
        this.mSubclass = usbDevice.getDeviceSubclass();
        this.mProtocol = usbDevice.getDeviceProtocol();
        this.mManufacturerName = null;
        this.mProductName = null;
        this.mSerialNumber = null;
        this.mCheckVideoInterface = false;
    }

    public static List<DeviceFilter> getDeviceFilters(Context context, int i) {
        XmlResourceParser xml = context.getResources().getXml(i);
        ArrayList arrayList = new ArrayList();
        try {
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                if (eventType == 2) {
                    DeviceFilter read = read(context, xml);
                    if (read != null) {
                        arrayList.add(read);
                    }
                }
            }
        } catch (XmlPullParserException e) {
            Log.d(TAG, "XmlPullParserException", e);
        } catch (IOException e2) {
            Log.d(TAG, "IOException", e2);
        }
        return Collections.unmodifiableList(arrayList);
    }

    private static final int getAttributeInteger(@NonNull Context context, @NonNull XmlPullParser xmlPullParser, String str, String str2, int i) {
        try {
            String attributeValue = xmlPullParser.getAttributeValue(str, str2);
            if (TextUtils.isEmpty(attributeValue) || !attributeValue.startsWith("@")) {
                return Integer.parseInt(attributeValue);
            }
            int identifier = context.getResources().getIdentifier(attributeValue.substring(1), null, context.getPackageName());
            if (identifier > 0) {
                return context.getResources().getInteger(identifier);
            }
            return i;
        } catch (NotFoundException | NullPointerException | NumberFormatException unused) {
            return i;
        }
    }

    private static final String getAttributeString(@NonNull Context context, @NonNull XmlPullParser xmlPullParser, String str, String str2, String str3) {
        try {
            String attributeValue = xmlPullParser.getAttributeValue(str, str2);
            if (attributeValue == null) {
                attributeValue = str3;
            }
            if (!TextUtils.isEmpty(attributeValue) && attributeValue.startsWith("@")) {
                int identifier = context.getResources().getIdentifier(attributeValue.substring(1), null, context.getPackageName());
                if (identifier > 0) {
                    return context.getResources().getString(identifier);
                }
            }
            return attributeValue;
        } catch (NotFoundException | NullPointerException | NumberFormatException unused) {
            return str3;
        }
    }

    public static DeviceFilter read(@NonNull Context context, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        Context context2 = context;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        int eventType = xmlPullParser.getEventType();
        boolean z = false;
        String str = null;
        String str2 = null;
        String str3 = null;
        int i = -1;
        int i2 = -1;
        int i3 = -1;
        int i4 = -1;
        int i5 = -1;
        while (eventType != 1) {
            String name = xmlPullParser.getName();
            if (!TextUtils.isEmpty(name) && name.equalsIgnoreCase("usb-device")) {
                if (eventType == 2) {
                    int attributeInteger = getAttributeInteger(context2, xmlPullParser2, null, "venderId", -1);
                    int attributeInteger2 = getAttributeInteger(context2, xmlPullParser2, null, "productId", -1);
                    int attributeInteger3 = getAttributeInteger(context2, xmlPullParser2, null, "class", -1);
                    int attributeInteger4 = getAttributeInteger(context2, xmlPullParser2, null, "subclass", -1);
                    int attributeInteger5 = getAttributeInteger(context2, xmlPullParser2, null, "protocol", -1);
                    String attributeString = getAttributeString(context2, xmlPullParser2, null, "manufacture", null);
                    str = attributeString;
                    str2 = getAttributeString(context2, xmlPullParser2, null, BuildConfig.FLAVOR, null);
                    str3 = getAttributeString(context2, xmlPullParser2, null, "serial", null);
                    i3 = attributeInteger3;
                    i4 = attributeInteger4;
                    i5 = attributeInteger5;
                    i = attributeInteger;
                    i2 = attributeInteger2;
                    z = true;
                } else if (eventType == 3 && z) {
                    DeviceFilter deviceFilter = new DeviceFilter(i, i2, i3, i4, i5, str, str2, str3, false);
                    return deviceFilter;
                }
            }
            eventType = xmlPullParser.next();
        }
        return null;
    }

    private boolean matches(int i, int i2, int i3) {
        int i4 = this.mClass;
        if (i4 == -1 || i == i4) {
            int i5 = this.mSubclass;
            if (i5 == -1 || i2 == i5) {
                int i6 = this.mProtocol;
                if (i6 == -1 || i3 == i6) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean matches(Context context, @NonNull UsbDevice usbDevice) {
        if (UIUtil.isTV(context) || ResourcesUtil.getBoolean(context, C4558R.bool.zm_config_no_uvc_camera, false)) {
            return false;
        }
        if (this.mVendorId != -1 && usbDevice.getVendorId() != this.mVendorId) {
            return false;
        }
        if ((this.mProductId != -1 && usbDevice.getProductId() != this.mProductId) || !matches(usbDevice.getDeviceClass(), usbDevice.getDeviceSubclass(), usbDevice.getDeviceProtocol())) {
            return false;
        }
        if (!this.mCheckVideoInterface) {
            return true;
        }
        if (nativeCheckVideoInterface(usbDevice.getVendorId(), usbDevice.getProductId(), 0, getUSBFSName(usbDevice)) == 1) {
            return true;
        }
        return false;
    }

    @NonNull
    private final String getUSBFSName(UsbDevice usbDevice) {
        String deviceName = usbDevice.getDeviceName();
        String str = null;
        String[] split = !TextUtils.isEmpty(deviceName) ? deviceName.split("/") : null;
        if (split != null && split.length > 2) {
            StringBuilder sb = new StringBuilder(split[0]);
            for (int i = 1; i < split.length - 2; i++) {
                sb.append("/");
                sb.append(split[i]);
            }
            str = sb.toString();
        }
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("failed to get USBFS path, try to use default path:");
        sb2.append(deviceName);
        Log.w(str2, sb2.toString());
        return DEFAULT_USBFS;
    }

    public boolean matches(@NonNull DeviceFilter deviceFilter) {
        int i = this.mVendorId;
        if (i != -1 && deviceFilter.mVendorId != i) {
            return false;
        }
        int i2 = this.mProductId;
        if (i2 != -1 && deviceFilter.mProductId != i2) {
            return false;
        }
        if (deviceFilter.mManufacturerName != null && this.mManufacturerName == null) {
            return false;
        }
        if (deviceFilter.mProductName != null && this.mProductName == null) {
            return false;
        }
        if (deviceFilter.mSerialNumber != null && this.mSerialNumber == null) {
            return false;
        }
        String str = this.mManufacturerName;
        if (str != null) {
            String str2 = deviceFilter.mManufacturerName;
            if (str2 != null && !str.equals(str2)) {
                return false;
            }
        }
        String str3 = this.mProductName;
        if (str3 != null) {
            String str4 = deviceFilter.mProductName;
            if (str4 != null && !str3.equals(str4)) {
                return false;
            }
        }
        String str5 = this.mSerialNumber;
        if (str5 != null) {
            String str6 = deviceFilter.mSerialNumber;
            if (str6 != null && !str5.equals(str6)) {
                return false;
            }
        }
        return matches(deviceFilter.mClass, deviceFilter.mSubclass, deviceFilter.mProtocol);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0071, code lost:
        if (r1.equals(r0) == false) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x007f, code lost:
        if (r1.equals(r0) == false) goto L_0x008f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r9) {
        /*
            r8 = this;
            int r0 = r8.mVendorId
            r1 = -1
            r2 = 0
            if (r0 == r1) goto L_0x00c4
            int r3 = r8.mProductId
            if (r3 == r1) goto L_0x00c4
            int r4 = r8.mClass
            if (r4 == r1) goto L_0x00c4
            int r5 = r8.mSubclass
            if (r5 == r1) goto L_0x00c4
            int r6 = r8.mProtocol
            if (r6 != r1) goto L_0x0018
            goto L_0x00c4
        L_0x0018:
            boolean r1 = r9 instanceof com.zipow.nydus.DeviceFilter
            r7 = 1
            if (r1 == 0) goto L_0x0092
            com.zipow.nydus.DeviceFilter r9 = (com.zipow.nydus.DeviceFilter) r9
            int r1 = r9.mVendorId
            if (r1 != r0) goto L_0x0091
            int r0 = r9.mProductId
            if (r0 != r3) goto L_0x0091
            int r0 = r9.mClass
            if (r0 != r4) goto L_0x0091
            int r0 = r9.mSubclass
            if (r0 != r5) goto L_0x0091
            int r0 = r9.mProtocol
            if (r0 == r6) goto L_0x0034
            goto L_0x0091
        L_0x0034:
            java.lang.String r0 = r9.mManufacturerName
            if (r0 == 0) goto L_0x003c
            java.lang.String r0 = r8.mManufacturerName
            if (r0 == 0) goto L_0x0064
        L_0x003c:
            java.lang.String r0 = r9.mManufacturerName
            if (r0 != 0) goto L_0x0044
            java.lang.String r0 = r8.mManufacturerName
            if (r0 != 0) goto L_0x0064
        L_0x0044:
            java.lang.String r0 = r9.mProductName
            if (r0 == 0) goto L_0x004c
            java.lang.String r0 = r8.mProductName
            if (r0 == 0) goto L_0x0064
        L_0x004c:
            java.lang.String r0 = r9.mProductName
            if (r0 != 0) goto L_0x0054
            java.lang.String r0 = r8.mProductName
            if (r0 != 0) goto L_0x0064
        L_0x0054:
            java.lang.String r0 = r9.mSerialNumber
            if (r0 == 0) goto L_0x005c
            java.lang.String r0 = r8.mSerialNumber
            if (r0 == 0) goto L_0x0064
        L_0x005c:
            java.lang.String r0 = r9.mSerialNumber
            if (r0 != 0) goto L_0x0065
            java.lang.String r0 = r8.mSerialNumber
            if (r0 == 0) goto L_0x0065
        L_0x0064:
            return r2
        L_0x0065:
            java.lang.String r0 = r9.mManufacturerName
            if (r0 == 0) goto L_0x0073
            java.lang.String r1 = r8.mManufacturerName
            if (r1 == 0) goto L_0x0073
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x008f
        L_0x0073:
            java.lang.String r0 = r9.mProductName
            if (r0 == 0) goto L_0x0081
            java.lang.String r1 = r8.mProductName
            if (r1 == 0) goto L_0x0081
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x008f
        L_0x0081:
            java.lang.String r9 = r9.mSerialNumber
            if (r9 == 0) goto L_0x0090
            java.lang.String r0 = r8.mSerialNumber
            if (r0 == 0) goto L_0x0090
            boolean r9 = r0.equals(r9)
            if (r9 != 0) goto L_0x0090
        L_0x008f:
            return r2
        L_0x0090:
            return r7
        L_0x0091:
            return r2
        L_0x0092:
            boolean r0 = r9 instanceof android.hardware.usb.UsbDevice
            if (r0 == 0) goto L_0x00c3
            android.hardware.usb.UsbDevice r9 = (android.hardware.usb.UsbDevice) r9
            int r0 = r9.getVendorId()
            int r1 = r8.mVendorId
            if (r0 != r1) goto L_0x00c2
            int r0 = r9.getProductId()
            int r1 = r8.mProductId
            if (r0 != r1) goto L_0x00c2
            int r0 = r9.getDeviceClass()
            int r1 = r8.mClass
            if (r0 != r1) goto L_0x00c2
            int r0 = r9.getDeviceSubclass()
            int r1 = r8.mSubclass
            if (r0 != r1) goto L_0x00c2
            int r9 = r9.getDeviceProtocol()
            int r0 = r8.mProtocol
            if (r9 == r0) goto L_0x00c1
            goto L_0x00c2
        L_0x00c1:
            return r7
        L_0x00c2:
            return r2
        L_0x00c3:
            return r2
        L_0x00c4:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.nydus.DeviceFilter.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return ((this.mVendorId << 16) | this.mProductId) ^ (((this.mClass << 16) | (this.mSubclass << 8)) | this.mProtocol);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceFilter[mVendorId=");
        sb.append(this.mVendorId);
        sb.append(",mProductId=");
        sb.append(this.mProductId);
        sb.append(",mClass=");
        sb.append(this.mClass);
        sb.append(",mSubclass=");
        sb.append(this.mSubclass);
        sb.append(",mProtocol=");
        sb.append(this.mProtocol);
        sb.append(",mManufacturerName=");
        sb.append(this.mManufacturerName);
        sb.append(",mProductName=");
        sb.append(this.mProductName);
        sb.append(",mSerialNumber=");
        sb.append(this.mSerialNumber);
        sb.append("]");
        return sb.toString();
    }
}

package com.zipow.videobox.sip;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import java.util.ArrayList;
import p021us.zoom.androidlib.util.DnsServersDetector;

public class DnsUtil {
    private static final int PROP_VALUE_MAX = 91;
    @Nullable
    private static DnsServersDetector mDnsServersDetector;

    public static String[] getDns() {
        if (mDnsServersDetector == null) {
            if (VideoBoxApplication.getGlobalContext() == null) {
                return null;
            }
            mDnsServersDetector = new DnsServersDetector(VideoBoxApplication.getGlobalContext());
        }
        String[] servers = mDnsServersDetector.getServers();
        ArrayList arrayList = new ArrayList(servers.length);
        if (servers != null) {
            for (int i = 0; i < servers.length; i++) {
                if (!TextUtils.isEmpty(servers[i]) && servers[i].length() < 91) {
                    arrayList.add(servers[i]);
                }
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }
}

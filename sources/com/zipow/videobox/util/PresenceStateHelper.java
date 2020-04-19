package com.zipow.videobox.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PresenceStateHelper {
    private static final String SUBJID_LONG = "0";
    private static final String TAG = "PresenceStateHelper";
    @Nullable
    private static volatile PresenceStateHelper singleton;
    /* access modifiers changed from: private */
    @NonNull
    public Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                PresenceStateHelper.this.mRealSubJids.clear();
                if (PresenceStateHelper.this.mSubJids.size() > 0) {
                    PresenceStateHelper.this.mRealSubJids.addAll(PresenceStateHelper.this.mSubJids);
                }
                if (PresenceStateHelper.this.mUnSubJids.size() > 0) {
                    if (PresenceStateHelper.this.mRealSubJids.size() > 0) {
                        PresenceStateHelper.this.mUnSubJids.removeAll(PresenceStateHelper.this.mRealSubJids);
                    }
                    if (PresenceStateHelper.this.mUnSubJids.size() > 0) {
                        zoomMessenger.TPV2_UnsubscribePresence(PresenceStateHelper.this.mUnSubJids);
                        PresenceStateHelper.this.mUnSubJids.clear();
                    }
                }
                if (PresenceStateHelper.this.mRealSubJids.size() > 0 && zoomMessenger.TPV2_SubscribePresence(PresenceStateHelper.this.mRealSubJids, 2) == 0) {
                    PresenceStateHelper.this.mSubJids.clear();
                }
                sendEmptyMessageDelayed(0, 500);
                return;
            }
            PresenceStateHelper.this.handler.sendEmptyMessageDelayed(0, 3000);
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public List<String> mRealSubJids = new ArrayList();
    /* access modifiers changed from: private */
    @NonNull
    public Set<String> mSubJids = new HashSet();
    /* access modifiers changed from: private */
    @NonNull
    public List<String> mUnSubJids = new ArrayList();

    @NonNull
    public static PresenceStateHelper getInstance() {
        if (singleton != null) {
            return singleton;
        }
        synchronized (PresenceStateHelper.class) {
            if (singleton == null) {
                singleton = new PresenceStateHelper();
            }
        }
        return singleton;
    }

    private PresenceStateHelper() {
        this.handler.sendEmptyMessage(0);
    }

    public void subscribe(@Nullable String str, String str2) {
        if (str != null && !str.startsWith("IMAddrBookItem")) {
            this.mSubJids.add(str);
        }
    }

    public void unSubscribe(@Nullable List list) {
        if (list != null && list.size() > 0) {
            this.mUnSubJids.addAll(list);
        }
    }
}

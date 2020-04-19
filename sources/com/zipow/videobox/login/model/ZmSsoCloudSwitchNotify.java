package com.zipow.videobox.login.model;

import java.util.ArrayList;
import java.util.List;

public class ZmSsoCloudSwitchNotify {
    private static String TAG = "com.zipow.videobox.login.model.ZmSsoCloudSwitchNotify";
    private static ZmSsoCloudSwitchNotify mInstance;
    private List<CloudSwitchNotifyListener> mCloudSwitchNotifyList = new ArrayList();

    public interface CloudSwitchNotifyListener {
        void updateCloudSwitch(int i);
    }

    public static ZmSsoCloudSwitchNotify getInstance() {
        if (mInstance == null) {
            mInstance = new ZmSsoCloudSwitchNotify();
        }
        return mInstance;
    }

    public void addCloudSwitchNotifyListener(CloudSwitchNotifyListener cloudSwitchNotifyListener) {
        if (!this.mCloudSwitchNotifyList.contains(cloudSwitchNotifyListener)) {
            this.mCloudSwitchNotifyList.add(cloudSwitchNotifyListener);
        }
    }

    public void removeCloudSwitchNotifyListener(CloudSwitchNotifyListener cloudSwitchNotifyListener) {
        if (this.mCloudSwitchNotifyList.size() > 0) {
            this.mCloudSwitchNotifyList.remove(cloudSwitchNotifyListener);
        }
    }

    public void onCloudSwitchChange(int i) {
        if (this.mCloudSwitchNotifyList.size() > 0) {
            for (CloudSwitchNotifyListener updateCloudSwitch : this.mCloudSwitchNotifyList) {
                updateCloudSwitch.updateCloudSwitch(i);
            }
        }
    }
}

package com.zipow.videobox.ptapp.delegate;

import android.os.RemoteException;
import com.zipow.videobox.IPTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.IMHelper;

public class IMHelperDelegation {
    private static final String TAG = "IMHelperDelegation";
    private boolean mIMSignedOn;
    private boolean mIMSignedOnAssigned = false;
    private IMHelper mImHelper;

    protected IMHelperDelegation() {
    }

    protected IMHelperDelegation(IMHelper iMHelper) {
        this.mImHelper = iMHelper;
    }

    public synchronized boolean isIMSignedOn() {
        if (this.mImHelper != null) {
            return this.mImHelper.isIMSignedOn();
        }
        if (!this.mIMSignedOnAssigned) {
            initIMSignedOn();
        }
        return this.mIMSignedOn;
    }

    /* access modifiers changed from: protected */
    public synchronized void initIMSignedOn() {
        IPTService pTService = VideoBoxApplication.getInstance().getPTService();
        if (pTService != null) {
            try {
                this.mIMSignedOn = pTService.isIMSignedIn();
                this.mIMSignedOnAssigned = true;
            } catch (RemoteException unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void setIMSignedOn(boolean z) {
        this.mIMSignedOn = z;
        this.mIMSignedOnAssigned = true;
    }
}

package com.zipow.videobox.ptapp.delegate;

import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IPTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;

public class PTAppDelegation {
    private static final String TAG = "PTAppDelegation";
    @Nullable
    private static PTAppDelegation instance;
    @Nullable
    private PTBuddyHelperDelegation buddyHelperDelegation;
    private ABContactsHelperDelegation contactsHelperDelegation;
    private FavoriteMgrDelegation favMgrDelegation;
    private IMHelperDelegation imHelperDelegation;
    private int mPTLoginType = 102;
    private boolean mPTLoginTypeAssigned = false;
    private boolean mWebSignedOn;
    private boolean mWebSignedOnAssigned = false;

    @NonNull
    public static synchronized PTAppDelegation getInstance() {
        PTAppDelegation pTAppDelegation;
        synchronized (PTAppDelegation.class) {
            if (instance == null) {
                instance = new PTAppDelegation();
            }
            pTAppDelegation = instance;
        }
        return pTAppDelegation;
    }

    private PTAppDelegation() {
        if (!VideoBoxApplication.getInstance().isPTApp()) {
            this.buddyHelperDelegation = getBuddyHelper();
        }
    }

    public void initDelegations() {
        initPTLoginType();
        final IMHelperDelegation iMHelper = getIMHelper();
        new Thread("InitDelegationsThread") {
            public void run() {
                PTAppDelegation.this.initWebSignedOn();
                iMHelper.initIMSignedOn();
            }
        }.start();
    }

    @NonNull
    public IMHelperDelegation getIMHelper() {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            return new IMHelperDelegation(PTApp.getInstance().getIMHelper());
        }
        if (this.imHelperDelegation == null) {
            this.imHelperDelegation = new IMHelperDelegation();
        }
        return this.imHelperDelegation;
    }

    @NonNull
    public PTBuddyHelperDelegation getBuddyHelper() {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            return new PTBuddyHelperDelegation(PTApp.getInstance().getBuddyHelper());
        }
        if (this.buddyHelperDelegation == null) {
            this.buddyHelperDelegation = new PTBuddyHelperDelegation();
        }
        return this.buddyHelperDelegation;
    }

    @NonNull
    public FavoriteMgrDelegation getFavoriteMgr() {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            return new FavoriteMgrDelegation(PTApp.getInstance().getFavoriteMgr());
        }
        if (this.favMgrDelegation == null) {
            this.favMgrDelegation = new FavoriteMgrDelegation();
        }
        return this.favMgrDelegation;
    }

    @Nullable
    public ABContactsHelperDelegation getABContactsHelper() {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            return new ABContactsHelperDelegation(PTApp.getInstance().getABContactsHelper());
        }
        if (this.contactsHelperDelegation == null) {
            this.contactsHelperDelegation = new ABContactsHelperDelegation();
        }
        return this.contactsHelperDelegation;
    }

    public boolean hasActiveCall() {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            return PTApp.getInstance().hasActiveCall();
        }
        return true;
    }

    public boolean presentToRoom(int i, String str, long j, boolean z) {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            return PTApp.getInstance().presentToRoom(i, str, j, z);
        }
        IPTService pTService = VideoBoxApplication.getInstance().getPTService();
        if (pTService != null) {
            try {
                return pTService.presentToRoom(i, str, j, z);
            } catch (RemoteException unused) {
            }
        }
        return false;
    }

    public void stopPresentToRoom(boolean z) {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            PTApp.getInstance().stopPresentToRoom(z);
            return;
        }
        IPTService pTService = VideoBoxApplication.getInstance().getPTService();
        if (pTService != null) {
            try {
                pTService.stopPresentToRoom(z);
            } catch (RemoteException unused) {
            }
        }
    }

    public int getCallStatus() {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            return PTApp.getInstance().getCallStatus();
        }
        return 2;
    }

    public int inviteBuddiesToConf(String[] strArr, String[] strArr2, String str, long j, String str2) {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            return PTApp.getInstance().inviteBuddiesToConf(strArr, strArr2, str, j, str2);
        }
        IPTService pTService = VideoBoxApplication.getInstance().getPTService();
        if (pTService != null) {
            try {
                return pTService.inviteBuddiesToConf(strArr, strArr2, str, j, str2);
            } catch (RemoteException unused) {
            }
        }
        return -1;
    }

    public synchronized int getPTLoginType() {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            return PTApp.getInstance().getPTLoginType();
        }
        if (!this.mPTLoginTypeAssigned) {
            initPTLoginType();
        }
        return this.mPTLoginType;
    }

    private synchronized void initPTLoginType() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            IPTService pTService = VideoBoxApplication.getInstance().getPTService();
            if (pTService != null) {
                try {
                    this.mPTLoginType = pTService.getPTLoginType();
                    this.mPTLoginTypeAssigned = true;
                } catch (RemoteException unused) {
                }
            }
        } else if (VideoBoxApplication.getInstance().isConfApp()) {
            this.mPTLoginType = ConfMgr.getInstance().getPTLoginType();
            this.mPTLoginTypeAssigned = true;
        } else {
            this.mPTLoginType = PTApp.getInstance().getPTLoginType();
            this.mPTLoginTypeAssigned = true;
        }
    }

    public synchronized boolean isWebSignedOn() {
        if (VideoBoxApplication.getInstance().isPTApp()) {
            return PTApp.getInstance().isWebSignedOn();
        }
        if (!this.mWebSignedOnAssigned) {
            initWebSignedOn();
        }
        return this.mWebSignedOn;
    }

    /* access modifiers changed from: private */
    public synchronized void initWebSignedOn() {
        IPTService pTService = VideoBoxApplication.getInstance().getPTService();
        if (pTService != null) {
            try {
                this.mWebSignedOn = pTService.isSignedIn();
                this.mWebSignedOnAssigned = true;
            } catch (RemoteException unused) {
            }
        }
    }

    public boolean isCurrentLoginTypeSupportIM() {
        return ZmLoginHelper.isTypeSupportIM(getPTLoginType());
    }

    public boolean isCurrentLoginTypeSupportFavoriteContacts() {
        int pTLoginType = getPTLoginType();
        if (pTLoginType == 100) {
            if (VideoBoxApplication.getInstance().isPTApp()) {
                return PTApp.getInstance().isCurrentLoginTypeSupportFavoriteContacts();
            }
            if (VideoBoxApplication.getInstance().isConfApp() && 1 == ConfMgr.getInstance().getCurrentVendor()) {
                return false;
            }
        }
        return ZmLoginHelper.isTypeSupportFavoriteContacts(pTLoginType);
    }

    /* access modifiers changed from: protected */
    public synchronized void setWebSignedOn(boolean z) {
        this.mWebSignedOn = z;
        this.mWebSignedOnAssigned = true;
    }

    /* access modifiers changed from: protected */
    public void resetPTLoginType() {
        initPTLoginType();
    }

    public boolean isAuthenticating() {
        if (VideoBoxApplication.getNonNullInstance().isPTApp()) {
            return PTApp.getInstance().isAuthenticating();
        }
        IPTService pTService = VideoBoxApplication.getNonNullInstance().getPTService();
        if (pTService != null) {
            try {
                return pTService.isAuthenticating();
            } catch (RemoteException unused) {
            }
        }
        return false;
    }

    public void setNeedCheckSwitchCall(boolean z) {
        if (VideoBoxApplication.getNonNullInstance().isPTApp()) {
            PTApp.getInstance().setNeedCheckSwitchCall(z);
            return;
        }
        IPTService pTService = VideoBoxApplication.getNonNullInstance().getPTService();
        if (pTService != null) {
            try {
                pTService.setNeedCheckSwitchCall(z);
            } catch (RemoteException unused) {
            }
        }
    }

    public void logout() {
        if (VideoBoxApplication.getNonNullInstance().isPTApp()) {
            PTApp.getInstance().logout(0, false);
            return;
        }
        IPTService pTService = VideoBoxApplication.getNonNullInstance().getPTService();
        if (pTService != null) {
            try {
                pTService.logout();
            } catch (RemoteException unused) {
            }
        }
    }

    public boolean isTaiWanZH() {
        if (VideoBoxApplication.getNonNullInstance().isPTApp()) {
            return PTApp.getInstance().isTaiWanZH();
        }
        IPTService pTService = VideoBoxApplication.getNonNullInstance().getPTService();
        if (pTService != null) {
            try {
                return pTService.isTaiWanZH();
            } catch (RemoteException unused) {
            }
        }
        return false;
    }
}

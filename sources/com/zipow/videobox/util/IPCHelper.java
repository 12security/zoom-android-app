package com.zipow.videobox.util;

import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.IPTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;

public class IPCHelper {
    private static final String TAG = "IPCHelper";
    @Nullable
    private static IPCHelper instance;

    @NonNull
    public static synchronized IPCHelper getInstance() {
        IPCHelper iPCHelper;
        synchronized (IPCHelper.class) {
            if (instance == null) {
                instance = new IPCHelper();
            }
            iPCHelper = instance;
        }
        return iPCHelper;
    }

    private IPCHelper() {
    }

    public void sendBOStatusChangeStart(boolean z, int i, String str) {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            IPTService pTService = instance2.getPTService();
            if (pTService != null) {
                try {
                    pTService.onBOStatusChangeStart(z, i, str);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public void sendBOStatusChangeComplete() {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            IPTService pTService = instance2.getPTService();
            if (pTService != null) {
                try {
                    pTService.onBOStatusChangeComplete();
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public void sendJoinConfConfirmMeetingStatus(boolean z, boolean z2) {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            IPTService pTService = instance2.getPTService();
            if (pTService != null) {
                try {
                    pTService.onJoinConfMeetingStatus(z, z2);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public void sendCallOutStatus(int i) {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            IPTService pTService = instance2.getPTService();
            if (pTService != null) {
                try {
                    pTService.onCallOutStatus(i);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public void sendRoomSystemCallStatus(int i, long j, boolean z) {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            IConfService confService = instance2.getConfService();
            if (confService != null) {
                try {
                    confService.onRoomCallEvent(i, j, z);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public void onNewIncomingCallCanceled(long j) {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            IConfService confService = instance2.getConfService();
            if (confService != null) {
                try {
                    confService.onNewIncomingCallCanceled(j);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public void dispacthPtLoginResultEventToConf() {
        String urlAction = PTApp.getInstance().getUrlAction();
        IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
        if (confService != null) {
            String str = "";
            if (PTApp.getInstance().isWebSignedOn()) {
                PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
                if (currentUserProfile != null) {
                    str = currentUserProfile.getUserName();
                }
            }
            try {
                confService.dispacthPtLoginResultEvent(PTApp.getInstance().isWebSignedOn(), urlAction, str);
            } catch (RemoteException unused) {
            }
        }
    }

    public void notifyLeaveAndPerformAction(int i, int i2) {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            IPTService pTService = instance2.getPTService();
            if (pTService != null) {
                try {
                    pTService.notifyLeaveAndPerformAction(i, i2, -1);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public void notifyLeaveAndPerformAction(int i, int i2, int i3) {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            IPTService pTService = instance2.getPTService();
            if (pTService != null) {
                try {
                    pTService.notifyLeaveAndPerformAction(i, i2, i3);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public void callOutRoomSystem(String str, int i) {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            IPTService pTService = instance2.getPTService();
            String.format("callOutRoomSystem address=%s; deviceType=%d", new Object[]{str, Integer.valueOf(i)});
            if (pTService != null) {
                try {
                    pTService.callOutRoomSystem(str, i);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public boolean notifyPTStartLogin(String str) {
        IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
        if (confService == null) {
            return false;
        }
        try {
            return confService.notifyPTStartLogin(str);
        } catch (RemoteException unused) {
            return false;
        }
    }

    public void tryRetrieveConfMicrophone() {
        IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
        if (confService != null) {
            try {
                confService.tryRetrieveMicrophone();
            } catch (RemoteException unused) {
            }
        }
    }
}

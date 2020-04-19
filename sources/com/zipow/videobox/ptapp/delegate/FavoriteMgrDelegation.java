package com.zipow.videobox.ptapp.delegate;

import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IPTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.FavoriteMgr;
import com.zipow.videobox.ptapp.ZoomContact;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class FavoriteMgrDelegation {
    private static final String TAG = "FavoriteMgrDelegation";
    private FavoriteMgr mFavMgr;

    protected FavoriteMgrDelegation() {
    }

    protected FavoriteMgrDelegation(FavoriteMgr favoriteMgr) {
        this.mFavMgr = favoriteMgr;
    }

    @Nullable
    public String getLocalPicturePath(String str) {
        FavoriteMgr favoriteMgr = this.mFavMgr;
        if (favoriteMgr != null) {
            return favoriteMgr.getLocalPicturePath(str);
        }
        IPTService pTService = VideoBoxApplication.getInstance().getPTService();
        if (pTService != null) {
            try {
                return pTService.FavoriteMgr_getLocalPicturePath(str);
            } catch (RemoteException unused) {
            }
        }
        return null;
    }

    public boolean getFavoriteListWithFilter(String str, @NonNull List<ZoomContact> list) {
        ByteArrayInputStream byteArrayInputStream;
        ObjectInputStream objectInputStream;
        Throwable th;
        Throwable th2;
        FavoriteMgr favoriteMgr = this.mFavMgr;
        if (favoriteMgr != null) {
            return favoriteMgr.getFavoriteListWithFilter(str, list);
        }
        IPTService pTService = VideoBoxApplication.getInstance().getPTService();
        if (pTService != null) {
            try {
                byte[] FavoriteMgr_getFavoriteListWithFilter = pTService.FavoriteMgr_getFavoriteListWithFilter(str);
                if (FavoriteMgr_getFavoriteListWithFilter == null) {
                    return false;
                }
                try {
                    byteArrayInputStream = new ByteArrayInputStream(FavoriteMgr_getFavoriteListWithFilter);
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    try {
                        list.addAll((List) objectInputStream.readObject());
                        objectInputStream.close();
                        byteArrayInputStream.close();
                        return true;
                    } catch (Throwable th3) {
                        Throwable th4 = th3;
                        th2 = r7;
                        th = th4;
                    }
                } catch (Exception unused) {
                    return false;
                } catch (Throwable th5) {
                    r6.addSuppressed(th5);
                }
            } catch (RemoteException unused2) {
            }
        }
        return false;
        if (th2 != null) {
            try {
                objectInputStream.close();
            } catch (Throwable th6) {
                th2.addSuppressed(th6);
            }
        } else {
            objectInputStream.close();
        }
        throw th;
        throw th;
        throw th;
    }
}

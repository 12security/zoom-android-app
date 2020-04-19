package com.zipow.videobox.ptapp.delegate;

import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IPTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.ptapp.ABContactsHelper;
import java.util.ArrayList;
import java.util.List;

public class ABContactsHelperDelegation {
    private static final String TAG = "ABContactsHelperDelegation";
    private ABContactsHelper mContactsHelper;

    protected ABContactsHelperDelegation() {
    }

    protected ABContactsHelperDelegation(ABContactsHelper aBContactsHelper) {
        this.mContactsHelper = aBContactsHelper;
    }

    public int getMatchedPhoneNumbers(@NonNull List<String> list) {
        ABContactsHelper aBContactsHelper = this.mContactsHelper;
        if (aBContactsHelper != null) {
            return aBContactsHelper.getMatchedPhoneNumbers(list);
        }
        String[] matchedPhoneNumbers = getMatchedPhoneNumbers();
        if (matchedPhoneNumbers == null) {
            return 0;
        }
        for (String add : matchedPhoneNumbers) {
            list.add(add);
        }
        return list.size();
    }

    @Nullable
    public String[] getMatchedPhoneNumbers() {
        if (this.mContactsHelper != null) {
            ArrayList arrayList = new ArrayList();
            this.mContactsHelper.getMatchedPhoneNumbers(arrayList);
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance == null) {
            return null;
        }
        IPTService pTService = instance.getPTService();
        if (pTService != null) {
            try {
                return pTService.ABContactsHelper_getMatchedPhoneNumbers();
            } catch (RemoteException unused) {
            }
        }
        return null;
    }

    public int inviteABContacts(@Nullable String[] strArr, String str) {
        if (strArr == null) {
            return 1;
        }
        if (this.mContactsHelper != null) {
            ArrayList arrayList = new ArrayList();
            for (String add : strArr) {
                arrayList.add(add);
            }
            return this.mContactsHelper.inviteABContacts(arrayList, str);
        }
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance == null) {
            return 11;
        }
        IPTService pTService = instance.getPTService();
        if (pTService != null) {
            try {
                return pTService.ABContactsHelper_inviteABContacts(strArr, str);
            } catch (RemoteException unused) {
            }
        }
        return 11;
    }

    @Nullable
    public String getVerifiedPhoneNumber() {
        ABContactsHelper aBContactsHelper = this.mContactsHelper;
        if (aBContactsHelper != null) {
            return aBContactsHelper.getVerifiedPhoneNumber();
        }
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null && instance.isConfApp()) {
            return ConfMgr.getInstance().getVerifiedPhoneNumber();
        }
        return null;
    }
}

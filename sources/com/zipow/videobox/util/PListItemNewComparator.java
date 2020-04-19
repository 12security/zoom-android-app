package com.zipow.videobox.util;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.view.ComparablePItemFields;
import com.zipow.videobox.view.PListItem;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import p021us.zoom.androidlib.util.CollectionsUtil;

public class PListItemNewComparator implements Comparator<PListItem> {
    Collator mCollator;

    public PListItemNewComparator(Locale locale) {
        this.mCollator = Collator.getInstance(locale);
        this.mCollator.setStrength(0);
    }

    public static void updatePlistItems(@NonNull ArrayList<PListItem> arrayList) {
        if (!CollectionsUtil.isListEmpty(arrayList)) {
            ConfMgr instance = ConfMgr.getInstance();
            CmmConfStatus confStatusObj = instance.getConfStatusObj();
            ConfUI instance2 = ConfUI.getInstance();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                PListItem pListItem = (PListItem) it.next();
                ComparablePItemFields comparablePItemFields = pListItem.getComparablePItemFields();
                comparablePItemFields.setUser(instance.getUserById(pListItem.userId));
                if (confStatusObj != null) {
                    comparablePItemFields.setMySelf(confStatusObj.isMyself(pListItem.userId));
                }
                comparablePItemFields.setHost(instance2.isDisplayAsHost(pListItem.userId));
                comparablePItemFields.setCoHost(instance2.isDisplayAsCohost(pListItem.userId));
                comparablePItemFields.setScreenName(pListItem.screenName);
            }
        }
    }

    public static String convertPlistItemsToString(@NonNull ArrayList<PListItem> arrayList) {
        if (CollectionsUtil.isListEmpty(arrayList)) {
            return null;
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((PListItem) it.next()).getComparablePItemFields());
        }
        return new Gson().toJson((Object) arrayList2);
    }

    public int compare(@NonNull PListItem pListItem, @NonNull PListItem pListItem2) {
        ComparablePItemFields comparablePItemFields = pListItem.getComparablePItemFields();
        ComparablePItemFields comparablePItemFields2 = pListItem2.getComparablePItemFields();
        if (comparablePItemFields.getUser() == null && comparablePItemFields2.getUser() == null) {
            return 0;
        }
        if (comparablePItemFields.getUser() == null) {
            return 1;
        }
        if (comparablePItemFields2.getUser() == null) {
            return -1;
        }
        if (comparablePItemFields.isMySelf() && !comparablePItemFields2.isMySelf()) {
            return -1;
        }
        if (!comparablePItemFields.isMySelf() && comparablePItemFields2.isMySelf()) {
            return 1;
        }
        if (comparablePItemFields.isHost() && !comparablePItemFields2.isHost()) {
            return -1;
        }
        if (comparablePItemFields2.isHost() && !comparablePItemFields.isHost()) {
            return 1;
        }
        if (comparablePItemFields.isSharingComputerAudio() && !comparablePItemFields2.isSharingComputerAudio()) {
            return -1;
        }
        if (comparablePItemFields2.isSharingComputerAudio() && !comparablePItemFields.isSharingComputerAudio()) {
            return 1;
        }
        if (comparablePItemFields.isRaiseHandState() == comparablePItemFields2.isRaiseHandState()) {
            if (comparablePItemFields.isRaiseHandState()) {
                int i = ((comparablePItemFields.getRaiseHandTimestamp() - comparablePItemFields2.getRaiseHandTimestamp()) > 0 ? 1 : ((comparablePItemFields.getRaiseHandTimestamp() - comparablePItemFields2.getRaiseHandTimestamp()) == 0 ? 0 : -1));
                if (i > 0) {
                    return 1;
                }
                if (i < 0) {
                    return -1;
                }
            }
            if (comparablePItemFields.isCoHost() && !comparablePItemFields2.isCoHost()) {
                return -1;
            }
            if (comparablePItemFields2.isCoHost() && !comparablePItemFields.isCoHost()) {
                return 1;
            }
            if (comparablePItemFields.isInterpreter() && !comparablePItemFields2.isInterpreter()) {
                return -1;
            }
            if (comparablePItemFields2.isInterpreter() && !comparablePItemFields.isInterpreter()) {
                return 1;
            }
            if (comparablePItemFields.getAudioStatus() == null && comparablePItemFields2.getAudioStatus() == null) {
                return 0;
            }
            if (comparablePItemFields.getAudioStatus() == null) {
                return 1;
            }
            if (comparablePItemFields2.getAudioStatus() == null) {
                return -1;
            }
            if (comparablePItemFields.getAudioType() != 2 && comparablePItemFields2.getAudioType() == 2) {
                return -1;
            }
            if (comparablePItemFields.getAudioType() == 2 && comparablePItemFields2.getAudioType() != 2) {
                return 1;
            }
            if (!comparablePItemFields.isMuted() && comparablePItemFields2.isMuted()) {
                return -1;
            }
            if (!comparablePItemFields.isMuted() || comparablePItemFields2.isMuted()) {
                return this.mCollator.compare(comparablePItemFields.getScreenName(), comparablePItemFields2.getScreenName());
            }
            return 1;
        } else if (comparablePItemFields.isRaiseHandState()) {
            return -1;
        } else {
            return 1;
        }
    }
}

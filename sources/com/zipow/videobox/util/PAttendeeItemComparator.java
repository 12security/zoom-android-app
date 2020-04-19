package com.zipow.videobox.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.view.PAttendeeItem;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class PAttendeeItemComparator implements Comparator<PAttendeeItem> {
    Collator mCollator;
    @Nullable
    ConfMgr mConfMgr = ConfMgr.getInstance();

    public PAttendeeItemComparator(Locale locale) {
        this.mCollator = Collator.getInstance(locale);
        this.mCollator.setStrength(0);
    }

    public int compare(@NonNull PAttendeeItem pAttendeeItem, @NonNull PAttendeeItem pAttendeeItem2) {
        int i = -1;
        if (pAttendeeItem.isRaisedHand != pAttendeeItem2.isRaisedHand) {
            return pAttendeeItem.isRaisedHand ? -1 : 1;
        }
        if (pAttendeeItem.audioType != 2 && pAttendeeItem2.audioType == 2) {
            return -1;
        }
        if (pAttendeeItem.audioType == 2 && pAttendeeItem2.audioType != 2) {
            return 1;
        }
        if (pAttendeeItem.audioType != 2) {
            if (pAttendeeItem.audioOn && !pAttendeeItem2.audioOn) {
                return -1;
            }
            if (!pAttendeeItem.audioOn && pAttendeeItem2.audioOn) {
                return 1;
            }
            if (pAttendeeItem.audioOn) {
                boolean isTalking = ConfLocalHelper.isTalking(pAttendeeItem.nodeID);
                if (isTalking != ConfLocalHelper.isTalking(pAttendeeItem2.nodeID)) {
                    if (!isTalking) {
                        i = 1;
                    }
                    return i;
                }
            }
        }
        return this.mCollator.compare(pAttendeeItem.name, pAttendeeItem2.name);
    }
}

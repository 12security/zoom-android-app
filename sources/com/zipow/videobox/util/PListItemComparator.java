package com.zipow.videobox.util;

import androidx.annotation.NonNull;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.view.PListItem;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class PListItemComparator implements Comparator<PListItem> {
    Collator mCollator;
    @NonNull
    final ConfMgr mConfMgr = ConfMgr.getInstance();
    @NonNull
    final ConfUI mConfUI;

    public PListItemComparator(Locale locale) {
        this.mCollator = Collator.getInstance(locale);
        this.mCollator.setStrength(0);
        this.mConfUI = ConfUI.getInstance();
    }

    public int compare(@NonNull PListItem pListItem, @NonNull PListItem pListItem2) {
        CmmUser userById = this.mConfMgr.getUserById(pListItem.userId);
        CmmUser userById2 = this.mConfMgr.getUserById(pListItem2.userId);
        if (userById == null && userById2 == null) {
            return 0;
        }
        if (userById == null) {
            return 1;
        }
        if (userById2 == null) {
            return -1;
        }
        CmmConfStatus confStatusObj = this.mConfMgr.getConfStatusObj();
        if (confStatusObj != null) {
            if (confStatusObj.isMyself(pListItem.userId) && !confStatusObj.isMyself(pListItem2.userId)) {
                return -1;
            }
            if (confStatusObj.isMyself(pListItem2.userId) && !confStatusObj.isMyself(pListItem.userId)) {
                return 1;
            }
        }
        boolean isDisplayAsHost = this.mConfUI.isDisplayAsHost(pListItem.userId);
        boolean isDisplayAsHost2 = this.mConfUI.isDisplayAsHost(pListItem2.userId);
        if (isDisplayAsHost && !isDisplayAsHost2) {
            return -1;
        }
        if (isDisplayAsHost2 && !isDisplayAsHost) {
            return 1;
        }
        if (userById.isSharingPureComputerAudio() && !userById2.isSharingPureComputerAudio()) {
            return -1;
        }
        if (userById2.isSharingPureComputerAudio() && !userById.isSharingPureComputerAudio()) {
            return 1;
        }
        boolean raiseHandState = userById.getRaiseHandState();
        if (raiseHandState != userById2.getRaiseHandState()) {
            return raiseHandState ? -1 : 1;
        }
        if (raiseHandState) {
            int i = ((pListItem.getmRaiseHandTimestamp() - pListItem2.getmRaiseHandTimestamp()) > 0 ? 1 : ((pListItem.getmRaiseHandTimestamp() - pListItem2.getmRaiseHandTimestamp()) == 0 ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            if (i < 0) {
                return -1;
            }
        }
        boolean isDisplayAsCohost = this.mConfUI.isDisplayAsCohost(pListItem.userId);
        boolean isDisplayAsCohost2 = this.mConfUI.isDisplayAsCohost(pListItem2.userId);
        if (isDisplayAsCohost && !isDisplayAsCohost2) {
            return -1;
        }
        if (isDisplayAsCohost2 && !isDisplayAsCohost) {
            return 1;
        }
        if (userById.isInterpreter() && !userById2.isInterpreter()) {
            return -1;
        }
        if (userById.isInterpreter() && !userById2.isInterpreter()) {
            return 1;
        }
        CmmAudioStatus audioStatusObj = userById.getAudioStatusObj();
        CmmAudioStatus audioStatusObj2 = userById2.getAudioStatusObj();
        if (audioStatusObj == null && audioStatusObj2 == null) {
            return 0;
        }
        if (audioStatusObj == null) {
            return 1;
        }
        if (audioStatusObj2 == null) {
            return -1;
        }
        if (audioStatusObj.getAudiotype() != 2 && audioStatusObj2.getAudiotype() == 2) {
            return -1;
        }
        if (audioStatusObj.getAudiotype() == 2 && audioStatusObj2.getAudiotype() != 2) {
            return 1;
        }
        if (!audioStatusObj.getIsMuted() && audioStatusObj2.getIsMuted()) {
            return -1;
        }
        if (!audioStatusObj.getIsMuted() || audioStatusObj2.getIsMuted()) {
            return this.mCollator.compare(pListItem.screenName, pListItem2.screenName);
        }
        return 1;
    }
}

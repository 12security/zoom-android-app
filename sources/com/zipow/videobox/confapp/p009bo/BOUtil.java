package com.zipow.videobox.confapp.p009bo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfMgr;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.confapp.bo.BOUtil */
public class BOUtil {
    public static final int BO_ERROR_CANNOT_EDIT = 4;
    public static final int BO_ERROR_CANNOT_FIND = 5;
    public static final int BO_ERROR_NO_ERROR = 0;
    public static final int BO_ERROR_NO_PRIVILEGE = 7;
    public static final int BO_ERROR_NULL_POINTER = 1;
    public static final int BO_ERROR_OUT_OF_MAX_COUNT = 8;
    public static final int BO_ERROR_TOKEN_NOT_READY = 2;
    public static final int BO_ERROR_UPLOAD_FAIL = 3;
    public static final int BO_ERROR_WRONG_CURRENT_STATUS = 6;
    public static final int BO_WAITING_SECONDS = 60;

    public static boolean joinBO(String str, int i) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return false;
        }
        if (i == 1 || !bOMgr.isInBOMeeting()) {
            return bOMgr.joinBO(str, i);
        }
        return false;
    }

    public static boolean leaveBO() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr != null && bOMgr.isInBOMeeting()) {
            return bOMgr.leaveBO();
        }
        return false;
    }

    public static boolean endBO(int i) {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr != null && bOMgr.isBOController()) {
            return bOMgr.stop(i);
        }
        return false;
    }

    public static void checkBOStatus() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr != null) {
            int controlStatus = bOMgr.getControlStatus();
            if (bOMgr.isInBOMeeting() && controlStatus != 2) {
                leaveBO();
            }
        }
    }

    public static void requestBOForHelp() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr != null && bOMgr.isInBOMeeting() && !bOMgr.isBOController()) {
            bOMgr.requestForHelp();
        }
    }

    public static BOObject getMyBOMeeting(int i) {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return null;
        }
        return bOMgr.getMyBOObject(i);
    }

    @NonNull
    public static String getMyBOMeetingName(int i) {
        BOObject myBOMeeting = getMyBOMeeting(i);
        if (myBOMeeting == null) {
            return "";
        }
        String meetingName = myBOMeeting.getMeetingName();
        return StringUtil.isEmptyOrNull(meetingName) ? "" : meetingName;
    }

    public static boolean isInBOMeeting() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return false;
        }
        return bOMgr.isInBOMeeting();
    }

    public static boolean isInBOController() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return false;
        }
        return bOMgr.isBOController();
    }

    public static int getBOControlStatus() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return 1;
        }
        return bOMgr.getControlStatus();
    }

    public static boolean isBOControllerStarted() {
        int bOControlStatus = getBOControlStatus();
        return bOControlStatus == 2 || bOControlStatus == 3;
    }

    public static int getBOConfStatus(@Nullable BOObject bOObject) {
        if (bOObject == null) {
            return 5;
        }
        return bOObject.getMeetingStatus();
    }

    public static int getBOUserStatus(@Nullable BOObject bOObject, @Nullable CmmUser cmmUser) {
        if (bOObject == null || cmmUser == null) {
            return 1;
        }
        String userGUID = cmmUser.getUserGUID();
        if (StringUtil.isEmptyOrNull(userGUID)) {
            return 1;
        }
        BOUser userByUserGUID = bOObject.getUserByUserGUID(userGUID);
        if (userByUserGUID == null) {
            return 1;
        }
        return userByUserGUID.getUserStatus();
    }

    private static CmmUserList getBOMasterConfUserList() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return null;
        }
        return bOMgr.getMasterConfUserList();
    }

    public static String getBOMeetingNameByBid(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return "";
        }
        BOObject bOObjectByBId = bOMgr.getBOObjectByBId(str);
        if (bOObjectByBId == null) {
            return "";
        }
        return bOObjectByBId.getMeetingName();
    }

    public static CmmUser getBOHostUser() {
        CmmUserList bOMasterConfUserList = getBOMasterConfUserList();
        if (bOMasterConfUserList == null) {
            return null;
        }
        return bOMasterConfUserList.getHostUser();
    }

    public static CmmUser getBOUser(long j) {
        CmmUserList bOMasterConfUserList = getBOMasterConfUserList();
        if (bOMasterConfUserList == null) {
            return null;
        }
        return bOMasterConfUserList.getUserById(j);
    }

    public static boolean isHostInThisBoMeeting() {
        if (!isInBOMeeting()) {
            return false;
        }
        return isUserInThisBOMeeting(getBOHostUser());
    }

    public static boolean isUserInThisBOMeeting(CmmUser cmmUser) {
        return isUserInBOMeeting(getMyBOMeeting(2), cmmUser);
    }

    public static boolean isUserInBOMeeting(@Nullable BOObject bOObject, @Nullable CmmUser cmmUser) {
        boolean z = false;
        if (bOObject == null || cmmUser == null) {
            return false;
        }
        BOUser userByUserGUID = bOObject.getUserByUserGUID(cmmUser.getUserGUID());
        if (userByUserGUID == null) {
            return false;
        }
        if (userByUserGUID.getUserStatus() == 2) {
            z = true;
        }
        return z;
    }

    public static boolean isBOStartedAndUnassigned() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr != null && !bOMgr.isInBOMeeting() && getBOControlStatus() == 2 && !isInBOController() && getMyBOMeeting(3) == null) {
            return true;
        }
        return false;
    }

    public static boolean isAutoJoinEnable() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return false;
        }
        return bOMgr.isAutoJoinEnable();
    }

    public static boolean isBackToMainSessionEnabled() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return false;
        }
        return bOMgr.isBackToMainSessionEnabled();
    }

    public static int getStopWaitingSeconds() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return 0;
        }
        return bOMgr.getStopWaitingSeconds();
    }

    public static boolean isTimerEnabled() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return false;
        }
        return bOMgr.isTimerEnabled();
    }

    public static boolean isTimerAutoEndEnabled() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return false;
        }
        return bOMgr.isTimerAutoEndEnabled();
    }

    public static int getTimerDuration() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return 0;
        }
        return bOMgr.getTimerDuration();
    }

    public static int getRunTimeElapsedTime() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return 0;
        }
        return bOMgr.getRunTimeElapsedTime();
    }

    public static boolean isLaunchReasonByBO() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z = false;
        if (confContext == null) {
            return false;
        }
        int launchReason = confContext.getLaunchReason();
        if (launchReason == 10 || launchReason == 11) {
            z = true;
        }
        return z;
    }
}

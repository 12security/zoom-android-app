package com.zipow.videobox.confapp.meeting;

import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.view.ConfChatAttendeeItem;
import java.io.Serializable;
import p021us.zoom.androidlib.util.StringUtil;

public class PromoteOrDowngradeItem implements Serializable, Cloneable {
    public static final int DEPROMOTE_PANELIST = 2;
    public static final int PROMOTE_ATTENDEE = 1;
    private int mAction = 1;
    @Nullable
    private String mJid;
    @Nullable
    private String mName;
    private long mUserId;

    public PromoteOrDowngradeItem(ConfChatAttendeeItem confChatAttendeeItem, int i) {
        this.mUserId = confChatAttendeeItem.nodeID;
        this.mJid = confChatAttendeeItem.jid;
        this.mName = confChatAttendeeItem.name;
        this.mAction = i;
    }

    public PromoteOrDowngradeItem() {
    }

    public static PromoteOrDowngradeItem getPromoteAttendeeItem(long j, int i) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById == null) {
            return null;
        }
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null) {
            return null;
        }
        String userJIDByNodeID = qAComponent.getUserJIDByNodeID(j);
        if (StringUtil.isEmptyOrNull(userJIDByNodeID)) {
            return null;
        }
        PromoteOrDowngradeItem promoteOrDowngradeItem = new PromoteOrDowngradeItem();
        promoteOrDowngradeItem.mUserId = j;
        promoteOrDowngradeItem.mJid = userJIDByNodeID;
        promoteOrDowngradeItem.mName = userById.getScreenName();
        promoteOrDowngradeItem.mAction = i;
        return promoteOrDowngradeItem;
    }

    @Nullable
    public String getmJid() {
        return this.mJid;
    }

    public void setmJid(@Nullable String str) {
        this.mJid = str;
    }

    @Nullable
    public String getmName() {
        return this.mName;
    }

    public void setmName(@Nullable String str) {
        this.mName = str;
    }

    public long getmUserId() {
        return this.mUserId;
    }

    public void setmUserId(long j) {
        this.mUserId = j;
    }

    public int getmAction() {
        return this.mAction;
    }

    public void setmAction(int i) {
        this.mAction = i;
    }

    @Nullable
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}

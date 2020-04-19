package com.zipow.videobox.view.sip.sms;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.IPBXMessage;
import com.zipow.videobox.sip.server.IPBXMessageAPI;
import com.zipow.videobox.sip.server.IPBXMessageSession;
import com.zipow.videobox.util.ZMPhoneUtils;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;

public class IPBXMessageSessionItem {

    /* renamed from: ID */
    private String f351ID;
    private int countOfMessage;
    private int direction;
    private String displayName;
    private String draftText;
    private boolean isLocalSession;
    private int lastMessageSendStatus = 0;
    private String lastViewedMessageId;

    /* renamed from: me */
    private PBXMessageContact f352me;
    private List<PBXMessageContact> others;
    private String summary;
    private int totalUnReadCount;
    private long updatedTime;

    @NonNull
    public static IPBXMessageSessionItem fromMessageSession(@NonNull String str, @NonNull IPBXMessageAPI iPBXMessageAPI) {
        IPBXMessageSessionItem iPBXMessageSessionItem = new IPBXMessageSessionItem();
        iPBXMessageSessionItem.initializeLocalSession(str, iPBXMessageAPI);
        return iPBXMessageSessionItem;
    }

    @NonNull
    public static IPBXMessageSessionItem fromMessageSession(@NonNull IPBXMessageSession iPBXMessageSession) {
        IPBXMessageSessionItem iPBXMessageSessionItem = new IPBXMessageSessionItem();
        iPBXMessageSessionItem.initialize(iPBXMessageSession);
        return iPBXMessageSessionItem;
    }

    public void initialize(@NonNull IPBXMessageSession iPBXMessageSession) {
        this.f351ID = iPBXMessageSession.getID();
        this.summary = iPBXMessageSession.getSummary();
        this.f352me = iPBXMessageSession.getMe();
        this.others = iPBXMessageSession.getOthers();
        this.direction = iPBXMessageSession.getDirection();
        this.totalUnReadCount = iPBXMessageSession.getTotalUnreadCount();
        this.draftText = iPBXMessageSession.getDraftText();
        this.updatedTime = iPBXMessageSession.getUpdatedTime();
        this.lastViewedMessageId = iPBXMessageSession.getLastViewedMessageId();
        this.countOfMessage = iPBXMessageSession.getCountOfMessage();
        IPBXMessage latestMessage = iPBXMessageSession.getLatestMessage();
        if (latestMessage != null) {
            this.lastMessageSendStatus = latestMessage.getSendStatus();
        }
        updateDisplayName();
    }

    public void initializeLocalSession(@NonNull String str, @NonNull IPBXMessageAPI iPBXMessageAPI) {
        setID(str);
        int messageCountInLocalSession = iPBXMessageAPI.getMessageCountInLocalSession(str);
        if (messageCountInLocalSession != 0) {
            IPBXMessage messageByIndexInLocalSession = iPBXMessageAPI.getMessageByIndexInLocalSession(str, 0);
            if (messageByIndexInLocalSession != null) {
                this.lastMessageSendStatus = messageByIndexInLocalSession.getSendStatus();
                setMe(messageByIndexInLocalSession.getFromContact());
                setOthers(messageByIndexInLocalSession.getToContacts());
                setDirection(1);
                this.countOfMessage = messageCountInLocalSession;
                this.updatedTime = messageByIndexInLocalSession.getCreateTime();
                this.summary = messageByIndexInLocalSession.getText();
                this.isLocalSession = true;
                updateDisplayName();
            }
        }
    }

    public String getID() {
        return this.f351ID;
    }

    public void setID(String str) {
        this.f351ID = str;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String str) {
        this.summary = str;
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int i) {
        this.direction = i;
    }

    public String getDraftText() {
        return this.draftText;
    }

    public void setDraftText(String str) {
        this.draftText = str;
    }

    public int getTotalUnReadCount() {
        return this.totalUnReadCount;
    }

    public void setTotalUnReadCount(int i) {
        this.totalUnReadCount = i;
    }

    public long getUpdatedTime() {
        return this.updatedTime;
    }

    public void setUpdatedTime(long j) {
        this.updatedTime = j;
    }

    public String getLastViewedMessageId() {
        return this.lastViewedMessageId;
    }

    public void setLastViewedMessageId(String str) {
        this.lastViewedMessageId = str;
    }

    public int getCountOfMessage() {
        return this.countOfMessage;
    }

    public void setCountOfMessage(int i) {
        this.countOfMessage = i;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public PBXMessageContact getMe() {
        return this.f352me;
    }

    public void setMe(PBXMessageContact pBXMessageContact) {
        this.f352me = pBXMessageContact;
    }

    public List<PBXMessageContact> getOthers() {
        return this.others;
    }

    public void setOthers(List<PBXMessageContact> list) {
        this.others = list;
    }

    public boolean isLocalSession() {
        return this.isLocalSession;
    }

    public void setLocalSession(boolean z) {
        this.isLocalSession = z;
    }

    public void updateDisplayName() {
        List others2 = getOthers();
        if (!CollectionsUtil.isListEmpty(others2)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < others2.size(); i++) {
                PBXMessageContact pBXMessageContact = (PBXMessageContact) others2.get(i);
                if (pBXMessageContact != null && (!TextUtils.isEmpty(pBXMessageContact.getDisplayName()) || !TextUtils.isEmpty(pBXMessageContact.getPhoneNumber()))) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    String displayNameByNumber = ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(pBXMessageContact.getPhoneNumber());
                    if (TextUtils.isEmpty(displayNameByNumber)) {
                        displayNameByNumber = pBXMessageContact.getDisplayName();
                    }
                    if (TextUtils.isEmpty(displayNameByNumber)) {
                        displayNameByNumber = ZMPhoneUtils.formatPhoneNumber(pBXMessageContact.getPhoneNumber());
                    }
                    sb.append(displayNameByNumber);
                }
            }
            this.displayName = sb.toString();
        }
    }

    public boolean equals(@Nullable Object obj) {
        return (obj instanceof IPBXMessageSessionItem) && TextUtils.equals(this.f351ID, ((IPBXMessageSessionItem) obj).f351ID);
    }

    public int getLastMessageSendStatus() {
        return this.lastMessageSendStatus;
    }

    public void setLastMessageSendStatus(int i) {
        this.lastMessageSendStatus = i;
    }
}

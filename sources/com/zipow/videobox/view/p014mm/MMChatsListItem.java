package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.FileIntegrationInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.TextCommandHelper;
import com.zipow.videobox.util.TextCommandHelper.DraftBean;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ISearchableItem;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMChatsListItem */
public class MMChatsListItem implements ISearchableItem {
    private static final int MAX_SCORE = 2048;
    private static final String TAG = "MMChatsListItem";

    /* renamed from: at */
    private String f342at;
    private String avatar;
    private CharSequence draftMessage;
    private long draftTimeStamp;
    private IMAddrBookItem fromContact;
    private boolean isGroup;
    private boolean isNotifyOff;
    private boolean isRoom;
    private CharSequence latestMessage;
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mIsE2E;
    private int mThreadSortType;
    private int markUnreadMessageCount;
    private int matchScore;
    private long searchOpenTimeStamp;
    private String sessionId;
    private long timeStamp;
    private String title;
    private int unreadMessageCount;
    private int unreadMessageCountBySetting;

    public int getPriority() {
        return 2;
    }

    public boolean isMessageDecrypted(int i) {
        return i == 7 || i == 4 || i == 1 || i == 2;
    }

    public MMChatsListItem() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                this.mThreadSortType = threadDataProvider.getThreadSortType();
            }
        }
    }

    @Nullable
    public static MMChatsListItem fromZoomChatSession(@NonNull ZoomChatSession zoomChatSession, @NonNull ZoomMessenger zoomMessenger, @Nullable Context context) {
        return fromZoomChatSession(zoomChatSession, zoomMessenger, context, false);
    }

    @Nullable
    public static MMChatsListItem fromZoomChatSession(@NonNull ZoomChatSession zoomChatSession, @NonNull ZoomMessenger zoomMessenger, @Nullable Context context, boolean z) {
        ZoomBuddy zoomBuddy;
        boolean z2;
        String str;
        boolean z3;
        String str2;
        boolean z4;
        String str3;
        CharSequence charSequence;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11;
        String str12;
        ZoomMessenger zoomMessenger2 = zoomMessenger;
        Context context2 = context;
        String str13 = null;
        if (context2 == null) {
            return null;
        }
        boolean isGroup2 = zoomChatSession.isGroup();
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return null;
        }
        if (isGroup2) {
            ZoomGroup sessionGroup = zoomChatSession.getSessionGroup();
            if (sessionGroup == null) {
                return null;
            }
            str = sessionGroup.getGroupDisplayName(context2);
            z2 = sessionGroup.isForceE2EGroup();
            z3 = sessionGroup.isRoom();
            zoomBuddy = null;
        } else {
            ZoomBuddy sessionBuddy = zoomChatSession.getSessionBuddy();
            if (sessionBuddy == null) {
                if (!TextUtils.equals(myself.getJid(), zoomChatSession.getSessionId())) {
                    return null;
                }
                sessionBuddy = myself;
            }
            str = BuddyNameUtil.getBuddyDisplayName(sessionBuddy, null);
            zoomBuddy = sessionBuddy;
            z3 = false;
            z2 = false;
        }
        MMChatsListItem mMChatsListItem = new MMChatsListItem();
        mMChatsListItem.setSessionId(zoomChatSession.getSessionId());
        mMChatsListItem.setE2E(z2);
        mMChatsListItem.setTitle(str);
        mMChatsListItem.setIsGroup(isGroup2);
        mMChatsListItem.setUnreadMessageCount(zoomChatSession.getUnreadMessageCount());
        mMChatsListItem.setMarkUnreadMessageCount(zoomChatSession.getMarkUnreadMessageCount());
        mMChatsListItem.setUnreadMessageCountBySetting(zoomChatSession.getUnreadMessageCountBySetting());
        mMChatsListItem.setRoom(z3);
        mMChatsListItem.setAt("");
        mMChatsListItem.setSearchOpenTimeStamp(zoomChatSession.getLastSearchAndOpenSessionTime());
        if (!isGroup2) {
            mMChatsListItem.setAvatar(zoomBuddy.getLocalPicturePath());
            IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(zoomBuddy);
            if (fromZoomBuddy != null) {
                fromZoomBuddy.setJid(zoomBuddy.getJid());
                mMChatsListItem.setFromContact(fromZoomBuddy);
            }
        }
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        mMChatsListItem.setNotifyOff(notificationSettingMgr != null && notificationSettingMgr.isSessionBlocked(zoomChatSession.getSessionId()));
        DraftBean restoreTextCommand = zoomChatSession.getMessageDraftTime() > 0 ? TextCommandHelper.getInstance().restoreTextCommand(false, zoomChatSession.getSessionId()) : null;
        if (restoreTextCommand == null || StringUtil.isEmptyOrNull(restoreTextCommand.getLabel())) {
            mMChatsListItem.draftMessage = "";
            mMChatsListItem.draftTimeStamp = 0;
        } else {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(context2.getString(C4558R.string.zm_msg_draft_71416, new Object[]{restoreTextCommand.getLabel()}));
            spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(C4558R.color.zm_ui_kit_color_red_E02828)), 0, context2.getString(C4558R.string.zm_msg_draft_71416, new Object[]{""}).length(), 33);
            mMChatsListItem.draftMessage = spannableStringBuilder;
            mMChatsListItem.draftTimeStamp = restoreTextCommand.getDraftTime();
        }
        ZoomMessage lastMessage = zoomChatSession.getLastMessage();
        if (lastMessage == null) {
            mMChatsListItem.setTimeStamp(0);
            mMChatsListItem.setLatestMessage("");
            if (zoomChatSession.isGroup() || TextUtils.equals(myself.getJid(), zoomChatSession.getSessionId()) || z || zoomChatSession.getMarkUnreadMessageCount() > 0) {
                return mMChatsListItem;
            }
            return null;
        }
        String str14 = "";
        String senderID = lastMessage.getSenderID();
        ZoomBuddy buddyWithJID = zoomMessenger2.getBuddyWithJID(senderID);
        if (StringUtil.isSameString(senderID, myself.getJid())) {
            str2 = BuddyNameUtil.getBuddyDisplayName(myself, null, false);
            z4 = true;
        } else if (buddyWithJID != null) {
            str2 = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null);
            z4 = false;
        } else {
            str2 = str14;
            z4 = false;
        }
        mMChatsListItem.setTimeStamp(lastMessage.getStamp());
        if (lastMessage.couldReallySupport()) {
            int messageType = lastMessage.getMessageType();
            if (messageType == 80) {
                String str15 = (String) lastMessage.getBody();
                if (StringUtil.isSameString(str15, myself.getJid())) {
                    str13 = context2.getString(C4558R.string.zm_msg_delete_by_me_24679);
                } else if (!StringUtil.isEmptyOrNull(str15)) {
                    ZoomBuddy buddyWithJID2 = zoomMessenger2.getBuddyWithJID(str15);
                    if (buddyWithJID2 != null) {
                        str13 = context2.getString(C4558R.string.zm_msg_delete_by_other_24679, new Object[]{BuddyNameUtil.getBuddyDisplayName(buddyWithJID2, null)});
                    }
                }
                mMChatsListItem.setLatestMessage(str13);
            } else if (messageType != 88) {
                switch (messageType) {
                    case 0:
                        if (z4 || !isGroup2) {
                            charSequence = lastMessage.getBody();
                        } else {
                            charSequence = TextUtils.concat(new CharSequence[]{str2, ": ", lastMessage.getBody()});
                        }
                        if (lastMessage.isE2EMessage() && !mMChatsListItem.isMessageDecrypted(lastMessage.getMessageState())) {
                            StringBuilder sb = new StringBuilder();
                            if (z4 || !isGroup2) {
                                str4 = "";
                            } else {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(str2);
                                sb2.append(": ");
                                str4 = sb2.toString();
                            }
                            sb.append(str4);
                            sb.append(context.getResources().getString(C4558R.string.zm_msg_e2e_chatslist_decrypt_failed));
                            charSequence = sb.toString();
                        }
                        mMChatsListItem.setLatestMessage(charSequence);
                        break;
                    case 1:
                        if (!z4) {
                        }
                        str5 = context2.getString(C4558R.string.zm_mm_lbl_message_picture);
                        StringBuilder sb3 = new StringBuilder();
                        if (!z4) {
                        }
                        str6 = "";
                        sb3.append(str6);
                        sb3.append(context.getResources().getString(C4558R.string.zm_msg_e2e_chatslist_decrypt_failed));
                        str5 = sb3.toString();
                        mMChatsListItem.setLatestMessage(str5);
                        break;
                    case 2:
                        if (z4 || !isGroup2) {
                            str7 = context2.getString(C4558R.string.zm_mm_lbl_message_voice);
                        } else {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str2);
                            sb4.append(": ");
                            sb4.append(context2.getString(C4558R.string.zm_mm_lbl_message_voice));
                            str7 = sb4.toString();
                        }
                        if (lastMessage.isE2EMessage() && !mMChatsListItem.isMessageDecrypted(lastMessage.getMessageState())) {
                            StringBuilder sb5 = new StringBuilder();
                            if (z4 || !isGroup2) {
                                str8 = "";
                            } else {
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append(str2);
                                sb6.append(": ");
                                str8 = sb6.toString();
                            }
                            sb5.append(str8);
                            sb5.append(context.getResources().getString(C4558R.string.zm_msg_e2e_chatslist_decrypt_failed));
                            str7 = sb5.toString();
                        }
                        mMChatsListItem.setLatestMessage(str7);
                        break;
                    default:
                        switch (messageType) {
                            case 4:
                                if (!z4 && isGroup2) {
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append(str2);
                                    sb7.append(": ");
                                    sb7.append(context2.getString(C4558R.string.zm_mm_lbl_message_meeting_invitation));
                                    sb7.toString();
                                    break;
                                } else {
                                    context2.getString(C4558R.string.zm_mm_lbl_message_meeting_invitation);
                                    break;
                                }
                            case 5:
                            case 6:
                                break;
                            default:
                                switch (messageType) {
                                    case 10:
                                        FileInfo fileInfo = lastMessage.getFileInfo();
                                        if (fileInfo != null) {
                                            if (PTApp.getInstance().isFileTransferDisabled()) {
                                                str13 = context2.getString(C4558R.string.zm_msg_message_file_86061);
                                            } else if (z4 || !isGroup2) {
                                                str13 = context2.getString(C4558R.string.zm_mm_lbl_message_file, new Object[]{fileInfo.name});
                                            } else {
                                                StringBuilder sb8 = new StringBuilder();
                                                sb8.append(str2);
                                                sb8.append(": ");
                                                sb8.append(context2.getString(C4558R.string.zm_mm_lbl_message_file, new Object[]{fileInfo.name}));
                                                str13 = sb8.toString();
                                            }
                                        }
                                        if (lastMessage.isE2EMessage() && !mMChatsListItem.isMessageDecrypted(lastMessage.getMessageState())) {
                                            StringBuilder sb9 = new StringBuilder();
                                            if (z4 || !isGroup2) {
                                                str9 = "";
                                            } else {
                                                StringBuilder sb10 = new StringBuilder();
                                                sb10.append(str2);
                                                sb10.append(": ");
                                                str9 = sb10.toString();
                                            }
                                            sb9.append(str9);
                                            sb9.append(context.getResources().getString(C4558R.string.zm_msg_e2e_chatslist_decrypt_failed));
                                            str13 = sb9.toString();
                                        }
                                        mMChatsListItem.setLatestMessage(str13);
                                        break;
                                    case 11:
                                    case 14:
                                        mMChatsListItem.setLatestMessage(context2.getString(C4558R.string.zm_msg_webhoot_new_notification, new Object[]{str2}));
                                        break;
                                    case 12:
                                        break;
                                    case 13:
                                        if (z4 || !isGroup2) {
                                            str10 = context2.getString(C4558R.string.zm_mm_lbl_message_code_snippet_31945);
                                        } else {
                                            StringBuilder sb11 = new StringBuilder();
                                            sb11.append(str2);
                                            sb11.append(": ");
                                            sb11.append(context2.getString(C4558R.string.zm_mm_lbl_message_code_snippet_31945));
                                            str10 = sb11.toString();
                                        }
                                        if (lastMessage.isE2EMessage() && !mMChatsListItem.isMessageDecrypted(lastMessage.getMessageState())) {
                                            StringBuilder sb12 = new StringBuilder();
                                            if (z4 || !isGroup2) {
                                                str11 = "";
                                            } else {
                                                StringBuilder sb13 = new StringBuilder();
                                                sb13.append(str2);
                                                sb13.append(": ");
                                                str11 = sb13.toString();
                                            }
                                            sb12.append(str11);
                                            sb12.append(context.getResources().getString(C4558R.string.zm_msg_e2e_chatslist_decrypt_failed));
                                            str10 = sb12.toString();
                                        }
                                        mMChatsListItem.setLatestMessage(str10);
                                        break;
                                    case 15:
                                        FileIntegrationInfo fileIntegrationShareInfo = lastMessage.getFileIntegrationShareInfo();
                                        if (fileIntegrationShareInfo != null) {
                                            if (z4 || !isGroup2) {
                                                str13 = context2.getString(C4558R.string.zm_mm_lbl_message_file, new Object[]{fileIntegrationShareInfo.getFileName()});
                                            } else {
                                                StringBuilder sb14 = new StringBuilder();
                                                sb14.append(str2);
                                                sb14.append(": ");
                                                sb14.append(context2.getString(C4558R.string.zm_mm_lbl_message_file, new Object[]{fileIntegrationShareInfo.getFileName()}));
                                                str13 = sb14.toString();
                                            }
                                        }
                                        if (lastMessage.isE2EMessage() && !mMChatsListItem.isMessageDecrypted(lastMessage.getMessageState())) {
                                            StringBuilder sb15 = new StringBuilder();
                                            if (z4 || !isGroup2) {
                                                str12 = "";
                                            } else {
                                                StringBuilder sb16 = new StringBuilder();
                                                sb16.append(str2);
                                                sb16.append(": ");
                                                str12 = sb16.toString();
                                            }
                                            sb15.append(str12);
                                            sb15.append(context.getResources().getString(C4558R.string.zm_msg_e2e_chatslist_decrypt_failed));
                                            str13 = sb15.toString();
                                        }
                                        mMChatsListItem.setLatestMessage(str13);
                                        break;
                                    default:
                                        switch (messageType) {
                                            case 20:
                                            case 21:
                                            case 22:
                                            case 23:
                                            case 24:
                                            case 25:
                                                CharSequence body = lastMessage.getBody();
                                                if (body != null) {
                                                    str13 = body.toString();
                                                }
                                                GroupAction loadFromString = GroupAction.loadFromString(str13);
                                                if (loadFromString != null) {
                                                    body = loadFromString.toMessage(context2);
                                                }
                                                mMChatsListItem.setLatestMessage(body);
                                                break;
                                            default:
                                                switch (messageType) {
                                                    case 50:
                                                    case 51:
                                                    case 52:
                                                    case 53:
                                                    case 54:
                                                        Resources resources = context.getResources();
                                                        if (resources != null) {
                                                            mMChatsListItem.setLatestMessage(resources.getString(C4558R.string.zm_mm_call_session_list_format, new Object[]{lastMessage.getBody()}));
                                                            break;
                                                        }
                                                        break;
                                                    case 55:
                                                        mMChatsListItem.setLatestMessage(context2.getString(C4558R.string.zm_msg_calling_out_54639));
                                                        break;
                                                    default:
                                                        switch (messageType) {
                                                            case 70:
                                                                mMChatsListItem.setLatestMessage(context2.getString(C4558R.string.zm_msg_e2e_get_invite, new Object[]{str2}));
                                                                break;
                                                            case 71:
                                                                mMChatsListItem.setLatestMessage(context2.getString(C4558R.string.zm_msg_e2e_invite_accepted, new Object[]{str2}));
                                                                break;
                                                            default:
                                                                mMChatsListItem.setLatestMessage("");
                                                                break;
                                                        }
                                                }
                                        }
                                }
                        }
                        if (!z4 || !isGroup2) {
                            str5 = context2.getString(C4558R.string.zm_mm_lbl_message_picture);
                        } else {
                            StringBuilder sb17 = new StringBuilder();
                            sb17.append(str2);
                            sb17.append(": ");
                            sb17.append(context2.getString(C4558R.string.zm_mm_lbl_message_picture));
                            str5 = sb17.toString();
                        }
                        if (lastMessage.isE2EMessage() && !mMChatsListItem.isMessageDecrypted(lastMessage.getMessageState())) {
                            StringBuilder sb32 = new StringBuilder();
                            if (!z4 || !isGroup2) {
                                str6 = "";
                            } else {
                                StringBuilder sb18 = new StringBuilder();
                                sb18.append(str2);
                                sb18.append(": ");
                                str6 = sb18.toString();
                            }
                            sb32.append(str6);
                            sb32.append(context.getResources().getString(C4558R.string.zm_msg_e2e_chatslist_decrypt_failed));
                            str5 = sb32.toString();
                        }
                        mMChatsListItem.setLatestMessage(str5);
                        break;
                }
            } else {
                mMChatsListItem.setLatestMessage(lastMessage.getBody());
            }
            if (lastMessage.IsDeletedThread()) {
                mMChatsListItem.setLatestMessage(context.getResources().getString(C4558R.string.zm_lbl_msg_deleted_thread_88133));
            }
        } else {
            if (z4) {
                str3 = context2.getString(C4558R.string.zm_msg_unsupport_message_13802);
            } else {
                StringBuilder sb19 = new StringBuilder();
                sb19.append(str2);
                sb19.append(": ");
                sb19.append(context2.getString(C4558R.string.zm_msg_unsupport_message_13802));
                str3 = sb19.toString();
            }
            mMChatsListItem.setLatestMessage(str3);
        }
        if (zoomChatSession.hasUnreadMessageAtMe() || zoomChatSession.hasUnreadedMessageAtAllMembers()) {
            mMChatsListItem.setAt(context2.getString(zoomChatSession.hasUnreadMessageAtMe() ? C4558R.string.zm_mm_msg_at_me_104608 : C4558R.string.zm_mm_msg_at_all_104608));
        }
        return mMChatsListItem;
    }

    public boolean isStarredMyNotes() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        if (zoomMessenger == null) {
            return false;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return false;
        }
        if (TextUtils.equals(myself.getJid(), this.sessionId) && zoomMessenger.isStarSession(this.sessionId)) {
            z = true;
        }
        return z;
    }

    public boolean hasDraftMessage() {
        return this.draftTimeStamp > 0 && !TextUtils.isEmpty(this.draftMessage);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String str) {
        this.sessionId = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String str) {
        this.avatar = str;
    }

    public CharSequence getLatestMessage() {
        return this.latestMessage;
    }

    public void setLatestMessage(CharSequence charSequence) {
        this.latestMessage = charSequence;
    }

    public boolean isGroup() {
        return this.isGroup;
    }

    public void setIsGroup(boolean z) {
        this.isGroup = z;
    }

    public boolean calculateMatchScore(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        if (StringUtil.isEmptyOrNull(this.title)) {
            this.matchScore = 2048;
            return false;
        }
        String lowerCase2 = this.title.toLowerCase();
        if (this.isGroup) {
            int i = 2048;
            for (String trim : lowerCase2.split(PreferencesConstants.COOKIE_DELIMITER)) {
                int indexOf = trim.trim().indexOf(lowerCase);
                int i2 = indexOf > -1 ? indexOf == 0 ? 0 : indexOf + 1 : 2048;
                if (i2 < i) {
                    i = i2;
                }
            }
            for (String trim2 : lowerCase2.split("&")) {
                int indexOf2 = trim2.trim().indexOf(lowerCase);
                int i3 = indexOf2 > -1 ? indexOf2 == 0 ? 0 : indexOf2 + 1 : 2048;
                if (i3 < i) {
                    i = i3;
                }
            }
            this.matchScore = i;
            if (i != 2048) {
                return true;
            }
        } else {
            int indexOf3 = lowerCase2.indexOf(lowerCase);
            if (indexOf3 > -1) {
                if (indexOf3 == 0) {
                    this.matchScore = 0;
                } else {
                    this.matchScore = indexOf3 + 1;
                }
                return true;
            }
            this.matchScore = 2048;
        }
        return false;
    }

    public int getMatchScore() {
        return this.matchScore;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long j) {
        this.timeStamp = j;
    }

    public long getDraftTimeStamp() {
        return this.draftTimeStamp;
    }

    public void setDraftTimeStamp(long j) {
        this.draftTimeStamp = j;
    }

    public CharSequence getDraftMessage() {
        return this.draftMessage;
    }

    public void setDraftMessage(CharSequence charSequence) {
        this.draftMessage = charSequence;
    }

    public int getUnreadMessageCount() {
        return this.unreadMessageCount;
    }

    public void setUnreadMessageCount(int i) {
        this.unreadMessageCount = i;
    }

    public IMAddrBookItem getFromContact() {
        return this.fromContact;
    }

    public void setFromContact(IMAddrBookItem iMAddrBookItem) {
        this.fromContact = iMAddrBookItem;
    }

    public int getThreadSortType() {
        return this.mThreadSortType;
    }

    public void setThreadSortType(int i) {
        this.mThreadSortType = i;
    }

    public boolean isBuddyWithPhoneNumberInSession(@Nullable String str) {
        if (str == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        if (this.isGroup) {
            ZoomGroup groupById = zoomMessenger.getGroupById(this.sessionId);
            if (groupById == null) {
                return false;
            }
            int buddyCount = groupById.getBuddyCount();
            for (int i = 0; i < buddyCount; i++) {
                ZoomBuddy buddyAt = groupById.getBuddyAt(i);
                if (buddyAt != null && StringUtil.isSameString(str, buddyAt.getPhoneNumber())) {
                    return true;
                }
            }
            return false;
        }
        ZoomBuddy buddyWithPhoneNumber = zoomMessenger.getBuddyWithPhoneNumber(str);
        if (buddyWithPhoneNumber == null) {
            return false;
        }
        return StringUtil.isSameString(str, buddyWithPhoneNumber.getPhoneNumber());
    }

    public boolean isBuddyWithJIDInSession(@Nullable String str) {
        if (str == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        if (this.isGroup) {
            return zoomMessenger.isBuddyWithJIDInGroup(str, this.sessionId);
        }
        return StringUtil.isSameString(this.sessionId, str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x0172  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0175  */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View getView(@androidx.annotation.Nullable android.content.Context r22, @androidx.annotation.Nullable android.view.View r23, android.view.ViewGroup r24) {
        /*
            r21 = this;
            r0 = r21
            r1 = r22
            r2 = 0
            if (r1 != 0) goto L_0x0008
            return r2
        L_0x0008:
            r3 = 0
            if (r23 == 0) goto L_0x001a
            java.lang.String r4 = "MMChatsListItem"
            java.lang.Object r5 = r23.getTag()
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L_0x001a
            r4 = r23
            goto L_0x002e
        L_0x001a:
            android.view.LayoutInflater r4 = android.view.LayoutInflater.from(r22)
            if (r4 != 0) goto L_0x0021
            return r2
        L_0x0021:
            int r5 = p021us.zoom.videomeetings.C4558R.layout.zm_mm_chats_list_item
            r6 = r24
            android.view.View r4 = r4.inflate(r5, r6, r3)
            java.lang.String r5 = "MMChatsListItem"
            r4.setTag(r5)
        L_0x002e:
            com.zipow.videobox.ptapp.PTApp r5 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r5 = r5.getZoomMessenger()
            if (r5 != 0) goto L_0x0039
            return r4
        L_0x0039:
            int r6 = p021us.zoom.videomeetings.C4558R.C4560id.avatarView
            android.view.View r6 = r4.findViewById(r6)
            com.zipow.videobox.view.AvatarView r6 = (com.zipow.videobox.view.AvatarView) r6
            int r7 = p021us.zoom.videomeetings.C4558R.C4560id.txtTitle
            android.view.View r7 = r4.findViewById(r7)
            us.zoom.androidlib.widget.ZMEllipsisTextView r7 = (p021us.zoom.androidlib.widget.ZMEllipsisTextView) r7
            int r8 = p021us.zoom.videomeetings.C4558R.C4560id.txtMessage
            android.view.View r8 = r4.findViewById(r8)
            android.widget.TextView r8 = (android.widget.TextView) r8
            int r9 = p021us.zoom.videomeetings.C4558R.C4560id.txtTime
            android.view.View r9 = r4.findViewById(r9)
            android.widget.TextView r9 = (android.widget.TextView) r9
            int r10 = p021us.zoom.videomeetings.C4558R.C4560id.txtNoteBubble
            android.view.View r10 = r4.findViewById(r10)
            android.widget.TextView r10 = (android.widget.TextView) r10
            int r11 = p021us.zoom.videomeetings.C4558R.C4560id.imgPresence
            android.view.View r11 = r4.findViewById(r11)
            com.zipow.videobox.view.PresenceStateView r11 = (com.zipow.videobox.view.PresenceStateView) r11
            int r12 = p021us.zoom.videomeetings.C4558R.C4560id.imgE2EFlag
            android.view.View r12 = r4.findViewById(r12)
            android.widget.ImageView r12 = (android.widget.ImageView) r12
            int r13 = p021us.zoom.videomeetings.C4558R.C4560id.imgBell
            android.view.View r13 = r4.findViewById(r13)
            android.widget.ImageView r13 = (android.widget.ImageView) r13
            int r14 = p021us.zoom.videomeetings.C4558R.C4560id.unreadBubble
            android.view.View r14 = r4.findViewById(r14)
            int r15 = p021us.zoom.videomeetings.C4558R.C4560id.imgErrorMessage
            android.view.View r15 = r4.findViewById(r15)
            android.widget.ImageView r15 = (android.widget.ImageView) r15
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.txtAt
            android.view.View r3 = r4.findViewById(r3)
            android.widget.TextView r3 = (android.widget.TextView) r3
            java.lang.String r2 = r0.sessionId
            boolean r2 = com.zipow.videobox.util.UIMgr.isMyNotes(r2)
            r23 = r15
            int r15 = r0.mThreadSortType
            r24 = r13
            if (r15 != 0) goto L_0x00a7
            java.lang.String r15 = r0.sessionId
            boolean r15 = r5.hasFailedMessage(r15)
            if (r15 == 0) goto L_0x00a7
            r15 = 1
            goto L_0x00a8
        L_0x00a7:
            r15 = 0
        L_0x00a8:
            java.lang.String r13 = r5.getContactRequestsSessionID()
            if (r6 == 0) goto L_0x010c
            r17 = r12
            java.lang.String r12 = r0.sessionId
            boolean r12 = p021us.zoom.androidlib.util.StringUtil.isSameString(r13, r12)
            if (r12 == 0) goto L_0x00c9
            com.zipow.videobox.view.AvatarView$ParamsBuilder r12 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r12.<init>()
            r18 = r11
            int r11 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_im_contact_request
            r19 = r5
            r5 = 0
            com.zipow.videobox.view.AvatarView$ParamsBuilder r5 = r12.setResource(r11, r5)
            goto L_0x0108
        L_0x00c9:
            r19 = r5
            r18 = r11
            boolean r5 = r0.isGroup
            if (r5 != 0) goto L_0x00e0
            com.zipow.videobox.view.IMAddrBookItem r5 = r21.getFromContact()
            if (r5 == 0) goto L_0x00e0
            com.zipow.videobox.view.IMAddrBookItem r5 = r21.getFromContact()
            com.zipow.videobox.view.AvatarView$ParamsBuilder r5 = r5.getAvatarParamsBuilder()
            goto L_0x0108
        L_0x00e0:
            boolean r5 = r0.isGroup
            if (r5 == 0) goto L_0x0106
            java.lang.String r5 = r0.sessionId
            boolean r5 = r0.isAnnounceMent(r5)
            if (r5 == 0) goto L_0x00f9
            com.zipow.videobox.view.AvatarView$ParamsBuilder r5 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r5.<init>()
            int r11 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_announcement
            r12 = 0
            com.zipow.videobox.view.AvatarView$ParamsBuilder r5 = r5.setResource(r11, r12)
            goto L_0x0108
        L_0x00f9:
            r12 = 0
            com.zipow.videobox.view.AvatarView$ParamsBuilder r5 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r5.<init>()
            int r11 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_avatar_group
            com.zipow.videobox.view.AvatarView$ParamsBuilder r5 = r5.setResource(r11, r12)
            goto L_0x0108
        L_0x0106:
            r12 = 0
            r5 = r12
        L_0x0108:
            r6.show(r5)
            goto L_0x0112
        L_0x010c:
            r19 = r5
            r18 = r11
            r17 = r12
        L_0x0112:
            if (r7 == 0) goto L_0x01b7
            java.lang.String r6 = r0.title
            if (r6 == 0) goto L_0x01b7
            java.lang.String r6 = r0.sessionId
            boolean r6 = r0.isAnnounceMent(r6)
            if (r6 == 0) goto L_0x0133
            android.text.TextUtils$TruncateAt r6 = android.text.TextUtils.TruncateAt.MIDDLE
            r7.setEllipsize(r6)
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_msg_announcements_108966
            java.lang.String r6 = r1.getString(r6)
            r7.setText(r6)
            r20 = r2
            r2 = 0
            goto L_0x01ba
        L_0x0133:
            if (r2 == 0) goto L_0x0151
            android.text.TextUtils$TruncateAt r6 = android.text.TextUtils.TruncateAt.MIDDLE
            r7.setEllipsize(r6)
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_msg_my_notes_65147
            r11 = 1
            java.lang.Object[] r12 = new java.lang.Object[r11]
            java.lang.String r5 = r0.title
            r16 = 0
            r12[r16] = r5
            java.lang.String r5 = r1.getString(r6, r12)
            r7.setText(r5)
            r20 = r2
            r2 = 0
            goto L_0x01ba
        L_0x0151:
            r11 = 1
            boolean r5 = r0.isGroup
            if (r5 != 0) goto L_0x018a
            com.zipow.videobox.view.IMAddrBookItem r5 = r21.getFromContact()
            if (r5 == 0) goto L_0x016f
            int r6 = r5.getAccountStatus()
            if (r6 != r11) goto L_0x0165
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_deactivated_62074
            goto L_0x0170
        L_0x0165:
            int r5 = r5.getAccountStatus()
            r6 = 2
            if (r5 != r6) goto L_0x016f
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_terminated_62074
            goto L_0x0170
        L_0x016f:
            r5 = 0
        L_0x0170:
            if (r5 != 0) goto L_0x0175
            java.lang.String r6 = r0.title
            goto L_0x0186
        L_0x0175:
            android.content.res.Resources r6 = r22.getResources()
            r11 = 1
            java.lang.Object[] r12 = new java.lang.Object[r11]
            java.lang.String r11 = r0.title
            r16 = 0
            r12[r16] = r11
            java.lang.String r6 = r6.getString(r5, r12)
        L_0x0186:
            r20 = r2
            r2 = 0
            goto L_0x01ae
        L_0x018a:
            java.lang.String r5 = r0.sessionId
            boolean r5 = android.text.TextUtils.equals(r13, r5)
            if (r5 == 0) goto L_0x0199
            java.lang.String r6 = r0.title
            r20 = r2
            r2 = 0
            r5 = 0
            goto L_0x01ae
        L_0x0199:
            android.content.res.Resources r5 = r22.getResources()
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_accessibility_group_pre_77383
            r11 = 1
            java.lang.Object[] r12 = new java.lang.Object[r11]
            java.lang.String r11 = r0.title
            r20 = r2
            r2 = 0
            r12[r2] = r11
            java.lang.String r6 = r5.getString(r6, r12)
            r5 = 0
        L_0x01ae:
            java.lang.String r11 = r0.title
            r7.setEllipsisText(r11, r5)
            r7.setContentDescription(r6)
            goto L_0x01ba
        L_0x01b7:
            r20 = r2
            r2 = 0
        L_0x01ba:
            if (r8 == 0) goto L_0x01d5
            r8.setVisibility(r2)
            boolean r2 = r21.hasDraftMessage()
            if (r2 == 0) goto L_0x01cb
            java.lang.CharSequence r2 = r0.draftMessage
            r8.setText(r2)
            goto L_0x01d5
        L_0x01cb:
            java.lang.CharSequence r2 = r0.latestMessage
            if (r2 == 0) goto L_0x01d0
            goto L_0x01d2
        L_0x01d0:
            java.lang.String r2 = ""
        L_0x01d2:
            r8.setText(r2)
        L_0x01d5:
            r2 = 8
            if (r14 == 0) goto L_0x0208
            if (r15 != 0) goto L_0x0205
            int r5 = r0.unreadMessageCountBySetting
            if (r5 != 0) goto L_0x0205
            int r5 = r0.unreadMessageCount
            if (r5 <= 0) goto L_0x0205
            int r5 = r0.markUnreadMessageCount
            if (r5 > 0) goto L_0x0205
            boolean r5 = r0.isGroup
            if (r5 == 0) goto L_0x0205
            java.lang.String r5 = r0.sessionId
            boolean r5 = android.text.TextUtils.equals(r13, r5)
            if (r5 != 0) goto L_0x0205
            android.content.res.Resources r5 = r22.getResources()
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_new_message_14491
            java.lang.String r5 = r5.getString(r6)
            r14.setContentDescription(r5)
            r5 = 0
            r14.setVisibility(r5)
            goto L_0x0208
        L_0x0205:
            r14.setVisibility(r2)
        L_0x0208:
            if (r10 == 0) goto L_0x0256
            boolean r5 = r0.isGroup
            if (r5 == 0) goto L_0x0211
            int r5 = r0.unreadMessageCountBySetting
            goto L_0x0213
        L_0x0211:
            int r5 = r0.unreadMessageCount
        L_0x0213:
            int r6 = r0.markUnreadMessageCount
            int r5 = r5 + r6
            java.lang.String r6 = r0.sessionId
            boolean r6 = android.text.TextUtils.equals(r13, r6)
            if (r6 == 0) goto L_0x0220
            int r5 = r0.unreadMessageCount
        L_0x0220:
            if (r15 != 0) goto L_0x0253
            if (r5 != 0) goto L_0x0225
            goto L_0x0253
        L_0x0225:
            r6 = 99
            if (r5 <= r6) goto L_0x022c
            java.lang.String r6 = "99+"
            goto L_0x0230
        L_0x022c:
            java.lang.String r6 = java.lang.String.valueOf(r5)
        L_0x0230:
            r10.setText(r6)
            r6 = 0
            r10.setVisibility(r6)
            android.content.res.Resources r8 = r22.getResources()
            int r11 = p021us.zoom.videomeetings.C4558R.plurals.zm_msg_notification_unread_num_8295
            r12 = 2
            java.lang.Object[] r12 = new java.lang.Object[r12]
            java.lang.String r13 = ""
            r12[r6] = r13
            java.lang.Integer r6 = java.lang.Integer.valueOf(r5)
            r13 = 1
            r12[r13] = r6
            java.lang.String r5 = r8.getQuantityString(r11, r5, r12)
            r10.setContentDescription(r5)
            goto L_0x0256
        L_0x0253:
            r10.setVisibility(r2)
        L_0x0256:
            if (r9 == 0) goto L_0x0279
            boolean r5 = r9.isInEditMode()
            if (r5 != 0) goto L_0x0279
            long r5 = r21.getTimeStamp()
            r10 = 0
            int r5 = (r5 > r10 ? 1 : (r5 == r10 ? 0 : -1))
            if (r5 <= 0) goto L_0x0274
            long r5 = r21.getTimeStamp()
            java.lang.String r1 = r0.formatTime(r1, r5)
            r9.setText(r1)
            goto L_0x0279
        L_0x0274:
            java.lang.String r1 = ""
            r9.setText(r1)
        L_0x0279:
            if (r3 == 0) goto L_0x0297
            java.lang.String r1 = r0.f342at
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r1 == 0) goto L_0x0286
            r1 = 8
            goto L_0x0287
        L_0x0286:
            r1 = 0
        L_0x0287:
            r3.setVisibility(r1)
            java.lang.String r1 = r0.f342at
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r1 != 0) goto L_0x0297
            java.lang.String r1 = r0.f342at
            r3.setText(r1)
        L_0x0297:
            boolean r1 = r0.isGroup
            if (r1 != 0) goto L_0x02ed
            com.zipow.videobox.view.IMAddrBookItem r1 = r21.getFromContact()
            if (r1 != 0) goto L_0x02a2
            return r4
        L_0x02a2:
            java.lang.String r3 = r1.getJid()
            r5 = r19
            com.zipow.videobox.ptapp.mm.ZoomBuddy r3 = r5.getBuddyWithJID(r3)
            if (r3 != 0) goto L_0x02af
            return r4
        L_0x02af:
            com.zipow.videobox.view.IMAddrBookItem r3 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r3)
            if (r20 == 0) goto L_0x02bd
            r11 = r18
            r11.setVisibility(r2)
            r12 = r17
            goto L_0x02cb
        L_0x02bd:
            r11 = r18
            r5 = 0
            r11.setVisibility(r5)
            r11.setState(r3)
            r11.setmTxtDeviceTypeGone()
            r12 = r17
        L_0x02cb:
            r12.setVisibility(r2)
            com.zipow.videobox.util.AlertWhenAvailableHelper r3 = com.zipow.videobox.util.AlertWhenAvailableHelper.getInstance()
            java.lang.String r1 = r1.getJid()
            boolean r1 = r3.isInAlertQueen(r1)
            boolean r3 = r0.isRoom
            r5 = 1
            r3 = r3 ^ r5
            r1 = r1 & r3
            if (r1 == 0) goto L_0x02e5
            r13 = r24
            r1 = 0
            goto L_0x02e9
        L_0x02e5:
            r13 = r24
            r1 = 8
        L_0x02e9:
            r13.setVisibility(r1)
            goto L_0x0304
        L_0x02ed:
            r13 = r24
            r12 = r17
            r11 = r18
            r13.setVisibility(r2)
            r11.setVisibility(r2)
            boolean r1 = r0.mIsE2E
            if (r1 == 0) goto L_0x02ff
            r1 = 0
            goto L_0x0301
        L_0x02ff:
            r1 = 8
        L_0x0301:
            r12.setVisibility(r1)
        L_0x0304:
            if (r7 == 0) goto L_0x0314
            boolean r1 = r0.isNotifyOff
            if (r1 == 0) goto L_0x030e
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_notifications_off
            r1 = 0
            goto L_0x0310
        L_0x030e:
            r1 = 0
            r3 = 0
        L_0x0310:
            r7.setCompoundDrawablesWithIntrinsicBounds(r1, r1, r3, r1)
            goto L_0x0315
        L_0x0314:
            r1 = 0
        L_0x0315:
            if (r15 == 0) goto L_0x031a
            r15 = r23
            goto L_0x031e
        L_0x031a:
            r15 = r23
            r1 = 8
        L_0x031e:
            r15.setVisibility(r1)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMChatsListItem.getView(android.content.Context, android.view.View, android.view.ViewGroup):android.view.View");
    }

    public boolean isAnnounceMent(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        if (zoomMessenger == null) {
            return false;
        }
        ZoomGroup groupById = zoomMessenger.getGroupById(str);
        if (groupById != null && groupById.isBroadcast()) {
            z = true;
        }
        return z;
    }

    public String formatTime(@NonNull Context context, long j) {
        long currentTimeMillis = System.currentTimeMillis();
        long j2 = currentTimeMillis - 86400000;
        if (TimeUtil.isSameDate(j, currentTimeMillis)) {
            return TimeUtil.formatTime(context, j);
        }
        if (TimeUtil.isSameDate(j, j2)) {
            return context.getString(C4558R.string.zm_lbl_yesterday);
        }
        return TimeUtil.formatDate(context, j);
    }

    public boolean isNotifyOff() {
        return this.isNotifyOff;
    }

    public void setNotifyOff(boolean z) {
        this.isNotifyOff = z;
    }

    public boolean isE2E() {
        return this.mIsE2E;
    }

    public void setE2E(boolean z) {
        this.mIsE2E = z;
    }

    public int getUnreadMessageCountBySetting() {
        return this.unreadMessageCountBySetting;
    }

    public void setUnreadMessageCountBySetting(int i) {
        this.unreadMessageCountBySetting = i;
    }

    public int getMarkUnreadMessageCount() {
        return this.markUnreadMessageCount;
    }

    public void setMarkUnreadMessageCount(int i) {
        this.markUnreadMessageCount = i;
    }

    public boolean isRoom() {
        return this.isRoom;
    }

    public void setRoom(boolean z) {
        this.isRoom = z;
    }

    public void setAt(String str) {
        this.f342at = str;
    }

    public String getAt() {
        return this.f342at;
    }

    public long getSearchOpenTimeStamp() {
        return this.searchOpenTimeStamp;
    }

    public void setSearchOpenTimeStamp(long j) {
        this.searchOpenTimeStamp = j;
    }
}

package com.zipow.videobox.view;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.ContactType;
import com.zipow.videobox.ptapp.IMProtos;
import com.zipow.videobox.ptapp.IMProtos.RobotCommand;
import com.zipow.videobox.ptapp.IMProtos.RobotCommandList;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ContactCloudSIP;
import com.zipow.videobox.ptapp.p013mm.ICloudSIPCallNumber;
import com.zipow.videobox.ptapp.p013mm.RoomDeviceInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.ContactsAvatarCache;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMBitmapFactory;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.sip.IMAddrPbxSearchItemView;
import com.zipow.videobox.view.sip.IMAddrSipItemView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMAddrBookItem implements Serializable, ISearchableItem {
    private static final int MAX_SCORE = 9999;
    private static final String PRE_BUDDY_IN_PHONE_CONTACTS = "IMAddrBookItem";
    private static final String TAG = "IMAddrBookItem";
    private static final long serialVersionUID = 1;
    @Nullable
    private String accountEmail = null;
    private int accountStatus = 0;
    @Nullable
    private String avatarPath = null;
    private String buddyPhoneNumber;
    private String cloudDefaultPhoneNo;
    private boolean cloudDefaultPhoneNoDirectedOnly;
    @Nullable
    private Contact contact;
    private int contactId = 0;
    private int contactType = -1;
    private String contactTypeString = "";
    @Nullable
    private String department;
    @NonNull
    private ArrayList<String> emails = new ArrayList<>();
    private String introduction;
    private boolean isDesktopOnline = false;
    private boolean isEmailLoaded = false;
    private boolean isFromWebSearch = false;
    private boolean isIMBlockedByIB = false;
    private boolean isManualInput;
    private boolean isMeetingBlockedByIB = false;
    private boolean isMobileOnline = false;
    private boolean isMyContact;
    private boolean isMyNote;
    private boolean isPending;
    private boolean isRobot;
    private boolean isRoomDevice;
    private boolean isZoomUser = false;
    private String jid = "";
    @Nullable
    private String jobTitle;
    private int lastMatchScore = MAX_SCORE;
    private Long lastMessageTime;
    @Nullable
    private String location;
    private ContactCloudSIP mICloudSIPCallNumber;
    private boolean mIsBlocked = false;
    private boolean mIsInit = false;
    private boolean mIsZoomRoomContact = false;
    private RoomDeviceInfo mRoomDevice;
    private int matchPriority;
    private int matchScore;
    private boolean needIndicateZoomUser = true;
    @NonNull
    private ArrayList<PhoneNumber> numbers = new ArrayList<>();
    private String personLink;
    private long pmi;
    private int presence = 0;
    private String profileCountryCode;
    private String profilePhoneNumber;
    private String robotCmdPrefix;
    private List<RobotCommand> robotCommands;
    @Nullable
    private String screenName = "";
    @Nullable
    private String signature;
    private String sipPhoneNumber;
    private String sortKey = "";

    static class PhoneNumber implements Serializable {
        private static final long serialVersionUID = 1;
        String normalizedNumber;
        String number;

        PhoneNumber() {
        }
    }

    private native boolean isPBXAccountImpl(String str);

    private native boolean isSIPAccountImpl(String str);

    private native boolean isSameCompanyImpl(String str, String str2);

    public IMAddrBookItem() {
    }

    public IMAddrBookItem(String str, @Nullable String str2, String str3, boolean z, boolean z2, boolean z3, @Nullable String str4, boolean z4, String str5) {
        this.jid = str;
        if (!StringUtil.isEmptyOrNull(str2)) {
            str2 = str2.trim();
        }
        this.screenName = str2;
        this.sortKey = SortUtil.getSortKey(str2, CompatUtils.getLocalDefault());
        this.buddyPhoneNumber = str3;
        this.isDesktopOnline = z2;
        this.isMobileOnline = z3;
        this.isMyContact = z;
        this.accountEmail = str4;
        this.mIsZoomRoomContact = z4;
        this.sipPhoneNumber = str5;
    }

    @Nullable
    public static IMAddrBookItem fromContact(@Nullable Contact contact2) {
        if (contact2 == null) {
            return null;
        }
        IMAddrBookItem iMAddrBookItem = new IMAddrBookItem();
        iMAddrBookItem.mIsInit = true;
        iMAddrBookItem.contact = contact2;
        iMAddrBookItem.screenName = contact2.displayName;
        iMAddrBookItem.contactId = contact2.contactId;
        StringBuilder sb = new StringBuilder();
        sb.append(PRE_BUDDY_IN_PHONE_CONTACTS);
        sb.append(iMAddrBookItem.contactId);
        iMAddrBookItem.jid = sb.toString();
        iMAddrBookItem.sortKey = contact2.sortKey;
        iMAddrBookItem.matchPriority = 1;
        return iMAddrBookItem;
    }

    public static IMAddrBookItem fromZoomBuddy(ZoomBuddy zoomBuddy) {
        IMAddrBookItem iMAddrBookItem = new IMAddrBookItem();
        if (iMAddrBookItem.init(zoomBuddy)) {
            return iMAddrBookItem;
        }
        return null;
    }

    public boolean isPropertyInit() {
        return this.mIsInit;
    }

    public boolean refreshBuddy() {
        this.mIsInit = false;
        String sortKey2 = getSortKey();
        init();
        return !StringUtil.isSameString(sortKey2, getSortKey());
    }

    private boolean init(@Nullable ZoomBuddy zoomBuddy) {
        String str;
        boolean z;
        ZoomBuddy zoomBuddy2 = zoomBuddy;
        if (zoomBuddy2 == null) {
            return false;
        }
        if (this.mIsInit) {
            return true;
        }
        ABContactsCache instance = ABContactsCache.getInstance();
        String jid2 = zoomBuddy.getJid();
        String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(zoomBuddy2, null);
        String phoneNumber = zoomBuddy.getPhoneNumber();
        String email = zoomBuddy.getEmail();
        if (!zoomBuddy.isPending() || zoomBuddy.isRobot()) {
            str = SortUtil.getSortKey(buddyDisplayName, CompatUtils.getLocalDefault());
        } else {
            str = email;
        }
        boolean isDesktopOnline2 = zoomBuddy.isDesktopOnline();
        boolean z2 = zoomBuddy.isMobileOnline() || zoomBuddy.isPadOnline();
        int presence2 = zoomBuddy.getPresence();
        String signature2 = zoomBuddy.getSignature();
        String localPicturePath = zoomBuddy.getLocalPicturePath();
        String department2 = zoomBuddy.getDepartment();
        String jobTitle2 = zoomBuddy.getJobTitle();
        String location2 = zoomBuddy.getLocation();
        Contact firstContactByPhoneNumber = instance.getFirstContactByPhoneNumber(phoneNumber);
        int i = firstContactByPhoneNumber != null ? firstContactByPhoneNumber.contactId : -1;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        String str2 = localPicturePath;
        boolean z3 = false;
        setContactId(i);
        setScreenName(buddyDisplayName);
        addPhoneNumber(phoneNumber, phoneNumber);
        setSortKey(str);
        setNeedIndicateZoomUser(false);
        setBuddyPhoneNumber(phoneNumber);
        setJid(jid2);
        setAccoutEmail(email);
        setIsZoomUser(true);
        setIsDesktopOnline(isDesktopOnline2);
        if (z2 || (i >= 0 && !isDesktopOnline2 && !zoomMessenger.isMyContact(jid2))) {
            z3 = true;
        }
        setIsMobileOnline(z3);
        setPresence(presence2);
        setSignature(signature2);
        setDepartment(department2);
        setJobTitle(jobTitle2);
        setLocation(location2);
        setAvatarPath(str2);
        setPending(zoomBuddy.isPending());
        setZoomRoomContact(zoomBuddy.isZoomRoom());
        setBlocked(zoomMessenger.blockUserIsBlocked(jid2));
        setPmi(zoomBuddy.getMeetingNumber());
        setPersonLink(zoomBuddy.getVanityUrl());
        setIsRobot(zoomBuddy.isRobot());
        setRobotCmdPrefix(zoomBuddy.getRobotCmdPrefix());
        setContactType(zoomBuddy.getBuddyType());
        setProfileCountryCode(zoomBuddy.getProfileCountryCode());
        setProfilePhoneNumber(zoomBuddy.getProfilePhoneNumber());
        setSipPhoneNumber(zoomBuddy.getSipPhoneNumber());
        setIsMyContact(zoomMessenger.isMyContact(jid2));
        setICloudSIPCallNumber(zoomBuddy.getCloudSIPCallNumber());
        setAccountStatus(zoomBuddy.getAccountStatus());
        setIntroduction(zoomBuddy.getIntroduction());
        setMyNote(UIMgr.isMyNotes(jid2));
        setIMBlockedByIB(zoomBuddy.isIMBlockedByIB());
        setMeetingBlockedByIB(zoomBuddy.isMeetingBlockedByIB());
        IMProtos.RoomDeviceInfo roomDeviceInfo = zoomBuddy.getRoomDeviceInfo();
        if (roomDeviceInfo != null) {
            setRoomDeviceInfo(roomDeviceInfo);
        }
        setIsRoomDevice(zoomBuddy.getIsRoomDevice());
        RobotCommandList robotCommands2 = zoomBuddy.getRobotCommands();
        if (robotCommands2 != null) {
            setRobotCommands(robotCommands2.getRobotCommandList());
            z = true;
        } else {
            z = true;
        }
        this.mIsInit = z;
        return z;
    }

    private void init() {
        if (!StringUtil.isEmptyOrNull(this.jid)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.jid);
                if (buddyWithJID != null) {
                    init(buddyWithJID);
                }
            }
        }
    }

    @Nullable
    public IMAddrBookItemView getView(Context context, View view, boolean z, boolean z2) {
        return getView(context, view, z, z2, 0);
    }

    @Nullable
    public IMAddrBookItemView getView(Context context, View view, boolean z, boolean z2, int i) {
        IMAddrBookItemView iMAddrBookItemView;
        if (view instanceof IMAddrBookItemView) {
            iMAddrBookItemView = (IMAddrBookItemView) view;
        } else {
            iMAddrBookItemView = new IMAddrBookItemView(context);
        }
        bindView(iMAddrBookItemView, z, z2, i);
        return iMAddrBookItemView;
    }

    @Nullable
    public IMAddrBookItemView getPBXSearchView(Context context, View view, boolean z, boolean z2, boolean z3, String str, int i) {
        IMAddrPbxSearchItemView iMAddrPbxSearchItemView;
        if (view instanceof IMAddrPbxSearchItemView) {
            iMAddrPbxSearchItemView = (IMAddrPbxSearchItemView) view;
        } else {
            iMAddrPbxSearchItemView = new IMAddrPbxSearchItemView(context);
        }
        iMAddrPbxSearchItemView.setAddrBookItem(this, str, this.needIndicateZoomUser, z, z2, z3, i);
        return iMAddrPbxSearchItemView;
    }

    @Nullable
    public IMAddrSipItemView getSipView(Context context, View view, boolean z, boolean z2) {
        IMAddrSipItemView iMAddrSipItemView;
        if (view instanceof IMAddrSipItemView) {
            iMAddrSipItemView = (IMAddrSipItemView) view;
        } else {
            iMAddrSipItemView = new IMAddrSipItemView(context);
        }
        bindView(iMAddrSipItemView, z, z2, 0);
        return iMAddrSipItemView;
    }

    private void bindView(IMAddrBookItemView iMAddrBookItemView, boolean z, boolean z2, int i) {
        iMAddrBookItemView.setAddrBookItem(this, this.needIndicateZoomUser, z, z2, i);
    }

    @Nullable
    public Contact getContact() {
        return this.contact;
    }

    public void setContact(@Nullable Contact contact2) {
        this.contact = contact2;
    }

    public boolean isMyContact() {
        return this.isMyContact;
    }

    public void setIsMyContact(boolean z) {
        this.isMyContact = z;
    }

    public void checkIsMyContact(@Nullable ZoomMessenger zoomMessenger) {
        if (zoomMessenger != null && !StringUtil.isEmptyOrNull(this.jid)) {
            this.isMyContact = zoomMessenger.isMyContact(this.jid);
        }
    }

    public void updatePresence(@Nullable ZoomMessenger zoomMessenger) {
        if (zoomMessenger != null && !StringUtil.isEmptyOrNull(this.jid)) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.jid);
            if (buddyWithJID != null) {
                setIsDesktopOnline(buddyWithJID.isDesktopOnline());
                setPresence(buddyWithJID.getPresence());
                setSignature(buddyWithJID.getSignature());
                boolean z = true;
                if (buddyWithJID.isMobileOnline()) {
                    setIsMobileOnline(true);
                } else {
                    if (this.contactId < 0 || this.isDesktopOnline || zoomMessenger.isMyContact(this.jid)) {
                        z = false;
                    }
                    setIsMobileOnline(z);
                }
            }
        }
    }

    public void updatePicture(@Nullable ZoomMessenger zoomMessenger) {
        if (zoomMessenger != null && !StringUtil.isEmptyOrNull(this.jid)) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.jid);
            if (buddyWithJID != null) {
                setAvatarPath(buddyWithJID.getLocalPicturePath());
            }
        }
    }

    @Nullable
    public String getScreenName() {
        return this.screenName;
    }

    public void setScreenName(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        this.screenName = str;
    }

    public boolean isManualInput() {
        return this.isManualInput;
    }

    public void setManualInput(boolean z) {
        this.isManualInput = z;
    }

    public void setSortKey(String str) {
        this.sortKey = str;
    }

    public String getSortKey() {
        return this.sortKey;
    }

    @Nullable
    public String getAccountEmail() {
        return this.accountEmail;
    }

    public void setAccoutEmail(String str) {
        this.accountEmail = str;
    }

    public int getPhoneNumberCount() {
        init();
        return this.numbers.size();
    }

    @Nullable
    public String getPhoneNumber(int i) {
        init();
        if (i < 0 || i > this.numbers.size()) {
            return null;
        }
        PhoneNumber phoneNumber = (PhoneNumber) this.numbers.get(i);
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.number;
    }

    @Nullable
    public String getNormalizedPhoneNumber(int i) {
        init();
        if (i < 0 || i >= this.numbers.size()) {
            return null;
        }
        PhoneNumber phoneNumber = (PhoneNumber) this.numbers.get(i);
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.normalizedNumber;
    }

    public String addPhoneNumber(String str, String str2, String str3) {
        if (StringUtil.isEmptyOrNull(str)) {
            return str;
        }
        if (StringUtil.isEmptyOrNull(str2)) {
            str2 = getNormalizedNumber(str, str3);
        }
        if (normalizedNumberExists(str2)) {
            return str2;
        }
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.number = str;
        phoneNumber.normalizedNumber = str2;
        this.numbers.add(phoneNumber);
        return str2;
    }

    public String addPhoneNumber(String str, String str2) {
        return addPhoneNumber(str, str2, null);
    }

    public void clearPhoneNumbers() {
        this.numbers.clear();
    }

    private boolean normalizedNumberExists(@Nullable String str) {
        if (str == null) {
            return false;
        }
        Iterator it = this.numbers.iterator();
        while (it.hasNext()) {
            PhoneNumber phoneNumber = (PhoneNumber) it.next();
            if (phoneNumber != null && str.equals(phoneNumber.normalizedNumber)) {
                return true;
            }
        }
        return false;
    }

    private String getNormalizedNumber(String str, String str2) {
        init();
        return PhoneNumberUtil.formatNumber(str, str2);
    }

    public boolean getIsZoomUser() {
        init();
        return this.isZoomUser;
    }

    public void setIsZoomUser(boolean z) {
        this.isZoomUser = z;
    }

    public int getContactId() {
        init();
        return this.contactId;
    }

    public void setContactId(int i) {
        this.contactId = i;
    }

    public int getEmailCount() {
        init();
        if (this.isEmailLoaded) {
            return this.emails.size();
        }
        return loadEmails();
    }

    @Nullable
    public String getEmail(int i) {
        init();
        if (i < 0 || i >= this.emails.size()) {
            return null;
        }
        return (String) this.emails.get(i);
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String str) {
        this.jid = str;
    }

    public void setNeedIndicateZoomUser(boolean z) {
        this.needIndicateZoomUser = z;
    }

    public boolean getNeedIndicateZoomUser() {
        return this.needIndicateZoomUser;
    }

    @Nullable
    public String getAvatarPath() {
        init();
        return this.avatarPath;
    }

    public ParamsBuilder getAvatarParamsBuilder() {
        ParamsBuilder paramsBuilder = new ParamsBuilder();
        if (isZoomRoomContact()) {
            paramsBuilder.setResource(C4558R.C4559drawable.zm_room_icon, getJid());
        } else if (getIsRoomDevice()) {
            paramsBuilder.setResource(C4558R.C4559drawable.zm_room_device_icon, getJid());
        } else {
            paramsBuilder.setName(getScreenName(), getJid()).setPath(getAvatarLocalPath());
        }
        return paramsBuilder;
    }

    private String getAvatarLocalPath() {
        if (!StringUtil.isEmptyOrNull(getAvatarPath())) {
            return getAvatarPath();
        }
        return ContactsAvatarCache.getInstance().getContactAvatarPath(getContactId());
    }

    public void setAvatarPath(@Nullable String str) {
        this.avatarPath = str;
    }

    public boolean getIsDesktopOnline() {
        return this.isDesktopOnline;
    }

    public void setIsDesktopOnline(boolean z) {
        this.isDesktopOnline = z;
    }

    public boolean getIsMobileOnline() {
        return this.isMobileOnline;
    }

    public void setIsMobileOnline(boolean z) {
        this.isMobileOnline = z;
    }

    public int getPresence() {
        init();
        return this.presence;
    }

    public void setPresence(int i) {
        this.presence = i;
    }

    @Nullable
    public String getSignature() {
        init();
        return this.signature;
    }

    public void setSignature(@Nullable String str) {
        if (str != null) {
            str = str.trim();
        }
        this.signature = str;
    }

    @Nullable
    public String getDepartment() {
        init();
        return this.department;
    }

    public void setDepartment(@Nullable String str) {
        if (str != null) {
            str = str.trim();
        }
        this.department = str;
    }

    @Nullable
    public String getJobTitle() {
        init();
        return this.jobTitle;
    }

    public void setJobTitle(@Nullable String str) {
        if (str != null) {
            str = str.trim();
        }
        this.jobTitle = str;
    }

    @Nullable
    public String getLocation() {
        init();
        return this.location;
    }

    public void setLocation(@Nullable String str) {
        if (str != null) {
            str = str.trim();
        }
        this.location = str;
    }

    public boolean isFromWebSearch() {
        init();
        return this.isFromWebSearch;
    }

    public void setIsFromWebSearch(boolean z) {
        this.isFromWebSearch = z;
    }

    private int loadEmails() {
        init();
        if (this.contactId == 0) {
            return 0;
        }
        String[] strArr = {String.valueOf(this.contactId)};
        Cursor query = VideoBoxApplication.getInstance().getContentResolver().query(Email.CONTENT_URI, new String[]{"_id", "data1", "data2"}, "contact_id = ?", strArr, null);
        if (query != null) {
            for (boolean moveToFirst = query.moveToFirst(); moveToFirst; moveToFirst = query.moveToNext()) {
                String string = query.getString(1);
                if (!StringUtil.isEmptyOrNull(string) && !emailExists(string)) {
                    this.emails.add(string);
                }
            }
            query.close();
        }
        this.isEmailLoaded = true;
        return this.emails.size();
    }

    private boolean emailExists(String str) {
        String lowerCase = str.toLowerCase(CompatUtils.getLocalDefault());
        Iterator it = this.emails.iterator();
        while (it.hasNext()) {
            if (((String) it.next()).toLowerCase(CompatUtils.getLocalDefault()).equals(lowerCase)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public Bitmap getAvatarBitmap(Context context) {
        init();
        return getAvatarBitmap(context, false);
    }

    @Nullable
    public Bitmap getAvatarBitmap(Context context, boolean z) {
        init();
        if (!StringUtil.isEmptyOrNull(this.avatarPath)) {
            Bitmap decodeFile = ZMBitmapFactory.decodeFile(this.avatarPath, z);
            if (decodeFile != null) {
                return decodeFile;
            }
        }
        return ContactsAvatarCache.getInstance().getContactAvatar(context, getContactId(), z);
    }

    public boolean equals(Object obj) {
        if (obj instanceof IMAddrBookItem) {
            return StringUtil.isSameString(this.jid, ((IMAddrBookItem) obj).jid);
        }
        return false;
    }

    public int hashCode() {
        String str = this.jid;
        if (str != null) {
            return str.hashCode();
        }
        return super.hashCode();
    }

    public boolean isFromPhoneContacts() {
        String str = this.jid;
        return (str == null || !str.startsWith(PRE_BUDDY_IN_PHONE_CONTACTS) || this.contact == null) ? false : true;
    }

    public boolean isSharedGlobalDirectory() {
        return getContactType() == 8;
    }

    public boolean isPending() {
        init();
        return this.isPending;
    }

    public void setPending(boolean z) {
        this.isPending = z;
    }

    public boolean isZoomRoomContact() {
        return this.mIsZoomRoomContact;
    }

    public void setZoomRoomContact(boolean z) {
        this.mIsZoomRoomContact = z;
    }

    public String getBuddyPhoneNumber() {
        init();
        return this.buddyPhoneNumber;
    }

    public void setBuddyPhoneNumber(String str) {
        this.buddyPhoneNumber = str;
    }

    public boolean isBlocked() {
        return this.mIsBlocked;
    }

    public void setBlocked(boolean z) {
        this.mIsBlocked = z;
    }

    public long getPmi() {
        return this.pmi;
    }

    public void setPmi(long j) {
        this.pmi = j;
    }

    public String getPersonLink() {
        return this.personLink;
    }

    public void setPersonLink(String str) {
        this.personLink = str;
    }

    public boolean getIsRobot() {
        return this.isRobot;
    }

    public void setIsRobot(boolean z) {
        this.isRobot = z;
    }

    public String getRobotCmdPrefix() {
        return this.robotCmdPrefix;
    }

    public void setRobotCmdPrefix(String str) {
        this.robotCmdPrefix = str;
    }

    public int getContactType() {
        return this.contactType;
    }

    public void setContactType(int i) {
        this.contactType = i;
    }

    public String getContactTypeString() {
        if (!StringUtil.isEmptyOrNull(this.contactTypeString)) {
            return this.contactTypeString;
        }
        switch (this.contactType) {
            case 4:
                this.contactTypeString = VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_pbx_search_receptionist_104213);
                break;
            case 5:
                this.contactTypeString = VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_pbx_search_common_area_104213);
                break;
            case 6:
                this.contactTypeString = VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_pbx_search_call_queue_104213);
                break;
            case 7:
                this.contactTypeString = VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_pbx_search_group_104213);
                break;
        }
        return this.contactTypeString;
    }

    public String getProfilePhoneNumber() {
        return this.profilePhoneNumber;
    }

    public void setProfilePhoneNumber(String str) {
        this.profilePhoneNumber = str;
    }

    public String getProfileCountryCode() {
        return this.profileCountryCode;
    }

    public void setProfileCountryCode(String str) {
        this.profileCountryCode = str;
    }

    public String getSipPhoneNumber() {
        return this.sipPhoneNumber;
    }

    public void setSipPhoneNumber(String str) {
        this.sipPhoneNumber = str;
    }

    public int getAccountStatus() {
        return this.accountStatus;
    }

    public void setAccountStatus(int i) {
        this.accountStatus = i;
    }

    public String getCloudDefaultPhoneNo(boolean z) {
        if (TextUtils.isEmpty(this.cloudDefaultPhoneNo) || z != this.cloudDefaultPhoneNoDirectedOnly) {
            refreshCloudDefaultPhoneNo(z);
        }
        return this.cloudDefaultPhoneNo;
    }

    public int getLastMatchScore() {
        return this.lastMatchScore;
    }

    public void setLastMatchScore(int i) {
        this.lastMatchScore = i;
    }

    private void refreshCloudDefaultPhoneNo(boolean z) {
        this.cloudDefaultPhoneNoDirectedOnly = z;
        if (CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
            ContactCloudSIP contactCloudSIP = this.mICloudSIPCallNumber;
            if (contactCloudSIP != null) {
                String extension = contactCloudSIP.getExtension();
                if (z || ((!isSameCompany(this.mICloudSIPCallNumber.getCompanyNumber()) && !isSharedGlobalDirectory()) || StringUtil.isEmptyOrNull(extension))) {
                    List externalCloudNumbers = getExternalCloudNumbers();
                    if (!CollectionsUtil.isListEmpty(externalCloudNumbers)) {
                        this.cloudDefaultPhoneNo = (String) externalCloudNumbers.get(0);
                    }
                } else {
                    this.cloudDefaultPhoneNo = extension;
                }
            }
        } else if (!z && CmmSIPCallManager.getInstance().isSipCallEnabled()) {
            this.cloudDefaultPhoneNo = getSipPhoneNumber();
        }
    }

    @Nullable
    public List<String> getExternalCloudNumbers() {
        if (this.mICloudSIPCallNumber == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList formattedDirectNumber = this.mICloudSIPCallNumber.getFormattedDirectNumber();
        if (!CollectionsUtil.isListEmpty(formattedDirectNumber)) {
            arrayList.addAll(formattedDirectNumber);
        }
        if (!StringUtil.isEmptyOrNull(this.buddyPhoneNumber)) {
            arrayList.add(this.buddyPhoneNumber);
        }
        if (StringUtil.isEmptyOrNull(this.profilePhoneNumber)) {
            return arrayList;
        }
        arrayList.add(this.profilePhoneNumber);
        return arrayList;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setIntroduction(String str) {
        this.introduction = str;
    }

    public void setCloudDefaultPhoneNo(String str) {
        this.cloudDefaultPhoneNo = str;
    }

    @Nullable
    public String getTitle() {
        return this.screenName;
    }

    public boolean calculateMatchScore(String str) {
        String str2;
        int i;
        this.lastMessageTime = null;
        if (StringUtil.isEmptyOrNull(str)) {
            return true;
        }
        String[] split = str.toLowerCase().split(OAuth.SCOPE_DELIMITER);
        if (split.length > 2) {
            return false;
        }
        String str3 = "";
        String str4 = "";
        if (!StringUtil.isEmptyOrNull(this.screenName)) {
            String[] split2 = this.screenName.split(OAuth.SCOPE_DELIMITER);
            str2 = split2[0];
            if (split2.length > 1) {
                str4 = split2[1];
            }
        } else {
            str2 = str3;
        }
        if (1 == split.length || str4.isEmpty()) {
            String str5 = split[0];
            if (str4.isEmpty() && 2 == split.length) {
                StringBuilder sb = new StringBuilder();
                sb.append(str5);
                sb.append(OAuth.SCOPE_DELIMITER);
                sb.append(split[1]);
                str5 = sb.toString();
            }
            if (!str2.isEmpty()) {
                String lowerCase = str2.toLowerCase();
                int indexOf = lowerCase.indexOf(str5);
                if (indexOf > -1) {
                    if (indexOf == 0) {
                        this.matchScore = 0;
                    } else {
                        this.matchScore = indexOf + 1;
                    }
                    return true;
                }
                i = lowerCase.length() + 1;
            } else {
                i = 0;
            }
            if (!str4.isEmpty()) {
                int indexOf2 = str4.toLowerCase().indexOf(str5);
                if (indexOf2 > -1) {
                    if (indexOf2 == 0) {
                        this.matchScore = 1;
                    } else {
                        this.matchScore = i + indexOf2;
                    }
                    return true;
                }
            }
            this.matchScore = MAX_SCORE;
            if (StringUtil.isEmptyOrNull(this.accountEmail) || this.accountEmail.toLowerCase().indexOf(str5) != 0) {
                this.matchScore = MAX_SCORE;
                return false;
            }
            if (str5.contains(".") || str5.contains("@")) {
                this.matchScore = 0;
            }
            return true;
        } else if (2 != split.length) {
            return false;
        } else {
            String str6 = split[0];
            String str7 = split[1];
            if (str2.toLowerCase().indexOf(str6) == -1) {
                this.matchScore = MAX_SCORE;
                return false;
            }
            int indexOf3 = str4.toLowerCase().indexOf(str7);
            if (indexOf3 != 0) {
                this.matchScore = MAX_SCORE;
                return false;
            }
            this.matchScore = indexOf3;
            return true;
        }
    }

    public int getMatchScore() {
        return this.matchScore;
    }

    public long getTimeStamp() {
        if (this.lastMessageTime == null) {
            if (!TextUtils.isEmpty(this.jid)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession findSessionById = zoomMessenger.findSessionById(this.jid);
                    if (findSessionById != null) {
                        ZoomMessage lastMessage = findSessionById.getLastMessage();
                        if (lastMessage != null) {
                            this.lastMessageTime = Long.valueOf(lastMessage.getStamp());
                            return this.lastMessageTime.longValue();
                        }
                    }
                }
            }
            this.lastMessageTime = Long.valueOf(0);
        }
        return this.lastMessageTime.longValue();
    }

    public int getPriority() {
        return this.matchPriority;
    }

    public ContactCloudSIP getICloudSIPCallNumber() {
        init();
        return this.mICloudSIPCallNumber;
    }

    public void setICloudSIPCallNumber(@Nullable ICloudSIPCallNumber iCloudSIPCallNumber) {
        if (iCloudSIPCallNumber != null) {
            this.mICloudSIPCallNumber = new ContactCloudSIP();
            this.mICloudSIPCallNumber.setDirectNumber(iCloudSIPCallNumber.getDirectNumber());
            this.mICloudSIPCallNumber.setFormattedDirectNumber(iCloudSIPCallNumber.getFormattedDirectNumber());
            this.mICloudSIPCallNumber.setCompanyNumber(iCloudSIPCallNumber.getCompanyNumber());
            this.mICloudSIPCallNumber.setExtension(iCloudSIPCallNumber.getExtension());
        }
    }

    @NonNull
    public LinkedHashSet<String> getPhoneCallNumbersForPBX() {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        ContactCloudSIP iCloudSIPCallNumber = getICloudSIPCallNumber();
        if (iCloudSIPCallNumber != null) {
            String companyNumber = iCloudSIPCallNumber.getCompanyNumber();
            String extension = iCloudSIPCallNumber.getExtension();
            if ((CmmSIPCallManager.getInstance().isSameCompanyWithLoginUser(companyNumber) || isSharedGlobalDirectory()) && !StringUtil.isEmptyOrNull(extension)) {
                linkedHashSet.add(extension);
            }
            ArrayList directNumber = iCloudSIPCallNumber.getDirectNumber();
            if (directNumber != null && !directNumber.isEmpty()) {
                linkedHashSet.addAll(directNumber);
            }
        }
        LinkedHashSet phoneCallNumbersFromContact = getPhoneCallNumbersFromContact();
        if (!phoneCallNumbersFromContact.isEmpty()) {
            linkedHashSet.addAll(phoneCallNumbersFromContact);
        }
        return linkedHashSet;
    }

    @NonNull
    public LinkedHashSet<String> getPhoneCallNumbersForPhoneContact() {
        return getPhoneCallNumbersFromContact();
    }

    @NonNull
    private LinkedHashSet<String> getPhoneCallNumbersFromContact() {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        if (!StringUtil.isEmptyOrNull(getProfilePhoneNumber())) {
            linkedHashSet.add(ZMPhoneUtils.formatPhoneNumber(getProfilePhoneNumber(), getProfileCountryCode(), "", true));
        }
        Contact contact2 = getContact();
        if (contact2 != null && !CollectionsUtil.isCollectionEmpty(contact2.accounts)) {
            Iterator it = contact2.accounts.iterator();
            while (it.hasNext()) {
                ContactType contactType2 = (ContactType) it.next();
                if (contactType2 != null && !CollectionsUtil.isCollectionEmpty(contactType2.phoneNumbers)) {
                    Iterator it2 = contactType2.phoneNumbers.iterator();
                    while (it2.hasNext()) {
                        linkedHashSet.add(((com.zipow.videobox.ptapp.ABContactsCache.Contact.PhoneNumber) it2.next()).getDisplayPhoneNumber());
                    }
                }
            }
        }
        return linkedHashSet;
    }

    public void removeItem(@Nullable Context context) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!zoomMessenger.isConnectionGood()) {
                if (context != null) {
                    Toast.makeText(context, C4558R.string.zm_mm_msg_cannot_remove_buddy_no_connection, 1).show();
                }
                return;
            }
            if (zoomMessenger.canRemoveBuddy(this.jid)) {
                zoomMessenger.updateAutoAnswerGroupBuddy(this.jid, false);
                zoomMessenger.removeBuddy(this.jid, null);
            }
        }
    }

    public boolean isMyNote() {
        return this.isMyNote;
    }

    public void setMyNote(boolean z) {
        this.isMyNote = z;
    }

    public boolean isIMBlockedByIB() {
        return this.isIMBlockedByIB;
    }

    public void setIMBlockedByIB(boolean z) {
        this.isIMBlockedByIB = z;
    }

    public boolean isMeetingBlockedByIB() {
        return this.isMeetingBlockedByIB;
    }

    public void setMeetingBlockedByIB(boolean z) {
        this.isMeetingBlockedByIB = z;
    }

    public RoomDeviceInfo getRoomDeviceInfo() {
        init();
        return this.mRoomDevice;
    }

    public void setRoomDeviceInfo(@NonNull IMProtos.RoomDeviceInfo roomDeviceInfo) {
        this.mRoomDevice = new RoomDeviceInfo();
        this.mRoomDevice.setName(roomDeviceInfo.getName());
        this.mRoomDevice.setIp(roomDeviceInfo.getIp());
        this.mRoomDevice.setE164num(roomDeviceInfo.getE164Num());
        this.mRoomDevice.setDeviceType(roomDeviceInfo.getType());
        this.mRoomDevice.setEncrypt(roomDeviceInfo.getEncrypt());
    }

    public boolean getIsRoomDevice() {
        return this.isRoomDevice;
    }

    public void setIsRoomDevice(boolean z) {
        this.isRoomDevice = z;
    }

    public List<RobotCommand> getRobotCommands() {
        return this.robotCommands;
    }

    public void setRobotCommands(List<RobotCommand> list) {
        this.robotCommands = list;
    }

    public boolean isPBXAccount() {
        return isPBXAccountImpl(this.jid);
    }

    public boolean isSIPAccount() {
        return isSIPAccountImpl(this.jid);
    }

    public boolean isSameCompany(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return isSameCompanyImpl(this.jid, str);
    }
}

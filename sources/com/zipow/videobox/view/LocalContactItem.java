package com.zipow.videobox.view;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ContactsAvatarCache;
import com.zipow.videobox.util.ZMBitmapFactory;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class LocalContactItem implements Serializable {
    private static final String TAG = "LocalContactItem";
    private static final long serialVersionUID = 1;
    @Nullable
    private String accountEmail = null;
    @Nullable
    private String avatarPath = null;
    private int contactId = 0;
    @NonNull
    private ArrayList<String> emails = new ArrayList<>();
    private boolean isEmailLoaded = false;
    private boolean isZoomUser = false;
    private String jid = "";
    private boolean needIndicateZoomUser = true;
    @NonNull
    private ArrayList<PhoneNumber> numbers = new ArrayList<>();
    @Nullable
    private String screenName = "";
    private String sortKey = "";

    static class PhoneNumber implements Serializable {
        private static final long serialVersionUID = 1;
        String normalizedNumber;
        String number;

        PhoneNumber() {
        }
    }

    @Nullable
    public static LocalContactItem fromAddrBookContact(@Nullable Contact contact) {
        if (contact == null) {
            return null;
        }
        LocalContactItem localContactItem = new LocalContactItem();
        localContactItem.setScreenName(contact.displayName);
        localContactItem.addPhoneNumber(contact.number, contact.normalizedNumber);
        localContactItem.setContactId(contact.contactId);
        return localContactItem;
    }

    @Nullable
    public View getView(Context context, View view, InviteLocalContactsListView inviteLocalContactsListView, boolean z) {
        LocalContactItemView localContactItemView;
        if (view instanceof LocalContactItemView) {
            localContactItemView = (LocalContactItemView) view;
        } else {
            localContactItemView = new LocalContactItemView(context);
        }
        bindView(inviteLocalContactsListView, localContactItemView, z);
        return localContactItemView;
    }

    private void bindView(InviteLocalContactsListView inviteLocalContactsListView, LocalContactItemView localContactItemView, boolean z) {
        localContactItemView.setParentListView(inviteLocalContactsListView);
        localContactItemView.setLocalContactItem(this, this.needIndicateZoomUser, z);
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
        return this.numbers.size();
    }

    @Nullable
    public String getPhoneNumber(int i) {
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
        if (i < 0 || i > this.numbers.size()) {
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
        return PhoneNumberUtil.formatNumber(str, str2);
    }

    public boolean getIsZoomUser() {
        return this.isZoomUser;
    }

    public void setIsZoomUser(boolean z) {
        this.isZoomUser = z;
    }

    public int getContactId() {
        return this.contactId;
    }

    public void setContactId(int i) {
        this.contactId = i;
    }

    public int getEmailCount() {
        if (this.isEmailLoaded) {
            return this.emails.size();
        }
        return loadEmails();
    }

    @Nullable
    public String getEmail(int i) {
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

    @Nullable
    public String getAvatarPath() {
        return this.avatarPath;
    }

    public void setAvatarPath(@Nullable String str) {
        this.avatarPath = str;
    }

    private int loadEmails() {
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
        return getAvatarBitmap(context, false);
    }

    @Nullable
    public Bitmap getAvatarBitmap(Context context, boolean z) {
        if (!StringUtil.isEmptyOrNull(this.avatarPath)) {
            Bitmap decodeFile = ZMBitmapFactory.decodeFile(this.avatarPath, z);
            if (decodeFile != null) {
                return decodeFile;
            }
        }
        return ContactsAvatarCache.getInstance().getContactAvatar(context, getContactId(), z);
    }

    @NonNull
    public ParamsBuilder getAvatarParamsBuilder() {
        ParamsBuilder paramsBuilder = new ParamsBuilder();
        paramsBuilder.setName(getScreenName(), getJid()).setPath(getAvatar());
        if (getIsZoomUser()) {
            paramsBuilder.setResource(C4558R.C4559drawable.zm_room_icon, null);
        }
        return paramsBuilder;
    }

    @Nullable
    public String getAvatar() {
        if (getContactId() == 0) {
            return null;
        }
        if (getIsZoomUser()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                int phoneNumberCount = getPhoneNumberCount();
                for (int i = 0; i < phoneNumberCount; i++) {
                    ZoomBuddy buddyWithPhoneNumber = zoomMessenger.getBuddyWithPhoneNumber(getNormalizedPhoneNumber(i));
                    if (buddyWithPhoneNumber != null) {
                        String localPicturePath = buddyWithPhoneNumber.getLocalPicturePath();
                        if (!StringUtil.isEmptyOrNull(localPicturePath)) {
                            return localPicturePath;
                        }
                    }
                }
            }
        }
        if (!StringUtil.isEmptyOrNull(getAvatarPath())) {
            return getAvatarPath();
        }
        return ContactsAvatarCache.getInstance().getContactAvatarPath(getContactId());
    }
}

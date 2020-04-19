package com.zipow.videobox.view.sip.sms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.ContactType;
import com.zipow.videobox.ptapp.ABContactsCache.Contact.PhoneNumber;
import com.zipow.videobox.ptapp.PTAppProtos.CloudPBX;
import com.zipow.videobox.ptapp.p013mm.ContactCloudSIP;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.IMAddrBookItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class PBXMessageContact implements Serializable {
    private String displayName;
    private String displayPhoneNumber;
    private IMAddrBookItem item;
    private String numberType;
    private String phoneNumber;

    static PBXMessageContact fromProto(com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact pBXMessageContact) {
        IMAddrBookItem iMAddrBookItemByNumber = ZMPhoneSearchHelper.getInstance().getIMAddrBookItemByNumber(pBXMessageContact.getPhoneNumber());
        if (iMAddrBookItemByNumber != null) {
            return new PBXMessageContact(pBXMessageContact.getPhoneNumber(), getNumberType(iMAddrBookItemByNumber, pBXMessageContact.getPhoneNumber()), iMAddrBookItemByNumber);
        }
        PBXMessageContact pBXMessageContact2 = new PBXMessageContact(pBXMessageContact.getPhoneNumber());
        pBXMessageContact2.setDisplayName(pBXMessageContact.getDisplayName());
        return pBXMessageContact2;
    }

    public PBXMessageContact(@NonNull String str, @Nullable IMAddrBookItem iMAddrBookItem) {
        this(str, null, iMAddrBookItem);
    }

    public PBXMessageContact(@NonNull String str, @Nullable String str2, @Nullable IMAddrBookItem iMAddrBookItem) {
        this.phoneNumber = str;
        this.numberType = str2;
        this.item = iMAddrBookItem;
    }

    public PBXMessageContact(@NonNull String str) {
        this(str, null, null);
    }

    @NonNull
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(@NonNull String str) {
        this.phoneNumber = str;
    }

    @Nullable
    public IMAddrBookItem getItem() {
        return this.item;
    }

    public void setItem(@Nullable IMAddrBookItem iMAddrBookItem) {
        this.item = iMAddrBookItem;
    }

    public String getNumberType() {
        return this.numberType;
    }

    public void setNumberType(String str) {
        this.numberType = str;
    }

    public String getDisplayPhoneNumber() {
        if (this.displayPhoneNumber == null) {
            this.displayPhoneNumber = ZMPhoneUtils.formatPhoneNumber(this.phoneNumber, true);
        }
        return this.displayPhoneNumber;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    @Nullable
    private static String getNumberType(IMAddrBookItem iMAddrBookItem, String str) {
        String str2;
        String str3;
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        VideoBoxApplication nonNullInstance = VideoBoxApplication.getNonNullInstance();
        ContactCloudSIP iCloudSIPCallNumber = iMAddrBookItem.getICloudSIPCallNumber();
        if (CmmSIPCallManager.getInstance().isPBXActive() && iCloudSIPCallNumber != null) {
            ArrayList directNumber = iCloudSIPCallNumber.getDirectNumber();
            if (!CollectionsUtil.isCollectionEmpty(directNumber)) {
                for (int i = 0; i < directNumber.size(); i++) {
                    hashMap.put(ZMPhoneUtils.formatPhoneNumber((String) directNumber.get(i)), nonNullInstance.getString(C4558R.string.zm_title_direct_number_31439));
                }
            }
        }
        if (!StringUtil.isEmptyOrNull(iMAddrBookItem.getBuddyPhoneNumber())) {
            String str4 = "";
            CloudPBX cloudPBXInfo = CmmSIPCallManager.getInstance().getCloudPBXInfo();
            if (cloudPBXInfo != null) {
                str3 = cloudPBXInfo.getCountryCode();
                str2 = cloudPBXInfo.getAreaCode();
            } else {
                str2 = str4;
                str3 = CountryCodeUtil.isoCountryCode2PhoneCountryCode(CountryCodeUtil.getIsoCountryCode(nonNullInstance));
            }
            String formatPhoneNumber = ZMPhoneUtils.formatPhoneNumber(iMAddrBookItem.getBuddyPhoneNumber(), str3, str2, true);
            if (!StringUtil.isEmptyOrNull(formatPhoneNumber)) {
                hashMap.put(formatPhoneNumber, nonNullInstance.getString(C4558R.string.zm_lbl_mobile_phone_number_124795));
                arrayList.add(formatPhoneNumber);
            }
        }
        if (!StringUtil.isEmptyOrNull(iMAddrBookItem.getProfilePhoneNumber())) {
            String formatPhoneNumber2 = ZMPhoneUtils.formatPhoneNumber(iMAddrBookItem.getProfilePhoneNumber(), iMAddrBookItem.getProfileCountryCode(), "", true);
            if (!StringUtil.isEmptyOrNull(formatPhoneNumber2) && !arrayList.contains(formatPhoneNumber2)) {
                hashMap.put(formatPhoneNumber2, nonNullInstance.getString(arrayList.size() > 0 ? C4558R.string.zm_lbl_others_phone_number_124795 : C4558R.string.zm_lbl_web_phone_number_124795));
                arrayList.add(formatPhoneNumber2);
            }
        }
        if (iMAddrBookItem.getContact() == null) {
            iMAddrBookItem.setContact(ABContactsCache.getInstance().getFirstContactByPhoneNumber(iMAddrBookItem.getBuddyPhoneNumber()));
        }
        Contact contact = iMAddrBookItem.getContact();
        if (contact != null && !CollectionsUtil.isCollectionEmpty(contact.accounts)) {
            Iterator it = contact.accounts.iterator();
            while (it.hasNext()) {
                ContactType contactType = (ContactType) it.next();
                if (contactType != null && !CollectionsUtil.isCollectionEmpty(contactType.phoneNumbers)) {
                    Iterator it2 = contactType.phoneNumbers.iterator();
                    while (it2.hasNext()) {
                        PhoneNumber phoneNumber2 = (PhoneNumber) it2.next();
                        String displayPhoneNumber2 = phoneNumber2.getDisplayPhoneNumber();
                        if (!StringUtil.isEmptyOrNull(displayPhoneNumber2) && !arrayList.contains(displayPhoneNumber2)) {
                            hashMap.put(displayPhoneNumber2, phoneNumber2.getLabel());
                            arrayList.add(displayPhoneNumber2);
                        }
                    }
                }
            }
        }
        return (String) hashMap.get(ZMPhoneUtils.formatPhoneNumber(str, true));
    }
}

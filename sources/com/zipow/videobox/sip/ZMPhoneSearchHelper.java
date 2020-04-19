package com.zipow.videobox.sip;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.NumberMatchedBuddyItem;
import com.zipow.videobox.ptapp.PTAppProtos.NumberMatchedBuddyItemList;
import com.zipow.videobox.ptapp.p013mm.ICloudSIPCallNumber;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.IMAddrBookItem;
import p021us.zoom.androidlib.util.StringUtil;

public class ZMPhoneSearchHelper {
    private static final String TAG = ZMPhoneSearchHelper.class.getSimpleName();
    private static final ZMPhoneSearchHelper ourInstance = new ZMPhoneSearchHelper();

    public static class NumberMatchedItem {
        private NumberMatchedBuddyItem matchedBuddyItem;
        private String number;
        private Contact pabContact;

        public NumberMatchedItem(String str) {
            this.number = str;
        }

        public boolean hasMatched() {
            if (this.matchedBuddyItem == null) {
                Contact contact = this.pabContact;
                if (contact == null || contact == Contact.invalidInstance()) {
                    return false;
                }
            }
            return true;
        }

        public NumberMatchedBuddyItem getMatchedBuddyItem() {
            return this.matchedBuddyItem;
        }

        public void setMatchedBuddyItem(NumberMatchedBuddyItem numberMatchedBuddyItem) {
            this.matchedBuddyItem = numberMatchedBuddyItem;
        }

        public Contact getPabContact() {
            return this.pabContact;
        }

        public void setPabContact(Contact contact) {
            this.pabContact = contact;
        }

        public int getNumberType() {
            NumberMatchedBuddyItem numberMatchedBuddyItem = this.matchedBuddyItem;
            if (numberMatchedBuddyItem == null) {
                return 2;
            }
            int matchedType = numberMatchedBuddyItem.getMatchedType();
            if (matchedType == 2 || matchedType == 4) {
                return 1;
            }
            return 2;
        }

        public ZoomBuddy getZoomBuddy() {
            NumberMatchedBuddyItem numberMatchedBuddyItem = this.matchedBuddyItem;
            if (numberMatchedBuddyItem != null) {
                String jid = numberMatchedBuddyItem.getJid();
                if (!TextUtils.isEmpty(jid)) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        return zoomMessenger.getBuddyWithJID(jid);
                    }
                }
            }
            return null;
        }

        @Nullable
        public String getDisplayName() {
            if (this.matchedBuddyItem != null) {
                ZoomBuddy zoomBuddy = getZoomBuddy();
                if (zoomBuddy != null) {
                    String screenName = zoomBuddy.getScreenName();
                    if (!TextUtils.isEmpty(screenName)) {
                        return screenName.trim();
                    }
                }
            }
            Contact contact = this.pabContact;
            if (contact == null || StringUtil.isEmptyOrNull(contact.displayName)) {
                return null;
            }
            return this.pabContact.displayName.trim();
        }
    }

    public static ZMPhoneSearchHelper getInstance() {
        return ourInstance;
    }

    private ZMPhoneSearchHelper() {
    }

    public String getDisplayNameByNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        NumberMatchedItem searchByNumber = searchByNumber(str);
        return StringUtil.safeString(searchByNumber != null ? searchByNumber.getDisplayName() : null);
    }

    @Nullable
    public NumberMatchedItem searchByNumber(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int indexOf = str.indexOf("@");
        if (indexOf > -1) {
            str = str.substring(0, indexOf);
        }
        NumberMatchedItem numberMatchedItem = new NumberMatchedItem(str);
        if (!ZMPhoneUtils.isE164Format(str)) {
            NumberMatchedBuddyItem searchZoomBuddyByNumber = searchZoomBuddyByNumber(str);
            numberMatchedItem.setMatchedBuddyItem(searchZoomBuddyByNumber);
            if (searchZoomBuddyByNumber == null) {
                numberMatchedItem.setPabContact(getContactFromPABByNumber(str));
            }
        } else {
            Contact contactFromPABByNumber = getContactFromPABByNumber(str);
            numberMatchedItem.setPabContact(contactFromPABByNumber);
            if (contactFromPABByNumber == null) {
                numberMatchedItem.setMatchedBuddyItem(searchZoomBuddyByNumber(str));
            }
        }
        return numberMatchedItem;
    }

    private String getDisplayNameFromPABByNumber(String str) {
        Contact contactFromPABByNumber = getContactFromPABByNumber(str);
        if (contactFromPABByNumber == null || StringUtil.isEmptyOrNull(contactFromPABByNumber.displayName)) {
            return null;
        }
        return contactFromPABByNumber.displayName.trim();
    }

    private Contact getContactFromPABByNumber(String str) {
        Contact firstContactByPhoneNumber = ABContactsCache.getInstance().getFirstContactByPhoneNumber(str);
        if (firstContactByPhoneNumber != null) {
            StringUtil.isEmptyOrNull(firstContactByPhoneNumber.displayName);
        }
        return firstContactByPhoneNumber;
    }

    private String getDisplayNameFromIMContactByNumber(String str) {
        ZoomBuddy zoomBuddyByNumber = getZoomBuddyByNumber(str);
        if (zoomBuddyByNumber != null) {
            String screenName = zoomBuddyByNumber.getScreenName();
            if (!TextUtils.isEmpty(screenName)) {
                return screenName.trim();
            }
        }
        return null;
    }

    @Nullable
    public IMAddrBookItem getIMAddrBookItemByNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        NumberMatchedItem searchByNumber = searchByNumber(str);
        if (searchByNumber == null || !searchByNumber.hasMatched()) {
            return null;
        }
        ZoomBuddy zoomBuddy = searchByNumber.getZoomBuddy();
        if (zoomBuddy != null) {
            return IMAddrBookItem.fromZoomBuddy(zoomBuddy);
        }
        Contact pabContact = searchByNumber.getPabContact();
        if (pabContact != null) {
            return IMAddrBookItem.fromContact(pabContact);
        }
        return null;
    }

    @Nullable
    public ZoomBuddy getZoomBuddyByNumber(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            NumberMatchedBuddyItem searchZoomBuddyByNumber = searchZoomBuddyByNumber(str);
            if (searchZoomBuddyByNumber != null) {
                String jid = searchZoomBuddyByNumber.getJid();
                if (!TextUtils.isEmpty(jid)) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        return zoomMessenger.getBuddyWithJID(jid);
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public NumberMatchedBuddyItem searchZoomBuddyByNumber(String str) {
        NumberMatchedBuddyItem numberMatchedBuddyItem = null;
        if (!StringUtil.isEmptyOrNull(str)) {
            int indexOf = str.indexOf("@");
            if (indexOf > -1) {
                str = str.substring(0, indexOf);
            }
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                boolean isCloudPBXEnabled = instance.isCloudPBXEnabled();
                boolean isSipCallEnabled = instance.isSipCallEnabled();
                if (isCloudPBXEnabled && ZMPhoneUtils.isE164Format(str)) {
                    str = ZMPhoneUtils.formatPhoneNumberAsE164(str);
                }
                NumberMatchedBuddyItemList buddyWithNumber = zoomMessenger.getBuddyWithNumber(str);
                if (buddyWithNumber != null && buddyWithNumber.getItemListCount() > 0) {
                    int itemListCount = buddyWithNumber.getItemListCount();
                    NumberMatchedBuddyItem numberMatchedBuddyItem2 = null;
                    NumberMatchedBuddyItem numberMatchedBuddyItem3 = null;
                    NumberMatchedBuddyItem numberMatchedBuddyItem4 = null;
                    NumberMatchedBuddyItem numberMatchedBuddyItem5 = null;
                    for (int i = 0; i < itemListCount; i++) {
                        NumberMatchedBuddyItem itemList = buddyWithNumber.getItemList(i);
                        if (itemList != null) {
                            switch (itemList.getMatchedType()) {
                                case 1:
                                case 5:
                                    if (numberMatchedBuddyItem5 != null) {
                                        break;
                                    } else {
                                        numberMatchedBuddyItem5 = itemList;
                                        break;
                                    }
                                case 2:
                                    if (numberMatchedBuddyItem3 != null) {
                                        break;
                                    } else {
                                        numberMatchedBuddyItem3 = itemList;
                                        break;
                                    }
                                case 3:
                                    if (numberMatchedBuddyItem4 != null) {
                                        break;
                                    } else {
                                        numberMatchedBuddyItem4 = itemList;
                                        break;
                                    }
                                case 4:
                                    if (numberMatchedBuddyItem2 != null) {
                                        break;
                                    } else {
                                        numberMatchedBuddyItem2 = itemList;
                                        break;
                                    }
                            }
                        }
                    }
                    if (isCloudPBXEnabled) {
                        numberMatchedBuddyItem = numberMatchedBuddyItem2 != null ? numberMatchedBuddyItem2 : numberMatchedBuddyItem4 != null ? numberMatchedBuddyItem4 : numberMatchedBuddyItem5;
                    } else if (isSipCallEnabled && numberMatchedBuddyItem3 != null) {
                        numberMatchedBuddyItem = numberMatchedBuddyItem3;
                    }
                }
                return numberMatchedBuddyItem;
            }
        }
        return null;
    }

    @Nullable
    public ZoomBuddy getSelfBuddyByNumber(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                ICloudSIPCallNumber cloudSIPCallNumber = myself.getCloudSIPCallNumber();
                if (cloudSIPCallNumber != null) {
                    if (StringUtil.isSameStringForNotAllowNull(str, cloudSIPCallNumber.getExtension()) || StringUtil.isSameStringForNotAllowNull(str, cloudSIPCallNumber.getCompanyNumber())) {
                        return myself;
                    }
                    if (cloudSIPCallNumber.getDirectNumber() != null) {
                        for (String isSameStringForNotAllowNull : cloudSIPCallNumber.getDirectNumber()) {
                            if (StringUtil.isSameStringForNotAllowNull(str, isSameStringForNotAllowNull)) {
                                return myself;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}

package com.zipow.videobox.ptapp.p013mm;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.view.IMAddrBookItem;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.BuddyNameUtil */
public class BuddyNameUtil {
    @NonNull
    public static String getBuddyDisplayName(ZoomBuddy zoomBuddy, IMAddrBookItem iMAddrBookItem) {
        return getBuddyDisplayName(zoomBuddy, iMAddrBookItem, false);
    }

    @NonNull
    public static String getBuddyDisplayName(@Nullable ZoomBuddy zoomBuddy, @Nullable IMAddrBookItem iMAddrBookItem, boolean z) {
        String screenName = zoomBuddy != null ? zoomBuddy.getScreenName() : null;
        if (StringUtil.isEmptyOrNull(screenName)) {
            if (iMAddrBookItem != null) {
                screenName = iMAddrBookItem.getScreenName();
                if (zoomBuddy != null && StringUtil.isEmptyOrNull(screenName)) {
                    String screenName2 = zoomBuddy.getScreenName();
                    if (StringUtil.isEmptyOrNull(screenName2)) {
                        screenName2 = zoomBuddy.getPhoneNumber();
                    }
                    screenName = StringUtil.isEmptyOrNull(screenName2) ? zoomBuddy.getEmail() : screenName2;
                    if (StringUtil.isEmptyOrNull(screenName)) {
                        screenName = zoomBuddy.getJid();
                    }
                }
            } else if (zoomBuddy != null) {
                String phoneNumber = zoomBuddy.getPhoneNumber();
                Contact firstContactByPhoneNumber = ABContactsCache.getInstance().getFirstContactByPhoneNumber(phoneNumber);
                if (firstContactByPhoneNumber != null) {
                    screenName = firstContactByPhoneNumber.displayName;
                } else {
                    if (screenName == null) {
                        screenName = zoomBuddy.getScreenName();
                    }
                    if (!StringUtil.isEmptyOrNull(screenName)) {
                        phoneNumber = screenName;
                    }
                    screenName = StringUtil.isEmptyOrNull(phoneNumber) ? zoomBuddy.getEmail() : phoneNumber;
                    if (StringUtil.isEmptyOrNull(screenName)) {
                        screenName = zoomBuddy.getJid();
                    }
                }
            }
        }
        return screenName == null ? "" : screenName;
    }

    @Nullable
    public static String getPedingDisplayName(@Nullable IMAddrBookItem iMAddrBookItem) {
        String str = "";
        if (iMAddrBookItem == null) {
            return str;
        }
        if (!iMAddrBookItem.isPending()) {
            return getBuddyDisplayName(null, iMAddrBookItem);
        }
        String accountEmail = iMAddrBookItem.getAccountEmail();
        if (TextUtils.isEmpty(accountEmail)) {
            accountEmail = iMAddrBookItem.getScreenName();
        }
        if (TextUtils.isEmpty(accountEmail)) {
            accountEmail = iMAddrBookItem.getJid();
        }
        return accountEmail;
    }

    @NonNull
    public static String getMyDisplayName(@Nullable ZoomBuddy zoomBuddy) {
        String str;
        if (zoomBuddy != null) {
            str = zoomBuddy.getScreenName();
            if (StringUtil.isEmptyOrNull(str)) {
                str = zoomBuddy.getPhoneNumber();
            }
            if (StringUtil.isEmptyOrNull(str)) {
                str = zoomBuddy.getEmail();
            }
            if (StringUtil.isEmptyOrNull(str)) {
                str = zoomBuddy.getJid();
            }
        } else {
            str = null;
        }
        return str == null ? "" : str;
    }
}

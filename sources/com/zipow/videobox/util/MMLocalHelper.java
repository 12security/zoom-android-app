package com.zipow.videobox.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.meeting.SelectAlterHostItem;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.MMSelectContactsListItemSpan;
import com.zipow.videobox.view.p014mm.MMSelectContactsListItem;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMEditText;

public class MMLocalHelper {
    public static int getMMSelectContactsListItemSpanCount(ZMEditText zMEditText) {
        Editable text = zMEditText.getText();
        int i = 0;
        if (text == null) {
            return 0;
        }
        MMSelectContactsListItemSpan[] mMSelectContactsListItemSpanArr = (MMSelectContactsListItemSpan[]) text.getSpans(0, text.length(), MMSelectContactsListItemSpan.class);
        if (mMSelectContactsListItemSpanArr != null) {
            i = mMSelectContactsListItemSpanArr.length;
        }
        return i;
    }

    public static void onSelected(@Nullable Context context, @NonNull ZMEditText zMEditText, boolean z, @Nullable MMSelectContactsListItem mMSelectContactsListItem) {
        String str;
        if (mMSelectContactsListItem != null && context != null) {
            Editable text = zMEditText.getText();
            int i = 0;
            MMSelectContactsListItemSpan[] mMSelectContactsListItemSpanArr = (MMSelectContactsListItemSpan[]) text.getSpans(0, text.length(), MMSelectContactsListItemSpan.class);
            MMSelectContactsListItemSpan mMSelectContactsListItemSpan = null;
            int length = mMSelectContactsListItemSpanArr.length;
            while (true) {
                if (i >= length) {
                    break;
                }
                MMSelectContactsListItemSpan mMSelectContactsListItemSpan2 = mMSelectContactsListItemSpanArr[i];
                MMSelectContactsListItem item = mMSelectContactsListItemSpan2.getItem();
                if (item != null && isSameMMSelectContactsListItem(item.isAlternativeHost(), item, mMSelectContactsListItem)) {
                    mMSelectContactsListItemSpan = mMSelectContactsListItemSpan2;
                    break;
                }
                i++;
            }
            if (z) {
                if (mMSelectContactsListItemSpan != null) {
                    mMSelectContactsListItemSpan.setItem(mMSelectContactsListItem);
                    return;
                }
                int length2 = mMSelectContactsListItemSpanArr.length;
                if (length2 > 0) {
                    int spanEnd = text.getSpanEnd(mMSelectContactsListItemSpanArr[length2 - 1]);
                    int length3 = text.length();
                    if (spanEnd < length3) {
                        text.delete(spanEnd, length3);
                    }
                } else {
                    text.clear();
                }
                MMSelectContactsListItemSpan mMSelectContactsListItemSpan3 = new MMSelectContactsListItemSpan(context, mMSelectContactsListItem);
                mMSelectContactsListItemSpan3.setInterval(UIUtil.dip2px(context, 2.0f));
                String screenName = mMSelectContactsListItem.getScreenName();
                if (!TextUtils.isEmpty(screenName)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(OAuth.SCOPE_DELIMITER);
                    sb.append(TextUtils.ellipsize(screenName, zMEditText.getPaint(), (float) UIUtil.dip2px(VideoBoxApplication.getGlobalContext(), 150.0f), TruncateAt.END));
                    sb.append(OAuth.SCOPE_DELIMITER);
                    str = sb.toString();
                } else {
                    str = "";
                }
                int length4 = text.length();
                int length5 = str.length() + length4;
                text.append(str);
                text.setSpan(mMSelectContactsListItemSpan3, length4, length5, 33);
                zMEditText.setSelection(length5);
                zMEditText.setCursorVisible(true);
            } else if (mMSelectContactsListItemSpan != null) {
                int spanStart = text.getSpanStart(mMSelectContactsListItemSpan);
                int spanEnd2 = text.getSpanEnd(mMSelectContactsListItemSpan);
                if (spanStart >= 0 && spanEnd2 >= 0 && spanEnd2 >= spanStart) {
                    text.delete(spanStart, spanEnd2);
                    text.removeSpan(mMSelectContactsListItemSpan);
                }
            }
        }
    }

    public static boolean searchBuddyByKey(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || !zoomMessenger.isConnectionGood()) {
            return false;
        }
        return zoomMessenger.searchBuddyByKey(str);
    }

    public static ZoomMessenger getGoodConnectedZoomMessenger() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || !zoomMessenger.isConnectionGood()) {
            return null;
        }
        return zoomMessenger;
    }

    @NonNull
    public static MMSelectContactsListItem transformSelectAlterHostToMMSelectContactsListItem(SelectAlterHostItem selectAlterHostItem) {
        IMAddrBookItem iMAddrBookItem = new IMAddrBookItem();
        iMAddrBookItem.setAccoutEmail(selectAlterHostItem.getEmail());
        iMAddrBookItem.setPmi(selectAlterHostItem.getPmi());
        iMAddrBookItem.setScreenName(selectAlterHostItem.getScreenName());
        iMAddrBookItem.setJid(selectAlterHostItem.getHostID());
        MMSelectContactsListItem mMSelectContactsListItem = new MMSelectContactsListItem(iMAddrBookItem);
        if (StringUtil.isEmptyOrNull(iMAddrBookItem.getAccountEmail())) {
            iMAddrBookItem.setAccoutEmail(selectAlterHostItem.getEmail());
        }
        if (StringUtil.isEmptyOrNull(iMAddrBookItem.getScreenName())) {
            iMAddrBookItem.setScreenName(selectAlterHostItem.getScreenName());
        }
        if (StringUtil.isEmptyOrNull(mMSelectContactsListItem.getEmail())) {
            mMSelectContactsListItem.setEmail(selectAlterHostItem.getEmail());
        }
        if (StringUtil.isEmptyOrNull(mMSelectContactsListItem.getScreenName())) {
            mMSelectContactsListItem.setScreenName(selectAlterHostItem.getScreenName());
        }
        mMSelectContactsListItem.setIsDisabled(false);
        mMSelectContactsListItem.setIsChecked(true);
        mMSelectContactsListItem.setAlternativeHost(true);
        return mMSelectContactsListItem;
    }

    @NonNull
    public static MMSelectContactsListItem transformEmailToMMSelectContactsListItem(String str) {
        IMAddrBookItem iMAddrBookItem = new IMAddrBookItem();
        iMAddrBookItem.setAccoutEmail(str);
        MMSelectContactsListItem mMSelectContactsListItem = new MMSelectContactsListItem(iMAddrBookItem);
        if (StringUtil.isEmptyOrNull(iMAddrBookItem.getAccountEmail())) {
            iMAddrBookItem.setAccoutEmail(str);
        }
        if (StringUtil.isEmptyOrNull(iMAddrBookItem.getScreenName())) {
            iMAddrBookItem.setScreenName(str);
        }
        if (StringUtil.isEmptyOrNull(mMSelectContactsListItem.getEmail())) {
            mMSelectContactsListItem.setEmail(str);
        }
        if (StringUtil.isEmptyOrNull(mMSelectContactsListItem.getScreenName())) {
            mMSelectContactsListItem.setScreenName(str);
        }
        mMSelectContactsListItem.setIsDisabled(false);
        mMSelectContactsListItem.setIsChecked(true);
        mMSelectContactsListItem.setAlternativeHost(true);
        return mMSelectContactsListItem;
    }

    public static boolean isSameMMSelectContactsListItem(boolean z, @NonNull MMSelectContactsListItem mMSelectContactsListItem, @NonNull MMSelectContactsListItem mMSelectContactsListItem2) {
        if (!z) {
            return StringUtil.isSameStringForNotAllowNull(mMSelectContactsListItem.getBuddyJid(), mMSelectContactsListItem2.getBuddyJid());
        }
        return StringUtil.isSameStringForNotAllowNull(mMSelectContactsListItem.getBuddyJid(), mMSelectContactsListItem2.getBuddyJid()) || StringUtil.isSameStringForNotAllowNull(mMSelectContactsListItem.getEmail(), mMSelectContactsListItem2.getEmail());
    }
}

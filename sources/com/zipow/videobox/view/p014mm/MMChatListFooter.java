package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.MMChatListFooterView;

/* renamed from: com.zipow.videobox.view.mm.MMChatListFooter */
public class MMChatListFooter {
    private String filter;
    private boolean isHideContent;
    private boolean onlyContact;

    public MMChatListFooter(String str, boolean z) {
        this(str, z, false);
    }

    public MMChatListFooter(String str, boolean z, boolean z2) {
        this.filter = str;
        this.onlyContact = z;
        this.isHideContent = z2;
    }

    @Nullable
    public View getView(Context context, View view, ViewGroup viewGroup) {
        MMChatListFooterView mMChatListFooterView;
        if (view instanceof MMChatListFooterView) {
            mMChatListFooterView = (MMChatListFooterView) view;
        } else {
            mMChatListFooterView = new MMChatListFooterView(context);
        }
        mMChatListFooterView.setSearchInclude(this.filter);
        mMChatListFooterView.setOnlyContact(this.onlyContact);
        mMChatListFooterView.setHideContact(this.isHideContent);
        return mMChatListFooterView;
    }
}

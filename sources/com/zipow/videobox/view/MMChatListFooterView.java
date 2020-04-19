package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.zipow.videobox.fragment.SystemNotificationFragment;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.view.p014mm.MMContentSearchFragment;
import com.zipow.videobox.view.p014mm.MMMessageSearchFragment;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class MMChatListFooterView extends LinearLayout {
    private View mContactRequestsItem;
    private View mContentsIncludeItem;
    private TextView mContentsSearchInclude;
    private View mMessageIncludeItem;
    private TextView mMessageSearchInclude;
    /* access modifiers changed from: private */
    public String mSearchInclude;

    @RequiresApi(api = 21)
    public MMChatListFooterView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView();
    }

    public MMChatListFooterView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MMChatListFooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMChatListFooterView(Context context) {
        super(context);
        initView();
    }

    public void setSearchInclude(@Nullable String str) {
        this.mSearchInclude = str;
        boolean z = true;
        int i = 0;
        this.mMessageSearchInclude.setText(getResources().getString(C4558R.string.zm_mm_msg_im_search_include_content_18680, new Object[]{str}));
        this.mContentsSearchInclude.setText(getResources().getString(C4558R.string.zm_mm_msg_im_search_include_content_18680, new Object[]{str}));
        if (StringUtil.isEmptyOrNull(str) || !getResources().getString(C4558R.string.zm_contact_requests_83123).toLowerCase().contains(str.toLowerCase())) {
            z = false;
        }
        View view = this.mContactRequestsItem;
        if (!z) {
            i = 8;
        }
        view.setVisibility(i);
    }

    public void setOnlyContact(boolean z) {
        if (z) {
            this.mMessageIncludeItem.setVisibility(8);
            this.mContentsIncludeItem.setVisibility(8);
        }
    }

    public void setHideContact(boolean z) {
        if (z) {
            this.mContentsIncludeItem.setVisibility(8);
        } else {
            this.mContentsIncludeItem.setVisibility(0);
        }
    }

    private void initView() {
        inflate(getContext(), C4558R.layout.zm_chat_list_footer, this);
        this.mMessageSearchInclude = (TextView) findViewById(C4558R.C4560id.zm_message_search_include);
        this.mContentsSearchInclude = (TextView) findViewById(C4558R.C4560id.zm_contents_search_include);
        this.mMessageIncludeItem = findViewById(C4558R.C4560id.panel_message_include);
        this.mContentsIncludeItem = findViewById(C4558R.C4560id.panel_contents_include);
        this.mContactRequestsItem = findViewById(C4558R.C4560id.panel_contact_requests);
        this.mMessageIncludeItem.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UIUtil.closeSoftKeyboard(MMChatListFooterView.this.getContext(), view);
                MMMessageSearchFragment.showAsFragment(MMChatListFooterView.this.getContext(), MMChatListFooterView.this.mSearchInclude);
            }
        });
        this.mContentsIncludeItem.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UIUtil.closeSoftKeyboard(MMChatListFooterView.this.getContext(), view);
                MMContentSearchFragment.showAsFragment(MMChatListFooterView.this.getContext(), false, MMChatListFooterView.this.mSearchInclude);
                ZoomLogEventTracking.eventTrackOpenSearchedContent();
            }
        });
        this.mContactRequestsItem.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ZMActivity zMActivity = (ZMActivity) MMChatListFooterView.this.getContext();
                if (zMActivity != null) {
                    SystemNotificationFragment.showAsActivity(zMActivity, 0);
                }
            }
        });
    }
}

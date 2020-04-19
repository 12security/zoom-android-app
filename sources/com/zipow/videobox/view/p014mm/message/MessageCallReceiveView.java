package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.p014mm.MMChatMessageBgDrawable;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageCallReceiveView */
public class MessageCallReceiveView extends MessageCallView {
    private LinearLayout contactLinear;
    private TextView contactName;
    private TextView groupContact;
    private LinearLayout groupLinear;
    private TextView groupName;
    private LinearLayout starredMsgTitleLinear;
    private TextView time;

    public MessageCallReceiveView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessageCallReceiveView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_call_receive, this);
        this.starredMsgTitleLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_title_linear);
        this.contactLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_linear);
        this.contactName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_name);
        this.groupLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_linear);
        this.groupContact = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_contact);
        this.groupName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_name);
        this.time = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_time);
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        super.setMessageItem(mMMessageItem);
        Resources resources = getResources();
        if (resources != null) {
            int i = C4558R.C4559drawable.zm_mm_msg_call_from;
            CharSequence charSequence = mMMessageItem.message;
            int i2 = mMMessageItem.messageType;
            if (i2 == 40) {
                charSequence = resources.getString(C4558R.string.zm_mm_unknow_call_35364);
            } else if (i2 != 43) {
                switch (i2) {
                    case 21:
                        charSequence = resources.getString(C4558R.string.zm_mm_miss_call);
                        i = C4558R.C4559drawable.zm_mm_msg_call_miss;
                        break;
                    case 22:
                        charSequence = resources.getString(C4558R.string.zm_mm_accepted_call_35364);
                        break;
                    case 23:
                        charSequence = resources.getString(C4558R.string.zm_mm_declined_call);
                        break;
                }
            } else {
                charSequence = resources.getString(C4558R.string.zm_mm_cancel_call_46218);
            }
            setMessage(charSequence);
            setCallTypeImage(i);
            setStarredMessage(mMMessageItem);
        }
    }

    public void setStarredMessage(@NonNull MMMessageItem mMMessageItem) {
        if (mMMessageItem.hideStarView) {
            this.mTxtMessage.setFocusable(false);
            this.mTxtMessage.setClickable(false);
            this.starredMsgTitleLinear.setVisibility(0);
            this.mTxtScreenName.setVisibility(8);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
                    if (sessionById != null) {
                        if (mMMessageItem.isGroupMessage) {
                            this.contactLinear.setVisibility(8);
                            this.groupLinear.setVisibility(0);
                            ZoomGroup sessionGroup = sessionById.getSessionGroup();
                            if (sessionGroup != null) {
                                this.groupName.setText(sessionGroup.getGroupName());
                            }
                        } else {
                            this.contactLinear.setVisibility(0);
                            this.groupLinear.setVisibility(8);
                            ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                            if (sessionBuddy != null) {
                                this.groupName.setText(BuddyNameUtil.getMyDisplayName(sessionBuddy));
                            } else if (TextUtils.equals(mMMessageItem.sessionId, myself.getJid())) {
                                this.groupName.setText(BuddyNameUtil.getMyDisplayName(myself));
                            }
                        }
                        this.time.setText(TimeUtil.formatDateTimeCap(getContext(), mMMessageItem.serverSideTime));
                        String string = StringUtil.isSameString(myself.getJid(), mMMessageItem.fromJid) ? getContext().getString(C4558R.string.zm_lbl_content_you) : mMMessageItem.fromScreenName;
                        this.groupContact.setText(string);
                        this.contactName.setText(string);
                    }
                }
            }
        } else {
            this.starredMsgTitleLinear.setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public Drawable getMesageBackgroudDrawable() {
        return new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, true);
    }
}

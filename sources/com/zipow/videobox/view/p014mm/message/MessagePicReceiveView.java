package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
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
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessagePicReceiveView */
public class MessagePicReceiveView extends MessagePicView {
    public static String TAG = "MessagePicReceiveView";
    private LinearLayout contactLinear;
    private TextView contactName;
    private TextView groupContact;
    private LinearLayout groupLinear;
    private TextView groupName;
    private LinearLayout starredMsgTitleLinear;
    private TextView time;
    private TextView txtStarDes;

    public MessagePicReceiveView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MessagePicReceiveView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessagePicReceiveView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public Drawable getMesageBackgroudDrawable() {
        return new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, true);
    }

    /* access modifiers changed from: protected */
    public Drawable getProgressBackgroudDrawable() {
        return new MMChatMessageBgDrawable(getContext(), 4, this.mMessageItem.onlyMessageShow, true);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_pic_receive, this);
        this.txtStarDes = (TextView) findViewById(C4558R.C4560id.txtStarDes);
    }

    /* access modifiers changed from: protected */
    public int[] getImgRadius() {
        int dip2px = UIUtil.dip2px(getContext(), 10.0f);
        return new int[]{dip2px, dip2px, dip2px, dip2px};
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        super.setMessageItem(mMMessageItem);
        if (mMMessageItem.fileDownloadResultCode != 5061) {
            setFailed(mMMessageItem.isPreviewDownloadFailed);
        }
        setStarredMessage(mMMessageItem);
    }

    public void setStarredMessage(@NonNull MMMessageItem mMMessageItem) {
        if (mMMessageItem.hideStarView) {
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
                        if (mMMessageItem.isComment) {
                            this.txtStarDes.setText(C4558R.string.zm_lbl_from_thread_88133);
                            this.txtStarDes.setVisibility(0);
                        } else if (mMMessageItem.commentsCount > 0) {
                            this.txtStarDes.setText(getResources().getQuantityString(C4558R.plurals.zm_lbl_comment_reply_title_88133, (int) mMMessageItem.commentsCount, new Object[]{Integer.valueOf((int) mMMessageItem.commentsCount)}));
                            this.txtStarDes.setVisibility(0);
                        } else {
                            this.txtStarDes.setVisibility(8);
                        }
                    }
                }
            }
        } else {
            this.starredMsgTitleLinear.setVisibility(8);
            this.txtStarDes.setVisibility(8);
        }
    }

    public void setFailed(boolean z) {
        setStatusImage(z, C4558R.C4559drawable.zm_mm_msg_state_fail);
    }

    /* access modifiers changed from: protected */
    public void initView() {
        super.initView();
        this.starredMsgTitleLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_title_linear);
        this.contactLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_linear);
        this.contactName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_name);
        this.groupLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_linear);
        this.groupContact = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_contact);
        this.groupName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_name);
        this.time = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_time);
    }
}

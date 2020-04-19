package com.zipow.videobox.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.util.TimeFormatUtil;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class ConfChatItemView extends LinearLayout {
    private static final String TAG = "com.zipow.videobox.view.ConfChatItemView";
    private AvatarView mAvatarView;
    private String mMessage;
    private TextView mTxtMessage;
    private TextView mTxtScreenName;
    private TextView mTxtTime;

    public static class ConfChatPrivateItemView extends ConfChatItemView {
        public ConfChatPrivateItemView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public ConfChatPrivateItemView(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void inflateLayout() {
            View.inflate(getContext(), C4558R.layout.zm_conf_chat_item_private, this);
        }
    }

    public static class ConfChatPublicItemView extends ConfChatItemView {
        public ConfChatPublicItemView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public ConfChatPublicItemView(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void inflateLayout() {
            View.inflate(getContext(), C4558R.layout.zm_conf_chat_item_public, this);
        }
    }

    static class MessageContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 0;

        public MessageContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public ConfChatItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ConfChatItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mTxtTime = (TextView) findViewById(C4558R.C4560id.txtTime);
        this.mTxtMessage = (TextView) findViewById(C4558R.C4560id.txtMessage);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mTxtMessage.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                ConfChatItemView.this.showMessageContextMenu();
                return true;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_conf_chat_item_public, this);
    }

    public void setScreenName(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            this.mTxtScreenName.setText(charSequence);
        }
    }

    public void setTime(long j) {
        String str;
        if (isInEditMode()) {
            str = "00:00 am";
        } else {
            str = TimeFormatUtil.formatTime(getContext(), j);
        }
        this.mTxtTime.setText(str);
    }

    public void setMessage(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            this.mMessage = charSequence.toString();
            this.mTxtMessage.setText(charSequence);
            this.mTxtMessage.setMovementMethod(new LinkMovementMethod() {
                public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
                    try {
                        return super.onTouchEvent(textView, spannable, motionEvent);
                    } catch (Exception unused) {
                        return false;
                    }
                }
            });
        }
    }

    public void setChatItem(@NonNull ConfChatItem confChatItem) {
        if (confChatItem.type == 0) {
            setScreenName(getContext().getString(C4558R.string.zm_title_conf_chat_public, new Object[]{confChatItem.senderName}));
        } else {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isMyself(confChatItem.sender)) {
                setScreenName(getContext().getString(C4558R.string.zm_title_conf_chat_private_to, new Object[]{confChatItem.receiverName}));
            } else {
                setScreenName(getContext().getString(C4558R.string.zm_title_conf_chat_private_from, new Object[]{confChatItem.senderName}));
            }
        }
        setTime(confChatItem.time);
        setMessage(confChatItem.content);
        if (!isInEditMode()) {
            CmmUser userById = ConfMgr.getInstance().getUserById(confChatItem.sender);
            if (userById != null) {
                this.mAvatarView.show(new ParamsBuilder().setName(confChatItem.senderName, confChatItem.senderName).setPath(userById.getSmallPicPath()));
            }
        }
    }

    /* access modifiers changed from: private */
    public void showMessageContextMenu() {
        Activity activity = (Activity) getContext();
        if (activity != null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(activity, false);
            zMMenuAdapter.addItem(new MessageContextMenuItem(activity.getString(C4558R.string.zm_mm_lbl_copy_message), 0));
            ZMAlertDialog create = new Builder(activity).setAdapter(zMMenuAdapter, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ConfChatItemView.this.onSelectContextMenuItem((MessageContextMenuItem) zMMenuAdapter.getItem(i));
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(MessageContextMenuItem messageContextMenuItem) {
        if (messageContextMenuItem.getAction() == 0 && !StringUtil.isEmptyOrNull(this.mMessage)) {
            AndroidAppUtil.copyText(getContext(), this.mMessage);
        }
    }
}

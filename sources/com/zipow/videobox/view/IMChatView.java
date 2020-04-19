package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMChatView extends LinearLayout implements OnClickListener {
    private static final int CONF_BTN_STATUS_INVITE = 1;
    private static final int CONF_BTN_STATUS_RETURN = 2;
    private static final int CONF_BTN_STATUS_START = 0;
    private static final String TAG = "IMChatView";
    private Button mBtnBack;
    private Button mBtnSend;
    private Button mBtnStartConf;
    private IMBuddyItem mBuddyChatTo;
    private int mConfBtnStatus = 0;
    private EditText mEdtMessage;
    private ImageView mImgPresence;
    private Listener mListener;
    private IMMessageListView mMessageListView;
    private TextView mTxtBuddyChatTo;

    public interface Listener {
        void onClickButtonBack();
    }

    public IMChatView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public IMChatView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), C4558R.layout.zm_im_chat_view, this);
        this.mMessageListView = (IMMessageListView) findViewById(C4558R.C4560id.messageListView);
        this.mTxtBuddyChatTo = (TextView) findViewById(C4558R.C4560id.txtBuddyChatTo);
        this.mImgPresence = (ImageView) findViewById(C4558R.C4560id.imgPresence);
        this.mEdtMessage = (EditText) findViewById(C4558R.C4560id.edtMessage);
        this.mBtnSend = (Button) findViewById(C4558R.C4560id.btnSend);
        this.mBtnStartConf = (Button) findViewById(C4558R.C4560id.btnStartConf);
        this.mBtnBack = (Button) findViewById(C4558R.C4560id.btnBack);
        this.mBtnSend.setOnClickListener(this);
        this.mBtnStartConf.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        if (UIMgr.isLargeMode(getContext())) {
            this.mBtnBack.setVisibility(8);
        }
    }

    public void scrollToBottom(boolean z) {
        this.mMessageListView.scrollToBottom(z);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    private void setBuddyNameChatTo(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            this.mTxtBuddyChatTo.setText(charSequence);
        }
    }

    public void reloadData(@Nullable IMBuddyItem iMBuddyItem, @Nullable String str) {
        if (iMBuddyItem != null && str != null) {
            setBuddyChatTo(iMBuddyItem);
            reloadMessages(iMBuddyItem.userId, iMBuddyItem.screenName, str);
            updateStartConfBtn(PTApp.getInstance().getCallStatus());
        }
    }

    private void updateStartConfBtn(int i) {
        switch (i) {
            case 1:
                this.mBtnStartConf.setText(C4558R.string.zm_btn_start_conf);
                this.mConfBtnStatus = 0;
                this.mBtnStartConf.setEnabled(false);
                return;
            case 2:
                if (PTApp.getInstance().probeUserStatus(this.mBuddyChatTo.userId)) {
                    this.mBtnStartConf.setText(C4558R.string.zm_btn_return_to_conf);
                    this.mConfBtnStatus = 2;
                } else {
                    this.mBtnStartConf.setText(C4558R.string.zm_btn_invite_to_conf);
                    this.mConfBtnStatus = 1;
                }
                this.mBtnStartConf.setEnabled(true);
                return;
            default:
                this.mBtnStartConf.setText(C4558R.string.zm_btn_start_conf);
                this.mConfBtnStatus = 0;
                this.mBtnStartConf.setEnabled(checkStartButtonEnabled());
                return;
        }
    }

    private boolean checkStartButtonEnabled() {
        PTApp instance = PTApp.getInstance();
        return instance.hasPrescheduleMeeting() || instance.canAccessZoomWebservice();
    }

    private void setBuddyChatTo(@Nullable IMBuddyItem iMBuddyItem) {
        if (iMBuddyItem != null) {
            this.mBuddyChatTo = iMBuddyItem;
            setBuddyNameChatTo(iMBuddyItem.screenName);
            setPresence(iMBuddyItem.presence);
        }
    }

    public void setPresence(int i) {
        if (i != 0) {
            switch (i) {
                case 2:
                    this.mImgPresence.setImageResource(C4558R.C4559drawable.zm_status_idle);
                    ImageView imageView = this.mImgPresence;
                    imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_idle));
                    return;
                case 3:
                    this.mImgPresence.setImageResource(C4558R.C4559drawable.zm_status_idle);
                    ImageView imageView2 = this.mImgPresence;
                    imageView2.setContentDescription(imageView2.getResources().getString(C4558R.string.zm_description_mm_presence_dnd_19903));
                    return;
                case 4:
                    this.mImgPresence.setImageResource(C4558R.C4559drawable.zm_status_dnd);
                    ImageView imageView3 = this.mImgPresence;
                    imageView3.setContentDescription(imageView3.getResources().getString(C4558R.string.zm_description_mm_presence_xa_19903));
                    return;
                default:
                    this.mImgPresence.setImageResource(C4558R.C4559drawable.zm_offline);
                    ImageView imageView4 = this.mImgPresence;
                    imageView4.setContentDescription(imageView4.getResources().getString(C4558R.string.zm_description_mm_presence_offline));
                    return;
            }
        } else {
            this.mImgPresence.setImageResource(C4558R.C4559drawable.zm_status_available);
            ImageView imageView5 = this.mImgPresence;
            imageView5.setContentDescription(imageView5.getResources().getString(C4558R.string.zm_description_mm_presence_available));
        }
    }

    private void reloadMessages(String str, String str2, String str3) {
        this.mMessageListView.reloadMessages(str, str2, str3);
    }

    public void onIMReceived(@NonNull IMMessage iMMessage) {
        IMBuddyItem iMBuddyItem = this.mBuddyChatTo;
        if (iMBuddyItem != null && iMBuddyItem.userId != null) {
            boolean equals = this.mBuddyChatTo.userId.equals(iMMessage.getFromScreenName());
            boolean equals2 = this.mBuddyChatTo.userId.equals(iMMessage.getToScreenName());
            boolean z = false;
            if (equals || equals2) {
                ZMActivity zMActivity = (ZMActivity) getContext();
                if (zMActivity != null) {
                    this.mMessageListView.onIMReceived(iMMessage, zMActivity.isActive() || equals2);
                    if (iMMessage.getMessageType() == 0 && equals && !zMActivity.isActive()) {
                        Context context = getContext();
                        if (iMMessage.getMessageType() == 0) {
                            z = true;
                        }
                        NotificationMgr.showMessageNotificationMM(context, z);
                    }
                }
            } else {
                Context context2 = getContext();
                if (iMMessage.getMessageType() == 0) {
                    z = true;
                }
                NotificationMgr.showMessageNotificationMM(context2, z);
            }
        }
    }

    public void onIMBuddyPresence(@Nullable BuddyItem buddyItem) {
        if (buddyItem != null) {
            IMBuddyItem iMBuddyItem = this.mBuddyChatTo;
            if (iMBuddyItem != null && iMBuddyItem.userId != null && this.mBuddyChatTo.userId.equals(buddyItem.getJid())) {
                setBuddyChatTo(new IMBuddyItem(buddyItem));
            }
        }
    }

    public void onIMBuddyPic(@Nullable BuddyItem buddyItem) {
        if (buddyItem != null) {
            IMBuddyItem iMBuddyItem = this.mBuddyChatTo;
            if (iMBuddyItem != null && iMBuddyItem.userId != null && this.mBuddyChatTo.userId.equals(buddyItem.getJid())) {
                setBuddyChatTo(new IMBuddyItem(buddyItem));
            }
        }
    }

    public void onWebLogin(long j) {
        updateStartConfBtn(PTApp.getInstance().getCallStatus());
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnSend) {
            onClickBtnSend();
        } else if (id == C4558R.C4560id.btnStartConf) {
            onClickBtnStartConf();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        }
    }

    private void onClickBtnBack() {
        UIUtil.closeSoftKeyboard(getContext(), this);
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onClickButtonBack();
        }
    }

    private void onClickBtnSend() {
        IMBuddyItem iMBuddyItem = this.mBuddyChatTo;
        if (iMBuddyItem != null && iMBuddyItem.userId != null) {
            String trim = this.mEdtMessage.getText().toString().trim();
            if (trim.length() != 0) {
                IMHelper iMHelper = PTApp.getInstance().getIMHelper();
                if (iMHelper != null) {
                    scrollToBottom(true);
                    iMHelper.sendIMMessage(this.mBuddyChatTo.userId, trim, true);
                    this.mEdtMessage.setText("");
                }
            }
        }
    }

    private void onClickBtnStartConf() {
        UIUtil.closeSoftKeyboard(getContext(), this);
        switch (this.mConfBtnStatus) {
            case 0:
                startConf();
                return;
            case 1:
                inviteToConf();
                return;
            case 2:
                returnToConf();
                return;
            default:
                return;
        }
    }

    private void returnToConf() {
        ConfLocalHelper.returnToConf(getContext());
    }

    private void inviteToConf() {
        PTApp instance = PTApp.getInstance();
        String activeCallId = instance.getActiveCallId();
        if (!StringUtil.isEmptyOrNull(activeCallId)) {
            if (instance.inviteBuddiesToConf(new String[]{this.mBuddyChatTo.userId}, null, activeCallId, 0, getContext().getString(C4558R.string.zm_msg_invitation_message_template)) == 0) {
                ConfLocalHelper.returnToConf(getContext());
            }
        }
    }

    private void startConf() {
        int inviteToVideoCall = ConfActivity.inviteToVideoCall(getContext(), this.mBuddyChatTo.userId, 1);
        if (inviteToVideoCall != 0) {
            StartHangoutFailedDialog.show(((ZMActivity) getContext()).getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), inviteToVideoCall);
        }
    }

    public void onCallStatusChanged(long j) {
        updateStartConfBtn((int) j);
    }

    public void onCallPlistChanged() {
        updateStartConfBtn(PTApp.getInstance().getCallStatus());
    }
}

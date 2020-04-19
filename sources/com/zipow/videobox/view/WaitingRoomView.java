package com.zipow.videobox.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.common.ZMConfiguration;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.dialog.LeaveAlertDialog;
import com.zipow.videobox.fragment.ConfChatFragmentForWaitingRoom;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.LazyLoadDrawable;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class WaitingRoomView extends LinearLayout implements OnClickListener {
    private static boolean receivedMsgInWaitingRoom = false;
    private View mBtnChat;
    private View mBtnLeave = null;
    @Nullable
    private View mBtnSignIn;
    private Runnable mFirstFocusRunnable = new Runnable() {
        public void run() {
            if (ConfLocalHelper.isInSilentMode() && WaitingRoomView.this.mTxtMeetingTitle != null) {
                AccessibilityUtil.announceForAccessibilityCompat((View) WaitingRoomView.this.mTxtMeetingTitle, WaitingRoomView.this.mTxtMeetingTitle.getContentDescription());
            }
        }
    };
    @NonNull
    private Handler mHandler = new Handler();
    private View mPanelDescriptionView;
    private View mTitleBar = null;
    private ImageView mTopicImage = null;
    private TextView mTxtChatBubble = null;
    private TextView mTxtDescription = null;
    private TextView mTxtMeetingNumber = null;
    /* access modifiers changed from: private */
    public TextView mTxtMeetingTitle = null;
    private TextView mTxtTopic = null;

    public WaitingRoomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    public WaitingRoomView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        inflateLayout();
        this.mPanelDescriptionView = findViewById(C4558R.C4560id.panelDescriptionView);
        this.mBtnLeave = findViewById(C4558R.C4560id.btnLeave);
        this.mTxtMeetingNumber = (TextView) findViewById(C4558R.C4560id.txtMeetingNumber);
        this.mTitleBar = findViewById(C4558R.C4560id.vTitleBar);
        this.mTxtMeetingTitle = (TextView) findViewById(C4558R.C4560id.txtTitle);
        this.mTopicImage = (ImageView) findViewById(C4558R.C4560id.imgTitleIcon);
        this.mTxtTopic = (TextView) findViewById(C4558R.C4560id.meetingTopic);
        this.mTxtDescription = (TextView) findViewById(C4558R.C4560id.txtDescription);
        this.mTxtChatBubble = (TextView) findViewById(C4558R.C4560id.txtBubble);
        this.mBtnSignIn = findViewById(C4558R.C4560id.btnSignIn);
        this.mBtnChat = findViewById(C4558R.C4560id.buttonChat);
        this.mBtnLeave.setOnClickListener(this);
        View view = this.mBtnSignIn;
        if (view != null) {
            view.setOnClickListener(this);
        }
        this.mBtnChat.setOnClickListener(this);
        updateOrientation(context);
        updateData();
        checkShowChatBtn();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_waiting_room_view, this);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateOrientation(getContext());
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateOrientation(getContext());
    }

    private void updateOrientation(@Nullable Context context) {
        if (context != null) {
            String waitingRoomLayoutDescription = ConfMgr.getInstance().getWaitingRoomLayoutDescription();
            if (StringUtil.isEmptyOrNull(waitingRoomLayoutDescription)) {
                this.mPanelDescriptionView.setPadding(0, 0, 0, UIUtil.dip2px(getContext(), 60.0f));
            } else if (!UIUtil.isPortraitMode(context)) {
                this.mPanelDescriptionView.setPadding(0, 0, 0, UIUtil.dip2px(getContext(), 10.0f));
            } else if (!StringUtil.isEmptyOrNull(waitingRoomLayoutDescription)) {
                this.mPanelDescriptionView.setPadding(0, 0, 0, UIUtil.dip2px(getContext(), 20.0f));
            }
        }
    }

    public void setTitlePadding(int i, int i2, int i3, int i4) {
        View view = this.mTitleBar;
        if (view != null) {
            view.setPadding(i, i2, i3, i4);
        }
    }

    private void updateDefaultType(@NonNull MeetingInfoProto meetingInfoProto) {
        this.mTxtDescription.setVisibility(4);
        this.mTopicImage.setVisibility(8);
        if (isInEditMode()) {
            this.mTxtTopic.setText("In Meeting");
        } else if (!ConfMgr.getInstance().isWaitingRoomLayoutReady()) {
            this.mTxtTopic.setVisibility(8);
            updateMeetingTitle(null, 8);
        } else {
            updateMeetingTitle(null, 0);
            String topic = meetingInfoProto.getTopic();
            if (!StringUtil.isEmptyOrNull(topic)) {
                this.mTxtTopic.setVisibility(0);
                this.mTxtTopic.setText(topic);
            } else {
                this.mTxtTopic.setVisibility(8);
            }
        }
    }

    private void updateSimpleType(@NonNull MeetingInfoProto meetingInfoProto) {
        this.mTopicImage.setVisibility(8);
        if (isInEditMode()) {
            this.mTxtTopic.setText("In Meeting");
            this.mTxtMeetingTitle.setText(getResources().getString(C4558R.string.zm_msg_waiting_meeting_nitification));
            TextView textView = this.mTxtMeetingTitle;
            textView.setContentDescription(textView.getText());
            return;
        }
        this.mTxtTopic.setVisibility(0);
        updateMeetingTitle(ConfMgr.getInstance().getWaitingRoomLayoutTitle(), 0);
        String waitingRoomLayoutDescription = ConfMgr.getInstance().getWaitingRoomLayoutDescription();
        if (!StringUtil.isEmptyOrNull(waitingRoomLayoutDescription)) {
            this.mTxtDescription.setVisibility(0);
            this.mTxtDescription.setText(waitingRoomLayoutDescription);
        } else {
            this.mTxtDescription.setVisibility(4);
        }
        String topic = meetingInfoProto.getTopic();
        if (!StringUtil.isEmptyOrNull(topic)) {
            this.mTxtTopic.setVisibility(0);
            this.mTxtTopic.setText(topic);
        } else {
            this.mTxtTopic.setVisibility(8);
        }
        boolean isWaitingRoomLayoutReady = ConfMgr.getInstance().isWaitingRoomLayoutReady();
        String waitingRoomLayoutImagePath = ConfMgr.getInstance().getWaitingRoomLayoutImagePath();
        if (!isWaitingRoomLayoutReady || StringUtil.isEmptyOrNull(waitingRoomLayoutImagePath)) {
            this.mTopicImage.setVisibility(8);
        } else {
            LazyLoadDrawable lazyLoadDrawable = new LazyLoadDrawable(waitingRoomLayoutImagePath);
            if (lazyLoadDrawable.isValidDrawable()) {
                this.mTopicImage.setVisibility(0);
                this.mTopicImage.setImageDrawable(lazyLoadDrawable);
            }
        }
    }

    public void updateData() {
        if (!isInEditMode()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && confContext.supportPutUserinWaitingListUponEntryFeature() && this.mBtnSignIn != null) {
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                int i = 0;
                if (!confContext.needRemindLoginWhenInWaitingRoom() || confStatusObj == null || confStatusObj.isVerifyingMyGuestRole()) {
                    this.mBtnSignIn.setVisibility(8);
                } else {
                    this.mBtnSignIn.setVisibility(0);
                }
                MeetingInfoProto meetingItem = confContext.getMeetingItem();
                if (meetingItem != null) {
                    this.mTxtMeetingNumber.setText(StringUtil.formatConfNumber(meetingItem.getMeetingNumber()));
                    if (ConfMgr.getInstance().getWaitingRoomLayoutType() == 1) {
                        updateSimpleType(meetingItem);
                    } else {
                        updateDefaultType(meetingItem);
                    }
                    int[] unreadChatMessageIndexes = ConfMgr.getInstance().getUnreadChatMessageIndexes();
                    if (unreadChatMessageIndexes != null) {
                        i = unreadChatMessageIndexes.length;
                    }
                    setUnreadMsgCount(i);
                }
            }
        }
    }

    private void checkShowChatBtn() {
        if (receivedMsgInWaitingRoom) {
            this.mBtnChat.setVisibility(0);
        } else {
            this.mBtnChat.setVisibility(8);
        }
    }

    public void hideWaitingRoomSignInBtn() {
        if (!isInEditMode()) {
            View view = this.mBtnSignIn;
            if (view != null && view.getVisibility() == 0) {
                this.mBtnSignIn.setVisibility(8);
            }
        }
    }

    public void setUnreadMsgCount(int i) {
        String str;
        if (i > 0) {
            receivedMsgInWaitingRoom = true;
            this.mBtnChat.setVisibility(0);
            this.mTxtChatBubble.setVisibility(0);
            this.mTxtChatBubble.setText(String.valueOf(i));
            str = getResources().getQuantityString(C4558R.plurals.zm_accessibility_waiting_room_unread_message_button_46304, i, new Object[]{String.valueOf(i)});
        } else {
            this.mTxtChatBubble.setVisibility(8);
            str = getResources().getString(C4558R.string.zm_accessibility_waiting_room_chat_button_46304);
        }
        this.mBtnChat.setContentDescription(str);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnLeave) {
            onClickBtnLeave();
        } else if (id == C4558R.C4560id.btnSignIn) {
            onClickBtnSignIn();
        } else if (id == C4558R.C4560id.buttonChat) {
            onClickChat();
        }
    }

    private void onClickBtnLeave() {
        new LeaveAlertDialog().show(((ZMActivity) getContext()).getSupportFragmentManager(), LeaveAlertDialog.class.getName());
    }

    private void onClickBtnSignIn() {
        ConfMgr.getInstance().loginWhenInWaitingRoom();
        hideWaitingRoomSignInBtn();
    }

    private void onClickChat() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            ConfChatFragmentForWaitingRoom.showAsActivity(zMActivity);
        }
    }

    private void updateMeetingTitle(@Nullable String str, int i) {
        this.mTxtMeetingTitle.setVisibility(i);
        if (i == 0) {
            if (!StringUtil.isEmptyOrNull(str)) {
                this.mTxtMeetingTitle.setText(str);
                this.mTxtMeetingTitle.setContentDescription(String.format("%1$s.%2$s", new Object[]{str, getResources().getString(C4558R.string.zm_msg_waiting_meeting_nitification)}));
            } else {
                this.mTxtMeetingTitle.setText(getResources().getString(C4558R.string.zm_msg_waiting_meeting_nitification));
                TextView textView = this.mTxtMeetingTitle;
                textView.setContentDescription(textView.getText());
            }
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext()) && getVisibility() == 0 && ConfLocalHelper.isInSilentMode()) {
                this.mHandler.removeCallbacks(this.mFirstFocusRunnable);
                this.mHandler.postDelayed(this.mFirstFocusRunnable, (long) ZMConfiguration.FIRST_AUTO_FOCUS_DELAY);
            }
        }
    }
}

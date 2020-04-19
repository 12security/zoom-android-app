package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ContactsAvatarCache;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class CallConnectingView extends LinearLayout implements OnClickListener, IABContactsCacheListener {
    private static final String TAG = "CallConnectingView";
    private AvatarView mAvatarView;
    private Button mBtnEndCall;
    private Button mBtnSpeaker;
    private View mSpeakerDivider;
    private TextView mTxtMsgCalling;
    private TextView mTxtScreenName;
    private View mViewFrame;

    public CallConnectingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public CallConnectingView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mBtnEndCall.setOnClickListener(this);
        this.mBtnSpeaker.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_call_connecting, this);
        this.mViewFrame = findViewById(C4558R.C4560id.viewFrame);
        this.mBtnEndCall = (Button) findViewById(C4558R.C4560id.btnEndCall);
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mTxtMsgCalling = (TextView) findViewById(C4558R.C4560id.txtMsgCalling);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mBtnSpeaker = (Button) findViewById(C4558R.C4560id.btnSpeaker);
        this.mSpeakerDivider = findViewById(C4558R.C4560id.speakerDivider);
    }

    public void updateUIForCallType(int i) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            switch (i) {
                case 0:
                    setVisibility(0);
                    this.mViewFrame.setBackgroundResource(C4558R.C4559drawable.zm_audiocall_bg);
                    setScreenName(confContext);
                    setAvatar(confContext);
                    setCallingMessage(confContext, i);
                    this.mAvatarView.setVisibility(0);
                    boolean isFeatureTelephonySupported = VoiceEngineCompat.isFeatureTelephonySupported(getContext());
                    boolean z = HeadsetUtil.getInstance().isBluetoothHeadsetOn() || HeadsetUtil.getInstance().isWiredHeadsetOn();
                    if (VoiceEngineCompat.isPlayerCommunicationModeAvailable() && (isFeatureTelephonySupported || z)) {
                        this.mBtnSpeaker.setVisibility(0);
                        this.mSpeakerDivider.setVisibility(0);
                        break;
                    } else {
                        this.mBtnSpeaker.setVisibility(8);
                        this.mSpeakerDivider.setVisibility(8);
                        break;
                    }
                case 1:
                    setVisibility(0);
                    this.mViewFrame.setBackgroundResource(0);
                    setScreenName(confContext);
                    setAvatar(confContext);
                    setCallingMessage(confContext, i);
                    this.mAvatarView.setVisibility(8);
                    this.mBtnSpeaker.setVisibility(8);
                    this.mSpeakerDivider.setVisibility(8);
                    break;
                case 2:
                    setVisibility(0);
                    this.mViewFrame.setBackgroundResource(C4558R.C4559drawable.zm_audiocall_bg);
                    setScreenName(confContext);
                    setAvatar(confContext);
                    setCallingMessage(confContext, i);
                    this.mAvatarView.setVisibility(0);
                    this.mBtnSpeaker.setVisibility(8);
                    this.mSpeakerDivider.setVisibility(8);
                    break;
                case 3:
                case 4:
                    setVisibility(8);
                    this.mBtnSpeaker.setVisibility(8);
                    this.mSpeakerDivider.setVisibility(8);
                    break;
            }
            this.mBtnSpeaker.setVisibility(8);
            this.mSpeakerDivider.setVisibility(8);
            initBtnSpeakerStatus(i);
        }
    }

    private void setCallingMessage(CmmConfContext cmmConfContext, int i) {
        if (cmmConfContext.getLaunchReason() == 1) {
            switch (i) {
                case 0:
                case 2:
                    this.mTxtMsgCalling.setText(C4558R.string.zm_msg_audio_calling);
                    return;
                case 1:
                    this.mTxtMsgCalling.setText(C4558R.string.zm_msg_video_calling);
                    return;
                default:
                    return;
            }
        } else {
            this.mTxtMsgCalling.setText(C4558R.string.zm_msg_connecting);
        }
    }

    private void setAvatar(CmmConfContext cmmConfContext) {
        String str = cmmConfContext.get1On1BuddyLocalPic();
        if (ImageUtil.isValidImageFile(str)) {
            this.mAvatarView.show(new ParamsBuilder().setPath(str));
            return;
        }
        String str2 = cmmConfContext.get1On1BuddyPhoneNumber();
        boolean isPhoneCall = cmmConfContext.isPhoneCall();
        if (!StringUtil.isEmptyOrNull(str2) && isPhoneCall) {
            this.mAvatarView.show(new ParamsBuilder().setPath(getContactsAvatarPathFromPhoneNumber(str2)));
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ABContactsCache.getInstance().removeListener(this);
    }

    public void onContactsCacheUpdated() {
        ABContactsCache.getInstance().removeListener(this);
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            String str = confContext.get1On1BuddyPhoneNumber();
            boolean isPhoneCall = confContext.isPhoneCall();
            if (!StringUtil.isEmptyOrNull(str) && isPhoneCall) {
                this.mAvatarView.show(new ParamsBuilder().setPath(getContactsAvatarPathFromPhoneNumber(str)));
            }
        }
    }

    private Bitmap getContactsAvatarFromPhoneNumber(String str) {
        Context context = getContext();
        if (context == null) {
            return null;
        }
        ABContactsCache instance = ABContactsCache.getInstance();
        instance.addListener(this);
        if (!instance.isCached() && !instance.reloadAllContacts()) {
            return null;
        }
        Contact firstContactByPhoneNumber = instance.getFirstContactByPhoneNumber(str);
        if (firstContactByPhoneNumber == null) {
            return null;
        }
        instance.removeListener(this);
        return ContactsAvatarCache.getInstance().getContactAvatar(context, firstContactByPhoneNumber.contactId);
    }

    private String getContactsAvatarPathFromPhoneNumber(String str) {
        if (getContext() == null) {
            return null;
        }
        ABContactsCache instance = ABContactsCache.getInstance();
        instance.addListener(this);
        if (!instance.isCached() && !instance.reloadAllContacts()) {
            return null;
        }
        Contact firstContactByPhoneNumber = instance.getFirstContactByPhoneNumber(str);
        if (firstContactByPhoneNumber == null) {
            return null;
        }
        instance.removeListener(this);
        return ContactsAvatarCache.getInstance().getContactAvatarPath(firstContactByPhoneNumber.contactId);
    }

    private void setScreenName(CmmConfContext cmmConfContext) {
        this.mTxtScreenName.setText(cmmConfContext.get1On1BuddyScreeName());
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnEndCall) {
            onClickButtonEndCall();
        } else if (id == C4558R.C4560id.btnSpeaker) {
            onClickBtnSpeaker();
        }
    }

    private void onClickButtonEndCall() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            ConfActivity confActivity = (ConfActivity) getContext();
            if (confActivity != null) {
                if (confContext.getOrginalHost()) {
                    ConfLocalHelper.endCall(confActivity);
                } else {
                    ConfLocalHelper.leaveCall(confActivity);
                }
            }
        }
    }

    private void onClickBtnSpeaker() {
        ConfActivity confActivity = (ConfActivity) getContext();
        if (confActivity != null) {
            ConfLocalHelper.switchAudioSource(confActivity);
        }
    }

    private void initBtnSpeakerStatus(int i) {
        this.mBtnSpeaker.setSelected(ConfUI.getInstance().getCurrentAudioSourceType() == 0);
    }
}

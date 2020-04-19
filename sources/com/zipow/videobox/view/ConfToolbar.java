package com.zipow.videobox.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.confapp.meeting.ConfParams;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.UIMgr;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfToolbar extends LinearLayout implements OnClickListener {
    public static final int BUTTON_ALL = 255;
    public static final int BUTTON_AUDIO = 2;
    public static final int BUTTON_CHATS = 256;
    public static final int BUTTON_LEAVE = 16;
    public static final int BUTTON_MORE = 32;
    public static final int BUTTON_PARTICIPANTS = 8;
    public static final int BUTTON_QA = 128;
    public static final int BUTTON_RAISEHAND = 64;
    public static final int BUTTON_SHARE = 4;
    public static final int BUTTON_VIDEO = 1;
    public static final int BUTTON_VIEWONLY = 448;
    private static final int DISPLAY_ANIM_DURATION = 200;
    private static final String TAG = "ConfToolbar";
    private long mAudioType;
    private ToolbarButton mBtnAudio;
    private PListButton mBtnChats;
    private ToolbarButton mBtnLeave;
    private ToolbarButton mBtnLowerHand;
    private PListButton mBtnMore;
    private PListButton mBtnPList;
    private ToolbarButton mBtnQA;
    private ToolbarButton mBtnRaiseHand;
    private ToolbarButton mBtnShare;
    private ToolbarButton mBtnStopShare;
    private ToolbarButton mBtnVideo;
    private int mButtons;
    /* access modifiers changed from: private */
    public Listener mListener;
    private boolean mbAudioMuted;
    private boolean mbVideoMuted;

    public interface Listener {
        void onClickAttendeeLowerHand();

        void onClickAttendeeRaiseHand();

        void onClickBtnAudio();

        void onClickChats();

        void onClickLeave();

        void onClickMore();

        void onClickParticipants();

        void onClickQA();

        void onToolbarVisibilityChanged(boolean z);
    }

    public ConfToolbar(Context context) {
        this(context, null);
    }

    public ConfToolbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mbAudioMuted = false;
        this.mbVideoMuted = false;
        this.mAudioType = 0;
        this.mButtons = 255;
        init();
        setFocusable(false);
    }

    public boolean hasEnableBUtton(int i) {
        return (i & this.mButtons) > 0;
    }

    private void init() {
        if (isInEditMode() || !UIMgr.isLargeMode(getContext())) {
            View.inflate(getContext(), C4558R.layout.zm_conf_toolbar, this);
        } else {
            View.inflate(getContext(), C4558R.layout.zm_conf_toolbar_large, this);
        }
        this.mBtnAudio = (ToolbarButton) findViewById(C4558R.C4560id.btnAudio);
        this.mBtnVideo = (ToolbarButton) findViewById(C4558R.C4560id.btnVideo);
        this.mBtnLeave = (ToolbarButton) findViewById(C4558R.C4560id.btnLeave);
        this.mBtnPList = (PListButton) findViewById(C4558R.C4560id.btnPList);
        this.mBtnShare = (ToolbarButton) findViewById(C4558R.C4560id.btnShare);
        this.mBtnStopShare = (ToolbarButton) findViewById(C4558R.C4560id.btnStopShare);
        this.mBtnMore = (PListButton) findViewById(C4558R.C4560id.btnMore);
        this.mBtnRaiseHand = (ToolbarButton) findViewById(C4558R.C4560id.btnRaiseHand);
        this.mBtnLowerHand = (ToolbarButton) findViewById(C4558R.C4560id.btnLowerHand);
        this.mBtnQA = (ToolbarButton) findViewById(C4558R.C4560id.btnQA);
        this.mBtnChats = (PListButton) findViewById(C4558R.C4560id.btnChats);
        ToolbarButton toolbarButton = this.mBtnAudio;
        if (toolbarButton != null) {
            toolbarButton.setOnClickListener(this);
        }
        refreshBtnVideo();
        ToolbarButton toolbarButton2 = this.mBtnVideo;
        if (toolbarButton2 != null) {
            toolbarButton2.setOnClickListener(this);
        }
        ToolbarButton toolbarButton3 = this.mBtnLeave;
        if (toolbarButton3 != null) {
            toolbarButton3.setOnClickListener(this);
        }
        PListButton pListButton = this.mBtnPList;
        if (pListButton != null) {
            pListButton.setOnClickListener(this);
        }
        ToolbarButton toolbarButton4 = this.mBtnShare;
        if (toolbarButton4 != null) {
            toolbarButton4.setOnClickListener(this);
        }
        ToolbarButton toolbarButton5 = this.mBtnStopShare;
        if (toolbarButton5 != null) {
            toolbarButton5.setOnClickListener(this);
        }
        PListButton pListButton2 = this.mBtnMore;
        if (pListButton2 != null) {
            pListButton2.setOnClickListener(this);
        }
        ToolbarButton toolbarButton6 = this.mBtnRaiseHand;
        if (toolbarButton6 != null) {
            toolbarButton6.setOnClickListener(this);
        }
        ToolbarButton toolbarButton7 = this.mBtnLowerHand;
        if (toolbarButton7 != null) {
            toolbarButton7.setOnClickListener(this);
        }
        ToolbarButton toolbarButton8 = this.mBtnQA;
        if (toolbarButton8 != null) {
            toolbarButton8.setOnClickListener(this);
        }
        PListButton pListButton3 = this.mBtnChats;
        if (pListButton3 != null) {
            pListButton3.setVisibility(8);
            this.mBtnChats.setOnClickListener(this);
        }
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void setAudioMuted(boolean z) {
        ToolbarButton toolbarButton = this.mBtnAudio;
        if (toolbarButton != null) {
            boolean z2 = this.mbAudioMuted;
            this.mbAudioMuted = z;
            long j = this.mAudioType;
            if (j == 2) {
                toolbarButton.setImageResource(C4558R.C4559drawable.zm_btn_audio_none);
                this.mBtnAudio.setText(C4558R.string.zm_btn_join_audio_98431);
                if (z2 != this.mbAudioMuted && AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                    this.mBtnAudio.setContentDescription(getContext().getString(C4558R.string.zm_description_toolbar_btn_status_audio_disconnect));
                    this.mBtnAudio.sendAccessibilityEvent(8);
                }
            } else {
                if (j == 1) {
                    toolbarButton.setImageResource(this.mbAudioMuted ? C4558R.C4559drawable.zm_btn_unmute_phone : C4558R.C4559drawable.zm_btn_mute_phone);
                } else {
                    toolbarButton.setImageResource(this.mbAudioMuted ? C4558R.C4559drawable.zm_btn_unmute_audio : C4558R.C4559drawable.zm_btn_mute_audio);
                }
                if (z2 != this.mbAudioMuted) {
                    this.mBtnAudio.setContentDescription(getContext().getString(this.mbAudioMuted ? C4558R.string.zm_description_toolbar_btn_status_audio_unmuted_17843 : C4558R.string.zm_description_toolbar_btn_status_audio_muted_17843));
                    this.mBtnAudio.sendAccessibilityEvent(8);
                } else if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext()) && AccessibilityUtil.getIsAccessibilityFocused(this.mBtnAudio)) {
                    AccessibilityUtil.announceNoInterruptForAccessibilityCompat(this.mBtnAudio, this.mbAudioMuted ? C4558R.string.zm_description_toolbar_btn_status_audio_already_muted_17843 : C4558R.string.zm_description_toolbar_btn_status_audio_already_unmuted_17843);
                }
                this.mBtnAudio.setText(z ? C4558R.string.zm_btn_unmute_61381 : C4558R.string.zm_btn_mute_61381);
            }
        }
    }

    public void refreshBtnVideo() {
        ToolbarButton toolbarButton = this.mBtnVideo;
        if (toolbarButton != null) {
            toolbarButton.setEnabled(true ^ ConfLocalHelper.hasDisableSendVideoReason(1));
        }
    }

    public void refreshBtnShare(@NonNull ConfParams confParams) {
        if (ConfLocalHelper.isNeedShowBtnShare(confParams)) {
            this.mButtons &= 4;
        } else {
            this.mButtons &= -5;
        }
        ToolbarButton toolbarButton = this.mBtnShare;
        int i = 0;
        if (toolbarButton != null) {
            toolbarButton.setVisibility(((this.mButtons & 4) == 0 || isSharing()) ? 8 : 0);
        }
        ToolbarButton toolbarButton2 = this.mBtnStopShare;
        if (toolbarButton2 != null) {
            if ((this.mButtons & 4) == 0 || !isSharing()) {
                i = 8;
            }
            toolbarButton2.setVisibility(i);
        }
    }

    public void setVideoMuted(boolean z) {
        ToolbarButton toolbarButton = this.mBtnVideo;
        if (toolbarButton != null) {
            boolean z2 = this.mbVideoMuted;
            this.mbVideoMuted = z;
            toolbarButton.setImageResource(z ? C4558R.C4559drawable.zm_btn_unmute_video : C4558R.C4559drawable.zm_btn_mute_video);
            if (z2 != this.mbVideoMuted) {
                this.mBtnVideo.setContentDescription(getContext().getString(this.mbVideoMuted ? C4558R.string.zm_description_toolbar_btn_status_video_unmuted_17843 : C4558R.string.zm_description_toolbar_btn_status_video_muted_17843));
                this.mBtnVideo.sendAccessibilityEvent(8);
            } else if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext()) && AccessibilityUtil.getIsAccessibilityFocused(this.mBtnVideo)) {
                AccessibilityUtil.announceNoInterruptForAccessibilityCompat(this.mBtnVideo, this.mbVideoMuted ? C4558R.string.zm_description_toolbar_btn_status_video_already_muted_17843 : C4558R.string.zm_description_toolbar_btn_status_video_already_unmuted_17843);
            }
            this.mBtnVideo.setText(z ? C4558R.string.zm_btn_start_video : C4558R.string.zm_btn_stop_video_120444);
        }
    }

    public void setChatsButton(int i) {
        PListButton pListButton = this.mBtnChats;
        if (pListButton == null || pListButton.getVisibility() != 0) {
            PListButton pListButton2 = this.mBtnMore;
            if (pListButton2 != null) {
                pListButton2.setUnreadMessageCount(i);
                return;
            }
            return;
        }
        this.mBtnChats.setUnreadMessageCount(i);
    }

    public void setQANoteMsgButton(int i) {
        ToolbarButton toolbarButton = this.mBtnQA;
        if (toolbarButton != null) {
            String str = i == 0 ? null : i < 100 ? String.valueOf(i) : "99+";
            toolbarButton.setNoteMessage((CharSequence) str);
        }
    }

    public void setButtons(int i) {
        if (!UIMgr.isLargeMode(getContext())) {
            i &= -17;
        }
        this.mButtons = i;
        ToolbarButton toolbarButton = this.mBtnAudio;
        int i2 = 0;
        if (toolbarButton != null) {
            toolbarButton.setVisibility((i & 2) != 0 ? 0 : 8);
        }
        ToolbarButton toolbarButton2 = this.mBtnVideo;
        if (toolbarButton2 != null) {
            toolbarButton2.setVisibility((i & 1) != 0 ? 0 : 8);
        }
        ToolbarButton toolbarButton3 = this.mBtnLeave;
        if (toolbarButton3 != null) {
            toolbarButton3.setVisibility((i & 16) != 0 ? 0 : 8);
        }
        PListButton pListButton = this.mBtnPList;
        if (pListButton != null) {
            pListButton.setVisibility((i & 8) != 0 ? 0 : 8);
        }
        ToolbarButton toolbarButton4 = this.mBtnShare;
        if (toolbarButton4 != null) {
            toolbarButton4.setVisibility(((i & 4) == 0 || isSharing()) ? 8 : 0);
        }
        ToolbarButton toolbarButton5 = this.mBtnStopShare;
        if (toolbarButton5 != null) {
            toolbarButton5.setVisibility(((i & 4) == 0 || !isSharing()) ? 8 : 0);
        }
        PListButton pListButton2 = this.mBtnMore;
        if (pListButton2 != null) {
            pListButton2.setVisibility((i & 32) != 0 ? 0 : 8);
        }
        ToolbarButton toolbarButton6 = this.mBtnRaiseHand;
        if (toolbarButton6 != null) {
            int i3 = i & 64;
            toolbarButton6.setVisibility(i3 != 0 ? 0 : 8);
            if (i3 == 0) {
                this.mBtnLowerHand.setVisibility(8);
            }
        }
        ToolbarButton toolbarButton7 = this.mBtnQA;
        if (toolbarButton7 != null) {
            toolbarButton7.setVisibility((i & 128) != 0 ? 0 : 8);
        }
        PListButton pListButton3 = this.mBtnChats;
        if (pListButton3 != null) {
            if ((i & 256) == 0) {
                i2 = 8;
            }
            pListButton3.setVisibility(i2);
        }
    }

    private boolean isSharing() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        boolean z = false;
        if (shareObj == null) {
            return false;
        }
        if (shareObj.getShareStatus() == 2) {
            z = true;
        }
        return z;
    }

    public void setHostRole(boolean z) {
        if (this.mBtnPList != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                Context context = getContext();
                if (context != null) {
                    if (confContext.isChatOff()) {
                        this.mBtnPList.setImageResource(C4558R.C4559drawable.zm_btn_control);
                        this.mBtnPList.setText(C4558R.string.zm_btn_participants);
                        this.mBtnPList.setContentDescription(context.getString(C4558R.string.zm_btn_participants));
                    } else {
                        this.mBtnPList.setImageResource(z ? C4558R.C4559drawable.zm_btn_control : C4558R.C4559drawable.zm_btn_plist);
                        this.mBtnPList.setText(C4558R.string.zm_btn_participants_chat);
                        this.mBtnPList.setContentDescription(context.getString(C4558R.string.zm_btn_participants_chat));
                    }
                    if (ConfLocalHelper.canControlWaitingRoom()) {
                        List clientOnHoldUserList = ConfMgr.getInstance().getClientOnHoldUserList();
                        if (!CollectionsUtil.isCollectionEmpty(clientOnHoldUserList)) {
                            this.mBtnPList.setContentDescription(context.getString(C4558R.string.zm_accessibility_waiting_room_users_count_149486, new Object[]{Integer.valueOf(clientOnHoldUserList.size())}));
                        }
                    }
                }
            }
        }
    }

    public void setAudioType(long j) {
        ToolbarButton toolbarButton = this.mBtnAudio;
        if (toolbarButton != null) {
            long j2 = this.mAudioType;
            this.mAudioType = j;
            long j3 = this.mAudioType;
            if (j3 == 2) {
                toolbarButton.setImageResource(C4558R.C4559drawable.zm_btn_audio_none);
                this.mBtnAudio.setText(C4558R.string.zm_btn_join_audio_98431);
                if (j2 != this.mAudioType) {
                    this.mBtnAudio.setContentDescription(getContext().getString(C4558R.string.zm_description_toolbar_btn_status_audio_disconnect));
                    this.mBtnAudio.sendAccessibilityEvent(8);
                }
            } else {
                if (j3 == 1) {
                    toolbarButton.setImageResource(this.mbAudioMuted ? C4558R.C4559drawable.zm_btn_unmute_phone : C4558R.C4559drawable.zm_btn_mute_phone);
                } else {
                    toolbarButton.setImageResource(this.mbAudioMuted ? C4558R.C4559drawable.zm_btn_unmute_audio : C4558R.C4559drawable.zm_btn_mute_audio);
                }
                if (j2 != this.mAudioType) {
                    this.mBtnAudio.setContentDescription(getContext().getString(this.mbAudioMuted ? C4558R.string.zm_description_toolbar_btn_status_audio_unmuted_17843 : C4558R.string.zm_description_toolbar_btn_status_audio_muted_17843));
                    this.mBtnAudio.sendAccessibilityEvent(8);
                } else if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext()) && AccessibilityUtil.getIsAccessibilityFocused(this.mBtnAudio)) {
                    AccessibilityUtil.announceNoInterruptForAccessibilityCompat(this.mBtnAudio, this.mbAudioMuted ? C4558R.string.zm_description_toolbar_btn_status_audio_already_muted_17843 : C4558R.string.zm_description_toolbar_btn_status_audio_already_unmuted_17843);
                }
                this.mBtnAudio.setText(this.mbAudioMuted ? C4558R.string.zm_btn_unmute_61381 : C4558R.string.zm_btn_mute_61381);
            }
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnAudio) {
            Listener listener = this.mListener;
            if (listener != null) {
                listener.onClickBtnAudio();
            }
        } else if (id == C4558R.C4560id.btnVideo) {
            ZMConfComponentMgr.getInstance().sinkInClickBtnVideo();
        } else if (id == C4558R.C4560id.btnLeave) {
            Listener listener2 = this.mListener;
            if (listener2 != null) {
                listener2.onClickLeave();
            }
        } else if (id == C4558R.C4560id.btnPList) {
            Listener listener3 = this.mListener;
            if (listener3 != null) {
                listener3.onClickParticipants();
            }
        } else if (id == C4558R.C4560id.btnShare) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj == null || !confStatusObj.isShareDisabledByInfoBarrier()) {
                ZMConfComponentMgr.getInstance().showShareChoice();
            } else {
                UIUtil.showSimpleMessageDialog((Activity) ZMActivity.getFrontActivity(), getContext().getString(C4558R.string.zm_unable_to_share_in_meeting_title_93170), getContext().getString(C4558R.string.zm_unable_to_share_in_meeting_msg_93170));
            }
        } else if (id == C4558R.C4560id.btnStopShare) {
            ZMConfComponentMgr.getInstance().stopShare();
        } else if (id == C4558R.C4560id.btnMore) {
            Listener listener4 = this.mListener;
            if (listener4 != null) {
                listener4.onClickMore();
            }
        } else if (id == C4558R.C4560id.btnRaiseHand) {
            Listener listener5 = this.mListener;
            if (listener5 != null) {
                listener5.onClickAttendeeRaiseHand();
            }
        } else if (id == C4558R.C4560id.btnLowerHand) {
            Listener listener6 = this.mListener;
            if (listener6 != null) {
                listener6.onClickAttendeeLowerHand();
            }
        } else if (id == C4558R.C4560id.btnQA) {
            Listener listener7 = this.mListener;
            if (listener7 != null) {
                listener7.onClickQA();
            }
        } else if (id == C4558R.C4560id.btnChats) {
            Listener listener8 = this.mListener;
            if (listener8 != null) {
                listener8.onClickChats();
            }
        }
    }

    public void showRaiseHand() {
        ToolbarButton toolbarButton = this.mBtnLowerHand;
        if (toolbarButton != null) {
            toolbarButton.setVisibility(8);
        }
        ToolbarButton toolbarButton2 = this.mBtnRaiseHand;
        if (toolbarButton2 != null) {
            toolbarButton2.setVisibility(0);
        }
    }

    public void showLowerHand() {
        ToolbarButton toolbarButton = this.mBtnLowerHand;
        if (toolbarButton != null) {
            toolbarButton.setVisibility(0);
        }
        ToolbarButton toolbarButton2 = this.mBtnRaiseHand;
        if (toolbarButton2 != null) {
            toolbarButton2.setVisibility(8);
        }
    }

    public void disableRaiseHand() {
        ToolbarButton toolbarButton = this.mBtnRaiseHand;
        if (toolbarButton != null) {
            toolbarButton.setVisibility(8);
        }
        ToolbarButton toolbarButton2 = this.mBtnLowerHand;
        if (toolbarButton2 != null) {
            toolbarButton2.setVisibility(8);
        }
    }

    public void show(boolean z, boolean z2) {
        TranslateAnimation translateAnimation;
        int i = 0;
        if ((getVisibility() == 0) != z) {
            if (z2) {
                if (z) {
                    setVisibility(0);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) getHeight(), 0.0f);
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            if (ConfToolbar.this.mListener != null) {
                                ConfToolbar.this.mListener.onToolbarVisibilityChanged(true);
                            }
                        }
                    });
                } else {
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) getHeight());
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            ConfToolbar.this.setVisibility(8);
                            if (ConfToolbar.this.mListener != null) {
                                ConfToolbar.this.mListener.onToolbarVisibilityChanged(false);
                            }
                        }
                    });
                }
                translateAnimation.setInterpolator(new DecelerateInterpolator());
                translateAnimation.setDuration(200);
                startAnimation(translateAnimation);
            } else {
                if (!z) {
                    i = 8;
                }
                setVisibility(i);
                Listener listener = this.mListener;
                if (listener != null) {
                    listener.onToolbarVisibilityChanged(z);
                }
            }
        }
    }

    public void focusFirstVisibleButton() {
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            View childAt = getChildAt(i);
            if (childAt == null || childAt.getVisibility() != 0) {
                i++;
            } else {
                childAt.requestFocus();
                return;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x006b  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:57:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void focus(int r2) {
        /*
            r1 = this;
            r0 = 4
            if (r2 == r0) goto L_0x002e
            r0 = 8
            if (r2 == r0) goto L_0x004f
            r0 = 16
            if (r2 == r0) goto L_0x008c
            r0 = 32
            if (r2 == r0) goto L_0x0056
            r0 = 64
            if (r2 == r0) goto L_0x005d
            r0 = 128(0x80, float:1.794E-43)
            if (r2 == r0) goto L_0x007e
            r0 = 256(0x100, float:3.59E-43)
            if (r2 == r0) goto L_0x0085
            switch(r2) {
                case 1: goto L_0x0027;
                case 2: goto L_0x0020;
                default: goto L_0x001e;
            }
        L_0x001e:
            goto L_0x0093
        L_0x0020:
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnAudio
            if (r2 == 0) goto L_0x0027
            r2.requestFocus()
        L_0x0027:
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnVideo
            if (r2 == 0) goto L_0x002e
            r2.requestFocus()
        L_0x002e:
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnShare
            if (r2 == 0) goto L_0x004f
            com.zipow.videobox.view.ToolbarButton r0 = r1.mBtnStopShare
            if (r0 == 0) goto L_0x004f
            int r2 = r2.getVisibility()
            if (r2 != 0) goto L_0x0042
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnShare
            r2.requestFocus()
            goto L_0x004f
        L_0x0042:
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnStopShare
            int r2 = r2.getVisibility()
            if (r2 != 0) goto L_0x004f
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnStopShare
            r2.requestFocus()
        L_0x004f:
            com.zipow.videobox.view.PListButton r2 = r1.mBtnPList
            if (r2 == 0) goto L_0x0056
            r2.requestFocus()
        L_0x0056:
            com.zipow.videobox.view.PListButton r2 = r1.mBtnMore
            if (r2 == 0) goto L_0x005d
            r2.requestFocus()
        L_0x005d:
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnRaiseHand
            if (r2 == 0) goto L_0x007e
            com.zipow.videobox.view.ToolbarButton r0 = r1.mBtnLowerHand
            if (r0 == 0) goto L_0x007e
            int r2 = r2.getVisibility()
            if (r2 != 0) goto L_0x0071
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnRaiseHand
            r2.requestFocus()
            goto L_0x007e
        L_0x0071:
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnLowerHand
            int r2 = r2.getVisibility()
            if (r2 != 0) goto L_0x007e
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnLowerHand
            r2.requestFocus()
        L_0x007e:
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnQA
            if (r2 == 0) goto L_0x0085
            r2.requestFocus()
        L_0x0085:
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnQA
            if (r2 == 0) goto L_0x008c
            r2.requestFocus()
        L_0x008c:
            com.zipow.videobox.view.ToolbarButton r2 = r1.mBtnLeave
            if (r2 == 0) goto L_0x0093
            r2.requestFocus()
        L_0x0093:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.ConfToolbar.focus(int):void");
    }
}

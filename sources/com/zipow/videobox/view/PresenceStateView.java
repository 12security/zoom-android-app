package com.zipow.videobox.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.PresenceStateHelper;
import java.util.List;
import p021us.zoom.videomeetings.C4558R;

public class PresenceStateView extends LinearLayout {
    /* access modifiers changed from: private */
    @Nullable
    public String buddyJID;
    private boolean mDarkMode;
    @NonNull
    private Handler mHandler;
    private ImageView mImgPresenceStatus;
    private TextView mTxtDeviceType;
    /* access modifiers changed from: private */
    @NonNull
    public IZoomMessengerUIListener mZoomMessengerUIListener;
    /* access modifiers changed from: private */
    @Nullable
    public String subPresenceMethod;

    public PresenceStateView(@NonNull Context context) {
        this(context, null);
    }

    public PresenceStateView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PresenceStateView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.buddyJID = "";
        this.subPresenceMethod = "";
        this.mHandler = new Handler();
        this.mDarkMode = false;
        this.mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
            public void Indicate_TPV2_WillExpirePresence(@Nullable List<String> list, int i) {
                if (i == 3 && list != null) {
                    PresenceStateHelper.getInstance().unSubscribe(list);
                    if (!ViewCompat.isAttachedToWindow(PresenceStateView.this)) {
                        ZoomMessengerUI.getInstance().removeListener(PresenceStateView.this.mZoomMessengerUIListener);
                    } else if (list.contains(PresenceStateView.this.buddyJID)) {
                        PresenceStateHelper.getInstance().subscribe(PresenceStateView.this.buddyJID, PresenceStateView.this.subPresenceMethod);
                    }
                }
            }

            public void onConnectReturn(int i) {
                if (i == 0) {
                    PresenceStateHelper.getInstance().subscribe(PresenceStateView.this.buddyJID, PresenceStateView.this.subPresenceMethod);
                }
            }
        };
        initView(context, attributeSet);
    }

    @TargetApi(21)
    public PresenceStateView(@NonNull Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.buddyJID = "";
        this.subPresenceMethod = "";
        this.mHandler = new Handler();
        this.mDarkMode = false;
        this.mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
            public void Indicate_TPV2_WillExpirePresence(@Nullable List<String> list, int i) {
                if (i == 3 && list != null) {
                    PresenceStateHelper.getInstance().unSubscribe(list);
                    if (!ViewCompat.isAttachedToWindow(PresenceStateView.this)) {
                        ZoomMessengerUI.getInstance().removeListener(PresenceStateView.this.mZoomMessengerUIListener);
                    } else if (list.contains(PresenceStateView.this.buddyJID)) {
                        PresenceStateHelper.getInstance().subscribe(PresenceStateView.this.buddyJID, PresenceStateView.this.subPresenceMethod);
                    }
                }
            }

            public void onConnectReturn(int i) {
                if (i == 0) {
                    PresenceStateHelper.getInstance().subscribe(PresenceStateView.this.buddyJID, PresenceStateView.this.subPresenceMethod);
                }
            }
        };
        initView(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
    }

    private void initView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        inflate(context, C4558R.layout.zm_mm_presence_state_view, this);
        this.mTxtDeviceType = (TextView) findViewById(C4558R.C4560id.txtDeviceType);
        this.mImgPresenceStatus = (ImageView) findViewById(C4558R.C4560id.imgPresences);
        setVisibility(8);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4558R.styleable.SubPresence);
            this.subPresenceMethod = obtainStyledAttributes.getString(C4558R.styleable.SubPresence_zm_subpresence_type);
            obtainStyledAttributes.recycle();
        }
    }

    public void setmTxtDeviceTypeGone() {
        this.mTxtDeviceType.setVisibility(8);
    }

    public String getTxtDeviceTypeText() {
        return this.mTxtDeviceType.getText().toString();
    }

    public void setState(IMAddrBookItem iMAddrBookItem) {
        int i;
        int i2;
        if (iMAddrBookItem == null) {
            setVisibility(8);
            return;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            setVisibility(8);
            return;
        }
        String jid = iMAddrBookItem.getJid();
        if (!TextUtils.isEmpty(jid) && jid.charAt(0) == '!') {
            jid = jid.substring(1);
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(jid);
        if (buddyWithJID == null) {
            setVisibility(8);
            return;
        }
        this.buddyJID = buddyWithJID.getJid();
        this.mHandler.removeCallbacksAndMessages(null);
        if (zoomMessenger.isConnectionGood()) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    PresenceStateHelper.getInstance().subscribe(PresenceStateView.this.buddyJID, PresenceStateView.this.subPresenceMethod);
                }
            }, 200);
        }
        if (iMAddrBookItem.getAccountStatus() == 0 || isDarkMode()) {
            this.mImgPresenceStatus.setVisibility(0);
            if (iMAddrBookItem.isBlocked()) {
                this.mTxtDeviceType.setText(C4558R.string.zm_lbl_blocked);
                TextView textView = this.mTxtDeviceType;
                textView.setTextColor(textView.getResources().getColor(C4558R.color.zm_mm_presence_busy));
                setVisibility(0);
                this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_ic_buddy_blocked_ondark : C4558R.C4559drawable.zm_ic_buddy_blocked);
                ImageView imageView = this.mImgPresenceStatus;
                imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_block));
                return;
            }
            if (zoomMessenger.isConnectionGood() && (iMAddrBookItem.getIsDesktopOnline() || iMAddrBookItem.getIsMobileOnline() || iMAddrBookItem.getIsRobot())) {
                switch (buddyWithJID.getPresence()) {
                    case 1:
                        this.mTxtDeviceType.setText(C4558R.string.zm_lbl_desktop_away);
                        TextView textView2 = this.mTxtDeviceType;
                        textView2.setTextColor(textView2.getResources().getColor(C4558R.color.zm_mm_presence_away));
                        setVisibility(0);
                        this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_away_ondark : C4558R.C4559drawable.zm_away);
                        ImageView imageView2 = this.mImgPresenceStatus;
                        imageView2.setContentDescription(imageView2.getResources().getString(C4558R.string.zm_description_mm_presence_away_40739));
                        break;
                    case 2:
                        if (buddyWithJID.getPresenceStatus() == 1) {
                            this.mTxtDeviceType.setText(C4558R.string.zm_lbl_presence_dnd_33945);
                            ImageView imageView3 = this.mImgPresenceStatus;
                            imageView3.setContentDescription(imageView3.getResources().getString(C4558R.string.zm_description_mm_presence_dnd_33945));
                            this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_mm_presence_inmeeting_ondark : C4558R.C4559drawable.zm_mm_presence_inmeeting);
                        } else if (buddyWithJID.getPresenceStatus() == 3) {
                            this.mTxtDeviceType.setText(C4558R.string.zm_lbl_presence_dnd_64479);
                            ImageView imageView4 = this.mImgPresenceStatus;
                            imageView4.setContentDescription(imageView4.getResources().getString(C4558R.string.zm_lbl_presence_dnd_64479));
                            this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_mm_presence_oncall_ondark : C4558R.C4559drawable.zm_mm_presence_oncall_normal);
                        } else if (buddyWithJID.getPresenceStatus() == 2) {
                            this.mTxtDeviceType.setText(C4558R.string.zm_lbl_presence_calendar_69119);
                            ImageView imageView5 = this.mImgPresenceStatus;
                            imageView5.setContentDescription(imageView5.getResources().getString(C4558R.string.zm_lbl_presence_calendar_69119));
                            this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_status_in_calendar_ondark_3 : C4558R.C4559drawable.zm_status_in_calendar_3);
                        } else if (buddyWithJID.getPresenceStatus() == 4) {
                            this.mTxtDeviceType.setText(C4558R.string.zm_title_hint_sharing_screen_text_93141);
                            ImageView imageView6 = this.mImgPresenceStatus;
                            imageView6.setContentDescription(imageView6.getResources().getString(C4558R.string.zm_title_hint_sharing_screen_text_93141));
                            this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_mm_presence_inmeeting_ondark : C4558R.C4559drawable.zm_mm_presence_inmeeting);
                        } else {
                            this.mTxtDeviceType.setText(C4558R.string.zm_lbl_presence_dnd_19903);
                            ImageView imageView7 = this.mImgPresenceStatus;
                            imageView7.setContentDescription(imageView7.getResources().getString(C4558R.string.zm_description_mm_presence_dnd_19903));
                            this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_mm_presence_inmeeting_ondark : C4558R.C4559drawable.zm_mm_presence_inmeeting);
                        }
                        TextView textView3 = this.mTxtDeviceType;
                        textView3.setTextColor(textView3.getResources().getColor(C4558R.color.zm_mm_presence_busy));
                        setVisibility(0);
                        break;
                    case 3:
                        int i3 = this.mDarkMode ? C4558R.C4559drawable.zm_status_available_ondark : C4558R.C4559drawable.zm_status_available;
                        if (iMAddrBookItem.getIsDesktopOnline() || iMAddrBookItem.getIsRobot()) {
                            i = C4558R.string.zm_lbl_desktop_online_33945;
                        } else {
                            i = iMAddrBookItem.isZoomRoomContact() ? C4558R.string.zm_lbl_room_online_33945 : C4558R.string.zm_lbl_mobile_online_33945;
                            i3 = this.mDarkMode ? C4558R.C4559drawable.zm_status_mobileonline_ondark : C4558R.C4559drawable.zm_status_mobileonline;
                        }
                        this.mTxtDeviceType.setText(i);
                        TextView textView4 = this.mTxtDeviceType;
                        textView4.setTextColor(textView4.getResources().getColor(C4558R.color.zm_mm_presence_available));
                        setVisibility(0);
                        this.mImgPresenceStatus.setImageResource(i3);
                        ImageView imageView8 = this.mImgPresenceStatus;
                        imageView8.setContentDescription(imageView8.getResources().getString(C4558R.string.zm_description_mm_presence_available));
                        break;
                    case 4:
                        this.mTxtDeviceType.setText(C4558R.string.zm_lbl_presence_xa_19903);
                        TextView textView5 = this.mTxtDeviceType;
                        textView5.setTextColor(textView5.getResources().getColor(C4558R.color.zm_mm_presence_busy));
                        setVisibility(0);
                        this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_status_dnd_ondark : C4558R.C4559drawable.zm_status_dnd);
                        ImageView imageView9 = this.mImgPresenceStatus;
                        imageView9.setContentDescription(imageView9.getResources().getString(C4558R.string.zm_description_mm_presence_xa_19903));
                        break;
                    default:
                        if (!iMAddrBookItem.getIsMobileOnline()) {
                            if (!buddyWithJID.isPresenceSynced()) {
                                this.mTxtDeviceType.setText(iMAddrBookItem.isZoomRoomContact() ? C4558R.string.zm_lbl_room_offline_33945 : C4558R.string.zm_lbl_desktop_offline_33945);
                                TextView textView6 = this.mTxtDeviceType;
                                textView6.setTextColor(textView6.getResources().getColor(C4558R.color.zm_mm_presence_offline));
                                setVisibility(0);
                                this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_offline_ondark : C4558R.C4559drawable.zm_offline);
                                ImageView imageView10 = this.mImgPresenceStatus;
                                imageView10.setContentDescription(imageView10.getResources().getString(C4558R.string.zm_description_mm_presence_offline));
                                break;
                            }
                        } else {
                            this.mTxtDeviceType.setText(C4558R.string.zm_lbl_mobile_online_33945);
                            TextView textView7 = this.mTxtDeviceType;
                            textView7.setTextColor(textView7.getResources().getColor(C4558R.color.zm_mm_presence_available));
                            setVisibility(0);
                            ImageView imageView11 = this.mImgPresenceStatus;
                            if (this.mDarkMode) {
                                i2 = C4558R.C4559drawable.zm_status_mobileonline_ondark;
                            } else {
                                i2 = C4558R.C4559drawable.zm_status_mobileonline;
                            }
                            imageView11.setImageResource(i2);
                            ImageView imageView12 = this.mImgPresenceStatus;
                            imageView12.setContentDescription(imageView12.getResources().getString(C4558R.string.zm_description_mm_presence_available));
                            break;
                        }
                        break;
                }
            } else if (zoomMessenger.isConnectionGood() || !iMAddrBookItem.getIsMobileOnline()) {
                this.mTxtDeviceType.setText(iMAddrBookItem.isZoomRoomContact() ? C4558R.string.zm_lbl_room_offline_33945 : C4558R.string.zm_lbl_desktop_offline_33945);
                TextView textView8 = this.mTxtDeviceType;
                textView8.setTextColor(textView8.getResources().getColor(C4558R.color.zm_mm_presence_offline));
                setVisibility(0);
                this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_offline_ondark : C4558R.C4559drawable.zm_offline);
                ImageView imageView13 = this.mImgPresenceStatus;
                imageView13.setContentDescription(imageView13.getResources().getString(C4558R.string.zm_description_mm_presence_offline));
            } else {
                this.mTxtDeviceType.setText(C4558R.string.zm_lbl_mobile_offline_33945);
                TextView textView9 = this.mTxtDeviceType;
                textView9.setTextColor(textView9.getResources().getColor(C4558R.color.zm_mm_presence_offline));
                setVisibility(0);
                this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_offline_ondark : C4558R.C4559drawable.zm_offline);
                ImageView imageView14 = this.mImgPresenceStatus;
                imageView14.setContentDescription(imageView14.getResources().getString(C4558R.string.zm_description_mm_presence_offline));
            }
            return;
        }
        this.mTxtDeviceType.setVisibility(8);
        this.mImgPresenceStatus.setVisibility(8);
    }

    public void setState(int i, int i2) {
        int i3;
        switch (i) {
            case 1:
                this.mTxtDeviceType.setText(C4558R.string.zm_lbl_desktop_away);
                TextView textView = this.mTxtDeviceType;
                textView.setTextColor(textView.getResources().getColor(C4558R.color.zm_mm_presence_away));
                setVisibility(0);
                this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_away_ondark : C4558R.C4559drawable.zm_away);
                ImageView imageView = this.mImgPresenceStatus;
                imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_away_40739));
                return;
            case 2:
                if (i2 == 1) {
                    this.mTxtDeviceType.setText(C4558R.string.zm_lbl_presence_dnd_33945);
                    ImageView imageView2 = this.mImgPresenceStatus;
                    imageView2.setContentDescription(imageView2.getResources().getString(C4558R.string.zm_description_mm_presence_dnd_33945));
                    this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_mm_presence_inmeeting_ondark : C4558R.C4559drawable.zm_mm_presence_inmeeting);
                } else if (i2 == 3) {
                    this.mTxtDeviceType.setText(C4558R.string.zm_lbl_presence_dnd_64479);
                    ImageView imageView3 = this.mImgPresenceStatus;
                    imageView3.setContentDescription(imageView3.getResources().getString(C4558R.string.zm_lbl_presence_dnd_64479));
                    this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_mm_presence_oncall_ondark : C4558R.C4559drawable.zm_mm_presence_oncall_normal);
                } else if (i2 == 2) {
                    this.mTxtDeviceType.setText(C4558R.string.zm_lbl_presence_calendar_69119);
                    ImageView imageView4 = this.mImgPresenceStatus;
                    imageView4.setContentDescription(imageView4.getResources().getString(C4558R.string.zm_lbl_presence_calendar_69119));
                    this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_status_in_calendar_ondark_3 : C4558R.C4559drawable.zm_status_in_calendar_3);
                } else {
                    this.mTxtDeviceType.setText(C4558R.string.zm_lbl_presence_dnd_19903);
                    ImageView imageView5 = this.mImgPresenceStatus;
                    imageView5.setContentDescription(imageView5.getResources().getString(C4558R.string.zm_description_mm_presence_dnd_19903));
                    this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_mm_presence_inmeeting_ondark : C4558R.C4559drawable.zm_mm_presence_inmeeting);
                }
                TextView textView2 = this.mTxtDeviceType;
                textView2.setTextColor(textView2.getResources().getColor(C4558R.color.zm_mm_presence_busy));
                setVisibility(0);
                return;
            case 3:
                this.mTxtDeviceType.setText(C4558R.string.zm_lbl_mobile_online_33945);
                TextView textView3 = this.mTxtDeviceType;
                textView3.setTextColor(textView3.getResources().getColor(C4558R.color.zm_mm_presence_available));
                setVisibility(0);
                this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_status_mobileonline_ondark : C4558R.C4559drawable.zm_status_mobileonline);
                ImageView imageView6 = this.mImgPresenceStatus;
                imageView6.setContentDescription(imageView6.getResources().getString(C4558R.string.zm_description_mm_presence_available));
                return;
            case 4:
                this.mTxtDeviceType.setText(C4558R.string.zm_lbl_presence_xa_19903);
                TextView textView4 = this.mTxtDeviceType;
                textView4.setTextColor(textView4.getResources().getColor(C4558R.color.zm_mm_presence_busy));
                setVisibility(0);
                this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_status_dnd_ondark : C4558R.C4559drawable.zm_status_dnd);
                ImageView imageView7 = this.mImgPresenceStatus;
                imageView7.setContentDescription(imageView7.getResources().getString(C4558R.string.zm_description_mm_presence_xa_19903));
                return;
            default:
                this.mTxtDeviceType.setText(C4558R.string.zm_lbl_mobile_offline_33945);
                TextView textView5 = this.mTxtDeviceType;
                textView5.setTextColor(textView5.getResources().getColor(C4558R.color.zm_mm_presence_offline));
                setVisibility(0);
                ImageView imageView8 = this.mImgPresenceStatus;
                if (this.mDarkMode) {
                    i3 = C4558R.C4559drawable.zm_offline_ondark;
                } else {
                    i3 = C4558R.C4559drawable.zm_offline;
                }
                imageView8.setImageResource(i3);
                ImageView imageView9 = this.mImgPresenceStatus;
                imageView9.setContentDescription(imageView9.getResources().getString(C4558R.string.zm_description_mm_presence_offline));
                return;
        }
    }

    public boolean setState(int i) {
        if (this.mImgPresenceStatus == null) {
            setVisibility(8);
            return false;
        }
        boolean z = true;
        setVisibility(0);
        this.mTxtDeviceType.setVisibility(8);
        if (i != 0) {
            switch (i) {
                case 2:
                    this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_away_ondark : C4558R.C4559drawable.zm_away);
                    ImageView imageView = this.mImgPresenceStatus;
                    imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_away_40739));
                    break;
                case 3:
                    this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_status_idle_ondark : C4558R.C4559drawable.zm_status_idle);
                    ImageView imageView2 = this.mImgPresenceStatus;
                    imageView2.setContentDescription(imageView2.getResources().getString(C4558R.string.zm_description_mm_presence_dnd_19903));
                    break;
                case 4:
                    this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_status_dnd_ondark : C4558R.C4559drawable.zm_status_dnd);
                    ImageView imageView3 = this.mImgPresenceStatus;
                    imageView3.setContentDescription(imageView3.getResources().getString(C4558R.string.zm_description_mm_presence_xa_19903));
                    break;
                default:
                    this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_offline_ondark : C4558R.C4559drawable.zm_offline);
                    ImageView imageView4 = this.mImgPresenceStatus;
                    imageView4.setContentDescription(imageView4.getResources().getString(C4558R.string.zm_description_mm_presence_offline));
                    z = false;
                    break;
            }
        } else {
            this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_status_available_ondark : C4558R.C4559drawable.zm_status_available);
            ImageView imageView5 = this.mImgPresenceStatus;
            imageView5.setContentDescription(imageView5.getResources().getString(C4558R.string.zm_description_mm_presence_available));
        }
        return z;
    }

    public void resetState() {
        TextView textView = this.mTxtDeviceType;
        textView.setTextColor(textView.getResources().getColor(C4558R.color.zm_mm_presence_offline));
        this.mImgPresenceStatus.setImageResource(this.mDarkMode ? C4558R.C4559drawable.zm_offline_ondark : C4558R.C4559drawable.zm_offline);
        ImageView imageView = this.mImgPresenceStatus;
        imageView.setContentDescription(imageView.getResources().getString(C4558R.string.zm_description_mm_presence_offline));
    }

    public boolean isDarkMode() {
        return this.mDarkMode;
    }

    public void setDarkMode(boolean z) {
        this.mDarkMode = z;
    }
}

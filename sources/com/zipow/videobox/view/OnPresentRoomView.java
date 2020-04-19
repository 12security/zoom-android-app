package com.zipow.videobox.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.meeting.confhelper.ShareOptionType;
import com.zipow.videobox.ptapp.PTUI.IPresentToRoomStatusListener;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.videomeetings.C4558R;

public class OnPresentRoomView extends LinearLayout implements IPresentToRoomStatusListener, OnClickListener {
    private static final int ShareStatus_None = 0;
    private static final int ShareStatus_Started = 2;
    private static final int ShareStatus_Starting = 1;
    private static final String TAG = "OnPresentRoomView";
    private View btnClose;
    private View btnStopShare;
    private int mShareStatus = 0;
    private View sharingView;
    private View waitingView;

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(@NonNull Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int shareStatus;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(@NonNull Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.shareStatus);
        }

        @NonNull
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("OnPresentRoomView.SavedState{ mShareStatus=");
            sb.append(this.shareStatus);
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append("}");
            return sb3.toString();
        }

        private SavedState(@NonNull Parcel parcel) {
            super(parcel);
            this.shareStatus = parcel.readInt();
        }
    }

    public OnPresentRoomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public OnPresentRoomView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, C4558R.layout.zm_on_present_room_view, this);
        this.waitingView = findViewById(C4558R.C4560id.waitingView);
        this.btnClose = this.waitingView.findViewById(C4558R.C4560id.btnClose);
        this.waitingView.setOnClickListener(this);
        this.btnClose.setOnClickListener(this);
        this.sharingView = findViewById(C4558R.C4560id.frSharingView);
        this.btnStopShare = this.sharingView.findViewById(C4558R.C4560id.btnStopShare);
        this.sharingView.setOnClickListener(this);
        this.btnStopShare.setOnClickListener(this);
        updateShareStatus(this.mShareStatus);
    }

    public void setTitlePadding(int i, int i2, int i3, int i4) {
        View view = this.btnClose;
        if (view != null) {
            view.setPadding(i, i2, i3, i4);
        }
    }

    private void updateShareStatus(int i) {
        this.mShareStatus = i;
        switch (i) {
            case 0:
            case 1:
                this.waitingView.setVisibility(0);
                this.sharingView.setVisibility(8);
                return;
            case 2:
                this.waitingView.setVisibility(8);
                this.sharingView.setVisibility(0);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        updateShareStatus(savedState.shareStatus);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.shareStatus = this.mShareStatus;
        return savedState;
    }

    public void presentToRoomStatusUpdate(int i) {
        switch (i) {
            case 20:
                this.waitingView.setVisibility(0);
                this.sharingView.setVisibility(8);
                AccessibilityUtil.sendAccessibilityFocusEvent(this.waitingView);
                return;
            case 21:
                AccessibilityUtil.sendAccessibilityFocusEvent(this.waitingView);
                return;
            default:
                return;
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnClose || id == C4558R.C4560id.btnStopShare) {
            PTAppDelegation.getInstance().stopPresentToRoom(false);
        }
    }

    public boolean canShare() {
        return this.mShareStatus == 0;
    }

    public void startShare() {
        this.mShareStatus = 1;
        ShareTip.selectShareType(ShareOptionType.SHARE_SCREEN);
        this.waitingView.setVisibility(0);
        this.sharingView.setVisibility(8);
        AccessibilityUtil.sendAccessibilityFocusEvent(this.waitingView);
    }

    public void startShareOK() {
        this.mShareStatus = 2;
        this.waitingView.setVisibility(8);
        this.sharingView.setVisibility(0);
        AccessibilityUtil.sendAccessibilityFocusEvent(this.sharingView);
    }

    public void finishShare() {
        this.mShareStatus = 0;
        this.waitingView.setVisibility(0);
        this.sharingView.setVisibility(8);
    }
}

package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IRoomCallListener;
import com.zipow.videobox.ptapp.RoomDevice;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class RoomSystemCallOutView extends ScrollView implements TextWatcher, OnClickListener, IRoomCallListener {
    private static final String CALLOUT_ERROR_CODE = "callout_error_code";
    private static final String CALLOUT_STATE = "callout_state";
    private static final String CALLOUT_TYPE = "callout_type";
    private static final String CALLOUT_VIEW_STATE = "callout_view_state";
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTTING = 1;
    private static final int STATE_FAILED = 3;
    private static final int STATE_INITIALED = 0;
    private static final String TAG = "RoomSystemCallOutView";
    private TextView mAddressInputPromt;
    /* access modifiers changed from: private */
    public RoomDeviceAutoCompleteTextView mAutoCompleteRoomDevice;
    private ImageButton mBtnRoomDeviceDropdown;
    private Button mCallOutBtn;
    private Button mCancelBtn;
    private Context mContext;
    private long mErrCode;
    private View mImgH323;
    private View mImgSip;
    private RoomSystemCallViewListener mListener;
    private TextView mNotification;
    private View mPanelH323;
    private View mPanelSip;
    private int mRoomDeviceType = 2;
    @NonNull
    private List<RoomDevice> mRoomDevices = new ArrayList();
    private int mState;

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public RoomSystemCallOutView(Context context) {
        super(context);
        initView(context, null);
    }

    public RoomSystemCallOutView(Context context, Bundle bundle) {
        super(context);
        initView(context, bundle);
    }

    public RoomSystemCallOutView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context, null);
    }

    public RoomSystemCallOutView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context, null);
    }

    private void initView(Context context, Bundle bundle) {
        LayoutInflater from = LayoutInflater.from(context);
        this.mContext = context;
        if (from != null) {
            View inflate = from.inflate(C4558R.layout.zm_room_system_call_out_view, this, true);
            this.mNotification = (TextView) inflate.findViewById(C4558R.C4560id.txtNotification);
            this.mAutoCompleteRoomDevice = (RoomDeviceAutoCompleteTextView) inflate.findViewById(C4558R.C4560id.edtRoomDevice);
            this.mAddressInputPromt = (TextView) inflate.findViewById(C4558R.C4560id.txtAddressPromt);
            this.mBtnRoomDeviceDropdown = (ImageButton) inflate.findViewById(C4558R.C4560id.btnRoomDeviceDropdown);
            this.mPanelH323 = findViewById(C4558R.C4560id.panelH323);
            this.mImgH323 = findViewById(C4558R.C4560id.imgH323);
            this.mPanelSip = findViewById(C4558R.C4560id.panelSip);
            this.mImgSip = findViewById(C4558R.C4560id.imgSip);
            this.mCallOutBtn = (Button) inflate.findViewById(C4558R.C4560id.btnCall);
            this.mCancelBtn = (Button) inflate.findViewById(C4558R.C4560id.btnCancel);
            this.mState = 0;
            initial(bundle);
            this.mAutoCompleteRoomDevice.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View view, boolean z) {
                    if (z && StringUtil.isEmptyOrNull(RoomSystemCallOutView.this.mAutoCompleteRoomDevice.getText().toString())) {
                        RoomSystemCallOutView.this.mAutoCompleteRoomDevice.performFilter("");
                    }
                }
            });
            PTApp.getInstance().setNeedFilterCallRoomEventCallbackInMeeting(true);
        }
    }

    public void initialUIListener() {
        this.mAutoCompleteRoomDevice.addTextChangedListener(this);
        this.mPanelH323.setOnClickListener(this);
        this.mPanelSip.setOnClickListener(this);
        this.mCallOutBtn.setOnClickListener(this);
        this.mCancelBtn.setOnClickListener(this);
        this.mBtnRoomDeviceDropdown.setOnClickListener(this);
        PTUI.getInstance().addRoomCallListener(this);
    }

    private void initial(@Nullable Bundle bundle) {
        if (bundle != null) {
            SparseArray sparseParcelableArray = bundle.getSparseParcelableArray(CALLOUT_VIEW_STATE);
            if (sparseParcelableArray != null) {
                try {
                    restoreHierarchyState(sparseParcelableArray);
                } catch (Exception unused) {
                }
            }
            this.mState = bundle.getInt(CALLOUT_STATE, 0);
            this.mRoomDeviceType = bundle.getInt(CALLOUT_TYPE, 1);
            this.mErrCode = bundle.getLong(CALLOUT_ERROR_CODE);
        }
        loadAllRoomDevices();
        refreshUI();
    }

    @NonNull
    public Bundle getSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt(CALLOUT_STATE, this.mState);
        bundle.putInt(CALLOUT_TYPE, this.mRoomDeviceType);
        bundle.putLong(CALLOUT_ERROR_CODE, this.mErrCode);
        SparseArray sparseArray = new SparseArray();
        saveHierarchyState(sparseArray);
        bundle.putSparseParcelableArray(CALLOUT_VIEW_STATE, sparseArray);
        return bundle;
    }

    public void setListener(RoomSystemCallViewListener roomSystemCallViewListener) {
        this.mListener = roomSystemCallViewListener;
    }

    public void destroy() {
        PTUI.getInstance().removeRoomCallListener(this);
        PTApp.getInstance().setNeedFilterCallRoomEventCallbackInMeeting(false);
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            if (view == this.mCallOutBtn) {
                onClickCallOut();
                UIUtil.closeSoftKeyboard(this.mContext, this);
            } else if (view == this.mCancelBtn) {
                onClickCancel();
            } else if (view == this.mPanelH323) {
                onClickH323();
            } else if (view == this.mPanelSip) {
                onClickSip();
            } else if (view == this.mBtnRoomDeviceDropdown) {
                onClickDevicesDropdown();
            }
            refreshUI();
        }
    }

    private boolean checkInput() {
        if (StringUtil.isEmptyOrNull(this.mAutoCompleteRoomDevice.getText().toString())) {
            this.mCallOutBtn.setEnabled(false);
            return false;
        }
        this.mCallOutBtn.setEnabled(true);
        return true;
    }

    private void onClickCallOut() {
        if (checkInput()) {
            String obj = this.mAutoCompleteRoomDevice.getText().toString();
            Iterator it = this.mRoomDevices.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                RoomDevice roomDevice = (RoomDevice) it.next();
                if (obj.equals(roomDevice.getDisplayName())) {
                    obj = roomDevice.getAddress();
                    break;
                }
            }
            callOutRoomSystem(obj.trim(), this.mRoomDeviceType, 2);
        }
    }

    private void callOutRoomSystem(String str, int i, int i2) {
        if (!StringUtil.isEmptyOrNull(str)) {
            MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
            if (meetingHelper != null) {
                if (meetingHelper.callOutRoomSystem(str, i, i2)) {
                    this.mState = 1;
                    RoomSystemCallViewListener roomSystemCallViewListener = this.mListener;
                    if (roomSystemCallViewListener != null) {
                        roomSystemCallViewListener.onConnecting(true);
                    }
                } else {
                    this.mState = 3;
                }
            }
        }
    }

    private void onClickCancel() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (this.mState == 1 && meetingHelper != null && meetingHelper.cancelRoomDevice()) {
            this.mState = 0;
        }
        RoomSystemCallViewListener roomSystemCallViewListener = this.mListener;
        if (roomSystemCallViewListener != null) {
            roomSystemCallViewListener.onCancel(true);
        }
    }

    private void onClickH323() {
        if (this.mRoomDeviceType != 1) {
            this.mRoomDeviceType = 1;
            this.mImgH323.setVisibility(0);
            this.mImgSip.setVisibility(8);
        }
    }

    private void onClickSip() {
        if (this.mRoomDeviceType != 2) {
            this.mRoomDeviceType = 2;
            this.mImgH323.setVisibility(8);
            this.mImgSip.setVisibility(0);
        }
    }

    private void onClickDevicesDropdown() {
        this.mAutoCompleteRoomDevice.performFilter(RoomDeviceAutoCompleteTextView.ALL_DEVICES_MODE);
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.mAutoCompleteRoomDevice.setSelected(true);
    }

    public void afterTextChanged(Editable editable) {
        this.mState = 0;
        refreshUI();
    }

    public void onRoomCallEvent(int i, long j, boolean z) {
        if (i == 8) {
            if (j == 0) {
                RoomSystemCallViewListener roomSystemCallViewListener = this.mListener;
                if (roomSystemCallViewListener != null) {
                    roomSystemCallViewListener.onConnected(true);
                }
                this.mState = 2;
            } else if (j >= 100) {
                this.mState = 3;
                this.mErrCode = j;
                RoomSystemCallViewListener roomSystemCallViewListener2 = this.mListener;
                if (roomSystemCallViewListener2 != null) {
                    roomSystemCallViewListener2.onFailed(true);
                }
            } else {
                this.mState = 1;
                this.mErrCode = j;
                RoomSystemCallViewListener roomSystemCallViewListener3 = this.mListener;
                if (roomSystemCallViewListener3 != null) {
                    roomSystemCallViewListener3.onConnecting(true);
                }
            }
            refreshUI();
        }
    }

    private void refreshUI() {
        switch (this.mState) {
            case 0:
            case 2:
                this.mNotification.setVisibility(4);
                this.mCallOutBtn.setVisibility(0);
                this.mCancelBtn.setVisibility(8);
                break;
            case 1:
                this.mNotification.setVisibility(0);
                this.mNotification.setBackgroundColor(getResources().getColor(C4558R.color.zm_notification_background_green));
                this.mNotification.setTextColor(getResources().getColor(C4558R.color.zm_white));
                this.mNotification.setText(C4558R.string.zm_room_system_notify_calling);
                this.mCallOutBtn.setVisibility(8);
                this.mCancelBtn.setVisibility(0);
                break;
            case 3:
                this.mNotification.setVisibility(0);
                this.mNotification.setBackgroundColor(getResources().getColor(C4558R.color.zm_notification_background));
                this.mNotification.setTextColor(getResources().getColor(C4558R.color.zm_notification_font_red));
                this.mNotification.setText(getResources().getString(C4558R.string.zm_room_system_notify_call_out_failed, new Object[]{Long.valueOf(this.mErrCode)}));
                this.mCallOutBtn.setVisibility(0);
                this.mCancelBtn.setVisibility(8);
                break;
        }
        if (this.mRoomDeviceType == 1) {
            this.mAddressInputPromt.setText(C4558R.string.zm_room_system_h323_input_instruction);
        } else {
            this.mAddressInputPromt.setText(C4558R.string.zm_room_system_sip_input_instruction);
        }
        if (this.mRoomDevices.size() > 0) {
            this.mBtnRoomDeviceDropdown.setVisibility(0);
        } else {
            this.mBtnRoomDeviceDropdown.setVisibility(8);
        }
        checkInput();
    }

    private void loadAllRoomDevices() {
        this.mRoomDevices.clear();
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            ArrayList<RoomDevice> arrayList = new ArrayList<>();
            if (meetingHelper.getRoomDevices(arrayList)) {
                for (RoomDevice roomDevice : arrayList) {
                    if (checkRoomDevice(roomDevice)) {
                        this.mRoomDevices.add(roomDevice);
                    }
                }
            }
        }
    }

    private boolean checkRoomDevice(@Nullable RoomDevice roomDevice) {
        if (roomDevice == null) {
            return false;
        }
        return !StringUtil.isEmptyOrNull(roomDevice.getIp()) || !StringUtil.isEmptyOrNull(roomDevice.getE164num());
    }
}

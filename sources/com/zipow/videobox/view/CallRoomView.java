package com.zipow.videobox.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.RoomDevice;
import com.zipow.videobox.util.UIMgr;
import java.util.ArrayList;
import java.util.Iterator;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class CallRoomView extends LinearLayout implements OnClickListener, OnEditorActionListener {
    /* access modifiers changed from: private */
    public RoomDeviceAutoCompleteTextView mAutoCompleteRoomDevice;
    private Button mBtnBack;
    /* access modifiers changed from: private */
    public Button mBtnCall;
    private ImageButton mBtnRoomDeviceDropdown;
    /* access modifiers changed from: private */
    @NonNull
    public ArrayList<RoomDevice> mDevices = new ArrayList<>();
    private View mImgH323;
    private View mImgSip;
    private Listener mListener;
    private View mPanelH323;
    private View mPanelSip;
    /* access modifiers changed from: private */
    @Nullable
    public RoomDevice mRoomDevice = null;
    private int mRoomDeviceType = 2;
    private TextView mTxtTitle;
    @Nullable
    private TextWatcher watcher;

    public static class CallRoomRecentMeetingsDialog extends ZMDialogFragment {
        private static final String ARGS_KEY_MEETINGLIST = "args_key_meetinglist";
        /* access modifiers changed from: private */
        public OnMeetingItemSelectListener mListener;

        public interface OnMeetingItemSelectListener {
            void onMeetingItemSelect(RoomDevice roomDevice);
        }

        @NonNull
        public static CallRoomRecentMeetingsDialog showAsDialog(@NonNull FragmentManager fragmentManager, @NonNull ArrayList<RoomDevice> arrayList) {
            CallRoomRecentMeetingsDialog callRoomRecentMeetingsDialog = new CallRoomRecentMeetingsDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARGS_KEY_MEETINGLIST, arrayList);
            callRoomRecentMeetingsDialog.setArguments(bundle);
            callRoomRecentMeetingsDialog.show(fragmentManager, CallRoomRecentMeetingsDialog.class.getName());
            return callRoomRecentMeetingsDialog;
        }

        public void setOnMeetingItemSelectListener(OnMeetingItemSelectListener onMeetingItemSelectListener) {
            this.mListener = onMeetingItemSelectListener;
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            View createContent = createContent();
            if (createContent == null) {
                return createEmptyDialog();
            }
            return new Builder(getActivity()).setCancelable(true).setView(createContent).setTheme(C4558R.style.ZMDialog_Material_Transparent).create();
        }

        private View createContent() {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return null;
            }
            ArrayList arrayList = (ArrayList) arguments.getSerializable(ARGS_KEY_MEETINGLIST);
            View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_select_room, null);
            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(C4558R.C4560id.panelMeetingNo);
            LayoutParams layoutParams = new LayoutParams(-1, UIUtil.dip2px(getActivity(), 35.0f));
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                final RoomDevice roomDevice = (RoomDevice) it.next();
                View inflate2 = View.inflate(getActivity(), C4558R.layout.zm_select_room_item, null);
                ((TextView) inflate2.findViewById(C4558R.C4560id.txtTopic)).setText(roomDevice.getDisplayName());
                linearLayout.addView(inflate2, layoutParams);
                inflate2.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (CallRoomRecentMeetingsDialog.this.mListener != null) {
                            CallRoomRecentMeetingsDialog.this.mListener.onMeetingItemSelect(roomDevice);
                        }
                        CallRoomRecentMeetingsDialog.this.dismiss();
                    }
                });
            }
            return inflate;
        }

        public void dismiss() {
            finishFragment(true);
        }
    }

    public static class CannotJoinDialog extends ZMDialogFragment {
        public CannotJoinDialog() {
            setCancelable(true);
        }

        public static void show(ZMActivity zMActivity, final String str) {
            zMActivity.getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                public void run(@NonNull IUIElement iUIElement) {
                    CannotJoinDialog cannotJoinDialog = new CannotJoinDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", str);
                    cannotJoinDialog.setArguments(bundle);
                    cannotJoinDialog.show(((ZMActivity) iUIElement).getSupportFragmentManager(), CannotJoinDialog.class.getName());
                }
            });
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return createEmptyDialog();
            }
            return new Builder(getActivity()).setTitle(C4558R.string.zm_alert_join_failed).setMessage(arguments.getString("message")).setNegativeButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
        }
    }

    public interface Listener {
        void onBack();
    }

    public CallRoomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public boolean onNestedPrePerformAccessibilityAction(View view, int i, Bundle bundle) {
        return super.onNestedPrePerformAccessibilityAction(view, i, bundle);
    }

    public CallRoomView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setRoomDevice(@Nullable RoomDevice roomDevice) {
        if (roomDevice != null) {
            this.mRoomDevice = roomDevice;
            setDisplayRoomDeviceType(roomDevice);
            this.mAutoCompleteRoomDevice.setText(this.mRoomDevice.getDisplayName());
            RoomDeviceAutoCompleteTextView roomDeviceAutoCompleteTextView = this.mAutoCompleteRoomDevice;
            roomDeviceAutoCompleteTextView.setSelection(roomDeviceAutoCompleteTextView.length());
            this.mAutoCompleteRoomDevice.clearFocus();
        }
    }

    private void initView() {
        View.inflate(getContext(), C4558R.layout.zm_call_room, this);
        this.mTxtTitle = (TextView) findViewById(C4558R.C4560id.txtTitle);
        this.mAutoCompleteRoomDevice = (RoomDeviceAutoCompleteTextView) findViewById(C4558R.C4560id.edtRoomDevice);
        this.mBtnCall = (Button) findViewById(C4558R.C4560id.btnCall);
        this.mBtnBack = (Button) findViewById(C4558R.C4560id.btnBack);
        this.mPanelH323 = findViewById(C4558R.C4560id.panelH323);
        this.mImgH323 = findViewById(C4558R.C4560id.imgH323);
        this.mPanelSip = findViewById(C4558R.C4560id.panelSip);
        this.mImgSip = findViewById(C4558R.C4560id.imgSip);
        this.mBtnCall.setEnabled(false);
        this.mBtnCall.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mPanelH323.setOnClickListener(this);
        this.mPanelSip.setOnClickListener(this);
        this.mBtnRoomDeviceDropdown = (ImageButton) findViewById(C4558R.C4560id.btnRoomDeviceDropdown);
        this.mBtnRoomDeviceDropdown.setOnClickListener(this);
        if (!hasCallHistory()) {
            this.mBtnRoomDeviceDropdown.setVisibility(8);
        }
        this.watcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                CallRoomView.this.mBtnCall.setEnabled(CallRoomView.this.mAutoCompleteRoomDevice.getText().toString().length() > 0);
                CallRoomView.this.mRoomDevice = null;
                String obj = CallRoomView.this.mAutoCompleteRoomDevice.getText().toString();
                if (CallRoomView.this.hasCallHistory() && !StringUtil.isEmptyOrNull(obj)) {
                    Iterator it = CallRoomView.this.mDevices.iterator();
                    while (it.hasNext()) {
                        RoomDevice roomDevice = (RoomDevice) it.next();
                        if (!obj.equalsIgnoreCase(roomDevice.getAddress())) {
                            if (obj.equalsIgnoreCase(roomDevice.getName())) {
                            }
                        }
                        CallRoomView.this.mRoomDevice = roomDevice;
                        CallRoomView callRoomView = CallRoomView.this;
                        callRoomView.setDisplayRoomDeviceType(callRoomView.mRoomDevice);
                        return;
                    }
                }
            }
        };
        this.mAutoCompleteRoomDevice.addTextChangedListener(this.watcher);
        if (UIMgr.isLargeMode(getContext())) {
            this.mBtnBack.setVisibility(8);
        }
        this.mAutoCompleteRoomDevice.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z && StringUtil.isEmptyOrNull(CallRoomView.this.mAutoCompleteRoomDevice.getText().toString())) {
                    CallRoomView.this.mAutoCompleteRoomDevice.performFilter("");
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean hasCallHistory() {
        if (PTApp.getInstance().getAllRoomSystemList(3, this.mDevices) && this.mDevices.size() != 0) {
            return true;
        }
        return false;
    }

    public void setTitle(int i) {
        this.mTxtTitle.setText(i);
    }

    public void setConfNumber(String str) {
        this.mAutoCompleteRoomDevice.setText(str);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnCall) {
            onClickJoin();
        } else if (id == C4558R.C4560id.btnBack || id == C4558R.C4560id.btnCancel) {
            onClickBack();
        } else if (id == C4558R.C4560id.btnRoomDeviceDropdown) {
            onClickDevicesDropdown();
        } else if (id == C4558R.C4560id.panelH323) {
            onClickH323();
        } else if (id == C4558R.C4560id.panelSip) {
            onClickSip();
        }
    }

    private void onClickDevicesDropdown() {
        this.mAutoCompleteRoomDevice.performFilter(RoomDeviceAutoCompleteTextView.ALL_DEVICES_MODE);
    }

    private void onClickH323() {
        this.mRoomDeviceType = 1;
        this.mImgH323.setVisibility(0);
        this.mImgSip.setVisibility(8);
    }

    private void onClickSip() {
        this.mRoomDeviceType = 2;
        this.mImgH323.setVisibility(8);
        this.mImgSip.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void setDisplayRoomDeviceType(@Nullable RoomDevice roomDevice) {
        if (roomDevice != null) {
            if (roomDevice.getDeviceType() == 1) {
                onClickH323();
            } else {
                onClickSip();
            }
        }
    }

    private void onClickJoin() {
        int i;
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            CannotJoinDialog.show((ZMActivity) getContext(), getResources().getString(C4558R.string.zm_alert_network_disconnected));
            return;
        }
        PTApp.getInstance().setVideoCallWithRoomSystemPrepareStatus(true);
        if (this.mListener != null) {
            UIUtil.closeSoftKeyboard(getContext(), this);
        }
        String obj = this.mAutoCompleteRoomDevice.getText().toString();
        if (this.mRoomDevice != null) {
            i = PTApp.getInstance().startVideoCallWithRoomSystem(this.mRoomDevice, 3, 0);
        } else {
            PTApp instance = PTApp.getInstance();
            RoomDevice roomDevice = new RoomDevice("", obj, "", this.mRoomDeviceType, 2);
            i = instance.startVideoCallWithRoomSystem(roomDevice, 3, 0);
        }
        if (i == 0) {
            this.mBtnCall.setEnabled(false);
        } else {
            this.mBtnCall.setEnabled(true);
        }
    }

    private void onClickBack() {
        if (this.mListener != null) {
            UIUtil.closeSoftKeyboard(getContext(), this);
            this.mListener.onBack();
        }
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 2) {
            return false;
        }
        onClickJoin();
        return true;
    }
}

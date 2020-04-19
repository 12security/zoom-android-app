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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IRoomCallListener;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import org.apache.http.message.TokenParser;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class RoomSystemCallInView extends ScrollView implements TextWatcher, OnClickListener, IRoomCallListener {
    private static final String CALLIN_ERROR_CODE = "callin_error_code";
    private static final String CALLIN_STATE = "callin_sate";
    private static final String CALLIN_VIEW_STATE = "callin_view_state";
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTTING = 1;
    private static final int STATE_FAILED = 3;
    private static final int STATE_INITIALED = 0;
    private static final String TAG = "RoomSystemCallInView";
    private Button mBtnInvite;
    private Context mContext;
    private EditText mEdtPairingCode = null;
    private long mErrCode;
    private View mH323Info;
    private TextView mH323Password;
    private RoomSystemCallViewListener mListener;
    private TextView mMeetingId;
    private TextView mNotification;
    private View mPasswordView;
    private int mState;
    private TextView mTxtH323Info;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public RoomSystemCallInView(Context context) {
        super(context);
        initView(context, null);
    }

    public RoomSystemCallInView(Context context, Bundle bundle) {
        super(context);
        initView(context, bundle);
    }

    public RoomSystemCallInView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context, null);
    }

    public RoomSystemCallInView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context, null);
    }

    private void initView(Context context, Bundle bundle) {
        LayoutInflater from = LayoutInflater.from(context);
        if (from != null) {
            this.mContext = context;
            View inflate = from.inflate(C4558R.layout.zm_room_system_call_in_view, this, true);
            this.mNotification = (TextView) inflate.findViewById(C4558R.C4560id.txtNotification);
            this.mEdtPairingCode = (EditText) inflate.findViewById(C4558R.C4560id.editPairingCode);
            this.mBtnInvite = (Button) inflate.findViewById(C4558R.C4560id.btnInvite);
            this.mH323Info = inflate.findViewById(C4558R.C4560id.vH323Info);
            this.mTxtH323Info = (TextView) inflate.findViewById(C4558R.C4560id.tH323IpInfo);
            this.mPasswordView = inflate.findViewById(C4558R.C4560id.vH323MeetingPassword);
            this.mH323Password = (TextView) inflate.findViewById(C4558R.C4560id.tH323MeetingPassword);
            this.mMeetingId = (TextView) inflate.findViewById(C4558R.C4560id.tH323MeetingID);
            initialInfo();
            this.mState = 0;
            initial(bundle);
        }
    }

    public void initialUIListener() {
        this.mEdtPairingCode.addTextChangedListener(this);
        this.mBtnInvite.setOnClickListener(this);
        PTUI.getInstance().addRoomCallListener(this);
    }

    private void initial(@Nullable Bundle bundle) {
        if (bundle != null) {
            SparseArray sparseParcelableArray = bundle.getSparseParcelableArray(CALLIN_VIEW_STATE);
            if (sparseParcelableArray != null) {
                try {
                    restoreHierarchyState(sparseParcelableArray);
                } catch (Exception unused) {
                }
            }
            this.mState = bundle.getInt(CALLIN_STATE, 0);
            this.mErrCode = bundle.getLong(CALLIN_ERROR_CODE);
        }
        refreshUI();
    }

    @NonNull
    public Bundle getSaveInstanceState() {
        Bundle bundle = new Bundle();
        SparseArray sparseArray = new SparseArray();
        saveHierarchyState(sparseArray);
        bundle.putSparseParcelableArray(CALLIN_VIEW_STATE, sparseArray);
        bundle.putInt(CALLIN_STATE, this.mState);
        bundle.putLong(CALLIN_ERROR_CODE, this.mErrCode);
        return bundle;
    }

    public void destroy() {
        PTUI.getInstance().removeRoomCallListener(this);
    }

    public void setListener(RoomSystemCallViewListener roomSystemCallViewListener) {
        this.mListener = roomSystemCallViewListener;
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            if (view == this.mBtnInvite) {
                onClickInvite();
                UIUtil.closeSoftKeyboard(this.mContext, this);
            }
            refreshUI();
        }
    }

    public void afterTextChanged(Editable editable) {
        this.mState = 0;
        refreshUI();
    }

    private boolean checkInput() {
        if (this.mState == 1 || StringUtil.isEmptyOrNull(this.mEdtPairingCode.getText().toString())) {
            this.mBtnInvite.setEnabled(false);
            return false;
        }
        this.mBtnInvite.setEnabled(true);
        return true;
    }

    private void onClickInvite() {
        if (checkInput()) {
            PTApp instance = PTApp.getInstance();
            long h323AccessCode = instance.getH323AccessCode();
            MeetingHelper meetingHelper = instance.getMeetingHelper();
            if (meetingHelper != null) {
                if (meetingHelper.sendMeetingParingCode(h323AccessCode, this.mEdtPairingCode.getText().toString().trim())) {
                    this.mState = 1;
                } else {
                    this.mState = 3;
                }
            }
        }
    }

    private void initialInfo() {
        PTApp instance = PTApp.getInstance();
        String formatConfNumber = StringUtil.formatConfNumber(instance.getH323AccessCode(), (char) TokenParser.f498SP);
        String h323Gateway = instance.getH323Gateway();
        String h323Password = instance.getH323Password();
        if (StringUtil.isEmptyOrNull(h323Gateway)) {
            this.mH323Info.setVisibility(8);
            return;
        }
        this.mH323Info.setVisibility(0);
        String[] split = h323Gateway.split(";");
        if (split.length > 1) {
            StringBuilder sb = new StringBuilder();
            int length = split.length;
            int i = 0;
            boolean z = true;
            while (i < length) {
                String str = split[i];
                if (!z) {
                    sb.append(FontStyleHelper.SPLITOR);
                }
                sb.append(str.trim());
                i++;
                z = false;
            }
            this.mTxtH323Info.setText(sb.toString());
        } else {
            this.mTxtH323Info.setText(h323Gateway);
        }
        this.mMeetingId.setText(formatConfNumber);
        if (StringUtil.isEmptyOrNull(h323Password)) {
            this.mPasswordView.setVisibility(8);
            return;
        }
        this.mPasswordView.setVisibility(0);
        this.mH323Password.setText(h323Password);
    }

    private void refreshUI() {
        switch (this.mState) {
            case 0:
            case 2:
                this.mNotification.setVisibility(4);
                break;
            case 1:
                this.mNotification.setVisibility(0);
                this.mNotification.setBackgroundColor(getResources().getColor(C4558R.color.zm_notification_background_green));
                this.mNotification.setTextColor(getResources().getColor(C4558R.color.zm_white));
                this.mNotification.setText(C4558R.string.zm_room_system_notify_inviting);
                break;
            case 3:
                this.mNotification.setVisibility(0);
                this.mNotification.setBackgroundColor(getResources().getColor(C4558R.color.zm_notification_background));
                this.mNotification.setTextColor(getResources().getColor(C4558R.color.zm_notification_font_red));
                this.mNotification.setText(getResources().getString(C4558R.string.zm_room_system_notify_invite_failed, new Object[]{Long.valueOf(this.mErrCode)}));
                break;
        }
        checkInput();
    }

    public void onRoomCallEvent(int i, long j, boolean z) {
        if (i == 7) {
            if (z) {
                if (j == 0) {
                    RoomSystemCallViewListener roomSystemCallViewListener = this.mListener;
                    if (roomSystemCallViewListener != null) {
                        roomSystemCallViewListener.onConnected(false);
                    }
                    this.mState = 2;
                } else {
                    this.mState = 3;
                    this.mErrCode = j;
                }
            }
            refreshUI();
        }
    }
}

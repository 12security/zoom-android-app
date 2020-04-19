package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class CustomStatusFragment extends ZMDialogFragment implements OnClickListener {
    private Button mBtnSave;
    private EditText mEdtCustomStatus;
    /* access modifiers changed from: private */
    public ImageView mImgClear;
    private IZoomMessengerUIListener mZoomMessengerListener;

    public static void showAsActivity(Fragment fragment, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(fragment, CustomStatusFragment.class.getName(), bundle, i, true, 1);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_custom_status, viewGroup, false);
        this.mEdtCustomStatus = (EditText) inflate.findViewById(C4558R.C4560id.edtCustomStatus);
        this.mBtnSave = (Button) inflate.findViewById(C4558R.C4560id.btnSave);
        this.mImgClear = (ImageView) inflate.findViewById(C4558R.C4560id.imgClear);
        this.mEdtCustomStatus.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                CustomStatusFragment.this.mImgClear.setVisibility(editable.length() > 0 ? 0 : 4);
            }
        });
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                this.mEdtCustomStatus.setText(myself.getSignature());
                if (!TextUtils.isEmpty(myself.getSignature())) {
                    this.mEdtCustomStatus.setSelection(myself.getSignature().length());
                }
            }
        }
        this.mImgClear.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        this.mBtnSave.setOnClickListener(this);
        this.mEdtCustomStatus.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                CustomStatusFragment.this.onClickBtnSave();
                return true;
            }
        });
        return inflate;
    }

    public void onResume() {
        super.onResume();
        if (this.mZoomMessengerListener == null) {
            this.mZoomMessengerListener = new SimpleZoomMessengerUIListener() {
                public void Indicate_VCardInfoReady(String str) {
                    CustomStatusFragment.this.Indicate_VCardInfoReady(str);
                }
            };
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerListener);
    }

    /* access modifiers changed from: private */
    public void Indicate_VCardInfoReady(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null && StringUtil.isSameString(myself.getJid(), str)) {
                this.mEdtCustomStatus.setText(myself.getSignature());
                if (!TextUtils.isEmpty(myself.getSignature())) {
                    this.mEdtCustomStatus.setSelection(myself.getSignature().length());
                }
            }
        }
    }

    public void onPause() {
        super.onPause();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerListener);
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.imgClear) {
                onClickImgClear();
            } else if (id == C4558R.C4560id.btnCancel) {
                onClickBtnCancel();
            } else if (id == C4558R.C4560id.btnSave) {
                onClickBtnSave();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnSave() {
        String obj = this.mEdtCustomStatus.getText().toString();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!zoomMessenger.isConnectionGood()) {
                showConnectionError();
                return;
            }
            if (!StringUtil.isEmptyOrNull(zoomMessenger.setUserSignature(obj))) {
                dismiss();
            }
            ZoomLogEventTracking.eventTrackSetPersonalNote();
        }
    }

    private void onClickBtnCancel() {
        dismiss();
    }

    private void onClickImgClear() {
        this.mEdtCustomStatus.setText("");
        ZoomLogEventTracking.eventTrackClearPersonalNote();
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboardInActivity((ZMActivity) getActivity());
        finishFragment(true);
    }
}

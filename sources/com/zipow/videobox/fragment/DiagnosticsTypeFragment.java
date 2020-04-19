package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class DiagnosticsTypeFragment extends ZMDialogFragment implements OnClickListener, ExtListener {
    private static final String STATE_FEATURE = "State_Feature";
    private Button mBtnBack;
    private Button mBtnSendLog;
    /* access modifiers changed from: private */
    public ScrollView mContentScrollView;
    private int mFeature = -1;
    private ImageView mImgChat;
    private ImageView mImgMeeting;
    private ImageView mImgOthers;
    private ImageView mImgPhone;
    private ImageView mImgWebinar;
    private View mOptChat;
    private View mOptMeeting;
    private View mOptOthers;
    private View mOptPhone;
    private View mOptWebinar;
    private TextView mTvChat;
    private TextView mTvMeeting;
    private TextView mTvOthers;
    private TextView mTvPhone;
    private TextView mTvWebinar;

    public boolean onBackPressed() {
        return false;
    }

    public void onKeyboardClosed() {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, DiagnosticsTypeFragment.class.getName(), new Bundle(), 0, true);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_sip_diagnostics_type, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mContentScrollView = (ScrollView) inflate.findViewById(C4558R.C4560id.sv_content);
        this.mOptMeeting = inflate.findViewById(C4558R.C4560id.optMeeting);
        this.mTvMeeting = (TextView) inflate.findViewById(C4558R.C4560id.tvMeeting);
        this.mImgMeeting = (ImageView) inflate.findViewById(C4558R.C4560id.imgMeeting);
        this.mOptPhone = inflate.findViewById(C4558R.C4560id.optPhone);
        this.mTvPhone = (TextView) inflate.findViewById(C4558R.C4560id.tvPhone);
        this.mImgPhone = (ImageView) inflate.findViewById(C4558R.C4560id.imgPhone);
        this.mOptChat = inflate.findViewById(C4558R.C4560id.optChat);
        this.mTvChat = (TextView) inflate.findViewById(C4558R.C4560id.tvChat);
        this.mImgChat = (ImageView) inflate.findViewById(C4558R.C4560id.imgChat);
        this.mOptWebinar = inflate.findViewById(C4558R.C4560id.optWebinar);
        this.mTvWebinar = (TextView) inflate.findViewById(C4558R.C4560id.tvWebinar);
        this.mImgWebinar = (ImageView) inflate.findViewById(C4558R.C4560id.imgWebinar);
        this.mOptOthers = inflate.findViewById(C4558R.C4560id.optOthers);
        this.mTvOthers = (TextView) inflate.findViewById(C4558R.C4560id.tvOthers);
        this.mImgOthers = (ImageView) inflate.findViewById(C4558R.C4560id.imgOthers);
        this.mBtnSendLog = (Button) inflate.findViewById(C4558R.C4560id.btnDiagnoistic);
        this.mBtnBack.setOnClickListener(this);
        this.mOptMeeting.setOnClickListener(this);
        this.mOptPhone.setOnClickListener(this);
        this.mOptChat.setOnClickListener(this);
        this.mOptWebinar.setOnClickListener(this);
        this.mOptOthers.setOnClickListener(this);
        this.mBtnSendLog.setOnClickListener(this);
        if (bundle != null) {
            this.mFeature = bundle.getInt(STATE_FEATURE);
            switch (this.mFeature) {
                case 0:
                    onClick(this.mOptMeeting);
                    break;
                case 1:
                    onClick(this.mOptChat);
                    break;
                case 2:
                    onClick(this.mOptPhone);
                    break;
                case 3:
                    onClick(this.mOptWebinar);
                    break;
                case 4:
                    onClick(this.mOptOthers);
                    break;
            }
        }
        return inflate;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            finish();
        } else if (id == C4558R.C4560id.optMeeting) {
            onClickOption(id, 0);
            announceOnClickOption(view, this.mTvMeeting.getText().toString());
        } else if (id == C4558R.C4560id.optPhone) {
            onClickOption(id, 2);
            announceOnClickOption(view, this.mTvPhone.getText().toString());
        } else if (id == C4558R.C4560id.optChat) {
            onClickOption(id, 1);
            announceOnClickOption(view, this.mTvChat.getText().toString());
        } else if (id == C4558R.C4560id.optWebinar) {
            onClickOption(id, 3);
            announceOnClickOption(view, this.mTvWebinar.getText().toString());
        } else if (id == C4558R.C4560id.optOthers) {
            onClickOption(id, 4);
            announceOnClickOption(view, this.mTvOthers.getText().toString());
        } else if (id == C4558R.C4560id.btnDiagnoistic) {
            onClickSendLog();
        }
    }

    private void onClickOption(@IdRes int i, int i2) {
        int i3 = 0;
        this.mImgMeeting.setVisibility(i == C4558R.C4560id.optMeeting ? 0 : 8);
        this.mImgPhone.setVisibility(i == C4558R.C4560id.optPhone ? 0 : 8);
        this.mImgChat.setVisibility(i == C4558R.C4560id.optChat ? 0 : 8);
        this.mImgWebinar.setVisibility(i == C4558R.C4560id.optWebinar ? 0 : 8);
        ImageView imageView = this.mImgOthers;
        if (i != C4558R.C4560id.optOthers) {
            i3 = 8;
        }
        imageView.setVisibility(i3);
        this.mFeature = i2;
        this.mBtnSendLog.setEnabled(validateInput());
    }

    private void announceOnClickOption(View view, String str) {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(getString(C4558R.string.zm_accessibility_icon_item_selected_19247));
            AccessibilityUtil.announceForAccessibilityCompat(view, (CharSequence) sb.toString());
        }
    }

    private boolean validateInput() {
        return this.mFeature >= 0;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putInt(STATE_FEATURE, this.mFeature);
        super.onSaveInstanceState(bundle);
    }

    private void onClickSendLog() {
        DiagnosticsFragment.showAsActivity(this, this.mFeature);
        finish();
    }

    private void finish() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onKeyboardOpen() {
        this.mContentScrollView.post(new Runnable() {
            public void run() {
                DiagnosticsTypeFragment.this.mContentScrollView.fullScroll(130);
            }
        });
    }
}

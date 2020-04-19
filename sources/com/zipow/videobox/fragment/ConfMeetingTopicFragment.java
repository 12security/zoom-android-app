package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.text.Editable;
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
import com.zipow.videobox.confapp.ConfMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfMeetingTopicFragment extends ZMDialogFragment implements OnClickListener {
    private static final String TAG = "ConfMeetingTopicFragment";
    private Button mBtnBack;
    private Button mBtnSave;
    private EditText mEdtMeetingTopic;
    /* access modifiers changed from: private */
    public ImageView mImgClear;

    public static void showAsActivity(Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, ConfMeetingTopicFragment.class.getName(), new Bundle(), 0);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_meeting_topic, viewGroup, false);
        this.mEdtMeetingTopic = (EditText) inflate.findViewById(C4558R.C4560id.edtMeetingTopic);
        this.mBtnSave = (Button) inflate.findViewById(C4558R.C4560id.btnSave);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mImgClear = (ImageView) inflate.findViewById(C4558R.C4560id.imgClear);
        this.mEdtMeetingTopic.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                ConfMeetingTopicFragment.this.mImgClear.setVisibility(editable.length() > 0 ? 0 : 4);
            }
        });
        if (ConfMgr.getInstance().isConfConnected()) {
            String meetingTopic = ConfMgr.getInstance().getMeetingTopic();
            if (!StringUtil.isEmptyOrNull(meetingTopic)) {
                this.mEdtMeetingTopic.setText(meetingTopic);
                this.mEdtMeetingTopic.setSelection(meetingTopic.length());
            } else if (ConfMgr.getInstance().getMyself() != null) {
                this.mEdtMeetingTopic.setHint(String.format(getString(C4558R.string.zm_mi_meeting_topic_name_105983), new Object[]{ConfMgr.getInstance().getMyself().getScreenName()}));
            }
        }
        this.mImgClear.setOnClickListener(this);
        this.mBtnSave.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mEdtMeetingTopic.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ConfMeetingTopicFragment.this.onClickBtnSave();
                return true;
            }
        });
        return inflate;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.imgClear) {
                onClickImgClear();
            } else if (id == C4558R.C4560id.btnBack) {
                onClickBtnCancel();
            } else if (id == C4558R.C4560id.btnSave) {
                onClickBtnSave();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnSave() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mBtnSave);
        if (!NetworkUtil.hasDataNetwork(getActivity())) {
            showConnectionError(getString(C4558R.string.zm_msg_disconnected_try_again));
            return;
        }
        String trim = this.mEdtMeetingTopic.getText().toString().trim();
        if (StringUtil.isEmptyOrNull(trim)) {
            dismiss();
        } else if (!ConfMgr.getInstance().setMeetingTopic(trim)) {
            showConnectionError(getString(C4558R.string.zm_lbl_profile_change_fail_cannot_connect_service));
        } else {
            dismiss();
        }
    }

    private void showConnectionError(String str) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, str, 1).show();
        }
    }

    private void onClickBtnCancel() {
        dismiss();
    }

    private void onClickImgClear() {
        this.mEdtMeetingTopic.setText("");
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboardInActivity((ZMActivity) getActivity());
        finishFragment(true);
    }
}

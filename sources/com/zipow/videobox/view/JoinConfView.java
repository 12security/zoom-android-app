package com.zipow.videobox.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Editable.Factory;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.CmmSavedMeeting;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.UIMgr;
import java.util.ArrayList;
import java.util.Iterator;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class JoinConfView extends LinearLayout implements OnClickListener, OnEditorActionListener {
    private static final int UI_MODE_MEETINGID = 0;
    private static final int UI_MODE_VANITYURL = 1;
    /* access modifiers changed from: private */
    public ConfNumberAutoCompleteTextView mAutoCompleteConfNumber;
    /* access modifiers changed from: private */
    public VanityUrlAutoCompleteTextView mAutoCompleteEdtVanityUrl;
    private Button mBtnBack;
    private Button mBtnCancel;
    /* access modifiers changed from: private */
    public ImageButton mBtnConfNumberDropdown;
    /* access modifiers changed from: private */
    public ImageButton mBtnConfVanityUrlDropdown;
    private Button mBtnGotoMeetingId;
    private Button mBtnGotoVanityUrl;
    private Button mBtnJoin;
    /* access modifiers changed from: private */
    public CheckedTextView mChkNoAudio;
    /* access modifiers changed from: private */
    public CheckedTextView mChkNoVideo;
    /* access modifiers changed from: private */
    public EditText mEdtScreenName;
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mJoinRunnable = new Runnable() {
        public void run() {
            if (JoinConfView.this.mListener != null) {
                String screenName = JoinConfView.this.getScreenName();
                if (StringUtil.isEmptyOrNull(screenName)) {
                    JoinConfView.this.mEdtScreenName.requestFocus();
                    return;
                }
                if (!PTApp.getInstance().isWebSignedOn()) {
                    PTApp.getInstance().setDeviceUserName(screenName);
                } else {
                    PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
                    if (currentUserProfile != null && !screenName.equals(currentUserProfile.getUserName())) {
                        PTApp.getInstance().setDeviceUserName(screenName);
                    }
                }
                long confNumber = JoinConfView.this.mUIMode == 0 ? JoinConfView.this.getConfNumber() : 0;
                String vanityUrl = JoinConfView.this.mUIMode == 1 ? JoinConfView.this.getVanityUrl() : "";
                if (JoinConfView.this.mUrlAction == null || JoinConfView.this.mUrlAction.length() <= 0) {
                    JoinConfView.this.mListener.onJoinByNumber(confNumber, screenName, vanityUrl, JoinConfView.this.mChkNoAudio != null ? JoinConfView.this.mChkNoAudio.isChecked() : false, JoinConfView.this.mChkNoVideo != null ? JoinConfView.this.mChkNoVideo.isChecked() : false);
                } else {
                    JoinConfView.this.mListener.onJoinByUrl(JoinConfView.this.mUrlAction, screenName);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Listener mListener;
    private View mOptionNoAudio;
    private View mOptionNoVideo;
    private View mPanelConfNumber;
    private View mPanelConfVanityUrl;
    private TextView mTxtTitle;
    /* access modifiers changed from: private */
    public int mUIMode = 0;
    /* access modifiers changed from: private */
    public String mUrlAction;

    public static class CannotJoinDialog extends ZMDialogFragment {
        public CannotJoinDialog() {
            setCancelable(true);
        }

        @Nullable
        public static CannotJoinDialog getAuthFailedDialog(ZMActivity zMActivity) {
            return (CannotJoinDialog) zMActivity.getSupportFragmentManager().findFragmentByTag(CannotJoinDialog.class.getName());
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

    public static class JoinConfRecentMeetingsDialog extends ZMDialogFragment implements OnClickListener {
        private static final String ARGS_KEY_MEETINGLIST = "args_key_meetinglist";
        /* access modifiers changed from: private */
        public OnMeetingItemSelectListener mListener;

        public interface OnMeetingItemSelectListener {
            void onClearMeetingHistory();

            void onMeetingItemSelect(CmmSavedMeeting cmmSavedMeeting);
        }

        @NonNull
        public static JoinConfRecentMeetingsDialog showAsDialog(@NonNull FragmentManager fragmentManager, @NonNull ArrayList<CmmSavedMeeting> arrayList) {
            JoinConfRecentMeetingsDialog joinConfRecentMeetingsDialog = new JoinConfRecentMeetingsDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARGS_KEY_MEETINGLIST, arrayList);
            joinConfRecentMeetingsDialog.setArguments(bundle);
            joinConfRecentMeetingsDialog.show(fragmentManager, JoinConfRecentMeetingsDialog.class.getName());
            return joinConfRecentMeetingsDialog;
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
            View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_recent_meeting, null);
            inflate.findViewById(C4558R.C4560id.btnClearHistory).setOnClickListener(this);
            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(C4558R.C4560id.panelMeetingNo);
            LayoutParams layoutParams = new LayoutParams(-1, UIUtil.dip2px(getActivity(), 35.0f));
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                final CmmSavedMeeting cmmSavedMeeting = (CmmSavedMeeting) it.next();
                View inflate2 = View.inflate(getActivity(), C4558R.layout.zm_recent_meeting_item, null);
                TextView textView = (TextView) inflate2.findViewById(C4558R.C4560id.txtId);
                TextView textView2 = (TextView) inflate2.findViewById(C4558R.C4560id.txtTopic);
                String str = cmmSavedMeeting.getmConfID();
                if (ConfNumberMgr.isValidVanityUrl(str)) {
                    textView2.setText(str);
                    textView.setVisibility(8);
                } else {
                    Editable newEditable = Factory.getInstance().newEditable(str);
                    ConfNumberMgr.formatText(newEditable, 0);
                    textView.setText(newEditable.toString());
                    textView2.setText(cmmSavedMeeting.getmConfTopic());
                }
                linearLayout.addView(inflate2, layoutParams);
                inflate2.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (JoinConfRecentMeetingsDialog.this.mListener != null) {
                            JoinConfRecentMeetingsDialog.this.mListener.onMeetingItemSelect(cmmSavedMeeting);
                        }
                        JoinConfRecentMeetingsDialog.this.dismiss();
                    }
                });
            }
            return inflate;
        }

        public void onClick(@Nullable View view) {
            if (view != null && view.getId() == C4558R.C4560id.btnClearHistory) {
                OnMeetingItemSelectListener onMeetingItemSelectListener = this.mListener;
                if (onMeetingItemSelectListener != null) {
                    onMeetingItemSelectListener.onClearMeetingHistory();
                }
                dismiss();
            }
        }

        public void dismiss() {
            finishFragment(true);
        }
    }

    public interface Listener {
        void onBack();

        void onJoinByNumber(long j, String str, String str2, boolean z, boolean z2);

        void onJoinByUrl(String str, String str2);
    }

    public JoinConfView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public JoinConfView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        removeCallbacks(this.mJoinRunnable);
        super.onDetachedFromWindow();
    }

    private void initView() {
        View.inflate(getContext(), C4558R.layout.zm_join_conf, this);
        this.mTxtTitle = (TextView) findViewById(C4558R.C4560id.txtTitle);
        this.mAutoCompleteConfNumber = (ConfNumberAutoCompleteTextView) findViewById(C4558R.C4560id.edtConfNumber);
        this.mEdtScreenName = (EditText) findViewById(C4558R.C4560id.edtScreenName);
        this.mBtnJoin = (Button) findViewById(C4558R.C4560id.btnJoin);
        this.mBtnBack = (Button) findViewById(C4558R.C4560id.btnBack);
        this.mBtnCancel = (Button) findViewById(C4558R.C4560id.btnCancel);
        this.mChkNoAudio = (CheckedTextView) findViewById(C4558R.C4560id.chkNoAudio);
        this.mOptionNoAudio = findViewById(C4558R.C4560id.optionNoAudio);
        this.mChkNoVideo = (CheckedTextView) findViewById(C4558R.C4560id.chkNoVideo);
        this.mOptionNoVideo = findViewById(C4558R.C4560id.optionNoVideo);
        this.mBtnGotoVanityUrl = (Button) findViewById(C4558R.C4560id.btnGotoVanityUrl);
        this.mBtnGotoMeetingId = (Button) findViewById(C4558R.C4560id.btnGotoMeetingId);
        this.mAutoCompleteEdtVanityUrl = (VanityUrlAutoCompleteTextView) findViewById(C4558R.C4560id.edtConfVanityUrl);
        this.mPanelConfNumber = findViewById(C4558R.C4560id.panelConfNumber);
        this.mPanelConfVanityUrl = findViewById(C4558R.C4560id.panelConfVanityUrl);
        if (!isInEditMode()) {
            String myName = PTApp.getInstance().getMyName();
            if (!StringUtil.isEmptyOrNull(myName)) {
                this.mEdtScreenName.setText(myName);
            } else {
                this.mEdtScreenName.setText(PTApp.getInstance().getDeviceUserName());
            }
            if (this.mEdtScreenName.getText().toString().trim().length() > 0) {
                this.mAutoCompleteConfNumber.setImeOptions(2);
                this.mAutoCompleteConfNumber.setOnEditorActionListener(this);
            }
            this.mEdtScreenName.setImeOptions(2);
            this.mEdtScreenName.setOnEditorActionListener(this);
        }
        CheckedTextView checkedTextView = this.mChkNoAudio;
        if (checkedTextView != null) {
            checkedTextView.setChecked(false);
            this.mOptionNoAudio.setOnClickListener(this);
        }
        CheckedTextView checkedTextView2 = this.mChkNoVideo;
        if (checkedTextView2 != null) {
            ZMPolicyUIHelper.applyNotOpenCamera(checkedTextView2, this.mOptionNoVideo);
            this.mOptionNoVideo.setOnClickListener(this);
        }
        this.mBtnJoin.setEnabled(false);
        this.mBtnJoin.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnGotoVanityUrl.setOnClickListener(this);
        this.mBtnGotoMeetingId.setOnClickListener(this);
        this.mBtnConfNumberDropdown = (ImageButton) findViewById(C4558R.C4560id.btnConfNumberDropdown);
        this.mBtnConfVanityUrlDropdown = (ImageButton) findViewById(C4558R.C4560id.btnConfVanityUrlDropdown);
        this.mBtnConfNumberDropdown.setOnClickListener(this);
        this.mBtnConfVanityUrlDropdown.setOnClickListener(this);
        if (!hasMeetingJoinHistory()) {
            this.mBtnConfNumberDropdown.setVisibility(8);
            this.mBtnConfVanityUrlDropdown.setVisibility(8);
        }
        Button button = this.mBtnCancel;
        if (button != null) {
            button.setOnClickListener(this);
        }
        C35192 r0 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                JoinConfView.this.validateInput();
            }
        };
        this.mAutoCompleteConfNumber.addTextChangedListener(r0);
        this.mEdtScreenName.addTextChangedListener(r0);
        this.mAutoCompleteEdtVanityUrl.addTextChangedListener(r0);
        if (UIMgr.isLargeMode(getContext())) {
            this.mBtnBack.setVisibility(8);
            Button button2 = this.mBtnCancel;
            if (button2 != null) {
                button2.setVisibility(0);
            }
        }
    }

    private boolean hasMeetingJoinHistory() {
        return ConfNumberMgr.loadConfUrlFromDB().size() != 0;
    }

    /* access modifiers changed from: private */
    public void validateInput() {
        boolean z = false;
        boolean z2 = this.mEdtScreenName.getText().length() > 0;
        int i = this.mUIMode;
        if (i == 0) {
            if (validateConfNumber() || !StringUtil.isEmptyOrNull(this.mUrlAction)) {
                z = true;
            }
            z2 &= z;
        } else if (i == 1) {
            if (validateConfVanityURL() || !StringUtil.isEmptyOrNull(this.mUrlAction)) {
                z = true;
            }
            z2 &= z;
        }
        this.mBtnJoin.setEnabled(z2);
    }

    private boolean validateConfNumber() {
        return this.mAutoCompleteConfNumber.getText().length() >= 11 && this.mAutoCompleteConfNumber.getText().length() <= 13 && getConfNumber() > 0;
    }

    private boolean validateConfVanityURL() {
        return ConfNumberMgr.isValidVanityUrl(getVanityUrl());
    }

    public void setTitle(int i) {
        this.mTxtTitle.setText(i);
    }

    public long getConfNumber() {
        String replaceAll = this.mAutoCompleteConfNumber.getText().toString().replaceAll("\\s", "");
        if (replaceAll.length() <= 0) {
            return 0;
        }
        try {
            return Long.parseLong(replaceAll);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    public void setConfNumber(String str) {
        this.mAutoCompleteConfNumber.setText(str);
        validateInput();
    }

    public void setUrlAction(String str) {
        this.mUrlAction = str;
        validateInput();
    }

    @NonNull
    public String getScreenName() {
        return this.mEdtScreenName.getText().toString().trim();
    }

    @NonNull
    public String getVanityUrl() {
        return this.mAutoCompleteEdtVanityUrl.getText().toString().toLowerCase(CompatUtils.getLocalDefault());
    }

    public void setScreenName(String str) {
        this.mEdtScreenName.setText(str);
        validateInput();
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnJoin) {
            onClickJoin();
        } else if (id == C4558R.C4560id.btnBack || id == C4558R.C4560id.btnCancel) {
            onClickBack();
        } else if (id == C4558R.C4560id.optionNoAudio) {
            onClickNoAudio();
        } else if (id == C4558R.C4560id.optionNoVideo) {
            onClickNoVideo();
        } else if (id == C4558R.C4560id.btnGotoMeetingId) {
            onClickGotoMeetingId();
        } else if (id == C4558R.C4560id.btnGotoVanityUrl) {
            onClickGotoVanityUrl();
        } else if (id == C4558R.C4560id.btnConfNumberDropdown) {
            onClickConfNumberDropdown();
        } else if (id == C4558R.C4560id.btnConfVanityUrlDropdown) {
            onClickConfVanityUrlDropdown();
        }
    }

    private void onClickConfVanityUrlDropdown() {
        showConfUrlChooseDialog();
    }

    private void onClickConfNumberDropdown() {
        showConfUrlChooseDialog();
    }

    private void showConfUrlChooseDialog() {
        ArrayList loadConfUrlFromDB = ConfNumberMgr.loadConfUrlFromDB();
        if (loadConfUrlFromDB.size() != 0) {
            Context context = getContext();
            if (context instanceof ZMActivity) {
                JoinConfRecentMeetingsDialog.showAsDialog(((ZMActivity) context).getSupportFragmentManager(), loadConfUrlFromDB).setOnMeetingItemSelectListener(new OnMeetingItemSelectListener() {
                    public void onMeetingItemSelect(@Nullable CmmSavedMeeting cmmSavedMeeting) {
                        if (cmmSavedMeeting != null) {
                            String str = cmmSavedMeeting.getmConfID();
                            if (ConfNumberMgr.isValidVanityUrl(str)) {
                                JoinConfView.this.mAutoCompleteEdtVanityUrl.setText(str);
                                JoinConfView.this.upateUIMode(1);
                            } else {
                                JoinConfView.this.mAutoCompleteConfNumber.setText(str);
                                JoinConfView.this.upateUIMode(0);
                            }
                            JoinConfView.this.mEdtScreenName.requestFocus();
                            JoinConfView.this.mEdtScreenName.setSelection(JoinConfView.this.mEdtScreenName.length());
                        }
                    }

                    public void onClearMeetingHistory() {
                        PTApp.getInstance().clearSavedMeetingList();
                        JoinConfView.this.mBtnConfNumberDropdown.setVisibility(8);
                        JoinConfView.this.mBtnConfVanityUrlDropdown.setVisibility(8);
                        JoinConfView.this.mAutoCompleteConfNumber.clearHistory();
                        JoinConfView.this.mAutoCompleteEdtVanityUrl.clearHistory();
                    }
                });
            }
        }
    }

    private void onClickGotoMeetingId() {
        upateUIMode(0);
    }

    /* access modifiers changed from: private */
    public void upateUIMode(int i) {
        this.mUIMode = i;
        switch (this.mUIMode) {
            case 0:
                this.mBtnGotoMeetingId.setVisibility(8);
                this.mBtnGotoVanityUrl.setVisibility(0);
                this.mPanelConfNumber.setVisibility(0);
                this.mPanelConfVanityUrl.setVisibility(8);
                this.mAutoCompleteConfNumber.requestFocus();
                break;
            case 1:
                this.mBtnGotoMeetingId.setVisibility(0);
                this.mBtnGotoVanityUrl.setVisibility(8);
                this.mPanelConfNumber.setVisibility(8);
                this.mPanelConfVanityUrl.setVisibility(0);
                this.mAutoCompleteEdtVanityUrl.requestFocus();
                break;
        }
        validateInput();
    }

    private void onClickGotoVanityUrl() {
        upateUIMode(1);
    }

    private void onClickJoin() {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            CannotJoinDialog.show((ZMActivity) getContext(), getResources().getString(C4558R.string.zm_alert_network_disconnected));
            return;
        }
        if (this.mListener != null) {
            UIUtil.closeSoftKeyboard(getContext(), this);
            if (this.mUIMode == 0 && !validateConfNumber()) {
                this.mAutoCompleteConfNumber.requestFocus();
            } else if (this.mUIMode != 1 || validateConfVanityURL()) {
                if (!PTApp.getInstance().isWebSignedOn()) {
                    PTApp.getInstance().setCurrentUIFlag();
                }
                removeCallbacks(this.mJoinRunnable);
                checkJoinMeeting();
            } else {
                this.mAutoCompleteEdtVanityUrl.requestFocus();
            }
        }
    }

    private void checkJoinMeeting() {
        MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(getContext(), new SimpleOnButtonClickListener() {
            public void onPositiveClick() {
                JoinConfView joinConfView = JoinConfView.this;
                joinConfView.removeCallbacks(joinConfView.mJoinRunnable);
                JoinConfView joinConfView2 = JoinConfView.this;
                joinConfView2.postDelayed(joinConfView2.mJoinRunnable, 100);
            }
        });
    }

    private void onClickNoAudio() {
        CheckedTextView checkedTextView = this.mChkNoAudio;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    private void onClickNoVideo() {
        CheckedTextView checkedTextView = this.mChkNoVideo;
        checkedTextView.setChecked(!checkedTextView.isChecked());
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

    public void updateUIForCallStatus(long j) {
        if (((int) j) != 1) {
            this.mBtnJoin.setEnabled(true);
        } else {
            this.mBtnJoin.setEnabled(false);
        }
    }
}

package com.zipow.videobox.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ZMWebPageUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil.OnURLSpanClickListener;
import p021us.zoom.androidlib.util.ZMLocaleUtils;
import p021us.zoom.androidlib.widget.ZMCheckedTextView;
import p021us.zoom.androidlib.widget.ZMDatePickerDialog;
import p021us.zoom.androidlib.widget.ZMDatePickerDialog.OnDateSetListener;
import p021us.zoom.androidlib.widget.ZMTimePickerDialog;
import p021us.zoom.androidlib.widget.ZMTimePickerDialog.OnTimeSetListener;
import p021us.zoom.videomeetings.C4558R;

public class DiagnosticsFragment extends ZMDialogFragment implements OnClickListener, ExtListener {
    private static final String ARGS_FEATURE = "feature";
    private static final int MAX_LENGTH = 500;
    private static final String STATE_BRIEF = "State_Brief";
    private static final String STATE_FEATURE = "State_Feature";
    private static final String STATE_HAVE_TICKET = "State_Have_Ticket";
    private static final String STATE_IS_SEND_LOG = "State_Is_Send_Log";
    private static final String STATE_REASON_TYPE = "State_Reason_Typo";
    private static final String STATE_TICKET_ID = "State_Ticket_Id";
    private static final String STATE_TIME = "State_Time";
    private Button mBtnBack;
    private View mBtnCrashTime;
    /* access modifiers changed from: private */
    public Button mBtnSendLog;
    private ZMCheckedTextView mChkHaveTicketID;
    private ZMCheckedTextView mChkSendLog;
    private int mColorDateTimeNormal = 0;
    /* access modifiers changed from: private */
    public ScrollView mContentScrollView;
    /* access modifiers changed from: private */
    @Nullable
    public ZMDatePickerDialog mDatePickerDialog;
    private EditText mEdtTicketID;
    /* access modifiers changed from: private */
    public EditText mEtBrief;
    private int mFeature = -1;
    ArrayList<View> mImgViews = new ArrayList<>();
    private View mLayoutLogBrief;
    /* access modifiers changed from: private */
    @NonNull
    public Calendar mLogTime = Calendar.getInstance();
    private View mOptionHaveTicketID;
    private View mOptionSendLog;
    private View mOptionTicketID;
    private int mReasonType = -1;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    /* access modifiers changed from: private */
    @Nullable
    public ZMTimePickerDialog mTimePickerDialog;
    private TextView mTvCrashTime;
    /* access modifiers changed from: private */
    public TextView mTvReachMaximum;
    private TextView mTxtDesc;

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

    public static void showAsActivity(@Nullable Fragment fragment, int i) {
        if (fragment != null && i >= 0) {
            Bundle bundle = new Bundle();
            bundle.putInt(ARGS_FEATURE, i);
            SimpleActivity.show(fragment, DiagnosticsFragment.class.getName(), bundle, 0, true);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_sip_diagnostics, null);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mFeature = arguments.getInt(ARGS_FEATURE);
        }
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mContentScrollView = (ScrollView) inflate.findViewById(C4558R.C4560id.sv_content);
        this.mLayoutLogBrief = inflate.findViewById(C4558R.C4560id.layoutLogBrief);
        this.mEtBrief = (EditText) inflate.findViewById(C4558R.C4560id.et_brief);
        this.mEtBrief.setAccessibilityDelegate(new AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                CharSequence hint = DiagnosticsFragment.this.mEtBrief.getHint();
                if (hint != null) {
                    accessibilityNodeInfo.setText(hint.toString().replaceAll("\\.\\.\\.", ""));
                }
            }
        });
        this.mTvReachMaximum = (TextView) inflate.findViewById(C4558R.C4560id.tv_reach_maximum);
        this.mTvReachMaximum.setText(getString(C4558R.string.zm_sip_send_log_brief_limit_101987, Integer.valueOf(500)));
        this.mBtnCrashTime = inflate.findViewById(C4558R.C4560id.btnCrashTime);
        this.mTvCrashTime = (TextView) inflate.findViewById(C4558R.C4560id.txtCrashTime);
        this.mOptionSendLog = inflate.findViewById(C4558R.C4560id.optionSendLog);
        this.mChkSendLog = (ZMCheckedTextView) inflate.findViewById(C4558R.C4560id.chkSendLog);
        this.mChkSendLog.setChecked(false);
        this.mTxtDesc = (TextView) inflate.findViewById(C4558R.C4560id.txtDesc);
        initDesc();
        this.mOptionHaveTicketID = inflate.findViewById(C4558R.C4560id.optionHaveTicketID);
        this.mChkHaveTicketID = (ZMCheckedTextView) inflate.findViewById(C4558R.C4560id.chkHaveTicketID);
        this.mOptionTicketID = inflate.findViewById(C4558R.C4560id.optionTicketID);
        this.mEdtTicketID = (EditText) inflate.findViewById(C4558R.C4560id.edtTicketId);
        this.mOptionTicketID.setVisibility(8);
        this.mBtnSendLog = (Button) inflate.findViewById(C4558R.C4560id.btnDiagnoistic);
        this.mColorDateTimeNormal = this.mTvCrashTime.getTextColors().getDefaultColor();
        this.mBtnSendLog.setOnClickListener(this);
        this.mBtnCrashTime.setOnClickListener(this);
        this.mOptionSendLog.setOnClickListener(this);
        this.mOptionHaveTicketID.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mEtBrief.setFilters(new InputFilter[]{new LengthFilter(500)});
        this.mEtBrief.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                DiagnosticsFragment.this.mTvReachMaximum.setVisibility(DiagnosticsFragment.this.mEtBrief.getText().length() >= 500 ? 0 : 8);
                DiagnosticsFragment.this.mBtnSendLog.setEnabled(DiagnosticsFragment.this.validateInput());
            }
        });
        if (bundle != null) {
            this.mReasonType = bundle.getInt(STATE_REASON_TYPE, -1);
            long j = bundle.getLong(STATE_TIME, 0);
            if (j != 0) {
                this.mLogTime.setTimeInMillis(j);
            }
            this.mEtBrief.setText(bundle.getString(STATE_BRIEF, ""));
            this.mChkSendLog.setChecked(bundle.getBoolean(STATE_IS_SEND_LOG, false));
            boolean z = bundle.getBoolean(STATE_HAVE_TICKET, false);
            this.mChkHaveTicketID.setChecked(z);
            if (z) {
                this.mOptionTicketID.setVisibility(0);
                this.mEdtTicketID.setText(bundle.getString(STATE_TICKET_ID, ""));
            }
        }
        initProblemType(inflate);
        updateCrashTime();
        return inflate;
    }

    private void initDesc() {
        this.mTxtDesc.setMovementMethod(LinkMovementMethod.getInstance());
        final String safeString = StringUtil.safeString(PTApp.getInstance().getURLByType(ZMLocaleUtils.isChineseLanguage() ? 20 : 21));
        this.mTxtDesc.setText(ZMHtmlUtil.fromHtml(getContext(), getString(C4558R.string.zm_sip_send_log_desc_send_log_148869, safeString), new OnURLSpanClickListener() {
            public void onClick(View view, String str, String str2) {
                ZMWebPageUtil.startWebPage((Fragment) DiagnosticsFragment.this, str, str2);
            }
        }));
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getActivity())) {
            this.mTxtDesc.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    DiagnosticsFragment diagnosticsFragment = DiagnosticsFragment.this;
                    ZMWebPageUtil.startWebPage((Fragment) diagnosticsFragment, safeString, diagnosticsFragment.getString(C4558R.string.zm_title_privacy_policy));
                }
            });
        } else {
            this.mTxtDesc.setOnClickListener(null);
        }
    }

    private void initProblemType(View view) {
        ArrayList arrayList = new ArrayList();
        this.mImgViews.clear();
        View view2 = view;
        View initProblemType = initProblemType(view2, C4558R.C4560id.optAudioQuality, C4558R.C4560id.tvAudioQuality, C4558R.C4560id.imgAudioQuality, 30);
        arrayList.add(initProblemType);
        View initProblemType2 = initProblemType(view2, C4558R.C4560id.optVideoQuality, C4558R.C4560id.tvVideoQuality, C4558R.C4560id.imgVideoQuality, 31);
        arrayList.add(initProblemType2);
        View initProblemType3 = initProblemType(view2, C4558R.C4560id.optScreenSharing, C4558R.C4560id.tvScreenSharing, C4558R.C4560id.imgScreenSharing, 32);
        arrayList.add(initProblemType3);
        View initProblemType4 = initProblemType(view2, C4558R.C4560id.optRecord, C4558R.C4560id.tvRecord, C4558R.C4560id.imgRecord, 33);
        arrayList.add(initProblemType4);
        View initProblemType5 = initProblemType(view2, C4558R.C4560id.optRegister, C4558R.C4560id.tvRegister, C4558R.C4560id.imgRegister, 34);
        arrayList.add(initProblemType5);
        View initProblemType6 = initProblemType(view2, C4558R.C4560id.optCalling, C4558R.C4560id.tvCalling, C4558R.C4560id.imgCalling, 35);
        arrayList.add(initProblemType6);
        View initProblemType7 = initProblemType(view2, C4558R.C4560id.optMessage, C4558R.C4560id.tvMessage, C4558R.C4560id.imgMessage, 36);
        arrayList.add(initProblemType7);
        View initProblemType8 = initProblemType(view2, C4558R.C4560id.optContacts, C4558R.C4560id.tvContacts, C4558R.C4560id.imgContacts, 37);
        arrayList.add(initProblemType8);
        View initProblemType9 = initProblemType(view2, C4558R.C4560id.optFileTransfer, C4558R.C4560id.tvFileTransfer, C4558R.C4560id.imgFileTransfer, 38);
        arrayList.add(initProblemType9);
        View view3 = initProblemType9;
        arrayList.add(initProblemType(view2, C4558R.C4560id.optNoFunction, C4558R.C4560id.tvNoFunction, C4558R.C4560id.imgNoFunction, 39));
        View initProblemType10 = initProblemType(view2, C4558R.C4560id.optOthers, C4558R.C4560id.tvOthers, C4558R.C4560id.imgOthers, 40);
        arrayList.add(initProblemType10);
        switch (this.mFeature) {
            case 0:
                hiddenAllView(arrayList);
                initProblemType.setVisibility(0);
                initProblemType2.setVisibility(0);
                initProblemType3.setVisibility(0);
                initProblemType4.setVisibility(0);
                initProblemType6.setVisibility(0);
                initProblemType10.setVisibility(0);
                return;
            case 1:
                hiddenAllView(arrayList);
                initProblemType7.setVisibility(0);
                initProblemType8.setVisibility(0);
                view3.setVisibility(0);
                initProblemType10.setVisibility(0);
                return;
            case 2:
                hiddenAllView(arrayList);
                initProblemType.setVisibility(0);
                initProblemType5.setVisibility(0);
                initProblemType6.setVisibility(0);
                initProblemType10.setVisibility(0);
                return;
            case 3:
                hiddenAllView(arrayList);
                initProblemType.setVisibility(0);
                initProblemType2.setVisibility(0);
                initProblemType3.setVisibility(0);
                initProblemType4.setVisibility(0);
                initProblemType5.setVisibility(0);
                initProblemType10.setVisibility(0);
                return;
            case 4:
                hiddenAllView(arrayList);
                initProblemType10.setVisibility(0);
                return;
            default:
                return;
        }
    }

    private View initProblemType(View view, int i, int i2, int i3, int i4) {
        View findViewById = view.findViewById(i);
        final TextView textView = (TextView) findViewById.findViewById(i2);
        final View findViewById2 = findViewById.findViewById(i3);
        this.mImgViews.add(findViewById2);
        final int i5 = i;
        final int i6 = i4;
        final View view2 = findViewById;
        C25285 r0 = new OnClickListener() {
            public void onClick(View view) {
                DiagnosticsFragment diagnosticsFragment = DiagnosticsFragment.this;
                diagnosticsFragment.hiddenAllView(diagnosticsFragment.mImgViews);
                findViewById2.setVisibility(0);
                DiagnosticsFragment.this.onClickOption(i5, i6);
                DiagnosticsFragment.this.announceOnClickOption(view2, textView.getText().toString());
            }
        };
        findViewById.setOnClickListener(r0);
        if (i4 == this.mReasonType) {
            findViewById.performClick();
        }
        return findViewById;
    }

    /* access modifiers changed from: private */
    public void hiddenAllView(ArrayList<View> arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((View) it.next()).setVisibility(8);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            finish();
        } else if (id == C4558R.C4560id.btnCrashTime) {
            onClickCrashTime();
        } else if (id == C4558R.C4560id.optionSendLog) {
            onClickOptionSendLog();
        } else if (id == C4558R.C4560id.optionHaveTicketID) {
            onClickOptionHaveTicketID();
        } else if (id == C4558R.C4560id.btnDiagnoistic) {
            onClickSendLog();
        }
    }

    private void onClickOptionHaveTicketID() {
        if (this.mChkHaveTicketID.isChecked()) {
            this.mChkHaveTicketID.setChecked(false);
            this.mOptionTicketID.setVisibility(8);
            UIUtil.closeSoftKeyboardInActivity((ZMActivity) getActivity());
            return;
        }
        this.mChkHaveTicketID.setChecked(true);
        this.mOptionTicketID.setVisibility(0);
        this.mContentScrollView.fullScroll(130);
    }

    private void onClickOptionSendLog() {
        ZMCheckedTextView zMCheckedTextView = this.mChkSendLog;
        zMCheckedTextView.setChecked(!zMCheckedTextView.isChecked());
    }

    /* access modifiers changed from: private */
    public void onClickOption(@IdRes int i, int i2) {
        this.mReasonType = i2;
        this.mLayoutLogBrief.setVisibility(this.mReasonType >= 0 ? 0 : 8);
        this.mEtBrief.setHint(this.mReasonType == 40 ? C4558R.string.zm_sip_send_log_brief_hint_required_101987 : C4558R.string.zm_sip_send_log_brief_hint_101987);
        this.mBtnSendLog.setEnabled(validateInput());
    }

    /* access modifiers changed from: private */
    public void announceOnClickOption(View view, String str) {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(getString(C4558R.string.zm_accessibility_icon_item_selected_19247));
            AccessibilityUtil.announceForAccessibilityCompat(view, (CharSequence) sb.toString());
        }
    }

    private void onClickCrashTime() {
        if (this.mDatePickerDialog == null && this.mTimePickerDialog == null) {
            ZMDatePickerDialog zMDatePickerDialog = new ZMDatePickerDialog(getActivity(), new OnDateSetListener() {
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    DiagnosticsFragment.this.mDatePickerDialog = null;
                    DiagnosticsFragment.this.mLogTime.set(1, i);
                    DiagnosticsFragment.this.mLogTime.set(2, i2);
                    DiagnosticsFragment.this.mLogTime.set(5, i3);
                    DiagnosticsFragment.this.mBtnSendLog.setEnabled(DiagnosticsFragment.this.validateInput());
                    DiagnosticsFragment.this.updateCrashTime();
                    DiagnosticsFragment.this.setCrashTime();
                }
            }, this.mLogTime.get(1), this.mLogTime.get(2), this.mLogTime.get(5));
            this.mDatePickerDialog = zMDatePickerDialog;
            this.mDatePickerDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    DiagnosticsFragment.this.mDatePickerDialog = null;
                }
            });
            this.mDatePickerDialog.show(System.currentTimeMillis(), 0);
        }
    }

    /* access modifiers changed from: private */
    public void setCrashTime() {
        if (this.mDatePickerDialog == null && this.mTimePickerDialog == null) {
            ZMTimePickerDialog zMTimePickerDialog = new ZMTimePickerDialog(getActivity(), new OnTimeSetListener() {
                public void onTimeSet(TimePicker timePicker, int i, int i2) {
                    DiagnosticsFragment.this.mTimePickerDialog = null;
                    DiagnosticsFragment.this.mLogTime.set(11, i);
                    DiagnosticsFragment.this.mLogTime.set(12, i2);
                    DiagnosticsFragment.this.mBtnSendLog.setEnabled(DiagnosticsFragment.this.validateInput());
                    DiagnosticsFragment.this.updateCrashTime();
                }
            }, this.mLogTime.get(11), this.mLogTime.get(12), DateFormat.is24HourFormat(getActivity()));
            this.mTimePickerDialog = zMTimePickerDialog;
            this.mTimePickerDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    DiagnosticsFragment.this.mTimePickerDialog = null;
                }
            });
            this.mTimePickerDialog.show();
        }
    }

    /* access modifiers changed from: private */
    public void updateCrashTime() {
        long timeInMillis = this.mLogTime.getTimeInMillis();
        if (timeInMillis > System.currentTimeMillis()) {
            this.mTvCrashTime.setTextColor(SupportMenu.CATEGORY_MASK);
        } else {
            this.mTvCrashTime.setTextColor(this.mColorDateTimeNormal);
        }
        this.mTvCrashTime.setText(TimeUtil.formatDateTime(getContext(), timeInMillis));
    }

    /* access modifiers changed from: private */
    public boolean validateInput() {
        int i = this.mReasonType;
        if (i < 0) {
            return false;
        }
        if ((i != 40 || !TextUtils.isEmpty(this.mEtBrief.getText())) && this.mLogTime.getTimeInMillis() <= System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    private void onClickSendLog() {
        PTApp.getInstance().uploadFeedback(this.mFeature, this.mReasonType, this.mLogTime.getTimeInMillis(), this.mEtBrief.getText().toString(), this.mEdtTicketID.getText().toString(), this.mChkSendLog.isChecked());
        Toast makeText = Toast.makeText(getContext(), C4558R.string.zm_sip_send_log_success_new_88945, 0);
        makeText.setGravity(17, 0, 0);
        makeText.show();
        finish();
    }

    private void finish() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putInt(STATE_FEATURE, this.mFeature);
        bundle.putInt(STATE_REASON_TYPE, this.mReasonType);
        bundle.putLong(STATE_TIME, this.mLogTime.getTimeInMillis());
        bundle.putString(STATE_BRIEF, this.mEtBrief.getText().toString());
        bundle.putBoolean(STATE_IS_SEND_LOG, this.mChkSendLog.isChecked());
        bundle.putBoolean(STATE_HAVE_TICKET, this.mChkHaveTicketID.isChecked());
        bundle.putString(STATE_TICKET_ID, this.mEdtTicketID.getText().toString());
        super.onSaveInstanceState(bundle);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onKeyboardOpen() {
        this.mContentScrollView.post(new Runnable() {
            public void run() {
                DiagnosticsFragment.this.mContentScrollView.fullScroll(130);
            }
        });
    }
}

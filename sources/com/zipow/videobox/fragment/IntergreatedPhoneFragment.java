package com.zipow.videobox.fragment;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CloudPBX;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.ISIPCallEventListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.sip.SipBatteryOptDialog;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSettingsLayout;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class IntergreatedPhoneFragment extends ZMDialogFragment implements OnClickListener {
    private Button mBtnBack;
    private View mBtnCompanyNumber;
    private LinearLayout mBtnSendLog;
    private View mCatReceiveCallsFromCallQueues;
    private View mCatReceiveCallsFromSLG;
    private CheckedTextView mChkIgnoreBatteryOpt;
    /* access modifiers changed from: private */
    public CheckedTextView mChkReceiveCallsFromCallQueues;
    /* access modifiers changed from: private */
    public CheckedTextView mChkReceiveCallsFromSLG;
    private LinearLayout mDirectContainer;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            IntergreatedPhoneFragment.this.updateFeatureOption(message.what);
        }
    };
    @NonNull
    private ISIPCallEventListener mISIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
            super.OnPBXFeatureOptionsChanged(list);
            IntergreatedPhoneFragment.this.onPBXFeatureOptionsChanged(list, true);
        }

        public void OnRequestDoneForUpdatePBXFeatureOptions(boolean z, List<CmmPBXFeatureOptionBit> list) {
            super.OnRequestDoneForUpdatePBXFeatureOptions(z, list);
            IntergreatedPhoneFragment.this.onPBXFeatureOptionsChanged(list, z);
        }

        public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
            super.OnRequestDoneForQueryPBXUserInfo(z);
            IntergreatedPhoneFragment.this.initViews();
        }

        public void OnPBXUserStatusChange(int i) {
            super.OnPBXUserStatusChange(i);
            if (i == 3 || i == 1) {
                IntergreatedPhoneFragment.this.finishFragment(true);
            }
        }
    };
    private SimpleISIPLineMgrEventSinkListener mISIPLineMgrEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnMySelfInfoUpdated(boolean z, int i) {
            super.OnMySelfInfoUpdated(z, i);
            IntergreatedPhoneFragment.this.initViews();
        }
    };
    private View mOptionReceiveCallsFromCallQueues;
    private View mOptionReceiveCallsFromSLG;
    private IPTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onDataNetworkStatusChanged(boolean z) {
            super.onDataNetworkStatusChanged(z);
            IntergreatedPhoneFragment.this.mChkReceiveCallsFromCallQueues.setEnabled(z);
            IntergreatedPhoneFragment.this.mChkReceiveCallsFromSLG.setEnabled(z);
        }
    };
    private ZMSettingsLayout mSettingsBatteryOpt;
    private TextView mTxtAreaCode;
    private TextView mTxtCompanyNumber;
    private TextView mTxtLocalDialing;
    private TextView mTxtTips;
    private TextView mTxtTitleBatteryOpt;

    public static class ClickContextMenu extends ZMSimpleMenuItem {
        public static final int ACTION_COPY = 0;

        public ClickContextMenu(int i, String str) {
            super(i, str);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface OperationType {
        public static final int OPT_CALL_QUEUE = 1;
        public static final int OPT_SLG = 2;
    }

    private class ShareActionClickableSpan extends ClickableSpan {
        private ShareActionClickableSpan() {
        }

        public void onClick(@NonNull View view) {
            PTApp instance = PTApp.getInstance();
            CloudPBX cloudPBXInfo = CmmSIPCallManager.getInstance().getCloudPBXInfo();
            if (cloudPBXInfo != null) {
                instance.navWebWithDefaultBrowser(5, instance.getPhoneSettingUrl(cloudPBXInfo.getRcSettingsLink()));
            }
        }

        public void updateDrawState(@NonNull TextPaint textPaint) {
            textPaint.setColor(IntergreatedPhoneFragment.this.getResources().getColor(C4558R.color.box_link_text));
            textPaint.setUnderlineText(true);
        }
    }

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, IntergreatedPhoneFragment.class.getName(), new Bundle(), 0);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_intergreated_phone, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mDirectContainer = (LinearLayout) inflate.findViewById(C4558R.C4560id.directContainer);
        this.mTxtCompanyNumber = (TextView) inflate.findViewById(C4558R.C4560id.txtCompanyNumber);
        this.mTxtLocalDialing = (TextView) inflate.findViewById(C4558R.C4560id.txtLocalDialing);
        this.mTxtAreaCode = (TextView) inflate.findViewById(C4558R.C4560id.txtAreaCode);
        this.mCatReceiveCallsFromCallQueues = inflate.findViewById(C4558R.C4560id.catReceiveCallsFromCallQueues);
        this.mOptionReceiveCallsFromCallQueues = inflate.findViewById(C4558R.C4560id.optionReceiveCallsFromCallQueues);
        this.mBtnCompanyNumber = inflate.findViewById(C4558R.C4560id.btnCompanyNumber);
        this.mChkReceiveCallsFromCallQueues = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkReceiveCallsFromCallQueues);
        this.mTxtTips = (TextView) inflate.findViewById(C4558R.C4560id.txtTips);
        this.mCatReceiveCallsFromSLG = inflate.findViewById(C4558R.C4560id.catReceiveCallsFromSLG);
        this.mOptionReceiveCallsFromSLG = inflate.findViewById(C4558R.C4560id.optionReceiveCallsFromSLG);
        this.mChkReceiveCallsFromSLG = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkReceiveCallsFromSLG);
        String string = getResources().getString(C4558R.string.zm_intergeated_phone_tips_1_31439);
        String string2 = getResources().getString(C4558R.string.zm_intergeated_phone_tips_2_31439);
        String string3 = getResources().getString(C4558R.string.zm_intergeated_phone_tips_3_31439);
        SpannableString spannableString = new SpannableString(getString(C4558R.string.zm_intergeated_phone_tips_4_31439, string, string2, string3));
        spannableString.setSpan(new ShareActionClickableSpan(), string.length() + 1, string.length() + string2.length() + 1, 33);
        this.mTxtTips.setMovementMethod(LinkMovementMethod.getInstance());
        this.mTxtTips.setText(spannableString);
        this.mTxtTitleBatteryOpt = (TextView) inflate.findViewById(C4558R.C4560id.titleBatteryOpt);
        this.mSettingsBatteryOpt = (ZMSettingsLayout) inflate.findViewById(C4558R.C4560id.settingsBatteryOpt);
        this.mChkIgnoreBatteryOpt = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkIgnoreBatteryOpt);
        this.mChkIgnoreBatteryOpt.setOnClickListener(this);
        this.mBtnSendLog = (LinearLayout) inflate.findViewById(C4558R.C4560id.btnDiagnoistic);
        this.mBtnSendLog.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mOptionReceiveCallsFromCallQueues.setOnClickListener(this);
        this.mBtnCompanyNumber.setOnClickListener(this);
        this.mOptionReceiveCallsFromSLG.setOnClickListener(this);
        CmmSIPCallManager.getInstance().addListener(this.mISIPCallEventListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mISIPLineMgrEventSinkListener);
        PTUI.getInstance().addPTUIListener(this.mPTUIListener);
        return inflate;
    }

    /* access modifiers changed from: private */
    public void onClickCopyPhoneNumber(final String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getActivity(), false);
                zMMenuAdapter.addItem(new ClickContextMenu(0, activity.getString(C4558R.string.zm_mm_msg_copy_82273)));
                ZMAlertDialog create = new Builder(activity).setTheme(C4558R.style.ZMDialog_Material_RoundRect_NormalCorners).setTitle((CharSequence) str).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (((ClickContextMenu) zMMenuAdapter.getItem(i)).getAction() == 0) {
                            AndroidAppUtil.copyText(IntergreatedPhoneFragment.this.getActivity(), str);
                        }
                    }
                }).create();
                create.setCanceledOnTouchOutside(true);
                create.show();
            }
        }
    }

    private void initDirectNumberListener() {
        int childCount = this.mDirectContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mDirectContainer.getChildAt(i).setOnClickListener(new OnClickListener() {
                public void onClick(@NonNull View view) {
                    IntergreatedPhoneFragment.this.onClickCopyPhoneNumber(((TextView) view).getText().toString());
                }
            });
        }
    }

    public void initViews() {
        CloudPBX cloudPBXInfo = CmmSIPCallManager.getInstance().getCloudPBXInfo();
        if (cloudPBXInfo != null) {
            List directNumberFormattedList = cloudPBXInfo.getDirectNumberFormattedList();
            this.mDirectContainer.removeAllViews();
            if (directNumberFormattedList.isEmpty()) {
                List arrayList = new ArrayList(directNumberFormattedList);
                arrayList.add(getString(C4558R.string.zm_intergeated_phone_not_set_31439));
                directNumberFormattedList = arrayList;
            }
            initDirectNumberList(directNumberFormattedList);
            initDirectNumberListener();
            String mainCompanyNumberFormatted = cloudPBXInfo.getMainCompanyNumberFormatted();
            String extension = cloudPBXInfo.getExtension();
            if (!StringUtil.isEmptyOrNull(extension)) {
                StringBuilder sb = new StringBuilder();
                sb.append(mainCompanyNumberFormatted);
                sb.append(" #");
                sb.append(extension);
                this.mTxtCompanyNumber.setText(sb.toString());
            }
            String countryName = cloudPBXInfo.getCountryName();
            if (!StringUtil.isEmptyOrNull(countryName)) {
                this.mTxtLocalDialing.setText(countryName);
            }
            String areaCode = cloudPBXInfo.getAreaCode();
            if (!StringUtil.isEmptyOrNull(areaCode)) {
                this.mTxtAreaCode.setText(areaCode);
            }
        }
        refreshOptionReceiveCallsFromCallQueuesViewState(true);
        refreshOptionReceiveCallsFromSLGViewState(true);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initViews();
    }

    @TargetApi(23)
    private boolean isBatteryOptIgnored() {
        boolean z = false;
        if (!OsUtil.isAtLeastM()) {
            return false;
        }
        PowerManager powerManager = (PowerManager) VideoBoxApplication.getInstance().getSystemService("power");
        if (powerManager != null && powerManager.isIgnoringBatteryOptimizations(VideoBoxApplication.getInstance().getPackageName())) {
            z = true;
        }
        return z;
    }

    private void initDirectNumberList(@Nullable List<String> list) {
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                addDirectView((String) list.get(i), i);
            }
        }
    }

    private void addDirectView(String str, int i) {
        getLayoutInflater().inflate(C4558R.layout.zm_sip_intergrated_phone_direct_item, this.mDirectContainer, true);
        ((TextView) this.mDirectContainer.getChildAt(i)).setText(str);
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnBack) {
            onBtnBackClicked();
        } else if (view.getId() == C4558R.C4560id.chkIgnoreBatteryOpt) {
            if (OsUtil.isAtLeastM()) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    SipBatteryOptDialog.newInstance(!this.mChkIgnoreBatteryOpt.isChecked()).show(activity.getSupportFragmentManager(), SipBatteryOptDialog.class.getName());
                }
            }
        } else if (view.getId() == C4558R.C4560id.optionReceiveCallsFromCallQueues) {
            onClickReceiveCallsFromCallQueues();
        } else if (view.getId() == C4558R.C4560id.optionReceiveCallsFromSLG) {
            onClickReceiveCallsFromSLG();
        } else if (view.getId() == C4558R.C4560id.btnDiagnoistic) {
            onClickBtnDiagnoistic();
        } else if (view.getId() == C4558R.C4560id.btnCompanyNumber) {
            onClickCopyPhoneNumber(this.mTxtCompanyNumber.getText().toString());
        }
    }

    private String getDirectNumbers() {
        CloudPBX cloudPBXInfo = CmmSIPCallManager.getInstance().getCloudPBXInfo();
        if (cloudPBXInfo == null) {
            return "";
        }
        List<String> directNumberList = cloudPBXInfo.getDirectNumberList();
        if (CollectionsUtil.isListEmpty(directNumberList)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String append : directNumberList) {
            sb.append(append);
            sb.append(OAuth.SCOPE_DELIMITER);
        }
        return sb.toString().trim();
    }

    private void onClickBtnDiagnoistic() {
        DiagnosticsFragment.showAsActivity(this, 2);
    }

    private void onBtnBackClicked() {
        if (getShowsDialog()) {
            dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    /* access modifiers changed from: private */
    public void onPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list, boolean z) {
        if (list != null && list.size() != 0) {
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            if (instance != null) {
                if (ZMPhoneUtils.isPBXFeatureOptionChanged(list, instance.getUserInCallQueueBit()) || ZMPhoneUtils.isPBXFeatureOptionChanged(list, instance.getReceiveCallsFromCallQueueBit())) {
                    refreshOptionReceiveCallsFromCallQueuesViewState(z);
                } else if (ZMPhoneUtils.isPBXFeatureOptionChanged(list, instance.getUserInSLGBit()) || ZMPhoneUtils.isPBXFeatureOptionChanged(list, instance.getReceiveCallsFromSLGBit()) || ZMPhoneUtils.isPBXFeatureOptionChanged(list, instance.getSharedLineGroupEnabledBit())) {
                    refreshOptionReceiveCallsFromSLGViewState(z);
                }
            }
        }
    }

    private void refreshOptionReceiveCallsFromCallQueuesViewState(boolean z) {
        boolean isInCallQueues = CmmSIPCallManager.getInstance().isInCallQueues();
        this.mCatReceiveCallsFromCallQueues.setVisibility(isInCallQueues ? 0 : 8);
        if (isInCallQueues) {
            boolean receiveCallsFromCallQueuesState = getReceiveCallsFromCallQueuesState();
            this.mChkReceiveCallsFromCallQueues.setChecked(receiveCallsFromCallQueuesState);
            if (!z) {
                onUpdateFeatureOptionFailed(1, !receiveCallsFromCallQueuesState);
            }
        }
    }

    private void onClickReceiveCallsFromCallQueues() {
        if (this.mChkReceiveCallsFromCallQueues.isEnabled()) {
            CheckedTextView checkedTextView = this.mChkReceiveCallsFromCallQueues;
            checkedTextView.setChecked(!checkedTextView.isChecked());
            updateFeatureOptionDelayed(1);
        }
    }

    private boolean getReceiveCallsFromCallQueuesState() {
        return CmmSIPCallManager.getInstance().isReceiveCallsFromCallQueues();
    }

    private boolean updateReceiveCallsFromCallQueues(boolean z) {
        return CmmSIPCallManager.getInstance().updateReceiveCallsFromCallQueues(z, false) == 0;
    }

    private void refreshOptionReceiveCallsFromSLGViewState(boolean z) {
        boolean isInSLG = CmmSIPCallManager.getInstance().isInSLG();
        this.mCatReceiveCallsFromSLG.setVisibility(isInSLG ? 0 : 8);
        if (isInSLG) {
            boolean receiveCallsFromSLGState = getReceiveCallsFromSLGState();
            this.mChkReceiveCallsFromSLG.setChecked(receiveCallsFromSLGState);
            if (!z) {
                onUpdateFeatureOptionFailed(2, !receiveCallsFromSLGState);
            }
        }
    }

    private void onClickReceiveCallsFromSLG() {
        if (this.mChkReceiveCallsFromSLG.isEnabled()) {
            CheckedTextView checkedTextView = this.mChkReceiveCallsFromSLG;
            checkedTextView.setChecked(!checkedTextView.isChecked());
            updateFeatureOptionDelayed(2);
        }
    }

    private boolean getReceiveCallsFromSLGState() {
        return CmmSIPCallManager.getInstance().isReceiveCallsFromSLG();
    }

    private boolean updateReceiveCallsFromSLG(boolean z) {
        return CmmSIPCallManager.getInstance().updateReceiveCallsFromSLG(z, false) == 0;
    }

    private void updateFeatureOptionDelayed(int i) {
        this.mHandler.removeMessages(i);
        this.mHandler.sendEmptyMessageDelayed(i, 300);
    }

    /* access modifiers changed from: private */
    public void updateFeatureOption(int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = zoomMessenger == null || zoomMessenger.isStreamConflict();
        switch (i) {
            case 1:
                boolean receiveCallsFromCallQueuesState = getReceiveCallsFromCallQueuesState();
                boolean isChecked = this.mChkReceiveCallsFromCallQueues.isChecked();
                if (receiveCallsFromCallQueuesState == isChecked) {
                    return;
                }
                if (z || !updateReceiveCallsFromCallQueues(isChecked)) {
                    onUpdateFeatureOptionFailed(i, isChecked);
                    this.mChkReceiveCallsFromCallQueues.setChecked(receiveCallsFromCallQueuesState);
                    return;
                }
                return;
            case 2:
                boolean receiveCallsFromSLGState = getReceiveCallsFromSLGState();
                boolean isChecked2 = this.mChkReceiveCallsFromSLG.isChecked();
                if (receiveCallsFromSLGState == isChecked2) {
                    return;
                }
                if (z || !updateReceiveCallsFromSLG(isChecked2)) {
                    onUpdateFeatureOptionFailed(i, isChecked2);
                    this.mChkReceiveCallsFromSLG.setChecked(receiveCallsFromSLGState);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void onUpdateFeatureOptionFailed(int i, boolean z) {
        switch (i) {
            case 1:
                DialogUtils.showAlertDialog((ZMActivity) getActivity(), z ? C4558R.string.zm_sip_error_turn_on_receive_call_queue_calls_113697 : C4558R.string.zm_sip_error_turn_off_receive_call_queue_calls_113697, C4558R.string.zm_btn_ok_88102);
                return;
            case 2:
                DialogUtils.showAlertDialog((ZMActivity) getActivity(), z ? C4558R.string.zm_sip_error_turn_on_receive_slg_calls_113697 : C4558R.string.zm_sip_error_turn_off_receive_slg_calls_113697, C4558R.string.zm_btn_ok_88102);
                return;
            default:
                return;
        }
    }

    public void onResume() {
        super.onResume();
        this.mTxtTitleBatteryOpt.setVisibility(8);
        this.mSettingsBatteryOpt.setVisibility(8);
    }

    public void onDestroyView() {
        this.mHandler.removeCallbacksAndMessages(null);
        CmmSIPCallManager.getInstance().removeListener(this.mISIPCallEventListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mISIPLineMgrEventSinkListener);
        PTUI.getInstance().removePTUIListener(this.mPTUIListener);
        super.onDestroyView();
    }
}

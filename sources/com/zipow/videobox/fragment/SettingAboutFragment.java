package com.zipow.videobox.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.FeedbackActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.confapp.meeting.confhelper.ZoomRateHelper;
import com.zipow.videobox.fragment.NewVersionDialog.RequestPermissionListener;
import com.zipow.videobox.ptapp.IMProtos.MessageInput;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.LogUtil;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.UpgradeUtil;
import com.zipow.videobox.util.ZMZipUtil;
import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMSendMessageFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SettingAboutFragment extends ZMDialogFragment implements OnClickListener, IPTUIListener {
    private static final String ARGS_SHOW_IN_VIEW = "showInView";
    private static final int REQUEST_GET_SHAREES = 109;
    public static final int REQUEST_PERMISSION_BY_UPDATE = 106;
    private static final int REQUEST_PERMISSION_WRITE = 100;
    private static final String TIME_FOEMAT = "yyyy-MM-dd HH:mm:ss";
    private Button mBtnBack;
    private View mBtnFeedback;
    private View mBtnPrivacy;
    private View mBtnRate;
    private View mBtnRecommend;
    private View mBtnReportProblem;
    private View mClearLog;
    private ImageView mImgIndicatorNewVersion;
    private boolean mIsCheckingUpdates = false;
    private View mOptionVersion;
    private View mPanelTitleBar;
    private View mProgressBarCheckingUpdate;
    private View mSendLog;

    class LogFileFilter implements FilenameFilter {
        LogFileFilter() {
        }

        public boolean accept(File file, String str) {
            return str.endsWith(".log") || str.startsWith("crash");
        }
    }

    @SuppressLint({"NewApi"})
    class SendFileAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;
        private String mJid;

        public SendFileAsyncTask(Context context, String str) {
            this.dialog = new ProgressDialog(context);
            this.dialog.setMessage(context.getString(C4558R.string.zm_mm_send_log_dialog_msg_65868));
            this.mJid = str;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.dialog.show();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Void... voidArr) {
            return SettingAboutFragment.this.getLogZipFile();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            this.dialog.dismiss();
            if (TextUtils.isEmpty(str)) {
                Toast.makeText(SettingAboutFragment.this.getContext(), SettingAboutFragment.this.getString(C4558R.string.zm_mm_send_log_file_empty_65868), 1).show();
                return;
            }
            SettingAboutFragment.this.sendFile(this.mJid, str);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mJid);
                if (sessionById != null) {
                    FragmentActivity activity = SettingAboutFragment.this.getActivity();
                    if (activity instanceof ZMActivity) {
                        if (sessionById.isGroup()) {
                            MMChatActivity.showAsGroupChat((ZMActivity) activity, this.mJid);
                        } else {
                            MMChatActivity.showAsOneToOneChat((ZMActivity) activity, sessionById.getSessionBuddy());
                        }
                    }
                }
            }
        }
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static boolean needShowAboutTip(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        return hasNewVersion(context);
    }

    private static boolean hasNewVersion(Context context) {
        String latestVersionString = PTApp.getInstance().getLatestVersionString();
        return !StringUtil.isEmptyOrNull(latestVersionString) && !context.getString(C4558R.string.zm_version_name).equals(latestVersionString);
    }

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, SettingAboutFragment.class.getName(), new Bundle(), 0);
        }
    }

    public static void showSettingAboutFragmentInView(Fragment fragment, int i) {
        SettingAboutFragment settingAboutFragment = new SettingAboutFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_SHOW_IN_VIEW, true);
        settingAboutFragment.setArguments(bundle);
        fragment.getChildFragmentManager().beginTransaction().add(i, settingAboutFragment, SettingAboutFragment.class.getName()).commit();
    }

    public static SettingAboutFragment getSettingAboutFragmentInView(Fragment fragment) {
        Fragment findFragmentByTag = fragment.getChildFragmentManager().findFragmentByTag(SettingAboutFragment.class.getName());
        if (findFragmentByTag instanceof SettingAboutFragment) {
            return (SettingAboutFragment) findFragmentByTag;
        }
        return null;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_setting_about, null);
        this.mBtnFeedback = inflate.findViewById(C4558R.C4560id.btnFeedback);
        this.mBtnReportProblem = inflate.findViewById(C4558R.C4560id.btnReportProblem);
        this.mBtnReportProblem.setVisibility(PTApp.getInstance().isWebSignedOn() ? 0 : 8);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnRecommend = inflate.findViewById(C4558R.C4560id.btnRecommend);
        this.mBtnRate = inflate.findViewById(C4558R.C4560id.btnRate);
        this.mImgIndicatorNewVersion = (ImageView) inflate.findViewById(C4558R.C4560id.imgIndicatorNewVersion);
        this.mOptionVersion = inflate.findViewById(C4558R.C4560id.optionVersion);
        this.mProgressBarCheckingUpdate = inflate.findViewById(C4558R.C4560id.progressBarCheckingUpdate);
        this.mBtnPrivacy = inflate.findViewById(C4558R.C4560id.btnPrivacy);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mSendLog = inflate.findViewById(C4558R.C4560id.btnSendLog);
        this.mClearLog = inflate.findViewById(C4558R.C4560id.btnClearLog);
        this.mBtnRecommend.setEnabled(AndroidAppUtil.hasSMSApp(getActivity()) || AndroidAppUtil.hasEmailApp(getActivity()));
        if (!AndroidAppUtil.hasGooglePlayStoreApp(getActivity())) {
            this.mBtnRate.setVisibility(8);
        }
        if (bundle != null) {
            this.mIsCheckingUpdates = bundle.getBoolean("mIsCheckingUpdates", false);
        }
        this.mBtnFeedback.setOnClickListener(this);
        this.mBtnReportProblem.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnRecommend.setOnClickListener(this);
        this.mBtnRate.setOnClickListener(this);
        this.mOptionVersion.setOnClickListener(this);
        this.mBtnPrivacy.setOnClickListener(this);
        PTUI.getInstance().addPTUIListener(this);
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getBoolean(ARGS_SHOW_IN_VIEW, false)) {
            this.mPanelTitleBar.setVisibility(8);
        }
    }

    private void updateNewVersionIndicator() {
        boolean hasNewVersion = hasNewVersion(getActivity());
        if (hasNewVersion) {
            this.mIsCheckingUpdates = false;
        }
        if (this.mIsCheckingUpdates) {
            this.mImgIndicatorNewVersion.setVisibility(8);
            this.mProgressBarCheckingUpdate.setVisibility(0);
            return;
        }
        this.mProgressBarCheckingUpdate.setVisibility(8);
        if (hasNewVersion) {
            this.mImgIndicatorNewVersion.setVisibility(0);
        } else {
            this.mImgIndicatorNewVersion.setVisibility(8);
        }
    }

    private void updateFeedbackButton() {
        if (PTApp.getInstance().isFeedbackOff()) {
            this.mBtnFeedback.setVisibility(8);
        } else {
            this.mBtnFeedback.setVisibility(0);
        }
    }

    public void onResume() {
        super.onResume();
        updateNewVersionIndicator();
        updateFeedbackButton();
    }

    public void onDestroy() {
        super.onDestroy();
        PTUI.getInstance().removePTUIListener(this);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("mIsCheckingUpdates", this.mIsCheckingUpdates);
    }

    public void onPTAppEvent(int i, long j) {
        switch (i) {
            case 25:
                onNewVersionReady();
                return;
            case 26:
                onCheckFailed();
                return;
            case 28:
                onNoNewVersion();
                return;
            default:
                return;
        }
    }

    private void onCheckFailed() {
        getNonNullEventTaskManagerOrThrowException().push("onCheckFailed", new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((SettingAboutFragment) iUIElement).handleOnCheckFailed();
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnCheckFailed() {
        if (this.mIsCheckingUpdates) {
            showCheckFailedMessage();
        }
        this.mIsCheckingUpdates = false;
        this.mProgressBarCheckingUpdate.setVisibility(8);
    }

    private void onNoNewVersion() {
        getNonNullEventTaskManagerOrThrowException().push("onNoNewVersion", new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((SettingAboutFragment) iUIElement).handleOnNoNewVersion();
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnNoNewVersion() {
        if (this.mIsCheckingUpdates) {
            showNoNewVersionMessage();
        }
        this.mIsCheckingUpdates = false;
        updateNewVersionIndicator();
    }

    private void onNewVersionReady() {
        getNonNullEventTaskManagerOrThrowException().push("onNewVersionReady", new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((SettingAboutFragment) iUIElement).handleOnNewVersionReady();
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnNewVersionReady() {
        this.mIsCheckingUpdates = false;
        updateNewVersionIndicator();
        showNewVersionDialog();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnFeedback) {
            onClickBtnSendFeedback();
        } else if (id == C4558R.C4560id.btnReportProblem) {
            onClickBtnReportProblem();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnRecommend) {
            onClickBtnRecommend();
        } else if (id == C4558R.C4560id.btnRate) {
            ZoomRateHelper.launchGooglePlayAppDetail(getActivity());
        } else if (id == C4558R.C4560id.optionVersion) {
            onClickOptionVersion();
        } else if (id == C4558R.C4560id.btnPrivacy) {
            onClickPrivacyPolicy();
        } else if (id == C4558R.C4560id.btnSendLog) {
            onClickSendLog();
        } else if (id == C4558R.C4560id.btnClearLog) {
            onClickClearLog();
        }
    }

    private void onClickBtnReportProblem() {
        if (!PTApp.getInstance().isWebSignedOn()) {
            DiagnosticsFragment.showAsActivity(this, 0);
        } else {
            DiagnosticsTypeFragment.showAsActivity(this);
        }
    }

    private void onClickPrivacyPolicy() {
        PrivacyPolicyFragment.showAsActivity(this);
    }

    private void onClickSendLog() {
        shareMessage();
    }

    private void onClickClearLog() {
        Toast.makeText(getContext(), getString(C4558R.string.zm_mm_clear_log_success_65868), 1).show();
        String logFolder = LogUtil.getLogFolder();
        if (!TextUtils.isEmpty(logFolder)) {
            File file = new File(logFolder);
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles(new LogFileFilter());
                if (listFiles != null || listFiles.length > 0) {
                    for (File delete : listFiles) {
                        delete.delete();
                    }
                }
            }
        }
    }

    private void onClickBtnSendFeedback() {
        if (getShowsDialog()) {
            FeedbackFragment.showDialog(getFragmentManager());
            dismiss();
            return;
        }
        FeedbackActivity.show(getActivity());
    }

    private void onClickBtnBack() {
        if (getShowsDialog()) {
            dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private void onClickBtnRecommend() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ZMSendMessageFragment.show(getActivity(), getFragmentManager(), null, null, PTApp.getInstance().getZoomInvitationEmailSubject(), PTApp.getInstance().getZoomInvitationEmailBody(), activity.getString(C4558R.string.zm_msg_sms_invitation_content), null, null, 3);
        }
    }

    private void onClickOptionVersion() {
        String latestVersionString = PTApp.getInstance().getLatestVersionString();
        String string = getString(C4558R.string.zm_version_name);
        if (StringUtil.isEmptyOrNull(latestVersionString) || string.equals(latestVersionString)) {
            this.mIsCheckingUpdates = true;
            updateNewVersionIndicator();
            PTApp.getInstance().checkForUpdates(true, true);
            return;
        }
        showNewVersionDialog();
        PTApp.getInstance().checkForUpdates(false, true);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i == 106 && checkStoragePermission()) {
            UpgradeUtil.upgrade((ZMActivity) getActivity());
        }
    }

    private boolean checkStoragePermission() {
        return VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private void showNewVersionDialog() {
        PreferenceUtil.saveLongValue(PreferenceUtil.LAST_SHOW_NEW_VERSION_TIME, System.currentTimeMillis());
        String latestVersionString = PTApp.getInstance().getLatestVersionString();
        String latestVersionReleaseNote = PTApp.getInstance().getLatestVersionReleaseNote();
        if (!StringUtil.isEmptyOrNull(latestVersionString)) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                NewVersionDialog newVersionDialog = (NewVersionDialog) fragmentManager.findFragmentByTag(NewVersionDialog.class.getName());
                if (newVersionDialog != null) {
                    newVersionDialog.setArguments(latestVersionString, latestVersionReleaseNote);
                    return;
                }
                NewVersionDialog lastInstance = NewVersionDialog.getLastInstance();
                if (lastInstance != null) {
                    lastInstance.setArguments(latestVersionString, latestVersionReleaseNote);
                    return;
                }
                PTApp.getInstance().setLastUpdateNotesDisplayTime(System.currentTimeMillis());
                NewVersionDialog.newInstance(latestVersionString, latestVersionReleaseNote, new RequestPermissionListener() {
                    public void requestPermission() {
                        SettingAboutFragment.this.zm_requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 106);
                    }
                }).show(fragmentManager, NewVersionDialog.class.getName());
            }
        }
    }

    private void showNoNewVersionMessage() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            new Builder(activity).setTitle(C4558R.string.zm_msg_no_new_version).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        }
    }

    private void showCheckFailedMessage() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            new Builder(activity).setTitle(C4558R.string.zm_lbl_profile_change_fail_cannot_connect_service).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        }
    }

    public void shareMessage() {
        MMSelectSessionAndBuddyFragment.showAsFragment(this, new Bundle(), false, false, 109);
    }

    public void sendFile(String str, String str2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            MessageInput.Builder newBuilder = MessageInput.newBuilder();
            newBuilder.setMsgType(10);
            newBuilder.setMsgSubType(1);
            newBuilder.setIsE2EMessage(false);
            newBuilder.setSessionID(str);
            newBuilder.setE2EMessageFakeBody(getString(C4558R.string.zm_msg_e2e_fake_message));
            newBuilder.setLocalFilePath(str2);
            newBuilder.setIsMyNote(UIMgr.isMyNotes(str));
            zoomMessenger.sendMessage(newBuilder.build());
        }
    }

    /* access modifiers changed from: private */
    public String getLogZipFile() {
        String logFolder = LogUtil.getLogFolder();
        if (TextUtils.isEmpty(logFolder)) {
            return null;
        }
        File file = new File(logFolder);
        if (!file.isDirectory()) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FOEMAT);
        StringBuilder sb = new StringBuilder();
        sb.append(logFolder);
        sb.append(File.separator);
        sb.append(simpleDateFormat.format(new Date()));
        sb.append(".zip");
        String sb2 = sb.toString();
        File[] listFiles = file.listFiles(new LogFileFilter());
        if (listFiles == null || listFiles.length <= 0) {
            return null;
        }
        ZMZipUtil.zipFile(listFiles, sb2);
        return sb2;
    }

    @SuppressLint({"NewApi"})
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 109 && i2 == -1 && intent != null && intent.getExtras() != null) {
            String stringExtra = intent.getStringExtra(MMSelectSessionAndBuddyFragment.RESULT_ARG_SELECTED_ITEM);
            if (!StringUtil.isEmptyOrNull(stringExtra)) {
                new SendFileAsyncTask(getContext(), stringExtra).execute(new Void[0]);
            }
        }
    }
}

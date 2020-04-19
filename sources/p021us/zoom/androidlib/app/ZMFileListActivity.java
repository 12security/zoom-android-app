package p021us.zoom.androidlib.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMDynTextSizeTextView;

/* renamed from: us.zoom.androidlib.app.ZMFileListActivity */
public class ZMFileListActivity extends ZMActivity implements OnItemClickListener, OnClickListener, ZMFileListListener {
    private static final String ADAPTER_CLASS_NAME = "adapter_class_name";
    private static final String DIR_START_PATH = "dir_start_path";
    public static final String FAILED_PROMT = "failed_promt";
    private static final String FILE_LIST_PROMPT_MESSAGE = "file_list_prompt_message";
    private static final String FILTER_FILE_EXTENSIONS = "filter_file_extensions";
    private static final String IS_SHARE_LINK_ENABLE = "is_share_link_enable";
    private static final String PROXY_INFO = "proxy_info";
    private static final String SELECTED_BUTTON_TEXT_RES_ID = "selected_button_text_res_id";
    public static final String SELECTED_FILE_NAME = "selected_file_name";
    public static final String SELECTED_FILE_PATH = "selected_file_path";
    public static final String SHARED_FILE_ID = "shared_file_id";
    public static final String SHARED_FILE_LINK = "shared_file_link";
    public static final String SHARED_FILE_SIZE = "shared_file_size";
    public static final String SHARED_FILE_TYPE = "shared_file_type";
    private static final String STARTED_STATUS_FLAG = "started_status_flag";
    public static String[] mProxyInfo;
    private final int STATUS_ERROR = 4;
    private final int STATUS_IDLE = 0;
    private final int STATUS_OPENED = 3;
    private final int STATUS_STARTED = 2;
    private final int STATUS_STARTING = 1;
    private ZMFileListBaseAdapter mAdapter;
    private String mAdatpterClassName = null;
    private Button mBtnBack;
    private Button mBtnClose;
    private Button mBtnExit;
    private Button mBtnSelect;
    private String mDirPath = null;
    private ListView mFileList;
    private String[] mFilterExtensions = null;
    private boolean mIsShareLinkEnable = false;
    private View mPrompt;
    private String mPromptMessage = null;
    private TextView mPromptMsgView;
    private int mSelTextResId = 0;
    private int mStatus = 0;
    private ZMDynTextSizeTextView mTitle;
    private View mWaitingProgress;
    private TextView mWaitingPromt;

    /* renamed from: us.zoom.androidlib.app.ZMFileListActivity$ShowAlertDialog */
    public static class ShowAlertDialog extends ZMDialogFragment {
        private static final String ARG_MESSAGE = "arg_message";

        public static void showDialog(FragmentManager fragmentManager, String str) {
            ShowAlertDialog showAlertDialog = new ShowAlertDialog();
            Bundle bundle = new Bundle();
            if (!StringUtil.isEmptyOrNull(str)) {
                bundle.putString(ARG_MESSAGE, str);
            }
            showAlertDialog.setArguments(bundle);
            showAlertDialog.show(fragmentManager, ShowAlertDialog.class.getName());
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return createEmptyDialog();
            }
            String string = arguments.getString(ARG_MESSAGE);
            Builder positiveButton = new Builder(getActivity()).setCancelable(true).setPositiveButton(C4409R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            positiveButton.setMessage(string);
            return positiveButton.create();
        }
    }

    public static void startFileListActivity(Activity activity, Class<? extends ZMFileListBaseAdapter> cls, int i) {
        startFileListActivity(activity, cls, i, (String[]) null, (String) null, 0, (String) null);
    }

    public static void startFileListActivity(Activity activity, Class<? extends ZMFileListBaseAdapter> cls, int i, String[] strArr) {
        startFileListActivity(activity, cls, i, strArr, (String) null, 0, (String) null);
    }

    public static void startFileListActivity(Activity activity, Class<? extends ZMFileListBaseAdapter> cls, int i, String str) {
        startFileListActivity(activity, cls, i, (String[]) null, str, 0, (String) null);
    }

    public static void startFileListActivity(Activity activity, Class<? extends ZMFileListBaseAdapter> cls, int i, String[] strArr, String str, int i2, String str2) {
        if (activity != null && cls != null) {
            Intent buildIntent = buildIntent(cls, i, strArr, str, i2, str2);
            buildIntent.setClass(activity, ZMFileListActivity.class);
            activity.startActivityForResult(buildIntent, i);
        }
    }

    public static void startFileListActivity(Activity activity, Class<? extends ZMFileListBaseAdapter> cls, int i, String[] strArr, String str, int i2, String str2, String[] strArr2) {
        if (activity != null && cls != null) {
            Intent buildIntent = buildIntent(cls, i, strArr, str, i2, str2, strArr2);
            buildIntent.setClass(activity, ZMFileListActivity.class);
            activity.startActivityForResult(buildIntent, i);
        }
    }

    public static void startFileListActivity(Fragment fragment, Class<? extends ZMFileListBaseAdapter> cls, int i) {
        startFileListActivity(fragment, cls, i, (String[]) null, (String) null, 0, (String) null);
    }

    public static void startFileListActivity(Fragment fragment, Class<? extends ZMFileListBaseAdapter> cls, int i, String[] strArr) {
        startFileListActivity(fragment, cls, i, strArr, (String) null, 0, (String) null);
    }

    public static void startFileListActivity(Fragment fragment, Class<? extends ZMFileListBaseAdapter> cls, int i, String str) {
        startFileListActivity(fragment, cls, i, (String[]) null, str, 0, (String) null);
    }

    public static void startFileListActivity(Fragment fragment, Class<? extends ZMFileListBaseAdapter> cls, int i, String[] strArr, String str, int i2, String str2) {
        startFileListActivity(fragment, cls, i, strArr, str, i2, str2, false);
    }

    public static void startFileListActivity(Fragment fragment, Class<? extends ZMFileListBaseAdapter> cls, int i, String[] strArr, String str, int i2, String str2, boolean z) {
        if (fragment != null && cls != null) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                Intent buildIntent = buildIntent(cls, i, strArr, str, i2, str2, z, null);
                buildIntent.setClass(activity, ZMFileListActivity.class);
                int i3 = i;
                fragment.startActivityForResult(buildIntent, i);
            }
        }
    }

    public static Intent buildIntent(Class<? extends ZMFileListBaseAdapter> cls, int i, String[] strArr, String str, int i2, String str2) {
        return buildIntent(cls, i, strArr, str, i2, str2, false, null);
    }

    public static Intent buildIntent(Class<? extends ZMFileListBaseAdapter> cls, int i, String[] strArr, String str, int i2, String str2, String[] strArr2) {
        return buildIntent(cls, i, strArr, str, i2, str2, false, strArr2);
    }

    public static Intent buildIntent(Class<? extends ZMFileListBaseAdapter> cls, int i, String[] strArr, String str, int i2, String str2, boolean z, String[] strArr2) {
        Intent intent = new Intent();
        intent.putExtra(ADAPTER_CLASS_NAME, cls.getName());
        if (strArr != null && strArr.length > 0) {
            intent.putExtra(FILTER_FILE_EXTENSIONS, strArr);
        }
        if (!StringUtil.isEmptyOrNull(str)) {
            intent.putExtra(DIR_START_PATH, str);
        }
        if (i2 > 0) {
            intent.putExtra(SELECTED_BUTTON_TEXT_RES_ID, i2);
        }
        if (!StringUtil.isEmptyOrNull(str2)) {
            intent.putExtra(FILE_LIST_PROMPT_MESSAGE, str2);
        }
        intent.putExtra(IS_SHARE_LINK_ENABLE, z);
        if (strArr2 != null && strArr2.length > 0) {
            intent.putExtra(PROXY_INFO, strArr2);
        }
        return intent;
    }

    private void initial(Bundle bundle) {
        this.mAdatpterClassName = null;
        if (bundle != null) {
            this.mAdatpterClassName = bundle.getString(ADAPTER_CLASS_NAME);
            this.mFilterExtensions = bundle.getStringArray(FILTER_FILE_EXTENSIONS);
            this.mDirPath = bundle.getString(DIR_START_PATH);
            this.mSelTextResId = bundle.getInt(SELECTED_BUTTON_TEXT_RES_ID);
            this.mPromptMessage = bundle.getString(FILE_LIST_PROMPT_MESSAGE);
            this.mStatus = bundle.getInt(STARTED_STATUS_FLAG);
            this.mIsShareLinkEnable = bundle.getBoolean(IS_SHARE_LINK_ENABLE);
            mProxyInfo = bundle.getStringArray(PROXY_INFO);
            return;
        }
        Intent intent = getIntent();
        if (intent != null) {
            this.mAdatpterClassName = intent.getStringExtra(ADAPTER_CLASS_NAME);
            this.mFilterExtensions = intent.getStringArrayExtra(FILTER_FILE_EXTENSIONS);
            this.mDirPath = intent.getStringExtra(DIR_START_PATH);
            this.mSelTextResId = intent.getIntExtra(SELECTED_BUTTON_TEXT_RES_ID, 0);
            this.mPromptMessage = intent.getStringExtra(FILE_LIST_PROMPT_MESSAGE);
            this.mStatus = 0;
            this.mIsShareLinkEnable = intent.getBooleanExtra(IS_SHARE_LINK_ENABLE, false);
            mProxyInfo = intent.getStringArrayExtra(PROXY_INFO);
        }
    }

    private ZMFileListBaseAdapter getAdapter(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        try {
            try {
                return (ZMFileListBaseAdapter) Class.forName(str).newInstance();
            } catch (Exception unused) {
                return null;
            }
        } catch (ClassNotFoundException unused2) {
            return null;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C4409R.layout.zm_file_list);
        this.mPrompt = findViewById(C4409R.C4411id.file_list_prompt);
        this.mPromptMsgView = (TextView) findViewById(C4409R.C4411id.prompt_message);
        this.mBtnExit = (Button) findViewById(C4409R.C4411id.btnExit);
        this.mBtnBack = (Button) findViewById(C4409R.C4411id.btnBack);
        this.mBtnClose = (Button) findViewById(C4409R.C4411id.btnClose);
        this.mBtnSelect = (Button) findViewById(C4409R.C4411id.btnSelect);
        this.mWaitingProgress = findViewById(C4409R.C4411id.waitingProgress);
        this.mWaitingPromt = (TextView) findViewById(C4409R.C4411id.txtWaitingPromt);
        this.mTitle = (ZMDynTextSizeTextView) findViewById(C4409R.C4411id.txtTitle);
        this.mFileList = (ListView) findViewById(C4409R.C4411id.file_list);
        this.mBtnExit.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnClose.setOnClickListener(this);
        this.mBtnSelect.setOnClickListener(this);
        initial(bundle);
        int i = this.mSelTextResId;
        if (i > 0) {
            this.mBtnSelect.setText(i);
        }
        this.mAdapter = getAdapter(this.mAdatpterClassName);
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter == null) {
            this.mStatus = 4;
            return;
        }
        zMFileListBaseAdapter.init(this, this);
        String[] strArr = this.mFilterExtensions;
        if (strArr != null && strArr.length > 0) {
            this.mAdapter.setFilterExtens(strArr);
        }
        this.mAdapter.enableShareLink(this.mIsShareLinkEnable);
        this.mFileList.setChoiceMode(1);
        this.mFileList.setOnItemClickListener(this);
        this.mFileList.setAdapter(this.mAdapter);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter != null) {
            zMFileListBaseAdapter.notifyDataSetChanged();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        String str = this.mAdatpterClassName;
        if (str != null) {
            bundle.putString(ADAPTER_CLASS_NAME, str);
        }
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter != null && !zMFileListBaseAdapter.isRootDir()) {
            this.mDirPath = this.mAdapter.getCurrentDirPath();
            bundle.putString(DIR_START_PATH, this.mAdapter.getCurrentDirPath());
        }
        String[] strArr = this.mFilterExtensions;
        if (strArr != null && strArr.length > 0) {
            bundle.putStringArray(FILTER_FILE_EXTENSIONS, strArr);
        }
        int i = this.mSelTextResId;
        if (i > 0) {
            bundle.putInt(SELECTED_BUTTON_TEXT_RES_ID, i);
        }
        if (StringUtil.isEmptyOrNull(this.mPromptMessage)) {
            bundle.putString(FILE_LIST_PROMPT_MESSAGE, this.mPromptMessage);
        }
        bundle.putInt(STARTED_STATUS_FLAG, this.mStatus);
        bundle.putBoolean(IS_SHARE_LINK_ENABLE, this.mIsShareLinkEnable);
        String[] strArr2 = mProxyInfo;
        if (strArr2 != null && strArr2.length > 0) {
            bundle.putStringArray(PROXY_INFO, strArr2);
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        initial(bundle);
    }

    public void onDestroy() {
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter != null) {
            zMFileListBaseAdapter.onDestroy();
        }
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter != null) {
            zMFileListBaseAdapter.onPause();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter != null) {
            zMFileListBaseAdapter.onStart();
        }
    }

    public void onResume() {
        super.onResume();
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter == null) {
            exit();
            return;
        }
        if (!zMFileListBaseAdapter.isNeedAuth() || this.mStatus != 0) {
            this.mAdapter.onResume();
        } else {
            this.mAdapter.login();
        }
        refresh();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter != null) {
            zMFileListBaseAdapter.onActivityResult(i, i2, intent);
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C44231 r2 = new EventAction("fileListPermissionResult") {
            public void run(IUIElement iUIElement) {
                ((ZMFileListActivity) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
            }
        };
        getEventTaskManager().pushLater("fileListPermissionResult", r2);
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, String[] strArr, int[] iArr) {
        if (strArr != null && iArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.READ_EXTERNAL_STORAGE".equals(strArr[i2])) {
                    ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
                    if (zMFileListBaseAdapter != null) {
                        zMFileListBaseAdapter.onStoragePermissionResult(iArr[i2]);
                    }
                }
            }
        }
    }

    private void open() {
        if (this.mStatus != 2) {
            return;
        }
        if (this.mAdapter.openDir(this.mDirPath)) {
            this.mStatus = 3;
        } else {
            this.mStatus = 4;
        }
    }

    public void refresh() {
        switch (this.mStatus) {
            case 0:
            case 1:
                this.mBtnExit.setVisibility(8);
                this.mBtnBack.setVisibility(8);
                this.mBtnSelect.setVisibility(8);
                this.mBtnClose.setVisibility(0);
                this.mPrompt.setVisibility(8);
                return;
            case 2:
            case 3:
                if (!this.mAdapter.isRootDir() || StringUtil.isEmptyOrNull(this.mPromptMessage)) {
                    this.mPrompt.setVisibility(8);
                } else {
                    this.mPromptMsgView.setText(this.mPromptMessage);
                    this.mPrompt.setVisibility(0);
                }
                this.mTitle.setText(this.mAdapter.getCurrentDirName());
                if (this.mAdapter.isRootDir()) {
                    if (this.mAdapter.isNeedAuth()) {
                        this.mBtnExit.setVisibility(0);
                    } else {
                        this.mBtnExit.setVisibility(8);
                    }
                    this.mBtnBack.setVisibility(8);
                } else {
                    this.mBtnExit.setVisibility(8);
                    this.mBtnBack.setVisibility(0);
                }
                if (this.mAdapter.isFileSelected()) {
                    this.mBtnSelect.setVisibility(0);
                    this.mBtnClose.setVisibility(8);
                    return;
                }
                this.mBtnSelect.setVisibility(8);
                this.mBtnClose.setVisibility(0);
                return;
            case 4:
                this.mBtnExit.setVisibility(8);
                this.mBtnBack.setVisibility(8);
                this.mBtnSelect.setVisibility(8);
                this.mBtnClose.setVisibility(0);
                ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
                if (zMFileListBaseAdapter == null || zMFileListBaseAdapter.isNeedAuth()) {
                    this.mPrompt.setVisibility(8);
                    return;
                }
                String lastErrorMessage = this.mAdapter.getLastErrorMessage();
                if (!StringUtil.isEmptyOrNull(lastErrorMessage)) {
                    this.mPromptMsgView.setText(lastErrorMessage);
                    this.mPrompt.setVisibility(0);
                    return;
                }
                this.mPrompt.setVisibility(8);
                return;
            default:
                return;
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter != null) {
            zMFileListBaseAdapter.onClickItem(i);
        }
        refresh();
    }

    public void onBackPressed() {
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter == null || !zMFileListBaseAdapter.onBackPressed()) {
            refresh();
        } else {
            exit();
        }
    }

    private void onClickExit() {
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter != null && zMFileListBaseAdapter.isNeedAuth()) {
            this.mAdapter.logout();
        }
        exit();
    }

    private void onClickBack() {
        if (this.mStatus == 3) {
            ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
            if (zMFileListBaseAdapter != null && !zMFileListBaseAdapter.isRootDir()) {
                this.mAdapter.upDir();
                refresh();
            }
        }
    }

    private void onClickSelect() {
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter != null && zMFileListBaseAdapter.isFileSelected()) {
            this.mAdapter.openSelectedFile();
        }
    }

    private void onClickClose() {
        exit();
    }

    public void onClick(View view) {
        if (view == this.mBtnExit) {
            onClickExit();
        } else if (view == this.mBtnBack) {
            onClickBack();
        } else if (view == this.mBtnSelect) {
            onClickSelect();
        } else if (view == this.mBtnClose) {
            onClickClose();
        }
    }

    public void onRefresh() {
        refresh();
    }

    public void onSelectedFile(String str, String str2) {
        exitOK(str, str2);
    }

    public void onReLogin() {
        ZMFileListBaseAdapter zMFileListBaseAdapter = this.mAdapter;
        if (zMFileListBaseAdapter != null && zMFileListBaseAdapter.isNeedAuth()) {
            this.mAdapter.logout();
            this.mStatus = 0;
            this.mAdapter.login();
        }
    }

    private void exit() {
        setResult(0);
        finish();
    }

    private void exitOK(String str, String str2) {
        Intent intent = new Intent();
        if (!StringUtil.isEmptyOrNull(str)) {
            intent.putExtra(SELECTED_FILE_PATH, str);
        }
        if (!StringUtil.isEmptyOrNull(str2)) {
            intent.putExtra(SELECTED_FILE_NAME, str2);
        }
        setResult(-1, intent);
        finish();
    }

    private void exitOK(String str, String str2, String str3, long j, int i) {
        Intent intent = new Intent();
        if (!StringUtil.isEmptyOrNull(str)) {
            intent.putExtra(SHARED_FILE_ID, str);
        }
        if (!StringUtil.isEmptyOrNull(str3)) {
            intent.putExtra(SHARED_FILE_LINK, str3);
        }
        if (!StringUtil.isEmptyOrNull(str2)) {
            intent.putExtra(SELECTED_FILE_NAME, str2);
        }
        intent.putExtra(SHARED_FILE_SIZE, j);
        intent.putExtra(SHARED_FILE_TYPE, i);
        setResult(-1, intent);
        finish();
    }

    private void exitByFailed(String str) {
        Intent intent = new Intent();
        if (!StringUtil.isEmptyOrNull(str)) {
            intent.putExtra(FAILED_PROMT, str);
        }
        setResult(0, intent);
        finish();
    }

    public void onStarting() {
        this.mStatus = 1;
    }

    public void onStarted(boolean z, String str) {
        if (!z) {
            exitByFailed(str);
            return;
        }
        this.mStatus = 2;
        open();
        refresh();
    }

    public void onWaitingStart(String str) {
        this.mWaitingProgress.setVisibility(0);
        if (StringUtil.isEmptyOrNull(str)) {
            this.mWaitingPromt.setText(getString(C4409R.string.zm_msg_loading));
        } else {
            this.mWaitingPromt.setText(str);
        }
    }

    public void onUpdateWaitingMessage(String str) {
        if (!StringUtil.isEmptyOrNull(str) && this.mWaitingProgress.getVisibility() == 0) {
            this.mWaitingPromt.setText(str);
        }
    }

    public void onWaitingEnd() {
        this.mWaitingProgress.setVisibility(8);
    }

    public void onOpenDirFailed(String str) {
        ShowAlertDialog.showDialog(getSupportFragmentManager(), str);
    }

    public void onOpenFileFailed(String str) {
        ShowAlertDialog.showDialog(getSupportFragmentManager(), str);
    }

    public void onSharedFileLink(String str, String str2, String str3, long j, int i) {
        exitOK(str, str2, str3, j, i);
    }
}

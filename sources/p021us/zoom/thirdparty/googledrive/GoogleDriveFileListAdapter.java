package p021us.zoom.thirdparty.googledrive;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import p021us.zoom.androidlib.app.IZMAppUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFileListBaseAdapter;
import p021us.zoom.androidlib.app.ZMFileListEntry;
import p021us.zoom.androidlib.app.ZMFileListListener;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.googledrive.GoogleDrive.DriveFileListener;
import p021us.zoom.thirdparty.googledrive.GoogleDrive.DriveFoldListener;
import p021us.zoom.thirdparty.googledrive.GoogleDrive.GDAsyncLoadFolder;
import p021us.zoom.thirdparty.googledrive.GoogleDrive.GoogleDriveAuthListener;
import p021us.zoom.thirdparty.login.C4538R;

/* renamed from: us.zoom.thirdparty.googledrive.GoogleDriveFileListAdapter */
public class GoogleDriveFileListAdapter extends ZMFileListBaseAdapter implements GoogleDriveAuthListener {
    private String mCachedDir;
    /* access modifiers changed from: private */
    public String mCurrentDir;
    private HashMap<String, GoogleDriveObjectEntry> mDirCached = new HashMap<>();
    private DriveFileListener mDownloadFileListener = new DriveFileListener() {
        public void onDownloadCompleted(String str, String str2, String str3) {
            GoogleDriveFileListAdapter.this.dismissWaitingDialog();
            if (GoogleDriveFileListAdapter.this.mListener != null) {
                GoogleDriveFileListAdapter.this.mListener.onSelectedFile(str3, str2);
            }
        }

        public void onDownloadFailed(String str, String str2, Exception exc) {
            String str3;
            GoogleDriveFileListAdapter.this.dismissWaitingDialog();
            if (GoogleDriveFileListAdapter.this.mListener != null) {
                if (exc != null) {
                    str3 = exc.getMessage();
                } else {
                    str3 = GoogleDriveFileListAdapter.this.mActivity.getString(C4538R.string.zm_msg_load_file_fail, new Object[]{str2});
                }
                GoogleDriveFileListAdapter.this.mListener.onOpenFileFailed(str3);
            }
        }

        public void onDownloadProgress(String str, String str2, long j, long j2) {
            if (j > 0) {
                long j3 = (j2 * 100) / j;
                GoogleDriveFileListAdapter googleDriveFileListAdapter = GoogleDriveFileListAdapter.this;
                googleDriveFileListAdapter.updateProgressWaitingDialog(googleDriveFileListAdapter.mActivity.getString(C4538R.string.zm_msg_download_file_progress, new Object[]{Long.valueOf(j3)}));
                return;
            }
            GoogleDriveFileListAdapter googleDriveFileListAdapter2 = GoogleDriveFileListAdapter.this;
            googleDriveFileListAdapter2.updateProgressWaitingDialog(googleDriveFileListAdapter2.mActivity.getString(C4538R.string.zm_msg_download_file_size, new Object[]{FileUtils.toFileSizeString(GoogleDriveFileListAdapter.this.mActivity, j2)}));
        }

        public void onDownloadCanceled(String str, String str2) {
            GoogleDriveFileListAdapter.this.dismissWaitingDialog();
        }

        public void onRefreshTokenFailed(String str, String str2, String str3) {
            GoogleDriveFileListAdapter.this.dismissWaitingDialog();
        }
    };
    private GoogleDrive mGoogleDrive;
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public ZMFileListListener mListener;
    private DriveFoldListener mLoadFolderListener = new DriveFoldListener() {
        public void onLoadFolderCompeleted(GDAsyncLoadFolder gDAsyncLoadFolder, String str, ArrayList<GoogleDriveObjectEntry> arrayList) {
            GoogleDriveFileListAdapter.this.dismissWaitingDialog();
            if (!StringUtil.isEmptyOrNull(str)) {
                GoogleDriveFileListAdapter.this.mCurrentDir = gDAsyncLoadFolder.getPath();
                GoogleDriveFileListAdapter.this.mFileList.clear();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    GoogleDriveObjectEntry googleDriveObjectEntry = (GoogleDriveObjectEntry) it.next();
                    if (googleDriveObjectEntry != null && (googleDriveObjectEntry.isDir() || GoogleDriveFileListAdapter.this.acceptFileTypeByMimeType(googleDriveObjectEntry.getMimeType()))) {
                        GoogleDriveFileListAdapter.this.mFileList.add(googleDriveObjectEntry);
                    }
                }
                GoogleDriveFileListAdapter.this.sortFileList();
                GoogleDriveFileListAdapter.this.notifyDataSetChanged();
                if (GoogleDriveFileListAdapter.this.mListener != null) {
                    GoogleDriveFileListAdapter.this.mListener.onRefresh();
                }
            }
        }

        public void onLoadFolderCanceled(GDAsyncLoadFolder gDAsyncLoadFolder, String str) {
            GoogleDriveFileListAdapter.this.dismissWaitingDialog();
        }

        public void onLoadFolderFailed(GDAsyncLoadFolder gDAsyncLoadFolder, String str, Exception exc) {
            String str2;
            GoogleDriveFileListAdapter.this.dismissWaitingDialog();
            if (GoogleDriveFileListAdapter.this.mListener != null) {
                if (exc == null) {
                    str2 = null;
                } else {
                    str2 = exc.getMessage();
                }
                if (StringUtil.isEmptyOrNull(str2)) {
                    str2 = GoogleDriveFileListAdapter.this.mActivity.getString(C4538R.string.zm_msg_load_dir_fail, new Object[]{""});
                }
                GoogleDriveFileListAdapter.this.mListener.onOpenDirFailed(str2);
            }
        }

        public void onRefreshTokenFailed(String str, String str2) {
            GoogleDriveFileListAdapter.this.dismissWaitingDialog();
        }
    };
    private OnCancelListener mWaitingDialogCancelListener = new OnCancelListener() {
        public void onCancel(DialogInterface dialogInterface) {
            GoogleDriveFileListAdapter.this.cancel();
        }
    };

    public boolean isNeedAuth() {
        return true;
    }

    public void init(ZMActivity zMActivity, ZMFileListListener zMFileListListener) {
        super.init(zMActivity, zMFileListListener);
        this.mListener = zMFileListListener;
        this.mGoogleDrive = GoogleDriveMgr.getInstance().getGoogleDrive(zMActivity);
        this.mGoogleDrive.bind(zMActivity, this.mHandler, this);
        IZMAppUtil iZMAppUtil = GoogleDriveMgr.getInstance().getmIZMAppUtil();
        if (iZMAppUtil != null) {
            this.mCachedDir = iZMAppUtil.getCachePath();
        }
    }

    public void onResume() {
        login();
    }

    public void login() {
        if (this.mGoogleDrive.isAuthed()) {
            ZMFileListListener zMFileListListener = this.mListener;
            if (zMFileListListener != null) {
                zMFileListListener.onStarted(true, null);
            }
            return;
        }
        this.mGoogleDrive.login();
        ZMFileListListener zMFileListListener2 = this.mListener;
        if (zMFileListListener2 != null) {
            zMFileListListener2.onStarting();
        }
    }

    public void onDestroy() {
        this.mGoogleDrive.onDestroy();
    }

    public void logout() {
        super.logout();
        this.mGoogleDrive.logout();
    }

    /* access modifiers changed from: protected */
    public void openFile(ZMFileListEntry zMFileListEntry) {
        if (zMFileListEntry != null && !zMFileListEntry.isDir()) {
            long bytes = zMFileListEntry.getBytes();
            if (bytes <= 0) {
                bytes = 52428800;
            }
            IZMAppUtil iZMAppUtil = GoogleDriveMgr.getInstance().getmIZMAppUtil();
            if (iZMAppUtil != null && !iZMAppUtil.hasEnoughDiskSpace(this.mCachedDir, bytes)) {
                alertMemoryNotEnough(this.mActivity.getString(C4538R.string.zm_title_error), this.mActivity.getString(C4538R.string.zm_msg_memory_size_insufficient));
            } else if (zMFileListEntry instanceof GoogleDriveObjectEntry) {
                if (this.mGoogleDrive.downloadFile((GoogleDriveObjectEntry) zMFileListEntry, this.mCachedDir, this.mDownloadFileListener)) {
                    showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_download_file_progress, new Object[]{Integer.valueOf(0)}), this.mWaitingDialogCancelListener);
                }
            }
        }
    }

    public boolean openDir(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            str = File.separator;
        }
        if (str.equals(this.mCurrentDir)) {
            return true;
        }
        String str2 = null;
        if (this.mDirCached.containsKey(str)) {
            GoogleDriveObjectEntry googleDriveObjectEntry = (GoogleDriveObjectEntry) this.mDirCached.get(str);
            if (googleDriveObjectEntry != null) {
                str2 = googleDriveObjectEntry.getId();
            }
        }
        if (this.mGoogleDrive.loadFolder(str, str2, this.mLoadFolderListener)) {
            showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void openDir(ZMFileListEntry zMFileListEntry) {
        if (zMFileListEntry != null && zMFileListEntry.isDir() && (zMFileListEntry instanceof GoogleDriveObjectEntry)) {
            GoogleDriveObjectEntry googleDriveObjectEntry = (GoogleDriveObjectEntry) zMFileListEntry;
            if (!this.mCurrentDir.equals(googleDriveObjectEntry.getPath()) && this.mGoogleDrive.loadFolder(googleDriveObjectEntry.getPath(), googleDriveObjectEntry.getId(), this.mLoadFolderListener)) {
                showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
            }
        }
    }

    public boolean onBackPressed() {
        cancel();
        return super.onBackPressed();
    }

    public boolean isRootDir() {
        String str = this.mCurrentDir;
        return str == null || str.equals(File.separator);
    }

    public String getCurrentDirName() {
        if (StringUtil.isEmptyOrNull(this.mCurrentDir)) {
            return "";
        }
        return AndroidAppUtil.getPathLastName(this.mCurrentDir);
    }

    public String getCurrentDirPath() {
        if (StringUtil.isEmptyOrNull(this.mCurrentDir)) {
            return "";
        }
        return this.mCurrentDir;
    }

    /* access modifiers changed from: protected */
    public void gotoParentDir() {
        if (!isRootDir() && this.mCurrentDir != null && this.mGoogleDrive.isAuthed()) {
            String sb = new StringBuilder(getCurrentDirPath()).toString();
            if (sb.endsWith(File.separator)) {
                sb = sb.substring(0, sb.lastIndexOf(File.separator));
            }
            String substring = sb.substring(0, sb.lastIndexOf(File.separator) + 1);
            if (!substring.equals(getCurrentDirName())) {
                if (!substring.equals(File.separator) && substring.endsWith(File.separator)) {
                    substring = substring.substring(0, substring.length() - 1);
                }
                String str = null;
                if (this.mDirCached.containsKey(substring)) {
                    GoogleDriveObjectEntry googleDriveObjectEntry = (GoogleDriveObjectEntry) this.mDirCached.get(substring);
                    if (googleDriveObjectEntry != null) {
                        str = googleDriveObjectEntry.getId();
                    }
                }
                if (this.mGoogleDrive.loadFolder(substring, str, this.mLoadFolderListener)) {
                    showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
                }
            }
        }
    }

    public void onAuthCredentialStarting() {
        showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
    }

    public void onAuthStarting() {
        ZMFileListListener zMFileListListener = this.mListener;
        if (zMFileListListener != null) {
            zMFileListListener.onStarting();
        }
    }

    public void onAuthSuccess() {
        dismissWaitingDialog();
        this.mHandler.post(new Runnable() {
            public void run() {
                if (GoogleDriveFileListAdapter.this.mListener != null) {
                    GoogleDriveFileListAdapter.this.mListener.onStarted(true, null);
                }
            }
        });
    }

    public void onAuthError(final String str) {
        dismissWaitingDialog();
        this.mHandler.post(new Runnable() {
            public void run() {
                if (GoogleDriveFileListAdapter.this.mListener == null) {
                    return;
                }
                if (StringUtil.isEmptyOrNull(str)) {
                    GoogleDriveFileListAdapter.this.mListener.onStarted(false, GoogleDriveFileListAdapter.this.mActivity.getString(C4538R.string.zm_alert_auth_token_failed_msg));
                } else {
                    GoogleDriveFileListAdapter.this.mListener.onStarted(false, str);
                }
            }
        });
    }

    public void onAuthCancel() {
        dismissWaitingDialog();
        this.mHandler.post(new Runnable() {
            public void run() {
                if (GoogleDriveFileListAdapter.this.mListener != null) {
                    GoogleDriveFileListAdapter.this.mListener.onStarted(false, "");
                }
            }
        });
    }

    public void onAuthUnknowError() {
        dismissWaitingDialog();
        this.mHandler.post(new Runnable() {
            public void run() {
                if (GoogleDriveFileListAdapter.this.mListener != null) {
                    GoogleDriveFileListAdapter.this.mListener.onStarted(false, GoogleDriveFileListAdapter.this.mActivity.getString(C4538R.string.zm_alert_auth_token_failed_msg));
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void cancel() {
        this.mGoogleDrive.cancelAuth();
    }
}

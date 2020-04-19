package p021us.zoom.thirdparty.box;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import p021us.zoom.androidlib.app.IZMAppUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFileListBaseAdapter;
import p021us.zoom.androidlib.app.ZMFileListEntry;
import p021us.zoom.androidlib.app.ZMFileListListener;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.box.Box.BoxListener;
import p021us.zoom.thirdparty.login.C4538R;

/* renamed from: us.zoom.thirdparty.box.BoxFileListAdapter */
public class BoxFileListAdapter extends ZMFileListBaseAdapter implements BoxListener {
    /* access modifiers changed from: private */
    public Box mBox;
    private String mCachedDir;
    /* access modifiers changed from: private */
    public String mCurrentDir;
    /* access modifiers changed from: private */
    public HashMap<String, BoxFileObject> mDirCached = new HashMap<>();
    private IBoxFileDownloadListener mFileDownloadListener = new IBoxFileDownloadListener() {
        public void onDownloadFileCompeleted(BoxAsyncDownloadFile boxAsyncDownloadFile, BoxFileObject boxFileObject, String str) {
            BoxFileListAdapter.this.dismissWaitingDialog();
            BoxFileListAdapter.this.mBox.downloadFileTaskCompeleted(boxAsyncDownloadFile);
            if (BoxFileListAdapter.this.mListener != null) {
                BoxFileListAdapter.this.mListener.onSelectedFile(str, boxFileObject.getName());
            }
        }

        public void onError(BoxAsyncDownloadFile boxAsyncDownloadFile, BoxFileObject boxFileObject, Exception exc) {
            String str;
            BoxFileListAdapter.this.dismissWaitingDialog();
            BoxFileListAdapter.this.mBox.downloadFileTaskCompeleted(boxAsyncDownloadFile);
            if (!(boxFileObject == null || BoxFileListAdapter.this.mListener == null)) {
                if (exc != null) {
                    str = exc.getMessage();
                } else {
                    str = BoxFileListAdapter.this.mActivity.getString(C4538R.string.zm_msg_load_file_fail, new Object[]{boxFileObject.getPath()});
                }
                BoxFileListAdapter.this.mListener.onOpenFileFailed(str);
            }
        }

        public void onCancel(BoxAsyncDownloadFile boxAsyncDownloadFile, BoxFileObject boxFileObject) {
            BoxFileListAdapter.this.dismissWaitingDialog();
            BoxFileListAdapter.this.mBox.downloadFileTaskCompeleted(boxAsyncDownloadFile);
        }

        public void onProgress(BoxAsyncDownloadFile boxAsyncDownloadFile, BoxFileObject boxFileObject, long j, long j2) {
            if (j > 0) {
                long j3 = (j2 * 100) / j;
                BoxFileListAdapter boxFileListAdapter = BoxFileListAdapter.this;
                boxFileListAdapter.updateProgressWaitingDialog(boxFileListAdapter.mActivity.getString(C4538R.string.zm_msg_download_file_progress, new Object[]{Long.valueOf(j3)}));
                return;
            }
            BoxFileListAdapter boxFileListAdapter2 = BoxFileListAdapter.this;
            boxFileListAdapter2.updateProgressWaitingDialog(boxFileListAdapter2.mActivity.getString(C4538R.string.zm_msg_download_file_size, new Object[]{FileUtils.toFileSizeString(BoxFileListAdapter.this.mActivity, j2)}));
        }
    };
    /* access modifiers changed from: private */
    public ZMFileListListener mListener;
    private IBoxLoadFolderListener mLoadFolderListener = new IBoxLoadFolderListener() {
        public void onCompeleted(BoxAsyncLoadFolder boxAsyncLoadFolder, boolean z, String str, BoxFileObject boxFileObject, List<BoxFileObject> list) {
            BoxFileListAdapter.this.dismissWaitingDialog();
            BoxFileListAdapter.this.mBox.loadFolderTaskCompeleted(boxAsyncLoadFolder);
            if (z) {
                BoxFileListAdapter.this.mCurrentDir = str;
            } else if (boxFileObject == null) {
                BoxFileListAdapter.this.mCurrentDir = File.separator;
            } else {
                BoxFileListAdapter.this.mCurrentDir = boxFileObject.getPath();
                BoxFileListAdapter.this.mDirCached.put(str, boxFileObject);
            }
            BoxFileListAdapter.this.mFileList.clear();
            for (BoxFileObject boxFileObject2 : list) {
                if (boxFileObject2 != null && (boxFileObject2.isDir() || BoxFileListAdapter.this.acceptFileType(boxFileObject2.getPath()))) {
                    BoxFileListAdapter.this.mFileList.add(new BoxFileListEntry(boxFileObject2, boxFileObject));
                }
            }
            BoxFileListAdapter.this.sortFileList();
            BoxFileListAdapter.this.notifyDataSetChanged();
            if (BoxFileListAdapter.this.mListener != null) {
                BoxFileListAdapter.this.mListener.onRefresh();
            }
        }

        public void onError(BoxAsyncLoadFolder boxAsyncLoadFolder, String str, Exception exc) {
            String str2;
            BoxFileListAdapter.this.dismissWaitingDialog();
            BoxFileListAdapter.this.mBox.loadFolderTaskCompeleted(boxAsyncLoadFolder);
            if (BoxFileListAdapter.this.mListener != null) {
                if (exc == null) {
                    str2 = null;
                } else {
                    str2 = exc.getMessage();
                }
                if (StringUtil.isEmptyOrNull(str2)) {
                    str2 = BoxFileListAdapter.this.mActivity.getString(C4538R.string.zm_msg_load_dir_fail, new Object[]{str});
                }
                BoxFileListAdapter.this.mListener.onOpenDirFailed(str2);
            }
        }

        public void onCancel(BoxAsyncLoadFolder boxAsyncLoadFolder, String str) {
            BoxFileListAdapter.this.dismissWaitingDialog();
            BoxFileListAdapter.this.mBox.loadFolderTaskCompeleted(boxAsyncLoadFolder);
        }
    };
    private OnCancelListener mWaitingDialogCancelListener = new OnCancelListener() {
        public void onCancel(DialogInterface dialogInterface) {
            BoxFileListAdapter.this.mBox.cancel();
        }
    };

    public boolean isNeedAuth() {
        return true;
    }

    public void init(ZMActivity zMActivity, ZMFileListListener zMFileListListener) {
        super.init(zMActivity, zMFileListListener);
        this.mListener = zMFileListListener;
        IZMAppUtil iZMAppUtil = BoxMgr.getInstance().getmIZMAppUtil();
        if (iZMAppUtil != null) {
            this.mCachedDir = iZMAppUtil.getCachePath();
        }
        this.mBox = BoxMgr.getInstance().getBox();
        this.mBox.setListener(this);
    }

    public void login() {
        if (!this.mBox.hasAuthenticated()) {
            this.mBox.login(this.mActivity);
            ZMFileListListener zMFileListListener = this.mListener;
            if (zMFileListListener != null) {
                zMFileListListener.onStarting();
                return;
            }
            return;
        }
        ZMFileListListener zMFileListListener2 = this.mListener;
        if (zMFileListListener2 != null) {
            zMFileListListener2.onStarted(true, null);
        }
    }

    public void logout() {
        this.mBox.logout();
    }

    public void onDestroy() {
        if (this.mActivity.isFinishing()) {
            this.mBox.stopAuth(this.mActivity);
        }
    }

    public void onResume() {
        login();
    }

    public void onPause() {
        cancel();
    }

    public boolean onBackPressed() {
        cancel();
        return super.onBackPressed();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        this.mBox.handleActivityResult(i, i2, intent);
    }

    public boolean openDir(String str) {
        boolean z;
        if (StringUtil.isEmptyOrNull(str)) {
            str = File.separator;
        }
        BoxFileObject boxFileObject = null;
        if (this.mDirCached.containsKey(str)) {
            boxFileObject = (BoxFileObject) this.mDirCached.get(str);
        }
        if (boxFileObject == null) {
            z = this.mBox.asyncLoadDir(str, this.mLoadFolderListener);
        } else {
            z = this.mBox.asyncLoadDir(boxFileObject, this.mLoadFolderListener);
        }
        if (z) {
            showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void openDir(ZMFileListEntry zMFileListEntry) {
        if (zMFileListEntry != null && zMFileListEntry.isDir() && (zMFileListEntry instanceof BoxFileListEntry)) {
            BoxFileObject object = ((BoxFileListEntry) zMFileListEntry).getObject();
            if (object != null && this.mBox.asyncLoadDir(object, this.mLoadFolderListener)) {
                showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void openFile(ZMFileListEntry zMFileListEntry) {
        if (zMFileListEntry != null && !zMFileListEntry.isDir()) {
            long bytes = zMFileListEntry.getBytes();
            if (bytes <= 0) {
                bytes = 52428800;
            }
            IZMAppUtil iZMAppUtil = BoxMgr.getInstance().getmIZMAppUtil();
            if (iZMAppUtil != null && !iZMAppUtil.hasEnoughDiskSpace(this.mCachedDir, bytes)) {
                alertMemoryNotEnough(this.mActivity.getString(C4538R.string.zm_title_error), this.mActivity.getString(C4538R.string.zm_msg_memory_size_insufficient));
            } else if (zMFileListEntry instanceof BoxFileListEntry) {
                BoxFileObject object = ((BoxFileListEntry) zMFileListEntry).getObject();
                if (object != null && acceptFileType(object.getPath()) && this.mBox.asyncDownloadFile(object, this.mCachedDir, this.mFileDownloadListener)) {
                    showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_download_file_progress, new Object[]{Integer.valueOf(0)}), this.mWaitingDialogCancelListener);
                }
            }
        }
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
        boolean z;
        if (!isRootDir() && this.mCurrentDir != null && this.mBox.hasAuthenticated()) {
            String sb = new StringBuilder(getCurrentDirPath()).toString();
            if (sb.endsWith(File.separator)) {
                sb = sb.substring(0, sb.lastIndexOf(File.separator));
            }
            String substring = sb.substring(0, sb.lastIndexOf(File.separator) + 1);
            if (!substring.equals(getCurrentDirName())) {
                if (!substring.equals(File.separator) && substring.endsWith(File.separator)) {
                    substring = substring.substring(0, substring.length() - 1);
                }
                BoxFileObject boxFileObject = null;
                if (this.mDirCached.containsKey(substring)) {
                    boxFileObject = (BoxFileObject) this.mDirCached.get(substring);
                }
                if (boxFileObject == null) {
                    z = this.mBox.asyncLoadDir(substring, this.mLoadFolderListener);
                } else {
                    z = this.mBox.asyncLoadDir(boxFileObject, this.mLoadFolderListener);
                }
                if (z) {
                    showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
                }
            }
        }
    }

    public void onAuthStarting() {
        ZMFileListListener zMFileListListener = this.mListener;
        if (zMFileListListener != null) {
            zMFileListListener.onStarting();
        }
    }

    public void onAuthSuccess() {
        ZMFileListListener zMFileListListener = this.mListener;
        if (zMFileListListener != null) {
            zMFileListListener.onStarted(true, null);
        }
    }

    public void onAuthError(String str) {
        if (this.mListener == null) {
            return;
        }
        if (StringUtil.isEmptyOrNull(str)) {
            this.mListener.onStarted(false, this.mActivity.getString(C4538R.string.zm_alert_auth_token_failed_msg));
        } else {
            this.mListener.onStarted(false, str);
        }
    }

    public void onAuthUnknowError() {
        ZMFileListListener zMFileListListener = this.mListener;
        if (zMFileListListener != null) {
            zMFileListListener.onStarted(false, this.mActivity.getString(C4538R.string.zm_alert_auth_token_failed_msg));
        }
    }

    private void cancel() {
        this.mBox.cancel();
    }
}

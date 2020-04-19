package p021us.zoom.thirdparty.dropbox;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import com.dropbox.core.p005v2.files.FileMetadata;
import com.dropbox.core.p005v2.files.FolderMetadata;
import com.dropbox.core.p005v2.files.Metadata;
import com.dropbox.core.p005v2.sharing.SharedLinkMetadata;
import java.io.File;
import java.util.List;
import p021us.zoom.androidlib.app.IZMAppUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFileListBaseAdapter;
import p021us.zoom.androidlib.app.ZMFileListEntry;
import p021us.zoom.androidlib.app.ZMFileListListener;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.thirdparty.dropbox.ZMDropboxClient.DropboxListener;
import p021us.zoom.thirdparty.login.C4538R;

/* renamed from: us.zoom.thirdparty.dropbox.DropboxFileListAdapter */
public class DropboxFileListAdapter extends ZMFileListBaseAdapter implements DropboxListener {
    private String mCachedDir;
    private String mCurrentDir = null;
    /* access modifiers changed from: private */
    public ZMDropbox mDropbox;
    /* access modifiers changed from: private */
    public ZMFileListListener mListener;
    private OnCancelListener mWaitingDialogCancelListener = new OnCancelListener() {
        public void onCancel(DialogInterface dialogInterface) {
            if (DropboxFileListAdapter.this.mDropbox.hasLogin()) {
                DropboxFileListAdapter.this.mDropbox.getClient().cancel();
            }
        }
    };

    public boolean isNeedAuth() {
        return true;
    }

    public void init(ZMActivity zMActivity, ZMFileListListener zMFileListListener) {
        super.init(zMActivity, zMFileListListener);
        this.mActivity = zMActivity;
        this.mDropbox = ZMDropbox.getInstance();
        this.mListener = zMFileListListener;
        IZMAppUtil iZMAppUtil = this.mDropbox.getmIZMAppUtil();
        if (iZMAppUtil != null) {
            this.mCachedDir = iZMAppUtil.getCachePath();
        }
        if (this.mDropbox.hasLogin()) {
            this.mDropbox.setListener(this);
        }
    }

    public void login() {
        if (!this.mDropbox.hasLogin()) {
            this.mDropbox.login(this.mActivity);
            ZMFileListListener zMFileListListener = this.mListener;
            if (zMFileListListener != null) {
                zMFileListListener.onStarting();
                return;
            }
            return;
        }
        this.mDropbox.setListener(this);
        ZMFileListListener zMFileListListener2 = this.mListener;
        if (zMFileListListener2 != null) {
            zMFileListListener2.onStarted(true, null);
        }
    }

    public void logout() {
        if (this.mDropbox.hasLogin()) {
            ZMDropbox zMDropbox = this.mDropbox;
            ZMDropbox.release();
        }
    }

    public void onResume() {
        this.mDropbox.onResume(this.mActivity);
        if (this.mDropbox.hasLogin()) {
            this.mDropbox.setListener(this);
            ZMFileListListener zMFileListListener = this.mListener;
            if (zMFileListListener != null) {
                zMFileListListener.onStarted(true, null);
                return;
            }
            return;
        }
        ZMFileListListener zMFileListListener2 = this.mListener;
        if (zMFileListListener2 != null) {
            zMFileListListener2.onStarted(false, this.mActivity.getResources().getString(C4538R.string.zm_alert_auth_token_failed_msg));
        }
    }

    public boolean openDir(String str) {
        if (!this.mDropbox.hasLogin()) {
            return false;
        }
        if (StringUtil.isEmptyOrNull(str)) {
            if (this.mCurrentDir == null) {
                str = File.separator;
            } else if (this.mFileList.size() > 0) {
                return true;
            } else {
                str = this.mCurrentDir;
            }
        } else if (str.equals(this.mCurrentDir) && this.mFileList.size() > 0) {
            return true;
        }
        return loadDir(str);
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
        if (this.mDropbox.hasLogin() && !isRootDir()) {
            String sb = new StringBuilder(getCurrentDirPath()).toString();
            if (sb.endsWith(File.separator)) {
                sb = sb.substring(0, sb.lastIndexOf(File.separator));
            }
            String substring = sb.substring(0, sb.lastIndexOf(File.separator) + 1);
            if (!substring.equals(getCurrentDirName())) {
                loadDir(substring);
            }
        }
    }

    public void onPause() {
        this.mDropbox.setListener(null);
        cancel();
    }

    public boolean onBackPressed() {
        cancel();
        return super.onBackPressed();
    }

    private void cancel() {
        dismissWaitingDialog();
        if (this.mDropbox.hasLogin()) {
            this.mDropbox.getClient().cancel();
        }
    }

    private boolean dropboxLoadDir(String str) {
        if (StringUtil.isEmptyOrNull(str) || !this.mDropbox.hasLogin()) {
            return false;
        }
        boolean loadDir = this.mDropbox.getClient().loadDir(str);
        if (loadDir) {
            showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
        }
        return loadDir;
    }

    private boolean loadDir(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return dropboxLoadDir(str);
    }

    /* access modifiers changed from: protected */
    public void openDir(ZMFileListEntry zMFileListEntry) {
        if (zMFileListEntry != null && zMFileListEntry.isDir()) {
            loadDir(zMFileListEntry.getPath());
        }
    }

    /* access modifiers changed from: protected */
    public void openFile(ZMFileListEntry zMFileListEntry) {
        if (zMFileListEntry != null && this.mDropbox.hasLogin() && !StringUtil.isEmptyOrNull(zMFileListEntry.getPath()) && !zMFileListEntry.isDir()) {
            IZMAppUtil iZMAppUtil = this.mDropbox.getmIZMAppUtil();
            if (iZMAppUtil != null) {
                if (!iZMAppUtil.hasEnoughDiskSpace(this.mCachedDir, zMFileListEntry.getBytes())) {
                    alertMemoryNotEnough(this.mActivity.getString(C4538R.string.zm_title_error), this.mActivity.getString(C4538R.string.zm_msg_memory_size_insufficient));
                    return;
                }
                if (zMFileListEntry instanceof DropboxFileListEntry) {
                    showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
                    if (this.mIsShareLinkEnable) {
                        this.mDropbox.getClient().shareFile((DropboxFileListEntry) zMFileListEntry);
                    } else {
                        this.mDropbox.getClient().loadFile((DropboxFileListEntry) zMFileListEntry, this.mCachedDir);
                    }
                }
            }
        }
    }

    public void onLoadDir(DropboxResult dropboxResult, String str, List<Metadata> list) {
        if (!StringUtil.isEmptyOrNull(str)) {
            dismissWaitingDialog();
            if (dropboxResult == DropboxResult.CANCELED) {
                if (isRootDir()) {
                    ZMFileListListener zMFileListListener = this.mListener;
                    if (zMFileListListener != null) {
                        zMFileListListener.onSelectedFile(null, null);
                    }
                }
                return;
            }
            if (dropboxResult == DropboxResult.OK) {
                this.mCurrentDir = str;
                this.mFileList.clear();
                addAllEntry(list);
                notifyDataSetChanged();
                ZMFileListListener zMFileListListener2 = this.mListener;
                if (zMFileListListener2 != null) {
                    zMFileListListener2.onRefresh();
                }
            } else if (dropboxResult == DropboxResult.SERVICE_UNAVAILABLE || dropboxResult == DropboxResult.SSL_EXCEPTION || dropboxResult == DropboxResult.UNAUTHORIZED || dropboxResult == DropboxResult.UNLINKED) {
                ZMFileListListener zMFileListListener3 = this.mListener;
                if (zMFileListListener3 != null) {
                    zMFileListListener3.onReLogin();
                }
            } else {
                alertError(this.mActivity.getString(C4538R.string.zm_msg_load_dir_fail, new Object[]{str}));
            }
        }
    }

    public void onLoadFile(DropboxResult dropboxResult, String str, String str2) {
        if (dropboxResult != DropboxResult.CANCELED) {
            dismissWaitingDialog();
            if (dropboxResult == DropboxResult.OK) {
                ZMFileListListener zMFileListListener = this.mListener;
                if (zMFileListListener != null) {
                    zMFileListListener.onSelectedFile(str2, str);
                }
            } else if (dropboxResult == DropboxResult.SERVICE_UNAVAILABLE || dropboxResult == DropboxResult.SSL_EXCEPTION || dropboxResult == DropboxResult.UNAUTHORIZED || dropboxResult == DropboxResult.UNLINKED) {
                ZMFileListListener zMFileListListener2 = this.mListener;
                if (zMFileListListener2 != null) {
                    zMFileListListener2.onReLogin();
                }
            } else {
                alertError(this.mActivity.getString(C4538R.string.zm_msg_load_file_fail, new Object[]{str}));
            }
        }
    }

    public void onShareLink(DropboxResult dropboxResult, SharedLinkMetadata sharedLinkMetadata, FileMetadata fileMetadata) {
        if (dropboxResult != DropboxResult.CANCELED) {
            dismissWaitingDialog();
            if (dropboxResult == DropboxResult.OK) {
                if (sharedLinkMetadata != null && fileMetadata != null) {
                    ZMFileListListener zMFileListListener = this.mListener;
                    if (zMFileListListener != null) {
                        zMFileListListener.onSharedFileLink(fileMetadata.getId(), fileMetadata.getName(), sharedLinkMetadata.getUrl(), fileMetadata.getSize(), ZMDropbox.getInstance().getmFileIntegrationType());
                    }
                } else if (fileMetadata != null) {
                    alertError(this.mActivity.getString(C4538R.string.zm_msg_share_file_fail_68764, new Object[]{fileMetadata.getName()}));
                }
            } else if (dropboxResult == DropboxResult.SERVICE_UNAVAILABLE || dropboxResult == DropboxResult.SSL_EXCEPTION || dropboxResult == DropboxResult.UNAUTHORIZED || dropboxResult == DropboxResult.UNLINKED) {
                ZMFileListListener zMFileListListener2 = this.mListener;
                if (zMFileListListener2 != null) {
                    zMFileListListener2.onReLogin();
                }
            } else {
                alertError(this.mActivity.getString(C4538R.string.zm_msg_share_file_fail_68764, new Object[]{fileMetadata.getName()}));
            }
        }
    }

    private void alertError(String str) {
        new Builder(this.mActivity).setTitle((CharSequence) str).setPositiveButton(C4538R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (DropboxFileListAdapter.this.mListener != null) {
                    DropboxFileListAdapter.this.mListener.onSelectedFile(null, null);
                }
            }
        }).create().show();
    }

    private void addAllEntry(List<Metadata> list) {
        if (list != null && !list.isEmpty()) {
            for (Metadata metadata : list) {
                String pathLower = metadata.getPathLower();
                if (metadata instanceof FileMetadata) {
                    if (acceptFileType(pathLower)) {
                        FileMetadata fileMetadata = (FileMetadata) metadata;
                        DropboxFileListEntry dropboxFileListEntry = new DropboxFileListEntry(metadata);
                        dropboxFileListEntry.setBytes(fileMetadata.getSize());
                        dropboxFileListEntry.setDate(fileMetadata.getClientModified());
                        dropboxFileListEntry.setDir(false);
                        dropboxFileListEntry.setDisplayName(fileMetadata.getName());
                        dropboxFileListEntry.setPath(fileMetadata.getPathLower());
                        this.mFileList.add(dropboxFileListEntry);
                    }
                } else if (metadata instanceof FolderMetadata) {
                    FolderMetadata folderMetadata = (FolderMetadata) metadata;
                    DropboxFileListEntry dropboxFileListEntry2 = new DropboxFileListEntry(metadata);
                    dropboxFileListEntry2.setBytes(0);
                    dropboxFileListEntry2.setDir(true);
                    dropboxFileListEntry2.setDisplayName(folderMetadata.getName());
                    dropboxFileListEntry2.setPath(folderMetadata.getPathLower());
                    this.mFileList.add(dropboxFileListEntry2);
                }
            }
            sortFileList();
        }
    }

    public boolean hasAuthorized() {
        return this.mDropbox.hasLogin();
    }
}

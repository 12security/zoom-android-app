package p021us.zoom.thirdparty.onedrive;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.extensions.Permission;
import java.util.List;
import p021us.zoom.androidlib.app.IZMAppUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFileListBaseAdapter;
import p021us.zoom.androidlib.app.ZMFileListEntry;
import p021us.zoom.androidlib.app.ZMFileListListener;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.IDownloadFileListener;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncURLDownloadFile;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.thirdparty.login.C4538R;
import p021us.zoom.thirdparty.onedrive.OneDrive.Listener;

/* renamed from: us.zoom.thirdparty.onedrive.BaseOneDriveFileListAdapter */
public abstract class BaseOneDriveFileListAdapter extends ZMFileListBaseAdapter implements Listener {
    private OnCancelListener mAuthingDialogCancelListener = new OnCancelListener() {
        public void onCancel(DialogInterface dialogInterface) {
            BaseOneDriveFileListAdapter.this.mOneDrive.cancelAuth();
            if (BaseOneDriveFileListAdapter.this.mActivity != null && BaseOneDriveFileListAdapter.this.mActivity.isActive()) {
                BaseOneDriveFileListAdapter.this.mActivity.onBackPressed();
            }
        }
    };
    private String mCachedDir;
    /* access modifiers changed from: private */
    public Item mCurrentItem;
    private IDownloadFileListener mFileDownloadListener = new IDownloadFileListener() {
        public void onDownloadCompleted(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, Uri uri, String str) {
            BaseOneDriveFileListAdapter.this.mOneDrive.removeDownloadFileTask(zMAsyncURLDownloadFile);
            BaseOneDriveFileListAdapter.this.dismissWaitingDialog();
            if (BaseOneDriveFileListAdapter.this.mListener != null) {
                BaseOneDriveFileListAdapter.this.mListener.onSelectedFile(str, FileUtils.getFileName(str));
            }
        }

        public void onDownloadFailed(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, Uri uri) {
            BaseOneDriveFileListAdapter.this.mOneDrive.removeDownloadFileTask(zMAsyncURLDownloadFile);
            BaseOneDriveFileListAdapter.this.dismissWaitingDialog();
            String str = zMAsyncURLDownloadFile.getmOutput();
            if (StringUtil.isEmptyOrNull(str) && BaseOneDriveFileListAdapter.this.mListener != null) {
                BaseOneDriveFileListAdapter.this.mListener.onOpenFileFailed(BaseOneDriveFileListAdapter.this.mActivity.getString(C4538R.string.zm_msg_load_file_fail, new Object[]{str}));
            }
        }

        public void onDownloadProgress(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, long j, long j2) {
            BaseOneDriveFileListAdapter.this.updateProgressWaitingDialog(BaseOneDriveFileListAdapter.this.mActivity.getString(C4538R.string.zm_msg_download_file_progress, new Object[]{Integer.valueOf((int) ((((float) j2) / ((float) j)) * 100.0f))}));
        }

        public void onDownloadCanceled(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, Uri uri) {
            BaseOneDriveFileListAdapter.this.mOneDrive.removeDownloadFileTask(zMAsyncURLDownloadFile);
            BaseOneDriveFileListAdapter.this.dismissWaitingDialog();
        }
    };
    private IODFoldLoaderListener mFloaderListener = new IODFoldLoaderListener() {
        public void onLoadFoldCompleted(Item item, List<Item> list) {
            BaseOneDriveFileListAdapter.this.dismissWaitingDialog();
            BaseOneDriveFileListAdapter.this.mCurrentItem = item;
            BaseOneDriveFileListAdapter.this.mFileList.clear();
            for (Item item2 : list) {
                if (item2.isFolder() || BaseOneDriveFileListAdapter.this.acceptFileType(item2.getPath())) {
                    BaseOneDriveFileListAdapter.this.mFileList.add(new OneDriveEntry(item2, item));
                }
            }
            BaseOneDriveFileListAdapter.this.sortFileList();
            BaseOneDriveFileListAdapter.this.notifyDataSetChanged();
            if (BaseOneDriveFileListAdapter.this.mListener != null) {
                BaseOneDriveFileListAdapter.this.mListener.onRefresh();
            }
        }

        public void onError(ClientException clientException) {
            BaseOneDriveFileListAdapter.this.dismissWaitingDialog();
            if (BaseOneDriveFileListAdapter.this.mListener != null) {
                String message = clientException.getMessage();
                if (StringUtil.isEmptyOrNull(message) || (BaseOneDriveFileListAdapter.this.mCurrentItem != null && !StringUtil.isEmptyOrNull(BaseOneDriveFileListAdapter.this.mCurrentItem.getPath()))) {
                    message = BaseOneDriveFileListAdapter.this.mActivity.getString(C4538R.string.zm_msg_load_dir_fail, new Object[]{BaseOneDriveFileListAdapter.this.mCurrentItem.getPath()});
                }
                BaseOneDriveFileListAdapter.this.mListener.onOpenDirFailed(message);
            }
        }

        public void onCanceled(String str) {
            BaseOneDriveFileListAdapter.this.dismissWaitingDialog();
        }
    };
    /* access modifiers changed from: private */
    public ZMFileListListener mListener;
    protected OneDrive mOneDrive;
    private OnCancelListener mWaitingDialogCancelListener = new OnCancelListener() {
        public void onCancel(DialogInterface dialogInterface) {
            BaseOneDriveFileListAdapter.this.mOneDrive.cancel();
            if (BaseOneDriveFileListAdapter.this.mActivity != null && BaseOneDriveFileListAdapter.this.mActivity.isActive()) {
                BaseOneDriveFileListAdapter.this.mActivity.onBackPressed();
            }
        }
    };

    public boolean isNeedAuth() {
        return true;
    }

    public void init(ZMActivity zMActivity, ZMFileListListener zMFileListListener) {
        super.init(zMActivity, zMFileListListener);
        this.mListener = zMFileListListener;
        IZMAppUtil iZMAppUtil = OneDriveManager.getInstance().getmIZMAppUtil();
        if (iZMAppUtil != null) {
            this.mCachedDir = iZMAppUtil.getCachePath();
        }
    }

    public void login() {
        this.mOneDrive.login(this.mActivity);
    }

    public void logout() {
        this.mOneDrive.logout();
    }

    public void onResume() {
        this.mOneDrive.onResume(this.mActivity);
    }

    public void onDestroy() {
        cancel();
        this.mOneDrive.setListener(null);
    }

    public boolean onBackPressed() {
        cancel();
        return super.onBackPressed();
    }

    private void cancel() {
        this.mOneDrive.cancel();
    }

    public boolean openDir(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            str = "root";
        }
        boolean asyncLoadFolderByItemId = this.mOneDrive.asyncLoadFolderByItemId(str, this.mFloaderListener);
        if (asyncLoadFolderByItemId) {
            showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
        }
        return asyncLoadFolderByItemId;
    }

    /* access modifiers changed from: protected */
    public void openDir(ZMFileListEntry zMFileListEntry) {
        if (zMFileListEntry != null && zMFileListEntry.isDir() && (zMFileListEntry instanceof OneDriveEntry)) {
            if (this.mOneDrive.asyncLoadFolder((OneDriveEntry) zMFileListEntry, this.mFloaderListener)) {
                showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void openFile(ZMFileListEntry zMFileListEntry) {
        if (zMFileListEntry != null && this.mActivity != null) {
            IZMAppUtil iZMAppUtil = OneDriveManager.getInstance().getmIZMAppUtil();
            if (iZMAppUtil != null && !iZMAppUtil.hasEnoughDiskSpace(this.mCachedDir, zMFileListEntry.getBytes())) {
                alertMemoryNotEnough(this.mActivity.getString(C4538R.string.zm_title_error), this.mActivity.getString(C4538R.string.zm_msg_memory_size_insufficient));
            } else if (zMFileListEntry instanceof OneDriveEntry) {
                OneDriveEntry oneDriveEntry = (OneDriveEntry) zMFileListEntry;
                if (!oneDriveEntry.isDir() && acceptFileType(oneDriveEntry.getPath())) {
                    final Item object = oneDriveEntry.getObject();
                    if (this.mIsShareLinkEnable) {
                        this.mOneDrive.getmClient().getDrive().getItems(object.getItemId()).getCreateLink("view").buildRequest().post(new ICallback<Permission>() {
                            public void success(Permission permission) {
                                BaseOneDriveFileListAdapter.this.mListener.onSharedFileLink(permission.f302id, object.name, permission.link.webUrl, object.size.longValue(), 2);
                            }

                            public void failure(ClientException clientException) {
                                BaseOneDriveFileListAdapter baseOneDriveFileListAdapter = BaseOneDriveFileListAdapter.this;
                                baseOneDriveFileListAdapter.alertError(baseOneDriveFileListAdapter.mActivity.getString(C4538R.string.zm_msg_share_file_fail_68764, new Object[]{object.name}));
                            }
                        });
                        return;
                    }
                    if (this.mOneDrive.asyncDownloadFile(oneDriveEntry.getObject(), this.mCachedDir, this.mFileDownloadListener)) {
                        showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_download_file_progress, new Object[]{Integer.valueOf(0)}), this.mWaitingDialogCancelListener);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void alertError(String str) {
        new Builder(this.mActivity).setTitle((CharSequence) str).setPositiveButton(C4538R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (BaseOneDriveFileListAdapter.this.mListener != null) {
                    BaseOneDriveFileListAdapter.this.mListener.onSelectedFile(null, null);
                }
            }
        }).create().show();
    }

    public boolean isRootDir() {
        Item item = this.mCurrentItem;
        return item == null || item.isRoot();
    }

    public String getCurrentDirName() {
        Item item = this.mCurrentItem;
        if (item == null) {
            return "";
        }
        return StringUtil.safeString(item.getShowName());
    }

    public String getCurrentDirPath() {
        Item item = this.mCurrentItem;
        if (item == null) {
            return "root";
        }
        return item.getItemId();
    }

    /* access modifiers changed from: protected */
    public void gotoParentDir() {
        if (!isRootDir() && this.mOneDrive.isAuthed()) {
            Item item = this.mCurrentItem;
            if (item != null && !item.isRoot() && this.mOneDrive.asyncLoadFolderByItemId(this.mCurrentItem.getmPItemId(), this.mFloaderListener)) {
                showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mWaitingDialogCancelListener);
            }
        }
    }

    public void onAuthStarting() {
        showWaitingDialog(this.mActivity.getString(C4538R.string.zm_msg_loading), this.mAuthingDialogCancelListener);
        ZMFileListListener zMFileListListener = this.mListener;
        if (zMFileListListener != null) {
            zMFileListListener.onStarting();
        }
    }

    public void onAuthSuccess() {
        dismissWaitingDialog();
        ZMFileListListener zMFileListListener = this.mListener;
        if (zMFileListListener != null) {
            zMFileListListener.onStarted(true, null);
        }
    }

    public void onAuthError(ClientException clientException) {
        dismissWaitingDialog();
        ZMFileListListener zMFileListListener = this.mListener;
        if (zMFileListListener != null) {
            zMFileListListener.onStarted(false, this.mActivity.getString(C4538R.string.zm_alert_auth_token_failed_msg));
        }
    }
}

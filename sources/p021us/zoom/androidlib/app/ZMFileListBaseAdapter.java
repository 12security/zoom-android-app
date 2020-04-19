package p021us.zoom.androidlib.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil.MimeType;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;

/* renamed from: us.zoom.androidlib.app.ZMFileListBaseAdapter */
public abstract class ZMFileListBaseAdapter extends BaseAdapter {
    protected final long MIN_CHECK_SPACE_SIZE = 52428800;
    /* access modifiers changed from: protected */
    public ZMActivity mActivity;
    /* access modifiers changed from: protected */
    public ArrayList<ZMFileListEntry> mFileList = new ArrayList<>();
    protected ArrayList<String> mFilterExtensions = new ArrayList<>();
    protected ArrayList<MimeType> mFilterMimtypes = new ArrayList<>();
    protected LayoutInflater mInflater;
    protected boolean mIsShareLinkEnable = false;
    protected String mLastErrorMessage = null;
    private int mSelectedPos = -1;
    protected ProgressDialog mWaitingDialog = null;

    /* renamed from: us.zoom.androidlib.app.ZMFileListBaseAdapter$ItemFileNameComparator */
    private class ItemFileNameComparator implements Comparator<ZMFileListEntry> {
        private ItemFileNameComparator() {
        }

        public int compare(ZMFileListEntry zMFileListEntry, ZMFileListEntry zMFileListEntry2) {
            if (zMFileListEntry.isDir() && !zMFileListEntry2.isDir()) {
                return -1;
            }
            if (!zMFileListEntry.isDir() && zMFileListEntry2.isDir()) {
                return 1;
            }
            Locale localDefault = CompatUtils.getLocalDefault();
            return zMFileListEntry.getDisplayName().toLowerCase(localDefault).compareTo(zMFileListEntry2.getDisplayName().toLowerCase(localDefault));
        }
    }

    public abstract String getCurrentDirName();

    public abstract String getCurrentDirPath();

    public long getItemId(int i) {
        return (long) i;
    }

    /* access modifiers changed from: protected */
    public abstract void gotoParentDir();

    public boolean hasAuthorized() {
        return false;
    }

    public boolean isNeedAuth() {
        return false;
    }

    public abstract boolean isRootDir();

    public void login() {
    }

    public void logout() {
    }

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onStart() {
    }

    public void onStoragePermissionResult(int i) {
    }

    /* access modifiers changed from: protected */
    public void openDir(ZMFileListEntry zMFileListEntry) {
    }

    public abstract boolean openDir(String str);

    /* access modifiers changed from: protected */
    public void openFile(ZMFileListEntry zMFileListEntry) {
    }

    public Context getContext() {
        return this.mActivity;
    }

    public ZMActivity getActivity() {
        return this.mActivity;
    }

    public void init(ZMActivity zMActivity, ZMFileListListener zMFileListListener) {
        this.mActivity = zMActivity;
        this.mInflater = (LayoutInflater) this.mActivity.getSystemService("layout_inflater");
    }

    public void setLastErrorMessage(String str) {
        this.mLastErrorMessage = str;
    }

    public String getLastErrorMessage() {
        return this.mLastErrorMessage;
    }

    public void onDestroy() {
        dismissWaitingDialog();
    }

    public final void upDir() {
        this.mSelectedPos = -1;
        gotoParentDir();
    }

    public void onRefresh() {
        notifyDataSetChanged();
    }

    public void onClickItem(int i) {
        if (i < getCount()) {
            ZMFileListEntry item = getItem(i);
            if (item == null || !item.isDir()) {
                if (this.mSelectedPos == i) {
                    this.mSelectedPos = -1;
                } else {
                    this.mSelectedPos = i;
                }
                notifyDataSetChanged();
                return;
            }
            openDir(item);
            this.mSelectedPos = -1;
            notifyDataSetChanged();
        }
    }

    public boolean isFileSelected() {
        int i = this.mSelectedPos;
        if (i < 0 || i > getCount()) {
            return false;
        }
        return !isDir(this.mSelectedPos);
    }

    public boolean onBackPressed() {
        if (isRootDir()) {
            return true;
        }
        upDir();
        return false;
    }

    public void openSelectedFile() {
        int i = this.mSelectedPos;
        if (i >= 0 && i <= getCount()) {
            openFile(getItem(this.mSelectedPos));
        }
    }

    /* access modifiers changed from: protected */
    public boolean isDir(int i) {
        ZMFileListEntry item = getItem(i);
        if (item == null) {
            return false;
        }
        return item.isDir();
    }

    public int getCount() {
        return this.mFileList.size();
    }

    @Nullable
    public ZMFileListEntry getItem(int i) {
        if (i >= this.mFileList.size()) {
            return null;
        }
        return (ZMFileListEntry) this.mFileList.get(i);
    }

    @Nullable
    private View getFileItemView(ZMFileListEntry zMFileListEntry, View view, ViewGroup viewGroup, boolean z) {
        ZMFileListItemView zMFileListItemView;
        if (zMFileListEntry == null) {
            return null;
        }
        if (view == null || !(view instanceof ZMFileListItemView)) {
            zMFileListItemView = new ZMFileListItemView(this.mActivity);
        } else {
            zMFileListItemView = (ZMFileListItemView) view;
        }
        zMFileListItemView.setDisplayName(zMFileListEntry.getDisplayName());
        if (zMFileListEntry.isDir()) {
            zMFileListItemView.setFolderIndicatorVisible(true);
        } else {
            zMFileListItemView.setFolderIndicatorVisible(false);
        }
        String path = zMFileListEntry.getPath();
        if (zMFileListEntry.isDir()) {
            zMFileListItemView.setIcon(C4409R.C4410drawable.zm_ic_filetype_folder);
        } else {
            zMFileListItemView.setIcon(AndroidAppUtil.getIconForFile(path));
        }
        zMFileListItemView.setLastModified(zMFileListEntry.getDate());
        if (zMFileListEntry.isDir()) {
            zMFileListItemView.setChildrenCount(zMFileListEntry.getChildEntryCount());
        } else {
            zMFileListItemView.setFileSize(zMFileListEntry.getBytes());
        }
        zMFileListItemView.setItemChecked(z);
        return zMFileListItemView;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        ZMFileListEntry item = getItem(i);
        if (item == null) {
            return null;
        }
        return getFileItemView(item, view, viewGroup, i == this.mSelectedPos);
    }

    /* access modifiers changed from: protected */
    public void showToast(String str) {
        Toast.makeText(this.mActivity, str, 1).show();
    }

    public void setFilterExtens(String[] strArr) {
        this.mFilterExtensions.clear();
        this.mFilterMimtypes.clear();
        if (strArr != null && strArr.length > 0) {
            for (String str : strArr) {
                if (!StringUtil.isEmptyOrNull(str)) {
                    this.mFilterExtensions.add(str);
                    this.mFilterMimtypes.add(AndroidAppUtil.getMimeTypeOfFile(str));
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean acceptFileType(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        if (this.mFilterExtensions.size() <= 0) {
            return true;
        }
        String fileExtendName = AndroidAppUtil.getFileExtendName(str);
        if (fileExtendName != null && this.mFilterExtensions.contains(fileExtendName.toLowerCase(Locale.US))) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean acceptFileTypeByMimeType(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        if (this.mFilterMimtypes.size() <= 0) {
            return true;
        }
        Iterator it = this.mFilterMimtypes.iterator();
        while (it.hasNext()) {
            if (str.equals(((MimeType) it.next()).mimeType)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void sortFileList() {
        ArrayList<ZMFileListEntry> arrayList = this.mFileList;
        if (arrayList != null && arrayList.size() > 0) {
            Collections.sort(this.mFileList, new ItemFileNameComparator());
        }
    }

    /* access modifiers changed from: protected */
    public void showWaitingDialog(String str, OnCancelListener onCancelListener) {
        if (this.mWaitingDialog == null) {
            this.mWaitingDialog = new ProgressDialog(this.mActivity);
            this.mWaitingDialog.setOnCancelListener(onCancelListener);
            this.mWaitingDialog.setCancelable(true);
            this.mWaitingDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    if (ZMFileListBaseAdapter.this.mWaitingDialog == null || !ZMFileListBaseAdapter.this.mWaitingDialog.isShowing()) {
                        ZMFileListBaseAdapter.this.mWaitingDialog = null;
                    }
                }
            });
            this.mWaitingDialog.requestWindowFeature(1);
            this.mWaitingDialog.setMessage(str);
            this.mWaitingDialog.setCanceledOnTouchOutside(false);
            if (this.mActivity.isActive()) {
                this.mWaitingDialog.show();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void dismissWaitingDialog() {
        ProgressDialog progressDialog = this.mWaitingDialog;
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                this.mWaitingDialog.dismiss();
            }
            this.mWaitingDialog = null;
        }
    }

    /* access modifiers changed from: protected */
    public void updateProgressWaitingDialog(String str) {
        ProgressDialog progressDialog = this.mWaitingDialog;
        if (progressDialog != null) {
            progressDialog.setMessage(str);
        }
    }

    /* access modifiers changed from: protected */
    public void alertMemoryNotEnough(String str, String str2) {
        Builder positiveButton = new Builder(this.mActivity).setPositiveButton(C4409R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        if (!StringUtil.isEmptyOrNull(str)) {
            positiveButton.setTitle((CharSequence) str);
        }
        if (!StringUtil.isEmptyOrNull(str2)) {
            positiveButton.setMessage(str2);
        }
        positiveButton.create().show();
    }

    public void enableShareLink(boolean z) {
        this.mIsShareLinkEnable = z;
    }
}

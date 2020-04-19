package p021us.zoom.androidlib.app;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMStorageUtil.ZMStorageItem;
import p021us.zoom.androidlib.app.ZMStorageUtil.ZMStorageListener;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;
import p021us.zoom.androidlib.util.ZMAsyncTask.Status;

/* renamed from: us.zoom.androidlib.app.ZMLocalFileListAdapter */
public class ZMLocalFileListAdapter extends ZMFileListBaseAdapter implements ZMStorageListener {
    /* access modifiers changed from: private */
    public String mCurrentDir = null;
    private FilenameFilter mFileFilter = new FilenameFilter() {
        public boolean accept(File file, String str) {
            File file2 = new File(file, str);
            if (!file2.exists() || !file2.canRead() || file2.isHidden()) {
                return false;
            }
            if (file2.isDirectory() || ZMLocalFileListAdapter.this.acceptFileType(str)) {
                return true;
            }
            return false;
        }
    };
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public ZMFileListListener mListener;
    /* access modifiers changed from: private */
    public ZMLocalFileListSession mLocalFileSession;
    private int mSDCardCount = 0;
    private Runnable mShowWaitingStartRunnable = new Runnable() {
        public void run() {
            if (ZMLocalFileListAdapter.this.mListener != null) {
                ZMLocalFileListAdapter.this.mListener.onWaitingStart(null);
            }
        }
    };
    private List<ZMStorageItem> mStorages = new ArrayList();
    private ZMAsyncTask<String, Void, ArrayList<ZMFileListEntry>> mTaskOpenDir;
    private int mUsbCount = 0;

    /* renamed from: us.zoom.androidlib.app.ZMLocalFileListAdapter$Holder */
    private class Holder {
        TextView mDisplayName;
        ImageView mIcon;

        private Holder() {
        }
    }

    public void init(ZMActivity zMActivity, ZMFileListListener zMFileListListener) {
        super.init(zMActivity, zMFileListListener);
        this.mListener = zMFileListListener;
        this.mLocalFileSession = new ZMLocalFileListSession();
        this.mLocalFileSession.setFileNameFilter(this.mFileFilter);
    }

    private void asyncLoadStorages() {
        ZMStorageUtil.asyncGetAllStorages(this, 8000);
        ZMFileListListener zMFileListListener = this.mListener;
        if (zMFileListListener != null) {
            zMFileListListener.onStarting();
            this.mHandler.postDelayed(this.mShowWaitingStartRunnable, 50);
        }
    }

    public void onStart() {
        super.onStart();
        if (checkStoragePermission()) {
            asyncLoadStorages();
        } else if (OsUtil.isAtLeastM()) {
            requestStoragePermission();
        }
    }

    public boolean openDir(String str) {
        if (this.mStorages.size() <= 0) {
            Context context = getContext();
            if (context != null) {
                setLastErrorMessage(context.getString(C4409R.string.zm_alert_no_sdcard));
            }
            return false;
        }
        ZMAsyncTask<String, Void, ArrayList<ZMFileListEntry>> zMAsyncTask = this.mTaskOpenDir;
        if (zMAsyncTask != null && zMAsyncTask.getStatus() == Status.RUNNING) {
            this.mTaskOpenDir.cancel(true);
            this.mTaskOpenDir = null;
        }
        this.mTaskOpenDir = new ZMAsyncTask<String, Void, ArrayList<ZMFileListEntry>>() {
            /* access modifiers changed from: protected */
            @Nullable
            public ArrayList<ZMFileListEntry> doInBackground(String... strArr) {
                if (isCancelled()) {
                    return null;
                }
                String str = strArr[0];
                if (StringUtil.isEmptyOrNull(str)) {
                    return null;
                }
                ArrayList<ZMFileListEntry> arrayList = new ArrayList<>();
                boolean access$100 = ZMLocalFileListAdapter.this.getFileListInfo(str, arrayList);
                if (isCancelled() || !access$100) {
                    return null;
                }
                ZMLocalFileListAdapter.this.mLocalFileSession.setDir(str);
                return arrayList;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(ArrayList<ZMFileListEntry> arrayList) {
                if (!isCancelled() && arrayList != null) {
                    ZMLocalFileListAdapter.this.mFileList.clear();
                    ZMLocalFileListAdapter.this.mFileList.addAll(arrayList);
                    ZMLocalFileListAdapter zMLocalFileListAdapter = ZMLocalFileListAdapter.this;
                    zMLocalFileListAdapter.mCurrentDir = zMLocalFileListAdapter.mLocalFileSession.getCurrentDirPath();
                    ZMLocalFileListAdapter.this.sortFileList();
                    ZMLocalFileListAdapter.this.notifyDataSetChanged();
                    if (ZMLocalFileListAdapter.this.mListener != null) {
                        ZMLocalFileListAdapter.this.mListener.onRefresh();
                    }
                }
            }
        };
        this.mTaskOpenDir.execute((Params[]) new String[]{str});
        setLastErrorMessage(null);
        return true;
    }

    public void onDestroy() {
        ZMAsyncTask<String, Void, ArrayList<ZMFileListEntry>> zMAsyncTask = this.mTaskOpenDir;
        if (zMAsyncTask != null && zMAsyncTask.getStatus() == Status.RUNNING) {
            this.mTaskOpenDir.cancel(true);
            this.mTaskOpenDir = null;
        }
        ZMStorageUtil.removeListener(this);
    }

    public boolean isRootDir() {
        return StringUtil.isEmptyOrNull(this.mCurrentDir);
    }

    private boolean checkStoragePermission() {
        ZMActivity activity = getActivity();
        boolean z = false;
        if (activity == null) {
            return false;
        }
        if (!OsUtil.isAtLeastM()) {
            return true;
        }
        if (activity.zm_checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) {
            z = true;
        }
        return z;
    }

    @RequiresApi(api = 16)
    private void requestStoragePermission() {
        ZMActivity activity = getActivity();
        if (activity != null) {
            activity.zm_requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 0);
        }
    }

    public void onStoragePermissionResult(int i) {
        if (i == 0) {
            asyncLoadStorages();
            return;
        }
        ZMActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private boolean isInternalStorageRoot() {
        if (StringUtil.isEmptyOrNull(this.mCurrentDir)) {
            return false;
        }
        for (ZMStorageItem zMStorageItem : this.mStorages) {
            if (zMStorageItem.mounted && zMStorageItem.type == 1 && this.mCurrentDir.equals(zMStorageItem.path)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSDCardStorageRoot() {
        if (StringUtil.isEmptyOrNull(this.mCurrentDir)) {
            return false;
        }
        for (ZMStorageItem zMStorageItem : this.mStorages) {
            if (zMStorageItem.mounted && zMStorageItem.type == 2 && this.mCurrentDir.equals(zMStorageItem.path)) {
                return true;
            }
        }
        return false;
    }

    private boolean isUSBStorageRoot() {
        if (StringUtil.isEmptyOrNull(this.mCurrentDir)) {
            return false;
        }
        for (ZMStorageItem zMStorageItem : this.mStorages) {
            if (zMStorageItem.mounted && zMStorageItem.type == 3 && this.mCurrentDir.equals(zMStorageItem.path)) {
                return true;
            }
        }
        return false;
    }

    private boolean isStorageRootDir() {
        if (!StringUtil.isEmptyOrNull(this.mCurrentDir) && !isSDCardStorageRoot() && !isUSBStorageRoot() && !isInternalStorageRoot()) {
            return false;
        }
        return true;
    }

    public String getCurrentDirName() {
        if (this.mCurrentDir == null) {
            return "";
        }
        if (isInternalStorageRoot()) {
            return this.mActivity.getString(C4409R.string.zm_lbl_internal_storage);
        }
        if (isSDCardStorageRoot()) {
            return getSDCardName(getStorageIndex(this.mStorages, this.mCurrentDir, 2));
        }
        if (isUSBStorageRoot()) {
            return getUsbStorageName(getStorageIndex(this.mStorages, this.mCurrentDir, 3));
        }
        File currentDir = this.mLocalFileSession.getCurrentDir();
        if (currentDir == null) {
            return "";
        }
        return currentDir.getName();
    }

    private String getSDCardName(int i) {
        int i2 = this.mSDCardCount;
        if (i2 <= 0) {
            return "";
        }
        if (i2 <= 1) {
            return this.mActivity.getResources().getString(C4409R.string.zm_lbl_sdcard, new Object[]{""});
        }
        return this.mActivity.getResources().getString(C4409R.string.zm_lbl_sdcard, new Object[]{String.valueOf(i)});
    }

    private String getUsbStorageName(int i) {
        int i2 = this.mUsbCount;
        if (i2 <= 0) {
            return "";
        }
        if (i2 == 1) {
            return this.mActivity.getResources().getString(C4409R.string.zm_lbl_usb_storage, new Object[]{""});
        }
        return this.mActivity.getResources().getString(C4409R.string.zm_lbl_usb_storage, new Object[]{String.valueOf(i)});
    }

    public String getCurrentDirPath() {
        String str = this.mCurrentDir;
        return str == null ? "" : str;
    }

    /* access modifiers changed from: protected */
    public void gotoParentDir() {
        if (isStorageRootDir()) {
            this.mCurrentDir = null;
            notifyDataSetChanged();
            return;
        }
        this.mLocalFileSession.upCurrentDirectory();
        openDir(this.mLocalFileSession.getCurrentDirPath());
    }

    /* access modifiers changed from: protected */
    public void openDir(ZMFileListEntry zMFileListEntry) {
        if (zMFileListEntry != null) {
            String path = zMFileListEntry.getPath();
            if (!StringUtil.isEmptyOrNull(path) && zMFileListEntry.isDir()) {
                openDir(path);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void openFile(ZMFileListEntry zMFileListEntry) {
        if (zMFileListEntry != null) {
            String path = zMFileListEntry.getPath();
            if (!StringUtil.isEmptyOrNull(path) && !zMFileListEntry.isDir()) {
                ZMFileListListener zMFileListListener = this.mListener;
                if (zMFileListListener != null) {
                    zMFileListListener.onSelectedFile(path, zMFileListEntry.getDisplayName());
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean getFileListInfo(String str, ArrayList<ZMFileListEntry> arrayList) {
        int i;
        ArrayList<File> arrayList2 = new ArrayList<>();
        ZMLocalFileListSession.getDirectoryFileList(str, this.mLocalFileSession.getFileNameFilter(), arrayList2);
        for (File file : arrayList2) {
            if (file.exists() && file.canRead()) {
                if (file.isFile() || file.isDirectory()) {
                    if (file.isDirectory()) {
                        try {
                            i = this.mLocalFileSession.getDirFilesCount(file.getPath());
                        } catch (Exception unused) {
                        }
                    } else {
                        i = 0;
                    }
                    ZMFileListEntry zMFileListEntry = new ZMFileListEntry();
                    zMFileListEntry.setBytes(file.length());
                    zMFileListEntry.setDate(file.lastModified());
                    if (file.isDirectory()) {
                        zMFileListEntry.setDir(true);
                        zMFileListEntry.setChildEntryCount(i);
                    } else {
                        zMFileListEntry.setDir(false);
                    }
                    zMFileListEntry.setDisplayName(file.getName());
                    zMFileListEntry.setPath(file.getPath());
                    arrayList.add(zMFileListEntry);
                }
            }
        }
        return true;
    }

    private View createView(ViewGroup viewGroup) {
        Holder holder = new Holder();
        View inflate = this.mInflater.inflate(C4409R.layout.zm_storage_list_item, viewGroup, false);
        holder.mDisplayName = (TextView) inflate.findViewById(C4409R.C4411id.txtStorageName);
        holder.mIcon = (ImageView) inflate.findViewById(C4409R.C4411id.storageIcon);
        inflate.setTag(holder);
        return inflate;
    }

    private int getStorageIndex(List<ZMStorageItem> list, int i, int i2) {
        if (list == null || i >= list.size()) {
            return 0;
        }
        int i3 = 0;
        for (int i4 = 0; i4 <= i; i4++) {
            if (((ZMStorageItem) list.get(i4)).type == i2) {
                i3++;
            }
        }
        return i3;
    }

    private int getStorageCount(List<ZMStorageItem> list, int i) {
        int i2 = 0;
        if (list == null || list.size() == 0) {
            return 0;
        }
        for (ZMStorageItem zMStorageItem : list) {
            if (zMStorageItem.type == i) {
                i2++;
            }
        }
        return i2;
    }

    private int getStorageIndex(List<ZMStorageItem> list, String str, int i) {
        if (list == null || list.size() == 0 || StringUtil.isEmptyOrNull(str)) {
            return 0;
        }
        int i2 = 0;
        for (ZMStorageItem zMStorageItem : list) {
            if (zMStorageItem.type == i) {
                i2++;
                if (str.equals(zMStorageItem.path)) {
                    return i2;
                }
            }
        }
        return 0;
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (!isRootDir()) {
            return super.getView(i, view, viewGroup);
        }
        if (i >= this.mStorages.size()) {
            return null;
        }
        ZMStorageItem zMStorageItem = (ZMStorageItem) this.mStorages.get(i);
        if (view == null || !(view.getTag() instanceof Holder)) {
            view = createView(viewGroup);
        }
        Holder holder = (Holder) view.getTag();
        switch (zMStorageItem.type) {
            case 1:
                holder.mDisplayName.setText(C4409R.string.zm_lbl_internal_storage);
                holder.mIcon.setImageResource(C4409R.C4410drawable.zm_ic_storage_internal);
                break;
            case 2:
                holder.mDisplayName.setText(getSDCardName(getStorageIndex(this.mStorages, i, 2)));
                holder.mIcon.setImageResource(C4409R.C4410drawable.zm_ic_storage_sdcard);
                break;
            case 3:
                holder.mDisplayName.setText(getUsbStorageName(getStorageIndex(this.mStorages, i, 3)));
                holder.mIcon.setImageResource(C4409R.C4410drawable.zm_ic_storage_external);
                break;
        }
        return view;
    }

    public int getCount() {
        if (!isRootDir()) {
            return super.getCount();
        }
        List<ZMStorageItem> list = this.mStorages;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Nullable
    public ZMFileListEntry getItem(int i) {
        if (!isRootDir()) {
            return super.getItem(i);
        }
        List<ZMStorageItem> list = this.mStorages;
        if (list == null || i >= list.size()) {
            return null;
        }
        ZMStorageItem zMStorageItem = (ZMStorageItem) this.mStorages.get(i);
        ZMFileListEntry zMFileListEntry = new ZMFileListEntry();
        zMFileListEntry.setPath(zMStorageItem.path);
        zMFileListEntry.setDir(true);
        return zMFileListEntry;
    }

    public void onRecieveStorageInfo(List<ZMStorageItem> list) {
        ZMStorageUtil.removeListener(this);
        this.mStorages.clear();
        if (list != null && list.size() > 0) {
            for (ZMStorageItem zMStorageItem : list) {
                if (zMStorageItem.mounted) {
                    this.mStorages.add(zMStorageItem);
                }
            }
        }
        this.mSDCardCount = getStorageCount(this.mStorages, 2);
        this.mUsbCount = getStorageCount(this.mStorages, 3);
        if (this.mListener != null) {
            this.mHandler.removeCallbacks(this.mShowWaitingStartRunnable);
            this.mListener.onWaitingEnd();
            if (this.mStorages.size() > 0) {
                setLastErrorMessage(null);
                this.mListener.onStarted(true, null);
            } else {
                Context context = getContext();
                if (context != null) {
                    setLastErrorMessage(context.getString(C4409R.string.zm_alert_no_sdcard));
                    this.mListener.onStarted(false, context.getString(C4409R.string.zm_alert_no_sdcard));
                } else {
                    this.mListener.onStarted(false, null);
                }
            }
        }
        notifyDataSetChanged();
    }
}

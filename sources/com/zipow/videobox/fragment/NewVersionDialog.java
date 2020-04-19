package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.MinVersionForceUpdateActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.upgrade.UpgradeMgr;
import com.zipow.videobox.upgrade.UpgradeMgr.UpgradeMgrListener;
import com.zipow.videobox.util.UpgradeUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class NewVersionDialog extends ZMDialogFragment implements UpgradeMgrListener {
    private static final String ARG_NOTE = "note";
    private static final String ARG_VERSION = "version";
    public static final int REQUEST_PERMISSION_BY_UPDATE = 106;
    private static NewVersionDialog dialogInstance = null;
    /* access modifiers changed from: private */
    public static boolean mNeedFinishMinVerForceUpdateActivity = true;
    @Nullable
    private static NewVersionDialog s_lastInstance;
    @NonNull
    private OnClickListener deleteDownloadClick = new OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int i) {
            FragmentActivity activity = NewVersionDialog.this.getActivity();
            if (activity != null) {
                UpgradeMgr.getInstance(activity).cancel(activity);
            }
        }
    };
    @NonNull
    private OnClickListener hidenDownloadClick = new OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int i) {
        }
    };
    @Nullable
    private View mContentView = null;
    @NonNull
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public ProgressBar mProgress;
    /* access modifiers changed from: private */
    public RequestPermissionListener mRequestPermissionListener;
    @Nullable
    private RetainedFragment mRetainedFragment;
    @NonNull
    private OnClickListener redownloadClick = new OnClickListener() {
        public void onClick(@NonNull DialogInterface dialogInterface, int i) {
            ZMActivity zMActivity = (ZMActivity) NewVersionDialog.this.getActivity();
            if (zMActivity != null) {
                UpgradeMgr.getInstance(zMActivity).cancel(zMActivity);
                if (!NetworkUtil.hasDataNetwork(zMActivity)) {
                    NewVersionDialog.this.showConnectionError();
                    return;
                }
                UpgradeUtil.upgradeByUrl(zMActivity);
                dialogInterface.dismiss();
            }
        }
    };

    public interface RequestPermissionListener {
        void requestPermission();
    }

    public static class RetainedFragment extends ZMFragment {
        private RequestPermissionListener mRequestPermissionListener;

        public RetainedFragment() {
            setRetainInstance(true);
        }

        public void saveRequestPermissionListener(RequestPermissionListener requestPermissionListener) {
            this.mRequestPermissionListener = requestPermissionListener;
        }

        public RequestPermissionListener restoreRequestPermissionListener() {
            return this.mRequestPermissionListener;
        }
    }

    public static NewVersionDialog getInstance() {
        return dialogInstance;
    }

    @NonNull
    public static NewVersionDialog newInstance(String str, String str2, RequestPermissionListener requestPermissionListener) {
        if (dialogInstance == null) {
            dialogInstance = new NewVersionDialog();
        }
        dialogInstance.setRequestPermissionListener(requestPermissionListener);
        Bundle bundle = new Bundle();
        bundle.putString("version", str);
        bundle.putString(ARG_NOTE, str2);
        dialogInstance.setArguments(bundle);
        return dialogInstance;
    }

    public static void showDownloading(@Nullable ZMActivity zMActivity, RequestPermissionListener requestPermissionListener) {
        if (zMActivity != null) {
            if (UpgradeMgr.getInstance(zMActivity).getStatus() == 2 || UpgradeMgr.getInstance(zMActivity).getStatus() == 3) {
                FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    NewVersionDialog instance = getInstance();
                    if (instance != null) {
                        mNeedFinishMinVerForceUpdateActivity = false;
                        instance.dismiss();
                    }
                    newInstance("", "", requestPermissionListener).show(supportFragmentManager, NewVersionDialog.class.getName());
                }
            }
        }
    }

    @Nullable
    public static NewVersionDialog getLastInstance() {
        return s_lastInstance;
    }

    private static void setLastInstance(NewVersionDialog newVersionDialog) {
        s_lastInstance = newVersionDialog;
    }

    public NewVersionDialog() {
        setCancelable(true);
        setLastInstance(this);
    }

    public void show(FragmentManager fragmentManager, String str) {
        mNeedFinishMinVerForceUpdateActivity = true;
        super.show(fragmentManager, str);
    }

    public void setArguments(String str, String str2) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putString("version", str);
            arguments.putString(ARG_NOTE, str2);
        }
        View view = this.mContentView;
        if (view != null) {
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtNote);
            if (textView != null) {
                textView.setText(str2);
            }
            this.mContentView.setVisibility(StringUtil.isEmptyOrNull(str2) ? 8 : 0);
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        initRetainedFragment();
    }

    public void onStart() {
        super.onStart();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
        setLastInstance(null);
        dialogInstance = null;
        Context activity = getActivity();
        if (activity == null) {
            activity = VideoBoxApplication.getInstance();
        }
        UpgradeMgr.getInstance(activity).removeUpgradeMgrListener(this);
        FragmentActivity activity2 = getActivity();
        if ((activity2 instanceof MinVersionForceUpdateActivity) && mNeedFinishMinVerForceUpdateActivity) {
            activity2.finish();
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        ZMAlertDialog zMAlertDialog;
        Builder builder;
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return createEmptyDialog();
        }
        if (UpgradeMgr.getInstance(activity).getStatus() == 1) {
            View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_new_version, null, false);
            TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtNote);
            String str = "";
            Bundle arguments = getArguments();
            if (arguments != null) {
                str = arguments.getString(ARG_NOTE);
            }
            if (str == null) {
                str = "";
            }
            textView.setText(str);
            Builder positiveButton = new Builder(activity).setTitle(C4558R.string.zm_title_new_version_ready).setView(inflate).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_btn_update, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ZMActivity zMActivity = (ZMActivity) NewVersionDialog.this.getActivity();
                    if (zMActivity != null) {
                        if (!NetworkUtil.hasDataNetwork(zMActivity)) {
                            NewVersionDialog.this.showConnectionError();
                            return;
                        }
                        if (NewVersionDialog.this.checkStoragePermission()) {
                            UpgradeUtil.upgrade(zMActivity);
                        } else if (NewVersionDialog.this.mRequestPermissionListener != null) {
                            NewVersionDialog.this.mRequestPermissionListener.requestPermission();
                        }
                    }
                }
            });
            if (StringUtil.isEmptyOrNull(str)) {
                inflate.setVisibility(8);
            }
            this.mContentView = inflate;
            zMAlertDialog = positiveButton.create();
        } else {
            View inflate2 = LayoutInflater.from(activity).inflate(C4558R.layout.zm_version_download, null, false);
            this.mProgress = (ProgressBar) inflate2.findViewById(C4558R.C4560id.down_pre);
            long downloadSize = (long) UpgradeMgr.getInstance(activity).getDownloadSize();
            long fileSize = (long) UpgradeMgr.getInstance(activity).getFileSize();
            int i = C4558R.string.zm_downloading;
            if (UpgradeMgr.getInstance(activity).getStatus() != 2 || fileSize <= 0) {
                this.mProgress.setProgress(0);
            } else {
                this.mProgress.setProgress((int) ((downloadSize * 100) / fileSize));
            }
            if (UpgradeMgr.getInstance(getActivity()).getStatus() == 3) {
                i = C4558R.string.zm_download_failed_82691;
                builder = new Builder(getActivity()).setTitle(i).setNegativeButton(C4558R.string.zm_btn_cancel, this.deleteDownloadClick);
            } else {
                this.mProgress.setMax(100);
                builder = new Builder(getActivity()).setTitle(i).setView(inflate2).setNegativeButton(C4558R.string.zm_btn_cancel, this.deleteDownloadClick);
            }
            if (i == C4558R.string.zm_downloading) {
                builder.setPositiveButton(C4558R.string.zm_btn_download_in_background, this.hidenDownloadClick);
            } else if (i == C4558R.string.zm_download_failed_82691) {
                builder.setPositiveButton(C4558R.string.zm_btn_redownload, this.redownloadClick);
            }
            UpgradeMgr.getInstance(getActivity()).addUpgradeMgrListener(this);
            zMAlertDialog = builder.create();
        }
        if (zMAlertDialog != null) {
            zMAlertDialog.setCanceledOnTouchOutside(false);
        }
        return zMAlertDialog == null ? createEmptyDialog() : zMAlertDialog;
    }

    /* access modifiers changed from: private */
    public boolean checkStoragePermission() {
        return VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    /* access modifiers changed from: private */
    public void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    /* access modifiers changed from: private */
    public void showNewDialog() {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                NewVersionDialog.mNeedFinishMinVerForceUpdateActivity = false;
                ((NewVersionDialog) iUIElement).dismiss();
                new NewVersionDialog().show(NewVersionDialog.this.getFragmentManager(), NewVersionDialog.class.getName());
            }
        });
    }

    public void onUpgradeEvent(final int i, int i2, int i3) {
        this.mHandler.post(new Runnable() {
            public void run() {
                int i = i;
                if (i == 1) {
                    NewVersionDialog.this.showNewDialog();
                } else if (i == 4) {
                    if (NewVersionDialog.this.mProgress == null) {
                        NewVersionDialog.this.showNewDialog();
                        return;
                    }
                    FragmentActivity activity = NewVersionDialog.this.getActivity();
                    if (activity != null) {
                        long downloadSize = (long) UpgradeMgr.getInstance(activity).getDownloadSize();
                        long fileSize = (long) UpgradeMgr.getInstance(activity).getFileSize();
                        if (downloadSize > 0 && fileSize > 0) {
                            NewVersionDialog.this.mProgress.setProgress((int) ((downloadSize * 100) / fileSize));
                        }
                    }
                } else if (i == 5) {
                    NewVersionDialog.this.getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                        public void run(@NonNull IUIElement iUIElement) {
                            ((NewVersionDialog) iUIElement).dismiss();
                        }
                    });
                }
            }
        });
    }

    public void setRequestPermissionListener(RequestPermissionListener requestPermissionListener) {
        this.mRequestPermissionListener = requestPermissionListener;
    }

    private void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment == null) {
            this.mRetainedFragment = new RetainedFragment();
            this.mRetainedFragment.saveRequestPermissionListener(this.mRequestPermissionListener);
            ((ZMActivity) getContext()).getSupportFragmentManager().beginTransaction().add((Fragment) this.mRetainedFragment, RetainedFragment.class.getName()).commit();
            return;
        }
        RequestPermissionListener restoreRequestPermissionListener = retainedFragment.restoreRequestPermissionListener();
        if (restoreRequestPermissionListener != null) {
            this.mRequestPermissionListener = restoreRequestPermissionListener;
        }
    }

    @Nullable
    private RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        return (RetainedFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(RetainedFragment.class.getName());
    }
}

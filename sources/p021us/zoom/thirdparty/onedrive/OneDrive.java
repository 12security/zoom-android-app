package p021us.zoom.thirdparty.onedrive;

import android.app.Activity;
import android.net.Uri;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.extensions.OneDriveClient.Builder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.IDownloadFileListener;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncURLDownloadFile;

/* renamed from: us.zoom.thirdparty.onedrive.OneDrive */
public class OneDrive {
    /* access modifiers changed from: private */
    public Listener mAuthListener;
    /* access modifiers changed from: private */
    public IOneDriveClient mClient;
    private List<ZMAsyncURLDownloadFile> mDownloadOperations = new ArrayList();
    private List<OneDriveLoadFolderTask> mFolderLoaders = new ArrayList();
    /* access modifiers changed from: private */
    public boolean mIsCanceled;
    private OneDriveDefaultCallback<IOneDriveClient> mLoginCallBack = new OneDriveDefaultCallback<IOneDriveClient>() {
        public void success(IOneDriveClient iOneDriveClient) {
            OneDrive.this.mClient = iOneDriveClient;
            OneDrive.this.mStatus = OneDriveStatus.CONNECTED;
            if (OneDrive.this.mAuthListener != null) {
                if (!OneDrive.this.mIsCanceled) {
                    OneDrive.this.mAuthListener.onAuthSuccess();
                }
                OneDrive.this.mIsCanceled = false;
            }
        }

        public void failure(ClientException clientException) {
            OneDrive.this.mStatus = OneDriveStatus.NOT_CONNECTED;
            if (OneDrive.this.mAuthListener != null) {
                if (!OneDrive.this.mIsCanceled) {
                    OneDrive.this.mAuthListener.onAuthError(clientException);
                }
                OneDrive.this.mIsCanceled = false;
            }
            if (clientException != null && clientException.isError(OneDriveErrorCodes.AuthenticationFailure)) {
                OneDrive.this.logout();
            }
        }
    };
    private ICallback<Void> mLogoutCallBack = new OneDriveDefaultCallback<Void>() {
        public void success(Void voidR) {
            OneDrive.this.mAuthListener = null;
            if (OneDrive.this.mStatusListener != null) {
                OneDrive.this.mStatusListener.onLogout(OneDrive.this);
            }
        }

        public void failure(ClientException clientException) {
            OneDrive.this.mStatus = OneDriveStatus.NOT_CONNECTED;
        }
    };
    /* access modifiers changed from: private */
    public OneDriveStatus mStatus;
    /* access modifiers changed from: private */
    public StatusChangedListener mStatusListener;

    /* renamed from: us.zoom.thirdparty.onedrive.OneDrive$Listener */
    public interface Listener {
        void onAuthError(ClientException clientException);

        void onAuthStarting();

        void onAuthSuccess();
    }

    /* renamed from: us.zoom.thirdparty.onedrive.OneDrive$OneDriveStatus */
    private enum OneDriveStatus {
        UNKNOWN,
        INITIALED,
        CONNECTING,
        CONNECTED,
        NOT_CONNECTED
    }

    /* renamed from: us.zoom.thirdparty.onedrive.OneDrive$StatusChangedListener */
    public interface StatusChangedListener {
        void onLogout(OneDrive oneDrive);
    }

    public OneDrive(StatusChangedListener statusChangedListener, boolean z) {
        this.mClient = new Builder().fromConfig(z ? OneDriveManager.createBusinessConfig() : OneDriveManager.createConfig()).build();
        this.mStatus = OneDriveStatus.UNKNOWN;
        this.mStatusListener = statusChangedListener;
    }

    public IOneDriveClient getmClient() {
        return this.mClient;
    }

    public void setListener(Listener listener) {
        this.mAuthListener = listener;
    }

    public void onResume(Activity activity) {
        login(activity);
    }

    public void initial() {
        if (this.mStatus == OneDriveStatus.UNKNOWN || this.mStatus == OneDriveStatus.NOT_CONNECTED) {
            this.mClient.validate();
            this.mStatus = OneDriveStatus.INITIALED;
        }
    }

    public void login(Activity activity) {
        if (isAuthed()) {
            Listener listener = this.mAuthListener;
            if (listener != null) {
                listener.onAuthSuccess();
            }
        } else if (!isAuthing()) {
            this.mIsCanceled = false;
            this.mClient.login(activity, this.mLoginCallBack);
            Listener listener2 = this.mAuthListener;
            if (listener2 != null) {
                listener2.onAuthStarting();
            }
            this.mStatus = OneDriveStatus.CONNECTING;
        }
    }

    public void logout() {
        if (this.mStatus != OneDriveStatus.UNKNOWN) {
            this.mClient.getAuthenticator().logout(this.mLogoutCallBack);
            this.mStatus = OneDriveStatus.UNKNOWN;
        }
    }

    public boolean isAuthed() {
        return this.mStatus == OneDriveStatus.CONNECTED;
    }

    public boolean isAuthing() {
        return this.mStatus == OneDriveStatus.CONNECTING;
    }

    public boolean asyncLoadFolder(OneDriveEntry oneDriveEntry, IODFoldLoaderListener iODFoldLoaderListener) {
        if (oneDriveEntry == null || !oneDriveEntry.isDir()) {
            return false;
        }
        return asyncLoadFolderByItemId(oneDriveEntry.getObject().getItemId(), iODFoldLoaderListener);
    }

    public boolean asyncLoadFolderByItemId(String str, IODFoldLoaderListener iODFoldLoaderListener) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        OneDriveLoadFolderTask oneDriveLoadFolderTask = new OneDriveLoadFolderTask(this, str, iODFoldLoaderListener);
        this.mFolderLoaders.add(oneDriveLoadFolderTask);
        oneDriveLoadFolderTask.execute();
        return true;
    }

    public void removeDownloadFileTask(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile) {
        this.mDownloadOperations.remove(zMAsyncURLDownloadFile);
    }

    public void removeLoadFolderTask(OneDriveLoadFolderTask oneDriveLoadFolderTask) {
        this.mFolderLoaders.remove(oneDriveLoadFolderTask);
    }

    public boolean asyncDownloadFile(Item item, String str, IDownloadFileListener iDownloadFileListener) {
        if (item == null || item.isFolder() || !isAuthed() || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(item.getItemId())) {
            return false;
        }
        String absolutePath = new File(str, item.getShowName()).getAbsolutePath();
        if (!StringUtil.isEmptyOrNull(absolutePath)) {
            ZMAsyncURLDownloadFile zMAsyncURLDownloadFile = new ZMAsyncURLDownloadFile(Uri.parse(item.getDownloadUrl()), item.size.longValue(), absolutePath, iDownloadFileListener);
            this.mDownloadOperations.add(zMAsyncURLDownloadFile);
            zMAsyncURLDownloadFile.execute((Params[]) new Void[0]);
        }
        return true;
    }

    public void cancel() {
        for (ZMAsyncURLDownloadFile cancel : this.mDownloadOperations) {
            cancel.cancel(true);
        }
        this.mDownloadOperations.clear();
        for (OneDriveLoadFolderTask cancel2 : this.mFolderLoaders) {
            cancel2.cancel();
        }
        this.mFolderLoaders.clear();
    }

    public void cancelAuth() {
        this.mIsCanceled = true;
    }
}

package p021us.zoom.thirdparty.box;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.box.androidsdk.content.BoxApiFile;
import com.box.androidsdk.content.BoxApiFolder;
import com.box.androidsdk.content.auth.BoxAuthentication.AuthListener;
import com.box.androidsdk.content.auth.BoxAuthentication.BoxAuthenticationInfo;
import com.box.androidsdk.content.models.BoxSession;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.IZMAppUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.thirdparty.box.Box */
public class Box implements AuthListener {
    private static final int BOX_AUTHENTICATE_REQUEST = 9000;
    private BoxSession mBoxSession;
    /* access modifiers changed from: private */
    public BoxChangeListener mChangeListener;
    private BoxApiFile mFileApi;
    private List<BoxAsyncDownloadFile> mFileDownloadTasks = new ArrayList();
    private BoxApiFolder mFolderApi;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public BoxListener mListener;
    private List<BoxAsyncLoadFolder> mLoadFolderTasks = new ArrayList();

    /* renamed from: us.zoom.thirdparty.box.Box$BoxChangeListener */
    public interface BoxChangeListener {
        void onLogout(Box box);
    }

    /* renamed from: us.zoom.thirdparty.box.Box$BoxListener */
    public interface BoxListener {
        void onAuthError(String str);

        void onAuthStarting();

        void onAuthSuccess();

        void onAuthUnknowError();
    }

    private native void InitAuthImpl(boolean z);

    public void handleActivityResult(int i, int i2, Intent intent) {
    }

    public Box(BoxChangeListener boxChangeListener) {
        this.mChangeListener = boxChangeListener;
        this.mHandler = new Handler();
    }

    public void setListener(BoxListener boxListener) {
        this.mListener = boxListener;
    }

    public void login(ZMActivity zMActivity) {
        if (zMActivity != null) {
            startAuth(zMActivity);
        }
    }

    public void logout() {
        BoxSession boxSession = this.mBoxSession;
        if (boxSession != null) {
            boxSession.logout();
        }
        this.mBoxSession = null;
        this.mFolderApi = null;
        this.mFileApi = null;
    }

    public void stopAuth(ZMActivity zMActivity) {
        if (zMActivity != null) {
            zMActivity.finishActivity(9000);
        }
    }

    public boolean cancel() {
        for (BoxAsyncLoadFolder cancel : this.mLoadFolderTasks) {
            cancel.cancel(true);
        }
        this.mLoadFolderTasks.clear();
        for (BoxAsyncDownloadFile cancel2 : this.mFileDownloadTasks) {
            cancel2.cancel(true);
        }
        this.mFileDownloadTasks.clear();
        return true;
    }

    public boolean asyncDownloadFile(BoxFileObject boxFileObject, String str, IBoxFileDownloadListener iBoxFileDownloadListener) {
        if (!hasAuthenticated() || boxFileObject == null || !boxFileObject.isFileObject() || boxFileObject.isDir() || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        IZMAppUtil iZMAppUtil = BoxMgr.getInstance().getmIZMAppUtil();
        if (iZMAppUtil == null) {
            return false;
        }
        BoxAsyncDownloadFile boxAsyncDownloadFile = new BoxAsyncDownloadFile(this.mFileApi, boxFileObject, iZMAppUtil.getShareCachePathByExtension(str, boxFileObject.getName()), iBoxFileDownloadListener);
        this.mFileDownloadTasks.add(boxAsyncDownloadFile);
        boxAsyncDownloadFile.execute((Params[]) new Void[0]);
        return true;
    }

    public boolean asyncLoadDir(BoxFileObject boxFileObject, IBoxLoadFolderListener iBoxLoadFolderListener) {
        if (!hasAuthenticated() || boxFileObject == null || !boxFileObject.isFileObject() || !boxFileObject.isDir() || checkFolderTask(boxFileObject.getPath())) {
            return false;
        }
        BoxAsyncLoadFolder boxAsyncLoadFolder = new BoxAsyncLoadFolder(this.mFolderApi, boxFileObject, iBoxLoadFolderListener);
        this.mLoadFolderTasks.add(boxAsyncLoadFolder);
        boxAsyncLoadFolder.execute((Params[]) new Void[0]);
        return true;
    }

    public boolean asyncLoadDir(String str, IBoxLoadFolderListener iBoxLoadFolderListener) {
        if (!hasAuthenticated() || StringUtil.isEmptyOrNull(str) || checkFolderTask(str)) {
            return false;
        }
        BoxAsyncLoadFolder boxAsyncLoadFolder = new BoxAsyncLoadFolder(this.mFolderApi, str, iBoxLoadFolderListener);
        this.mLoadFolderTasks.add(boxAsyncLoadFolder);
        boxAsyncLoadFolder.execute((Params[]) new Void[0]);
        return true;
    }

    private boolean checkFolderTask(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        for (BoxAsyncLoadFolder boxAsyncLoadFolder : this.mLoadFolderTasks) {
            if (!boxAsyncLoadFolder.isCancelled() && str.equals(boxAsyncLoadFolder.getPath())) {
                return true;
            }
        }
        return false;
    }

    public void loadFolderTaskCompeleted(BoxAsyncLoadFolder boxAsyncLoadFolder) {
        if (boxAsyncLoadFolder != null) {
            this.mLoadFolderTasks.remove(boxAsyncLoadFolder);
        }
    }

    public void downloadFileTaskCompeleted(BoxAsyncDownloadFile boxAsyncDownloadFile) {
        if (boxAsyncDownloadFile != null) {
            this.mFileDownloadTasks.remove(boxAsyncDownloadFile);
        }
    }

    private void startAuth(ZMActivity zMActivity) {
        if (BoxMgr.getInstance().checkValid(zMActivity)) {
            BoxListener boxListener = this.mListener;
            if (boxListener != null) {
                boxListener.onAuthStarting();
            }
            InitAuthImpl(BoxMgr.getInstance().isZoomApp());
            this.mBoxSession = new BoxSession((Context) zMActivity);
            this.mBoxSession.setSessionAuthListener(this);
            this.mBoxSession.authenticate();
        }
    }

    public boolean hasAuthenticated() {
        boolean z = false;
        if (this.mBoxSession == null) {
            return false;
        }
        if (!(this.mFileApi == null || this.mFolderApi == null)) {
            z = true;
        }
        return z;
    }

    public void onRefreshed(BoxAuthenticationInfo boxAuthenticationInfo) {
        this.mFolderApi = new BoxApiFolder(this.mBoxSession);
        this.mFileApi = new BoxApiFile(this.mBoxSession);
    }

    public void onAuthCreated(BoxAuthenticationInfo boxAuthenticationInfo) {
        this.mFolderApi = new BoxApiFolder(this.mBoxSession);
        this.mFileApi = new BoxApiFile(this.mBoxSession);
        if (this.mListener != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    Box.this.mListener.onAuthSuccess();
                }
            });
        }
    }

    public void onAuthFailure(BoxAuthenticationInfo boxAuthenticationInfo, Exception exc) {
        this.mFileApi = null;
        this.mFolderApi = null;
        if (this.mListener != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    Box.this.mListener.onAuthError(null);
                }
            });
        }
        BoxSession boxSession = this.mBoxSession;
        if (boxSession != null) {
            boxSession.logout();
            this.mBoxSession = null;
        }
    }

    public void onLoggedOut(BoxAuthenticationInfo boxAuthenticationInfo, Exception exc) {
        this.mFileApi = null;
        this.mFolderApi = null;
        if (this.mChangeListener != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    Box.this.mChangeListener.onLogout(Box.this);
                }
            });
        }
    }
}

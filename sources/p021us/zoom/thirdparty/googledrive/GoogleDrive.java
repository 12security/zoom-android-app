package p021us.zoom.thirdparty.googledrive;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Builder;
import com.google.api.services.drive.model.FileList;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.IZMAppUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;
import p021us.zoom.thirdparty.googledrive.ZMGoogleCredential.CredentialListener;
import p021us.zoom.thirdparty.login.C4538R;
import p021us.zoom.thirdparty.login.google.GoogleAuthActivity;
import p021us.zoom.thirdparty.login.google.ZmBaseGoogleAuthActivity;

/* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive */
public class GoogleDrive {
    /* access modifiers changed from: private */
    public static int BUFFER_SIZE = 1024;
    public static final String ROOT_ID = "root";
    private static final String TAG = "GoogleDrive";
    private static final long TIME_OUT = 60000;
    /* access modifiers changed from: private */
    public ZMActivity mActivity;
    private Runnable mAuthCancelRunnable = new Runnable() {
        public void run() {
            GoogleDrive.this.mAuthCode = null;
            GoogleDrive.this.mStatus = DriveStatus.INITIAL;
            if (GoogleDrive.this.mListener != null) {
                GoogleDrive.this.mListener.onAuthCancel();
            }
        }
    };
    /* access modifiers changed from: private */
    public String mAuthCode;
    private String mAuthErrorMsg;
    private DriveChangeListener mChangeListener;
    /* access modifiers changed from: private */
    public Credential mCredential;
    private CredentialListener mCredentialListener = new CredentialListener() {
        public void onCompeleted(final Credential credential) {
            GoogleDrive.this.mHandler.post(new Runnable() {
                public void run() {
                    GoogleDrive.this.mCredential = credential;
                    if (GoogleDrive.this.mCredential == null) {
                        GoogleDrive.this.mStatus = DriveStatus.ERROR;
                        if (GoogleDrive.this.mListener != null) {
                            GoogleDrive.this.mListener.onAuthError(GoogleDrive.this.mActivity.getString(C4538R.string.zm_alert_auth_token_failed_msg));
                        }
                        return;
                    }
                    GoogleDrive.this.mDrive = GoogleDrive.this.getDriveService(GoogleDrive.this.mCredential);
                    GoogleDrive.this.mStatus = DriveStatus.CREDENTIALED;
                    if (GoogleDrive.this.mListener != null) {
                        GoogleDrive.this.mListener.onAuthSuccess();
                    }
                }
            });
        }

        public void onCancel() {
            GoogleDrive.this.mHandler.post(new Runnable() {
                public void run() {
                    GoogleDrive.this.mStatus = DriveStatus.AUTHCODED;
                    if (GoogleDrive.this.mListener != null) {
                        GoogleDrive.this.mListener.onAuthCancel();
                    }
                }
            });
        }

        public void onError(final Exception exc) {
            GoogleDrive.this.mHandler.post(new Runnable() {
                public void run() {
                    if (exc != null) {
                        GoogleDrive.this.setError(exc.getMessage());
                    } else {
                        GoogleDrive.this.setError(null);
                    }
                }
            });
        }

        public void onRefreshToken(Credential credential, Credential credential2) {
            if (GoogleDrive.this.mCredential == credential) {
                if (credential2 == null) {
                    GoogleDrive.this.setError(null);
                    return;
                }
                GoogleDrive googleDrive = GoogleDrive.this;
                googleDrive.mDrive = googleDrive.getDriveService(googleDrive.mCredential);
                GoogleDrive.this.mStatus = DriveStatus.CREDENTIALED;
            }
        }

        public void onRefreshTokenError(Credential credential, String str) {
            GoogleDrive.this.mbRefreshTokenFailed = true;
        }
    };
    private ZMGoogleCredential mCredentialServer;
    /* access modifiers changed from: private */
    public List<GDAsyncDownloadFile> mDownloadFileTasks = new ArrayList();
    /* access modifiers changed from: private */
    public Drive mDrive;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private boolean mIsZoomApp;
    /* access modifiers changed from: private */
    public GoogleDriveAuthListener mListener;
    /* access modifiers changed from: private */
    public List<GDAsyncLoadFolder> mLoadFolderTasks = new ArrayList();
    private String[] mScopes;
    /* access modifiers changed from: private */
    public DriveStatus mStatus;
    private Runnable mTimeOutRunnable = new Runnable() {
        public void run() {
            GoogleDrive.this.mStatus = DriveStatus.ERROR;
            if (GoogleDrive.this.mListener != null) {
                GoogleDrive.this.mListener.onAuthUnknowError();
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mbRefreshTokenFailed = false;

    /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$DriveChangeListener */
    public interface DriveChangeListener {
        void onLogout(GoogleDrive googleDrive);
    }

    /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$DriveFileListener */
    public interface DriveFileListener {
        void onDownloadCanceled(String str, String str2);

        void onDownloadCompleted(String str, String str2, String str3);

        void onDownloadFailed(String str, String str2, Exception exc);

        void onDownloadProgress(String str, String str2, long j, long j2);

        void onRefreshTokenFailed(String str, String str2, String str3);
    }

    /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$DriveFoldListener */
    public interface DriveFoldListener {
        void onLoadFolderCanceled(GDAsyncLoadFolder gDAsyncLoadFolder, String str);

        void onLoadFolderCompeleted(GDAsyncLoadFolder gDAsyncLoadFolder, String str, ArrayList<GoogleDriveObjectEntry> arrayList);

        void onLoadFolderFailed(GDAsyncLoadFolder gDAsyncLoadFolder, String str, Exception exc);

        void onRefreshTokenFailed(String str, String str2);
    }

    /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$DriveStatus */
    private enum DriveStatus {
        INITIAL,
        AUTHCODING,
        AUTHCODED,
        CREDENTIALING,
        CREDENTIALED,
        ERROR
    }

    /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile */
    public class GDAsyncDownloadFile extends ZMAsyncTask<Void, Long, Runnable> {
        private long mBytes;
        /* access modifiers changed from: private */
        public String mFileId;
        /* access modifiers changed from: private */
        public DriveFileListener mFileListener;
        /* access modifiers changed from: private */
        public String mFileName;
        /* access modifiers changed from: private */
        public String mOutPath;

        /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$OnCompeletedRunnable */
        private class OnCompeletedRunnable implements Runnable {
            private OnCompeletedRunnable() {
            }

            public void run() {
                if (GDAsyncDownloadFile.this.mFileListener != null) {
                    GDAsyncDownloadFile.this.mFileListener.onDownloadCompleted(GDAsyncDownloadFile.this.mFileId, GDAsyncDownloadFile.this.mFileName, GDAsyncDownloadFile.this.mOutPath);
                }
            }
        }

        /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$OnErrorRunnable */
        private class OnErrorRunnable implements Runnable {
            private Exception mException = null;

            public OnErrorRunnable() {
            }

            public void run() {
                if (GoogleDrive.this.mbRefreshTokenFailed) {
                    if (GDAsyncDownloadFile.this.mFileListener != null) {
                        GDAsyncDownloadFile.this.mFileListener.onRefreshTokenFailed(GDAsyncDownloadFile.this.mFileId, GDAsyncDownloadFile.this.mFileName, GDAsyncDownloadFile.this.mOutPath);
                    }
                    GoogleDrive.this.relogin();
                    return;
                }
                if (GDAsyncDownloadFile.this.mFileListener != null) {
                    GDAsyncDownloadFile.this.mFileListener.onDownloadFailed(GDAsyncDownloadFile.this.mFileId, GDAsyncDownloadFile.this.mFileName, this.mException);
                }
            }
        }

        /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$onCanceledRunnable */
        private class onCanceledRunnable implements Runnable {
            private onCanceledRunnable() {
            }

            public void run() {
                if (GDAsyncDownloadFile.this.mFileListener != null) {
                    GDAsyncDownloadFile.this.mFileListener.onDownloadCanceled(GDAsyncDownloadFile.this.mFileId, GDAsyncDownloadFile.this.mFileName);
                }
            }
        }

        public GDAsyncDownloadFile(String str, long j, String str2, String str3, DriveFileListener driveFileListener) {
            this.mFileId = str;
            this.mBytes = j;
            this.mOutPath = str3;
            this.mFileListener = driveFileListener;
            this.mFileName = str2;
        }

        /* JADX WARNING: type inference failed for: r0v1 */
        /* JADX WARNING: type inference failed for: r11v10, types: [java.io.Closeable] */
        /* JADX WARNING: type inference failed for: r0v2, types: [java.io.Closeable] */
        /* JADX WARNING: type inference failed for: r11v11 */
        /* JADX WARNING: type inference failed for: r9v0 */
        /* JADX WARNING: type inference failed for: r0v3 */
        /* JADX WARNING: type inference failed for: r1v2, types: [java.io.Closeable] */
        /* JADX WARNING: type inference failed for: r0v4, types: [java.io.Closeable] */
        /* JADX WARNING: type inference failed for: r9v1 */
        /* JADX WARNING: type inference failed for: r11v13 */
        /* JADX WARNING: type inference failed for: r1v4 */
        /* JADX WARNING: type inference failed for: r11v15 */
        /* JADX WARNING: type inference failed for: r11v20, types: [java.io.Closeable, java.io.InputStream] */
        /* JADX WARNING: type inference failed for: r1v7 */
        /* JADX WARNING: type inference failed for: r0v5 */
        /* JADX WARNING: type inference failed for: r1v8 */
        /* JADX WARNING: type inference failed for: r1v11, types: [java.io.OutputStream, java.io.Closeable, java.io.BufferedOutputStream] */
        /* JADX WARNING: type inference failed for: r9v2 */
        /* JADX WARNING: type inference failed for: r0v7 */
        /* JADX WARNING: type inference failed for: r0v8 */
        /* JADX WARNING: type inference failed for: r0v9 */
        /* JADX WARNING: type inference failed for: r11v25 */
        /* JADX WARNING: type inference failed for: r11v26 */
        /* JADX WARNING: type inference failed for: r11v27 */
        /* JADX WARNING: type inference failed for: r1v14 */
        /* JADX WARNING: type inference failed for: r1v15 */
        /* access modifiers changed from: protected */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r11v11
          assigns: []
          uses: []
          mth insns count: 95
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
        	at jadx.core.ProcessClass.process(ProcessClass.java:35)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 10 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Runnable doInBackground(java.lang.Void... r11) {
            /*
                r10 = this;
                us.zoom.thirdparty.googledrive.GoogleDrive r11 = p021us.zoom.thirdparty.googledrive.GoogleDrive.this
                com.google.api.services.drive.Drive r11 = r11.mDrive
                if (r11 == 0) goto L_0x00de
                java.lang.String r11 = r10.mFileId
                boolean r11 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r11)
                if (r11 != 0) goto L_0x00de
                java.lang.String r11 = r10.mOutPath
                boolean r11 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r11)
                if (r11 != 0) goto L_0x00de
                long r0 = r10.mBytes
                r2 = 0
                int r11 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r11 > 0) goto L_0x0022
                goto L_0x00de
            L_0x0022:
                boolean r11 = r10.isCancelled()
                r0 = 0
                if (r11 == 0) goto L_0x002f
                us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$onCanceledRunnable r11 = new us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$onCanceledRunnable
                r11.<init>()
                return r11
            L_0x002f:
                us.zoom.thirdparty.googledrive.GoogleDrive r11 = p021us.zoom.thirdparty.googledrive.GoogleDrive.this     // Catch:{ IOException -> 0x00c5, all -> 0x00c2 }
                com.google.api.services.drive.Drive r11 = r11.mDrive     // Catch:{ IOException -> 0x00c5, all -> 0x00c2 }
                com.google.api.services.drive.Drive$Files r11 = r11.files()     // Catch:{ IOException -> 0x00c5, all -> 0x00c2 }
                java.lang.String r1 = r10.mFileId     // Catch:{ IOException -> 0x00c5, all -> 0x00c2 }
                com.google.api.services.drive.Drive$Files$Get r11 = r11.get(r1)     // Catch:{ IOException -> 0x00c5, all -> 0x00c2 }
                java.io.InputStream r11 = r11.executeMediaAsInputStream()     // Catch:{ IOException -> 0x00c5, all -> 0x00c2 }
                boolean r1 = r10.isCancelled()     // Catch:{ IOException -> 0x00bf, all -> 0x00bd }
                if (r1 == 0) goto L_0x005a
                if (r11 == 0) goto L_0x004e
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r11)     // Catch:{ IOException -> 0x00bf, all -> 0x00bd }
            L_0x004e:
                us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$onCanceledRunnable r1 = new us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$onCanceledRunnable     // Catch:{ IOException -> 0x00bf, all -> 0x00bd }
                r1.<init>()     // Catch:{ IOException -> 0x00bf, all -> 0x00bd }
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r11)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r0)
                return r1
            L_0x005a:
                java.io.BufferedOutputStream r1 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x00bf, all -> 0x00bd }
                java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00bf, all -> 0x00bd }
                java.lang.String r5 = r10.mOutPath     // Catch:{ IOException -> 0x00bf, all -> 0x00bd }
                r4.<init>(r5)     // Catch:{ IOException -> 0x00bf, all -> 0x00bd }
                r1.<init>(r4)     // Catch:{ IOException -> 0x00bf, all -> 0x00bd }
                int r4 = p021us.zoom.thirdparty.googledrive.GoogleDrive.BUFFER_SIZE     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                byte[] r4 = new byte[r4]     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
            L_0x006c:
                int r5 = r11.read(r4)     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                r6 = -1
                if (r5 == r6) goto L_0x00a1
                boolean r6 = r10.isCancelled()     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                if (r6 == 0) goto L_0x0085
                us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$onCanceledRunnable r2 = new us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$onCanceledRunnable     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                r2.<init>()     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r11)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r1)
                return r2
            L_0x0085:
                r6 = 0
                r1.write(r4, r6, r5)     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                long r7 = (long) r5     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                long r2 = r2 + r7
                r5 = 2
                java.lang.Long[] r5 = new java.lang.Long[r5]     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                long r7 = r10.mBytes     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                r5[r6] = r7     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                r6 = 1
                java.lang.Long r7 = java.lang.Long.valueOf(r2)     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                r5[r6] = r7     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                r10.publishProgress(r5)     // Catch:{ IOException -> 0x00c0, all -> 0x00b9 }
                goto L_0x006c
            L_0x00a1:
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r11)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r1)
                boolean r11 = r10.isCancelled()
                if (r11 == 0) goto L_0x00b3
                us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$onCanceledRunnable r11 = new us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$onCanceledRunnable
                r11.<init>()
                return r11
            L_0x00b3:
                us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$OnCompeletedRunnable r11 = new us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$OnCompeletedRunnable
                r11.<init>()
                return r11
            L_0x00b9:
                r0 = move-exception
                r9 = r1
                r1 = r0
                goto L_0x00d6
            L_0x00bd:
                r1 = move-exception
                goto L_0x00d7
            L_0x00bf:
                r1 = r0
            L_0x00c0:
                r0 = r11
                goto L_0x00c6
            L_0x00c2:
                r1 = move-exception
                r11 = r0
                goto L_0x00d7
            L_0x00c5:
                r1 = r0
            L_0x00c6:
                us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$OnErrorRunnable r11 = new us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$OnErrorRunnable     // Catch:{ all -> 0x00d2 }
                r11.<init>()     // Catch:{ all -> 0x00d2 }
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r0)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r1)
                return r11
            L_0x00d2:
                r11 = move-exception
                r9 = r1
                r1 = r11
                r11 = r0
            L_0x00d6:
                r0 = r9
            L_0x00d7:
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r11)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r0)
                throw r1
            L_0x00de:
                us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$OnErrorRunnable r11 = new us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncDownloadFile$OnErrorRunnable
                r11.<init>()
                return r11
            */
            throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.thirdparty.googledrive.GoogleDrive.GDAsyncDownloadFile.doInBackground(java.lang.Void[]):java.lang.Runnable");
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Runnable runnable) {
            GoogleDrive.this.mDownloadFileTasks.remove(this);
            runnable.run();
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Long... lArr) {
            long longValue = lArr[0].longValue();
            long longValue2 = lArr[1].longValue();
            DriveFileListener driveFileListener = this.mFileListener;
            if (driveFileListener != null) {
                driveFileListener.onDownloadProgress(this.mFileId, this.mFileName, longValue, longValue2);
            }
            super.onProgressUpdate(lArr);
        }
    }

    /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncLoadFolder */
    public class GDAsyncLoadFolder extends ZMAsyncTask<Void, Void, Runnable> {
        private boolean isByPath = false;
        /* access modifiers changed from: private */
        public ArrayList<GoogleDriveObjectEntry> mFiles = new ArrayList<>();
        /* access modifiers changed from: private */
        public String mFolderId;
        /* access modifiers changed from: private */
        public DriveFoldListener mFolderListener;
        /* access modifiers changed from: private */
        public String mPath;

        /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncLoadFolder$OnCanceledRunnable */
        public class OnCanceledRunnable implements Runnable {
            public OnCanceledRunnable() {
            }

            public void run() {
                if (GDAsyncLoadFolder.this.mFolderListener != null) {
                    DriveFoldListener access$1900 = GDAsyncLoadFolder.this.mFolderListener;
                    GDAsyncLoadFolder gDAsyncLoadFolder = GDAsyncLoadFolder.this;
                    access$1900.onLoadFolderCanceled(gDAsyncLoadFolder, gDAsyncLoadFolder.mFolderId);
                }
            }
        }

        /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncLoadFolder$OnCompletedRunnable */
        public class OnCompletedRunnable implements Runnable {
            public OnCompletedRunnable() {
            }

            public void run() {
                if (GDAsyncLoadFolder.this.mFolderListener != null) {
                    DriveFoldListener access$1900 = GDAsyncLoadFolder.this.mFolderListener;
                    GDAsyncLoadFolder gDAsyncLoadFolder = GDAsyncLoadFolder.this;
                    access$1900.onLoadFolderCompeleted(gDAsyncLoadFolder, gDAsyncLoadFolder.mFolderId, GDAsyncLoadFolder.this.mFiles);
                }
            }
        }

        /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncLoadFolder$OnErrorRunnable */
        public class OnErrorRunnable implements Runnable {
            private Exception mError;

            public OnErrorRunnable(Exception exc) {
                this.mError = exc;
            }

            public void run() {
                if (GoogleDrive.this.mbRefreshTokenFailed) {
                    if (GDAsyncLoadFolder.this.mFolderListener != null) {
                        GDAsyncLoadFolder.this.mFolderListener.onRefreshTokenFailed(GDAsyncLoadFolder.this.mPath, GDAsyncLoadFolder.this.mFolderId);
                    }
                    GoogleDrive.this.relogin();
                    return;
                }
                if (GDAsyncLoadFolder.this.mFolderListener != null) {
                    DriveFoldListener access$1900 = GDAsyncLoadFolder.this.mFolderListener;
                    GDAsyncLoadFolder gDAsyncLoadFolder = GDAsyncLoadFolder.this;
                    access$1900.onLoadFolderFailed(gDAsyncLoadFolder, gDAsyncLoadFolder.mFolderId, this.mError);
                }
            }
        }

        /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$GDAsyncLoadFolder$OnRequestAuthorizationRunnable */
        public class OnRequestAuthorizationRunnable implements Runnable {
            public OnRequestAuthorizationRunnable() {
            }

            public void run() {
                GoogleDrive.this.relogin();
            }
        }

        public GDAsyncLoadFolder(String str, String str2, DriveFoldListener driveFoldListener) {
            this.mFolderId = str2;
            this.mFolderListener = driveFoldListener;
            this.mPath = str;
            if (StringUtil.isEmptyOrNull(this.mFolderId)) {
                this.isByPath = true;
            }
        }

        public String getFolderId() {
            return this.mFolderId;
        }

        public String getPath() {
            return this.mPath;
        }

        private FileList getFileList(String str) throws IOException {
            StringBuilder sb = new StringBuilder();
            sb.append("'");
            sb.append(str);
            sb.append("' in parents and trashed=false");
            return (FileList) GoogleDrive.this.mDrive.files().list().setFields("nextPageToken, files(modifiedTime, id, name, mimeType, size)").setQ(sb.toString()).execute();
        }

        /* access modifiers changed from: protected */
        public Runnable doInBackground(Void... voidArr) {
            FileList fileList;
            if (GoogleDrive.this.mDrive == null || (this.isByPath && StringUtil.isEmptyOrNull(this.mPath))) {
                return new OnErrorRunnable(new Exception(GoogleDrive.this.mActivity.getString(C4538R.string.zm_msg_load_dir_fail, new Object[]{""})));
            } else if (isCancelled()) {
                return new OnCanceledRunnable();
            } else {
                try {
                    if (!this.isByPath) {
                        fileList = getFileList(this.mFolderId);
                    } else {
                        if (!StringUtil.isEmptyOrNull(this.mPath)) {
                            if (!this.mPath.equals(File.separator)) {
                                String[] split = this.mPath.split(File.separator);
                                com.google.api.services.drive.model.File file = null;
                                FileList fileList2 = null;
                                String str = "root";
                                for (int i = 0; i < split.length; i++) {
                                    if (file != null) {
                                        str = file.getId();
                                    }
                                    fileList2 = getFileList(str);
                                    if (isCancelled()) {
                                        return new OnCanceledRunnable();
                                    }
                                    if (i < split.length - 1) {
                                        file = findFolderByName(fileList2, split[i + 1]);
                                        if (file == null) {
                                            return new OnErrorRunnable(new Exception(GoogleDrive.this.mActivity.getString(C4538R.string.zm_msg_load_dir_fail, new Object[]{this.mPath})));
                                        }
                                    }
                                }
                                this.mFolderId = str;
                                fileList = fileList2;
                            }
                        }
                        this.mFolderId = "root";
                        fileList = getFileList("root");
                    }
                    if (isCancelled()) {
                        return new OnCanceledRunnable();
                    }
                    if (fileList == null) {
                        return new OnErrorRunnable(new Exception(GoogleDrive.this.mActivity.getString(C4538R.string.zm_msg_load_dir_fail, new Object[]{this.mPath})));
                    }
                    for (com.google.api.services.drive.model.File file2 : fileList.getFiles()) {
                        if (file2 != null) {
                            this.mFiles.add(new GoogleDriveObjectEntry(this.mPath, file2));
                        }
                    }
                    return new OnCompletedRunnable();
                } catch (UserRecoverableAuthIOException unused) {
                    return new OnRequestAuthorizationRunnable();
                } catch (IOException e) {
                    return new OnErrorRunnable(e);
                }
            }
        }

        @Nullable
        private com.google.api.services.drive.model.File findFolderByName(FileList fileList, String str) {
            if (fileList == null || StringUtil.isEmptyOrNull(str)) {
                return null;
            }
            for (com.google.api.services.drive.model.File file : fileList.getFiles()) {
                if (GoogleDriveUtil.isFolder(file) && str.equals(file.getName())) {
                    return file;
                }
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Runnable runnable) {
            GoogleDrive.this.mLoadFolderTasks.remove(this);
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    /* renamed from: us.zoom.thirdparty.googledrive.GoogleDrive$GoogleDriveAuthListener */
    public interface GoogleDriveAuthListener {
        void onAuthCancel();

        void onAuthCredentialStarting();

        void onAuthError(String str);

        void onAuthStarting();

        void onAuthSuccess();

        void onAuthUnknowError();
    }

    private native void exchangeCodeImpl(ZMGoogleCredential zMGoogleCredential, String str, boolean z);

    private native void setAuthIntentInfoImpl(Intent intent, String str, String str2, boolean z);

    public GoogleDrive(String[] strArr, DriveChangeListener driveChangeListener, boolean z) {
        this.mChangeListener = driveChangeListener;
        this.mIsZoomApp = z;
        this.mScopes = strArr;
        this.mStatus = DriveStatus.INITIAL;
        this.mCredentialServer = new ZMGoogleCredential(this.mScopes, this.mCredentialListener, this.mIsZoomApp);
    }

    public void bind(ZMActivity zMActivity, Handler handler, GoogleDriveAuthListener googleDriveAuthListener) {
        cancelAllRunnable();
        this.mActivity = zMActivity;
        this.mHandler = handler;
        this.mListener = googleDriveAuthListener;
    }

    public boolean isAuthed() {
        return this.mStatus.equals(DriveStatus.CREDENTIALED);
    }

    public void login() {
        if (!isAuthed()) {
            if (this.mStatus.equals(DriveStatus.ERROR)) {
                resetAuth();
                cancelAllRunnable();
            }
            GoogleDriveAuthListener googleDriveAuthListener = this.mListener;
            if (googleDriveAuthListener != null) {
                googleDriveAuthListener.onAuthStarting();
            }
            if (this.mStatus.equals(DriveStatus.INITIAL)) {
                startAuthCode();
            } else if (this.mStatus.equals(DriveStatus.AUTHCODING)) {
                handleWebAuthResult();
            } else if (this.mStatus.equals(DriveStatus.AUTHCODED)) {
                startCredential();
            } else {
                postTimeout();
            }
        }
    }

    public void relogin() {
        resetAuth();
        cancelAllRunnable();
        login();
    }

    private void postTimeout() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mTimeOutRunnable);
            this.mHandler.postDelayed(this.mTimeOutRunnable, TIME_OUT);
        }
    }

    private void resetAuth() {
        this.mAuthCode = null;
        this.mAuthErrorMsg = null;
        this.mCredential = null;
        this.mDrive = null;
        this.mbRefreshTokenFailed = false;
        this.mStatus = DriveStatus.INITIAL;
    }

    private void startAuthCode() {
        if (this.mStatus.equals(DriveStatus.INITIAL)) {
            this.mStatus = DriveStatus.AUTHCODING;
            startWebAuth();
        }
    }

    public void startWebAuth() {
        ZMActivity zMActivity = this.mActivity;
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, GoogleAuthActivity.class);
            setAuthIntentInfoImpl(intent, ZmBaseGoogleAuthActivity.EXTRA_GOOGLE_CLIENT_ID, ZmBaseGoogleAuthActivity.EXTRA_GOOGLE_REDIRECT_URI, this.mIsZoomApp);
            try {
                this.mActivity.startActivity(intent);
            } catch (Exception unused) {
            }
        }
    }

    private static String encodeUrl(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }

    private void handleWebAuthResult() {
        if (this.mActivity != null) {
            if (StringUtil.isEmptyOrNull(this.mAuthCode)) {
                if (!StringUtil.isEmptyOrNull(this.mAuthErrorMsg)) {
                    setError(this.mAuthErrorMsg);
                } else {
                    setError(null);
                }
                return;
            }
            this.mStatus = DriveStatus.AUTHCODED;
            startCredential();
        }
    }

    private void startCredential() {
        if (this.mStatus.equals(DriveStatus.AUTHCODED)) {
            this.mStatus = DriveStatus.CREDENTIALING;
            GoogleDriveAuthListener googleDriveAuthListener = this.mListener;
            if (googleDriveAuthListener != null) {
                googleDriveAuthListener.onAuthCredentialStarting();
            }
            exchangeCodeImpl(this.mCredentialServer, this.mAuthCode, this.mIsZoomApp);
        }
    }

    public void logout() {
        DriveChangeListener driveChangeListener = this.mChangeListener;
        if (driveChangeListener != null) {
            driveChangeListener.onLogout(this);
        }
        resetAuth();
        cancelAllRunnable();
    }

    public void onDestroy() {
        cancelAuth();
        cancelAllAsyncTask();
    }

    public boolean cancelAuth() {
        if (this.mStatus.equals(DriveStatus.AUTHCODING)) {
            this.mStatus = DriveStatus.INITIAL;
        } else if (this.mStatus.equals(DriveStatus.CREDENTIALING)) {
            ZMGoogleCredential zMGoogleCredential = this.mCredentialServer;
            if (zMGoogleCredential != null) {
                zMGoogleCredential.cancel();
                this.mStatus = DriveStatus.AUTHCODED;
            }
        }
        return true;
    }

    private void cancelAllAsyncTask() {
        for (GDAsyncLoadFolder cancel : this.mLoadFolderTasks) {
            cancel.cancel(true);
        }
        this.mLoadFolderTasks.clear();
        for (GDAsyncDownloadFile cancel2 : this.mDownloadFileTasks) {
            cancel2.cancel(true);
        }
        this.mDownloadFileTasks.clear();
    }

    private String getExtendByMimeType(String str) {
        return AndroidAppUtil.getFileExtendNameFromMimType(str);
    }

    public boolean downloadFile(GoogleDriveObjectEntry googleDriveObjectEntry, String str, DriveFileListener driveFileListener) {
        String str2;
        if (googleDriveObjectEntry == null || googleDriveObjectEntry.isDir() || StringUtil.isEmptyOrNull(str) || !isAuthed()) {
            return false;
        }
        IZMAppUtil iZMAppUtil = GoogleDriveMgr.getInstance().getmIZMAppUtil();
        if (iZMAppUtil == null) {
            return false;
        }
        String shareCachePathByExtension = iZMAppUtil.getShareCachePathByExtension(str, googleDriveObjectEntry.getDisplayName());
        if (StringUtil.isEmptyOrNull(AndroidAppUtil.getFileExtendName(googleDriveObjectEntry.getDisplayName()))) {
            String extendByMimeType = getExtendByMimeType(googleDriveObjectEntry.getMimeType());
            StringBuilder sb = new StringBuilder();
            sb.append(shareCachePathByExtension);
            sb.append(extendByMimeType);
            str2 = sb.toString();
        } else {
            str2 = shareCachePathByExtension;
        }
        String id = googleDriveObjectEntry.getId();
        if (StringUtil.isEmptyOrNull(id)) {
            return false;
        }
        GDAsyncDownloadFile gDAsyncDownloadFile = new GDAsyncDownloadFile(id, googleDriveObjectEntry.getBytes(), googleDriveObjectEntry.getDisplayName(), str2, driveFileListener);
        this.mDownloadFileTasks.add(gDAsyncDownloadFile);
        gDAsyncDownloadFile.execute((Params[]) new Void[0]);
        return true;
    }

    public boolean loadFolder(String str, String str2, DriveFoldListener driveFoldListener) {
        if (!isAuthed() || (StringUtil.isEmptyOrNull(str2) && StringUtil.isEmptyOrNull(str))) {
            return false;
        }
        for (GDAsyncLoadFolder cancel : this.mLoadFolderTasks) {
            cancel.cancel(false);
        }
        this.mLoadFolderTasks.clear();
        GDAsyncLoadFolder gDAsyncLoadFolder = new GDAsyncLoadFolder(str, str2, driveFoldListener);
        this.mLoadFolderTasks.add(gDAsyncLoadFolder);
        gDAsyncLoadFolder.execute((Params[]) new Void[0]);
        return true;
    }

    /* access modifiers changed from: private */
    @Nullable
    public Drive getDriveService(Credential credential) {
        if (credential == null) {
            return null;
        }
        return new Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), credential).build();
    }

    public void setAuthCode(String str) {
        this.mAuthCode = str;
    }

    public void setAuthErrorMsg(String str) {
        this.mAuthErrorMsg = str;
    }

    /* access modifiers changed from: private */
    public void setError(String str) {
        this.mStatus = DriveStatus.ERROR;
        this.mAuthErrorMsg = null;
        this.mAuthCode = null;
        this.mCredential = null;
        this.mDrive = null;
        if (this.mListener == null) {
            return;
        }
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mListener.onAuthError(str);
        } else {
            this.mListener.onAuthError(null);
        }
    }

    private void cancelAllRunnable() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mAuthCancelRunnable);
            this.mHandler.removeCallbacks(this.mTimeOutRunnable);
        }
    }

    public static boolean canAuthGoogleViaBrowser(Context context) {
        PackageManager packageManager = context.getPackageManager();
        boolean z = false;
        if (packageManager == null) {
            return false;
        }
        List queryIntentActivities = packageManager.queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("https://www.example.com")), 65536);
        if (queryIntentActivities != null && queryIntentActivities.size() > 0) {
            z = true;
        }
        return z;
    }
}

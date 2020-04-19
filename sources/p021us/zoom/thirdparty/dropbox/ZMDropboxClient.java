package p021us.zoom.thirdparty.dropbox;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.p005v2.DbxClientV2;
import com.dropbox.core.p005v2.files.FileMetadata;
import com.dropbox.core.p005v2.files.ListFolderResult;
import com.dropbox.core.p005v2.files.Metadata;
import com.dropbox.core.p005v2.sharing.ListSharedLinksResult;
import com.dropbox.core.p005v2.sharing.SharedLinkMetadata;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import p021us.zoom.androidlib.app.IZMAppUtil;
import p021us.zoom.androidlib.app.ZMFileListActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;

/* renamed from: us.zoom.thirdparty.dropbox.ZMDropboxClient */
public class ZMDropboxClient {
    private static final String TAG = "ZMDropboxClient";
    private static String mAccessToken;
    /* access modifiers changed from: private */
    public static DbxClientV2 mDbxClient;
    /* access modifiers changed from: private */
    public ArrayList<AsyncFileDownload> mAsyncFileDownloads;
    /* access modifiers changed from: private */
    public ArrayList<AsyncShareFile> mAsyncShareFile;
    /* access modifiers changed from: private */
    public ArrayList<AsyncLoadDir> mDirAsyncLoader;
    /* access modifiers changed from: private */
    public DropboxListener mListener;

    /* renamed from: us.zoom.thirdparty.dropbox.ZMDropboxClient$AsyncFileDownload */
    private class AsyncFileDownload extends ZMAsyncTask<Void, Long, DropboxResult> {
        private boolean mCanceled = false;
        private FileMetadata mFile;
        private String mFileName;
        private String mOutPath;

        public AsyncFileDownload(FileMetadata fileMetadata, String str) {
            this.mOutPath = str;
            this.mFile = fileMetadata;
            this.mFileName = fileMetadata.getName();
        }

        /* access modifiers changed from: protected */
        public DropboxResult doInBackground(Void... voidArr) {
            FileOutputStream fileOutputStream;
            Throwable th;
            FileMetadata fileMetadata = this.mFile;
            if (fileMetadata == null) {
                return DropboxResult.INTERNAL_ERR;
            }
            String pathLower = fileMetadata.getPathLower();
            if (ZMDropboxClient.mDbxClient == null || StringUtil.isEmptyOrNull(this.mOutPath) || StringUtil.isEmptyOrNull(pathLower)) {
                return DropboxResult.INTERNAL_ERR;
            }
            if (this.mCanceled) {
                return DropboxResult.CANCELED;
            }
            try {
                fileOutputStream = new FileOutputStream(this.mOutPath);
                ZMDropboxClient.mDbxClient.files().download(pathLower, this.mFile.getRev()).download(fileOutputStream);
                fileOutputStream.close();
                if (this.mCanceled) {
                    return DropboxResult.CANCELED;
                }
                return DropboxResult.OK;
            } catch (Exception e) {
                return ZMDropboxClient.this.parseDropboxException(e);
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            this.mCanceled = true;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(DropboxResult dropboxResult) {
            if (ZMDropboxClient.this.mListener != null) {
                ZMDropboxClient.this.mListener.onLoadFile(dropboxResult, this.mFileName, this.mOutPath);
            }
            ZMDropboxClient.this.mAsyncFileDownloads.remove(this);
        }
    }

    /* renamed from: us.zoom.thirdparty.dropbox.ZMDropboxClient$AsyncLoadDir */
    private class AsyncLoadDir extends ZMAsyncTask<Void, Long, DropboxResult> {
        private boolean mCanceled = false;
        private String mDir;
        private ArrayList<Metadata> mFileEntries = new ArrayList<>();

        public AsyncLoadDir(String str) {
            this.mDir = ZMDropboxClient.this.checkDir(str);
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            this.mCanceled = true;
        }

        /* access modifiers changed from: protected */
        public DropboxResult doInBackground(Void... voidArr) {
            ListFolderResult listFolderResult;
            if (this.mCanceled) {
                return DropboxResult.CANCELED;
            }
            if (ZMDropboxClient.mDbxClient == null) {
                return DropboxResult.INTERNAL_ERR;
            }
            this.mFileEntries.clear();
            try {
                if (File.separator.equals(this.mDir)) {
                    listFolderResult = ZMDropboxClient.mDbxClient.files().listFolder("");
                } else {
                    listFolderResult = ZMDropboxClient.mDbxClient.files().listFolder(this.mDir);
                }
                while (true) {
                    if (!listFolderResult.getEntries().isEmpty()) {
                        this.mFileEntries.addAll(listFolderResult.getEntries());
                    }
                    if (!listFolderResult.getHasMore()) {
                        break;
                    }
                    listFolderResult = ZMDropboxClient.mDbxClient.files().listFolderContinue(listFolderResult.getCursor());
                }
                if (this.mCanceled) {
                    return DropboxResult.CANCELED;
                }
                return DropboxResult.OK;
            } catch (Exception e) {
                return ZMDropboxClient.this.parseDropboxException(e);
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(DropboxResult dropboxResult) {
            if (ZMDropboxClient.this.mListener != null) {
                ZMDropboxClient.this.mListener.onLoadDir(dropboxResult, this.mDir, this.mFileEntries);
            }
            ZMDropboxClient.this.mDirAsyncLoader.remove(this);
        }
    }

    /* renamed from: us.zoom.thirdparty.dropbox.ZMDropboxClient$AsyncShareFile */
    private class AsyncShareFile extends ZMAsyncTask<Void, Long, DropboxResult> {
        private boolean mCanceled = false;
        private FileMetadata mFileMetadata;
        private SharedLinkMetadata mSharedLinkMetadata;

        public AsyncShareFile(FileMetadata fileMetadata) {
            this.mFileMetadata = fileMetadata;
            this.mSharedLinkMetadata = null;
        }

        /* access modifiers changed from: protected */
        public DropboxResult doInBackground(Void... voidArr) {
            try {
                this.mSharedLinkMetadata = ZMDropbox.getInstance().getClient().getSharedLink(this.mFileMetadata.getId());
                if (this.mCanceled) {
                    return DropboxResult.CANCELED;
                }
                return DropboxResult.OK;
            } catch (DbxException e) {
                return ZMDropboxClient.this.parseDropboxException(e);
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(DropboxResult dropboxResult) {
            if (ZMDropboxClient.this.mListener != null) {
                ZMDropboxClient.this.mListener.onShareLink(dropboxResult, this.mSharedLinkMetadata, this.mFileMetadata);
            }
            ZMDropboxClient.this.mAsyncShareFile.remove(this);
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            this.mCanceled = true;
        }
    }

    /* renamed from: us.zoom.thirdparty.dropbox.ZMDropboxClient$DropboxListener */
    public interface DropboxListener {
        void onLoadDir(DropboxResult dropboxResult, String str, List<Metadata> list);

        void onLoadFile(DropboxResult dropboxResult, String str, String str2);

        void onShareLink(DropboxResult dropboxResult, SharedLinkMetadata sharedLinkMetadata, FileMetadata fileMetadata);
    }

    public ZMDropboxClient(String str) {
        this.mDirAsyncLoader = new ArrayList<>();
        this.mAsyncFileDownloads = new ArrayList<>();
        this.mAsyncShareFile = new ArrayList<>();
        this.mListener = null;
        this.mListener = null;
        mAccessToken = str;
        Builder defaultOkHttpClientBuilder = OkHttp3Requestor.defaultOkHttpClientBuilder();
        final String[] strArr = ZMFileListActivity.mProxyInfo;
        if (ZMFileListActivity.mProxyInfo != null && !TextUtils.isEmpty(strArr[0]) && !TextUtils.isEmpty(strArr[1])) {
            defaultOkHttpClientBuilder.proxyAuthenticator(new Authenticator() {
                public Request authenticate(Route route, Response response) throws IOException {
                    String[] strArr = strArr;
                    return response.request().newBuilder().header("Proxy-Authorization", Credentials.basic(strArr[0], strArr[1])).build();
                }
            });
        }
        mDbxClient = new DbxClientV2(DbxRequestConfig.newBuilder("DropboxV2Client").withHttpRequestor(new OkHttp3Requestor(defaultOkHttpClientBuilder.build())).build(), str);
    }

    public void setListener(DropboxListener dropboxListener) {
        this.mListener = dropboxListener;
    }

    public String getToken() {
        return mAccessToken;
    }

    public boolean loadDir(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        asyncLoadDir(str);
        return true;
    }

    public boolean cancel() {
        if (this.mDirAsyncLoader.size() > 0) {
            Iterator it = this.mDirAsyncLoader.iterator();
            while (it.hasNext()) {
                ((AsyncLoadDir) it.next()).cancel(true);
            }
            this.mDirAsyncLoader.clear();
        }
        if (this.mAsyncFileDownloads.size() > 0) {
            Iterator it2 = this.mAsyncFileDownloads.iterator();
            while (it2.hasNext()) {
                ((AsyncFileDownload) it2.next()).cancel(true);
            }
            this.mAsyncFileDownloads.clear();
        }
        if (this.mAsyncShareFile.size() > 0) {
            Iterator it3 = this.mAsyncShareFile.iterator();
            while (it3.hasNext()) {
                ((AsyncShareFile) it3.next()).cancel(true);
            }
            this.mAsyncShareFile.clear();
        }
        return true;
    }

    public boolean loadFile(DropboxFileListEntry dropboxFileListEntry, String str) {
        IZMAppUtil iZMAppUtil = ZMDropbox.getInstance().getmIZMAppUtil();
        if (iZMAppUtil == null) {
            return false;
        }
        String shareCachePathByExtension = iZMAppUtil.getShareCachePathByExtension(str, dropboxFileListEntry.getPath());
        if (StringUtil.isEmptyOrNull(shareCachePathByExtension)) {
            return false;
        }
        Metadata metadata = dropboxFileListEntry.getMetadata();
        if (!(metadata instanceof FileMetadata)) {
            return false;
        }
        AsyncFileDownload asyncFileDownload = new AsyncFileDownload((FileMetadata) metadata, shareCachePathByExtension);
        this.mAsyncFileDownloads.add(asyncFileDownload);
        asyncFileDownload.execute((Params[]) new Void[0]);
        return true;
    }

    public boolean shareFile(DropboxFileListEntry dropboxFileListEntry) {
        if (dropboxFileListEntry == null) {
            return false;
        }
        Metadata metadata = dropboxFileListEntry.getMetadata();
        if (!(metadata instanceof FileMetadata)) {
            return false;
        }
        AsyncShareFile asyncShareFile = new AsyncShareFile((FileMetadata) metadata);
        this.mAsyncShareFile.add(asyncShareFile);
        asyncShareFile.execute((Params[]) new Void[0]);
        return true;
    }

    @Nullable
    public SharedLinkMetadata createSharedLinkWithSettings(String str) throws DbxException {
        DbxClientV2 dbxClientV2 = mDbxClient;
        if (dbxClientV2 == null || dbxClientV2.sharing() == null) {
            return null;
        }
        try {
            return mDbxClient.sharing().createSharedLinkWithSettings(str);
        } catch (DbxException e) {
            throw e;
        }
    }

    @Nullable
    public SharedLinkMetadata getSharedLink(String str) throws DbxException {
        SharedLinkMetadata sharedLinkMetadata;
        DbxClientV2 dbxClientV2 = mDbxClient;
        SharedLinkMetadata sharedLinkMetadata2 = null;
        if (dbxClientV2 == null || dbxClientV2.sharing() == null) {
            return null;
        }
        try {
            ListSharedLinksResult listSharedLinks = mDbxClient.sharing().listSharedLinks();
            if (listSharedLinks.getLinks() != null && listSharedLinks.getLinks().size() > 0) {
                Iterator it = listSharedLinks.getLinks().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    sharedLinkMetadata = (SharedLinkMetadata) it.next();
                    if (!str.equals(sharedLinkMetadata.getId())) {
                        if (str.equals(sharedLinkMetadata.getPathLower())) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                sharedLinkMetadata2 = sharedLinkMetadata;
            }
            if (sharedLinkMetadata2 == null) {
                sharedLinkMetadata2 = createSharedLinkWithSettings(str);
            }
            return sharedLinkMetadata2;
        } catch (DbxException e) {
            throw e;
        }
    }

    private void asyncLoadDir(String str) {
        AsyncLoadDir asyncLoadDir = new AsyncLoadDir(str);
        this.mDirAsyncLoader.add(asyncLoadDir);
        asyncLoadDir.execute((Params[]) new Void[0]);
    }

    /* access modifiers changed from: protected */
    public String checkDir(String str) {
        return (str == null || str.equals("")) ? File.separator : str;
    }

    /* access modifiers changed from: private */
    public DropboxResult parseDropboxException(Exception exc) {
        DropboxResult dropboxResult = DropboxResult.INTERNAL_ERR;
        if (exc instanceof FileNotFoundException) {
            return DropboxResult.NOT_FOUND;
        }
        return exc instanceof IOException ? DropboxResult.IO_EXCEPTION : dropboxResult;
    }
}

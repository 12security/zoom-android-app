package p021us.zoom.thirdparty.box;

import com.box.androidsdk.content.BoxApiFolder;
import com.box.androidsdk.content.models.BoxFolder;
import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxListItems;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;

/* renamed from: us.zoom.thirdparty.box.BoxAsyncLoadFolder */
public class BoxAsyncLoadFolder extends ZMAsyncTask<Void, Void, Runnable> {
    /* access modifiers changed from: private */
    public boolean isByPath;
    private BoxApiFolder mApiFolder;
    /* access modifiers changed from: private */
    public Exception mException;
    /* access modifiers changed from: private */
    public BoxFileObject mFolder;
    /* access modifiers changed from: private */
    public String mFolderPath;
    /* access modifiers changed from: private */
    public List<BoxFileObject> mItems = new ArrayList();
    /* access modifiers changed from: private */
    public IBoxLoadFolderListener mListener;

    /* renamed from: us.zoom.thirdparty.box.BoxAsyncLoadFolder$onCancelRunnable */
    private class onCancelRunnable implements Runnable {
        private onCancelRunnable() {
        }

        public void run() {
            if (BoxAsyncLoadFolder.this.mListener != null) {
                IBoxLoadFolderListener access$400 = BoxAsyncLoadFolder.this.mListener;
                BoxAsyncLoadFolder boxAsyncLoadFolder = BoxAsyncLoadFolder.this;
                access$400.onCancel(boxAsyncLoadFolder, boxAsyncLoadFolder.mFolderPath);
            }
        }
    }

    /* renamed from: us.zoom.thirdparty.box.BoxAsyncLoadFolder$onCompletedRunnable */
    private class onCompletedRunnable implements Runnable {
        private onCompletedRunnable() {
        }

        public void run() {
            if (BoxAsyncLoadFolder.this.mListener != null) {
                IBoxLoadFolderListener access$400 = BoxAsyncLoadFolder.this.mListener;
                BoxAsyncLoadFolder boxAsyncLoadFolder = BoxAsyncLoadFolder.this;
                access$400.onCompeleted(boxAsyncLoadFolder, boxAsyncLoadFolder.isByPath, BoxAsyncLoadFolder.this.mFolderPath, BoxAsyncLoadFolder.this.mFolder, BoxAsyncLoadFolder.this.mItems);
            }
        }
    }

    /* renamed from: us.zoom.thirdparty.box.BoxAsyncLoadFolder$onErrorRunnable */
    private class onErrorRunnable implements Runnable {
        private onErrorRunnable() {
        }

        public void run() {
            if (BoxAsyncLoadFolder.this.mListener != null) {
                IBoxLoadFolderListener access$400 = BoxAsyncLoadFolder.this.mListener;
                BoxAsyncLoadFolder boxAsyncLoadFolder = BoxAsyncLoadFolder.this;
                access$400.onError(boxAsyncLoadFolder, boxAsyncLoadFolder.mFolderPath, BoxAsyncLoadFolder.this.mException);
            }
        }
    }

    /* renamed from: us.zoom.thirdparty.box.BoxAsyncLoadFolder$onInvalidParameterRunnable */
    private class onInvalidParameterRunnable implements Runnable {
        private onInvalidParameterRunnable() {
        }

        public void run() {
            IBoxLoadFolderListener access$400 = BoxAsyncLoadFolder.this.mListener;
            BoxAsyncLoadFolder boxAsyncLoadFolder = BoxAsyncLoadFolder.this;
            access$400.onError(boxAsyncLoadFolder, boxAsyncLoadFolder.mFolderPath, null);
        }
    }

    public BoxAsyncLoadFolder(BoxApiFolder boxApiFolder, BoxFileObject boxFileObject, IBoxLoadFolderListener iBoxLoadFolderListener) {
        this.mApiFolder = boxApiFolder;
        this.mFolder = boxFileObject;
        this.mListener = iBoxLoadFolderListener;
        this.isByPath = false;
    }

    public BoxAsyncLoadFolder(BoxApiFolder boxApiFolder, String str, IBoxLoadFolderListener iBoxLoadFolderListener) {
        this.mApiFolder = boxApiFolder;
        this.mFolderPath = str;
        this.mListener = iBoxLoadFolderListener;
        this.isByPath = true;
    }

    public String getPath() {
        if (this.isByPath) {
            return this.mFolderPath;
        }
        BoxFileObject boxFileObject = this.mFolder;
        if (boxFileObject != null) {
            return boxFileObject.getPath();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0011, code lost:
        if (r11.isDir() != false) goto L_0x0013;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Runnable doInBackground(java.lang.Void... r11) {
        /*
            r10 = this;
            com.box.androidsdk.content.BoxApiFolder r11 = r10.mApiFolder
            r0 = 0
            if (r11 == 0) goto L_0x0108
            boolean r11 = r10.isByPath
            if (r11 != 0) goto L_0x0013
            us.zoom.thirdparty.box.BoxFileObject r11 = r10.mFolder
            if (r11 == 0) goto L_0x0108
            boolean r11 = r11.isDir()
            if (r11 == 0) goto L_0x0108
        L_0x0013:
            boolean r11 = r10.isByPath
            if (r11 == 0) goto L_0x0021
            java.lang.String r11 = r10.mFolderPath
            boolean r11 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r11)
            if (r11 == 0) goto L_0x0021
            goto L_0x0108
        L_0x0021:
            boolean r11 = r10.isCancelled()
            if (r11 == 0) goto L_0x002d
            us.zoom.thirdparty.box.BoxAsyncLoadFolder$onCancelRunnable r11 = new us.zoom.thirdparty.box.BoxAsyncLoadFolder$onCancelRunnable
            r11.<init>()
            return r11
        L_0x002d:
            boolean r11 = r10.isByPath     // Catch:{ Exception -> 0x00ff }
            if (r11 != 0) goto L_0x0061
            us.zoom.thirdparty.box.BoxFileObject r11 = r10.mFolder     // Catch:{ Exception -> 0x00ff }
            java.lang.String r11 = r11.getPath()     // Catch:{ Exception -> 0x00ff }
            r10.mFolderPath = r11     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.BoxApiFolder r11 = r10.mApiFolder     // Catch:{ Exception -> 0x00ff }
            us.zoom.thirdparty.box.BoxFileObject r1 = r10.mFolder     // Catch:{ Exception -> 0x00ff }
            java.lang.String r1 = r1.getId()     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.requests.BoxRequestsFolder$GetFolderItems r11 = r11.getItemsRequest(r1)     // Catch:{ Exception -> 0x00ff }
            java.lang.String r1 = "size"
            java.lang.String r2 = "name"
            java.lang.String r3 = "id"
            java.lang.String r4 = "modified_at"
            java.lang.String r5 = "created_at"
            java.lang.String[] r1 = new java.lang.String[]{r1, r2, r3, r4, r5}     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.requests.BoxRequest r11 = r11.setFields(r1)     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.requests.BoxRequestsFolder$GetFolderItems r11 = (com.box.androidsdk.content.requests.BoxRequestsFolder.GetFolderItems) r11     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.models.BoxObject r11 = r11.send()     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.models.BoxListItems r11 = (com.box.androidsdk.content.models.BoxListItems) r11     // Catch:{ Exception -> 0x00ff }
            goto L_0x00e8
        L_0x0061:
            java.lang.String r11 = "0"
            java.lang.String r1 = r10.mFolderPath     // Catch:{ Exception -> 0x00ff }
            java.lang.String r2 = java.io.File.separator     // Catch:{ Exception -> 0x00ff }
            boolean r1 = r1.equals(r2)     // Catch:{ Exception -> 0x00ff }
            if (r1 == 0) goto L_0x008e
            com.box.androidsdk.content.BoxApiFolder r1 = r10.mApiFolder     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.requests.BoxRequestsFolder$GetFolderItems r11 = r1.getItemsRequest(r11)     // Catch:{ Exception -> 0x00ff }
            java.lang.String r1 = "size"
            java.lang.String r2 = "name"
            java.lang.String r3 = "id"
            java.lang.String r4 = "modified_at"
            java.lang.String r5 = "created_at"
            java.lang.String[] r1 = new java.lang.String[]{r1, r2, r3, r4, r5}     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.requests.BoxRequest r11 = r11.setFields(r1)     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.requests.BoxRequestsFolder$GetFolderItems r11 = (com.box.androidsdk.content.requests.BoxRequestsFolder.GetFolderItems) r11     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.models.BoxObject r11 = r11.send()     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.models.BoxListItems r11 = (com.box.androidsdk.content.models.BoxListItems) r11     // Catch:{ Exception -> 0x00ff }
            goto L_0x00e8
        L_0x008e:
            java.lang.String r1 = r10.mFolderPath     // Catch:{ Exception -> 0x00ff }
            java.lang.String r2 = java.io.File.separator     // Catch:{ Exception -> 0x00ff }
            java.lang.String[] r1 = r1.split(r2)     // Catch:{ Exception -> 0x00ff }
            r2 = 0
            r3 = r11
            r11 = r0
            r4 = r11
        L_0x009a:
            int r5 = r1.length     // Catch:{ Exception -> 0x00ff }
            if (r2 >= r5) goto L_0x00e7
            if (r11 == 0) goto L_0x00a3
            java.lang.String r3 = r11.getId()     // Catch:{ Exception -> 0x00ff }
        L_0x00a3:
            com.box.androidsdk.content.BoxApiFolder r4 = r10.mApiFolder     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.requests.BoxRequestsFolder$GetFolderItems r4 = r4.getItemsRequest(r3)     // Catch:{ Exception -> 0x00ff }
            java.lang.String r5 = "size"
            java.lang.String r6 = "name"
            java.lang.String r7 = "id"
            java.lang.String r8 = "modified_at"
            java.lang.String r9 = "created_at"
            java.lang.String[] r5 = new java.lang.String[]{r5, r6, r7, r8, r9}     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.requests.BoxRequest r4 = r4.setFields(r5)     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.requests.BoxRequestsFolder$GetFolderItems r4 = (com.box.androidsdk.content.requests.BoxRequestsFolder.GetFolderItems) r4     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.models.BoxObject r4 = r4.send()     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.models.BoxListItems r4 = (com.box.androidsdk.content.models.BoxListItems) r4     // Catch:{ Exception -> 0x00ff }
            int r5 = r1.length     // Catch:{ Exception -> 0x00ff }
            int r5 = r5 + -1
            if (r2 >= r5) goto L_0x00d8
            int r11 = r2 + 1
            r11 = r1[r11]     // Catch:{ Exception -> 0x00ff }
            com.box.androidsdk.content.models.BoxItem r11 = r10.findObject(r4, r11)     // Catch:{ Exception -> 0x00ff }
            if (r11 != 0) goto L_0x00d8
            us.zoom.thirdparty.box.BoxAsyncLoadFolder$onErrorRunnable r11 = new us.zoom.thirdparty.box.BoxAsyncLoadFolder$onErrorRunnable     // Catch:{ Exception -> 0x00ff }
            r11.<init>()     // Catch:{ Exception -> 0x00ff }
            return r11
        L_0x00d8:
            boolean r5 = r10.isCancelled()     // Catch:{ Exception -> 0x00ff }
            if (r5 == 0) goto L_0x00e4
            us.zoom.thirdparty.box.BoxAsyncLoadFolder$onCancelRunnable r11 = new us.zoom.thirdparty.box.BoxAsyncLoadFolder$onCancelRunnable     // Catch:{ Exception -> 0x00ff }
            r11.<init>()     // Catch:{ Exception -> 0x00ff }
            return r11
        L_0x00e4:
            int r2 = r2 + 1
            goto L_0x009a
        L_0x00e7:
            r11 = r4
        L_0x00e8:
            boolean r1 = r10.isCancelled()
            if (r1 == 0) goto L_0x00f4
            us.zoom.thirdparty.box.BoxAsyncLoadFolder$onCancelRunnable r11 = new us.zoom.thirdparty.box.BoxAsyncLoadFolder$onCancelRunnable
            r11.<init>()
            return r11
        L_0x00f4:
            java.lang.String r1 = r10.mFolderPath
            r10.parseResult(r11, r1)
            us.zoom.thirdparty.box.BoxAsyncLoadFolder$onCompletedRunnable r11 = new us.zoom.thirdparty.box.BoxAsyncLoadFolder$onCompletedRunnable
            r11.<init>()
            return r11
        L_0x00ff:
            r11 = move-exception
            r10.mException = r11
            us.zoom.thirdparty.box.BoxAsyncLoadFolder$onErrorRunnable r11 = new us.zoom.thirdparty.box.BoxAsyncLoadFolder$onErrorRunnable
            r11.<init>()
            return r11
        L_0x0108:
            us.zoom.thirdparty.box.BoxAsyncLoadFolder$onInvalidParameterRunnable r11 = new us.zoom.thirdparty.box.BoxAsyncLoadFolder$onInvalidParameterRunnable
            r11.<init>()
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.thirdparty.box.BoxAsyncLoadFolder.doInBackground(java.lang.Void[]):java.lang.Runnable");
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Runnable runnable) {
        runnable.run();
    }

    private void parseResult(BoxListItems boxListItems, String str) {
        if (boxListItems != null && !StringUtil.isEmptyOrNull(str)) {
            this.mItems.clear();
            Iterator it = boxListItems.iterator();
            while (it.hasNext()) {
                this.mItems.add(new BoxFileObject(str, (BoxItem) it.next()));
            }
        }
    }

    private BoxItem findObject(BoxListItems boxListItems, String str) {
        if (boxListItems == null || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        Iterator it = boxListItems.iterator();
        while (it.hasNext()) {
            BoxItem boxItem = (BoxItem) it.next();
            if ((boxItem instanceof BoxFolder) && str.equals(boxItem.getName())) {
                return boxItem;
            }
        }
        return null;
    }
}

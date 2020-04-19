package com.zipow.videobox.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.FileInfoChecker;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.ZMBitmapFactory;
import com.zipow.videobox.view.ZMGifView;
import com.zipow.videobox.view.p014mm.message.FontStyle;
import java.io.File;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;
import p021us.zoom.androidlib.util.ZMAsyncTask.Status;
import p021us.zoom.androidlib.widget.TouchImageView;
import p021us.zoom.videomeetings.C4558R;

public class MMImageSendConfirmFragment extends ZMDialogFragment implements OnClickListener {
    public static final String ARG_DELETE_ORIGIN_FILE = "deleteOriginFile";
    public static final String ARG_IMAGE_URI = "imageUri";
    public static final String ARG_TRANS_PATH = "transPath";
    private static final int MAX_IMAGE_FILE_SIZE = 1048576;
    private static final int MAX_IMAGE_SIZE_TO_SEND_IN_AREA = 921600;
    public static final String RESULT_IMAGE_PATH = "imagePath";
    private static final String TAG = "MMImageSendConfirmFragment";
    private Button mBtnBack;
    private View mBtnSend;
    /* access modifiers changed from: private */
    public boolean mDeleteOriginFile = true;
    @Nullable
    private String mImagePath;
    /* access modifiers changed from: private */
    public Uri mImageUri;
    private View mProgressBar;
    private boolean mSendConfirmed = false;
    /* access modifiers changed from: private */
    @Nullable
    public ZMAsyncTask<Void, Void, String> mTaskTransPhoto = null;
    /* access modifiers changed from: private */
    @Nullable
    public String mTransPath;
    private ZMGifView mViewGif;
    private TouchImageView mViewImage;
    private View mViewPlaceHolder;

    public static void showAsActivity(@NonNull ZMActivity zMActivity, String str, String str2, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_IMAGE_URI, str);
        bundle.putString(ARG_TRANS_PATH, str2);
        SimpleActivity.show(zMActivity, MMImageSendConfirmFragment.class.getName(), bundle, i);
    }

    public static void showAsActivity(Fragment fragment, String str, String str2, boolean z, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_IMAGE_URI, str);
        bundle.putString(ARG_TRANS_PATH, str2);
        bundle.putBoolean(ARG_DELETE_ORIGIN_FILE, z);
        SimpleActivity.show(fragment, MMImageSendConfirmFragment.class.getName(), bundle, i);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_image_send_confirm, viewGroup, false);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnSend = inflate.findViewById(C4558R.C4560id.btnSend);
        this.mViewImage = (TouchImageView) inflate.findViewById(C4558R.C4560id.viewImage);
        this.mProgressBar = inflate.findViewById(C4558R.C4560id.progressBar1);
        this.mViewPlaceHolder = inflate.findViewById(C4558R.C4560id.viewPlaceHolder);
        this.mViewGif = (ZMGifView) inflate.findViewById(C4558R.C4560id.viewGif);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSend.setOnClickListener(this);
        if (bundle != null) {
            this.mImagePath = bundle.getString("mImagePath");
        }
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mImagePath", this.mImagePath);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(ARG_IMAGE_URI);
            if (string != null) {
                this.mImageUri = Uri.parse(string);
            }
            this.mTransPath = arguments.getString(ARG_TRANS_PATH);
            this.mDeleteOriginFile = arguments.getBoolean(ARG_DELETE_ORIGIN_FILE);
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setRequestedOrientation(4);
        }
        if (needExternalStoragePermission()) {
            zm_requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
        }
    }

    private void loadImage() {
        if (!StringUtil.isEmptyOrNull(this.mImagePath)) {
            updateUIForLoadSuccess();
            return;
        }
        this.mTaskTransPhoto = new ZMAsyncTask<Void, Void, String>() {
            /* access modifiers changed from: protected */
            /* JADX WARNING: Code restructure failed: missing block: B:64:0x0118, code lost:
                r2 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
                r1.addSuppressed(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:73:0x012a, code lost:
                r9 = th;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:74:0x012b, code lost:
                r1 = null;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:82:?, code lost:
                r4.close();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:83:0x0139, code lost:
                r2 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:85:?, code lost:
                r1.addSuppressed(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:86:0x013e, code lost:
                r4.close();
             */
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Removed duplicated region for block: B:73:0x012a A[ExcHandler: all (th java.lang.Throwable), Splitter:B:44:0x00e3] */
            /* JADX WARNING: Removed duplicated region for block: B:81:0x0135 A[SYNTHETIC, Splitter:B:81:0x0135] */
            /* JADX WARNING: Removed duplicated region for block: B:86:0x013e A[Catch:{ Exception -> 0x0142 }] */
            @androidx.annotation.Nullable
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.lang.String doInBackground(java.lang.Void... r9) {
                /*
                    r8 = this;
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r9 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    android.net.Uri r9 = r9.mImageUri
                    r0 = 0
                    if (r9 != 0) goto L_0x000a
                    return r0
                L_0x000a:
                    com.zipow.videobox.VideoBoxApplication r9 = com.zipow.videobox.VideoBoxApplication.getInstance()
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r1 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    android.net.Uri r1 = r1.mImageUri
                    java.lang.String r9 = com.zipow.videobox.util.ImageUtil.getPathFromUri(r9, r1)
                    r1 = 0
                    if (r9 == 0) goto L_0x0025
                    java.lang.String r2 = java.io.File.separator
                    boolean r2 = r9.startsWith(r2)
                    if (r2 == 0) goto L_0x0025
                    r2 = 1
                    goto L_0x0026
                L_0x0025:
                    r2 = 0
                L_0x0026:
                    if (r9 == 0) goto L_0x003b
                    java.lang.String r3 = ".png"
                    boolean r3 = r9.endsWith(r3)
                    if (r3 != 0) goto L_0x0038
                    java.lang.String r3 = ".PNG"
                    boolean r3 = r9.endsWith(r3)
                    if (r3 == 0) goto L_0x003b
                L_0x0038:
                    java.lang.String r3 = "png"
                    goto L_0x003d
                L_0x003b:
                    java.lang.String r3 = "jpg"
                L_0x003d:
                    java.lang.String r4 = "pic"
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r5 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    java.lang.String r5 = r5.mTransPath
                    java.lang.String r3 = com.zipow.cmmlib.AppUtil.createTempFile(r4, r5, r3)
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r4 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    android.net.Uri r4 = r4.mImageUri
                    java.lang.String r4 = r4.getScheme()
                    java.lang.String r5 = "http"
                    boolean r5 = r5.equalsIgnoreCase(r4)
                    if (r5 != 0) goto L_0x0143
                    java.lang.String r5 = "https"
                    boolean r5 = r5.equalsIgnoreCase(r4)
                    if (r5 == 0) goto L_0x0065
                    goto L_0x0143
                L_0x0065:
                    java.lang.String r5 = "content"
                    boolean r4 = r5.equals(r4)
                    if (r4 == 0) goto L_0x00a0
                    com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getInstance()
                    android.content.ContentResolver r1 = r1.getContentResolver()
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r2 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    android.net.Uri r2 = r2.mImageUri
                    java.lang.String r1 = r1.getType(r2)
                    java.lang.String r1 = p021us.zoom.androidlib.util.AndroidAppUtil.getFileExtendNameFromMimType(r1)
                    java.lang.String r2 = "pic"
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r3 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    java.lang.String r3 = r3.mTransPath
                    java.lang.String r3 = com.zipow.cmmlib.AppUtil.createTempFile(r2, r3, r1)
                    com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getInstance()
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r2 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    android.net.Uri r2 = r2.mImageUri
                    boolean r1 = p021us.zoom.androidlib.util.FileUtils.copyFileFromUri(r1, r2, r3)
                    if (r1 != 0) goto L_0x0185
                    return r0
                L_0x00a0:
                    if (r2 == 0) goto L_0x00ca
                    java.lang.String r1 = com.zipow.videobox.util.ImageUtil.getImageMimeType(r9)
                    java.lang.String r2 = "image/gif"
                    boolean r1 = r2.equals(r1)
                    if (r1 == 0) goto L_0x00c3
                    java.lang.String r1 = "pic"
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r2 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    java.lang.String r2 = r2.mTransPath
                    java.lang.String r3 = "gif"
                    java.lang.String r3 = com.zipow.cmmlib.AppUtil.createTempFile(r1, r2, r3)
                    boolean r1 = p021us.zoom.androidlib.util.FileUtils.copyFile(r9, r3)
                    if (r1 == 0) goto L_0x00c3
                    return r3
                L_0x00c3:
                    boolean r1 = p021us.zoom.androidlib.util.FileUtils.copyFile(r9, r3)
                    if (r1 != 0) goto L_0x0185
                    return r0
                L_0x00ca:
                    java.io.File r2 = new java.io.File
                    r2.<init>(r3)
                    boolean r4 = r2.exists()
                    if (r4 != 0) goto L_0x00de
                    boolean r4 = r2.createNewFile()     // Catch:{ IOException -> 0x00da }
                    goto L_0x00db
                L_0x00da:
                    r4 = 0
                L_0x00db:
                    if (r4 != 0) goto L_0x00de
                    return r0
                L_0x00de:
                    java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0142 }
                    r4.<init>(r2)     // Catch:{ Exception -> 0x0142 }
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r2 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                    androidx.fragment.app.FragmentActivity r2 = r2.getActivity()     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                    android.content.ContentResolver r2 = r2.getContentResolver()     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r5 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                    android.net.Uri r5 = r5.mImageUri     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                    java.io.InputStream r2 = r2.openInputStream(r5)     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                    if (r2 == 0) goto L_0x0121
                    r5 = 8192(0x2000, float:1.14794E-41)
                    byte[] r5 = new byte[r5]     // Catch:{ Throwable -> 0x010a, all -> 0x0107 }
                L_0x00fd:
                    int r6 = r2.read(r5)     // Catch:{ Throwable -> 0x010a, all -> 0x0107 }
                    if (r6 <= 0) goto L_0x0121
                    r4.write(r5, r1, r6)     // Catch:{ Throwable -> 0x010a, all -> 0x0107 }
                    goto L_0x00fd
                L_0x0107:
                    r9 = move-exception
                    r1 = r0
                    goto L_0x0110
                L_0x010a:
                    r9 = move-exception
                    throw r9     // Catch:{ all -> 0x010c }
                L_0x010c:
                    r1 = move-exception
                    r7 = r1
                    r1 = r9
                    r9 = r7
                L_0x0110:
                    if (r2 == 0) goto L_0x0120
                    if (r1 == 0) goto L_0x011d
                    r2.close()     // Catch:{ Throwable -> 0x0118, all -> 0x012a }
                    goto L_0x0120
                L_0x0118:
                    r2 = move-exception
                    r1.addSuppressed(r2)     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                    goto L_0x0120
                L_0x011d:
                    r2.close()     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                L_0x0120:
                    throw r9     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                L_0x0121:
                    if (r2 == 0) goto L_0x0126
                    r2.close()     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                L_0x0126:
                    r4.close()     // Catch:{ Exception -> 0x0142 }
                    goto L_0x0185
                L_0x012a:
                    r9 = move-exception
                    r1 = r0
                    goto L_0x0133
                L_0x012d:
                    r9 = move-exception
                    throw r9     // Catch:{ all -> 0x012f }
                L_0x012f:
                    r1 = move-exception
                    r7 = r1
                    r1 = r9
                    r9 = r7
                L_0x0133:
                    if (r1 == 0) goto L_0x013e
                    r4.close()     // Catch:{ Throwable -> 0x0139 }
                    goto L_0x0141
                L_0x0139:
                    r2 = move-exception
                    r1.addSuppressed(r2)     // Catch:{ Exception -> 0x0142 }
                    goto L_0x0141
                L_0x013e:
                    r4.close()     // Catch:{ Exception -> 0x0142 }
                L_0x0141:
                    throw r9     // Catch:{ Exception -> 0x0142 }
                L_0x0142:
                    return r0
                L_0x0143:
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r1 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this     // Catch:{ MalformedURLException -> 0x01b6 }
                    androidx.fragment.app.FragmentActivity r1 = r1.getActivity()     // Catch:{ MalformedURLException -> 0x01b6 }
                    if (r1 == 0) goto L_0x0160
                    java.net.URL r2 = new java.net.URL     // Catch:{ MalformedURLException -> 0x01b6 }
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r3 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this     // Catch:{ MalformedURLException -> 0x01b6 }
                    android.net.Uri r3 = r3.mImageUri     // Catch:{ MalformedURLException -> 0x01b6 }
                    java.lang.String r3 = r3.toString()     // Catch:{ MalformedURLException -> 0x01b6 }
                    r2.<init>(r3)     // Catch:{ MalformedURLException -> 0x01b6 }
                    java.lang.String r1 = p021us.zoom.androidlib.util.FileUtils.downloadFile(r1, r2)     // Catch:{ MalformedURLException -> 0x01b6 }
                    r3 = r1
                    goto L_0x0161
                L_0x0160:
                    r3 = r0
                L_0x0161:
                    if (r3 != 0) goto L_0x0164
                    return r0
                L_0x0164:
                    java.lang.String r1 = com.zipow.videobox.util.ImageUtil.getImageMimeType(r3)
                    java.lang.String r2 = "image/gif"
                    boolean r1 = r1.equals(r2)
                    if (r1 == 0) goto L_0x0185
                    java.lang.String r1 = "pic"
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r2 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    java.lang.String r2 = r2.mTransPath
                    java.lang.String r4 = "gif"
                    java.lang.String r1 = com.zipow.cmmlib.AppUtil.createTempFile(r1, r2, r4)
                    boolean r2 = p021us.zoom.androidlib.util.FileUtils.copyFile(r3, r1)
                    if (r2 == 0) goto L_0x0185
                    return r1
                L_0x0185:
                    java.lang.String r1 = com.zipow.videobox.util.ImageUtil.getImageMimeType(r3)
                    java.lang.String r2 = "image/png"
                    boolean r1 = r1.equals(r2)
                    if (r1 == 0) goto L_0x019f
                    if (r9 == 0) goto L_0x019e
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r0 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    boolean r0 = r0.mDeleteOriginFile
                    if (r0 == 0) goto L_0x019e
                    p021us.zoom.androidlib.util.FileUtils.deleteFile(r9)
                L_0x019e:
                    return r3
                L_0x019f:
                    r1 = 1048576(0x100000, float:1.469368E-39)
                    boolean r1 = com.zipow.videobox.util.ImageUtil.compressImage(r3, r1)
                    if (r1 != 0) goto L_0x01a8
                    return r0
                L_0x01a8:
                    if (r9 == 0) goto L_0x01b5
                    com.zipow.videobox.fragment.MMImageSendConfirmFragment r0 = com.zipow.videobox.fragment.MMImageSendConfirmFragment.this
                    boolean r0 = r0.mDeleteOriginFile
                    if (r0 == 0) goto L_0x01b5
                    p021us.zoom.androidlib.util.FileUtils.deleteFile(r9)
                L_0x01b5:
                    return r3
                L_0x01b6:
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMImageSendConfirmFragment.C27191.doInBackground(java.lang.Void[]):java.lang.String");
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(@Nullable String str) {
                MMImageSendConfirmFragment.this.mTaskTransPhoto = null;
                if (MMImageSendConfirmFragment.this.isResumed() && !isCancelled()) {
                    if (str == null) {
                        MMImageSendConfirmFragment.this.onLoadFailed();
                    } else {
                        MMImageSendConfirmFragment.this.onLoadSuccess(str);
                    }
                }
            }
        };
        this.mViewPlaceHolder.setVisibility(8);
        this.mProgressBar.setVisibility(0);
        this.mBtnSend.setEnabled(false);
        this.mTaskTransPhoto.execute((Params[]) new Void[0]);
    }

    /* access modifiers changed from: private */
    public void onLoadFailed() {
        this.mViewPlaceHolder.setVisibility(0);
        this.mProgressBar.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void onLoadSuccess(String str) {
        this.mImagePath = str;
        updateUIForLoadSuccess();
    }

    private void updateUIForLoadSuccess() {
        this.mViewPlaceHolder.setVisibility(8);
        this.mProgressBar.setVisibility(8);
        String str = this.mImagePath;
        if (str != null) {
            if (new File(str).length() >= FontStyle.FontStyle_PNG) {
                SimpleMessageDialog.newInstance(C4558R.string.zm_msg_img_too_large, true).show(getFragmentManager(), SimpleMessageDialog.class.getName());
            }
            if (AndroidAppUtil.IMAGE_MIME_TYPE_GIF.equals(ImageUtil.getImageMimeType(this.mImagePath))) {
                this.mViewGif.setVisibility(0);
                this.mViewImage.setVisibility(8);
                this.mViewGif.setGifResourse(this.mImagePath);
                FileInfoChecker zoomFileInfoChecker = PTApp.getInstance().getZoomFileInfoChecker();
                if (zoomFileInfoChecker != null && !zoomFileInfoChecker.isLegalGif(this.mImagePath)) {
                    SimpleMessageDialog.newInstance(C4558R.string.zm_msg_illegal_image, true).show(getFragmentManager(), SimpleMessageDialog.class.getName());
                    return;
                }
            } else {
                this.mViewGif.setVisibility(8);
                this.mViewImage.setVisibility(0);
                if (!this.mViewImage.hasImage()) {
                    Bitmap decodeFile = ZMBitmapFactory.decodeFile(this.mImagePath);
                    if (decodeFile != null) {
                        this.mViewImage.setImageBitmap(decodeFile);
                    }
                }
            }
            this.mBtnSend.setEnabled(true);
        }
    }

    public void onPause() {
        super.onPause();
        ZMAsyncTask<Void, Void, String> zMAsyncTask = this.mTaskTransPhoto;
        if (zMAsyncTask != null && zMAsyncTask.getStatus() == Status.RUNNING) {
            this.mTaskTransPhoto.cancel(true);
            this.mTaskTransPhoto = null;
        }
    }

    public void onResume() {
        super.onResume();
        if (!needExternalStoragePermission()) {
            loadImage();
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C27202 r1 = new EventAction("MMImageSendConfirmForPermission") {
            public void run(@NonNull IUIElement iUIElement) {
                ((MMImageSendConfirmFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
            }
        };
        nonNullEventTaskManagerOrThrowException.pushLater(r1);
    }

    /* access modifiers changed from: private */
    public void handleRequestPermissionResult(int i, String[] strArr, int[] iArr) {
        loadImage();
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        if (!StringUtil.isEmptyOrNull(this.mImagePath)) {
            try {
                if (this.mDeleteOriginFile) {
                    FileUtils.deleteFile(this.mImagePath);
                }
            } catch (Exception unused) {
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (!getShowsDialog()) {
            FragmentActivity activity = getActivity();
            if (activity != null && activity.isFinishing() && !this.mSendConfirmed && !StringUtil.isEmptyOrNull(this.mImagePath)) {
                try {
                    if (this.mDeleteOriginFile) {
                        FileUtils.deleteFile(this.mImagePath);
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    public void onClick(View view) {
        if (view == this.mBtnBack) {
            onClickBtnBack();
        } else if (view == this.mBtnSend) {
            onClickBtnSend();
        }
    }

    private boolean needExternalStoragePermission() {
        String dataPath = AppUtil.getDataPath();
        String pathFromUri = ImageUtil.getPathFromUri(VideoBoxApplication.getInstance(), this.mImageUri);
        return (StringUtil.isEmptyOrNull(pathFromUri) || StringUtil.isEmptyOrNull(dataPath) || !(pathFromUri != null && pathFromUri.startsWith(File.separator)) || !pathFromUri.startsWith(dataPath)) && VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0;
    }

    private void onClickBtnBack() {
        dimiss();
    }

    private void dimiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(0);
    }

    private void onClickBtnSend() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        if (getShowsDialog()) {
            super.dismiss();
            return;
        }
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            this.mSendConfirmed = true;
            Intent intent = new Intent();
            intent.putExtra(RESULT_IMAGE_PATH, this.mImagePath);
            zMActivity.setResult(-1, intent);
            zMActivity.finish();
        }
    }
}

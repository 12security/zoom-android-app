package com.zipow.videobox;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.fragment.MMSelectSessionFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ImageUtil;
import java.io.File;
import p015io.reactivex.Observable;
import p015io.reactivex.ObservableEmitter;
import p015io.reactivex.ObservableOnSubscribe;
import p015io.reactivex.android.schedulers.AndroidSchedulers;
import p015io.reactivex.disposables.CompositeDisposable;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.schedulers.Schedulers;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.data.FileInfo;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class MMShareActivity extends ZMActivity {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        boolean z = false;
        if (VideoBoxApplication.getInstance() == null) {
            VideoBoxApplication.initialize(getApplicationContext(), false, 0);
        }
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null) {
            finish();
            return;
        }
        Uri uri = null;
        if ("android.intent.action.VIEW".equals(intent.getAction())) {
            intent.setAction("android.intent.action.SEND");
            uri = intent.getData();
        } else {
            Parcelable parcelableExtra = intent.getParcelableExtra("android.intent.extra.STREAM");
            if (parcelableExtra instanceof Uri) {
                uri = (Uri) parcelableExtra;
            }
        }
        if (uri != null) {
            if (!OsUtil.isAtLeastQ() || !Param.CONTENT.equals(uri.getScheme())) {
                String pathFromUri = ImageUtil.getPathFromUri(VideoBoxApplication.getInstance(), uri);
                if (pathFromUri != null && pathFromUri.startsWith("/")) {
                    z = true;
                }
                if (z) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("file://");
                    sb.append(pathFromUri);
                    uri = Uri.parse(sb.toString());
                }
                intent.putExtra("android.intent.extra.STREAM", uri);
            } else {
                String str = "";
                FileInfo dumpImageMetaData = FileUtils.dumpImageMetaData(VideoBoxApplication.getInstance(), uri);
                if (dumpImageMetaData != null) {
                    if (!checkFileSize(dumpImageMetaData.getSize())) {
                        str = dumpImageMetaData.getExt();
                    } else {
                        return;
                    }
                }
                if (StringUtil.isEmptyOrNull(str)) {
                    str = AndroidAppUtil.getFileExtendNameFromMimType(VideoBoxApplication.getInstance().getContentResolver().getType(uri));
                }
                if (!PTApp.getInstance().isFileTypeAllowSendInChat(str)) {
                    showFileFormatNotSupportDialog();
                    return;
                } else {
                    copyToPrivatePath(uri);
                    return;
                }
            }
        }
        if (!mainboard.isInitialized()) {
            finish();
            LauncherActivity.showLauncherActivityForActionSend(this, intent);
            return;
        }
        if (!PTApp.getInstance().isFileTransferDisabled()) {
            MMSelectSessionFragment.showSendTo(this, intent);
        }
        finish();
    }

    /* access modifiers changed from: private */
    public void onCopyContentSuccess(String str) {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null) {
            finish();
            return;
        }
        Intent intent = getIntent();
        if (intent != null) {
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(str)));
        }
        if (!mainboard.isInitialized()) {
            finish();
            LauncherActivity.showLauncherActivityForActionSend(this, intent);
            return;
        }
        if (!PTApp.getInstance().isFileTransferDisabled()) {
            MMSelectSessionFragment.showSendTo(this, intent);
        }
        finish();
    }

    private void copyToPrivatePath(@NonNull final Uri uri) {
        this.mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<String>() {
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                String str = Event.SHARE;
                String str2 = "";
                FileInfo dumpImageMetaData = FileUtils.dumpImageMetaData(VideoBoxApplication.getNonNullInstance(), uri);
                if (dumpImageMetaData != null) {
                    if (!StringUtil.isEmptyOrNull(dumpImageMetaData.getDisplayName())) {
                        str = dumpImageMetaData.getDisplayName();
                    }
                    str2 = dumpImageMetaData.getExt();
                }
                if (StringUtil.isEmptyOrNull(str2)) {
                    str2 = AndroidAppUtil.getFileExtendNameFromMimType(VideoBoxApplication.getNonNullInstance().getContentResolver().getType(uri));
                }
                String createTempFile = AppUtil.createTempFile(str, null, str2);
                File file = new File(createTempFile);
                if (file.exists()) {
                    file.delete();
                }
                if (FileUtils.copyFileFromUri(VideoBoxApplication.getNonNullInstance(), uri, createTempFile)) {
                    observableEmitter.onNext(createTempFile);
                } else {
                    observableEmitter.onNext("");
                }
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.m266io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer<? super T>) new Consumer<String>() {
            public void accept(String str) throws Exception {
                if (!StringUtil.isEmptyOrNull(str) && !ZMActivity.isActivityDestroyed(MMShareActivity.this)) {
                    MMShareActivity.this.onCopyContentSuccess(str);
                }
            }
        }));
    }

    private void destroyDisposable() {
        CompositeDisposable compositeDisposable = this.mCompositeDisposable;
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        destroyDisposable();
    }

    private boolean checkFileSize(long j) {
        if (j <= 536870912) {
            return false;
        }
        if (isActive()) {
            new Builder(this).setMessage(C4558R.string.zm_msg_file_too_large).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    MMShareActivity.this.finish();
                    MMShareActivity.this.overridePendingTransition(0, 0);
                }
            }).show();
        }
        return true;
    }

    private void showFileFormatNotSupportDialog() {
        if (isActive()) {
            new Builder(this).setTitle(C4558R.string.zm_msg_file_format_not_support_title_110716).setMessage(C4558R.string.zm_msg_file_format_not_support_msg_110716).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    MMShareActivity.this.finish();
                    MMShareActivity.this.overridePendingTransition(0, 0);
                }
            }).show();
        }
    }
}

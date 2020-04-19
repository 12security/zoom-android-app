package com.zipow.videobox.view.p014mm.sticker;

import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.dialog.ShareAlertDialog;
import com.zipow.videobox.fragment.ErrorMsgDialog;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack.SimpleZoomPrivateStickerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.view.p014mm.message.FontStyle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.sticker.MMPrivateStickerFragment */
public class MMPrivateStickerFragment extends ZMDialogFragment implements OnClickListener {
    private static final int REQUEST_CHOOSE_PICTURE = 100;
    private static final int REQUEST_PERMISSION_BY_WRITE_EXTERNAL_STORAGE = 10000;
    private View mBtnDelete;
    @NonNull
    private SimpleZoomPrivateStickerUIListener mLisener = new SimpleZoomPrivateStickerUIListener() {
        public void OnNewStickerUploaded(String str, int i, @Nullable String str2) {
            MMPrivateStickerFragment.this.OnNewStickerUploaded(str, i, str2);
        }

        public void OnMakePrivateSticker(int i, String str, @Nullable String str2) {
            MMPrivateStickerFragment.this.OnMakePrivateSticker(i, str, str2);
        }

        public void OnDiscardPrivateSticker(int i, String str) {
            MMPrivateStickerFragment.this.OnDiscardPrivateSticker(i, str);
        }

        public void OnPrivateStickersUpdated() {
            MMPrivateStickerFragment.this.OnPrivateStickersUpdated();
        }

        public void OnStickerDownloaded(String str, int i) {
            MMPrivateStickerFragment.this.OnStickerDownloaded(str, i);
        }
    };
    @NonNull
    private IZoomMessengerUIListener mMessengerListener = new SimpleZoomMessengerUIListener() {
        public void FT_UploadToMyList_OnProgress(String str, int i, int i2, int i3) {
            MMPrivateStickerFragment.this.FT_UploadToMyList_OnProgress(str, i, i2, i3);
        }

        public void Indicate_UploadToMyFiles_Sent(String str, String str2, int i) {
            MMPrivateStickerFragment.this.Indicate_UploadToMyFiles_Sent(str, str2, i);
        }
    };
    private MMPrivateStickerGridView mStickerGridView;
    private TextView mTitleView;

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        SimpleActivity.show(zMActivity, MMPrivateStickerFragment.class.getName(), new Bundle(), 0, true);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_set_private_stickers, viewGroup, false);
        this.mTitleView = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnDelete = inflate.findViewById(C4558R.C4560id.btnDelete);
        this.mStickerGridView = (MMPrivateStickerGridView) inflate.findViewById(C4558R.C4560id.gridViewStickers);
        this.mStickerGridView.setParentFragment(this);
        this.mBtnDelete.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        this.mStickerGridView.refreshData();
        ZoomMessengerUI.getInstance().addListener(this.mMessengerListener);
        PrivateStickerUICallBack.getInstance().addListener(this.mLisener);
    }

    public void onPause() {
        PrivateStickerUICallBack.getInstance().removeListener(this.mLisener);
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerListener);
        super.onPause();
    }

    public void onStickerSelected(@Nullable List<String> list) {
        if (CollectionsUtil.isListEmpty(list)) {
            this.mTitleView.setText(C4558R.string.zm_title_edit_emoji_no_selected);
            this.mBtnDelete.setVisibility(4);
            return;
        }
        int size = list.size();
        this.mTitleView.setText(getResources().getQuantityString(C4558R.plurals.zm_title_edit_emoji_selected, size, new Object[]{Integer.valueOf(size)}));
        this.mBtnDelete.setVisibility(0);
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            Uri uri = null;
            if (intent != null) {
                uri = intent.getData();
            }
            if (uri != null) {
                String pathFromUri = ImageUtil.getPathFromUri(getActivity(), uri);
                if (pathFromUri == null) {
                    alertImageInvalid();
                    return;
                }
                uploadSticker(pathFromUri);
            }
        }
    }

    private void uploadSticker(@NonNull String str) {
        if (new File(str).length() >= FontStyle.FontStyle_PNG) {
            SimpleMessageDialog.newInstance(C4558R.string.zm_msg_sticker_too_large, false).show(getFragmentManager(), SimpleMessageDialog.class.getName());
            return;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || !zoomMessenger.isConnectionGood()) {
            SimpleMessageDialog.newInstance(C4558R.string.zm_msg_xmpp_disconnect, false).show(getFragmentManager(), SimpleMessageDialog.class.getName());
            return;
        }
        MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
        if (zoomPrivateStickerMgr != null) {
            zoomPrivateStickerMgr.uploadAndMakePrivateSticker(str);
        }
    }

    private void alertImageInvalid() {
        ShareAlertDialog.showDialog(getFragmentManager(), getString(C4558R.string.zm_alert_invalid_image), true);
    }

    private boolean checkExternalSoragePermission() {
        return VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public void onAddStickerClick() {
        if (checkExternalSoragePermission()) {
            takePhoto();
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        zm_requestPermissions((String[]) arrayList.toArray(new String[arrayList.size()]), 10000);
    }

    private void takePhoto() {
        AndroidAppUtil.selectImageNoDefault((Fragment) this, C4558R.string.zm_select_a_image, 100);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C39713 r2 = new EventAction("MMPrivateStickerFragmentPermissionResult") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((MMPrivateStickerFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
                }
            };
            eventTaskManager.pushLater("MMPrivateStickerFragmentPermissionResult", r2);
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null && i == 10000 && checkExternalSoragePermission()) {
            takePhoto();
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnDelete) {
            onClickBtnDelete();
        } else if (id == C4558R.C4560id.btnBack) {
            dismiss();
        }
    }

    private void onClickBtnDelete() {
        if (!NetworkUtil.hasDataNetwork(getActivity())) {
            showConnectionError();
            return;
        }
        List<String> selectStickers = this.mStickerGridView.getSelectStickers();
        if (!CollectionsUtil.isListEmpty(selectStickers)) {
            MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
            if (zoomPrivateStickerMgr != null) {
                for (String discardPrivateSticker : selectStickers) {
                    zoomPrivateStickerMgr.discardPrivateSticker(discardPrivateSticker);
                }
                this.mStickerGridView.removeSticker(selectStickers);
            }
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 0).show();
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    /* access modifiers changed from: private */
    public void OnNewStickerUploaded(String str, int i, @Nullable String str2) {
        if (this.mStickerGridView.hasSticker(str2)) {
            ErrorMsgDialog.newInstance(getString(C4558R.string.zm_msg_duplicate_emoji), -1).show(getFragmentManager(), ErrorMsgDialog.class.getName());
        }
        this.mStickerGridView.OnNewStickerUploaded(str, i, str2);
    }

    /* access modifiers changed from: private */
    public void OnMakePrivateSticker(int i, String str, @Nullable String str2) {
        if (i == 0) {
            this.mStickerGridView.onAddSticker(str2);
        }
    }

    /* access modifiers changed from: private */
    public void OnDiscardPrivateSticker(int i, String str) {
        if (i == 0) {
            this.mStickerGridView.onRemoveSticker(str);
        }
    }

    /* access modifiers changed from: private */
    public void OnPrivateStickersUpdated() {
        this.mStickerGridView.refreshData();
    }

    /* access modifiers changed from: private */
    public void OnStickerDownloaded(String str, int i) {
        if (i == 0) {
            this.mStickerGridView.onStickerDownloaded(str);
        }
    }

    /* access modifiers changed from: private */
    public void FT_UploadToMyList_OnProgress(String str, int i, int i2, int i3) {
        this.mStickerGridView.updateOrAddUploadSticker(str, i);
    }

    /* access modifiers changed from: private */
    public void Indicate_UploadToMyFiles_Sent(String str, String str2, int i) {
        this.mStickerGridView.Indicate_UploadToMyFiles_Sent(str, str2, i);
    }
}

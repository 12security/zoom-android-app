package com.zipow.videobox.view;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.confapp.meeting.confhelper.ShareOptionType;
import com.zipow.videobox.dialog.PermissionUnableAccessDialog;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;

public class ShareTip extends ZMTipFragment {
    private static final String ARG_ANCHOR_ID = "anchorId";
    private static final int REQUEST_CODE_IMAGE = 3001;
    private static final int REQUEST_CODE_NATIVE_FILE = 3002;
    private long mRequestPermissionTime;
    private View shareImage;

    public static void show(@Nullable FragmentManager fragmentManager, int i) {
        if (fragmentManager != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("anchorId", i);
            ShareTip shareTip = new ShareTip();
            shareTip.setArguments(bundle);
            shareTip.show(fragmentManager, ShareTip.class.getName());
        }
    }

    public static boolean isShown(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        if (((ShareTip) fragmentManager.findFragmentByTag(ShareTip.class.getName())) != null) {
            z = true;
        }
        return z;
    }

    public static boolean dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        ShareTip shareTip = (ShareTip) fragmentManager.findFragmentByTag(ShareTip.class.getName());
        if (shareTip == null) {
            return false;
        }
        shareTip.dismiss();
        return true;
    }

    public void onResume() {
        super.onResume();
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getActivity())) {
            this.shareImage.sendAccessibilityEvent(8);
        }
    }

    public static void selectShareType(@NonNull ShareOptionType shareOptionType) {
        ZMConfComponentMgr.getInstance().selectShareType(shareOptionType);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0183  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x018a  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x01e2  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0205  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0210  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public p021us.zoom.androidlib.widget.ZMTip onCreateTip(@androidx.annotation.NonNull android.content.Context r20, @androidx.annotation.NonNull android.view.LayoutInflater r21, android.os.Bundle r22) {
        /*
            r19 = this;
            r0 = r19
            r1 = r20
            r2 = r21
            us.zoom.androidlib.widget.ZMTip r3 = new us.zoom.androidlib.widget.ZMTip
            r3.<init>(r1)
            int r4 = p021us.zoom.videomeetings.C4558R.layout.zm_share_tip
            r5 = 0
            android.view.View r4 = r2.inflate(r4, r3, r5)
            int r6 = p021us.zoom.videomeetings.C4558R.C4560id.shareItemContainer
            android.view.View r6 = r4.findViewById(r6)
            android.widget.LinearLayout r6 = (android.widget.LinearLayout) r6
            int r7 = p021us.zoom.videomeetings.C4558R.C4560id.share_image
            android.view.View r7 = r4.findViewById(r7)
            r0.shareImage = r7
            int r7 = p021us.zoom.videomeetings.C4558R.C4560id.share_dropbox
            android.view.View r7 = r4.findViewById(r7)
            int r8 = p021us.zoom.videomeetings.C4558R.C4560id.share_url
            android.view.View r8 = r4.findViewById(r8)
            int r9 = p021us.zoom.videomeetings.C4558R.C4560id.share_from_bookmark
            android.view.View r9 = r4.findViewById(r9)
            int r10 = p021us.zoom.videomeetings.C4558R.C4560id.share_native_file
            android.view.View r10 = r4.findViewById(r10)
            int r11 = p021us.zoom.videomeetings.C4558R.C4560id.share_screen
            android.view.View r11 = r4.findViewById(r11)
            int r12 = p021us.zoom.videomeetings.C4558R.C4560id.share_one_drive
            android.view.View r12 = r4.findViewById(r12)
            int r13 = p021us.zoom.videomeetings.C4558R.C4560id.share_one_drive_business
            android.view.View r13 = r4.findViewById(r13)
            int r14 = p021us.zoom.videomeetings.C4558R.C4560id.share_box
            android.view.View r14 = r4.findViewById(r14)
            int r15 = p021us.zoom.videomeetings.C4558R.C4560id.share_google_drive
            android.view.View r15 = r4.findViewById(r15)
            r22 = r3
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.share_whiteboard
            android.view.View r3 = r4.findViewById(r3)
            r3.setVisibility(r5)
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_config_share_custom_screen_handler
            java.lang.String r5 = p021us.zoom.androidlib.util.ResourcesUtil.getString(r1, r5)
            boolean r16 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r5)
            if (r16 != 0) goto L_0x00af
            java.lang.String r16 = ""
            java.lang.Class r5 = java.lang.Class.forName(r5)     // Catch:{ Exception -> 0x0088 }
            java.lang.Object r5 = r5.newInstance()     // Catch:{ Exception -> 0x0088 }
            com.zipow.videobox.util.IShareCustomScreenHandler r5 = (com.zipow.videobox.util.IShareCustomScreenHandler) r5     // Catch:{ Exception -> 0x0088 }
            r17 = r4
            com.zipow.videobox.VideoBoxApplication r4 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Exception -> 0x008a }
            java.lang.String r16 = r5.getShareCustomScreenName(r4)     // Catch:{ Exception -> 0x008a }
            r4 = r16
            goto L_0x008c
        L_0x0088:
            r17 = r4
        L_0x008a:
            r4 = r16
        L_0x008c:
            int r5 = p021us.zoom.videomeetings.C4558R.layout.zm_share_custom_tip_item
            r16 = r11
            r11 = 0
            r18 = r10
            r10 = 0
            android.view.View r2 = r2.inflate(r5, r11, r10)
            int r5 = p021us.zoom.videomeetings.C4558R.C4560id.share_custom
            android.view.View r5 = r2.findViewById(r5)
            android.widget.TextView r5 = (android.widget.TextView) r5
            r5.setText(r4)
            com.zipow.videobox.view.ShareTip$1 r4 = new com.zipow.videobox.view.ShareTip$1
            r4.<init>()
            r5.setOnClickListener(r4)
            r6.addView(r2)
            goto L_0x00b5
        L_0x00af:
            r17 = r4
            r18 = r10
            r16 = r11
        L_0x00b5:
            com.zipow.videobox.confapp.ConfMgr r2 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmConfContext r2 = r2.getConfContext()
            r5 = 8
            if (r2 == 0) goto L_0x00db
            boolean r4 = r2.isShareDropBoxOFF()
            if (r4 != 0) goto L_0x00db
            us.zoom.thirdparty.dropbox.ZMDropbox r4 = p021us.zoom.thirdparty.dropbox.ZMDropbox.getInstance()
            boolean r4 = r4.isDropboxLoginSupported(r1)
            if (r4 == 0) goto L_0x00db
            us.zoom.thirdparty.dropbox.ZMDropbox r4 = p021us.zoom.thirdparty.dropbox.ZMDropbox.getInstance()
            boolean r4 = r4.checkValid(r1)
            if (r4 != 0) goto L_0x00de
        L_0x00db:
            r7.setVisibility(r5)
        L_0x00de:
            r4 = 1
            if (r2 == 0) goto L_0x0110
            boolean r6 = r2.isShareOneDriveOFF()
            if (r6 == 0) goto L_0x00e8
            goto L_0x0110
        L_0x00e8:
            r6 = 0
            boolean r10 = p021us.zoom.thirdparty.onedrive.OneDriveManager.isLoginSupported(r1, r6)
            if (r10 == 0) goto L_0x00f9
            us.zoom.thirdparty.onedrive.OneDriveManager r10 = p021us.zoom.thirdparty.onedrive.OneDriveManager.getInstance()
            boolean r10 = r10.checkValid(r6)
            if (r10 != 0) goto L_0x00fc
        L_0x00f9:
            r12.setVisibility(r5)
        L_0x00fc:
            boolean r6 = p021us.zoom.thirdparty.onedrive.OneDriveManager.isLoginSupported(r1, r4)
            if (r6 == 0) goto L_0x010c
            us.zoom.thirdparty.onedrive.OneDriveManager r6 = p021us.zoom.thirdparty.onedrive.OneDriveManager.getInstance()
            boolean r6 = r6.checkValid(r4)
            if (r6 != 0) goto L_0x0116
        L_0x010c:
            r13.setVisibility(r5)
            goto L_0x0116
        L_0x0110:
            r13.setVisibility(r5)
            r12.setVisibility(r5)
        L_0x0116:
            boolean r6 = p021us.zoom.androidlib.util.OsUtil.isAtLeastJB_MR2()
            if (r6 != 0) goto L_0x011f
            r13.setVisibility(r5)
        L_0x011f:
            if (r2 == 0) goto L_0x0131
            boolean r6 = r2.isShareBoxComOFF()
            if (r6 != 0) goto L_0x0131
            us.zoom.thirdparty.box.BoxMgr r6 = p021us.zoom.thirdparty.box.BoxMgr.getInstance()
            boolean r6 = r6.checkValid(r1)
            if (r6 != 0) goto L_0x0134
        L_0x0131:
            r14.setVisibility(r5)
        L_0x0134:
            if (r2 == 0) goto L_0x0152
            boolean r6 = r2.isShareGoogleDriveOFF()
            if (r6 != 0) goto L_0x0152
            boolean r6 = p021us.zoom.thirdparty.googledrive.GoogleDrive.canAuthGoogleViaBrowser(r20)
            if (r6 == 0) goto L_0x0152
            us.zoom.thirdparty.googledrive.GoogleDriveMgr r6 = p021us.zoom.thirdparty.googledrive.GoogleDriveMgr.getInstance()
            boolean r6 = r6.checkValid(r1)
            if (r6 == 0) goto L_0x0152
            boolean r6 = com.zipow.videobox.util.ZMUtils.isItuneApp(r20)
            if (r6 == 0) goto L_0x0155
        L_0x0152:
            r15.setVisibility(r5)
        L_0x0155:
            if (r2 == 0) goto L_0x015d
            boolean r2 = r2.isWBFeatureOFF()
            if (r2 == 0) goto L_0x0160
        L_0x015d:
            r3.setVisibility(r5)
        L_0x0160:
            android.view.View r2 = r0.shareImage
            com.zipow.videobox.view.ShareTip$2 r6 = new com.zipow.videobox.view.ShareTip$2
            r6.<init>()
            r2.setOnClickListener(r6)
            com.zipow.videobox.view.ShareTip$3 r2 = new com.zipow.videobox.view.ShareTip$3
            r2.<init>()
            r14.setOnClickListener(r2)
            com.zipow.videobox.view.ShareTip$4 r2 = new com.zipow.videobox.view.ShareTip$4
            r2.<init>()
            r7.setOnClickListener(r2)
            int r2 = p021us.zoom.videomeetings.C4558R.bool.zm_config_no_share_webview
            r6 = 0
            boolean r2 = p021us.zoom.androidlib.util.ResourcesUtil.getBoolean(r1, r2, r6)
            if (r2 == 0) goto L_0x018a
            r8.setVisibility(r5)
            r9.setVisibility(r5)
            goto L_0x019a
        L_0x018a:
            com.zipow.videobox.view.ShareTip$5 r2 = new com.zipow.videobox.view.ShareTip$5
            r2.<init>()
            r8.setOnClickListener(r2)
            com.zipow.videobox.view.ShareTip$6 r2 = new com.zipow.videobox.view.ShareTip$6
            r2.<init>()
            r9.setOnClickListener(r2)
        L_0x019a:
            com.zipow.videobox.view.ShareTip$7 r2 = new com.zipow.videobox.view.ShareTip$7
            r2.<init>()
            r6 = r18
            r6.setOnClickListener(r2)
            com.zipow.videobox.view.ShareTip$8 r2 = new com.zipow.videobox.view.ShareTip$8
            r2.<init>()
            r12.setOnClickListener(r2)
            com.zipow.videobox.view.ShareTip$9 r2 = new com.zipow.videobox.view.ShareTip$9
            r2.<init>()
            r13.setOnClickListener(r2)
            com.zipow.videobox.view.ShareTip$10 r2 = new com.zipow.videobox.view.ShareTip$10
            r2.<init>()
            r6 = r16
            r6.setOnClickListener(r2)
            com.zipow.videobox.view.ShareTip$11 r2 = new com.zipow.videobox.view.ShareTip$11
            r2.<init>()
            r15.setOnClickListener(r2)
            com.zipow.videobox.view.ShareTip$12 r2 = new com.zipow.videobox.view.ShareTip$12
            r2.<init>()
            r3.setOnClickListener(r2)
            r2 = r22
            r3 = r17
            r2.addView(r3)
            android.os.Bundle r3 = r19.getArguments()
            java.lang.String r7 = "anchorId"
            r10 = 0
            int r3 = r3.getInt(r7, r10)
            if (r3 <= 0) goto L_0x01ff
            androidx.fragment.app.FragmentActivity r7 = r19.getActivity()
            com.zipow.videobox.ConfActivity r7 = (com.zipow.videobox.ConfActivity) r7
            if (r7 == 0) goto L_0x01ff
            android.view.View r3 = r7.findViewById(r3)
            if (r3 == 0) goto L_0x01ff
            androidx.fragment.app.FragmentActivity r7 = r19.getActivity()
            boolean r7 = com.zipow.videobox.util.UIMgr.isLargeMode(r7)
            if (r7 == 0) goto L_0x01fb
            goto L_0x01fc
        L_0x01fb:
            r4 = 3
        L_0x01fc:
            r2.setAnchor(r3, r4)
        L_0x01ff:
            boolean r3 = p021us.zoom.androidlib.util.AndroidAppUtil.hasActiviyToSelectImage(r20)
            if (r3 != 0) goto L_0x020a
            android.view.View r3 = r0.shareImage
            r3.setVisibility(r5)
        L_0x020a:
            boolean r1 = com.zipow.videobox.util.ConfLocalHelper.canShareScreen(r20)
            if (r1 == 0) goto L_0x0211
            r5 = 0
        L_0x0211:
            r6.setVisibility(r5)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.ShareTip.onCreateTip(android.content.Context, android.view.LayoutInflater, android.os.Bundle):us.zoom.androidlib.widget.ZMTip");
    }

    /* access modifiers changed from: private */
    public boolean checkStoragePermission() {
        return VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0;
    }

    /* access modifiers changed from: private */
    @TargetApi(16)
    public void requestStoragePermission(int i) {
        this.mRequestPermissionTime = System.currentTimeMillis();
        zm_requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, i);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        final long currentTimeMillis = System.currentTimeMillis() - this.mRequestPermissionTime;
        this.mRequestPermissionTime = 0;
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C355713 r4 = new EventAction("ShareTipPermissionResult") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ShareTip) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2, currentTimeMillis);
            }
        };
        getNonNullEventTaskManagerOrThrowException().pushLater("ShareTipPermissionResult", r4);
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr, long j) {
        if (strArr != null && iArr != null) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                for (int i2 = 0; i2 < strArr.length; i2++) {
                    if ("android.permission.READ_EXTERNAL_STORAGE".equals(strArr[i2])) {
                        if (iArr[i2] != 0) {
                            if (j <= 1000 && !ActivityCompat.shouldShowRequestPermissionRationale(zMActivity, strArr[i2])) {
                                PermissionUnableAccessDialog.showDialog(zMActivity.getSupportFragmentManager(), strArr[i2]);
                            }
                            dismiss();
                            return;
                        } else if (i == 3001) {
                            selectShareType(ShareOptionType.SHARE_IMAGE);
                        } else if (i == 3002) {
                            selectShareType(ShareOptionType.SHARE_NATIVE_FILE);
                        }
                    }
                }
            }
        }
    }
}

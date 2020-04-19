package com.zipow.videobox.confapp.meeting.confhelper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.nydus.KUBIDeviceController;
import com.zipow.nydus.KUBIDeviceController.SimpleKubiListener;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.kubi.KubiChoiceFragment;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class KubiComponent extends SimpleKubiListener implements OnClickListener {
    private static final int REQUEST_ENABLE_BT = 1017;
    private static final int REQUEST_LOCATION_PERMISSION_FOR_KUBI = 1018;
    private View mBtnKubi = this.mContext.findViewById(C4558R.C4560id.btnKubi);
    /* access modifiers changed from: private */
    @NonNull
    public final ConfActivity mContext;
    private ImageView mImgKubi = ((ImageView) this.mContext.findViewById(C4558R.C4560id.imgKubi));
    private TextView mTxtKubiStatus = ((TextView) this.mContext.findViewById(C4558R.C4560id.txtKubiStatus));

    public KubiComponent(@NonNull ConfActivity confActivity) {
        this.mContext = confActivity;
        View view = this.mBtnKubi;
        if (view != null) {
            view.setOnClickListener(this);
        }
        KUBIDeviceController instance = KUBIDeviceController.getInstance();
        if (instance != null) {
            instance.addKubiListener(this);
        }
    }

    public void onDestroy() {
        KUBIDeviceController instance = KUBIDeviceController.getInstance();
        if (instance != null) {
            instance.removeKubiListener(this);
        }
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i != 1017 || i2 != -1) {
            return false;
        }
        chooseKubi(false);
        return true;
    }

    public boolean handleRequestPermissionResult(int i, @NonNull String str, int i2) {
        if (1018 != i || !"android.permission.ACCESS_FINE_LOCATION".equals(str) || i2 != 0) {
            return false;
        }
        chooseKubi(true);
        return true;
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnKubi) {
            chooseKubi(true);
        }
    }

    public void onKubiManagerStatusChanged(int i, int i2) {
        updateKubiButton();
        if (i != 4 && i2 == 4) {
            ConfMgr.getInstance().handleConfCmd(60);
        } else if (i == 4 && i2 != 4) {
            ConfMgr.getInstance().handleConfCmd(61);
        }
    }

    public void updateKubiButton() {
        View view = this.mBtnKubi;
        if (view != null && this.mImgKubi != null && this.mTxtKubiStatus != null) {
            view.setVisibility(8);
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && ConfMgr.getInstance().isConfConnected() && confContext.isKubiEnabled()) {
                KUBIDeviceController instance = KUBIDeviceController.getInstance();
                if (instance != null) {
                    this.mBtnKubi.setVisibility(0);
                    switch (instance.getKubiStatus()) {
                        case 0:
                            this.mImgKubi.setImageResource(C4558R.C4559drawable.zm_ic_kubi_disconnected);
                            this.mTxtKubiStatus.setText(C4558R.string.zm_kubi_status_disconnected);
                            break;
                        case 1:
                            this.mImgKubi.setImageResource(C4558R.C4559drawable.zm_ic_kubi_connected);
                            this.mTxtKubiStatus.setText(C4558R.string.zm_kubi_status_disconnecting);
                            break;
                        case 2:
                        case 3:
                        case 5:
                            this.mImgKubi.setImageResource(C4558R.C4559drawable.zm_ic_kubi_disconnected);
                            this.mTxtKubiStatus.setText(C4558R.string.zm_kubi_status_connecting);
                            break;
                        case 4:
                            this.mImgKubi.setImageResource(C4558R.C4559drawable.zm_ic_kubi_connected);
                            this.mTxtKubiStatus.setText(C4558R.string.zm_kubi_status_connected);
                            break;
                    }
                    this.mImgKubi.setDuplicateParentStateEnabled(true);
                    this.mTxtKubiStatus.setDuplicateParentStateEnabled(true);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void chooseKubi(boolean z) {
        if (z && !checkBluetoothStatus()) {
            turnOnBluetoothForKubi();
        } else if (checkLocationPermissionForKubi()) {
            showKubiChoiceFragment();
        } else {
            requestLocationPermissionForKubi();
        }
    }

    public void onKubiChoiceFragmentClosed() {
        this.mContext.hideToolbarDelayed(5000);
    }

    private boolean checkBluetoothStatus() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) {
            return false;
        }
        return defaultAdapter.isEnabled();
    }

    private boolean checkLocationPermissionForKubi() {
        if (VERSION.SDK_INT > 22 && this.mContext.zm_checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0) {
            return false;
        }
        return true;
    }

    private void requestLocationPermissionForKubi() {
        new Builder(this.mContext).setMessage(C4558R.string.zm_kubi_request_location_permission).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                KubiComponent.this.mContext.requestPermission("android.permission.ACCESS_FINE_LOCATION", 1018, 0);
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create().show();
    }

    private void turnOnBluetoothForKubi() {
        PackageManager packageManager = this.mContext.getPackageManager();
        if (packageManager == null || packageManager.checkPermission("android.permission.BLUETOOTH_ADMIN", this.mContext.getPackageName()) != 0) {
            ActivityStartHelper.startActivityForResult((Activity) this.mContext, new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1017);
            return;
        }
        ZMAlertDialog create = new Builder(this.mContext).setTitle(C4558R.string.zm_kubi_bluetooth_turn_on_request).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                KubiComponent.this.turnOnBluetooth();
                KubiComponent.this.chooseKubi(false);
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
    }

    private void showKubiChoiceFragment() {
        this.mContext.disableToolbarAutoHide();
        KubiChoiceFragment.showDialog(this.mContext.getSupportFragmentManager());
    }

    /* access modifiers changed from: private */
    public void turnOnBluetooth() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null && !defaultAdapter.isEnabled()) {
            defaultAdapter.enable();
        }
    }
}

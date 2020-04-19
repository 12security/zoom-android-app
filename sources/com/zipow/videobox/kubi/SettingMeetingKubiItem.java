package com.zipow.videobox.kubi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import p021us.zoom.videomeetings.C4558R;

public class SettingMeetingKubiItem extends LinearLayout {
    public static final int KUBI_STATUS_CONNECTED = 2;
    public static final int KUBI_STATUS_CONNECTING = 1;
    public static final int KUBI_STATUS_DISCONNECTED = 0;
    @Nullable
    private KubiDevice mKubiDevice;
    private TextView mTxtKubiName;
    private TextView mTxtKubiStatus;

    public SettingMeetingKubiItem(Context context) {
        super(context);
        initView(context);
    }

    public SettingMeetingKubiItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public SettingMeetingKubiItem(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    @RequiresApi(api = 21)
    public SettingMeetingKubiItem(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(C4558R.layout.zm_setting_meeting_kubi_item, this, true);
        this.mTxtKubiName = (TextView) findViewById(C4558R.C4560id.txtKubiName);
        this.mTxtKubiStatus = (TextView) findViewById(C4558R.C4560id.txtKubiStatus);
    }

    public void setKubiDevice(@Nullable KubiDevice kubiDevice) {
        this.mKubiDevice = kubiDevice;
        String str = "";
        if (kubiDevice != null) {
            str = kubiDevice.getName();
        }
        this.mTxtKubiName.setText(str);
    }

    @Nullable
    public KubiDevice getKubiDevice() {
        return this.mKubiDevice;
    }

    public void setKubiStatus(int i) {
        switch (i) {
            case 1:
                this.mTxtKubiStatus.setVisibility(0);
                this.mTxtKubiStatus.setText(C4558R.string.zm_msg_connecting);
                return;
            case 2:
                this.mTxtKubiStatus.setVisibility(0);
                this.mTxtKubiStatus.setText(C4558R.string.zm_msg_connected);
                return;
            default:
                this.mTxtKubiStatus.setVisibility(8);
                return;
        }
    }
}

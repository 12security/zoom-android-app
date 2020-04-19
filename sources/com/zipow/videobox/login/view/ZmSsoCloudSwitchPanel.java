package com.zipow.videobox.login.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.common.p008pt.ZMNativeSsoCloudInfo;
import com.zipow.videobox.fragment.MMSSOLoginFragment;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.login.model.ZmSsoCloudSwitchNotify;
import com.zipow.videobox.ptapp.PTApp;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class ZmSsoCloudSwitchPanel extends LinearLayout {
    private static final String TAG = "ZmInternationalLoginPanel";
    /* access modifiers changed from: private */
    public boolean isRefreshState;
    /* access modifiers changed from: private */
    public RadioButton mRadioLeft;
    /* access modifiers changed from: private */
    public RadioButton mRadioRight;

    public ZmSsoCloudSwitchPanel(Context context) {
        this(context, null);
    }

    public ZmSsoCloudSwitchPanel(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public ZmSsoCloudSwitchPanel(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isRefreshState = false;
        init();
    }

    private void init() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_layout_cloudswitch, this);
        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(C4558R.C4560id.zmRadioGroupDomain);
        this.mRadioLeft = (RadioButton) inflate.findViewById(C4558R.C4560id.radioZoomLeftDomain);
        this.mRadioRight = (RadioButton) inflate.findViewById(C4558R.C4560id.radioZoomRightDomain);
        int i = 8;
        if (!ZmLoginHelper.isCloudSwitchShow(ZmLoginHelper.getZoom3rdPartyVendor())) {
            radioGroup.setVisibility(8);
        } else {
            PTApp instance = PTApp.getInstance();
            ArrayList arrayList = new ArrayList();
            instance.getCloudSwitchList(arrayList);
            radioGroup.setVisibility(0);
            if (arrayList.size() > 1) {
                this.mRadioRight.setVisibility(!StringUtil.isEmptyOrNull((String) arrayList.get(1)) ? 0 : 8);
                this.mRadioRight.setText((CharSequence) arrayList.get(1));
            } else {
                this.mRadioRight.setVisibility(8);
            }
            RadioButton radioButton = this.mRadioLeft;
            if (!StringUtil.isEmptyOrNull((String) arrayList.get(0))) {
                i = 0;
            }
            radioButton.setVisibility(i);
            this.mRadioLeft.setText((CharSequence) arrayList.get(0));
            radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int i2 = 0;
                    if (!ZmSsoCloudSwitchPanel.this.isRefreshState) {
                        if (i == C4558R.C4560id.radioZoomLeftDomain) {
                            i2 = ZmLoginHelper.getSelectVendorByDomain(ZmSsoCloudSwitchPanel.this.mRadioLeft.getText().toString());
                        } else if (i == C4558R.C4560id.radioZoomRightDomain) {
                            i2 = ZmLoginHelper.getSelectVendorByDomain(ZmSsoCloudSwitchPanel.this.mRadioRight.getText().toString());
                        }
                        ZmSsoCloudSwitchPanel.this.setCloudSwitch(i2);
                        ZmSsoCloudSwitchNotify.getInstance().onCloudSwitchChange(i2);
                        return;
                    }
                    ZmSsoCloudSwitchPanel.this.isRefreshState = false;
                }
            });
        }
        setFocusable(false);
    }

    /* access modifiers changed from: private */
    public void setCloudSwitch(int i) {
        String str = "";
        ZMActivity activity = getActivity(getContext());
        if (activity != null) {
            Fragment findFragmentByTag = activity.getSupportFragmentManager().findFragmentByTag(MMSSOLoginFragment.class.getName());
            if (findFragmentByTag != null) {
                str = ((MMSSOLoginFragment) findFragmentByTag).getEdtDomain();
            }
        }
        ZmLoginHelper.setSSODomain(str, i);
    }

    private static ZMActivity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof ZMActivity) {
            return (ZMActivity) context;
        }
        if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public void refreshCloudSwitchState() {
        ZMNativeSsoCloudInfo sSOCloudInfo = PTApp.getInstance().getSSOCloudInfo();
        if (sSOCloudInfo != null) {
            refreshCloudSwitchState(sSOCloudInfo.getmSsoCloud());
        }
    }

    public void refreshCloudSwitchState(int i) {
        this.isRefreshState = true;
        int selectVendorByDomain = ZmLoginHelper.getSelectVendorByDomain(this.mRadioLeft.getText().toString());
        if (this.mRadioLeft.getVisibility() == 0 && i == selectVendorByDomain) {
            this.mRadioLeft.setChecked(true);
        } else if (this.mRadioRight.getVisibility() == 0) {
            this.mRadioRight.setChecked(true);
        }
        this.isRefreshState = false;
    }
}

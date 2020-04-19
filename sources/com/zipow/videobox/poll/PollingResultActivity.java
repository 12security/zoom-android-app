package com.zipow.videobox.poll;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;

public class PollingResultActivity extends ZMActivity implements IPollingListener {
    private static final String EXTRA_POLLING_ID = "pollingId";
    private String mPollingId;
    private IPollingMgr mPollingMgr;

    /* access modifiers changed from: protected */
    public void loadPollingMgr() {
    }

    public void onPollingSubmitResult(String str, int i) {
    }

    public PollingResultActivity() {
        loadPollingMgr();
    }

    private String getmPollingId() {
        return this.mPollingId;
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        this.mPollingId = getIntent().getStringExtra("pollingId");
        ZMActivity frontActivity = getFrontActivity();
        if (frontActivity instanceof PollingResultActivity) {
            if (StringUtil.isSameStringForNotAllowNull(this.mPollingId, ((PollingResultActivity) frontActivity).getmPollingId())) {
                finish();
                return;
            }
        }
        if (!ConfLocalHelper.isShareResulting(this.mPollingId)) {
            finish();
            return;
        }
        if (bundle == null) {
            showPollingResult();
        }
    }

    private void showPollingResult() {
        if (this.mPollingMgr != null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (supportFragmentManager != null) {
                PollingResultFragment pollingResultFragment = new PollingResultFragment();
                Bundle bundle = new Bundle();
                bundle.putString("pollingId", this.mPollingId);
                pollingResultFragment.setArguments(bundle);
                FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
                beginTransaction.replace(16908290, pollingResultFragment, PollingResultFragment.class.getName());
                beginTransaction.commit();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        IPollingMgr iPollingMgr = this.mPollingMgr;
        if (iPollingMgr != null) {
            iPollingMgr.removeListener(this);
        }
    }

    public void onPollingStatusChanged(String str, int i) {
        if (StringUtil.isSameString(str, this.mPollingId) && i == 2) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                public void run(@NonNull IUIElement iUIElement) {
                    ((PollingResultActivity) iUIElement).handlePollingClosed();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handlePollingClosed() {
        setResult(0);
        finish();
    }

    public void setPollingMgr(IPollingMgr iPollingMgr) {
        IPollingMgr iPollingMgr2 = this.mPollingMgr;
        if (iPollingMgr2 != null) {
            iPollingMgr2.removeListener(this);
        }
        this.mPollingMgr = iPollingMgr;
        IPollingMgr iPollingMgr3 = this.mPollingMgr;
        if (iPollingMgr3 != null) {
            iPollingMgr3.addListener(this);
        }
    }

    public IPollingMgr getPollingMgr() {
        return this.mPollingMgr;
    }
}

package com.zipow.videobox.poll;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.zipow.videobox.mainboard.Mainboard;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class PollingActivity extends ZMActivity implements IPollingListener {
    public static final String EXTRA_IS_READONLY = "isReadOnly";
    public static final String EXTRA_POLLING_ID = "pollingId";
    public static final String EXTRA_READYONLY_MESSAGE_RES = "readOnlyMessageRes";
    private static final String TAG = "PollingActivity";
    private boolean mIsReadOnly = false;
    private String mPollingId;
    private IPollingMgr mPollingMgr;
    @Nullable
    private PollingQuestionFragment mQuestionFragment = null;
    private int mReadOnlyMessageRes = 0;
    @Nullable
    private ProgressDialog mWaitingDialog;

    /* access modifiers changed from: protected */
    public void loadPollingMgr() {
    }

    public PollingActivity() {
        loadPollingMgr();
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        Intent intent = getIntent();
        this.mPollingId = intent.getStringExtra("pollingId");
        this.mIsReadOnly = intent.getBooleanExtra("isReadOnly", false);
        this.mReadOnlyMessageRes = intent.getIntExtra("readOnlyMessageRes", 0);
        if (bundle == null) {
            showQuestion(0, 0, 0);
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

    public void onPollingSubmitResult(final String str, final int i) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((PollingActivity) iUIElement).handlePollingSumbitResult(str, i);
            }
        });
    }

    public void onPollingStatusChanged(String str, int i) {
        if (StringUtil.isSameString(str, this.mPollingId) && i == 2) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                public void run(@NonNull IUIElement iUIElement) {
                    ((PollingActivity) iUIElement).handlePollingClosed();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handlePollingSumbitResult(String str, int i) {
        dismissWaitingDialog();
        if (i == 0) {
            setResult(-1);
            finish();
            return;
        }
        showSubmitFailure(str, i);
    }

    private void showSubmitFailure(String str, int i) {
        String str2;
        if (i == 1) {
            str2 = getString(C4558R.string.zm_polling_msg_failed_to_submit_closed_18524);
        } else {
            str2 = getString(C4558R.string.zm_polling_msg_failed_to_submit_poll, new Object[]{Integer.valueOf(i)});
        }
        Toast.makeText(this, str2, 1).show();
    }

    /* access modifiers changed from: private */
    public void handlePollingClosed() {
        setResult(0);
        finish();
    }

    @Nullable
    private PollingQuestionFragment getCurrentQuestionFragment() {
        if (this.mQuestionFragment == null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (supportFragmentManager != null) {
                this.mQuestionFragment = (PollingQuestionFragment) supportFragmentManager.findFragmentByTag(PollingQuestionFragment.class.getName());
            }
        }
        return this.mQuestionFragment;
    }

    public void showQuestion(int i, int i2, int i3) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager != null) {
            IPollingQuestion pollingQuestionAt = getPollingQuestionAt(i);
            if (pollingQuestionAt != null) {
                this.mQuestionFragment = new PollingQuestionFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(PollingQuestionFragment.ARG_QUESTION_INDEX, i);
                bundle.putString("pollingId", this.mPollingId);
                bundle.putString(PollingQuestionFragment.ARG_QUESTION_ID, pollingQuestionAt.getQuestionId());
                bundle.putBoolean("isReadOnly", this.mIsReadOnly);
                bundle.putInt("readOnlyMessageRes", this.mReadOnlyMessageRes);
                this.mQuestionFragment.setArguments(bundle);
                FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
                beginTransaction.setCustomAnimations(i2, i3);
                beginTransaction.replace(16908290, this.mQuestionFragment, PollingQuestionFragment.class.getName());
                beginTransaction.commit();
            }
        }
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

    public int getQuestionCount() {
        IPollingDoc pollingDoc = getPollingDoc();
        if (pollingDoc == null) {
            return 0;
        }
        return pollingDoc.getQuestionCount();
    }

    private IPollingQuestion getPollingQuestionAt(int i) {
        IPollingDoc pollingDoc = getPollingDoc();
        if (pollingDoc == null) {
            return null;
        }
        return pollingDoc.getQuestionAt(i);
    }

    private IPollingDoc getPollingDoc() {
        IPollingMgr iPollingMgr = this.mPollingMgr;
        if (iPollingMgr != null) {
            String str = this.mPollingId;
            if (str != null) {
                IPollingDoc pollingDocById = iPollingMgr.getPollingDocById(str);
                if (pollingDocById == null) {
                    return null;
                }
                return pollingDocById;
            }
        }
        return null;
    }

    public int getCurrentQuestionIndex() {
        PollingQuestionFragment currentQuestionFragment = getCurrentQuestionFragment();
        if (currentQuestionFragment == null) {
            return -1;
        }
        return currentQuestionFragment.getQuestionIndex();
    }

    public void showPreviousQuesion() {
        int currentQuestionIndex = getCurrentQuestionIndex() - 1;
        if (currentQuestionIndex >= 0) {
            showQuestion(currentQuestionIndex, C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
        }
    }

    public void showNextQuestion() {
        int currentQuestionIndex = getCurrentQuestionIndex() + 1;
        if (currentQuestionIndex < getQuestionCount()) {
            showQuestion(currentQuestionIndex, C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
    }

    public void submitPolling() {
        IPollingMgr iPollingMgr = this.mPollingMgr;
        if (iPollingMgr != null) {
            String str = this.mPollingId;
            if (str != null) {
                iPollingMgr.submitPoll(str);
                showWaitingDownloadDialog();
            }
        }
    }

    private void showWaitingDownloadDialog() {
        if (this.mWaitingDialog == null) {
            this.mWaitingDialog = new ProgressDialog(this);
            this.mWaitingDialog.requestWindowFeature(1);
            this.mWaitingDialog.setMessage(getString(C4558R.string.zm_msg_waiting));
            this.mWaitingDialog.setCanceledOnTouchOutside(false);
            this.mWaitingDialog.setCancelable(false);
            this.mWaitingDialog.show();
        }
    }

    private void dismissWaitingDialog() {
        ProgressDialog progressDialog = this.mWaitingDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mWaitingDialog = null;
        }
    }
}

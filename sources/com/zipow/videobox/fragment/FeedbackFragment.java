package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.SystemInfoHelper;
import com.zipow.videobox.util.UIMgr;
import java.util.Timer;
import java.util.TimerTask;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class FeedbackFragment extends ZMDialogFragment implements OnClickListener, IPTUIListener {
    private static final int STATE_NORMAL = 0;
    private static final int STATE_SENDING = 1;
    private static final int STATE_SEND_DONE = 2;
    private static final int STATE_SEND_FAILED = 3;
    private Button mBtnBack;
    /* access modifiers changed from: private */
    public EditText mEdtFeedback;
    private View mPanelSendFeedback;
    /* access modifiers changed from: private */
    public int mState = 0;
    /* access modifiers changed from: private */
    @Nullable
    public Timer mTimer;
    private TextView mTxtSending;
    private TextView mTxtSentFailed;
    private TextView mTxtThanks;
    private TextView mTxtWelcome;
    /* access modifiers changed from: private */
    public long mWaitStartTime = 0;
    /* access modifiers changed from: private */
    public long mWaitTime = 0;
    /* access modifiers changed from: private */
    public boolean mbWaitingShowPanelSendFeedback = false;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static void showInActivity(ZMActivity zMActivity) {
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, new FeedbackFragment(), FeedbackFragment.class.getName()).commit();
    }

    public static void showDialog(@NonNull FragmentManager fragmentManager) {
        if (getFeedbackFragment(fragmentManager) == null) {
            new FeedbackFragment().show(fragmentManager, FeedbackFragment.class.getName());
        }
    }

    @Nullable
    public static FeedbackFragment getFeedbackFragment(FragmentManager fragmentManager) {
        return (FeedbackFragment) fragmentManager.findFragmentByTag(FeedbackFragment.class.getName());
    }

    @Nullable
    public static FeedbackFragment getFeedbackFragment(ZMActivity zMActivity) {
        return (FeedbackFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(FeedbackFragment.class.getName());
    }

    public FeedbackFragment() {
        setStyle(1, C4558R.style.ZMDialog);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_feedback, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mEdtFeedback = (EditText) inflate.findViewById(C4558R.C4560id.edtFeedback);
        Button button = (Button) inflate.findViewById(C4558R.C4560id.btnSend);
        this.mTxtSending = (TextView) inflate.findViewById(C4558R.C4560id.txtSending);
        this.mTxtSentFailed = (TextView) inflate.findViewById(C4558R.C4560id.txtSentFailed);
        this.mTxtThanks = (TextView) inflate.findViewById(C4558R.C4560id.txtThanks);
        this.mPanelSendFeedback = inflate.findViewById(C4558R.C4560id.panelSendFeedback);
        this.mTxtWelcome = (TextView) inflate.findViewById(C4558R.C4560id.txtWelcome);
        this.mTxtSending.setVisibility(8);
        this.mTxtSentFailed.setVisibility(8);
        this.mTxtThanks.setVisibility(8);
        this.mTxtWelcome.setMovementMethod(LinkMovementMethod.getInstance());
        this.mBtnBack.setOnClickListener(this);
        button.setOnClickListener(this);
        if (bundle != null) {
            this.mState = bundle.getInt("mState");
            this.mbWaitingShowPanelSendFeedback = bundle.getBoolean("mbWaitingShowPanelSendFeedback");
            this.mWaitTime = bundle.getLong("mWaitTime");
            updateUIForCurrentState();
            if (this.mbWaitingShowPanelSendFeedback) {
                showPanelSendFeedbackDelayed(this.mWaitTime);
            }
        }
        if (UIMgr.isLargeMode(getActivity())) {
            this.mBtnBack.setVisibility(8);
        }
        String uRLByType = PTApp.getInstance().getURLByType(0);
        if (!StringUtil.isEmptyOrNull(uRLByType)) {
            this.mTxtWelcome.setText(Html.fromHtml(getString(C4558R.string.zm_msg_feedback_welcome, uRLByType)));
        }
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        PTUI.getInstance().addPTUIListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        PTUI.getInstance().removePTUIListener(this);
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
    }

    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            view.postDelayed(new Runnable() {
                public void run() {
                    FeedbackFragment.this.mEdtFeedback.requestFocus();
                    UIUtil.openSoftKeyboard(FeedbackFragment.this.getActivity(), FeedbackFragment.this.mEdtFeedback);
                }
            }, 100);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("mState", this.mState);
        bundle.putBoolean("mbWaitingShowPanelSendFeedback", this.mbWaitingShowPanelSendFeedback);
        if (this.mbWaitingShowPanelSendFeedback) {
            bundle.putLong("mWaitTime", System.currentTimeMillis() - this.mWaitStartTime);
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnSend) {
            onClickBtnSend();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    private void onClickBtnSend() {
        String trim = this.mEdtFeedback.getText().toString().trim();
        if (trim.length() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(trim);
            sb.append("\n[");
            sb.append(SystemInfoHelper.getHardwareInfo());
            sb.append("]");
            sb.append("\n[Version:");
            sb.append(getString(C4558R.string.zm_version_name));
            sb.append("]");
            String sb2 = sb.toString();
            this.mPanelSendFeedback.setVisibility(8);
            UIUtil.closeSoftKeyboard(getActivity(), getView());
            if (PTApp.getInstance().sendFeedback(sb2)) {
                this.mState = 1;
                updateUIForCurrentState();
            } else {
                this.mState = 3;
                updateUIForCurrentState();
                showPanelSendFeedbackDelayed(2000);
            }
        }
    }

    private void showPanelSendFeedbackDelayed(long j) {
        this.mbWaitingShowPanelSendFeedback = true;
        this.mWaitTime = j;
        this.mWaitStartTime = System.currentTimeMillis();
        final ZMActivity zMActivity = (ZMActivity) getActivity();
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                FeedbackFragment.this.mbWaitingShowPanelSendFeedback = false;
                FeedbackFragment.this.mWaitTime = 0;
                FeedbackFragment.this.mWaitStartTime = 0;
                FeedbackFragment.this.mTimer = null;
                ZMActivity zMActivity = zMActivity;
                if (zMActivity != null && zMActivity.isActive()) {
                    FeedbackFragment.this.onShowPanelSendFeedback();
                }
            }
        }, j);
    }

    /* access modifiers changed from: private */
    public void onShowPanelSendFeedback() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    FeedbackFragment.this.mState = 0;
                    FeedbackFragment.this.updateUIForCurrentState();
                }
            });
        }
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 29) {
            onSendFeedbackResult(j);
        }
    }

    private void onSendFeedbackResult(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(IUIElement iUIElement) {
                FeedbackFragment feedbackFragment = (FeedbackFragment) iUIElement;
                if (feedbackFragment != null) {
                    feedbackFragment.handleOnSendFeedbackResult(j);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnSendFeedbackResult(long j) {
        if (j == 0) {
            this.mState = 2;
            this.mEdtFeedback.setText("");
            updateUIForCurrentState();
            showPanelSendFeedbackDelayed(8000);
            return;
        }
        this.mState = 3;
        updateUIForCurrentState();
        showPanelSendFeedbackDelayed(2000);
    }

    /* access modifiers changed from: private */
    public void updateUIForCurrentState() {
        switch (this.mState) {
            case 0:
                this.mPanelSendFeedback.setVisibility(0);
                this.mTxtWelcome.setVisibility(0);
                this.mTxtSending.setVisibility(8);
                this.mTxtThanks.setVisibility(8);
                this.mTxtSentFailed.setVisibility(8);
                return;
            case 1:
                this.mPanelSendFeedback.setVisibility(8);
                this.mTxtWelcome.setVisibility(8);
                this.mTxtSending.setVisibility(0);
                this.mTxtThanks.setVisibility(8);
                this.mTxtSentFailed.setVisibility(8);
                return;
            case 2:
                this.mPanelSendFeedback.setVisibility(8);
                this.mTxtWelcome.setVisibility(8);
                this.mTxtSending.setVisibility(8);
                this.mTxtThanks.setVisibility(0);
                this.mTxtSentFailed.setVisibility(8);
                return;
            case 3:
                this.mPanelSendFeedback.setVisibility(8);
                this.mTxtWelcome.setVisibility(8);
                this.mTxtSending.setVisibility(8);
                this.mTxtThanks.setVisibility(8);
                this.mTxtSentFailed.setVisibility(0);
                return;
            default:
                return;
        }
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }
}

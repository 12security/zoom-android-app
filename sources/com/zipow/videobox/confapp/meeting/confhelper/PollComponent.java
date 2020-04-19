package com.zipow.videobox.confapp.meeting.confhelper;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.annotation.NonNull;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.confapp.poll.PollingMgr;
import com.zipow.videobox.confapp.poll.PollingUI;
import com.zipow.videobox.confapp.poll.PollingUI.SimplePollingUIListener;
import com.zipow.videobox.poll.IPollingDoc;
import com.zipow.videobox.poll.WebinarPollingActivity;
import com.zipow.videobox.poll.WebinarPollingResultActivity;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.NormalMessageTip;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.videomeetings.C4558R;

public class PollComponent extends SimplePollingUIListener implements OnClickListener {
    public static final int REQUEST_POLLING = 1011;
    public static final int REQUEST_POLLING_RESULT = 1012;
    private Button mBtnPoll = ((Button) this.mContext.findViewById(C4558R.C4560id.btnPoll));
    @NonNull
    private final ConfActivity mContext;

    public PollComponent(@NonNull ConfActivity confActivity) {
        this.mContext = confActivity;
        Button button = this.mBtnPoll;
        if (button != null) {
            button.setOnClickListener(this);
        }
        PollingUI.getInstance().addListener(this);
    }

    public void onDestroy() {
        PollingUI.getInstance().removeListener(this);
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i != 1011 || i2 != -1) {
            return false;
        }
        ConfActivity confActivity = this.mContext;
        NormalMessageTip.show(confActivity, confActivity.getSupportFragmentManager(), TipMessageType.TIP_VOTE_SUBMITTED.name(), -1, C4558R.string.zm_polling_msg_vote_submited, C4558R.C4559drawable.zm_ic_tick, 0, 0, 3000);
        return true;
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnPoll) {
            onClickBtnPoll();
        }
    }

    public void onPollingStatusChanged(int i, final String str) {
        if (this.mContext.isActive()) {
            updatePollButton();
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && !confContext.inSilentMode()) {
            if (i == 1) {
                this.mContext.getNonNullEventTaskManagerOrThrowException().push("handlePollingOpened", new EventAction("handlePollingOpened") {
                    public void run(IUIElement iUIElement) {
                        PollComponent.this.handlePollingOpened(str);
                    }
                });
            } else if (i == 3) {
                this.mContext.getNonNullEventTaskManagerOrThrowException().push("handlePollingShareResult", new EventAction("handlePollingShareResult") {
                    public void run(IUIElement iUIElement) {
                        PollComponent.this.handlePollingShareResult(str);
                    }
                });
            }
        }
    }

    public void onPollingActionResult(int i, final String str, final int i2) {
        if (i == 0 && i2 != 0) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().push("handlePollingRetrieveDocFailed", new EventAction("handlePollingRetrieveDocFailed") {
                public void run(IUIElement iUIElement) {
                    PollComponent.this.handlePollingRetrieveDocFailed(str, i2);
                }
            });
        }
    }

    private void onClickBtnPoll() {
        PollingMgr pollObj = ConfMgr.getInstance().getPollObj();
        if (pollObj != null) {
            int pollingCount = pollObj.getPollingCount();
            if (pollingCount > 0) {
                for (int i = 0; i < pollingCount; i++) {
                    IPollingDoc pollingAtIdx = pollObj.getPollingAtIdx(i);
                    if (pollingAtIdx != null) {
                        int pollingState = pollingAtIdx.getPollingState();
                        int myPollingState = pollingAtIdx.getMyPollingState();
                        String pollingId = pollingAtIdx.getPollingId();
                        if (pollingState == 1 && myPollingState != 2) {
                            handlePollingOpened(pollingId);
                            return;
                        } else if (pollingState == 3) {
                            handlePollingShareResult(pollingId);
                            return;
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlePollingOpened(String str) {
        PollingMgr pollObj = ConfMgr.getInstance().getPollObj();
        if (pollObj != null) {
            IPollingDoc pollingDocById = pollObj.getPollingDocById(str);
            if (pollingDocById != null) {
                int i = 1;
                if (pollingDocById.getPollingState() == 1 && ConfMgr.getInstance().getQAComponent() != null) {
                    if (!pollObj.isPanelistofPolling()) {
                        i = pollObj.isHostofPolling() ? 2 : 0;
                    }
                    WebinarPollingActivity.show(this.mContext, str, i, 1011);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlePollingShareResult(String str) {
        if (ConfLocalHelper.isShareResulting(str)) {
            WebinarPollingResultActivity.show(this.mContext, str, 1012);
        }
    }

    /* access modifiers changed from: private */
    public void handlePollingRetrieveDocFailed(String str, int i) {
        new Builder(this.mContext).setTitle(this.mContext.getString(C4558R.string.zm_polling_msg_failed_to_fetch_poll, new Object[]{Integer.valueOf(i)})).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create().show();
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0047  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updatePollButton() {
        /*
            r9 = this;
            com.zipow.videobox.confapp.ConfMgr r0 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.poll.PollingMgr r0 = r0.getPollObj()
            r1 = 0
            if (r0 == 0) goto L_0x0041
            int r2 = r0.getPollingCount()
            if (r2 <= 0) goto L_0x0041
            r3 = 0
            r4 = 0
        L_0x0013:
            if (r3 >= r2) goto L_0x0042
            com.zipow.videobox.poll.IPollingDoc r5 = r0.getPollingAtIdx(r3)
            r6 = 1
            if (r5 != 0) goto L_0x001d
            goto L_0x003e
        L_0x001d:
            int r7 = r5.getPollingState()
            int r5 = r5.getMyPollingState()
            if (r7 != r6) goto L_0x0033
            r8 = 2
            if (r5 == r8) goto L_0x0033
            android.widget.Button r4 = r9.mBtnPoll
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_polling_btn_return_to_poll
            r4.setText(r5)
            r4 = 1
            goto L_0x003e
        L_0x0033:
            r5 = 3
            if (r7 != r5) goto L_0x003e
            android.widget.Button r4 = r9.mBtnPoll
            int r5 = p021us.zoom.videomeetings.C4558R.string.zm_polling_btn_view_poll_result
            r4.setText(r5)
            r4 = 1
        L_0x003e:
            int r3 = r3 + 1
            goto L_0x0013
        L_0x0041:
            r4 = 0
        L_0x0042:
            android.widget.Button r0 = r9.mBtnPoll
            if (r4 == 0) goto L_0x0047
            goto L_0x0049
        L_0x0047:
            r1 = 8
        L_0x0049:
            r0.setVisibility(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.confapp.meeting.confhelper.PollComponent.updatePollButton():void");
    }
}

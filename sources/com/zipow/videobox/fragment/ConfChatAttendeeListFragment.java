package com.zipow.videobox.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.ConfChatAttendeeItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public abstract class ConfChatAttendeeListFragment extends ZMDialogFragment implements OnItemClickListener {
    @Nullable
    private ConfChatAttendeeItem mAttendeePendingPromote;
    private IConfUIListener mConfUIListener;
    @Nullable
    private String mPromotingJid;
    @Nullable
    private String mPromotingName;

    @Nullable
    public abstract ConfChatAttendeeItem getItemAtPosition(int i);

    /* access modifiers changed from: protected */
    public abstract void onRemoveItem(String str);

    /* access modifiers changed from: protected */
    public void enableAttendeeItemPopMenu(@NonNull ListView listView) {
        listView.setOnItemClickListener(this);
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onConfStatusChanged2(int i, final long j) {
                    if (i == 110) {
                        ConfChatAttendeeListFragment.this.getNonNullEventTaskManagerOrThrowException().pushLater("onPromotePanelistResult", new EventAction("onPromotePanelistResult") {
                            public void run(IUIElement iUIElement) {
                                ((ConfChatAttendeeListFragment) iUIElement).onPromotePanelistResult((int) j);
                            }
                        });
                    } else if (i == 3) {
                        ConfChatAttendeeListFragment.this.getNonNullEventTaskManagerOrThrowException().pushLater("onConfLockStatusChanged", new EventAction("onConfLockStatusChanged") {
                            public void run(IUIElement iUIElement) {
                                ((ConfChatAttendeeListFragment) iUIElement).onConfLockStatusChanged();
                            }
                        });
                    }
                    return true;
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
    }

    public void onDestroy() {
        if (this.mConfUIListener != null) {
            ConfUI.getInstance().removeListener(this.mConfUIListener);
        }
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void onPromotePanelistResult(int i) {
        dismissWaitingDialog();
        if (i != 0) {
            showErrorPromotePanelistMessage(i);
        } else {
            String str = this.mPromotingJid;
            if (str != null) {
                onRemoveItem(str);
                showPromoteRejoinMessage(this.mPromotingName);
            }
        }
        this.mPromotingJid = null;
        this.mPromotingName = null;
    }

    private void showErrorPromotePanelistMessage(int i) {
        String str;
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i != 3035) {
                str = getString(C4558R.string.zm_webinar_msg_failed_to_promote_panelist, Integer.valueOf(i));
            } else {
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                str = getString(C4558R.string.zm_webinar_msg_failed_to_promote_max_panelists, Integer.valueOf(confContext != null ? confContext.getParticipantLimit() : 0));
            }
            new Builder(activity).setTitle((CharSequence) str).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        }
    }

    /* access modifiers changed from: private */
    public void onConfLockStatusChanged() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && !confStatusObj.isConfLocked()) {
            checkContinuePromoteAttendee();
        }
    }

    private void showPromoteRejoinMessage(@Nullable String str) {
        if (str != null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Toast.makeText(activity, getString(C4558R.string.zm_webinar_msg_user_will_rejoin_as_panelist, str), 1).show();
            }
        }
    }

    public void onInflate(Activity activity, AttributeSet attributeSet, @Nullable Bundle bundle) {
        super.onInflate(activity, attributeSet, bundle);
        if (bundle != null) {
            this.mPromotingJid = bundle.getString("mPromotingJid");
            this.mPromotingName = bundle.getString("mPromotingName");
            this.mAttendeePendingPromote = (ConfChatAttendeeItem) bundle.getSerializable("mAttendeePendingPromote");
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mPromotingJid", this.mPromotingJid);
        bundle.putString("mPromotingName", this.mPromotingName);
        bundle.putSerializable("mAttendeePendingPromote", this.mAttendeePendingPromote);
    }

    public void showWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(true);
            newInstance.show(fragmentManager, "FreshWaitingDialog");
        }
    }

    public void dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("FreshWaitingDialog");
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    public void promotePanelist(@NonNull ConfChatAttendeeItem confChatAttendeeItem) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            if (confStatusObj.isConfLocked()) {
                confirmToUnlockMeetingForPromoteAttendee(confChatAttendeeItem);
                return;
            }
            if (ConfMgr.getInstance().promotePanelist(confChatAttendeeItem.jid)) {
                this.mPromotingJid = confChatAttendeeItem.jid;
                this.mPromotingName = confChatAttendeeItem.name;
                showWaitingDialog();
            }
        }
    }

    private void confirmToUnlockMeetingForPromoteAttendee(final ConfChatAttendeeItem confChatAttendeeItem) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            new Builder(zMActivity).setTitle(C4558R.string.zm_webinar_msg_change_role_on_meeting_locked).setPositiveButton(C4558R.string.zm_mi_unlock_meeting, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ConfChatAttendeeListFragment.this.unlockMeetingToPromoteAttendee(confChatAttendeeItem);
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        }
    }

    /* access modifiers changed from: private */
    public void unlockMeetingToPromoteAttendee(ConfChatAttendeeItem confChatAttendeeItem) {
        this.mAttendeePendingPromote = confChatAttendeeItem;
        ConfMgr.getInstance().handleConfCmd(59);
    }

    private void checkContinuePromoteAttendee() {
        ConfChatAttendeeItem confChatAttendeeItem = this.mAttendeePendingPromote;
        if (confChatAttendeeItem != null) {
            promotePanelist(confChatAttendeeItem);
            this.mAttendeePendingPromote = null;
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && ConfLocalHelper.isNeedShowAttendeeActionList()) {
            ConfChatAttendeeItem itemAtPosition = getItemAtPosition(i);
            if (itemAtPosition != null) {
                PAttendeeListActionDialog.show(zMActivity.getSupportFragmentManager(), itemAtPosition);
            }
        }
    }
}

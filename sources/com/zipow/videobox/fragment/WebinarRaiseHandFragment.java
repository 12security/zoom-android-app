package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.SimpleInMeetingActivity;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.ZoomRaiseHandInWebinar;
import com.zipow.videobox.confapp.meeting.PromoteOrDowngradeItem;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.IZoomQAUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.SimpleZoomQAUIListener;
import com.zipow.videobox.fragment.meeting.PromoteOrDowngradeMockFragment;
import com.zipow.videobox.view.WebinarRaiseHandListView;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.videomeetings.C4558R;

public class WebinarRaiseHandFragment extends ZMDialogFragment implements OnClickListener {
    private static final int DELAY_REFRESH_TIME = 600;
    private static final String TAG = "WebinarRaiseHandFragment";
    private Button mBtnLowerAllHands;
    @Nullable
    private IConfUIListener mConfUIListener;
    private final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public WebinarRaiseHandListView mListView;
    /* access modifiers changed from: private */
    public PromoteOrDowngradeMockFragment mPromoteOrDowngradeMockFragment;
    private IZoomQAUIListener mQAUIListener;
    private final Runnable mRunnableRefreshAttendees = new Runnable() {
        public void run() {
            WebinarRaiseHandFragment.this.refreshAttendees();
        }
    };
    private TextView mTxtTitle;

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_webinar_raise_hand, viewGroup, false);
        this.mPromoteOrDowngradeMockFragment = new PromoteOrDowngradeMockFragment(this);
        this.mPromoteOrDowngradeMockFragment.onCreateView(bundle);
        this.mListView = (WebinarRaiseHandListView) inflate.findViewById(C4558R.C4560id.raiseHandListView);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        inflate.findViewById(C4558R.C4560id.btnDone).setOnClickListener(this);
        this.mBtnLowerAllHands = (Button) inflate.findViewById(C4558R.C4560id.btnLowerAllHands);
        this.mBtnLowerAllHands.setOnClickListener(this);
        this.mListView.setEmptyView(inflate.findViewById(C4558R.C4560id.emptyView));
        if (this.mQAUIListener == null) {
            this.mQAUIListener = new SimpleZoomQAUIListener() {
                public void onUserListUpdated() {
                    WebinarRaiseHandFragment.this.postRefreshAttendees();
                }

                public void onUserListInitialized() {
                    WebinarRaiseHandFragment.this.postRefreshAttendees();
                }

                public void onUserRemoved(@NonNull String str) {
                    WebinarRaiseHandFragment.this.postRefreshAttendees();
                    WebinarRaiseHandFragment.this.onUserRemoved(str);
                }

                public void onWebinarAttendeeGuestStatusChanged(long j, boolean z) {
                    WebinarRaiseHandFragment.this.postRefreshAttendees();
                }
            };
        }
        ZoomQAUI.getInstance().addListener(this.mQAUIListener);
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onConfStatusChanged2(int i, final long j) {
                    EventTaskManager eventTaskManager = WebinarRaiseHandFragment.this.getEventTaskManager();
                    if (eventTaskManager != null) {
                        if (i == 31) {
                            eventTaskManager.pushLater("onConfAllowRaiseHandStatusChanged", new EventAction("onConfAllowRaiseHandStatusChanged") {
                                public void run(IUIElement iUIElement) {
                                    ((WebinarRaiseHandFragment) iUIElement).onConfAllowRaiseHandStatusChanged();
                                }
                            });
                        } else if (i == 110) {
                            eventTaskManager.pushLater("onPromotePanelistResult", new EventAction("onPromotePanelistResult") {
                                public void run(IUIElement iUIElement) {
                                    if (WebinarRaiseHandFragment.this.mPromoteOrDowngradeMockFragment != null) {
                                        WebinarRaiseHandFragment.this.mPromoteOrDowngradeMockFragment.onPromotePanelistResult((int) j);
                                        if (j == 0 && ((WebinarRaiseHandFragment) iUIElement) != null) {
                                            WebinarRaiseHandFragment.this.mListView.reloadAllAttendees();
                                        }
                                    }
                                }
                            });
                        } else if (i == 3) {
                            eventTaskManager.pushLater("onConfLockStatusChanged", new EventAction("onConfLockStatusChanged") {
                                public void run(IUIElement iUIElement) {
                                    if (WebinarRaiseHandFragment.this.mPromoteOrDowngradeMockFragment != null) {
                                        WebinarRaiseHandFragment.this.mPromoteOrDowngradeMockFragment.onConfLockStatusChanged();
                                    }
                                }
                            });
                        } else if (i == 111) {
                            eventTaskManager.push("onDePromotePanelist", new EventAction("onDePromotePanelist") {
                                public void run(IUIElement iUIElement) {
                                    if (WebinarRaiseHandFragment.this.mPromoteOrDowngradeMockFragment != null) {
                                        WebinarRaiseHandFragment.this.mPromoteOrDowngradeMockFragment.onDePromotePanelist((int) j);
                                        if (j == 0 && ((WebinarRaiseHandFragment) iUIElement) != null) {
                                            WebinarRaiseHandFragment.this.mListView.reloadAllPanelist();
                                        }
                                    }
                                }
                            });
                        } else if (i == 103) {
                            eventTaskManager.pushLater("onRosterAttributeChangedForAll", new EventAction("onRosterAttributeChangedForAll") {
                                public void run(IUIElement iUIElement) {
                                    ((WebinarRaiseHandFragment) iUIElement).refreshPanelist();
                                }
                            });
                        }
                    }
                    return true;
                }

                public boolean onUserStatusChanged(int i, final long j, int i2) {
                    EventTaskManager eventTaskManager = WebinarRaiseHandFragment.this.getEventTaskManager();
                    if (eventTaskManager != null) {
                        if (i == 36 || i == 37 || i == 38) {
                            eventTaskManager.pushLater("onUserRaiseHandStatusChange", new EventAction("onUserRaiseHandStatusChange") {
                                public void run(IUIElement iUIElement) {
                                    ((WebinarRaiseHandFragment) iUIElement).refreshPanelist();
                                }
                            });
                        } else if (i == 1 || i == 44 || i == 45) {
                            eventTaskManager.pushLater("onHostChanged", new EventAction("onHostChanged") {
                                public void run(IUIElement iUIElement) {
                                    ((WebinarRaiseHandFragment) iUIElement).onHostChanged(j);
                                }
                            });
                        } else if (i == 9 || i == 21) {
                            WebinarRaiseHandFragment.this.updateUserAudioStatus(j);
                        } else if (i == 28 || i == 29) {
                            WebinarRaiseHandFragment.this.talkPrivilegeChange(j);
                        } else if (i == 46) {
                            WebinarRaiseHandFragment.this.refreshPanelist();
                        }
                    }
                    return true;
                }

                public boolean onUserEvent(int i, long j, int i2) {
                    EventTaskManager eventTaskManager = WebinarRaiseHandFragment.this.getEventTaskManager();
                    if (eventTaskManager != null) {
                        final int i3 = i;
                        final long j2 = j;
                        C30578 r1 = new EventAction("onUserEvent") {
                            public void run(IUIElement iUIElement) {
                                ((WebinarRaiseHandFragment) iUIElement).onUserEvent(i3, j2);
                            }
                        };
                        eventTaskManager.pushLater("onUserEvent", r1);
                    }
                    return true;
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        PromoteOrDowngradeMockFragment promoteOrDowngradeMockFragment = this.mPromoteOrDowngradeMockFragment;
        if (promoteOrDowngradeMockFragment != null) {
            promoteOrDowngradeMockFragment.onSaveInstanceState(bundle);
        }
    }

    /* access modifiers changed from: private */
    public void onHostChanged(long j) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            PAttendeeListActionDialog.refreshAction(zMActivity.getSupportFragmentManager(), j);
        }
    }

    /* access modifiers changed from: private */
    public void updateUserAudioStatus(long j) {
        refreshPanelist();
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            PAttendeeListActionDialog.refreshAction(zMActivity.getSupportFragmentManager(), j);
        }
    }

    /* access modifiers changed from: private */
    public void talkPrivilegeChange(long j) {
        refreshAttendees();
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            PAttendeeListActionDialog.refreshAction(zMActivity.getSupportFragmentManager(), j);
        }
    }

    /* access modifiers changed from: private */
    public void onConfAllowRaiseHandStatusChanged() {
        updateLowerHandAllButton();
        refreshTitle();
        this.mListView.onConfAllowRaiseHandStatusChanged();
    }

    private void updateLowerHandAllButton() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (myself == null || ((!myself.isHost() && !myself.isCoHost()) || confStatusObj == null || !confStatusObj.isAllowRaiseHand())) {
            this.mBtnLowerAllHands.setVisibility(8);
        } else {
            this.mBtnLowerAllHands.setVisibility(0);
        }
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SimpleInMeetingActivity.show(zMActivity, WebinarRaiseHandFragment.class.getName(), bundle, i, true, 2);
    }

    @Nullable
    public static WebinarRaiseHandFragment getFragment(FragmentManager fragmentManager) {
        return (WebinarRaiseHandFragment) fragmentManager.findFragmentByTag(WebinarRaiseHandFragment.class.getName());
    }

    public void onResume() {
        super.onResume();
        refreshTitle();
    }

    public void promoteOrDowngrade(@NonNull PromoteOrDowngradeItem promoteOrDowngradeItem) {
        PromoteOrDowngradeMockFragment promoteOrDowngradeMockFragment = this.mPromoteOrDowngradeMockFragment;
        if (promoteOrDowngradeMockFragment != null) {
            promoteOrDowngradeMockFragment.promoteOrDowngrade(promoteOrDowngradeItem);
        }
    }

    private void refreshTitle() {
        if (isAdded()) {
            int raiseHandCount = this.mListView.getRaiseHandCount();
            boolean z = true;
            this.mTxtTitle.setText(getString(C4558R.string.zm_title_webinar_raise_hand, Integer.valueOf(raiseHandCount)));
            Button button = this.mBtnLowerAllHands;
            if (raiseHandCount == 0) {
                z = false;
            }
            button.setEnabled(z);
        }
    }

    /* access modifiers changed from: private */
    public void postRefreshAttendees() {
        this.mHandler.removeCallbacks(this.mRunnableRefreshAttendees);
        this.mHandler.postDelayed(this.mRunnableRefreshAttendees, 600);
    }

    /* access modifiers changed from: private */
    public void refreshAttendees() {
        this.mListView.reloadAllAttendees();
        refreshTitle();
    }

    /* access modifiers changed from: private */
    public void onUserEvent(int i, long j) {
        refreshPanelist();
        if (i == 1) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                PListItemActionDialog.dismissPListActionDialogForUserId(zMActivity.getSupportFragmentManager(), j);
            }
        }
    }

    /* access modifiers changed from: private */
    public void refreshPanelist() {
        this.mListView.reloadAllPanelist();
        refreshTitle();
    }

    /* access modifiers changed from: private */
    public void onUserRemoved(@NonNull String str) {
        refreshPanelist();
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            PAttendeeListActionDialog.dismissPAttendeeListActionDialogForUserId(zMActivity.getSupportFragmentManager(), str);
        }
    }

    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRunnableRefreshAttendees);
        ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
        ConfUI.getInstance().removeListener(this.mConfUIListener);
        super.onDestroyView();
    }

    public void dismiss() {
        finishFragment(true);
    }

    private void onClickBtnLowerHandAll() {
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj != null) {
            raiseHandAPIObj.lowerAllHand();
        }
        if (ConfMgr.getInstance().handleUserCmd(38, 0) && AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            AccessibilityUtil.announceForAccessibilityCompat((View) this.mListView, C4558R.string.zm_accessibility_all_hands_lowered_23053);
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnDone) {
                dismiss();
            } else if (id == C4558R.C4560id.btnLowerAllHands) {
                onClickBtnLowerHandAll();
            }
        }
    }
}

package com.zipow.videobox.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.common.ZMConfiguration;
import com.zipow.videobox.confapp.AttentionTrackEventSinkUI;
import com.zipow.videobox.confapp.AttentionTrackEventSinkUI.IAttentionTrackEventSinkUIListener;
import com.zipow.videobox.confapp.AttentionTrackEventSinkUI.SimpleAttentionTrackEventSinkUIListener;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmFeedbackMgr;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.ZoomShareUI;
import com.zipow.videobox.confapp.ZoomShareUI.SimpleZoomShareUIListener;
import com.zipow.videobox.confapp.meeting.ConfParams;
import com.zipow.videobox.confapp.meeting.PromoteOrDowngradeItem;
import com.zipow.videobox.confapp.meeting.optimize.ZMConfPListUserEventPolicy;
import com.zipow.videobox.confapp.meeting.optimize.ZMConfPListUserEventPolicy.CallBack;
import com.zipow.videobox.confapp.p009bo.BOMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.IZoomQAUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.SimpleZoomQAUIListener;
import com.zipow.videobox.fragment.meeting.PromoteOrDowngradeMockFragment;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.InviteContentGenerator;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.NonVerbalFeedbackActionView;
import com.zipow.videobox.view.NonVerbalFeedbackActionView.NonVerbalFBListener;
import com.zipow.videobox.view.PListView;
import java.util.Collection;
import java.util.HashMap;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.ParamsList;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.UiModeUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.androidlib.widget.ZMTipLayer;
import p021us.zoom.template.Template;
import p021us.zoom.videomeetings.C4558R;

public class PListFragment extends ZMTipFragment implements OnClickListener, OnEditorActionListener, KeyboardListener, CallBack {
    public static final String ARG_ANCHOR_ID = "anchorId";
    public static final int DISPLAY_SEARCH_BAR_NUM = 7;
    public static final int REQUEST_CHAT = 1000;
    public static final int REQUEST_INVITE_BUDDIES = 1001;
    public static final int REQUEST_INVITE_BY_PHONE = 1002;
    public static final int REQUEST_INVITE_ROOM_SYSTEM = 1003;
    private static final String TAG = "PListFragment";
    public static final int USERID_EVEROYONE = 0;
    private boolean isWebinar = false;
    private int mAnchorId = 0;
    private IAttentionTrackEventSinkUIListener mAttentionTrackEventSinkUIListener;
    private boolean mBKeyboardOpen = false;
    private Button mBtnBack;
    private Button mBtnClearSearchView;
    private Button mBtnInvite;
    private Button mBtnMuteAll;
    /* access modifiers changed from: private */
    public Button mBtnUnmuteAll;
    private IConfUIListener mConfUIListener;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public EditText mEdtSearch;
    private NonVerbalFeedbackActionView mFeedbackActionView;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public FrameLayout mListContainer;
    @NonNull
    private NonVerbalFBListener mNonVerbalFBListener = new NonVerbalFBListener() {
        public void onSetFeedback(int i) {
            CmmFeedbackMgr feedbackMgr = ConfMgr.getInstance().getFeedbackMgr();
            if (feedbackMgr != null) {
                if (i == 1) {
                    ConfLocalHelper.performRaiseOrLowerHandAction((ZMActivity) PListFragment.this.getActivity(), null);
                } else {
                    feedbackMgr.changeMyFeedback(i);
                }
            }
        }

        public void onClearFeedback() {
            CmmFeedbackMgr feedbackMgr = ConfMgr.getInstance().getFeedbackMgr();
            if (feedbackMgr != null) {
                feedbackMgr.changeMyFeedback(0);
            }
        }
    };
    /* access modifiers changed from: private */
    public PListView mPListView;
    private View mPanelActions;
    private View mPanelFeedback;
    private View mPanelSearchBar;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    /* access modifiers changed from: private */
    public PromoteOrDowngradeMockFragment mPromoteOrDowngradeMockFragment;
    private IZoomQAUIListener mQAUIListener;
    private Runnable mRefreshRunnable = new Runnable() {
        public void run() {
            PListFragment.this.refreshNow();
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = PListFragment.this.mEdtSearch.getText().toString();
            PListFragment.this.mPListView.filter(obj);
            if ((obj.length() <= 0 || PListFragment.this.mPListView.getCount() <= 0) && PListFragment.this.mPanelTitleBar.getVisibility() != 0) {
                PListFragment.this.mListContainer.setForeground(PListFragment.this.mDimmedForground);
            } else {
                PListFragment.this.mListContainer.setForeground(null);
            }
        }
    };
    private SimpleZoomShareUIListener mShareUIListener;
    /* access modifiers changed from: private */
    public ZMTipLayer mTipLayer;
    private TextView mTxtTitle;
    /* access modifiers changed from: private */
    @Nullable
    public ZMAlertDialog mUnlockToInviteDialog;
    /* access modifiers changed from: private */
    @NonNull
    public ZMConfPListUserEventPolicy mZMConfPListUserEventPolicy = new ZMConfPListUserEventPolicy();
    /* access modifiers changed from: private */
    public long mlShowInviteOnMeetingUnlockedTriggerTime = 0;

    public static void show(@NonNull FragmentManager fragmentManager, int i) {
        PListFragment pListFragment = getPListFragment(fragmentManager);
        if (pListFragment == null) {
            PListFragment pListFragment2 = new PListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("anchorId", i);
            pListFragment2.setArguments(bundle);
            pListFragment2.show(fragmentManager, PListFragment.class.getName());
            return;
        }
        pListFragment.setTipVisible(true);
    }

    @Nullable
    public static PListFragment getPListFragment(@NonNull FragmentManager fragmentManager) {
        return (PListFragment) fragmentManager.findFragmentByTag(PListFragment.class.getName());
    }

    public static boolean hide(@NonNull FragmentManager fragmentManager) {
        PListFragment pListFragment = getPListFragment(fragmentManager);
        if (pListFragment != null) {
            if (!pListFragment.getShowsTip()) {
                pListFragment.dismiss();
                return true;
            } else if (pListFragment.isTipVisible()) {
                pListFragment.setTipVisible(false);
                return true;
            }
        }
        return false;
    }

    public static boolean dismiss(@NonNull FragmentManager fragmentManager) {
        PListFragment pListFragment = getPListFragment(fragmentManager);
        if (pListFragment == null) {
            return false;
        }
        pListFragment.dismiss();
        return true;
    }

    public void onResume() {
        super.onResume();
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onConfStatusChanged2(int i, long j) {
                    return PListFragment.this.onConfStatusChanged2(i, j);
                }

                public boolean onUserStatusChanged(int i, long j, int i2) {
                    return PListFragment.this.onUserStatusChanged(i, j);
                }

                public boolean onUserEvent(int i, long j, int i2) {
                    return PListFragment.this.onUserEvent(i, j, i2);
                }

                public void onLeavingSilentModeStatusChanged(long j, boolean z) {
                    if (z) {
                        PListFragment.this.mZMConfPListUserEventPolicy.onReceiveUserEvent(-11, j);
                    }
                }

                public boolean onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
                    return PListFragment.this.onChatMessageReceived(str, j, str2, j2, str3, str4, j3);
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        if (this.mAttentionTrackEventSinkUIListener == null) {
            this.mAttentionTrackEventSinkUIListener = new SimpleAttentionTrackEventSinkUIListener() {
                public void OnUserAttentionStatusChanged(int i, boolean z) {
                    PListFragment.this.OnUserAttentionStatusChanged(i);
                }

                public void OnConfAttentionTrackStatusChanged(boolean z) {
                    PListFragment.this.refresh(false);
                }
            };
        }
        AttentionTrackEventSinkUI.getInstance().addListener(this.mAttentionTrackEventSinkUIListener);
        if (this.mQAUIListener == null) {
            this.mQAUIListener = new SimpleZoomQAUIListener() {
                public void onWebinarAttendeeRaisedHand(long j) {
                    PListFragment.this.attendeeRaiseOrLowerHand(j);
                }

                public void onWebinarAttendeeLowerHand(long j) {
                    PListFragment.this.attendeeRaiseOrLowerHand(j);
                }

                public void onWebinarAttendeeGuestStatusChanged(long j, boolean z) {
                    PListFragment.this.onWebinarAttendeeGuestStatusChanged(j);
                }
            };
        }
        ZoomQAUI.getInstance().addListener(this.mQAUIListener);
        if (this.mShareUIListener == null) {
            this.mShareUIListener = new SimpleZoomShareUIListener() {
                public void OnStartViewPureComputerAudio(long j) {
                    PListFragment.this.refresh(false);
                }

                public void OnStopViewPureComputerAudio(long j) {
                    PListFragment.this.refresh(false);
                }
            };
        }
        ZoomShareUI.getInstance().addListener(this.mShareUIListener);
        refresh(true);
        refreshFeedback();
        this.mZMConfPListUserEventPolicy.start();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mFeedbackActionView.setListener(null);
        this.mHandler.removeCallbacksAndMessages(null);
        this.mZMConfPListUserEventPolicy.end();
        ConfUI.getInstance().removeListener(this.mConfUIListener);
        ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
        ZoomShareUI.getInstance().removeListener(this.mShareUIListener);
        AttentionTrackEventSinkUI.getInstance().removeListener(this.mAttentionTrackEventSinkUIListener);
    }

    public void onDestroy() {
        super.onDestroy();
        FragmentActivity activity = getActivity();
        if (activity != null && activity.isFinishing()) {
            activity.finishActivity(1001);
            activity.finishActivity(1002);
            activity.finishActivity(1003);
        }
    }

    public void promoteOrDowngrade(@NonNull PromoteOrDowngradeItem promoteOrDowngradeItem) {
        PromoteOrDowngradeMockFragment promoteOrDowngradeMockFragment = this.mPromoteOrDowngradeMockFragment;
        if (promoteOrDowngradeMockFragment != null) {
            promoteOrDowngradeMockFragment.promoteOrDowngrade(promoteOrDowngradeItem);
        }
    }

    public void updateAttendeeCount() {
        this.mPListView.updateAttendeeCount();
    }

    public void refresh(boolean z) {
        int clientUserCount = ConfMgr.getInstance().getClientUserCount();
        if (z || clientUserCount < ZMConfiguration.MAX_PLIST_REFRESH_NOW_USER_COUNT) {
            refreshNow();
            return;
        }
        this.mHandler.removeCallbacks(this.mRefreshRunnable);
        this.mHandler.postDelayed(this.mRefreshRunnable, (long) (clientUserCount / 10));
    }

    /* access modifiers changed from: private */
    public void refreshNow() {
        this.mPListView.reloadAllItems(true);
        updateTitle();
        updateActionButtons();
        updateBtnClearSearchView();
        updateAttendeeCount();
    }

    /* access modifiers changed from: private */
    @SuppressLint({"StringFormatMatches"})
    public void updateTitle() {
        String str;
        int i;
        if (getContext() != null) {
            if (this.isWebinar) {
                int viewOnlyUserCount = ConfMgr.getInstance().getViewOnlyUserCount();
                int clientUserCount = ConfMgr.getInstance().getClientUserCount();
                str = getString(C4558R.string.zm_title_plist, Integer.valueOf(clientUserCount + viewOnlyUserCount));
            } else {
                if (ConfLocalHelper.canControlWaitingRoom()) {
                    i = ConfMgr.getInstance().getClientUserCount();
                } else {
                    i = ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true);
                }
                str = getString(C4558R.string.zm_title_plist, Integer.valueOf(i));
            }
            this.mTxtTitle.setText(str);
        }
    }

    public void onPause() {
        super.onPause();
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity == null || (!UiModeUtil.isInDesktopMode(zMActivity) && !zMActivity.isInMultiWindowMode())) {
            ConfUI.getInstance().removeListener(this.mConfUIListener);
            ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
            ZoomShareUI.getInstance().removeListener(this.mShareUIListener);
            AttentionTrackEventSinkUI.getInstance().removeListener(this.mAttentionTrackEventSinkUIListener);
        }
    }

    public void onStop() {
        super.onStop();
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity == null) {
            return;
        }
        if (UiModeUtil.isInDesktopMode(zMActivity) || zMActivity.isInMultiWindowMode()) {
            ConfUI.getInstance().removeListener(this.mConfUIListener);
            ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
            ZoomShareUI.getInstance().removeListener(this.mShareUIListener);
            AttentionTrackEventSinkUI.getInstance().removeListener(this.mAttentionTrackEventSinkUIListener);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isTipVisible", isTipVisible());
        PromoteOrDowngradeMockFragment promoteOrDowngradeMockFragment = this.mPromoteOrDowngradeMockFragment;
        if (promoteOrDowngradeMockFragment != null) {
            promoteOrDowngradeMockFragment.onSaveInstanceState(bundle);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ZMConfiguration.initConstantByDeviceRank();
        View inflate = layoutInflater.inflate(C4558R.layout.zm_plist_screen, viewGroup, false);
        this.mPromoteOrDowngradeMockFragment = new PromoteOrDowngradeMockFragment(this);
        this.mPromoteOrDowngradeMockFragment.onCreateView(bundle);
        this.mPListView = (PListView) inflate.findViewById(C4558R.C4560id.plistView);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mTipLayer = (ZMTipLayer) inflate.findViewById(C4558R.C4560id.tipLayer);
        this.mBtnMuteAll = (Button) inflate.findViewById(C4558R.C4560id.btnMuteAll);
        this.mBtnUnmuteAll = (Button) inflate.findViewById(C4558R.C4560id.btnUnmuteAll);
        this.mBtnInvite = (Button) inflate.findViewById(C4558R.C4560id.btnInvite);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mBtnClearSearchView = (Button) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mPanelSearchBar = inflate.findViewById(C4558R.C4560id.panelSearchBar);
        ZMKeyboardDetector zMKeyboardDetector = (ZMKeyboardDetector) inflate.findViewById(C4558R.C4560id.keyboardDetector);
        this.mPanelActions = inflate.findViewById(C4558R.C4560id.panelActions);
        initFeedBackView(inflate);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnMuteAll.setOnClickListener(this);
        this.mBtnUnmuteAll.setOnClickListener(this);
        this.mBtnInvite.setOnClickListener(this);
        ZMTipLayer zMTipLayer = this.mTipLayer;
        if (zMTipLayer != null) {
            zMTipLayer.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return PListFragment.this.mTipLayer.dismissAllTips();
                }
            });
        }
        if (UIMgr.isLargeMode(getActivity())) {
            this.mBtnBack.setVisibility(8);
        }
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                PListFragment.this.mHandler.removeCallbacks(PListFragment.this.mRunnableFilter);
                PListFragment.this.mHandler.postDelayed(PListFragment.this.mRunnableFilter, (long) ZMConfiguration.INTERVAL_PLIST_FILTER);
                PListFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        zMKeyboardDetector.setKeyboardListener(this);
        updateActionButtons();
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            this.isWebinar = confContext.isWebinar();
        }
        this.mZMConfPListUserEventPolicy.setmCallBack(this);
        return inflate;
    }

    @Nullable
    public ZMTip onCreateTip(@NonNull Context context, LayoutInflater layoutInflater, @Nullable Bundle bundle) {
        View view = getView();
        if (view == null) {
            return null;
        }
        int dip2px = UIUtil.dip2px(context, 400.0f);
        if (UIUtil.getDisplayWidth(context) < dip2px) {
            dip2px = UIUtil.getDisplayWidth(context);
        }
        view.setLayoutParams(new LayoutParams(dip2px, -2));
        ZMTip zMTip = new ZMTip(context);
        zMTip.setBackgroundColor(-263173);
        zMTip.setArrowSize(UIUtil.dip2px(context, 30.0f), UIUtil.dip2px(context, 11.0f));
        int i = 0;
        zMTip.setCornerArcSize(0);
        zMTip.addView(view);
        Bundle arguments = getArguments();
        if (arguments == null) {
            return null;
        }
        this.mAnchorId = arguments.getInt("anchorId", 0);
        if (this.mAnchorId > 0) {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return null;
            }
            View findViewById = activity.findViewById(this.mAnchorId);
            if (findViewById != null) {
                zMTip.setAnchor(findViewById, 1);
            }
        }
        if (bundle != null) {
            if (!bundle.getBoolean("isTipVisible", true)) {
                i = 4;
            }
            zMTip.setVisibility(i);
        }
        return zMTip;
    }

    private void initFeedBackView(@NonNull View view) {
        this.mPanelFeedback = view.findViewById(C4558R.C4560id.panelFeedback);
        this.mFeedbackActionView = (NonVerbalFeedbackActionView) view.findViewById(C4558R.C4560id.viewFeedback);
        this.mFeedbackActionView.setListener(this.mNonVerbalFBListener);
    }

    private void refreshFeedback() {
        if (ConfLocalHelper.isHostCoHostBOModerator()) {
            this.mPanelFeedback.setVisibility(8);
        } else {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                if (confContext.isFeedbackEnable()) {
                    CmmUser myself = ConfMgr.getInstance().getMyself();
                    if (myself != null) {
                        this.mPanelFeedback.setVisibility(0);
                        this.mFeedbackActionView.setFeedbackFocus(myself.getFeedback());
                    }
                } else {
                    this.mPanelFeedback.setVisibility(8);
                }
            }
        }
    }

    private boolean isInviteDisabled() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return false;
        }
        ParamsList appContextParams = confContext.getAppContextParams();
        if (appContextParams == null) {
            return false;
        }
        return appContextParams.getBoolean(ConfParams.CONF_PARAM_NO_INVITE, false);
    }

    /* access modifiers changed from: private */
    public boolean onConfStatusChanged2(int i, final long j) {
        if (i == 3) {
            EventTaskManager eventTaskManager = getEventTaskManager();
            if (eventTaskManager != null) {
                eventTaskManager.pushLater("onConfLockStatusChanged", new EventAction("onConfLockStatusChanged") {
                    public void run(IUIElement iUIElement) {
                        PListFragment pListFragment = (PListFragment) iUIElement;
                        if (pListFragment != null) {
                            pListFragment.onConfLockStatusChanged();
                        }
                    }
                });
            }
        } else if (i == 97) {
            updateTitle();
            updateAttendeeCount();
        } else if (i == 79) {
            this.mPListView.reloadAllItems(false);
            updateActionButtons();
        } else if (i == 28) {
            onAllowAttendeeChatStatusChange();
        } else if (i == 110) {
            EventTaskManager eventTaskManager2 = getEventTaskManager();
            if (eventTaskManager2 != null) {
                eventTaskManager2.pushLater("onPromotePanelistResult", new EventAction("onPromotePanelistResult") {
                    public void run(IUIElement iUIElement) {
                        if (PListFragment.this.mPromoteOrDowngradeMockFragment != null) {
                            PListFragment.this.mPromoteOrDowngradeMockFragment.onPromotePanelistResult((int) j);
                            if (j == 0) {
                                PListFragment pListFragment = (PListFragment) iUIElement;
                                if (pListFragment != null) {
                                    pListFragment.onRemoveItem(PListFragment.this.mPromoteOrDowngradeMockFragment.getCurUserId());
                                }
                            }
                        }
                    }
                });
            }
        } else if (i == 111) {
            EventTaskManager eventTaskManager3 = getEventTaskManager();
            if (eventTaskManager3 != null) {
                eventTaskManager3.push("onDePromotePanelist", new EventAction("onDePromotePanelist") {
                    public void run(IUIElement iUIElement) {
                        if (PListFragment.this.mPromoteOrDowngradeMockFragment != null) {
                            PListFragment.this.mPromoteOrDowngradeMockFragment.onDePromotePanelist((int) j);
                            if (j == 0) {
                                PListFragment pListFragment = (PListFragment) iUIElement;
                                if (pListFragment != null) {
                                    pListFragment.onRemoveItem(PListFragment.this.mPromoteOrDowngradeMockFragment.getCurUserId());
                                }
                            }
                        }
                    }
                });
            }
        } else if (i == 103) {
            updateAttendeeCount();
            refreshFeedback();
        } else if (i == 141) {
            PListView pListView = this.mPListView;
            if (pListView != null) {
                pListView.onConfAdmitAllSilentUsersStatusChanged();
            }
        } else if (i == 139) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                PListItemActionDialog.refreshAction(fragmentManager);
                if (!ConfLocalHelper.isAllowParticipantRename()) {
                    ChangeScreenNameDialog changeScreenNameDialog = (ChangeScreenNameDialog) fragmentManager.findFragmentByTag(ChangeScreenNameDialog.class.getName());
                    if (changeScreenNameDialog != null) {
                        changeScreenNameDialog.dismiss();
                    }
                }
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onConfLockStatusChanged() {
        updateActionButtons();
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (!(this.mPromoteOrDowngradeMockFragment == null || confStatusObj == null || confStatusObj.isConfLocked())) {
            this.mPromoteOrDowngradeMockFragment.onConfLockStatusChanged();
        }
        if (Math.abs(System.currentTimeMillis() - this.mlShowInviteOnMeetingUnlockedTriggerTime) < 5000 && confStatusObj != null && !confStatusObj.isConfLocked()) {
            onClickBtnInvite();
        }
    }

    private void onAllowAttendeeChatStatusChange() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null && ConfMgr.getInstance().getMyself() != null) {
            PListItemActionDialog.refreshAction(fragmentManager);
        }
    }

    /* access modifiers changed from: private */
    public void OnUserAttentionStatusChanged(int i) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (this.mPListView != null && myself != null) {
            if (myself.isHost() || myself.isCoHost() || myself.isBOModerator()) {
                this.mZMConfPListUserEventPolicy.onReceiveUserEvent(-10, (long) i);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean onUserStatusChanged(int i, long j) {
        switch (i) {
            case 1:
            case 25:
            case 44:
                processOnHostOrCoHostChanged(j);
                break;
            case 9:
            case 21:
            case 46:
                this.mZMConfPListUserEventPolicy.onReceiveUserEvent(2, j);
                break;
            case 12:
            case 18:
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    AccessibilityManager accessibilityManager = (AccessibilityManager) activity.getSystemService("accessibility");
                    if (accessibilityManager == null || !accessibilityManager.isEnabled()) {
                        this.mZMConfPListUserEventPolicy.onReceiveUserEvent(2, j);
                        break;
                    }
                }
                break;
            case 26:
                ccPrivilegeChange(j);
                break;
            case 28:
            case 29:
                onTalkPrivilegeChange(j);
                break;
            case 36:
            case 37:
            case 40:
                onFeedbackChanged(j);
                break;
            default:
                if (!(i == 10 || i == 13 || i == 16 || i == 17 || i == 19 || i == 55)) {
                    this.mZMConfPListUserEventPolicy.onReceiveUserEvent(-10, j);
                    break;
                }
        }
        return true;
    }

    public void processOnHostOrCoHostChanged(long j) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        boolean z = true;
        boolean z2 = myself != null && myself.isHost();
        if (myself == null || !myself.isCoHost()) {
            z = false;
        }
        updateTitle();
        updateActionButtons();
        if (!z2 && !z) {
            ZMAlertDialog zMAlertDialog = this.mUnlockToInviteDialog;
            if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
                this.mUnlockToInviteDialog.cancel();
            }
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null && !ConfLocalHelper.isAllowParticipantRename()) {
            ChangeScreenNameDialog changeScreenNameDialog = (ChangeScreenNameDialog) fragmentManager.findFragmentByTag(ChangeScreenNameDialog.class.getName());
            if (changeScreenNameDialog != null) {
                changeScreenNameDialog.dismiss();
            }
        }
        this.mPListView.onHostCohostChanged(j);
        refreshFeedback();
    }

    private void onTalkPrivilegeChange(long j) {
        this.mPListView.onTalkPrivilegeChange(j);
    }

    /* access modifiers changed from: private */
    public boolean onUserEvent(int i, long j, int i2) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i3 = i;
            final long j2 = j;
            final int i4 = i2;
            C288413 r1 = new EventAction() {
                public void run(IUIElement iUIElement) {
                    PListFragment pListFragment = (PListFragment) iUIElement;
                    if (pListFragment != null) {
                        pListFragment.handleOnUserEvent(i3, j2, i4);
                    }
                }
            };
            eventTaskManager.push(r1);
        }
        return true;
    }

    private void onFeedbackChanged(long j) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isSameUser(j, myself.getNodeId())) {
                this.mFeedbackActionView.setFeedbackFocus(myself.getFeedback());
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleOnUserEvent(int i, long j, int i2) {
        switch (i) {
            case 0:
                this.mZMConfPListUserEventPolicy.onReceiveUserEvent(0, j);
                break;
            case 1:
                this.mZMConfPListUserEventPolicy.onReceiveUserEvent(1, j);
                break;
            case 2:
                this.mZMConfPListUserEventPolicy.onReceiveUserEvent(2, j);
                break;
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                PListFragment.this.mPListView.requestLayout();
                PListFragment.this.updateTitle();
            }
        });
        if (this.mPListView.getCount() >= 7) {
            this.mPanelSearchBar.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public boolean onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
        if (!this.isWebinar) {
            this.mZMConfPListUserEventPolicy.onReceiveUserEvent(-10, j);
            this.mZMConfPListUserEventPolicy.onReceiveUserEvent(-10, j2);
        }
        return false;
    }

    private void updateActionButtons() {
        boolean z;
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (ConfMgr.getInstance().getConfContext() != null) {
            int i = 8;
            if (myself == null || (!myself.isHost() && !myself.isCoHost() && !myself.isBOModerator())) {
                this.mBtnMuteAll.setVisibility(8);
                this.mBtnUnmuteAll.setVisibility(8);
                z = true;
            } else {
                this.mBtnMuteAll.setVisibility(0);
                this.mBtnUnmuteAll.setVisibility(0);
                z = false;
            }
            BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
            if (isInviteDisabled() || (bOMgr != null && bOMgr.isInBOMeeting())) {
                this.mBtnInvite.setVisibility(8);
            } else {
                this.mBtnInvite.setVisibility(0);
                z = false;
            }
            View view = this.mPanelActions;
            if (!z) {
                i = 0;
            }
            view.setVisibility(i);
        }
    }

    /* access modifiers changed from: private */
    public void attendeeRaiseOrLowerHand(long j) {
        this.mZMConfPListUserEventPolicy.onReceiveUserEvent(2, j);
    }

    /* access modifiers changed from: private */
    public void onWebinarAttendeeGuestStatusChanged(long j) {
        this.mZMConfPListUserEventPolicy.onReceiveUserEvent(2, j);
    }

    private void ccPrivilegeChange(long j) {
        this.mPListView.ccPrivilegeChange(j);
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    public boolean onSearchRequested() {
        if (getView() == null) {
            return true;
        }
        this.mEdtSearch.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSearch);
        this.mPanelTitleBar.setVisibility(8);
        this.mPanelSearchBar.setVisibility(0);
        this.mPListView.setInSearchProgress(true);
        this.mListContainer.setForeground(this.mDimmedForground);
        this.mEdtSearch.requestFocus();
        return true;
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void onKeyboardOpen() {
        this.mBKeyboardOpen = true;
    }

    public void onKeyboardClosed() {
        if (this.mEdtSearch != null) {
            this.mBKeyboardOpen = false;
            if (this.mPListView.getCount() == 0 || this.mEdtSearch.getText().length() == 0) {
                this.mEdtSearch.setText("");
                this.mPanelTitleBar.setVisibility(0);
                this.mPanelSearchBar.setVisibility(4);
                this.mPListView.setInSearchProgress(false);
            }
            this.mListContainer.setForeground(null);
            this.mPListView.post(new Runnable() {
                public void run() {
                    PListFragment.this.mPListView.requestLayout();
                }
            });
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnMuteAll) {
            onClickBtnMuteAll();
        } else if (id == C4558R.C4560id.btnUnmuteAll) {
            onClickBtnUnmuteAll();
        } else if (id == C4558R.C4560id.btnInvite) {
            onClickBtnInvite();
        } else if (id == C4558R.C4560id.btnClearSearchView) {
            onClickBtnClearSearchView();
        }
    }

    private void onClickBtnBack() {
        if (getShowsTip()) {
            setTipVisible(false);
        } else {
            dismiss();
        }
    }

    private void onClickBtnMuteAll() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            View inflate = LayoutInflater.from(zMActivity).inflate(C4558R.layout.zm_mute_all_confirm, null, false);
            final CheckBox checkBox = (CheckBox) inflate.findViewById(C4558R.C4560id.check);
            checkBox.setChecked(!ConfMgr.getInstance().disabledAttendeeUnmuteSelf());
            new Builder(zMActivity).setTitle(C4558R.string.zm_msg_mute_all_confirm).setView(inflate).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (ConfMgr.getInstance().handleUserCmd(49, 0) && AccessibilityUtil.isSpokenFeedbackEnabled(PListFragment.this.getContext())) {
                        AccessibilityUtil.announceForAccessibilityCompat((View) PListFragment.this.mBtnUnmuteAll, C4558R.string.zm_accessibility_muted_all_23049);
                    }
                    if (checkBox.isChecked()) {
                        ConfMgr.getInstance().handleConfCmd(88);
                    } else {
                        ConfMgr.getInstance().handleConfCmd(89);
                    }
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).setCancelable(true).create().show();
        }
    }

    private void onClickBtnUnmuteAll() {
        if (ConfMgr.getInstance().handleUserCmd(50, 0) && AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            AccessibilityUtil.announceForAccessibilityCompat((View) this.mBtnUnmuteAll, C4558R.string.zm_accessibility_unmuted_all_23049);
        }
    }

    private void onClickBtnInvite() {
        String str;
        String str2;
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj == null || !confStatusObj.isConfLocked()) {
                MeetingInfoProto meetingItem = confContext.getMeetingItem();
                if (meetingItem != null) {
                    String joinMeetingUrl = meetingItem.getJoinMeetingUrl();
                    long meetingNumber = meetingItem.getMeetingNumber();
                    String password = meetingItem.getPassword();
                    String rawMeetingPassword = confContext.getRawMeetingPassword();
                    HashMap hashMap = new HashMap();
                    hashMap.put("joinMeetingUrl", joinMeetingUrl);
                    hashMap.put(InviteFragment.ARG_MEETING_ID, String.valueOf(meetingNumber));
                    String format = new Template(getString(C4558R.string.zm_msg_sms_invite_in_meeting)).format(hashMap);
                    String inviteEmailSubject = meetingItem.getInviteEmailSubject();
                    String inviteEmailContent = meetingItem.getInviteEmailContent();
                    String str3 = "";
                    CmmUser myself = ConfMgr.getInstance().getMyself();
                    String screenName = myself != null ? myself.getScreenName() : str3;
                    try {
                        InviteContentGenerator inviteContentGenerator = (InviteContentGenerator) Class.forName(ResourcesUtil.getString((Context) getActivity(), C4558R.string.zm_config_invite_content_generator)).newInstance();
                        String genEmailTopic = inviteContentGenerator.genEmailTopic(VideoBoxApplication.getInstance(), meetingNumber, joinMeetingUrl, screenName, password, rawMeetingPassword);
                        if (!StringUtil.isEmptyOrNull(genEmailTopic)) {
                            inviteEmailSubject = genEmailTopic;
                        }
                        String genEmailContent = inviteContentGenerator.genEmailContent(VideoBoxApplication.getInstance(), meetingNumber, joinMeetingUrl, screenName, password, rawMeetingPassword);
                        if (!StringUtil.isEmptyOrNull(genEmailContent)) {
                            inviteEmailContent = genEmailContent;
                        }
                        String genSmsContent = inviteContentGenerator.genSmsContent(VideoBoxApplication.getInstance(), meetingNumber, joinMeetingUrl, screenName, password, rawMeetingPassword);
                        if (!StringUtil.isEmptyOrNull(genSmsContent)) {
                            format = genSmsContent;
                        }
                        str2 = inviteEmailContent;
                        str = format;
                    } catch (Exception unused) {
                        str2 = inviteEmailContent;
                        str = format;
                    }
                    InviteViaDialogFragment.show(getFragmentManager(), StringUtil.isEmptyOrNull(inviteEmailSubject) ? getResources().getString(C4558R.string.zm_title_invite_email_topic) : inviteEmailSubject, str2, str, joinMeetingUrl, meetingNumber, password, rawMeetingPassword, 1001, 1002, 1003);
                    ZMConfEventTracking.eventTrackParticipantsPanelInviteToMeeting();
                    return;
                }
                return;
            }
            showCannotInviteForMeetingLocked();
        }
    }

    private void onClickClearMyFeedback() {
        CmmFeedbackMgr feedbackMgr = ConfMgr.getInstance().getFeedbackMgr();
        if (feedbackMgr != null) {
            feedbackMgr.changeMyFeedback(0);
        }
    }

    private void showCannotInviteForMeetingLocked() {
        boolean z = true;
        Builder cancelable = new Builder(getActivity()).setTitle(C4558R.string.zm_msg_cannot_invite_for_meeting_is_locked).setCancelable(true);
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null || (!myself.isHost() && !myself.isCoHost())) {
            cancelable.setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            z = false;
        } else {
            cancelable.setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_mi_unlock_meeting, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    PListFragment.this.mlShowInviteOnMeetingUnlockedTriggerTime = System.currentTimeMillis();
                    ConfMgr.getInstance().handleConfCmd(59);
                }
            });
        }
        ZMAlertDialog create = cancelable.create();
        create.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                PListFragment.this.mUnlockToInviteDialog = null;
            }
        });
        create.show();
        if (z) {
            this.mUnlockToInviteDialog = create;
        }
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
        if (!this.mBKeyboardOpen) {
            this.mPanelTitleBar.setVisibility(0);
            this.mPanelSearchBar.setVisibility(4);
            this.mPListView.setInSearchProgress(false);
            this.mListContainer.setForeground(null);
        }
    }

    public void dismiss() {
        if (getShowsTip()) {
            super.dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private boolean isTipVisible() {
        ZMTip tip = getTip();
        boolean z = false;
        if (tip == null) {
            return false;
        }
        if (tip.getVisibility() == 0) {
            z = true;
        }
        return z;
    }

    private void setTipVisible(boolean z) {
        ZMTip tip = getTip();
        if (tip != null) {
            int i = 0;
            if ((tip.getVisibility() == 0) != z) {
                if (!z) {
                    i = 4;
                }
                tip.setVisibility(i);
                if (z) {
                    tip.startAnimation(AnimationUtils.loadAnimation(getActivity(), C4558R.anim.zm_tip_fadein));
                } else {
                    ConfActivity confActivity = (ConfActivity) getActivity();
                    if (confActivity != null) {
                        confActivity.onPListTipClosed();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onRemoveItem(long j) {
        if (j >= 0) {
            this.mPListView.onPromoteOrDowngrade(j);
            this.mPListView.updateAttendeeCount();
        }
    }

    public void onRefreshAll(boolean z) {
        refresh(z);
    }

    public void onSmallBatchUsers(int i, @Nullable Collection<String> collection) {
        if (i == 0) {
            PListView pListView = this.mPListView;
            if (pListView != null) {
                pListView.onUserJoin(collection, 0);
            }
        } else if (i == 2) {
            updateActionButtons();
            PListView pListView2 = this.mPListView;
            if (pListView2 != null) {
                pListView2.updateUser(collection, 2);
            }
        } else if (i == -10) {
            PListView pListView3 = this.mPListView;
            if (pListView3 != null) {
                pListView3.updateUserAndNotReSort(collection, 2);
            }
        } else if (i == -11) {
            PListView pListView4 = this.mPListView;
            if (pListView4 != null) {
                pListView4.onLeavingSilentModeStatusChanged(collection, 2);
            }
        } else if (i == 1) {
            PListView pListView5 = this.mPListView;
            if (pListView5 != null) {
                pListView5.onUserLeave(collection);
            }
        }
    }

    public void onPerformExtraActionForUsers(int i, @Nullable Collection<String> collection) {
        if (i != 0) {
            if (i == 2) {
                updateActionButtons();
                PListView pListView = this.mPListView;
                if (pListView != null) {
                    pListView.updateActionDialog(collection);
                }
            } else if (i == 1) {
                PListView pListView2 = this.mPListView;
                if (pListView2 != null) {
                    pListView2.checkUserLeavesForActionDialog(collection);
                }
            }
        }
    }
}

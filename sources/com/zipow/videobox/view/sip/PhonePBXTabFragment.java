package com.zipow.videobox.view.sip;

import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.eventbus.ZMReturnToCallEvent;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmPBXCallHistoryManager;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI.IPBXMessageEventSinkUIListener;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI.SimpleIPBXMessageEventSinkUIListener;
import com.zipow.videobox.sip.server.ISIPCallRepositoryEventSinkListenerUI.SimpleISIPCallRepositoryEventSinkListener;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.PBXLoginConflictListenerUI.SimplePBXLoginConflictListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.p014mm.MMConnectAlertView;
import com.zipow.videobox.view.sip.ListCoverView.ExpandListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import org.greenrobot.eventbus.EventBus;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkStatusReceiver.SimpleNetworkStatusListener;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMViewPager;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXTabFragment extends ZMDialogFragment implements OnClickListener, com.zipow.videobox.view.IMView.OnFragmentShowListener, Observer {
    public static final int REQUEST_PERMISSION_MIC = 11;
    private static final String TAG = "PhonePBXTabFragment";
    public static final String TAG_RELOAD_USER_CONFIG = "reload_user_config";
    /* access modifiers changed from: private */
    public View mBtnBackToCall;
    private Button mBtnDoneInSelect;
    private View mContentContainerView;
    private PhonePBXListCoverView mCoverView;
    private TextView mDismiss;
    private Handler mHandler = new Handler();
    private boolean mHasShow = false;
    private ImageView mImgDeleteInSelect;
    private boolean mIsInSelectMode = false;
    private TextView mLearnMore;
    private SimpleISIPLineMgrEventSinkListener mLineEventSinkListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
            super.OnRegisterResult(str, cmmSIPCallRegResult);
            PhonePBXTabFragment.this.updateErrorMessage();
        }
    };
    @NonNull
    private SimplePBXLoginConflictListener mLoginConflictListener = new SimplePBXLoginConflictListener() {
        public void onConflict() {
            super.onConflict();
            PhonePBXTabFragment.this.exitSelectMode();
            PhonePBXTabFragment.this.dismissCoverView();
        }
    };
    private MMConnectAlertView mMMConnectAlertView;
    private IPBXMessageEventSinkUIListener mMessageEventSinkUIListener = new SimpleIPBXMessageEventSinkUIListener() {
        public void OnTotalUnreadCountChanged() {
            super.OnTotalUnreadCountChanged();
            PhonePBXTabFragment.this.updateTxtSmsBubble();
        }
    };
    @NonNull
    private SimpleNetworkStatusListener mNetworkStatusListener = new SimpleNetworkStatusListener() {
        public void networkStatusChanged(boolean z, int i, String str, boolean z2, int i2, String str2) {
            super.networkStatusChanged(z, i, str, z2, i2, str2);
            PhonePBXTabFragment.this.updateErrorMessage();
        }
    };
    /* access modifiers changed from: private */
    public Map<String, String> mNumberNameCache = new HashMap();
    @Nullable
    SimpleISIPCallRepositoryEventSinkListener mPBXRepositoryListener = new SimpleISIPCallRepositoryEventSinkListener() {
        public void OnTotalUnreadVoiceMailCountChanged(int i) {
            super.OnTotalUnreadVoiceMailCountChanged(i);
            PhonePBXTabFragment.this.updateTxtVoiceMailBubble();
        }

        public void OnMissedCallHistoryChanged(int i) {
            super.OnMissedCallHistoryChanged(i);
            PhonePBXTabFragment.this.updateTxtCallHistoryBubble();
        }

        public void OnBlockPhoneNumerDone(int i, List<String> list) {
            super.OnBlockPhoneNumerDone(i, list);
            if (list != null && !list.isEmpty()) {
                String str = (String) list.get(0);
                if (PhonePBXTabFragment.this.mNumberNameCache == null || PhonePBXTabFragment.this.mNumberNameCache.isEmpty()) {
                    String displayNameByNumber = ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(str);
                    if (!TextUtils.isEmpty(displayNameByNumber)) {
                        PhonePBXTabFragment.this.mNumberNameCache.put(str, displayNameByNumber);
                    } else {
                        return;
                    }
                }
                String str2 = (String) PhonePBXTabFragment.this.mNumberNameCache.get(str);
                if (i == 0) {
                    CmmSIPCallManager.getInstance().showTipsOnUITop(PhonePBXTabFragment.this.getContext().getResources().getString(C4558R.string.zm_sip_block_number_success_125232, new Object[]{str2}));
                } else {
                    CmmSIPCallManager.getInstance().showErrorTipsOnUITop(PhonePBXTabFragment.this.getContext().getResources().getString(C4558R.string.zm_sip_block_number_fail_125232, new Object[]{str2}));
                }
            }
        }
    };
    private PhonePBXPagerAdapter mPagerAdapter;
    private View mPanel911;
    private View mPanelBottomInSelect;
    /* access modifiers changed from: private */
    public View mPanelTabCallHistory;
    private View mPanelTabSMS;
    private View mPanelTabSharedLine;
    private View mPanelTabVoiceMail;
    private View mPanelTopInSelect;
    private ZMViewPager mPbxViewPager;
    private Runnable mQueryUserPbxInfoRunnable = new Runnable() {
        public void run() {
            CmmSIPCallManager.getInstance().queryUserPbxInfo();
        }
    };
    @NonNull
    SimpleSIPCallEventListener mSIPCallListener = new SimpleSIPCallEventListener() {
        public void OnSIPCallServiceStoped(boolean z) {
            super.OnSIPCallServiceStoped(z);
            PhonePBXTabFragment.this.dismissCoverView();
        }

        public void OnNewCallGenerate(String str, int i) {
            super.OnNewCallGenerate(str, i);
            PhonePBXTabFragment.this.mBtnBackToCall.setVisibility(0);
            PhonePBXTabFragment.this.dismissCoverView();
        }

        public void OnCallTerminate(String str, int i) {
            super.OnCallTerminate(str, i);
            if (!CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
                PhonePBXTabFragment.this.mBtnBackToCall.setVisibility(8);
            }
        }

        public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
            super.OnRequestDoneForQueryPBXUserInfo(z);
            if (z && PhonePBXTabFragment.this.updatePagerAdapter()) {
                PhonePBXTabFragment.this.updateUI();
            }
            PhonePBXTabFragment.this.updateErrorMessage();
        }

        public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
            super.OnPBXFeatureOptionsChanged(list);
            if (list != null && list.size() != 0 && ZMPhoneUtils.isPBXFeatureOptionChanged(list, CmmSIPMessageManager.getInstance().getMessageEnabledBit()) && PhonePBXTabFragment.this.updatePagerAdapter()) {
                PhonePBXTabFragment.this.updateUI();
            }
        }
    };
    @Nullable
    private String mSelectedDisplayName;
    @Nullable
    private String mSelectedPhoneNumber;
    private TextView mTxtCallHistory;
    private TextView mTxtCallHistoryBubble;
    private TextView mTxtLine;
    private TextView mTxtSMS;
    private TextView mTxtSMSBubble;
    private TextView mTxtSelectAllInSelect;
    private TextView mTxtSipUnavailable;
    private TextView mTxtVoiceMail;
    private TextView mTxtVoiceMailBubble;

    public interface DeleteStatus {
        public static final int ADD = 1;
        public static final int DELETE = 0;
        public static final int FULL = 2;
        public static final int ZERO = 3;
    }

    public interface IListCoverListener {
        void onCollapseEnd();

        void onExpandStart();
    }

    public interface IPhonePBX {
        View getListView();

        void onDeleteInSelectMode();

        void onEnterSelectMode();

        void onExitSelectMode();

        boolean setSelectAllMode();
    }

    public interface IPhonePBXAccessbility {
        void accessibilityControl(long j);
    }

    public interface OnFragmentShowListener {
        void onShow();
    }

    public void onKeyboardClosed() {
    }

    public void onKeyboardOpen() {
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_phone_pbx_tab, null);
        this.mPbxViewPager = (ZMViewPager) inflate.findViewById(C4558R.C4560id.pbxViewPager);
        this.mPbxViewPager.setDisableScroll(true);
        this.mPbxViewPager.setOffscreenPageLimit(4);
        this.mPbxViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                PhonePBXTabFragment.this.updateTabbar();
                PhonePBXTabFragment.this.dismissCoverView();
                ZMPhoneUtils.saveIntValue(PreferenceUtil.PBX_FRAGMENT_INDEX, i);
            }
        });
        this.mContentContainerView = inflate.findViewById(C4558R.C4560id.contentContainer);
        this.mMMConnectAlertView = (MMConnectAlertView) inflate.findViewById(C4558R.C4560id.panelConnectionAlert);
        this.mMMConnectAlertView.setActionType(1);
        this.mBtnBackToCall = inflate.findViewById(C4558R.C4560id.btn_back_to_call);
        this.mPanelTabCallHistory = inflate.findViewById(C4558R.C4560id.panelCallHistory);
        this.mTxtCallHistoryBubble = (TextView) inflate.findViewById(C4558R.C4560id.txtCallHistoryBubble);
        this.mTxtCallHistory = (TextView) inflate.findViewById(C4558R.C4560id.txtCallHistory);
        this.mPanelTabVoiceMail = inflate.findViewById(C4558R.C4560id.panelTabVoicemail);
        this.mTxtVoiceMailBubble = (TextView) inflate.findViewById(C4558R.C4560id.txtvoicemailBubble);
        this.mTxtVoiceMail = (TextView) inflate.findViewById(C4558R.C4560id.txtVoicemail);
        this.mPanelTabSharedLine = inflate.findViewById(C4558R.C4560id.panelTabSharedLine);
        this.mTxtLine = (TextView) inflate.findViewById(C4558R.C4560id.txtSharedLine);
        this.mPanelTabSMS = inflate.findViewById(C4558R.C4560id.panelTabSms);
        this.mTxtSMS = (TextView) inflate.findViewById(C4558R.C4560id.txtSms);
        this.mTxtSMSBubble = (TextView) inflate.findViewById(C4558R.C4560id.txtSmsBubble);
        this.mTxtSipUnavailable = (TextView) inflate.findViewById(C4558R.C4560id.txtSipUnavailable);
        this.mPanel911 = inflate.findViewById(C4558R.C4560id.panel911);
        this.mLearnMore = (TextView) inflate.findViewById(C4558R.C4560id.learn_more);
        this.mDismiss = (TextView) inflate.findViewById(C4558R.C4560id.dismiss);
        this.mCoverView = (PhonePBXListCoverView) inflate.findViewById(C4558R.C4560id.cover);
        this.mCoverView.setExpandListener(new ExpandListener() {
            public void onCollapseStart() {
            }

            public void onExpandEnd() {
            }

            public void onExpandStart() {
                Fragment currentFragment = PhonePBXTabFragment.this.getCurrentFragment();
                if (currentFragment instanceof IListCoverListener) {
                    ((IListCoverListener) currentFragment).onExpandStart();
                }
            }

            public void onCollapseEnd() {
                Fragment currentFragment = PhonePBXTabFragment.this.getCurrentFragment();
                if (currentFragment instanceof IListCoverListener) {
                    ((IListCoverListener) currentFragment).onCollapseEnd();
                }
            }
        });
        this.mPanelTabCallHistory.setOnClickListener(this);
        this.mPanelTabVoiceMail.setOnClickListener(this);
        this.mPanelTabSharedLine.setOnClickListener(this);
        this.mPanelTabSMS.setOnClickListener(this);
        this.mBtnBackToCall.setOnClickListener(this);
        this.mTxtSipUnavailable.setOnClickListener(this);
        this.mLearnMore.setOnClickListener(this);
        this.mDismiss.setOnClickListener(this);
        initPagerAdapter();
        CmmSIPCallManager.getInstance().addListener(this.mSIPCallListener);
        CmmSIPCallManager.getInstance().addPBXLoginConflictListener(this.mLoginConflictListener);
        CmmPBXCallHistoryManager.getInstance().addISIPCallRepositoryEventSinkListener(this.mPBXRepositoryListener);
        CmmSIPCallManager.getInstance().addNetworkListener(this.mNetworkStatusListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mLineEventSinkListener);
        CmmSIPMessageManager.getInstance().addListener(this.mMessageEventSinkUIListener);
        this.mMMConnectAlertView.setActionType(1);
        if (bundle != null) {
            this.mSelectedPhoneNumber = bundle.getString("mSelectedPhoneNumber");
            this.mSelectedDisplayName = bundle.getString("mSelectedDisplayName");
            this.mHasShow = bundle.getBoolean("mHasShow");
        }
        return inflate;
    }

    public void onDestroyView() {
        CmmSIPCallManager.getInstance().removeListener(this.mSIPCallListener);
        CmmSIPCallManager.getInstance().removePBXLoginConflictListener(this.mLoginConflictListener);
        CmmPBXCallHistoryManager.getInstance().removeISIPCallRepositoryEventSinkListener(this.mPBXRepositoryListener);
        CmmSIPCallManager.getInstance().removeNetworkListener(this.mNetworkStatusListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mLineEventSinkListener);
        CmmSIPMessageManager.getInstance().removeListener(this.mMessageEventSinkUIListener);
        MainObservable.getInstance().deleteObserver(this);
        super.onDestroyView();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mMMConnectAlertView.setActionType(1);
        this.mHandler.post(new Runnable() {
            public void run() {
                PhonePBXTabFragment.this.setCurrentItem(ZMPhoneUtils.readIntValue(PreferenceUtil.PBX_FRAGMENT_INDEX, 0));
            }
        });
    }

    private void initPagerAdapter() {
        this.mPanelTabSMS.setVisibility(CmmSIPMessageManager.getInstance().isMessageEnabled() ? 0 : 8);
        this.mPagerAdapter = new PhonePBXPagerAdapter(getChildFragmentManager());
        this.mPbxViewPager.setAdapter(this.mPagerAdapter);
    }

    /* access modifiers changed from: private */
    public boolean updatePagerAdapter() {
        if (this.mPagerAdapter != null) {
            boolean isMessageEnabled = CmmSIPMessageManager.getInstance().isMessageEnabled();
            this.mPanelTabSMS.setVisibility(isMessageEnabled ? 0 : 8);
            int currentItem = this.mPbxViewPager.getCurrentItem();
            if (this.mPagerAdapter.checkPages(isMessageEnabled)) {
                setCurrentItem(currentItem);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        updateTabbar();
        updateTxtCallHistoryBubble();
        updateTxtVoiceMailBubble();
        updateTxtLine();
        updateTxtSmsBubble();
        updateE911();
    }

    /* access modifiers changed from: private */
    public void updateTabbar() {
        int currentItem = this.mPbxViewPager.getCurrentItem();
        boolean z = false;
        this.mPanelTabCallHistory.setSelected(currentItem == 0);
        this.mPanelTabVoiceMail.setSelected(currentItem == 1);
        this.mPanelTabSharedLine.setSelected(currentItem == 2);
        View view = this.mPanelTabSMS;
        if (currentItem == 3) {
            z = true;
        }
        view.setSelected(z);
    }

    /* access modifiers changed from: private */
    public void setCurrentItem(int i) {
        if (i > 0) {
            int count = this.mPagerAdapter.getCount() - 1;
            if (i > count) {
                i = count;
            }
            this.mPbxViewPager.setCurrentItem(i);
        }
    }

    public void enterSelectMode() {
        this.mIsInSelectMode = true;
        showInSelectView();
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof IPhonePBX) {
            ((IPhonePBX) currentFragment).onEnterSelectMode();
        }
        FragmentActivity activity = getActivity();
        if (activity instanceof IMActivity) {
            ((IMActivity) activity).enterSelectMode();
        }
    }

    public boolean exitSelectMode() {
        if (!this.mIsInSelectMode) {
            return false;
        }
        this.mIsInSelectMode = false;
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof IPhonePBX) {
            ((IPhonePBX) currentFragment).onExitSelectMode();
        }
        hideInSelectView();
        updateUI();
        FragmentActivity activity = getActivity();
        if (activity instanceof IMActivity) {
            ((IMActivity) activity).exitSelectMode();
        }
        return true;
    }

    private void showInSelectView() {
        if (((ZMActivity) getActivity()) != null) {
            this.mPanelBottomInSelect = View.inflate(getActivity(), C4558R.layout.zm_sip_select_all, null);
            this.mTxtSelectAllInSelect = (TextView) this.mPanelBottomInSelect.findViewById(C4558R.C4560id.select_all);
            this.mImgDeleteInSelect = (ImageView) this.mPanelBottomInSelect.findViewById(C4558R.C4560id.delete);
            this.mTxtSelectAllInSelect.setOnClickListener(this);
            this.mImgDeleteInSelect.setOnClickListener(this);
            this.mImgDeleteInSelect.setEnabled(false);
            WindowManager windowManager = getActivity().getWindowManager();
            LayoutParams layoutParams = new LayoutParams();
            layoutParams.type = 1000;
            layoutParams.format = 1;
            layoutParams.flags |= 1320;
            layoutParams.width = -1;
            layoutParams.height = -2;
            layoutParams.gravity = 80;
            windowManager.addView(this.mPanelBottomInSelect, layoutParams);
            this.mPanelTopInSelect = View.inflate(getActivity(), C4558R.layout.zm_sip_inselect_top, null);
            this.mBtnDoneInSelect = (Button) this.mPanelTopInSelect.findViewById(C4558R.C4560id.btnDone);
            this.mBtnDoneInSelect.setOnClickListener(this);
            LayoutParams layoutParams2 = new LayoutParams();
            layoutParams2.type = 1000;
            layoutParams2.format = 1;
            layoutParams2.flags |= 1320;
            layoutParams2.width = -1;
            layoutParams2.height = -2;
            layoutParams2.y = UIUtil.getStatusBarHeight(getActivity());
            layoutParams2.gravity = 48;
            windowManager.addView(this.mPanelTopInSelect, layoutParams2);
            MainObservable.getInstance().addObserver(this);
        }
    }

    private void hideInSelectView() {
        MainObservable.getInstance().deleteObserver(this);
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            WindowManager windowManager = zMActivity.getWindowManager();
            View view = this.mPanelBottomInSelect;
            if (view != null) {
                windowManager.removeView(view);
            }
            View view2 = this.mPanelTopInSelect;
            if (view2 != null) {
                windowManager.removeView(view2);
            }
            this.mPanelBottomInSelect = null;
        }
    }

    public void onClick(View view) {
        if (view == this.mPanelTabCallHistory) {
            this.mPbxViewPager.setCurrentItem(0);
        } else if (view == this.mPanelTabVoiceMail) {
            this.mPbxViewPager.setCurrentItem(1);
        } else if (view == this.mPanelTabSharedLine) {
            this.mPbxViewPager.setCurrentItem(2);
        } else if (view == this.mPanelTabSMS) {
            this.mPbxViewPager.setCurrentItem(3);
        } else if (view == this.mImgDeleteInSelect) {
            onClickDeleteInSelect();
        } else if (view == this.mBtnDoneInSelect) {
            onClickBtnDoneInSelect();
        } else if (view == this.mTxtSelectAllInSelect) {
            onClickBtnSelectAll();
        } else if (view == this.mBtnBackToCall) {
            EventBus.getDefault().post(new ZMReturnToCallEvent());
        } else if (view == this.mTxtSipUnavailable) {
            onClickTxtSipUnavailable();
        } else if (view == this.mLearnMore) {
            onClickBtnLearnMore();
        } else if (view == this.mDismiss) {
            removeE911Panel();
        }
    }

    private void onClickDeleteInSelect() {
        if (CmmSIPCallManager.getInstance().checkNetwork(getContext())) {
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment instanceof IPhonePBX) {
                ((IPhonePBX) currentFragment).onDeleteInSelectMode();
            }
        }
    }

    private void onClickBtnSelectAll() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof IPhonePBX) {
            ((IPhonePBX) currentFragment).setSelectAllMode();
        }
        this.mTxtSelectAllInSelect.setText(getString(C4558R.string.zm_sip_select_all_61381));
    }

    public void onClickBtnDoneInSelect() {
        exitSelectMode();
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof IPhonePBX) {
            ((IPhonePBX) currentFragment).onExitSelectMode();
        }
        updateUI();
    }

    public void updateTxtVoiceMailBubble() {
        if (isAdded()) {
            String str = "";
            int totalUnreadVoiceMailCount = CmmPBXCallHistoryManager.getInstance().getTotalUnreadVoiceMailCount();
            if (totalUnreadVoiceMailCount > 99) {
                str = "99+";
            } else if (totalUnreadVoiceMailCount > 0) {
                str = String.valueOf(totalUnreadVoiceMailCount);
            }
            if (!StringUtil.isEmptyOrNull(str)) {
                this.mTxtVoiceMailBubble.setText(str);
                this.mTxtVoiceMail.setContentDescription(getString(C4558R.string.zm_sip_accessbility_voice_mail_unread_bubble_61381, str, Integer.valueOf(this.mPagerAdapter.getCount())));
                this.mTxtVoiceMailBubble.setVisibility(0);
            } else {
                this.mTxtVoiceMailBubble.setText("");
                this.mTxtVoiceMail.setContentDescription(getString(C4558R.string.zm_sip_accessbility_voice_mail_unread_bubble_61381, "0", Integer.valueOf(this.mPagerAdapter.getCount())));
                this.mTxtVoiceMailBubble.setVisibility(4);
            }
        }
    }

    public void updateTxtCallHistoryBubble() {
        if (isAdded()) {
            String str = "";
            int missedCallHistoryCount = CmmPBXCallHistoryManager.getInstance().getMissedCallHistoryCount();
            if (missedCallHistoryCount > 99) {
                str = "99+";
            } else if (missedCallHistoryCount > 0) {
                str = String.valueOf(missedCallHistoryCount);
            }
            if (!StringUtil.isEmptyOrNull(str)) {
                this.mTxtCallHistoryBubble.setText(str);
                this.mTxtCallHistory.setContentDescription(getString(C4558R.string.zm_sip_accessbility_call_history_unread_bubble_61381, str, Integer.valueOf(this.mPagerAdapter.getCount())));
                this.mTxtCallHistoryBubble.setVisibility(0);
            } else {
                this.mTxtCallHistoryBubble.setText("");
                this.mTxtCallHistory.setContentDescription(getString(C4558R.string.zm_sip_accessbility_call_history_unread_bubble_61381, "0", Integer.valueOf(this.mPagerAdapter.getCount())));
                this.mTxtCallHistoryBubble.setVisibility(4);
            }
        }
    }

    public void updateTxtLine() {
        this.mTxtLine.setContentDescription(getString(C4558R.string.zm_sip_sla_accessibility_lines_82852, Integer.valueOf(this.mPagerAdapter.getCount())));
    }

    public void updateTxtSmsBubble() {
        if (isAdded() && CmmSIPMessageManager.getInstance().isMessageEnabled()) {
            String str = "";
            int totalUnreadCount = CmmSIPMessageManager.getInstance().getTotalUnreadCount();
            if (totalUnreadCount > 99) {
                str = "99+";
            } else if (totalUnreadCount > 0) {
                str = String.valueOf(totalUnreadCount);
            }
            if (!StringUtil.isEmptyOrNull(str)) {
                this.mTxtSMSBubble.setText(str);
                this.mTxtSMS.setContentDescription(getString(C4558R.string.zm_sip_sms_accessibility_117773, str));
                this.mTxtSMSBubble.setVisibility(0);
            } else {
                this.mTxtSMSBubble.setText("");
                this.mTxtSMS.setContentDescription(getString(C4558R.string.zm_sip_sms_accessibility_117773, "0"));
                this.mTxtSMSBubble.setVisibility(4);
            }
        }
    }

    public void onShow() {
        this.mHasShow = true;
    }

    public boolean isHasShow() {
        return this.mHasShow;
    }

    public boolean isInSelectMode() {
        return this.mIsInSelectMode;
    }

    public Fragment getCurrentFragment() {
        ZMViewPager zMViewPager = this.mPbxViewPager;
        if (zMViewPager == null) {
            return null;
        }
        return this.mPagerAdapter.getItem(zMViewPager.getCurrentItem());
    }

    public void update(Observable observable, Object obj) {
        if (isAdded()) {
            View view = this.mPanelTopInSelect;
            if (view != null && ViewCompat.isAttachedToWindow(view)) {
                if (obj instanceof Boolean) {
                    this.mImgDeleteInSelect.setEnabled(((Boolean) obj).booleanValue());
                }
                if (obj instanceof Integer) {
                    if (((Integer) obj).intValue() == 2) {
                        this.mTxtSelectAllInSelect.setText(getString(C4558R.string.zm_sip_unselect_all_61381));
                    } else {
                        this.mTxtSelectAllInSelect.setText(getString(C4558R.string.zm_sip_select_all_61381));
                    }
                }
            }
        }
    }

    public void onListViewDatasetChanged(boolean z) {
        int currentItem = this.mPbxViewPager.getCurrentItem();
        if (z) {
            if (currentItem == 0) {
                dismissCoverView();
            }
        } else if (currentItem == 1) {
            dismissCoverView();
        }
    }

    /* access modifiers changed from: private */
    public void dismissCoverView() {
        PhonePBXListCoverView phonePBXListCoverView = this.mCoverView;
        if (phonePBXListCoverView != null && phonePBXListCoverView.isShow()) {
            this.mCoverView.dismiss();
            accessibilityControl(1000);
        }
    }

    private void accessibilityControl(long j) {
        if (getContext() != null) {
            AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
            if (accessibilityManager != null && accessibilityManager.isEnabled()) {
                if (this.mCoverView.isShow()) {
                    this.mCoverView.requestCoverFocusForAccessibility(j);
                    return;
                }
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment instanceof IPhonePBXAccessbility) {
                    ((IPhonePBXAccessbility) currentFragment).accessibilityControl(j);
                }
            }
        }
    }

    public void displayCoverView(@NonNull PBXCallHistory pBXCallHistory, View view, boolean z) {
        PhonePBXListCoverView phonePBXListCoverView = this.mCoverView;
        if (phonePBXListCoverView != null && !phonePBXListCoverView.isAnimRunning()) {
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment instanceof IPhonePBX) {
                this.mCoverView.setViews(((IPhonePBX) currentFragment).getListView(), this.mContentContainerView);
            }
            this.mCoverView.setSelectListItemView(view);
            this.mCoverView.bindView(pBXCallHistory, z);
            this.mCoverView.start();
        }
    }

    public void openKeyboard() {
        SipDialKeyboardFragment.showAsActivity((Fragment) this, 0);
    }

    public void switchToHistory() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (PhonePBXTabFragment.this.isAdded() && PhonePBXTabFragment.this.mPanelTabCallHistory != null) {
                    PhonePBXTabFragment.this.mPanelTabCallHistory.performClick();
                }
            }
        }, 200);
    }

    public boolean onBackPressed() {
        boolean exitSelectMode = exitSelectMode();
        if (!this.mCoverView.isShow()) {
            return exitSelectMode;
        }
        dismissCoverView();
        return true;
    }

    private void pauseAudioPlayer() {
        PhonePBXListCoverView phonePBXListCoverView = this.mCoverView;
        if (phonePBXListCoverView != null && phonePBXListCoverView.isShow()) {
            this.mCoverView.setPlayAudioPause();
        }
    }

    public void onPickSipResult(@Nullable String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                callSip(str, str2);
                return;
            }
            this.mSelectedPhoneNumber = str;
            this.mSelectedDisplayName = str2;
            zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 11);
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null && i == 11 && (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0)) {
            String str = this.mSelectedPhoneNumber;
            if (str != null) {
                callSip(str, this.mSelectedDisplayName);
            }
            this.mSelectedPhoneNumber = null;
            this.mSelectedDisplayName = null;
        }
    }

    private void callSip(@NonNull String str, String str2) {
        CmmSIPCallManager.getInstance().callPeer(str, str2);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 11) {
            EventTaskManager eventTaskManager = getEventTaskManager();
            if (eventTaskManager != null) {
                final int i2 = i;
                final String[] strArr2 = strArr;
                final int[] iArr2 = iArr;
                C407112 r2 = new EventAction("PhonePBXTabFragmentPermissionResult") {
                    public void run(IUIElement iUIElement) {
                        if (iUIElement instanceof PhonePBXTabFragment) {
                            PhonePBXTabFragment phonePBXTabFragment = (PhonePBXTabFragment) iUIElement;
                            if (phonePBXTabFragment.isAdded()) {
                                phonePBXTabFragment.handleRequestPermissionResult(i2, strArr2, iArr2);
                            }
                        }
                    }
                };
                eventTaskManager.pushLater("PhonePBXTabFragmentPermissionResult", r2);
            }
        }
    }

    public void onPause() {
        super.onPause();
        pauseAudioPlayer();
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (!z && isAdded() && isResumed()) {
            pauseAudioPlayer();
        }
        if (z && isAdded()) {
            this.mHasShow = true;
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment instanceof OnFragmentShowListener) {
                ((OnFragmentShowListener) currentFragment).onShow();
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mSelectedPhoneNumber", this.mSelectedPhoneNumber);
        bundle.putString("mSelectedDisplayName", this.mSelectedDisplayName);
        bundle.putBoolean("mHasShow", this.mHasShow);
    }

    /* access modifiers changed from: private */
    public void updateErrorMessage() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (instance.getSipIntergration() == null) {
            this.mTxtSipUnavailable.setVisibility(0);
            this.mTxtSipUnavailable.setText(C4558R.string.zm_sip_error_user_configuration_99728);
            this.mTxtSipUnavailable.setTag("reload_user_config");
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                AccessibilityUtil.sendAccessibilityFocusEvent(this.mTxtSipUnavailable);
                TextView textView = this.mTxtSipUnavailable;
                AccessibilityUtil.announceForAccessibilityCompat((View) textView, (CharSequence) textView.getText().toString());
            }
        } else if (instance.isSipError()) {
            String registerErrorString = CmmSIPCallManager.getInstance().getRegisterErrorString();
            if (registerErrorString == null) {
                this.mTxtSipUnavailable.setVisibility(8);
                return;
            }
            this.mTxtSipUnavailable.setVisibility(0);
            this.mTxtSipUnavailable.setText(registerErrorString);
            this.mTxtSipUnavailable.setTag(null);
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                AccessibilityUtil.announceForAccessibilityCompat((View) this.mTxtSipUnavailable, (CharSequence) registerErrorString);
            }
        } else {
            this.mTxtSipUnavailable.setVisibility(8);
        }
    }

    private void onClickTxtSipUnavailable() {
        if ("reload_user_config".equals(this.mTxtSipUnavailable.getTag())) {
            this.mTxtSipUnavailable.setVisibility(8);
            this.mHandler.removeCallbacks(this.mQueryUserPbxInfoRunnable);
            this.mHandler.postDelayed(this.mQueryUserPbxInfoRunnable, 500);
        }
    }

    public void onResume() {
        super.onResume();
        updateUI();
        updateErrorMessage();
        accessibilityControl(3000);
    }

    public void blockNumber(@NonNull PBXCallHistory pBXCallHistory) {
        this.mNumberNameCache.put(pBXCallHistory.number, pBXCallHistory.getDisplayNameAndNumber());
        PBXBlockNumberDialogFragment.showInActivity((ZMActivity) getContext(), pBXCallHistory);
    }

    private boolean checkShowE911() {
        return CmmSIPCallManager.getInstance().isCloudPBXEnabled() && !CmmSIPCallManager.getInstance().isE911ServicePromptReaded();
    }

    private void updateE911() {
        this.mPanel911.setVisibility(checkShowE911() ? 0 : 8);
    }

    private void onClickBtnLearnMore() {
        if (getActivity() != null) {
            ActivityStartHelper.startActivityForeground(getActivity(), new Intent("android.intent.action.VIEW", Uri.parse(getString(C4558R.string.zm_zoom_E911_learn_more))));
        }
        removeE911Panel();
    }

    private void removeE911Panel() {
        if (CmmSIPCallManager.getInstance().setE911ServicePromptAsReaded()) {
            this.mPanel911.setVisibility(8);
        }
    }
}

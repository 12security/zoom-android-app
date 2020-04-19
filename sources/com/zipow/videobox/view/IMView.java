package com.zipow.videobox.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener;
import com.zipow.videobox.IMChatActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.IMAddrBookListFragment;
import com.zipow.videobox.fragment.IMBuddyListFragment;
import com.zipow.videobox.fragment.IMChatFragment;
import com.zipow.videobox.fragment.IMFavoriteListFragment;
import com.zipow.videobox.fragment.IMMeetingFragment;
import com.zipow.videobox.fragment.MMChatsListFragment;
import com.zipow.videobox.fragment.SettingFragment;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomProductHelper;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CallHistoryMgr;
import com.zipow.videobox.sip.server.CmmPBXCallHistoryManager;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.UpgradeUtil;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.p014mm.MMContentFragment;
import com.zipow.videobox.view.sip.PhoneCallFragment;
import com.zipow.videobox.view.sip.PhonePBXTabFragment;
import com.zipow.videobox.view.sip.SipDialKeyboardFragment;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMIgnoreKeyboardLayout;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.androidlib.widget.ZMViewPager;
import p021us.zoom.thirdparty.login.facebook.FBSessionStore;
import p021us.zoom.videomeetings.C4558R;

public class IMView extends ZMIgnoreKeyboardLayout implements OnClickListener, KeyboardListener {
    private static final String TAB_ADDRBOOK = "AddressBook";
    private static final String TAB_BUDDYLIST = "BuddyList";
    private static final String TAB_CHATS = "Chats";
    private static final String TAB_CONTENT = "Content";
    private static final String TAB_MEETING = "Meeting";
    private static final String TAB_MEETINGS = "Meetings";
    private static final String TAB_SETTINGS = "Settings";
    private static final String TAB_SIP = "SIP";
    private static final String TAG = "IMView";
    private static long g_lastAuthFailedTime;
    private View chatTabView;
    private AvatarView mAvatar;
    private Button mBtnReturnToConf2;
    private boolean mHasTabBuddyList = false;
    private boolean mIsLargeMode = false;
    private int mLoginType = 102;
    private int mLoginVendor = 0;
    private String mMyName;
    /* access modifiers changed from: private */
    public IMViewPagerAdapter mPagerAdapter;
    private ViewGroup mPanelChatParent;
    /* access modifiers changed from: private */
    public ImageView mRedDot;
    @NonNull
    SimpleSIPCallEventListener mSIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnWMIActive(boolean z) {
            super.OnWMIActive(z);
            if (!z) {
                IMView.this.mRedDot.setVisibility(8);
            }
        }

        public void OnWMIMessageCountChanged(int i, int i2, boolean z) {
            super.OnWMIMessageCountChanged(i, i2, z);
            IMView.this.updateUnreadCallsCountTabBubble();
        }
    };
    private TextView mSettingsNoteBubble;
    /* access modifiers changed from: private */
    public TabHost mTabHost;
    private TextView mTxtUnreadCallsCountTabBubble;
    private TextView mTxtUnreadChatsCountTabBubble;
    private TextView mTxtUnreadThirdPartyIMMsgCountTabBubble;
    /* access modifiers changed from: private */
    public ZMViewPager mViewPager;
    private boolean mbHasContacts = false;
    private View sipTabView;

    public interface OnFragmentShowListener {
        void onShow();
    }

    public static class StartHangoutFailedDialog extends ZMDialogFragment {
        public static void show(FragmentManager fragmentManager, String str, int i) {
            Bundle bundle = new Bundle();
            bundle.putInt("errorCode", i);
            StartHangoutFailedDialog startHangoutFailedDialog = new StartHangoutFailedDialog();
            startHangoutFailedDialog.setArguments(bundle);
            startHangoutFailedDialog.show(fragmentManager, str);
        }

        public StartHangoutFailedDialog() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            int i = arguments != null ? arguments.getInt("errorCode") : -1;
            Builder message = new Builder(getActivity()).setTitle(C4558R.string.zm_alert_start_conf_failed).setMessage(errorCodeToMessage(getResources(), i));
            if (i != 8) {
                message.setNegativeButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
            } else {
                message.setPositiveButton(C4558R.string.zm_btn_update, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StartHangoutFailedDialog.this.updateClient();
                    }
                }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
            }
            return message.create();
        }

        @NonNull
        private String errorCodeToMessage(@NonNull Resources resources, int i) {
            if (i == 8) {
                return resources.getString(C4558R.string.zm_msg_conffail_needupdate_confirm);
            }
            return resources.getString(C4558R.string.zm_msg_conffail_unknownerror_confirm, new Object[]{Integer.valueOf(i)});
        }

        /* access modifiers changed from: private */
        public void updateClient() {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                UpgradeUtil.upgrade(zMActivity);
            }
        }
    }

    private void updateLocalStatus(int i) {
    }

    public void onIMBuddySort() {
    }

    public void onSubscriptionRequest() {
    }

    public void onSubscriptionUpdate() {
    }

    public IMView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public IMView(Context context) {
        super(context);
        initView();
    }

    @SuppressLint({"NewApi"})
    public void announceAccessibility(String str) {
        Context context = getContext();
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            if (VERSION.SDK_INT >= 16) {
                announceForAccessibility(str);
                return;
            }
            AccessibilityEvent obtain = AccessibilityEvent.obtain(8);
            obtain.getText().add(str);
            obtain.setEnabled(true);
            obtain.setClassName(getClass().getName());
            obtain.setPackageName(context.getPackageName());
            AccessibilityEventCompat.asRecord(obtain).setSource(this);
            accessibilityManager.sendAccessibilityEvent(obtain);
        }
    }

    public void onResume() {
        if (this.mbHasContacts != PTApp.getInstance().hasContacts()) {
            updateUI(true);
            return;
        }
        updateButtons();
        if (isSipUIChanged()) {
            reloadView();
        }
    }

    private boolean isSipUIChanged() {
        if (this.mPagerAdapter == null) {
            return false;
        }
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        Fragment itemById = this.mPagerAdapter.getItemById(8);
        Fragment itemById2 = this.mPagerAdapter.getItemById(9);
        boolean isSipCallEnabled = instance.isSipCallEnabled();
        boolean isPBXActive = instance.isPBXActive();
        boolean isCloudPBXEnabled = instance.isCloudPBXEnabled();
        if (isPBXActive && itemById2 == null) {
            return true;
        }
        if (!isPBXActive) {
            if (itemById2 != null) {
                return true;
            }
            if (!isCloudPBXEnabled && isSipCallEnabled && itemById == null) {
                return true;
            }
            if (isSipCallEnabled || itemById == null) {
                return false;
            }
            return true;
        }
        return false;
    }

    private void initView() {
        String str;
        int i;
        setOrientation(1);
        this.mIsLargeMode = UIMgr.isLargeMode(getContext());
        if (this.mIsLargeMode) {
            View.inflate(getContext(), C4558R.layout.zm_imview_large, this);
        } else {
            View.inflate(getContext(), C4558R.layout.zm_imview, this);
        }
        if (this.mIsLargeMode) {
            FragmentManager supportFragmentManager = ((ZMActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
            if (PTApp.getInstance().isCurrentLoginTypeSupportIM()) {
                beginTransaction.replace(C4558R.C4560id.panelBuddyList, new IMBuddyListFragment(), IMBuddyListFragment.class.getName());
                beginTransaction.setTransition(0);
                beginTransaction.commit();
            } else {
                beginTransaction.replace(C4558R.C4560id.panelBuddyList, new IMFavoriteListFragment(), IMFavoriteListFragment.class.getName());
                beginTransaction.setTransition(0);
                beginTransaction.commit();
            }
            ViewGroup viewGroup = (ViewGroup) findViewById(C4558R.C4560id.panelRight);
            this.mPanelChatParent = (ViewGroup) viewGroup.findViewById(C4558R.C4560id.panelChatParent);
            this.mPanelChatParent.setVisibility(8);
            this.mBtnReturnToConf2 = (Button) viewGroup.findViewById(C4558R.C4560id.btnReturnToConf2);
            IMMeetingFragment iMMeetingFragment = new IMMeetingFragment();
            FragmentTransaction beginTransaction2 = supportFragmentManager.beginTransaction();
            beginTransaction2.replace(C4558R.C4560id.panelMeeting, iMMeetingFragment, IMMeetingFragment.class.getName());
            beginTransaction2.setTransition(0);
            beginTransaction2.commit();
        } else {
            this.mTabHost = (TabHost) findViewById(16908306);
            this.mTabHost.setup();
            C35062 r1 = new TabContentFactory() {
                public View createTabContent(String str) {
                    return new View(IMView.this.getContext());
                }
            };
            PTApp.getInstance().getZoomMessenger();
            if (ResourcesUtil.getBoolean((View) this, C4558R.bool.zm_config_use_4_pies_meeting_tab, false)) {
                TabHost tabHost = this.mTabHost;
                tabHost.addTab(tabHost.newTabSpec(TAB_MEETING).setIndicator(createMeetingTabView()).setContent(r1));
                str = TAB_MEETING;
            } else {
                TabHost tabHost2 = this.mTabHost;
                tabHost2.addTab(tabHost2.newTabSpec(TAB_CHATS).setIndicator(createChatsTabView()).setContent(r1));
                str = TAB_CHATS;
            }
            if (CmmSIPCallManager.getInstance().isPBXActive() || (!CmmSIPCallManager.getInstance().isCloudPBXEnabled() && CmmSIPCallManager.getInstance().isSipCallEnabled())) {
                TabHost tabHost3 = this.mTabHost;
                TabSpec newTabSpec = tabHost3.newTabSpec(TAB_SIP);
                View createSipTabView = createSipTabView();
                this.sipTabView = createSipTabView;
                tabHost3.addTab(newTabSpec.setIndicator(createSipTabView).setContent(r1));
                i = 2;
            } else {
                i = 1;
            }
            TabHost tabHost4 = this.mTabHost;
            tabHost4.addTab(tabHost4.newTabSpec(TAB_MEETINGS).setIndicator(createMeetingsTabView()).setContent(r1));
            this.mbHasContacts = false;
            if (PTApp.getInstance().hasContacts()) {
                TabHost tabHost5 = this.mTabHost;
                tabHost5.addTab(tabHost5.newTabSpec(TAB_ADDRBOOK).setIndicator(createAddrBookTabView()).setContent(r1));
                i++;
                this.mbHasContacts = true;
            }
            int i2 = i + 1;
            TabHost tabHost6 = this.mTabHost;
            tabHost6.addTab(tabHost6.newTabSpec(TAB_SETTINGS).setIndicator(createSettingsTabView()).setContent(r1));
            int i3 = i2 + 1;
            this.mViewPager = (ZMViewPager) findViewById(C4558R.C4560id.viewpager);
            this.mPagerAdapter = new IMViewPagerAdapter(((ZMActivity) getContext()).getSupportFragmentManager());
            this.mViewPager.setAdapter(this.mPagerAdapter);
            this.mViewPager.setOffscreenPageLimit(4);
            showTab(str);
            if (i3 <= 1) {
                this.mTabHost.setVisibility(8);
            }
            this.mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
                public void onTabChanged(String str) {
                    ZMActivity zMActivity = (ZMActivity) IMView.this.getContext();
                    if (zMActivity != null && zMActivity.isActive()) {
                        IMView.this.mViewPager.setCurrentItem(IMView.this.mTabHost.getCurrentTab(), false);
                        IMView iMView = IMView.this;
                        iMView.meetingLogEventTrack(iMView.mTabHost.getCurrentTabTag());
                        if (IMView.this.mTabHost.getCurrentTabView() != null) {
                            IMView.this.announceAccessibility(IMView.this.getResources().getString(C4558R.string.zm_description_tab_selected, new Object[]{IMView.this.mTabHost.getCurrentTabView().getContentDescription()}));
                        }
                    }
                }
            });
            this.mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
                public void onPageSelected(int i) {
                    IMView.this.mTabHost.setCurrentTab(i);
                    UIUtil.closeSoftKeyboard(IMView.this.getContext(), IMView.this);
                    if (IMView.TAB_SETTINGS.equals(IMView.this.mTabHost.getCurrentTabTag())) {
                        if (SettingFragment.needShowNewTipsOnSettingsTab(IMView.this.getContext())) {
                            IMView.this.clearSettingsNoteBubble();
                        }
                    } else if (IMView.TAB_ADDRBOOK.equals(IMView.this.mTabHost.getCurrentTabTag())) {
                        if (PreferenceUtil.readBooleanValue(PreferenceUtil.FIRST_OPEN_CONTACTS, true)) {
                            IMView.this.onFirstOpenContacts();
                        }
                        IMView.this.onSelectContactsPage();
                    } else if (IMView.TAB_SIP.equals(IMView.this.mTabHost.getCurrentTabTag()) && !CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
                        IMView.this.updateUnreadCallsCountTabBubble();
                    }
                    Fragment item = IMView.this.mPagerAdapter.getItem(i);
                    if (item instanceof OnFragmentShowListener) {
                        ((OnFragmentShowListener) item).onShow();
                    }
                }
            });
        }
        AvatarView avatarView = this.mAvatar;
        if (avatarView != null) {
            avatarView.setOnClickListener(this);
        }
        Button button = this.mBtnReturnToConf2;
        if (button != null) {
            button.setOnClickListener(this);
        }
        ViewGroup viewGroup2 = this.mPanelChatParent;
        if (viewGroup2 != null) {
            viewGroup2.setOnClickListener(this);
        }
        if (!isInEditMode()) {
            IMHelper iMHelper = PTApp.getInstance().getIMHelper();
            if (iMHelper != null) {
                updateLocalStatus(iMHelper.getIMLocalStatus());
            }
            updateButtons();
            showMyInfo();
        }
        this.mLoginType = PTApp.getInstance().getPTLoginType();
        this.mLoginVendor = getCurrentVendor();
        CmmSIPCallManager.getInstance().addListener(this.mSIPCallEventListener);
    }

    /* access modifiers changed from: private */
    public void meetingLogEventTrack(String str) {
        if (TAB_MEETINGS.equals(str)) {
            ZoomLogEventTracking.eventTrackSwitchTabToMeeting();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        IMViewPagerAdapter iMViewPagerAdapter = this.mPagerAdapter;
        if (iMViewPagerAdapter != null) {
            iMViewPagerAdapter.onConfigurationChanged(configuration);
        }
    }

    private View createTabView(String str, int i) {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_tab_indicator, null);
        ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.icon);
        ((TextView) inflate.findViewById(C4558R.C4560id.title)).setText(str);
        if (i > 0) {
            imageView.setImageResource(i);
        } else {
            imageView.setVisibility(8);
        }
        return inflate;
    }

    private View createChatsTabView() {
        String string = getResources().getString(C4558R.string.zm_tab_chats);
        String string2 = getResources().getString(C4558R.string.zm_description_tab_chats);
        int i = C4558R.C4559drawable.zm_icon_im;
        if (!PTApp.getInstance().hasZoomMessenger() || PTApp.getInstance().getZoomMessenger().imChatGetOption() == 2) {
            string = getResources().getString(C4558R.string.zm_tab_meeting);
            string2 = getResources().getString(C4558R.string.zm_description_tab_chats_no_messenger);
            i = C4558R.C4559drawable.zm_icon_home;
        }
        View createTabView = createTabView(string, i);
        createTabView.setContentDescription(string2);
        this.mTxtUnreadChatsCountTabBubble = (TextView) createTabView.findViewById(C4558R.C4560id.txtNoteBubble);
        this.chatTabView = createTabView;
        return createTabView;
    }

    private View createMeetingsTabView() {
        View createTabView = createTabView(getResources().getString(C4558R.string.zm_tab_content_meetings_52777), C4558R.C4559drawable.zm_icon_meeting);
        createTabView.setContentDescription(getResources().getString(C4558R.string.zm_description_tab_meetings_52777));
        return createTabView;
    }

    private View createAddrBookTabView() {
        if (!PTApp.getInstance().hasZoomMessenger()) {
            return createBuddyListTabView();
        }
        View createTabView = createTabView(getResources().getString(C4558R.string.zm_tab_content_contact_52777), C4558R.C4559drawable.zm_icon_contacts);
        createTabView.setContentDescription(getResources().getString(C4558R.string.zm_description_tab_addrbook));
        return createTabView;
    }

    private View createContentTabView() {
        View createTabView = createTabView(getResources().getString(C4558R.string.zm_tab_content), C4558R.C4559drawable.zm_icon_contents);
        createTabView.setContentDescription(getResources().getString(C4558R.string.zm_description_tab_content));
        return createTabView;
    }

    private View createSipTabView() {
        View createTabView = createTabView(getResources().getString(C4558R.string.zm_tab_sip_14480), C4558R.C4559drawable.zm_icon_sip);
        createTabView.setContentDescription(getResources().getString(C4558R.string.zm_description_tab_sip_14480));
        this.mTxtUnreadCallsCountTabBubble = (TextView) createTabView.findViewById(C4558R.C4560id.txtNoteBubble);
        this.mRedDot = (ImageView) createTabView.findViewById(C4558R.C4560id.dot);
        return createTabView;
    }

    private View createMeetingTabView() {
        View createTabView = createTabView(getResources().getString(C4558R.string.zm_tab_meeting), C4558R.C4559drawable.zm_icon_meeting);
        createTabView.setContentDescription(getResources().getString(C4558R.string.zm_description_tab_meeting));
        return createTabView;
    }

    private View createBuddyListTabView() {
        String str;
        int i;
        int i2 = C4558R.string.zm_tab_buddylist_google;
        String str2 = "";
        if (PTApp.getInstance().getPTLoginType() == 2) {
            i2 = C4558R.string.zm_tab_buddylist_google;
            i = C4558R.C4559drawable.zm_tab_icon_google;
            str = getResources().getString(C4558R.string.zm_description_tab_buddylist_google);
        } else if (PTApp.getInstance().getPTLoginType() == 0) {
            i2 = C4558R.string.zm_tab_buddylist_facebook;
            i = C4558R.C4559drawable.zm_tab_icon_fb;
            str = getResources().getString(C4558R.string.zm_description_tab_buddylist_facebook);
        } else {
            str = str2;
            i = 0;
        }
        View createTabView = createTabView(getResources().getString(i2), i);
        this.mTxtUnreadThirdPartyIMMsgCountTabBubble = (TextView) createTabView.findViewById(C4558R.C4560id.txtNoteBubble);
        createTabView.setContentDescription(str);
        return createTabView;
    }

    private View createFavoriteContactsTabView() {
        View createTabView = createTabView(getResources().getString(C4558R.string.zm_tab_favorite_contacts), C4558R.C4559drawable.zm_icon_contacts);
        createTabView.setContentDescription(getResources().getString(C4558R.string.zm_description_tab_favorite_contacts));
        return createTabView;
    }

    private View createSettingsTabView() {
        View createTabView = createTabView(getResources().getString(C4558R.string.zm_title_setting), C4558R.C4559drawable.zm_icon_settings);
        this.mSettingsNoteBubble = (TextView) createTabView.findViewById(C4558R.C4560id.txtNoteBubble);
        Drawable drawable = getResources().getDrawable(C4558R.C4559drawable.zm_ic_indicator_new);
        this.mSettingsNoteBubble.setBackgroundDrawable(drawable);
        this.mSettingsNoteBubble.setText("");
        this.mSettingsNoteBubble.setWidth(drawable.getIntrinsicWidth());
        this.mSettingsNoteBubble.setHeight(drawable.getIntrinsicHeight());
        updateSettingsNoteBubble();
        createTabView.setContentDescription(getResources().getString(C4558R.string.zm_description_tab_setting));
        return createTabView;
    }

    public void updateSettingsNoteBubble() {
        if (this.mSettingsNoteBubble != null) {
            if (SettingFragment.needShowNewTipsOnSettingsTab(getContext())) {
                this.mSettingsNoteBubble.setVisibility(0);
            } else {
                this.mSettingsNoteBubble.setVisibility(8);
            }
        }
    }

    public void onSipCallEvent() {
        updateUnreadCallsCountTabBubble();
        if (isSipUIChanged()) {
            reloadView();
        }
    }

    public void onUnreadBadgeSettingUpdated() {
        updateUnreadChatsCountTabBubble();
    }

    public void onSipVoiceMailsCountChanged() {
        updateUnreadCallsCountTabBubble();
    }

    public void onSipCallHistoryCountChanged() {
        updateUnreadCallsCountTabBubble();
    }

    public void onPBXMessageUnreadCountChanged() {
        updateUnreadCallsCountTabBubble();
    }

    /* access modifiers changed from: private */
    public void clearSettingsNoteBubble() {
        SettingFragment.saveNewTipsOnSettingsTabCleared();
        updateSettingsNoteBubble();
    }

    public boolean isLargeMode() {
        return this.mIsLargeMode;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        setIgnoreKeyboardOpen(true);
        super.onMeasure(i, i2);
    }

    public void onKeyboardOpen() {
        IMBuddyListFragment buddyListFragment = getBuddyListFragment();
        IMFavoriteListFragment favoriteListFragment = getFavoriteListFragment();
        IMAddrBookListFragment addrBookListFragment = getAddrBookListFragment();
        getChatsListFragment();
        PhoneCallFragment recentCallFragment = getRecentCallFragment();
        PhonePBXTabFragment recentPBXFragment = getRecentPBXFragment();
        if (buddyListFragment != null) {
            buddyListFragment.onKeyboardOpen();
        }
        if (favoriteListFragment != null) {
            favoriteListFragment.onKeyboardOpen();
        }
        if (addrBookListFragment != null) {
            addrBookListFragment.onKeyboardOpen();
        }
        if (recentCallFragment != null) {
            recentCallFragment.onKeyboardOpen();
        }
        if (recentPBXFragment != null) {
            recentPBXFragment.onKeyboardOpen();
        }
    }

    public void onKeyboardClosed() {
        IMBuddyListFragment buddyListFragment = getBuddyListFragment();
        IMFavoriteListFragment favoriteListFragment = getFavoriteListFragment();
        IMAddrBookListFragment addrBookListFragment = getAddrBookListFragment();
        getChatsListFragment();
        PhoneCallFragment recentCallFragment = getRecentCallFragment();
        PhonePBXTabFragment recentPBXFragment = getRecentPBXFragment();
        if (buddyListFragment != null) {
            buddyListFragment.onKeyboardClosed();
        }
        if (favoriteListFragment != null) {
            favoriteListFragment.onKeyboardClosed();
        }
        if (addrBookListFragment != null) {
            addrBookListFragment.onKeyboardClosed();
        }
        if (recentCallFragment != null) {
            recentCallFragment.onKeyboardClosed();
        }
        if (recentPBXFragment != null) {
            recentPBXFragment.onKeyboardClosed();
        }
    }

    public boolean onBackPressed() {
        PhoneCallFragment recentCallFragment = getRecentCallFragment();
        if (recentCallFragment != null && recentCallFragment.onBackPressed()) {
            return true;
        }
        PhonePBXTabFragment recentPBXFragment = getRecentPBXFragment();
        if (recentPBXFragment != null && recentPBXFragment.onBackPressed()) {
            return true;
        }
        IMAddrBookListFragment addrBookListFragment = getAddrBookListFragment();
        if (addrBookListFragment == null || !addrBookListFragment.handleBackPressed()) {
            return false;
        }
        return true;
    }

    private void updateButtons() {
        if (!PTApp.getInstance().hasActiveCall() || !VideoBoxApplication.getInstance().isConfProcessRunning()) {
            Button button = this.mBtnReturnToConf2;
            if (button != null) {
                button.setVisibility(8);
            }
        } else {
            Button button2 = this.mBtnReturnToConf2;
            if (button2 != null) {
                button2.setVisibility(0);
            }
        }
        updateUnreadThirdPartyIMMsgCountTabBubble();
        updateUnreadChatsCountTabBubble();
        updateUnreadCallsCountTabBubble();
    }

    private void updateUnreadThirdPartyIMMsgCountTabBubble() {
        if (this.mTxtUnreadThirdPartyIMMsgCountTabBubble != null) {
            IMHelper iMHelper = PTApp.getInstance().getIMHelper();
            if (iMHelper != null) {
                int unreadMsgCount = iMHelper.getUnreadMsgCount();
                if (unreadMsgCount == 0) {
                    this.mTxtUnreadThirdPartyIMMsgCountTabBubble.setVisibility(8);
                } else {
                    this.mTxtUnreadThirdPartyIMMsgCountTabBubble.setText(unreadMsgCount < 100 ? String.valueOf(unreadMsgCount) : "99+");
                    this.mTxtUnreadThirdPartyIMMsgCountTabBubble.setVisibility(0);
                }
            }
        }
    }

    private void updateUnreadChatsCountTabBubble() {
        if (this.mTxtUnreadChatsCountTabBubble != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (!(zoomMessenger == null || zoomMessenger.imChatGetOption() == 2)) {
                int totalUnreadMessageCountBySetting = zoomMessenger.getTotalUnreadMessageCountBySetting() + zoomMessenger.getUnreadRequestCount() + zoomMessenger.getTotalMarkedUnreadMsgCount();
                String string = getResources().getString(C4558R.string.zm_description_tab_chats);
                if (totalUnreadMessageCountBySetting == 0) {
                    this.mTxtUnreadChatsCountTabBubble.setVisibility(8);
                } else {
                    this.mTxtUnreadChatsCountTabBubble.setText(totalUnreadMessageCountBySetting < 100 ? String.valueOf(totalUnreadMessageCountBySetting) : "99+");
                    this.mTxtUnreadChatsCountTabBubble.setVisibility(0);
                    string = getResources().getQuantityString(C4558R.plurals.zm_description_tab_chats_77383, totalUnreadMessageCountBySetting, new Object[]{this.mTxtUnreadChatsCountTabBubble.getText().toString()});
                }
                this.chatTabView.setContentDescription(string);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateUnreadCallsCountTabBubble() {
        int i;
        if (this.mTxtUnreadCallsCountTabBubble != null && this.mRedDot != null) {
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            int i2 = 8;
            if (instance.isSipRegisterError() || instance.isPBXInactive()) {
                this.mTxtUnreadCallsCountTabBubble.setText("!");
                this.mTxtUnreadCallsCountTabBubble.setVisibility(0);
                this.mRedDot.setVisibility(8);
                return;
            }
            String str = null;
            if (!instance.isCloudPBXEnabled()) {
                CallHistoryMgr callHistoryMgr = PTApp.getInstance().getCallHistoryMgr();
                if (callHistoryMgr != null) {
                    if (!TAB_SIP.equals(this.mTabHost.getCurrentTabTag())) {
                        i = callHistoryMgr.getMissedCallInCount();
                        if (i > 0) {
                            str = i < 100 ? String.valueOf(i) : "99+";
                        }
                    } else {
                        callHistoryMgr.clearMissedCallIn();
                        i = 0;
                    }
                } else {
                    return;
                }
            } else if (instance.isShowSipRegisterError()) {
                str = "!";
                i = 0;
            } else {
                i = CmmPBXCallHistoryManager.getInstance().getTotalUnreadVoiceMailCount() + CmmPBXCallHistoryManager.getInstance().getMissedCallHistoryCount() + CmmSIPMessageManager.getInstance().getTotalUnreadCount();
                if (i > 0) {
                    str = i < 100 ? String.valueOf(i) : "99+";
                }
            }
            if (str == null) {
                this.mTxtUnreadCallsCountTabBubble.setVisibility(8);
                ImageView imageView = this.mRedDot;
                if (instance.hasNewMessage()) {
                    i2 = 0;
                }
                imageView.setVisibility(i2);
            } else {
                this.mRedDot.setVisibility(8);
                this.mTxtUnreadCallsCountTabBubble.setText(str);
                this.mTxtUnreadCallsCountTabBubble.setVisibility(0);
                View view = this.sipTabView;
                if (view != null) {
                    view.setContentDescription(getResources().getQuantityString(C4558R.plurals.zm_description_tab_sip_4_117773, i, new Object[]{getResources().getString(C4558R.string.zm_description_tab_sip_14480), str}));
                }
            }
        }
    }

    private void updateLocalStatus() {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            updateLocalStatus(iMHelper.getIMLocalStatus());
        }
    }

    public void showBuddyList() {
        if (UIMgr.isLargeMode(getContext())) {
            return;
        }
        if (this.mHasTabBuddyList) {
            showTab(TAB_BUDDYLIST);
        } else {
            showContacts();
        }
    }

    public void showChatsList() {
        showTab(TAB_CHATS);
    }

    public void showContacts() {
        showTab(TAB_ADDRBOOK);
    }

    public void showSIPDialpad(final String str) {
        showTab(TAB_SIP);
        getHandler().postDelayed(new Runnable() {
            public void run() {
                SipDialKeyboardFragment.showAsActivity((ZMActivity) IMView.this.getContext(), str);
            }
        }, 200);
    }

    public void showSIPHistory() {
        showTab(TAB_SIP);
        Fragment item = this.mPagerAdapter.getItem(this.mTabHost.getCurrentTab());
        if (item == null) {
            return;
        }
        if (item instanceof PhoneCallFragment) {
            ((PhoneCallFragment) item).switchToHistory();
        } else if (item instanceof PhonePBXTabFragment) {
            ((PhonePBXTabFragment) item).switchToHistory();
        }
    }

    public void showChatUI(@Nullable IMBuddyItem iMBuddyItem) {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            if (this.mIsLargeMode) {
                FragmentManager supportFragmentManager = ((ZMActivity) getContext()).getSupportFragmentManager();
                IMChatFragment chatFragment = getChatFragment();
                if ((iMBuddyItem == null || iMBuddyItem.userId == null) && chatFragment != null) {
                    this.mPanelChatParent.setVisibility(8);
                    try {
                        supportFragmentManager.beginTransaction().remove(chatFragment).commit();
                    } catch (Exception unused) {
                    }
                } else if (chatFragment == null || !iMBuddyItem.userId.equals(chatFragment.getChatToUserId())) {
                    this.mPanelChatParent.setVisibility(0);
                    IMChatFragment iMChatFragment = new IMChatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("buddyItem", iMBuddyItem);
                    bundle.putString("myName", this.mMyName);
                    iMChatFragment.setArguments(bundle);
                    FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
                    beginTransaction.replace(C4558R.C4560id.panelChat, iMChatFragment, IMChatFragment.class.getName());
                    beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    beginTransaction.commit();
                }
            } else if (iMBuddyItem != null) {
                Intent intent = new Intent(getContext(), IMChatActivity.class);
                intent.setFlags(131072);
                intent.putExtra("buddyItem", iMBuddyItem);
                intent.putExtra("myName", currentUserProfile.getUserName());
                ActivityStartHelper.startActivityForResult((Activity) (ZMActivity) getContext(), intent, 100);
            }
        }
    }

    @Nullable
    public IMChatFragment getChatFragment() {
        return (IMChatFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(IMChatFragment.class.getName());
    }

    @Nullable
    public IMBuddyListFragment getBuddyListFragment() {
        if (this.mViewPager != null) {
            IMBuddyListFragment iMBuddyListFragment = (IMBuddyListFragment) this.mPagerAdapter.getItemById(3);
            if (!(iMBuddyListFragment == null || iMBuddyListFragment.getView() == null)) {
                return iMBuddyListFragment;
            }
        }
        return (IMBuddyListFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(IMBuddyListFragment.class.getName());
    }

    @Nullable
    public IMMeetingFragment getMeetingFragment() {
        if (this.mViewPager != null) {
            IMMeetingFragment iMMeetingFragment = (IMMeetingFragment) this.mPagerAdapter.getItemById(2);
            if (!(iMMeetingFragment == null || iMMeetingFragment.getView() == null)) {
                return iMMeetingFragment;
            }
        }
        return (IMMeetingFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(IMMeetingFragment.class.getName());
    }

    @Nullable
    public IMFavoriteListFragment getFavoriteListFragment() {
        if (this.mViewPager != null) {
            IMFavoriteListFragment iMFavoriteListFragment = (IMFavoriteListFragment) this.mPagerAdapter.getItemById(5);
            if (!(iMFavoriteListFragment == null || iMFavoriteListFragment.getView() == null)) {
                return iMFavoriteListFragment;
            }
        }
        return (IMFavoriteListFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(IMFavoriteListFragment.class.getName());
    }

    @Nullable
    public SettingFragment getSettingFragment() {
        if (this.mViewPager != null) {
            SettingFragment settingFragment = (SettingFragment) this.mPagerAdapter.getItemById(4);
            if (!(settingFragment == null || settingFragment.getView() == null)) {
                return settingFragment;
            }
        }
        return null;
    }

    @Nullable
    public IMAddrBookListFragment getAddrBookListFragment() {
        if (this.mViewPager != null) {
            IMAddrBookListFragment iMAddrBookListFragment = (IMAddrBookListFragment) this.mPagerAdapter.getItemById(0);
            if (!(iMAddrBookListFragment == null || iMAddrBookListFragment.getView() == null)) {
                return iMAddrBookListFragment;
            }
        }
        return (IMAddrBookListFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(IMAddrBookListFragment.class.getName());
    }

    @Nullable
    public MMChatsListFragment getChatsListFragment() {
        if (this.mViewPager != null) {
            MMChatsListFragment mMChatsListFragment = (MMChatsListFragment) this.mPagerAdapter.getItemById(6);
            if (!(mMChatsListFragment == null || mMChatsListFragment.getView() == null)) {
                return mMChatsListFragment;
            }
        }
        return (MMChatsListFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(MMChatsListFragment.class.getName());
    }

    @Nullable
    public PhoneCallFragment getRecentCallFragment() {
        if (this.mViewPager != null) {
            PhoneCallFragment phoneCallFragment = (PhoneCallFragment) this.mPagerAdapter.getItemById(8);
            if (!(phoneCallFragment == null || phoneCallFragment.getView() == null)) {
                return phoneCallFragment;
            }
        }
        return (PhoneCallFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(PhoneCallFragment.class.getName());
    }

    @Nullable
    public PhonePBXTabFragment getRecentPBXFragment() {
        if (this.mViewPager != null) {
            PhonePBXTabFragment phonePBXTabFragment = (PhonePBXTabFragment) this.mPagerAdapter.getItemById(9);
            if (!(phonePBXTabFragment == null || phonePBXTabFragment.getView() == null)) {
                return phonePBXTabFragment;
            }
        }
        return (PhonePBXTabFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(PhonePBXTabFragment.class.getName());
    }

    @Nullable
    public MMContentFragment getContentFragment() {
        if (this.mViewPager != null) {
            MMContentFragment mMContentFragment = (MMContentFragment) this.mPagerAdapter.getItemById(7);
            if (!(mMContentFragment == null || mMContentFragment.getView() == null)) {
                return mMContentFragment;
            }
        }
        return (MMContentFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(MMContentFragment.class.getName());
    }

    public void updateUI() {
        updateUI(false);
    }

    public void updateUI(boolean z) {
        int currentVendor = getCurrentVendor();
        if (!(!z && this.mLoginType == PTApp.getInstance().getPTLoginType() && this.mLoginVendor == currentVendor)) {
            reloadView();
        }
        showMyInfo();
        updateButtons();
        updateLocalStatus();
        if (getChatFragment() != null) {
            this.mPanelChatParent.setVisibility(0);
        }
        updateSettingsNoteBubble();
    }

    private int getCurrentVendor() {
        ZoomProductHelper zoomProductHelper = PTApp.getInstance().getZoomProductHelper();
        if (zoomProductHelper != null) {
            return zoomProductHelper.getCurrentVendor();
        }
        return 0;
    }

    public void reloadView() {
        removeAllViews();
        this.mPagerAdapter.clear();
        this.mPagerAdapter.notifyDataSetChanged();
        initView();
    }

    public void onActivityDestroy() {
        CmmSIPCallManager.getInstance().removeListener(this.mSIPCallEventListener);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("IMView.superState", onSaveInstanceState);
        ZMViewPager zMViewPager = this.mViewPager;
        if (zMViewPager != null) {
            bundle.putInt("IMView.tabPage", zMViewPager.getCurrentItem());
        }
        return bundle;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            Parcelable parcelable2 = bundle.getParcelable("IMView.superState");
            int i = bundle.getInt("IMView.tabPage");
            if (i >= 0) {
                ZMViewPager zMViewPager = this.mViewPager;
                if (zMViewPager != null) {
                    zMViewPager.setCurrentItem(i, false);
                }
                TabHost tabHost = this.mTabHost;
                if (tabHost != null) {
                    tabHost.setCurrentTab(i);
                }
            }
            parcelable = parcelable2;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public void onIMReceived(@Nullable IMMessage iMMessage) {
        IMChatFragment chatFragment = getChatFragment();
        if (!(chatFragment == null || iMMessage == null)) {
            chatFragment.onIMReceived(iMMessage);
        }
        updateUnreadThirdPartyIMMsgCountTabBubble();
    }

    public void onIMBuddyPresence(BuddyItem buddyItem) {
        IMChatFragment chatFragment = getChatFragment();
        if (chatFragment != null) {
            chatFragment.onIMBuddyPresence(buddyItem);
        }
    }

    public void onIMBuddyPic(BuddyItem buddyItem) {
        IMChatFragment chatFragment = getChatFragment();
        if (chatFragment != null) {
            chatFragment.onIMBuddyPic(buddyItem);
        }
    }

    public void onMyInfoReady() {
        showMyInfo();
        IMMeetingFragment meetingFragment = getMeetingFragment();
        if (meetingFragment != null) {
            meetingFragment.onMyInfoReady();
        }
    }

    public void onMyPictureReady() {
        showMyInfo();
        IMMeetingFragment meetingFragment = getMeetingFragment();
        if (meetingFragment != null) {
            meetingFragment.onMyPictureReady();
        }
    }

    public void onIndicateZoomMessengerBuddyListUpdated() {
        updateUnreadChatsCountTabBubble();
    }

    public void onZoomMessengerNotifySubscribeRequest() {
        updateUnreadChatsCountTabBubble();
    }

    private void showMyInfo() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null && !StringUtil.isEmptyOrNull(currentUserProfile.getPictureLocalPath())) {
            String pictureLocalPath = currentUserProfile.getPictureLocalPath();
            AvatarView avatarView = this.mAvatar;
            if (avatarView != null) {
                avatarView.show(new ParamsBuilder().setPath(pictureLocalPath));
            }
            PreferenceUtil.saveStringValue(PreferenceUtil.LOCAL_AVATAR, pictureLocalPath);
        }
        if (currentUserProfile != null) {
            this.mMyName = currentUserProfile.getUserName();
        }
    }

    public void onCallStatusChanged(long j) {
        switch ((int) j) {
            case 1:
            case 2:
                Button button = this.mBtnReturnToConf2;
                if (button != null) {
                    button.setVisibility(0);
                    break;
                }
                break;
            default:
                Button button2 = this.mBtnReturnToConf2;
                if (button2 != null) {
                    button2.setVisibility(8);
                    break;
                }
                break;
        }
        IMChatFragment chatFragment = getChatFragment();
        if (chatFragment != null) {
            chatFragment.onCallStatusChanged(j);
        }
    }

    public void onCallPlistChanged() {
        IMChatFragment chatFragment = getChatFragment();
        if (chatFragment != null) {
            chatFragment.onCallPlistChanged();
        }
    }

    public void onIMLocalStatusChanged(int i) {
        updateLocalStatus(i);
    }

    public void onIMLogin(long j) {
        int i = (int) j;
        if (i == 0) {
            updateLocalStatus(4);
        } else if (i != 3) {
            updateLocalStatus(5);
        } else {
            updateLocalStatus(0);
            int pTLoginType = PTApp.getInstance().getPTLoginType();
            if (pTLoginType != 97) {
                if (j == 3 && pTLoginType == 0) {
                    FBSessionStore.clear(FBSessionStore.FACEBOOK_KEY, getContext());
                }
                PTApp.getInstance().setRencentJid("");
                PTApp.getInstance().logout(1);
                PTApp.getInstance().setWebSignedOn(false);
                if (g_lastAuthFailedTime == 0 || System.currentTimeMillis() - g_lastAuthFailedTime < 5000) {
                    showLoginUIForTokenExpired(true);
                } else {
                    showLoginUIForTokenExpired(false);
                }
                g_lastAuthFailedTime = System.currentTimeMillis();
            }
        }
    }

    public void onWebLogin(long j) {
        updateUI(true);
    }

    public void onGoogleWebAccessFail() {
        updateLocalStatus(5);
    }

    public void onZoomMessengerMessageReceived() {
        updateUnreadChatsCountTabBubble();
    }

    public void onZoomMessengerChatSessionListUpdate() {
        updateUnreadChatsCountTabBubble();
    }

    public void onZoomMessengerNotifyChatSessionUnreadUpdate() {
        updateUnreadChatsCountTabBubble();
    }

    public void onZoomMessengerNotifyChatSessionUnreadUpdate(String str) {
        updateUnreadChatsCountTabBubble();
    }

    public void onZoomMessengerIndicateRevokeMessageResult(String str, String str2, String str3, String str4, boolean z) {
        updateUnreadChatsCountTabBubble();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnReturnToConf2) {
            onClickBtnReturnToConf();
        } else if (id == C4558R.C4560id.avatarViewRight) {
            onClickAvatarView(view.getId());
        } else if (id == C4558R.C4560id.panelChatParent) {
            onClickPanelChatParent();
        }
    }

    private void showLoginUIForTokenExpired(boolean z) {
        PTApp.getInstance().setTokenExpired(true);
        LoginUtil.showLoginUI(getContext(), z, 100);
    }

    private void onClickBtnReturnToConf() {
        ConfLocalHelper.returnToConf(getContext());
    }

    private void onClickAvatarView(int i) {
        if (this.mIsLargeMode) {
            SettingFragment.show(((ZMActivity) getContext()).getSupportFragmentManager(), i);
        }
    }

    private void onClickPanelChatParent() {
        IMBuddyListFragment buddyListFragment = getBuddyListFragment();
        if (buddyListFragment != null && buddyListFragment.isFocusedOnSearchField()) {
            UIUtil.closeSoftKeyboard(getContext(), this);
        }
        showChatUI(null);
    }

    public boolean closeTips() {
        SettingFragment settingFragment = SettingFragment.getSettingFragment(((ZMActivity) getContext()).getSupportFragmentManager());
        if (settingFragment == null) {
            return false;
        }
        settingFragment.dismiss();
        return true;
    }

    public boolean onSearchRequested() {
        IMViewPagerAdapter iMViewPagerAdapter = this.mPagerAdapter;
        if (iMViewPagerAdapter != null) {
            ZMViewPager zMViewPager = this.mViewPager;
            if (zMViewPager != null) {
                Fragment item = iMViewPagerAdapter.getItem(zMViewPager.getCurrentItem());
                IMBuddyListFragment buddyListFragment = getBuddyListFragment();
                if (buddyListFragment != null && item == buddyListFragment) {
                    return buddyListFragment.onSearchRequested();
                }
                IMFavoriteListFragment favoriteListFragment = getFavoriteListFragment();
                if (favoriteListFragment != null && item == favoriteListFragment) {
                    return favoriteListFragment.onSearchRequested();
                }
                IMAddrBookListFragment addrBookListFragment = getAddrBookListFragment();
                if (addrBookListFragment == null || item != addrBookListFragment) {
                    return true;
                }
                return addrBookListFragment.onSearchRequested();
            }
        }
        return false;
    }

    public void onScheduleSuccess(ScheduledMeetingItem scheduledMeetingItem) {
        IMMeetingFragment meetingFragment = getMeetingFragment();
        if (meetingFragment != null) {
            meetingFragment.onScheduleSuccess(scheduledMeetingItem);
        }
    }

    public void onAddressBookEnabled(boolean z) {
        reloadView();
        if (this.mTabHost != null && this.mViewPager != null) {
            if (z) {
                showTab(TAB_ADDRBOOK);
            } else {
                showTab(TAB_SETTINGS);
            }
        }
    }

    public void onMMSessionDeleted(String str) {
        updateUnreadChatsCountTabBubble();
        MMChatsListFragment chatsListFragment = getChatsListFragment();
        if (chatsListFragment != null) {
            chatsListFragment.onSessionDeleted(str);
        }
        IMAddrBookListFragment addrBookListFragment = getAddrBookListFragment();
        if (addrBookListFragment != null) {
            addrBookListFragment.onSessionDelete(str);
        }
    }

    private void showTab(String str) {
        TabHost tabHost = this.mTabHost;
        if (tabHost != null) {
            tabHost.setCurrentTabByTag(str);
            this.mViewPager.setCurrentItem(this.mTabHost.getCurrentTab(), false);
        }
    }

    public void onNewVersionReady() {
        updateSettingsNoteBubble();
    }

    /* access modifiers changed from: private */
    public void onFirstOpenContacts() {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.FIRST_OPEN_CONTACTS, false);
        if (!PTApp.getInstance().isPhoneNumberRegistered()) {
            IMAddrBookListFragment addrBookListFragment = getAddrBookListFragment();
            if (addrBookListFragment != null) {
                addrBookListFragment.enableAddrBook();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContactsPage() {
        IMAddrBookListFragment addrBookListFragment = getAddrBookListFragment();
        if (addrBookListFragment != null) {
            addrBookListFragment.onSelected();
        }
    }

    public boolean isPhoneTabSelected() {
        Fragment item = this.mPagerAdapter.getItem(this.mViewPager.getCurrentItem());
        return (item instanceof PhoneCallFragment) || (item instanceof PhonePBXTabFragment);
    }

    public void enterSelectMode() {
        ZMViewPager zMViewPager = this.mViewPager;
        if (zMViewPager != null) {
            zMViewPager.setDisableScroll(true);
        }
    }

    public void exitSelectMode() {
        ZMViewPager zMViewPager = this.mViewPager;
        if (zMViewPager != null) {
            zMViewPager.setDisableScroll(false);
        }
    }
}

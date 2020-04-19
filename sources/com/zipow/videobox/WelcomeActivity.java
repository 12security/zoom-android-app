package com.zipow.videobox;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.zipow.videobox.dialog.DialogActionCallBack;
import com.zipow.videobox.dialog.ZMGDPRConfirmDialog;
import com.zipow.videobox.fragment.JoinConfFragment;
import com.zipow.videobox.fragment.RateZoomDialogFragment;
import com.zipow.videobox.fragment.SettingFragment;
import com.zipow.videobox.login.AuthFailedDialog;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.LogoutHandler;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IGDPRListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.UIMgr;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class WelcomeActivity extends ZMActivity implements IPTUIListener, OnClickListener, IGDPRListener, DialogActionCallBack {
    private static final String ARG_ACTION_FOR_IM_ACTIVITY = "actionForIMActivity";
    private static final String ARG_ACTION_SEND_INTENT;
    private static final String ARG_AUTO_LOGIN = "autoLogin";
    private static final String ARG_EXTRAS_FOR_IM_ACTIVITY = "extrasForIMActivity";
    private static final String ARG_IS_SHOWN_FOR_ACTION_SEND = "isShownForActionSend";
    private static final int REQUEST_DIALOG_GDPR = 1000;
    private static final String TAG = "WelcomeActivity";
    private static boolean gbShowRateRoomDialog = false;
    @Nullable
    private static WelcomeActivity sCurrentInstance = null;
    private int[] img = {C4558R.C4559drawable.zm_wlc_page_index1, C4558R.C4559drawable.zm_wlc_page_index2, C4558R.C4559drawable.zm_wlc_page_index3, C4558R.C4559drawable.zm_wlc_page_index4};
    /* access modifiers changed from: private */
    public WlcViewPagerAdapter mAdapter;
    private Button mBtnJoinConf;
    private Button mBtnLogin;
    private View mBtnLoginInternational;
    private Button mBtnReturnToConf;
    private View mBtnSettings;
    private View mBtnSignup;
    private boolean mLoginFailed = false;
    private boolean mNewVersionReady = false;
    private View mPanelActions;
    private View mPanelConnecting;
    private LinearLayout mVpIndexer;
    private List<View> mVpViews;
    private ViewPager mWlcViewpager;
    private boolean mbNeedBlockNextTimeAutoLogin = false;
    private int[] msg = {C4558R.string.zm_lbl_wlc_content1_90302, C4558R.string.zm_lbl_wlc_content2_90302, C4558R.string.zm_lbl_wlc_content3_90302, C4558R.string.zm_lbl_wlc_content4_90302};
    private int[] title = {C4558R.string.zm_lbl_wlc_title1_90302, C4558R.string.zm_lbl_wlc_title2_90302, C4558R.string.zm_lbl_wlc_title3_90302, C4558R.string.zm_lbl_wlc_title4_90302};

    public class WlcPageIndicator implements OnPageChangeListener {
        private List<ImageView> mImgList = new ArrayList();
        private int mImgSelectId = C4558R.C4559drawable.zm_dot_select;
        private int mImgUnSelectId = C4558R.C4559drawable.zm_dot_unselect;
        private int mPageCount;

        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        public WlcPageIndicator(Context context, LinearLayout linearLayout, int i) {
            this.mPageCount = i;
            int dip2px = UIUtil.dip2px(context, 7.0f);
            int dip2px2 = UIUtil.dip2px(context, 5.0f);
            for (int i2 = 0; i2 < this.mPageCount; i2++) {
                ImageView imageView = new ImageView(context);
                LayoutParams layoutParams = new LayoutParams(new ViewGroup.LayoutParams(-2, -2));
                layoutParams.leftMargin = dip2px2;
                layoutParams.rightMargin = dip2px2;
                layoutParams.height = dip2px;
                layoutParams.width = dip2px;
                if (i2 == 0) {
                    imageView.setBackgroundResource(this.mImgSelectId);
                } else {
                    imageView.setBackgroundResource(this.mImgUnSelectId);
                }
                linearLayout.addView(imageView, layoutParams);
                this.mImgList.add(imageView);
            }
        }

        public void onPageSelected(int i) {
            WelcomeActivity.this.mAdapter.sendAccessibilityFocusEvent(i);
            int i2 = 0;
            while (true) {
                int i3 = this.mPageCount;
                if (i2 < i3) {
                    if (i % i3 == i2) {
                        ((ImageView) this.mImgList.get(i2)).setBackgroundResource(this.mImgSelectId);
                    } else {
                        ((ImageView) this.mImgList.get(i2)).setBackgroundResource(this.mImgUnSelectId);
                    }
                    i2++;
                } else {
                    return;
                }
            }
        }
    }

    public class WlcViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        public WlcViewPagerAdapter(List<View> list) {
            this.mListViews = list;
        }

        public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
            viewGroup.removeView((View) this.mListViews.get(i));
        }

        @NonNull
        public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
            viewGroup.addView((View) this.mListViews.get(i));
            return this.mListViews.get(i);
        }

        public int getCount() {
            return this.mListViews.size();
        }

        public void sendAccessibilityFocusEvent(int i) {
            if (i >= 0 && i < this.mListViews.size()) {
                AccessibilityUtil.sendAccessibilityFocusEvent((TextView) ((View) this.mListViews.get(i)).findViewById(C4558R.C4560id.txtMsg));
            }
        }
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(LauncherActivity.class.getName());
        sb.append(".extra.ACTION_SEND_INTENT");
        ARG_ACTION_SEND_INTENT = sb.toString();
    }

    public static void showRateRoomDialogOnResume() {
        gbShowRateRoomDialog = true;
    }

    @Nullable
    public static WelcomeActivity getCurrentInstance() {
        return sCurrentInstance;
    }

    public static void show(Context context, boolean z, boolean z2) {
        show(context, z, z2, null, null);
    }

    public static void show(@Nullable Context context, boolean z, boolean z2, String str, Bundle bundle) {
        if (context != null) {
            Intent intent = new Intent(context, WelcomeActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(268435456);
            }
            if (z) {
                intent.addFlags(131072);
            }
            intent.putExtra(ARG_AUTO_LOGIN, z2);
            intent.putExtra(ARG_ACTION_FOR_IM_ACTIVITY, str);
            intent.putExtra(ARG_EXTRAS_FOR_IM_ACTIVITY, bundle);
            ActivityStartHelper.startActivityForeground(context, intent);
        }
    }

    public static void showForActionSend(@Nullable Context context, Intent intent) {
        if (context != null) {
            Intent intent2 = new Intent(context, WelcomeActivity.class);
            if (!(context instanceof Activity)) {
                intent2.addFlags(268435456);
            }
            intent2.putExtra(ARG_AUTO_LOGIN, true);
            intent2.putExtra(ARG_IS_SHOWN_FOR_ACTION_SEND, true);
            intent2.putExtra(ARG_ACTION_SEND_INTENT, intent);
            ActivityStartHelper.startActivityForeground(context, intent2);
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        UIUtil.renderStatueBar(this, true, C4409R.color.zm_ui_kit_color_white_ffffff);
        disableFinishActivityByGesture(true);
        if (UIUtil.getDisplayMinWidthInDip(this) < 500.0f) {
            setRequestedOrientation(1);
        }
        if (!PTApp.getInstance().isWebSignedOn() && (!PTApp.getInstance().hasZoomMessenger() || !PTApp.getInstance().autoSignin())) {
            setContentView(C4558R.layout.zm_welcome_new);
            this.mWlcViewpager = (ViewPager) findViewById(C4558R.C4560id.wlcViewpager);
            this.mVpIndexer = (LinearLayout) findViewById(C4558R.C4560id.vpIndexer);
            this.mBtnLogin = (Button) findViewById(C4558R.C4560id.btnLogin);
            this.mBtnJoinConf = (Button) findViewById(C4558R.C4560id.btnJoinConf);
            this.mBtnReturnToConf = (Button) findViewById(C4558R.C4560id.btnReturnToConf);
            this.mBtnLoginInternational = findViewById(C4558R.C4560id.loginInternational);
            this.mPanelConnecting = findViewById(C4558R.C4560id.panelConnecting);
            this.mPanelActions = findViewById(C4558R.C4560id.panelActions);
            this.mBtnSignup = findViewById(C4558R.C4560id.btnSignup);
            this.mBtnSettings = findViewById(C4558R.C4560id.btnSettings);
            initViewPager();
            this.mPanelActions.setVisibility(8);
            this.mPanelConnecting.setVisibility(8);
            this.mBtnLogin.setOnClickListener(this);
            this.mBtnJoinConf.setOnClickListener(this);
            this.mBtnReturnToConf.setOnClickListener(this);
            this.mBtnSettings.setOnClickListener(this);
            View view = this.mBtnSignup;
            if (view != null) {
                view.setOnClickListener(this);
            }
            View view2 = this.mBtnLoginInternational;
            if (view2 != null) {
                view2.setOnClickListener(this);
            }
            PTUI.getInstance().addPTUIListener(this);
            PTUI.getInstance().addGDPRListener(this);
            Mainboard mainboard = Mainboard.getMainboard();
            if (mainboard == null) {
                finish();
                return;
            }
            if (!mainboard.isInitialized()) {
                showLauncherActivity();
            }
            if (PTApp.getInstance().isWebSignedOn()) {
                showMainActivityAfterLogin();
                return;
            }
            if (bundle == null) {
                checkAutoLogin();
            } else {
                this.mLoginFailed = bundle.getBoolean("mLoginFailed", this.mLoginFailed);
            }
        } else if (!handleSendAction()) {
            IMActivity.show(this);
            finish();
        }
    }

    public void checkAutoLogin() {
        Intent intent = getIntent();
        if (!(intent == null || !intent.getBooleanExtra(ARG_AUTO_LOGIN, true) || PTApp.getInstance().getPTLoginType() == 102 || PTApp.getInstance().getPTLoginType() == 97)) {
            autoLogin();
        }
        if (PTUI.getInstance().NeedGDPRConfirm()) {
            showConnecting(false);
        } else if (PTUI.getInstance().NeedLoginDisclaimerConfirm()) {
            showConnecting(false);
        } else {
            showConnecting(PTApp.getInstance().isAuthenticating());
        }
        updateButtons();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("mLoginFailed", this.mLoginFailed);
    }

    public void onDestroy() {
        super.onDestroy();
        PTUI.getInstance().removePTUIListener(this);
        PTUI.getInstance().removeGDPRListener(this);
        sCurrentInstance = null;
    }

    public void onResume() {
        super.onResume();
        sCurrentInstance = this;
        if (PTApp.getInstance().isWebSignedOn()) {
            showMainActivityAfterLogin();
        } else {
            checkAutoLogin();
        }
        if (gbShowRateRoomDialog) {
            RateZoomDialogFragment.show(getSupportFragmentManager());
            gbShowRateRoomDialog = false;
        }
    }

    private void updateButtons() {
        if (!PTApp.getInstance().hasActiveCall() || !VideoBoxApplication.getInstance().isConfProcessRunning()) {
            this.mBtnJoinConf.setVisibility(0);
            this.mBtnReturnToConf.setVisibility(8);
        } else {
            this.mBtnJoinConf.setVisibility(8);
            this.mBtnReturnToConf.setVisibility(0);
        }
        if (ZMBuildConfig.BUILD_TARGET != 0) {
            return;
        }
        if (getDefaultVendor() == 1) {
            View view = this.mBtnSignup;
            if (view != null) {
                view.setVisibility(8);
            }
            View view2 = this.mBtnLoginInternational;
            if (view2 != null) {
                view2.setVisibility(0);
                this.mBtnLogin.setGravity(21);
                return;
            }
            return;
        }
        View view3 = this.mBtnSignup;
        if (view3 != null) {
            view3.setVisibility(0);
            this.mBtnLogin.setGravity(17);
        }
        View view4 = this.mBtnLoginInternational;
        if (view4 != null) {
            view4.setVisibility(8);
        }
    }

    private void showLauncherActivity() {
        LauncherActivity.showLauncherActivity(this);
        finish();
    }

    private boolean needBlockNextTimeAutoLogin() {
        return this.mbNeedBlockNextTimeAutoLogin;
    }

    public void setNeedBlockNextTimeAutoLogin(boolean z) {
        this.mbNeedBlockNextTimeAutoLogin = z;
    }

    private void autoLogin() {
        if (!needBlockNextTimeAutoLogin() && PTApp.getInstance().autoSignin()) {
            showConnecting(true);
        }
        setNeedBlockNextTimeAutoLogin(false);
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            sinkWebLogin(j);
        } else if (i == 22) {
            sinkCallStatusChanged(j);
        } else if (i == 25) {
            sinkNewVersionReady();
        } else if (i == 37) {
            sinkWebAccessFail();
        }
    }

    public void sinkNewVersionReady() {
        this.mNewVersionReady = true;
    }

    public void sinkWebLogin(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebLogin", new EventAction("sinkWebLogin") {
            public void run(@NonNull IUIElement iUIElement) {
                ((WelcomeActivity) iUIElement).handleOnWebLogin(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnWebLogin(long j) {
        onWebLogin(j);
    }

    private void sinkCallStatusChanged(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("handleOnCallStatusChanged") {
            public void run(@NonNull IUIElement iUIElement) {
                ((WelcomeActivity) iUIElement).handleOnCallStatusChanged(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnCallStatusChanged(long j) {
        onCallStatusChanged(j);
    }

    private void sinkWebAccessFail() {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebAccessFail", new EventAction("sinkWebAccessFail") {
            public void run(@NonNull IUIElement iUIElement) {
                ((WelcomeActivity) iUIElement).handleOnWebAccessFail();
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnWebAccessFail() {
        onWebAccessFail();
    }

    private void onWebLogin(long j) {
        if (j == 0) {
            showMainActivityAfterLogin();
            this.mLoginFailed = false;
        } else if (j == 1006) {
            PTApp.getInstance().setRencentJid("");
            showConnecting(false);
            if (!this.mLoginFailed) {
                this.mLoginFailed = true;
                showLoginExpired();
            }
        } else {
            PTApp.getInstance().setRencentJid("");
            showConnecting(false);
            String loginErrorMessage = getLoginErrorMessage(j);
            if (!this.mLoginFailed) {
                this.mLoginFailed = true;
                AuthFailedDialog.show(this, loginErrorMessage);
            }
        }
    }

    private void showMainActivityAfterLogin() {
        if (!handleSendAction()) {
            showIMActivity();
        }
    }

    private boolean handleSendAction() {
        Intent intent = getIntent();
        if (intent == null) {
            return false;
        }
        boolean booleanExtra = intent.getBooleanExtra(ARG_IS_SHOWN_FOR_ACTION_SEND, false);
        Intent intent2 = (Intent) intent.getParcelableExtra(ARG_ACTION_SEND_INTENT);
        boolean isFileTransferDisabled = PTApp.getInstance().isFileTransferDisabled();
        if (!booleanExtra || intent2 == null || isFileTransferDisabled) {
            return false;
        }
        Intent intent3 = new Intent(this, MMShareActivity.class);
        intent3.setAction(intent2.getAction());
        intent3.addCategory("android.intent.category.DEFAULT");
        intent3.setType(intent2.getType());
        intent3.putExtras(intent2);
        ActivityStartHelper.startActivityForeground(this, intent3);
        finish();
        return true;
    }

    private void onCallStatusChanged(long j) {
        switch ((int) j) {
            case 1:
            case 2:
                this.mBtnJoinConf.setVisibility(8);
                this.mBtnReturnToConf.setVisibility(0);
                return;
            default:
                this.mBtnJoinConf.setVisibility(0);
                this.mBtnReturnToConf.setVisibility(8);
                return;
        }
    }

    private String getLoginErrorMessage(long j) {
        int i = (int) j;
        if (i == 1006) {
            return getResources().getString(C4558R.string.zm_alert_auth_token_failed_msg);
        }
        if (i == 2006) {
            return getResources().getString(C4558R.string.zm_rc_alert_meetings_feature_is_not_enabled);
        }
        switch (i) {
            case 1000:
            case 1001:
            case 1002:
                return getResources().getString(C4558R.string.zm_alert_auth_zoom_failed_msg);
            default:
                return getResources().getString(C4558R.string.zm_alert_auth_error_code_msg, new Object[]{Long.valueOf(j)});
        }
    }

    public void onWebAccessFail() {
        showConnecting(false);
        int i = C4558R.string.zm_alert_connect_zoomus_failed_msg;
        if (!this.mLoginFailed && i != 0) {
            this.mLoginFailed = true;
            AuthFailedDialog.show(this, getResources().getString(i));
        }
    }

    public void onDataNetworkStatusChanged(boolean z) {
        if (z && isActive()) {
            if (PTApp.getInstance().isWebSignedOn() || (PTApp.getInstance().hasZoomMessenger() && PTApp.getInstance().autoSignin())) {
                IMActivity.show(this);
                finish();
                return;
            }
            checkAutoLogin();
        }
    }

    public void onClick(View view) {
        if (view == this.mBtnLogin) {
            onClickBtnLogin();
            this.mLoginFailed = false;
        } else if (view == this.mBtnJoinConf) {
            onClickBtnJoinConf();
        } else if (view == this.mBtnReturnToConf) {
            onClickBtnReturnToConf();
        } else if (view == this.mBtnLoginInternational) {
            onClickBtnLoginInternational();
            this.mLoginFailed = false;
        } else if (view == this.mBtnSignup) {
            onClickBtnSignup();
        } else if (view == this.mBtnSettings) {
            onClickBtnSettings();
        }
    }

    private void onClickBtnSettings() {
        SettingFragment.showAsActivity(this, 0, false);
    }

    private int getDefaultVendor() {
        return LoginUtil.getDefaultVendor();
    }

    private void onClickBtnLogin() {
        showLoginUI(getDefaultVendor());
    }

    private void onClickBtnLoginInternational() {
        showLoginUI(0);
    }

    private void onClickBtnSignup() {
        if (ZMBuildConfig.BUILD_TARGET == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(getZoomScheme());
            sb.append("://client/signup");
            UIUtil.openURL(this, sb.toString());
            return;
        }
        String uRLByType = PTApp.getInstance().getURLByType(6);
        if (!StringUtil.isEmptyOrNull(uRLByType)) {
            UIUtil.openURL(this, uRLByType);
        }
    }

    private String getZoomScheme() {
        return getString(C4558R.string.zm_zoom_scheme);
    }

    /* access modifiers changed from: private */
    public void showLoginUI(int i) {
        if (LoginUtil.showLoginUI(this, false, i)) {
            finish();
        }
    }

    private void showIMActivity() {
        Bundle bundle;
        Intent intent = getIntent();
        String str = null;
        if (intent != null) {
            str = intent.getStringExtra(ARG_ACTION_FOR_IM_ACTIVITY);
            bundle = intent.getBundleExtra(ARG_EXTRAS_FOR_IM_ACTIVITY);
        } else {
            bundle = null;
        }
        if (this.mNewVersionReady) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean(IMActivity.ARG_NEW_VERSIONS, true);
        }
        IMActivity.show(this, false, str, bundle);
        finish();
    }

    private void showConnecting(boolean z) {
        int i = 8;
        this.mPanelActions.setVisibility(z ? 8 : 0);
        View view = this.mPanelConnecting;
        if (z) {
            i = 0;
        }
        view.setVisibility(i);
    }

    private void onClickBtnJoinConf() {
        if (UIMgr.isLargeMode(this)) {
            JoinConfFragment.showJoinByNumber(getSupportFragmentManager(), null, null);
        } else {
            JoinConfActivity.showJoinByNumber(this, null, null);
        }
    }

    private void onClickBtnReturnToConf() {
        ConfLocalHelper.returnToConf(this);
    }

    private void showLoginExpired() {
        new Builder(this).setTitle(C4558R.string.zm_msg_login_expired_title).setMessage(C4558R.string.zm_msg_login_expired).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                WelcomeActivity.this.showLoginUI(100);
            }
        }).create().show();
    }

    public void OnShowPrivacyDialog(@Nullable String str, @Nullable String str2) {
        showGDPRConfirmDialog(str, str2);
    }

    public void NotifyUIToLogOut() {
        LogoutHandler.getInstance().startLogout();
        PTUI.getInstance().ClearGDPRConfirmFlag();
        PTUI.getInstance().ClearLoginDisclaimerConfirmFlag();
        showConnecting(false);
        setNeedBlockNextTimeAutoLogin(false);
    }

    private void showGDPRConfirmDialog(@Nullable String str, @Nullable String str2) {
        if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            ZMGDPRConfirmDialog.checkShowDialog(this, 1000, 1, str2, str);
        }
    }

    public void performDialogAction(int i, int i2, Bundle bundle) {
        if (i != 1000) {
            return;
        }
        if (i2 == -1) {
            PTUI.getInstance().ClearGDPRConfirmFlag();
            PTApp.getInstance().confirmGDPR(true);
        } else if (i2 == -2) {
            PTApp.getInstance().confirmGDPR(false);
        } else if (i2 == 1) {
            PTApp.getInstance().confirmGDPR(false);
            ZMGDPRConfirmDialog.dismiss(getSupportFragmentManager());
        }
    }

    private void initViewPager() {
        this.mVpViews = new ArrayList();
        int i = 0;
        while (true) {
            int[] iArr = this.title;
            if (i < iArr.length) {
                this.mVpViews.add(getVpView(iArr[i], this.msg[i], this.img[i]));
                i++;
            } else {
                this.mAdapter = new WlcViewPagerAdapter(this.mVpViews);
                this.mWlcViewpager.setAdapter(this.mAdapter);
                this.mWlcViewpager.addOnPageChangeListener(new WlcPageIndicator(this, this.mVpIndexer, this.mAdapter.getCount()));
                return;
            }
        }
    }

    private View getVpView(int i, int i2, int i3) {
        getLayoutInflater();
        View inflate = LayoutInflater.from(this).inflate(C4558R.layout.zm_wlc_viewpage, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtMsg);
        ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.wlcImg);
        ((TextView) inflate.findViewById(C4558R.C4560id.txtTitle)).setText(i);
        textView.setText(i2);
        imageView.setImageResource(i3);
        return inflate;
    }
}

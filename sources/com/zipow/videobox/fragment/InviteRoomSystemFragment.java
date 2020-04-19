package com.zipow.videobox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.view.RoomSystemCallInView;
import com.zipow.videobox.view.RoomSystemCallOutView;
import com.zipow.videobox.view.RoomSystemCallViewListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class InviteRoomSystemFragment extends ZMDialogFragment implements TabContentFactory, RoomSystemCallViewListener, OnClickListener, ExtListener {
    private static final String CALL_IN_INFO = "call_in_info";
    private static final String CALL_OUT_INFO = "call_out_info";
    private static final String CURRENT_TAB = "current_tab";
    private static final String TAB_CALL_IN = "call_in";
    private static final String TAB_CALL_OUT = "call_out";
    private View mBtnClose;
    @Nullable
    private Bundle mCallInSaveState;
    @Nullable
    private RoomSystemCallInView mCallInView;
    @Nullable
    private Bundle mCallOutSaveState;
    @Nullable
    private RoomSystemCallOutView mCallOutView;
    private boolean mDisableBackPress = false;
    private boolean mStarted = false;
    private TabHost mTabHost;

    public void onKeyboardClosed() {
    }

    public void onKeyboardOpen() {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@Nullable ZMActivity zMActivity, @Nullable Bundle bundle, int i) {
        if (zMActivity != null) {
            SimpleActivity.show(zMActivity, InviteRoomSystemFragment.class.getName(), bundle == null ? new Bundle() : bundle, i, true, 1);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        String str;
        View inflate = layoutInflater.inflate(C4558R.layout.zm_invite_room_system_view, viewGroup, false);
        this.mTabHost = (TabHost) inflate.findViewById(16908306);
        this.mBtnClose = inflate.findViewById(C4558R.C4560id.btnClose);
        this.mBtnClose.setOnClickListener(this);
        if (bundle != null) {
            str = bundle.getString(CURRENT_TAB);
            this.mCallInSaveState = bundle.getBundle(CALL_IN_INFO);
            this.mCallOutSaveState = bundle.getBundle(CALL_OUT_INFO);
        } else {
            str = null;
        }
        setupTabHost(this.mTabHost, layoutInflater);
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mTabHost.setCurrentTabByTag(str);
        }
        return inflate;
    }

    public void onResume() {
        super.onResume();
        RoomSystemCallInView roomSystemCallInView = this.mCallInView;
        if (roomSystemCallInView != null) {
            roomSystemCallInView.initialUIListener();
        }
        RoomSystemCallOutView roomSystemCallOutView = this.mCallOutView;
        if (roomSystemCallOutView != null) {
            roomSystemCallOutView.initialUIListener();
        }
        this.mStarted = true;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        String str = "";
        TabHost tabHost = this.mTabHost;
        if (tabHost != null) {
            str = tabHost.getCurrentTabTag();
        }
        if (!StringUtil.isEmptyOrNull(str)) {
            bundle.putString(CURRENT_TAB, str);
            RoomSystemCallInView roomSystemCallInView = this.mCallInView;
            if (roomSystemCallInView != null) {
                bundle.putBundle(CALL_IN_INFO, roomSystemCallInView.getSaveInstanceState());
            }
            RoomSystemCallOutView roomSystemCallOutView = this.mCallOutView;
            if (roomSystemCallOutView != null) {
                bundle.putBundle(CALL_OUT_INFO, roomSystemCallOutView.getSaveInstanceState());
            }
        }
    }

    private void setupTabHost(TabHost tabHost, @NonNull LayoutInflater layoutInflater) {
        tabHost.setup();
        tabHost.addTab(this.mTabHost.newTabSpec(TAB_CALL_IN).setIndicator(createCallInTabView(layoutInflater)).setContent(this));
        tabHost.addTab(this.mTabHost.newTabSpec(TAB_CALL_OUT).setIndicator(createCallOutTabView(layoutInflater)).setContent(this));
    }

    private View createCallInTabView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_tab_indicator_top, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.title);
        inflate.findViewById(C4558R.C4560id.icon).setVisibility(8);
        textView.setText(C4558R.string.zm_room_system_title_call_in);
        inflate.setBackgroundResource(C4558R.C4559drawable.zm_tab_indicator_top_first);
        inflate.setMinimumWidth(UIUtil.dip2px(getActivity(), 100.0f));
        return inflate;
    }

    private View createCallOutTabView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_tab_indicator_top, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.title);
        inflate.findViewById(C4558R.C4560id.icon).setVisibility(8);
        textView.setText(C4558R.string.zm_room_system_title_call_out);
        inflate.setBackgroundResource(C4558R.C4559drawable.zm_tab_indicator_top_last);
        inflate.setMinimumWidth(UIUtil.dip2px(getActivity(), 100.0f));
        return inflate;
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onDestroy() {
        super.onDestroy();
        RoomSystemCallInView roomSystemCallInView = this.mCallInView;
        if (roomSystemCallInView != null) {
            roomSystemCallInView.destroy();
        }
        RoomSystemCallOutView roomSystemCallOutView = this.mCallOutView;
        if (roomSystemCallOutView != null) {
            roomSystemCallOutView.destroy();
        }
    }

    @Nullable
    public View createTabContent(String str) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return null;
        }
        if (TAB_CALL_IN.equals(str)) {
            this.mCallInView = new RoomSystemCallInView((Context) activity, this.mCallInSaveState);
            this.mCallInView.setListener(this);
            if (this.mStarted) {
                this.mCallInView.initialUIListener();
            }
            return this.mCallInView;
        } else if (!TAB_CALL_OUT.equals(str)) {
            return null;
        } else {
            this.mCallOutView = new RoomSystemCallOutView((Context) activity, this.mCallOutSaveState);
            this.mCallOutView.setListener(this);
            if (this.mStarted) {
                this.mCallOutView.initialUIListener();
            }
            return this.mCallOutView;
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null && view == this.mBtnClose) {
            dismiss();
        }
    }

    public void onConnected(final boolean z) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(IUIElement iUIElement) {
                InviteRoomSystemFragment inviteRoomSystemFragment = (InviteRoomSystemFragment) iUIElement;
                if (z) {
                    inviteRoomSystemFragment.enableClose(true);
                }
                inviteRoomSystemFragment.dismiss();
            }
        });
    }

    public void onConnecting(final boolean z) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(IUIElement iUIElement) {
                InviteRoomSystemFragment inviteRoomSystemFragment = (InviteRoomSystemFragment) iUIElement;
                if (z) {
                    inviteRoomSystemFragment.enableClose(false);
                }
            }
        });
    }

    public void onFailed(final boolean z) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(IUIElement iUIElement) {
                InviteRoomSystemFragment inviteRoomSystemFragment = (InviteRoomSystemFragment) iUIElement;
                if (z) {
                    inviteRoomSystemFragment.enableClose(true);
                }
            }
        });
    }

    public void onCancel(final boolean z) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(IUIElement iUIElement) {
                InviteRoomSystemFragment inviteRoomSystemFragment = (InviteRoomSystemFragment) iUIElement;
                if (z) {
                    inviteRoomSystemFragment.enableClose(true);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void enableClose(boolean z) {
        this.mBtnClose.setEnabled(z);
        this.mDisableBackPress = !z;
    }

    public boolean onBackPressed() {
        return this.mDisableBackPress;
    }
}

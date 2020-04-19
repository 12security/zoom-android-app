package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.ConfChatViewOld;
import com.zipow.videobox.view.ConfChatViewOld.Listener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfChatFragmentOld extends ZMDialogFragment implements Listener {
    public static final String ARG_USERID = "userId";
    @Nullable
    private ConfChatViewOld mChatView;
    private IConfUIListener mConfUIListener;

    public static void showDialog(@NonNull FragmentManager fragmentManager, long j) {
        if (getConfChatFragment(fragmentManager) == null) {
            ConfChatFragmentOld confChatFragmentOld = new ConfChatFragmentOld();
            Bundle bundle = new Bundle();
            bundle.putLong("userId", j);
            confChatFragmentOld.setArguments(bundle);
            confChatFragmentOld.show(fragmentManager, ConfChatFragmentOld.class.getName());
        }
    }

    public static void showActivity(ZMActivity zMActivity, long j) {
        ConfChatFragmentOld confChatFragmentOld = new ConfChatFragmentOld();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", j);
        confChatFragmentOld.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, confChatFragmentOld, ConfChatFragmentOld.class.getName()).commit();
    }

    @Nullable
    public static ConfChatFragmentOld getConfChatFragment(FragmentManager fragmentManager) {
        return (ConfChatFragmentOld) fragmentManager.findFragmentByTag(ConfChatFragmentOld.class.getName());
    }

    public ConfChatFragmentOld() {
        setStyle(1, C4558R.style.ZMDialog);
    }

    public long getUserId() {
        ConfChatViewOld confChatViewOld = this.mChatView;
        if (confChatViewOld == null) {
            return 0;
        }
        return confChatViewOld.getUserId();
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onUserEvent(int i, long j, int i2) {
                    return ConfChatFragmentOld.this.onUserEvent(i, j, i2);
                }

                public boolean onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
                    return ConfChatFragmentOld.this.onChatMessageReceived(str, j, str2, j2, str3, str4, j3);
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mChatView.loadData(arguments.getLong("userId", 0));
        }
        if (getActivity() instanceof ConfActivity) {
            ((ConfActivity) getActivity()).refreshUnreadChatCount();
        }
        ConfChatViewOld confChatViewOld = this.mChatView;
        if (confChatViewOld != null) {
            confChatViewOld.scrollToBottom(true);
        }
    }

    public void onPause() {
        super.onPause();
        ConfUI.getInstance().removeListener(this.mConfUIListener);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mChatView = new ConfChatViewOld(getActivity());
        this.mChatView.setListener(this);
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return this.mChatView;
        }
        if (UIMgr.isLargeMode(activity)) {
            LinearLayout linearLayout = (LinearLayout) this.mChatView.findViewById(C4558R.C4560id.chatView);
            int dip2px = UIUtil.dip2px(activity, 600.0f);
            if (UIUtil.getDisplayWidth(activity) < dip2px) {
                dip2px = UIUtil.getDisplayWidth(activity);
            }
            linearLayout.setLayoutParams(new LayoutParams(dip2px, UIUtil.getDisplayHeight(activity) / 2));
        }
        return this.mChatView;
    }

    /* access modifiers changed from: private */
    public boolean onUserEvent(int i, long j, int i2) {
        switch (i) {
            case 1:
                if (this.mChatView != null) {
                    CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                    if (confStatusObj != null && confStatusObj.isSameUser(j, this.mChatView.getUserId())) {
                        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                            public void run(@NonNull IUIElement iUIElement) {
                                ((ConfChatFragmentOld) iUIElement).dismiss();
                            }
                        });
                        break;
                    }
                } else {
                    return true;
                }
            case 2:
                getNonNullEventTaskManagerOrThrowException().push("reloadData", new EventAction() {
                    public void run(IUIElement iUIElement) {
                        ConfChatFragmentOld confChatFragmentOld = (ConfChatFragmentOld) iUIElement;
                        if (confChatFragmentOld != null) {
                            confChatFragmentOld.handleUserUpdate();
                        }
                    }
                });
                break;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void handleUserUpdate() {
        ConfChatViewOld confChatViewOld = this.mChatView;
        if (confChatViewOld != null) {
            confChatViewOld.reloadData();
        }
    }

    /* access modifiers changed from: private */
    public boolean onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity == null) {
            return false;
        }
        if (zMActivity.isActive()) {
            this.mChatView.onChatMessageReceived(str, j, str2, j2, str3, str4, j3);
        }
        if (zMActivity instanceof ConfActivity) {
            ((ConfActivity) getActivity()).refreshUnreadChatCount();
        }
        return true;
    }

    public void onClickBack() {
        dismiss();
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }
}

package com.zipow.videobox.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.view.JoinConfView;
import com.zipow.videobox.view.JoinConfView.Listener;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class JoinConfFragment extends ZMDialogFragment implements Listener, IPTUIListener {
    public static final String ARG_CONF_NUMBER = "hangoutNumber";
    public static final String ARG_SCREEN_NAME = "screenName";
    public static final String ARG_URL_ACTION = "urlAction";
    @Nullable
    private JoinConfView mView;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static void showJoinByNumber(FragmentManager fragmentManager, String str, String str2) {
        JoinConfFragment joinConfFragment = new JoinConfFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hangoutNumber", str);
        bundle.putString("screenName", str2);
        joinConfFragment.setArguments(bundle);
        joinConfFragment.show(fragmentManager, JoinConfFragment.class.getName());
    }

    public static void showJoinByUrl(FragmentManager fragmentManager, String str) {
        JoinConfFragment joinConfFragment = new JoinConfFragment();
        Bundle bundle = new Bundle();
        bundle.putString("urlAction", str);
        joinConfFragment.setArguments(bundle);
        joinConfFragment.show(fragmentManager, JoinConfFragment.class.getName());
    }

    @Nullable
    public static JoinConfFragment getJoinConfFragment(FragmentManager fragmentManager) {
        return (JoinConfFragment) fragmentManager.findFragmentByTag(JoinConfFragment.class.getName());
    }

    public JoinConfFragment() {
        setStyle(1, C4558R.style.ZMDialog);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mView = new JoinConfView(getActivity());
        this.mView.setListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString("hangoutNumber");
            String string2 = arguments.getString("screenName");
            String string3 = arguments.getString("urlAction");
            if (string != null && string.length() > 0) {
                this.mView.setConfNumber(string);
            } else if (string3 != null && string3.length() > 0) {
                this.mView.setUrlAction(string3);
            }
            if (string2 != null && string2.length() > 0) {
                this.mView.setScreenName(string2);
            }
        }
        PTUI.getInstance().addPTUIListener(this);
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return this.mView;
        }
        float displayHeightInDip = UIUtil.getDisplayHeightInDip(activity);
        if ((!UIUtil.isPortraitMode(activity) || displayHeightInDip <= 540.0f) && !UIUtil.isLargeScreen(activity)) {
            getActivity().getWindow().setSoftInputMode(2);
        } else {
            getActivity().getWindow().setSoftInputMode(4);
        }
        return this.mView;
    }

    public void onDestroyView() {
        PTUI.getInstance().removePTUIListener(this);
        super.onDestroyView();
    }

    public void onJoinByNumber(long j, String str, String str2, boolean z, boolean z2) {
        finish(true);
        ConfActivity.joinById(getActivity(), j, str, str2, null, z, z2);
    }

    public void onJoinByUrl(String str, String str2) {
        finish(true);
        ConfActivity.joinByUrl(getActivity(), str, str2);
    }

    public void onBack() {
        finish(false);
    }

    private void finish(boolean z) {
        if (getShowsDialog()) {
            dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
            activity.overridePendingTransition(C4558R.anim.zm_fade_in, C4558R.anim.zm_slide_out_bottom);
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (getShowsDialog()) {
            FragmentActivity activity = getActivity();
            if (activity instanceof JoinByURLActivity) {
                activity.finish();
            }
        }
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 22) {
            JoinConfView joinConfView = this.mView;
            if (joinConfView != null) {
                joinConfView.updateUIForCallStatus(j);
            }
        }
    }
}

package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class ToastTip extends ZMTipFragment {
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_POSITION = "position";
    private static final String TAG = "ToastTip";

    public static void showNow(@Nullable FragmentManager fragmentManager, String str, long j, boolean z) {
        if (z) {
            showNow(fragmentManager, str, 1, j);
        } else {
            showNow(fragmentManager, str, 2, j);
        }
    }

    public static void showNow(@Nullable FragmentManager fragmentManager, String str, int i, long j) {
        if (fragmentManager != null && !StringUtil.isEmptyOrNull(str)) {
            dismiss(fragmentManager);
            Bundle bundle = new Bundle();
            bundle.putString("message", str);
            bundle.putInt(ARG_POSITION, i);
            ToastTip toastTip = new ToastTip();
            toastTip.setArguments(bundle);
            toastTip.show(fragmentManager, ToastTip.class.getName(), j);
        }
    }

    public static boolean isShown(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        if (((ToastTip) fragmentManager.findFragmentByTag(ToastTip.class.getName())) != null) {
            z = true;
        }
        return z;
    }

    public static boolean dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        try {
            fragmentManager.executePendingTransactions();
        } catch (Exception unused) {
        }
        ToastTip toastTip = (ToastTip) fragmentManager.findFragmentByTag(ToastTip.class.getName());
        if (toastTip == null) {
            return false;
        }
        toastTip.dismiss();
        return true;
    }

    public ZMTip onCreateTip(@NonNull Context context, @NonNull LayoutInflater layoutInflater, Bundle bundle) {
        int i;
        View inflate = layoutInflater.inflate(C4558R.layout.zm_join_leave_wait_tip, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        Bundle arguments = getArguments();
        if (textView == null || arguments == null) {
            i = 2;
        } else {
            i = arguments.getInt(ARG_POSITION);
            textView.setText(StringUtil.safeString(arguments.getString("message")));
        }
        ZMTip zMTip = new ZMTip(context);
        zMTip.setShadow(0.0f, 0, 0, 0);
        zMTip.setBackgroundColor(-16777216);
        zMTip.setBorderColor(0);
        zMTip.setArrowSize(0, 0);
        zMTip.setCornerArcSize(0);
        zMTip.setOverlyingType(i);
        zMTip.addView(inflate, new LayoutParams(UIUtil.getDisplayWidth(context), context.getResources().getDimensionPixelSize(C4558R.dimen.zm_padding_largest)));
        return zMTip;
    }
}

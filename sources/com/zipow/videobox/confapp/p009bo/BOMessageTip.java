package com.zipow.videobox.confapp.p009bo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.confapp.bo.BOMessageTip */
public class BOMessageTip extends ZMTipFragment {
    private static final String ARGS_BOMESSAGE_TIP_INDEX = "args_bomessage_tip_index";
    private static final String ARGS_MESSAGE = "args_message";
    private static final int MAX_BO_MESSAGE_NUM = 4;
    private static int gChatTipIndex;

    /* renamed from: com.zipow.videobox.confapp.bo.BOMessageTip$BOMessageTipComparator */
    public static class BOMessageTipComparator implements Comparator<BOMessageTip> {
        public int compare(@NonNull BOMessageTip bOMessageTip, @NonNull BOMessageTip bOMessageTip2) {
            Bundle arguments = bOMessageTip.getArguments();
            long j = 0;
            long j2 = arguments != null ? (long) arguments.getInt(BOMessageTip.ARGS_BOMESSAGE_TIP_INDEX, 0) : 0;
            Bundle arguments2 = bOMessageTip2.getArguments();
            if (arguments2 != null) {
                j = (long) arguments2.getInt(BOMessageTip.ARGS_BOMESSAGE_TIP_INDEX, 0);
            }
            return (int) (j2 - j);
        }
    }

    public static void show(@Nullable FragmentManager fragmentManager, long j, String str) {
        if (fragmentManager != null && !StringUtil.isEmptyOrNull(str)) {
            dismissExceedMessageTips(fragmentManager);
            Bundle bundle = new Bundle();
            bundle.putString(ARGS_MESSAGE, str);
            String str2 = ARGS_BOMESSAGE_TIP_INDEX;
            int i = gChatTipIndex;
            gChatTipIndex = i + 1;
            bundle.putInt(str2, i);
            BOMessageTip bOMessageTip = new BOMessageTip();
            bOMessageTip.setArguments(bundle);
            bOMessageTip.show(fragmentManager, BOMessageTip.class.getName(), j);
            fragmentManager.executePendingTransactions();
        }
    }

    private static void dismissExceedMessageTips(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            ArrayList arrayList = new ArrayList();
            int i = 0;
            for (Fragment fragment : fragments) {
                if (fragment instanceof BOMessageTip) {
                    i++;
                    arrayList.add((BOMessageTip) fragment);
                }
            }
            if (i > 4) {
                Collections.sort(arrayList, new BOMessageTipComparator());
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    ((BOMessageTip) arrayList.get(i2)).dismiss();
                    i--;
                    if (i < 4) {
                        break;
                    }
                }
            }
        }
    }

    public static boolean isShown(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        if (((BOMessageTip) fragmentManager.findFragmentByTag(BOMessageTip.class.getName())) != null) {
            z = true;
        }
        return z;
    }

    public static boolean dismissAll(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment instanceof BOMessageTip) {
                ((BOMessageTip) fragment).dismiss();
                z = true;
            }
        }
        return z;
    }

    public ZMTip onCreateTip(@NonNull Context context, @NonNull LayoutInflater layoutInflater, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_bo_message_tip, null);
        ((TextView) inflate.findViewById(C4558R.C4560id.txtMessage)).setText(getArguments().getString(ARGS_MESSAGE));
        ZMTip zMTip = new ZMTip(context);
        zMTip.addView(inflate);
        zMTip.setBackgroundColor(context.getResources().getColor(C4558R.color.zm_message_tip_background));
        zMTip.setBorderColor(context.getResources().getColor(C4558R.color.zm_message_tip_border));
        zMTip.setShadow(4.0f, 0, 0, context.getResources().getColor(C4558R.color.zm_message_tip_shadow));
        return zMTip;
    }
}

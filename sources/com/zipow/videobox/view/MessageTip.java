package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class MessageTip extends ZMTipFragment implements OnClickListener {
    public static final String ARG_ANCHOR_ID = "anchorId";
    public static final String ARG_AVATAR = "avatar";
    public static final String ARG_MESSAGE = "message";
    public static final String ARG_MESSAGE_TIP_INDEX = "chatTipIndex";
    public static final String ARG_TITLE = "title";
    private static final int MAX_MESSAGE_TIP_NUMBER = 4;
    private static int gMessageTipIndex;

    public static class MessageTipComparator implements Comparator<MessageTip> {
        public int compare(@NonNull MessageTip messageTip, @NonNull MessageTip messageTip2) {
            Bundle arguments = messageTip.getArguments();
            long j = 0;
            long j2 = arguments != null ? (long) arguments.getInt("chatTipIndex", 0) : 0;
            Bundle arguments2 = messageTip2.getArguments();
            if (arguments2 != null) {
                j = (long) arguments2.getInt("chatTipIndex", 0);
            }
            return (int) (j2 - j);
        }
    }

    public static void show(@Nullable FragmentManager fragmentManager, String str, String str2, String str3, int i) {
        if (fragmentManager != null) {
            dismissExceedMessageTips(fragmentManager);
            Bundle bundle = new Bundle();
            bundle.putString("avatar", str);
            bundle.putString("title", str2);
            bundle.putString("message", str3);
            bundle.putInt("anchorId", i);
            int i2 = gMessageTipIndex;
            gMessageTipIndex = i2 + 1;
            bundle.putInt("chatTipIndex", i2);
            MessageTip messageTip = new MessageTip();
            messageTip.setArguments(bundle);
            messageTip.show(fragmentManager, MessageTip.class.getName(), 6000);
            fragmentManager.executePendingTransactions();
        }
    }

    private static void dismissExceedMessageTips(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        for (Fragment fragment : fragments) {
            if (fragment instanceof MessageTip) {
                i2++;
                arrayList.add((MessageTip) fragment);
            }
        }
        if (i2 >= 4) {
            Collections.sort(arrayList, new MessageTipComparator());
            while (i < arrayList.size()) {
                ((MessageTip) arrayList.get(i)).dismiss();
                i2--;
                if (i2 >= 4) {
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public static boolean isShown(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        if (((MessageTip) fragmentManager.findFragmentByTag(MessageTip.class.getName())) != null) {
            z = true;
        }
        return z;
    }

    public static boolean dismiss(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment instanceof MessageTip) {
                ((MessageTip) fragment).dismiss();
                z = true;
            }
        }
        return z;
    }

    public ZMTip onCreateTip(@NonNull Context context, @NonNull LayoutInflater layoutInflater, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_message_tip, null);
        View findViewById = inflate.findViewById(C4558R.C4560id.content);
        AvatarView avatarView = (AvatarView) inflate.findViewById(C4558R.C4560id.avatarView);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        Bundle arguments = getArguments();
        avatarView.show(new ParamsBuilder().setPath(arguments.getString("avatar")));
        textView.setText(arguments.getString("title"));
        textView2.setText(arguments.getString("message"));
        ZMTip zMTip = new ZMTip(context);
        zMTip.addView(inflate);
        int i = arguments.getInt("anchorId", 0);
        if (i > 0) {
            View findViewById2 = getActivity().findViewById(i);
            if (findViewById2 != null) {
                zMTip.setAnchor(findViewById2, UIMgr.isLargeMode(getActivity()) ? 1 : 3);
            }
        }
        findViewById.setOnClickListener(this);
        zMTip.setBackgroundColor(context.getResources().getColor(C4558R.color.zm_message_tip_background));
        zMTip.setBorderColor(context.getResources().getColor(C4558R.color.zm_message_tip_border));
        zMTip.setShadow(4.0f, 0, 0, context.getResources().getColor(C4558R.color.zm_message_tip_shadow));
        return zMTip;
    }

    public void onClick(View view) {
        dismiss();
    }
}

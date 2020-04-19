package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.fragment.WebinarRaiseHandFragment;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.video.MeetingReactionMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class RaiseHandTip extends ZMTipFragment implements OnClickListener {
    public static final String ARG_ANCHOR_ID = "anchorId";
    public static final String ARG_MESSAGE = "message";
    public static int sHeight;
    public static int sWidth;

    public static void showTip(@Nullable FragmentManager fragmentManager, String str, int i) {
        if (fragmentManager != null) {
            RaiseHandTip raiseHandTip = (RaiseHandTip) fragmentManager.findFragmentByTag(RaiseHandTip.class.getName());
            if (raiseHandTip != null) {
                raiseHandTip.dismiss();
            }
            Bundle bundle = new Bundle();
            bundle.putString("message", str);
            bundle.putInt("anchorId", i);
            RaiseHandTip raiseHandTip2 = new RaiseHandTip();
            raiseHandTip2.setArguments(bundle);
            raiseHandTip2.show(fragmentManager, RaiseHandTip.class.getName());
        }
    }

    public static boolean isShown(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        if (((RaiseHandTip) fragmentManager.findFragmentByTag(RaiseHandTip.class.getName())) != null) {
            z = true;
        }
        return z;
    }

    public static boolean dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        RaiseHandTip raiseHandTip = (RaiseHandTip) fragmentManager.findFragmentByTag(RaiseHandTip.class.getName());
        if (raiseHandTip == null) {
            return false;
        }
        raiseHandTip.dismiss();
        return true;
    }

    public ZMTip onCreateTip(@NonNull Context context, @NonNull LayoutInflater layoutInflater, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_raisehand_tip, null);
        refreshMeetingReactionPos(inflate);
        View findViewById = inflate.findViewById(C4558R.C4560id.content);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        Bundle arguments = getArguments();
        String string = arguments.getString("message");
        if (!StringUtil.isEmptyOrNull(string)) {
            textView.setVisibility(0);
            textView.setText(string);
        } else {
            textView.setVisibility(8);
        }
        this.mAutoFocus = false;
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
        zMTip.setCornerArcSize(UIUtil.dip2px(context, 10.0f));
        zMTip.setLayoutGravity(3, UIUtil.dip2px(context, 60.0f));
        zMTip.setBackgroundColor(context.getResources().getColor(C4558R.color.zm_message_tip_background));
        zMTip.setBorderColor(context.getResources().getColor(C4558R.color.zm_message_tip_border));
        zMTip.setShadow(4.0f, 0, 0, context.getResources().getColor(C4558R.color.zm_message_tip_shadow));
        if (!StringUtil.isEmptyOrNull(string)) {
            textView.setContentDescription(getString(C4558R.string.zm_accessibility_raised_hand_description_23051, string));
        }
        return zMTip;
    }

    public void onClick(View view) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return;
        }
        if (myself.isHost() || myself.isCoHost()) {
            showRaiseHand();
        }
    }

    private void showRaiseHand() {
        FragmentActivity activity = getActivity();
        if (activity instanceof ZMActivity) {
            WebinarRaiseHandFragment.showAsActivity((ZMActivity) activity, 0);
        }
    }

    private void refreshMeetingReactionPos(@NonNull View view) {
        if (sHeight == 0 || sWidth == 0) {
            view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
            sHeight = view.getMeasuredHeight();
            sWidth = view.getMeasuredWidth();
        }
        view.post(new Runnable() {
            public void run() {
                MeetingReactionMgr.getInstance().refreshMainVideoEmojiPos();
            }
        });
    }
}

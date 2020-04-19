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
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class FeccMessageButtonTip extends ZMTipFragment implements OnClickListener {
    private TextView mTxtButton;
    private long mUserId;

    public static void show(FragmentManager fragmentManager, String str, long j, String str2, String str3) {
        show(fragmentManager, str, j, str2, str3, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, long j, String str2, String str3, long j2) {
        show(fragmentManager, str, j, -1, 0, str2, str3, 0, 0, j2);
    }

    public static void show(FragmentManager fragmentManager, String str, long j, int i, int i2, String str2, String str3) {
        show(fragmentManager, str, j, i, i2, str2, str3, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, long j, int i, int i2, String str2, String str3, long j2) {
        show(fragmentManager, str, j, i, i2, str2, str3, 0, 0, j2);
    }

    public static void show(FragmentManager fragmentManager, String str, long j, String str2, String str3, int i, int i2) {
        show(fragmentManager, str, j, -1, 0, str2, str3, i, i2, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, long j, int i, int i2, String str2, String str3, int i3, int i4, long j2) {
        Bundle bundle = new Bundle();
        bundle.putLong("userId", j);
        bundle.putString("message", str2);
        bundle.putString("button", str3);
        bundle.putInt("anchor", i3);
        bundle.putInt(VideoTip.ARG_ARROW_DIRECTION, i4);
        bundle.putInt("gravity", i);
        bundle.putInt("padding", i2);
        FeccMessageButtonTip feccMessageButtonTip = new FeccMessageButtonTip();
        feccMessageButtonTip.setArguments(bundle);
        feccMessageButtonTip.show(fragmentManager, str, j2);
    }

    public static boolean hasTip(FragmentManager fragmentManager, String str) {
        return fragmentManager.findFragmentByTag(str) instanceof FeccMessageButtonTip;
    }

    public static boolean dismiss(FragmentManager fragmentManager, String str) {
        Fragment findFragmentByTag = fragmentManager.findFragmentByTag(str);
        if (!(findFragmentByTag instanceof FeccMessageButtonTip)) {
            return false;
        }
        ((FeccMessageButtonTip) findFragmentByTag).dismiss();
        return true;
    }

    @Nullable
    public ZMTip onCreateTip(@NonNull Context context, @NonNull LayoutInflater layoutInflater, Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return null;
        }
        this.mUserId = arguments.getLong("userId");
        String string = arguments.getString("message");
        String string2 = arguments.getString("button");
        int i = arguments.getInt("anchor");
        int i2 = arguments.getInt(VideoTip.ARG_ARROW_DIRECTION);
        int i3 = arguments.getInt("gravity", -1);
        int i4 = arguments.getInt("padding", 0);
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fecc_message_button_tip, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        this.mTxtButton = (TextView) inflate.findViewById(C4558R.C4560id.txtButton);
        TextView textView2 = this.mTxtButton;
        if (textView2 != null) {
            textView2.setOnClickListener(this);
        }
        textView.setFocusable(false);
        textView.setText(string);
        if (StringUtil.isEmptyOrNull(string2)) {
            this.mTxtButton.setVisibility(8);
        } else {
            this.mTxtButton.setText(string2);
        }
        View findViewById = getActivity().findViewById(i);
        ZMTip zMTip = new ZMTip(context);
        zMTip.setLayoutGravity(i3, i4);
        zMTip.addView(inflate);
        zMTip.setAnchor(findViewById, i2);
        zMTip.setBackgroundColor(context.getResources().getColor(C4558R.color.zm_message_tip_background));
        zMTip.setBorderColor(context.getResources().getColor(C4558R.color.zm_message_tip_border));
        zMTip.setShadow(4.0f, 0, 0, context.getResources().getColor(C4558R.color.zm_message_tip_shadow));
        return zMTip;
    }

    public void onClick(View view) {
        if (view == this.mTxtButton) {
            onClickTextButton();
        }
    }

    private void onClickTextButton() {
        dismiss();
        ZMConfComponentMgr.getInstance().sinkInFeccUserApproved(this.mUserId);
    }
}

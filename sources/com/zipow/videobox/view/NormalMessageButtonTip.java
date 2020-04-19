package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class NormalMessageButtonTip extends ZMTipFragment implements OnClickListener {
    private TextView mTxtButton;

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3) {
        show(fragmentManager, str, "", str2, str3, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3, long j) {
        show(fragmentManager, str, "", str2, str3, j);
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3, String str4) {
        show(fragmentManager, str, str2, str3, str4, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3, String str4, long j) {
        show(fragmentManager, str, -1, 0, str2, str3, str4, 0, 0, j);
    }

    public static void show(FragmentManager fragmentManager, String str, int i, int i2, String str2, String str3) {
        show(fragmentManager, str, i, i2, "", str2, str3, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, int i, int i2, String str2, String str3, String str4) {
        show(fragmentManager, str, i, i2, str2, str3, str4, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, int i, int i2, String str2, String str3, String str4, long j) {
        show(fragmentManager, str, i, i2, str2, str3, str4, 0, 0, j);
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3, String str4, int i, int i2) {
        show(fragmentManager, str, -1, 0, str2, str3, str4, i, i2, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, int i, int i2, String str2, String str3, String str4, int i3, int i4, long j) {
        Bundle bundle = new Bundle();
        bundle.putString("title", str2);
        bundle.putString("message", str3);
        bundle.putString("button", str4);
        bundle.putInt("anchor", i3);
        bundle.putInt(VideoTip.ARG_ARROW_DIRECTION, i4);
        bundle.putInt("gravity", i);
        bundle.putInt("padding", i2);
        NormalMessageButtonTip normalMessageButtonTip = new NormalMessageButtonTip();
        normalMessageButtonTip.setArguments(bundle);
        normalMessageButtonTip.show(fragmentManager, str, j);
    }

    public static boolean hasTip(FragmentManager fragmentManager, String str) {
        return fragmentManager.findFragmentByTag(str) instanceof NormalMessageButtonTip;
    }

    public static boolean dismiss(FragmentManager fragmentManager, String str) {
        Fragment findFragmentByTag = fragmentManager.findFragmentByTag(str);
        if (!(findFragmentByTag instanceof NormalMessageButtonTip)) {
            return false;
        }
        ((NormalMessageButtonTip) findFragmentByTag).dismiss();
        return true;
    }

    public ZMTip onCreateTip(@NonNull Context context, @NonNull LayoutInflater layoutInflater, Bundle bundle) {
        Bundle arguments = getArguments();
        String string = arguments.getString("title");
        String string2 = arguments.getString("message");
        String string3 = arguments.getString("button");
        int i = arguments.getInt("anchor");
        int i2 = arguments.getInt(VideoTip.ARG_ARROW_DIRECTION);
        int i3 = arguments.getInt("gravity", -1);
        int i4 = arguments.getInt("padding", 0);
        View inflate = layoutInflater.inflate(C4558R.layout.zm_normal_message_button_tip, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        this.mTxtButton = (TextView) inflate.findViewById(C4558R.C4560id.txtButton);
        TextView textView3 = this.mTxtButton;
        if (textView3 != null) {
            textView3.setOnClickListener(this);
        }
        textView.setFocusable(false);
        textView2.setFocusable(false);
        if (StringUtil.isEmptyOrNull(string)) {
            textView.setVisibility(8);
        } else {
            textView.setText(string);
        }
        textView2.setText(string2);
        if (StringUtil.isEmptyOrNull(string3)) {
            this.mTxtButton.setVisibility(8);
        } else {
            this.mTxtButton.setText(string3);
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
    }
}

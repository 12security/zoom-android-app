package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class NormalMessageTip extends ZMTipFragment {
    public static final int DEFAULT_TIP_SHOW_DURATION = 3000;

    public static void show(@NonNull Context context, FragmentManager fragmentManager, String str, int i, int i2) {
        show(fragmentManager, str, i > -1 ? context.getString(i) : null, i2 > -1 ? context.getString(i2) : null, 0, 0);
    }

    public static void show(@NonNull Context context, FragmentManager fragmentManager, String str, int i, int i2, int i3, int i4) {
        Context context2 = context;
        int i5 = i;
        int i6 = i2;
        show(fragmentManager, str, i5 > -1 ? context.getString(i5) : null, i6 > -1 ? context.getString(i6) : null, 0, i3, i4, 0);
    }

    public static void show(@NonNull Context context, FragmentManager fragmentManager, String str, int i, int i2, long j) {
        Context context2 = context;
        int i3 = i;
        int i4 = i2;
        show(fragmentManager, str, i3 > -1 ? context.getString(i3) : null, i4 > -1 ? context.getString(i4) : null, 0, 0, 0, j);
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3) {
        show(fragmentManager, str, str2, str3, 0, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3, int i, int i2) {
        show(fragmentManager, str, str2, str3, 0, i, i2, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3, long j) {
        show(fragmentManager, str, str2, str3, 0, 0, 0, j);
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3, int i, int i2, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putString("title", str2);
        bundle.putString("message", str3);
        bundle.putInt("icon", 0);
        bundle.putInt("anchor", i);
        bundle.putInt(VideoTip.ARG_ARROW_DIRECTION, i2);
        bundle.putBoolean("autoFocus", z);
        NormalMessageTip normalMessageTip = new NormalMessageTip();
        normalMessageTip.setArguments(bundle);
        normalMessageTip.show(fragmentManager, str, 0);
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, String str3, int i, int i2, int i3, long j) {
        Bundle bundle = new Bundle();
        bundle.putString("title", str2);
        bundle.putString("message", str3);
        bundle.putInt("icon", i);
        bundle.putInt("anchor", i2);
        bundle.putInt(VideoTip.ARG_ARROW_DIRECTION, i3);
        NormalMessageTip normalMessageTip = new NormalMessageTip();
        normalMessageTip.setArguments(bundle);
        normalMessageTip.show(fragmentManager, str, j);
    }

    public static void show(@NonNull Context context, FragmentManager fragmentManager, String str, int i, int i2, int i3, int i4, int i5, long j) {
        Bundle bundle = new Bundle();
        if (i > -1) {
            bundle.putString("title", context.getResources().getString(i));
        }
        if (i2 > -1) {
            bundle.putString("message", context.getResources().getString(i2));
        }
        bundle.putInt("icon", i3);
        bundle.putInt("anchor", i4);
        bundle.putInt(VideoTip.ARG_ARROW_DIRECTION, i5);
        NormalMessageTip normalMessageTip = new NormalMessageTip();
        normalMessageTip.setArguments(bundle);
        normalMessageTip.show(fragmentManager, str, j);
    }

    public static boolean hasTip(FragmentManager fragmentManager, String str) {
        return ((NormalMessageTip) fragmentManager.findFragmentByTag(str)) != null;
    }

    public static boolean dismiss(FragmentManager fragmentManager, String str) {
        NormalMessageTip normalMessageTip = (NormalMessageTip) fragmentManager.findFragmentByTag(str);
        if (normalMessageTip == null) {
            return false;
        }
        normalMessageTip.dismiss();
        return true;
    }

    public ZMTip onCreateTip(@NonNull Context context, @NonNull LayoutInflater layoutInflater, Bundle bundle) {
        Bundle arguments = getArguments();
        String string = arguments.getString("title");
        String string2 = arguments.getString("message");
        int i = arguments.getInt("icon");
        int i2 = arguments.getInt("anchor");
        int i3 = arguments.getInt(VideoTip.ARG_ARROW_DIRECTION);
        this.mAutoFocus = arguments.getBoolean("autoFocus", true);
        View inflate = layoutInflater.inflate(C4558R.layout.zm_normal_message_tip, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.imgIcon);
        textView.setFocusable(false);
        textView2.setFocusable(false);
        if (StringUtil.isEmptyOrNull(string)) {
            textView.setVisibility(8);
        } else {
            textView.setText(string);
        }
        textView2.setText(string2);
        if (i > 0) {
            imageView.setImageResource(i);
        } else {
            imageView.setVisibility(8);
        }
        View findViewById = getActivity().findViewById(i2);
        ZMTip zMTip = new ZMTip(context);
        zMTip.addView(inflate);
        inflate.setFocusable(false);
        zMTip.setAnchor(findViewById, i3);
        zMTip.setFocusable(false);
        zMTip.setBackgroundColor(context.getResources().getColor(C4558R.color.zm_message_tip_background));
        zMTip.setBorderColor(context.getResources().getColor(C4558R.color.zm_message_normal_tip_border));
        zMTip.setShadow(4.0f, 0, 0, context.getResources().getColor(C4558R.color.zm_message_tip_shadow));
        return zMTip;
    }
}

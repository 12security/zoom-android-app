package com.zipow.videobox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMTip;

public class SettingTipFragment extends ZMTipFragment {
    public static final String ARG_ANCHOR_ID = "anchorId";
    private int mAnchorId = 0;

    @Nullable
    public ZMTip onCreateTip(@NonNull Context context, LayoutInflater layoutInflater, Bundle bundle) {
        View view = getView();
        if (view == null) {
            return null;
        }
        int dip2px = UIUtil.dip2px(context, 500.0f);
        if (UIUtil.getDisplayWidth(context) < dip2px) {
            dip2px = UIUtil.getDisplayWidth(context);
        }
        int dip2px2 = UIUtil.dip2px(context, 300.0f);
        if (UIUtil.getDisplayHeight(context) < dip2px2) {
            dip2px2 = UIUtil.getDisplayHeight(context);
        }
        view.setLayoutParams(new LayoutParams(dip2px, dip2px2));
        ZMTip zMTip = new ZMTip(context);
        zMTip.setBackgroundColor(-263173);
        zMTip.setArrowSize(UIUtil.dip2px(context, 30.0f), UIUtil.dip2px(context, 11.0f));
        zMTip.setCornerArcSize(0);
        zMTip.addView(view);
        this.mAnchorId = getArguments().getInt("anchorId", 0);
        if (this.mAnchorId > 0) {
            View findViewById = getActivity().findViewById(this.mAnchorId);
            if (findViewById != null) {
                zMTip.setAnchor(findViewById, 1);
            }
        }
        return zMTip;
    }

    public int getAnchorId() {
        return this.mAnchorId;
    }
}

package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.InterpretationMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.UIMgr;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class InterpretationTip extends ZMTipFragment {
    public static final String ARG_ANCHOR_ID = "anchorId";
    private static transient boolean isShowed = false;
    private TextView showInfo;
    private ImageView showLan;

    public static void show(FragmentManager fragmentManager, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("anchorId", i);
        InterpretationTip interpretationTip = new InterpretationTip();
        interpretationTip.setArguments(bundle);
        interpretationTip.show(fragmentManager, InterpretationTip.class.getName());
        isShowed = true;
    }

    public static boolean canShowTip(FragmentManager fragmentManager) {
        if (fragmentManager == null || ConfLocalHelper.isInSilentMode()) {
            return false;
        }
        InterpretationMgr interpretationObj = ConfMgr.getInstance().getInterpretationObj();
        if (!ConfLocalHelper.isInterpretationStarted(interpretationObj)) {
            return false;
        }
        if (ConfLocalHelper.isInterpreter(interpretationObj)) {
            dismiss(fragmentManager);
            isShowed = false;
            return false;
        } else if (!isShown(fragmentManager) && !isShowed) {
            return true;
        } else {
            return false;
        }
    }

    public static void clearShowState() {
        isShowed = false;
    }

    public static boolean isShown(FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        if (((InterpretationTip) fragmentManager.findFragmentByTag(InterpretationTip.class.getName())) != null) {
            z = true;
        }
        return z;
    }

    public static boolean dismiss(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        InterpretationTip interpretationTip = (InterpretationTip) fragmentManager.findFragmentByTag(InterpretationTip.class.getName());
        if (interpretationTip == null) {
            return false;
        }
        interpretationTip.dismiss();
        return true;
    }

    public ZMTip onCreateTip(Context context, LayoutInflater layoutInflater, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_interpretation_tip, null);
        this.showLan = (ImageView) inflate.findViewById(C4558R.C4560id.showLan);
        this.showInfo = (TextView) inflate.findViewById(C4558R.C4560id.showInfo);
        updateUI();
        int displayWidth = UIUtil.getDisplayWidth(context);
        inflate.measure(MeasureSpec.makeMeasureSpec(displayWidth, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(UIUtil.getDisplayHeight(context), Integer.MIN_VALUE));
        int i = (displayWidth * 7) / 8;
        if (inflate.getMeasuredWidth() > i) {
            inflate.setLayoutParams(new LayoutParams(i, -2));
        }
        Bundle arguments = getArguments();
        ZMTip zMTip = new ZMTip(context);
        zMTip.addView(inflate);
        int i2 = arguments.getInt("anchorId", 0);
        if (i2 > 0) {
            View findViewById = getActivity().findViewById(i2);
            if (findViewById != null) {
                zMTip.setAnchor(findViewById, UIMgr.isLargeMode(getActivity()) ? 1 : 3);
            }
        }
        return zMTip;
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        InterpretationMgr interpretationObj = ConfMgr.getInstance().getInterpretationObj();
        if (ConfLocalHelper.isInterpretationStarted(interpretationObj) && !ConfLocalHelper.isInterpreter(interpretationObj)) {
            int[] availableInterpreteLansList = interpretationObj.getAvailableInterpreteLansList();
            if (availableInterpreteLansList != null && availableInterpreteLansList.length != 0) {
                int i = availableInterpreteLansList[0];
                this.showLan.setImageResource(InterpretationMgr.LAN_RES_IDS[i]);
                if (2 == availableInterpreteLansList.length) {
                    this.showInfo.setText(getResources().getString(C4558R.string.zm_tip_interpretation_2_88102, new Object[]{getString(InterpretationMgr.LAN_NAME_IDS[availableInterpreteLansList[0]]), getString(InterpretationMgr.LAN_NAME_IDS[availableInterpreteLansList[1]])}));
                } else {
                    this.showInfo.setText(getResources().getString(C4558R.string.zm_tip_interpretation_3_88102, new Object[]{getString(InterpretationMgr.LAN_NAME_IDS[i]), Integer.valueOf(availableInterpreteLansList.length)}));
                }
            }
        }
    }
}

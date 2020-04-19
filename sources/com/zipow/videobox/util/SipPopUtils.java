package com.zipow.videobox.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.sip.SipPopWindow;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMPopupWindow;
import p021us.zoom.videomeetings.C4558R;

public class SipPopUtils {

    @Retention(RetentionPolicy.SOURCE)
    public @interface PopStyle {
        public static final int TOP_MID = 1;
        public static final int TOP_RIGHT = 2;
    }

    public static void showToggleAudioForUnHoldPop(Context context, View view) {
        final SipPopWindow sipPopWindow = new SipPopWindow(context);
        sipPopWindow.setDismissOnTouchOutside(true);
        sipPopWindow.setFocusable(false);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C4558R.dimen.zm_sip_pop_width);
        sipPopWindow.setWidth(dimensionPixelSize);
        sipPopWindow.setText(context.getString(C4558R.string.zm_sip_unhold_tips_audio_inmeeting_108086));
        sipPopWindow.showAsDropDown(view, ((-dimensionPixelSize) / 2) + (view.getWidth() / 2), 0);
        view.postDelayed(new Runnable() {
            public void run() {
                if (sipPopWindow.isShowing() && AccessibilityUtil.isSpokenFeedbackEnabled(sipPopWindow.getContentView().getContext())) {
                    AccessibilityUtil.announceNoInterruptForAccessibilityCompat(sipPopWindow.getContentView(), C4558R.string.zm_sip_unhold_tips_audio_inmeeting_108086);
                }
            }
        }, 1500);
    }

    public static void showFirstTimeForSLAHoldPop(Context context, View view, OnDismissListener onDismissListener) {
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C4558R.dimen.zm_sip_pop_width);
        showTipsPop(context, view, dimensionPixelSize, context.getString(C4558R.string.zm_sip_call_hold_pop_text_82852), 1, ((-dimensionPixelSize) / 2) + (view.getWidth() / 2), -UIUtil.dip2px(context, 18.0f), onDismissListener);
    }

    public static void showAudioTransferToMeetingPop(Context context, View view) {
        View inflate = View.inflate(context, C4558R.layout.zm_sip_pop_up, null);
        ((TextView) inflate.findViewById(C4558R.C4560id.tvMsg)).setText(C4558R.string.zm_sip_income_meeting_insip_audio_pop_108086);
        final ZMPopupWindow zMPopupWindow = new ZMPopupWindow(context);
        zMPopupWindow.setContentView(inflate);
        zMPopupWindow.setBackgroundDrawable(new ColorDrawable());
        zMPopupWindow.setFocusable(false);
        inflate.measure(0, 0);
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        zMPopupWindow.showAtLocation(view, 53, UIUtil.dip2px(context, 20.0f), (iArr[1] - view.getMeasuredHeight()) - UIUtil.dip2px(context, 12.0f));
        view.postDelayed(new Runnable() {
            public void run() {
                if (zMPopupWindow.isShowing() && AccessibilityUtil.isSpokenFeedbackEnabled(zMPopupWindow.getContentView().getContext())) {
                    AccessibilityUtil.announceNoInterruptForAccessibilityCompat(zMPopupWindow.getContentView(), C4558R.string.zm_sip_income_meeting_insip_audio_pop_108086);
                }
            }
        }, 1500);
    }

    @Nullable
    public static ZMPopupWindow getTipsPop(@Nullable Context context, int i, String str, int i2, @Nullable OnDismissListener onDismissListener) {
        if (context == null) {
            return null;
        }
        int i3 = C4558R.layout.zm_sip_tips_top_mid;
        switch (i2) {
            case 1:
                i3 = C4558R.layout.zm_sip_tips_top_mid;
                break;
            case 2:
                i3 = C4558R.layout.zm_sip_tips_top_right;
                break;
        }
        View inflate = View.inflate(context, i3, null);
        final ZMPopupWindow zMPopupWindow = new ZMPopupWindow(context);
        zMPopupWindow.setDismissOnTouchOutside(false);
        zMPopupWindow.setContentView(inflate);
        zMPopupWindow.setWidth(i);
        zMPopupWindow.setBackgroundDrawable(new ColorDrawable());
        ((TextView) inflate.findViewById(C4558R.C4560id.tv_title)).setText(str);
        inflate.findViewById(C4558R.C4560id.btnOK).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                zMPopupWindow.dismiss();
            }
        });
        if (onDismissListener != null) {
            zMPopupWindow.setOnDismissListener(onDismissListener);
        }
        return zMPopupWindow;
    }

    public static void showTipsPop(@Nullable Context context, View view, int i, final String str, int i2, int i3, int i4, @Nullable OnDismissListener onDismissListener) {
        if (context != null) {
            final ZMPopupWindow tipsPop = getTipsPop(context, i, str, i2, onDismissListener);
            tipsPop.showAsDropDown(view, i3, i4);
            view.postDelayed(new Runnable() {
                public void run() {
                    if (tipsPop.isShowing() && AccessibilityUtil.isSpokenFeedbackEnabled(tipsPop.getContentView().getContext())) {
                        AccessibilityUtil.announceForAccessibilityCompat(tipsPop.getContentView(), str, false);
                    }
                }
            }, 1500);
        }
    }
}

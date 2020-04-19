package com.zipow.videobox.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import p021us.zoom.videomeetings.C4558R;

public class SnackbarToast {
    public static Toast makeText(Context context, CharSequence charSequence, int i) {
        return makeText(context, charSequence, i, context.getResources().getColor(C4558R.color.zm_snackbar_info_bkg), context.getResources().getColor(C4558R.color.zm_ui_kit_color_white_ffffff));
    }

    public static Toast makeErrorText(Context context, CharSequence charSequence, int i) {
        return makeText(context, charSequence, i, context.getResources().getColor(C4558R.color.zm_snackbar_error_bkg), context.getResources().getColor(C4558R.color.zm_ui_kit_color_black_232333));
    }

    public static Toast makeText(Context context, CharSequence charSequence, int i, int i2, int i3) {
        Toast toast = new Toast(context);
        View inflate = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C4558R.layout.zm_snackbar_toast, null);
        inflate.setBackgroundColor(i2);
        TextView textView = (TextView) inflate.findViewById(16908299);
        textView.setTextColor(i3);
        ((LayoutParams) textView.getLayoutParams()).width = context.getResources().getDisplayMetrics().widthPixels;
        textView.setText(charSequence);
        toast.setView(inflate);
        toast.setDuration(i);
        return toast;
    }
}

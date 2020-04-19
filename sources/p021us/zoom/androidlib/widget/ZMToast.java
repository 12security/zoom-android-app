package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMToast */
public abstract class ZMToast {
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_SHORT = 0;
    private static int defaultGravity;
    private static Handler mHandler = new Handler();
    private static Runnable mRunnable = new Runnable() {
        public void run() {
            if (ZMToast.mToast != null) {
                ZMToast.mToast.cancel();
            }
        }
    };
    private static TextView mTextView;
    /* access modifiers changed from: private */
    public static Toast mToast;

    /* access modifiers changed from: private */
    public static void toast(Context context, CharSequence charSequence, int i, Integer num) {
        mHandler.removeCallbacks(mRunnable);
        int i2 = 1000;
        switch (i) {
            case 1:
                i2 = 3000;
                break;
        }
        if (mToast != null) {
            mTextView.setText(charSequence);
        } else {
            mToast = new Toast(context);
            View inflate = View.inflate(context, C4409R.layout.zm_toast, null);
            mTextView = (TextView) inflate.findViewById(C4409R.C4411id.text);
            mTextView.setText(charSequence);
            defaultGravity = mToast.getGravity();
            mToast.setView(inflate);
        }
        if (num != null) {
            mToast.setGravity(num.intValue(), 0, 0);
        } else {
            mToast.setGravity(defaultGravity, 0, 0);
        }
        mHandler.postDelayed(mRunnable, (long) i2);
        mToast.show();
    }

    public static void show(final Context context, final CharSequence charSequence, final int i, final int i2, long j) {
        if (context != null) {
            if (i < 0) {
                i = 0;
            }
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    ZMToast.toast(context, charSequence, i, Integer.valueOf(i2));
                }
            }, j);
        }
    }

    public static void show(Context context, CharSequence charSequence, int i) {
        if (context != null) {
            if (i < 0) {
                i = 0;
            }
            toast(context, charSequence, i, null);
        }
    }

    public static void show(Context context, int i, int i2) throws NullPointerException {
        if (context != null) {
            if (i2 < 0) {
                i2 = 0;
            }
            toast(context, context.getResources().getString(i), i2, null);
        }
    }
}

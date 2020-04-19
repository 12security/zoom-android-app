package p021us.zoom.androidlib.util;

import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.Snackbar.Callback;
import java.lang.ref.WeakReference;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.util.SnackbarUtils */
public class SnackbarUtils {
    private static WeakReference<Snackbar> snackbarWeakReference;

    private SnackbarUtils() {
        throw new RuntimeException("Disable null params");
    }

    private SnackbarUtils(@Nullable WeakReference<Snackbar> weakReference) {
        snackbarWeakReference = weakReference;
    }

    public Snackbar getSnackbar() {
        WeakReference<Snackbar> weakReference = snackbarWeakReference;
        if (weakReference == null || weakReference.get() == null) {
            return null;
        }
        return (Snackbar) snackbarWeakReference.get();
    }

    public static SnackbarUtils Short(View view, String str) {
        return new SnackbarUtils(new WeakReference(Snackbar.make(view, (CharSequence) str, -1))).backColor(-13487566);
    }

    public static SnackbarUtils Long(View view, String str) {
        return new SnackbarUtils(new WeakReference(Snackbar.make(view, (CharSequence) str, 0))).backColor(-13487566);
    }

    public static SnackbarUtils custom(View view, String str, int i) {
        return new SnackbarUtils(new WeakReference(Snackbar.make(view, (CharSequence) str, i))).backColor(-13487566);
    }

    public SnackbarUtils backColor(@ColorInt int i) {
        if (getSnackbar() != null) {
            getSnackbar().getView().setBackgroundColor(i);
        }
        return this;
    }

    public SnackbarUtils messageColor(@ColorInt int i) {
        if (getSnackbar() != null) {
            ((TextView) getSnackbar().getView().findViewById(C4409R.C4411id.snackbar_text)).setTextColor(i);
        }
        return this;
    }

    public SnackbarUtils messageCenter() {
        if (getSnackbar() != null) {
            TextView textView = (TextView) getSnackbar().getView().findViewById(C4409R.C4411id.snackbar_text);
            if (VERSION.SDK_INT >= 17) {
                textView.setTextAlignment(1);
            }
            textView.setGravity(17);
        }
        return this;
    }

    public SnackbarUtils gravity(int i) {
        if (getSnackbar() != null) {
            Snackbar snackbar = getSnackbar();
            LayoutParams layoutParams = snackbar.getView().getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                ((CoordinatorLayout.LayoutParams) layoutParams).gravity = i;
            } else {
                ((FrameLayout.LayoutParams) layoutParams).gravity = i;
            }
            snackbar.getView().setLayoutParams(layoutParams);
        }
        return this;
    }

    public SnackbarUtils margins(int i) {
        return getSnackbar() != null ? margins(i, i, i, i) : this;
    }

    public SnackbarUtils margins(int i, int i2, int i3, int i4) {
        if (getSnackbar() != null) {
            LayoutParams layoutParams = getSnackbar().getView().getLayoutParams();
            ((MarginLayoutParams) layoutParams).setMargins(i, i2, i3, i4);
            getSnackbar().getView().setLayoutParams(layoutParams);
        }
        return this;
    }

    public SnackbarUtils actionColor(@ColorInt int i) {
        if (getSnackbar() != null) {
            ((Button) getSnackbar().getView().findViewById(C4409R.C4411id.snackbar_action)).setTextColor(i);
        }
        return this;
    }

    public SnackbarUtils setAction(@StringRes int i, OnClickListener onClickListener) {
        return getSnackbar() != null ? setAction(getSnackbar().getView().getResources().getText(i), onClickListener) : this;
    }

    public SnackbarUtils setAction(CharSequence charSequence, OnClickListener onClickListener) {
        if (getSnackbar() != null) {
            getSnackbar().setAction(charSequence, onClickListener);
        }
        return this;
    }

    public SnackbarUtils setCallback(Callback callback) {
        if (getSnackbar() != null) {
            getSnackbar().setCallback(callback);
        }
        return this;
    }

    public SnackbarUtils duration(int i) {
        if (getSnackbar() != null) {
            getSnackbar().setDuration(i);
        }
        return this;
    }

    public void show() {
        if (getSnackbar() != null) {
            getSnackbar().show();
        }
    }
}

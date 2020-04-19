package p021us.zoom.androidlib.material;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.material.ZMViewPagerBottomSheetBehavior.BottomSheetCallback;

/* renamed from: us.zoom.androidlib.material.ZMViewPagerBottomSheetDialog */
public class ZMViewPagerBottomSheetDialog extends AppCompatDialog {
    private ZMViewPagerBottomSheetBehavior<FrameLayout> mBehavior;
    private ZMViewPagerBottomSheetBehavior mBottomSheetBehavior;
    private BottomSheetCallback mBottomSheetCallback;
    /* access modifiers changed from: private */
    public boolean mCancelable;
    private boolean mCanceledOnTouchOutside;
    private boolean mCanceledOnTouchOutsideSet;
    private boolean mCreated;
    private int mMaxHeight;
    private int mPeekHeight;
    private Window mWindow;

    public ZMViewPagerBottomSheetDialog(@NonNull Context context) {
        super(context, getThemeResId(context, 0));
        this.mCancelable = true;
        this.mCanceledOnTouchOutside = true;
        this.mBottomSheetCallback = new BottomSheetCallback() {
            public void onSlide(@NonNull View view, float f) {
            }

            public void onStateChanged(@NonNull View view, int i) {
                if (i == 5) {
                    ZMViewPagerBottomSheetDialog.this.dismiss();
                }
            }
        };
        init(1000, 1000);
    }

    public ZMViewPagerBottomSheetDialog(@NonNull Context context, @StyleRes int i) {
        super(context, getThemeResId(context, i));
        this.mCancelable = true;
        this.mCanceledOnTouchOutside = true;
        this.mBottomSheetCallback = new BottomSheetCallback() {
            public void onSlide(@NonNull View view, float f) {
            }

            public void onStateChanged(@NonNull View view, int i) {
                if (i == 5) {
                    ZMViewPagerBottomSheetDialog.this.dismiss();
                }
            }
        };
        supportRequestWindowFeature(1);
        init(1000, 1000);
    }

    public ZMViewPagerBottomSheetDialog(@NonNull Context context, int i, int i2) {
        this(context, 0, i, i2);
        init(i, i2);
    }

    public ZMViewPagerBottomSheetDialog(@NonNull Context context, @StyleRes int i, int i2, int i3) {
        super(context, getThemeResId(context, i));
        this.mCancelable = true;
        this.mCanceledOnTouchOutside = true;
        this.mBottomSheetCallback = new BottomSheetCallback() {
            public void onSlide(@NonNull View view, float f) {
            }

            public void onStateChanged(@NonNull View view, int i) {
                if (i == 5) {
                    ZMViewPagerBottomSheetDialog.this.dismiss();
                }
            }
        };
        supportRequestWindowFeature(1);
        init(i2, i3);
    }

    protected ZMViewPagerBottomSheetDialog(@NonNull Context context, boolean z, OnCancelListener onCancelListener, int i, int i2) {
        super(context, z, onCancelListener);
        this.mCancelable = true;
        this.mCanceledOnTouchOutside = true;
        this.mBottomSheetCallback = new BottomSheetCallback() {
            public void onSlide(@NonNull View view, float f) {
            }

            public void onStateChanged(@NonNull View view, int i) {
                if (i == 5) {
                    ZMViewPagerBottomSheetDialog.this.dismiss();
                }
            }
        };
        supportRequestWindowFeature(1);
        this.mCancelable = z;
        init(i, i2);
    }

    private void init(int i, int i2) {
        this.mWindow = getWindow();
        this.mPeekHeight = i;
        this.mMaxHeight = i2;
    }

    public void setContentView(@LayoutRes int i) {
        super.setContentView(wrapInBottomSheet(i, null, null));
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setLayout(-1, -1);
        setPeekHeight();
        setMaxHeight();
        this.mCreated = true;
    }

    public void setPeekHeight(int i) {
        this.mPeekHeight = i;
        if (this.mCreated) {
            setPeekHeight();
        }
    }

    public void setMaxHeight(int i) {
        this.mMaxHeight = i;
        if (this.mCreated) {
            setMaxHeight();
        }
    }

    private void setPeekHeight() {
        if (this.mPeekHeight > 0 && getBottomSheetBehavior() != null) {
            this.mBottomSheetBehavior.setPeekHeight(this.mPeekHeight);
        }
    }

    private void setMaxHeight() {
        int i = this.mMaxHeight;
        if (i > 0) {
            this.mWindow.setLayout(-1, i);
            this.mWindow.setGravity(80);
        }
    }

    private ZMViewPagerBottomSheetBehavior getBottomSheetBehavior() {
        ZMViewPagerBottomSheetBehavior zMViewPagerBottomSheetBehavior = this.mBottomSheetBehavior;
        if (zMViewPagerBottomSheetBehavior != null) {
            return zMViewPagerBottomSheetBehavior;
        }
        View findViewById = this.mWindow.findViewById(C4409R.C4411id.design_bottom_sheet);
        if (findViewById == null) {
            return null;
        }
        this.mBottomSheetBehavior = ZMViewPagerBottomSheetBehavior.from(findViewById);
        return this.mBottomSheetBehavior;
    }

    public void setContentView(View view) {
        super.setContentView(wrapInBottomSheet(0, view, null));
    }

    public void setContentView(View view, LayoutParams layoutParams) {
        super.setContentView(wrapInBottomSheet(0, view, layoutParams));
    }

    public void setCancelable(boolean z) {
        super.setCancelable(z);
        if (this.mCancelable != z) {
            this.mCancelable = z;
            ZMViewPagerBottomSheetBehavior<FrameLayout> zMViewPagerBottomSheetBehavior = this.mBehavior;
            if (zMViewPagerBottomSheetBehavior != null) {
                zMViewPagerBottomSheetBehavior.setHideable(z);
            }
        }
    }

    public void setCanceledOnTouchOutside(boolean z) {
        super.setCanceledOnTouchOutside(z);
        if (z && !this.mCancelable) {
            this.mCancelable = true;
        }
        this.mCanceledOnTouchOutside = z;
        this.mCanceledOnTouchOutsideSet = true;
    }

    private View wrapInBottomSheet(int i, View view, LayoutParams layoutParams) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) View.inflate(getContext(), C4409R.layout.zm_view_pager_bottom_sheet_dialog, null);
        if (i != 0 && view == null) {
            view = getLayoutInflater().inflate(i, coordinatorLayout, false);
        }
        FrameLayout frameLayout = (FrameLayout) coordinatorLayout.findViewById(C4409R.C4411id.design_bottom_sheet);
        this.mBehavior = ZMViewPagerBottomSheetBehavior.from(frameLayout);
        this.mBehavior.setBottomSheetCallback(this.mBottomSheetCallback);
        this.mBehavior.setHideable(this.mCancelable);
        if (layoutParams == null) {
            frameLayout.addView(view);
        } else {
            frameLayout.addView(view, layoutParams);
        }
        coordinatorLayout.findViewById(C4409R.C4411id.touch_outside).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ZMViewPagerBottomSheetDialog.this.mCancelable && ZMViewPagerBottomSheetDialog.this.isShowing() && ZMViewPagerBottomSheetDialog.this.shouldWindowCloseOnTouchOutside()) {
                    ZMViewPagerBottomSheetDialog.this.cancel();
                }
            }
        });
        return coordinatorLayout;
    }

    /* access modifiers changed from: private */
    public boolean shouldWindowCloseOnTouchOutside() {
        if (!this.mCanceledOnTouchOutsideSet) {
            if (VERSION.SDK_INT < 11) {
                this.mCanceledOnTouchOutside = true;
            } else {
                TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16843611});
                this.mCanceledOnTouchOutside = obtainStyledAttributes.getBoolean(0, true);
                obtainStyledAttributes.recycle();
            }
            this.mCanceledOnTouchOutsideSet = true;
        }
        return this.mCanceledOnTouchOutside;
    }

    private static int getThemeResId(Context context, int i) {
        if (i != 0) {
            return i;
        }
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(C4409R.attr.bottomSheetDialogTheme, typedValue, true)) {
            return typedValue.resourceId;
        }
        return C4409R.style.Theme_Design_Light_BottomSheetDialog;
    }
}

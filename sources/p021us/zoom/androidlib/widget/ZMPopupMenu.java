package p021us.zoom.androidlib.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMPopupMenu */
public class ZMPopupMenu {
    private boolean isOutSideDark;
    /* access modifiers changed from: private */
    public Activity mActivity;
    private View mAnchor;
    private View mContentView;
    private Context mContext;
    /* access modifiers changed from: private */
    public OnDismissListener mDismissListener;
    /* access modifiers changed from: private */
    public ZMMenuAdapter<?> mMenuAdapter;
    protected OnMenuItemClickListener mMenuItemClickListener;
    private ZMMenuListView mMenuListView;
    private ZMPopupWindow mPopup;
    private float outSideAlpha;

    /* renamed from: us.zoom.androidlib.widget.ZMPopupMenu$OnDismissListener */
    public interface OnDismissListener {
        void onDismiss(ZMPopupMenu zMPopupMenu);
    }

    /* renamed from: us.zoom.androidlib.widget.ZMPopupMenu$OnMenuItemClickListener */
    public interface OnMenuItemClickListener {
        void onMenuItemClick(IZMMenuItem iZMMenuItem);
    }

    public ZMPopupMenu(Activity activity, Context context, int i, ZMMenuAdapter<?> zMMenuAdapter, View view, int i2, int i3) {
        this.outSideAlpha = 0.38f;
        this.isOutSideDark = false;
        this.mActivity = activity;
        this.mContext = context;
        this.mAnchor = view;
        this.mMenuAdapter = zMMenuAdapter;
        this.mContentView = LayoutInflater.from(this.mContext).inflate(i, null);
        this.mMenuListView = (ZMMenuListView) this.mContentView.findViewById(C4409R.C4411id.menuListView);
        ZMMenuAdapter<?> zMMenuAdapter2 = this.mMenuAdapter;
        if (zMMenuAdapter2 != null) {
            this.mMenuListView.setAdapter(zMMenuAdapter2);
        }
        this.mMenuListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Object item = ZMPopupMenu.this.mMenuAdapter.getItem(i);
                if (item instanceof IZMMenuItem) {
                    ZMPopupMenu.this.onMenuItemClick((IZMMenuItem) item);
                }
            }
        });
        this.mPopup = new ZMPopupWindow(this.mContentView, i2, i3, false);
        this.mPopup.setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() {
            public void onDismiss() {
                ZMPopupMenu zMPopupMenu = ZMPopupMenu.this;
                zMPopupMenu.setWindowBgAlpha(zMPopupMenu.mActivity, 1.0f);
                if (ZMPopupMenu.this.mDismissListener != null) {
                    ZMPopupMenu.this.mDismissListener.onDismiss(ZMPopupMenu.this);
                }
            }
        });
    }

    public ZMPopupMenu(Activity activity, Context context, ZMMenuAdapter<?> zMMenuAdapter, View view, int i, int i2) {
        this(activity, context, C4409R.layout.zm_popup_menu, zMMenuAdapter, view, i, i2);
    }

    public ZMPopupMenu(Context context, ZMMenuAdapter<?> zMMenuAdapter, View view, int i) {
        this(null, context, zMMenuAdapter, view, i, -2);
    }

    public ZMPopupMenu(Context context, ZMMenuAdapter<?> zMMenuAdapter, View view) {
        this(context, zMMenuAdapter, view, -2);
    }

    private static int getWidestView(Context context, Adapter adapter) {
        FrameLayout frameLayout = new FrameLayout(context);
        int count = adapter.getCount();
        View view = null;
        int i = 0;
        for (int i2 = 0; i2 < count; i2++) {
            view = adapter.getView(i2, view, frameLayout);
            view.measure(0, 0);
            int measuredWidth = view.getMeasuredWidth();
            if (measuredWidth > i) {
                i = measuredWidth;
            }
        }
        return i;
    }

    /* access modifiers changed from: private */
    public void setWindowBgAlpha(Activity activity, float f) {
        if (activity != null) {
            LayoutParams attributes = activity.getWindow().getAttributes();
            attributes.alpha = f;
            activity.getWindow().setAttributes(attributes);
        }
    }

    public void setOutSideAlpha(float f) {
        this.outSideAlpha = f;
    }

    public void setOutSideDark(boolean z) {
        this.isOutSideDark = z;
    }

    public void show() {
        this.mPopup.showAsDropDown(this.mAnchor);
        if (this.isOutSideDark) {
            setWindowBgAlpha(this.mActivity, this.outSideAlpha);
        }
    }

    public void show(int i, int i2, int i3) {
        this.mPopup.showAtLocation(this.mAnchor, i, i2, i3);
        if (this.isOutSideDark) {
            setWindowBgAlpha(this.mActivity, this.outSideAlpha);
        }
    }

    public void setBackgroudColor(@ColorInt int i) {
        ZMMenuListView zMMenuListView = this.mMenuListView;
        if (zMMenuListView != null) {
            zMMenuListView.setBackgroundColor(i);
        }
    }

    public void setBackgroudResource(@DrawableRes int i) {
        ZMMenuListView zMMenuListView = this.mMenuListView;
        if (zMMenuListView != null) {
            zMMenuListView.setBackgroundResource(i);
        }
    }

    public void dismiss() {
        this.mPopup.dismiss();
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mDismissListener = onDismissListener;
    }

    /* access modifiers changed from: protected */
    public void onMenuItemClick(IZMMenuItem iZMMenuItem) {
        OnMenuItemClickListener onMenuItemClickListener = this.mMenuItemClickListener;
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(iZMMenuItem);
        }
        dismiss();
    }
}

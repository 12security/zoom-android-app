package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.widget.IZMMenuItem;

/* renamed from: us.zoom.androidlib.widget.ZMMenuAdapter */
public class ZMMenuAdapter<MenuItemType extends IZMMenuItem> extends BaseAdapter {
    private Context mContext;
    private float mFontSize;
    private List<MenuItemType> mList;
    private boolean mShowIcon;
    private boolean mShowSelectStatus;
    private boolean mSmallfontMultilineMode;

    public long getItemId(int i) {
        return (long) i;
    }

    public ZMMenuAdapter(Context context, boolean z) {
        this.mList = new ArrayList();
        this.mShowIcon = false;
        this.mShowSelectStatus = false;
        this.mSmallfontMultilineMode = false;
        this.mFontSize = 0.0f;
        this.mContext = context;
        this.mShowIcon = z;
    }

    public ZMMenuAdapter(Context context, boolean z, float f) {
        this(context, false);
        this.mSmallfontMultilineMode = z;
        this.mFontSize = f;
    }

    public void setShowSelectedStatus(boolean z) {
        this.mShowSelectStatus = z;
    }

    public boolean ismShowIcon() {
        return this.mShowIcon;
    }

    public boolean ismShowSelectStatus() {
        return this.mShowSelectStatus;
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int i) {
        return this.mList.get(i);
    }

    public void addAll(MenuItemType... menuitemtypeArr) {
        for (MenuItemType addItem : menuitemtypeArr) {
            addItem(addItem);
        }
    }

    public void addAll(List<MenuItemType> list) {
        for (MenuItemType addItem : list) {
            addItem(addItem);
        }
    }

    public void addItem(MenuItemType menuitemtype) {
        this.mList.add(menuitemtype);
    }

    public void clear() {
        this.mList.clear();
    }

    @Nullable
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = onCreateView(LayoutInflater.from(this.mContext), viewGroup);
        }
        IZMMenuItem iZMMenuItem = (IZMMenuItem) this.mList.get(i);
        if (iZMMenuItem == null) {
            return null;
        }
        onBindView(view, iZMMenuItem);
        return view;
    }

    private View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(getLayoutId(), viewGroup, false);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C4409R.layout.zm_menu_item;
    }

    /* access modifiers changed from: protected */
    public void onBindView(@NonNull View view, @NonNull MenuItemType menuitemtype) {
        ZMCheckedTextView zMCheckedTextView = (ZMCheckedTextView) view.findViewById(C4409R.C4411id.check);
        ImageView imageView = (ImageView) view.findViewById(C4409R.C4411id.imgIcon);
        TextView textView = (TextView) view.findViewById(C4409R.C4411id.txtLabel);
        textView.setSingleLine(!this.mSmallfontMultilineMode);
        float f = this.mFontSize;
        if (f != 0.0f) {
            textView.setTextSize(f);
        }
        if (menuitemtype.isDisable()) {
            view.setBackgroundResource(C4409R.color.zm_ui_kit_color_gray_E4E4ED);
        } else {
            view.setBackgroundResource(C4409R.color.zm_ui_kit_color_white_ffffff);
        }
        textView.setText(menuitemtype.getLabel());
        if (ismShowIcon()) {
            imageView.setVisibility(0);
            imageView.setImageDrawable(menuitemtype.getIcon());
        } else {
            imageView.setVisibility(8);
        }
        if (ismShowSelectStatus()) {
            zMCheckedTextView.setVisibility(0);
            zMCheckedTextView.setChecked(menuitemtype.isSelected());
            return;
        }
        zMCheckedTextView.setVisibility(8);
    }
}

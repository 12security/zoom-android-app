package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.widget.IZMMenuItem;

/* renamed from: us.zoom.androidlib.widget.ZMChoiceAdapter */
public class ZMChoiceAdapter<MenuItemType extends IZMMenuItem> extends BaseAdapter {
    private Context mContext;
    private int mDrawableChoiceResId;
    private String mDrawableContentDescription;
    private float mFontSize;
    private List<MenuItemType> mList;

    public long getItemId(int i) {
        return (long) i;
    }

    public ZMChoiceAdapter(Context context, int i, String str) {
        this.mList = new ArrayList();
        this.mFontSize = 0.0f;
        this.mContext = context;
        this.mDrawableChoiceResId = i;
        this.mDrawableContentDescription = str;
    }

    public ZMChoiceAdapter(Context context, int i, String str, float f) {
        this(context, i, str);
        this.mFontSize = f;
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
            view = LayoutInflater.from(this.mContext).inflate(C4409R.layout.zm_item_with_choice, null);
        }
        TextView textView = (TextView) view.findViewById(C4409R.C4411id.txtTitle);
        float f = this.mFontSize;
        if (f != 0.0f) {
            textView.setTextSize(f);
        }
        ImageView imageView = (ImageView) view.findViewById(C4409R.C4411id.imgIcon);
        imageView.setImageResource(this.mDrawableChoiceResId);
        imageView.setContentDescription(this.mDrawableContentDescription);
        IZMMenuItem iZMMenuItem = (IZMMenuItem) this.mList.get(i);
        if (iZMMenuItem == null) {
            return null;
        }
        textView.setText(iZMMenuItem.getLabel());
        imageView.setVisibility(iZMMenuItem.isSelected() ? 0 : 8);
        return view;
    }
}

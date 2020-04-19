package p021us.zoom.androidlib.widget.recyclerview;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/* renamed from: us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewItemHolder */
public class ZMBaseRecyclerViewItemHolder extends ViewHolder {
    /* access modifiers changed from: private */
    public ZMBaseRecyclerViewAdapter adapter;
    Object associatedObject;
    private final LinkedHashSet<Integer> childClickViewIds = new LinkedHashSet<>();
    @Deprecated
    public View convertView;
    private final LinkedHashSet<Integer> itemChildLongClickViewIds = new LinkedHashSet<>();
    private final HashSet<Integer> nestViews = new HashSet<>();
    private final SparseArray<View> views = new SparseArray<>();

    public Set<Integer> getNestViews() {
        return this.nestViews;
    }

    public ZMBaseRecyclerViewItemHolder(View view) {
        super(view);
        this.convertView = view;
    }

    /* access modifiers changed from: private */
    public int getClickPosition() {
        if (getLayoutPosition() >= this.adapter.getHeaderLayoutCount()) {
            return getLayoutPosition() - this.adapter.getHeaderLayoutCount();
        }
        return 0;
    }

    public HashSet<Integer> getItemChildLongClickViewIds() {
        return this.itemChildLongClickViewIds;
    }

    public HashSet<Integer> getChildClickViewIds() {
        return this.childClickViewIds;
    }

    @Deprecated
    public View getConvertView() {
        return this.convertView;
    }

    public ZMBaseRecyclerViewItemHolder setText(@IdRes int i, CharSequence charSequence) {
        ((TextView) getView(i)).setText(charSequence);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setText(@IdRes int i, @StringRes int i2) {
        ((TextView) getView(i)).setText(i2);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setImageResource(@IdRes int i, @DrawableRes int i2) {
        ((ImageView) getView(i)).setImageResource(i2);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setBackgroundColor(@IdRes int i, @ColorInt int i2) {
        getView(i).setBackgroundColor(i2);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setBackgroundRes(@IdRes int i, @DrawableRes int i2) {
        getView(i).setBackgroundResource(i2);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setTextColor(@IdRes int i, @ColorInt int i2) {
        ((TextView) getView(i)).setTextColor(i2);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setImageDrawable(@IdRes int i, Drawable drawable) {
        ((ImageView) getView(i)).setImageDrawable(drawable);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setImageBitmap(@IdRes int i, Bitmap bitmap) {
        ((ImageView) getView(i)).setImageBitmap(bitmap);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setAlpha(@IdRes int i, float f) {
        getView(i).setAlpha(f);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setGone(@IdRes int i, boolean z) {
        getView(i).setVisibility(z ? 0 : 8);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setVisible(@IdRes int i, boolean z) {
        getView(i).setVisibility(z ? 0 : 4);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder linkify(@IdRes int i) {
        Linkify.addLinks((TextView) getView(i), 15);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setTypeface(@IdRes int i, Typeface typeface) {
        TextView textView = (TextView) getView(i);
        textView.setTypeface(typeface);
        textView.setPaintFlags(textView.getPaintFlags() | 128);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setTypeface(Typeface typeface, int... iArr) {
        for (int view : iArr) {
            TextView textView = (TextView) getView(view);
            textView.setTypeface(typeface);
            textView.setPaintFlags(textView.getPaintFlags() | 128);
        }
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setProgress(@IdRes int i, int i2) {
        ((ProgressBar) getView(i)).setProgress(i2);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setProgress(@IdRes int i, int i2, int i3) {
        ProgressBar progressBar = (ProgressBar) getView(i);
        progressBar.setMax(i3);
        progressBar.setProgress(i2);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setMax(@IdRes int i, int i2) {
        ((ProgressBar) getView(i)).setMax(i2);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setRating(@IdRes int i, float f) {
        ((RatingBar) getView(i)).setRating(f);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setRating(@IdRes int i, float f, int i2) {
        RatingBar ratingBar = (RatingBar) getView(i);
        ratingBar.setMax(i2);
        ratingBar.setRating(f);
        return this;
    }

    @Deprecated
    public ZMBaseRecyclerViewItemHolder setOnClickListener(@IdRes int i, OnClickListener onClickListener) {
        getView(i).setOnClickListener(onClickListener);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder addOnClickListener(@IdRes int i) {
        this.childClickViewIds.add(Integer.valueOf(i));
        View view = getView(i);
        if (view != null) {
            if (!view.isClickable()) {
                view.setClickable(true);
            }
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (ZMBaseRecyclerViewItemHolder.this.adapter.getOnItemChildClickListener() != null) {
                        ZMBaseRecyclerViewItemHolder.this.adapter.getOnItemChildClickListener().onItemChildClick(ZMBaseRecyclerViewItemHolder.this.adapter, view, ZMBaseRecyclerViewItemHolder.this.getClickPosition());
                    }
                }
            });
        }
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setNestView(@IdRes int i) {
        addOnClickListener(i);
        addOnLongClickListener(i);
        this.nestViews.add(Integer.valueOf(i));
        return this;
    }

    public ZMBaseRecyclerViewItemHolder addOnLongClickListener(@IdRes int i) {
        this.itemChildLongClickViewIds.add(Integer.valueOf(i));
        View view = getView(i);
        if (view != null) {
            if (!view.isLongClickable()) {
                view.setLongClickable(true);
            }
            view.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    return ZMBaseRecyclerViewItemHolder.this.adapter.getOnItemChildLongClickListener() != null && ZMBaseRecyclerViewItemHolder.this.adapter.getOnItemChildLongClickListener().onItemChildLongClick(ZMBaseRecyclerViewItemHolder.this.adapter, view, ZMBaseRecyclerViewItemHolder.this.getClickPosition());
                }
            });
        }
        return this;
    }

    @Deprecated
    public ZMBaseRecyclerViewItemHolder setOnTouchListener(@IdRes int i, OnTouchListener onTouchListener) {
        getView(i).setOnTouchListener(onTouchListener);
        return this;
    }

    @Deprecated
    public ZMBaseRecyclerViewItemHolder setOnLongClickListener(@IdRes int i, OnLongClickListener onLongClickListener) {
        getView(i).setOnLongClickListener(onLongClickListener);
        return this;
    }

    @Deprecated
    public ZMBaseRecyclerViewItemHolder setOnItemClickListener(@IdRes int i, OnItemClickListener onItemClickListener) {
        ((AdapterView) getView(i)).setOnItemClickListener(onItemClickListener);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setOnItemLongClickListener(@IdRes int i, OnItemLongClickListener onItemLongClickListener) {
        ((AdapterView) getView(i)).setOnItemLongClickListener(onItemLongClickListener);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setOnItemSelectedClickListener(@IdRes int i, OnItemSelectedListener onItemSelectedListener) {
        ((AdapterView) getView(i)).setOnItemSelectedListener(onItemSelectedListener);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setOnCheckedChangeListener(@IdRes int i, OnCheckedChangeListener onCheckedChangeListener) {
        ((CompoundButton) getView(i)).setOnCheckedChangeListener(onCheckedChangeListener);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setTag(@IdRes int i, Object obj) {
        getView(i).setTag(obj);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setTag(@IdRes int i, int i2, Object obj) {
        getView(i).setTag(i2, obj);
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setChecked(@IdRes int i, boolean z) {
        View view = getView(i);
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(z);
        }
        return this;
    }

    public ZMBaseRecyclerViewItemHolder setAdapter(@IdRes int i, Adapter adapter2) {
        ((AdapterView) getView(i)).setAdapter(adapter2);
        return this;
    }

    /* access modifiers changed from: protected */
    public ZMBaseRecyclerViewItemHolder setAdapter(ZMBaseRecyclerViewAdapter zMBaseRecyclerViewAdapter) {
        this.adapter = zMBaseRecyclerViewAdapter;
        return this;
    }

    public <T extends View> T getView(@IdRes int i) {
        T t = (View) this.views.get(i);
        if (t != null) {
            return t;
        }
        T findViewById = this.itemView.findViewById(i);
        this.views.put(i, findViewById);
        return findViewById;
    }

    public Object getAssociatedObject() {
        return this.associatedObject;
    }

    public void setAssociatedObject(Object obj) {
        this.associatedObject = obj;
    }
}

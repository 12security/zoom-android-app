package p021us.zoom.androidlib.widget.recyclerview;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import p021us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewItemHolder;

/* renamed from: us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter */
public abstract class ZMBaseRecyclerViewAdapter<T, K extends ZMBaseRecyclerViewItemHolder> extends Adapter<K> {
    public static final int ALPHAIN = 1;
    public static final int EMPTY_VIEW = 1365;
    public static final int FOOTER_VIEW = 819;
    public static final int HEADER_VIEW = 273;
    public static final int SCALEIN = 2;
    public static final int SLIDEIN_BOTTOM = 3;
    public static final int SLIDEIN_LEFT = 4;
    public static final int SLIDEIN_RIGHT = 5;
    protected static final String TAG = "ZMBaseRecyclerViewAdapter";
    private boolean footerViewAsFlow;
    private boolean headerViewAsFlow;
    protected Context mContext;
    protected List<T> mData;
    private int mDuration;
    private FrameLayout mEmptyLayout;
    private boolean mFirstOnlyEnable;
    private boolean mFootAndEmptyEnable;
    private LinearLayout mFooterLayout;
    private boolean mHeadAndEmptyEnable;
    private LinearLayout mHeaderLayout;
    private Interpolator mInterpolator;
    private boolean mIsUseEmpty;
    private int mLastPosition;
    protected LayoutInflater mLayoutInflater;
    protected int mLayoutResId;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemChildLongClickListener mOnItemChildLongClickListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private boolean mOpenAnimationEnable;
    private int mPreLoadNumber;
    private RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public SpanSizeLookup mSpanSizeLookup;
    private int mStartUpFetchPosition;
    private boolean mUpFetchEnable;
    private UpFetchListener mUpFetchListener;
    private boolean mUpFetching;
    private ZMMultiTypeDelegate<T> mZMMultiTypeDelegate;

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter$AnimationType */
    public @interface AnimationType {
    }

    /* renamed from: us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter$OnItemChildClickListener */
    public interface OnItemChildClickListener {
        void onItemChildClick(ZMBaseRecyclerViewAdapter zMBaseRecyclerViewAdapter, View view, int i);
    }

    /* renamed from: us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter$OnItemChildLongClickListener */
    public interface OnItemChildLongClickListener {
        boolean onItemChildLongClick(ZMBaseRecyclerViewAdapter zMBaseRecyclerViewAdapter, View view, int i);
    }

    /* renamed from: us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter$OnItemClickListener */
    public interface OnItemClickListener {
        void onItemClick(ZMBaseRecyclerViewAdapter zMBaseRecyclerViewAdapter, View view, int i);
    }

    /* renamed from: us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter$OnItemLongClickListener */
    public interface OnItemLongClickListener {
        boolean onItemLongClick(ZMBaseRecyclerViewAdapter zMBaseRecyclerViewAdapter, View view, int i);
    }

    /* renamed from: us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter$SpanSizeLookup */
    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int i);
    }

    /* renamed from: us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter$UpFetchListener */
    public interface UpFetchListener {
        void onUpFetch();
    }

    /* access modifiers changed from: protected */
    public abstract void convert(K k, T t);

    public long getItemId(int i) {
        return (long) i;
    }

    /* access modifiers changed from: protected */
    public boolean isFixedViewType(int i) {
        return i == 1365 || i == 273 || i == 819;
    }

    /* access modifiers changed from: protected */
    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    private void checkNotNull() {
        if (getRecyclerView() == null) {
            throw new RuntimeException("please bind recyclerView first!");
        }
    }

    public void bindToRecyclerView(RecyclerView recyclerView) {
        if (getRecyclerView() == null) {
            setRecyclerView(recyclerView);
            getRecyclerView().setAdapter(this);
            return;
        }
        throw new RuntimeException("Don't bind twice");
    }

    private int getTheBiggestNumber(int[] iArr) {
        int i = -1;
        if (iArr == null || iArr.length == 0) {
            return -1;
        }
        for (int i2 : iArr) {
            if (i2 > i) {
                i = i2;
            }
        }
        return i;
    }

    public void setUpFetchEnable(boolean z) {
        this.mUpFetchEnable = z;
    }

    public boolean isUpFetchEnable() {
        return this.mUpFetchEnable;
    }

    public void setStartUpFetchPosition(int i) {
        this.mStartUpFetchPosition = i;
    }

    private void autoUpFetch(int i) {
        if (isUpFetchEnable() && !isUpFetching() && i <= this.mStartUpFetchPosition) {
            UpFetchListener upFetchListener = this.mUpFetchListener;
            if (upFetchListener != null) {
                upFetchListener.onUpFetch();
            }
        }
    }

    public boolean isUpFetching() {
        return this.mUpFetching;
    }

    public void setUpFetching(boolean z) {
        this.mUpFetching = z;
    }

    public void setUpFetchListener(UpFetchListener upFetchListener) {
        this.mUpFetchListener = upFetchListener;
    }

    public void setNotDoAnimationCount(int i) {
        this.mLastPosition = i;
    }

    public void setDuration(int i) {
        this.mDuration = i;
    }

    public final void refreshNotifyItemChanged(int i) {
        notifyItemChanged(i + getHeaderLayoutCount());
    }

    public ZMBaseRecyclerViewAdapter(@LayoutRes int i, @Nullable List<T> list) {
        this.mFirstOnlyEnable = true;
        this.mOpenAnimationEnable = false;
        this.mInterpolator = new LinearInterpolator();
        this.mDuration = 300;
        this.mLastPosition = -1;
        this.mIsUseEmpty = true;
        this.mStartUpFetchPosition = 1;
        this.mPreLoadNumber = 1;
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mData = list;
        if (i != 0) {
            this.mLayoutResId = i;
        }
    }

    public ZMBaseRecyclerViewAdapter(@Nullable List<T> list) {
        this(0, list);
    }

    public ZMBaseRecyclerViewAdapter(@LayoutRes int i) {
        this(i, null);
    }

    public void setNewData(@Nullable List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mData = list;
        this.mLastPosition = -1;
        notifyDataSetChanged();
    }

    @Deprecated
    public void add(@IntRange(from = 0) int i, @NonNull T t) {
        addData(i, t);
    }

    public void addData(@IntRange(from = 0) int i, @NonNull T t) {
        this.mData.add(i, t);
        notifyItemInserted(i + getHeaderLayoutCount());
        compatibilityDataSizeChanged(1);
    }

    public void addData(@NonNull T t) {
        this.mData.add(t);
        notifyItemInserted(this.mData.size() + getHeaderLayoutCount());
        compatibilityDataSizeChanged(1);
    }

    public void remove(@IntRange(from = 0) int i) {
        this.mData.remove(i);
        int headerLayoutCount = i + getHeaderLayoutCount();
        notifyItemRemoved(headerLayoutCount);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChanged(headerLayoutCount, this.mData.size() - headerLayoutCount);
    }

    public void setData(@IntRange(from = 0) int i, @NonNull T t) {
        this.mData.set(i, t);
        notifyItemChanged(i + getHeaderLayoutCount());
    }

    public void addData(@IntRange(from = 0) int i, @NonNull Collection<? extends T> collection) {
        this.mData.addAll(i, collection);
        notifyItemRangeInserted(i + getHeaderLayoutCount(), collection.size());
        compatibilityDataSizeChanged(collection.size());
    }

    public void addData(@NonNull Collection<? extends T> collection) {
        this.mData.addAll(collection);
        notifyItemRangeInserted((this.mData.size() - collection.size()) + getHeaderLayoutCount(), collection.size());
        compatibilityDataSizeChanged(collection.size());
    }

    public void replaceData(@NonNull Collection<? extends T> collection) {
        List<T> list = this.mData;
        if (collection != list) {
            list.clear();
            this.mData.addAll(collection);
        }
        notifyDataSetChanged();
    }

    private void compatibilityDataSizeChanged(int i) {
        List<T> list = this.mData;
        if ((list == null ? 0 : list.size()) == i) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    public List<T> getData() {
        return this.mData;
    }

    @Nullable
    public T getItem(@IntRange(from = 0) int i) {
        if (i < 0 || i >= this.mData.size()) {
            return null;
        }
        return this.mData.get(i);
    }

    @Deprecated
    public int getHeaderViewsCount() {
        return getHeaderLayoutCount();
    }

    @Deprecated
    public int getFooterViewsCount() {
        return getFooterLayoutCount();
    }

    public int getHeaderLayoutCount() {
        LinearLayout linearLayout = this.mHeaderLayout;
        return (linearLayout == null || linearLayout.getChildCount() == 0) ? 0 : 1;
    }

    public int getFooterLayoutCount() {
        LinearLayout linearLayout = this.mFooterLayout;
        return (linearLayout == null || linearLayout.getChildCount() == 0) ? 0 : 1;
    }

    public int getEmptyViewCount() {
        FrameLayout frameLayout = this.mEmptyLayout;
        if (frameLayout == null || frameLayout.getChildCount() == 0 || !this.mIsUseEmpty || this.mData.size() != 0) {
            return 0;
        }
        return 1;
    }

    public int getItemCount() {
        int i = 1;
        if (getEmptyViewCount() == 1) {
            if (this.mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                i = 2;
            }
            if (!this.mFootAndEmptyEnable || getFooterLayoutCount() == 0) {
                return i;
            }
            return i + 1;
        }
        return getFooterLayoutCount() + getHeaderLayoutCount() + this.mData.size();
    }

    public int getItemViewType(int i) {
        boolean z = true;
        if (getEmptyViewCount() == 1) {
            if (!this.mHeadAndEmptyEnable || getHeaderLayoutCount() == 0) {
                z = false;
            }
            switch (i) {
                case 0:
                    if (z) {
                        return HEADER_VIEW;
                    }
                    return EMPTY_VIEW;
                case 1:
                    if (z) {
                        return EMPTY_VIEW;
                    }
                    return FOOTER_VIEW;
                case 2:
                    return FOOTER_VIEW;
                default:
                    return EMPTY_VIEW;
            }
        } else {
            int headerLayoutCount = getHeaderLayoutCount();
            if (i < headerLayoutCount) {
                return HEADER_VIEW;
            }
            int i2 = i - headerLayoutCount;
            if (i2 < this.mData.size()) {
                return getDefItemViewType(i2);
            }
            return FOOTER_VIEW;
        }
    }

    /* access modifiers changed from: protected */
    public int getDefItemViewType(int i) {
        ZMMultiTypeDelegate<T> zMMultiTypeDelegate = this.mZMMultiTypeDelegate;
        if (zMMultiTypeDelegate != null) {
            return zMMultiTypeDelegate.getDefItemViewType(this.mData, i);
        }
        return super.getItemViewType(i);
    }

    @NonNull
    public K onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        K k;
        this.mContext = viewGroup.getContext();
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
        if (i == 273) {
            k = createBaseViewHolder(this.mHeaderLayout);
        } else if (i == 819) {
            k = createBaseViewHolder(this.mFooterLayout);
        } else if (i != 1365) {
            k = onCreateDefViewHolder(viewGroup, i);
            bindViewClickListener(k);
        } else {
            k = createBaseViewHolder(this.mEmptyLayout);
        }
        k.setAdapter(this);
        return k;
    }

    public void onViewAttachedToWindow(@NonNull K k) {
        super.onViewAttachedToWindow(k);
        int itemViewType = k.getItemViewType();
        if (itemViewType == 1365 || itemViewType == 273 || itemViewType == 819) {
            setFullSpan(k);
        }
    }

    /* access modifiers changed from: protected */
    public void setFullSpan(ViewHolder viewHolder) {
        if (viewHolder.itemView.getLayoutParams() instanceof LayoutParams) {
            ((LayoutParams) viewHolder.itemView.getLayoutParams()).setFullSpan(true);
        }
    }

    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
                public int getSpanSize(int i) {
                    int itemViewType = ZMBaseRecyclerViewAdapter.this.getItemViewType(i);
                    int i2 = 1;
                    if (itemViewType == 273 && ZMBaseRecyclerViewAdapter.this.isHeaderViewAsFlow()) {
                        return 1;
                    }
                    if (itemViewType == 819 && ZMBaseRecyclerViewAdapter.this.isFooterViewAsFlow()) {
                        return 1;
                    }
                    if (ZMBaseRecyclerViewAdapter.this.mSpanSizeLookup == null) {
                        if (ZMBaseRecyclerViewAdapter.this.isFixedViewType(itemViewType)) {
                            i2 = gridLayoutManager.getSpanCount();
                        }
                        return i2;
                    }
                    return ZMBaseRecyclerViewAdapter.this.isFixedViewType(itemViewType) ? gridLayoutManager.getSpanCount() : ZMBaseRecyclerViewAdapter.this.mSpanSizeLookup.getSpanSize(gridLayoutManager, i - ZMBaseRecyclerViewAdapter.this.getHeaderLayoutCount());
                }
            });
        }
    }

    public void setHeaderViewAsFlow(boolean z) {
        this.headerViewAsFlow = z;
    }

    public boolean isHeaderViewAsFlow() {
        return this.headerViewAsFlow;
    }

    public void setFooterViewAsFlow(boolean z) {
        this.footerViewAsFlow = z;
    }

    public boolean isFooterViewAsFlow() {
        return this.footerViewAsFlow;
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    public void onBindViewHolder(@NonNull K k, int i) {
        autoUpFetch(i);
        int itemViewType = k.getItemViewType();
        if (itemViewType == 0) {
            convert(k, getItem(i - getHeaderLayoutCount()));
        } else if (itemViewType != 273 && itemViewType != 819 && itemViewType != 1365) {
            convert(k, getItem(i - getHeaderLayoutCount()));
        }
    }

    private void bindViewClickListener(final ZMBaseRecyclerViewItemHolder zMBaseRecyclerViewItemHolder) {
        if (zMBaseRecyclerViewItemHolder != null) {
            View view = zMBaseRecyclerViewItemHolder.itemView;
            if (getOnItemClickListener() != null) {
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        ZMBaseRecyclerViewAdapter.this.setOnItemClick(view, zMBaseRecyclerViewItemHolder.getLayoutPosition() - ZMBaseRecyclerViewAdapter.this.getHeaderLayoutCount());
                    }
                });
            }
            if (getOnItemLongClickListener() != null) {
                view.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        return ZMBaseRecyclerViewAdapter.this.setOnItemLongClick(view, zMBaseRecyclerViewItemHolder.getLayoutPosition() - ZMBaseRecyclerViewAdapter.this.getHeaderLayoutCount());
                    }
                });
            }
        }
    }

    public void setOnItemClick(View view, int i) {
        getOnItemClickListener().onItemClick(this, view, i);
    }

    public boolean setOnItemLongClick(View view, int i) {
        return getOnItemLongClickListener().onItemLongClick(this, view, i);
    }

    public void setMultiTypeDelegate(ZMMultiTypeDelegate<T> zMMultiTypeDelegate) {
        this.mZMMultiTypeDelegate = zMMultiTypeDelegate;
    }

    public ZMMultiTypeDelegate<T> getMultiTypeDelegate() {
        return this.mZMMultiTypeDelegate;
    }

    /* access modifiers changed from: protected */
    public K onCreateDefViewHolder(ViewGroup viewGroup, int i) {
        int i2 = this.mLayoutResId;
        ZMMultiTypeDelegate<T> zMMultiTypeDelegate = this.mZMMultiTypeDelegate;
        if (zMMultiTypeDelegate != null) {
            i2 = zMMultiTypeDelegate.getLayoutId(i);
        }
        return createBaseViewHolder(viewGroup, i2);
    }

    /* access modifiers changed from: protected */
    public K createBaseViewHolder(ViewGroup viewGroup, int i) {
        return createBaseViewHolder(getItemView(i, viewGroup));
    }

    /* access modifiers changed from: protected */
    public K createBaseViewHolder(View view) {
        K k;
        Class cls = getClass();
        Class cls2 = null;
        while (cls2 == null && cls != null) {
            cls2 = getInstancedGenericKClass(cls);
            cls = cls.getSuperclass();
        }
        if (cls2 == null) {
            k = new ZMBaseRecyclerViewItemHolder(view);
        } else {
            k = createGenericKInstance(cls2, view);
        }
        return k != null ? k : new ZMBaseRecyclerViewItemHolder(view);
    }

    private K createGenericKInstance(Class cls, View view) {
        try {
            if (!cls.isMemberClass() || Modifier.isStatic(cls.getModifiers())) {
                Constructor declaredConstructor = cls.getDeclaredConstructor(new Class[]{View.class});
                declaredConstructor.setAccessible(true);
                return (ZMBaseRecyclerViewItemHolder) declaredConstructor.newInstance(new Object[]{view});
            }
            Constructor declaredConstructor2 = cls.getDeclaredConstructor(new Class[]{getClass(), View.class});
            declaredConstructor2.setAccessible(true);
            return (ZMBaseRecyclerViewItemHolder) declaredConstructor2.newInstance(new Object[]{this, view});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (InstantiationException e3) {
            e3.printStackTrace();
            return null;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    private Class getInstancedGenericKClass(Class cls) {
        Type[] actualTypeArguments;
        Type genericSuperclass = cls.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            for (Type type : ((ParameterizedType) genericSuperclass).getActualTypeArguments()) {
                if (type instanceof Class) {
                    Class cls2 = (Class) type;
                    if (ZMBaseRecyclerViewItemHolder.class.isAssignableFrom(cls2)) {
                        return cls2;
                    }
                } else if (type instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) type).getRawType();
                    if (rawType instanceof Class) {
                        Class cls3 = (Class) rawType;
                        if (ZMBaseRecyclerViewItemHolder.class.isAssignableFrom(cls3)) {
                            return cls3;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    public LinearLayout getHeaderLayout() {
        return this.mHeaderLayout;
    }

    public LinearLayout getFooterLayout() {
        return this.mFooterLayout;
    }

    public int addHeaderView(View view) {
        return addHeaderView(view, -1);
    }

    public int addHeaderView(View view, int i) {
        return addHeaderView(view, i, 1);
    }

    public int addHeaderView(View view, int i, int i2) {
        if (this.mHeaderLayout == null) {
            this.mHeaderLayout = new LinearLayout(view.getContext());
            if (i2 == 1) {
                this.mHeaderLayout.setOrientation(1);
                this.mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            } else {
                this.mHeaderLayout.setOrientation(0);
                this.mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(-2, -1));
            }
        }
        int childCount = this.mHeaderLayout.getChildCount();
        if (i < 0 || i > childCount) {
            i = childCount;
        }
        this.mHeaderLayout.addView(view, i);
        if (this.mHeaderLayout.getChildCount() == 1) {
            int headerViewPosition = getHeaderViewPosition();
            if (headerViewPosition != -1) {
                notifyItemInserted(headerViewPosition);
            }
        }
        return i;
    }

    public int setHeaderView(View view) {
        return setHeaderView(view, 0, 1);
    }

    public int setHeaderView(View view, int i) {
        return setHeaderView(view, i, 1);
    }

    public int setHeaderView(View view, int i, int i2) {
        LinearLayout linearLayout = this.mHeaderLayout;
        if (linearLayout == null || linearLayout.getChildCount() <= i) {
            return addHeaderView(view, i, i2);
        }
        this.mHeaderLayout.removeViewAt(i);
        this.mHeaderLayout.addView(view, i);
        return i;
    }

    public int addFooterView(View view) {
        return addFooterView(view, -1, 1);
    }

    public int addFooterView(View view, int i) {
        return addFooterView(view, i, 1);
    }

    public int addFooterView(View view, int i, int i2) {
        if (this.mFooterLayout == null) {
            this.mFooterLayout = new LinearLayout(view.getContext());
            if (i2 == 1) {
                this.mFooterLayout.setOrientation(1);
                this.mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            } else {
                this.mFooterLayout.setOrientation(0);
                this.mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(-2, -1));
            }
        }
        int childCount = this.mFooterLayout.getChildCount();
        if (i < 0 || i > childCount) {
            i = childCount;
        }
        this.mFooterLayout.addView(view, i);
        if (this.mFooterLayout.getChildCount() == 1) {
            int footerViewPosition = getFooterViewPosition();
            if (footerViewPosition != -1) {
                notifyItemInserted(footerViewPosition);
            }
        }
        return i;
    }

    public int setFooterView(View view) {
        return setFooterView(view, 0, 1);
    }

    public int setFooterView(View view, int i) {
        return setFooterView(view, i, 1);
    }

    public int setFooterView(View view, int i, int i2) {
        LinearLayout linearLayout = this.mFooterLayout;
        if (linearLayout == null || linearLayout.getChildCount() <= i) {
            return addFooterView(view, i, i2);
        }
        this.mFooterLayout.removeViewAt(i);
        this.mFooterLayout.addView(view, i);
        return i;
    }

    public void removeHeaderView(View view) {
        if (getHeaderLayoutCount() != 0) {
            this.mHeaderLayout.removeView(view);
            if (this.mHeaderLayout.getChildCount() == 0) {
                int headerViewPosition = getHeaderViewPosition();
                if (headerViewPosition != -1) {
                    notifyItemRemoved(headerViewPosition);
                }
            }
        }
    }

    public void removeFooterView(View view) {
        if (getFooterLayoutCount() != 0) {
            this.mFooterLayout.removeView(view);
            if (this.mFooterLayout.getChildCount() == 0) {
                int footerViewPosition = getFooterViewPosition();
                if (footerViewPosition != -1) {
                    notifyItemRemoved(footerViewPosition);
                }
            }
        }
    }

    public void removeAllHeaderView() {
        if (getHeaderLayoutCount() != 0) {
            this.mHeaderLayout.removeAllViews();
            int headerViewPosition = getHeaderViewPosition();
            if (headerViewPosition != -1) {
                notifyItemRemoved(headerViewPosition);
            }
        }
    }

    public void removeAllFooterView() {
        if (getFooterLayoutCount() != 0) {
            this.mFooterLayout.removeAllViews();
            int footerViewPosition = getFooterViewPosition();
            if (footerViewPosition != -1) {
                notifyItemRemoved(footerViewPosition);
            }
        }
    }

    private int getHeaderViewPosition() {
        if (getEmptyViewCount() != 1 || this.mHeadAndEmptyEnable) {
            return 0;
        }
        return -1;
    }

    private int getFooterViewPosition() {
        int i = 1;
        if (getEmptyViewCount() != 1) {
            return getHeaderLayoutCount() + this.mData.size();
        }
        if (this.mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
            i = 2;
        }
        if (this.mFootAndEmptyEnable) {
            return i;
        }
        return -1;
    }

    public void setEmptyView(int i, ViewGroup viewGroup) {
        setEmptyView(LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false));
    }

    @Deprecated
    public void setEmptyView(int i) {
        checkNotNull();
        setEmptyView(i, getRecyclerView());
    }

    public void setEmptyView(View view) {
        boolean z;
        int i = 0;
        if (this.mEmptyLayout == null) {
            this.mEmptyLayout = new FrameLayout(view.getContext());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(-1, -1);
            ViewGroup.LayoutParams layoutParams2 = view.getLayoutParams();
            if (layoutParams2 != null) {
                layoutParams.width = layoutParams2.width;
                layoutParams.height = layoutParams2.height;
            }
            this.mEmptyLayout.setLayoutParams(layoutParams);
            z = true;
        } else {
            z = false;
        }
        this.mEmptyLayout.removeAllViews();
        this.mEmptyLayout.addView(view);
        this.mIsUseEmpty = true;
        if (z && getEmptyViewCount() == 1) {
            if (this.mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                i = 1;
            }
            notifyItemInserted(i);
        }
    }

    public void setHeaderAndEmpty(boolean z) {
        setHeaderFooterEmpty(z, false);
    }

    public void setHeaderFooterEmpty(boolean z, boolean z2) {
        this.mHeadAndEmptyEnable = z;
        this.mFootAndEmptyEnable = z2;
    }

    public void isUseEmpty(boolean z) {
        this.mIsUseEmpty = z;
    }

    public View getEmptyView() {
        return this.mEmptyLayout;
    }

    @Deprecated
    public void setAutoLoadMoreSize(int i) {
        setPreLoadNumber(i);
    }

    public void setPreLoadNumber(int i) {
        if (i > 1) {
            this.mPreLoadNumber = i;
        }
    }

    /* access modifiers changed from: protected */
    public void startAnim(Animator animator, int i) {
        animator.setDuration((long) this.mDuration).start();
        animator.setInterpolator(this.mInterpolator);
    }

    /* access modifiers changed from: protected */
    public View getItemView(@LayoutRes int i, ViewGroup viewGroup) {
        return this.mLayoutInflater.inflate(i, viewGroup, false);
    }

    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }

    public void closeLoadAnimation() {
        this.mOpenAnimationEnable = false;
    }

    public void isFirstOnly(boolean z) {
        this.mFirstOnlyEnable = z;
    }

    @Nullable
    public View getViewByPosition(int i, @IdRes int i2) {
        checkNotNull();
        return getViewByPosition(getRecyclerView(), i, i2);
    }

    @Nullable
    public View getViewByPosition(RecyclerView recyclerView, int i, @IdRes int i2) {
        if (recyclerView == null) {
            return null;
        }
        ZMBaseRecyclerViewItemHolder zMBaseRecyclerViewItemHolder = (ZMBaseRecyclerViewItemHolder) recyclerView.findViewHolderForLayoutPosition(i);
        if (zMBaseRecyclerViewItemHolder == null) {
            return null;
        }
        return zMBaseRecyclerViewItemHolder.getView(i2);
    }

    private int getItemPosition(T t) {
        if (t != null) {
            List<T> list = this.mData;
            if (list != null && !list.isEmpty()) {
                return this.mData.indexOf(t);
            }
        }
        return -1;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener) {
        this.mOnItemChildLongClickListener = onItemChildLongClickListener;
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        return this.mOnItemLongClickListener;
    }

    public final OnItemClickListener getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener() {
        return this.mOnItemChildClickListener;
    }

    @Nullable
    public final OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return this.mOnItemChildLongClickListener;
    }
}

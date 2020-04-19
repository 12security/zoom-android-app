package com.zipow.videobox.view.p014mm.sticker;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnScrollChangeListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.EmojiHelper;
import com.zipow.videobox.util.EmojiHelper.EmojiIndex;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiAdapter.OnItemViewTouchListener;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper.OnEmojiPackageInstallListener;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMPopupWindow;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmojiPanelView */
public class CommonEmojiPanelView extends LinearLayout implements OnClickListener, OnEmojiPackageInstallListener, OnTouchListener, OnRecyclerViewListener, OnItemViewTouchListener {
    private static final int SPAN_COUNT = 5;
    private List<EmojiCategory> categories;
    private CommonEmojiAdapter mAdapter;
    @Nullable
    private int[] mCategoryIndex = null;
    @NonNull
    private List<View> mDiversitiesEmojiViews = new ArrayList();
    private GridLayoutManager mGridLayoutManager;
    private boolean mNeedAdjust = false;
    private OnCommonEmojiClickListener mOnCommonEmojiClickListener;
    private View mPanelDownloadError;
    private View mPanelDownloadIng;
    private LinearLayout mPanelEmojiCategories;
    private View mPanelEmojiOneUninstall;
    private RecyclerView mPanelEmojiRecyclerView;
    private View mPanelEmojis;
    private View mPanelInstall;
    private View mPanelInstallIng;
    private View mPanelNoInstall;
    private LinearLayout mPanelZoomEmojis;
    @Nullable
    private ZMPopupWindow mPopWin;
    private ProgressBar mProgressBar;
    private TextView mTxtCategoryAnchor;
    private TextView mTxtCategoryLeft;
    private TextView mTxtCategoryRight;
    private TextView mTxtProcess;

    /* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmojiPanelView$OnCommonEmojiClickListener */
    public interface OnCommonEmojiClickListener {
        void onCommonEmojiClick(CommonEmoji commonEmoji);

        void onZoomEmojiClick(EmojiIndex emojiIndex);
    }

    public void onItemClick(View view, int i) {
        CommonEmoji commonEmoji = (CommonEmoji) this.mAdapter.getItem(i);
        if (commonEmoji != null) {
            OnCommonEmojiClickListener onCommonEmojiClickListener = this.mOnCommonEmojiClickListener;
            if (onCommonEmojiClickListener != null) {
                onCommonEmojiClickListener.onCommonEmojiClick(commonEmoji);
            }
            CommonEmojiHelper.getInstance().addFrequentUsedEmoji(commonEmoji.getKey());
        }
    }

    public boolean onItemLongClick(View view, int i) {
        showSkinTones(view);
        return false;
    }

    public CommonEmojiPanelView(Context context) {
        super(context);
        init();
    }

    public CommonEmojiPanelView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void setOnCommonEmojiClickListener(OnCommonEmojiClickListener onCommonEmojiClickListener) {
        this.mOnCommonEmojiClickListener = onCommonEmojiClickListener;
    }

    private void init() {
        View.inflate(getContext(), C4558R.layout.zm_mm_emoji_common_panel, this);
        this.mAdapter = new CommonEmojiAdapter(getContext());
        this.mAdapter.setOnRecyclerViewListener(this);
        this.mAdapter.setOnItemViewTouchListener(this);
        this.mGridLayoutManager = new GridLayoutManager(getContext(), 5, 0, false);
        this.mPanelEmojiRecyclerView = (RecyclerView) findViewById(C4558R.C4560id.panelEmojiRecyclerView);
        this.mPanelEmojiRecyclerView.setLayoutManager(this.mGridLayoutManager);
        this.mPanelEmojiRecyclerView.setAdapter(this.mAdapter);
        this.mPanelEmojiRecyclerView.setOnTouchListener(this);
        this.mPanelInstall = findViewById(C4558R.C4560id.panelInstall);
        this.mTxtProcess = (TextView) findViewById(C4558R.C4560id.txtProcess);
        this.mPanelDownloadIng = findViewById(C4558R.C4560id.panelDownloadIng);
        this.mPanelNoInstall = findViewById(C4558R.C4560id.panelNoInstall);
        this.mPanelEmojis = findViewById(C4558R.C4560id.panelEmojis);
        this.mTxtCategoryAnchor = (TextView) findViewById(C4558R.C4560id.txtCategoryAnchor);
        this.mTxtCategoryLeft = (TextView) findViewById(C4558R.C4560id.txtCategoryLeft);
        this.mTxtCategoryRight = (TextView) findViewById(C4558R.C4560id.txtCategoryRight);
        this.mPanelInstallIng = findViewById(C4558R.C4560id.panelInstallIng);
        this.mProgressBar = (ProgressBar) findViewById(C4558R.C4560id.progressBar);
        this.mPanelDownloadError = findViewById(C4558R.C4560id.panelDownloadError);
        this.mPanelZoomEmojis = (LinearLayout) findViewById(C4558R.C4560id.panelZoomEmojis);
        this.mPanelEmojiCategories = (LinearLayout) findViewById(C4558R.C4560id.panelEmojiCategories);
        this.mPanelEmojiOneUninstall = findViewById(C4558R.C4560id.panelEmojiOneUninstall);
        findViewById(C4558R.C4560id.btnStartUse).setOnClickListener(this);
        findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        findViewById(C4558R.C4560id.btnRetry).setOnClickListener(this);
        if (VERSION.SDK_INT < 23) {
            this.mPanelEmojiRecyclerView.setOnScrollListener(new OnScrollListener() {
                public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
                    super.onScrolled(recyclerView, i, i2);
                    CommonEmojiPanelView.this.onRecyclerViewScrolled();
                }
            });
        } else {
            this.mPanelEmojiRecyclerView.setOnScrollChangeListener(new OnScrollChangeListener() {
                public void onScrollChange(View view, int i, int i2, int i3, int i4) {
                    CommonEmojiPanelView.this.onRecyclerViewScrolled();
                }
            });
        }
        updateUI();
    }

    private void updateUI() {
        updateUI(false);
    }

    private void updateUI(boolean z) {
        CommonEmojiHelper instance = CommonEmojiHelper.getInstance();
        if (instance.isEmojiInstalled()) {
            installEmoji();
            this.mPanelEmojis.setVisibility(0);
            this.mPanelInstall.setVisibility(8);
            this.mPanelDownloadError.setVisibility(8);
            return;
        }
        installZoomEmoji();
        this.mPanelInstall.setVisibility(0);
        this.mPanelEmojis.setVisibility(8);
        int downloadProcess = instance.getDownloadProcess();
        if (downloadProcess != -1) {
            this.mPanelDownloadError.setVisibility(8);
            this.mPanelDownloadIng.setVisibility(0);
            this.mPanelNoInstall.setVisibility(8);
            this.mTxtProcess.setText(getResources().getString(C4558R.string.zm_lbl_download_emoji_process_23626, new Object[]{Integer.valueOf(downloadProcess)}));
            this.mProgressBar.setProgress(downloadProcess);
        } else if (z) {
            this.mPanelDownloadIng.setVisibility(8);
            this.mPanelNoInstall.setVisibility(8);
            this.mPanelDownloadError.setVisibility(0);
        } else {
            this.mPanelDownloadIng.setVisibility(8);
            this.mPanelNoInstall.setVisibility(0);
            this.mPanelDownloadError.setVisibility(8);
        }
        instance.addListener(this);
    }

    public List<EmojiCategory> getEmojiCategories() {
        if (!CollectionsUtil.isCollectionEmpty(this.categories)) {
            return this.categories;
        }
        this.categories = new ArrayList(CommonEmojiHelper.getInstance().getEmojiCategories());
        return this.categories;
    }

    private void installEmoji() {
        CommonEmojiHelper.getInstance();
        List emojiCategories = getEmojiCategories();
        if (!CollectionsUtil.isListEmpty(emojiCategories)) {
            int[] iArr = this.mCategoryIndex;
            if (iArr == null || iArr.length != this.categories.size()) {
                this.mCategoryIndex = new int[this.categories.size()];
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.categories.size(); i++) {
                this.mCategoryIndex[i] = arrayList.size();
                EmojiCategory emojiCategory = (EmojiCategory) emojiCategories.get(i);
                if (emojiCategory != null) {
                    int i2 = 0;
                    for (CommonEmoji commonEmoji : emojiCategory.getEmojis()) {
                        if (!commonEmoji.isIllegal()) {
                            arrayList.add(commonEmoji);
                            i2++;
                        }
                    }
                    int i3 = i2 % 5;
                    if (i3 != 0) {
                        int i4 = 5 - i3;
                        for (int i5 = 0; i5 < i4; i5++) {
                            arrayList.add(new CommonEmoji());
                        }
                    }
                }
            }
            this.mAdapter.setData(arrayList);
            this.mTxtCategoryAnchor.setText(getCategoryTitleLable(((EmojiCategory) emojiCategories.get(0)).getName()));
            installEmojiCategory();
        }
    }

    private void installEmojiCategory() {
        if (CommonEmojiHelper.getInstance().isEmojiInstalled() && this.mPanelEmojiCategories.getChildCount() != getEmojiCategories().size()) {
            this.mPanelEmojiCategories.removeAllViews();
            LayoutParams layoutParams = new LayoutParams(0, -1);
            layoutParams.weight = 1.0f;
            for (EmojiCategory emojiCategory : getEmojiCategories()) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setId(C4558R.C4560id.emojiCategory);
                linearLayout.setTag(emojiCategory);
                linearLayout.setContentDescription(emojiCategory.getLabel());
                linearLayout.setGravity(17);
                ImageView imageView = new ImageView(getContext());
                int dip2px = UIUtil.dip2px(getContext(), 1.0f);
                imageView.setPadding(dip2px, dip2px, dip2px, dip2px);
                imageView.setImageResource(emojiCategory.getIconResource());
                linearLayout.addView(imageView);
                this.mPanelEmojiCategories.addView(linearLayout, layoutParams);
                linearLayout.setOnClickListener(this);
            }
            if (this.mPanelEmojiCategories.getChildCount() > 0) {
                this.mPanelEmojiCategories.getChildAt(0).setSelected(true);
            }
        }
    }

    private void installZoomEmoji() {
        if (this.mPanelZoomEmojis.getChildCount() <= 0) {
            if (!OsUtil.isAtLeastKLP()) {
                this.mPanelEmojiOneUninstall.setVisibility(8);
                LayoutParams layoutParams = (LayoutParams) this.mPanelZoomEmojis.getLayoutParams();
                layoutParams.width = -1;
                this.mPanelZoomEmojis.setLayoutParams(layoutParams);
            }
            LinearLayout linearLayout = null;
            int i = 0;
            for (EmojiIndex emojiIndex : EmojiHelper.getInstance().getZMEmojis()) {
                if (linearLayout == null || i >= linearLayout.getChildCount()) {
                    i = 0;
                }
                if (i == 0) {
                    View inflate = View.inflate(getContext(), C4558R.layout.zm_mm_emoji_zoom_panel_item, null);
                    LayoutParams layoutParams2 = new LayoutParams(-2, -2);
                    if (!OsUtil.isAtLeastKLP()) {
                        layoutParams2 = new LayoutParams(0, -2);
                        layoutParams2.weight = 1.0f;
                    }
                    this.mPanelZoomEmojis.addView(inflate, layoutParams2);
                    linearLayout = (LinearLayout) inflate.findViewById(C4558R.C4560id.panelCommonEmojis);
                }
                ImageView imageView = (ImageView) linearLayout.getChildAt(i);
                imageView.setImageResource(emojiIndex.getDrawResource());
                imageView.setTag(emojiIndex);
                imageView.setOnClickListener(this);
                i++;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        CommonEmojiHelper.getInstance().addListener(this);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CommonEmojiHelper.getInstance().removeListener(this);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnStartUse) {
            CommonEmojiHelper.getInstance().installEmoji();
            updateUI();
        } else if (id == C4558R.C4560id.btnCancel) {
            CommonEmojiHelper.getInstance().cancelInstallEmoji();
            updateUI();
        } else if (id == C4558R.C4560id.btnRetry) {
            CommonEmojiHelper.getInstance().installEmoji();
            updateUI();
        } else if (id == C4558R.C4560id.emojiCategory) {
            onClickEmojiCategory(view);
        } else {
            Object tag = view.getTag();
            if (tag instanceof CommonEmoji) {
                ZMPopupWindow zMPopupWindow = this.mPopWin;
                if (zMPopupWindow != null && zMPopupWindow.isShowing()) {
                    this.mPopWin.dismiss();
                    this.mPopWin = null;
                }
                OnCommonEmojiClickListener onCommonEmojiClickListener = this.mOnCommonEmojiClickListener;
                if (onCommonEmojiClickListener != null) {
                    onCommonEmojiClickListener.onCommonEmojiClick((CommonEmoji) tag);
                }
                CommonEmojiHelper.getInstance().addFrequentUsedEmoji(((CommonEmoji) tag).getKey());
            } else if (tag instanceof EmojiIndex) {
                OnCommonEmojiClickListener onCommonEmojiClickListener2 = this.mOnCommonEmojiClickListener;
                if (onCommonEmojiClickListener2 != null) {
                    onCommonEmojiClickListener2.onZoomEmojiClick((EmojiIndex) tag);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(@NonNull View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i == 0) {
            installEmoji();
        }
    }

    private void onClickEmojiCategory(@Nullable View view) {
        if (this.mCategoryIndex != null && view != null && !view.isSelected()) {
            Object tag = view.getTag();
            if (tag instanceof EmojiCategory) {
                int indexOf = getEmojiCategories().indexOf((EmojiCategory) tag);
                if (indexOf >= 0) {
                    int[] iArr = this.mCategoryIndex;
                    if (indexOf < iArr.length) {
                        int i = iArr[indexOf];
                        if (i < this.mAdapter.getItemCount()) {
                            for (int i2 = 0; i2 < this.mPanelEmojiCategories.getChildCount(); i2++) {
                                View childAt = this.mPanelEmojiCategories.getChildAt(i2);
                                childAt.setSelected(childAt == view);
                            }
                            moveToPosition(i);
                        }
                    }
                }
            }
        }
    }

    private void moveToPosition(int i) {
        GridLayoutManager gridLayoutManager = this.mGridLayoutManager;
        if (gridLayoutManager != null && this.mPanelEmojiRecyclerView != null) {
            int findFirstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = this.mGridLayoutManager.findLastVisibleItemPosition();
            if (i <= findFirstVisibleItemPosition) {
                this.mPanelEmojiRecyclerView.scrollToPosition(i);
            } else if (i <= findLastVisibleItemPosition) {
                this.mPanelEmojiRecyclerView.scrollBy(this.mPanelEmojiRecyclerView.getChildAt(i - findFirstVisibleItemPosition).getLeft(), 0);
            } else {
                this.mPanelEmojiRecyclerView.scrollToPosition(i);
                this.mNeedAdjust = true;
            }
        }
    }

    private void adjustPosition() {
        if (this.mNeedAdjust) {
            this.mNeedAdjust = false;
            int i = 0;
            while (true) {
                if (i >= this.mPanelEmojiCategories.getChildCount()) {
                    i = 0;
                    break;
                }
                View childAt = this.mPanelEmojiCategories.getChildAt(i);
                if (childAt != null && childAt.isSelected()) {
                    break;
                }
                i++;
            }
            int[] iArr = this.mCategoryIndex;
            if (iArr != null && iArr.length != 0) {
                int i2 = iArr[i];
                if (i2 < this.mAdapter.getItemCount()) {
                    int findFirstVisibleItemPosition = i2 - this.mGridLayoutManager.findFirstVisibleItemPosition();
                    if (findFirstVisibleItemPosition >= 0 && findFirstVisibleItemPosition < this.mPanelEmojiRecyclerView.getChildCount()) {
                        View childAt2 = this.mPanelEmojiRecyclerView.getChildAt(findFirstVisibleItemPosition);
                        if (childAt2 != null) {
                            this.mPanelEmojiRecyclerView.scrollBy(childAt2.getLeft(), 0);
                        }
                    }
                }
            }
        }
    }

    public void onEmojiPkgDownload(int i) {
        updateUI();
    }

    public void onEmojiPkgInstalled() {
        this.mPanelInstall.setVisibility(0);
        this.mPanelEmojis.setVisibility(8);
        this.mPanelInstallIng.setVisibility(0);
        this.mPanelNoInstall.setVisibility(8);
        this.mPanelDownloadIng.setVisibility(8);
        this.mPanelDownloadIng.setVisibility(8);
        updateUI();
    }

    public void onEmojiPkgDownloadFailed() {
        updateUI(true);
    }

    public boolean onTouch(View view, @NonNull MotionEvent motionEvent) {
        ZMPopupWindow zMPopupWindow = this.mPopWin;
        if (zMPopupWindow == null || !zMPopupWindow.isShowing()) {
            return false;
        }
        float rawX = motionEvent.getRawX();
        Rect rect = new Rect();
        View view2 = null;
        for (View view3 : this.mDiversitiesEmojiViews) {
            view3.getGlobalVisibleRect(rect);
            if (rawX < ((float) rect.left) || rawX > ((float) rect.right)) {
                view3.setBackgroundColor(getResources().getColor(C4558R.color.zm_white));
            } else {
                view3.setBackgroundColor(getResources().getColor(C4558R.color.zm_highlight));
                view2 = view3;
            }
        }
        if (motionEvent.getAction() == 1) {
            if (view2 != null) {
                Object tag = view2.getTag();
                if (tag instanceof CommonEmoji) {
                    OnCommonEmojiClickListener onCommonEmojiClickListener = this.mOnCommonEmojiClickListener;
                    if (onCommonEmojiClickListener != null) {
                        CommonEmoji commonEmoji = (CommonEmoji) tag;
                        onCommonEmojiClickListener.onCommonEmojiClick(commonEmoji);
                        CommonEmojiHelper.getInstance().addFrequentUsedEmoji(commonEmoji.getKey());
                    }
                }
            }
            this.mPopWin.dismiss();
            this.mPopWin = null;
        }
        return true;
    }

    private String getCategoryTitleLable(String str) {
        Resources resources = getResources();
        if (resources == null) {
            return null;
        }
        Context globalContext = VideoBoxApplication.getGlobalContext();
        int i = 0;
        if (globalContext != null) {
            i = resources.getIdentifier(String.format("zm_lbl_emoji_one_category_%s_23626", new Object[]{str}), "string", globalContext.getPackageName());
        }
        if (i == 0) {
            return str;
        }
        return resources.getString(i);
    }

    private void showSkinTones(@Nullable View view) {
        if (view != null) {
            Object tag = view.getTag();
            if (tag instanceof CommonEmoji) {
                CommonEmoji commonEmoji = (CommonEmoji) tag;
                if (!CollectionsUtil.isListEmpty(commonEmoji.getDiversityEmojis())) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(commonEmoji);
                    arrayList.addAll(commonEmoji.getDiversityEmojis());
                    this.mDiversitiesEmojiViews.clear();
                    Context context = getContext();
                    LinearLayout linearLayout = (LinearLayout) View.inflate(getContext(), C4558R.layout.zm_mm_emoji_common_diversities, null).findViewById(C4558R.C4560id.panelCommonEmojis);
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        TextView textView = (TextView) linearLayout.getChildAt(i);
                        if (i < arrayList.size()) {
                            CommonEmoji commonEmoji2 = (CommonEmoji) arrayList.get(i);
                            textView.setText(commonEmoji2.getOutput());
                            textView.setTag(commonEmoji2);
                            textView.setOnClickListener(this);
                            this.mDiversitiesEmojiViews.add(textView);
                        } else {
                            textView.setVisibility(8);
                        }
                    }
                    RelativeLayout relativeLayout = new RelativeLayout(context);
                    this.mPopWin = new ZMPopupWindow((View) relativeLayout, -1, -1);
                    this.mPopWin.setDismissOnTouchOutside(true);
                    Rect rect = new Rect();
                    view.getGlobalVisibleRect(rect);
                    boolean isFullScreen = context instanceof Activity ? UIUtil.isFullScreen((Activity) context) : false;
                    linearLayout.measure(0, 0);
                    int measuredWidth = linearLayout.getMeasuredWidth();
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                    layoutParams.topMargin = (rect.top - (isFullScreen ? 0 : UIUtil.getStatusBarHeight(context))) - linearLayout.getMeasuredHeight();
                    int i2 = (rect.left + rect.right) / 2;
                    int displayWidth = UIUtil.getDisplayWidth(context);
                    int dip2px = UIUtil.dip2px(context, 10.0f);
                    int i3 = measuredWidth / 2;
                    if (i2 + i3 > displayWidth - dip2px) {
                        layoutParams.leftMargin = (displayWidth - measuredWidth) - dip2px;
                    } else {
                        int i4 = i2 - i3;
                        if (i4 < dip2px) {
                            layoutParams.leftMargin = dip2px;
                        } else {
                            layoutParams.leftMargin = i4;
                        }
                    }
                    relativeLayout.addView(linearLayout, layoutParams);
                    this.mPopWin.showAtLocation(view.getRootView(), 48, 0, 0);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onRecyclerViewScrolled() {
        adjustPosition();
        updateCategoryAnchor();
    }

    private void updateCategoryAnchor() {
        if (this.mCategoryIndex != null) {
            int findFirstVisibleItemPosition = this.mGridLayoutManager.findFirstVisibleItemPosition();
            int i = 0;
            while (true) {
                int[] iArr = this.mCategoryIndex;
                if (i >= iArr.length - 1) {
                    i = 0;
                    break;
                }
                int i2 = i + 1;
                if (findFirstVisibleItemPosition < iArr[i2]) {
                    break;
                }
                i = i2;
            }
            int[] iArr2 = this.mCategoryIndex;
            if (findFirstVisibleItemPosition >= iArr2[iArr2.length - 1]) {
                i = iArr2.length - 1;
            }
            int i3 = i + 1;
            List emojiCategories = getEmojiCategories();
            this.mTxtCategoryLeft.setVisibility(8);
            this.mTxtCategoryRight.setVisibility(8);
            View view = null;
            if (i3 < emojiCategories.size()) {
                view = this.mGridLayoutManager.findViewByPosition(this.mCategoryIndex[i3]);
            }
            if (view == null) {
                if (i < emojiCategories.size()) {
                    this.mTxtCategoryAnchor.setText(getCategoryTitleLable(((EmojiCategory) emojiCategories.get(i)).getName()));
                    this.mTxtCategoryAnchor.setVisibility(0);
                    int i4 = 0;
                    while (i4 < this.mPanelEmojiCategories.getChildCount()) {
                        this.mPanelEmojiCategories.getChildAt(i4).setSelected(i4 == i);
                        i4++;
                    }
                }
            } else if (i3 < emojiCategories.size()) {
                Rect rect = new Rect();
                this.mTxtCategoryAnchor.getLocalVisibleRect(rect);
                int left = view.getLeft();
                int max = Math.max(left, 0);
                ((RelativeLayout.LayoutParams) this.mTxtCategoryRight.getLayoutParams()).leftMargin = max;
                this.mTxtCategoryLeft.setText(getCategoryTitleLable(((EmojiCategory) emojiCategories.get(i)).getName()));
                this.mTxtCategoryLeft.measure(0, 0);
                int i5 = rect.left;
                int i6 = rect.right;
                if (this.mTxtCategoryLeft.getMeasuredWidth() > i6 - i5) {
                    i6 = this.mTxtCategoryLeft.getMeasuredWidth() + i5;
                }
                if (max < i5 || max > i6) {
                    this.mTxtCategoryAnchor.setVisibility(0);
                    this.mTxtCategoryAnchor.setText(getCategoryTitleLable(((EmojiCategory) emojiCategories.get(i)).getName()));
                } else {
                    this.mTxtCategoryAnchor.setVisibility(8);
                    this.mTxtCategoryLeft.setVisibility(0);
                    ((RelativeLayout.LayoutParams) this.mTxtCategoryLeft.getLayoutParams()).leftMargin = Math.min(left - this.mTxtCategoryLeft.getMeasuredWidth(), 0);
                }
                this.mTxtCategoryRight.setVisibility(0);
                this.mTxtCategoryRight.setText(getCategoryTitleLable(((EmojiCategory) emojiCategories.get(i3)).getName()));
            }
        }
    }
}

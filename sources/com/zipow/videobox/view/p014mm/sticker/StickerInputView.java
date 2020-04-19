package com.zipow.videobox.view.p014mm.sticker;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.google.common.primitives.Ints;
import com.zipow.videobox.ptapp.IMProtos.GiphyMsgInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.EmojiHelper.EmojiIndex;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.view.GiphyPreviewView;
import com.zipow.videobox.view.GiphyPreviewView.GiphyPreviewItem;
import com.zipow.videobox.view.GiphyPreviewView.OnBackClickListener;
import com.zipow.videobox.view.GiphyPreviewView.OnGiphyPreviewItemClickListener;
import com.zipow.videobox.view.GiphyPreviewView.OnSearchListener;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiPanelView.OnCommonEmojiClickListener;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMViewPager;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.sticker.StickerInputView */
public class StickerInputView extends LinearLayout implements OnClickListener, OnCommonEmojiClickListener {
    public static final int CATE_PRIVATE_STICKER = 2;
    public static final int CATE_ZOOM_EMOJI = 1;
    public static final int MODE_STICK_EMOJI = 0;
    public static final int MODE_STICK_GIPHT_HIDE = 2;
    public static final int MODE_STICK_GIPHY = 1;
    public static final int MODE_STICK_GIPHY_BLOCK = 3;
    private static final String TAG = "StickerInputView";
    private EditText mEditText;
    private GiphyPreviewView mGiphyPreview;
    /* access modifiers changed from: private */
    public OnGiphyPreviewItemClickListener mGiphyPreviewItemClickListener;
    private boolean mIsGiphyDisable;
    private boolean mIsPrivateStickDisable;
    private int mKeyboardHeight;
    /* access modifiers changed from: private */
    public OnGiphyPreviewBackClickListener mOnGiphyPreviewBackClickListener;
    private OnStickerSelectListener mOnGiphySelectListener;
    private OnPrivateStickerSelectListener mOnPrivateStickerSelectListener;
    /* access modifiers changed from: private */
    public OnSearchListener mOnsearchListener;
    private CommonEmojiPanelView mPanelCommonEmojisView;
    private View mPanelEmoji;
    private View mPanelEmojiType;
    private View mPanelGiphyType;
    private LinearLayout mPanelIndicator;
    private View mPanelLinear;
    private View mPanelStickerType;
    private StickerAdapter mStickerAdapter;
    private StickerManager mStickerManager;
    private ZMViewPager mStickerPager;
    /* access modifiers changed from: private */
    public int mode = 0;

    /* renamed from: com.zipow.videobox.view.mm.sticker.StickerInputView$OnGiphyPreviewBackClickListener */
    public interface OnGiphyPreviewBackClickListener {
        void onGiphyPreviewBack();
    }

    /* renamed from: com.zipow.videobox.view.mm.sticker.StickerInputView$OnPrivateStickerSelectListener */
    public interface OnPrivateStickerSelectListener {
        void onPrivateStickerSelect(StickerEvent stickerEvent);
    }

    /* renamed from: com.zipow.videobox.view.mm.sticker.StickerInputView$OnStickerSelectListener */
    public interface OnStickerSelectListener {
        void onStickerSelect(View view);
    }

    public StickerInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public StickerInputView(Context context) {
        super(context);
        init();
    }

    public int getMode() {
        return this.mode;
    }

    public void setKeyboardHeight(int i) {
        if (getResources().getConfiguration().orientation == 1 && getResources().getDisplayMetrics().heightPixels - i > UIUtil.dip2px(getContext(), 100.0f)) {
            if (i != this.mKeyboardHeight) {
                PreferenceUtil.saveIntValue(PreferenceUtil.KEYBOARD_HEIGHT, i);
            }
            this.mKeyboardHeight = i;
        }
    }

    public void setOnPrivateStickerSelectListener(OnPrivateStickerSelectListener onPrivateStickerSelectListener) {
        this.mOnPrivateStickerSelectListener = onPrivateStickerSelectListener;
    }

    public void setmOnGiphySelectListener(OnStickerSelectListener onStickerSelectListener) {
        this.mOnGiphySelectListener = onStickerSelectListener;
    }

    public void setmGiphyPreviewItemClickListener(OnGiphyPreviewItemClickListener onGiphyPreviewItemClickListener) {
        this.mGiphyPreviewItemClickListener = onGiphyPreviewItemClickListener;
    }

    public void setmOnGiphyPreviewBackClickListener(OnGiphyPreviewBackClickListener onGiphyPreviewBackClickListener) {
        this.mOnGiphyPreviewBackClickListener = onGiphyPreviewBackClickListener;
    }

    public void onStickerDownloaded(String str, int i) {
        this.mStickerAdapter.onStickerDownloaded(str, i);
    }

    public void hideGiphyAndSticker(boolean z) {
        if (z) {
            this.mPanelGiphyType.setVisibility(8);
            this.mPanelStickerType.setVisibility(8);
            this.mPanelEmojiType.setVisibility(0);
            this.mPanelEmojiType.setSelected(true);
            this.mGiphyPreview.setVisibility(8);
            this.mPanelEmoji.setVisibility(8);
            this.mPanelCommonEmojisView.setVisibility(0);
            return;
        }
        if (!this.mIsGiphyDisable) {
            this.mPanelGiphyType.setVisibility(0);
        }
        if (!this.mIsPrivateStickDisable) {
            this.mPanelStickerType.setVisibility(0);
        }
    }

    public void enableCustomSticker(boolean z) {
        if (z) {
            this.mPanelStickerType.setVisibility(0);
            this.mStickerManager.refreshAllStickerView();
            reloadAll();
            this.mIsPrivateStickDisable = false;
            return;
        }
        this.mPanelStickerType.setVisibility(8);
        this.mStickerManager.refreshAllStickerView();
        this.mPanelEmojiType.setSelected(true);
        reloadAll();
        this.mIsPrivateStickDisable = true;
    }

    public void disableGiphy() {
        this.mGiphyPreview.setVisibility(8);
        this.mPanelGiphyType.setVisibility(8);
        this.mIsGiphyDisable = true;
    }

    public void setmGiphyPreviewVisible(int i) {
        this.mGiphyPreview.setPreviewVisible(i);
        this.mPanelLinear.setVisibility(i);
        if (i == 0) {
            this.mode = 1;
        } else {
            this.mode = 2;
        }
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3 = this.mode;
        if (i3 == 2) {
            super.onMeasure(i, i2);
            return;
        }
        int panelMiniHeight = (i3 == 0 ? this.mStickerManager.getPanelMiniHeight() : this.mKeyboardHeight) + UIUtil.dip2px(getContext(), 55.0f);
        if (getResources().getConfiguration().orientation == 1) {
            if (this.mode == 0) {
                int i4 = this.mKeyboardHeight;
                if (panelMiniHeight <= i4) {
                    panelMiniHeight = i4;
                }
            } else if (panelMiniHeight <= this.mStickerManager.getPanelMiniHeight() + UIUtil.dip2px(getContext(), 55.0f)) {
                panelMiniHeight = this.mStickerManager.getPanelMiniHeight() + UIUtil.dip2px(getContext(), 55.0f);
            }
        } else if (this.mode != 0 && panelMiniHeight <= this.mStickerManager.getPanelMiniHeight() + UIUtil.dip2px(getContext(), 55.0f)) {
            panelMiniHeight = this.mStickerManager.getPanelMiniHeight() + UIUtil.dip2px(getContext(), 55.0f);
        }
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(panelMiniHeight, Ints.MAX_POWER_OF_TWO));
    }

    public void setEmojiInputEditText(EditText editText) {
        this.mEditText = editText;
    }

    public void OnNewStickerUploaded(String str, int i, String str2) {
        if (i == 0) {
            reloadAll();
        }
    }

    public void OnMakePrivateSticker(int i, String str, String str2) {
        if (i == 0) {
            reloadAll();
        }
    }

    public void OnDiscardPrivateSticker(int i, String str) {
        if (i == 0) {
            reloadAll();
        }
    }

    public void OnPrivateStickersUpdated() {
        reloadAll();
    }

    public void onStickerEvent(@Nullable StickerEvent stickerEvent) {
        if (stickerEvent != null) {
            switch (stickerEvent.getEventType()) {
                case 1:
                    onEmojiInput(stickerEvent.getEmoji());
                    break;
                case 2:
                    onStickerDelete();
                    break;
                case 3:
                    OnPrivateStickerSelectListener onPrivateStickerSelectListener = this.mOnPrivateStickerSelectListener;
                    if (onPrivateStickerSelectListener != null) {
                        onPrivateStickerSelectListener.onPrivateStickerSelect(stickerEvent);
                        break;
                    }
                    break;
                case 4:
                    onEmojiInput(stickerEvent.getCommonEmoji());
                    break;
            }
        }
    }

    public void reloadAll() {
        this.mStickerManager.refreshAllStickerView();
        int currentItem = this.mStickerPager.getCurrentItem();
        this.mStickerPager.removeAllViews();
        this.mStickerAdapter.updatePanelStickerViews(this.mStickerManager.getAllStickerView());
        this.mStickerAdapter.notifyDataSetChanged();
        if (currentItem >= this.mStickerAdapter.getCount()) {
            currentItem = this.mStickerAdapter.getCount() - 1;
        }
        this.mStickerPager.setCurrentItem(currentItem, false);
    }

    public void onBackPressed() {
        if (this.mode != 0) {
            this.mode = 3;
        }
    }

    private void init() {
        this.mStickerManager = new StickerManager(getContext());
        View.inflate(getContext(), C4558R.layout.zm_mm_emoji_input_view, this);
        this.mStickerPager = (ZMViewPager) findViewById(C4558R.C4560id.emojiPager);
        this.mStickerPager.setDisableScroll(false);
        this.mGiphyPreview = (GiphyPreviewView) findViewById(C4558R.C4560id.panelGiphyPreview);
        this.mStickerAdapter = new StickerAdapter(getContext(), this.mStickerManager.getAllStickerView(), this);
        this.mStickerPager.setAdapter(this.mStickerAdapter);
        this.mPanelLinear = findViewById(C4558R.C4560id.panelType);
        this.mPanelEmojiType = findViewById(C4558R.C4560id.panelEmojiType);
        this.mPanelGiphyType = findViewById(C4558R.C4560id.panelGiphyType);
        this.mPanelStickerType = findViewById(C4558R.C4560id.panelStickerType);
        this.mPanelIndicator = (LinearLayout) findViewById(C4558R.C4560id.panelEmojiIndicator);
        this.mPanelEmojiType.setSelected(true);
        this.mPanelCommonEmojisView = (CommonEmojiPanelView) findViewById(C4558R.C4560id.panelCommonEmojisView);
        this.mPanelEmoji = findViewById(C4558R.C4560id.panelEmoji);
        if (PreferenceUtil.readIntValue(PreferenceUtil.IM_GIPHY_OPTION, 0) != 1) {
            this.mPanelGiphyType.setVisibility(8);
        } else {
            this.mPanelGiphyType.setVisibility(0);
        }
        this.mPanelCommonEmojisView.setOnCommonEmojiClickListener(this);
        this.mGiphyPreview.setmGiphyPreviewItemClickListener(new OnGiphyPreviewItemClickListener() {
            public void onGiphyPreviewItemClick(@Nullable GiphyPreviewItem giphyPreviewItem) {
                if (StickerInputView.this.mGiphyPreviewItemClickListener != null) {
                    StickerInputView.this.mGiphyPreviewItemClickListener.onGiphyPreviewItemClick(giphyPreviewItem);
                }
                if (giphyPreviewItem != null && giphyPreviewItem.getInfo() != null) {
                    ZoomLogEventTracking.eventTrackSelectGiphy(giphyPreviewItem.getInfo().getId());
                }
            }
        });
        this.mGiphyPreview.setmOnBackClickListener(new OnBackClickListener() {
            public void onBackClick(View view) {
                if (StickerInputView.this.mOnGiphyPreviewBackClickListener != null) {
                    StickerInputView.this.mode = 3;
                    StickerInputView.this.mOnGiphyPreviewBackClickListener.onGiphyPreviewBack();
                }
            }
        });
        this.mGiphyPreview.setOnSearchListener(new OnSearchListener() {
            public void onSearch(@Nullable String str) {
                if (StickerInputView.this.mOnsearchListener != null) {
                    StickerInputView.this.mOnsearchListener.onSearch(str);
                }
                if (str != null) {
                    ZoomLogEventTracking.eventTrackSearchGiphy(str);
                }
            }
        });
        this.mStickerPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                StickerInputView.this.refreshIndicator();
            }
        });
        this.mKeyboardHeight = PreferenceUtil.readIntValue(PreferenceUtil.KEYBOARD_HEIGHT, 0);
        this.mPanelEmojiType.setOnClickListener(this);
        this.mPanelGiphyType.setOnClickListener(this);
        this.mPanelStickerType.setOnClickListener(this);
        Context context = getContext();
        if (context instanceof ZMActivity) {
            ((ZMActivity) context).addDisableGestureFinishView(this);
        }
    }

    public void onStickerDelete() {
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.dispatchKeyEvent(new KeyEvent(0, 67));
        }
    }

    public void onEmojiInput(@Nullable EmojiIndex emojiIndex) {
        EditText editText = this.mEditText;
        if (editText != null && emojiIndex != null) {
            this.mEditText.getText().replace(editText.getSelectionStart(), this.mEditText.getSelectionEnd(), CommonEmojiHelper.getInstance().formatImgEmojiSize(this.mEditText.getTextSize(), emojiIndex.getShortCut(), true));
            ZoomLogEventTracking.eventTrackSelectEmoji(emojiIndex.getShortCut());
        }
    }

    public void onEmojiInput(@Nullable CommonEmoji commonEmoji) {
        EditText editText = this.mEditText;
        if (editText != null && commonEmoji != null) {
            this.mEditText.getText().replace(editText.getSelectionStart(), this.mEditText.getSelectionEnd(), CommonEmojiHelper.getInstance().formatImgEmojiSize(this.mEditText.getTextSize(), commonEmoji.getOutput(), true));
            ZoomLogEventTracking.eventTrackSelectEmoji(commonEmoji.getShortName());
        }
    }

    /* access modifiers changed from: private */
    public void refreshIndicator() {
        StickerPanelView item = this.mStickerAdapter.getItem(this.mStickerPager.getCurrentItem());
        if (item != null) {
            int indexInCategory = item.getIndexInCategory();
            int category = item.getCategory();
            int countByCategory = this.mStickerManager.getCountByCategory(category);
            this.mPanelIndicator.removeAllViews();
            boolean z = true;
            this.mPanelEmojiType.setSelected(category == 1);
            View view = this.mPanelStickerType;
            if (category != 2) {
                z = false;
            }
            view.setSelected(z);
            if (countByCategory >= 2) {
                for (int i = 0; i < countByCategory; i++) {
                    ImageView imageView = new ImageView(getContext());
                    int i2 = C4558R.C4559drawable.zm_btn_switch_scene_selected_normal;
                    if (i == indexInCategory) {
                        i2 = C4558R.C4559drawable.zm_btn_switch_scene_unselected_normal;
                    }
                    imageView.setImageResource(i2);
                    int dip2px = UIUtil.dip2px(getContext(), 3.0f);
                    imageView.setPadding(dip2px, 0, dip2px, 0);
                    this.mPanelIndicator.addView(imageView);
                }
            }
        }
    }

    public void setOnsearchListener(OnSearchListener onSearchListener) {
        this.mOnsearchListener = onSearchListener;
    }

    public void Indicate_GetGIFFromGiphyResultIml(int i, String str, @Nullable List<String> list, String str2, String str3) {
        if (i == 0) {
            ArrayList arrayList = new ArrayList();
            if (list != null && !list.isEmpty()) {
                for (String str4 : list) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null && !TextUtils.isEmpty(str4)) {
                        GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(str4);
                        if (giphyInfo != null) {
                            arrayList.add(giphyInfo);
                        }
                    }
                }
            }
            this.mGiphyPreview.setDatas(str2, arrayList);
            return;
        }
        this.mGiphyPreview.updateEmptyViewMode(2);
    }

    public void Indicate_GetHotGiphyInfoResult(int i, String str, @Nullable List<String> list, String str2, String str3) {
        if (i == 0) {
            ArrayList arrayList = new ArrayList();
            if (list != null && !list.isEmpty()) {
                for (String str4 : list) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null && !TextUtils.isEmpty(str4)) {
                        GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(str4);
                        if (giphyInfo != null) {
                            arrayList.add(giphyInfo);
                        }
                    }
                }
            }
            if (arrayList.size() >= 7) {
                ArrayList arrayList2 = new ArrayList();
                ArrayList arrayList3 = new ArrayList();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    if (i2 % 2 == 0) {
                        arrayList3.add(arrayList.get(i2));
                    } else {
                        arrayList2.add(arrayList.get(i2));
                    }
                }
                arrayList.clear();
                arrayList.addAll(arrayList3);
                arrayList.addAll(arrayList2);
            }
            this.mGiphyPreview.setDatas(str2, arrayList);
            return;
        }
        this.mGiphyPreview.updateEmptyViewMode(2);
    }

    public boolean hasGiphyData() {
        GiphyPreviewView giphyPreviewView = this.mGiphyPreview;
        return giphyPreviewView != null && giphyPreviewView.hasData();
    }

    public void setGiphyVisiable(int i) {
        if (this.mPanelGiphyType != null) {
            if (PreferenceUtil.readIntValue(PreferenceUtil.IM_GIPHY_OPTION, 0) != 1) {
                i = 8;
            }
            boolean z = this.mPanelGiphyType.getVisibility() != i;
            this.mPanelGiphyType.setVisibility(i);
            if (z) {
                this.mode = 0;
                this.mPanelEmojiType.setSelected(true);
                this.mPanelStickerType.setSelected(false);
                this.mPanelGiphyType.setSelected(false);
                this.mGiphyPreview.setVisibility(8);
                this.mPanelEmoji.setVisibility(8);
                this.mPanelCommonEmojisView.setVisibility(0);
            }
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.panelEmojiType) {
            this.mode = 0;
            this.mPanelEmojiType.setSelected(true);
            this.mPanelStickerType.setSelected(false);
            this.mPanelGiphyType.setSelected(false);
            this.mGiphyPreview.setVisibility(8);
            this.mPanelEmoji.setVisibility(8);
            this.mPanelCommonEmojisView.setVisibility(0);
        } else if (id == C4558R.C4560id.panelStickerType) {
            this.mode = 0;
            this.mPanelEmojiType.setSelected(false);
            this.mPanelGiphyType.setSelected(false);
            this.mPanelStickerType.setSelected(true);
            this.mGiphyPreview.setVisibility(8);
            this.mPanelEmoji.setVisibility(0);
            int firstItemPositionAtCategory = this.mStickerManager.getFirstItemPositionAtCategory(2);
            if (firstItemPositionAtCategory != -1) {
                this.mStickerPager.setCurrentItem(firstItemPositionAtCategory, true);
            }
            this.mPanelCommonEmojisView.setVisibility(8);
        } else if (id == C4558R.C4560id.panelGiphyType) {
            this.mode = 1;
            this.mPanelEmojiType.setSelected(false);
            this.mPanelGiphyType.setSelected(true);
            this.mPanelStickerType.setSelected(false);
            this.mGiphyPreview.setVisibility(0);
            this.mPanelEmoji.setVisibility(8);
            this.mPanelCommonEmojisView.setVisibility(8);
        }
        requestLayout();
        OnStickerSelectListener onStickerSelectListener = this.mOnGiphySelectListener;
        if (onStickerSelectListener != null) {
            onStickerSelectListener.onStickerSelect(view);
        }
    }

    public void onCommonEmojiClick(CommonEmoji commonEmoji) {
        onEmojiInput(commonEmoji);
    }

    public void onZoomEmojiClick(EmojiIndex emojiIndex) {
        onEmojiInput(emojiIndex);
    }
}

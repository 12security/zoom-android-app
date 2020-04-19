package com.zipow.videobox.photopicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zipow.videobox.util.ZMGlideUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.videomeetings.C4558R;

public class PhotoPagerFragment extends ZMDialogFragment {
    public static final long ANIM_DURATION = 200;
    public static final String ARG_ALL_PATH = "ARG_ALL_PATHS";
    public static final String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";
    public static final String ARG_HAS_ANIM = "HAS_ANIM";
    public static final String ARG_MAX_SELECTED_COUNT = "MAX_COUNT";
    public static final String ARG_SELECTED_PATH = "ARG_SELECTED_PATHS";
    public static final String ARG_SOURCE_CHECKED = "ARG_SOURCE_CHECKED";
    public static final float BOTTOM_BAR_ALPHA = 0.85f;
    public static final String TAG = "com.zipow.videobox.photopicker.PhotoPagerFragment";
    /* access modifiers changed from: private */
    @Nullable
    public ArrayList<String> mAllPaths = new ArrayList<>();
    /* access modifiers changed from: private */
    @NonNull
    public Map<String, Integer> mAllPathsCache = new HashMap();
    /* access modifiers changed from: private */
    public View mBottomBar;
    /* access modifiers changed from: private */
    public CheckBox mChk_Select;
    /* access modifiers changed from: private */
    public CheckBox mChk_Source;
    /* access modifiers changed from: private */
    public View mDividerLine;
    private boolean mHasAnim = false;
    /* access modifiers changed from: private */
    public PhotoHorizentalAdapter mHorizentalAdapter;
    /* access modifiers changed from: private */
    public RecyclerView mHorizentalRecyclerView;
    /* access modifiers changed from: private */
    public int mMaxSelectedCount = 0;
    private int mOrigPageCurrentItem = 0;
    @Nullable
    private PhotoPagerAdapter mPagerAdapter;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    /* access modifiers changed from: private */
    @NonNull
    public ArrayList<String> mSelectedPaths = new ArrayList<>();
    /* access modifiers changed from: private */
    @NonNull
    public Map<String, Integer> mSelectedPathsCache = new HashMap();
    private boolean mSourceChecked = false;
    private TextView mTextView_Send;
    private TextView mTextView_Title;
    /* access modifiers changed from: private */
    public ViewPager mViewPager;

    @NonNull
    public static PhotoPagerFragment newInstance(@NonNull List<String> list, int i, @NonNull List<String> list2, int i2, boolean z) {
        return newInstance(list, i, list2, i2, z, false);
    }

    @NonNull
    public static PhotoPagerFragment newInstance(List<String> list, int i, List<String> list2, int i2, boolean z, boolean z2) {
        PhotoPagerFragment photoPagerFragment = new PhotoPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(ARG_ALL_PATH, (String[]) list.toArray(new String[list.size()]));
        bundle.putInt(ARG_CURRENT_ITEM, i);
        bundle.putStringArray(ARG_SELECTED_PATH, (String[]) list2.toArray(new String[list2.size()]));
        bundle.putBoolean(ARG_HAS_ANIM, z2);
        bundle.putInt("MAX_COUNT", i2);
        bundle.putBoolean(ARG_SOURCE_CHECKED, z);
        photoPagerFragment.setArguments(bundle);
        return photoPagerFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] stringArray = arguments.getStringArray(ARG_ALL_PATH);
            if (stringArray != null) {
                this.mAllPaths.addAll(Arrays.asList(stringArray));
                for (int i = 0; i < stringArray.length; i++) {
                    this.mAllPathsCache.put(stringArray[i], Integer.valueOf(i));
                }
            }
            this.mHasAnim = arguments.getBoolean(ARG_HAS_ANIM);
            this.mOrigPageCurrentItem = arguments.getInt(ARG_CURRENT_ITEM);
            this.mMaxSelectedCount = arguments.getInt("MAX_COUNT");
            this.mSourceChecked = arguments.getBoolean(ARG_SOURCE_CHECKED);
            String[] stringArray2 = arguments.getStringArray(ARG_SELECTED_PATH);
            if (stringArray2 != null) {
                this.mSelectedPaths.addAll(Arrays.asList(stringArray2));
                for (int i2 = 0; i2 < stringArray2.length; i2++) {
                    this.mSelectedPathsCache.put(stringArray2[i2], Integer.valueOf(i2));
                }
            }
        }
        ArrayList<String> arrayList = this.mAllPaths;
        if (arrayList == null || arrayList.isEmpty()) {
            getActivity().finish();
        }
        this.mPagerAdapter = new PhotoPagerAdapter(ZMGlideUtil.getGlideRequestManager((Fragment) this), this.mAllPaths, new OnPagerItemClickListener() {
            public void onItemClick(View view) {
                int i = PhotoPagerFragment.this.mPanelTitleBar.getVisibility() == 0 ? 8 : 0;
                PhotoPagerFragment.this.mPanelTitleBar.setVisibility(i);
                PhotoPagerFragment.this.mDividerLine.setVisibility(i);
                PhotoPagerFragment.this.mBottomBar.setVisibility(i);
                PhotoPagerFragment.this.updateSelectStateInHorizentalRecyclerView();
            }
        });
        this.mHorizentalAdapter = new PhotoHorizentalAdapter(ZMGlideUtil.getGlideRequestManager((Fragment) this), this.mSelectedPaths, new OnPhotoPickerHoriItemCallback() {
            public boolean canSelected(String str, int i) {
                return PhotoPagerFragment.this.mAllPathsCache.containsKey(str);
            }

            public void onItemClick(View view, String str, int i) {
                if (PhotoPagerFragment.this.mAllPathsCache.containsKey(str)) {
                    PhotoPagerFragment.this.mViewPager.setCurrentItem(((Integer) PhotoPagerFragment.this.mAllPathsCache.get(str)).intValue());
                    PhotoPagerFragment.this.mChk_Select.setChecked(true);
                }
            }
        });
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_picker_fragment_image_pager, viewGroup, false);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mTextView_Send = (TextView) inflate.findViewById(C4558R.C4560id.btnSend);
        this.mTextView_Send.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (PhotoPagerFragment.this.getActivity() instanceof PhotoPickerActivity) {
                    ((PhotoPickerActivity) PhotoPagerFragment.this.getActivity()).completeSelect(PhotoPagerFragment.this.mChk_Source.isChecked(), PhotoPagerFragment.this.mSelectedPaths);
                }
            }
        });
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentActivity activity = PhotoPagerFragment.this.getActivity();
                if (activity != null) {
                    activity.onBackPressed();
                }
            }
        });
        this.mTextView_Title = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mTextView_Title.setText("");
        this.mViewPager = (ViewPager) inflate.findViewById(C4558R.C4560id.vp_photos);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setCurrentItem(this.mOrigPageCurrentItem);
        this.mViewPager.setOffscreenPageLimit(5);
        if (bundle == null && this.mHasAnim) {
            this.mViewPager.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    PhotoPagerFragment.this.mViewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                    PhotoPagerFragment.this.mViewPager.getLocationOnScreen(new int[2]);
                    PhotoPagerFragment.this.runEnterAnimation();
                    return true;
                }
            });
        }
        this.mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                String str = (String) PhotoPagerFragment.this.mAllPaths.get(i);
                if (PhotoPagerFragment.this.mSelectedPathsCache.containsKey(str)) {
                    int intValue = ((Integer) PhotoPagerFragment.this.mSelectedPathsCache.get(str)).intValue();
                    PhotoPagerFragment.this.mHorizentalRecyclerView.scrollToPosition(intValue);
                    PhotoPagerFragment.this.mHorizentalAdapter.setCurrentItem(intValue);
                } else {
                    PhotoPagerFragment.this.mHorizentalAdapter.setCurrentItem(-1);
                }
                PhotoPagerFragment.this.updateSelectState(false);
            }
        });
        this.mHorizentalRecyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.photoHorizentalRecycler);
        this.mBottomBar = inflate.findViewById(C4558R.C4560id.bottomBar);
        this.mDividerLine = inflate.findViewById(C4558R.C4560id.line);
        this.mChk_Select = (CheckBox) inflate.findViewById(C4558R.C4560id.chkSelect);
        this.mChk_Source = (CheckBox) inflate.findViewById(C4558R.C4560id.rbSource);
        this.mChk_Source.setChecked(this.mSourceChecked);
        this.mChk_Select.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String str = (String) PhotoPagerFragment.this.mAllPaths.get(PhotoPagerFragment.this.mViewPager.getCurrentItem());
                if (PhotoPagerFragment.this.mChk_Select.isChecked()) {
                    if (PhotoPagerFragment.this.mMaxSelectedCount <= 1) {
                        if (!PhotoPagerFragment.this.mSelectedPaths.contains(str)) {
                            PhotoPagerFragment.this.mSelectedPaths.clear();
                            PhotoPagerFragment.this.mSelectedPaths.add(str);
                            PhotoPagerFragment.this.mHorizentalAdapter.setCurrentItem(0);
                            PhotoPagerFragment.this.mSelectedPathsCache.clear();
                            PhotoPagerFragment.this.mSelectedPathsCache.put(str, Integer.valueOf(0));
                        }
                    } else if (PhotoPagerFragment.this.mSelectedPaths.size() < PhotoPagerFragment.this.mMaxSelectedCount) {
                        PhotoPagerFragment.this.mSelectedPaths.add(str);
                        PhotoPagerFragment.this.mHorizentalAdapter.setCurrentItem(PhotoPagerFragment.this.mSelectedPaths.size() - 1);
                        PhotoPagerFragment.this.mHorizentalRecyclerView.scrollToPosition(PhotoPagerFragment.this.mSelectedPaths.size() - 1);
                        PhotoPagerFragment.this.mSelectedPathsCache.put(str, Integer.valueOf(PhotoPagerFragment.this.mSelectedPaths.size() - 1));
                    } else {
                        PhotoPagerFragment.this.mChk_Select.setChecked(false);
                    }
                } else if (PhotoPagerFragment.this.mSelectedPathsCache.containsKey(str)) {
                    int intValue = ((Integer) PhotoPagerFragment.this.mSelectedPathsCache.get(str)).intValue();
                    PhotoPagerFragment.this.mSelectedPaths.remove(str);
                    if (!PhotoPagerFragment.this.mSelectedPaths.isEmpty()) {
                        int min = Math.min(intValue, PhotoPagerFragment.this.mSelectedPaths.size() - 1);
                        PhotoPagerFragment.this.mHorizentalAdapter.setCurrentItem(min);
                        PhotoPagerFragment.this.mHorizentalRecyclerView.scrollToPosition(min);
                    }
                    PhotoPagerFragment.this.mSelectedPathsCache.clear();
                    for (int i = 0; i < PhotoPagerFragment.this.mSelectedPaths.size(); i++) {
                        PhotoPagerFragment.this.mSelectedPathsCache.put(PhotoPagerFragment.this.mSelectedPaths.get(i), Integer.valueOf(i));
                    }
                }
                PhotoPagerFragment.this.updateSelectStateInHorizentalRecyclerView();
                PhotoPagerFragment.this.updateSelectState(true);
            }
        });
        this.mBottomBar.setAlpha(0.85f);
        this.mHorizentalRecyclerView.setAlpha(0.85f);
        initBottomRecycleView();
        updateSelectState(true);
        return inflate;
    }

    private void initBottomRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(0);
        this.mHorizentalRecyclerView.setLayoutManager(linearLayoutManager);
        this.mHorizentalRecyclerView.setAdapter(this.mHorizentalAdapter);
        updateSelectStateInHorizentalRecyclerView();
    }

    public void updateSelectStateInHorizentalRecyclerView() {
        if (!CollectionsUtil.isListEmpty(this.mAllPaths)) {
            int i = (this.mSelectedPaths.isEmpty() || this.mBottomBar.getVisibility() != 0) ? 8 : 0;
            this.mHorizentalRecyclerView.setVisibility(i);
            this.mDividerLine.setVisibility(i);
            Integer num = (Integer) this.mSelectedPathsCache.get(this.mAllPaths.get(this.mViewPager.getCurrentItem()));
            if (num != null) {
                this.mHorizentalAdapter.setCurrentItem(num.intValue());
            } else {
                this.mHorizentalAdapter.setCurrentItem(-1);
            }
        }
    }

    /* access modifiers changed from: private */
    public void runEnterAnimation() {
        ViewHelper.setPivotX(this.mViewPager, 0.0f);
        ViewHelper.setPivotY(this.mViewPager, 0.0f);
        ViewHelper.setScaleX(this.mViewPager, 0.5f);
        ViewHelper.setScaleY(this.mViewPager, 0.5f);
        ViewHelper.setTranslationX(this.mViewPager, (float) (getResources().getDisplayMetrics().widthPixels / 4));
        ViewHelper.setTranslationY(this.mViewPager, (float) (getResources().getDisplayMetrics().heightPixels / 4));
        ViewPropertyAnimator.animate(this.mViewPager).setDuration(200).scaleX(1.0f).scaleY(1.0f).translationX(0.0f).translationY(0.0f).setInterpolator(new DecelerateInterpolator());
        ObjectAnimator ofInt = ObjectAnimator.ofInt((Object) this.mViewPager.getBackground(), "alpha", 0, 255);
        ofInt.setDuration(200);
        ofInt.start();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) this, "saturation", 0.0f, 1.0f);
        ofFloat.setDuration(200);
        ofFloat.start();
    }

    public void runExitAnimation(@NonNull final Runnable runnable) {
        if (!getArguments().getBoolean(ARG_HAS_ANIM, false) || !this.mHasAnim) {
            runnable.run();
            return;
        }
        ViewPropertyAnimator.animate(this.mViewPager).setDuration(200).setInterpolator(new AccelerateInterpolator()).scaleX(0.5f).scaleY(0.5f).translationX((float) (getResources().getDisplayMetrics().widthPixels / 4)).translationY((float) (getResources().getDisplayMetrics().heightPixels / 4)).setListener(new AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                PhotoPagerFragment.this.getNonNullEventTaskManagerOrThrowException().pushLater(new EventAction("runExitAnimation") {
                    public void run(IUIElement iUIElement) {
                        runnable.run();
                    }
                });
            }
        });
        ObjectAnimator ofInt = ObjectAnimator.ofInt((Object) this.mViewPager.getBackground(), "alpha", 0);
        ofInt.setDuration(200);
        ofInt.start();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) this, "saturation", 1.0f, 0.0f);
        ofFloat.setDuration(200);
        ofFloat.start();
    }

    public void updateSelectState(boolean z) {
        boolean z2 = false;
        if (z) {
            int size = this.mSelectedPaths.size();
            TextView textView = this.mTextView_Send;
            if (textView != null) {
                textView.setEnabled(size > 0);
                this.mTextView_Send.setText(getString(C4558R.string.zm_picker_done_with_count, Integer.valueOf(size)));
            }
        }
        if (this.mChk_Select != null) {
            boolean z3 = !CollectionsUtil.isCollectionEmpty(this.mAllPaths) && this.mSelectedPathsCache.containsKey(this.mAllPaths.get(this.mViewPager.getCurrentItem()));
            this.mChk_Select.setChecked(z3);
            if (!z3) {
                CheckBox checkBox = this.mChk_Select;
                int size2 = this.mSelectedPaths.size();
                int i = this.mMaxSelectedCount;
                if (size2 < i || i <= 1) {
                    z2 = true;
                }
                checkBox.setEnabled(z2);
                return;
            }
            this.mChk_Select.setEnabled(true);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mAllPaths.clear();
        this.mAllPaths = null;
        ViewPager viewPager = this.mViewPager;
        if (viewPager != null) {
            viewPager.setAdapter(null);
        }
    }

    @NonNull
    public List<String> getSelectedPhotos() {
        return this.mSelectedPaths;
    }

    public boolean isSourceChecked() {
        return this.mChk_Source.isChecked();
    }
}

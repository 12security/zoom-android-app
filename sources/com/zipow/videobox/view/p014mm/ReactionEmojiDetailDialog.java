package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import java.util.List;
import p021us.zoom.androidlib.material.ZMViewPagerBottomSheetDialog;
import p021us.zoom.androidlib.material.ZMViewPagerBottomSheetDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMViewPager;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.ReactionEmojiDetailDialog */
public class ReactionEmojiDetailDialog extends ZMViewPagerBottomSheetDialogFragment implements BaseOnTabSelectedListener {
    private static final String TAG = "ReactionEmojiDetailDialog";
    private ReactionEmojiDetailViewPagerAdapter adapter;
    private Context context;
    private String emoji;
    private Boolean enableScroll;
    private MMMessageItem mMessageItem;
    private int maxHeight = 0;
    private int offscreenPageLimit = 5;
    private int peekHeight = 0;
    private TabLayout tabLayout;
    private ZMViewPager viewPager;

    /* renamed from: com.zipow.videobox.view.mm.ReactionEmojiDetailDialog$Builder */
    public static class Builder {
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public String emoji;
        /* access modifiers changed from: private */
        public Boolean enableScroll;
        /* access modifiers changed from: private */
        public int maxHeight = 0;
        /* access modifiers changed from: private */
        public MMMessageItem messageItem;
        /* access modifiers changed from: private */
        public int offscreenPageLimit;
        /* access modifiers changed from: private */
        public int peekHeight = 0;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setPeekHeight(int i) {
            this.peekHeight = i;
            return this;
        }

        public Builder setMaxHeight(int i) {
            this.maxHeight = i;
            return this;
        }

        public Builder setOffscreenPageLimit(int i) {
            this.offscreenPageLimit = i;
            return this;
        }

        public Builder setEnableScroll(Boolean bool) {
            this.enableScroll = bool;
            return this;
        }

        public Builder setData(MMMessageItem mMMessageItem) {
            this.messageItem = mMMessageItem;
            return this;
        }

        public Builder setEmoji(String str) {
            this.emoji = str;
            return this;
        }

        public ReactionEmojiDetailDialog build() {
            return ReactionEmojiDetailDialog.newInstance(this);
        }

        public ReactionEmojiDetailDialog show(FragmentManager fragmentManager) {
            ReactionEmojiDetailDialog build = build();
            build.show(fragmentManager);
            return build;
        }
    }

    public void onTabReselected(Tab tab) {
    }

    public void onTabUnselected(Tab tab) {
    }

    private void setContext(Context context2) {
        this.context = context2;
    }

    private void setHeight(int i, int i2) {
        this.peekHeight = i;
        this.maxHeight = i2;
    }

    private void setOffscreenPageLimit(int i) {
        if (i != 0) {
            this.offscreenPageLimit = i;
        }
    }

    private void setEnableScroll(Boolean bool) {
        this.enableScroll = bool;
    }

    private void setData(MMMessageItem mMMessageItem) {
        this.mMessageItem = mMMessageItem;
    }

    public void setEmoji(String str) {
        this.emoji = str;
    }

    /* access modifiers changed from: private */
    public static ReactionEmojiDetailDialog newInstance(Builder builder) {
        ReactionEmojiDetailDialog reactionEmojiDetailDialog = new ReactionEmojiDetailDialog();
        reactionEmojiDetailDialog.setHeight(builder.peekHeight, builder.maxHeight);
        reactionEmojiDetailDialog.setData(builder.messageItem);
        reactionEmojiDetailDialog.setEmoji(builder.emoji);
        reactionEmojiDetailDialog.setContext(builder.context);
        reactionEmojiDetailDialog.setOffscreenPageLimit(builder.offscreenPageLimit);
        reactionEmojiDetailDialog.setEnableScroll(builder.enableScroll);
        return reactionEmojiDetailDialog;
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        float f;
        ZMViewPagerBottomSheetDialog zMViewPagerBottomSheetDialog = new ZMViewPagerBottomSheetDialog(this.context, C4558R.style.SheetDialog);
        float f2 = 0.6f;
        if (getMaxCount() >= 5) {
            f = 0.7f;
        } else if (getMaxCount() >= 3) {
            f = 0.6f;
            f2 = 0.45f;
        } else {
            f2 = 0.33f;
            f = 0.5f;
        }
        if (getContext() == null) {
            return zMViewPagerBottomSheetDialog;
        }
        int displayHeight = (int) (((float) UIUtil.getDisplayHeight(getContext())) * f2);
        int displayHeight2 = (int) (((float) UIUtil.getDisplayHeight(getContext())) * f);
        int i = this.peekHeight;
        if (i != 0) {
            displayHeight = i;
        }
        zMViewPagerBottomSheetDialog.setPeekHeight(displayHeight);
        int i2 = this.maxHeight;
        if (i2 != 0) {
            displayHeight2 = i2;
        }
        zMViewPagerBottomSheetDialog.setMaxHeight(displayHeight2);
        return zMViewPagerBottomSheetDialog;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_reaction_emoji_detail_dialog, viewGroup, false);
        this.tabLayout = (TabLayout) inflate.findViewById(C4558R.C4560id.tablayout);
        this.viewPager = (ZMViewPager) inflate.findViewById(C4558R.C4560id.viewpager);
        this.adapter = new ReactionEmojiDetailViewPagerAdapter(this.context, getChildFragmentManager());
        this.adapter.setData(this.mMessageItem);
        this.viewPager.setAdapter(this.adapter);
        this.viewPager.setOffscreenPageLimit(this.offscreenPageLimit);
        this.viewPager.setCurrentItem(this.adapter.getIndex(this.emoji));
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.tabLayout.addOnTabSelectedListener(this);
        setupViewPager(this.viewPager);
        for (int i = 0; i < this.adapter.getCount(); i++) {
            Tab tabAt = this.tabLayout.getTabAt(i);
            if (tabAt != null) {
                String pageContentDescription = this.adapter.getPageContentDescription(i);
                if (!StringUtil.isEmptyOrNull(pageContentDescription)) {
                    tabAt.setContentDescription((CharSequence) pageContentDescription);
                }
            }
        }
        return inflate;
    }

    public void onDestroyView() {
        this.tabLayout.removeOnTabSelectedListener(this);
        super.onDestroyView();
    }

    public void show(FragmentManager fragmentManager) {
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        Fragment findFragmentByTag = fragmentManager.findFragmentByTag(TAG);
        if (findFragmentByTag != null) {
            beginTransaction.remove(findFragmentByTag);
        }
        beginTransaction.addToBackStack(null);
        show(beginTransaction, TAG);
    }

    private long getMaxCount() {
        MMMessageItem mMMessageItem = this.mMessageItem;
        long j = 0;
        if (mMMessageItem == null) {
            return 0;
        }
        List<MMCommentsEmojiCountItem> emojiCountItems = mMMessageItem.getEmojiCountItems();
        if (emojiCountItems == null || emojiCountItems.size() == 0) {
            return 0;
        }
        for (MMCommentsEmojiCountItem mMCommentsEmojiCountItem : emojiCountItems) {
            if (mMCommentsEmojiCountItem.getCount() > j) {
                j = mMCommentsEmojiCountItem.getCount();
            }
        }
        return j;
    }

    public static Builder builder(Context context2) {
        return new Builder(context2);
    }

    public void onTabSelected(Tab tab) {
        ZMViewPager zMViewPager = this.viewPager;
        if (zMViewPager != null) {
            zMViewPager.setCurrentItem(tab.getPosition(), false);
            View view = getView();
            if (view != null) {
                view.requestLayout();
            }
        }
    }
}

package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.ReactionEmojiDetailViewPagerAdapter */
public class ReactionEmojiDetailViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private MMMessageItem mMessageItem;
    private List<MMCommentsEmojiCountItem> sortedList = new ArrayList();

    /* renamed from: com.zipow.videobox.view.mm.ReactionEmojiDetailViewPagerAdapter$CommentsEmojiCountComparator */
    static class CommentsEmojiCountComparator implements Comparator<MMCommentsEmojiCountItem> {
        CommentsEmojiCountComparator() {
        }

        public int compare(MMCommentsEmojiCountItem mMCommentsEmojiCountItem, MMCommentsEmojiCountItem mMCommentsEmojiCountItem2) {
            int i = ((mMCommentsEmojiCountItem.getFirstEmojiTime() - mMCommentsEmojiCountItem2.getFirstEmojiTime()) > 0 ? 1 : ((mMCommentsEmojiCountItem.getFirstEmojiTime() - mMCommentsEmojiCountItem2.getFirstEmojiTime()) == 0 ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            return i < 0 ? -1 : 0;
        }
    }

    public ReactionEmojiDetailViewPagerAdapter(Context context, @NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mContext = context;
    }

    public ReactionEmojiDetailViewPagerAdapter(Context context, @NonNull FragmentManager fragmentManager, MMMessageItem mMMessageItem) {
        super(fragmentManager);
        this.mContext = context;
        this.mMessageItem = mMMessageItem;
        sort();
    }

    private void sort() {
        MMMessageItem mMMessageItem = this.mMessageItem;
        if (mMMessageItem != null && mMMessageItem.getEmojiCountItems() != null) {
            this.sortedList.clear();
            for (MMCommentsEmojiCountItem mMCommentsEmojiCountItem : this.mMessageItem.getEmojiCountItems()) {
                if (mMCommentsEmojiCountItem.getCount() != 0) {
                    this.sortedList.add(mMCommentsEmojiCountItem);
                }
            }
            Collections.sort(this.sortedList, new CommentsEmojiCountComparator());
        }
    }

    public Fragment getItem(int i) {
        if (this.mMessageItem == null) {
            return null;
        }
        MMCommentsEmojiCountItem mMCommentsEmojiCountItem = (MMCommentsEmojiCountItem) this.sortedList.get(i);
        if (mMCommentsEmojiCountItem == null) {
            return null;
        }
        return ReactionEmojiDetailListFragment.newInstance(this.mMessageItem.sessionId, this.mMessageItem.messageXMPPId, mMCommentsEmojiCountItem.getEmoji());
    }

    public int getCount() {
        return this.sortedList.size();
    }

    @Nullable
    public String getPageContentDescription(int i) {
        Resources resources = this.mContext.getResources();
        if (resources == null) {
            return null;
        }
        CommonEmojiHelper.getInstance();
        if (((MMCommentsEmojiCountItem) this.sortedList.get(i)).getEmoji() == null) {
            return null;
        }
        String shortnameToUnicode = CommonEmojiHelper.shortnameToUnicode(((MMCommentsEmojiCountItem) this.sortedList.get(i)).getEmoji());
        int count = (int) ((MMCommentsEmojiCountItem) this.sortedList.get(i)).getCount();
        return resources.getQuantityString(C4558R.plurals.zm_accessibility_reacion_label_88133, count, new Object[]{shortnameToUnicode, Integer.valueOf(count)}).toString();
    }

    public CharSequence getPageTitle(int i) {
        String emoji = ((MMCommentsEmojiCountItem) this.sortedList.get(i)).getEmoji();
        StringBuilder sb = new StringBuilder();
        sb.append(emoji);
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(((MMCommentsEmojiCountItem) this.sortedList.get(i)).getCount());
        CharSequence formatImgEmojiSize = CommonEmojiHelper.getInstance().formatImgEmojiSize((float) new TextAppearanceSpan(this.mContext, C4558R.style.UIKitTextView_ReactionLabel_Text).getTextSize(), sb.toString(), false);
        if (TextUtils.isEmpty(formatImgEmojiSize)) {
            formatImgEmojiSize = "";
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(formatImgEmojiSize);
        Matcher matcher = Pattern.compile(emoji.toString()).matcher(formatImgEmojiSize);
        while (matcher.find()) {
            spannableStringBuilder.setSpan(new TextAppearanceSpan(this.mContext, C4558R.style.UIKitTextView_ReactionLabel_Text), matcher.start(), matcher.end(), 33);
        }
        return spannableStringBuilder;
    }

    public void setData(MMMessageItem mMMessageItem) {
        this.mMessageItem = mMMessageItem;
        sort();
        notifyDataSetChanged();
    }

    public int getIndex(String str) {
        int i = 0;
        if (StringUtil.isEmptyOrNull(str)) {
            return 0;
        }
        List<MMCommentsEmojiCountItem> list = this.sortedList;
        if (list == null) {
            return 0;
        }
        Iterator it = list.iterator();
        while (it.hasNext() && !StringUtil.isSameString(str, ((MMCommentsEmojiCountItem) it.next()).getEmoji())) {
            i++;
        }
        return i;
    }
}

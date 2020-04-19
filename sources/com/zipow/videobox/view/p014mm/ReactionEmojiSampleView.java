package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.p014mm.sticker.CommonEmoji;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.ReactionEmojiSampleView */
public class ReactionEmojiSampleView extends FrameLayout implements OnClickListener {
    private static final String TAG = "ReactionEmojiSampleView";
    private ImageView mBtnMore;
    private int[] mEmojiArray;
    private MMMessageItem mMessage;
    private OnReactionEmojiSampleListener mReactionEmojiListener;
    private int mWindowOffset;

    /* renamed from: com.zipow.videobox.view.mm.ReactionEmojiSampleView$OnReactionEmojiSampleListener */
    public interface OnReactionEmojiSampleListener {
        void onMoreEmojiClick(MMMessageItem mMMessageItem);

        void onReactionEmojiClick(View view, int i, CharSequence charSequence, Object obj);
    }

    public ReactionEmojiSampleView(@NonNull Context context) {
        super(context);
        this.mEmojiArray = new int[]{C4558R.C4560id.emoji1, C4558R.C4560id.emoji2, C4558R.C4560id.emoji3, C4558R.C4560id.emoji4, C4558R.C4560id.emoji5, C4558R.C4560id.emoji6};
        init();
    }

    public ReactionEmojiSampleView(@NonNull Context context, Object obj) {
        this(context);
        if (obj instanceof MMMessageItem) {
            this.mMessage = (MMMessageItem) obj;
        }
    }

    public ReactionEmojiSampleView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEmojiArray = new int[]{C4558R.C4560id.emoji1, C4558R.C4560id.emoji2, C4558R.C4560id.emoji3, C4558R.C4560id.emoji4, C4558R.C4560id.emoji5, C4558R.C4560id.emoji6};
        init();
    }

    public ReactionEmojiSampleView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mEmojiArray = new int[]{C4558R.C4560id.emoji1, C4558R.C4560id.emoji2, C4558R.C4560id.emoji3, C4558R.C4560id.emoji4, C4558R.C4560id.emoji5, C4558R.C4560id.emoji6};
        init();
    }

    public ReactionEmojiSampleView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mEmojiArray = new int[]{C4558R.C4560id.emoji1, C4558R.C4560id.emoji2, C4558R.C4560id.emoji3, C4558R.C4560id.emoji4, C4558R.C4560id.emoji5, C4558R.C4560id.emoji6};
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        View.inflate(getContext(), C4558R.layout.zm_mm_reaction_emoji_sample_view, this);
        this.mBtnMore = (ImageView) findViewById(C4558R.C4560id.btn_more);
        this.mBtnMore.setOnClickListener(this);
        initEmojiSample();
    }

    private void initEmojiSample() {
        if (CommonEmojiHelper.getInstance().isEmojiInstalled()) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            List frequentUsedEmojiKeys = CommonEmojiHelper.getInstance().getFrequentUsedEmojiKeys();
            if (frequentUsedEmojiKeys != null) {
                linkedHashSet.addAll(frequentUsedEmojiKeys);
            }
            linkedHashSet.add("1f44d");
            linkedHashSet.add("2764");
            linkedHashSet.add("1f389");
            linkedHashSet.add("1f602");
            linkedHashSet.add("1f44f");
            linkedHashSet.add("1f60e");
            int i = 0;
            Iterator it = linkedHashSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (i < this.mEmojiArray.length) {
                    if (!StringUtil.isEmptyOrNull(str)) {
                        CommonEmoji commonEmojiByKey = CommonEmojiHelper.getInstance().getCommonEmojiByKey(str);
                        if (commonEmojiByKey != null) {
                            updateReactionEmoji(this.mEmojiArray[i], commonEmojiByKey);
                            i++;
                        }
                    }
                } else {
                    return;
                }
            }
            return;
        }
        updateReactionEmoji(C4558R.C4560id.emoji1, "1f44d", "👍");
        updateReactionEmoji(C4558R.C4560id.emoji2, "2764", "❤️");
        updateReactionEmoji(C4558R.C4560id.emoji3, "1f389", "🎉");
        updateReactionEmoji(C4558R.C4560id.emoji4, "1f602", "😂");
        updateReactionEmoji(C4558R.C4560id.emoji5, "1f44f", "👏");
        updateReactionEmoji(C4558R.C4560id.emoji6, "1f60e", "😎");
    }

    private void updateReactionEmoji(int i, CommonEmoji commonEmoji) {
        TextView textView = (TextView) findViewById(i);
        textView.setOnClickListener(this);
        if (getResources() != null) {
            textView.setContentDescription(getResources().getString(C4558R.string.zm_accessibility_add_sample_reaction_88133, new Object[]{commonEmoji.getShortName()}));
        }
        setReactionEmoji(textView, commonEmoji);
    }

    private void updateReactionEmoji(int i, String str, String str2) {
        CommonEmoji commonEmoji = new CommonEmoji();
        commonEmoji.setKey(str);
        commonEmoji.setOutput(str2);
        updateReactionEmoji(i, commonEmoji);
    }

    private void setReactionEmoji(@NonNull TextView textView, @NonNull CommonEmoji commonEmoji) {
        CharSequence formatImgEmojiSize = CommonEmojiHelper.getInstance().formatImgEmojiSize(textView.getTextSize(), commonEmoji.getOutput(), false);
        if (TextUtils.isEmpty(formatImgEmojiSize)) {
            formatImgEmojiSize = "";
        }
        textView.setText(new SpannableStringBuilder(formatImgEmojiSize));
        textView.setTag(commonEmoji);
    }

    public void bindData(@Nullable Object obj) {
        if (obj != null && (obj instanceof MMMessageItem)) {
            this.mMessage = (MMMessageItem) obj;
            updateEmojis();
        }
    }

    private void updateEmojis() {
        boolean z;
        MMMessageItem mMMessageItem = this.mMessage;
        if (mMMessageItem != null) {
            List emojiCountItems = mMMessageItem.getEmojiCountItems();
            if (emojiCountItems != null && emojiCountItems.size() != 0) {
                int i = 0;
                while (true) {
                    int[] iArr = this.mEmojiArray;
                    if (i < iArr.length) {
                        TextView textView = (TextView) findViewById(iArr[i]);
                        if (textView != null) {
                            CharSequence text = textView.getText();
                            if (!TextUtils.isEmpty(text)) {
                                Iterator it = emojiCountItems.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        z = false;
                                        break;
                                    }
                                    MMCommentsEmojiCountItem mMCommentsEmojiCountItem = (MMCommentsEmojiCountItem) it.next();
                                    if (StringUtil.isSameString(mMCommentsEmojiCountItem.getEmoji(), text.toString())) {
                                        z = mMCommentsEmojiCountItem.isContainMe();
                                        break;
                                    }
                                }
                                textView.setSelected(z);
                            }
                        }
                        i++;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public void setWindowOffset(int i) {
        this.mWindowOffset = i;
    }

    public void onClick(View view) {
        if (view == this.mBtnMore) {
            OnReactionEmojiSampleListener onReactionEmojiSampleListener = this.mReactionEmojiListener;
            if (onReactionEmojiSampleListener != null) {
                onReactionEmojiSampleListener.onMoreEmojiClick(this.mMessage);
            }
            return;
        }
        TextView textView = (TextView) view;
        Object tag = textView.getTag();
        if ((tag instanceof CommonEmoji) && CommonEmojiHelper.getInstance().isEmojiInstalled()) {
            CommonEmojiHelper.getInstance().addFrequentUsedEmoji(((CommonEmoji) tag).getKey());
        }
        CharSequence text = textView.getText();
        if (!TextUtils.isEmpty(text)) {
            OnReactionEmojiSampleListener onReactionEmojiSampleListener2 = this.mReactionEmojiListener;
            if (onReactionEmojiSampleListener2 != null) {
                onReactionEmojiSampleListener2.onReactionEmojiClick(view, this.mWindowOffset, text, this.mMessage);
            }
        }
    }

    public void setOnReactionEmojiSampleListener(OnReactionEmojiSampleListener onReactionEmojiSampleListener) {
        this.mReactionEmojiListener = onReactionEmojiSampleListener;
    }
}

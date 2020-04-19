package com.zipow.videobox.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import androidx.core.content.ContextCompat;
import com.google.android.material.chip.Chip;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickReactionLabelListener;
import com.zipow.videobox.view.p014mm.MMCommentsEmojiCountItem;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p021us.zoom.videomeetings.C4558R;

public class ReactionLabelView extends Chip implements OnClickListener, OnCheckedChangeListener, OnLongClickListener {
    public static final int ACTION_ADD_REACTION = 1;
    public static final int ACTION_ADD_REPLY = 2;
    public static final int ACTION_MORE = 3;
    public static final int ACTION_REACTION = 0;
    private static final String TAG = "ReactionLabelView";
    private static Map<CharSequence, Long> mSentReactions = new HashMap();
    private int mAction;
    private MMCommentsEmojiCountItem mEmojiItem;
    private Handler mHandler;
    private boolean mIsChecked;
    private OnClickReactionLabelListener mListener;
    private MMMessageItem mMessageItem;
    private OnDeleteListener mOnDeleteListener;
    private long mOriginCount;
    private boolean mPraised;
    private Runnable mRunnableNotifyDataSetChanged = new Runnable() {
        public void run() {
            ReactionLabelView.this.setEnabled(true);
        }
    };

    public interface OnDeleteListener {
        void onDeleted(View view);
    }

    public ReactionLabelView(Context context) {
        super(context);
        init();
    }

    public ReactionLabelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ReactionLabelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setOnClickListener(this);
        setOnCheckedChangeListener(this);
        setOnLongClickListener(this);
    }

    public void setReactionEnable(boolean z) {
        if (z) {
            setOnClickListener(this);
        } else {
            setOnClickListener(null);
        }
    }

    public void setData(MMMessageItem mMMessageItem, MMCommentsEmojiCountItem mMCommentsEmojiCountItem, int i, OnClickReactionLabelListener onClickReactionLabelListener) {
        this.mListener = onClickReactionLabelListener;
        if (mMMessageItem != null) {
            this.mMessageItem = mMMessageItem;
            this.mEmojiItem = mMCommentsEmojiCountItem;
            this.mAction = i;
            if (mMCommentsEmojiCountItem != null) {
                this.mPraised = mMCommentsEmojiCountItem.isContainMe();
            }
            MMCommentsEmojiCountItem mMCommentsEmojiCountItem2 = this.mEmojiItem;
            if (mMCommentsEmojiCountItem2 != null) {
                this.mOriginCount = mMCommentsEmojiCountItem2.getCount();
            }
            this.mListener = onClickReactionLabelListener;
            updateViews(this.mPraised);
        }
    }

    public void updateViews(boolean z) {
        if (this.mEmojiItem != null && getContext() != null) {
            long count = this.mEmojiItem.getCount();
            StringBuilder sb = new StringBuilder();
            sb.append(this.mEmojiItem.getEmoji());
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(count);
            String sb2 = sb.toString();
            CommonEmojiHelper instance = CommonEmojiHelper.getInstance();
            CharSequence formatImgEmojiSize = instance.formatImgEmojiSize(getTextSize(), sb2, false);
            if (TextUtils.isEmpty(formatImgEmojiSize)) {
                formatImgEmojiSize = "";
            }
            CharSequence formatImgEmojiSize2 = instance.formatImgEmojiSize(getTextSize(), this.mEmojiItem.getEmoji(), false);
            if (TextUtils.isEmpty(formatImgEmojiSize2)) {
                formatImgEmojiSize2 = "";
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(formatImgEmojiSize);
            Matcher matcher = Pattern.compile(formatImgEmojiSize2.toString()).matcher(formatImgEmojiSize);
            while (matcher.find()) {
                spannableStringBuilder.setSpan(new TextAppearanceSpan(getContext(), C4558R.style.UIKitTextView_ReactionLabel), matcher.start(), matcher.end(), 33);
            }
            setText(spannableStringBuilder);
            setChecked(z);
            setChipBackgroundColorResource(z ? C4558R.color.zm_ui_kit_color_gray_E7F1FD : C4558R.color.zm_white);
            setChipStrokeColorResource(z ? C4558R.color.zm_ui_kit_color_gray_E7F1FD : C4558R.color.zm_transparent);
            setTextColor(ContextCompat.getColor(getContext(), z ? C4558R.color.zm_ui_kit_color_blue_0862D1 : C4558R.color.zm_ui_kit_color_gray_747487));
            Resources resources = getResources();
            if (resources != null) {
                setContentDescription(resources.getQuantityString(C4558R.plurals.zm_accessibility_reacion_label_88133, (int) count, new Object[]{CommonEmojiHelper.shortnameToUnicode(this.mEmojiItem.getEmoji()), Long.valueOf(count)}));
            }
        }
    }

    public boolean isAddReactionLabel() {
        return this.mAction == 1;
    }

    public boolean isAddReplyLabel() {
        return this.mAction == 2;
    }

    public boolean isMoreActionLabel() {
        return this.mAction == 3;
    }

    public void onClick(View view) {
        long j;
        MMCommentsEmojiCountItem mMCommentsEmojiCountItem = this.mEmojiItem;
        if (mMCommentsEmojiCountItem != null) {
            Long l = (Long) mSentReactions.get(mMCommentsEmojiCountItem.getEmoji());
            if (l == null || System.currentTimeMillis() - l.longValue() >= 1000) {
                mSentReactions.put(this.mEmojiItem.getEmoji(), Long.valueOf(System.currentTimeMillis()));
            } else {
                return;
            }
        }
        if (!this.mIsChecked || !this.mMessageItem.isReachReactionLimit()) {
            if (this.mListener != null) {
                if (isAddReactionLabel()) {
                    this.mListener.onClickAddReactionLabel(view, this.mMessageItem);
                    return;
                } else if (isAddReplyLabel()) {
                    this.mListener.onClickAddReplyLabel(view, this.mMessageItem);
                    return;
                } else if (isMoreActionLabel()) {
                    this.mListener.onClickMoreActionLabel(view, this.mMessageItem);
                    return;
                }
            }
            if (this.mEmojiItem != null) {
                setEnabled(false);
                long count = this.mEmojiItem.getCount();
                if (this.mIsChecked) {
                    j = count + 1;
                    long j2 = this.mPraised ? this.mOriginCount : this.mOriginCount + 1;
                    if (j > j2) {
                        j = j2;
                    }
                } else {
                    j = count - 1;
                    long j3 = this.mPraised ? this.mOriginCount - 1 : this.mOriginCount;
                    if (j < j3) {
                        j = j3;
                    }
                }
                this.mEmojiItem.setCount(j);
                this.mEmojiItem.setContainMe(this.mIsChecked);
                updateViews(this.mIsChecked);
                OnClickReactionLabelListener onClickReactionLabelListener = this.mListener;
                if (onClickReactionLabelListener != null) {
                    onClickReactionLabelListener.onShowFloatingText(view, 0, this.mIsChecked);
                    this.mListener.onClickReactionLabel(this, this.mMessageItem, this.mEmojiItem, this.mIsChecked);
                }
                if (this.mHandler == null) {
                    this.mHandler = new Handler();
                }
                this.mHandler.removeCallbacks(this.mRunnableNotifyDataSetChanged);
                this.mHandler.postDelayed(this.mRunnableNotifyDataSetChanged, 1000);
                if (j <= 0) {
                    setVisibility(8);
                    OnDeleteListener onDeleteListener = this.mOnDeleteListener;
                    if (onDeleteListener != null) {
                        onDeleteListener.onDeleted(this);
                    }
                }
                return;
            }
            return;
        }
        setChecked(false);
        OnClickReactionLabelListener onClickReactionLabelListener2 = this.mListener;
        if (onClickReactionLabelListener2 != null) {
            onClickReactionLabelListener2.onReachReactionLimit();
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.mIsChecked = z;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mRunnableNotifyDataSetChanged);
        }
        super.onDetachedFromWindow();
    }

    public boolean onLongClick(View view) {
        if (this.mListener == null) {
            return false;
        }
        if (isAddReactionLabel()) {
            return this.mListener.onLongClickAddReactionLabel(view, this.mMessageItem);
        }
        return this.mListener.onLongClickReactionLabel(view, this.mMessageItem, this.mEmojiItem);
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.mOnDeleteListener = onDeleteListener;
    }
}

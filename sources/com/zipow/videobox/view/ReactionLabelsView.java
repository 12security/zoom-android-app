package com.zipow.videobox.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.OnHierarchyChangeListener;
import androidx.annotation.NonNull;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.chip.ChipGroup.LayoutParams;
import com.zipow.videobox.ptapp.IMProtos.EmojiCountMap;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ThreadDataProvider;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.ReactionLabelView.OnDeleteListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickReactionLabelListener;
import com.zipow.videobox.view.p014mm.MMCommentsEmojiCountItem;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ReactionLabelsView extends ChipGroup implements OnClickListener, OnHierarchyChangeListener, OnDeleteListener {
    private static final String TAG = "ReactionLabelsView";
    private List<MMCommentsEmojiCountItem> mLabels = new ArrayList();
    private OnClickReactionLabelListener mListener;
    private MMMessageItem mMessageItem;

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

    private void init() {
    }

    public void onChildViewAdded(View view, View view2) {
    }

    public ReactionLabelsView(Context context) {
        super(context);
        init();
    }

    public ReactionLabelsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ReactionLabelsView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void setLabels(MMMessageItem mMMessageItem, OnClickReactionLabelListener onClickReactionLabelListener) {
        if (mMMessageItem != null && !ZMIMUtils.isAnnouncement(mMMessageItem.sessionId)) {
            this.mListener = onClickReactionLabelListener;
            this.mMessageItem = mMMessageItem;
            this.mLabels.clear();
            if (!mMMessageItem.isNotExitThread && !mMMessageItem.isDeletedThread) {
                if (mMMessageItem.getEmojiCountItems() == null || mMMessageItem.getEmojiCountItems().size() == 0) {
                    getMessageEmojiCountInfo(mMMessageItem);
                }
                if (mMMessageItem.getEmojiCountItems() != null) {
                    for (MMCommentsEmojiCountItem mMCommentsEmojiCountItem : mMMessageItem.getEmojiCountItems()) {
                        if (!(mMCommentsEmojiCountItem == null || mMCommentsEmojiCountItem.getCount() == 0)) {
                            this.mLabels.add(mMCommentsEmojiCountItem);
                        }
                    }
                }
                Collections.sort(this.mLabels, new CommentsEmojiCountComparator());
            }
            createLabelViews(mMMessageItem);
        }
    }

    private void createLabelViews(@NonNull MMMessageItem mMMessageItem) {
        removeAllViews();
        boolean isReactionEnable = ZMIMUtils.isReactionEnable();
        boolean isReplyEnable = ZMIMUtils.isReplyEnable();
        if (mMMessageItem.showReaction) {
            MMMessageItem mMMessageItem2 = this.mMessageItem;
            if (mMMessageItem2 != null && ((mMMessageItem2.hasCommentsOdds == 1 || this.mMessageItem.commentsCount != 0 || this.mMessageItem.unreadCommentCount != 0 || !CollectionsUtil.isCollectionEmpty(this.mMessageItem.getComments()) || !TextUtils.isEmpty(this.mMessageItem.draftReply)) && !this.mMessageItem.isComment)) {
                addMoreReplyView(mMMessageItem);
            } else if (isReplyEnable) {
                addReplyLabelView(mMMessageItem);
            }
            if (isReactionEnable && mMMessageItem.serverSideTime != 0) {
                addDefaultLabelView(mMMessageItem);
            }
            if (isReactionEnable || isReplyEnable) {
                addMoreActionLabelView(mMMessageItem);
            }
            addReactionLabels(mMMessageItem, isReactionEnable);
        } else {
            addMoreReplyView(mMMessageItem);
            addReactionLabels(mMMessageItem, isReactionEnable);
            if (isReactionEnable && this.mLabels.size() > 0 && mMMessageItem.serverSideTime != 0) {
                addDefaultLabelView(mMMessageItem);
            }
        }
        setOnHierarchyChangeListener(this);
    }

    private void addReplyLabelView(@NonNull MMMessageItem mMMessageItem) {
        if (!mMMessageItem.isComment) {
            Context context = getContext();
            if (context != null) {
                View inflate = View.inflate(context, C4558R.layout.zm_im_reaction_label_view, null);
                ReactionLabelView reactionLabelView = (ReactionLabelView) inflate.findViewById(C4558R.C4560id.label_view);
                reactionLabelView.setChipIcon(getResources().getDrawable(C4558R.C4559drawable.zm_ic_add_reply));
                reactionLabelView.setChipIconSize((float) UIUtil.dip2px(getContext(), 24.0f));
                reactionLabelView.setChipIconVisible(true);
                reactionLabelView.setContentDescription(context.getString(C4558R.string.zm_accessibility_add_reply_129964));
                reactionLabelView.setMinimumWidth(UIUtil.dip2px(getContext(), 32.0f));
                int dip2px = (UIUtil.dip2px(getContext(), 32.0f) - ((int) reactionLabelView.getChipIconSize())) >> 1;
                if (dip2px > 0) {
                    reactionLabelView.setChipStartPadding((float) dip2px);
                }
                reactionLabelView.setData(mMMessageItem, null, 2, this.mListener);
                reactionLabelView.setOnLongClickListener(null);
                addView(inflate, new LayoutParams(UIUtil.dip2px(getContext(), 32.0f), -2));
            }
        }
    }

    private void addMoreActionLabelView(@NonNull MMMessageItem mMMessageItem) {
        if (!mMMessageItem.isComment) {
            Context context = getContext();
            if (context != null) {
                View inflate = View.inflate(context, C4558R.layout.zm_im_reaction_label_view, null);
                ReactionLabelView reactionLabelView = (ReactionLabelView) inflate.findViewById(C4558R.C4560id.label_view);
                reactionLabelView.setChipIcon(getResources().getDrawable(C4558R.C4559drawable.zm_ic_reaction_more_action));
                reactionLabelView.setChipIconSize((float) UIUtil.dip2px(getContext(), 24.0f));
                reactionLabelView.setChipIconVisible(true);
                reactionLabelView.setContentDescription(context.getString(C4558R.string.zm_accessibility_more_action_129964));
                reactionLabelView.setMinimumWidth(UIUtil.dip2px(getContext(), 32.0f));
                int dip2px = (UIUtil.dip2px(getContext(), 32.0f) - ((int) reactionLabelView.getChipIconSize())) >> 1;
                if (dip2px > 0) {
                    reactionLabelView.setChipStartPadding((float) dip2px);
                }
                reactionLabelView.setData(mMMessageItem, null, 3, this.mListener);
                reactionLabelView.setOnLongClickListener(null);
                addView(inflate, new LayoutParams(UIUtil.dip2px(getContext(), 32.0f), -2));
            }
        }
    }

    private void addMoreReplyView(@NonNull MMMessageItem mMMessageItem) {
        MoreReplyView moreReplyView = (MoreReplyView) View.inflate(getContext(), C4558R.layout.zm_im_more_reply_view, null).findViewById(C4558R.C4560id.more_reply_view);
        moreReplyView.setData(mMMessageItem);
        moreReplyView.setOnClickListener(this);
        addView(moreReplyView, new LayoutParams(-2, -2));
    }

    private void addReactionLabels(@NonNull MMMessageItem mMMessageItem, boolean z) {
        for (MMCommentsEmojiCountItem mMCommentsEmojiCountItem : this.mLabels) {
            if (!(mMCommentsEmojiCountItem == null || mMCommentsEmojiCountItem.getCount() == 0)) {
                View inflate = View.inflate(getContext(), C4558R.layout.zm_im_reaction_label_view, null);
                ReactionLabelView reactionLabelView = (ReactionLabelView) inflate.findViewById(C4558R.C4560id.label_view);
                reactionLabelView.setData(mMMessageItem, mMCommentsEmojiCountItem, 0, this.mListener);
                reactionLabelView.setReactionEnable(z);
                reactionLabelView.setOnDeleteListener(this);
                addView(inflate, new LayoutParams(-2, -2));
            }
        }
    }

    private void addDefaultLabelView(@NonNull MMMessageItem mMMessageItem) {
        if (!mMMessageItem.isComment) {
            Context context = getContext();
            if (context != null) {
                View inflate = View.inflate(context, C4558R.layout.zm_im_reaction_label_view, null);
                ReactionLabelView reactionLabelView = (ReactionLabelView) inflate.findViewById(C4558R.C4560id.label_view);
                reactionLabelView.setChipIcon(getResources().getDrawable(C4558R.C4559drawable.zm_ic_add_reaction));
                reactionLabelView.setChipIconSize((float) UIUtil.dip2px(getContext(), 24.0f));
                reactionLabelView.setChipIconVisible(true);
                reactionLabelView.setContentDescription(context.getString(C4558R.string.zm_accessibility_add_reaction_88133));
                reactionLabelView.setMinimumWidth(UIUtil.dip2px(getContext(), 32.0f));
                int dip2px = (UIUtil.dip2px(getContext(), 32.0f) - ((int) reactionLabelView.getChipIconSize())) >> 1;
                if (dip2px > 0) {
                    reactionLabelView.setChipStartPadding((float) dip2px);
                }
                reactionLabelView.setData(mMMessageItem, null, 1, this.mListener);
                reactionLabelView.setOnLongClickListener(null);
                addView(inflate, new LayoutParams(UIUtil.dip2px(getContext(), 32.0f), -2));
            }
        }
    }

    private void getMessageEmojiCountInfo(MMMessageItem mMMessageItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
            if (threadDataProvider != null) {
                if (threadDataProvider.isMessageEmojiCountInfoDirty(mMMessageItem.sessionId, mMMessageItem.messageXMPPId)) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(mMMessageItem.messageXMPPId);
                    threadDataProvider.syncMessageEmojiCountInfo(mMMessageItem.sessionId, arrayList);
                    return;
                }
                EmojiCountMap messageEmojiCountInfo = threadDataProvider.getMessageEmojiCountInfo(true, mMMessageItem.sessionId, mMMessageItem.messageXMPPId);
                if (messageEmojiCountInfo != null) {
                    mMMessageItem.updateCommentsEmojiCountItems(messageEmojiCountInfo);
                }
            }
        }
    }

    public void onClick(View view) {
        if (view != null && view.getId() == C4558R.C4560id.more_reply_view) {
            OnClickReactionLabelListener onClickReactionLabelListener = this.mListener;
            if (onClickReactionLabelListener != null) {
                onClickReactionLabelListener.onMoreReply(view, this.mMessageItem);
            }
        }
    }

    public void onChildViewRemoved(View view, View view2) {
        MMMessageItem mMMessageItem = this.mMessageItem;
        if (mMMessageItem != null && mMMessageItem.getEmojiCountItems() != null) {
            ArrayList arrayList = new ArrayList();
            for (MMCommentsEmojiCountItem mMCommentsEmojiCountItem : this.mMessageItem.getEmojiCountItems()) {
                if (!(mMCommentsEmojiCountItem == null || mMCommentsEmojiCountItem.getCount() == 0)) {
                    arrayList.add(mMCommentsEmojiCountItem);
                }
            }
            if (arrayList.size() == 0) {
                for (int childCount = getChildCount() - 1; childCount > 0; childCount--) {
                    View childAt = getChildAt(childCount);
                    if (childAt instanceof ReactionLabelView) {
                        ReactionLabelView reactionLabelView = (ReactionLabelView) childAt;
                        if (!this.mMessageItem.showReaction || (!reactionLabelView.isAddReactionLabel() && !reactionLabelView.isAddReplyLabel() && !reactionLabelView.isMoreActionLabel())) {
                            removeView(childAt);
                        }
                    }
                }
            }
        }
    }

    public void onDeleted(View view) {
        if (view instanceof ReactionLabelView) {
            removeView(view);
        }
    }
}

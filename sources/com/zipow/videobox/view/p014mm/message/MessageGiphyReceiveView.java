package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.GiphyMsgInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.GifException;
import com.zipow.videobox.util.ImageLoader;
import com.zipow.videobox.util.ZMGlideRequestListener;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ReactionLabelsView;
import com.zipow.videobox.view.ZMGifView;
import com.zipow.videobox.view.ZMGifView.OnResizeListener;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnLongClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import java.io.File;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageGiphyReceiveView */
public class MessageGiphyReceiveView extends AbsMessageView {
    private LinearLayout contactLinear;
    private TextView contactName;
    private TextView groupContact;
    private LinearLayout groupLinear;
    private TextView groupName;
    protected TextView mAvatarName;
    protected AvatarView mAvatarView;
    protected ZMGifView mGifView;
    protected View mGiphyContentLinear;
    protected TextView mGiphyMsgName;
    protected ImageView mImgStarred;
    protected MMMessageItem mMessageItem;
    protected View mProgress;
    protected ReactionLabelsView mReactionLabels;
    protected OnResizeListener mResizeListener = new OnResizeListener() {
        public void onResize(int i, int i2) {
            if (MessageGiphyReceiveView.this.mGifView != null && MessageGiphyReceiveView.this.mGifView.getLayoutParams() != null) {
                int maxWidth = MessageGiphyReceiveView.this.mGifView.getMaxWidth();
                int maxHeight = MessageGiphyReceiveView.this.mGifView.getMaxHeight();
                int paddingLeft = MessageGiphyReceiveView.this.mGifView.getPaddingLeft();
                int paddingTop = MessageGiphyReceiveView.this.mGifView.getPaddingTop();
                int paddingRight = MessageGiphyReceiveView.this.mGifView.getPaddingRight();
                int paddingBottom = MessageGiphyReceiveView.this.mGifView.getPaddingBottom();
                float f = (float) i;
                float f2 = ((float) ((maxWidth - paddingLeft) - paddingRight)) / (f * 1.0f);
                float f3 = (float) i2;
                float f4 = ((float) ((maxHeight - paddingTop) - paddingBottom)) / (1.0f * f3);
                if (f2 > f4) {
                    f2 = f4;
                }
                MessageGiphyReceiveView.this.mGifView.getLayoutParams().width = (int) ((f * f2) + ((float) paddingLeft) + ((float) paddingRight));
                MessageGiphyReceiveView.this.mGifView.getLayoutParams().height = (int) ((f3 * f2) + ((float) paddingBottom) + ((float) paddingTop));
            }
        }
    };
    protected View placeHolder;
    private LinearLayout starredMsgTitleLinear;
    private TextView time;
    private TextView txtStarDes;
    @NonNull
    protected ZMGlideRequestListener zmGlideRequestListener = new ZMGlideRequestListener() {
        public void onSuccess(String str) {
            MessageGiphyReceiveView.this.showGifView();
        }

        public void onError(String str, GifException gifException) {
            MessageGiphyReceiveView.this.showErrorView();
        }
    };

    public MessageGiphyReceiveView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MessageGiphyReceiveView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageGiphyReceiveView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: private */
    public void showGifView() {
        if (this.mGifView != null) {
            View view = this.mProgress;
            if (!(view == null || this.placeHolder == null)) {
                view.setVisibility(8);
                this.mGifView.setVisibility(0);
                this.placeHolder.setVisibility(8);
            }
        }
    }

    private void showProgressView() {
        if (this.mGifView != null) {
            View view = this.mProgress;
            if (!(view == null || this.placeHolder == null)) {
                view.setVisibility(0);
                this.mGifView.setVisibility(8);
                this.placeHolder.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: private */
    public void showErrorView() {
        if (this.mGifView != null) {
            View view = this.mProgress;
            if (!(view == null || this.placeHolder == null)) {
                view.setVisibility(8);
                this.mGifView.setVisibility(8);
                this.placeHolder.setVisibility(0);
            }
        }
    }

    private void initView() {
        inflateLayout();
        this.starredMsgTitleLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_title_linear);
        this.contactLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_linear);
        this.contactName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_name);
        this.groupLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_linear);
        this.groupContact = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_contact);
        this.groupName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_name);
        this.time = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_time);
        this.txtStarDes = (TextView) findViewById(C4558R.C4560id.txtStarDes);
        this.mImgStarred = (ImageView) findViewById(C4558R.C4560id.zm_mm_starred);
        this.mReactionLabels = (ReactionLabelsView) findViewById(C4558R.C4560id.reaction_labels_view);
        this.mProgress = findViewById(C4558R.C4560id.giphy_panel_progress);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.giphy_avatar);
        this.mAvatarName = (TextView) findViewById(C4558R.C4560id.giphy_avatar_name);
        this.placeHolder = findViewById(C4558R.C4560id.giphy_panel_place_holder);
        this.mGifView = (ZMGifView) findViewById(C4558R.C4560id.giphy_gifView);
        this.mGifView.setRadius(UIUtil.dip2px(getContext(), 10.0f));
        this.mGifView.setmScale(1.2f);
        this.mGiphyMsgName = (TextView) findViewById(C4558R.C4560id.giphy_message_name);
        this.mGiphyContentLinear = findViewById(C4558R.C4560id.giphy_content_linear);
        this.mGifView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OnClickMessageListener onClickMessageListener = MessageGiphyReceiveView.this.getOnClickMessageListener();
                if (onClickMessageListener != null) {
                    onClickMessageListener.onClickMessage(MessageGiphyReceiveView.this.mMessageItem);
                }
            }
        });
        this.mGifView.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                OnShowContextMenuListener onShowContextMenuListener = MessageGiphyReceiveView.this.getOnShowContextMenuListener();
                if (onShowContextMenuListener != null) {
                    onShowContextMenuListener.onShowContextMenu(view, MessageGiphyReceiveView.this.mMessageItem);
                }
                return true;
            }
        });
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickAvatarListener onClickAvatarListener = MessageGiphyReceiveView.this.getOnClickAvatarListener();
                    if (onClickAvatarListener != null) {
                        onClickAvatarListener.onClickAvatar(MessageGiphyReceiveView.this.mMessageItem);
                    }
                }
            });
            this.mAvatarView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnLongClickAvatarListener onLongClickAvatarListener = MessageGiphyReceiveView.this.getOnLongClickAvatarListener();
                    if (onLongClickAvatarListener != null) {
                        return onLongClickAvatarListener.onLongClickAvatar(MessageGiphyReceiveView.this.mMessageItem);
                    }
                    return false;
                }
            });
        }
        this.placeHolder.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MessageGiphyReceiveView messageGiphyReceiveView = MessageGiphyReceiveView.this;
                messageGiphyReceiveView.setMessageItem(messageGiphyReceiveView.mMessageItem);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_giphy_receive, this);
    }

    public void changeAvatar(boolean z) {
        if (z) {
            LayoutParams layoutParams = (LayoutParams) this.mAvatarView.getLayoutParams();
            layoutParams.width = UIUtil.dip2px(getContext(), 24.0f);
            layoutParams.height = UIUtil.dip2px(getContext(), 24.0f);
            layoutParams.leftMargin = UIUtil.dip2px(getContext(), 16.0f);
            this.mAvatarView.setLayoutParams(layoutParams);
            return;
        }
        LayoutParams layoutParams2 = (LayoutParams) this.mAvatarView.getLayoutParams();
        layoutParams2.width = UIUtil.dip2px(getContext(), 40.0f);
        layoutParams2.height = UIUtil.dip2px(getContext(), 40.0f);
        this.mAvatarView.setLayoutParams(layoutParams2);
    }

    public void setScreenName(@Nullable String str) {
        if (str != null) {
            TextView textView = this.mAvatarName;
            if (textView != null) {
                textView.setText(str);
            }
        }
    }

    public void setMessageName(String str) {
        TextView textView = this.mGiphyMsgName;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void setStarredMessage(@NonNull MMMessageItem mMMessageItem) {
        if (mMMessageItem.hideStarView) {
            this.starredMsgTitleLinear.setVisibility(0);
            this.mAvatarName.setVisibility(8);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
                    if (sessionById != null) {
                        if (mMMessageItem.isGroupMessage) {
                            this.contactLinear.setVisibility(8);
                            this.groupLinear.setVisibility(0);
                            ZoomGroup sessionGroup = sessionById.getSessionGroup();
                            if (sessionGroup != null) {
                                this.groupName.setText(sessionGroup.getGroupName());
                            }
                        } else {
                            this.contactLinear.setVisibility(0);
                            this.groupLinear.setVisibility(8);
                            ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                            if (sessionBuddy != null) {
                                this.groupName.setText(BuddyNameUtil.getMyDisplayName(sessionBuddy));
                            } else if (TextUtils.equals(mMMessageItem.sessionId, myself.getJid())) {
                                this.groupName.setText(BuddyNameUtil.getMyDisplayName(myself));
                            }
                        }
                        this.time.setText(TimeUtil.formatDateTimeCap(getContext(), mMMessageItem.serverSideTime));
                        String string = StringUtil.isSameString(myself.getJid(), mMMessageItem.fromJid) ? getContext().getString(C4558R.string.zm_lbl_content_you) : mMMessageItem.fromScreenName;
                        this.groupContact.setText(string);
                        this.contactName.setText(string);
                        if (mMMessageItem.isComment) {
                            this.txtStarDes.setText(C4558R.string.zm_lbl_from_thread_88133);
                            this.txtStarDes.setVisibility(0);
                        } else if (mMMessageItem.commentsCount > 0) {
                            this.txtStarDes.setText(getResources().getQuantityString(C4558R.plurals.zm_lbl_comment_reply_title_88133, (int) mMMessageItem.commentsCount, new Object[]{Integer.valueOf((int) mMMessageItem.commentsCount)}));
                            this.txtStarDes.setVisibility(0);
                        } else {
                            this.txtStarDes.setVisibility(8);
                        }
                    }
                }
            }
        } else {
            this.starredMsgTitleLinear.setVisibility(8);
            this.txtStarDes.setVisibility(8);
        }
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        this.mProgress.setVisibility(0);
        this.mGifView.setVisibility(8);
        this.placeHolder.setVisibility(8);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (mMMessageItem.hideStarView || !mMMessageItem.isMessgeStarred) {
            this.mImgStarred.setVisibility(8);
        } else {
            this.mImgStarred.setVisibility(0);
        }
        this.mMessageItem = mMMessageItem;
        setMessageName(String.valueOf(mMMessageItem.message));
        setReactionLabels(mMMessageItem);
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setVisibility(0);
        }
        int dip2px = UIUtil.dip2px(getContext(), 10.0f);
        if (mMMessageItem.onlyMessageShow) {
            this.mAvatarView.setVisibility(4);
            TextView textView = this.mAvatarName;
            if (textView != null) {
                textView.setVisibility(8);
            }
            View view = this.mGiphyContentLinear;
            view.setPadding(view.getPaddingLeft(), 0, this.mGiphyContentLinear.getPaddingRight(), this.mGiphyContentLinear.getPaddingBottom());
            this.mGifView.setRadius(dip2px);
        } else {
            this.mAvatarView.setVisibility(0);
            if (this.mAvatarName == null || !mMMessageItem.isIncomingMessage() || !mMMessageItem.isGroupMessage) {
                TextView textView2 = this.mAvatarName;
                if (textView2 != null) {
                    textView2.setVisibility(8);
                }
            } else {
                setScreenName(mMMessageItem.fromScreenName);
                TextView textView3 = this.mAvatarName;
                if (textView3 != null) {
                    textView3.setVisibility(0);
                }
            }
            View view2 = this.mGiphyContentLinear;
            view2.setPadding(view2.getPaddingLeft(), this.mGiphyContentLinear.getPaddingTop(), this.mGiphyContentLinear.getPaddingRight(), this.mGiphyContentLinear.getPaddingBottom());
            this.mGifView.setRadius(new int[]{0, dip2px, dip2px, dip2px});
        }
        if (!isInEditMode()) {
            String str = mMMessageItem.fromJid;
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself == null || str == null || !str.equals(myself.getJid())) {
                    myself = zoomMessenger.getBuddyWithJID(str);
                }
                if (mMMessageItem.fromContact == null && myself != null) {
                    mMMessageItem.fromContact = IMAddrBookItem.fromZoomBuddy(myself);
                }
                if (mMMessageItem.fromContact != null) {
                    AvatarView avatarView2 = this.mAvatarView;
                    if (avatarView2 != null) {
                        avatarView2.show(mMMessageItem.fromContact.getAvatarParamsBuilder());
                    }
                }
                GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(mMMessageItem.giphyID);
                if (giphyInfo != null) {
                    int dataNetworkType = NetworkUtil.getDataNetworkType(getContext());
                    if (dataNetworkType == 1 || dataNetworkType == 4 || dataNetworkType == 3) {
                        ImageLoader.getInstance().displayGif(this.mGifView, giphyInfo.getPcUrl(), this.zmGlideRequestListener, this.mResizeListener);
                    } else {
                        File cacheFile = ImageLoader.getInstance().getCacheFile(giphyInfo.getPcUrl());
                        if (cacheFile == null || !cacheFile.exists()) {
                            ImageLoader.getInstance().displayGif(this.mGifView, giphyInfo.getMobileUrl(), this.zmGlideRequestListener, this.mResizeListener);
                        } else {
                            ImageLoader.getInstance().displayGif(this.mGifView, giphyInfo.getPcUrl(), this.zmGlideRequestListener, this.mResizeListener);
                        }
                    }
                }
            }
        }
        setStarredMessage(mMMessageItem);
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
        setMessageItem(mMMessageItem);
        if (z) {
            this.mAvatarView.setVisibility(4);
            this.mReactionLabels.setVisibility(8);
            if (this.mAvatarName.getVisibility() == 0) {
                this.mAvatarName.setVisibility(4);
            }
        }
    }

    public Rect getMessageLocationOnScreen() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        ReactionLabelsView reactionLabelsView = this.mReactionLabels;
        return new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), (iArr[1] + getHeight()) - ((reactionLabelsView == null || reactionLabelsView.getVisibility() == 8) ? 0 : this.mReactionLabels.getHeight() + (UIUtil.dip2px(getContext(), 4.0f) * 2)));
    }

    public void setReactionLabels(MMMessageItem mMMessageItem) {
        if (!(mMMessageItem == null || this.mReactionLabels == null)) {
            if (mMMessageItem.hideStarView) {
                this.mReactionLabels.setVisibility(8);
                return;
            }
            this.mReactionLabels.setLabels(mMMessageItem, getOnClickReactionLabelListener());
        }
    }

    public MMMessageItem getMessageItem() {
        return this.mMessageItem;
    }
}

package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.IMProtos.GiphyMsgInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ImageLoader;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickGiphyBtnListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickStatusImageListener;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import java.io.File;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageGiphySendView */
public class MessageGiphySendView extends MessageGiphyReceiveView {
    private TextView mCancelBtn;
    private ImageView mImgStatus;
    private TextView mSendBtn;
    private TextView mShuffleBtn;

    public MessageGiphySendView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MessageGiphySendView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageGiphySendView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        this.mSendBtn = (TextView) findViewById(C4558R.C4560id.giphy_send_btn);
        this.mShuffleBtn = (TextView) findViewById(C4558R.C4560id.giphy_shuffle_btn);
        this.mCancelBtn = (TextView) findViewById(C4558R.C4560id.giphy_cancel_btn);
        this.mImgStatus = (ImageView) findViewById(C4558R.C4560id.imgStatus);
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickStatusImageListener onClickStatusImageListener = MessageGiphySendView.this.getOnClickStatusImageListener();
                    if (onClickStatusImageListener != null) {
                        onClickStatusImageListener.onClickStatusImage(MessageGiphySendView.this.mMessageItem);
                    }
                }
            });
        }
        TextView textView = this.mSendBtn;
        if (textView != null) {
            textView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickGiphyBtnListener onClickGiphyBtnListener = MessageGiphySendView.this.getmOnClickGiphyBtnListener();
                    if (onClickGiphyBtnListener != null) {
                        onClickGiphyBtnListener.onClickGiphyBtn(MessageGiphySendView.this.mMessageItem, view);
                    }
                }
            });
        }
        TextView textView2 = this.mShuffleBtn;
        if (textView2 != null) {
            textView2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickGiphyBtnListener onClickGiphyBtnListener = MessageGiphySendView.this.getmOnClickGiphyBtnListener();
                    if (onClickGiphyBtnListener != null) {
                        onClickGiphyBtnListener.onClickGiphyBtn(MessageGiphySendView.this.mMessageItem, view);
                    }
                }
            });
        }
        TextView textView3 = this.mCancelBtn;
        if (textView3 != null) {
            textView3.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickGiphyBtnListener onClickGiphyBtnListener = MessageGiphySendView.this.getmOnClickGiphyBtnListener();
                    if (onClickGiphyBtnListener != null) {
                        onClickGiphyBtnListener.onClickGiphyBtn(MessageGiphySendView.this.mMessageItem, view);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_giphy_send, this);
    }

    public void setStatusImage(boolean z, int i) {
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setVisibility(z ? 0 : 8);
            this.mImgStatus.setImageResource(i);
        }
    }

    public void setFailed(boolean z) {
        setStatusImage(z, C4558R.C4559drawable.zm_mm_msg_state_fail);
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        this.mMessageItem = mMMessageItem;
        setReactionLabels(mMMessageItem);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (mMMessageItem.hideStarView || !mMMessageItem.isMessgeStarred) {
            this.mImgStarred.setVisibility(8);
        } else {
            this.mImgStarred.setVisibility(0);
        }
        setFailed(mMMessageItem.messageState == 4 || mMMessageItem.messageState == 5 || mMMessageItem.messageState == 6);
        this.mGifView.setVisibility(8);
        this.mProgress.setVisibility(0);
        if (this.mAvatarView != null) {
            this.mAvatarView.setVisibility(0);
        }
        int dip2px = UIUtil.dip2px(getContext(), 10.0f);
        if (mMMessageItem.onlyMessageShow) {
            this.mAvatarView.setVisibility(4);
            if (this.mAvatarName != null) {
                this.mAvatarName.setVisibility(8);
            }
            this.mGiphyContentLinear.setPadding(this.mGiphyContentLinear.getPaddingLeft(), 0, this.mGiphyContentLinear.getPaddingRight(), this.mGiphyContentLinear.getPaddingBottom());
            this.mGifView.setRadius(dip2px);
        } else {
            this.mAvatarView.setVisibility(0);
            if (this.mAvatarName != null && mMMessageItem.isIncomingMessage()) {
                setScreenName(mMMessageItem.fromScreenName);
                if (this.mAvatarName != null) {
                    this.mAvatarName.setVisibility(0);
                }
            } else if (this.mAvatarName != null && mMMessageItem.isSendingMessage() && getContext() != null) {
                setScreenName(getContext().getString(C4558R.string.zm_lbl_content_you));
                this.mAvatarName.setVisibility(0);
            } else if (this.mAvatarName != null) {
                this.mAvatarName.setVisibility(8);
            }
            this.mGiphyContentLinear.setPadding(this.mGiphyContentLinear.getPaddingLeft(), this.mGiphyContentLinear.getPaddingTop(), this.mGiphyContentLinear.getPaddingRight(), this.mGiphyContentLinear.getPaddingBottom());
            this.mGifView.setRadius(new int[]{dip2px, 0, dip2px, dip2px});
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
                if (!(mMMessageItem.fromContact == null || this.mAvatarView == null)) {
                    this.mAvatarView.show(mMMessageItem.fromContact.getAvatarParamsBuilder());
                }
                GiphyMsgInfo giphyInfo = zoomMessenger.getGiphyInfo(mMMessageItem.giphyID);
                if (giphyInfo != null) {
                    int dataNetworkType = NetworkUtil.getDataNetworkType(getContext());
                    if (dataNetworkType == 1 || dataNetworkType == 4 || dataNetworkType == 3) {
                        ImageLoader.getInstance().displayGif(this.mGifView, giphyInfo.getPcUrl(), this.zmGlideRequestListener, this.mResizeListener);
                        return;
                    }
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
}

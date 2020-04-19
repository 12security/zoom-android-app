package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ImageLoader;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.MaskedDrawable;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ReactionLabelsView;
import com.zipow.videobox.view.ZMGifView;
import com.zipow.videobox.view.ZMGifView.OnResizeListener;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickStatusImageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnLongClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import java.io.File;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessagePicView */
public class MessagePicView extends AbsMessageView {
    public static String TAG = "MessagePicView";
    private int PREVIEW_PIC_SIZE_MAX;
    protected AvatarView mAvatarView;
    protected TextView mFileUnavailableTextView;
    private int mImgPaddingBottom = 0;
    private int mImgPaddingLeft = 0;
    private int mImgPaddingRight = 0;
    private int mImgPaddingTop = 0;
    protected ZMGifView mImgPic;
    protected ImageView mImgStarred;
    protected ImageView mImgStatus;
    protected MMMessageItem mMessageItem;
    protected LinearLayout mPanelProgress;
    protected ProgressBar mProgressBar;
    protected ProgressBar mProgressBarDownload;
    protected ReactionLabelsView mReactionLabels;
    private OnResizeListener mResizeListener = new OnResizeListener() {
        public void onResize(int i, int i2) {
            if (MessagePicView.this.mImgPic != null && MessagePicView.this.mImgPic.getLayoutParams() != null) {
                int maxWidth = MessagePicView.this.mImgPic.getMaxWidth();
                int maxHeight = MessagePicView.this.mImgPic.getMaxHeight();
                int paddingLeft = MessagePicView.this.mImgPic.getPaddingLeft();
                int paddingTop = MessagePicView.this.mImgPic.getPaddingTop();
                int paddingRight = MessagePicView.this.mImgPic.getPaddingRight();
                int paddingBottom = MessagePicView.this.mImgPic.getPaddingBottom();
                float f = (float) i;
                float f2 = ((float) ((maxWidth - paddingLeft) - paddingRight)) / (f * 1.0f);
                float f3 = (float) i2;
                float f4 = ((float) ((maxHeight - paddingTop) - paddingBottom)) / (f3 * 1.0f);
                if (f2 > f4) {
                    f2 = f4;
                }
                if (f2 > 1.0f) {
                    f2 = 1.0f;
                }
                MessagePicView.this.mImgPic.getLayoutParams().width = (int) ((f * f2) + ((float) paddingLeft) + ((float) paddingRight));
                MessagePicView.this.mImgPic.getLayoutParams().height = (int) ((f3 * f2) + ((float) paddingBottom) + ((float) paddingTop));
            }
        }
    };
    protected TextView mTxtRatio;
    protected TextView mTxtScreenName;

    private int getScale(int i, int i2, int i3, int i4) {
        int i5 = i2;
        int i6 = i;
        int i7 = 1;
        while (i7 < i4) {
            i6 <<= 1;
            if (i6 > i3) {
                break;
            }
            i5 <<= 1;
            if (i5 > i3) {
                break;
            }
            i7 <<= 1;
        }
        return i7;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public int[] getImgRadius() {
        return null;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Drawable getMesageBackgroudDrawable() {
        return null;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Drawable getProgressBackgroudDrawable() {
        return null;
    }

    public MessagePicView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MessagePicView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessagePicView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void initView() {
        this.PREVIEW_PIC_SIZE_MAX = UIUtil.dip2px(getContext(), 200.0f);
        inflateLayout();
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mImgStatus = (ImageView) findViewById(C4558R.C4560id.imgStatus);
        this.mImgPic = (ZMGifView) findViewById(C4558R.C4560id.imgPic);
        this.mProgressBar = (ProgressBar) findViewById(C4558R.C4560id.progressBar1);
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mPanelProgress = (LinearLayout) findViewById(C4558R.C4560id.panelProgress);
        this.mProgressBarDownload = (ProgressBar) findViewById(C4558R.C4560id.progressBarDownload);
        this.mTxtRatio = (TextView) findViewById(C4558R.C4560id.txtRatio);
        this.mImgStarred = (ImageView) findViewById(C4558R.C4560id.zm_mm_starred);
        this.mFileUnavailableTextView = (TextView) findViewById(C4558R.C4560id.file_unavailable_text_view);
        this.mReactionLabels = (ReactionLabelsView) findViewById(C4558R.C4560id.reaction_labels_view);
        this.mImgPaddingLeft = this.mImgPic.getPaddingLeft();
        this.mImgPaddingRight = this.mImgPic.getPaddingRight();
        this.mImgPaddingTop = this.mImgPic.getPaddingTop();
        this.mImgPaddingBottom = this.mImgPic.getPaddingBottom();
        setStatusImage(false, 0);
        ZMGifView zMGifView = this.mImgPic;
        if (zMGifView != null) {
            zMGifView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnShowContextMenuListener onShowContextMenuListener = MessagePicView.this.getOnShowContextMenuListener();
                    if (onShowContextMenuListener != null) {
                        return onShowContextMenuListener.onShowContextMenu(view, MessagePicView.this.mMessageItem);
                    }
                    return false;
                }
            });
            this.mImgPic.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickMessageListener onClickMessageListener = MessagePicView.this.getOnClickMessageListener();
                    if (onClickMessageListener != null) {
                        onClickMessageListener.onClickMessage(MessagePicView.this.mMessageItem);
                    }
                }
            });
        }
        LinearLayout linearLayout = this.mPanelProgress;
        if (linearLayout != null) {
            linearLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickMessageListener onClickMessageListener = MessagePicView.this.getOnClickMessageListener();
                    if (onClickMessageListener != null) {
                        onClickMessageListener.onClickMessage(MessagePicView.this.mMessageItem);
                    }
                }
            });
        }
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickStatusImageListener onClickStatusImageListener = MessagePicView.this.getOnClickStatusImageListener();
                    if (onClickStatusImageListener != null) {
                        onClickStatusImageListener.onClickStatusImage(MessagePicView.this.mMessageItem);
                    }
                }
            });
        }
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickAvatarListener onClickAvatarListener = MessagePicView.this.getOnClickAvatarListener();
                    if (onClickAvatarListener != null) {
                        onClickAvatarListener.onClickAvatar(MessagePicView.this.mMessageItem);
                    }
                }
            });
            this.mAvatarView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnLongClickAvatarListener onLongClickAvatarListener = MessagePicView.this.getOnLongClickAvatarListener();
                    if (onLongClickAvatarListener != null) {
                        return onLongClickAvatarListener.onLongClickAvatar(MessagePicView.this.mMessageItem);
                    }
                    return false;
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_pic_receive, this);
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

    public void setRatio(int i) {
        TextView textView = this.mTxtRatio;
        if (textView != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append("%");
            textView.setText(sb.toString());
        }
        ZMGifView zMGifView = this.mImgPic;
        if (zMGifView != null) {
            zMGifView.setRatio(i);
        }
    }

    public void clearRatio() {
        TextView textView = this.mTxtRatio;
        if (textView != null) {
            textView.setText("");
        }
        ZMGifView zMGifView = this.mImgPic;
        if (zMGifView != null) {
            zMGifView.clearRatio();
        }
    }

    public void setStatusImage(boolean z, int i) {
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setVisibility(z ? 0 : 8);
            this.mImgStatus.setImageResource(i);
        }
    }

    public void setPic(@Nullable String str) {
        int i;
        int i2;
        if (this.mImgPic != null) {
            Context context = getContext();
            if (context != null) {
                int i3 = this.PREVIEW_PIC_SIZE_MAX;
                StringBuilder sb = new StringBuilder();
                sb.append("file://");
                sb.append(str);
                Uri parse = Uri.parse(sb.toString());
                try {
                    Options options = new Options();
                    int i4 = 1;
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(context.getContentResolver().openInputStream(parse), null, options);
                    i2 = options.outWidth;
                    i = options.outHeight;
                    if (i2 > i3 || i > i3) {
                        int i5 = i2;
                        int i6 = i;
                        while (true) {
                            if (i4 < 3) {
                                int i7 = i4 + 1;
                                try {
                                    int i8 = i3 * 3;
                                    if (i2 / i7 < i8 / 4 && i / i7 < i8 / 4) {
                                        break;
                                    }
                                } catch (Exception unused) {
                                    i2 = i5;
                                    i = i6;
                                }
                            }
                            int i9 = i3 * 3;
                            if (i5 < i9 / 2 && i6 < i9 / 2) {
                                break;
                            }
                            i4++;
                            i5 = i2 / i4;
                            i6 = i / i4;
                        }
                        i2 = i5;
                        i = i6;
                    }
                } catch (Exception unused2) {
                    i2 = 0;
                    i = 0;
                }
                if (i2 <= 0 || i <= 0) {
                    if (VERSION.SDK_INT < 16) {
                        this.mImgPic.setBackgroundDrawable(getMesageBackgroudDrawable());
                    } else {
                        this.mImgPic.setBackground(getMesageBackgroudDrawable());
                    }
                    this.mImgPic.setPadding(this.mImgPaddingLeft, this.mImgPaddingTop, this.mImgPaddingRight, this.mImgPaddingBottom);
                    this.mImgPic.setImageResource(C4558R.C4559drawable.zm_image_placeholder);
                    ImageLoader.getInstance().clearImageLoding(this.mImgPic);
                } else {
                    this.mImgPic.setBackgroundResource(0);
                    this.mImgPic.setPadding(0, 0, 0, 0);
                    int scale = getScale(i2, i, i3, Math.round(context.getResources().getDisplayMetrics().density + 0.5f));
                    this.mImgPic.getLayoutParams().width = i2 * scale;
                    this.mImgPic.getLayoutParams().height = scale * i;
                    this.mImgPic.setImageResource(0);
                    ImageLoader.getInstance().displayImage(this.mImgPic, str, 0);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Drawable getMaskDrawable() {
        return getMesageBackgroudDrawable();
    }

    /* access modifiers changed from: protected */
    public int getBubbleImageRes() {
        return C4558R.C4559drawable.zm_chatfrom_bg;
    }

    @NonNull
    private Drawable createMaskedDrawable(Context context, Bitmap bitmap) {
        return new MaskedDrawable(getMaskDrawable(), bitmap, context.getResources().getDisplayMetrics().density);
    }

    public void setScreenName(@Nullable String str) {
        if (str != null) {
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setText(str);
            }
        }
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

    public void setMessageItem(MMMessageItem mMMessageItem) {
        this.mMessageItem = mMMessageItem;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        int i = 0;
        if (mMMessageItem.hideStarView || !mMMessageItem.isMessgeStarred) {
            this.mImgStarred.setVisibility(8);
        } else {
            this.mImgStarred.setVisibility(0);
        }
        setReactionLabels(mMMessageItem);
        LinearLayout linearLayout = (LinearLayout) findViewById(C4558R.C4560id.panelMsgLayout);
        if (mMMessageItem.onlyMessageShow) {
            this.mAvatarView.setVisibility(4);
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setVisibility(8);
            }
            linearLayout.setPadding(linearLayout.getPaddingLeft(), 0, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
        } else {
            linearLayout.setPadding(linearLayout.getPaddingLeft(), linearLayout.getPaddingBottom(), linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
            this.mAvatarView.setVisibility(0);
            if (this.mTxtScreenName != null && mMMessageItem.isIncomingMessage()) {
                setScreenName(mMMessageItem.fromScreenName);
                TextView textView2 = this.mTxtScreenName;
                if (textView2 != null) {
                    textView2.setVisibility(0);
                }
            } else if (this.mTxtScreenName == null || !mMMessageItem.isSendingMessage() || getContext() == null) {
                TextView textView3 = this.mTxtScreenName;
                if (textView3 != null) {
                    textView3.setVisibility(8);
                }
            } else {
                setScreenName(getContext().getString(C4558R.string.zm_lbl_content_you));
                this.mTxtScreenName.setVisibility(0);
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
                        AvatarView avatarView = this.mAvatarView;
                        if (avatarView != null) {
                            avatarView.show(mMMessageItem.fromContact.getAvatarParamsBuilder());
                        }
                    }
                }
            }
        }
        int[] imgRadius = getImgRadius();
        if (imgRadius != null) {
            ZMGifView zMGifView = this.mImgPic;
            if (zMGifView != null) {
                zMGifView.setRadius(imgRadius);
            }
        }
        if ((!StringUtil.isEmptyOrNull(mMMessageItem.picturePreviewPath) && new File(mMMessageItem.picturePreviewPath).exists()) || (!StringUtil.isEmptyOrNull(mMMessageItem.localFilePath) && new File(mMMessageItem.localFilePath).exists())) {
            LinearLayout linearLayout2 = this.mPanelProgress;
            if (linearLayout2 != null) {
                linearLayout2.setVisibility(8);
            }
            TextView textView4 = this.mFileUnavailableTextView;
            if (textView4 != null) {
                textView4.setVisibility(8);
            }
            this.mImgPic.setVisibility(0);
        } else if (mMMessageItem.fileDownloadResultCode == 5061) {
            this.mPanelProgress.setVisibility(8);
            this.mImgPic.setVisibility(8);
            this.mFileUnavailableTextView.setVisibility(0);
            if (VERSION.SDK_INT < 16) {
                this.mFileUnavailableTextView.setBackgroundDrawable(getMesageBackgroudDrawable());
            } else {
                this.mFileUnavailableTextView.setBackground(getMesageBackgroudDrawable());
            }
        } else {
            LinearLayout linearLayout3 = this.mPanelProgress;
            if (linearLayout3 != null) {
                linearLayout3.setVisibility(0);
                if (VERSION.SDK_INT < 16) {
                    this.mPanelProgress.setBackgroundDrawable(getProgressBackgroudDrawable());
                } else {
                    this.mPanelProgress.setBackground(getProgressBackgroudDrawable());
                }
                this.mImgPic.setVisibility(8);
                TextView textView5 = this.mFileUnavailableTextView;
                if (textView5 != null) {
                    textView5.setVisibility(8);
                }
                ProgressBar progressBar = this.mProgressBarDownload;
                if (progressBar != null) {
                    if (mMMessageItem.isPreviewDownloadFailed) {
                        i = 4;
                    }
                    progressBar.setVisibility(i);
                }
            }
        }
        if ((mMMessageItem.messageType == 27 || mMMessageItem.messageType == 28) && ((!StringUtil.isEmptyOrNull(mMMessageItem.localFilePath) && new File(mMMessageItem.localFilePath).exists()) || (!StringUtil.isEmptyOrNull(mMMessageItem.picturePreviewPath) && new File(mMMessageItem.picturePreviewPath).exists()))) {
            this.mImgPic.setGifRemoteResourse((StringUtil.isEmptyOrNull(mMMessageItem.localFilePath) || !new File(mMMessageItem.localFilePath).exists()) ? mMMessageItem.picturePreviewPath : mMMessageItem.localFilePath, null, this.mResizeListener);
        } else if (!StringUtil.isEmptyOrNull(mMMessageItem.picturePreviewPath) && new File(mMMessageItem.picturePreviewPath).exists() && ImageUtil.isValidImageFile(mMMessageItem.picturePreviewPath)) {
            setPic(mMMessageItem.picturePreviewPath);
        } else if (!StringUtil.isEmptyOrNull(mMMessageItem.localFilePath)) {
            setPic(mMMessageItem.localFilePath);
        } else {
            setPic(null);
        }
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
        setMessageItem(mMMessageItem);
        if (z) {
            this.mAvatarView.setVisibility(4);
            this.mReactionLabels.setVisibility(8);
            if (this.mTxtScreenName.getVisibility() == 0) {
                this.mTxtScreenName.setVisibility(4);
            }
        }
    }

    public Rect getMessageLocationOnScreen() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        ReactionLabelsView reactionLabelsView = this.mReactionLabels;
        return new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), (iArr[1] + getHeight()) - ((reactionLabelsView == null || reactionLabelsView.getVisibility() == 8) ? 0 : this.mReactionLabels.getHeight() + (UIUtil.dip2px(getContext(), 4.0f) * 2)));
    }

    public MMMessageItem getMessageItem() {
        return this.mMessageItem;
    }
}

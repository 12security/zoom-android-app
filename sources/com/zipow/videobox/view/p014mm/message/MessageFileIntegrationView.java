package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ReactionLabelsView;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickCancelListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickStatusImageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnLongClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import java.text.NumberFormat;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageFileIntegrationView */
public class MessageFileIntegrationView extends AbsMessageView {
    private static final int ONE_KB = 1024;
    private static final int ONE_MB = 1048576;
    protected AvatarView mAvatarView;
    protected View mBtnCancel;
    protected ProgressBar mDownloadPercent;
    protected ImageView mImgFileIcon;
    protected ImageView mImgFileStatus;
    protected ImageView mImgStarred;
    protected ImageView mImgStatus;
    protected MMMessageItem mMessageItem;
    protected View mPanelMessage;
    protected ProgressBar mProgressBar;
    protected ReactionLabelsView mReactionLabels;
    protected TextView mTxtFileName;
    protected TextView mTxtFileSize;
    protected TextView mTxtScreenName;

    /* access modifiers changed from: protected */
    @Nullable
    public Drawable getMesageBackgroudDrawable() {
        return null;
    }

    public MessageFileIntegrationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageFileIntegrationView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void initView() {
        inflateLayout();
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mImgStatus = (ImageView) findViewById(C4558R.C4560id.imgStatus);
        this.mProgressBar = (ProgressBar) findViewById(C4558R.C4560id.progressBar1);
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mPanelMessage = findViewById(C4558R.C4560id.panelMessage);
        this.mImgFileIcon = (ImageView) findViewById(C4558R.C4560id.imgFileIcon);
        this.mTxtFileName = (TextView) findViewById(C4558R.C4560id.txtFileName);
        this.mTxtFileSize = (TextView) findViewById(C4558R.C4560id.txtFileSize);
        this.mBtnCancel = findViewById(C4558R.C4560id.btnCancel);
        this.mImgFileStatus = (ImageView) findViewById(C4558R.C4560id.imgFileStatus);
        this.mDownloadPercent = (ProgressBar) findViewById(C4558R.C4560id.downloadPercent);
        this.mImgStarred = (ImageView) findViewById(C4558R.C4560id.zm_mm_starred);
        this.mReactionLabels = (ReactionLabelsView) findViewById(C4558R.C4560id.reaction_labels_view);
        setStatusImage(false, 0);
        View view = this.mPanelMessage;
        if (view != null) {
            view.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnShowContextMenuListener onShowContextMenuListener = MessageFileIntegrationView.this.getOnShowContextMenuListener();
                    if (onShowContextMenuListener != null) {
                        return onShowContextMenuListener.onShowContextMenu(view, MessageFileIntegrationView.this.mMessageItem);
                    }
                    return false;
                }
            });
            this.mPanelMessage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickMessageListener onClickMessageListener = MessageFileIntegrationView.this.getOnClickMessageListener();
                    if (onClickMessageListener != null) {
                        onClickMessageListener.onClickMessage(MessageFileIntegrationView.this.mMessageItem);
                    }
                }
            });
        }
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickStatusImageListener onClickStatusImageListener = MessageFileIntegrationView.this.getOnClickStatusImageListener();
                    if (onClickStatusImageListener != null) {
                        onClickStatusImageListener.onClickStatusImage(MessageFileIntegrationView.this.mMessageItem);
                    }
                }
            });
        }
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickAvatarListener onClickAvatarListener = MessageFileIntegrationView.this.getOnClickAvatarListener();
                    if (onClickAvatarListener != null) {
                        onClickAvatarListener.onClickAvatar(MessageFileIntegrationView.this.mMessageItem);
                    }
                }
            });
            this.mAvatarView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnLongClickAvatarListener onLongClickAvatarListener = MessageFileIntegrationView.this.getOnLongClickAvatarListener();
                    if (onLongClickAvatarListener != null) {
                        return onLongClickAvatarListener.onLongClickAvatar(MessageFileIntegrationView.this.mMessageItem);
                    }
                    return false;
                }
            });
        }
        View view2 = this.mBtnCancel;
        if (view2 != null) {
            view2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickCancelListener onClickCancelListenter = MessageFileIntegrationView.this.getOnClickCancelListenter();
                    if (onClickCancelListenter != null) {
                        onClickCancelListenter.onClickCancel(MessageFileIntegrationView.this.mMessageItem);
                    }
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null && MessageFileIntegrationView.this.mMessageItem != null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(MessageFileIntegrationView.this.mMessageItem.sessionId);
                        if (sessionById != null) {
                            ZoomLogEventTracking.eventTrackCancelDownload(sessionById.isGroup());
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_file_integration_receive, this);
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

    public void setStatusImage(boolean z, int i) {
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setVisibility(z ? 0 : 8);
            this.mImgStatus.setImageResource(i);
        }
    }

    public void setScreenName(@Nullable String str) {
        if (str != null) {
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setText(str);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005e, code lost:
        showNonProgressStatus(r6, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0062, code lost:
        showProgressStatus(r10, r6, r12, true, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006e, code lost:
        showProgressStatus(r10, r6, r12, true, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0078, code lost:
        showProgressStatus(r10, r6, r12, false, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setFileInfo(@androidx.annotation.Nullable com.zipow.videobox.ptapp.IMProtos.FileIntegrationInfo r16, com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileInfo r17, @androidx.annotation.Nullable java.lang.String r18, @androidx.annotation.Nullable com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo r19) {
        /*
            r15 = this;
            r9 = r15
            r0 = r18
            r1 = r19
            r2 = 0
            if (r0 == 0) goto L_0x0012
            java.io.File r3 = new java.io.File
            r3.<init>(r0)
            boolean r0 = r3.exists()
            goto L_0x0013
        L_0x0012:
            r0 = 0
        L_0x0013:
            r3 = 0
            r4 = 0
            if (r16 == 0) goto L_0x0021
            java.lang.String r3 = r16.getFileName()
            long r6 = r16.getFileSize()
            goto L_0x0022
        L_0x0021:
            r6 = r4
        L_0x0022:
            if (r1 == 0) goto L_0x003a
            long r4 = r1.bitsPerSecond
            long r10 = r1.transferredSize
            int r8 = r1.prevError
            int r1 = r1.state
            if (r0 != 0) goto L_0x0038
            r12 = 13
            if (r1 == r12) goto L_0x0035
            r12 = 4
            if (r1 != r12) goto L_0x0038
        L_0x0035:
            r12 = r4
            r1 = 0
            goto L_0x003e
        L_0x0038:
            r12 = r4
            goto L_0x003e
        L_0x003a:
            r10 = r4
            r12 = r10
            r1 = 0
            r8 = 0
        L_0x003e:
            android.widget.TextView r4 = r9.mTxtFileName
            if (r4 == 0) goto L_0x0047
            if (r3 == 0) goto L_0x0047
            r4.setText(r3)
        L_0x0047:
            android.widget.ImageView r4 = r9.mImgFileIcon
            if (r4 == 0) goto L_0x0054
            int r3 = p021us.zoom.androidlib.util.AndroidAppUtil.getIconForFile(r3)
            android.widget.ImageView r4 = r9.mImgFileIcon
            r4.setImageResource(r3)
        L_0x0054:
            switch(r1) {
                case 1: goto L_0x0078;
                case 2: goto L_0x006e;
                case 3: goto L_0x0062;
                case 4: goto L_0x005e;
                default: goto L_0x0057;
            }
        L_0x0057:
            switch(r1) {
                case 10: goto L_0x0078;
                case 11: goto L_0x006e;
                case 12: goto L_0x0062;
                case 13: goto L_0x005e;
                default: goto L_0x005a;
            }
        L_0x005a:
            r15.showNonProgressStatus(r6, r2)
            goto L_0x0083
        L_0x005e:
            r15.showNonProgressStatus(r6, r0)
            goto L_0x0083
        L_0x0062:
            r8 = 1
            r14 = 0
            r0 = r15
            r1 = r10
            r3 = r6
            r5 = r12
            r7 = r8
            r8 = r14
            r0.showProgressStatus(r1, r3, r5, r7, r8)
            goto L_0x0083
        L_0x006e:
            r14 = 1
            r0 = r15
            r1 = r10
            r3 = r6
            r5 = r12
            r7 = r14
            r0.showProgressStatus(r1, r3, r5, r7, r8)
            goto L_0x0083
        L_0x0078:
            r8 = 0
            r14 = 0
            r0 = r15
            r1 = r10
            r3 = r6
            r5 = r12
            r7 = r8
            r8 = r14
            r0.showProgressStatus(r1, r3, r5, r7, r8)
        L_0x0083:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.message.MessageFileIntegrationView.setFileInfo(com.zipow.videobox.ptapp.IMProtos$FileIntegrationInfo, com.zipow.videobox.ptapp.mm.ZoomMessage$FileInfo, java.lang.String, com.zipow.videobox.ptapp.mm.ZoomMessage$FileTransferInfo):void");
    }

    private void showNonProgressStatus(long j, boolean z) {
        String str;
        ProgressBar progressBar = this.mDownloadPercent;
        if (progressBar != null) {
            progressBar.setVisibility(8);
        }
        if (this.mTxtFileSize != null && j >= 0) {
            if (j > 1048576) {
                str = toFileSizeString(((double) j) / 1048576.0d, C4558R.string.zm_file_size_mb);
            } else if (j > 1024) {
                str = toFileSizeString(((double) j) / 1024.0d, C4558R.string.zm_file_size_kb);
            } else {
                str = toFileSizeString((double) j, C4558R.string.zm_file_size_bytes);
            }
            String str2 = "";
            MMMessageItem mMMessageItem = this.mMessageItem;
            if (!(mMMessageItem == null || mMMessageItem.fileIntegrationInfo == null)) {
                int type = this.mMessageItem.fileIntegrationInfo.getType();
                if (type == 1) {
                    str2 = getContext().getResources().getString(C4558R.string.zm_mm_file_from_68764, new Object[]{getContext().getResources().getString(C4558R.string.zm_btn_share_dropbox)});
                } else if (type == 2) {
                    str2 = getContext().getResources().getString(C4558R.string.zm_mm_file_from_68764, new Object[]{getContext().getResources().getString(C4558R.string.zm_btn_share_one_drive)});
                } else if (type == 3) {
                    str2 = getContext().getResources().getString(C4558R.string.zm_mm_file_from_68764, new Object[]{getContext().getResources().getString(C4558R.string.zm_btn_share_google_drive)});
                } else if (type == 4) {
                    str2 = getContext().getResources().getString(C4558R.string.zm_mm_file_from_68764, new Object[]{getContext().getResources().getString(C4558R.string.zm_btn_share_box)});
                } else {
                    str2 = getContext().getResources().getString(C4558R.string.zm_mm_open_in_browser_81340);
                }
            }
            if (!StringUtil.isEmptyOrNull(str2)) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(OAuth.SCOPE_DELIMITER);
                sb.append(str2);
                str = sb.toString();
            }
            this.mTxtFileSize.setText(str);
        }
        this.mBtnCancel.setVisibility(8);
        if (z) {
            ImageView imageView = this.mImgFileStatus;
            if (imageView != null) {
                imageView.setImageResource(C4558R.C4559drawable.zm_filebadge_success2);
                return;
            }
            return;
        }
        ImageView imageView2 = this.mImgFileStatus;
        if (imageView2 != null) {
            imageView2.setImageDrawable(null);
        }
    }

    private void showProgressStatus(long j, long j2, long j3, boolean z, int i) {
        String str;
        long j4 = j;
        long j5 = j2;
        int i2 = i;
        ProgressBar progressBar = this.mDownloadPercent;
        if (progressBar != null) {
            progressBar.setVisibility(0);
            if (j5 > 0) {
                this.mDownloadPercent.setProgress((int) ((100 * j4) / j5));
            } else {
                this.mDownloadPercent.setProgress(0);
            }
        }
        if (i2 == 0 && this.mTxtFileSize != null && j5 >= 0) {
            if (j5 > 1048576) {
                str = toFileSizeString(((double) j5) / 1048576.0d, ((double) j4) / 1048576.0d, C4558R.string.zm_ft_transfered_size_mb);
            } else if (j5 > 1024) {
                str = toFileSizeString(((double) j5) / 1024.0d, ((double) j4) / 1024.0d, C4558R.string.zm_ft_transfered_size_kb);
            } else {
                str = toFileSizeString((double) j5, (double) j4, C4558R.string.zm_ft_transfered_size_bytes);
            }
            if (z) {
                this.mTxtFileSize.setText(str);
            } else {
                TextView textView = this.mTxtFileSize;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" (");
                sb.append(toDownloadingSpeedString(j3));
                sb.append(")");
                textView.setText(sb.toString());
            }
        }
        if (this.mBtnCancel.getVisibility() != 0) {
            this.mBtnCancel.setVisibility(0);
        }
        if (i2 != 0) {
            ImageView imageView = this.mImgFileStatus;
            if (imageView != null) {
                imageView.setImageResource(C4558R.C4559drawable.zm_filebadge_error2);
            }
            TextView textView2 = this.mTxtFileSize;
            if (textView2 != null) {
                textView2.setText(errorCodeToMessage(i2));
            }
        } else if (z) {
            ImageView imageView2 = this.mImgFileStatus;
            if (imageView2 != null) {
                imageView2.setImageResource(C4558R.C4559drawable.zm_filebadge_paused2);
            }
        } else {
            ImageView imageView3 = this.mImgFileStatus;
            if (imageView3 != null) {
                imageView3.setImageDrawable(null);
            }
        }
    }

    private String toFileSizeString(double d, double d2, int i) {
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(1);
        String format = numberInstance.format(d);
        String format2 = numberInstance.format(d2);
        return getResources().getString(i, new Object[]{format2, format});
    }

    private String toFileSizeString(double d, int i) {
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(1);
        String format = numberInstance.format(d);
        return getResources().getString(i, new Object[]{format});
    }

    private String toDownloadingSpeedString(long j) {
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(1);
        if (j > 1048576) {
            String format = numberInstance.format(((double) j) / 1048576.0d);
            return getResources().getString(C4558R.string.zm_ft_speed_mb, new Object[]{format});
        } else if (j > 1024) {
            String format2 = numberInstance.format(((double) j) / 1024.0d);
            return getResources().getString(C4558R.string.zm_ft_speed_kb, new Object[]{format2});
        } else {
            String format3 = numberInstance.format((double) j);
            return getResources().getString(C4558R.string.zm_ft_speed_bytes, new Object[]{format3});
        }
    }

    private String errorCodeToMessage(int i) {
        switch (i) {
            case 20:
                return getResources().getString(C4558R.string.zm_ft_error_invalid_file);
            case 21:
                return getResources().getString(C4558R.string.zm_ft_error_file_too_big);
            case 22:
                return getResources().getString(C4558R.string.zm_ft_error_no_disk_space);
            case 23:
                return getResources().getString(C4558R.string.zm_ft_error_disk_io_error);
            case 24:
                return getResources().getString(C4558R.string.zm_ft_error_url_timeout);
            case 25:
                return getResources().getString(C4558R.string.zm_ft_error_network_disconnected);
            default:
                return getResources().getString(C4558R.string.zm_ft_error_unknown);
        }
    }

    private void updateChatMsgBackground() {
        if (VERSION.SDK_INT < 16) {
            this.mPanelMessage.setBackgroundDrawable(getMesageBackgroudDrawable());
        } else {
            this.mPanelMessage.setBackground(getMesageBackgroudDrawable());
        }
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        this.mMessageItem = mMMessageItem;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (mMMessageItem.hideStarView || !mMMessageItem.isMessgeStarred) {
            this.mImgStarred.setVisibility(8);
        } else {
            this.mImgStarred.setVisibility(0);
        }
        setReactionLabels(mMMessageItem);
        setFileInfo(mMMessageItem.fileIntegrationInfo, mMMessageItem.fileInfo, mMMessageItem.localFilePath, mMMessageItem.transferInfo);
        updateChatMsgBackground();
        LinearLayout linearLayout = (LinearLayout) findViewById(C4558R.C4560id.panelMsgLayout);
        if (mMMessageItem.onlyMessageShow) {
            this.mAvatarView.setVisibility(4);
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setVisibility(8);
            }
            linearLayout.setPadding(linearLayout.getPaddingLeft(), 0, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
            return;
        }
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
        return new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), (iArr[0] + getHeight()) - ((reactionLabelsView == null || reactionLabelsView.getVisibility() == 8) ? 0 : this.mReactionLabels.getHeight() + (UIUtil.dip2px(getContext(), 4.0f) * 2)));
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

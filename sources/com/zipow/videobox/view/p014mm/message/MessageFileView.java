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
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ReactionLabelsView;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickStatusImageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnLongClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import java.text.NumberFormat;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageFileView */
public class MessageFileView extends AbsMessageView {
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
    protected ProgressBar mPbFileStatus;
    protected ProgressBar mProgressBar;
    protected ReactionLabelsView mReactionLabels;
    protected TextView mTxtFileName;
    protected TextView mTxtFileSize;
    protected TextView mTxtScreenName;

    public MessageFileView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageFileView(Context context) {
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
        this.mPbFileStatus = (ProgressBar) findViewById(C4558R.C4560id.pbFileStatus);
        this.mImgStarred = (ImageView) findViewById(C4558R.C4560id.zm_mm_starred);
        this.mReactionLabels = (ReactionLabelsView) findViewById(C4558R.C4560id.reaction_labels_view);
        setStatusImage(false, 0);
        View view = this.mPanelMessage;
        if (view != null) {
            view.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnShowContextMenuListener onShowContextMenuListener = MessageFileView.this.getOnShowContextMenuListener();
                    if (onShowContextMenuListener != null) {
                        return onShowContextMenuListener.onShowContextMenu(view, MessageFileView.this.mMessageItem);
                    }
                    return false;
                }
            });
            this.mPanelMessage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickMessageListener onClickMessageListener = MessageFileView.this.getOnClickMessageListener();
                    if (onClickMessageListener != null) {
                        onClickMessageListener.onClickMessage(MessageFileView.this.mMessageItem);
                    }
                }
            });
        }
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickStatusImageListener onClickStatusImageListener = MessageFileView.this.getOnClickStatusImageListener();
                    if (onClickStatusImageListener != null) {
                        onClickStatusImageListener.onClickStatusImage(MessageFileView.this.mMessageItem);
                    }
                }
            });
        }
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickAvatarListener onClickAvatarListener = MessageFileView.this.getOnClickAvatarListener();
                    if (onClickAvatarListener != null) {
                        onClickAvatarListener.onClickAvatar(MessageFileView.this.mMessageItem);
                    }
                }
            });
            this.mAvatarView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnLongClickAvatarListener onLongClickAvatarListener = MessageFileView.this.getOnLongClickAvatarListener();
                    if (onLongClickAvatarListener != null) {
                        return onLongClickAvatarListener.onLongClickAvatar(MessageFileView.this.mMessageItem);
                    }
                    return false;
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_file_receive, this);
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

    /* access modifiers changed from: protected */
    @Nullable
    public Drawable getMesageBackgroudDrawable() {
        return getResources().getDrawable(C4558R.C4559drawable.zm_message_file);
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

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0062, code lost:
        showNonProgressStatus(r6, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0066, code lost:
        showProgressStatus(r4, r6, r12, true, 0, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0073, code lost:
        showProgressStatus(r4, r6, r12, true, r8, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007e, code lost:
        showProgressStatus(r4, r6, r12, false, 0, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x008a, code lost:
        setContentDescription(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x008d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setFileInfo(@androidx.annotation.Nullable com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileInfo r18, @androidx.annotation.Nullable java.lang.String r19, @androidx.annotation.Nullable com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo r20) {
        /*
            r17 = this;
            r10 = r17
            r0 = r18
            r1 = r19
            r11 = r20
            r2 = 0
            if (r1 == 0) goto L_0x0015
            java.io.File r3 = new java.io.File
            r3.<init>(r1)
            boolean r1 = r3.exists()
            goto L_0x0016
        L_0x0015:
            r1 = 0
        L_0x0016:
            r3 = 0
            r4 = 0
            if (r0 == 0) goto L_0x0020
            java.lang.String r3 = r0.name
            long r6 = r0.size
            goto L_0x0021
        L_0x0020:
            r6 = r4
        L_0x0021:
            if (r11 == 0) goto L_0x003f
            long r4 = r11.bitsPerSecond
            long r8 = r11.transferredSize
            int r0 = r11.prevError
            int r12 = r11.state
            if (r1 != 0) goto L_0x0039
            r13 = 13
            if (r12 == r13) goto L_0x0034
            r13 = 4
            if (r12 != r13) goto L_0x0039
        L_0x0034:
            r12 = r4
            r4 = r8
            r9 = 0
            r8 = r0
            goto L_0x0042
        L_0x0039:
            r15 = r8
            r8 = r0
            r9 = r12
            r12 = r4
            r4 = r15
            goto L_0x0042
        L_0x003f:
            r12 = r4
            r8 = 0
            r9 = 0
        L_0x0042:
            android.widget.TextView r0 = r10.mTxtFileName
            if (r0 == 0) goto L_0x004b
            if (r3 == 0) goto L_0x004b
            r0.setText(r3)
        L_0x004b:
            android.widget.ImageView r0 = r10.mImgFileIcon
            if (r0 == 0) goto L_0x0058
            int r0 = p021us.zoom.androidlib.util.AndroidAppUtil.getIconForFile(r3)
            android.widget.ImageView r3 = r10.mImgFileIcon
            r3.setImageResource(r0)
        L_0x0058:
            switch(r9) {
                case 1: goto L_0x007e;
                case 2: goto L_0x0073;
                case 3: goto L_0x0066;
                case 4: goto L_0x0062;
                default: goto L_0x005b;
            }
        L_0x005b:
            switch(r9) {
                case 10: goto L_0x007e;
                case 11: goto L_0x0073;
                case 12: goto L_0x0066;
                case 13: goto L_0x0062;
                default: goto L_0x005e;
            }
        L_0x005e:
            r10.showNonProgressStatus(r6, r2)
            goto L_0x008a
        L_0x0062:
            r10.showNonProgressStatus(r6, r1)
            goto L_0x008a
        L_0x0066:
            r8 = 1
            r14 = 0
            r0 = r17
            r1 = r4
            r3 = r6
            r5 = r12
            r7 = r8
            r8 = r14
            r0.showProgressStatus(r1, r3, r5, r7, r8, r9)
            goto L_0x008a
        L_0x0073:
            r14 = 1
            r0 = r17
            r1 = r4
            r3 = r6
            r5 = r12
            r7 = r14
            r0.showProgressStatus(r1, r3, r5, r7, r8, r9)
            goto L_0x008a
        L_0x007e:
            r8 = 0
            r14 = 0
            r0 = r17
            r1 = r4
            r3 = r6
            r5 = r12
            r7 = r8
            r8 = r14
            r0.showProgressStatus(r1, r3, r5, r7, r8, r9)
        L_0x008a:
            r10.setContentDescription(r11)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.message.MessageFileView.setFileInfo(com.zipow.videobox.ptapp.mm.ZoomMessage$FileInfo, java.lang.String, com.zipow.videobox.ptapp.mm.ZoomMessage$FileTransferInfo):void");
    }

    @NonNull
    private String getFileSize() {
        String str = "";
        MMMessageItem mMMessageItem = this.mMessageItem;
        if (mMMessageItem == null || mMMessageItem.fileInfo == null) {
            return str;
        }
        long j = this.mMessageItem.fileInfo.size;
        if (j > 1048576) {
            return toFileSizeString(((double) j) / 1048576.0d, C4558R.string.zm_file_size_mb);
        }
        if (j > 1024) {
            return toFileSizeString(((double) j) / 1024.0d, C4558R.string.zm_file_size_kb);
        }
        return toFileSizeString((double) j, C4558R.string.zm_file_size_bytes);
    }

    private void setContentDescription(@Nullable FileTransferInfo fileTransferInfo) {
        if (fileTransferInfo != null) {
            int i = fileTransferInfo.state;
            TextView textView = this.mTxtFileName;
            String charSequence = textView == null ? "" : textView.getText().toString();
            TextView textView2 = this.mTxtFileSize;
            String charSequence2 = textView2 == null ? "" : textView2.getText().toString();
            int i2 = 0;
            if (i == 4) {
                i2 = C4558R.string.zm_msg_file_state_uploaded_69051;
            } else if (i == 13) {
                i2 = C4558R.string.zm_msg_file_state_downloaded_69051;
            } else {
                if (i == 0) {
                    MMMessageItem mMMessageItem = this.mMessageItem;
                    if (mMMessageItem != null) {
                        if (mMMessageItem.messageType == 11) {
                            i2 = C4558R.string.zm_msg_file_state_uploaded_69051;
                        } else if (this.mMessageItem.messageType == 10) {
                            i2 = C4558R.string.zm_msg_file_state_ready_for_download_69051;
                        }
                    }
                }
                if (i == 12 || i == 3) {
                    i2 = C4558R.string.zm_msg_file_state_paused_97194;
                } else if (i == 2) {
                    i2 = C4558R.string.zm_msg_file_state_failed_upload_97194;
                } else if (i == 11) {
                    i2 = C4558R.string.zm_msg_file_state_failed_download_97194;
                }
            }
            if (i2 != 0) {
                View view = this.mPanelMessage;
                StringBuilder sb = new StringBuilder();
                sb.append(charSequence);
                sb.append(OAuth.SCOPE_DELIMITER);
                sb.append(charSequence2);
                sb.append(OAuth.SCOPE_DELIMITER);
                sb.append(getResources().getString(i2));
                view.setContentDescription(sb.toString());
            }
        }
    }

    private void showNonProgressStatus(long j, boolean z) {
        String str;
        if (this.mTxtFileSize != null && j >= 0) {
            if (j > 1048576) {
                str = toFileSizeString(((double) j) / 1048576.0d, C4558R.string.zm_file_size_mb);
            } else if (j > 1024) {
                str = toFileSizeString(((double) j) / 1024.0d, C4558R.string.zm_file_size_kb);
            } else {
                str = toFileSizeString((double) j, C4558R.string.zm_file_size_bytes);
            }
            MMMessageItem mMMessageItem = this.mMessageItem;
            if (mMMessageItem != null && mMMessageItem.messageState == 6) {
                str = getResources().getString(C4558R.string.zm_ft_state_canceled_101390, new Object[]{str});
            }
            this.mTxtFileSize.setText(str);
        }
        if (z) {
            ImageView imageView = this.mImgFileStatus;
            if (imageView != null) {
                imageView.setImageResource(C4558R.C4559drawable.zm_filebadge_success3);
                showView(this.mPbFileStatus, 8);
                return;
            }
            return;
        }
        ImageView imageView2 = this.mImgFileStatus;
        if (imageView2 != null) {
            imageView2.setImageDrawable(null);
            showView(this.mPbFileStatus, 8);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0020  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0024 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showProgressStatus(long r11, long r13, long r15, boolean r17, int r18, int r19) {
        /*
            r10 = this;
            r0 = r10
            r1 = r11
            r3 = r13
            r5 = r19
            r6 = 1
            r7 = 0
            if (r17 == 0) goto L_0x001d
            com.zipow.videobox.view.mm.MMMessageItem r8 = r0.mMessageItem
            if (r8 == 0) goto L_0x001d
            java.lang.String r8 = r8.sessionId
            boolean r8 = com.zipow.videobox.util.ZMIMUtils.isFileTransferResumeEnabled(r8)
            if (r8 == 0) goto L_0x001b
            com.zipow.videobox.view.mm.MMMessageItem r8 = r0.mMessageItem
            boolean r8 = r8.isE2E
            if (r8 == 0) goto L_0x001d
        L_0x001b:
            r8 = 1
            goto L_0x001e
        L_0x001d:
            r8 = 0
        L_0x001e:
            if (r8 == 0) goto L_0x0024
            r10.showNonProgressStatus(r13, r7)
            return
        L_0x0024:
            if (r18 != 0) goto L_0x0089
            android.widget.TextView r8 = r0.mTxtFileSize
            if (r8 == 0) goto L_0x0089
            r8 = 0
            int r8 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1))
            if (r8 < 0) goto L_0x0089
            r8 = 1048576(0x100000, double:5.180654E-318)
            int r8 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1))
            if (r8 <= 0) goto L_0x0049
            double r3 = (double) r3
            r8 = 4697254411347427328(0x4130000000000000, double:1048576.0)
            double r3 = r3 / r8
            double r1 = (double) r1
            double r1 = r1 / r8
            int r8 = p021us.zoom.videomeetings.C4558R.string.zm_ft_transfered_size_mb
            r11 = r10
            r12 = r3
            r14 = r1
            r16 = r8
            java.lang.String r1 = r11.toFileSizeString(r12, r14, r16)
            goto L_0x006e
        L_0x0049:
            r8 = 1024(0x400, double:5.06E-321)
            int r8 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1))
            if (r8 <= 0) goto L_0x0061
            double r3 = (double) r3
            r8 = 4652218415073722368(0x4090000000000000, double:1024.0)
            double r3 = r3 / r8
            double r1 = (double) r1
            double r1 = r1 / r8
            int r8 = p021us.zoom.videomeetings.C4558R.string.zm_ft_transfered_size_kb
            r11 = r10
            r12 = r3
            r14 = r1
            r16 = r8
            java.lang.String r1 = r11.toFileSizeString(r12, r14, r16)
            goto L_0x006e
        L_0x0061:
            double r3 = (double) r3
            double r1 = (double) r1
            int r8 = p021us.zoom.videomeetings.C4558R.string.zm_ft_transfered_size_bytes
            r11 = r10
            r12 = r3
            r14 = r1
            r16 = r8
            java.lang.String r1 = r11.toFileSizeString(r12, r14, r16)
        L_0x006e:
            if (r17 == 0) goto L_0x0084
            android.widget.TextView r2 = r0.mTxtFileSize
            android.content.res.Resources r3 = r10.getResources()
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_ft_state_paused_70707
            java.lang.Object[] r6 = new java.lang.Object[r6]
            r6[r7] = r1
            java.lang.String r1 = r3.getString(r4, r6)
            r2.setText(r1)
            goto L_0x0089
        L_0x0084:
            android.widget.TextView r2 = r0.mTxtFileSize
            r2.setText(r1)
        L_0x0089:
            r1 = 8
            if (r18 == 0) goto L_0x00a7
            android.widget.ImageView r2 = r0.mImgFileStatus
            if (r2 == 0) goto L_0x009b
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_filebadge_error2
            r2.setImageResource(r3)
            android.widget.ProgressBar r2 = r0.mPbFileStatus
            r10.showView(r2, r1)
        L_0x009b:
            android.widget.TextView r1 = r0.mTxtFileSize
            if (r1 == 0) goto L_0x00e7
            java.lang.String r2 = r10.errorMessage(r5)
            r1.setText(r2)
            goto L_0x00e7
        L_0x00a7:
            r2 = 2
            if (r5 == r2) goto L_0x00ce
            r2 = 11
            if (r5 != r2) goto L_0x00af
            goto L_0x00ce
        L_0x00af:
            if (r17 == 0) goto L_0x00c0
            android.widget.ImageView r2 = r0.mImgFileStatus
            if (r2 == 0) goto L_0x00e7
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_filebadge_paused2
            r2.setImageResource(r3)
            android.widget.ProgressBar r2 = r0.mPbFileStatus
            r10.showView(r2, r1)
            goto L_0x00e7
        L_0x00c0:
            android.widget.ImageView r1 = r0.mImgFileStatus
            if (r1 == 0) goto L_0x00e7
            r2 = 0
            r1.setImageDrawable(r2)
            android.widget.ProgressBar r1 = r0.mPbFileStatus
            r10.showView(r1, r7)
            goto L_0x00e7
        L_0x00ce:
            android.widget.ImageView r2 = r0.mImgFileStatus
            if (r2 == 0) goto L_0x00dc
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_filebadge_error2
            r2.setImageResource(r3)
            android.widget.ProgressBar r2 = r0.mPbFileStatus
            r10.showView(r2, r1)
        L_0x00dc:
            android.widget.TextView r1 = r0.mTxtFileSize
            if (r1 == 0) goto L_0x00e7
            java.lang.String r2 = r10.errorMessage(r5)
            r1.setText(r2)
        L_0x00e7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.message.MessageFileView.showProgressStatus(long, long, long, boolean, int, int):void");
    }

    private void showView(@Nullable View view, int i) {
        if (view != null) {
            view.setVisibility(i);
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

    @NonNull
    private String errorMessage(int i) {
        String fileSize = getFileSize();
        if (i == 2) {
            return getContext().getString(C4558R.string.zm_ft_error_fail_to_send_70707, new Object[]{fileSize});
        } else if (i == 11) {
            return getContext().getString(C4558R.string.zm_ft_error_fail_to_download_70707, new Object[]{fileSize});
        } else if (this.mMessageItem.messageType == 11) {
            return getContext().getString(C4558R.string.zm_ft_error_fail_to_send_70707, new Object[]{fileSize});
        } else if (this.mMessageItem.messageType != 10) {
            return fileSize;
        } else {
            return getContext().getString(C4558R.string.zm_ft_error_fail_to_download_70707, new Object[]{fileSize});
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
        setReactionLabels(mMMessageItem);
        setFileInfo(mMMessageItem.fileInfo, mMMessageItem.localFilePath, mMMessageItem.transferInfo);
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

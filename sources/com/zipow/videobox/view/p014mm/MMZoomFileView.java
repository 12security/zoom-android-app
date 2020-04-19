package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.LazyLoadDrawable;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.ZMGifView;
import com.zipow.videobox.view.p014mm.MMZoomFile.FileMatchInfo;
import com.zipow.videobox.view.p014mm.MMZoomFile.HighlightPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMZoomFileView */
public class MMZoomFileView extends LinearLayout {
    private View mBtnCancel;
    /* access modifiers changed from: private */
    public MMZoomFile mFile;
    private ZMGifView mImgFileLogo;
    private ImageView mImgPendingType;
    private ImageView mImgShare;
    /* access modifiers changed from: private */
    public OnContentFileOperatorListener mOnClickOperatorListener;
    /* access modifiers changed from: private */
    public OnShowAllShareActionListener mOnMoreShareActionListener;
    private View mPanelTranslate;
    private ProgressBar mProgressBarPending;
    private TextView mTxtFileName;
    private TextView mTxtFileOwner;
    private TextView mTxtFileShareIn;
    private TextView mTxtTranslateSpeed;
    @Nullable
    private String myJid;
    /* access modifiers changed from: private */
    public ArrayList<String> shareOperatorSessionIds;
    /* access modifiers changed from: private */
    public ArrayList<String> shareSessionIds;

    /* renamed from: com.zipow.videobox.view.mm.MMZoomFileView$MoreShareActionClickableSpan */
    class MoreShareActionClickableSpan extends ClickableSpan {
        MoreShareActionClickableSpan() {
        }

        public void onClick(@NonNull View view) {
            if (MMZoomFileView.this.mOnMoreShareActionListener != null) {
                MMZoomFileView.this.mOnMoreShareActionListener.onShowAllShareAction(MMZoomFileView.this.mFile.getWebID(), MMZoomFileView.this.shareSessionIds, MMZoomFileView.this.shareOperatorSessionIds);
            }
        }

        public void updateDrawState(@NonNull TextPaint textPaint) {
            textPaint.setColor(MMZoomFileView.this.getContext().getResources().getColor(C4558R.color.zm_black));
            textPaint.setUnderlineText(true);
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMZoomFileView$OnShowAllShareActionListener */
    public interface OnShowAllShareActionListener {
        void onShowAllShareAction(String str, ArrayList<String> arrayList, ArrayList<String> arrayList2);
    }

    /* renamed from: com.zipow.videobox.view.mm.MMZoomFileView$ShareActionClickableSpan */
    class ShareActionClickableSpan extends ClickableSpan {
        private boolean isShowDeleteItem;
        private MMZoomShareAction mAction;

        ShareActionClickableSpan(MMZoomShareAction mMZoomShareAction, boolean z) {
            this.mAction = mMZoomShareAction;
            this.isShowDeleteItem = z;
        }

        public void onClick(@NonNull View view) {
            if (MMZoomFileView.this.mOnClickOperatorListener != null) {
                MMZoomFileView.this.mOnClickOperatorListener.onZoomFileSharerAction(MMZoomFileView.this.mFile.getWebID(), this.mAction, MMZoomFileView.this.shareSessionIds != null && MMZoomFileView.this.shareSessionIds.size() == 2, this.isShowDeleteItem);
            }
        }

        public void updateDrawState(@NonNull TextPaint textPaint) {
            textPaint.setColor(MMZoomFileView.this.getContext().getResources().getColor(C4558R.color.zm_black));
            textPaint.setUnderlineText(true);
        }
    }

    public MMZoomFileView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMZoomFileView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mImgFileLogo = (ZMGifView) findViewById(C4558R.C4560id.imgFileLogo);
        this.mTxtFileName = (TextView) findViewById(C4558R.C4560id.txtFileName);
        this.mTxtFileOwner = (TextView) findViewById(C4558R.C4560id.txtFileOwner);
        this.mTxtFileShareIn = (TextView) findViewById(C4558R.C4560id.txtFileGroups);
        this.mImgShare = (ImageView) findViewById(C4558R.C4560id.imgShare);
        this.mTxtTranslateSpeed = (TextView) findViewById(C4558R.C4560id.txtTranslateSpeed);
        this.mBtnCancel = findViewById(C4558R.C4560id.btnCancel);
        this.mPanelTranslate = findViewById(C4558R.C4560id.panelTranslate);
        this.mProgressBarPending = (ProgressBar) findViewById(C4558R.C4560id.progressBarPending);
        this.mImgPendingType = (ImageView) findViewById(C4558R.C4560id.imgPendingType);
        this.mTxtFileShareIn.setHighlightColor(getContext().getResources().getColor(C4558R.color.zm_transparent));
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                this.myJid = myself.getJid();
            }
            this.mImgFileLogo.setRadius(UIUtil.dip2px(getContext(), 8.0f));
        }
    }

    public void setOnMoreShareActionListener(OnShowAllShareActionListener onShowAllShareActionListener) {
        this.mOnMoreShareActionListener = onShowAllShareActionListener;
    }

    public void setOnClickOperatorListener(OnContentFileOperatorListener onContentFileOperatorListener) {
        this.mOnClickOperatorListener = onContentFileOperatorListener;
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_mm_content_file_item, this);
    }

    @NonNull
    private CharSequence getFileScreenName(MMZoomFile mMZoomFile) {
        List<FileMatchInfo> matchInfos = mMZoomFile.getMatchInfos();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(mMZoomFile.getFileName());
        if (matchInfos != null) {
            for (FileMatchInfo fileMatchInfo : matchInfos) {
                if (fileMatchInfo.mType == 1) {
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getContext().getResources().getColor(C4558R.color.zm_highlight));
                    for (HighlightPosition highlightPosition : fileMatchInfo.mHighlightPositions) {
                        spannableStringBuilder.setSpan(foregroundColorSpan, highlightPosition.start, highlightPosition.end, 33);
                    }
                }
            }
        }
        return spannableStringBuilder;
    }

    public void setMMZoomFile(@NonNull MMZoomFile mMZoomFile, boolean z) {
        setMMZoomFile(mMZoomFile, z, null);
    }

    public void setMMZoomFile(@NonNull MMZoomFile mMZoomFile, boolean z, String str) {
        this.mFile = mMZoomFile;
        Context context = getContext();
        boolean isFileTransferDisabled = PTApp.getInstance().isFileTransferDisabled();
        if (!ZmPtUtils.isImageFile(mMZoomFile.getFileType())) {
            this.mImgFileLogo.setImageResource(AndroidAppUtil.getIconForFile(mMZoomFile.getFileName()));
        } else if (ImageUtil.isValidImageFile(mMZoomFile.getPicturePreviewPath())) {
            LazyLoadDrawable lazyLoadDrawable = new LazyLoadDrawable(mMZoomFile.getPicturePreviewPath());
            int width = this.mImgFileLogo.getWidth();
            if (width == 0) {
                width = UIUtil.dip2px(getContext(), 40.0f);
            }
            lazyLoadDrawable.setMaxArea(width * width);
            this.mImgFileLogo.setImageDrawable(lazyLoadDrawable);
        } else if (ImageUtil.isValidImageFile(mMZoomFile.getLocalPath())) {
            LazyLoadDrawable lazyLoadDrawable2 = new LazyLoadDrawable(mMZoomFile.getLocalPath());
            int width2 = this.mImgFileLogo.getWidth();
            if (width2 == 0) {
                width2 = UIUtil.dip2px(getContext(), 40.0f);
            }
            lazyLoadDrawable2.setMaxArea(width2 * width2);
            this.mImgFileLogo.setImageDrawable(lazyLoadDrawable2);
        } else {
            this.mImgFileLogo.setImageResource(AndroidAppUtil.getIconForFile(mMZoomFile.getFileName()));
        }
        this.mTxtFileName.setText(getFileScreenName(mMZoomFile));
        String formatSharedTime = formatSharedTime(mMZoomFile.getLastedShareTime(str));
        if (!StringUtil.isSameString(mMZoomFile.getOwnerJid(), this.myJid)) {
            this.mTxtFileOwner.setText(context.getString(C4558R.string.zm_lbl_content_share_by, new Object[]{StringUtil.isEmptyOrNull(mMZoomFile.getOwnerName()) ? "" : TextUtils.ellipsize(mMZoomFile.getOwnerName(), this.mTxtFileOwner.getPaint(), (float) UIUtil.dip2px(getContext(), 100.0f), TruncateAt.END), formatSharedTime}));
        } else if (z) {
            this.mTxtFileOwner.setText(formatSharedTime);
        } else {
            this.mTxtFileOwner.setText(context.getString(C4558R.string.zm_lbl_content_share_by_me, new Object[]{context.getString(C4558R.string.zm_lbl_content_you), formatSharedTime}));
        }
        CharSequence charSequence = "";
        List<MMZoomShareAction> shareAction = mMZoomFile.getShareAction();
        if (shareAction != null) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            if (shareAction.isEmpty()) {
                charSequence = StringUtil.isSameString(mMZoomFile.getOwnerJid(), this.myJid) ? "" : context.getString(C4558R.string.zm_lbl_content_share_in_buddy, new Object[]{mMZoomFile.getOwnerName()});
            } else {
                for (MMZoomShareAction mMZoomShareAction : shareAction) {
                    if (mMZoomShareAction.isGroupAdmin(context) && !mMZoomFile.getOperatorAbleSessions().contains(mMZoomShareAction.getSharee())) {
                        mMZoomFile.addOperatorAbleSession(mMZoomShareAction.getSharee());
                    }
                }
                this.shareOperatorSessionIds = new ArrayList<>();
                this.shareOperatorSessionIds.addAll(mMZoomFile.getOperatorAbleSessions());
                if (!StringUtil.isSameString(mMZoomFile.getOwnerJid(), this.myJid)) {
                    this.shareSessionIds = new ArrayList<>();
                    for (int size = shareAction.size() - 1; size >= 0; size--) {
                        MMZoomShareAction mMZoomShareAction2 = (MMZoomShareAction) shareAction.get(size);
                        String sharee = mMZoomShareAction2.getSharee();
                        if (StringUtil.isEmptyOrNull(sharee) || mMZoomShareAction2.isBlockedByIB(context)) {
                            shareAction.remove(size);
                        } else if (StringUtil.isSameString(sharee, this.myJid)) {
                            mMZoomShareAction2.setSharee(mMZoomFile.getOwnerJid());
                            mMZoomShareAction2.setIsToMe(true);
                            if (mMZoomShareAction2.isBlockedByIB(context)) {
                                shareAction.remove(size);
                            } else {
                                this.shareSessionIds.add(mMZoomFile.getOwnerJid());
                            }
                        } else if (StringUtil.isSameString(sharee, mMZoomFile.getOwnerJid()) && mMZoomShareAction2.isToMe()) {
                            this.shareSessionIds.add(mMZoomFile.getOwnerJid());
                        } else if (mMZoomShareAction2.isGroup() || mMZoomShareAction2.isMUC()) {
                            this.shareSessionIds.add(mMZoomShareAction2.getSharee());
                        } else {
                            shareAction.remove(size);
                        }
                    }
                    if (shareAction.size() > 2) {
                        SpannableString spannableString = new SpannableString(getContext().getString(C4558R.string.zm_lbl_content_share_in_more_group_89710, new Object[]{Integer.valueOf(shareAction.size())}));
                        spannableString.setSpan(new MoreShareActionClickableSpan(), 0, spannableString.length(), 33);
                        String str2 = "$$$$$$$$$$$$&&&&";
                        charSequence = TextUtils.replace(context.getString(C4558R.string.zm_lbl_content_share_in_group, new Object[]{str2}), new String[]{str2}, new CharSequence[]{spannableString});
                    } else if (shareAction.size() > 0) {
                        for (MMZoomShareAction mMZoomShareAction3 : shareAction) {
                            String shareeName = mMZoomShareAction3.getShareeName(context);
                            if (!TextUtils.isEmpty(shareeName)) {
                                String formatShareeName = formatShareeName(shareeName);
                                if (formatShareeName != null) {
                                    boolean z2 = !CollectionsUtil.isListEmpty(mMZoomFile.getOperatorAbleSessions()) && mMZoomFile.getOperatorAbleSessions().contains(mMZoomShareAction3.getSharee());
                                    SpannableString spannableString2 = new SpannableString(formatShareeName);
                                    spannableString2.setSpan(new ShareActionClickableSpan(mMZoomShareAction3, z2), 0, formatShareeName.length(), 33);
                                    spannableStringBuilder.append(spannableString2);
                                    spannableStringBuilder.append(PreferencesConstants.COOKIE_DELIMITER);
                                }
                            }
                        }
                        if (spannableStringBuilder.length() > 0) {
                            String str3 = "&&&&&&&&&&&&&";
                            charSequence = TextUtils.replace(context.getString(C4558R.string.zm_lbl_content_share_in_group, new Object[]{str3}), new String[]{str3}, new CharSequence[]{spannableStringBuilder.subSequence(0, spannableStringBuilder.length() - 1)});
                        }
                    }
                } else {
                    boolean isSameString = StringUtil.isSameString(mMZoomFile.getOwnerJid(), this.myJid);
                    this.shareSessionIds = new ArrayList<>();
                    for (int size2 = shareAction.size() - 1; size2 >= 0; size2--) {
                        MMZoomShareAction mMZoomShareAction4 = (MMZoomShareAction) shareAction.get(size2);
                        if (StringUtil.isEmptyOrNull(mMZoomShareAction4.getSharee()) || mMZoomShareAction4.isBlockedByIB(context)) {
                            shareAction.remove(size2);
                        } else if (mMZoomShareAction4.isBuddy(context)) {
                            this.shareSessionIds.add(mMZoomShareAction4.getSharee());
                        } else if (mMZoomShareAction4.isGroup() || mMZoomShareAction4.isMUC()) {
                            this.shareSessionIds.add(mMZoomShareAction4.getSharee());
                        } else {
                            shareAction.remove(size2);
                        }
                    }
                    if (shareAction.size() > 2) {
                        SpannableString spannableString3 = new SpannableString(getContext().getString(C4558R.string.zm_lbl_content_share_in_more_group_89710, new Object[]{Integer.valueOf(shareAction.size())}));
                        spannableString3.setSpan(new MoreShareActionClickableSpan(), 0, spannableString3.length(), 33);
                        String str4 = "$$$$$$$$$$$$&&&&";
                        charSequence = TextUtils.replace(context.getString(C4558R.string.zm_lbl_content_share_in_group, new Object[]{str4}), new String[]{str4}, new CharSequence[]{spannableString3});
                    } else if (shareAction.size() > 0) {
                        for (MMZoomShareAction mMZoomShareAction5 : shareAction) {
                            String shareeName2 = mMZoomShareAction5.getShareeName(context);
                            if (!StringUtil.isEmptyOrNull(shareeName2)) {
                                String formatShareeName2 = formatShareeName(shareeName2);
                                if (formatShareeName2 != null) {
                                    SpannableString spannableString4 = new SpannableString(formatShareeName2);
                                    spannableString4.setSpan(new ShareActionClickableSpan(mMZoomShareAction5, isSameString), 0, formatShareeName2.length(), 33);
                                    spannableStringBuilder.append(spannableString4);
                                    spannableStringBuilder.append(PreferencesConstants.COOKIE_DELIMITER);
                                }
                            }
                        }
                        if (spannableStringBuilder.length() > 0) {
                            String str5 = "&&&&&&&&&&&&&";
                            charSequence = TextUtils.replace(context.getString(C4558R.string.zm_lbl_content_share_in_group, new Object[]{str5}), new String[]{str5}, new CharSequence[]{spannableStringBuilder.subSequence(0, spannableStringBuilder.length() - 1)});
                        }
                    }
                }
            }
        }
        if (charSequence == null || charSequence.length() <= 0) {
            this.mTxtFileShareIn.setText(context.getString(C4558R.string.zm_lbl_content_no_share));
            this.mTxtFileShareIn.setMovementMethod(null);
        } else {
            this.mTxtFileShareIn.setText(charSequence);
            if (charSequence instanceof Spanned) {
                ClickableSpan[] clickableSpanArr = (ClickableSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), ClickableSpan.class);
                if (clickableSpanArr == null || clickableSpanArr.length == 0) {
                    this.mTxtFileShareIn.setMovementMethod(null);
                } else {
                    this.mTxtFileShareIn.setMovementMethod(LinkMovementMethod.getInstance());
                }
            } else {
                this.mTxtFileShareIn.setMovementMethod(null);
            }
        }
        C38401 r14 = new OnClickListener() {
            public void onClick(View view) {
                int id = view.getId();
                if (id == C4558R.C4560id.imgShare) {
                    if (MMZoomFileView.this.mOnClickOperatorListener != null) {
                        MMZoomFileView.this.mOnClickOperatorListener.onZoomFileShared(MMZoomFileView.this.mFile.getWebID());
                    }
                } else if (id == C4558R.C4560id.btnCancel && MMZoomFileView.this.mOnClickOperatorListener != null) {
                    MMZoomFileView.this.mOnClickOperatorListener.onZoomFileCancelTransfer(MMZoomFileView.this.mFile.getWebID());
                }
            }
        };
        this.mBtnCancel.setOnClickListener(r14);
        this.mImgShare.setVisibility((!mMZoomFile.isPending() || mMZoomFile.isFileDownloading()) ? 0 : 8);
        if (mMZoomFile.isPending() || mMZoomFile.isFileDownloading()) {
            this.mProgressBarPending.setVisibility(0);
            this.mProgressBarPending.setProgress(mMZoomFile.getRatio());
            this.mBtnCancel.setVisibility(0);
            this.mImgShare.setVisibility(8);
            this.mPanelTranslate.setVisibility(0);
            this.mTxtFileOwner.setVisibility(8);
            this.mTxtFileShareIn.setVisibility(8);
            this.mTxtTranslateSpeed.setText(context.getString(C4558R.string.zm_lbl_translate_speed, new Object[]{FileUtils.toFileSizeString(context, (long) mMZoomFile.getCompleteSize()), FileUtils.toFileSizeString(context, (long) mMZoomFile.getFileSize()), FileUtils.toFileSizeString(context, (long) mMZoomFile.getBitPerSecond())}));
            this.mImgPendingType.setVisibility((!mMZoomFile.isPending() || mMZoomFile.isFileDownloading()) ? 8 : 0);
        } else {
            this.mProgressBarPending.setVisibility(8);
            this.mBtnCancel.setVisibility(8);
            this.mImgShare.setVisibility(0);
            this.mPanelTranslate.setVisibility(8);
            this.mTxtFileOwner.setVisibility(0);
            this.mTxtFileShareIn.setVisibility(0);
        }
        if (isFileTransferDisabled) {
            this.mImgShare.setVisibility(8);
            return;
        }
        this.mImgShare.setVisibility(0);
        this.mImgShare.setOnClickListener(r14);
    }

    private String formatShareeName(String str) {
        CharSequence ellipsize = TextUtils.ellipsize(str, this.mTxtFileShareIn.getPaint(), (float) UIUtil.dip2px(getContext(), 200.0f), TruncateAt.END);
        if (ellipsize != null) {
            return ellipsize.toString();
        }
        return null;
    }

    private String formatSharedTime(long j) {
        int dateDiff = TimeUtil.dateDiff(j, System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a", CompatUtils.getLocalDefault());
        Date date = new Date(j);
        String format = simpleDateFormat.format(date);
        if (dateDiff == 0) {
            return getContext().getString(C4558R.string.zm_lbl_content_time_today_format, new Object[]{format});
        }
        String format2 = new SimpleDateFormat("MMM d", CompatUtils.getLocalDefault()).format(date);
        return getContext().getString(C4558R.string.zm_lbl_content_time_other_day_format, new Object[]{format2, format});
    }
}

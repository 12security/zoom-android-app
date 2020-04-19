package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zipow.videobox.tempbean.IMessageTemplateAttachmentDescription;
import com.zipow.videobox.tempbean.IMessageTemplateAttachmentInfo;
import com.zipow.videobox.tempbean.IMessageTemplateAttachmentTitle;
import com.zipow.videobox.tempbean.IMessageTemplateAttachments;
import com.zipow.videobox.tempbean.IMessageTemplateBase;
import com.zipow.videobox.tempbean.IMessageTemplateTextStyle;
import com.zipow.videobox.util.ZMGlideUtil;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.AndroidLifecycleUtils;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMMessageTemplateAttachmentsView */
public class MMMessageTemplateAttachmentsView extends LinearLayout {
    private LinearLayout attachmentGroup;
    private TextView sizeTxt;

    public MMMessageTemplateAttachmentsView(Context context) {
        super(context);
        initView(context);
    }

    public MMMessageTemplateAttachmentsView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public MMMessageTemplateAttachmentsView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    @RequiresApi(api = 21)
    public MMMessageTemplateAttachmentsView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, C4558R.layout.zm_mm_message_template_attachments, this);
        this.attachmentGroup = (LinearLayout) findViewById(C4558R.C4560id.attachments_group);
        this.sizeTxt = (TextView) findViewById(C4558R.C4560id.attachments_size);
    }

    public void setData(@Nullable List<IMessageTemplateAttachments> list) {
        if (list == null || list.isEmpty()) {
            setVisibility(8);
            return;
        }
        setVisibility(0);
        LinearLayout linearLayout = this.attachmentGroup;
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
        for (IMessageTemplateAttachments iMessageTemplateAttachments : list) {
            if (iMessageTemplateAttachments.isSupportItem()) {
                addAttachments(iMessageTemplateAttachments);
            } else {
                addUnsupportITem(iMessageTemplateAttachments);
            }
        }
        this.sizeTxt.setText(getResources().getQuantityString(C4558R.plurals.zm_mm_template_attachments_68416, list.size(), new Object[]{Integer.valueOf(list.size())}));
    }

    private void addUnsupportITem(@Nullable IMessageTemplateBase iMessageTemplateBase) {
        if (iMessageTemplateBase != null && !iMessageTemplateBase.isSupportItem()) {
            View inflate = LayoutInflater.from(getContext()).inflate(C4558R.layout.zm_mm_message_template_unsupport, this, false);
            TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.unsupport);
            if (textView != null) {
                textView.setText(iMessageTemplateBase.getFall_back());
            }
            this.attachmentGroup.addView(inflate);
        }
    }

    private void addAttachments(@Nullable final IMessageTemplateAttachments iMessageTemplateAttachments) {
        View view;
        if (iMessageTemplateAttachments != null && this.attachmentGroup != null) {
            if (TextUtils.isEmpty(iMessageTemplateAttachments.getImg_url())) {
                view = LayoutInflater.from(getContext()).inflate(C4558R.layout.zm_mm_message_template_attachments_item, this, false);
            } else {
                view = LayoutInflater.from(getContext()).inflate(C4558R.layout.zm_mm_message_template_attachments_img_item, this, false);
                final ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.attachments_img);
                final View findViewById = view.findViewById(C4558R.C4560id.attachments_img_content);
                if (AndroidLifecycleUtils.canLoadImage(getContext())) {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.fitCenter().dontAnimate().diskCacheStrategy(DiskCacheStrategy.DATA);
                    ZMGlideUtil.getGlideRequestManager(getContext()).setDefaultRequestOptions(requestOptions).asBitmap().override(Integer.MIN_VALUE).listener((RequestListener) new RequestListener<Bitmap>() {
                        public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                            return false;
                        }

                        public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
                            imageView.setVisibility(0);
                            findViewById.setBackgroundResource(C4558R.C4559drawable.zm_msg_template_attachments_img_bg);
                            return false;
                        }
                    }).load(iMessageTemplateAttachments.getImg_url()).into(imageView);
                }
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.attachments_file_name);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.attachments_file_sub);
            ImageView imageView2 = (ImageView) view.findViewById(C4558R.C4560id.img);
            TextView textView3 = (TextView) view.findViewById(C4558R.C4560id.attachments_file_description);
            view.setOnClickListener(new OnClickListener() {
                public void onClick(@NonNull View view) {
                    UIUtil.openURL(view.getContext(), iMessageTemplateAttachments.getResource_url());
                }
            });
            String ext = iMessageTemplateAttachments.getExt();
            IMessageTemplateAttachmentInfo information = iMessageTemplateAttachments.getInformation();
            if (information != null) {
                IMessageTemplateAttachmentTitle title = information.getTitle();
                if (title != null) {
                    IMessageTemplateTextStyle style = title.getStyle();
                    if (style != null) {
                        style.applyStyle(textView);
                    }
                    StringBuilder sb = new StringBuilder(title.getText() == null ? "" : title.getText());
                    if (!TextUtils.isEmpty(ext)) {
                        ext = ext.toLowerCase(Locale.US);
                        if (ext.charAt(0) != '.') {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(".");
                            sb2.append(ext);
                            ext = sb2.toString();
                        }
                        sb.append(ext);
                    } else if (!TextUtils.isEmpty(title.getText())) {
                        String[] split = title.getText().split("\\.");
                        if (split.length > 1) {
                            ext = split[split.length - 1];
                        }
                    }
                    textView.setText(sb.toString());
                }
                IMessageTemplateAttachmentDescription description = information.getDescription();
                if (description == null || TextUtils.isEmpty(description.getText())) {
                    textView3.setVisibility(8);
                } else {
                    textView3.setVisibility(0);
                    IMessageTemplateTextStyle style2 = description.getStyle();
                    if (style2 != null) {
                        style2.applyStyle(textView3);
                    }
                    textView3.setText(description.getText());
                }
            }
            imageView2.setImageResource(AndroidAppUtil.getIconByExt(ext));
            long size = iMessageTemplateAttachments.getSize();
            if (size >= 0) {
                textView2.setVisibility(0);
                textView2.setText(FileUtils.toTemplateFileSizeString(getContext(), size));
            } else {
                textView2.setVisibility(8);
            }
            if (this.attachmentGroup.getChildCount() > 0) {
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                layoutParams.topMargin = UIUtil.dip2px(getContext(), 8.0f);
                view.setLayoutParams(layoutParams);
            }
            this.attachmentGroup.addView(view);
        }
    }
}

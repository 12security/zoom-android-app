package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zipow.videobox.markdown.MarkDownUtils;
import com.zipow.videobox.markdown.URLImageParser.OnUrlDrawableUpdateListener;
import com.zipow.videobox.tempbean.IMessageTemplateActionItem;
import com.zipow.videobox.tempbean.IMessageTemplateBase;
import com.zipow.videobox.tempbean.IMessageTemplateExtendMessage;
import com.zipow.videobox.tempbean.IMessageTemplateSection;
import com.zipow.videobox.tempbean.IMessageTemplateSettings;
import com.zipow.videobox.util.TintUtil;
import com.zipow.videobox.util.ZMGlideUtil;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickTemplateListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMMessageTemplateItemView.OnClickTemplateActionMoreListener;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMMessageTemplateSectionView */
public class MMMessageTemplateSectionView extends AbsMessageView {
    /* access modifiers changed from: private */
    public ImageView mFooterImg;
    private LinearLayout mFooterLinear;
    /* access modifiers changed from: private */
    public TextView mFooterTxt;
    /* access modifiers changed from: private */
    @Nullable
    public MMMessageItem mMessageItem;
    private ImageView mSideBar;
    private MMMessageTemplateItemView messageView;
    private LinearLayout sectionLinear;
    private LinearLayout sectionUnsupportLinear;
    private TextView unSupportSectionTxt;

    public void setMessageItem(MMMessageItem mMMessageItem) {
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
    }

    public MMMessageTemplateSectionView(Context context) {
        super(context);
        initView(context);
    }

    public MMMessageTemplateSectionView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public MMMessageTemplateSectionView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public Rect getMessageLocationOnScreen() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        return new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), iArr[1] + getHeight());
    }

    private void initView(Context context) {
        inflate(context, C4558R.layout.zm_mm_message_template_section, this);
        this.mFooterLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_mm_footer_linear);
        this.mFooterImg = (ImageView) findViewById(C4558R.C4560id.zm_mm_footer_img);
        this.mFooterTxt = (TextView) findViewById(C4558R.C4560id.zm_mm_footer_txt);
        this.messageView = (MMMessageTemplateItemView) findViewById(C4558R.C4560id.zm_msg_messages);
        this.sectionLinear = (LinearLayout) findViewById(C4558R.C4560id.template_section_linear);
        this.sectionUnsupportLinear = (LinearLayout) findViewById(C4558R.C4560id.template_section_unsupport_linear);
        this.unSupportSectionTxt = (TextView) findViewById(C4558R.C4560id.template_section_unsupport_text);
        this.mSideBar = (ImageView) findViewById(C4558R.C4560id.zm_msg_side_bar);
    }

    public void setData(@Nullable MMMessageItem mMMessageItem, @Nullable IMessageTemplateSection iMessageTemplateSection, @Nullable IMessageTemplateSettings iMessageTemplateSettings) {
        if (iMessageTemplateSection != null && mMMessageItem != null) {
            this.mMessageItem = mMMessageItem;
            if (iMessageTemplateSection.isSupportItem()) {
                this.sectionLinear.setVisibility(0);
                this.sectionUnsupportLinear.setVisibility(8);
                List sections = iMessageTemplateSection.getSections();
                if (CollectionsUtil.isListEmpty(sections)) {
                    this.messageView.removeAllViews();
                    this.messageView.setVisibility(4);
                    setSideBar(null, null, false);
                } else {
                    setMessage(sections);
                    if (iMessageTemplateSettings != null) {
                        setSideBar(iMessageTemplateSettings.getDefault_sidebar_color(), iMessageTemplateSection.getSidebar_color(), iMessageTemplateSettings.isIs_split_sidebar());
                    } else {
                        setSideBar(null, null, false);
                    }
                }
                if (iMessageTemplateSection.isSupportFootItem()) {
                    setFooter(iMessageTemplateSection.getFooter(), iMessageTemplateSection.getFooter_icon(), iMessageTemplateSection.getTs(), iMessageTemplateSection.getExtendMessages());
                } else {
                    this.mFooterLinear.setVisibility(0);
                    this.mFooterTxt.setText(iMessageTemplateSection.getFooter_fall_back());
                    this.mFooterImg.setVisibility(8);
                }
                this.messageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        OnClickMessageListener onClickMessageListener = MMMessageTemplateSectionView.this.getOnClickMessageListener();
                        if (onClickMessageListener != null) {
                            onClickMessageListener.onClickMessage(MMMessageTemplateSectionView.this.mMessageItem);
                        }
                    }
                });
                this.messageView.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        OnShowContextMenuListener onShowContextMenuListener = MMMessageTemplateSectionView.this.getOnShowContextMenuListener();
                        if (onShowContextMenuListener != null) {
                            onShowContextMenuListener.onShowContextMenu(view, MMMessageTemplateSectionView.this.mMessageItem);
                        }
                        return false;
                    }
                });
            } else {
                this.sectionUnsupportLinear.setVisibility(0);
                this.sectionLinear.setVisibility(8);
                this.unSupportSectionTxt.setText(iMessageTemplateSection.getFall_back());
            }
        }
    }

    private void setFooter(String str, String str2, long j, @Nullable List<IMessageTemplateExtendMessage> list) {
        if (this.mFooterTxt != null && this.mFooterImg != null && this.mFooterLinear != null) {
            if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2) || j > 0) {
                int i = 0;
                this.mFooterLinear.setVisibility(0);
                if (!CollectionsUtil.isListEmpty(list)) {
                    this.mFooterTxt.setMovementMethod(LinkMovementMethod.getInstance());
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    IMessageTemplateExtendMessage iMessageTemplateExtendMessage = new IMessageTemplateExtendMessage();
                    iMessageTemplateExtendMessage.setText(OAuth.SCOPE_DELIMITER);
                    while (i < list.size()) {
                        int i2 = i + 1;
                        ((IMessageTemplateExtendMessage) list.get(i)).emitter(spannableStringBuilder, this.mFooterTxt, i2 >= list.size() ? iMessageTemplateExtendMessage : (IMessageTemplateExtendMessage) list.get(i2), new OnUrlDrawableUpdateListener() {
                            public void onUrlDrawableUpdate() {
                                MMMessageTemplateSectionView.this.mFooterTxt.invalidate();
                            }
                        });
                        i = i2;
                    }
                    if (j > 0) {
                        if (spannableStringBuilder.length() > 0) {
                            spannableStringBuilder.append("  ").append(TimeUtil.formatTemplateDateTime(getContext(), j));
                        } else {
                            spannableStringBuilder.append(TimeUtil.formatTemplateDateTime(getContext(), j));
                        }
                    }
                    this.mFooterTxt.setText(spannableStringBuilder);
                } else if (!TextUtils.isEmpty(str)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    if (j > 0) {
                        sb.append("  ");
                        sb.append(TimeUtil.formatTemplateDateTime(getContext(), j));
                    }
                    this.mFooterTxt.setText(sb.toString());
                } else if (j > 0) {
                    this.mFooterTxt.setText(TimeUtil.formatTemplateDateTime(getContext(), j));
                } else {
                    this.mFooterTxt.setText("");
                }
                MarkDownUtils.addAutoLink(this.mFooterTxt);
                this.mFooterImg.setVisibility(8);
                if (!TextUtils.isEmpty(str2)) {
                    ZMGlideUtil.load(getContext(), this.mFooterImg, (Object) str2, (RequestListener) new RequestListener() {
                        public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target target, boolean z) {
                            MMMessageTemplateSectionView.this.mFooterImg.setVisibility(8);
                            return false;
                        }

                        public boolean onResourceReady(Object obj, Object obj2, Target target, DataSource dataSource, boolean z) {
                            MMMessageTemplateSectionView.this.mFooterImg.setVisibility(0);
                            return false;
                        }
                    });
                }
            } else {
                this.mFooterLinear.setVisibility(8);
            }
        }
    }

    private void setSideBar(String str, String str2, boolean z) {
        ImageView imageView = this.mSideBar;
        if (imageView != null) {
            if (z) {
                imageView.setVisibility(0);
                if (!TextUtils.isEmpty(str2)) {
                    str = str2;
                } else if (TextUtils.isEmpty(str)) {
                    str = null;
                }
                setSideBarColor(str);
            } else {
                imageView.setVisibility(8);
            }
        }
    }

    private void setSideBarColor(String str) {
        if (this.mSideBar != null) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), C4558R.C4559drawable.zm_mm_template_side_bar);
            if (TextUtils.isEmpty(str)) {
                int color = ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_blue_0E71EB);
                if (drawable != null) {
                    this.mSideBar.setBackgroundDrawable(TintUtil.tintColor(drawable, color));
                }
                return;
            }
            if (drawable != null) {
                try {
                    this.mSideBar.setBackgroundDrawable(TintUtil.tintColor(drawable, Color.parseColor(str)));
                } catch (Exception unused) {
                    if (drawable != null) {
                        if ("orange".equalsIgnoreCase(str)) {
                            this.mSideBar.setBackgroundDrawable(TintUtil.tintColor(drawable, Color.parseColor("#FFA500")));
                        } else {
                            this.mSideBar.setBackgroundDrawable(TintUtil.tintColor(drawable, ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_blue_0E71EB)));
                        }
                    }
                }
            }
        }
    }

    private void setMessage(List<IMessageTemplateBase> list) {
        MMMessageTemplateItemView mMMessageTemplateItemView = this.messageView;
        if (mMMessageTemplateItemView != null) {
            mMMessageTemplateItemView.setData(this.mMessageItem, list);
            this.messageView.setmOnClickTemplateActionMoreListener(new OnClickTemplateActionMoreListener() {
                public void onClickTemplateActionMore(View view, String str, String str2, List<IMessageTemplateActionItem> list) {
                    AbsMessageView.OnClickTemplateActionMoreListener onClickTemplateActionMoreListener = MMMessageTemplateSectionView.this.getmOnClickTemplateActionMoreListener();
                    if (onClickTemplateActionMoreListener != null) {
                        onClickTemplateActionMoreListener.onClickTemplateActionMore(view, str, str2, list);
                    }
                }
            });
            this.messageView.setmEditTemplateListener(new OnclickTemplateListener() {
                public void onEditTemplate(String str, String str2, String str3) {
                    OnClickTemplateListener onClickTemplateListener = MMMessageTemplateSectionView.this.getmOnClickTemplateListener();
                    if (onClickTemplateListener != null) {
                        onClickTemplateListener.onClickEditTemplate(str, str2, str3);
                    }
                }

                public void onTemplateSelectClick(String str, String str2) {
                    OnClickTemplateListener onClickTemplateListener = MMMessageTemplateSectionView.this.getmOnClickTemplateListener();
                    if (onClickTemplateListener != null) {
                        onClickTemplateListener.onClickSelectTemplate(str, str2);
                    }
                }
            });
        }
    }

    @Nullable
    public MMMessageItem getMessageItem() {
        return this.mMessageItem;
    }
}

package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import com.zipow.videobox.markdown.MarkDownUtils;
import com.zipow.videobox.markdown.URLImageParser.OnUrlDrawableUpdateListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.tempbean.IMessageTemplateActionItem;
import com.zipow.videobox.tempbean.IMessageTemplateActions;
import com.zipow.videobox.tempbean.IMessageTemplateAttachments;
import com.zipow.videobox.tempbean.IMessageTemplateBase;
import com.zipow.videobox.tempbean.IMessageTemplateExtendMessage;
import com.zipow.videobox.tempbean.IMessageTemplateFieldItem;
import com.zipow.videobox.tempbean.IMessageTemplateFields;
import com.zipow.videobox.tempbean.IMessageTemplateImage;
import com.zipow.videobox.tempbean.IMessageTemplateMessage;
import com.zipow.videobox.tempbean.IMessageTemplateSelect;
import com.zipow.videobox.tempbean.IMessageTemplateSelectItem;
import com.zipow.videobox.tempbean.IMessageTemplateTextStyle;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMMessageTemplateItemView */
public class MMMessageTemplateItemView extends LinearLayout {
    private static final int FIELDS_COLUMN_COUNT = 2;
    /* access modifiers changed from: private */
    public OnclickTemplateListener mEditTemplateListener;
    /* access modifiers changed from: private */
    public OnClickTemplateActionMoreListener mOnClickTemplateActionMoreListener;
    /* access modifiers changed from: private */
    public MMMessageItem mmMessageItem;

    /* renamed from: com.zipow.videobox.view.mm.MMMessageTemplateItemView$OnClickTemplateActionMoreListener */
    public interface OnClickTemplateActionMoreListener {
        void onClickTemplateActionMore(View view, String str, String str2, List<IMessageTemplateActionItem> list);
    }

    public MMMessageTemplateItemView(Context context) {
        super(context);
        initView(context);
    }

    public MMMessageTemplateItemView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public MMMessageTemplateItemView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    @RequiresApi(api = 21)
    public MMMessageTemplateItemView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(1);
    }

    public void setmEditTemplateListener(OnclickTemplateListener onclickTemplateListener) {
        this.mEditTemplateListener = onclickTemplateListener;
    }

    public void setmOnClickTemplateActionMoreListener(OnClickTemplateActionMoreListener onClickTemplateActionMoreListener) {
        this.mOnClickTemplateActionMoreListener = onClickTemplateActionMoreListener;
    }

    public void setData(MMMessageItem mMMessageItem, @Nullable List<IMessageTemplateBase> list) {
        if (list == null || list.isEmpty()) {
            setVisibility(8);
            return;
        }
        int i = 0;
        setVisibility(0);
        this.mmMessageItem = mMMessageItem;
        removeAllViews();
        ArrayList arrayList = new ArrayList();
        while (i < list.size()) {
            IMessageTemplateBase iMessageTemplateBase = (IMessageTemplateBase) list.get(i);
            IMessageTemplateBase iMessageTemplateBase2 = null;
            IMessageTemplateBase iMessageTemplateBase3 = i > 0 ? (IMessageTemplateBase) list.get(i - 1) : null;
            i++;
            if (i < list.size()) {
                iMessageTemplateBase2 = (IMessageTemplateBase) list.get(i);
            }
            if (!iMessageTemplateBase.isSupportItem()) {
                addUnsupportITem(iMessageTemplateBase);
            } else if (iMessageTemplateBase instanceof IMessageTemplateMessage) {
                addMessageItem((IMessageTemplateMessage) iMessageTemplateBase);
            } else if (iMessageTemplateBase instanceof IMessageTemplateFields) {
                IMessageTemplateFields iMessageTemplateFields = (IMessageTemplateFields) iMessageTemplateBase;
                List items = iMessageTemplateFields.getItems();
                if (items != null) {
                    addFieldsView(2, iMessageTemplateFields.getEvent_id(), items);
                }
            } else if (iMessageTemplateBase instanceof IMessageTemplateSelect) {
                addSelectItem((IMessageTemplateSelect) iMessageTemplateBase);
            } else if (iMessageTemplateBase instanceof IMessageTemplateAttachments) {
                if (!(iMessageTemplateBase3 instanceof IMessageTemplateAttachments)) {
                    arrayList = new ArrayList();
                }
                arrayList.add((IMessageTemplateAttachments) iMessageTemplateBase);
                if (!(iMessageTemplateBase2 instanceof IMessageTemplateAttachments)) {
                    addAttachments(arrayList);
                }
            } else if (iMessageTemplateBase instanceof IMessageTemplateActions) {
                addActions((IMessageTemplateActions) iMessageTemplateBase);
            } else {
                boolean z = iMessageTemplateBase instanceof IMessageTemplateImage;
            }
        }
    }

    private void addUnsupportITem(@Nullable IMessageTemplateBase iMessageTemplateBase) {
        if (iMessageTemplateBase != null && !iMessageTemplateBase.isSupportItem()) {
            View inflate = LayoutInflater.from(getContext()).inflate(C4558R.layout.zm_mm_message_template_unsupport, this, false);
            TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.unsupport);
            if (textView != null) {
                textView.setText(iMessageTemplateBase.getFall_back());
            }
            addView(inflate);
        }
    }

    private void addActions(@Nullable IMessageTemplateActions iMessageTemplateActions) {
        if (iMessageTemplateActions != null && getContext() != null && !CollectionsUtil.isListEmpty(iMessageTemplateActions.getItems())) {
            MMMessageTemplateActionsView mMMessageTemplateActionsView = new MMMessageTemplateActionsView(getContext());
            LayoutParams layoutParams = new LayoutParams(-1, -2);
            layoutParams.leftMargin = UIUtil.dip2px(getContext(), 12.0f);
            layoutParams.rightMargin = UIUtil.dip2px(getContext(), 12.0f);
            if (getChildCount() > 0) {
                layoutParams.topMargin = UIUtil.dip2px(getContext(), 16.0f);
            }
            mMMessageTemplateActionsView.setData(this.mmMessageItem, iMessageTemplateActions);
            mMMessageTemplateActionsView.setmOnClickTemplateActionMoreListener(new com.zipow.videobox.view.p014mm.MMMessageTemplateActionsView.OnClickTemplateActionMoreListener() {
                public void onClickTemplateActionMore(View view, String str, String str2, List<IMessageTemplateActionItem> list) {
                    if (MMMessageTemplateItemView.this.mOnClickTemplateActionMoreListener != null) {
                        MMMessageTemplateItemView.this.mOnClickTemplateActionMoreListener.onClickTemplateActionMore(view, str, str2, list);
                    }
                }
            });
            addView(mMMessageTemplateActionsView, layoutParams);
        }
    }

    private void addAttachments(@Nullable List<IMessageTemplateAttachments> list) {
        if (!CollectionsUtil.isListEmpty(list) && getContext() != null) {
            MMMessageTemplateAttachmentsView mMMessageTemplateAttachmentsView = new MMMessageTemplateAttachmentsView(getContext());
            LayoutParams layoutParams = new LayoutParams(-1, -2);
            layoutParams.leftMargin = UIUtil.dip2px(getContext(), 12.0f);
            layoutParams.rightMargin = UIUtil.dip2px(getContext(), 12.0f);
            if (getChildCount() > 0) {
                layoutParams.topMargin = UIUtil.dip2px(getContext(), 16.0f);
            }
            mMMessageTemplateAttachmentsView.setData(list);
            addView(mMMessageTemplateAttachmentsView, layoutParams);
        }
    }

    private void addImageItem(@Nullable IMessageTemplateImage iMessageTemplateImage) {
        if (iMessageTemplateImage != null) {
            LayoutParams layoutParams = new LayoutParams(-2, -2);
            layoutParams.leftMargin = UIUtil.dip2px(getContext(), 12.0f);
            layoutParams.rightMargin = UIUtil.dip2px(getContext(), 12.0f);
            if (getChildCount() > 0) {
                layoutParams.topMargin = UIUtil.dip2px(getContext(), 7.0f);
            }
        }
    }

    private void addSelectItem(@Nullable final IMessageTemplateSelect iMessageTemplateSelect) {
        if (iMessageTemplateSelect != null) {
            LayoutParams layoutParams = new LayoutParams(-2, -2);
            layoutParams.leftMargin = UIUtil.dip2px(getContext(), 12.0f);
            layoutParams.rightMargin = UIUtil.dip2px(getContext(), 12.0f);
            if (getChildCount() > 0) {
                layoutParams.topMargin = UIUtil.dip2px(getContext(), 7.0f);
            }
            View inflate = inflate(getContext(), C4558R.layout.zm_mm_message_template_select, null);
            TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.select_message);
            TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.value);
            ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.arrow);
            ProgressBar progressBar = (ProgressBar) inflate.findViewById(C4558R.C4560id.progressBar);
            textView.setText(iMessageTemplateSelect.getText());
            if (iMessageTemplateSelect.getStyle() != null) {
                iMessageTemplateSelect.getStyle().applyStyle(textView);
            }
            if (iMessageTemplateSelect.isProgressing()) {
                imageView.setVisibility(8);
                progressBar.setVisibility(0);
            } else {
                imageView.setVisibility(0);
                progressBar.setVisibility(8);
            }
            List selectedItems = iMessageTemplateSelect.getSelectedItems();
            if (selectedItems == null || selectedItems.isEmpty()) {
                textView2.setTextColor(ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_gray_747487));
                textView2.setText(C4558R.string.zm_mm_template_drop_down_value_68416);
            } else {
                IMessageTemplateSelectItem iMessageTemplateSelectItem = (IMessageTemplateSelectItem) selectedItems.get(0);
                if (iMessageTemplateSelectItem != null) {
                    textView2.setTextColor(ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_black_232333));
                    if (TextUtils.isEmpty(iMessageTemplateSelectItem.getText())) {
                        textView2.setText(iMessageTemplateSelectItem.getValue());
                    } else {
                        textView2.setText(iMessageTemplateSelectItem.getText());
                    }
                }
            }
            inflate.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (MMMessageTemplateItemView.this.mEditTemplateListener != null) {
                        MMMessageTemplateItemView.this.mEditTemplateListener.onTemplateSelectClick(MMMessageTemplateItemView.this.mmMessageItem.messageXMPPId, iMessageTemplateSelect.getEvent_id());
                    }
                }
            });
            addView(inflate, layoutParams);
        }
    }

    private void addFieldsView(int i, String str, @Nullable List<IMessageTemplateFieldItem> list) {
        if (!CollectionsUtil.isListEmpty(list)) {
            LinearLayout linearLayout = null;
            for (int i2 = 0; i2 < list.size(); i2++) {
                IMessageTemplateFieldItem iMessageTemplateFieldItem = (IMessageTemplateFieldItem) list.get(i2);
                if (iMessageTemplateFieldItem != null) {
                    if (!iMessageTemplateFieldItem.isShorts()) {
                        if (linearLayout != null) {
                            addRowView(linearLayout, i);
                            linearLayout = null;
                        }
                        addColumnItem(this, str, iMessageTemplateFieldItem);
                    } else if (linearLayout == null) {
                        linearLayout = generateRowLinear(getContext());
                        if (linearLayout != null) {
                            addColumnItem(linearLayout, str, iMessageTemplateFieldItem);
                        }
                    } else {
                        if (linearLayout.getChildCount() >= i) {
                            addRowView(linearLayout, i);
                            linearLayout = generateRowLinear(getContext());
                        }
                        if (linearLayout != null) {
                            addColumnItem(linearLayout, str, iMessageTemplateFieldItem);
                        }
                    }
                }
            }
            if (linearLayout != null) {
                addRowView(linearLayout, i);
            }
        }
    }

    @Nullable
    private LinearLayout generateRowLinear(@Nullable Context context) {
        if (context == null) {
            return null;
        }
        LinearLayout linearLayout = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.topMargin = UIUtil.dip2px(getContext(), 8.0f);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    private void addRowView(@Nullable LinearLayout linearLayout, int i) {
        if (linearLayout != null) {
            int childCount = linearLayout.getChildCount();
            if (childCount < i) {
                for (int i2 = 0; i2 < i - childCount; i2++) {
                    LayoutParams layoutParams = new LayoutParams(-1, -2);
                    layoutParams.weight = 1.0f;
                    linearLayout.addView(new View(getContext()), layoutParams);
                }
            }
            addView(linearLayout);
        }
    }

    private void addColumnItem(@Nullable ViewGroup viewGroup, String str, @Nullable IMessageTemplateFieldItem iMessageTemplateFieldItem) {
        boolean z;
        ViewGroup viewGroup2 = viewGroup;
        final IMessageTemplateFieldItem iMessageTemplateFieldItem2 = iMessageTemplateFieldItem;
        if (iMessageTemplateFieldItem2 != null && viewGroup2 != null) {
            View inflate = LayoutInflater.from(getContext()).inflate(C4558R.layout.zm_mm_message_template_fields_item, viewGroup2, false);
            final TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.value);
            final TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.extendValue);
            ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.edit);
            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(C4558R.C4560id.fields_normal_linear);
            LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(C4558R.C4560id.fields_extend_linear);
            final TextView textView3 = (TextView) inflate.findViewById(C4558R.C4560id.showMore);
            TextView textView4 = (TextView) inflate.findViewById(C4558R.C4560id.showLess);
            ((TextView) inflate.findViewById(C4558R.C4560id.key)).setText(iMessageTemplateFieldItem.getKey());
            if (!TextUtils.isEmpty(iMessageTemplateFieldItem.getLink())) {
                linearLayout.setVisibility(8);
                linearLayout2.setVisibility(0);
                textView4.setVisibility(8);
                textView2.setMovementMethod(LinkMovementMethod.getInstance());
                SpannableString spannableString = new SpannableString(iMessageTemplateFieldItem.getValue());
                spannableString.setSpan(new ClickableSpan() {
                    public void onClick(@NonNull View view) {
                        UIUtil.openURL(MMMessageTemplateItemView.this.getContext(), iMessageTemplateFieldItem2.getLink());
                    }

                    public void updateDrawState(@NonNull TextPaint textPaint) {
                        textPaint.setColor(ContextCompat.getColor(MMMessageTemplateItemView.this.getContext(), C4558R.color.zm_template_link));
                        textPaint.setUnderlineText(false);
                    }
                }, 0, spannableString.length(), 33);
                textView2.setText(spannableString);
                z = false;
            } else {
                if (!CollectionsUtil.isListEmpty(iMessageTemplateFieldItem.getExtendMessages())) {
                    textView.setEllipsize(null);
                    textView2.setMovementMethod(LinkMovementMethod.getInstance());
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    int i = 0;
                    while (i < iMessageTemplateFieldItem.getExtendMessages().size()) {
                        int i2 = i + 1;
                        ((IMessageTemplateExtendMessage) iMessageTemplateFieldItem.getExtendMessages().get(i)).emitter(spannableStringBuilder, textView, i2 >= iMessageTemplateFieldItem.getExtendMessages().size() ? null : (IMessageTemplateExtendMessage) iMessageTemplateFieldItem.getExtendMessages().get(i2), new OnUrlDrawableUpdateListener() {
                            public void onUrlDrawableUpdate() {
                                textView.invalidate();
                                textView2.invalidate();
                            }
                        });
                        i = i2;
                    }
                    textView.setText(spannableStringBuilder);
                    textView2.setText(spannableStringBuilder);
                } else {
                    textView.setEllipsize(TruncateAt.END);
                    textView.setText(iMessageTemplateFieldItem.getValue());
                    textView2.setText(iMessageTemplateFieldItem.getValue());
                }
                MarkDownUtils.addAutoLink(textView);
                MarkDownUtils.addAutoLink(textView2);
                if (iMessageTemplateFieldItem.isEditable()) {
                    z = false;
                    imageView.setVisibility(0);
                    final String str2 = str;
                    imageView.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            if (MMMessageTemplateItemView.this.mEditTemplateListener != null) {
                                MMMessageTemplateItemView.this.mEditTemplateListener.onEditTemplate(MMMessageTemplateItemView.this.mmMessageItem.messageXMPPId, str2, iMessageTemplateFieldItem2.getKey());
                            }
                        }
                    });
                } else {
                    z = false;
                }
                if (iMessageTemplateFieldItem.isShowMoreExtend()) {
                    linearLayout.setVisibility(8);
                    linearLayout2.setVisibility(z);
                    textView4.setVisibility(z);
                } else if (iMessageTemplateFieldItem.isShowMore()) {
                    textView3.setVisibility(z ? 1 : 0);
                }
            }
            textView.setFocusable(z);
            textView.setClickable(z);
            textView2.setFocusable(z);
            textView2.setClickable(z);
            IMessageTemplateTextStyle style = iMessageTemplateFieldItem.getStyle();
            if (style != null) {
                style.applyStyle(textView);
                style.applyStyle(textView2);
            }
            if (iMessageTemplateFieldItem.isName() && CollectionsUtil.isListEmpty(iMessageTemplateFieldItem.getExtendMessages())) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        String screenName = myself.getScreenName();
                        if (!TextUtils.isEmpty(screenName) && !TextUtils.isEmpty(iMessageTemplateFieldItem.getValue()) && screenName.equals(iMessageTemplateFieldItem.getValue())) {
                            textView.setTextColor(ContextCompat.getColor(getContext(), C4558R.color.zm_template_fields_txt_light));
                            textView2.setTextColor(ContextCompat.getColor(getContext(), C4558R.color.zm_template_fields_txt_light));
                            if (textView.getText() instanceof Spannable) {
                                Spannable spannable = (Spannable) textView.getText();
                                spannable.setSpan(new BackgroundColorSpan(ContextCompat.getColor(getContext(), C4558R.color.zm_template_link)), 0, spannable.length(), 33);
                            } else {
                                SpannableString spannableString2 = new SpannableString(textView.getText());
                                spannableString2.setSpan(new BackgroundColorSpan(ContextCompat.getColor(getContext(), C4558R.color.zm_template_link)), 0, spannableString2.length(), 33);
                                textView.setText(spannableString2);
                            }
                            if (textView2.getText() instanceof Spannable) {
                                Spannable spannable2 = (Spannable) textView2.getText();
                                spannable2.setSpan(new BackgroundColorSpan(ContextCompat.getColor(getContext(), C4558R.color.zm_template_link)), 0, spannable2.length(), 33);
                            } else {
                                SpannableString spannableString3 = new SpannableString(textView2.getText());
                                spannableString3.setSpan(new BackgroundColorSpan(ContextCompat.getColor(getContext(), C4558R.color.zm_template_link)), 0, spannableString3.length(), 33);
                                textView2.setText(spannableString3);
                            }
                        }
                    }
                }
            }
            if (viewGroup2 instanceof MMMessageTemplateItemView) {
                LayoutParams layoutParams = (LayoutParams) inflate.getLayoutParams();
                layoutParams.topMargin = UIUtil.dip2px(getContext(), 8.0f);
                inflate.setLayoutParams(layoutParams);
            } else if (viewGroup.getChildCount() > 0) {
                LayoutParams layoutParams2 = (LayoutParams) inflate.getLayoutParams();
                layoutParams2.leftMargin = UIUtil.dip2px(getContext(), 6.0f);
                inflate.setLayoutParams(layoutParams2);
            }
            viewGroup2.addView(inflate);
            final LinearLayout linearLayout3 = linearLayout;
            final LinearLayout linearLayout4 = linearLayout2;
            final TextView textView5 = textView4;
            final IMessageTemplateFieldItem iMessageTemplateFieldItem3 = iMessageTemplateFieldItem;
            C37546 r0 = new OnClickListener() {
                public void onClick(View view) {
                    linearLayout3.setVisibility(8);
                    linearLayout4.setVisibility(0);
                    textView5.setVisibility(0);
                    iMessageTemplateFieldItem3.setShowMoreExtend(true);
                }
            };
            textView3.setOnClickListener(r0);
            final TextView textView6 = textView3;
            C37557 r02 = new OnClickListener() {
                public void onClick(View view) {
                    linearLayout3.setVisibility(0);
                    linearLayout4.setVisibility(8);
                    textView6.setVisibility(0);
                    iMessageTemplateFieldItem3.setShowMoreExtend(false);
                }
            };
            textView4.setOnClickListener(r02);
            inflate.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                public void onViewDetachedFromWindow(View view) {
                }

                public void onViewAttachedToWindow(@NonNull View view) {
                    if (!iMessageTemplateFieldItem2.isShowMore()) {
                        view.post(new Runnable() {
                            public void run() {
                                int lineCount = textView.getLineCount();
                                Layout layout = textView.getLayout();
                                if (layout != null && lineCount > 0) {
                                    if (lineCount > 4 || layout.getEllipsisCount(lineCount - 1) > 0) {
                                        textView3.setVisibility(0);
                                        iMessageTemplateFieldItem2.setShowMore(true);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void addMessageItem(@Nullable IMessageTemplateMessage iMessageTemplateMessage) {
        View view;
        LayoutParams layoutParams;
        boolean z;
        View view2;
        LayoutParams layoutParams2;
        final IMessageTemplateMessage iMessageTemplateMessage2 = iMessageTemplateMessage;
        if (iMessageTemplateMessage2 != null) {
            LayoutParams layoutParams3 = new LayoutParams(-2, -2);
            if (getChildCount() > 0) {
                layoutParams3.topMargin = UIUtil.dip2px(getContext(), 7.0f);
            }
            View inflate = inflate(getContext(), C4558R.layout.zm_mm_message_template_message_item, null);
            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(C4558R.C4560id.message_normal_linear);
            final TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.message);
            final TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.showMore);
            LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(C4558R.C4560id.message_expend_linear);
            final TextView textView3 = (TextView) inflate.findViewById(C4558R.C4560id.message_expend);
            TextView textView4 = (TextView) inflate.findViewById(C4558R.C4560id.showLess);
            ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.edit);
            if (!TextUtils.isEmpty(iMessageTemplateMessage.getLink())) {
                linearLayout.setVisibility(8);
                linearLayout2.setVisibility(0);
                textView4.setVisibility(8);
                textView3.setMovementMethod(LinkMovementMethod.getInstance());
                SpannableString spannableString = new SpannableString(iMessageTemplateMessage.getText());
                spannableString.setSpan(new ClickableSpan() {
                    public void onClick(@NonNull View view) {
                        UIUtil.openURL(MMMessageTemplateItemView.this.getContext(), iMessageTemplateMessage2.getLink());
                    }

                    public void updateDrawState(@NonNull TextPaint textPaint) {
                        textPaint.setColor(ContextCompat.getColor(MMMessageTemplateItemView.this.getContext(), C4558R.color.zm_template_link));
                        textPaint.setUnderlineText(false);
                    }
                }, 0, spannableString.length(), 33);
                textView3.setText(spannableString);
                layoutParams = layoutParams3;
                view = inflate;
                z = false;
            } else {
                if (!CollectionsUtil.isListEmpty(iMessageTemplateMessage.getExtendMessages())) {
                    textView.setEllipsize(null);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    textView3.setMovementMethod(LinkMovementMethod.getInstance());
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    int i = 0;
                    while (i < iMessageTemplateMessage.getExtendMessages().size()) {
                        int i2 = i + 1;
                        ((IMessageTemplateExtendMessage) iMessageTemplateMessage.getExtendMessages().get(i)).emitter(spannableStringBuilder, textView, i2 >= iMessageTemplateMessage.getExtendMessages().size() ? null : (IMessageTemplateExtendMessage) iMessageTemplateMessage.getExtendMessages().get(i2), new OnUrlDrawableUpdateListener() {
                            public void onUrlDrawableUpdate() {
                                textView.invalidate();
                                textView3.invalidate();
                            }
                        });
                        i = i2;
                    }
                    textView.setText(spannableStringBuilder);
                    textView3.setText(spannableStringBuilder);
                } else {
                    textView.setEllipsize(TruncateAt.END);
                    textView.setText(iMessageTemplateMessage.getText());
                    textView3.setText(iMessageTemplateMessage.getText());
                }
                MarkDownUtils.addAutoLink(textView);
                MarkDownUtils.addAutoLink(textView3);
                if (iMessageTemplateMessage.isShowMoreExtend()) {
                    linearLayout.setVisibility(8);
                    linearLayout2.setVisibility(0);
                    textView4.setVisibility(0);
                } else if (iMessageTemplateMessage.isShowMore()) {
                    textView2.setVisibility(0);
                }
                final LinearLayout linearLayout3 = linearLayout;
                layoutParams = layoutParams3;
                C374611 r8 = r0;
                final LinearLayout linearLayout4 = linearLayout2;
                view = inflate;
                final TextView textView5 = textView4;
                ImageView imageView2 = imageView;
                final IMessageTemplateMessage iMessageTemplateMessage3 = iMessageTemplateMessage;
                C374611 r0 = new OnClickListener() {
                    public void onClick(View view) {
                        linearLayout3.setVisibility(8);
                        linearLayout4.setVisibility(0);
                        textView5.setVisibility(0);
                        iMessageTemplateMessage3.setShowMoreExtend(true);
                    }
                };
                textView2.setOnClickListener(r8);
                final TextView textView6 = textView2;
                C374712 r02 = new OnClickListener() {
                    public void onClick(View view) {
                        linearLayout3.setVisibility(0);
                        linearLayout4.setVisibility(8);
                        textView6.setVisibility(0);
                        iMessageTemplateMessage3.setShowMoreExtend(false);
                    }
                };
                textView4.setOnClickListener(r02);
                if (iMessageTemplateMessage.isEditable()) {
                    z = false;
                    imageView2.setVisibility(0);
                    imageView2.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            if (MMMessageTemplateItemView.this.mEditTemplateListener != null) {
                                MMMessageTemplateItemView.this.mEditTemplateListener.onEditTemplate(MMMessageTemplateItemView.this.mmMessageItem.messageXMPPId, iMessageTemplateMessage2.getEvent_id(), null);
                            }
                        }
                    });
                } else {
                    z = false;
                }
            }
            textView.setFocusable(z);
            IMessageTemplateTextStyle style = iMessageTemplateMessage.getStyle();
            if (style != null) {
                style.applyStyle(textView);
                style.applyStyle(textView3);
                layoutParams2 = layoutParams;
                view2 = view;
            } else {
                layoutParams2 = layoutParams;
                view2 = view;
            }
            addView(view2, layoutParams2);
            if (!iMessageTemplateMessage.isShowMore()) {
                textView.post(new Runnable() {
                    public void run() {
                        int lineCount = textView.getLineCount();
                        Layout layout = textView.getLayout();
                        if (layout != null && lineCount > 0) {
                            if (lineCount > 4 || layout.getEllipsisCount(lineCount - 1) > 0) {
                                textView2.setVisibility(0);
                                iMessageTemplateMessage2.setShowMore(true);
                            }
                        }
                    }
                });
            }
        }
    }
}

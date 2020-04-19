package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomMessageTemplate;
import com.zipow.videobox.tempbean.IMessageTemplateActionItem;
import com.zipow.videobox.tempbean.IMessageTemplateActions;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMFlowLayout;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMMessageTemplateActionsView */
public class MMMessageTemplateActionsView extends ZMFlowLayout {
    private static final int MAX_BUTTON = 2;
    private LayoutInflater inflater;
    /* access modifiers changed from: private */
    public OnClickTemplateActionMoreListener mOnClickTemplateActionMoreListener;

    /* renamed from: com.zipow.videobox.view.mm.MMMessageTemplateActionsView$OnClickTemplateActionMoreListener */
    public interface OnClickTemplateActionMoreListener {
        void onClickTemplateActionMore(View view, String str, String str2, List<IMessageTemplateActionItem> list);
    }

    public void setmOnClickTemplateActionMoreListener(OnClickTemplateActionMoreListener onClickTemplateActionMoreListener) {
        this.mOnClickTemplateActionMoreListener = onClickTemplateActionMoreListener;
    }

    public MMMessageTemplateActionsView(Context context) {
        super(context);
        initViews(context);
    }

    public MMMessageTemplateActionsView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews(context);
    }

    public MMMessageTemplateActionsView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    private void initViews(Context context) {
        setFocusableInTouchMode(false);
        setFocusable(true);
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(@NonNull MMMessageItem mMMessageItem, @Nullable IMessageTemplateActions iMessageTemplateActions) {
        int i;
        if (iMessageTemplateActions != null && !CollectionsUtil.isListEmpty(iMessageTemplateActions.getItems())) {
            List items = iMessageTemplateActions.getItems();
            int limit = iMessageTemplateActions.getLimit();
            if (limit > 0) {
                if (limit != items.size()) {
                    limit--;
                }
                i = Math.min(limit, Math.min(2, items.size()));
            } else {
                i = Math.min(2, items.size());
            }
            for (int i2 = 0; i2 < i; i2++) {
                addSingleButton(mMMessageItem, (IMessageTemplateActionItem) items.get(i2), iMessageTemplateActions.getEvent_id());
            }
            if (items.size() > i) {
                addMoreButton(mMMessageItem, items.subList(i, items.size()), iMessageTemplateActions.getEvent_id());
            }
        }
    }

    private void addMoreButton(@NonNull final MMMessageItem mMMessageItem, final List<IMessageTemplateActionItem> list, final String str) {
        if (!CollectionsUtil.isListEmpty(list)) {
            View inflate = this.inflater.inflate(C4558R.layout.zm_mm_message_template_actions_more_btn, this, false);
            inflate.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (MMMessageTemplateActionsView.this.mOnClickTemplateActionMoreListener != null) {
                        MMMessageTemplateActionsView.this.mOnClickTemplateActionMoreListener.onClickTemplateActionMore(view, mMMessageItem.messageXMPPId, str, list);
                    }
                }
            });
            addView(inflate);
        }
    }

    private void addSingleButton(@NonNull final MMMessageItem mMMessageItem, final IMessageTemplateActionItem iMessageTemplateActionItem, final String str) {
        if (iMessageTemplateActionItem != null) {
            TextView textView = (TextView) this.inflater.inflate(C4558R.layout.zm_mm_message_template_actions_single_btn, this, false);
            if (getChildCount() > 0) {
                ((MarginLayoutParams) textView.getLayoutParams()).leftMargin = UIUtil.dip2px(getContext(), 8.0f);
            }
            iMessageTemplateActionItem.applayStyle(textView);
            textView.setText(iMessageTemplateActionItem.getText());
            textView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MMMessageTemplateActionsView.this.handleTempleActionMsg(mMMessageItem.sessionId, mMMessageItem.messageXMPPId, str, iMessageTemplateActionItem.getText(), iMessageTemplateActionItem.getValue());
                }
            });
            addView(textView);
        }
    }

    /* access modifiers changed from: private */
    public void handleTempleActionMsg(String str, String str2, String str3, String str4, String str5) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            ZoomMessageTemplate zoomMessageTemplate = PTApp.getInstance().getZoomMessageTemplate();
            if (zoomMessageTemplate != null) {
                zoomMessageTemplate.sendButtonCommand(str, str2, str3, str4, str5);
            }
        }
    }
}

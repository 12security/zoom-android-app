package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import com.zipow.videobox.tempbean.IMessageTemplateBase;
import com.zipow.videobox.tempbean.IMessageTemplateSection;
import com.zipow.videobox.tempbean.IMessageTemplateSettings;
import com.zipow.videobox.tempbean.IZoomMessageTemplate;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;

/* renamed from: com.zipow.videobox.view.mm.MMMessageTemplateSectionGroupView */
public class MMMessageTemplateSectionGroupView extends AbsMessageView {
    private void initView(Context context) {
    }

    public MMMessageItem getMessageItem() {
        return null;
    }

    public void setMessageItem(MMMessageItem mMMessageItem) {
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
    }

    public MMMessageTemplateSectionGroupView(Context context) {
        super(context);
        initView(context);
    }

    public MMMessageTemplateSectionGroupView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public MMMessageTemplateSectionGroupView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public Rect getMessageLocationOnScreen() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        return new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), iArr[1] + getHeight());
    }

    public void setData(@Nullable MMMessageItem mMMessageItem, @Nullable IZoomMessageTemplate iZoomMessageTemplate) {
        if (iZoomMessageTemplate != null && mMMessageItem != null) {
            List<IMessageTemplateBase> body = iZoomMessageTemplate.getBody();
            if (CollectionsUtil.isListEmpty(body)) {
                removeAllViews();
                return;
            }
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (IMessageTemplateBase iMessageTemplateBase : body) {
                if (iMessageTemplateBase != null) {
                    if (iMessageTemplateBase instanceof IMessageTemplateSection) {
                        arrayList.add((IMessageTemplateSection) iMessageTemplateBase);
                    } else {
                        arrayList2.add(iMessageTemplateBase);
                    }
                }
            }
            if (!CollectionsUtil.isListEmpty(arrayList2)) {
                IMessageTemplateSection iMessageTemplateSection = new IMessageTemplateSection();
                iMessageTemplateSection.setType("section");
                iMessageTemplateSection.setVersion(1);
                iMessageTemplateSection.setSections(arrayList2);
                arrayList.add(0, iMessageTemplateSection);
            }
            setDate(mMMessageItem, arrayList, iZoomMessageTemplate.getSettings());
        }
    }

    private void setDate(MMMessageItem mMMessageItem, @Nullable List<IMessageTemplateSection> list, IMessageTemplateSettings iMessageTemplateSettings) {
        if (!CollectionsUtil.isListEmpty(list)) {
            removeAllViews();
            for (IMessageTemplateSection iMessageTemplateSection : list) {
                if (iMessageTemplateSection != null) {
                    MMMessageTemplateSectionView mMMessageTemplateSectionView = new MMMessageTemplateSectionView(getContext());
                    mMMessageTemplateSectionView.setOnClickMessageListener(getOnClickMessageListener());
                    mMMessageTemplateSectionView.setOnShowContextMenuListener(getOnShowContextMenuListener());
                    mMMessageTemplateSectionView.setmOnClickTemplateListener(getmOnClickTemplateListener());
                    mMMessageTemplateSectionView.setmOnClickActionMoreListener(getmOnClickActionMoreListener());
                    mMMessageTemplateSectionView.setmOnClickTemplateActionMoreListener(getmOnClickTemplateActionMoreListener());
                    mMMessageTemplateSectionView.setData(mMMessageItem, iMessageTemplateSection, iMessageTemplateSettings);
                    addView(mMMessageTemplateSectionView);
                }
            }
        }
    }
}

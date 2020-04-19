package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMChatMessageBgDrawable */
public class MMChatMessageBgDrawable extends StateListDrawable {
    public static final int MSG_STATE_E2E_PENDING = 2;
    public static final int MSG_STATE_E2E_SUCCESS = 1;
    public static final int MSG_STATE_E2E_WARN = 3;
    public static final int MSG_STATE_IMAGE_PENDING = 4;
    public static final int MSG_STATE_PLAIN = 0;
    private boolean mBottomCorner;
    private Context mContext;
    private boolean mIsFrom;
    private int mMsgType;
    private boolean mNoTitle;

    public MMChatMessageBgDrawable(Context context, int i, boolean z, boolean z2) {
        this(context, i, z, z2, true);
    }

    public MMChatMessageBgDrawable(Context context, int i, boolean z, boolean z2, boolean z3) {
        this.mMsgType = i;
        this.mNoTitle = z;
        this.mContext = context;
        this.mIsFrom = z2;
        int dip2px = UIUtil.dip2px(this.mContext, 10.0f);
        int dip2px2 = UIUtil.dip2px(this.mContext, 16.0f);
        this.mBottomCorner = z3;
        init(dip2px2, dip2px, dip2px2, dip2px);
    }

    public MMChatMessageBgDrawable(Context context, int i, boolean z, boolean z2, int i2, int i3, int i4, int i5) {
        this.mMsgType = i;
        this.mNoTitle = z;
        this.mContext = context;
        this.mIsFrom = z2;
        this.mBottomCorner = true;
        init(i2, i3, i4, i5);
    }

    private void init(int i, int i2, int i3, int i4) {
        int i5;
        Context context = this.mContext;
        if (context != null) {
            Resources resources = context.getResources();
            if (resources != null) {
                switch (this.mMsgType) {
                    case 0:
                        if (!this.mIsFrom) {
                            i5 = resources.getColor(C4558R.color.zm_chat_msg_bg_e2e_success_normal_19884);
                            break;
                        } else {
                            i5 = resources.getColor(C4558R.color.zm_chat_msg_bg_plain_normal_19884);
                            break;
                        }
                    case 1:
                        i5 = resources.getColor(C4558R.color.zm_chat_msg_bg_e2e_success_normal_19884);
                        break;
                    case 2:
                        i5 = resources.getColor(C4558R.color.zm_chat_msg_bg_e2e_pending_normal);
                        break;
                    case 3:
                        i5 = resources.getColor(C4558R.color.zm_chat_msg_bg_e2e_warn_normal);
                        break;
                    case 4:
                        i5 = resources.getColor(C4558R.color.zm_chat_msg_bg_plain_normal_19884);
                        break;
                    default:
                        return;
                }
                int dip2px = UIUtil.dip2px(this.mContext, 10.0f);
                float[] fArr = new float[8];
                if (this.mBottomCorner) {
                    float f = (float) dip2px;
                    fArr[4] = f;
                    fArr[5] = f;
                    fArr[6] = f;
                    fArr[7] = f;
                } else {
                    fArr[4] = 0.0f;
                    fArr[5] = 0.0f;
                    fArr[6] = 0.0f;
                    fArr[7] = 0.0f;
                }
                float f2 = (float) dip2px;
                fArr[0] = f2;
                fArr[1] = f2;
                fArr[2] = f2;
                fArr[3] = f2;
                RoundRectShape roundRectShape = new RoundRectShape(fArr, null, null);
                ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
                shapeDrawable.getPaint().setColor(i5);
                shapeDrawable.setPadding(i, i2, i3, i4);
                ShapeDrawable shapeDrawable2 = new ShapeDrawable(roundRectShape);
                shapeDrawable2.getPaint().setColor(i5);
                ShapeDrawable shapeDrawable3 = new ShapeDrawable(roundRectShape);
                shapeDrawable3.getPaint().setColor(resources.getColor(C4558R.color.zm_chat_msg_bg_press_mask));
                LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shapeDrawable2, shapeDrawable3});
                shapeDrawable.setPadding(i, i2, i3, i4);
                addState(new int[]{16842919}, layerDrawable);
                addState(new int[0], shapeDrawable);
            }
        }
    }
}

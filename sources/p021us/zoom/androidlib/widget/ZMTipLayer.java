package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.ZMTipLayer */
public class ZMTipLayer extends FrameLayout {
    private void initView() {
    }

    public ZMTipLayer(Context context) {
        super(context);
        initView();
    }

    public ZMTipLayer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ZMTipLayer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt instanceof ZMTip) {
                layoutTip((ZMTip) childAt, getWidth(), getHeight());
            }
        }
    }

    private boolean isNonPositionedTip(ZMTip zMTip) {
        return zMTip != null && zMTip.getAnchor() == null && !zMTip.isPreferredPositionSetted() && zMTip.getLayoutGravity() == -1;
    }

    private void layoutTip(ZMTip zMTip, int i, int i2) {
        Rect rect;
        int i3;
        int i4;
        int i5;
        int i6;
        View anchor = zMTip.getAnchor();
        int i7 = 0;
        if (anchor != null) {
            Rect absoluteRect = getAbsoluteRect(anchor);
            switch (zMTip.getArrowDirection()) {
                case 0:
                case 2:
                    int centerY = absoluteRect.centerY() + (zMTip.getMeasuredHeight() / 2);
                    if (centerY > i2) {
                        centerY = i2;
                    }
                    int measuredHeight = centerY - zMTip.getMeasuredHeight();
                    if (measuredHeight < 0) {
                        centerY -= measuredHeight;
                    } else {
                        i7 = measuredHeight;
                    }
                    if (zMTip.getArrowDirection() == 0) {
                        i6 = absoluteRect.right + zMTip.getDistanceToAnchor();
                    } else {
                        i6 = (absoluteRect.left - zMTip.getMeasuredWidth()) - zMTip.getDistanceToAnchor();
                    }
                    i5 = centerY;
                    i = zMTip.getMeasuredWidth() + i6;
                    int i8 = i7;
                    i7 = i6;
                    i4 = i8;
                    break;
                case 1:
                case 3:
                    int centerX = absoluteRect.centerX() + (zMTip.getMeasuredWidth() / 2);
                    if (centerX <= i) {
                        i = centerX;
                    }
                    int measuredWidth = i - zMTip.getMeasuredWidth();
                    if (measuredWidth < 0) {
                        i -= measuredWidth;
                    } else {
                        i7 = measuredWidth;
                    }
                    if (zMTip.getArrowDirection() == 1) {
                        i4 = absoluteRect.bottom + zMTip.getDistanceToAnchor();
                    } else {
                        i4 = (absoluteRect.top - zMTip.getMeasuredHeight()) - zMTip.getDistanceToAnchor();
                    }
                    i5 = zMTip.getMeasuredHeight() + i4;
                    break;
                default:
                    i = 0;
                    i4 = 0;
                    i5 = 0;
                    break;
            }
            zMTip.layout(i7, i4, i, i5);
        } else if (zMTip.isPreferredPositionSetted()) {
            int preferredX = zMTip.getPreferredX();
            int preferredY = zMTip.getPreferredY();
            zMTip.layout(preferredX, preferredY, zMTip.getMeasuredWidth() + preferredX, zMTip.getMeasuredHeight() + preferredY);
        } else if (zMTip.getLayoutGravity() != -1) {
            int measuredWidth2 = getMeasuredWidth();
            int measuredHeight2 = getMeasuredHeight();
            int measuredWidth3 = zMTip.getMeasuredWidth();
            int measuredHeight3 = zMTip.getMeasuredHeight();
            int layoutGravityPadding = zMTip.getLayoutGravityPadding();
            switch (zMTip.getLayoutGravity()) {
                case 0:
                    i3 = (measuredHeight2 - measuredHeight3) / 2;
                    i7 = layoutGravityPadding;
                    break;
                case 1:
                    i7 = (measuredWidth2 - layoutGravityPadding) - measuredWidth3;
                    i3 = (measuredHeight2 - measuredHeight3) / 2;
                    break;
                case 2:
                    i7 = (measuredWidth2 - measuredWidth3) / 2;
                    i3 = layoutGravityPadding;
                    break;
                case 3:
                    i7 = (measuredWidth2 - measuredWidth3) / 2;
                    i3 = (measuredHeight2 - layoutGravityPadding) - measuredHeight3;
                    break;
                default:
                    i3 = 0;
                    break;
            }
            zMTip.layout(i7, i3, zMTip.getMeasuredWidth() + i7, zMTip.getMeasuredHeight() + i3);
        } else {
            switch (zMTip.getOverlyingType()) {
                case 0:
                    rect = getPreferredRectForNonPositionedTipFromCenter(zMTip, i, i2);
                    break;
                case 1:
                    rect = getPreferredRectForNonPositionedTipFromBottom(zMTip, i, i2);
                    break;
                case 2:
                    rect = getPreferredRectForNonPositionedTipFromTop(zMTip, i, i2);
                    break;
                default:
                    rect = getPreferredRectForNonPositionedTipFromCenter(zMTip, i, i2);
                    break;
            }
            zMTip.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    private Rect getPreferredRectForNonPositionedTipFromBottom(ZMTip zMTip, int i, int i2) {
        int measuredHeight = i2 - (zMTip.getMeasuredHeight() / 2);
        int dip2px = UIUtil.dip2px(getContext(), 2.0f);
        int childCount = getChildCount();
        if (isNonPositionedTip(zMTip)) {
            boolean z = false;
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt == zMTip) {
                    z = true;
                } else if (z && (childAt instanceof ZMTip)) {
                    ZMTip zMTip2 = (ZMTip) childAt;
                    if (isNonPositionedTip(zMTip2) && zMTip2.getOverlyingType() == 1) {
                        measuredHeight -= childAt.getMeasuredHeight() + dip2px;
                    }
                }
            }
        }
        int measuredWidth = (i - zMTip.getMeasuredWidth()) / 2;
        if (measuredHeight < 0) {
            measuredHeight = zMTip.getMeasuredHeight();
        }
        return new Rect(measuredWidth, measuredHeight - zMTip.getMeasuredHeight(), zMTip.getMeasuredWidth() + measuredWidth, measuredHeight);
    }

    private Rect getPreferredRectForNonPositionedTipFromCenter(ZMTip zMTip, int i, int i2) {
        int measuredHeight = ((i2 * 2) / 3) - zMTip.getMeasuredHeight();
        int dip2px = UIUtil.dip2px(getContext(), 2.0f);
        int childCount = getChildCount();
        if (isNonPositionedTip(zMTip)) {
            int i3 = measuredHeight;
            boolean z = false;
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = getChildAt(i4);
                if (childAt == zMTip) {
                    z = true;
                } else if (z && (childAt instanceof ZMTip)) {
                    ZMTip zMTip2 = (ZMTip) childAt;
                    if (isNonPositionedTip(zMTip2) && zMTip2.getOverlyingType() == 0) {
                        i3 -= childAt.getMeasuredHeight() + dip2px;
                    }
                }
            }
            measuredHeight = i3;
        }
        int measuredWidth = (i - zMTip.getMeasuredWidth()) / 2;
        if (measuredHeight < 0) {
            measuredHeight = 0;
        }
        return new Rect(measuredWidth, measuredHeight, zMTip.getMeasuredWidth() + measuredWidth, zMTip.getMeasuredHeight() + measuredHeight);
    }

    private Rect getPreferredRectForNonPositionedTipFromTop(ZMTip zMTip, int i, int i2) {
        int i3;
        int dip2px = UIUtil.dip2px(getContext(), 2.0f);
        int childCount = getChildCount();
        int i4 = 0;
        if (isNonPositionedTip(zMTip)) {
            i3 = 0;
            boolean z = false;
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                if (childAt == zMTip) {
                    z = true;
                } else if (z && (childAt instanceof ZMTip)) {
                    ZMTip zMTip2 = (ZMTip) childAt;
                    if (isNonPositionedTip(zMTip2) && zMTip2.getOverlyingType() == 2) {
                        i3 += childAt.getMeasuredHeight() + dip2px;
                    }
                }
            }
        } else {
            i3 = 0;
        }
        int measuredWidth = (i - zMTip.getMeasuredWidth()) / 2;
        if (i3 >= 0) {
            i4 = i3;
        }
        return new Rect(measuredWidth, i4, zMTip.getMeasuredWidth() + measuredWidth, zMTip.getMeasuredHeight() + i4);
    }

    private Rect getAbsoluteRect(@NonNull View view) {
        Rect absoluteRect = UIUtil.getAbsoluteRect(view);
        Rect absoluteRect2 = UIUtil.getAbsoluteRect(this);
        absoluteRect.offset(-absoluteRect2.left, -absoluteRect2.top);
        return absoluteRect;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt instanceof ZMTip) {
                measureTip((ZMTip) childAt, size, size2);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0065  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void measureTip(p021us.zoom.androidlib.widget.ZMTip r6, int r7, int r8) {
        /*
            r5 = this;
            android.view.View r0 = r6.getAnchor()
            if (r0 == 0) goto L_0x0020
            android.graphics.Rect r0 = r5.getAbsoluteRect(r0)
            int r1 = r6.getArrowDirection()
            switch(r1) {
                case 0: goto L_0x001c;
                case 1: goto L_0x0018;
                case 2: goto L_0x0015;
                case 3: goto L_0x0012;
                default: goto L_0x0011;
            }
        L_0x0011:
            goto L_0x0030
        L_0x0012:
            int r8 = r0.top
            goto L_0x0030
        L_0x0015:
            int r7 = r0.left
            goto L_0x0030
        L_0x0018:
            int r0 = r0.bottom
            int r8 = r8 - r0
            goto L_0x0030
        L_0x001c:
            int r0 = r0.right
            int r7 = r7 - r0
            goto L_0x0030
        L_0x0020:
            boolean r0 = r6.isPreferredPositionSetted()
            if (r0 == 0) goto L_0x0030
            int r0 = r6.getPreferredX()
            int r1 = r6.getPreferredY()
            int r8 = r8 - r1
            int r7 = r7 - r0
        L_0x0030:
            int r0 = r6.getChildCount()
            r1 = 1
            r2 = 0
            if (r0 != r1) goto L_0x0052
            android.view.View r0 = r6.getChildAt(r2)
            android.view.ViewGroup$LayoutParams r0 = r0.getLayoutParams()
            if (r0 == 0) goto L_0x0052
            int r3 = r0.width
            r4 = -1
            if (r3 != r4) goto L_0x0049
            r3 = 1
            goto L_0x004a
        L_0x0049:
            r3 = 0
        L_0x004a:
            int r0 = r0.height
            if (r0 != r4) goto L_0x004f
            r2 = 1
        L_0x004f:
            r0 = r2
            r2 = r3
            goto L_0x0053
        L_0x0052:
            r0 = 0
        L_0x0053:
            r1 = 1073741824(0x40000000, float:2.0)
            r3 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r2 == 0) goto L_0x005c
            r2 = 1073741824(0x40000000, float:2.0)
            goto L_0x005e
        L_0x005c:
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x005e:
            int r7 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r2)
            if (r0 == 0) goto L_0x0065
            goto L_0x0067
        L_0x0065:
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x0067:
            int r8 = android.view.View.MeasureSpec.makeMeasureSpec(r8, r1)
            r6.measure(r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.widget.ZMTipLayer.measureTip(us.zoom.androidlib.widget.ZMTip, int, int):void");
    }

    public boolean dismissAllTips() {
        boolean z = false;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (childAt instanceof ZMTip) {
                ((ZMTip) childAt).dismiss();
                z = true;
            }
        }
        return z;
    }
}

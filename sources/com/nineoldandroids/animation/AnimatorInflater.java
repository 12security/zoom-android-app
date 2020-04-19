package com.nineoldandroids.animation;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.animation.AnimationUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatorInflater {
    private static final int[] Animator = {16843073, 16843160, 16843198, 16843199, 16843200, 16843486, 16843487, 16843488};
    private static final int[] AnimatorSet = {16843490};
    private static final int AnimatorSet_ordering = 0;
    private static final int Animator_duration = 1;
    private static final int Animator_interpolator = 0;
    private static final int Animator_repeatCount = 3;
    private static final int Animator_repeatMode = 4;
    private static final int Animator_startOffset = 2;
    private static final int Animator_valueFrom = 5;
    private static final int Animator_valueTo = 6;
    private static final int Animator_valueType = 7;
    private static final int[] PropertyAnimator = {16843489};
    private static final int PropertyAnimator_propertyName = 0;
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_FLOAT = 0;

    public static Animator loadAnimator(Context context, int i) throws NotFoundException {
        XmlResourceParser xmlResourceParser = null;
        try {
            XmlResourceParser animation = context.getResources().getAnimation(i);
            Animator createAnimatorFromXml = createAnimatorFromXml(context, animation);
            if (animation != null) {
                animation.close();
            }
            return createAnimatorFromXml;
        } catch (XmlPullParserException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't load animation resource ID #0x");
            sb.append(Integer.toHexString(i));
            NotFoundException notFoundException = new NotFoundException(sb.toString());
            notFoundException.initCause(e);
            throw notFoundException;
        } catch (IOException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Can't load animation resource ID #0x");
            sb2.append(Integer.toHexString(i));
            NotFoundException notFoundException2 = new NotFoundException(sb2.toString());
            notFoundException2.initCause(e2);
            throw notFoundException2;
        } catch (Throwable th) {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
            throw th;
        }
    }

    private static Animator createAnimatorFromXml(Context context, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        return createAnimatorFromXml(context, xmlPullParser, Xml.asAttributeSet(xmlPullParser), null, 0);
    }

    private static Animator createAnimatorFromXml(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, AnimatorSet animatorSet, int i) throws XmlPullParserException, IOException {
        int i2;
        int depth = xmlPullParser.getDepth();
        ArrayList arrayList = null;
        Animator animator = null;
        while (true) {
            int next = xmlPullParser.next();
            i2 = 0;
            if ((next != 3 || xmlPullParser.getDepth() > depth) && next != 1) {
                if (next == 2) {
                    String name = xmlPullParser.getName();
                    if (name.equals("objectAnimator")) {
                        animator = loadObjectAnimator(context, attributeSet);
                    } else if (name.equals("animator")) {
                        animator = loadAnimator(context, attributeSet, null);
                    } else if (name.equals("set")) {
                        animator = new AnimatorSet();
                        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, AnimatorSet);
                        TypedValue typedValue = new TypedValue();
                        obtainStyledAttributes.getValue(0, typedValue);
                        if (typedValue.type == 16) {
                            i2 = typedValue.data;
                        }
                        createAnimatorFromXml(context, xmlPullParser, attributeSet, (AnimatorSet) animator, i2);
                        obtainStyledAttributes.recycle();
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unknown animator name: ");
                        sb.append(xmlPullParser.getName());
                        throw new RuntimeException(sb.toString());
                    }
                    if (animatorSet != null) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(animator);
                    }
                }
            }
        }
        if (!(animatorSet == null || arrayList == null)) {
            Animator[] animatorArr = new Animator[arrayList.size()];
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                int i3 = i2 + 1;
                animatorArr[i2] = (Animator) it.next();
                i2 = i3;
            }
            if (i == 0) {
                animatorSet.playTogether(animatorArr);
            } else {
                animatorSet.playSequentially(animatorArr);
            }
        }
        return animator;
    }

    private static ObjectAnimator loadObjectAnimator(Context context, AttributeSet attributeSet) throws NotFoundException {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        loadAnimator(context, attributeSet, objectAnimator);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, PropertyAnimator);
        objectAnimator.setPropertyName(obtainStyledAttributes.getString(0));
        obtainStyledAttributes.recycle();
        return objectAnimator;
    }

    private static ValueAnimator loadAnimator(Context context, AttributeSet attributeSet, ValueAnimator valueAnimator) throws NotFoundException {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        float f;
        float f2;
        float f3;
        Context context2 = context;
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, Animator);
        long j = (long) obtainStyledAttributes.getInt(1, 0);
        long j2 = (long) obtainStyledAttributes.getInt(2, 0);
        int i6 = obtainStyledAttributes.getInt(7, 0);
        ValueAnimator valueAnimator2 = valueAnimator == null ? new ValueAnimator() : valueAnimator;
        boolean z = i6 == 0;
        TypedValue peekValue = obtainStyledAttributes.peekValue(5);
        boolean z2 = peekValue != null;
        int i7 = z2 ? peekValue.type : 0;
        TypedValue peekValue2 = obtainStyledAttributes.peekValue(6);
        boolean z3 = peekValue2 != null;
        int i8 = z3 ? peekValue2.type : 0;
        if ((z2 && i7 >= 28 && i7 <= 31) || (z3 && i8 >= 28 && i8 <= 31)) {
            valueAnimator2.setEvaluator(new ArgbEvaluator());
            z = false;
        }
        if (!z) {
            i = 0;
            if (z2) {
                if (i7 == 5) {
                    i4 = (int) obtainStyledAttributes.getDimension(5, 0.0f);
                } else if (i7 < 28 || i7 > 31) {
                    i4 = obtainStyledAttributes.getInt(5, 0);
                } else {
                    i4 = obtainStyledAttributes.getColor(5, 0);
                }
                if (z3) {
                    if (i8 == 5) {
                        i5 = (int) obtainStyledAttributes.getDimension(6, 0.0f);
                    } else if (i8 < 28 || i8 > 31) {
                        i5 = obtainStyledAttributes.getInt(6, 0);
                    } else {
                        i5 = obtainStyledAttributes.getColor(6, 0);
                    }
                    valueAnimator2.setIntValues(i4, i5);
                } else {
                    valueAnimator2.setIntValues(i4);
                }
            } else if (z3) {
                if (i8 == 5) {
                    i3 = (int) obtainStyledAttributes.getDimension(6, 0.0f);
                    i2 = 1;
                } else if (i8 < 28 || i8 > 31) {
                    i3 = obtainStyledAttributes.getInt(6, 0);
                    i2 = 1;
                } else {
                    i3 = obtainStyledAttributes.getColor(6, 0);
                    i2 = 1;
                }
                int[] iArr = new int[i2];
                iArr[0] = i3;
                valueAnimator2.setIntValues(iArr);
            }
        } else if (z2) {
            if (i7 == 5) {
                f2 = obtainStyledAttributes.getDimension(5, 0.0f);
            } else {
                f2 = obtainStyledAttributes.getFloat(5, 0.0f);
            }
            if (z3) {
                if (i8 == 5) {
                    f3 = obtainStyledAttributes.getDimension(6, 0.0f);
                } else {
                    f3 = obtainStyledAttributes.getFloat(6, 0.0f);
                }
                i = 0;
                valueAnimator2.setFloatValues(f2, f3);
            } else {
                i = 0;
                valueAnimator2.setFloatValues(f2);
            }
        } else {
            i = 0;
            if (i8 == 5) {
                f = obtainStyledAttributes.getDimension(6, 0.0f);
            } else {
                f = obtainStyledAttributes.getFloat(6, 0.0f);
            }
            valueAnimator2.setFloatValues(f);
        }
        valueAnimator2.setDuration(j);
        valueAnimator2.setStartDelay(j2);
        if (obtainStyledAttributes.hasValue(3)) {
            valueAnimator2.setRepeatCount(obtainStyledAttributes.getInt(3, i));
        }
        if (obtainStyledAttributes.hasValue(4)) {
            valueAnimator2.setRepeatMode(obtainStyledAttributes.getInt(4, 1));
        }
        int resourceId = obtainStyledAttributes.getResourceId(i, i);
        if (resourceId > 0) {
            valueAnimator2.setInterpolator(AnimationUtils.loadInterpolator(context2, resourceId));
        }
        obtainStyledAttributes.recycle();
        return valueAnimator2;
    }
}

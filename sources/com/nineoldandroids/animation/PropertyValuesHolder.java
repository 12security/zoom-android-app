package com.nineoldandroids.animation;

import android.util.Log;
import com.nineoldandroids.util.FloatProperty;
import com.nineoldandroids.util.IntProperty;
import com.nineoldandroids.util.Property;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PropertyValuesHolder implements Cloneable {
    private static Class[] DOUBLE_VARIANTS = {Double.TYPE, Double.class, Float.TYPE, Integer.TYPE, Float.class, Integer.class};
    private static Class[] FLOAT_VARIANTS = {Float.TYPE, Float.class, Double.TYPE, Integer.TYPE, Double.class, Integer.class};
    private static Class[] INTEGER_VARIANTS = {Integer.TYPE, Integer.class, Float.TYPE, Double.TYPE, Float.class, Double.class};
    private static final TypeEvaluator sFloatEvaluator = new FloatEvaluator();
    private static final HashMap<Class, HashMap<String, Method>> sGetterPropertyMap = new HashMap<>();
    private static final TypeEvaluator sIntEvaluator = new IntEvaluator();
    private static final HashMap<Class, HashMap<String, Method>> sSetterPropertyMap = new HashMap<>();
    private Object mAnimatedValue;
    private TypeEvaluator mEvaluator;
    private Method mGetter;
    KeyframeSet mKeyframeSet;
    protected Property mProperty;
    final ReentrantReadWriteLock mPropertyMapLock;
    String mPropertyName;
    Method mSetter;
    final Object[] mTmpValueArray;
    Class mValueType;

    static class FloatPropertyValuesHolder extends PropertyValuesHolder {
        float mFloatAnimatedValue;
        FloatKeyframeSet mFloatKeyframeSet;
        private FloatProperty mFloatProperty;

        public FloatPropertyValuesHolder(String str, FloatKeyframeSet floatKeyframeSet) {
            super(str);
            this.mValueType = Float.TYPE;
            this.mKeyframeSet = floatKeyframeSet;
            this.mFloatKeyframeSet = (FloatKeyframeSet) this.mKeyframeSet;
        }

        public FloatPropertyValuesHolder(Property property, FloatKeyframeSet floatKeyframeSet) {
            super(property);
            this.mValueType = Float.TYPE;
            this.mKeyframeSet = floatKeyframeSet;
            this.mFloatKeyframeSet = (FloatKeyframeSet) this.mKeyframeSet;
            if (property instanceof FloatProperty) {
                this.mFloatProperty = (FloatProperty) this.mProperty;
            }
        }

        public FloatPropertyValuesHolder(String str, float... fArr) {
            super(str);
            setFloatValues(fArr);
        }

        public FloatPropertyValuesHolder(Property property, float... fArr) {
            super(property);
            setFloatValues(fArr);
            if (property instanceof FloatProperty) {
                this.mFloatProperty = (FloatProperty) this.mProperty;
            }
        }

        public void setFloatValues(float... fArr) {
            PropertyValuesHolder.super.setFloatValues(fArr);
            this.mFloatKeyframeSet = (FloatKeyframeSet) this.mKeyframeSet;
        }

        /* access modifiers changed from: 0000 */
        public void calculateValue(float f) {
            this.mFloatAnimatedValue = this.mFloatKeyframeSet.getFloatValue(f);
        }

        /* access modifiers changed from: 0000 */
        public Object getAnimatedValue() {
            return Float.valueOf(this.mFloatAnimatedValue);
        }

        public FloatPropertyValuesHolder clone() {
            FloatPropertyValuesHolder floatPropertyValuesHolder = (FloatPropertyValuesHolder) PropertyValuesHolder.super.clone();
            floatPropertyValuesHolder.mFloatKeyframeSet = (FloatKeyframeSet) floatPropertyValuesHolder.mKeyframeSet;
            return floatPropertyValuesHolder;
        }

        /* access modifiers changed from: 0000 */
        public void setAnimatedValue(Object obj) {
            FloatProperty floatProperty = this.mFloatProperty;
            if (floatProperty != null) {
                floatProperty.setValue(obj, this.mFloatAnimatedValue);
            } else if (this.mProperty != null) {
                this.mProperty.set(obj, Float.valueOf(this.mFloatAnimatedValue));
            } else {
                if (this.mSetter != null) {
                    try {
                        this.mTmpValueArray[0] = Float.valueOf(this.mFloatAnimatedValue);
                        this.mSetter.invoke(obj, this.mTmpValueArray);
                    } catch (InvocationTargetException e) {
                        Log.e("PropertyValuesHolder", e.toString());
                    } catch (IllegalAccessException e2) {
                        Log.e("PropertyValuesHolder", e2.toString());
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void setupSetter(Class cls) {
            if (this.mProperty == null) {
                PropertyValuesHolder.super.setupSetter(cls);
            }
        }
    }

    static class IntPropertyValuesHolder extends PropertyValuesHolder {
        int mIntAnimatedValue;
        IntKeyframeSet mIntKeyframeSet;
        private IntProperty mIntProperty;

        public IntPropertyValuesHolder(String str, IntKeyframeSet intKeyframeSet) {
            super(str);
            this.mValueType = Integer.TYPE;
            this.mKeyframeSet = intKeyframeSet;
            this.mIntKeyframeSet = (IntKeyframeSet) this.mKeyframeSet;
        }

        public IntPropertyValuesHolder(Property property, IntKeyframeSet intKeyframeSet) {
            super(property);
            this.mValueType = Integer.TYPE;
            this.mKeyframeSet = intKeyframeSet;
            this.mIntKeyframeSet = (IntKeyframeSet) this.mKeyframeSet;
            if (property instanceof IntProperty) {
                this.mIntProperty = (IntProperty) this.mProperty;
            }
        }

        public IntPropertyValuesHolder(String str, int... iArr) {
            super(str);
            setIntValues(iArr);
        }

        public IntPropertyValuesHolder(Property property, int... iArr) {
            super(property);
            setIntValues(iArr);
            if (property instanceof IntProperty) {
                this.mIntProperty = (IntProperty) this.mProperty;
            }
        }

        public void setIntValues(int... iArr) {
            PropertyValuesHolder.super.setIntValues(iArr);
            this.mIntKeyframeSet = (IntKeyframeSet) this.mKeyframeSet;
        }

        /* access modifiers changed from: 0000 */
        public void calculateValue(float f) {
            this.mIntAnimatedValue = this.mIntKeyframeSet.getIntValue(f);
        }

        /* access modifiers changed from: 0000 */
        public Object getAnimatedValue() {
            return Integer.valueOf(this.mIntAnimatedValue);
        }

        public IntPropertyValuesHolder clone() {
            IntPropertyValuesHolder intPropertyValuesHolder = (IntPropertyValuesHolder) PropertyValuesHolder.super.clone();
            intPropertyValuesHolder.mIntKeyframeSet = (IntKeyframeSet) intPropertyValuesHolder.mKeyframeSet;
            return intPropertyValuesHolder;
        }

        /* access modifiers changed from: 0000 */
        public void setAnimatedValue(Object obj) {
            IntProperty intProperty = this.mIntProperty;
            if (intProperty != null) {
                intProperty.setValue(obj, this.mIntAnimatedValue);
            } else if (this.mProperty != null) {
                this.mProperty.set(obj, Integer.valueOf(this.mIntAnimatedValue));
            } else {
                if (this.mSetter != null) {
                    try {
                        this.mTmpValueArray[0] = Integer.valueOf(this.mIntAnimatedValue);
                        this.mSetter.invoke(obj, this.mTmpValueArray);
                    } catch (InvocationTargetException e) {
                        Log.e("PropertyValuesHolder", e.toString());
                    } catch (IllegalAccessException e2) {
                        Log.e("PropertyValuesHolder", e2.toString());
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void setupSetter(Class cls) {
            if (this.mProperty == null) {
                PropertyValuesHolder.super.setupSetter(cls);
            }
        }
    }

    private PropertyValuesHolder(String str) {
        this.mSetter = null;
        this.mGetter = null;
        this.mKeyframeSet = null;
        this.mPropertyMapLock = new ReentrantReadWriteLock();
        this.mTmpValueArray = new Object[1];
        this.mPropertyName = str;
    }

    private PropertyValuesHolder(Property property) {
        this.mSetter = null;
        this.mGetter = null;
        this.mKeyframeSet = null;
        this.mPropertyMapLock = new ReentrantReadWriteLock();
        this.mTmpValueArray = new Object[1];
        this.mProperty = property;
        if (property != null) {
            this.mPropertyName = property.getName();
        }
    }

    public static PropertyValuesHolder ofInt(String str, int... iArr) {
        return new IntPropertyValuesHolder(str, iArr);
    }

    public static PropertyValuesHolder ofInt(Property<?, Integer> property, int... iArr) {
        return new IntPropertyValuesHolder((Property) property, iArr);
    }

    public static PropertyValuesHolder ofFloat(String str, float... fArr) {
        return new FloatPropertyValuesHolder(str, fArr);
    }

    public static PropertyValuesHolder ofFloat(Property<?, Float> property, float... fArr) {
        return new FloatPropertyValuesHolder((Property) property, fArr);
    }

    public static PropertyValuesHolder ofObject(String str, TypeEvaluator typeEvaluator, Object... objArr) {
        PropertyValuesHolder propertyValuesHolder = new PropertyValuesHolder(str);
        propertyValuesHolder.setObjectValues(objArr);
        propertyValuesHolder.setEvaluator(typeEvaluator);
        return propertyValuesHolder;
    }

    public static <V> PropertyValuesHolder ofObject(Property property, TypeEvaluator<V> typeEvaluator, V... vArr) {
        PropertyValuesHolder propertyValuesHolder = new PropertyValuesHolder(property);
        propertyValuesHolder.setObjectValues(vArr);
        propertyValuesHolder.setEvaluator(typeEvaluator);
        return propertyValuesHolder;
    }

    public static PropertyValuesHolder ofKeyframe(String str, Keyframe... keyframeArr) {
        KeyframeSet ofKeyframe = KeyframeSet.ofKeyframe(keyframeArr);
        if (ofKeyframe instanceof IntKeyframeSet) {
            return new IntPropertyValuesHolder(str, (IntKeyframeSet) ofKeyframe);
        }
        if (ofKeyframe instanceof FloatKeyframeSet) {
            return new FloatPropertyValuesHolder(str, (FloatKeyframeSet) ofKeyframe);
        }
        PropertyValuesHolder propertyValuesHolder = new PropertyValuesHolder(str);
        propertyValuesHolder.mKeyframeSet = ofKeyframe;
        propertyValuesHolder.mValueType = keyframeArr[0].getType();
        return propertyValuesHolder;
    }

    public static PropertyValuesHolder ofKeyframe(Property property, Keyframe... keyframeArr) {
        KeyframeSet ofKeyframe = KeyframeSet.ofKeyframe(keyframeArr);
        if (ofKeyframe instanceof IntKeyframeSet) {
            return new IntPropertyValuesHolder(property, (IntKeyframeSet) ofKeyframe);
        }
        if (ofKeyframe instanceof FloatKeyframeSet) {
            return new FloatPropertyValuesHolder(property, (FloatKeyframeSet) ofKeyframe);
        }
        PropertyValuesHolder propertyValuesHolder = new PropertyValuesHolder(property);
        propertyValuesHolder.mKeyframeSet = ofKeyframe;
        propertyValuesHolder.mValueType = keyframeArr[0].getType();
        return propertyValuesHolder;
    }

    public void setIntValues(int... iArr) {
        this.mValueType = Integer.TYPE;
        this.mKeyframeSet = KeyframeSet.ofInt(iArr);
    }

    public void setFloatValues(float... fArr) {
        this.mValueType = Float.TYPE;
        this.mKeyframeSet = KeyframeSet.ofFloat(fArr);
    }

    public void setKeyframes(Keyframe... keyframeArr) {
        int length = keyframeArr.length;
        Keyframe[] keyframeArr2 = new Keyframe[Math.max(length, 2)];
        this.mValueType = keyframeArr[0].getType();
        for (int i = 0; i < length; i++) {
            keyframeArr2[i] = keyframeArr[i];
        }
        this.mKeyframeSet = new KeyframeSet(keyframeArr2);
    }

    public void setObjectValues(Object... objArr) {
        this.mValueType = objArr[0].getClass();
        this.mKeyframeSet = KeyframeSet.ofObject(objArr);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x007d, code lost:
        return r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x007e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.reflect.Method getPropertyFunction(java.lang.Class r8, java.lang.String r9, java.lang.Class r10) {
        /*
            r7 = this;
            java.lang.String r0 = r7.mPropertyName
            java.lang.String r9 = getMethodName(r9, r0)
            r0 = 0
            r1 = 1
            if (r10 != 0) goto L_0x003e
            java.lang.reflect.Method r8 = r8.getMethod(r9, r0)     // Catch:{ NoSuchMethodException -> 0x0010 }
            goto L_0x00ae
        L_0x0010:
            r10 = move-exception
            java.lang.reflect.Method r0 = r8.getDeclaredMethod(r9, r0)     // Catch:{ NoSuchMethodException -> 0x001b }
            r0.setAccessible(r1)     // Catch:{ NoSuchMethodException -> 0x001b }
            r8 = r0
            goto L_0x00ae
        L_0x001b:
            java.lang.String r8 = "PropertyValuesHolder"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r1 = "Couldn't find no-arg method for property "
            r9.append(r1)
            java.lang.String r1 = r7.mPropertyName
            r9.append(r1)
            java.lang.String r1 = ": "
            r9.append(r1)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
            r8 = r0
            goto L_0x00ae
        L_0x003e:
            java.lang.Class[] r10 = new java.lang.Class[r1]
            java.lang.Class r2 = r7.mValueType
            java.lang.Class<java.lang.Float> r3 = java.lang.Float.class
            boolean r2 = r2.equals(r3)
            r3 = 0
            if (r2 == 0) goto L_0x004e
            java.lang.Class[] r2 = FLOAT_VARIANTS
            goto L_0x006e
        L_0x004e:
            java.lang.Class r2 = r7.mValueType
            java.lang.Class<java.lang.Integer> r4 = java.lang.Integer.class
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x005b
            java.lang.Class[] r2 = INTEGER_VARIANTS
            goto L_0x006e
        L_0x005b:
            java.lang.Class r2 = r7.mValueType
            java.lang.Class<java.lang.Double> r4 = java.lang.Double.class
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x0068
            java.lang.Class[] r2 = DOUBLE_VARIANTS
            goto L_0x006e
        L_0x0068:
            java.lang.Class[] r2 = new java.lang.Class[r1]
            java.lang.Class r4 = r7.mValueType
            r2[r3] = r4
        L_0x006e:
            int r4 = r2.length
            r5 = r0
            r0 = 0
        L_0x0071:
            if (r0 >= r4) goto L_0x008b
            r6 = r2[r0]
            r10[r3] = r6
            java.lang.reflect.Method r5 = r8.getMethod(r9, r10)     // Catch:{ NoSuchMethodException -> 0x007e }
            r7.mValueType = r6     // Catch:{ NoSuchMethodException -> 0x007e }
            return r5
        L_0x007e:
            java.lang.reflect.Method r5 = r8.getDeclaredMethod(r9, r10)     // Catch:{ NoSuchMethodException -> 0x0088 }
            r5.setAccessible(r1)     // Catch:{ NoSuchMethodException -> 0x0088 }
            r7.mValueType = r6     // Catch:{ NoSuchMethodException -> 0x0088 }
            return r5
        L_0x0088:
            int r0 = r0 + 1
            goto L_0x0071
        L_0x008b:
            java.lang.String r8 = "PropertyValuesHolder"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "Couldn't find setter/getter for property "
            r9.append(r10)
            java.lang.String r10 = r7.mPropertyName
            r9.append(r10)
            java.lang.String r10 = " with value type "
            r9.append(r10)
            java.lang.Class r10 = r7.mValueType
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
            r8 = r5
        L_0x00ae:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nineoldandroids.animation.PropertyValuesHolder.getPropertyFunction(java.lang.Class, java.lang.String, java.lang.Class):java.lang.reflect.Method");
    }

    private Method setupSetterOrGetter(Class cls, HashMap<Class, HashMap<String, Method>> hashMap, String str, Class cls2) {
        try {
            this.mPropertyMapLock.writeLock().lock();
            HashMap hashMap2 = (HashMap) hashMap.get(cls);
            Method method = hashMap2 != null ? (Method) hashMap2.get(this.mPropertyName) : null;
            if (method == null) {
                method = getPropertyFunction(cls, str, cls2);
                if (hashMap2 == null) {
                    hashMap2 = new HashMap();
                    hashMap.put(cls, hashMap2);
                }
                hashMap2.put(this.mPropertyName, method);
            }
            return method;
        } finally {
            this.mPropertyMapLock.writeLock().unlock();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setupSetter(Class cls) {
        this.mSetter = setupSetterOrGetter(cls, sSetterPropertyMap, "set", this.mValueType);
    }

    private void setupGetter(Class cls) {
        this.mGetter = setupSetterOrGetter(cls, sGetterPropertyMap, "get", null);
    }

    /* access modifiers changed from: 0000 */
    public void setupSetterAndGetter(Object obj) {
        Property property = this.mProperty;
        if (property != null) {
            try {
                property.get(obj);
                Iterator it = this.mKeyframeSet.mKeyframes.iterator();
                while (it.hasNext()) {
                    Keyframe keyframe = (Keyframe) it.next();
                    if (!keyframe.hasValue()) {
                        keyframe.setValue(this.mProperty.get(obj));
                    }
                }
                return;
            } catch (ClassCastException unused) {
                StringBuilder sb = new StringBuilder();
                sb.append("No such property (");
                sb.append(this.mProperty.getName());
                sb.append(") on target object ");
                sb.append(obj);
                sb.append(". Trying reflection instead");
                Log.e("PropertyValuesHolder", sb.toString());
                this.mProperty = null;
            }
        }
        Class cls = obj.getClass();
        if (this.mSetter == null) {
            setupSetter(cls);
        }
        Iterator it2 = this.mKeyframeSet.mKeyframes.iterator();
        while (it2.hasNext()) {
            Keyframe keyframe2 = (Keyframe) it2.next();
            if (!keyframe2.hasValue()) {
                if (this.mGetter == null) {
                    setupGetter(cls);
                }
                try {
                    keyframe2.setValue(this.mGetter.invoke(obj, new Object[0]));
                } catch (InvocationTargetException e) {
                    Log.e("PropertyValuesHolder", e.toString());
                } catch (IllegalAccessException e2) {
                    Log.e("PropertyValuesHolder", e2.toString());
                }
            }
        }
    }

    private void setupValue(Object obj, Keyframe keyframe) {
        Property property = this.mProperty;
        if (property != null) {
            keyframe.setValue(property.get(obj));
        }
        try {
            if (this.mGetter == null) {
                setupGetter(obj.getClass());
            }
            keyframe.setValue(this.mGetter.invoke(obj, new Object[0]));
        } catch (InvocationTargetException e) {
            Log.e("PropertyValuesHolder", e.toString());
        } catch (IllegalAccessException e2) {
            Log.e("PropertyValuesHolder", e2.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public void setupStartValue(Object obj) {
        setupValue(obj, (Keyframe) this.mKeyframeSet.mKeyframes.get(0));
    }

    /* access modifiers changed from: 0000 */
    public void setupEndValue(Object obj) {
        setupValue(obj, (Keyframe) this.mKeyframeSet.mKeyframes.get(this.mKeyframeSet.mKeyframes.size() - 1));
    }

    public PropertyValuesHolder clone() {
        try {
            PropertyValuesHolder propertyValuesHolder = (PropertyValuesHolder) super.clone();
            propertyValuesHolder.mPropertyName = this.mPropertyName;
            propertyValuesHolder.mProperty = this.mProperty;
            propertyValuesHolder.mKeyframeSet = this.mKeyframeSet.clone();
            propertyValuesHolder.mEvaluator = this.mEvaluator;
            return propertyValuesHolder;
        } catch (CloneNotSupportedException unused) {
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void setAnimatedValue(Object obj) {
        Property property = this.mProperty;
        if (property != null) {
            property.set(obj, getAnimatedValue());
        }
        if (this.mSetter != null) {
            try {
                this.mTmpValueArray[0] = getAnimatedValue();
                this.mSetter.invoke(obj, this.mTmpValueArray);
            } catch (InvocationTargetException e) {
                Log.e("PropertyValuesHolder", e.toString());
            } catch (IllegalAccessException e2) {
                Log.e("PropertyValuesHolder", e2.toString());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void init() {
        if (this.mEvaluator == null) {
            Class<Float> cls = this.mValueType;
            TypeEvaluator typeEvaluator = cls == Integer.class ? sIntEvaluator : cls == Float.class ? sFloatEvaluator : null;
            this.mEvaluator = typeEvaluator;
        }
        TypeEvaluator typeEvaluator2 = this.mEvaluator;
        if (typeEvaluator2 != null) {
            this.mKeyframeSet.setEvaluator(typeEvaluator2);
        }
    }

    public void setEvaluator(TypeEvaluator typeEvaluator) {
        this.mEvaluator = typeEvaluator;
        this.mKeyframeSet.setEvaluator(typeEvaluator);
    }

    /* access modifiers changed from: 0000 */
    public void calculateValue(float f) {
        this.mAnimatedValue = this.mKeyframeSet.getValue(f);
    }

    public void setPropertyName(String str) {
        this.mPropertyName = str;
    }

    public void setProperty(Property property) {
        this.mProperty = property;
    }

    public String getPropertyName() {
        return this.mPropertyName;
    }

    /* access modifiers changed from: 0000 */
    public Object getAnimatedValue() {
        return this.mAnimatedValue;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mPropertyName);
        sb.append(": ");
        sb.append(this.mKeyframeSet.toString());
        return sb.toString();
    }

    static String getMethodName(String str, String str2) {
        if (str2 == null || str2.length() == 0) {
            return str;
        }
        char upperCase = Character.toUpperCase(str2.charAt(0));
        String substring = str2.substring(1);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(upperCase);
        sb.append(substring);
        return sb.toString();
    }
}

package org.greenrobot.eventbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.greenrobot.eventbus.meta.SubscriberInfo;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

class SubscriberMethodFinder {
    private static final int BRIDGE = 64;
    private static final FindState[] FIND_STATE_POOL = new FindState[4];
    private static final Map<Class<?>, List<SubscriberMethod>> METHOD_CACHE = new ConcurrentHashMap();
    private static final int MODIFIERS_IGNORE = 5192;
    private static final int POOL_SIZE = 4;
    private static final int SYNTHETIC = 4096;
    private final boolean ignoreGeneratedIndex;
    private final boolean strictMethodVerification;
    private List<SubscriberInfoIndex> subscriberInfoIndexes;

    static class FindState {
        final Map<Class, Object> anyMethodByEventType = new HashMap();
        Class<?> clazz;
        final StringBuilder methodKeyBuilder = new StringBuilder(128);
        boolean skipSuperClasses;
        Class<?> subscriberClass;
        final Map<String, Class> subscriberClassByMethodKey = new HashMap();
        SubscriberInfo subscriberInfo;
        final List<SubscriberMethod> subscriberMethods = new ArrayList();

        FindState() {
        }

        /* access modifiers changed from: 0000 */
        public void initForSubscriber(Class<?> cls) {
            this.clazz = cls;
            this.subscriberClass = cls;
            this.skipSuperClasses = false;
            this.subscriberInfo = null;
        }

        /* access modifiers changed from: 0000 */
        public void recycle() {
            this.subscriberMethods.clear();
            this.anyMethodByEventType.clear();
            this.subscriberClassByMethodKey.clear();
            this.methodKeyBuilder.setLength(0);
            this.subscriberClass = null;
            this.clazz = null;
            this.skipSuperClasses = false;
            this.subscriberInfo = null;
        }

        /* access modifiers changed from: 0000 */
        public boolean checkAdd(Method method, Class<?> cls) {
            Object put = this.anyMethodByEventType.put(cls, method);
            if (put == null) {
                return true;
            }
            if (put instanceof Method) {
                if (checkAddWithMethodSignature((Method) put, cls)) {
                    this.anyMethodByEventType.put(cls, this);
                } else {
                    throw new IllegalStateException();
                }
            }
            return checkAddWithMethodSignature(method, cls);
        }

        private boolean checkAddWithMethodSignature(Method method, Class<?> cls) {
            this.methodKeyBuilder.setLength(0);
            this.methodKeyBuilder.append(method.getName());
            StringBuilder sb = this.methodKeyBuilder;
            sb.append('>');
            sb.append(cls.getName());
            String sb2 = this.methodKeyBuilder.toString();
            Class declaringClass = method.getDeclaringClass();
            Class cls2 = (Class) this.subscriberClassByMethodKey.put(sb2, declaringClass);
            if (cls2 == null || cls2.isAssignableFrom(declaringClass)) {
                return true;
            }
            this.subscriberClassByMethodKey.put(sb2, cls2);
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void moveToSuperclass() {
            if (this.skipSuperClasses) {
                this.clazz = null;
                return;
            }
            this.clazz = this.clazz.getSuperclass();
            String name = this.clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                this.clazz = null;
            }
        }
    }

    SubscriberMethodFinder(List<SubscriberInfoIndex> list, boolean z, boolean z2) {
        this.subscriberInfoIndexes = list;
        this.strictMethodVerification = z;
        this.ignoreGeneratedIndex = z2;
    }

    /* access modifiers changed from: 0000 */
    public List<SubscriberMethod> findSubscriberMethods(Class<?> cls) {
        List<SubscriberMethod> list;
        List<SubscriberMethod> list2 = (List) METHOD_CACHE.get(cls);
        if (list2 != null) {
            return list2;
        }
        if (this.ignoreGeneratedIndex) {
            list = findUsingReflection(cls);
        } else {
            list = findUsingInfo(cls);
        }
        if (!list.isEmpty()) {
            METHOD_CACHE.put(cls, list);
            return list;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Subscriber ");
        sb.append(cls);
        sb.append(" and its super classes have no public methods with the @Subscribe annotation");
        throw new EventBusException(sb.toString());
    }

    private List<SubscriberMethod> findUsingInfo(Class<?> cls) {
        SubscriberMethod[] subscriberMethods;
        FindState prepareFindState = prepareFindState();
        prepareFindState.initForSubscriber(cls);
        while (prepareFindState.clazz != null) {
            prepareFindState.subscriberInfo = getSubscriberInfo(prepareFindState);
            if (prepareFindState.subscriberInfo != null) {
                for (SubscriberMethod subscriberMethod : prepareFindState.subscriberInfo.getSubscriberMethods()) {
                    if (prepareFindState.checkAdd(subscriberMethod.method, subscriberMethod.eventType)) {
                        prepareFindState.subscriberMethods.add(subscriberMethod);
                    }
                }
            } else {
                findUsingReflectionInSingleClass(prepareFindState);
            }
            prepareFindState.moveToSuperclass();
        }
        return getMethodsAndRelease(prepareFindState);
    }

    private List<SubscriberMethod> getMethodsAndRelease(FindState findState) {
        ArrayList arrayList = new ArrayList(findState.subscriberMethods);
        findState.recycle();
        synchronized (FIND_STATE_POOL) {
            int i = 0;
            while (true) {
                if (i >= 4) {
                    break;
                } else if (FIND_STATE_POOL[i] == null) {
                    FIND_STATE_POOL[i] = findState;
                    break;
                } else {
                    i++;
                }
            }
        }
        return arrayList;
    }

    private FindState prepareFindState() {
        synchronized (FIND_STATE_POOL) {
            for (int i = 0; i < 4; i++) {
                FindState findState = FIND_STATE_POOL[i];
                if (findState != null) {
                    FIND_STATE_POOL[i] = null;
                    return findState;
                }
            }
            return new FindState();
        }
    }

    private SubscriberInfo getSubscriberInfo(FindState findState) {
        if (!(findState.subscriberInfo == null || findState.subscriberInfo.getSuperSubscriberInfo() == null)) {
            SubscriberInfo superSubscriberInfo = findState.subscriberInfo.getSuperSubscriberInfo();
            if (findState.clazz == superSubscriberInfo.getSubscriberClass()) {
                return superSubscriberInfo;
            }
        }
        List<SubscriberInfoIndex> list = this.subscriberInfoIndexes;
        if (list != null) {
            for (SubscriberInfoIndex subscriberInfo : list) {
                SubscriberInfo subscriberInfo2 = subscriberInfo.getSubscriberInfo(findState.clazz);
                if (subscriberInfo2 != null) {
                    return subscriberInfo2;
                }
            }
        }
        return null;
    }

    private List<SubscriberMethod> findUsingReflection(Class<?> cls) {
        FindState prepareFindState = prepareFindState();
        prepareFindState.initForSubscriber(cls);
        while (prepareFindState.clazz != null) {
            findUsingReflectionInSingleClass(prepareFindState);
            prepareFindState.moveToSuperclass();
        }
        return getMethodsAndRelease(prepareFindState);
    }

    private void findUsingReflectionInSingleClass(FindState findState) {
        Method[] methodArr;
        try {
            methodArr = findState.clazz.getDeclaredMethods();
        } catch (Throwable unused) {
            methodArr = findState.clazz.getMethods();
            findState.skipSuperClasses = true;
        }
        for (Method method : methodArr) {
            int modifiers = method.getModifiers();
            if ((modifiers & 1) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    Subscribe subscribe = (Subscribe) method.getAnnotation(Subscribe.class);
                    if (subscribe != null) {
                        Class cls = parameterTypes[0];
                        if (findState.checkAdd(method, cls)) {
                            ThreadMode threadMode = subscribe.threadMode();
                            List<SubscriberMethod> list = findState.subscriberMethods;
                            SubscriberMethod subscriberMethod = new SubscriberMethod(method, cls, threadMode, subscribe.priority(), subscribe.sticky());
                            list.add(subscriberMethod);
                        }
                    }
                } else if (this.strictMethodVerification && method.isAnnotationPresent(Subscribe.class)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(method.getDeclaringClass().getName());
                    sb.append(".");
                    sb.append(method.getName());
                    String sb2 = sb.toString();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("@Subscribe method ");
                    sb3.append(sb2);
                    sb3.append("must have exactly 1 parameter but has ");
                    sb3.append(parameterTypes.length);
                    throw new EventBusException(sb3.toString());
                }
            } else if (this.strictMethodVerification && method.isAnnotationPresent(Subscribe.class)) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(method.getDeclaringClass().getName());
                sb4.append(".");
                sb4.append(method.getName());
                String sb5 = sb4.toString();
                StringBuilder sb6 = new StringBuilder();
                sb6.append(sb5);
                sb6.append(" is a illegal @Subscribe method: must be public, non-static, and non-abstract");
                throw new EventBusException(sb6.toString());
            }
        }
    }

    static void clearCaches() {
        METHOD_CACHE.clear();
    }
}

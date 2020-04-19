package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Throwables {
    @GwtIncompatible
    private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";
    @GwtIncompatible
    @VisibleForTesting
    static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
    /* access modifiers changed from: private */
    @GwtIncompatible
    @Nullable
    public static final Method getStackTraceDepthMethod;
    /* access modifiers changed from: private */
    @GwtIncompatible
    @Nullable
    public static final Method getStackTraceElementMethod = (jla == null ? null : getGetMethod());
    /* access modifiers changed from: private */
    @GwtIncompatible
    @Nullable
    public static final Object jla = getJLA();

    private Throwables() {
    }

    @GwtIncompatible
    public static <X extends Throwable> void throwIfInstanceOf(Throwable th, Class<X> cls) throws Throwable {
        Preconditions.checkNotNull(th);
        if (cls.isInstance(th)) {
            throw ((Throwable) cls.cast(th));
        }
    }

    @GwtIncompatible
    @Deprecated
    public static <X extends Throwable> void propagateIfInstanceOf(@Nullable Throwable th, Class<X> cls) throws Throwable {
        if (th != null) {
            throwIfInstanceOf(th, cls);
        }
    }

    public static void throwIfUnchecked(Throwable th) {
        Preconditions.checkNotNull(th);
        if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        } else if (th instanceof Error) {
            throw ((Error) th);
        }
    }

    @GwtIncompatible
    @Deprecated
    public static void propagateIfPossible(@Nullable Throwable th) {
        if (th != null) {
            throwIfUnchecked(th);
        }
    }

    @GwtIncompatible
    public static <X extends Throwable> void propagateIfPossible(@Nullable Throwable th, Class<X> cls) throws Throwable {
        propagateIfInstanceOf(th, cls);
        propagateIfPossible(th);
    }

    @GwtIncompatible
    public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(@Nullable Throwable th, Class<X1> cls, Class<X2> cls2) throws Throwable, Throwable {
        Preconditions.checkNotNull(cls2);
        propagateIfInstanceOf(th, cls);
        propagateIfPossible(th, cls2);
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    @Deprecated
    public static RuntimeException propagate(Throwable th) {
        throwIfUnchecked(th);
        throw new RuntimeException(th);
    }

    public static Throwable getRootCause(Throwable th) {
        while (true) {
            Throwable cause = th.getCause();
            if (cause == null) {
                return th;
            }
            th = cause;
        }
    }

    @Beta
    public static List<Throwable> getCausalChain(Throwable th) {
        Preconditions.checkNotNull(th);
        ArrayList arrayList = new ArrayList(4);
        while (th != null) {
            arrayList.add(th);
            th = th.getCause();
        }
        return Collections.unmodifiableList(arrayList);
    }

    @GwtIncompatible
    public static String getStackTraceAsString(Throwable th) {
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    @GwtIncompatible
    @Beta
    public static List<StackTraceElement> lazyStackTrace(Throwable th) {
        return lazyStackTraceIsLazy() ? jlaStackTrace(th) : Collections.unmodifiableList(Arrays.asList(th.getStackTrace()));
    }

    @GwtIncompatible
    @Beta
    public static boolean lazyStackTraceIsLazy() {
        boolean z = true;
        boolean z2 = getStackTraceElementMethod != null;
        if (getStackTraceDepthMethod == null) {
            z = false;
        }
        return z2 & z;
    }

    @GwtIncompatible
    private static List<StackTraceElement> jlaStackTrace(final Throwable th) {
        Preconditions.checkNotNull(th);
        return new AbstractList<StackTraceElement>() {
            public StackTraceElement get(int i) {
                return (StackTraceElement) Throwables.invokeAccessibleNonThrowingMethod(Throwables.getStackTraceElementMethod, Throwables.jla, th, Integer.valueOf(i));
            }

            public int size() {
                return ((Integer) Throwables.invokeAccessibleNonThrowingMethod(Throwables.getStackTraceDepthMethod, Throwables.jla, th)).intValue();
            }
        };
    }

    /* access modifiers changed from: private */
    @GwtIncompatible
    public static Object invokeAccessibleNonThrowingMethod(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e2) {
            throw propagate(e2.getCause());
        }
    }

    static {
        Method method = null;
        if (jla != null) {
            method = getSizeMethod();
        }
        getStackTraceDepthMethod = method;
    }

    @GwtIncompatible
    @Nullable
    private static Object getJLA() {
        try {
            return Class.forName(SHARED_SECRETS_CLASSNAME, false, null).getMethod("getJavaLangAccess", new Class[0]).invoke(null, new Object[0]);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable unused) {
            return null;
        }
    }

    @GwtIncompatible
    @Nullable
    private static Method getGetMethod() {
        return getJlaMethod("getStackTraceElement", Throwable.class, Integer.TYPE);
    }

    @GwtIncompatible
    @Nullable
    private static Method getSizeMethod() {
        return getJlaMethod("getStackTraceDepth", Throwable.class);
    }

    @GwtIncompatible
    @Nullable
    private static Method getJlaMethod(String str, Class<?>... clsArr) throws ThreadDeath {
        try {
            return Class.forName(JAVA_LANG_ACCESS_CLASSNAME, false, null).getMethod(str, clsArr);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable unused) {
            return null;
        }
    }
}

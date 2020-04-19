package com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public class Finalizer implements Runnable {
    private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
    private static final Field inheritableThreadLocals = getInheritableThreadLocalsField();
    private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
    private final WeakReference<Class<?>> finalizableReferenceClassReference;
    private final PhantomReference<Object> frqReference;
    private final ReferenceQueue<Object> queue;

    public static void startFinalizer(Class<?> cls, ReferenceQueue<Object> referenceQueue, PhantomReference<Object> phantomReference) {
        if (cls.getName().equals(FINALIZABLE_REFERENCE)) {
            Thread thread = new Thread(new Finalizer(cls, referenceQueue, phantomReference));
            thread.setName(Finalizer.class.getName());
            thread.setDaemon(true);
            try {
                if (inheritableThreadLocals != null) {
                    inheritableThreadLocals.set(thread, null);
                }
            } catch (Throwable th) {
                logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", th);
            }
            thread.start();
            return;
        }
        throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
    }

    private Finalizer(Class<?> cls, ReferenceQueue<Object> referenceQueue, PhantomReference<Object> phantomReference) {
        this.queue = referenceQueue;
        this.finalizableReferenceClassReference = new WeakReference<>(cls);
        this.frqReference = phantomReference;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:0|1) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:0:0x0000 */
    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP:0: B:0:0x0000->B:2:0x000a, LOOP_START, SYNTHETIC, Splitter:B:0:0x0000] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r1 = this;
        L_0x0000:
            java.lang.ref.ReferenceQueue<java.lang.Object> r0 = r1.queue     // Catch:{ InterruptedException -> 0x0000 }
            java.lang.ref.Reference r0 = r0.remove()     // Catch:{ InterruptedException -> 0x0000 }
            boolean r0 = r1.cleanUp(r0)     // Catch:{ InterruptedException -> 0x0000 }
            if (r0 != 0) goto L_0x0000
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.base.internal.Finalizer.run():void");
    }

    private boolean cleanUp(Reference<?> reference) {
        Method finalizeReferentMethod = getFinalizeReferentMethod();
        if (finalizeReferentMethod == null) {
            return false;
        }
        do {
            reference.clear();
            if (reference == this.frqReference) {
                return false;
            }
            try {
                finalizeReferentMethod.invoke(reference, new Object[0]);
            } catch (Throwable th) {
                logger.log(Level.SEVERE, "Error cleaning up after reference.", th);
            }
            reference = this.queue.poll();
        } while (reference != null);
        return true;
    }

    @Nullable
    private Method getFinalizeReferentMethod() {
        Class cls = (Class) this.finalizableReferenceClassReference.get();
        if (cls == null) {
            return null;
        }
        try {
            return cls.getMethod("finalizeReferent", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    @Nullable
    public static Field getInheritableThreadLocalsField() {
        try {
            Field declaredField = Thread.class.getDeclaredField("inheritableThreadLocals");
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable unused) {
            logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
            return null;
        }
    }
}

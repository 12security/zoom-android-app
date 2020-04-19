package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;

@GwtCompatible(emulated = true)
abstract class InterruptibleTask implements Runnable {
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Logger log = Logger.getLogger(InterruptibleTask.class.getName());
    private volatile boolean doneInterrupting;
    /* access modifiers changed from: private */
    public volatile Thread runner;

    private static abstract class AtomicHelper {
        /* access modifiers changed from: 0000 */
        public abstract boolean compareAndSetRunner(InterruptibleTask interruptibleTask, Thread thread, Thread thread2);

        private AtomicHelper() {
        }
    }

    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<InterruptibleTask, Thread> runnerUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater atomicReferenceFieldUpdater) {
            super();
            this.runnerUpdater = atomicReferenceFieldUpdater;
        }

        /* access modifiers changed from: 0000 */
        public boolean compareAndSetRunner(InterruptibleTask interruptibleTask, Thread thread, Thread thread2) {
            return this.runnerUpdater.compareAndSet(interruptibleTask, thread, thread2);
        }
    }

    private static final class SynchronizedAtomicHelper extends AtomicHelper {
        private SynchronizedAtomicHelper() {
            super();
        }

        /* access modifiers changed from: 0000 */
        public boolean compareAndSetRunner(InterruptibleTask interruptibleTask, Thread thread, Thread thread2) {
            synchronized (interruptibleTask) {
                if (interruptibleTask.runner == thread) {
                    interruptibleTask.runner = thread2;
                }
            }
            return true;
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract void runInterruptibly();

    /* access modifiers changed from: 0000 */
    public abstract boolean wasInterrupted();

    InterruptibleTask() {
    }

    static {
        AtomicHelper atomicHelper;
        try {
            atomicHelper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(InterruptibleTask.class, Thread.class, "runner"));
        } catch (Throwable th) {
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", th);
            atomicHelper = new SynchronizedAtomicHelper();
        }
        ATOMIC_HELPER = atomicHelper;
    }

    public final void run() {
        if (ATOMIC_HELPER.compareAndSetRunner(this, null, Thread.currentThread())) {
            try {
                runInterruptibly();
            } finally {
                if (wasInterrupted()) {
                    while (!this.doneInterrupting) {
                        Thread.yield();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final void interruptTask() {
        Thread thread = this.runner;
        if (thread != null) {
            thread.interrupt();
        }
        this.doneInterrupting = true;
    }
}

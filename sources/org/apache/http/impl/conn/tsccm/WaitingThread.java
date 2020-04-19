package org.apache.http.impl.conn.tsccm;

import java.util.Date;
import java.util.concurrent.locks.Condition;
import org.apache.http.util.Args;

@Deprecated
public class WaitingThread {
    private boolean aborted;
    private final Condition cond;
    private final RouteSpecificPool pool;
    private Thread waiter;

    public WaitingThread(Condition condition, RouteSpecificPool routeSpecificPool) {
        Args.notNull(condition, "Condition");
        this.cond = condition;
        this.pool = routeSpecificPool;
    }

    public final Condition getCondition() {
        return this.cond;
    }

    public final RouteSpecificPool getPool() {
        return this.pool;
    }

    public final Thread getThread() {
        return this.waiter;
    }

    public boolean await(Date date) throws InterruptedException {
        boolean z;
        if (this.waiter != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("A thread is already waiting on this object.\ncaller: ");
            sb.append(Thread.currentThread());
            sb.append("\nwaiter: ");
            sb.append(this.waiter);
            throw new IllegalStateException(sb.toString());
        } else if (!this.aborted) {
            this.waiter = Thread.currentThread();
            if (date != null) {
                try {
                    z = this.cond.awaitUntil(date);
                } catch (Throwable th) {
                    this.waiter = null;
                    throw th;
                }
            } else {
                this.cond.await();
                z = true;
            }
            if (!this.aborted) {
                this.waiter = null;
                return z;
            }
            throw new InterruptedException("Operation interrupted");
        } else {
            throw new InterruptedException("Operation interrupted");
        }
    }

    public void wakeup() {
        if (this.waiter != null) {
            this.cond.signalAll();
            return;
        }
        throw new IllegalStateException("Nobody waiting on this object.");
    }

    public void interrupt() {
        this.aborted = true;
        this.cond.signalAll();
    }
}

package org.apache.http.pool;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.pool.PoolEntry;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
public abstract class AbstractConnPool<T, C, E extends PoolEntry<T, C>> implements ConnPool<T, E>, ConnPoolControl<T> {
    private final LinkedList<E> available = new LinkedList<>();
    /* access modifiers changed from: private */
    public final Condition condition = this.lock.newCondition();
    private final ConnFactory<T, C> connFactory;
    private volatile int defaultMaxPerRoute;
    private volatile boolean isShutDown;
    private final Set<E> leased = new HashSet();
    /* access modifiers changed from: private */
    public final Lock lock = new ReentrantLock();
    private final Map<T, Integer> maxPerRoute = new HashMap();
    private volatile int maxTotal;
    private final LinkedList<Future<E>> pending = new LinkedList<>();
    private final Map<T, RouteSpecificPool<T, C, E>> routeToPool = new HashMap();
    /* access modifiers changed from: private */
    public volatile int validateAfterInactivity;

    /* access modifiers changed from: protected */
    public abstract E createEntry(T t, C c);

    /* access modifiers changed from: protected */
    public void onLease(E e) {
    }

    /* access modifiers changed from: protected */
    public void onRelease(E e) {
    }

    /* access modifiers changed from: protected */
    public void onReuse(E e) {
    }

    /* access modifiers changed from: protected */
    public boolean validate(E e) {
        return true;
    }

    public AbstractConnPool(ConnFactory<T, C> connFactory2, int i, int i2) {
        this.connFactory = (ConnFactory) Args.notNull(connFactory2, "Connection factory");
        this.defaultMaxPerRoute = Args.positive(i, "Max per route value");
        this.maxTotal = Args.positive(i2, "Max total value");
    }

    public boolean isShutdown() {
        return this.isShutDown;
    }

    public void shutdown() throws IOException {
        if (!this.isShutDown) {
            this.isShutDown = true;
            this.lock.lock();
            try {
                Iterator it = this.available.iterator();
                while (it.hasNext()) {
                    ((PoolEntry) it.next()).close();
                }
                for (E close : this.leased) {
                    close.close();
                }
                for (RouteSpecificPool shutdown : this.routeToPool.values()) {
                    shutdown.shutdown();
                }
                this.routeToPool.clear();
                this.leased.clear();
                this.available.clear();
            } finally {
                this.lock.unlock();
            }
        }
    }

    private RouteSpecificPool<T, C, E> getPool(final T t) {
        RouteSpecificPool<T, C, E> routeSpecificPool = (RouteSpecificPool) this.routeToPool.get(t);
        if (routeSpecificPool != null) {
            return routeSpecificPool;
        }
        C44011 r0 = new RouteSpecificPool<T, C, E>(t) {
            /* access modifiers changed from: protected */
            public E createEntry(C c) {
                return AbstractConnPool.this.createEntry(t, c);
            }
        };
        this.routeToPool.put(t, r0);
        return r0;
    }

    public Future<E> lease(final T t, final Object obj, final FutureCallback<E> futureCallback) {
        Args.notNull(t, "Route");
        Asserts.check(!this.isShutDown, "Connection pool shut down");
        return new Future<E>() {
            private final AtomicBoolean cancelled = new AtomicBoolean(false);
            private final AtomicBoolean done = new AtomicBoolean(false);
            private final AtomicReference<E> entryRef = new AtomicReference<>(null);

            /* JADX INFO: finally extract failed */
            public boolean cancel(boolean z) {
                if (!this.cancelled.compareAndSet(false, true)) {
                    return false;
                }
                this.done.set(true);
                AbstractConnPool.this.lock.lock();
                try {
                    AbstractConnPool.this.condition.signalAll();
                    AbstractConnPool.this.lock.unlock();
                    FutureCallback futureCallback = futureCallback;
                    if (futureCallback != null) {
                        futureCallback.cancelled();
                    }
                    return true;
                } catch (Throwable th) {
                    AbstractConnPool.this.lock.unlock();
                    throw th;
                }
            }

            public boolean isCancelled() {
                return this.cancelled.get();
            }

            public boolean isDone() {
                return this.done.get();
            }

            public E get() throws InterruptedException, ExecutionException {
                try {
                    return get(0, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    throw new ExecutionException(e);
                }
            }

            public E get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                E access$200;
                E e = (PoolEntry) this.entryRef.get();
                if (e != null) {
                    return e;
                }
                synchronized (this) {
                    while (true) {
                        try {
                            access$200 = AbstractConnPool.this.getPoolEntryBlocking(t, obj, j, timeUnit, this);
                            if (AbstractConnPool.this.validateAfterInactivity <= 0 || access$200.getUpdated() + ((long) AbstractConnPool.this.validateAfterInactivity) > System.currentTimeMillis() || AbstractConnPool.this.validate(access$200)) {
                                this.entryRef.set(access$200);
                                this.done.set(true);
                                AbstractConnPool.this.onLease(access$200);
                            } else {
                                access$200.close();
                                AbstractConnPool.this.release(access$200, false);
                            }
                        } catch (IOException e2) {
                            this.done.set(true);
                            if (futureCallback != null) {
                                futureCallback.failed(e2);
                            }
                            throw new ExecutionException(e2);
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                    this.entryRef.set(access$200);
                    this.done.set(true);
                    AbstractConnPool.this.onLease(access$200);
                    if (futureCallback != null) {
                        futureCallback.completed(access$200);
                    }
                }
                return access$200;
            }
        };
    }

    public Future<E> lease(T t, Object obj) {
        return lease(t, obj, null);
    }

    /* access modifiers changed from: private */
    public E getPoolEntryBlocking(T t, Object obj, long j, TimeUnit timeUnit, Future<E> future) throws IOException, InterruptedException, TimeoutException {
        RouteSpecificPool pool;
        E free;
        Date date = j > 0 ? new Date(System.currentTimeMillis() + timeUnit.toMillis(j)) : null;
        this.lock.lock();
        try {
            pool = getPool(t);
            while (true) {
                boolean z = true;
                Asserts.check(!this.isShutDown, "Connection pool shut down");
                while (true) {
                    free = pool.getFree(obj);
                    if (free != null) {
                        if (free.isExpired(System.currentTimeMillis())) {
                            free.close();
                        }
                        if (!free.isClosed()) {
                            break;
                        }
                        this.available.remove(free);
                        pool.free(free, false);
                    } else {
                        break;
                    }
                }
                if (free != null) {
                    this.available.remove(free);
                    this.leased.add(free);
                    onReuse(free);
                    this.lock.unlock();
                    return free;
                }
                int max = getMax(t);
                int max2 = Math.max(0, (pool.getAllocatedCount() + 1) - max);
                if (max2 > 0) {
                    int i = 0;
                    while (true) {
                        if (i >= max2) {
                            break;
                        }
                        PoolEntry lastUsed = pool.getLastUsed();
                        if (lastUsed == null) {
                            break;
                        }
                        lastUsed.close();
                        this.available.remove(lastUsed);
                        pool.remove(lastUsed);
                        i++;
                    }
                }
                if (pool.getAllocatedCount() < max) {
                    int max3 = Math.max(this.maxTotal - this.leased.size(), 0);
                    if (max3 > 0) {
                        if (this.available.size() > max3 - 1 && !this.available.isEmpty()) {
                            PoolEntry poolEntry = (PoolEntry) this.available.removeLast();
                            poolEntry.close();
                            getPool(poolEntry.getRoute()).remove(poolEntry);
                        }
                        E add = pool.add(this.connFactory.create(t));
                        this.leased.add(add);
                        this.lock.unlock();
                        return add;
                    }
                }
                if (!future.isCancelled()) {
                    pool.queue(future);
                    this.pending.add(future);
                    if (date != null) {
                        z = this.condition.awaitUntil(date);
                    } else {
                        this.condition.await();
                    }
                    if (!future.isCancelled()) {
                        pool.unqueue(future);
                        this.pending.remove(future);
                        if (!z && date != null) {
                            if (date.getTime() <= System.currentTimeMillis()) {
                                throw new TimeoutException("Timeout waiting for connection");
                            }
                        }
                    } else {
                        throw new InterruptedException("Operation interrupted");
                    }
                } else {
                    throw new InterruptedException("Operation interrupted");
                }
            }
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }

    public void release(E e, boolean z) {
        this.lock.lock();
        try {
            if (this.leased.remove(e)) {
                RouteSpecificPool pool = getPool(e.getRoute());
                pool.free(e, z);
                if (!z || this.isShutDown) {
                    e.close();
                } else {
                    this.available.addFirst(e);
                }
                onRelease(e);
                Future nextPending = pool.nextPending();
                if (nextPending != null) {
                    this.pending.remove(nextPending);
                } else {
                    nextPending = (Future) this.pending.poll();
                }
                if (nextPending != null) {
                    this.condition.signalAll();
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    private int getMax(T t) {
        Integer num = (Integer) this.maxPerRoute.get(t);
        if (num != null) {
            return num.intValue();
        }
        return this.defaultMaxPerRoute;
    }

    public void setMaxTotal(int i) {
        Args.positive(i, "Max value");
        this.lock.lock();
        try {
            this.maxTotal = i;
        } finally {
            this.lock.unlock();
        }
    }

    public int getMaxTotal() {
        this.lock.lock();
        try {
            return this.maxTotal;
        } finally {
            this.lock.unlock();
        }
    }

    public void setDefaultMaxPerRoute(int i) {
        Args.positive(i, "Max per route value");
        this.lock.lock();
        try {
            this.defaultMaxPerRoute = i;
        } finally {
            this.lock.unlock();
        }
    }

    public int getDefaultMaxPerRoute() {
        this.lock.lock();
        try {
            return this.defaultMaxPerRoute;
        } finally {
            this.lock.unlock();
        }
    }

    public void setMaxPerRoute(T t, int i) {
        Args.notNull(t, "Route");
        Args.positive(i, "Max per route value");
        this.lock.lock();
        try {
            this.maxPerRoute.put(t, Integer.valueOf(i));
        } finally {
            this.lock.unlock();
        }
    }

    public int getMaxPerRoute(T t) {
        Args.notNull(t, "Route");
        this.lock.lock();
        try {
            return getMax(t);
        } finally {
            this.lock.unlock();
        }
    }

    public PoolStats getTotalStats() {
        this.lock.lock();
        try {
            return new PoolStats(this.leased.size(), this.pending.size(), this.available.size(), this.maxTotal);
        } finally {
            this.lock.unlock();
        }
    }

    public PoolStats getStats(T t) {
        Args.notNull(t, "Route");
        this.lock.lock();
        try {
            RouteSpecificPool pool = getPool(t);
            PoolStats poolStats = new PoolStats(pool.getLeasedCount(), pool.getPendingCount(), pool.getAvailableCount(), getMax(t));
            return poolStats;
        } finally {
            this.lock.unlock();
        }
    }

    public Set<T> getRoutes() {
        this.lock.lock();
        try {
            return new HashSet(this.routeToPool.keySet());
        } finally {
            this.lock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public void enumAvailable(PoolEntryCallback<T, C> poolEntryCallback) {
        this.lock.lock();
        try {
            Iterator it = this.available.iterator();
            while (it.hasNext()) {
                PoolEntry poolEntry = (PoolEntry) it.next();
                poolEntryCallback.process(poolEntry);
                if (poolEntry.isClosed()) {
                    getPool(poolEntry.getRoute()).remove(poolEntry);
                    it.remove();
                }
            }
            purgePoolMap();
        } finally {
            this.lock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public void enumLeased(PoolEntryCallback<T, C> poolEntryCallback) {
        this.lock.lock();
        try {
            for (E process : this.leased) {
                poolEntryCallback.process(process);
            }
        } finally {
            this.lock.unlock();
        }
    }

    private void purgePoolMap() {
        Iterator it = this.routeToPool.entrySet().iterator();
        while (it.hasNext()) {
            RouteSpecificPool routeSpecificPool = (RouteSpecificPool) ((Entry) it.next()).getValue();
            if (routeSpecificPool.getPendingCount() + routeSpecificPool.getAllocatedCount() == 0) {
                it.remove();
            }
        }
    }

    public void closeIdle(long j, TimeUnit timeUnit) {
        Args.notNull(timeUnit, "Time unit");
        long millis = timeUnit.toMillis(j);
        if (millis < 0) {
            millis = 0;
        }
        final long currentTimeMillis = System.currentTimeMillis() - millis;
        enumAvailable(new PoolEntryCallback<T, C>() {
            public void process(PoolEntry<T, C> poolEntry) {
                if (poolEntry.getUpdated() <= currentTimeMillis) {
                    poolEntry.close();
                }
            }
        });
    }

    public void closeExpired() {
        final long currentTimeMillis = System.currentTimeMillis();
        enumAvailable(new PoolEntryCallback<T, C>() {
            public void process(PoolEntry<T, C> poolEntry) {
                if (poolEntry.isExpired(currentTimeMillis)) {
                    poolEntry.close();
                }
            }
        });
    }

    public int getValidateAfterInactivity() {
        return this.validateAfterInactivity;
    }

    public void setValidateAfterInactivity(int i) {
        this.validateAfterInactivity = i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[leased: ");
        sb.append(this.leased);
        sb.append("][available: ");
        sb.append(this.available);
        sb.append("][pending: ");
        sb.append(this.pending);
        sb.append("]");
        return sb.toString();
    }
}

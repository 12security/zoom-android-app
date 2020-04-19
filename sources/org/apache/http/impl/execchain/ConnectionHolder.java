package org.apache.http.impl.execchain;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.logging.Log;
import org.apache.http.HttpClientConnection;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.conn.HttpClientConnectionManager;

@Contract(threading = ThreadingBehavior.SAFE)
class ConnectionHolder implements ConnectionReleaseTrigger, Cancellable, Closeable {
    private final Log log;
    private final HttpClientConnection managedConn;
    private final HttpClientConnectionManager manager;
    private final AtomicBoolean released = new AtomicBoolean(false);
    private volatile boolean reusable;
    private volatile Object state;
    private volatile TimeUnit tunit;
    private volatile long validDuration;

    public ConnectionHolder(Log log2, HttpClientConnectionManager httpClientConnectionManager, HttpClientConnection httpClientConnection) {
        this.log = log2;
        this.manager = httpClientConnectionManager;
        this.managedConn = httpClientConnection;
    }

    public boolean isReusable() {
        return this.reusable;
    }

    public void markReusable() {
        this.reusable = true;
    }

    public void markNonReusable() {
        this.reusable = false;
    }

    public void setState(Object obj) {
        this.state = obj;
    }

    public void setValidFor(long j, TimeUnit timeUnit) {
        synchronized (this.managedConn) {
            this.validDuration = j;
            this.tunit = timeUnit;
        }
    }

    private void releaseConnection(boolean z) {
        if (this.released.compareAndSet(false, true)) {
            synchronized (this.managedConn) {
                if (z) {
                    this.manager.releaseConnection(this.managedConn, this.state, this.validDuration, this.tunit);
                } else {
                    try {
                        this.managedConn.close();
                        this.log.debug("Connection discarded");
                        this.manager.releaseConnection(this.managedConn, null, 0, TimeUnit.MILLISECONDS);
                    } catch (IOException e) {
                        try {
                            if (this.log.isDebugEnabled()) {
                                this.log.debug(e.getMessage(), e);
                            }
                        } finally {
                            this.manager.releaseConnection(this.managedConn, null, 0, TimeUnit.MILLISECONDS);
                        }
                    }
                }
            }
        }
    }

    public void releaseConnection() {
        releaseConnection(this.reusable);
    }

    public void abortConnection() {
        if (this.released.compareAndSet(false, true)) {
            synchronized (this.managedConn) {
                try {
                    this.managedConn.shutdown();
                    this.log.debug("Connection discarded");
                    this.manager.releaseConnection(this.managedConn, null, 0, TimeUnit.MILLISECONDS);
                } catch (IOException e) {
                    try {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug(e.getMessage(), e);
                        }
                        this.manager.releaseConnection(this.managedConn, null, 0, TimeUnit.MILLISECONDS);
                    } catch (Throwable th) {
                        this.manager.releaseConnection(this.managedConn, null, 0, TimeUnit.MILLISECONDS);
                        throw th;
                    }
                }
            }
        }
    }

    public boolean cancel() {
        boolean z = this.released.get();
        this.log.debug("Cancelling request execution");
        abortConnection();
        return !z;
    }

    public boolean isReleased() {
        return this.released.get();
    }

    public void close() throws IOException {
        releaseConnection(false);
    }
}

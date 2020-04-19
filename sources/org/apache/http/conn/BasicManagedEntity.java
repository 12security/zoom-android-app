package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Deprecated
public class BasicManagedEntity extends HttpEntityWrapper implements ConnectionReleaseTrigger, EofSensorWatcher {
    protected final boolean attemptReuse;
    protected ManagedClientConnection managedConn;

    public boolean isRepeatable() {
        return false;
    }

    public BasicManagedEntity(HttpEntity httpEntity, ManagedClientConnection managedClientConnection, boolean z) {
        super(httpEntity);
        Args.notNull(managedClientConnection, "Connection");
        this.managedConn = managedClientConnection;
        this.attemptReuse = z;
    }

    public InputStream getContent() throws IOException {
        return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
    }

    private void ensureConsumed() throws IOException {
        ManagedClientConnection managedClientConnection = this.managedConn;
        if (managedClientConnection != null) {
            try {
                if (this.attemptReuse) {
                    EntityUtils.consume(this.wrappedEntity);
                    this.managedConn.markReusable();
                } else {
                    managedClientConnection.unmarkReusable();
                }
            } finally {
                releaseManagedConnection();
            }
        }
    }

    @Deprecated
    public void consumeContent() throws IOException {
        ensureConsumed();
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        super.writeTo(outputStream);
        ensureConsumed();
    }

    public void releaseConnection() throws IOException {
        ensureConsumed();
    }

    public void abortConnection() throws IOException {
        ManagedClientConnection managedClientConnection = this.managedConn;
        if (managedClientConnection != null) {
            try {
                managedClientConnection.abortConnection();
            } finally {
                this.managedConn = null;
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean eofDetected(InputStream inputStream) throws IOException {
        try {
            if (this.managedConn != null) {
                if (this.attemptReuse) {
                    inputStream.close();
                    this.managedConn.markReusable();
                } else {
                    this.managedConn.unmarkReusable();
                }
            }
            releaseManagedConnection();
            return false;
        } catch (Throwable th) {
            releaseManagedConnection();
            throw th;
        }
    }

    public boolean streamClosed(InputStream inputStream) throws IOException {
        boolean isOpen;
        try {
            if (this.managedConn != null) {
                if (this.attemptReuse) {
                    isOpen = this.managedConn.isOpen();
                    inputStream.close();
                    this.managedConn.markReusable();
                } else {
                    this.managedConn.unmarkReusable();
                }
            }
        } catch (SocketException e) {
            if (isOpen) {
                throw e;
            }
        } catch (Throwable th) {
            releaseManagedConnection();
            throw th;
        }
        releaseManagedConnection();
        return false;
    }

    public boolean streamAbort(InputStream inputStream) throws IOException {
        ManagedClientConnection managedClientConnection = this.managedConn;
        if (managedClientConnection != null) {
            managedClientConnection.abortConnection();
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void releaseManagedConnection() throws IOException {
        ManagedClientConnection managedClientConnection = this.managedConn;
        if (managedClientConnection != null) {
            try {
                managedClientConnection.releaseConnection();
            } finally {
                this.managedConn = null;
            }
        }
    }
}

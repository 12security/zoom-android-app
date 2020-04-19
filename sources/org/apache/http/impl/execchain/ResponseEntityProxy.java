package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.conn.EofSensorWatcher;
import org.apache.http.entity.HttpEntityWrapper;

class ResponseEntityProxy extends HttpEntityWrapper implements EofSensorWatcher {
    private final ConnectionHolder connHolder;

    public boolean isRepeatable() {
        return false;
    }

    public static void enchance(HttpResponse httpResponse, ConnectionHolder connectionHolder) {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null && entity.isStreaming() && connectionHolder != null) {
            httpResponse.setEntity(new ResponseEntityProxy(entity, connectionHolder));
        }
    }

    ResponseEntityProxy(HttpEntity httpEntity, ConnectionHolder connectionHolder) {
        super(httpEntity);
        this.connHolder = connectionHolder;
    }

    private void cleanup() throws IOException {
        ConnectionHolder connectionHolder = this.connHolder;
        if (connectionHolder != null) {
            connectionHolder.close();
        }
    }

    private void abortConnection() throws IOException {
        ConnectionHolder connectionHolder = this.connHolder;
        if (connectionHolder != null) {
            connectionHolder.abortConnection();
        }
    }

    public void releaseConnection() throws IOException {
        ConnectionHolder connectionHolder = this.connHolder;
        if (connectionHolder != null) {
            connectionHolder.releaseConnection();
        }
    }

    public InputStream getContent() throws IOException {
        return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
    }

    @Deprecated
    public void consumeContent() throws IOException {
        releaseConnection();
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        if (outputStream != null) {
            try {
                this.wrappedEntity.writeTo(outputStream);
            } catch (IOException e) {
                abortConnection();
                throw e;
            } catch (RuntimeException e2) {
                abortConnection();
                throw e2;
            } catch (Throwable th) {
                cleanup();
                throw th;
            }
        }
        releaseConnection();
        cleanup();
    }

    public boolean eofDetected(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                abortConnection();
                throw e;
            } catch (RuntimeException e2) {
                abortConnection();
                throw e2;
            } catch (Throwable th) {
                cleanup();
                throw th;
            }
        }
        releaseConnection();
        cleanup();
        return false;
    }

    public boolean streamClosed(InputStream inputStream) throws IOException {
        boolean z;
        try {
            z = this.connHolder != null && !this.connHolder.isReleased();
            if (inputStream != null) {
                inputStream.close();
            }
            releaseConnection();
        } catch (SocketException e) {
            if (z) {
                throw e;
            }
        } catch (IOException e2) {
            abortConnection();
            throw e2;
        } catch (RuntimeException e3) {
            try {
                abortConnection();
                throw e3;
            } catch (Throwable th) {
                cleanup();
                throw th;
            }
        }
        cleanup();
        return false;
    }

    public boolean streamAbort(InputStream inputStream) throws IOException {
        cleanup();
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ResponseEntityProxy{");
        sb.append(this.wrappedEntity);
        sb.append('}');
        return sb.toString();
    }
}

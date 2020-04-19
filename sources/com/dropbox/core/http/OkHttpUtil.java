package com.dropbox.core.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import okio.BufferedSink;
import okio.Okio;

final class OkHttpUtil {

    public static final class PipedStream implements Closeable {
        private static final int BUFFER_SIZE = 5242880;

        /* renamed from: in */
        private final PipedInputStream f61in = new PipedInputStream(BUFFER_SIZE);
        private final PipedOutputStream out;

        public PipedStream() {
            try {
                this.out = new PipedOutputStream(this.f61in);
            } catch (IOException unused) {
                throw new IllegalStateException("Unable to create piped stream for async upload request.");
            }
        }

        public OutputStream getOutputStream() {
            return this.out;
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(5:0|1|2|3|5) */
        /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0005 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void close() {
            /*
                r1 = this;
                java.io.PipedOutputStream r0 = r1.out     // Catch:{ IOException -> 0x0005 }
                r0.close()     // Catch:{ IOException -> 0x0005 }
            L_0x0005:
                java.io.PipedInputStream r0 = r1.f61in     // Catch:{ IOException -> 0x000a }
                r0.close()     // Catch:{ IOException -> 0x000a }
            L_0x000a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.http.OkHttpUtil.PipedStream.close():void");
        }

        public void writeTo(BufferedSink bufferedSink) throws IOException {
            bufferedSink.writeAll(Okio.source((InputStream) this.f61in));
        }
    }

    OkHttpUtil() {
    }

    public static void assertNotSameThreadExecutor(ExecutorService executorService) {
        Thread currentThread = Thread.currentThread();
        try {
            if (currentThread.equals((Thread) executorService.submit(new Callable<Thread>() {
                public Thread call() {
                    return Thread.currentThread();
                }
            }).get(2, TimeUnit.MINUTES))) {
                throw new IllegalArgumentException("OkHttp dispatcher uses same-thread executor. This is not supported by the SDK and may result in dead-locks. Please configure your Dispatcher to use an ExecutorService that runs tasks on separate threads.");
            }
        } catch (InterruptedException e) {
            currentThread.interrupt();
            throw new IllegalArgumentException("Unable to verify OkHttp dispatcher executor.", e);
        } catch (Exception e2) {
            throw new IllegalArgumentException("Unable to verify OkHttp dispatcher executor.", e2);
        }
    }
}

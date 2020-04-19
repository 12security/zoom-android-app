package org.apache.http.impl.client;

import java.util.concurrent.atomic.AtomicLong;

public final class FutureRequestExecutionMetrics {
    private final AtomicLong activeConnections = new AtomicLong();
    private final DurationCounter failedConnections = new DurationCounter();
    private final DurationCounter requests = new DurationCounter();
    private final AtomicLong scheduledConnections = new AtomicLong();
    private final DurationCounter successfulConnections = new DurationCounter();
    private final DurationCounter tasks = new DurationCounter();

    static class DurationCounter {
        private final AtomicLong count = new AtomicLong(0);
        private final AtomicLong cumulativeDuration = new AtomicLong(0);

        DurationCounter() {
        }

        public void increment(long j) {
            this.count.incrementAndGet();
            this.cumulativeDuration.addAndGet(System.currentTimeMillis() - j);
        }

        public long count() {
            return this.count.get();
        }

        public long averageDuration() {
            long j = this.count.get();
            if (j > 0) {
                return this.cumulativeDuration.get() / j;
            }
            return 0;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[count=");
            sb.append(count());
            sb.append(", averageDuration=");
            sb.append(averageDuration());
            sb.append("]");
            return sb.toString();
        }
    }

    FutureRequestExecutionMetrics() {
    }

    /* access modifiers changed from: 0000 */
    public AtomicLong getActiveConnections() {
        return this.activeConnections;
    }

    /* access modifiers changed from: 0000 */
    public AtomicLong getScheduledConnections() {
        return this.scheduledConnections;
    }

    /* access modifiers changed from: 0000 */
    public DurationCounter getSuccessfulConnections() {
        return this.successfulConnections;
    }

    /* access modifiers changed from: 0000 */
    public DurationCounter getFailedConnections() {
        return this.failedConnections;
    }

    /* access modifiers changed from: 0000 */
    public DurationCounter getRequests() {
        return this.requests;
    }

    /* access modifiers changed from: 0000 */
    public DurationCounter getTasks() {
        return this.tasks;
    }

    public long getActiveConnectionCount() {
        return this.activeConnections.get();
    }

    public long getScheduledConnectionCount() {
        return this.scheduledConnections.get();
    }

    public long getSuccessfulConnectionCount() {
        return this.successfulConnections.count();
    }

    public long getSuccessfulConnectionAverageDuration() {
        return this.successfulConnections.averageDuration();
    }

    public long getFailedConnectionCount() {
        return this.failedConnections.count();
    }

    public long getFailedConnectionAverageDuration() {
        return this.failedConnections.averageDuration();
    }

    public long getRequestCount() {
        return this.requests.count();
    }

    public long getRequestAverageDuration() {
        return this.requests.averageDuration();
    }

    public long getTaskCount() {
        return this.tasks.count();
    }

    public long getTaskAverageDuration() {
        return this.tasks.averageDuration();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[activeConnections=");
        sb.append(this.activeConnections);
        sb.append(", scheduledConnections=");
        sb.append(this.scheduledConnections);
        sb.append(", successfulConnections=");
        sb.append(this.successfulConnections);
        sb.append(", failedConnections=");
        sb.append(this.failedConnections);
        sb.append(", requests=");
        sb.append(this.requests);
        sb.append(", tasks=");
        sb.append(this.tasks);
        sb.append("]");
        return sb.toString();
    }
}

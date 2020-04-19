package com.google.common.util.concurrent;

import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor.Guard;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

@GwtIncompatible
@Beta
public abstract class AbstractService implements Service {
    private static final Callback<Listener> RUNNING_CALLBACK = new Callback<Listener>("running()") {
        /* access modifiers changed from: 0000 */
        public void call(Listener listener) {
            listener.running();
        }
    };
    private static final Callback<Listener> STARTING_CALLBACK = new Callback<Listener>("starting()") {
        /* access modifiers changed from: 0000 */
        public void call(Listener listener) {
            listener.starting();
        }
    };
    private static final Callback<Listener> STOPPING_FROM_RUNNING_CALLBACK = stoppingCallback(State.RUNNING);
    private static final Callback<Listener> STOPPING_FROM_STARTING_CALLBACK = stoppingCallback(State.STARTING);
    private static final Callback<Listener> TERMINATED_FROM_NEW_CALLBACK = terminatedCallback(State.NEW);
    private static final Callback<Listener> TERMINATED_FROM_RUNNING_CALLBACK = terminatedCallback(State.RUNNING);
    private static final Callback<Listener> TERMINATED_FROM_STOPPING_CALLBACK = terminatedCallback(State.STOPPING);
    private final Guard hasReachedRunning = new HasReachedRunningGuard();
    private final Guard isStartable = new IsStartableGuard();
    private final Guard isStoppable = new IsStoppableGuard();
    private final Guard isStopped = new IsStoppedGuard();
    @GuardedBy("monitor")
    private final List<ListenerCallQueue<Listener>> listeners = Collections.synchronizedList(new ArrayList());
    /* access modifiers changed from: private */
    public final Monitor monitor = new Monitor();
    @GuardedBy("monitor")
    private volatile StateSnapshot snapshot = new StateSnapshot(State.NEW);

    private final class HasReachedRunningGuard extends Guard {
        HasReachedRunningGuard() {
            super(AbstractService.this.monitor);
        }

        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(State.RUNNING) >= 0;
        }
    }

    private final class IsStartableGuard extends Guard {
        IsStartableGuard() {
            super(AbstractService.this.monitor);
        }

        public boolean isSatisfied() {
            return AbstractService.this.state() == State.NEW;
        }
    }

    private final class IsStoppableGuard extends Guard {
        IsStoppableGuard() {
            super(AbstractService.this.monitor);
        }

        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(State.RUNNING) <= 0;
        }
    }

    private final class IsStoppedGuard extends Guard {
        IsStoppedGuard() {
            super(AbstractService.this.monitor);
        }

        public boolean isSatisfied() {
            return AbstractService.this.state().isTerminal();
        }
    }

    @Immutable
    private static final class StateSnapshot {
        @Nullable
        final Throwable failure;
        final boolean shutdownWhenStartupFinishes;
        final State state;

        StateSnapshot(State state2) {
            this(state2, false, null);
        }

        StateSnapshot(State state2, boolean z, @Nullable Throwable th) {
            boolean z2 = false;
            Preconditions.checkArgument(!z || state2 == State.STARTING, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", (Object) state2);
            if (!((th != null) ^ (state2 == State.FAILED))) {
                z2 = true;
            }
            Preconditions.checkArgument(z2, "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", (Object) state2, (Object) th);
            this.state = state2;
            this.shutdownWhenStartupFinishes = z;
            this.failure = th;
        }

        /* access modifiers changed from: 0000 */
        public State externalState() {
            if (!this.shutdownWhenStartupFinishes || this.state != State.STARTING) {
                return this.state;
            }
            return State.STOPPING;
        }

        /* access modifiers changed from: 0000 */
        public Throwable failureCause() {
            Preconditions.checkState(this.state == State.FAILED, "failureCause() is only valid if the service has failed, service is %s", (Object) this.state);
            return this.failure;
        }
    }

    /* access modifiers changed from: protected */
    public abstract void doStart();

    /* access modifiers changed from: protected */
    public abstract void doStop();

    private static Callback<Listener> terminatedCallback(final State state) {
        StringBuilder sb = new StringBuilder();
        sb.append("terminated({from = ");
        sb.append(state);
        sb.append("})");
        return new Callback<Listener>(sb.toString()) {
            /* access modifiers changed from: 0000 */
            public void call(Listener listener) {
                listener.terminated(state);
            }
        };
    }

    private static Callback<Listener> stoppingCallback(final State state) {
        StringBuilder sb = new StringBuilder();
        sb.append("stopping({from = ");
        sb.append(state);
        sb.append("})");
        return new Callback<Listener>(sb.toString()) {
            /* access modifiers changed from: 0000 */
            public void call(Listener listener) {
                listener.stopping(state);
            }
        };
    }

    protected AbstractService() {
    }

    @CanIgnoreReturnValue
    public final Service startAsync() {
        if (this.monitor.enterIf(this.isStartable)) {
            try {
                this.snapshot = new StateSnapshot(State.STARTING);
                starting();
                doStart();
            } catch (Throwable th) {
                this.monitor.leave();
                executeListeners();
                throw th;
            }
            this.monitor.leave();
            executeListeners();
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Service ");
        sb.append(this);
        sb.append(" has already been started");
        throw new IllegalStateException(sb.toString());
    }

    @CanIgnoreReturnValue
    public final Service stopAsync() {
        if (this.monitor.enterIf(this.isStoppable)) {
            try {
                State state = state();
                switch (state) {
                    case NEW:
                        this.snapshot = new StateSnapshot(State.TERMINATED);
                        terminated(State.NEW);
                        break;
                    case STARTING:
                        this.snapshot = new StateSnapshot(State.STARTING, true, null);
                        stopping(State.STARTING);
                        break;
                    case RUNNING:
                        this.snapshot = new StateSnapshot(State.STOPPING);
                        stopping(State.RUNNING);
                        doStop();
                        break;
                    case STOPPING:
                    case TERMINATED:
                    case FAILED:
                        StringBuilder sb = new StringBuilder();
                        sb.append("isStoppable is incorrectly implemented, saw: ");
                        sb.append(state);
                        throw new AssertionError(sb.toString());
                    default:
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Unexpected state: ");
                        sb2.append(state);
                        throw new AssertionError(sb2.toString());
                }
            } catch (Throwable th) {
                this.monitor.leave();
                executeListeners();
                throw th;
            }
            this.monitor.leave();
            executeListeners();
        }
        return this;
    }

    public final void awaitRunning() {
        this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
        try {
            checkCurrentState(State.RUNNING);
        } finally {
            this.monitor.leave();
        }
    }

    public final void awaitRunning(long j, TimeUnit timeUnit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, j, timeUnit)) {
            try {
                checkCurrentState(State.RUNNING);
            } finally {
                this.monitor.leave();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Timed out waiting for ");
            sb.append(this);
            sb.append(" to reach the RUNNING state.");
            throw new TimeoutException(sb.toString());
        }
    }

    public final void awaitTerminated() {
        this.monitor.enterWhenUninterruptibly(this.isStopped);
        try {
            checkCurrentState(State.TERMINATED);
        } finally {
            this.monitor.leave();
        }
    }

    public final void awaitTerminated(long j, TimeUnit timeUnit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.isStopped, j, timeUnit)) {
            try {
                checkCurrentState(State.TERMINATED);
            } finally {
                this.monitor.leave();
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Timed out waiting for ");
            sb.append(this);
            sb.append(" to reach a terminal state. ");
            sb.append("Current state: ");
            sb.append(state());
            throw new TimeoutException(sb.toString());
        }
    }

    @GuardedBy("monitor")
    private void checkCurrentState(State state) {
        State state2 = state();
        if (state2 == state) {
            return;
        }
        if (state2 == State.FAILED) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected the service ");
            sb.append(this);
            sb.append(" to be ");
            sb.append(state);
            sb.append(", but the service has FAILED");
            throw new IllegalStateException(sb.toString(), failureCause());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Expected the service ");
        sb2.append(this);
        sb2.append(" to be ");
        sb2.append(state);
        sb2.append(", but was ");
        sb2.append(state2);
        throw new IllegalStateException(sb2.toString());
    }

    /* access modifiers changed from: protected */
    public final void notifyStarted() {
        this.monitor.enter();
        try {
            if (this.snapshot.state == State.STARTING) {
                if (this.snapshot.shutdownWhenStartupFinishes) {
                    this.snapshot = new StateSnapshot(State.STOPPING);
                    doStop();
                } else {
                    this.snapshot = new StateSnapshot(State.RUNNING);
                    running();
                }
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot notifyStarted() when the service is ");
            sb.append(this.snapshot.state);
            IllegalStateException illegalStateException = new IllegalStateException(sb.toString());
            notifyFailed(illegalStateException);
            throw illegalStateException;
        } finally {
            this.monitor.leave();
            executeListeners();
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyStopped() {
        this.monitor.enter();
        try {
            State state = this.snapshot.state;
            if (state != State.STOPPING) {
                if (state != State.RUNNING) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Cannot notifyStopped() when the service is ");
                    sb.append(state);
                    IllegalStateException illegalStateException = new IllegalStateException(sb.toString());
                    notifyFailed(illegalStateException);
                    throw illegalStateException;
                }
            }
            this.snapshot = new StateSnapshot(State.TERMINATED);
            terminated(state);
        } finally {
            this.monitor.leave();
            executeListeners();
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyFailed(Throwable th) {
        Preconditions.checkNotNull(th);
        this.monitor.enter();
        try {
            State state = state();
            switch (state) {
                case NEW:
                case TERMINATED:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed while in state:");
                    sb.append(state);
                    throw new IllegalStateException(sb.toString(), th);
                case STARTING:
                case RUNNING:
                case STOPPING:
                    this.snapshot = new StateSnapshot(State.FAILED, false, th);
                    failed(state, th);
                    break;
                case FAILED:
                    break;
                default:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unexpected state: ");
                    sb2.append(state);
                    throw new AssertionError(sb2.toString());
            }
        } finally {
            this.monitor.leave();
            executeListeners();
        }
    }

    public final boolean isRunning() {
        return state() == State.RUNNING;
    }

    public final State state() {
        return this.snapshot.externalState();
    }

    public final Throwable failureCause() {
        return this.snapshot.failureCause();
    }

    public final void addListener(Listener listener, Executor executor) {
        Preconditions.checkNotNull(listener, CastExtraArgs.LISTENER);
        Preconditions.checkNotNull(executor, "executor");
        this.monitor.enter();
        try {
            if (!state().isTerminal()) {
                this.listeners.add(new ListenerCallQueue(listener, executor));
            }
        } finally {
            this.monitor.leave();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(state());
        sb.append("]");
        return sb.toString();
    }

    private void executeListeners() {
        if (!this.monitor.isOccupiedByCurrentThread()) {
            for (int i = 0; i < this.listeners.size(); i++) {
                ((ListenerCallQueue) this.listeners.get(i)).execute();
            }
        }
    }

    @GuardedBy("monitor")
    private void starting() {
        STARTING_CALLBACK.enqueueOn(this.listeners);
    }

    @GuardedBy("monitor")
    private void running() {
        RUNNING_CALLBACK.enqueueOn(this.listeners);
    }

    @GuardedBy("monitor")
    private void stopping(State state) {
        if (state == State.STARTING) {
            STOPPING_FROM_STARTING_CALLBACK.enqueueOn(this.listeners);
        } else if (state == State.RUNNING) {
            STOPPING_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
        } else {
            throw new AssertionError();
        }
    }

    @GuardedBy("monitor")
    private void terminated(State state) {
        int i = C15136.$SwitchMap$com$google$common$util$concurrent$Service$State[state.ordinal()];
        if (i != 1) {
            switch (i) {
                case 3:
                    TERMINATED_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
                    return;
                case 4:
                    TERMINATED_FROM_STOPPING_CALLBACK.enqueueOn(this.listeners);
                    return;
                default:
                    throw new AssertionError();
            }
        } else {
            TERMINATED_FROM_NEW_CALLBACK.enqueueOn(this.listeners);
        }
    }

    @GuardedBy("monitor")
    private void failed(final State state, final Throwable th) {
        StringBuilder sb = new StringBuilder();
        sb.append("failed({from = ");
        sb.append(state);
        sb.append(", cause = ");
        sb.append(th);
        sb.append("})");
        new Callback<Listener>(sb.toString()) {
            /* access modifiers changed from: 0000 */
            public void call(Listener listener) {
                listener.failed(state, th);
            }
        }.enqueueOn(this.listeners);
    }
}

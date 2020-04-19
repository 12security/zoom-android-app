package com.google.api.client.util;

import java.io.IOException;

public class ExponentialBackOff implements BackOff {
    public static final int DEFAULT_INITIAL_INTERVAL_MILLIS = 500;
    public static final int DEFAULT_MAX_ELAPSED_TIME_MILLIS = 900000;
    public static final int DEFAULT_MAX_INTERVAL_MILLIS = 60000;
    public static final double DEFAULT_MULTIPLIER = 1.5d;
    public static final double DEFAULT_RANDOMIZATION_FACTOR = 0.5d;
    private int currentIntervalMillis;
    private final int initialIntervalMillis;
    private final int maxElapsedTimeMillis;
    private final int maxIntervalMillis;
    private final double multiplier;
    private final NanoClock nanoClock;
    private final double randomizationFactor;
    long startTimeNanos;

    public static class Builder {
        int initialIntervalMillis = 500;
        int maxElapsedTimeMillis = 900000;
        int maxIntervalMillis = 60000;
        double multiplier = 1.5d;
        NanoClock nanoClock = NanoClock.SYSTEM;
        double randomizationFactor = 0.5d;

        public ExponentialBackOff build() {
            return new ExponentialBackOff(this);
        }

        public final int getInitialIntervalMillis() {
            return this.initialIntervalMillis;
        }

        public Builder setInitialIntervalMillis(int i) {
            this.initialIntervalMillis = i;
            return this;
        }

        public final double getRandomizationFactor() {
            return this.randomizationFactor;
        }

        public Builder setRandomizationFactor(double d) {
            this.randomizationFactor = d;
            return this;
        }

        public final double getMultiplier() {
            return this.multiplier;
        }

        public Builder setMultiplier(double d) {
            this.multiplier = d;
            return this;
        }

        public final int getMaxIntervalMillis() {
            return this.maxIntervalMillis;
        }

        public Builder setMaxIntervalMillis(int i) {
            this.maxIntervalMillis = i;
            return this;
        }

        public final int getMaxElapsedTimeMillis() {
            return this.maxElapsedTimeMillis;
        }

        public Builder setMaxElapsedTimeMillis(int i) {
            this.maxElapsedTimeMillis = i;
            return this;
        }

        public final NanoClock getNanoClock() {
            return this.nanoClock;
        }

        public Builder setNanoClock(NanoClock nanoClock2) {
            this.nanoClock = (NanoClock) Preconditions.checkNotNull(nanoClock2);
            return this;
        }
    }

    static int getRandomValueFromInterval(double d, double d2, int i) {
        double d3 = (double) i;
        double d4 = d * d3;
        double d5 = d3 - d4;
        return (int) (d5 + (d2 * (((d3 + d4) - d5) + 1.0d)));
    }

    public ExponentialBackOff() {
        this(new Builder());
    }

    protected ExponentialBackOff(Builder builder) {
        this.initialIntervalMillis = builder.initialIntervalMillis;
        this.randomizationFactor = builder.randomizationFactor;
        this.multiplier = builder.multiplier;
        this.maxIntervalMillis = builder.maxIntervalMillis;
        this.maxElapsedTimeMillis = builder.maxElapsedTimeMillis;
        this.nanoClock = builder.nanoClock;
        boolean z = true;
        Preconditions.checkArgument(this.initialIntervalMillis > 0);
        double d = this.randomizationFactor;
        Preconditions.checkArgument(0.0d <= d && d < 1.0d);
        Preconditions.checkArgument(this.multiplier >= 1.0d);
        Preconditions.checkArgument(this.maxIntervalMillis >= this.initialIntervalMillis);
        if (this.maxElapsedTimeMillis <= 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        reset();
    }

    public final void reset() {
        this.currentIntervalMillis = this.initialIntervalMillis;
        this.startTimeNanos = this.nanoClock.nanoTime();
    }

    public long nextBackOffMillis() throws IOException {
        if (getElapsedTimeMillis() > ((long) this.maxElapsedTimeMillis)) {
            return -1;
        }
        int randomValueFromInterval = getRandomValueFromInterval(this.randomizationFactor, Math.random(), this.currentIntervalMillis);
        incrementCurrentInterval();
        return (long) randomValueFromInterval;
    }

    public final int getInitialIntervalMillis() {
        return this.initialIntervalMillis;
    }

    public final double getRandomizationFactor() {
        return this.randomizationFactor;
    }

    public final int getCurrentIntervalMillis() {
        return this.currentIntervalMillis;
    }

    public final double getMultiplier() {
        return this.multiplier;
    }

    public final int getMaxIntervalMillis() {
        return this.maxIntervalMillis;
    }

    public final int getMaxElapsedTimeMillis() {
        return this.maxElapsedTimeMillis;
    }

    public final long getElapsedTimeMillis() {
        return (this.nanoClock.nanoTime() - this.startTimeNanos) / 1000000;
    }

    private void incrementCurrentInterval() {
        int i = this.currentIntervalMillis;
        double d = (double) i;
        int i2 = this.maxIntervalMillis;
        double d2 = (double) i2;
        double d3 = this.multiplier;
        if (d >= d2 / d3) {
            this.currentIntervalMillis = i2;
        } else {
            this.currentIntervalMillis = (int) (((double) i) * d3);
        }
    }
}

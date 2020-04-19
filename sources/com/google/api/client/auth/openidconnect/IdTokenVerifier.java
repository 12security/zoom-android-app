package com.google.api.client.auth.openidconnect;

import com.google.api.client.util.Beta;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Preconditions;
import java.util.Collection;
import java.util.Collections;

@Beta
public class IdTokenVerifier {
    public static final long DEFAULT_TIME_SKEW_SECONDS = 300;
    private final long acceptableTimeSkewSeconds;
    private final Collection<String> audience;
    private final Clock clock;
    private final Collection<String> issuers;

    @Beta
    public static class Builder {
        long acceptableTimeSkewSeconds = 300;
        Collection<String> audience;
        Clock clock = Clock.SYSTEM;
        Collection<String> issuers;

        public IdTokenVerifier build() {
            return new IdTokenVerifier(this);
        }

        public final Clock getClock() {
            return this.clock;
        }

        public Builder setClock(Clock clock2) {
            this.clock = (Clock) Preconditions.checkNotNull(clock2);
            return this;
        }

        public final String getIssuer() {
            Collection<String> collection = this.issuers;
            if (collection == null) {
                return null;
            }
            return (String) collection.iterator().next();
        }

        public Builder setIssuer(String str) {
            if (str == null) {
                return setIssuers(null);
            }
            return setIssuers(Collections.singleton(str));
        }

        public final Collection<String> getIssuers() {
            return this.issuers;
        }

        public Builder setIssuers(Collection<String> collection) {
            Preconditions.checkArgument(collection == null || !collection.isEmpty(), "Issuers must not be empty");
            this.issuers = collection;
            return this;
        }

        public final Collection<String> getAudience() {
            return this.audience;
        }

        public Builder setAudience(Collection<String> collection) {
            this.audience = collection;
            return this;
        }

        public final long getAcceptableTimeSkewSeconds() {
            return this.acceptableTimeSkewSeconds;
        }

        public Builder setAcceptableTimeSkewSeconds(long j) {
            Preconditions.checkArgument(j >= 0);
            this.acceptableTimeSkewSeconds = j;
            return this;
        }
    }

    public IdTokenVerifier() {
        this(new Builder());
    }

    protected IdTokenVerifier(Builder builder) {
        this.clock = builder.clock;
        this.acceptableTimeSkewSeconds = builder.acceptableTimeSkewSeconds;
        Collection<String> collection = null;
        this.issuers = builder.issuers == null ? null : Collections.unmodifiableCollection(builder.issuers);
        if (builder.audience != null) {
            collection = Collections.unmodifiableCollection(builder.audience);
        }
        this.audience = collection;
    }

    public final Clock getClock() {
        return this.clock;
    }

    public final long getAcceptableTimeSkewSeconds() {
        return this.acceptableTimeSkewSeconds;
    }

    public final String getIssuer() {
        Collection<String> collection = this.issuers;
        if (collection == null) {
            return null;
        }
        return (String) collection.iterator().next();
    }

    public final Collection<String> getIssuers() {
        return this.issuers;
    }

    public final Collection<String> getAudience() {
        return this.audience;
    }

    public boolean verify(IdToken idToken) {
        Collection<String> collection = this.issuers;
        if (collection == null || idToken.verifyIssuer(collection)) {
            Collection<String> collection2 = this.audience;
            if ((collection2 == null || idToken.verifyAudience(collection2)) && idToken.verifyTime(this.clock.currentTimeMillis(), this.acceptableTimeSkewSeconds)) {
                return true;
            }
        }
        return false;
    }
}

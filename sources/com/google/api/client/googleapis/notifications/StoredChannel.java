package com.google.api.client.googleapis.notifications;

import com.google.api.client.util.Beta;
import com.google.api.client.util.Objects;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Beta
public final class StoredChannel implements Serializable {
    public static final String DEFAULT_DATA_STORE_ID = "StoredChannel";
    private static final long serialVersionUID = 1;
    private String clientToken;
    private Long expiration;

    /* renamed from: id */
    private final String f203id;
    private final Lock lock;
    private final UnparsedNotificationCallback notificationCallback;
    private String topicId;

    public StoredChannel(UnparsedNotificationCallback unparsedNotificationCallback) {
        this(unparsedNotificationCallback, NotificationUtils.randomUuidString());
    }

    public StoredChannel(UnparsedNotificationCallback unparsedNotificationCallback, String str) {
        this.lock = new ReentrantLock();
        this.notificationCallback = (UnparsedNotificationCallback) Preconditions.checkNotNull(unparsedNotificationCallback);
        this.f203id = (String) Preconditions.checkNotNull(str);
    }

    public StoredChannel store(DataStoreFactory dataStoreFactory) throws IOException {
        return store(getDefaultDataStore(dataStoreFactory));
    }

    public StoredChannel store(DataStore<StoredChannel> dataStore) throws IOException {
        this.lock.lock();
        try {
            dataStore.set(getId(), this);
            return this;
        } finally {
            this.lock.unlock();
        }
    }

    public UnparsedNotificationCallback getNotificationCallback() {
        this.lock.lock();
        try {
            return this.notificationCallback;
        } finally {
            this.lock.unlock();
        }
    }

    public String getClientToken() {
        this.lock.lock();
        try {
            return this.clientToken;
        } finally {
            this.lock.unlock();
        }
    }

    public StoredChannel setClientToken(String str) {
        this.lock.lock();
        try {
            this.clientToken = str;
            return this;
        } finally {
            this.lock.unlock();
        }
    }

    public Long getExpiration() {
        this.lock.lock();
        try {
            return this.expiration;
        } finally {
            this.lock.unlock();
        }
    }

    public StoredChannel setExpiration(Long l) {
        this.lock.lock();
        try {
            this.expiration = l;
            return this;
        } finally {
            this.lock.unlock();
        }
    }

    public String getId() {
        this.lock.lock();
        try {
            return this.f203id;
        } finally {
            this.lock.unlock();
        }
    }

    public String getTopicId() {
        this.lock.lock();
        try {
            return this.topicId;
        } finally {
            this.lock.unlock();
        }
    }

    public StoredChannel setTopicId(String str) {
        this.lock.lock();
        try {
            this.topicId = str;
            return this;
        } finally {
            this.lock.unlock();
        }
    }

    public String toString() {
        return Objects.toStringHelper(StoredChannel.class).add("notificationCallback", getNotificationCallback()).add("clientToken", getClientToken()).add("expiration", getExpiration()).add("id", getId()).add("topicId", getTopicId()).toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StoredChannel)) {
            return false;
        }
        return getId().equals(((StoredChannel) obj).getId());
    }

    public int hashCode() {
        return getId().hashCode();
    }

    public static DataStore<StoredChannel> getDefaultDataStore(DataStoreFactory dataStoreFactory) throws IOException {
        return dataStoreFactory.getDataStore(DEFAULT_DATA_STORE_ID);
    }
}

package com.google.api.client.util.store;

import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.io.Serializable;

public abstract class AbstractDataStore<V extends Serializable> implements DataStore<V> {
    private final DataStoreFactory dataStoreFactory;

    /* renamed from: id */
    private final String f205id;

    protected AbstractDataStore(DataStoreFactory dataStoreFactory2, String str) {
        this.dataStoreFactory = (DataStoreFactory) Preconditions.checkNotNull(dataStoreFactory2);
        this.f205id = (String) Preconditions.checkNotNull(str);
    }

    public DataStoreFactory getDataStoreFactory() {
        return this.dataStoreFactory;
    }

    public final String getId() {
        return this.f205id;
    }

    public boolean containsKey(String str) throws IOException {
        return get(str) != null;
    }

    public boolean containsValue(V v) throws IOException {
        return values().contains(v);
    }

    public boolean isEmpty() throws IOException {
        return size() == 0;
    }

    public int size() throws IOException {
        return keySet().size();
    }
}

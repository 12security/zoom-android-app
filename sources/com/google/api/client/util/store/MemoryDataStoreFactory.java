package com.google.api.client.util.store;

import java.io.IOException;
import java.io.Serializable;

public class MemoryDataStoreFactory extends AbstractDataStoreFactory {

    static class InstanceHolder {
        static final MemoryDataStoreFactory INSTANCE = new MemoryDataStoreFactory();

        InstanceHolder() {
        }
    }

    static class MemoryDataStore<V extends Serializable> extends AbstractMemoryDataStore<V> {
        MemoryDataStore(MemoryDataStoreFactory memoryDataStoreFactory, String str) {
            super(memoryDataStoreFactory, str);
        }

        public MemoryDataStoreFactory getDataStoreFactory() {
            return (MemoryDataStoreFactory) super.getDataStoreFactory();
        }
    }

    /* access modifiers changed from: protected */
    public <V extends Serializable> DataStore<V> createDataStore(String str) throws IOException {
        return new MemoryDataStore(this, str);
    }

    public static MemoryDataStoreFactory getDefaultInstance() {
        return InstanceHolder.INSTANCE;
    }
}

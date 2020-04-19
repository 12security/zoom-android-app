package com.zipow.videobox.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class WebSearchResult implements Serializable {
    private static final long serialVersionUID = 1;
    @Nullable
    private String mKey;
    @NonNull
    private HashMap<String, IMAddrBookItem> results = new HashMap<>();

    @Nullable
    public IMAddrBookItem findByJid(String str) {
        return (IMAddrBookItem) this.results.get(str);
    }

    public void setKey(@Nullable String str) {
        this.mKey = str;
    }

    @Nullable
    public String getKey() {
        return this.mKey;
    }

    public void putItem(String str, IMAddrBookItem iMAddrBookItem) {
        this.results.put(str, iMAddrBookItem);
    }

    @NonNull
    public Set<String> getJids() {
        return this.results.keySet();
    }

    @NonNull
    public Collection<IMAddrBookItem> getItems() {
        return this.results.values();
    }

    public void clear() {
        this.mKey = null;
        this.results.clear();
    }
}

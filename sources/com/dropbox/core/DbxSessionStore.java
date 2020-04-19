package com.dropbox.core;

public interface DbxSessionStore {
    void clear();

    String get();

    void set(String str);
}

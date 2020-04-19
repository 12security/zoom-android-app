package com.dropbox.core;

import javax.servlet.http.HttpSession;

public final class DbxStandardSessionStore implements DbxSessionStore {
    private final String key;
    private final HttpSession session;

    public DbxStandardSessionStore(HttpSession httpSession, String str) {
        this.session = httpSession;
        this.key = str;
    }

    public HttpSession getSession() {
        return this.session;
    }

    public String getKey() {
        return this.key;
    }

    public String get() {
        Object attribute = this.session.getAttribute(this.key);
        if (attribute instanceof String) {
            return (String) attribute;
        }
        return null;
    }

    public void set(String str) {
        this.session.setAttribute(this.key, str);
    }

    public void clear() {
        this.session.removeAttribute(this.key);
    }
}

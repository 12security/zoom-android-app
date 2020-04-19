package org.apache.http.impl.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieIdentityComparator;

@Contract(threading = ThreadingBehavior.SAFE)
public class BasicCookieStore implements CookieStore, Serializable {
    private static final long serialVersionUID = -7581093305228232025L;
    private final TreeSet<Cookie> cookies = new TreeSet<>(new CookieIdentityComparator());
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void addCookie(Cookie cookie) {
        if (cookie != null) {
            this.lock.writeLock().lock();
            try {
                this.cookies.remove(cookie);
                if (!cookie.isExpired(new Date())) {
                    this.cookies.add(cookie);
                }
            } finally {
                this.lock.writeLock().unlock();
            }
        }
    }

    public void addCookies(Cookie[] cookieArr) {
        if (cookieArr != null) {
            for (Cookie addCookie : cookieArr) {
                addCookie(addCookie);
            }
        }
    }

    public List<Cookie> getCookies() {
        this.lock.readLock().lock();
        try {
            return new ArrayList(this.cookies);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public boolean clearExpired(Date date) {
        boolean z = false;
        if (date == null) {
            return false;
        }
        this.lock.writeLock().lock();
        try {
            Iterator it = this.cookies.iterator();
            while (it.hasNext()) {
                if (((Cookie) it.next()).isExpired(date)) {
                    it.remove();
                    z = true;
                }
            }
            return z;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void clear() {
        this.lock.writeLock().lock();
        try {
            this.cookies.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public String toString() {
        this.lock.readLock().lock();
        try {
            return this.cookies.toString();
        } finally {
            this.lock.readLock().unlock();
        }
    }
}

package org.apache.http.impl.cookie;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

public class BasicClientCookie implements SetCookie, ClientCookie, Cloneable, Serializable {
    private static final long serialVersionUID = -3869795591041535538L;
    private Map<String, String> attribs = new HashMap();
    private String cookieComment;
    private String cookieDomain;
    private Date cookieExpiryDate;
    private String cookiePath;
    private int cookieVersion;
    private Date creationDate;
    private boolean isSecure;
    private final String name;
    private String value;

    public String getCommentURL() {
        return null;
    }

    public int[] getPorts() {
        return null;
    }

    public BasicClientCookie(String str, String str2) {
        Args.notNull(str, "Name");
        this.name = str;
        this.value = str2;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getComment() {
        return this.cookieComment;
    }

    public void setComment(String str) {
        this.cookieComment = str;
    }

    public Date getExpiryDate() {
        return this.cookieExpiryDate;
    }

    public void setExpiryDate(Date date) {
        this.cookieExpiryDate = date;
    }

    public boolean isPersistent() {
        return this.cookieExpiryDate != null;
    }

    public String getDomain() {
        return this.cookieDomain;
    }

    public void setDomain(String str) {
        if (str != null) {
            this.cookieDomain = str.toLowerCase(Locale.ROOT);
        } else {
            this.cookieDomain = null;
        }
    }

    public String getPath() {
        return this.cookiePath;
    }

    public void setPath(String str) {
        this.cookiePath = str;
    }

    public boolean isSecure() {
        return this.isSecure;
    }

    public void setSecure(boolean z) {
        this.isSecure = z;
    }

    public int getVersion() {
        return this.cookieVersion;
    }

    public void setVersion(int i) {
        this.cookieVersion = i;
    }

    public boolean isExpired(Date date) {
        Args.notNull(date, "Date");
        Date date2 = this.cookieExpiryDate;
        return date2 != null && date2.getTime() <= date.getTime();
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    public void setAttribute(String str, String str2) {
        this.attribs.put(str, str2);
    }

    public String getAttribute(String str) {
        return (String) this.attribs.get(str);
    }

    public boolean containsAttribute(String str) {
        return this.attribs.containsKey(str);
    }

    public boolean removeAttribute(String str) {
        return this.attribs.remove(str) != null;
    }

    public Object clone() throws CloneNotSupportedException {
        BasicClientCookie basicClientCookie = (BasicClientCookie) super.clone();
        basicClientCookie.attribs = new HashMap(this.attribs);
        return basicClientCookie;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[version: ");
        sb.append(Integer.toString(this.cookieVersion));
        sb.append("]");
        sb.append("[name: ");
        sb.append(this.name);
        sb.append("]");
        sb.append("[value: ");
        sb.append(this.value);
        sb.append("]");
        sb.append("[domain: ");
        sb.append(this.cookieDomain);
        sb.append("]");
        sb.append("[path: ");
        sb.append(this.cookiePath);
        sb.append("]");
        sb.append("[expiry: ");
        sb.append(this.cookieExpiryDate);
        sb.append("]");
        return sb.toString();
    }
}

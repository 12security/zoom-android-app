package com.microsoft.services.msa;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class LiveConnectSession {
    private String accessToken;
    private String authenticationToken;
    private final PropertyChangeSupport changeSupport;
    private final LiveAuthClient creator;
    private Date expiresIn;
    private String refreshToken;
    private Set<String> scopes;
    private String tokenType;

    LiveConnectSession(LiveAuthClient liveAuthClient) {
        if (liveAuthClient != null) {
            this.creator = liveAuthClient;
            this.changeSupport = new PropertyChangeSupport(this);
            return;
        }
        throw new AssertionError();
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener != null) {
            this.changeSupport.addPropertyChangeListener(propertyChangeListener);
        }
    }

    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener != null) {
            this.changeSupport.addPropertyChangeListener(str, propertyChangeListener);
        }
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getAuthenticationToken() {
        return this.authenticationToken;
    }

    public Date getExpiresIn() {
        return new Date(this.expiresIn.getTime());
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.changeSupport.getPropertyChangeListeners();
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String str) {
        return this.changeSupport.getPropertyChangeListeners(str);
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public Iterable<String> getScopes() {
        return this.scopes;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public boolean isExpired() {
        if (this.expiresIn == null) {
            return true;
        }
        return new Date().after(this.expiresIn);
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener != null) {
            this.changeSupport.removePropertyChangeListener(propertyChangeListener);
        }
    }

    public void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener != null) {
            this.changeSupport.removePropertyChangeListener(str, propertyChangeListener);
        }
    }

    public String toString() {
        return String.format("LiveConnectSession [accessToken=%s, authenticationToken=%s, expiresIn=%s, refreshToken=%s, scopes=%s, tokenType=%s]", new Object[]{this.accessToken, this.authenticationToken, this.expiresIn, this.refreshToken, this.scopes, this.tokenType});
    }

    /* access modifiers changed from: 0000 */
    public boolean contains(Iterable<String> iterable) {
        if (iterable == null) {
            return true;
        }
        if (this.scopes == null) {
            return false;
        }
        for (String contains : iterable) {
            if (!this.scopes.contains(contains)) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void loadFromOAuthResponse(OAuthSuccessfulResponse oAuthSuccessfulResponse) {
        this.accessToken = oAuthSuccessfulResponse.getAccessToken();
        this.tokenType = oAuthSuccessfulResponse.getTokenType().toString().toLowerCase();
        if (oAuthSuccessfulResponse.hasAuthenticationToken()) {
            this.authenticationToken = oAuthSuccessfulResponse.getAuthenticationToken();
        }
        if (oAuthSuccessfulResponse.hasExpiresIn()) {
            Calendar instance = Calendar.getInstance();
            instance.add(13, oAuthSuccessfulResponse.getExpiresIn());
            setExpiresIn(instance.getTime());
        }
        if (oAuthSuccessfulResponse.hasRefreshToken()) {
            this.refreshToken = oAuthSuccessfulResponse.getRefreshToken();
        }
        if (oAuthSuccessfulResponse.hasScope()) {
            setScopes(Arrays.asList(oAuthSuccessfulResponse.getScope().split(OAuth.SCOPE_DELIMITER)));
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean refresh() {
        return this.creator.tryRefresh(getScopes()).booleanValue();
    }

    /* access modifiers changed from: 0000 */
    public void setAccessToken(String str) {
        String str2 = this.accessToken;
        this.accessToken = str;
        this.changeSupport.firePropertyChange("accessToken", str2, this.accessToken);
    }

    /* access modifiers changed from: 0000 */
    public void setAuthenticationToken(String str) {
        String str2 = this.authenticationToken;
        this.authenticationToken = str;
        this.changeSupport.firePropertyChange("authenticationToken", str2, this.authenticationToken);
    }

    /* access modifiers changed from: 0000 */
    public void setExpiresIn(Date date) {
        Date date2 = this.expiresIn;
        this.expiresIn = new Date(date.getTime());
        this.changeSupport.firePropertyChange("expiresIn", date2, this.expiresIn);
    }

    /* access modifiers changed from: 0000 */
    public void setRefreshToken(String str) {
        String str2 = this.refreshToken;
        this.refreshToken = str;
        this.changeSupport.firePropertyChange("refreshToken", str2, this.refreshToken);
    }

    /* access modifiers changed from: 0000 */
    public void setScopes(Iterable<String> iterable) {
        Set<String> set = this.scopes;
        this.scopes = new HashSet();
        if (iterable != null) {
            for (String add : iterable) {
                this.scopes.add(add);
            }
        }
        this.scopes = Collections.unmodifiableSet(this.scopes);
        this.changeSupport.firePropertyChange("scopes", set, this.scopes);
    }

    /* access modifiers changed from: 0000 */
    public void setTokenType(String str) {
        String str2 = this.tokenType;
        this.tokenType = str;
        this.changeSupport.firePropertyChange("tokenType", str2, this.tokenType);
    }

    /* access modifiers changed from: 0000 */
    public boolean willExpireInSecs(int i) {
        Calendar instance = Calendar.getInstance();
        instance.add(13, i);
        return instance.getTime().after(this.expiresIn);
    }
}

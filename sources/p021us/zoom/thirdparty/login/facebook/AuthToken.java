package p021us.zoom.thirdparty.login.facebook;

import android.content.Context;
import android.content.Intent;

/* renamed from: us.zoom.thirdparty.login.facebook.AuthToken */
public class AuthToken {
    public long expires;
    private long lastAccessUpdate = 0;
    public String token;

    public void setAccessToken(String str) {
        this.token = str;
        this.lastAccessUpdate = System.currentTimeMillis();
    }

    public void setExpires(long j) {
        this.expires = j;
    }

    public void setAccessExpiresIn(String str) {
        long j;
        if (str != null) {
            if (str.equals("0")) {
                j = 0;
            } else {
                j = System.currentTimeMillis() + (Long.parseLong(str) * 1000);
            }
            this.expires = j;
        }
    }

    public boolean isSessionValid() {
        return this.token != null && (this.expires == 0 || System.currentTimeMillis() < this.expires);
    }

    public boolean shouldExtendAccessToken() {
        return isSessionValid() && System.currentTimeMillis() - this.lastAccessUpdate >= 86400000;
    }

    public boolean extendAccessTokenIfNeeded(Context context, ServiceListener serviceListener) {
        if (shouldExtendAccessToken()) {
            return extendAccessToken(context, serviceListener);
        }
        return true;
    }

    public boolean extendAccessToken(Context context, ServiceListener serviceListener) {
        Intent intent = new Intent();
        intent.setClassName("com.facebook.katana", "com.facebook.katana.platform.TokenRefreshService");
        if (!FBAuthUtil.validateServiceIntent(context, intent)) {
            return false;
        }
        return context.bindService(intent, new TokenRefreshServiceConnection(context, serviceListener, this), 1);
    }
}

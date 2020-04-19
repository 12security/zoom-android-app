package p021us.zoom.thirdparty.login.facebook;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import androidx.annotation.NonNull;

/* renamed from: us.zoom.thirdparty.login.facebook.TokenRefreshServiceConnection */
public class TokenRefreshServiceConnection implements ServiceConnection {
    public static final String EXPIRES = "expires_in";
    public static final String TOKEN = "access_token";
    final Context applicationsContext;
    final AuthToken authToken;
    boolean mIsConnected;
    final Messenger messageReceiver = new Messenger(new Handler() {
        public void handleMessage(Message message) {
            String string = message.getData().getString("access_token");
            long j = message.getData().getLong("expires_in") * 1000;
            Bundle bundle = (Bundle) message.getData().clone();
            bundle.putLong("expires_in", j);
            if (string != null) {
                TokenRefreshServiceConnection.this.authToken.setAccessToken(string);
                TokenRefreshServiceConnection.this.authToken.setExpires(j);
                if (TokenRefreshServiceConnection.this.serviceListener != null) {
                    TokenRefreshServiceConnection.this.serviceListener.onComplete(bundle);
                }
            } else if (TokenRefreshServiceConnection.this.serviceListener != null) {
                String string2 = message.getData().getString("error");
                if (message.getData().containsKey("error_code")) {
                    TokenRefreshServiceConnection.this.serviceListener.onFacebookError(new FacebookError(string2, null, message.getData().getInt("error_code")));
                } else {
                    ServiceListener serviceListener = TokenRefreshServiceConnection.this.serviceListener;
                    if (string2 == null) {
                        string2 = "Unknown service error";
                    }
                    serviceListener.onError(new Error(string2));
                }
            }
            TokenRefreshServiceConnection.this.applicationsContext.unbindService(TokenRefreshServiceConnection.this);
        }
    });
    Messenger messageSender = null;
    final ServiceListener serviceListener;

    public TokenRefreshServiceConnection(Context context, ServiceListener serviceListener2, @NonNull AuthToken authToken2) {
        this.applicationsContext = context;
        this.serviceListener = serviceListener2;
        this.authToken = authToken2;
        this.mIsConnected = false;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.mIsConnected = true;
        this.messageSender = new Messenger(iBinder);
        refreshToken();
    }

    public void onServiceDisconnected(ComponentName componentName) {
        this.serviceListener.onError(new Error("Service disconnected"));
        if (this.mIsConnected) {
            try {
                this.applicationsContext.unbindService(this);
            } catch (IllegalArgumentException unused) {
            }
            this.mIsConnected = false;
        }
    }

    private void refreshToken() {
        Bundle bundle = new Bundle();
        bundle.putString("access_token", this.authToken.token);
        Message obtain = Message.obtain();
        obtain.setData(bundle);
        obtain.replyTo = this.messageReceiver;
        try {
            this.messageSender.send(obtain);
        } catch (RemoteException unused) {
            this.serviceListener.onError(new Error("Service connection error"));
        }
    }
}

package com.microsoft.aad.adal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import com.microsoft.aad.adal.AuthenticationConstants.Browser;
import com.microsoft.aad.adal.IBrokerAccountService.Stub;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

final class BrokerAccountServiceHandler {
    private static final String BROKER_ACCOUNT_SERVICE_INTENT_FILTER = "com.microsoft.workaccount.BrokerAccount";
    /* access modifiers changed from: private */
    public static final String TAG = "BrokerAccountServiceHandler";
    /* access modifiers changed from: private */
    public static ExecutorService sThreadExecutor = Executors.newCachedThreadPool();
    /* access modifiers changed from: private */
    public ConcurrentMap<BrokerAccountServiceConnection, CallbackExecutor<BrokerAccountServiceConnection>> mPendingConnections;

    private class BrokerAccountServiceConnection implements ServiceConnection {
        /* access modifiers changed from: private */
        public boolean mBound;
        private IBrokerAccountService mBrokerAccountService;
        private BrokerEvent mEvent;

        private BrokerAccountServiceConnection() {
        }

        public IBrokerAccountService getBrokerAccountServiceProvider() {
            return this.mBrokerAccountService;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Logger.m236v(BrokerAccountServiceHandler.TAG, "Broker Account service is connected.");
            this.mBrokerAccountService = Stub.asInterface(iBinder);
            this.mBound = true;
            BrokerEvent brokerEvent = this.mEvent;
            if (brokerEvent != null) {
                brokerEvent.setBrokerAccountServiceConnected();
            }
            CallbackExecutor callbackExecutor = (CallbackExecutor) BrokerAccountServiceHandler.this.mPendingConnections.remove(this);
            if (callbackExecutor != null) {
                callbackExecutor.onSuccess(this);
            } else {
                Logger.m236v(BrokerAccountServiceHandler.TAG, "No callback is found.");
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Logger.m236v(BrokerAccountServiceHandler.TAG, "Broker Account service is disconnected.");
            this.mBound = false;
        }

        public void unBindService(final Context context) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    if (BrokerAccountServiceConnection.this.mBound) {
                        try {
                            context.unbindService(BrokerAccountServiceConnection.this);
                        } catch (IllegalArgumentException e) {
                            Logger.m232e(BrokerAccountServiceHandler.TAG, "Unbind threw IllegalArgumentException", "", null, e);
                        } catch (Throwable th) {
                            BrokerAccountServiceConnection.this.mBound = false;
                            throw th;
                        }
                        BrokerAccountServiceConnection.this.mBound = false;
                    }
                }
            });
        }

        public void setTelemetryEvent(BrokerEvent brokerEvent) {
            this.mEvent = brokerEvent;
        }
    }

    private static final class InstanceHolder {
        static final BrokerAccountServiceHandler INSTANCE = new BrokerAccountServiceHandler();

        private InstanceHolder() {
        }
    }

    public static BrokerAccountServiceHandler getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private BrokerAccountServiceHandler() {
        this.mPendingConnections = new ConcurrentHashMap();
    }

    /* access modifiers changed from: 0000 */
    public UserInfo[] getBrokerUsers(Context context) throws IOException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference atomicReference = new AtomicReference(null);
        final AtomicReference atomicReference2 = new AtomicReference(null);
        performAsyncCallOnBound(context, new Callback<BrokerAccountServiceConnection>() {
            public void onSuccess(BrokerAccountServiceConnection brokerAccountServiceConnection) {
                try {
                    atomicReference.set(brokerAccountServiceConnection.getBrokerAccountServiceProvider().getBrokerUsers());
                } catch (RemoteException e) {
                    atomicReference2.set(e);
                }
                countDownLatch.countDown();
            }

            public void onError(Throwable th) {
                atomicReference2.set(th);
                countDownLatch.countDown();
            }
        }, null);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            atomicReference2.set(e);
        }
        Throwable th = (Throwable) atomicReference2.getAndSet(null);
        if (th == null) {
            return convertUserInfoBundleToArray((Bundle) atomicReference.getAndSet(null));
        }
        throw new IOException(th.getMessage(), th);
    }

    public Bundle getAuthToken(Context context, Bundle bundle, BrokerEvent brokerEvent) throws AuthenticationException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference atomicReference = new AtomicReference(null);
        AtomicReference atomicReference2 = new AtomicReference(null);
        final AtomicReference atomicReference3 = atomicReference;
        final Context context2 = context;
        final Bundle bundle2 = bundle;
        final AtomicReference atomicReference4 = atomicReference2;
        final CountDownLatch countDownLatch2 = countDownLatch;
        C17032 r0 = new Callback<BrokerAccountServiceConnection>() {
            public void onSuccess(BrokerAccountServiceConnection brokerAccountServiceConnection) {
                try {
                    atomicReference3.set(brokerAccountServiceConnection.getBrokerAccountServiceProvider().acquireTokenSilently(BrokerAccountServiceHandler.this.prepareGetAuthTokenRequestData(context2, bundle2)));
                } catch (RemoteException e) {
                    atomicReference4.set(e);
                }
                countDownLatch2.countDown();
            }

            public void onError(Throwable th) {
                atomicReference4.set(th);
                countDownLatch2.countDown();
            }
        };
        performAsyncCallOnBound(context, r0, brokerEvent);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            atomicReference2.set(e);
        }
        Throwable th = (Throwable) atomicReference2.getAndSet(null);
        if (th == null) {
            return (Bundle) atomicReference.getAndSet(null);
        }
        if (th instanceof RemoteException) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":getAuthToken");
            Logger.m232e(sb.toString(), "Get error when trying to get token from broker. ", th.getMessage(), ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, th);
            throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, th.getMessage(), th);
        } else if (th instanceof InterruptedException) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(TAG);
            sb2.append(":getAuthToken");
            Logger.m232e(sb2.toString(), "The broker account service binding call is interrupted. ", th.getMessage(), ADALError.BROKER_AUTHENTICATOR_EXCEPTION, th);
            throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, th.getMessage(), th);
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(TAG);
            sb3.append(":getAuthToken");
            Logger.m232e(sb3.toString(), "Get error when trying to bind the broker account service.", th.getMessage(), ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, th);
            throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, th.getMessage(), th);
        }
    }

    public Intent getIntentForInteractiveRequest(Context context, BrokerEvent brokerEvent) throws AuthenticationException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference atomicReference = new AtomicReference(null);
        final AtomicReference atomicReference2 = new AtomicReference(null);
        performAsyncCallOnBound(context, new Callback<BrokerAccountServiceConnection>() {
            public void onSuccess(BrokerAccountServiceConnection brokerAccountServiceConnection) {
                try {
                    atomicReference.set(brokerAccountServiceConnection.getBrokerAccountServiceProvider().getIntentForInteractiveRequest());
                } catch (RemoteException e) {
                    atomicReference2.set(e);
                }
                countDownLatch.countDown();
            }

            public void onError(Throwable th) {
                atomicReference2.set(th);
                countDownLatch.countDown();
            }
        }, brokerEvent);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            atomicReference2.set(e);
        }
        Throwable th = (Throwable) atomicReference2.getAndSet(null);
        if (th == null) {
            return (Intent) atomicReference.getAndSet(null);
        }
        if (th instanceof RemoteException) {
            Logger.m232e(TAG, "Get error when trying to get token from broker. ", th.getMessage(), ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, th);
            throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, th.getMessage(), th);
        } else if (th instanceof InterruptedException) {
            Logger.m232e(TAG, "The broker account service binding call is interrupted. ", th.getMessage(), ADALError.BROKER_AUTHENTICATOR_EXCEPTION, th);
            throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, th.getMessage(), th);
        } else {
            Logger.m232e(TAG, "Didn't receive the activity to launch from broker. ", th.getMessage(), ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, th);
            ADALError aDALError = ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING;
            StringBuilder sb = new StringBuilder();
            sb.append("Didn't receive the activity to launch from broker: ");
            sb.append(th.getMessage());
            throw new AuthenticationException(aDALError, sb.toString(), th);
        }
    }

    public void removeAccounts(Context context) {
        performAsyncCallOnBound(context, new Callback<BrokerAccountServiceConnection>() {
            public void onSuccess(BrokerAccountServiceConnection brokerAccountServiceConnection) {
                try {
                    brokerAccountServiceConnection.getBrokerAccountServiceProvider().removeAccounts();
                } catch (RemoteException e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(BrokerAccountServiceHandler.TAG);
                    sb.append(":removeAccounts");
                    Logger.m232e(sb.toString(), "Encounter exception when removing accounts from broker", e.getMessage(), null, e);
                }
            }

            public void onError(Throwable th) {
                StringBuilder sb = new StringBuilder();
                sb.append(BrokerAccountServiceHandler.TAG);
                sb.append(":removeAccounts");
                Logger.m232e(sb.toString(), "Encounter exception when removing accounts from broker", th.getMessage(), null, th);
            }
        }, null);
    }

    public static Intent getIntentForBrokerAccountService(Context context) {
        String currentActiveBrokerPackageName = new BrokerProxy(context).getCurrentActiveBrokerPackageName();
        if (currentActiveBrokerPackageName == null) {
            Logger.m236v(TAG, "No recognized broker is installed on the device.");
            return null;
        }
        Intent intent = new Intent(BROKER_ACCOUNT_SERVICE_INTENT_FILTER);
        intent.setPackage(currentActiveBrokerPackageName);
        intent.setClassName(currentActiveBrokerPackageName, "com.microsoft.aad.adal.BrokerAccountService");
        return intent;
    }

    /* access modifiers changed from: private */
    public Map<String, String> prepareGetAuthTokenRequestData(Context context, Bundle bundle) {
        Set<String> keySet = bundle.keySet();
        HashMap hashMap = new HashMap();
        for (String str : keySet) {
            if (str.equals(Browser.REQUEST_ID) || str.equals(Broker.EXPIRATION_BUFFER)) {
                hashMap.put(str, String.valueOf(bundle.getInt(str)));
            } else {
                hashMap.put(str, bundle.getString(str));
            }
        }
        hashMap.put(Broker.CALLER_INFO_PACKAGE, context.getPackageName());
        return hashMap;
    }

    private UserInfo[] convertUserInfoBundleToArray(Bundle bundle) {
        if (bundle == null) {
            Logger.m236v(TAG, "No user info returned from broker account service.");
            return new UserInfo[0];
        }
        ArrayList arrayList = new ArrayList();
        for (String bundle2 : bundle.keySet()) {
            Bundle bundle3 = bundle.getBundle(bundle2);
            UserInfo userInfo = new UserInfo(bundle3.getString(Broker.ACCOUNT_USERINFO_USERID), bundle3.getString(Broker.ACCOUNT_USERINFO_GIVEN_NAME), bundle3.getString(Broker.ACCOUNT_USERINFO_FAMILY_NAME), bundle3.getString(Broker.ACCOUNT_USERINFO_IDENTITY_PROVIDER), bundle3.getString(Broker.ACCOUNT_USERINFO_USERID_DISPLAYABLE));
            arrayList.add(userInfo);
        }
        return (UserInfo[]) arrayList.toArray(new UserInfo[arrayList.size()]);
    }

    private void performAsyncCallOnBound(final Context context, final Callback<BrokerAccountServiceConnection> callback, BrokerEvent brokerEvent) {
        bindToBrokerAccountService(context, new Callback<BrokerAccountServiceConnection>() {
            public void onSuccess(final BrokerAccountServiceConnection brokerAccountServiceConnection) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    callback.onSuccess(brokerAccountServiceConnection);
                    brokerAccountServiceConnection.unBindService(context);
                    return;
                }
                BrokerAccountServiceHandler.sThreadExecutor.execute(new Runnable() {
                    public void run() {
                        callback.onSuccess(brokerAccountServiceConnection);
                        brokerAccountServiceConnection.unBindService(context);
                    }
                });
            }

            public void onError(Throwable th) {
                callback.onError(th);
            }
        }, brokerEvent);
    }

    private void bindToBrokerAccountService(Context context, Callback<BrokerAccountServiceConnection> callback, BrokerEvent brokerEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":bindToBrokerAccountService");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("uid: ");
        sb2.append(Process.myUid());
        Logger.m237v(sb.toString(), "Binding to BrokerAccountService for caller uid. ", sb2.toString(), null);
        Intent intentForBrokerAccountService = getIntentForBrokerAccountService(context);
        BrokerAccountServiceConnection brokerAccountServiceConnection = new BrokerAccountServiceConnection();
        if (brokerEvent != null) {
            brokerAccountServiceConnection.setTelemetryEvent(brokerEvent);
            brokerEvent.setBrokerAccountServerStartsBinding();
        }
        this.mPendingConnections.put(brokerAccountServiceConnection, new CallbackExecutor(callback));
        boolean bindService = context.bindService(intentForBrokerAccountService, brokerAccountServiceConnection, 1);
        StringBuilder sb3 = new StringBuilder();
        sb3.append(TAG);
        sb3.append(":bindToBrokerAccountService");
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("The status for brokerAccountService bindService call is: ");
        sb5.append(Boolean.valueOf(bindService));
        Logger.m236v(sb4, sb5.toString());
        if (brokerEvent != null) {
            brokerEvent.setBrokerAccountServiceBindingSucceed(bindService);
        }
        if (!bindService) {
            brokerAccountServiceConnection.unBindService(context);
            StringBuilder sb6 = new StringBuilder();
            sb6.append(TAG);
            sb6.append(":bindToBrokerAccountService");
            Logger.m231e(sb6.toString(), "Failed to bind service to broker app. ", "'bindService returned false", ADALError.BROKER_BIND_SERVICE_FAILED);
            callback.onError(new AuthenticationException(ADALError.BROKER_BIND_SERVICE_FAILED));
        }
    }
}

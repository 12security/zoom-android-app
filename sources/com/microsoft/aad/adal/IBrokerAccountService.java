package com.microsoft.aad.adal;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

public interface IBrokerAccountService extends IInterface {

    public static class Default implements IBrokerAccountService {
        public Bundle acquireTokenSilently(Map map) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }

        public Bundle getBrokerUsers() throws RemoteException {
            return null;
        }

        public Intent getIntentForInteractiveRequest() throws RemoteException {
            return null;
        }

        public void removeAccounts() throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IBrokerAccountService {
        private static final String DESCRIPTOR = "com.microsoft.aad.adal.IBrokerAccountService";
        static final int TRANSACTION_acquireTokenSilently = 2;
        static final int TRANSACTION_getBrokerUsers = 1;
        static final int TRANSACTION_getIntentForInteractiveRequest = 3;
        static final int TRANSACTION_removeAccounts = 4;

        private static class Proxy implements IBrokerAccountService {
            public static IBrokerAccountService sDefaultImpl;
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public Bundle getBrokerUsers() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBrokerUsers();
                    }
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle acquireTokenSilently(Map map) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeMap(map);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().acquireTokenSilently(map);
                    }
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Intent getIntentForInteractiveRequest() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIntentForInteractiveRequest();
                    }
                    obtain2.readException();
                    Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return intent;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeAccounts() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeAccounts();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBrokerAccountService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IBrokerAccountService)) {
                return new Proxy(iBinder);
            }
            return (IBrokerAccountService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = DESCRIPTOR;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(str);
                        Bundle brokerUsers = getBrokerUsers();
                        parcel2.writeNoException();
                        if (brokerUsers != null) {
                            parcel2.writeInt(1);
                            brokerUsers.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 2:
                        parcel.enforceInterface(str);
                        Bundle acquireTokenSilently = acquireTokenSilently(parcel.readHashMap(getClass().getClassLoader()));
                        parcel2.writeNoException();
                        if (acquireTokenSilently != null) {
                            parcel2.writeInt(1);
                            acquireTokenSilently.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        parcel.enforceInterface(str);
                        Intent intentForInteractiveRequest = getIntentForInteractiveRequest();
                        parcel2.writeNoException();
                        if (intentForInteractiveRequest != null) {
                            parcel2.writeInt(1);
                            intentForInteractiveRequest.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(str);
                        removeAccounts();
                        parcel2.writeNoException();
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString(str);
                return true;
            }
        }

        public static boolean setDefaultImpl(IBrokerAccountService iBrokerAccountService) {
            if (Proxy.sDefaultImpl != null || iBrokerAccountService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iBrokerAccountService;
            return true;
        }

        public static IBrokerAccountService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    Bundle acquireTokenSilently(Map map) throws RemoteException;

    Bundle getBrokerUsers() throws RemoteException;

    Intent getIntentForInteractiveRequest() throws RemoteException;

    void removeAccounts() throws RemoteException;
}

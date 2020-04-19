package com.zipow.videobox.kubi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IKubiService extends IInterface {

    public static class Default implements IKubiService {
        public IBinder asBinder() {
            return null;
        }

        public void connectToKubi(KubiDevice kubiDevice) throws RemoteException {
        }

        public boolean disconnectKubi() throws RemoteException {
            return false;
        }

        public void findAllKubiDevices() throws RemoteException {
        }

        public boolean findKubiDevice() throws RemoteException {
            return false;
        }

        public KubiDevice getCurrentKubi() throws RemoteException {
            return null;
        }

        public int getKubiStatus() throws RemoteException {
            return 0;
        }

        public float getPan() throws RemoteException {
            return 0.0f;
        }

        public float getTilt() throws RemoteException {
            return 0.0f;
        }

        public void moveInPanDirectionWithSpeed(int i, int i2) throws RemoteException {
        }

        public void moveInTiltDirectionWithSpeed(int i, int i2) throws RemoteException {
        }

        public void moveTo(float f, float f2, float f3) throws RemoteException {
        }

        public boolean resetDevicePosition() throws RemoteException {
            return false;
        }
    }

    public static abstract class Stub extends Binder implements IKubiService {
        private static final String DESCRIPTOR = "com.zipow.videobox.kubi.IKubiService";
        static final int TRANSACTION_connectToKubi = 5;
        static final int TRANSACTION_disconnectKubi = 3;
        static final int TRANSACTION_findAllKubiDevices = 4;
        static final int TRANSACTION_findKubiDevice = 2;
        static final int TRANSACTION_getCurrentKubi = 6;
        static final int TRANSACTION_getKubiStatus = 1;
        static final int TRANSACTION_getPan = 7;
        static final int TRANSACTION_getTilt = 8;
        static final int TRANSACTION_moveInPanDirectionWithSpeed = 9;
        static final int TRANSACTION_moveInTiltDirectionWithSpeed = 10;
        static final int TRANSACTION_moveTo = 11;
        static final int TRANSACTION_resetDevicePosition = 12;

        private static class Proxy implements IKubiService {
            public static IKubiService sDefaultImpl;
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

            public int getKubiStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getKubiStatus();
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.recycle();
                    obtain.recycle();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean findKubiDevice() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().findKubiDevice();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean disconnectKubi() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().disconnectKubi();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void findAllKubiDevices() throws RemoteException {
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
                    Stub.getDefaultImpl().findAllKubiDevices();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void connectToKubi(KubiDevice kubiDevice) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (kubiDevice != null) {
                        obtain.writeInt(1);
                        kubiDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(5, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().connectToKubi(kubiDevice);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public KubiDevice getCurrentKubi() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentKubi();
                    }
                    obtain2.readException();
                    KubiDevice kubiDevice = obtain2.readInt() != 0 ? (KubiDevice) KubiDevice.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return kubiDevice;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public float getPan() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPan();
                    }
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    obtain2.recycle();
                    obtain.recycle();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public float getTilt() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTilt();
                    }
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    obtain2.recycle();
                    obtain.recycle();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void moveInPanDirectionWithSpeed(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (this.mRemote.transact(9, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveInPanDirectionWithSpeed(i, i2);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void moveInTiltDirectionWithSpeed(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (this.mRemote.transact(10, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveInTiltDirectionWithSpeed(i, i2);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void moveTo(float f, float f2, float f3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeFloat(f);
                    obtain.writeFloat(f2);
                    obtain.writeFloat(f3);
                    if (this.mRemote.transact(11, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveTo(f, f2, f3);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean resetDevicePosition() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(12, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resetDevicePosition();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
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

        public static IKubiService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IKubiService)) {
                return new Proxy(iBinder);
            }
            return (IKubiService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = DESCRIPTOR;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(str);
                        int kubiStatus = getKubiStatus();
                        parcel2.writeNoException();
                        parcel2.writeInt(kubiStatus);
                        return true;
                    case 2:
                        parcel.enforceInterface(str);
                        boolean findKubiDevice = findKubiDevice();
                        parcel2.writeNoException();
                        parcel2.writeInt(findKubiDevice ? 1 : 0);
                        return true;
                    case 3:
                        parcel.enforceInterface(str);
                        boolean disconnectKubi = disconnectKubi();
                        parcel2.writeNoException();
                        parcel2.writeInt(disconnectKubi ? 1 : 0);
                        return true;
                    case 4:
                        parcel.enforceInterface(str);
                        findAllKubiDevices();
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(str);
                        connectToKubi(parcel.readInt() != 0 ? (KubiDevice) KubiDevice.CREATOR.createFromParcel(parcel) : null);
                        parcel2.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(str);
                        KubiDevice currentKubi = getCurrentKubi();
                        parcel2.writeNoException();
                        if (currentKubi != null) {
                            parcel2.writeInt(1);
                            currentKubi.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 7:
                        parcel.enforceInterface(str);
                        float pan = getPan();
                        parcel2.writeNoException();
                        parcel2.writeFloat(pan);
                        return true;
                    case 8:
                        parcel.enforceInterface(str);
                        float tilt = getTilt();
                        parcel2.writeNoException();
                        parcel2.writeFloat(tilt);
                        return true;
                    case 9:
                        parcel.enforceInterface(str);
                        moveInPanDirectionWithSpeed(parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(str);
                        moveInTiltDirectionWithSpeed(parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(str);
                        moveTo(parcel.readFloat(), parcel.readFloat(), parcel.readFloat());
                        parcel2.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(str);
                        boolean resetDevicePosition = resetDevicePosition();
                        parcel2.writeNoException();
                        parcel2.writeInt(resetDevicePosition ? 1 : 0);
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString(str);
                return true;
            }
        }

        public static boolean setDefaultImpl(IKubiService iKubiService) {
            if (Proxy.sDefaultImpl != null || iKubiService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iKubiService;
            return true;
        }

        public static IKubiService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void connectToKubi(KubiDevice kubiDevice) throws RemoteException;

    boolean disconnectKubi() throws RemoteException;

    void findAllKubiDevices() throws RemoteException;

    boolean findKubiDevice() throws RemoteException;

    KubiDevice getCurrentKubi() throws RemoteException;

    int getKubiStatus() throws RemoteException;

    float getPan() throws RemoteException;

    float getTilt() throws RemoteException;

    void moveInPanDirectionWithSpeed(int i, int i2) throws RemoteException;

    void moveInTiltDirectionWithSpeed(int i, int i2) throws RemoteException;

    void moveTo(float f, float f2, float f3) throws RemoteException;

    boolean resetDevicePosition() throws RemoteException;
}

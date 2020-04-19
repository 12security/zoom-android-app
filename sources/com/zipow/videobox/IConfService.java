package com.zipow.videobox;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IConfService extends IInterface {

    public static class Default implements IConfService {
        public IBinder asBinder() {
            return null;
        }

        public boolean disableConfAudio() throws RemoteException {
            return false;
        }

        public void dispacthPtLoginResultEvent(boolean z, String str, String str2) throws RemoteException {
        }

        public int getCallMeStatus() throws RemoteException {
            return 0;
        }

        public boolean hangUpCallOut() throws RemoteException {
            return false;
        }

        public boolean isCallOutInProgress() throws RemoteException {
            return false;
        }

        public boolean isCallOutSupported() throws RemoteException {
            return false;
        }

        public boolean isConfAppAtFront() throws RemoteException {
            return false;
        }

        public boolean isCurrentMeetingHost() throws RemoteException {
            return false;
        }

        public boolean isCurrentMeetingLocked() throws RemoteException {
            return false;
        }

        public boolean isInviteRoomSystemSupported() throws RemoteException {
            return false;
        }

        public void leaveCurrentMeeting(boolean z) throws RemoteException {
        }

        public boolean notifyPTStartLogin(String str) throws RemoteException {
            return false;
        }

        public boolean onAlertWhenAvailable(String str, String str2, String str3, boolean z, String str4) throws RemoteException {
            return false;
        }

        public void onNewIncomingCallCanceled(long j) throws RemoteException {
        }

        public void onPTUIMoveToBackground() throws RemoteException {
        }

        public void onPTUIMoveToFront(String str) throws RemoteException {
        }

        public void onRoomCallEvent(int i, long j, boolean z) throws RemoteException {
        }

        public void onSipCallEvent(int i, String str) throws RemoteException {
        }

        public void onSipStatusEvent(boolean z) throws RemoteException {
        }

        public void pauseCurrentMeeting() throws RemoteException {
        }

        public void resumeCurrentMeeting() throws RemoteException {
        }

        public void sendMessage(byte[] bArr) throws RemoteException {
        }

        public void sinkDataNetworkStatusChanged(boolean z) throws RemoteException {
        }

        public void sinkIMBuddyPic(byte[] bArr) throws RemoteException {
        }

        public void sinkIMBuddyPresence(byte[] bArr) throws RemoteException {
        }

        public void sinkIMBuddySort() throws RemoteException {
        }

        public void sinkIMLocalStatusChanged(int i) throws RemoteException {
        }

        public void sinkIMReceived(byte[] bArr) throws RemoteException {
        }

        public void sinkPTAppCustomEvent(int i, long j) throws RemoteException {
        }

        public void sinkPTAppEvent(int i, long j) throws RemoteException {
        }

        public void sinkPTCommonEvent(int i, byte[] bArr) throws RemoteException {
        }

        public void sinkPTPresentToRoomEvent(int i) throws RemoteException {
        }

        public boolean startCallOut(String str) throws RemoteException {
            return false;
        }

        public boolean tryRetrieveMicrophone() throws RemoteException {
            return false;
        }
    }

    public static abstract class Stub extends Binder implements IConfService {
        private static final String DESCRIPTOR = "com.zipow.videobox.IConfService";
        static final int TRANSACTION_disableConfAudio = 34;
        static final int TRANSACTION_dispacthPtLoginResultEvent = 31;
        static final int TRANSACTION_getCallMeStatus = 23;
        static final int TRANSACTION_hangUpCallOut = 21;
        static final int TRANSACTION_isCallOutInProgress = 22;
        static final int TRANSACTION_isCallOutSupported = 24;
        static final int TRANSACTION_isConfAppAtFront = 2;
        static final int TRANSACTION_isCurrentMeetingHost = 19;
        static final int TRANSACTION_isCurrentMeetingLocked = 18;
        static final int TRANSACTION_isInviteRoomSystemSupported = 25;
        static final int TRANSACTION_leaveCurrentMeeting = 15;
        static final int TRANSACTION_notifyPTStartLogin = 33;
        static final int TRANSACTION_onAlertWhenAvailable = 30;
        static final int TRANSACTION_onNewIncomingCallCanceled = 32;
        static final int TRANSACTION_onPTUIMoveToBackground = 14;
        static final int TRANSACTION_onPTUIMoveToFront = 13;
        static final int TRANSACTION_onRoomCallEvent = 26;
        static final int TRANSACTION_onSipCallEvent = 28;
        static final int TRANSACTION_onSipStatusEvent = 29;
        static final int TRANSACTION_pauseCurrentMeeting = 16;
        static final int TRANSACTION_resumeCurrentMeeting = 17;
        static final int TRANSACTION_sendMessage = 1;
        static final int TRANSACTION_sinkDataNetworkStatusChanged = 4;
        static final int TRANSACTION_sinkIMBuddyPic = 8;
        static final int TRANSACTION_sinkIMBuddyPresence = 7;
        static final int TRANSACTION_sinkIMBuddySort = 9;
        static final int TRANSACTION_sinkIMLocalStatusChanged = 5;
        static final int TRANSACTION_sinkIMReceived = 6;
        static final int TRANSACTION_sinkPTAppCustomEvent = 10;
        static final int TRANSACTION_sinkPTAppEvent = 3;
        static final int TRANSACTION_sinkPTCommonEvent = 12;
        static final int TRANSACTION_sinkPTPresentToRoomEvent = 11;
        static final int TRANSACTION_startCallOut = 20;
        static final int TRANSACTION_tryRetrieveMicrophone = 27;

        private static class Proxy implements IConfService {
            public static IConfService sDefaultImpl;
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

            public void sendMessage(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    if (this.mRemote.transact(1, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendMessage(bArr);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isConfAppAtFront() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isConfAppAtFront();
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

            public void sinkPTAppEvent(int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(3, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sinkPTAppEvent(i, j);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sinkDataNetworkStatusChanged(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(4, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sinkDataNetworkStatusChanged(z);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sinkIMLocalStatusChanged(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(5, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sinkIMLocalStatusChanged(i);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sinkIMReceived(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    if (this.mRemote.transact(6, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sinkIMReceived(bArr);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sinkIMBuddyPresence(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    if (this.mRemote.transact(7, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sinkIMBuddyPresence(bArr);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sinkIMBuddyPic(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    if (this.mRemote.transact(8, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sinkIMBuddyPic(bArr);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sinkIMBuddySort() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(9, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sinkIMBuddySort();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sinkPTAppCustomEvent(int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(10, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sinkPTAppCustomEvent(i, j);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sinkPTPresentToRoomEvent(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(11, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sinkPTPresentToRoomEvent(i);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sinkPTCommonEvent(int i, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    if (this.mRemote.transact(12, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sinkPTCommonEvent(i, bArr);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onPTUIMoveToFront(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(13, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onPTUIMoveToFront(str);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onPTUIMoveToBackground() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(14, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onPTUIMoveToBackground();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void leaveCurrentMeeting(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(15, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().leaveCurrentMeeting(z);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void pauseCurrentMeeting() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(16, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pauseCurrentMeeting();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void resumeCurrentMeeting() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(17, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resumeCurrentMeeting();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isCurrentMeetingLocked() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(18, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCurrentMeetingLocked();
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

            public boolean isCurrentMeetingHost() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(19, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCurrentMeetingHost();
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

            public boolean startCallOut(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    boolean z = false;
                    if (!this.mRemote.transact(20, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startCallOut(str);
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

            public boolean hangUpCallOut() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(21, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hangUpCallOut();
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

            public boolean isCallOutInProgress() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(22, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCallOutInProgress();
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

            public int getCallMeStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(23, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCallMeStatus();
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

            public boolean isCallOutSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(24, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCallOutSupported();
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

            public boolean isInviteRoomSystemSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(25, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isInviteRoomSystemSupported();
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

            public void onRoomCallEvent(int i, long j, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(26, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onRoomCallEvent(i, j, z);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean tryRetrieveMicrophone() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(27, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().tryRetrieveMicrophone();
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

            public void onSipCallEvent(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (this.mRemote.transact(28, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onSipCallEvent(i, str);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onSipStatusEvent(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(29, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onSipStatusEvent(z);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean onAlertWhenAvailable(String str, String str2, String str3, boolean z, String str4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str5 = str;
                    obtain.writeString(str);
                    String str6 = str2;
                    obtain.writeString(str2);
                    String str7 = str3;
                    obtain.writeString(str3);
                    boolean z2 = true;
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeString(str4);
                    try {
                        if (this.mRemote.transact(30, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                            obtain2.readException();
                            if (obtain2.readInt() == 0) {
                                z2 = false;
                            }
                            obtain2.recycle();
                            obtain.recycle();
                            return z2;
                        }
                        boolean onAlertWhenAvailable = Stub.getDefaultImpl().onAlertWhenAvailable(str, str2, str3, z, str4);
                        obtain2.recycle();
                        obtain.recycle();
                        return onAlertWhenAvailable;
                    } catch (Throwable th) {
                        th = th;
                        obtain2.recycle();
                        obtain.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    obtain2.recycle();
                    obtain.recycle();
                    throw th;
                }
            }

            public void dispacthPtLoginResultEvent(boolean z, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (this.mRemote.transact(31, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dispacthPtLoginResultEvent(z, str, str2);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onNewIncomingCallCanceled(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    if (this.mRemote.transact(32, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onNewIncomingCallCanceled(j);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean notifyPTStartLogin(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    boolean z = false;
                    if (!this.mRemote.transact(33, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().notifyPTStartLogin(str);
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

            public boolean disableConfAudio() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(34, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().disableConfAudio();
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

        public static IConfService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IConfService)) {
                return new Proxy(iBinder);
            }
            return (IConfService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = DESCRIPTOR;
            if (i != 1598968902) {
                boolean z = false;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(str);
                        sendMessage(parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(str);
                        boolean isConfAppAtFront = isConfAppAtFront();
                        parcel2.writeNoException();
                        parcel2.writeInt(isConfAppAtFront ? 1 : 0);
                        return true;
                    case 3:
                        parcel.enforceInterface(str);
                        sinkPTAppEvent(parcel.readInt(), parcel.readLong());
                        parcel2.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(str);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        sinkDataNetworkStatusChanged(z);
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(str);
                        sinkIMLocalStatusChanged(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(str);
                        sinkIMReceived(parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(str);
                        sinkIMBuddyPresence(parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(str);
                        sinkIMBuddyPic(parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(str);
                        sinkIMBuddySort();
                        parcel2.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(str);
                        sinkPTAppCustomEvent(parcel.readInt(), parcel.readLong());
                        parcel2.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(str);
                        sinkPTPresentToRoomEvent(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(str);
                        sinkPTCommonEvent(parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(str);
                        onPTUIMoveToFront(parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(str);
                        onPTUIMoveToBackground();
                        parcel2.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(str);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        leaveCurrentMeeting(z);
                        parcel2.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(str);
                        pauseCurrentMeeting();
                        parcel2.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(str);
                        resumeCurrentMeeting();
                        parcel2.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(str);
                        boolean isCurrentMeetingLocked = isCurrentMeetingLocked();
                        parcel2.writeNoException();
                        parcel2.writeInt(isCurrentMeetingLocked ? 1 : 0);
                        return true;
                    case 19:
                        parcel.enforceInterface(str);
                        boolean isCurrentMeetingHost = isCurrentMeetingHost();
                        parcel2.writeNoException();
                        parcel2.writeInt(isCurrentMeetingHost ? 1 : 0);
                        return true;
                    case 20:
                        parcel.enforceInterface(str);
                        boolean startCallOut = startCallOut(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(startCallOut ? 1 : 0);
                        return true;
                    case 21:
                        parcel.enforceInterface(str);
                        boolean hangUpCallOut = hangUpCallOut();
                        parcel2.writeNoException();
                        parcel2.writeInt(hangUpCallOut ? 1 : 0);
                        return true;
                    case 22:
                        parcel.enforceInterface(str);
                        boolean isCallOutInProgress = isCallOutInProgress();
                        parcel2.writeNoException();
                        parcel2.writeInt(isCallOutInProgress ? 1 : 0);
                        return true;
                    case 23:
                        parcel.enforceInterface(str);
                        int callMeStatus = getCallMeStatus();
                        parcel2.writeNoException();
                        parcel2.writeInt(callMeStatus);
                        return true;
                    case 24:
                        parcel.enforceInterface(str);
                        boolean isCallOutSupported = isCallOutSupported();
                        parcel2.writeNoException();
                        parcel2.writeInt(isCallOutSupported ? 1 : 0);
                        return true;
                    case 25:
                        parcel.enforceInterface(str);
                        boolean isInviteRoomSystemSupported = isInviteRoomSystemSupported();
                        parcel2.writeNoException();
                        parcel2.writeInt(isInviteRoomSystemSupported ? 1 : 0);
                        return true;
                    case 26:
                        parcel.enforceInterface(str);
                        int readInt = parcel.readInt();
                        long readLong = parcel.readLong();
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        onRoomCallEvent(readInt, readLong, z);
                        parcel2.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(str);
                        boolean tryRetrieveMicrophone = tryRetrieveMicrophone();
                        parcel2.writeNoException();
                        parcel2.writeInt(tryRetrieveMicrophone ? 1 : 0);
                        return true;
                    case 28:
                        parcel.enforceInterface(str);
                        onSipCallEvent(parcel.readInt(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(str);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        onSipStatusEvent(z);
                        parcel2.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(str);
                        boolean onAlertWhenAvailable = onAlertWhenAvailable(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt() != 0, parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(onAlertWhenAvailable ? 1 : 0);
                        return true;
                    case 31:
                        parcel.enforceInterface(str);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        dispacthPtLoginResultEvent(z, parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(str);
                        onNewIncomingCallCanceled(parcel.readLong());
                        parcel2.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(str);
                        boolean notifyPTStartLogin = notifyPTStartLogin(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(notifyPTStartLogin ? 1 : 0);
                        return true;
                    case 34:
                        parcel.enforceInterface(str);
                        boolean disableConfAudio = disableConfAudio();
                        parcel2.writeNoException();
                        parcel2.writeInt(disableConfAudio ? 1 : 0);
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString(str);
                return true;
            }
        }

        public static boolean setDefaultImpl(IConfService iConfService) {
            if (Proxy.sDefaultImpl != null || iConfService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iConfService;
            return true;
        }

        public static IConfService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean disableConfAudio() throws RemoteException;

    void dispacthPtLoginResultEvent(boolean z, String str, String str2) throws RemoteException;

    int getCallMeStatus() throws RemoteException;

    boolean hangUpCallOut() throws RemoteException;

    boolean isCallOutInProgress() throws RemoteException;

    boolean isCallOutSupported() throws RemoteException;

    boolean isConfAppAtFront() throws RemoteException;

    boolean isCurrentMeetingHost() throws RemoteException;

    boolean isCurrentMeetingLocked() throws RemoteException;

    boolean isInviteRoomSystemSupported() throws RemoteException;

    void leaveCurrentMeeting(boolean z) throws RemoteException;

    boolean notifyPTStartLogin(String str) throws RemoteException;

    boolean onAlertWhenAvailable(String str, String str2, String str3, boolean z, String str4) throws RemoteException;

    void onNewIncomingCallCanceled(long j) throws RemoteException;

    void onPTUIMoveToBackground() throws RemoteException;

    void onPTUIMoveToFront(String str) throws RemoteException;

    void onRoomCallEvent(int i, long j, boolean z) throws RemoteException;

    void onSipCallEvent(int i, String str) throws RemoteException;

    void onSipStatusEvent(boolean z) throws RemoteException;

    void pauseCurrentMeeting() throws RemoteException;

    void resumeCurrentMeeting() throws RemoteException;

    void sendMessage(byte[] bArr) throws RemoteException;

    void sinkDataNetworkStatusChanged(boolean z) throws RemoteException;

    void sinkIMBuddyPic(byte[] bArr) throws RemoteException;

    void sinkIMBuddyPresence(byte[] bArr) throws RemoteException;

    void sinkIMBuddySort() throws RemoteException;

    void sinkIMLocalStatusChanged(int i) throws RemoteException;

    void sinkIMReceived(byte[] bArr) throws RemoteException;

    void sinkPTAppCustomEvent(int i, long j) throws RemoteException;

    void sinkPTAppEvent(int i, long j) throws RemoteException;

    void sinkPTCommonEvent(int i, byte[] bArr) throws RemoteException;

    void sinkPTPresentToRoomEvent(int i) throws RemoteException;

    boolean startCallOut(String str) throws RemoteException;

    boolean tryRetrieveMicrophone() throws RemoteException;
}
